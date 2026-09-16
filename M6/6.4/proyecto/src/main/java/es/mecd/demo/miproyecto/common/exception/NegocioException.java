package es.mecd.demo.miproyecto.common.exception;

/**
 * Excepción genérica heredada de M4.
 * Se conserva por compatibilidad; en M5 se prefieren tipos específicos.
 */
@Deprecated(forRemoval = false)
public class NegocioException extends AplicacionException {
    public NegocioException(String mensaje) {
        super("NEGOCIO", mensaje);
    }

    protected NegocioException(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
}
