package es.mecd.demo.miproyecto.expediente;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/** Expone la API REST de expedientes. */
@RestController
@RequestMapping("/api/v1/expedientes")
public class ExpedienteController {
    private final ExpedienteService service;
    public ExpedienteController(ExpedienteService service) { this.service = service; }
    @GetMapping public List<ExpedienteDTO> listar() { return service.listar(); }
    @Operation(summary = "Consulta un expediente por identificador")
    @GetMapping("/{id}")
    public ResponseEntity<ExpedienteDTO> consultar(@PathVariable String id) {
        return service.consultar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<ExpedienteDTO> crear(@RequestBody ExpedienteDTO dto) {
        ExpedienteDTO creado = service.crear(dto);
        URI location = URI.create("/api/v1/expedientes/" + creado.getIdentificador());
        return ResponseEntity.created(location).body(creado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ExpedienteDTO> actualizar(@PathVariable String id, @RequestBody ExpedienteDTO dto) {
        return service.actualizar(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/ejemplo") public ExpedienteDTO ejemplo() { return service.ejemplo(); }
    @PostMapping("/eco") public ExpedienteDTO eco(@RequestBody ExpedienteDTO dto) { return service.eco(dto); }
}
