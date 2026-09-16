package es.mecd.demo.miproyecto.common.controller;

import es.mecd.demo.miproyecto.auth.AuthService;
import es.mecd.demo.miproyecto.auth.CambioPasswordRequestDTO;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {
    private final AuthService authService;

    public PerfilController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public Map<String, Object> perfil(@AuthenticationPrincipal UserDetails user) {
        return Map.of(
                "usuario", user.getUsername(),
                "authorities", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .sorted()
                        .toList());
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiarPassword(
            Authentication authentication,
            @Valid @RequestBody CambioPasswordRequestDTO request) {
        authService.cambiarPassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }
}
