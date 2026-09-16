package es.mecd.demo.miproyecto.auth;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class UsuariosInicialesConfig {
    @Bean @Profile("dev")
    CommandLineRunner cargarUsuarios(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder encoder) {
        return args -> {
            Rol user = rol(roles, "USER", "Usuario genérico");
            Rol admin = rol(roles, "ADMIN", "Administrador");
            Rol ciudadano = rol(roles, "CIUDADANO", "Ciudadano solicitante");
            Rol gestor = rol(roles, "GESTOR", "Gestor de solicitudes");
            Rol consultor = rol(roles, "CONSULTOR", "Consultor");
            crear(usuarios, encoder, "admin", "admin123", "admin@educacion.gob.es", admin);
            crear(usuarios, encoder, "user", "user12345", "user@educacion.gob.es", user);
            crear(usuarios, encoder, "ciudadano", "ciudadano123", "ciudadano@educacion.gob.es", ciudadano);
            crear(usuarios, encoder, "gestor", "gestor123", "gestor@educacion.gob.es", gestor, user);
            crear(usuarios, encoder, "consultor", "consultor123", "consultor@educacion.gob.es", consultor);
        };
    }
    private Rol rol(RolRepository repo, String nombre, String descripcion) {
        return repo.findByNombre(nombre).orElseGet(() -> repo.save(new Rol(nombre, descripcion)));
    }
    private void crear(UsuarioRepository repo, PasswordEncoder encoder, String username,
            String password, String email, Rol... roles) {
        if (repo.existsByUsername(username)) return;
        Usuario u = new Usuario(); u.setUsername(username); u.setPassword(encoder.encode(password));
        u.setEmail(email); for (Rol rol : roles) u.getRoles().add(rol); repo.save(u);
    }
}
