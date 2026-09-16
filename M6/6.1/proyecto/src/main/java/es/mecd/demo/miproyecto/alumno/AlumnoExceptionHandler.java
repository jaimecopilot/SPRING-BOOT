package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Advice de ejemplo restringido a controladores del paquete alumno. */
@RestControllerAdvice(basePackages = "es.mecd.demo.miproyecto.alumno")
@Order(0)
public class AlumnoExceptionHandler {
    @ExceptionHandler(AlumnoEspecificoException.class)
    public ResponseEntity<ErrorResponse> handleAlumnoEspecifico(
            AlumnoEspecificoException ex,
            HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setCodigo("ALUMNO_ESPECIFICO");
        error.setMensaje(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.badRequest().body(error);
    }
}
