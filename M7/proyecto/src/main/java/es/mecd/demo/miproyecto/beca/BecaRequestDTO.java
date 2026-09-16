package es.mecd.demo.miproyecto.beca;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class BecaRequestDTO {
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String descripcion;

    @NotNull(message = "El importe máximo es obligatorio")
    @PositiveOrZero(message = "El importe máximo debe ser positivo o cero")
    private BigDecimal importeMaximo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 2020, message = "El año debe ser igual o posterior a 2020")
    @Max(value = 2100, message = "El año debe ser igual o anterior a 2100")
    private Integer anio;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getImporteMaximo() { return importeMaximo; }
    public void setImporteMaximo(BigDecimal importeMaximo) { this.importeMaximo = importeMaximo; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
}
