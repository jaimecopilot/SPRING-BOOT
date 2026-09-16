package es.mecd.demo.miproyecto.auth;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository usuarios; @Mock RolRepository roles; UsuarioService service;
    @BeforeEach void setUp() { service=new UsuarioService(usuarios,roles); }
    @Test void cambiarRolesDebeResolverRoles() {
        Usuario u=new Usuario(); u.setUsername("u"); u.setEmail("u@e.es");
        Rol admin=new Rol("ADMIN","Admin"); when(usuarios.findById(1L)).thenReturn(Optional.of(u));
        when(roles.findByNombre("ADMIN")).thenReturn(Optional.of(admin)); when(usuarios.save(u)).thenReturn(u);
        assertEquals(List.of("ADMIN"), service.cambiarRoles(1L,List.of("ADMIN")).getRoles());
    }
}
