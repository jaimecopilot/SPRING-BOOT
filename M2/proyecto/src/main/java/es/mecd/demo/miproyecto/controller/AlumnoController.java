package es.mecd.demo.miproyecto.controller;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import es.mecd.demo.miproyecto.exception.NegocioException;
import es.mecd.demo.miproyecto.service.AlumnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlumnoDTO> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.listar(curso, sort, page, size);
    }

    @PostMapping
    public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
        AlumnoDTO creado = service.crear(dto);
        URI location = URI.create(
                "/api/v1/alumnos/" + creado.getIdentificador());
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping("/info-peticion")
    public Map<String, String> infoPeticion(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "Accept-Language", required = false) String idioma) {

        return Map.of(
                "userAgent", userAgent != null ? userAgent : "desconocido",
                "idioma", idioma != null ? idioma : "desconocido"
        );
    }

    @PostMapping("/promocionar")
    public ResponseEntity<Void> promocionar() {
        service.promocionar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
        return service.consultar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/con-enlaces")
    public ResponseEntity<Map<String, Object>> consultarConEnlaces(
            @PathVariable String id) {

        return service.consultar(id)
                .map(alumno -> {
                    Map<String, Object> respuesta = new LinkedHashMap<>();
                    respuesta.put("alumno", alumno);
                    respuesta.put("_links", Map.of(
                            "self", "/api/v1/alumnos/" + id,
                            "documentos", "/api/v1/alumnos/" + id + "/documentos",
                            "curso", "/api/v1/cursos/" + alumno.getCurso()
                    ));
                    return ResponseEntity.ok(respuesta);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDTO> actualizar(
            @PathVariable String id,
            @RequestBody AlumnoDTO dto) {

        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlumnoDTO> actualizarParcial(
            @PathVariable String id,
            @RequestBody Map<String, Object> cambios) {

        return service.actualizarParcial(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Map<String, Object>> handleNegocio(
            NegocioException ex) {

        Map<String, Object> error = Map.of(
                "status", 409,
                "error", "Conflict",
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
