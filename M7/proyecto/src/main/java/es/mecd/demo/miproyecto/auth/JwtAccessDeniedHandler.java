package es.mecd.demo.miproyecto.auth;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final SecurityErrorWriter writer;
    public JwtAccessDeniedHandler(SecurityErrorWriter writer) { this.writer = writer; }
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex)
            throws IOException { writer.write(request, response, 403, "ACCESO_DENEGADO", "No tiene permisos suficientes"); }
}
