package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService,
            UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager; this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository; this.rolRepository = rolRepository; this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public RegistroResponseDTO registrar(RegistroRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RecursoDuplicadoException("Usuario", "username", request.getUsername());
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException("Usuario", "email", request.getEmail());
        }
        Rol rol = rolRepository.findByNombre("CIUDADANO")
                .orElseGet(() -> rolRepository.findByNombre("USER")
                        .orElseThrow(() -> new RecursoNoEncontradoException("Rol", "USER")));
        Usuario u = new Usuario(); u.setUsername(request.getUsername());
        u.setPassword(passwordEncoder.encode(request.getPassword())); u.setEmail(request.getEmail());
        u.getRoles().add(rol); u = usuarioRepository.save(u);
        RegistroResponseDTO dto = new RegistroResponseDTO(); dto.setId(u.getId().toString());
        dto.setUsername(u.getUsername()); dto.setEmail(u.getEmail());
        dto.setRoles(u.getRoles().stream().map(Rol::getNombre).sorted().toList()); return dto;
    }
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) { throw new CredencialesInvalidasException(); }
        Usuario u = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(CredencialesInvalidasException::new);
        return tokens(u);
    }
    @Transactional(readOnly = true)
    public LoginResponseDTO refresh(String refreshToken) {
        if (!jwtService.esRefreshValido(refreshToken)) throw new TokenInvalidoException("Refresh token inválido");
        Usuario u = usuarioRepository.findByUsername(jwtService.username(refreshToken))
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", jwtService.username(refreshToken)));
        return tokens(u);
    }
    private LoginResponseDTO tokens(Usuario u) {
        return new LoginResponseDTO(jwtService.generarAccessToken(u), jwtService.generarRefreshToken(u),
                jwtService.getExpirationMs() / 1000);
    }
}
