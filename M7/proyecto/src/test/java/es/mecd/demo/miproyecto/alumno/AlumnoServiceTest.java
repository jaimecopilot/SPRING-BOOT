package es.mecd.demo.miproyecto.alumno;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {
    @Mock AlumnoRepository alumnos; @Mock CursoRepository cursos; AlumnoService service;
    @BeforeEach void setUp() { service = new AlumnoService(alumnos, cursos); }
    @Test void crearDebeCrearCursoSiNoExiste() {
        AlumnoRequestDTO dto = new AlumnoRequestDTO(); dto.setNombre("Ana"); dto.setApellidos("García");
        dto.setDni("12345678A"); dto.setFechaNacimiento(LocalDate.of(2010,5,12)); dto.setCurso("5º Primaria");
        Curso curso = new Curso("5º Primaria"); when(cursos.findByNombre("5º Primaria")).thenReturn(Optional.of(curso));
        when(alumnos.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));
        assertEquals("Ana", service.crear(dto).getNombre());
    }
}
