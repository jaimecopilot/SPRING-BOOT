package es.mecd.demo.miproyecto.fichero;

/** Error específico del subdominio de ficheros. */
public class FicheroNoEncontradoException extends RuntimeException {
    public FicheroNoEncontradoException(String nombre) {
        super("No se encontró el fichero " + nombre);
    }
}
