package es.mecd.demo.miproyecto.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Tests de la utilidad de fechas. */
class FechasUtilTest {
    @Test void formatearDevuelveDiaMesAnio() {
        assertEquals("15/01/2025", FechasUtil.formatear(LocalDate.of(2025, 1, 15)));
    }
    @Test void formatearNullDevuelveCadenaVacia() { assertEquals("", FechasUtil.formatear(null)); }
}
