package es.mecd.demo.miproyecto.auth;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class JwtConfig {
    @Bean
    SecretKey jwtSecretKey(@Value("${jwt.secret}") String secret) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
