package es.mecd.demo.miproyecto.auth; import static
    org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; import static
    org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; import
    org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import
    org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import
    org.springframework.boot.test.context.SpringBootTest; import
    org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class JwtFilterIntegrationTest {
    @Autowired MockMvc mockMvc; @Autowired JwtService jwt; @Autowired UsuarioRepository usuarios;
 @Test void perfil_200_tokenValido()throws Exception{Usuario ana=usuarios.findByUsername("ana")
     .orElseThrow();String t=jwt.generarToken(ana);mockMvc.perform(get("/api/v1/perfil")
     .header("Authorization","Bearer "+t)).andExpect(status().isOk()).andExpect(jsonPath("$.username").value("ana"));}
 @Test void perfil_401_tokenInvalido()throws Exception{mockMvc.perform(get("/api/v1/perfil")
     .header("Authorization","Bearer tokenInvalido")).andExpect(status().isUnauthorized())
     .andExpect(jsonPath("$.codigo").value("NO_AUTENTICADO"));}
 @Test void perfil_401_sinToken()throws Exception{mockMvc.perform(get("/api/v1/perfil"))
     .andExpect(status().isUnauthorized());}
}
