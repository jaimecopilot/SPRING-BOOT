package es.mecd.demo.miproyecto.solicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CambioEstadoRequestDTO {

       @NotBlank(message = "El estado es obligatorio")
       private String estado;

       @Size(max = 1000, message = "Las observaciones no pueden superar 1000 caracteres")
       private String observaciones;

       @PositiveOrZero(message = "El importe concedido debe ser positivo o cero")
       private BigDecimal importeConcedido;

       public String getEstado() { return estado; }
       public void setEstado(String estado) { this.estado = estado; }

       public String getObservaciones() { return observaciones; }
       public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

       public BigDecimal getImporteConcedido() { return importeConcedido; }
       public void setImporteConcedido(BigDecimal importeConcedido) { this.importeConcedido = importeConcedido; }
}
