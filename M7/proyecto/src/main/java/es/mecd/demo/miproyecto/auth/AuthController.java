package es.mecd.demo.miproyecto.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/registro")
    public ResponseEntity<RegistroResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }
    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) { return authService.login(request); }
    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@Valid @RequestBody RefreshRequestDTO request) {
        return authService.refresh(request.getRefreshToken());
    }
}
