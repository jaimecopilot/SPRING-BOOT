package es.mecd.demo.miproyecto.solicitud;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.beca.BecaRepository;
import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import es.mecd.demo.miproyecto.common.exception.ValidacionNegocioException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudService {
    private final SolicitudBecaRepository solicitudRepository;
    private final AlumnoRepository alumnoRepository;
    private final BecaRepository becaRepository;

    public SolicitudService(
            SolicitudBecaRepository solicitudRepository,
            AlumnoRepository alumnoRepository,
            BecaRepository becaRepository) {
        this.solicitudRepository = solicitudRepository;
        this.alumnoRepository = alumnoRepository;
        this.becaRepository = becaRepository;
    }

    @Transactional(readOnly = true)
    public Page<SolicitudResponseDTO> listar(
            EstadoSolicitud estado,
            Long alumnoId,
            Long becaId,
            Pageable pageable) {
        return solicitudRepository.buscarConFiltros(estado, alumnoId, becaId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public SolicitudResponseDTO consultar(Long id) {
        return solicitudRepository.findByIdConRelaciones(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));
    }

    @Transactional
    public SolicitudResponseDTO crear(SolicitudRequestDTO request) {
        Alumno alumno = alumnoRepository.findById(request.getAlumnoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno", request.getAlumnoId()));
        Beca beca = becaRepository.findById(request.getBecaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Beca", request.getBecaId()));
        if (!Boolean.TRUE.equals(beca.getActiva())) {
            throw new OperacionNoPermitidaException("No se puede solicitar una beca inactiva");
        }
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioAnio = LocalDateTime.of(ahora.getYear(), 1, 1, 0, 0, 0);
        LocalDateTime finAnio = LocalDateTime.of(ahora.getYear() + 1, 1, 1, 0, 0, 0);
        boolean duplicada = solicitudRepository.existsByAlumnoIdAndBecaIdAndFechaSolicitudBetween(
                alumno.getId(), beca.getId(), inicioAnio, finAnio);
        if (duplicada) {
            throw new RecursoDuplicadoException(
                    "Solicitud",
                    "alumnoId+becaId+año",
                    alumno.getId() + "+" + beca.getId() + "+" + ahora.getYear());
        }
        SolicitudBeca solicitud = new SolicitudBeca();
        solicitud.setAlumno(alumno);
        solicitud.setBeca(beca);
        solicitud.setEstado(EstadoSolicitud.BORRADOR);
        solicitud.setFechaSolicitud(ahora);
        solicitud.setObservaciones(request.getObservaciones());
        return toDTO(solicitudRepository.save(solicitud));
    }

    @Transactional
    public SolicitudResponseDTO cambiarEstado(Long id, CambioEstadoRequestDTO request) {
        SolicitudBeca solicitud = solicitudRepository.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));
        if (solicitud.getEstado() != EstadoSolicitud.ENVIADA
                && solicitud.getEstado() != EstadoSolicitud.EN_REVISION) {
            throw new OperacionNoPermitidaException(
                    "No se puede cambiar el estado de una solicitud en estado " + solicitud.getEstado());
        }
        EstadoSolicitud nuevoEstado;
        try {
            nuevoEstado = EstadoSolicitud.valueOf(request.getEstado());
        } catch (IllegalArgumentException ex) {
            throw new ValidacionNegocioException(
                    "estado",
                    "El estado " + request.getEstado() + " no es válido");
        }
        validarTransicion(solicitud.getEstado(), nuevoEstado);
        if (nuevoEstado == EstadoSolicitud.APROBADA) {
            if (request.getImporteConcedido() == null) {
                throw new ValidacionNegocioException(
                        "importeConcedido",
                        "El importe concedido es obligatorio al aprobar una solicitud");
            }
            if (request.getImporteConcedido().compareTo(solicitud.getBeca().getImporteMaximo()) > 0) {
                throw new ValidacionNegocioException(
                        "importeConcedido",
                        "El importe concedido no puede superar el importe máximo de la beca");
            }
            solicitud.setImporteConcedido(request.getImporteConcedido());
        }
        solicitud.setEstado(nuevoEstado);
        if (request.getObservaciones() != null) {
            solicitud.setObservaciones(request.getObservaciones());
        }
        if (nuevoEstado == EstadoSolicitud.APROBADA || nuevoEstado == EstadoSolicitud.DENEGADA) {
            solicitud.setFechaResolucion(LocalDateTime.now());
        }
        return toDTO(solicitudRepository.save(solicitud));
    }

    @Transactional
    public void cancelar(Long id) {
        SolicitudBeca solicitud = solicitudRepository.findByIdConRelaciones(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", id));
        if (solicitud.getEstado() == EstadoSolicitud.APROBADA
                || solicitud.getEstado() == EstadoSolicitud.DENEGADA
                || solicitud.getEstado() == EstadoSolicitud.CANCELADA) {
            throw new OperacionNoPermitidaException(
                    "No se puede cancelar una solicitud en estado " + solicitud.getEstado());
        }
        solicitud.setEstado(EstadoSolicitud.CANCELADA);
        solicitud.setFechaResolucion(LocalDateTime.now());
        solicitudRepository.save(solicitud);
    }

    @Transactional(readOnly = true)
    public Optional<SolicitudResponseDTO> buscarUltimaDeAlumno(Long alumnoId) {
        if (!alumnoRepository.existsById(alumnoId)) {
            throw new RecursoNoEncontradoException("Alumno", alumnoId);
        }
        return solicitudRepository.findFirstByAlumnoIdOrderByFechaSolicitudDesc(alumnoId)
                .map(this::toDTO);
    }

    private void validarTransicion(EstadoSolicitud actual, EstadoSolicitud nuevo) {
        Map<EstadoSolicitud, Set<EstadoSolicitud>> transiciones = Map.of(
                EstadoSolicitud.ENVIADA,
                Set.of(EstadoSolicitud.EN_REVISION, EstadoSolicitud.DENEGADA),
                EstadoSolicitud.EN_REVISION,
                Set.of(
                        EstadoSolicitud.SOLICITUD_DOCUMENTACION,
                        EstadoSolicitud.APROBADA,
                        EstadoSolicitud.DENEGADA));
        Set<EstadoSolicitud> permitidos = transiciones.getOrDefault(actual, Set.of());
        if (!permitidos.contains(nuevo)) {
            throw new OperacionNoPermitidaException(
                    "No se puede pasar de " + actual + " a " + nuevo);
        }
    }

    private SolicitudResponseDTO toDTO(SolicitudBeca solicitud) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setIdentificador(solicitud.getId() == null ? null : solicitud.getId().toString());
        dto.setEstado(solicitud.getEstado().name());
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setFechaResolucion(solicitud.getFechaResolucion());
        dto.setImporteConcedido(solicitud.getImporteConcedido());
        dto.setObservaciones(solicitud.getObservaciones());
        if (solicitud.getAlumno() != null) {
            AlumnoResumenDTO alumno = new AlumnoResumenDTO();
            alumno.setId(solicitud.getAlumno().getId().toString());
            alumno.setNombreCompleto(
                    solicitud.getAlumno().getNombre() + " " + solicitud.getAlumno().getApellidos());
            alumno.setDni(solicitud.getAlumno().getDni());
            dto.setAlumno(alumno);
        }
        if (solicitud.getBeca() != null) {
            BecaResumenDTO beca = new BecaResumenDTO();
            beca.setId(solicitud.getBeca().getId().toString());
            beca.setCodigo(solicitud.getBeca().getCodigo());
            beca.setNombre(solicitud.getBeca().getNombre());
            beca.setImporteMaximo(solicitud.getBeca().getImporteMaximo());
            dto.setBeca(beca);
        }
        dto.setNumeroDocumentos(solicitud.getDocumentos().size());
        return dto;
    }

    @Transactional(readOnly = true)
    public Page<SolicitudResponseDTO> listarPorAlumno(Long alumnoId, Pageable pageable) {
        if (!alumnoRepository.existsById(alumnoId)) {
            throw new RecursoNoEncontradoException("Alumno", alumnoId);
        }
    
        return solicitudRepository.findByAlumnoId(alumnoId, pageable)
                .map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contarPorEstado() {
        return solicitudRepository.contarPorEstado().stream()
                .collect(Collectors.toMap(
                        fila -> ((EstadoSolicitud) fila[0]).name(),
                        fila -> (Long) fila[1]
                ));
    }
}
