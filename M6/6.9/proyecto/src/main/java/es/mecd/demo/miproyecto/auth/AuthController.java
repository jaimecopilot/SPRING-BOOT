package es.mecd.demo.miproyecto.auth;
import jakarta.validation.Valid; import org.springframework.http.*; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth") public class AuthController { private final AuthService
    authService; public AuthController(AuthService a){authService=a;}
 @PostMapping("/registro") public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody
     RegistroRequestDTO r){return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(r));}
 @PostMapping("/login") public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody
     LoginRequestDTO r){return ResponseEntity.ok(authService.login(r));}
 @PostMapping("/refresh") public ResponseEntity<LoginResponseDTO> refresh(@Valid @RequestBody
     RefreshRequestDTO r){return ResponseEntity.ok(authService.refresh(r.getRefreshToken()));}
 @PostMapping("/logout") public ResponseEntity<Void> logout(@RequestHeader("Authorization") String h){
     authService.logout(extraerBearer(h));return ResponseEntity.noContent().build();}
 private String extraerBearer(String h){if(h==null||!h.startsWith("Bearer "))throw new
     TokenInvalidoException("Cabecera Authorization Bearer requerida");return h.substring(7);}
}
