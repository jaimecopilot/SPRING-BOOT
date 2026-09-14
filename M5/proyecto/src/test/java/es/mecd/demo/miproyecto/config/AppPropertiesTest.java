package es.mecd.demo.miproyecto.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AppPropertiesTest {
    @Autowired
    private AppProperties properties;

    @Test
    void cargaElPerfilTest() {
        assertEquals("Oficina de Pruebas", properties.getNombreOficina());
        assertEquals("5.0.0", properties.getVersion());
        assertEquals("test", properties.getEntorno());
    }
}
