package es.mecd.demo.miproyecto.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SaludoControllerTest {

    private final SaludoController controller = new SaludoController();

    @Test
    void saludarDebeDevolverElMensajeEsperado() {
        assertThat(controller.saludar())
                .isEqualTo("Hola, Ministerio de Educación");
    }

    @Test
    void despedirDebeDevolverElMensajeEsperado() {
        assertThat(controller.despedir())
                .isEqualTo("Adiós, Ministerio de Educación");
    }
}
