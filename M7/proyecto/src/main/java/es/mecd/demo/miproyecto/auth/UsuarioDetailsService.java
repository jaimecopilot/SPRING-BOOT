package es.mecd.demo.miproyecto.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    public UsuarioDetailsService(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String[] authorities = u.getRoles().stream().map(r -> "ROLE_" + r.getNombre()).toArray(String[]::new);
        return User.withUsername(u.getUsername()).password(u.getPassword()).authorities(authorities)
                .disabled(!u.isActivo()).build();
    }
}
