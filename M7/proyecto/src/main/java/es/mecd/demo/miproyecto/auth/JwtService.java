package es.mecd.demo.miproyecto.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String CLAIM_TIPO = "tipo";
    private final SecretKey clave;
    private final long expirationMs;
    private final long refreshExpirationMs;
    public JwtService(SecretKey clave, @Value("${jwt.expiration}") long expirationMs,
            @Value("${jwt.refresh-expiration}") long refreshExpirationMs) {
        this.clave = clave; this.expirationMs = expirationMs; this.refreshExpirationMs = refreshExpirationMs;
    }
    public String generarAccessToken(Usuario usuario) { return generar(usuario, "access", expirationMs, true); }
    public String generarRefreshToken(Usuario usuario) { return generar(usuario, "refresh", refreshExpirationMs, false); }
    private String generar(Usuario usuario, String tipo, long duracion, boolean incluirRoles) {
        Date ahora = new Date();
        var builder = Jwts.builder().subject(usuario.getUsername()).claim(CLAIM_TIPO, tipo)
                .issuedAt(ahora).expiration(new Date(ahora.getTime() + duracion));
        if (incluirRoles) {
            builder.claim("roles", usuario.getRoles().stream().map(r -> "ROLE_" + r.getNombre()).sorted().toList());
        }
        return builder.signWith(clave, Jwts.SIG.HS256).compact();
    }
    public Claims extraerClaims(String token) {
        return Jwts.parser().verifyWith(clave).build().parseSignedClaims(token).getPayload();
    }
    public boolean esAccessValido(String token) { return esTipoValido(token, "access"); }
    public boolean esRefreshValido(String token) { return esTipoValido(token, "refresh"); }
    private boolean esTipoValido(String token, String tipo) {
        try { return tipo.equals(extraerClaims(token).get(CLAIM_TIPO, String.class)); }
        catch (JwtException | IllegalArgumentException ex) { return false; }
    }
    public String username(String token) { return extraerClaims(token).getSubject(); }
    public List<GrantedAuthority> authorities(String token) {
        Object raw = extraerClaims(token).get("roles");
        if (!(raw instanceof List<?> lista)) return List.of();
        return lista.stream().filter(String.class::isInstance).map(String.class::cast)
                .map(SimpleGrantedAuthority::new).map(GrantedAuthority.class::cast).toList();
    }
    public long getExpirationMs() { return expirationMs; }
}
