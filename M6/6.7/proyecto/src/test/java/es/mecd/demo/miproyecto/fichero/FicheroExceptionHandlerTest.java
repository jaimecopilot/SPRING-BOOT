package es.mecd.demo.miproyecto.fichero;

import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.TokenRevocationService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FicheroController.class)
@AutoConfigureMockMvc(addFilters = false)
class FicheroExceptionHandlerTest {
    @MockitoBean
    private JwtService m6JwtService;

    @MockitoBean
    private TokenRevocationService m6TokenRevocationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adviceSelectivoDevuelve404Estructurado() throws Exception {
        mockMvc.perform(get("/api/v1/ficheros/manual.pdf"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.codigo")
                        .value("FICHERO_NO_ENCONTRADO"))
                .andExpect(jsonPath("$.traceId").isNotEmpty());
    }
}
