package es.mecd.demo.miproyecto.expediente;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;
import java.util.Map;

/** API REST de expedientes. */
@RestController
@RequestMapping("/api/v1/expedientes")
public class ExpedienteController {
    private final ExpedienteService service;

    public ExpedienteController(ExpedienteService service) { this.service = service; }

    @GetMapping
    public List<ExpedienteDTO> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public ResponseEntity<ExpedienteDTO> consultar(@PathVariable String id) {
        return service.consultar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ExpedienteDTO> crear(@Valid @RequestBody ExpedienteRequestDTO request) {
        ExpedienteDTO expediente = service.crear(request);
        URI location = URI.create("/api/v1/expedientes/" + expediente.getIdentificador());
        return ResponseEntity.created(location).body(expediente);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ExpedienteDTO> actualizarEstado(
            @PathVariable String id,
            @RequestBody Map<String, String> body) {
        String estado = body.get("estado");
        if (estado == null || estado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return service.cambiarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ejemplo")
    public ExpedienteDTO ejemplo() { return service.ejemplo(); }

    @PostMapping("/eco")
    public ExpedienteDTO eco(@RequestBody ExpedienteDTO expediente) { return service.eco(expediente); }
}
