package es.mecd.demo.miproyecto.auth;
import io.jsonwebtoken.Claims; import io.jsonwebtoken.JwtException; import io.jsonwebtoken.Jwts;
import java.util.Date; import java.util.List; import java.util.UUID; import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
@Service public class JwtService {
 private static final String CLAIM_TIPO="tipo", TIPO_ACCESS="access", TIPO_REFRESH="refresh"; private
     final SecretKey clave; private final long expirationMs,refreshExpirationMs;
 public JwtService(SecretKey clave,@Value("${jwt.expiration}") long e,
     @Value("${jwt.refresh-expiration}") long r){this.clave=clave;expirationMs=e;refreshExpirationMs=r;}
 public String generarToken(Usuario u){Date a=new Date(),e=new Date(a.getTime()+expirationMs);
     List<String> roles=u.getRoles().stream().map(r->"ROLE_"+r.getNombre()).sorted().toList();return
     Jwts.builder().subject(u.getUsername()).id(UUID.randomUUID().toString()).claim(CLAIM_TIPO,
     TIPO_ACCESS).claim("id",u.getId()).claim("email",u.getEmail()).claim("roles",roles).issuedAt(a)
     .expiration(e).signWith(clave,Jwts.SIG.HS256).compact();}
 public String generarRefreshToken(Usuario u){Date a=new Date(),e=new Date(a.getTime()
     +refreshExpirationMs);return Jwts.builder().subject(u.getUsername()).id(UUID.randomUUID()
     .toString()).claim(CLAIM_TIPO,TIPO_REFRESH).issuedAt(a).expiration(e).signWith(clave,Jwts.SIG.HS256).compact();}
 public Claims extraerClaims(String token){return Jwts.parser().verifyWith(clave).build()
     .parseSignedClaims(token).getPayload();}
 public boolean esValido(String token){return claimsSiValido(token)!=null;} public boolean
     esAccessTokenValido(String token){Claims c=claimsSiValido(token);return
     c!=null&&TIPO_ACCESS.equals(c.get(CLAIM_TIPO,String.class));}
 public boolean esRefreshTokenValido(String token){Claims c=claimsSiValido(token);return
     c!=null&&TIPO_REFRESH.equals(c.get(CLAIM_TIPO,String.class));}
 public long getExpirationMs(){return expirationMs;} public long getRefreshExpirationMs(){return refreshExpirationMs;}
 private Claims claimsSiValido(String t){try{return extraerClaims(t);}
     catch(JwtException|IllegalArgumentException e){return null;}}
}
