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

@RestControllerAdvice(assignableTypes = AuthController.class)
@Order(1)
public class AuthExceptionHandler {

    @ExceptionHandler(CredencialesInvalidasException.class)
    ResponseEntity<ErrorResponse> credenciales(
            CredencialesInvalidasException ex,
            HttpServletRequest request) {
        return respuesta(HttpStatus.UNAUTHORIZED,
                "CREDENCIALES_INVALIDAS", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    ResponseEntity<ErrorResponse> token(
            TokenInvalidoException ex,
            HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST,
                "TOKEN_INVALIDO", ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> respuesta(
            HttpStatus status, String codigo, String mensaje, String path) {
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(Instant.now().toString());
        error.setStatus(status.value());
        error.setCodigo(codigo);
        error.setMensaje(mensaje);
        error.setPath(path);
        error.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.status(status).body(error);
    }
}
