package es.mecd.demo.miproyecto.auth; import com.fasterxml.jackson.annotation.JsonProperty;
public class LoginResponseDTO { @JsonProperty("access_token") private final String accessToken;
    @JsonProperty("refresh_token") private final String refreshToken; @JsonProperty("token_type")
    private final String tokenType="Bearer"; @JsonProperty("expires_in") private final long expiresIn;
 public LoginResponseDTO(String a,String r,long e){accessToken=a;refreshToken=r;expiresIn=e;} public
     String getAccessToken(){return accessToken;} public String getRefreshToken(){return refreshToken;}
     public String getTokenType(){return tokenType;} public long getExpiresIn(){return expiresIn;} }
