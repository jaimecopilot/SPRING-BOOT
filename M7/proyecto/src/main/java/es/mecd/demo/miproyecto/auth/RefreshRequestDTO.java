package es.mecd.demo.miproyecto.auth;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
public class RefreshRequestDTO {
    @NotBlank @JsonProperty("refresh_token") private String refreshToken;
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
