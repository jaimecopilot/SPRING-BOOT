package es.mecd.demo.miproyecto.common.exception;

/** Regla funcional incumplida sobre un campo concreto. */
public class ValidacionNegocioException extends AplicacionException {
    private final String campo;
    private final String motivo;

    public ValidacionNegocioException(String campo, String motivo) {
        super(
                "VALIDACION_NEGOCIO",
                "El campo " + campo + " no cumple la regla: " + motivo);
        this.campo = campo;
        this.motivo = motivo;
    }

    public String getCampo() {
        return campo;
    }

    public String getMotivo() {
        return motivo;
    }
}
