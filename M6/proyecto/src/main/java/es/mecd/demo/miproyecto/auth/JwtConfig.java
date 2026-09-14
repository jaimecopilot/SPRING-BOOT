package es.mecd.demo.miproyecto.auth;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Construye la clave HMAC a partir de un secreto Base64 externalizado. */
@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    SecretKey jwtSecretKey() {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(
                    "jwt.secret debe ser Base64 válido y contener al menos 256 bits",
                    ex);
        }
    }
}
