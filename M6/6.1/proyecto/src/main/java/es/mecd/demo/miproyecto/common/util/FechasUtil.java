package es.mecd.demo.miproyecto.common.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Utilidades de presentación de fechas para ejemplos del curso. */
public final class FechasUtil {
    private static final DateTimeFormatter FORMATO_DIA_MES_ANIO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private FechasUtil() { throw new IllegalStateException("Clase de utilidad"); }

    public static String formatear(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(FORMATO_DIA_MES_ANIO);
    }
}
