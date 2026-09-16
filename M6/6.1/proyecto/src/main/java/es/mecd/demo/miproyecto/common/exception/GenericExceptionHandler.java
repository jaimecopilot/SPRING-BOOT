package es.mecd.demo.miproyecto.common.exception;

import es.mecd.demo.miproyecto.common.dto.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Última barrera: errores técnicos e inesperados. */
@RestControllerAdvice
@Order(3)
public class GenericExceptionHandler {
    private static final Logger LOG =
            LoggerFactory.getLogger(GenericExceptionHandler.class);

    @ExceptionHandler(ErrorTecnicoException.class)
    public ResponseEntity<ErrorResponse> handleTecnico(
            ErrorTecnicoException ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        LOG.error(
                "Error técnico en {} - traceId={}",
                request.getRequestURI(),
                traceId,
                ex);
        return ErrorResponseFactory.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getCodigo(),
                "Error interno del servidor",
                request,
                null,
                traceId);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(
            Exception ex,
            HttpServletRequest request) {
        String traceId = ErrorResponseFactory.newTraceId();
        LOG.error(
                "Error inesperado en {} - traceId={}",
                request.getRequestURI(),
                traceId,
                ex);
        return ErrorResponseFactory.build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR_INTERNO",
                "Error interno del servidor",
                request,
                null,
                traceId);
    }
}
