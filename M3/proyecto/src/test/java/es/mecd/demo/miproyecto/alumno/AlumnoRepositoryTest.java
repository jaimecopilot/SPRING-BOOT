package es.mecd.demo.miproyecto.alumno;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlumnoRepositoryTest {
    @Test
    void devuelveCopiasDefensivas() {
        AlumnoRepository repo = new AlumnoRepository();
        repo.guardar(new AlumnoDTO(
                "1", "Ana", "García", "12345678A", LocalDate.of(2010, 1, 1), "5º"));
        var lista = repo.listarTodos();
        assertThrows(UnsupportedOperationException.class, lista::clear);
        lista.get(0).setNombre("Cambio externo");
        assertEquals("Ana", repo.buscarPorId("1").orElseThrow().getNombre());
    }
}
