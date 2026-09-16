package es.mecd.demo.miproyecto.auth;
import static org.mockito.ArgumentMatchers.any; import static org.mockito.Mockito.when; import static
    org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; import static
    org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import
    org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; import
    org.springframework.http.MediaType; import
    org.springframework.test.context.bean.override.mockito.MockitoBean; import
    org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(AuthController.class) @AutoConfigureMockMvc(addFilters=false) class
    AuthControllerSecurityTest {
    @Autowired MockMvc mockMvc;
    @MockitoBean AuthService authService;
    @MockitoBean JwtService m6JwtService;
    @MockitoBean TokenRevocationService m6TokenRevocationService;
 @Test void login_200()throws Exception{when(authService.login(any())).thenReturn(new
     LoginResponseDTO("token","refresh",3600));mockMvc.perform(post("/api/v1/auth/login")
     .contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"ana\",\"password\":\"ana123\"}"))
     .andExpect(status().isOk()).andExpect(jsonPath("$.access_token").value("token"))
     .andExpect(jsonPath("$.token_type").value("Bearer"));}
 @Test void login_401()throws Exception{when(authService.login(any())).thenThrow(new
     CredencialesInvalidasException());mockMvc.perform(post("/api/v1/auth/login")
     .contentType(MediaType.APPLICATION_JSON)
     .content("{\"username\":\"ana\",\"password\":\"incorrecta\"}")).andExpect(status().isUnauthorized()
     ).andExpect(jsonPath("$.codigo").value("CREDENCIALES_INVALIDAS"));}
 @Test void refresh_200()throws Exception{when(authService.refresh("refreshValido")).thenReturn(new
     LoginResponseDTO("nuevoToken","nuevoRefresh",3600));mockMvc.perform(post("/api/v1/auth/refresh")
     .contentType(MediaType.APPLICATION_JSON).content("{\"refresh_token\":\"refreshValido\"}"))
     .andExpect(status().isOk()).andExpect(jsonPath("$.access_token").value("nuevoToken"));}
 @Test void refresh_401()throws Exception{when(authService.refresh("refreshInvalido")).thenThrow(new
     TokenInvalidoException("Refresh token inválido o expirado"));
     mockMvc.perform(post("/api/v1/auth/refresh").contentType(MediaType.APPLICATION_JSON)
     .content("{\"refresh_token\":\"refreshInvalido\"}")).andExpect(status().isUnauthorized())
     .andExpect(jsonPath("$.codigo").value("TOKEN_INVALIDO"));}
 @Test void logout_204()throws Exception{mockMvc.perform(post("/api/v1/auth/logout")
     .header("Authorization","Bearer tokenValido")).andExpect(status().isNoContent());}
}
