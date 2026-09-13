package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.exception.NegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Tests unitarios de la capa de servicio de alumnos. */
@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {
    @Mock private AlumnoRepository repositorio;
    private AlumnoService service;

    @BeforeEach void setUp() { service = new AlumnoService(repositorio); }

    @Test void consultaExitosa() {
        AlumnoDTO ana = alumno("1", "DNI-1");
        when(repositorio.buscarPorId("1")).thenReturn(Optional.of(ana));
        assertEquals("1", service.consultar("1").orElseThrow().getIdentificador());
    }

    @Test void consultaNoEncontrada() {
        when(repositorio.buscarPorId("999")).thenReturn(Optional.empty());
        assertTrue(service.consultar("999").isEmpty());
    }

    @Test void crearRechazaDniDuplicado() {
        when(repositorio.existePorDni("DNI-1")).thenReturn(true);
        assertThrows(NegocioException.class, () -> service.crear(alumno(null, "DNI-1")));
    }

    @Test void crearAsignaIdYGuarda() {
        AlumnoDTO nuevo = alumno(null, "DNI-3");
        when(repositorio.existePorDni("DNI-3")).thenReturn(false);
        when(repositorio.siguienteIdentificador()).thenReturn("3");
        when(repositorio.guardar(any(AlumnoDTO.class))).thenAnswer(i -> i.getArgument(0));
        assertEquals("3", service.crear(nuevo).getIdentificador());
        verify(repositorio).guardar(nuevo);
    }

    private AlumnoDTO alumno(String id, String dni) {
        return new AlumnoDTO(id, "Ana", "García", dni, LocalDate.of(2010, 1, 1), "5º");
    }
}
