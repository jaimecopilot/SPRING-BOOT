package es.mecd.demo.miproyecto.common.controller;

import es.mecd.demo.miproyecto.auth.AuthService;
import es.mecd.demo.miproyecto.auth.CambioPasswordRequestDTO;
import es.mecd.demo.miproyecto.auth.UsuarioPrincipal;
import es.mecd.demo.miproyecto.auth.UsuarioResponseDTO;
import es.mecd.demo.miproyecto.auth.UsuarioService;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Operaciones del perfil de la identidad autenticada. */
@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {
    private final AuthService authService;
    private final UsuarioService usuarioService;

    public PerfilController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Map<String, Object> perfil(
            @AuthenticationPrincipal UsuarioPrincipal user) {
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "authorities", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .sorted()
                        .toList());
    }

    @GetMapping("/usuario")
    public Map<String, Object> usuario(
            @AuthenticationPrincipal UsuarioPrincipal user) {
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail());
    }

    @GetMapping("/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public UsuarioResponseDTO perfilPorId(@PathVariable Long id) {
        return usuarioService.consultar(id);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiarPassword(
            @AuthenticationPrincipal UsuarioPrincipal user,
            @Valid @RequestBody CambioPasswordRequestDTO request) {
        authService.cambiarPassword(user.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
