package es.mecd.demo.miproyecto.auth;
import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.auth.TokenRevocationService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; import
    org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean; import
    org.springframework.test.web.servlet.MockMvc;
import es.mecd.demo.miproyecto.config.SecurityConfig;
@WebMvcTest(UsuarioController.class) @Import(SecurityConfig.class)
class UsuarioControllerTest {
    @MockitoBean
    private JwtService m6JwtService;

    @MockitoBean
    private TokenRevocationService m6TokenRevocationService;

 @Autowired MockMvc mockMvc; @MockitoBean UsuarioService usuarioService;
 @Test void listar_debeDevolver403_cuandoNoEsAdmin()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios").with(user("ana").roles("USER"))).andExpect(status().isForbidden());}
 @Test void listar_debeDevolver200_cuandoEsAdmin()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());}
}
