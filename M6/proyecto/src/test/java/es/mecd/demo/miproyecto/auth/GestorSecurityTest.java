package es.mecd.demo.miproyecto.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GestorSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "GESTOR")
    void documentos_debeDevolver200_cuandoGestor() throws Exception {
        mockMvc.perform(get("/api/v1/gestor/documentos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void documentos_debeDevolver200_cuandoAdmin() throws Exception {
        mockMvc.perform(get("/api/v1/gestor/documentos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void documentos_debeDevolver403_cuandoUser() throws Exception {
        mockMvc.perform(get("/api/v1/gestor/documentos"))
                .andExpect(status().isForbidden());
    }
}
