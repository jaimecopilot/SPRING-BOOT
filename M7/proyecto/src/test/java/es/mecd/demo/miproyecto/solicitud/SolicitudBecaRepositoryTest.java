package es.mecd.demo.miproyecto.solicitud;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.beca.BecaRepository;
import es.mecd.demo.miproyecto.alumno.Curso;
import es.mecd.demo.miproyecto.alumno.CursoRepository;
import es.mecd.demo.miproyecto.documento.Documento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SolicitudBecaRepositoryTest {
@Autowired
private SolicitudBecaRepository solicitudRepository;

@Autowired
private BecaRepository becaRepository;

@Autowired
private AlumnoRepository alumnoRepository;

@Autowired
private CursoRepository cursoRepository;

private Beca beca;
private Alumno alumno;

@BeforeEach
void setUp() {
   Curso curso = cursoRepository.save(new Curso("5º Primaria"));
   alumno = new Alumno("Ana", "García", "12345678A",
              LocalDate.of(2010, 5, 12), curso);
   alumno = alumnoRepository.save(alumno);

   beca = becaRepository.save(new Beca("TEST-2026", "Beca General", "Descripción",
            new BigDecimal("1500.00"), 2026));
}

@Test
void guardarSolicitudConDocumentos_debePersistirLaRelacion() {
    SolicitudBeca solicitud = new SolicitudBeca();
    solicitud.setAlumno(alumno);
    solicitud.setBeca(beca);
    solicitud.setFechaSolicitud(LocalDateTime.now());

    Documento doc = new Documento("DNI.pdf", "application/pdf", 12345L,
            new byte[]{1, 2, 3});
    solicitud.addDocumento(doc);

    SolicitudBeca guardada = solicitudRepository.save(solicitud);

    assertNotNull(guardada.getId());
    assertEquals(1, guardada.getDocumentos().size());
}

@Test
void findByIdConRelaciones_debeCargarAlumnoYBeca() {
    SolicitudBeca solicitud = new SolicitudBeca();
    solicitud.setAlumno(alumno);
    solicitud.setBeca(beca);
    solicitud.setFechaSolicitud(LocalDateTime.now());
    SolicitudBeca guardada = solicitudRepository.save(solicitud);

    SolicitudBeca recuperada = solicitudRepository
            .findByIdConRelaciones(guardada.getId())
            .orElseThrow();

    assertEquals("Ana", recuperada.getAlumno().getNombre());
    assertEquals("Beca General", recuperada.getBeca().getNombre());
}

@Test
void findByEstado_debeDevolverSolicitudesEnEseEstado() {
    SolicitudBeca enviada = new SolicitudBeca();
    enviada.setAlumno(alumno);
    enviada.setBeca(beca);
    enviada.setFechaSolicitud(LocalDateTime.now());
    enviada.setEstado(EstadoSolicitud.ENVIADA);
    solicitudRepository.save(enviada);

    SolicitudBeca borrador = new SolicitudBeca();
    borrador.setAlumno(alumno);
    borrador.setBeca(beca);
    borrador.setFechaSolicitud(LocalDateTime.now());
    borrador.setEstado(EstadoSolicitud.BORRADOR);
    solicitudRepository.save(borrador);

    List<SolicitudBeca> resultado = solicitudRepository
            .findByEstado(EstadoSolicitud.ENVIADA);

    assertEquals(1, resultado.size());
    assertEquals(EstadoSolicitud.ENVIADA, resultado.get(0).getEstado());
}

@Test
void existsByAlumnoIdAndBecaIdAndFechaSolicitudBetween_debeDetectarDuplicado() {
    SolicitudBeca solicitud = new SolicitudBeca();
    solicitud.setAlumno(alumno);
    solicitud.setBeca(beca);
    solicitud.setFechaSolicitud(LocalDateTime.now());
    solicitudRepository.save(solicitud);

    boolean existe = solicitudRepository
            .existsByAlumnoIdAndBecaIdAndFechaSolicitudBetween(
                    alumno.getId(), beca.getId(),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1));

    assertTrue(existe);
}
    @Test
    void buscarConFiltros_debeFiltrarPorEstado() {
        for (int i = 0; i < 3; i++) {
            SolicitudBeca s = new SolicitudBeca();
            s.setAlumno(alumno);
            s.setBeca(beca);
            s.setFechaSolicitud(LocalDateTime.now());
            s.setEstado(i == 0 ? EstadoSolicitud.ENVIADA : EstadoSolicitud.BORRADOR);
            solicitudRepository.save(s);
        }

        Page<SolicitudBeca> resultado = solicitudRepository
                .buscarConFiltros(EstadoSolicitud.ENVIADA, null, null,
                        PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void buscarPorTextoEnObservaciones_debeEncontrarCoincidencias() {
        SolicitudBeca s1 = new SolicitudBeca();
        s1.setAlumno(alumno);
        s1.setBeca(beca);
        s1.setFechaSolicitud(LocalDateTime.now());
        s1.setObservaciones("Falta el DNI del alumno");
        solicitudRepository.save(s1);
    
        SolicitudBeca s2 = new SolicitudBeca();
        s2.setAlumno(alumno);
        s2.setBeca(beca);
        s2.setFechaSolicitud(LocalDateTime.now());
        s2.setObservaciones("Solicitud completa");
        solicitudRepository.save(s2);
    
        List<SolicitudBeca> resultado = solicitudRepository
                .buscarPorTextoEnObservaciones("dni");
    
        assertEquals(1, resultado.size());
        assertEquals("Falta el DNI del alumno", resultado.get(0).getObservaciones());
    }
}
