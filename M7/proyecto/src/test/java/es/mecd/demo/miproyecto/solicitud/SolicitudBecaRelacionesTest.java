package es.mecd.demo.miproyecto.solicitud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.Curso;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.documento.Documento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class SolicitudBecaRelacionesTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void guardarSolicitudConDocumentos_debePersistirLaRelacion() {
        int anio = java.time.Year.now().getValue();
        Curso curso = entityManager.persistAndFlush(new Curso("5º Primaria"));
        Alumno alumno = entityManager.persistAndFlush(
                new Alumno("Ana", "García", "12345678A", LocalDate.of(2010, 5, 12), curso));
        Beca beca = entityManager.persistAndFlush(
                new Beca("TEST-" + anio, "Beca General", "Descripción",
                new BigDecimal("1500.00"), anio));

        SolicitudBeca solicitud = new SolicitudBeca();
        solicitud.setAlumno(alumno);
        solicitud.setBeca(beca);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.addDocumento(new Documento("DNI.pdf", "application/pdf", 3L, new byte[]{1, 2, 3}));
        solicitud.addDocumento(new Documento("Notas.pdf", "application/pdf", 3L, new byte[]{4, 5, 6}));

        entityManager.persistAndFlush(solicitud);
        Long id = solicitud.getId();
        entityManager.clear();

        SolicitudBeca recuperada = entityManager.find(SolicitudBeca.class, id);
        assertNotNull(recuperada);
        assertEquals(2, recuperada.getDocumentos().size());
        assertEquals("Beca General", recuperada.getBeca().getNombre());
        assertEquals("Ana", recuperada.getAlumno().getNombre());
    }
}
