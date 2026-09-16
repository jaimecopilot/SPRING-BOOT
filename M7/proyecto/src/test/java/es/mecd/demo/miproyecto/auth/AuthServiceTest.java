package es.mecd.demo.miproyecto.auth;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock AuthenticationManager manager; @Mock JwtService jwt; @Mock UsuarioRepository usuarios;
    @Mock RolRepository roles; @Mock PasswordEncoder encoder; AuthService service;
    @BeforeEach void setUp() { service = new AuthService(manager, jwt, usuarios, roles, encoder); }
    @Test void registrarDebeAsignarCiudadanoCuandoExiste() {
        Rol ciudadano = new Rol("CIUDADANO", "Ciudadano"); when(roles.findByNombre("CIUDADANO"))
                .thenReturn(Optional.of(ciudadano)); when(encoder.encode("nuevo12345")).thenReturn("hash");
        when(usuarios.save(any())).thenAnswer(i -> { Usuario u=i.getArgument(0); try {
            var f=Usuario.class.getDeclaredField("id"); f.setAccessible(true); f.set(u,1L); } catch(Exception e){ throw new RuntimeException(e); } return u; });
        RegistroRequestDTO r=new RegistroRequestDTO(); r.setUsername("nuevo"); r.setPassword("nuevo12345");
        r.setEmail("nuevo@educacion.gob.es");
        assertEquals("CIUDADANO", service.registrar(r).getRoles().get(0));
    }
}
