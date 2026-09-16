package es.mecd.demo.miproyecto.solicitud;

import java.math.BigDecimal;

public class BecaResumenDTO {
       private String id;
       private String codigo;
       private String nombre;
       private BigDecimal importeMaximo;

       public String getId() { return id; }
       public void setId(String id) { this.id = id; }

       public String getCodigo() { return codigo; }
       public void setCodigo(String codigo) { this.codigo = codigo; }

       public String getNombre() { return nombre; }
       public void setNombre(String nombre) { this.nombre = nombre; }

       public BigDecimal getImporteMaximo() { return importeMaximo; }
       public void setImporteMaximo(BigDecimal importeMaximo) { this.importeMaximo = importeMaximo; }
}
