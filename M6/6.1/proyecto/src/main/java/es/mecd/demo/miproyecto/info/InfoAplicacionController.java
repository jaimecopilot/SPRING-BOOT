package es.mecd.demo.miproyecto.info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/v1/info") public class InfoAplicacionController {
    @Value("${curso.entorno:base}") private String entorno;
    @Value("${curso.nombre:Curso Spring Boot 2026}") private String nombre;
    @GetMapping public Map<String,String> info() {
        return Map.of("entorno",entorno,"nombre",nombre);
    }
}
