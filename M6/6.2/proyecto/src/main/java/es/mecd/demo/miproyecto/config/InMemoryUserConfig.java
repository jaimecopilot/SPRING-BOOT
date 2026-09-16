package es.mecd.demo.miproyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/** Usuarios temporales del punto 6.2. Se elimina al comenzar 6.3. */
@Configuration
public class InMemoryUserConfig {
    @Bean
    UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails ana = User.builder().username("ana")
                .password(passwordEncoder.encode("ana123")).roles("USER").build();
        UserDetails admin = User.builder().username("admin")
                .password(passwordEncoder.encode("admin123")).roles("ADMIN", "USER").build();
        UserDetails gestor = User.builder().username("gestor")
                .password(passwordEncoder.encode("gestor123")).roles("GESTOR", "USER").build();
        return new InMemoryUserDetailsManager(ana, admin, gestor);
    }
}
