package es.mecd.demo.miproyecto.auth; import jakarta.validation.constraints.NotBlank;
public class LoginRequestDTO { @NotBlank private String username; @NotBlank private String password;
    public String getUsername(){return username;} public void setUsername(String v){username=v;} public
    String getPassword(){return password;} public void setPassword(String v){password=v;} }
