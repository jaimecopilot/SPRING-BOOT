package es.mecd.demo.miproyecto.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceIntegrationTest {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void accessYRefreshDebenTenerTiposDistintos() {
        Usuario ana = usuarioRepository.findByUsername("ana").orElseThrow();
        String access = jwtService.generarToken(ana);
        String refresh = jwtService.generarRefreshToken(ana);

        assertTrue(jwtService.esAccessTokenValido(access));
        assertFalse(jwtService.esRefreshTokenValido(access));
        assertTrue(jwtService.esRefreshTokenValido(refresh));
        assertFalse(jwtService.esAccessTokenValido(refresh));

        Claims claims = jwtService.extraerClaims(access);
        assertTrue(claims.containsKey("roles"));
        assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
    }
}
