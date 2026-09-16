package es.mecd.demo.miproyecto.common.exception;

/** Base estable para las excepciones funcionales de la aplicación. */
public abstract class AplicacionException extends RuntimeException {
    private final String codigo;

    protected AplicacionException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    protected AplicacionException(
            String codigo,
            String mensaje,
            Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
