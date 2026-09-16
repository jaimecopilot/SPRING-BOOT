package es.mecd.demo.miproyecto.solicitud;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.beca.BecaRepository;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudBecaRepository solicitudRepository;
    @Mock
    private AlumnoRepository alumnoRepository;
    @Mock
    private BecaRepository becaRepository;

    private SolicitudService service;

    @BeforeEach
    void setUp() {
        service = new SolicitudService(solicitudRepository, alumnoRepository, becaRepository);
    }

    @Test
    void consultar_debeLanzarRecursoNoEncontrado_cuandoNoExiste() {
        when(solicitudRepository.findByIdConRelaciones(999L))
            .thenReturn(Optional.empty());

    assertThrows(RecursoNoEncontradoException.class,
            () -> service.consultar(999L));
}

@Test
void crear_debeLanzarRecursoNoEncontrado_cuandoAlumnoNoExiste() {
    SolicitudRequestDTO request = new SolicitudRequestDTO();
    request.setAlumnoId(1L);
    request.setBecaId(1L);

    when(alumnoRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(RecursoNoEncontradoException.class,
            () -> service.crear(request));
}

@Test
void crear_debeLanzarRecursoDuplicado_cuandoYaExisteSolicitud() {
    Alumno alumno = mock(Alumno.class);
    when(alumno.getId()).thenReturn(1L);
    Beca beca = new Beca("TEST-2026", "Beca General", "Desc",
            new BigDecimal("1500.00"), 2026);
        beca.setId(1L);

        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setAlumnoId(1L);
        request.setBecaId(1L);

        when(alumnoRepository.findById(1L)).thenReturn(Optional.of(alumno));
        when(becaRepository.findById(1L)).thenReturn(Optional.of(beca));
        when(solicitudRepository.existsByAlumnoIdAndBecaIdAndFechaSolicitudBetween(
                eq(1L), eq(1L), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(true);

        assertThrows(RecursoDuplicadoException.class,
                () -> service.crear(request));
    }

    @Test
    void buscarUltimaDeAlumno_debeDevolverVacio_cuandoAlumnoSinSolicitudes() {
        when(alumnoRepository.existsById(1L)).thenReturn(true);
        when(solicitudRepository.findFirstByAlumnoIdOrderByFechaSolicitudDesc(1L))
                .thenReturn(Optional.empty());
    
        Optional<SolicitudResponseDTO> resultado = service.buscarUltimaDeAlumno(1L);
    
        assertTrue(resultado.isEmpty());
    }
}
