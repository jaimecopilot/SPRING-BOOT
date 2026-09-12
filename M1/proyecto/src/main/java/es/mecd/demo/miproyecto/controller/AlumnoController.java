package es.mecd.demo.miproyecto.controller;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final List<AlumnoDTO> alumnos = List.of(
            new AlumnoDTO(
                    "1", "Ana", "García López", "DNI-DEMO-01",
                    LocalDate.of(2010, 5, 12), "5º Primaria"),
            new AlumnoDTO(
                    "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                    LocalDate.of(2009, 9, 3), "6º Primaria")
    );

    @GetMapping
    public List<AlumnoDTO> listar(@RequestParam(required = false) String curso) {
        if (curso == null || curso.isBlank()) {
            return alumnos;
        }

        return alumnos.stream()
                .filter(a -> a.getCurso().equalsIgnoreCase(curso))
                .toList();
    }

    @PostMapping
    public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
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
}
