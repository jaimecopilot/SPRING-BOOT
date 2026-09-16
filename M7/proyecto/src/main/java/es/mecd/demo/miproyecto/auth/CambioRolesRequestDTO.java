package es.mecd.demo.miproyecto.auth;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CambioRolesRequestDTO {

       @NotEmpty(message = "Debe indicar al menos un rol")
       private List<String> roles;

       public List<String> getRoles() { return roles; }
       public void setRoles(List<String> roles) { this.roles = roles; }
}
