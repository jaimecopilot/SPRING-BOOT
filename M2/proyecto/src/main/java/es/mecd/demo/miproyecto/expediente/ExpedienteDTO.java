package es.mecd.demo.miproyecto.expediente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/** DTO utilizado por la API de expedientes. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExpedienteDTO {
    @JsonProperty("id") private String identificador;
    private String titular;
    private String dni;
    private String estado;
    private String tipo;
    @JsonFormat(pattern = "yyyy-MM-dd") private LocalDate fechaSolicitud;
    private Double importe;
    private Boolean activo;
    private List<String> documentos;
    private String observaciones;
    @JsonIgnore private String numeroSeguridadSocial;
    private SolicitanteDTO solicitante;

    /** Constructor requerido por Jackson. */
    public ExpedienteDTO() { }
    /** Construye un expediente. */
    public ExpedienteDTO(String identificador, String titular, String dni, String estado, String tipo,
                         LocalDate fechaSolicitud, Double importe, Boolean activo, List<String> documentos) {
        this.identificador = identificador; this.titular = titular; this.dni = dni; this.estado = estado;
        this.tipo = tipo; this.fechaSolicitud = fechaSolicitud; this.importe = importe; this.activo = activo;
        this.documentos = documentos;
    }
    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public List<String> getDocumentos() { return documentos; }
    public void setDocumentos(List<String> documentos) { this.documentos = documentos; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public String getNumeroSeguridadSocial() { return numeroSeguridadSocial; }
    public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
        this.numeroSeguridadSocial = numeroSeguridadSocial;
    }
    public SolicitanteDTO getSolicitante() { return solicitante; }
    public void setSolicitante(SolicitanteDTO solicitante) { this.solicitante = solicitante; }
}
