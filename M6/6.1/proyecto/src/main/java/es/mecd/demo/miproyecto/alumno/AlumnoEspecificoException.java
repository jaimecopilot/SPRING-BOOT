package es.mecd.demo.miproyecto.alumno;

/** Excepción didáctica para demostrar un advice limitado a un paquete. */
public class AlumnoEspecificoException extends RuntimeException {
    public AlumnoEspecificoException(String mensaje) {
        super(mensaje);
    }
}
