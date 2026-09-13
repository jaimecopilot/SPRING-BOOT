package es.mecd.demo.miproyecto.info;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/** Expone propiedades pedagógicas no sensibles de la aplicación. */
@RestController
@RequestMapping("/api/v1/info")
public class InfoAplicacionController {
    private final String nombre;
    private final String version;
    private final String entorno;
    private final String descripcion;

    /** Construye el controlador a partir de configuración externa. */
    public InfoAplicacionController(
            @Value("${app.nombre}") String nombre,
            @Value("${app.version}") String version,
            @Value("${app.entorno:desconocido}") String entorno,
            @Value("${app.descripcion:Sin descripción}") String descripcion) {
        this.nombre = nombre; this.version = version; this.entorno = entorno; this.descripcion = descripcion;
    }

    /** @return propiedades públicas de la aplicación */
    @GetMapping
    public Map<String, String> info() {
        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("nombre", nombre); respuesta.put("version", version);
        respuesta.put("entorno", entorno); respuesta.put("descripcion", descripcion);
        return respuesta;
    }
}
