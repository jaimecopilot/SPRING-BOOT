package es.mecd.demo.miproyecto.auth;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import java.util.HashSet; import java.util.List; import java.util.Set;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class UsuarioService {
 private final UsuarioRepository usuarioRepository; private final RolRepository rolRepository;
 public UsuarioService(UsuarioRepository u,RolRepository r){usuarioRepository=u;rolRepository=r;}
 @Transactional(readOnly=true) public List<UsuarioResponseDTO> listarTodos(){return
     usuarioRepository.findAll().stream().map(this::toDTO).toList();}
 @Transactional(readOnly=true) public UsuarioResponseDTO consultar(Long id){return
     usuarioRepository.findById(id).map(this::toDTO).orElseThrow(()->new RecursoNoEncontradoException("Usuario",id));}
 @Transactional public UsuarioResponseDTO cambiarRoles(Long id,List<String> nombres){Usuario
     u=usuarioRepository.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Usuario",id));
  Set<Rol> roles=new HashSet<>(); for(String n:nombres) roles.add(rolRepository.findByNombre(n)
      .orElseThrow(()->new RecursoNoEncontradoException("Rol",n))); u.setRoles(roles); return toDTO(u);}
 private UsuarioResponseDTO toDTO(Usuario u){UsuarioResponseDTO d=new UsuarioResponseDTO();
     d.setIdentificador(u.getId()==null?null:u.getId().toString());d.setUsername(u.getUsername());
     d.setEmail(u.getEmail());d.setRoles(u.getRoles().stream().map(Rol::getNombre).sorted().toList());return d;}
}
