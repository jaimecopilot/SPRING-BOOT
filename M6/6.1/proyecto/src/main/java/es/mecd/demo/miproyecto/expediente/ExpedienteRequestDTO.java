package es.mecd.demo.miproyecto.expediente;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** DTO de entrada para crear expedientes. */
public class ExpedienteRequestDTO {
    @NotBlank(message = "El titular es obligatorio")
    private String titular;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Valid
    @NotNull(message = "El solicitante es obligatorio")
    private SolicitanteDTO solicitante;

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public SolicitanteDTO getSolicitante() { return solicitante; }
    public void setSolicitante(SolicitanteDTO solicitante) { this.solicitante = solicitante; }
}
