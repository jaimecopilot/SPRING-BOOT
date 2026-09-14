package es.mecd.demo.miproyecto.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Datos iniciales de seguridad.
 *
 * <p>La carga es idempotente por entidad para que los módulos posteriores
 * puedan añadir roles o usuarios sin depender de una base completamente
 * vacía.</p>
 */
@Configuration
public class UsuariosInicialesConfig {

    @Bean
    CommandLineRunner cargarUsuarios(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            Rol user = buscarOCrearRol(
                    rolRepository, "USER", "Usuario");
            Rol admin = buscarOCrearRol(
                    rolRepository, "ADMIN", "Administrador");
            Rol gestor = buscarOCrearRol(
                    rolRepository, "GESTOR", "Gestor");

            crearSiFalta(
                    usuarioRepository,
                    passwordEncoder,
                    "ana",
                    "ana123",
                    "ana@educacion.gob.es",
                    user);

            crearSiFalta(
                    usuarioRepository,
                    passwordEncoder,
                    "admin",
                    "admin123",
                    "admin@educacion.gob.es",
                    user,
                    admin);

            crearSiFalta(
                    usuarioRepository,
                    passwordEncoder,
                    "gestor",
                    "gestor123",
                    "gestor@educacion.gob.es",
                    user,
                    gestor);
        };
    }

    private Rol buscarOCrearRol(
            RolRepository rolRepository,
            String nombre,
            String descripcion) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> rolRepository.save(
                        new Rol(nombre, descripcion)));
    }

    private void crearSiFalta(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String password,
            String email,
            Rol... roles) {
        if (usuarioRepository.existsByUsername(username)) {
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setActivo(true);

        for (Rol rol : roles) {
            usuario.getRoles().add(rol);
        }

        usuarioRepository.save(usuario);
    }
}
