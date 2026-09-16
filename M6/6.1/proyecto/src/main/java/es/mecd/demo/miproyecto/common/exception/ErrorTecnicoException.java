package es.mecd.demo.miproyecto.common.exception;

/** Envuelve una causa técnica sin exponer detalles internos al cliente. */
public class ErrorTecnicoException extends AplicacionException {
    public ErrorTecnicoException(String mensaje, Throwable causa) {
        super("ERROR_TECNICO", mensaje, causa);
    }
}
