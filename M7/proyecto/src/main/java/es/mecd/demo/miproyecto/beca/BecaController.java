package es.mecd.demo.miproyecto.beca;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/becas")
public class BecaController {

    private final BecaService becaService;

    public BecaController(BecaService becaService) {
         this.becaService = becaService;
    }

    @Operation(summary = "Listar becas")
    @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Lista de becas devuelta")
    })
    @GetMapping
    public Page<BecaResponseDTO> listar(
             @RequestParam(required = false) Boolean activa,
             @RequestParam(required = false) Integer anio,
             @PageableDefault(size = 20, sort = "id") Pageable pageable) {
         return becaService.listar(activa, anio, pageable);
    }
@Operation(summary = "Consultar beca por ID")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Beca encontrada"),
         @ApiResponse(responseCode = "404", description = "Beca no encontrada")
})
@GetMapping("/{id}")
public ResponseEntity<BecaResponseDTO> consultar(@PathVariable Long id) {
     return ResponseEntity.ok(becaService.consultar(id));
}

@Operation(summary = "Crear beca")
@ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Beca creada"),
         @ApiResponse(responseCode = "400", description = "Datos inválidos"),
         @ApiResponse(responseCode = "409", description = "Ya existe una beca con ese nombre")
})
@PostMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<BecaResponseDTO> crear(
         @Valid @RequestBody BecaRequestDTO request) {
     BecaResponseDTO creada = becaService.crear(request);
     URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(creada.getIdentificador())
               .toUri();
     return ResponseEntity.created(location).body(creada);
}

@Operation(summary = "Actualizar beca")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Beca actualizada"),
         @ApiResponse(responseCode = "404", description = "Beca no encontrada"),
         @ApiResponse(responseCode = "409", description = "Ya existe otra beca con ese nombre")
})
@PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<BecaResponseDTO> actualizar(
         @PathVariable Long id,
         @Valid @RequestBody BecaRequestDTO request) {
     return ResponseEntity.ok(becaService.actualizar(id, request));
}

@Operation(summary = "Eliminar beca")
@ApiResponses(value = {
         @ApiResponse(responseCode = "204", description = "Beca eliminada"),
         @ApiResponse(responseCode = "404", description = "Beca no encontrada"),
         @ApiResponse(responseCode = "409", description = "La beca tiene solicitudes asociadas")
})
@DeleteMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> eliminar(@PathVariable Long id) {
     becaService.eliminar(id);
         return ResponseEntity.noContent().build();
    }
}
