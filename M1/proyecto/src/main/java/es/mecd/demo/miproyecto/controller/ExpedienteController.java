package es.mecd.demo.miproyecto.controller;

import es.mecd.demo.miproyecto.dto.ExpedienteDTO;
import es.mecd.demo.miproyecto.dto.SolicitanteDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/expedientes")
public class ExpedienteController {

    @GetMapping("/ejemplo")
    public ExpedienteDTO ejemplo() {
        ExpedienteDTO dto = new ExpedienteDTO(
                "12345",
                "Ana García López",
                "12345678A",
                "EN_TRAMITE",
                "BECA",
                LocalDate.of(2025, 1, 15),
                1500.00,
                true,
                List.of("DNI.pdf", "Notas.pdf"));
        dto.setNumeroSeguridadSocial("DEMO-NO-REAL");
        dto.setSolicitante(new SolicitanteDTO(
                "Ana", "García López", "12345678A"));
        return dto;
    }

    @PostMapping("/eco")
    public ExpedienteDTO eco(@RequestBody ExpedienteDTO dto) {
        return dto;
    }
}
