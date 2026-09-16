package es.mecd.demo.miproyecto.solicitud;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SolicitudController.class)
@Import({SecurityConfig.class, SecurityErrorWriter.class, JwtAuthenticationEntryPoint.class,
        JwtAccessDeniedHandler.class, JwtAuthenticationFilter.class})
class SolicitudSecurityTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private SolicitudService service;
    @MockitoBean private JwtService jwt;

    @Test
    void sinAutenticacionDebeDar401() throws Exception {
        mockMvc.perform(get("/api/v1/solicitudes")).andExpect(status().isUnauthorized());
    }

    @Test
    void ciudadanoPuedeListar() throws Exception {
        when(service.listar(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(java.util.List.of()));
        mockMvc.perform(get("/api/v1/solicitudes").with(user("c").roles("CIUDADANO")))
                .andExpect(status().isOk());
    }

    @Test
    void ciudadanoPuedeCrear() throws Exception {
        SolicitudResponseDTO creada = new SolicitudResponseDTO();
        creada.setIdentificador("1");
        when(service.crear(any())).thenReturn(creada);
        mockMvc.perform(post("/api/v1/solicitudes").with(user("c").roles("CIUDADANO"))
                        .contentType("application/json")
                        .content("{\"alumnoId\":1,\"becaId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void gestorNoPuedeCrear() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes").with(user("g").roles("GESTOR"))
                        .contentType("application/json")
                        .content("{\"alumnoId\":1,\"becaId\":1}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void consultorPuedeListar() throws Exception {
        when(service.listar(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(java.util.List.of()));
        mockMvc.perform(get("/api/v1/solicitudes").with(user("c").roles("CONSULTOR")))
                .andExpect(status().isOk());
    }

    @Test
    void consultorNoPuedeCambiarEstado() throws Exception {
        mockMvc.perform(patch("/api/v1/solicitudes/1/estado").with(user("c").roles("CONSULTOR"))
                        .contentType("application/json").content("{\"estado\":\"EN_REVISION\"}"))
                .andExpect(status().isForbidden());
    }
}
