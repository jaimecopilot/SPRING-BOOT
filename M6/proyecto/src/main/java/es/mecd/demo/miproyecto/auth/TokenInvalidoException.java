package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.AplicacionException;

/** Credencial JWT inválida, expirada, revocada o del tipo incorrecto. */
public class TokenInvalidoException extends AplicacionException {
    public TokenInvalidoException(String mensaje) {
        super("TOKEN_INVALIDO", mensaje);
    }
}
