package es.mecd.demo.miproyecto.common.exception;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Traduce excepciones funcionales a códigos HTTP estables. */
@RestControllerAdvice
@Order(2)
public class BusinessExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNoEncontrado(
            RecursoNoEncontradoException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.NOT_FOUND,
                ex.getCodigo(),
                ex.getMessage(),
                request,
                Map.of("recurso", ex.getRecurso(), "id", ex.getId()));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleDuplicado(
            RecursoDuplicadoException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.CONFLICT,
                ex.getCodigo(),
                ex.getMessage(),
                request,
                Map.of(
                        "recurso", ex.getRecurso(),
                        "campo", ex.getCampo(),
                        "valor", ex.getValor()));
    }

    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ErrorResponse> handleOperacionNoPermitida(
            OperacionNoPermitidaException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.CONFLICT,
                ex.getCodigo(),
                ex.getMessage(),
                request,
                null);
    }

    @ExceptionHandler(ValidacionNegocioException.class)
    public ResponseEntity<ErrorResponse> handleValidacionNegocio(
            ValidacionNegocioException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getCodigo(),
                ex.getMessage(),
                request,
                Map.of("campo", ex.getCampo(), "motivo", ex.getMotivo()));
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrorResponse> handleNegocioHeredado(
            NegocioException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.CONFLICT,
                ex.getCodigo(),
                ex.getMessage(),
                request,
                null);
    }
}
