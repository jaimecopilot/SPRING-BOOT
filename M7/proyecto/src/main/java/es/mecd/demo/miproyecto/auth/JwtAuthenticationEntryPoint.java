package es.mecd.demo.miproyecto.auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final SecurityErrorWriter writer;
    public JwtAuthenticationEntryPoint(SecurityErrorWriter writer) { this.writer = writer; }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException { writer.write(request, response, 401, "NO_AUTENTICADO", "Autenticación requerida"); }
}
