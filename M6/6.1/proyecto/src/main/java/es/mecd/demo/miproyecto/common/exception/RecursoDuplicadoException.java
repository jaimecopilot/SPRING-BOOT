package es.mecd.demo.miproyecto.common.exception;

/** Conflicto por una clave o dato funcional ya existente. */
public class RecursoDuplicadoException extends NegocioException {
    private final String recurso;
    private final String campo;
    private final Object valor;

    public RecursoDuplicadoException(
            String recurso,
            String campo,
            Object valor) {
        super(
                "RECURSO_DUPLICADO",
                "Ya existe " + recurso + " con " + campo + " = " + valor);
        this.recurso = recurso;
        this.campo = campo;
        this.valor = valor;
    }

    public String getRecurso() {
        return recurso;
    }

    public String getCampo() {
        return campo;
    }

    public Object getValor() {
        return valor;
    }
}
