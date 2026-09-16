package es.mecd.demo.miproyecto.alumno;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Regresión del contrato HTTP heredado de M3 sobre la implementación JPA de M4. */
@WebMvcTest(AlumnoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlumnoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlumnoService service;

    @Test
    void listarDevuelveMetadatos() throws Exception {
        when(service.listar(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/alumnos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void crearConDatosInvalidosDevuelve400() throws Exception {
        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content("{\"nombre\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearDevuelve201YLocation() throws Exception {
        AlumnoResponseDTO response = new AlumnoResponseDTO();
        response.setIdentificador("3");
        response.setNombre("María");
        response.setActivo(true);
        when(service.crear(any())).thenReturn(response);

        String json = "{\"nombre\":\"María\",\"apellidos\":\"López\"," 
                + "\"dni\":\"11111111C\",\"fechaNacimiento\":\"2011-03-20\"," 
                + "\"curso\":\"4º\"}";

        mockMvc.perform(post("/api/v1/alumnos")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
