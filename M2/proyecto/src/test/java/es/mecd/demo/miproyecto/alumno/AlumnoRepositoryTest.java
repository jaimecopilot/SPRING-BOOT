package es.mecd.demo.miproyecto.alumno;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/** Tests del aislamiento y secuencia del repositorio en memoria. */
class AlumnoRepositoryTest {
    @Test
    void listarYConsultarDevuelvenCopiasDefensivas() {
        AlumnoRepository repo = new AlumnoRepository();
        repo.guardar(new AlumnoDTO("1", "Ana", "García", "DNI-1", LocalDate.of(2010, 1, 1), "5º"));
        var lista = repo.listarTodos();
        lista.clear();
        assertEquals(1, repo.contar());
        AlumnoDTO obtenido = repo.buscarPorId("1").orElseThrow();
        obtenido.setNombre("CAMBIO EXTERNO");
        assertEquals("Ana", repo.buscarPorId("1").orElseThrow().getNombre());
    }

    @Test
    void secuenciaNoReutilizaIdentificadoresBorrados() {
        AlumnoRepository repo = new AlumnoRepository();
        String uno = repo.siguienteIdentificador();
        repo.guardar(new AlumnoDTO(uno, "A", "A", "DNI-A", LocalDate.now(), "1"));
        String dos = repo.siguienteIdentificador();
        repo.guardar(new AlumnoDTO(dos, "B", "B", "DNI-B", LocalDate.now(), "1"));
        repo.eliminar(uno);
        assertNotEquals(dos, repo.siguienteIdentificador());
    }
}
