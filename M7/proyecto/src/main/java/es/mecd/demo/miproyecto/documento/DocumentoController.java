package es.mecd.demo.miproyecto.documento;

import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes/{solicitudId}/documentos")
public class DocumentoController {
private final DocumentoService documentoService;

public DocumentoController(DocumentoService documentoService) {
     this.documentoService = documentoService;
}

@Operation(summary = "Listar documentos de una solicitud")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Lista de documentos"),
         @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
})
@GetMapping
public List<DocumentoResponseDTO> listar(@PathVariable Long solicitudId) {
     return documentoService.listarPorSolicitud(solicitudId);
}

@Operation(summary = "Añadir documento a una solicitud")
@ApiResponses(value = {
         @ApiResponse(responseCode = "201", description = "Documento añadido"),
         @ApiResponse(responseCode = "400", description = "Fichero inválido"),
         @ApiResponse(responseCode = "404", description = "Solicitud no encontrada"),
         @ApiResponse(responseCode = "409", description = "La solicitud no permite añadir documentos")
})
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<DocumentoResponseDTO> anadir(
             @PathVariable Long solicitudId,
             @RequestParam("fichero") MultipartFile fichero) {
         DocumentoResponseDTO creado = documentoService.anadir(solicitudId, fichero);
         URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                 .path("/{id}")
                 .buildAndExpand(creado.getIdentificador())
                 .toUri();
         return ResponseEntity.created(location).body(creado);
    }

    @Operation(summary = "Eliminar documento de una solicitud")
    @ApiResponses(value = {
             @ApiResponse(responseCode = "204", description = "Documento eliminado"),
             @ApiResponse(responseCode = "404", description = "Solicitud o documento no encontrados"),
             @ApiResponse(responseCode = "409", description = "La solicitud no permite eliminar documentos")
    })
    @DeleteMapping("/{documentoId}")
@PreAuthorize("hasRole('CIUDADANO')")
    public ResponseEntity<Void> eliminar(
             @PathVariable Long solicitudId,
             @PathVariable Long documentoId) {
         documentoService.eliminar(solicitudId, documentoId);
         return ResponseEntity.noContent().build();
    }
}
