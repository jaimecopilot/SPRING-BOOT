package es.mecd.demo.miproyecto.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Autentica una petición a partir de Authorization: Bearer. */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final TokenRevocationService tokenRevocationService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            TokenRevocationService tokenRevocationService) {
        this.jwtService = jwtService;
        this.tokenRevocationService = tokenRevocationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtService.esAccessTokenValido(token)) {
            LOG.debug("JWT inválido o no-access en {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.extraerClaims(token);
            if (claims.getId() == null
                    || tokenRevocationService.isAccessRevoked(claims.getId())) {
                filterChain.doFilter(request, response);
                return;
            }

            Number rawId = claims.get("id", Number.class);
            Long id = rawId == null ? null : rawId.longValue();
            String username = claims.getSubject();
            String email = claims.get("email", String.class);
            List<GrantedAuthority> authorities = jwtService.extraerAuthorities(token);

            UsuarioPrincipal principal = new UsuarioPrincipal(
                    id, username, email, authorities);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal, null, authorities);
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            LOG.debug("Usuario {} autenticado en {}", username, request.getRequestURI());
        } catch (RuntimeException ex) {
            SecurityContextHolder.clearContext();
            LOG.debug(
                    "No se pudo construir el principal en {}: {}",
                    request.getRequestURI(), ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
