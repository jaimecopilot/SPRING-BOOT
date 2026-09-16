package es.mecd.demo.miproyecto.beca;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.auth.JwtAccessDeniedHandler;
import es.mecd.demo.miproyecto.auth.JwtAuthenticationEntryPoint;
import es.mecd.demo.miproyecto.auth.JwtAuthenticationFilter;
import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.SecurityErrorWriter;
import es.mecd.demo.miproyecto.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BecaController.class)
@Import({SecurityConfig.class, SecurityErrorWriter.class, JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class, JwtAuthenticationFilter.class})
class BecaControllerSecurityTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private BecaService service;
    @MockitoBean private JwtService jwt;

    @Test
    void listarEsPublico() throws Exception {
        mockMvc.perform(get("/api/v1/becas")).andExpect(status().isOk());
    }

    @Test
    void ciudadanoNoPuedeCrearBeca() throws Exception {
        mockMvc.perform(post("/api/v1/becas").with(user("c").roles("CIUDADANO"))
                        .contentType("application/json")
                        .content("{\"codigo\":\"TEST-2026\",\"nombre\":\"Beca\",\"importeMaximo\":1000.00,\"anio\":2026}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPuedeCrearBeca() throws Exception {
        BecaResponseDTO creada = new BecaResponseDTO();
        creada.setIdentificador("1");
        when(service.crear(any())).thenReturn(creada);
        mockMvc.perform(post("/api/v1/becas").with(user("a").roles("ADMIN"))
                        .contentType("application/json")
                        .content("{\"codigo\":\"TEST-2026\",\"nombre\":\"Beca\",\"importeMaximo\":1000.00,\"anio\":2026}"))
                .andExpect(status().isCreated());
    }
}
