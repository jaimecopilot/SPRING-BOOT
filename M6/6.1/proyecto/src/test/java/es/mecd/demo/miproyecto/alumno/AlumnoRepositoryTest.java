package es.mecd.demo.miproyecto.alumno;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import es.mecd.demo.miproyecto.curso.Curso;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
class AlumnoRepositoryTest {
    @Autowired
    private AlumnoRepository repositorio;

    @Autowired
    private TestEntityManager entityManager;

    private Curso curso;

    @BeforeEach
    void prepararCurso() {
        curso = entityManager.persistFlushFind(new Curso("5º Primaria"));
    }

    @Test
    void guardaYBuscaPorDni() {
        Alumno alumno = repositorio.saveAndFlush(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));

        assertThat(repositorio.findByDni("12345678A")).contains(alumno);
    }

    @Test
    void consultaDerivadaBuscaPorCurso() {
        repositorio.save(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));

        assertThat(
                repositorio.findByCursoNombreIgnoreCaseAndEliminadoFalse(
                        "5º primaria"))
                .hasSize(1);
    }

    @Test
    void buscarDniInexistenteDevuelveOptionalVacio() {
        assertThat(repositorio.findByDni("00000000Z")).isEmpty();
    }

    @Test
    void dniEsUnico() {
        repositorio.saveAndFlush(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));

        assertThatThrownBy(() -> repositorio.saveAndFlush(nuevoAlumno(
                "Luis", "Pérez", "12345678A", 2009, curso)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void queryBuscaPorCursoYNacidosDespues() {
        Curso otro = entityManager.persistFlushFind(new Curso("6º Primaria"));
        repositorio.save(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));
        repositorio.save(nuevoAlumno(
                "Luis", "Pérez", "87654321B", 2008, curso));
        repositorio.save(nuevoAlumno(
                "María", "López", "11111111C", 2011, otro));

        assertThat(repositorio.buscarPorCursoYNacidosDespues(
                "5º Primaria", LocalDate.of(2009, 1, 1)))
                .extracting(Alumno::getNombre)
                .containsExactly("Ana");
    }

    @Test
    void relacionMuchosAUnoSePersiste() {
        Alumno alumno = repositorio.saveAndFlush(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));
        entityManager.clear();

        Alumno recuperado = repositorio.buscarDetalle(alumno.getId())
                .orElseThrow();

        assertThat(recuperado.getCurso().getNombre()).isEqualTo("5º Primaria");
    }

    @Test
    void specificationCombinaFiltros() {
        repositorio.save(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));
        Specification<Alumno> specification = Specification.allOf(
                AlumnoSpecifications.activos(),
                AlumnoSpecifications.curso("5º Primaria"),
                AlumnoSpecifications.estado(EstadoAlumno.ACTIVO),
                AlumnoSpecifications.nacidosEntre(
                        LocalDate.of(2010, 1, 1),
                        LocalDate.of(2010, 12, 31)));

        assertThat(repositorio.findAll(specification)).hasSize(1);
    }

    @Test
    void proyeccionResumenDevuelveDatosDelCurso() {
        repositorio.saveAndFlush(nuevoAlumno(
                "Ana", "García", "12345678A", 2010, curso));

        assertThat(repositorio.buscarResumenPorCurso("5º Primaria"))
                .singleElement()
                .satisfies(resumen -> {
                    assertThat(resumen.nombreCompleto()).isEqualTo("Ana García");
                    assertThat(resumen.curso()).isEqualTo("5º Primaria");
                });
    }

    private Alumno nuevoAlumno(
            String nombre,
            String apellidos,
            String dni,
            int anio,
            Curso cursoAlumno) {
        return new Alumno(
                nombre,
                apellidos,
                dni,
                LocalDate.of(anio, 5, 12),
                cursoAlumno);
    }
}
