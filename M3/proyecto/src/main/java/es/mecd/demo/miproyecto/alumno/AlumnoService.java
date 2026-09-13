package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.exception.NegocioException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/** Reglas de aplicación del recurso alumno. */
@Service
public class AlumnoService {
    private final AlumnoRepository repositorio;
    private final DocumentoRepository documentos;

    public AlumnoService(AlumnoRepository repositorio, DocumentoRepository documentos) {
        this.repositorio = repositorio;
        this.documentos = documentos;
    }

    public List<AlumnoResponseDTO> listar(
            String curso,
            String dni,
            String sort,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            int page,
            int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Paginación inválida");
        }
        Stream<AlumnoDTO> stream = filtrar(curso, dni, fechaDesde, fechaHasta);
        Comparator<AlumnoDTO> comparador = comparador(sort);
        if (comparador != null) {
            stream = stream.sorted(comparador);
        }
        return stream.skip((long) page * size).limit(size).map(this::toResponse).toList();
    }

    public long contar(String curso, String dni, LocalDate fechaDesde, LocalDate fechaHasta) {
        return filtrar(curso, dni, fechaDesde, fechaHasta).count();
    }

    public Optional<AlumnoResponseDTO> consultar(String id) {
        return repositorio.buscarPorId(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(this::toResponse);
    }

    public AlumnoResponseDTO crear(AlumnoRequestDTO request) {
        if (repositorio.existePorDni(request.getDni())) {
            throw new NegocioException("Ya existe un alumno con el DNI " + request.getDni());
        }
        AlumnoDTO alumno = toModel(request);
        alumno.setIdentificador(repositorio.siguienteIdentificador());
        return toResponse(repositorio.guardar(alumno));
    }

    public Optional<AlumnoResponseDTO> actualizar(String id, AlumnoRequestDTO request) {
        if (repositorio.existePorDniYIdDistinto(request.getDni(), id)) {
            throw new NegocioException("El DNI pertenece a otro alumno");
        }
        return repositorio.buscarPorId(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(existente -> {
                    AlumnoDTO reemplazo = toModel(request);
                    reemplazo.setIdentificador(id);
                    return toResponse(repositorio.guardar(reemplazo));
                });
    }

    public Optional<AlumnoResponseDTO> actualizarParcial(String id, Map<String, Object> cambios) {
        return repositorio.buscarPorId(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> {
                    aplicarCambios(id, alumno, cambios);
                    return toResponse(repositorio.guardar(alumno));
                });
    }

    public boolean eliminar(String id) {
        if (documentos.existePorAlumnoId(id)) {
            throw new NegocioException(
                    "No se puede eliminar el alumno " + id + ": tiene documentos asociados");
        }
        return repositorio.buscarPorId(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> {
                    alumno.setEliminado(true);
                    repositorio.guardar(alumno);
                    return true;
                })
                .orElse(false);
    }

    public boolean eliminarEnCascada(String id) {
        if (repositorio.buscarPorId(id).filter(a -> !a.isEliminado()).isEmpty()) {
            return false;
        }
        documentos.eliminarPorAlumnoId(id);
        return eliminar(id);
    }

    public void promocionar() {
        repositorio.listarTodos().stream()
                .filter(alumno -> !alumno.isEliminado())
                .forEach(alumno -> {
                    alumno.setCurso(alumno.getCurso() + " (promocionado)");
                    repositorio.guardar(alumno);
                });
    }

    private Stream<AlumnoDTO> filtrar(
            String curso,
            String dni,
            LocalDate fechaDesde,
            LocalDate fechaHasta) {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
        return repositorio.listarTodos().stream()
                .filter(alumno -> !alumno.isEliminado())
                .filter(alumno -> curso == null || curso.isBlank()
                        || curso.equalsIgnoreCase(alumno.getCurso()))
                .filter(alumno -> dni == null || dni.isBlank()
                        || dni.equalsIgnoreCase(alumno.getDni()))
                .filter(alumno -> fechaDesde == null
                        || (alumno.getFechaNacimiento() != null
                        && !alumno.getFechaNacimiento().isBefore(fechaDesde)))
                .filter(alumno -> fechaHasta == null
                        || (alumno.getFechaNacimiento() != null
                        && !alumno.getFechaNacimiento().isAfter(fechaHasta)));
    }

    private Comparator<AlumnoDTO> comparador(String sort) {
        if (sort == null || sort.isBlank()) {
            return null;
        }
        return switch (sort.toLowerCase()) {
            case "nombre" -> Comparator.comparing(
                    AlumnoDTO::getNombre, String.CASE_INSENSITIVE_ORDER);
            case "apellidos" -> Comparator.comparing(
                    AlumnoDTO::getApellidos, String.CASE_INSENSITIVE_ORDER);
            case "curso" -> Comparator.comparing(
                    AlumnoDTO::getCurso, String.CASE_INSENSITIVE_ORDER);
            default -> null;
        };
    }

    private void aplicarCambios(String id, AlumnoDTO alumno, Map<String, Object> cambios) {
        if (cambios.containsKey("nombre")) {
            alumno.setNombre(String.valueOf(cambios.get("nombre")));
        }
        if (cambios.containsKey("apellidos")) {
            alumno.setApellidos(String.valueOf(cambios.get("apellidos")));
        }
        if (cambios.containsKey("curso")) {
            alumno.setCurso(String.valueOf(cambios.get("curso")));
        }
        if (cambios.containsKey("dni")) {
            String nuevoDni = String.valueOf(cambios.get("dni"));
            if (nuevoDni.length() != 9) {
                throw new IllegalArgumentException("DNI inválido");
            }
            if (repositorio.existePorDniYIdDistinto(nuevoDni, id)) {
                throw new NegocioException("El DNI pertenece a otro alumno");
            }
            alumno.setDni(nuevoDni);
        }
        if (cambios.containsKey("fechaNacimiento")) {
            alumno.setFechaNacimiento(
                    LocalDate.parse(String.valueOf(cambios.get("fechaNacimiento"))));
        }
    }

    private AlumnoDTO toModel(AlumnoRequestDTO request) {
        return new AlumnoDTO(
                null,
                request.getNombre(),
                request.getApellidos(),
                request.getDni(),
                request.getFechaNacimiento(),
                request.getCurso());
    }

    private AlumnoResponseDTO toResponse(AlumnoDTO alumno) {
        AlumnoResponseDTO response = new AlumnoResponseDTO();
        response.setIdentificador(alumno.getIdentificador());
        response.setNombre(alumno.getNombre());
        response.setApellidos(alumno.getApellidos());
        response.setDni(alumno.getDni());
        response.setFechaNacimiento(alumno.getFechaNacimiento());
        response.setCurso(alumno.getCurso());
        response.setActivo(!alumno.isEliminado());
        return response;
    }
}
