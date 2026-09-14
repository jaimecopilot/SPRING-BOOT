package es.mecd.demo.miproyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Beans sencillos para observar la activación de perfiles. */
@Configuration
public class PerfilConfig {
    @Bean
    @Profile("dev")
    String mensajePerfilDev() {
        return "perfil-dev-activo";
    }

    @Bean
    @Profile("prod")
    String mensajePerfilProd() {
        return "perfil-prod-activo";
    }
}
