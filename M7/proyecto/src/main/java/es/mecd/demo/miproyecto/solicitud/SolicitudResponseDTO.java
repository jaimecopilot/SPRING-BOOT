package es.mecd.demo.miproyecto.solicitud;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolicitudResponseDTO {

    @JsonProperty("id")
    private String identificador;

    private AlumnoResumenDTO alumno;
    private BecaResumenDTO beca;
    private String estado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaSolicitud;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaResolucion;

    private BigDecimal importeConcedido;
private String observaciones;
private Integer numeroDocumentos;

public String getIdentificador() { return identificador; }
public void setIdentificador(String identificador) { this.identificador = identificador; }

public AlumnoResumenDTO getAlumno() { return alumno; }
public void setAlumno(AlumnoResumenDTO alumno) { this.alumno = alumno; }

public BecaResumenDTO getBeca() { return beca; }
public void setBeca(BecaResumenDTO beca) { this.beca = beca; }

public String getEstado() { return estado; }
public void setEstado(String estado) { this.estado = estado; }

public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

public LocalDateTime getFechaResolucion() { return fechaResolucion; }
public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

public BigDecimal getImporteConcedido() { return importeConcedido; }
public void setImporteConcedido(BigDecimal importeConcedido) { this.importeConcedido = importeConcedido; }

public String getObservaciones() { return observaciones; }
       public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

       public Integer getNumeroDocumentos() { return numeroDocumentos; }
       public void setNumeroDocumentos(Integer numeroDocumentos) { this.numeroDocumentos = numeroDocumentos; }
}
