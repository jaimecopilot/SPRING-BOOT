package es.mecd.demo.miproyecto.expediente;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.TokenRevocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ExpedienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExpedienteControllerTest {
    @Autowired
    private MockMvc mockMvc;


    /** Colaborador del filtro JWT descubierto por el slice MVC. */
    @MockitoBean
    private JwtService jwtService;

    /** Estado de revocación requerido por JwtAuthenticationFilter. */
    @MockitoBean
    private TokenRevocationService tokenRevocationService;
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
