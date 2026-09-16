package es.mecd.demo.miproyecto.config;

import es.mecd.demo.miproyecto.auth.JwtAccessDeniedHandler;
import es.mecd.demo.miproyecto.auth.JwtAuthenticationEntryPoint;
import es.mecd.demo.miproyecto.auth.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() { return PasswordEncoderFactories.createDelegatingPasswordEncoder(); }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter filter,
            JwtAuthenticationEntryPoint entryPoint, JwtAccessDeniedHandler deniedHandler) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                         .requestMatchers(HttpMethod.POST,
                                "/api/v1/auth/registro",
                                "/api/v1/auth/login",
                                "/api/v1/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/becas", "/api/v1/becas/**").permitAll()
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/becas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/becas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/becas/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/solicitudes/*/estado").hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes").hasRole("CIUDADANO")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/solicitudes/*").hasRole("CIUDADANO")
                        .requestMatchers(HttpMethod.POST, "/api/v1/solicitudes/*/documentos").hasRole("CIUDADANO")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/solicitudes/*/documentos/**").hasRole("CIUDADANO")
                        .requestMatchers(HttpMethod.GET, "/api/v1/estadisticas/**")
                                .hasAnyRole("GESTOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/solicitudes/**")
                                .hasAnyRole("CIUDADANO", "GESTOR", "ADMIN", "CONSULTOR")
                        .requestMatchers("/api/v1/solicitudes/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint).accessDeniedHandler(deniedHandler))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*")); config.setExposedHeaders(List.of("Location"));
        config.setAllowCredentials(true); config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config); return source;
    }
}
