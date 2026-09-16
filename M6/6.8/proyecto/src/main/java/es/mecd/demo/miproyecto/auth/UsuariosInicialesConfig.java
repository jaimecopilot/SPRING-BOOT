package es.mecd.demo.miproyecto.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UsuariosInicialesConfig {

    @Bean
    CommandLineRunner cargarUsuarios(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // El orden se mantiene para que el reto 6.7 sea reproducible:
            // admin=id 1, ana=id 2 y gestor=id 3 en la BD H2 recién creada.
            Rol rolUser = obtenerOCrearRol(rolRepository, "USER", "Usuario");
            Rol rolAdmin = obtenerOCrearRol(rolRepository, "ADMIN", "Administrador");

            Usuario admin = obtenerOCrearUsuario(
                    usuarioRepository, "admin", "admin@educacion.gob.es");
            prepararUsuario(admin, "admin123", passwordEncoder, rolUser, rolAdmin);
            usuarioRepository.save(admin);

            Usuario ana = obtenerOCrearUsuario(
                    usuarioRepository, "ana", "ana@educacion.gob.es");
            prepararUsuario(ana, "ana123", passwordEncoder, rolUser);
            usuarioRepository.save(ana);

            // Punto 6.8: materializar GESTOR y conservar también USER.
            Rol rolGestor = obtenerOCrearRol(rolRepository, "GESTOR", "Gestor");
            Usuario gestor = obtenerOCrearUsuario(
                    usuarioRepository, "gestor", "gestor@educacion.gob.es");
            prepararUsuario(gestor, "gestor123", passwordEncoder, rolUser, rolGestor);
            usuarioRepository.save(gestor);
        };
    }

    private Rol obtenerOCrearRol(
            RolRepository rolRepository,
            String nombre,
            String descripcion) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> rolRepository.save(new Rol(nombre, descripcion)));
    }

    private Usuario obtenerOCrearUsuario(
            UsuarioRepository usuarioRepository,
            String username,
            String email) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setEmail(email);
            return usuario;
        });
    }

    private void prepararUsuario(
            Usuario usuario,
            String password,
            PasswordEncoder passwordEncoder,
            Rol... roles) {
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setActivo(true);
        usuario.getRoles().clear();
        for (Rol rol : roles) {
            usuario.getRoles().add(rol);
        }
    }
}
