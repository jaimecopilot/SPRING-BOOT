package es.mecd.demo.miproyecto.auth;

import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                            RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
public Page<UsuarioResponseDTO> listar(Pageable pageable) {
    return usuarioRepository.findAll(pageable).map(this::toDTO);
}

@Transactional(readOnly = true)
public UsuarioResponseDTO consultar(Long id) {
    return usuarioRepository.findById(id)
            .map(this::toDTO)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
}

@Transactional
public UsuarioResponseDTO cambiarRoles(Long id, List<String> nombresRoles) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));

    Set<Rol> roles = new HashSet<>();
    for (String nombre : nombresRoles) {
        Rol rol = rolRepository.findByNombre(nombre)
                 .orElseThrow(() -> new RecursoNoEncontradoException("Rol", nombre));
        roles.add(rol);
    }

    usuario.setRoles(roles);
    return toDTO(usuarioRepository.save(usuario));
    }

    private UsuarioResponseDTO toDTO(Usuario usuario) {
         UsuarioResponseDTO dto = new UsuarioResponseDTO();
         dto.setIdentificador(usuario.getId() != null ? usuario.getId().toString() : null);
         dto.setUsername(usuario.getUsername());
         dto.setEmail(usuario.getEmail());
         dto.setActivo(usuario.isActivo());
         dto.setRoles(usuario.getRoles().stream()
                 .map(Rol::getNombre)
                 .sorted()
                 .toList());
         return dto;
    }
}
