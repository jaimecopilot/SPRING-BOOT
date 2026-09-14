package es.mecd.demo.miproyecto.fichero;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FicheroController.class)
class FicheroExceptionHandlerTest {
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
