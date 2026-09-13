package es.mecd.demo.miproyecto.service;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import es.mecd.demo.miproyecto.exception.NegocioException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AlumnoService {

    private final List<AlumnoDTO> alumnos = new ArrayList<>(List.of(
            new AlumnoDTO(
                    "1", "Ana", "García López", "DNI-DEMO-01",
                    LocalDate.of(2010, 5, 12), "5º Primaria"),
            new AlumnoDTO(
                    "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                    LocalDate.of(2009, 9, 3), "6º Primaria")
    ));

    public List<AlumnoDTO> listar(
            String curso,
            String sort,
            int page,
            int size) {

        var stream = alumnos.stream();

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
                .toList();
    }

    public Optional<AlumnoDTO> consultar(String id) {
        return alumnos.stream()
                .filter(a -> a.getIdentificador().equals(id))
                .findFirst();
    }

    public AlumnoDTO crear(AlumnoDTO dto) {
        if (existePorDni(dto.getDni())) {
            throw new NegocioException(
                    "Ya existe un alumno con el DNI " + dto.getDni());
        }

        int siguienteId = alumnos.stream()
                .map(AlumnoDTO::getIdentificador)
                .filter(id -> id != null && id.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        dto.setIdentificador(String.valueOf(siguienteId));
        alumnos.add(dto);
        return dto;
    }

    public Optional<AlumnoDTO> actualizar(String id, AlumnoDTO dto) {
        return consultar(id).map(existente -> {
            dto.setIdentificador(id);
            alumnos.set(alumnos.indexOf(existente), dto);
            return dto;
        });
    }

    public Optional<AlumnoDTO> actualizarParcial(
            String id,
            Map<String, Object> cambios) {

        return consultar(id).map(alumno -> {
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
            return alumno;
        });
    }

    public boolean eliminar(String id) {
        return alumnos.removeIf(
                a -> a.getIdentificador().equals(id));
    }

    public void promocionar() {
        alumnos.forEach(
                a -> a.setCurso(a.getCurso() + " (promocionado)"));
    }

    private boolean existePorDni(String dni) {
        return alumnos.stream()
                .anyMatch(a -> Objects.equals(a.getDni(), dni));
    }
}
