package es.mecd.demo.miproyecto.fichero;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Advice restringido al paquete de ficheros. */
@RestControllerAdvice(basePackages = "es.mecd.demo.miproyecto.fichero")
@Order(0)
public class FicheroExceptionHandler {
    @ExceptionHandler(FicheroNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handle(
            FicheroNoEncontradoException ex,
            HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(404);
        error.setCodigo("FICHERO_NO_ENCONTRADO");
        error.setMensaje(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.status(404).body(error);
    }
}
