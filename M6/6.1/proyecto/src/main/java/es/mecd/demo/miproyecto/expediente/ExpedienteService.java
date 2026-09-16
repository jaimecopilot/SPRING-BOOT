package es.mecd.demo.miproyecto.expediente;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Operaciones de aplicación para expedientes. */
@Service
public class ExpedienteService {
    private final ExpedienteRepository repositorio;

    public ExpedienteService(ExpedienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<ExpedienteDTO> listar() { return repositorio.listarTodos(); }
    public Optional<ExpedienteDTO> consultar(String id) { return repositorio.buscarPorId(id); }

    public ExpedienteDTO crear(ExpedienteRequestDTO request) {
        ExpedienteDTO expediente = new ExpedienteDTO();
        expediente.setIdentificador(repositorio.siguienteIdentificador());
        expediente.setTitular(request.getTitular());
        expediente.setEstado(request.getEstado());
        expediente.setSolicitante(request.getSolicitante());
        expediente.setFechaSolicitud(LocalDate.now());
        expediente.setActivo(true);
        return repositorio.guardar(expediente);
    }

    public Optional<ExpedienteDTO> cambiarEstado(String id, String estado) {
        return repositorio.buscarPorId(id).map(expediente -> {
            expediente.setEstado(estado);
            return repositorio.guardar(expediente);
        });
    }

    public ExpedienteDTO ejemplo() {
        ExpedienteDTO expediente = new ExpedienteDTO(
                "12345",
                "Ministerio",
                "12345678A",
                "EN_TRAMITE",
                "BECA",
                LocalDate.of(2025, 1, 15),
                1200.0,
                true,
                List.of("solicitud.pdf"));
        expediente.setNumeroSeguridadSocial("SECRETO");
        return expediente;
    }

    public ExpedienteDTO eco(ExpedienteDTO expediente) { return expediente; }
}
