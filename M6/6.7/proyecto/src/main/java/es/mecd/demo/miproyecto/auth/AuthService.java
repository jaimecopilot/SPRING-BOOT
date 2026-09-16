package es.mecd.demo.miproyecto.auth;
import es.mecd.demo.miproyecto.common.exception.*; import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.*; import
    org.springframework.security.core.AuthenticationException; import
    org.springframework.security.crypto.password.PasswordEncoder; import
    org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class AuthService {
 private final AuthenticationManager authenticationManager; private final JwtService jwtService;
     private final TokenRevocationService tokenRevocationService; private final UsuarioRepository
     usuarioRepository; private final RolRepository rolRepository; private final PasswordEncoder passwordEncoder;
 public AuthService(AuthenticationManager a,JwtService j,TokenRevocationService t,UsuarioRepository u,
     RolRepository r,PasswordEncoder p){authenticationManager=a;jwtService=j;tokenRevocationService=t;
     usuarioRepository=u;rolRepository=r;passwordEncoder=p;}
 @Transactional public UsuarioResponseDTO registrar(RegistroRequestDTO r){
     if(usuarioRepository.existsByUsername(r.getUsername()))throw new
     RecursoDuplicadoException("Usuario","username",r.getUsername());
     if(usuarioRepository.existsByEmail(r.getEmail()))throw new RecursoDuplicadoException("Usuario",
     "email",r.getEmail());Rol role=rolRepository.findByNombre("USER").orElseThrow(()->new
     RecursoNoEncontradoException("Rol","USER"));Usuario u=new Usuario();u.setUsername(r.getUsername());
     u.setPassword(passwordEncoder.encode(r.getPassword()));u.setEmail(r.getEmail());u.setActivo(true);
     u.getRoles().add(role);return toDTO(usuarioRepository.save(u));}
 @Transactional(readOnly=true) public LoginResponseDTO login(LoginRequestDTO r){try{
     authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(r.getUsername(),
     r.getPassword()));}catch(AuthenticationException e){throw new CredencialesInvalidasException();}
     Usuario u=usuarioRepository.findByUsername(r.getUsername())
     .orElseThrow(CredencialesInvalidasException::new);return crearPar(u);}
 @Transactional(readOnly=true) public LoginResponseDTO refresh(String token){
     if(!jwtService.esRefreshTokenValido(token))throw new
     TokenInvalidoException("Refresh token inválido o expirado");Claims
     c=jwtService.extraerClaims(token);String jti=c.getId();
     if(jti==null||!tokenRevocationService.consumeRefresh(jti,c.getExpiration().toInstant()))throw new
     TokenInvalidoException("Refresh token ya utilizado");Usuario
     u=usuarioRepository.findByUsername(c.getSubject()).orElseThrow(()->new
     RecursoNoEncontradoException("Usuario",c.getSubject()));if(!u.isActivo())throw new
     TokenInvalidoException("Usuario inactivo");return crearPar(u);}
 public void logout(String token){if(!jwtService.esAccessTokenValido(token))throw new
     TokenInvalidoException("Access token inválido o expirado");Claims c=jwtService.extraerClaims(token)
     ;tokenRevocationService.revokeAccess(c.getId(),c.getExpiration().toInstant());}
 @Transactional public void cambiarPassword(String username,CambioPasswordRequestDTO r){Usuario
     u=usuarioRepository.findByUsername(username).orElseThrow(()->new
     RecursoNoEncontradoException("Usuario",username));if(!passwordEncoder.matches(r.getPasswordActual()
     ,u.getPassword()))throw new OperacionNoPermitidaException("La contraseña actual no es correcta");
     u.setPassword(passwordEncoder.encode(r.getPasswordNueva()));}
 private LoginResponseDTO crearPar(Usuario u){return new LoginResponseDTO(jwtService.generarToken(u),
     jwtService.generarRefreshToken(u),jwtService.getExpirationMs()/1000);}
 private UsuarioResponseDTO toDTO(Usuario u){UsuarioResponseDTO d=new UsuarioResponseDTO();
     d.setIdentificador(u.getId()==null?null:u.getId().toString());d.setUsername(u.getUsername());
     d.setEmail(u.getEmail());d.setRoles(u.getRoles().stream().map(Rol::getNombre).sorted().toList());return d;}
}
