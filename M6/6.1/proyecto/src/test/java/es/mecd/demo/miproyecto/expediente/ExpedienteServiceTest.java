package es.mecd.demo.miproyecto.expediente;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ExpedienteServiceTest {
    @Test
    void ejemploMantieneContratoHeredado() {
        ExpedienteService service = new ExpedienteService(new ExpedienteRepository());
        ExpedienteDTO ejemplo = service.ejemplo();
        assertEquals("12345", ejemplo.getIdentificador());
        assertEquals(LocalDate.of(2025, 1, 15), ejemplo.getFechaSolicitud());
    }

    @Test
    void cambioEstadoInexistenteNoCrea() {
        ExpedienteService service = new ExpedienteService(new ExpedienteRepository());
        assertFalse(service.cambiarEstado("404", "RESUELTO").isPresent());
    }
}
