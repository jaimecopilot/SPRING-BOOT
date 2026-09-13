package es.mecd.demo.miproyecto.alumno;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
class AlumnoServiceTest {
    AlumnoRepository r;
    DocumentoRepository d;
    AlumnoService s;
    @BeforeEach void setUp() {
        r=new AlumnoRepository();
        d=new DocumentoRepository();
        s=new AlumnoService(r,d);
        r.guardar(new AlumnoDTO("10","Ana","García","12345678A",LocalDate.of(2010,1,1),"5º"));
    }
    @Test void filtraYPagina() {
        assertEquals(1,s.listar("5º",null,"nombre",null,null,0,20).size());
    }
    @Test void duplicado() {
        AlumnoRequestDTO q=req("12345678A");
        assertThrows(RuntimeException.class,()->s.crear(q));
    }
    @Test void crea() {
        assertTrue(s.crear(req("11111111C")).isActivo());
    }
    @Test void rango() {
        assertEquals(1,s.contar(null,null,LocalDate.of(2009,1,1),LocalDate.of(2010,12,31)));
    }
    private AlumnoRequestDTO req(String dni) {
        AlumnoRequestDTO q=new AlumnoRequestDTO();
        q.setNombre("M");
        q.setApellidos("L");
        q.setDni(dni);
        q.setFechaNacimiento(LocalDate.of(2011,1,1));
        q.setCurso("4º");
        return q;
    }
}
