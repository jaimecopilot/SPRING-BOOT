package es.mecd.demo.miproyecto.saludo;
import org.springframework.web.bind.annotation.*;
@RestController public class SaludoController {
    @GetMapping("/hola") public String hola() {
        return "Hola, Ministerio de Educación";
    }
    @GetMapping("/adios") public String adios() {
        return "Adiós, Ministerio de Educación";
    }
}
