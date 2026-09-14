package es.mecd.demo.miproyecto.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/** Escribe 401/403 con el mismo contrato JSON heredado de M5. */
@Component
public class SecurityErrorWriter {
    private final ObjectMapper objectMapper;

    public SecurityErrorWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(
            HttpServletRequest request,
            HttpServletResponse response,
            int status,
            String codigo,
            String mensaje) throws IOException {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(status);
        error.setCodigo(codigo);
        error.setMensaje(mensaje);
        error.setPath(request.getRequestURI());
        error.setTraceId(UUID.randomUUID().toString());

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), error);
    }
}
