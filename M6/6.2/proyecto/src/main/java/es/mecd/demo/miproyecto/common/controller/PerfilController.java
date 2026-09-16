package es.mecd.demo.miproyecto.common.controller;

import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perfil")
public class PerfilController {
    @GetMapping
    public Map<String, Object> perfil(Authentication authentication) {
        return Map.of("usuario", authentication.getName(), "authorities",
                authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
    }
}
