package es.mecd.demo.miproyecto.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class AplicacionExceptionTest {
    @Test
    void conservaCodigoYDatosDeDuplicado() {
        RecursoDuplicadoException ex = new RecursoDuplicadoException(
                "Alumno", "dni", "12345678A");
        assertEquals("RECURSO_DUPLICADO", ex.getCodigo());
        assertEquals("dni", ex.getCampo());
        assertEquals("12345678A", ex.getValor());
    }

    @Test
    void errorTecnicoConservaLaCausa() {
        RuntimeException causa = new RuntimeException("fallo interno");
        ErrorTecnicoException ex = new ErrorTecnicoException(
                "No se pudo acceder a la base de datos", causa);
        assertEquals("ERROR_TECNICO", ex.getCodigo());
        assertSame(causa, ex.getCause());
    }
}
