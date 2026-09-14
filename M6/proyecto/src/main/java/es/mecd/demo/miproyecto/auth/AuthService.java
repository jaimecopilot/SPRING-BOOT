package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import io.jsonwebtoken.Claims;
import java.time.Instant;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Registro, login, refresh, logout y mantenimiento de credenciales. */
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRevocationService tokenRevocationService;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenRevocationService tokenRevocationService,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRevocationService = tokenRevocationService;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO registrar(RegistroRequestDTO request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RecursoDuplicadoException(
                    "Usuario", "username", request.getUsername());
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RecursoDuplicadoException(
                    "Usuario", "email", request.getEmail());
        }

        Rol rolUser = rolRepository.findByNombre("USER")
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", "USER"));

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setActivo(true);
        usuario.getRoles().add(rolUser);
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new CredencialesInvalidasException();
        }

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(CredencialesInvalidasException::new);
        return crearPar(usuario);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO refresh(String refreshToken) {
        if (!jwtService.esRefreshTokenValido(refreshToken)) {
            throw new TokenInvalidoException("Refresh token inválido o expirado");
        }

        Claims claims = jwtService.extraerClaims(refreshToken);
        String jti = claims.getId();
        if (jti == null || !tokenRevocationService.consumeRefresh(
                jti, claims.getExpiration().toInstant())) {
            throw new TokenInvalidoException("Refresh token ya utilizado");
        }

        String username = claims.getSubject();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", username));
        if (!usuario.isActivo()) {
            throw new TokenInvalidoException("Usuario inactivo");
        }
        return crearPar(usuario);
    }

    public void logout(String accessToken) {
        if (!jwtService.esAccessTokenValido(accessToken)) {
            throw new TokenInvalidoException("Access token inválido o expirado");
        }

        Claims claims = jwtService.extraerClaims(accessToken);
        tokenRevocationService.revokeAccess(
                claims.getId(), claims.getExpiration().toInstant());
    }

    @Transactional
    public void cambiarPassword(
            String username,
            CambioPasswordRequestDTO request) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", username));

        if (!passwordEncoder.matches(
                request.getPasswordActual(), usuario.getPassword())) {
            throw new OperacionNoPermitidaException(
                    "La contraseña actual no es correcta");
        }

        usuario.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
    }

    private LoginResponseDTO crearPar(Usuario usuario) {
        return new LoginResponseDTO(
                jwtService.generarToken(usuario),
                jwtService.generarRefreshToken(usuario),
                jwtService.getExpirationMs() / 1000);
    }

    private UsuarioResponseDTO toDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdentificador(usuario.getId() == null ? null : usuario.getId().toString());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());
        dto.setRoles(usuario.getRoles().stream()
                .map(Rol::getNombre)
                .sorted()
                .toList());
        return dto;
    }
}
