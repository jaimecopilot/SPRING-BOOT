package es.mecd.demo.miproyecto.common.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

/** Error de validación asociado a un campo de entrada. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationError {
    private String field;
    private Object rejectedValue;
    private String message;

    public ValidationError() {
    }

    public ValidationError(
            String field,
            Object rejectedValue,
            String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
