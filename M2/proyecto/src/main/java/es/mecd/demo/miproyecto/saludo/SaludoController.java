package es.mecd.demo.miproyecto.saludo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Conserva los endpoints de saludo heredados de M0. */
@RestController
public class SaludoController {
    /** @return saludo de la aplicación */
    @GetMapping("/hola")
    public String saludar() { return construirMensaje("Ministerio de Educación"); }
    /** @return despedida de la aplicación */
    @GetMapping("/adios")
    public String despedir() { return "Adiós, Ministerio de Educación"; }
    private String construirMensaje(String destinatario) { return "Hola, " + destinatario; }
}
