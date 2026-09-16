package es.mecd.demo.miproyecto.saludo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SaludoControllerTest {
    @Test void saludos() {
        SaludoController c=new SaludoController();
        assertEquals("Hola, Ministerio de Educación",c.hola());
        assertEquals("Adiós, Ministerio de Educación",c.adios());
    }
}
