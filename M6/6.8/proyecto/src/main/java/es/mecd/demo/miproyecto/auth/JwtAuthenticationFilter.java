package es.mecd.demo.miproyecto.auth;
import io.jsonwebtoken.Claims; import jakarta.servlet.*; import jakarta.servlet.http.*; import
    java.io.IOException; import java.util.List;
import org.slf4j.*; import
    org.springframework.security.authentication.UsernamePasswordAuthenticationToken; import
    org.springframework.security.core.GrantedAuthority; import
    org.springframework.security.core.context.SecurityContextHolder; import
    org.springframework.security.web.authentication.WebAuthenticationDetailsSource; import
    org.springframework.stereotype.Component; import org.springframework.web.filter.OncePerRequestFilter;
@Component public class JwtAuthenticationFilter extends OncePerRequestFilter { private static final
    Logger LOG=LoggerFactory.getLogger(JwtAuthenticationFilter.class); private final JwtService
    jwtService; private final TokenRevocationService revocation;
 public JwtAuthenticationFilter(JwtService j,TokenRevocationService r){jwtService=j;revocation=r;}
 @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain
     chain)throws ServletException,IOException{String h=req.getHeader("Authorization");
     if(h==null||!h.startsWith("Bearer ")){chain.doFilter(req,res);return;}String token=h.substring(7);
     if(!jwtService.esAccessTokenValido(token)){chain.doFilter(req,res);return;}try{Claims
     c=jwtService.extraerClaims(token);if(c.getId()==null||revocation.isAccessRevoked(c.getId())){
     chain.doFilter(req,res);return;}Number n=c.get("id",Number.class);Long
     id=n==null?null:n.longValue();String username=c.getSubject(),email=c.get("email",String.class);
     List<GrantedAuthority> auth=jwtService.extraerAuthorities(token);UsuarioPrincipal p=new
     UsuarioPrincipal(id,username,email,auth);UsernamePasswordAuthenticationToken a=new
     UsernamePasswordAuthenticationToken(p,null,auth);a.setDetails(new WebAuthenticationDetailsSource()
     .buildDetails(req));SecurityContextHolder.getContext().setAuthentication(a);
     LOG.debug("Usuario {} autenticado en {}",username,req.getRequestURI());}catch(RuntimeException ex){
     SecurityContextHolder.clearContext();LOG.debug("JWT no autenticó {}",req.getRequestURI());}
     chain.doFilter(req,res);}
}
