package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduce fallos de credenciales a un 401 estable. */
@RestControllerAdvice
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler({CredencialesInvalidasException.class, TokenInvalidoException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            RuntimeException ex,
            HttpServletRequest request) {
        String codigo = ex instanceof CredencialesInvalidasException
                ? "CREDENCIALES_INVALIDAS"
                : "TOKEN_INVALIDO";

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setCodigo(codigo);
        error.setMensaje(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
