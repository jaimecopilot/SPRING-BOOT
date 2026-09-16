package es.mecd.demo.miproyecto.common.controller;
import es.mecd.demo.miproyecto.auth.*; import jakarta.validation.Valid; import java.util.Map; import
    org.springframework.http.ResponseEntity; import
    org.springframework.security.access.prepost.PreAuthorize; import
    org.springframework.security.core.GrantedAuthority; import
    org.springframework.security.core.annotation.AuthenticationPrincipal; import
    org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/perfil") public class PerfilController { private final
    AuthService authService; private final UsuarioService usuarioService; public
    PerfilController(AuthService a,UsuarioService u){authService=a;usuarioService=u;}
 @GetMapping public Map<String,Object> perfil(@AuthenticationPrincipal UsuarioPrincipal u){return
     Map.of("id",u.getId(),"username",u.getUsername(),"email",u.getEmail(),"authorities",
     u.getAuthorities().stream().map(GrantedAuthority::getAuthority).sorted().toList());}
 @GetMapping("/usuario") public Map<String,Object> usuario(@AuthenticationPrincipal UsuarioPrincipal u){
     return Map.of("id",u.getId(),"username",u.getUsername(),"email",u.getEmail());}
 @GetMapping("/{id}") @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')") public
     UsuarioResponseDTO perfilPorId(@PathVariable Long id){return usuarioService.consultar(id);}
 @PutMapping("/password") public ResponseEntity<Void> cambiarPassword(@AuthenticationPrincipal
     UsuarioPrincipal u,@Valid @RequestBody CambioPasswordRequestDTO r){
     authService.cambiarPassword(u.getUsername(),r);return ResponseEntity.noContent().build();}
}
