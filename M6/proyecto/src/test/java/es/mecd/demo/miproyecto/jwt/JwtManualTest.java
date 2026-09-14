package es.mecd.demo.miproyecto.jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Verificaciones puramente criptográficas/didácticas de 6.5. */
class JwtManualTest {

    @Test
    void tokenIntactoDebeTenerFirmaValida() throws Exception {
        String jwt = JwtManual.generar(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}",
                "{\"sub\":\"ana\"}",
                JwtManual.DIDACTIC_SECRET);

        assertTrue(JwtManual.verificar(
                jwt,
                JwtManual.DIDACTIC_SECRET));
    }

    @Test
    void tokenModificadoDebeInvalidarFirma() throws Exception {
        String jwt = JwtManual.generar(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}",
                "{\"sub\":\"ana\"}",
                JwtManual.DIDACTIC_SECRET);

        String[] partes = jwt.split("\\.");
        String payloadModificado =
                JwtManual.base64UrlEncode(
                        "{\"sub\":\"admin\"}");

        String manipulado =
                partes[0] + "."
                + payloadModificado + "."
                + partes[2];

        assertFalse(JwtManual.verificar(
                manipulado,
                JwtManual.DIDACTIC_SECRET));
    }

    @Test
    void claveIncorrectaDebeInvalidarFirma() throws Exception {
        String jwt = JwtManual.generar(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}",
                "{\"sub\":\"ana\"}",
                JwtManual.DIDACTIC_SECRET);

        assertFalse(JwtManual.verificar(
                jwt,
                "otraClaveQueNoEsLaOriginal"));
    }
}
