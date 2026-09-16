package es.mecd.demo.miproyecto.beca;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BecaResponseDTO {
    @JsonProperty("id")
    private String identificador;
    private String codigo;
    private String nombre;
    private String descripcion;
    private BigDecimal importeMaximo;
    private Integer anio;
    private Boolean activa;

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }
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
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
