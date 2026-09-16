package es.mecd.demo.miproyecto.auth;
import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice(assignableTypes = AuthController.class)
@Order(1)
public class AuthExceptionHandler {
    @ExceptionHandler({CredencialesInvalidasException.class, TokenInvalidoException.class})
    ResponseEntity<ErrorResponse> auth(RuntimeException ex, HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(); error.setTimestamp(Instant.now().toString());
        error.setStatus(401); error.setCodigo("AUTH_ERROR"); error.setMensaje(ex.getMessage());
        error.setPath(request.getRequestURI()); return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
