package es.mecd.demo.miproyecto.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

       private final UsuarioService usuarioService;

       public UsuarioController(UsuarioService usuarioService) {
           this.usuarioService = usuarioService;
       }
@Operation(summary = "Listar usuarios")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Lista de usuarios")
})
@GetMapping
public Page<UsuarioResponseDTO> listar(
         @PageableDefault(size = 20, sort = "id") Pageable pageable) {
     return usuarioService.listar(pageable);
}

@Operation(summary = "Consultar usuario por ID")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
         @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
})
@GetMapping("/{id}")
public UsuarioResponseDTO consultar(@PathVariable Long id) {
     return usuarioService.consultar(id);
}

@Operation(summary = "Cambiar roles de un usuario")
@ApiResponses(value = {
         @ApiResponse(responseCode = "200", description = "Roles cambiados"),
         @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
})
    @PatchMapping("/{id}/roles")
    public UsuarioResponseDTO cambiarRoles(
            @PathVariable Long id,
            @Valid @RequestBody CambioRolesRequestDTO request) {
        return usuarioService.cambiarRoles(id, request.getRoles());
    }
}
