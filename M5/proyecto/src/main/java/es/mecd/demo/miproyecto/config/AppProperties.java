package es.mecd.demo.miproyecto.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/** Propiedades funcionales tipadas de la aplicación. */
@Component
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {
    @NotBlank
    private String nombreOficina;

    @NotBlank
    @Pattern(regexp = "\\d+\\.\\d+\\.\\d+")
    private String version;

    @NotBlank
    private String entorno;

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEntorno() {
        return entorno;
    }

    public void setEntorno(String entorno) {
        this.entorno = entorno;
    }
}
