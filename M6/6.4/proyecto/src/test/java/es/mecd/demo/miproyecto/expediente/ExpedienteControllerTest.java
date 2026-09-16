package es.mecd.demo.miproyecto.expediente;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpedienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpedienteControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ExpedienteService service;

    @Test
    void validaDtoAnidado() throws Exception {
        String json = "{\"titular\":\"T\",\"estado\":\"NUEVO\",\"solicitante\":{"
                + "\"nombre\":\"\",\"apellidos\":\"A\",\"dni\":\"123\"}}";
        mockMvc.perform(post("/api/v1/expedientes")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
