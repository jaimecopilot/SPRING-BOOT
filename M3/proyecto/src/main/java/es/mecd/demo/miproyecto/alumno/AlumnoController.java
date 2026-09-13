package es.mecd.demo.miproyecto.alumno;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** API REST de alumnos. */
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
    private final AlumnoService service;

    public AlumnoController(AlumnoService service) { this.service = service; }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<AlumnoResponseDTO> content = service.listar(
                curso, dni, sort, fechaDesde, fechaHasta, page, size);
        long total = service.contar(curso, dni, fechaDesde, fechaHasta);
        int totalPages = (int) Math.ceil((double) total / size);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", content);
        response.put("page", page);
        response.put("size", size);
        response.put("totalElements", total);
        response.put("totalPages", totalPages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> consultar(@PathVariable String id) {
        return service.consultar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlumnoResponseDTO> crear(@Valid @RequestBody AlumnoRequestDTO request) {
        AlumnoResponseDTO creado = service.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getIdentificador())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> actualizar(
            @PathVariable String id,
            @Valid @RequestBody AlumnoRequestDTO request) {
        return service.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> actualizarParcial(
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

    @DeleteMapping("/{id}/cascada")
    public ResponseEntity<Void> eliminarCascada(@PathVariable String id) {
        return service.eliminarEnCascada(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/promocionar")
    public ResponseEntity<Void> promocionar() {
        service.promocionar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info-peticion")
    public Map<String, String> infoPeticion(
            @RequestHeader(value = "User-Agent", required = false) String userAgent,
            @RequestHeader(value = "Accept-Language", required = false) String idioma,
            @RequestHeader(value = "X-Centro", required = false) String centro) {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("userAgent", userAgent == null ? "desconocido" : userAgent);
        response.put("idioma", idioma == null ? "desconocido" : idioma);
        response.put("centro", centro == null ? "desconocido" : centro);
        return response;
    }

    @GetMapping("/cookie")
    public Map<String, String> cookie(
            @CookieValue(value = "sesion", required = false) String sesion) {
        return Map.of("sesion", sesion == null ? "sin-sesion" : sesion);
    }

    @GetMapping("/por-cursos")
    public Map<String, Object> porCursos(@RequestParam(name = "curso") List<String> cursos) {
        return Map.of("cursos", cursos, "cantidad", cursos.size());
    }

    @GetMapping("/request-info")
    public Map<String, Object> requestInfo(
            HttpServletRequest request,
            @CookieValue(value = "sesion", required = false) String sesion) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("method", request.getMethod());
        response.put("path", request.getRequestURI());
        response.put("query", request.getQueryString() == null ? "" : request.getQueryString());
        response.put("userAgent", String.valueOf(request.getHeader("User-Agent")));
        response.put("sesion", sesion == null ? "sin-sesion" : sesion);
        return response;
    }
}
