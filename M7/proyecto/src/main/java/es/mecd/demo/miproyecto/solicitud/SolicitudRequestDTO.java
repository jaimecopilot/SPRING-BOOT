package es.mecd.demo.miproyecto.solicitud;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SolicitudRequestDTO {

    @NotNull(message = "El alumno es obligatorio")
    private Long alumnoId;

    @NotNull(message = "La beca es obligatoria")
    private Long becaId;

    @Size(max = 1000, message = "Las observaciones no pueden superar 1000 caracteres")
    private String observaciones;

    public Long getAlumnoId() { return alumnoId; }
    public void setAlumnoId(Long alumnoId) { this.alumnoId = alumnoId; }

    public Long getBecaId() { return becaId; }
    public void setBecaId(Long becaId) { this.becaId = becaId; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
