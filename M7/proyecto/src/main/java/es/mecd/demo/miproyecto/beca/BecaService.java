package es.mecd.demo.miproyecto.beca;

import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import es.mecd.demo.miproyecto.common.exception.ValidacionNegocioException;
import es.mecd.demo.miproyecto.solicitud.SolicitudBecaRepository;
import java.time.Year;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BecaService {
    private final BecaRepository becaRepository;
    private final SolicitudBecaRepository solicitudRepository;

    public BecaService(BecaRepository becaRepository, SolicitudBecaRepository solicitudRepository) {
        this.becaRepository = becaRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public Page<BecaResponseDTO> listar(Boolean activa, Integer anio, Pageable pageable) {
        return becaRepository.buscarConFiltros(activa, anio, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public BecaResponseDTO consultar(Long id) {
        return becaRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beca", id));
    }

    @Transactional
    public BecaResponseDTO crear(BecaRequestDTO request) {
        validarAnio(request.getAnio());
        validarDuplicados(null, request);
        Beca beca = new Beca(
                request.getCodigo(),
                request.getNombre(),
                request.getDescripcion(),
                request.getImporteMaximo(),
                request.getAnio());
        return toDTO(becaRepository.save(beca));
    }

    @Transactional
    public BecaResponseDTO actualizar(Long id, BecaRequestDTO request) {
        Beca beca = becaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beca", id));
        validarAnio(request.getAnio());
        validarDuplicados(beca, request);
        beca.setCodigo(request.getCodigo());
        beca.setNombre(request.getNombre());
        beca.setDescripcion(request.getDescripcion());
        beca.setImporteMaximo(request.getImporteMaximo());
        beca.setAnio(request.getAnio());
        return toDTO(becaRepository.save(beca));
    }

    @Transactional
    public void eliminar(Long id) {
        Beca beca = becaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Beca", id));
        if (solicitudRepository.existsByBecaId(id)) {
            throw new OperacionNoPermitidaException(
                    "No se puede eliminar la beca " + id + ": tiene solicitudes asociadas");
        }
        becaRepository.delete(beca);
    }

    private void validarAnio(Integer anio) {
        int actual = Year.now().getValue();
        if (anio == null || anio < actual || anio > actual + 1) {
            throw new ValidacionNegocioException(
                    "anio",
                    "El año de la beca debe ser el actual o el siguiente");
        }
    }

    private void validarDuplicados(Beca actual, BecaRequestDTO request) {
        if ((actual == null || !actual.getNombre().equals(request.getNombre()))
                && becaRepository.existsByNombre(request.getNombre())) {
            throw new RecursoDuplicadoException("Beca", "nombre", request.getNombre());
        }
        if ((actual == null || !actual.getCodigo().equals(request.getCodigo()))
                && becaRepository.existsByCodigo(request.getCodigo())) {
            throw new RecursoDuplicadoException("Beca", "codigo", request.getCodigo());
        }
    }

    private BecaResponseDTO toDTO(Beca beca) {
        BecaResponseDTO dto = new BecaResponseDTO();
        dto.setIdentificador(beca.getId() == null ? null : beca.getId().toString());
        dto.setCodigo(beca.getCodigo());
        dto.setNombre(beca.getNombre());
        dto.setDescripcion(beca.getDescripcion());
        dto.setImporteMaximo(beca.getImporteMaximo());
        dto.setAnio(beca.getAnio());
        dto.setActiva(beca.getActiva());
        return dto;
    }
}
