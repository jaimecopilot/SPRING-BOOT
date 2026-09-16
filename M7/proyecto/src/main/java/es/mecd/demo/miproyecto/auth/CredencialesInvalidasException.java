package es.mecd.demo.miproyecto.auth;
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() { super("Credenciales inválidas"); }
}
