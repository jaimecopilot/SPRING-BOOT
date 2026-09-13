package es.mecd.demo.miproyecto.controller;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final List<AlumnoDTO> alumnos = new ArrayList<>(List.of(
            new AlumnoDTO(
                    "1", "Ana", "García López", "DNI-DEMO-01",
                    LocalDate.of(2010, 5, 12), "5º Primaria"),
            new AlumnoDTO(
                    "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                    LocalDate.of(2009, 9, 3), "6º Primaria")
    ));

    @GetMapping
    public List<AlumnoDTO> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String sort) {

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

        return stream.toList();
    }

    @PostMapping
    public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
        int siguienteId = alumnos.stream()
                .map(AlumnoDTO::getIdentificador)
                .filter(id -> id != null && id.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        dto.setIdentificador(String.valueOf(siguienteId));
        alumnos.add(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
        Optional<AlumnoDTO> encontrado = alumnos.stream()
                .filter(a -> a.getIdentificador().equals(id))
                .findFirst();

        return encontrado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDTO> actualizar(
            @PathVariable String id,
            @RequestBody AlumnoDTO dto) {

        for (int i = 0; i < alumnos.size(); i++) {
            if (alumnos.get(i).getIdentificador().equals(id)) {
                dto.setIdentificador(id);
                alumnos.set(i, dto);
                return ResponseEntity.ok(dto);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlumnoDTO> actualizarParcial(
            @PathVariable String id,
            @RequestBody Map<String, Object> cambios) {

        for (AlumnoDTO alumno : alumnos) {
            if (alumno.getIdentificador().equals(id)) {
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
                return ResponseEntity.ok(alumno);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean eliminado = alumnos.removeIf(
                a -> a.getIdentificador().equals(id));

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
