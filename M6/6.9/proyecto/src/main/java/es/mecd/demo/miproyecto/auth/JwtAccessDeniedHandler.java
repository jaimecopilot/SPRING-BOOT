package es.mecd.demo.miproyecto.auth; import jakarta.servlet.http.*; import java.io.IOException; import
    org.springframework.security.access.AccessDeniedException; import
    org.springframework.security.web.access.AccessDeniedHandler; import org.springframework.stereotype.Component;
@Component public class JwtAccessDeniedHandler implements AccessDeniedHandler { private final
    SecurityErrorWriter w; public JwtAccessDeniedHandler(SecurityErrorWriter w){this.w=w;} @Override
    public void handle(HttpServletRequest r,HttpServletResponse s,AccessDeniedException e)throws
    IOException{w.write(r,s,403,"ACCESO_DENEGADO","No tiene permisos para acceder a este recurso");} }
