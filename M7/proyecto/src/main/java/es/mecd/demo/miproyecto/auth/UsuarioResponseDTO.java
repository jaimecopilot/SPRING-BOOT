package es.mecd.demo.miproyecto.auth;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
public class UsuarioResponseDTO {
    @JsonProperty("id") private String identificador;
    private String username; private String email; private List<String> roles; private boolean activo;
    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
