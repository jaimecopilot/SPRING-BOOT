package es.mecd.demo.miproyecto.auth;
import jakarta.validation.Valid; import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/admin/usuarios")
public class UsuarioController {
 private final UsuarioService usuarioService; public UsuarioController(UsuarioService s){usuarioService=s;}
 @GetMapping @PreAuthorize("hasRole('ADMIN')") public List<UsuarioResponseDTO> listar(){return
     usuarioService.listarTodos();}
 @GetMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public UsuarioResponseDTO
     consultar(@PathVariable Long id){return usuarioService.consultar(id);}
 @PutMapping("/{id}/roles") @PreAuthorize("hasRole('ADMIN')") public UsuarioResponseDTO
     cambiarRoles(@PathVariable Long id,@Valid @RequestBody CambioRolesRequestDTO r){return
     usuarioService.cambiarRoles(id,r.getRoles());}
}
