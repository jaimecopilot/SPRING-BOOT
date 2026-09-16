package es.mecd.demo.miproyecto.fichero;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint didáctico para demostrar un advice selectivo. */
@RestController
@RequestMapping("/api/v1/ficheros")
public class FicheroController {
    @GetMapping("/{nombre}")
    public String descargar(@PathVariable String nombre) {
        throw new FicheroNoEncontradoException(nombre);
    }
}
