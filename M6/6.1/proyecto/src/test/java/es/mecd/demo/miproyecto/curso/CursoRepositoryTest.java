package es.mecd.demo.miproyecto.curso;

import static org.assertj.core.api.Assertions.assertThat;

import es.mecd.demo.miproyecto.alumno.Alumno;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class CursoRepositoryTest {
    @Autowired
    private CursoRepository repositorio;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void buscaPorNombreSinDistinguirMayusculas() {
        repositorio.save(new Curso("Primero"));

        assertThat(repositorio.findByNombreIgnoreCase("PRIMERO")).isPresent();
    }

    @Test
    void joinFetchCargaAlumnosDelCurso() {
        Curso curso = repositorio.saveAndFlush(new Curso("5º Primaria"));
        entityManager.persistAndFlush(new Alumno(
                "Ana",
                "García",
                "12345678A",
                LocalDate.of(2010, 5, 12),
                curso));
        entityManager.clear();

        assertThat(repositorio.findAllConAlumnos())
                .singleElement()
                .satisfies(c -> assertThat(c.getAlumnos()).hasSize(1));
    }
}
