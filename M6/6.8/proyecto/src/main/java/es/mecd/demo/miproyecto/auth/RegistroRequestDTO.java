package es.mecd.demo.miproyecto.auth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class RegistroRequestDTO {
    @NotBlank @Size(min=3,max=50) private String username;
    @NotBlank @Size(min=8) private String password;
    @NotBlank @Email private String email;
    public String getUsername(){return username;} public void setUsername(String v){username=v;}
    public String getPassword(){return password;} public void setPassword(String v){password=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
}
