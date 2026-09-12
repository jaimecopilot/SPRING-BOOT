package es.mecd.demo.miproyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaludoController {

    @GetMapping("/hola")
    public String saludar() {
        return construirMensaje("Ministerio de Educación");
    }

    @GetMapping("/adios")
    public String despedir() {
        return "Adiós, Ministerio de Educación";
    }

    private String construirMensaje(String destinatario) {
        return "Hola, " + destinatario;
    }
}
