package es.mecd.demo.miproyecto.alumno;

import org.springframework.security.access.prepost.PreAuthorize;
import es.mecd.demo.miproyecto.solicitud.SolicitudResponseDTO;
import es.mecd.demo.miproyecto.solicitud.SolicitudService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
    private final AlumnoService alumnoService;
    private final SolicitudService solicitudService;

    public AlumnoController(AlumnoService alumnoService, SolicitudService solicitudService) {
        this.alumnoService = alumnoService;
        this.solicitudService = solicitudService;
    }

    @GetMapping
    public Page<AlumnoResponseDTO> listar(
            @RequestParam(required = false) String curso,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return alumnoService.listar(curso, pageable);
    }

    @GetMapping("/{id}")
    public AlumnoResponseDTO consultar(@PathVariable Long id) {
        return alumnoService.consultar(id);
    }

    @PostMapping
@PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<AlumnoResponseDTO> crear(@Valid @RequestBody AlumnoRequestDTO request) {
        AlumnoResponseDTO creado = alumnoService.crear(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(creado.getIdentificador()).toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
@PreAuthorize("hasRole('CIUDADANO')")
    public AlumnoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody AlumnoRequestDTO request) {
        return alumnoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{alumnoId}/solicitudes")
    public Page<SolicitudResponseDTO> listarSolicitudes(
            @PathVariable Long alumnoId,
            @PageableDefault(size = 20) Pageable pageable) {
        return solicitudService.listarPorAlumno(alumnoId, pageable);
    }
}
