package es.mecd.demo.miproyecto.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuariosInicialesConfig {
    @Bean
    CommandLineRunner cargarUsuarios(UsuarioRepository usuarios, RolRepository roles, PasswordEncoder encoder) {
        return args -> {
            Rol user = roles.findByNombre("USER").orElseGet(() -> roles.save(new Rol("USER", "Usuario")));
            Rol admin = roles.findByNombre("ADMIN").orElseGet(() -> roles.save(new Rol("ADMIN", "Administrador")));
            crearSiFalta(usuarios, encoder, "ana", "ana123", "ana@educacion.gob.es", user);
            crearSiFalta(usuarios, encoder, "admin", "admin123", "admin@educacion.gob.es", user, admin);
        };
    }
    private void crearSiFalta(UsuarioRepository repo, PasswordEncoder encoder, String username,
            String password, String email, Rol... roles) {
        if (repo.existsByUsername(username)) return;
        Usuario u = new Usuario(); u.setUsername(username); u.setPassword(encoder.encode(password));
        u.setEmail(email); u.setActivo(true); for (Rol r: roles) u.getRoles().add(r); repo.save(u);
    }
}
