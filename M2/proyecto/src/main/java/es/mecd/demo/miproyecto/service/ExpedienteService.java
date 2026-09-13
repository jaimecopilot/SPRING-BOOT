package es.mecd.demo.miproyecto.service;

import es.mecd.demo.miproyecto.dto.ExpedienteDTO;
import es.mecd.demo.miproyecto.dto.SolicitanteDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpedienteService {

    private final List<ExpedienteDTO> expedientes = new ArrayList<>(List.of(
            new ExpedienteDTO(
                    "1", "Ana García", "12345678A", "EN_TRAMITE", "BECA",
                    LocalDate.of(2025, 1, 15), 1500.0, true, List.of("DNI.pdf")),
            new ExpedienteDTO(
                    "2", "Luis Pérez", "87654321B", "RESUELTA", "AYUDA_LIBROS",
                    LocalDate.of(2025, 1, 10), 300.0, true, List.of("Solicitud.pdf"))
    ));

    public List<ExpedienteDTO> listar() {
        return new ArrayList<>(expedientes);
    }

    public Optional<ExpedienteDTO> consultar(String id) {
        return expedientes.stream()
                .filter(e -> e.getIdentificador().equals(id))
                .findFirst();
    }

    public ExpedienteDTO crear(ExpedienteDTO dto) {
        int siguienteId = expedientes.stream()
                .map(ExpedienteDTO::getIdentificador)
                .filter(id -> id != null && id.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;

        dto.setIdentificador(String.valueOf(siguienteId));
        expedientes.add(dto);
        return dto;
    }

    public Optional<ExpedienteDTO> actualizar(
            String id,
            ExpedienteDTO dto) {

        return consultar(id).map(existente -> {
            dto.setIdentificador(id);
            expedientes.set(expedientes.indexOf(existente), dto);
            return dto;
        });
    }

    public boolean eliminar(String id) {
        return expedientes.removeIf(
                e -> e.getIdentificador().equals(id));
    }

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

    public ExpedienteDTO eco(ExpedienteDTO dto) {
        return dto;
    }
}
