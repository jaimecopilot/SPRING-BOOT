package es.mecd.demo.miproyecto.common.exception;

/** Operación válida técnicamente pero prohibida por una regla de negocio. */
public class OperacionNoPermitidaException extends AplicacionException {
    public OperacionNoPermitidaException(String mensaje) {
        super("OPERACION_NO_PERMITIDA", mensaje);
    }
}
