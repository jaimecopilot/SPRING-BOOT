package es.mecd.demo.miproyecto.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/** Genera y valida access/refresh tokens con JJWT. */
@Service
public class JwtService {
    private static final String CLAIM_TIPO = "tipo";
    private static final String TIPO_ACCESS = "access";
    private static final String TIPO_REFRESH = "refresh";

    private final SecretKey clave;
    private final long expirationMs;
    private final long refreshExpirationMs;

    public JwtService(
            SecretKey clave,
            @Value("${jwt.expiration}") long expirationMs,
            @Value("${jwt.refresh-expiration}") long refreshExpirationMs) {
        this.clave = clave;
        this.expirationMs = expirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + expirationMs);
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> "ROLE_" + rol.getNombre())
                .sorted()
                .toList();

        return Jwts.builder()
                .subject(usuario.getUsername())
                .id(UUID.randomUUID().toString())
                .claim(CLAIM_TIPO, TIPO_ACCESS)
                .claim("id", usuario.getId())
                .claim("email", usuario.getEmail())
                .claim("roles", roles)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    public String generarRefreshToken(Usuario usuario) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .subject(usuario.getUsername())
                .id(UUID.randomUUID().toString())
                .claim(CLAIM_TIPO, TIPO_REFRESH)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(clave, Jwts.SIG.HS256)
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    public Long extraerId(String token) {
        Number id = extraerClaims(token).get("id", Number.class);
        return id == null ? null : id.longValue();
    }

    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public List<GrantedAuthority> extraerAuthorities(String token) {
        Claims claims = extraerClaims(token);
        Object rawRoles = claims.get("roles");
        if (!(rawRoles instanceof List<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
    }

    public boolean esValido(String token) {
        return claimsSiValido(token) != null;
    }

    public boolean esAccessTokenValido(String token) {
        Claims claims = claimsSiValido(token);
        return claims != null && TIPO_ACCESS.equals(claims.get(CLAIM_TIPO, String.class));
    }

    public boolean esRefreshTokenValido(String token) {
        Claims claims = claimsSiValido(token);
        return claims != null && TIPO_REFRESH.equals(claims.get(CLAIM_TIPO, String.class));
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    private Claims claimsSiValido(String token) {
        try {
            return extraerClaims(token);
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }
}
