package es.mecd.demo.miproyecto.expediente;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Opera sobre expedientes sin conocer HTTP ni la implementación del almacenamiento. */
@Service
public class ExpedienteService {
    private final ExpedienteRepository repositorio;
    public ExpedienteService(ExpedienteRepository repositorio) { this.repositorio = repositorio; }
    public List<ExpedienteDTO> listar() { return repositorio.listarTodos(); }
    public Optional<ExpedienteDTO> consultar(String id) { return repositorio.buscarPorId(id); }
    public ExpedienteDTO crear(ExpedienteDTO dto) {
        if (dto.getIdentificador() == null || dto.getIdentificador().isBlank()) {
            dto.setIdentificador(repositorio.siguienteIdentificador());
        }
        return repositorio.guardar(dto);
    }
    public Optional<ExpedienteDTO> actualizar(String id, ExpedienteDTO dto) {
        return repositorio.buscarPorId(id).map(existente -> {
            dto.setIdentificador(id);
            return repositorio.guardar(dto);
        });
    }
    public boolean eliminar(String id) { return repositorio.eliminar(id); }
    public ExpedienteDTO ejemplo() {
        ExpedienteDTO dto = new ExpedienteDTO(
                "12345", "Ana García López", "12345678A", "EN_TRAMITE", "BECA",
                LocalDate.of(2025, 1, 15), 1500.00, true, List.of("DNI.pdf", "Notas.pdf"));
        dto.setNumeroSeguridadSocial("DEMO-NO-REAL");
        dto.setSolicitante(new SolicitanteDTO("Ana", "García López", "12345678A"));
        return dto;
    }
    public ExpedienteDTO eco(ExpedienteDTO dto) { return dto; }
}
