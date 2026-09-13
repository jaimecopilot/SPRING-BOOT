package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.exception.NegocioException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Aplica las reglas y operaciones de aplicación del recurso Alumno. */
@Service
public class AlumnoService {

    private final AlumnoRepository repositorio;

    /** @param repositorio repositorio de alumnos */
    public AlumnoService(AlumnoRepository repositorio) { this.repositorio = repositorio; }

    /** Lista alumnos con filtro, orden y paginación. */
    public List<AlumnoDTO> listar(String curso, String sort, int page, int size) {
        var stream = repositorio.listarTodos().stream();
        if (curso != null && !curso.isBlank()) {
            stream = stream.filter(a -> a.getCurso().equalsIgnoreCase(curso));
        }
        if ("nombre".equalsIgnoreCase(sort)) {
            stream = stream.sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        } else if ("apellidos".equalsIgnoreCase(sort)) {
            stream = stream.sorted((a, b) -> a.getApellidos().compareToIgnoreCase(b.getApellidos()));
        }
        return stream.skip((long) page * size).limit(size).toList();
    }

    /** Consulta un alumno. */
    public Optional<AlumnoDTO> consultar(String id) { return repositorio.buscarPorId(id); }

    /** Crea un alumno validando DNI único. */
    public AlumnoDTO crear(AlumnoDTO dto) {
        if (repositorio.existePorDni(dto.getDni())) {
            throw new NegocioException("Ya existe un alumno con el DNI " + dto.getDni());
        }
        if (dto.getIdentificador() == null || dto.getIdentificador().isBlank()) {
            dto.setIdentificador(repositorio.siguienteIdentificador());
        }
        return repositorio.guardar(dto);
    }

    /** Actualiza completamente un alumno. */
    public Optional<AlumnoDTO> actualizar(String id, AlumnoDTO dto) {
        return repositorio.buscarPorId(id).map(existente -> {
            dto.setIdentificador(id);
            return repositorio.guardar(dto);
        });
    }

    /** Actualiza parcialmente campos admitidos. */
    public Optional<AlumnoDTO> actualizarParcial(String id, Map<String, Object> cambios) {
        return repositorio.buscarPorId(id).map(alumno -> {
            if (cambios.containsKey("nombre")) alumno.setNombre((String) cambios.get("nombre"));
            if (cambios.containsKey("apellidos")) alumno.setApellidos((String) cambios.get("apellidos"));
            if (cambios.containsKey("dni")) alumno.setDni((String) cambios.get("dni"));
            if (cambios.containsKey("curso")) alumno.setCurso((String) cambios.get("curso"));
            return repositorio.guardar(alumno);
        });
    }

    /** Elimina por ID. */
    public boolean eliminar(String id) { return repositorio.eliminar(id); }

    /** Promociona todos los cursos del laboratorio. */
    public void promocionar() {
        repositorio.listarTodos().forEach(alumno -> {
            alumno.setCurso(alumno.getCurso() + " (promocionado)");
            repositorio.guardar(alumno);
        });
    }
}
