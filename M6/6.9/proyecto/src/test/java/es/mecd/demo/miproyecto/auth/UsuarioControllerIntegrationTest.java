package es.mecd.demo.miproyecto.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarios;

    @Autowired
    private RolRepository roles;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        if (usuarios.findByUsername("test").isEmpty()) {
            Rol rol = roles.findByNombre("ADMIN")
                    .orElseGet(() -> roles.save(new Rol("ADMIN", "Admin")));
            Usuario usuario = new Usuario();
            usuario.setUsername("test");
            usuario.setPassword(encoder.encode("test123"));
            usuario.setEmail("test@educacion.gob.es");
            usuario.setActivo(true);
            usuario.getRoles().add(rol);
            usuarios.save(usuario);
        }
    }

    @Test
    @WithUserDetails(value = "test", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void listar_debeDevolver200_conUsuarioBD() throws Exception {
        mockMvc.perform(get("/api/v1/admin/usuarios"))
                .andExpect(status().isOk());
    }
}
