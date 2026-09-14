package es.mecd.demo.miproyecto.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.alumno.AlumnoController;
import es.mecd.demo.miproyecto.alumno.AlumnoService;
import java.util.List;
import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.TokenRevocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AlumnoController.class)
@Import(CorsConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class CorsConfigTest {
    @Autowired
    private MockMvc mockMvc;


    /** Colaborador del filtro JWT descubierto por el slice MVC. */
    @MockitoBean
    private JwtService jwtService;

    /** Estado de revocación requerido por JwtAuthenticationFilter. */
    @MockitoBean
    private TokenRevocationService tokenRevocationService;
    @MockitoBean
    private AlumnoService service;

    @Test
    void origenPermitidoRecibeCabeceraCors() throws Exception {
        when(service.listar(any(), any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(new PageImpl<>(List.of()));
        mockMvc.perform(get("/api/v1/alumnos")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin",
                        "http://localhost:3000"));
    }

    @Test
    void preflightAutorizaPost() throws Exception {
        mockMvc.perform(options("/api/v1/alumnos")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin",
                        "http://localhost:3000"));
    }
}
