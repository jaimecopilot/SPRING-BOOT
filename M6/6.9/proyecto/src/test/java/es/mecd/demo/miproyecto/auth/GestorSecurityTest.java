package es.mecd.demo.miproyecto.auth; import static
    org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; import static
    org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; import
    org.junit.jupiter.api.Test; import org.springframework.beans.factory.annotation.Autowired; import
    org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; import
    org.springframework.boot.test.context.SpringBootTest; import
    org.springframework.security.test.context.support.WithMockUser; import
    org.springframework.test.context.ActiveProfiles; import org.springframework.test.web.servlet.MockMvc;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test") class GestorSecurityTest { @Autowired
    MockMvc mockMvc; @Test @WithMockUser(roles="GESTOR") void gestor_200()throws Exception{
    mockMvc.perform(get("/api/v1/gestor/documentos")).andExpect(status().isOk());} @Test
    @WithMockUser(roles="ADMIN") void admin_200()throws Exception{
    mockMvc.perform(get("/api/v1/gestor/documentos")).andExpect(status().isOk());} @Test
    @WithMockUser(roles="USER") void user_403()throws Exception{
    mockMvc.perform(get("/api/v1/gestor/documentos")).andExpect(status().isForbidden());} }
