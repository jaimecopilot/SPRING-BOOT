package es.mecd.demo.miproyecto.config;
import org.springframework.context.annotation.*; import org.springframework.http.HttpMethod; import
    org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer; import
    org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; import
    org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity; import
    org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; import
    org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories; import
    org.springframework.security.crypto.password.PasswordEncoder; import
    org.springframework.security.web.SecurityFilterChain;
@Configuration @EnableWebSecurity @EnableMethodSecurity public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return PasswordEncoderFactories.createDelegatingPasswordEncoder();}
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration c)throws Exception{
     return c.getAuthenticationManager();}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
     http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults())
     .headers(h->h.frameOptions(f->f.sameOrigin()))
 .authorizeHttpRequests(a->a.requestMatchers(HttpMethod.POST,"/api/v1/auth/registro",
     "/api/v1/auth/login","/api/v1/auth/refresh","/api/v1/auth/logout").permitAll()
 .requestMatchers("/api/v1/public/**","/h2-console/**","/swagger-ui/**","/swagger-ui.html",
     "/v3/api-docs/**").permitAll().requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
     .requestMatchers("/api/v1/gestor/**").hasAnyRole("GESTOR","ADMIN").anyRequest().authenticated())
     .httpBasic(Customizer.withDefaults());return http.build();}
}
