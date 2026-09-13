package es.mecd.demo.miproyecto.common.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> validation(MethodArgumentNotValidException ex) {
        List<Map<String,String>> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(e -> Map.of("field", e.getField(), "message", String.valueOf(e.getDefaultMessage()))).toList();
        return ResponseEntity.badRequest().body(Map.of("timestamp", Instant.now().toString(), "status", 400,
        "error", "Bad Request", "message", "Errores de validación", "errors", errors));
    }
    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<Map<String,Object>> negocio(NegocioException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("status",409,"error","Conflict","message",ex.getMessage()));
    }
    @ExceptionHandler( {
        IllegalArgumentException.class, MethodArgumentTypeMismatchException.class,
        HttpMessageNotReadableException.class
    })
    public ResponseEntity<Map<String,Object>> badRequest(Exception ex) {
        return ResponseEntity.badRequest().body(Map.of("status",400,"error","Bad Request",
        "message","La petición no cumple el contrato de entrada"));
    }
}
