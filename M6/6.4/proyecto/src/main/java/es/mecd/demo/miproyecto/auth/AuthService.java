package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository; private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthService(UsuarioRepository u, RolRepository r, PasswordEncoder p){usuarioRepository=u;
        rolRepository=r;passwordEncoder=p;}
    @Transactional public UsuarioResponseDTO registrar(RegistroRequestDTO request){
        if(usuarioRepository.existsByUsername(request.getUsername())) throw new
            RecursoDuplicadoException("Usuario","username",request.getUsername());
        if(usuarioRepository.existsByEmail(request.getEmail())) throw new
            RecursoDuplicadoException("Usuario","email",request.getEmail());
        Rol user=rolRepository.findByNombre("USER").orElseThrow(() -> new RecursoNoEncontradoException("Rol","USER"));
        Usuario u=new Usuario(); u.setUsername(request.getUsername());
            u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setEmail(request.getEmail()); u.setActivo(true); u.getRoles().add(user); return
            toDTO(usuarioRepository.save(u));
    }
    @Transactional public void cambiarPassword(String username,CambioPasswordRequestDTO request){
        Usuario u=usuarioRepository.findByUsername(username).orElseThrow(() -> new
            RecursoNoEncontradoException("Usuario",username));
        if(!passwordEncoder.matches(request.getPasswordActual(),u.getPassword())) throw new
            OperacionNoPermitidaException("La contraseña actual no es correcta");
        u.setPassword(passwordEncoder.encode(request.getPasswordNueva()));
    }
    private UsuarioResponseDTO toDTO(Usuario u){ UsuarioResponseDTO d=new UsuarioResponseDTO();
        d.setIdentificador(u.getId()==null?null:u.getId().toString());
        d.setUsername(u.getUsername()); d.setEmail(u.getEmail()); d.setRoles(u.getRoles().stream()
            .map(Rol::getNombre).sorted().toList()); return d; }
}
