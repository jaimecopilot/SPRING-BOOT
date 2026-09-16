package es.mecd.demo.miproyecto.auth;
import static org.mockito.Mockito.when; import static
    org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
    import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; import
    static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List; import org.junit.jupiter.api.Test; import
    org.springframework.beans.factory.annotation.Autowired; import
    org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import
    org.springframework.boot.test.context.SpringBootTest; import
    org.springframework.security.test.context.support.WithMockUser; import
    org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class UsuarioControllerSecurityTest {
    @Autowired MockMvc mockMvc;
 @Test void listar_debeDevolver401_cuandoNoAutenticado()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios")).andExpect(status().isUnauthorized());}
 @Test @WithMockUser(roles="USER") void listar_debeDevolver403_cuandoRolUser()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios")).andExpect(status().isForbidden());}
 @Test @WithMockUser(roles="ADMIN") void listar_debeDevolver200_cuandoRolAdmin()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios")).andExpect(status().isOk());}
 @Test void listar_postprocessorAdmin_200()throws Exception{
     mockMvc.perform(get("/api/v1/admin/usuarios").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());}
 @Test void listar_postprocessorUser_403()throws Exception{mockMvc.perform(get("/api/v1/admin/usuarios")
     .with(user("ana").roles("USER"))).andExpect(status().isForbidden());}
}
