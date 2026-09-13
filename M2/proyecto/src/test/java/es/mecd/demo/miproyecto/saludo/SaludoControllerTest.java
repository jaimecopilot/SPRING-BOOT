package es.mecd.demo.miproyecto.saludo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Regresión de los endpoints heredados de M0. */
class SaludoControllerTest {
    private final SaludoController controller = new SaludoController();
    @Test void saludarDebeDevolverElMensajeEsperado() {
        assertEquals("Hola, Ministerio de Educación", controller.saludar());
    }
    @Test void despedirDebeDevolverElMensajeEsperado() {
        assertEquals("Adiós, Ministerio de Educación", controller.despedir());
    }
}
