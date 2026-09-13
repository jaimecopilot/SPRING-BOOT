package es.mecd.demo.miproyecto.expediente;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/** Tests unitarios del servicio de expedientes. */
@ExtendWith(MockitoExtension.class)
class ExpedienteServiceTest {
    @Mock private ExpedienteRepository repositorio;
    private ExpedienteService service;
    @BeforeEach void setUp() { service = new ExpedienteService(repositorio); }

    @Test void consultarDevuelveExpediente() {
        ExpedienteDTO dto = new ExpedienteDTO(
                "1", "Ana", "12345678A", "EN_TRAMITE", "BECA",
                LocalDate.of(2025, 1, 15), 100.0, true, null);
        when(repositorio.buscarPorId("1")).thenReturn(Optional.of(dto));
        assertEquals("1", service.consultar("1").orElseThrow().getIdentificador());
    }

    @Test void consultarAusenteDevuelveOptionalVacio() {
        when(repositorio.buscarPorId("999")).thenReturn(Optional.empty());
        assertTrue(service.consultar("999").isEmpty());
    }

    @Test void crearAsignaId() {
        ExpedienteDTO dto = new ExpedienteDTO();
        when(repositorio.siguienteIdentificador()).thenReturn("3");
        when(repositorio.guardar(any(ExpedienteDTO.class))).thenAnswer(i -> i.getArgument(0));
        assertEquals("3", service.crear(dto).getIdentificador());
    }
}
