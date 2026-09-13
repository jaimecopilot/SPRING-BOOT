package es.mecd.demo.miproyecto.common.exception;

/** Representa una violación esperada de una regla de negocio. */
public class NegocioException extends RuntimeException {

    /** Construye la excepción con un mensaje.
     * @param mensaje explicación de la regla violada
     */
    public NegocioException(String mensaje) {
        super(mensaje);
    }

    /** Construye la excepción con mensaje y causa.
     * @param mensaje explicación de la regla violada
     * @param causa causa original
     */
    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
