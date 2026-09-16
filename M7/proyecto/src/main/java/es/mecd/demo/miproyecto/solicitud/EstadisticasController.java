package es.mecd.demo.miproyecto.solicitud;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/estadisticas")
public class EstadisticasController {

    private final SolicitudService solicitudService;

    public EstadisticasController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @GetMapping("/solicitudes-por-estado")
    @PreAuthorize("hasAnyRole('GESTOR', 'ADMIN')")
    public Map<String, Long> solicitudesPorEstado() {
        return solicitudService.contarPorEstado();
    }
}
