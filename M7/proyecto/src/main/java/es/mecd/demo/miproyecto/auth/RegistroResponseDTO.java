package es.mecd.demo.miproyecto.auth;
import java.util.List;
public class RegistroResponseDTO {
    private String id; private String username; private String email; private List<String> roles;
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public List<String> getRoles() { return roles; } public void setRoles(List<String> roles) { this.roles = roles; }
}
