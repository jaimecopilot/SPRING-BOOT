package es.mecd.demo.miproyecto.common.exception;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import es.mecd.demo.miproyecto.common.dto.error.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/** Errores de contrato de entrada y validación. */
@RestControllerAdvice
@Order(1)
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ValidationError> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()))
                .toList();
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now().toString());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setCodigo("VALIDACION");
        response.setMensaje("La petición tiene errores de validación");
        response.setPath(request.getRequestURI());
        response.setTraceId(UUID.randomUUID().toString());
        response.setErrors(errores);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMensajeNoLegible(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.BAD_REQUEST,
                "MENSAJE_NO_LEIBLE",
                "El cuerpo de la petición no se puede leer",
                request,
                null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleParametroFaltante(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.BAD_REQUEST,
                "PARAMETRO_FALTANTE",
                "Falta el parámetro " + ex.getParameterName(),
                request,
                null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTipoIncorrecto(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        return ErrorResponseFactory.build(
                HttpStatus.BAD_REQUEST,
                "TIPO_INCORRECTO",
                "Valor no válido para " + ex.getName(),
                request,
                null);
    }
}
