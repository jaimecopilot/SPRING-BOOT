package es.mecd.demo.miproyecto.alumno;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import es.mecd.demo.miproyecto.auditoria.AuditoriaService;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.curso.Curso;
import es.mecd.demo.miproyecto.curso.CursoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.util.ReflectionTestUtils;

/** Regresión del servicio M3 adaptada al repositorio JPA de M4. */
@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {
    @Mock
    private AlumnoRepository repositorio;
    @Mock
    private CursoRepository cursos;
    @Mock
    private DocumentoRepository documentos;
    @Mock
    private AuditoriaService auditoria;

    private AlumnoService service;

    @BeforeEach
    void setUp() {
        service = new AlumnoService(repositorio, cursos, documentos, auditoria);
    }

    @Test
    void filtraYPagina() {
        Curso curso = cursoConId("5º", 1L);
        Alumno alumno = alumnoConId("Ana", "García", "12345678A", curso, 10L);
        when(repositorio.findAll(any(org.springframework.data.jpa.domain.Specification.class),
                any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(alumno)));

        assertEquals(1, service.listar("5º", null, "nombre", null, null, 0, 20)
                .getTotalElements());
    }

    @Test
    void duplicado() {
        when(repositorio.existsByDni("12345678A")).thenReturn(true);
        assertThrows(
                RecursoDuplicadoException.class,
                () -> service.crear(request("12345678A")));
    }

    @Test
    void crea() {
        Curso curso = cursoConId("4º", 2L);
        when(repositorio.existsByDni("11111111C")).thenReturn(false);
        when(cursos.findByNombreIgnoreCase("4º")).thenReturn(Optional.of(curso));
        when(repositorio.save(any(Alumno.class))).thenAnswer(invocation -> {
            Alumno alumno = invocation.getArgument(0);
            ReflectionTestUtils.setField(alumno, "id", 20L);
            return alumno;
        });

        assertTrue(service.crear(request("11111111C")).isActivo());
    }

    private AlumnoRequestDTO request(String dni) {
        AlumnoRequestDTO request = new AlumnoRequestDTO();
        request.setNombre("M");
        request.setApellidos("L");
        request.setDni(dni);
        request.setFechaNacimiento(LocalDate.of(2011, 1, 1));
        request.setCurso("4º");
        return request;
    }

    private Curso cursoConId(String nombre, long id) {
        Curso curso = new Curso(nombre);
        ReflectionTestUtils.setField(curso, "id", id);
        return curso;
    }

    private Alumno alumnoConId(
            String nombre,
            String apellidos,
            String dni,
            Curso curso,
            long id) {
        Alumno alumno = new Alumno(
                nombre,
                apellidos,
                dni,
                LocalDate.of(2010, 1, 1),
                curso);
        ReflectionTestUtils.setField(alumno, "id", id);
        return alumno;
    }
}
