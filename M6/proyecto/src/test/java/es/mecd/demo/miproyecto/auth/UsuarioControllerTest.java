package es.mecd.demo.miproyecto.auth;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Slice MVC del punto 6.4: separa autorización del mecanismo JWT. */
@WebMvcTest(UsuarioController.class)
@Import(UsuarioControllerTest.TestSecurityConfig.class)
class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;


    /** Colaborador del filtro JWT descubierto por el slice MVC. */
    @MockitoBean
    private JwtService jwtService;

    /** Estado de revocación requerido por JwtAuthenticationFilter. */
    @MockitoBean
    private TokenRevocationService tokenRevocationService;
    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void listar_debeDevolver403_cuandoNoEsAdmin()
            throws Exception {
        mockMvc.perform(get("/api/v1/admin/usuarios")
                .with(user("ana").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listar_debeDevolver200_cuandoEsAdmin()
            throws Exception {
        mockMvc.perform(get("/api/v1/admin/usuarios")
                .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void listar_debeDevolver401_cuandoNoAutenticado()
            throws Exception {
        mockMvc.perform(get("/api/v1/admin/usuarios"))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    @EnableMethodSecurity
    static class TestSecurityConfig {
        @Bean
        SecurityFilterChain testSecurityFilterChain(
                HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/v1/admin/**")
                            .hasRole("ADMIN")
                            .anyRequest()
                            .authenticated())
                    .exceptionHandling(ex -> ex
                            .authenticationEntryPoint((request, response, cause) ->
                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED)));
            return http.build();
        }
    }
}
