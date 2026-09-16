package es.mecd.demo.miproyecto.common.exception;

/** Recurso solicitado inexistente. */
public class RecursoNoEncontradoException extends AplicacionException {
    private final String recurso;
    private final Object id;

    public RecursoNoEncontradoException(String recurso, Object id) {
        super(
                "RECURSO_NO_ENCONTRADO",
                "No se encontró " + recurso + " con id " + id);
        this.recurso = recurso;
        this.id = id;
    }

    public String getRecurso() {
        return recurso;
    }

    public Object getId() {
        return id;
    }
}
