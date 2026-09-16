package es.mecd.demo.miproyecto.auth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class CambioPasswordRequestDTO {
    @NotBlank private String passwordActual;
    @NotBlank @Size(min=8) private String passwordNueva;
    public String getPasswordActual(){return passwordActual;} public void setPasswordActual(String v){passwordActual=v;}
    public String getPasswordNueva(){return passwordNueva;} public void setPasswordNueva(String v){passwordNueva=v;}
}
