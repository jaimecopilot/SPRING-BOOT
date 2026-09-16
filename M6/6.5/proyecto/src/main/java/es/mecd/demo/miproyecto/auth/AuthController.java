package es.mecd.demo.miproyecto.auth;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService; public AuthController(AuthService a){authService=a;}
    @PostMapping("/registro") public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody
        RegistroRequestDTO r){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(r)); }
}
