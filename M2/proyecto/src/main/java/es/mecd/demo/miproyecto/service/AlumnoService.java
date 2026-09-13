package es.mecd.demo.miproyecto.service;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import es.mecd.demo.miproyecto.exception.NegocioException;
import es.mecd.demo.miproyecto.repository.AlumnoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AlumnoService {

    private final AlumnoRepository repositorio;

    public AlumnoService(AlumnoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<AlumnoDTO> listar(
            String curso,
            String sort,
            int page,
            int size) {

        var stream = repositorio.listarTodos().stream();

        if (curso != null && !curso.isBlank()) {
            stream = stream.filter(
                    a -> a.getCurso().equalsIgnoreCase(curso));
        }

        if ("nombre".equalsIgnoreCase(sort)) {
            stream = stream.sorted(
                    (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        } else if ("apellidos".equalsIgnoreCase(sort)) {
            stream = stream.sorted(
                    (a, b) -> a.getApellidos().compareToIgnoreCase(b.getApellidos()));
        }

        return stream
                .skip((long) page * size)
                .limit(size)
                .map(this::toDTO)
                .toList();
    }

    public Optional<AlumnoDTO> consultar(String id) {
        return repositorio.buscarPorId(id)
                .map(this::toDTO);
    }

    public AlumnoDTO crear(AlumnoDTO dto) {
        if (repositorio.existePorDni(dto.getDni())) {
            throw new NegocioException(
                    "Ya existe un alumno con el DNI " + dto.getDni());
        }

        dto.setIdentificador(repositorio.siguienteIdentificador());
        return toDTO(repositorio.guardar(toEntity(dto)));
    }

    public Optional<AlumnoDTO> actualizar(String id, AlumnoDTO dto) {
        return repositorio.buscarPorId(id).map(existente -> {
            dto.setIdentificador(id);
            return toDTO(repositorio.guardar(toEntity(dto)));
        });
    }

    public Optional<AlumnoDTO> actualizarParcial(
            String id,
            Map<String, Object> cambios) {

        return repositorio.buscarPorId(id).map(alumno -> {
            if (cambios.containsKey("nombre")) {
                alumno.setNombre((String) cambios.get("nombre"));
            }
            if (cambios.containsKey("apellidos")) {
                alumno.setApellidos((String) cambios.get("apellidos"));
            }
            if (cambios.containsKey("dni")) {
                alumno.setDni((String) cambios.get("dni"));
            }
            if (cambios.containsKey("curso")) {
                alumno.setCurso((String) cambios.get("curso"));
            }
            return toDTO(repositorio.guardar(toEntity(alumno)));
        });
    }

    public boolean eliminar(String id) {
        return repositorio.eliminar(id);
    }

    public void promocionar() {
        repositorio.listarTodos().forEach(alumno -> {
            alumno.setCurso(alumno.getCurso() + " (promocionado)");
            repositorio.guardar(toEntity(alumno));
        });
    }

    private AlumnoDTO toDTO(AlumnoDTO entidad) {
        return entidad;
    }

    private AlumnoDTO toEntity(AlumnoDTO dto) {
        return dto;
    }
}
