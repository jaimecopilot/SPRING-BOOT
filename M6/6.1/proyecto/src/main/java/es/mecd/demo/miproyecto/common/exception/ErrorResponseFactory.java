package es.mecd.demo.miproyecto.common.exception;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Construcción común de respuestas de error. */
final class ErrorResponseFactory {
    private ErrorResponseFactory() {
    }

    static String newTraceId() {
        return UUID.randomUUID().toString();
    }

    static ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String codigo,
            String mensaje,
            HttpServletRequest request,
            Map<String, Object> detalles) {
        return build(
                status,
                codigo,
                mensaje,
                request,
                detalles,
                newTraceId());
    }

    static ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String codigo,
            String mensaje,
            HttpServletRequest request,
            Map<String, Object> detalles,
            String traceId) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(status.value());
        error.setCodigo(codigo);
        error.setMensaje(mensaje);
        error.setPath(request.getRequestURI());
        error.setTraceId(traceId);
        error.setDetalles(detalles);
        return ResponseEntity.status(status).body(error);
    }
}
