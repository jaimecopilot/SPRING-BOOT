package es.mecd.demo.miproyecto.solicitud;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/solicitudes")
public class SolicitudController {

       private final SolicitudService solicitudService;
public SolicitudController(SolicitudService solicitudService) {
     this.solicitudService = solicitudService;
}

@Operation(summary = "Listar solicitudes")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Lista de solicitudes devuelta")
})
@GetMapping
public Page<SolicitudResponseDTO> listar(
         @RequestParam(required = false) EstadoSolicitud estado,
         @RequestParam(required = false) Long alumnoId,
         @RequestParam(required = false) Long becaId,
         @PageableDefault(size = 20, sort = "fechaSolicitud",
                          direction = Sort.Direction.DESC) Pageable pageable) {
     return solicitudService.listar(estado, alumnoId, becaId, pageable);
}

@Operation(summary = "Consultar solicitud por ID")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
         @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
})
@GetMapping("/{id}")
public ResponseEntity<SolicitudResponseDTO> consultar(@PathVariable Long id) {
     return ResponseEntity.ok(solicitudService.consultar(id));
}

@Operation(summary = "Crear solicitud")
@ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Solicitud creada"),
         @ApiResponse(responseCode = "400", description = "Datos inválidos"),
         @ApiResponse(responseCode = "404", description = "Alumno o beca no encontrados"),
         @ApiResponse(responseCode = "409", description = "Ya existe una solicitud para ese alumno y beca este año")
})
@PostMapping
@PreAuthorize("hasRole('CIUDADANO')")
public ResponseEntity<SolicitudResponseDTO> crear(
         @Valid @RequestBody SolicitudRequestDTO request) {
     SolicitudResponseDTO creada = solicitudService.crear(request);
     URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(creada.getIdentificador())
               .toUri();
     return ResponseEntity.created(location).body(creada);
}

@Operation(summary = "Cambiar estado de una solicitud")
@ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Estado cambiado"),
             @ApiResponse(responseCode = "400", description = "Estado o importe inválidos"),
             @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
             @ApiResponse(responseCode = "409", description = "La solicitud no permite cambio de estado")
    })
    @PatchMapping("/{id}/estado")
@PreAuthorize("hasAnyRole('GESTOR', 'ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> cambiarEstado(
             @PathVariable Long id,
             @Valid @RequestBody CambioEstadoRequestDTO request) {
         return ResponseEntity.ok(solicitudService.cambiarEstado(id, request));
    }

    @Operation(summary = "Cancelar solicitud")
    @ApiResponses(value = {
             @ApiResponse(responseCode = "204", description = "Solicitud cancelada"),
             @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
             @ApiResponse(responseCode = "409", description = "La solicitud no se puede cancelar")
    })
    @DeleteMapping("/{id}")
@PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
         solicitudService.cancelar(id);
         return ResponseEntity.noContent().build();
    }
}
