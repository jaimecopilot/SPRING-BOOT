package es.mecd.demo.miproyecto.auth; import jakarta.servlet.http.*; import java.io.IOException; import
    org.springframework.security.core.AuthenticationException; import
    org.springframework.security.web.AuthenticationEntryPoint; import org.springframework.stereotype.Component;
@Component public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { private final
    SecurityErrorWriter w; public JwtAuthenticationEntryPoint(SecurityErrorWriter w){this.w=w;}
    @Override public void commence(HttpServletRequest r,HttpServletResponse s,AuthenticationException e)
    throws IOException{w.write(r,s,401,"NO_AUTENTICADO","Se requiere autenticación para acceder a este recurso");} }
