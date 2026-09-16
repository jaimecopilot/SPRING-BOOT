package es.mecd.demo.miproyecto.jwt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class JwtManualTest {
    @Test
    void firmaYDetectaManipulacion() {
        String token = JwtManual.firmar(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}",
                "{\"sub\":\"ana\"}",
                JwtManual.DIDACTIC_SECRET);

        assertTrue(JwtManual.verificar(token, JwtManual.DIDACTIC_SECRET));
        String adulterado = JwtManual.adulterarPayload(token, "{\"sub\":\"admin\"}");
        assertFalse(JwtManual.verificar(adulterado, JwtManual.DIDACTIC_SECRET));
        assertFalse(JwtManual.verificar(token, "otraClave"));
    }

    @Test
    void mainMuestraElFlujoDidacticoCompleto() {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            JwtManual.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String salida = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(salida.contains("JWT generado:"));
        assertTrue(salida.contains("Token válido: true"));
        assertTrue(salida.contains("Token modificado válido: false"));
        assertTrue(salida.contains("Con clave incorrecta: false"));
        assertTrue(salida.contains("Payload decodificado:"));
    }
}
