package es.mecd.demo.miproyecto.common.exception;

import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.TokenRevocationService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.alumno.AlumnoController;
import es.mecd.demo.miproyecto.alumno.AlumnoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AlumnoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ErrorHandlingTest {
    @MockitoBean
    private JwtService m6JwtService;

    @MockitoBean
    private TokenRevocationService m6TokenRevocationService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlumnoService service;

    @Test
    void duplicadoDevuelve409Estructurado() throws Exception {
        when(service.crear(any())).thenThrow(
                new RecursoDuplicadoException(
                        "Alumno", "dni", "12345678A"));
        String body = "{\"nombre\":\"Ana\",\"apellidos\":\"Garcia\"," 
                + "\"dni\":\"12345678A\"," 
                + "\"fechaNacimiento\":\"2010-05-12\"," 
                + "\"curso\":\"5 Primaria\"}";
        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("RECURSO_DUPLICADO"))
                .andExpect(jsonPath("$.traceId").isNotEmpty());
    }

    @Test
    void validacionDevuelveListaDeErrores() throws Exception {
        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACION"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.traceId").isNotEmpty());
    }

    @Test
    void inesperadoDevuelve500SinFiltrarDetalle() throws Exception {
        when(service.listar(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("secreto-tecnico"));
        mockMvc.perform(get("/api/v1/alumnos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.codigo").value("ERROR_INTERNO"))
                .andExpect(jsonPath("$.mensaje")
                        .value("Error interno del servidor"));
    }
}
