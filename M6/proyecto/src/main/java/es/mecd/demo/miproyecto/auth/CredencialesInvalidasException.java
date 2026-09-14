package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.AplicacionException;

/** Credenciales de login rechazadas sin revelar qué dato falló. */
public class CredencialesInvalidasException extends AplicacionException {
    public CredencialesInvalidasException() {
        super("CREDENCIALES_INVALIDAS", "Credenciales incorrectas");
    }
}
