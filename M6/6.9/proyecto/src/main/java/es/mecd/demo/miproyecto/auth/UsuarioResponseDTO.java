package es.mecd.demo.miproyecto.auth;
import java.util.List;
public class UsuarioResponseDTO {
    private String identificador; private String username; private String email; private List<String> roles;
    public String getIdentificador(){return identificador;} public void setIdentificador(String v){identificador=v;}
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public List<String> getRoles(){return roles;} public void setRoles(List<String> v){roles=v;}
}
