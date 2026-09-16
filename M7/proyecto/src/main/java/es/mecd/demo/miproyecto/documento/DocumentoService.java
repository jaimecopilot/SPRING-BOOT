package es.mecd.demo.miproyecto.documento;

import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import es.mecd.demo.miproyecto.common.exception.ValidacionNegocioException;
import es.mecd.demo.miproyecto.solicitud.EstadoSolicitud;
import es.mecd.demo.miproyecto.solicitud.SolicitudBeca;
import es.mecd.demo.miproyecto.solicitud.SolicitudBecaRepository;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentoService {
    private static final long TAMANO_MAXIMO = 10L * 1024 * 1024;
    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "application/pdf", "image/jpeg", "image/png");

    private final DocumentoRepository documentoRepository;
    private final SolicitudBecaRepository solicitudRepository;

    public DocumentoService(
            DocumentoRepository documentoRepository,
            SolicitudBecaRepository solicitudRepository) {
        this.documentoRepository = documentoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional(readOnly = true)
    public List<DocumentoResponseDTO> listarPorSolicitud(Long solicitudId) {
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new RecursoNoEncontradoException("Solicitud", solicitudId);
        }
        return documentoRepository.findBySolicitudId(solicitudId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public DocumentoResponseDTO anadir(Long solicitudId, MultipartFile fichero) {
        SolicitudBeca solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", solicitudId));
        validarEditable(solicitud);
        if (fichero == null || fichero.isEmpty()) {
            throw new ValidacionNegocioException("fichero", "El fichero es obligatorio");
        }
        if (fichero.getSize() > TAMANO_MAXIMO) {
            throw new ValidacionNegocioException("fichero", "El fichero supera el tamaño máximo de 10 MB");
        }
        if (!TIPOS_PERMITIDOS.contains(fichero.getContentType())) {
            throw new ValidacionNegocioException(
                    "fichero",
                    "El tipo de fichero no está permitido. Tipos válidos: PDF, JPG, PNG");
        }
        try {
            Documento documento = new Documento(
                    fichero.getOriginalFilename(),
                    fichero.getContentType(),
                    fichero.getSize(),
                    fichero.getBytes());
            solicitud.addDocumento(documento);
            Documento guardado = documentoRepository.saveAndFlush(documento);
            return toDTO(guardado);
        } catch (IOException ex) {
            throw new IllegalStateException("Error al leer el fichero", ex);
        }
    }

    @Transactional
    public void eliminar(Long solicitudId, Long documentoId) {
        SolicitudBeca solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud", solicitudId));
        validarEditable(solicitud);
        Documento documento = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Documento", documentoId));
        if (documento.getSolicitud() == null
                || !solicitudId.equals(documento.getSolicitud().getId())) {
            throw new RecursoNoEncontradoException("Documento", documentoId);
        }
        documentoRepository.delete(documento);
    }

    private void validarEditable(SolicitudBeca solicitud) {
        if (solicitud.getEstado() != EstadoSolicitud.BORRADOR
                && solicitud.getEstado() != EstadoSolicitud.ENVIADA) {
            throw new OperacionNoPermitidaException(
                    "No se pueden modificar documentos de una solicitud en estado "
                            + solicitud.getEstado());
        }
    }

    private DocumentoResponseDTO toDTO(Documento documento) {
        DocumentoResponseDTO dto = new DocumentoResponseDTO();
        dto.setIdentificador(documento.getId() == null ? null : documento.getId().toString());
        dto.setNombre(documento.getNombre());
        dto.setTipo(documento.getTipo());
        dto.setTamano(documento.getTamano());
        dto.setFechaSubida(documento.getFechaSubida());
        return dto;
    }
}
