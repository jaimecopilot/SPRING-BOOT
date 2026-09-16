package es.mecd.demo.miproyecto.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
@Component
public class SecurityErrorWriter {
    private final ObjectMapper objectMapper;
    public SecurityErrorWriter(ObjectMapper objectMapper) { this.objectMapper = objectMapper; }
    public void write(HttpServletRequest request, HttpServletResponse response, int status,
            String codigo, String mensaje) throws IOException {
        response.setStatus(status); response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new LinkedHashMap<>(); body.put("timestamp", Instant.now().toString());
        body.put("status", status); body.put("codigo", codigo); body.put("mensaje", mensaje);
        body.put("path", request.getRequestURI()); objectMapper.writeValue(response.getOutputStream(), body);
    }
}
