package es.mecd.demo.miproyecto.alumno;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/** API REST acumulativa de alumnos con persistencia JPA. */
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<AlumnoResponseDTO> resultado = service.listar(
                curso, dni, sort, fechaDesde, fechaHasta, page, size);
        return paginaComoMapa(resultado);
    }

    @GetMapping("/paginado")
    public Page<AlumnoResponseDTO> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort) {
        return service.listarPaginado(page, size, sort);
    }

    private Map<String, Object> paginaComoMapa(Page<AlumnoResponseDTO> pagina) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", pagina.getContent());
        response.put("page", pagina.getNumber());
        response.put("size", pagina.getSize());
        response.put("totalElements", pagina.getTotalElements());
        response.put("totalPages", pagina.getTotalPages());
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> consultar(@PathVariable Long id) {
        return service.consultar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlumnoResponseDTO> crear(
            @Valid @RequestBody AlumnoRequestDTO request) {
        AlumnoResponseDTO creado = service.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getIdentificador())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PostMapping("/con-timeout")
    public ResponseEntity<AlumnoResponseDTO> crearConTimeout(
            @Valid @RequestBody AlumnoRequestDTO request) {
        return ResponseEntity.ok(service.crearConTimeout(request));
    }

    @PostMapping("/{id}/foto")
    public ResponseEntity<Void> subirFoto(
            @PathVariable Long id,
            @RequestParam("fichero") MultipartFile fichero) throws IOException {
        return service.subirFoto(id, fichero.getBytes())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlumnoRequestDTO request) {
        return service.actualizar(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AlumnoResponseDTO> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> cambios) {
        return service.actualizarParcial(id, cambios)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/curso/{curso}/estado")
    public Map<String, Object> actualizarEstado(
            @PathVariable String curso,
            @RequestParam EstadoAlumno estado) {
        int actualizados = service.actualizarEstadoPorCurso(curso, estado);
        return Map.of(
                "actualizados", actualizados,
                "curso", curso,
                "estado", estado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}/cascada")
    public ResponseEntity<Void> eliminarCascada(@PathVariable Long id) {
        return service.eliminarEnCascada(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/promocionar")
    public ResponseEntity<Void> promocionar() {
        service.promocionar();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<AlumnoResponseDTO> buscar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) EstadoAlumno estado,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate nacidoDespues,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        if (curso != null && fecha != null && estado == null
                && nacidoDespues == null) {
            return service.buscarPorCursoYNacidosDespues(curso, fecha);
        }
        LocalDate limite = nacidoDespues != null ? nacidoDespues : fecha;
        return service.buscarConFiltros(curso, estado, limite);
    }

    @GetMapping("/por-curso")
    public List<AlumnoResponseDTO> buscarPorCurso(
            @RequestParam String nombre) {
        return service.buscarPorNombreCurso(nombre);
    }

    @GetMapping("/por-anio")
    public List<AlumnoResponseDTO> buscarPorAnio(@RequestParam int anio) {
        return service.buscarPorAnioNacimiento(anio);
    }

    @GetMapping("/estadisticas/por-curso")
    public Map<String, Long> contarPorCurso() {
        return service.contarPorCurso();
    }

    @GetMapping("/resumen")
    public List<AlumnoResumenDTO> resumenPorCurso(
            @RequestParam String curso) {
        return service.buscarResumenPorCurso(curso);
    }

    @GetMapping("/info-peticion")
    public Map<String, String> infoPeticion(
            @RequestHeader(value = "User-Agent", required = false)
            String userAgent,
            @RequestHeader(value = "Accept-Language", required = false)
            String idioma,
            @RequestHeader(value = "X-Centro", required = false)
            String centro) {
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
    public Map<String, Object> porCursos(
            @RequestParam(name = "curso") List<String> cursos) {
        return Map.of("cursos", cursos, "cantidad", cursos.size());
    }

    @GetMapping("/request-info")
    public Map<String, Object> requestInfo(
            HttpServletRequest request,
            @CookieValue(value = "sesion", required = false) String sesion) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("method", request.getMethod());
        response.put("path", request.getRequestURI());
        response.put(
                "query",
                request.getQueryString() == null ? "" : request.getQueryString());
        response.put("userAgent", String.valueOf(request.getHeader("User-Agent")));
        response.put("sesion", sesion == null ? "sin-sesion" : sesion);
        return response;
    }
}
