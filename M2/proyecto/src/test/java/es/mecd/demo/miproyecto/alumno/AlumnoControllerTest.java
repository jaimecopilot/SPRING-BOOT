package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.exception.NegocioException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Slice tests del controlador de alumnos. */
@WebMvcTest(AlumnoController.class)
class AlumnoControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private AlumnoService service;

    @Test void consultarDevuelve200() throws Exception {
        when(service.consultar("1")).thenReturn(Optional.of(
                new AlumnoDTO("1", "Ana", "García", "DNI-1", LocalDate.of(2010, 1, 1), "5º")));
        mockMvc.perform(get("/api/v1/alumnos/1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value("1"));
    }

    @Test void crearDevuelve201YLocation() throws Exception {
        when(service.crear(any(AlumnoDTO.class))).thenReturn(
                new AlumnoDTO("3", "Carlos", "Zeta", "DNI-3", LocalDate.of(2011, 1, 2), "5º"));
        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content("{\"nombre\":\"Carlos\",\"apellidos\":\"Zeta\",\"dni\":\"DNI-3\",\"fechaNacimiento\":\"2011-01-02\",\"curso\":\"5º\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/alumnos/3"))
                .andExpect(jsonPath("$.id").value("3"));
    }

    @Test void dniDuplicadoDevuelve409() throws Exception {
        when(service.crear(any(AlumnoDTO.class))).thenThrow(new NegocioException("DNI duplicado"));
        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content("{\"nombre\":\"Ana\",\"dni\":\"DNI-1\"}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    }
}
