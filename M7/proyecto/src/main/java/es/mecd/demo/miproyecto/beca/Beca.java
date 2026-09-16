package es.mecd.demo.miproyecto.beca;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "becas")
public class Beca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "importe_maximo", nullable = false, precision = 10, scale = 2)
    private BigDecimal importeMaximo;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private Boolean activa = true;
public Beca() {
}

public Beca(String codigo, String nombre, String descripcion,
            BigDecimal importeMaximo, Integer anio) {
    this.codigo = codigo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.importeMaximo = importeMaximo;
    this.anio = anio;
    this.activa = true;
}

public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

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
