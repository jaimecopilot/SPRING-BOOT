package es.mecd.demo.miproyecto.common.controller;
import es.mecd.demo.miproyecto.auth.AuthService;
import es.mecd.demo.miproyecto.auth.CambioPasswordRequestDTO;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/perfil")
public class PerfilController {
    private final AuthService authService; public PerfilController(AuthService a){authService=a;}
    @GetMapping public Map<String,Object> perfil(Authentication a){return Map.of("usuario",a.getName(),
        "authorities",a.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());}
    @PutMapping("/password") public ResponseEntity<Void> cambiarPassword(Authentication a,@Valid
        @RequestBody CambioPasswordRequestDTO r){authService.cambiarPassword(a.getName(),r);return
        ResponseEntity.noContent().build();}
}
