package es.mecd.demo.miproyecto.expediente;

import com.fasterxml.jackson.annotation.JsonProperty;

/** DTO anidado del solicitante de un expediente. */
public class SolicitanteDTO {
    private String nombre;
    private String apellidos;
    @JsonProperty("dni")
    private String documentoIdentidad;

    /** Constructor requerido por Jackson. */
    public SolicitanteDTO() { }
    /** Construye un solicitante. */
    public SolicitanteDTO(String nombre, String apellidos, String documentoIdentidad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.documentoIdentidad = documentoIdentidad;
    }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
}
