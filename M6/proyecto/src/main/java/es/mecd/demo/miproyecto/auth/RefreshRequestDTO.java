package es.mecd.demo.miproyecto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/** Refresh token usado para renovar el par de credenciales. */
public class RefreshRequestDTO {
    @NotBlank(message = "El refresh token es obligatorio")
    @JsonProperty("refresh_token")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
