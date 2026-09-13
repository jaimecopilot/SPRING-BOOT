package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.auditoria.AuditoriaService;
import es.mecd.demo.miproyecto.common.exception.NegocioException;
import es.mecd.demo.miproyecto.curso.Curso;
import es.mecd.demo.miproyecto.curso.CursoRepository;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Servicio transaccional de alumnos sobre JPA. */
@Service
public class AlumnoService {
    private final AlumnoRepository repositorio;
    private final CursoRepository cursos;
    private final DocumentoRepository documentos;
    private final AuditoriaService auditoria;

    public AlumnoService(
            AlumnoRepository repositorio,
            CursoRepository cursos,
            DocumentoRepository documentos,
            AuditoriaService auditoria) {
        this.repositorio = repositorio;
        this.cursos = cursos;
        this.documentos = documentos;
        this.auditoria = auditoria;
    }

    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> listar(
            String curso,
            String dni,
            String sort,
            LocalDate desde,
            LocalDate hasta,
            int page,
            int size) {
        if (page < 0 || size < 1 || size > 100) {
            throw new IllegalArgumentException("Paginación inválida");
        }
        Sort orden = switch (sort == null ? "" : sort.toLowerCase()) {
            case "nombre" -> Sort.by("nombre");
            case "apellidos" -> Sort.by("apellidos");
            default -> Sort.by("id");
        };
        Specification<Alumno> spec = Specification.allOf(
                AlumnoSpecifications.activos(),
                AlumnoSpecifications.curso(curso),
                AlumnoSpecifications.dni(dni),
                AlumnoSpecifications.nacidosEntre(desde, hasta));
        return repositorio
                .findAll(spec, PageRequest.of(page, size, orden))
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> listarPaginado(
            int page,
            int size,
            String sort) {
        return listar(null, null, sort, null, null, page, size);
    }

    @Transactional(readOnly = true)
    public Optional<AlumnoResponseDTO> consultar(Long id) {
        return repositorio.buscarDetalle(id).map(this::toResponse);
    }

    @Transactional
    public AlumnoResponseDTO crear(AlumnoRequestDTO request) {
        if (repositorio.existsByDni(request.getDni())) {
            throw new NegocioException(
                    "Ya existe un alumno con el DNI " + request.getDni());
        }
        Curso curso = obtenerOCrearCurso(request.getCurso());
        Alumno alumno = new Alumno(
                request.getNombre(),
                request.getApellidos(),
                request.getDni(),
                request.getFechaNacimiento(),
                curso);
        aplicarCamposOpcionales(alumno, request);
        Alumno guardado = repositorio.save(alumno);
        auditoria.registrar(
                "CREAR_ALUMNO",
                "DNI: " + request.getDni());
        return toResponse(guardado);
    }

    @Transactional(timeout = 5)
    public AlumnoResponseDTO crearConTimeout(AlumnoRequestDTO request) {
        return crear(request);
    }

    @Transactional
    public Optional<AlumnoResponseDTO> actualizar(
            Long id,
            AlumnoRequestDTO request) {
        if (repositorio.existsByDniAndIdNot(request.getDni(), id)) {
            throw new NegocioException("El DNI pertenece a otro alumno");
        }
        return repositorio.findById(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> {
                    alumno.setNombre(request.getNombre());
                    alumno.setApellidos(request.getApellidos());
                    alumno.setDni(request.getDni());
                    alumno.setFechaNacimiento(request.getFechaNacimiento());
                    alumno.setCurso(obtenerOCrearCurso(request.getCurso()));
                    aplicarCamposOpcionales(alumno, request);
                    return toResponse(alumno);
                });
    }

    @Transactional
    public Optional<AlumnoResponseDTO> actualizarParcial(
            Long id,
            Map<String, Object> cambios) {
        return repositorio.findById(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> aplicarCambios(alumno, id, cambios));
    }

    private AlumnoResponseDTO aplicarCambios(
            Alumno alumno,
            Long id,
            Map<String, Object> cambios) {
        if (cambios.containsKey("nombre")) {
            alumno.setNombre(String.valueOf(cambios.get("nombre")));
        }
        if (cambios.containsKey("apellidos")) {
            alumno.setApellidos(String.valueOf(cambios.get("apellidos")));
        }
        if (cambios.containsKey("curso")) {
            alumno.setCurso(
                    obtenerOCrearCurso(String.valueOf(cambios.get("curso"))));
        }
        if (cambios.containsKey("dni")) {
            String dni = String.valueOf(cambios.get("dni"));
            if (repositorio.existsByDniAndIdNot(dni, id)) {
                throw new NegocioException("El DNI pertenece a otro alumno");
            }
            alumno.setDni(dni);
        }
        if (cambios.containsKey("fechaNacimiento")) {
            alumno.setFechaNacimiento(
                    LocalDate.parse(
                            String.valueOf(cambios.get("fechaNacimiento"))));
        }
        return toResponse(alumno);
    }

    @Transactional
    public boolean subirFoto(Long id, byte[] foto) {
        return repositorio.findById(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> {
                    alumno.setFoto(foto);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (documentos.existePorAlumnoId(id.toString())) {
            throw new NegocioException(
                    "No se puede eliminar: tiene documentos asociados");
        }
        return repositorio.findById(id)
                .filter(alumno -> !alumno.isEliminado())
                .map(alumno -> {
                    alumno.setEliminado(true);
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public boolean eliminarEnCascada(Long id) {
        documentos.eliminarPorAlumnoId(id.toString());
        return eliminar(id);
    }

    @Transactional
    public void promocionar() {
        repositorio.findAll().stream()
                .filter(alumno -> !alumno.isEliminado())
                .forEach(alumno -> alumno.setEstado(EstadoAlumno.ACTIVO));
    }

    @Transactional
    public int actualizarEstadoPorCurso(
            String curso,
            EstadoAlumno estado) {
        return repositorio.actualizarEstadoPorCurso(curso, estado);
    }

    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarPorCursoYNacidosDespues(
            String curso,
            LocalDate fecha) {
        return repositorio
                .buscarPorCursoYNacidosDespues(curso, fecha)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarConFiltros(
            String curso,
            EstadoAlumno estado,
            LocalDate nacidoDespues) {
        Specification<Alumno> spec = Specification.allOf(
                AlumnoSpecifications.activos(),
                AlumnoSpecifications.curso(curso),
                AlumnoSpecifications.estado(estado),
                AlumnoSpecifications.nacidoDespues(nacidoDespues));
        return repositorio.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> listarPorCurso(Long cursoId) {
        return repositorio.findByCursoIdAndEliminadoFalse(cursoId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarPorNombreCurso(String nombreCurso) {
        return repositorio.buscarPorNombreCursoConCurso(nombreCurso).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AlumnoResponseDTO> buscarPorAnioNacimiento(int anio) {
        return repositorio.buscarPorAnioNacimiento(anio).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contarPorCurso() {
        Map<String, Long> resultado = new LinkedHashMap<>();
        repositorio.contarPorCurso().forEach(fila ->
                resultado.put((String) fila[0], (Long) fila[1]));
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<AlumnoResumenDTO> buscarResumenPorCurso(String curso) {
        return repositorio.buscarResumenPorCurso(curso);
    }

    private void aplicarCamposOpcionales(
            Alumno alumno,
            AlumnoRequestDTO request) {
        if (request.getEstado() != null) {
            alumno.setEstado(request.getEstado());
        }
        if (request.getDireccion() != null) {
            Direccion direccion = new Direccion();
            direccion.setCalle(request.getDireccion().getCalle());
            direccion.setNumero(request.getDireccion().getNumero());
            direccion.setCiudad(request.getDireccion().getCiudad());
            direccion.setCodigoPostal(
                    request.getDireccion().getCodigoPostal());
            alumno.setDireccion(direccion);
        }
        alumno.setObservaciones(request.getObservaciones());
    }

    private Curso obtenerOCrearCurso(String nombre) {
        return cursos.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> cursos.save(new Curso(nombre)));
    }

    private AlumnoResponseDTO toResponse(Alumno alumno) {
        AlumnoResponseDTO response = new AlumnoResponseDTO();
        response.setIdentificador(alumno.getId().toString());
        response.setNombre(alumno.getNombre());
        response.setApellidos(alumno.getApellidos());
        response.setDni(alumno.getDni());
        response.setFechaNacimiento(alumno.getFechaNacimiento());
        response.setCurso(alumno.getCurso().getNombre());
        response.setActivo(!alumno.isEliminado());
        response.setEstado(
                alumno.getEstado() == null ? null : alumno.getEstado().name());
        response.setEdad(alumno.getEdad());
        response.setObservaciones(alumno.getObservaciones());
        if (alumno.getDireccion() != null) {
            DireccionDTO direccion = new DireccionDTO();
            direccion.setCalle(alumno.getDireccion().getCalle());
            direccion.setNumero(alumno.getDireccion().getNumero());
            direccion.setCiudad(alumno.getDireccion().getCiudad());
            direccion.setCodigoPostal(
                    alumno.getDireccion().getCodigoPostal());
            response.setDireccion(direccion);
        }
        return response;
    }
}
