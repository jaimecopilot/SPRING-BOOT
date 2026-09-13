package es.mecd.demo.miproyecto.repository;

import es.mecd.demo.miproyecto.dto.ExpedienteDTO;
import es.mecd.demo.miproyecto.dto.SolicitanteDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ExpedienteRepository {

    private final Map<String, ExpedienteDTO> almacen = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger();

    public ExpedienteRepository() {
        guardar(new ExpedienteDTO(
                "1", "Ana García", "12345678A", "EN_TRAMITE", "BECA",
                LocalDate.of(2025, 1, 15), 1500.0, true, List.of("DNI.pdf")));
        guardar(new ExpedienteDTO(
                "2", "Luis Pérez", "87654321B", "RESUELTA", "AYUDA_LIBROS",
                LocalDate.of(2025, 1, 10), 300.0, true, List.of("Solicitud.pdf")));
    }

    public Optional<ExpedienteDTO> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id))
                .map(this::copiar);
    }

    public List<ExpedienteDTO> listarTodos() {
        List<ExpedienteDTO> resultado = almacen.values().stream()
                .map(this::copiar)
                .sorted(this::compararPorId)
                .toList();
        return new ArrayList<>(resultado);
    }

    public ExpedienteDTO guardar(ExpedienteDTO expediente) {
        ExpedienteDTO copia = copiar(expediente);
        almacen.put(copia.getIdentificador(), copia);
        actualizarSecuencia(copia.getIdentificador());
        return copiar(copia);
    }

    public boolean eliminar(String id) {
        return almacen.remove(id) != null;
    }

    public int contar() {
        return almacen.size();
    }

    public String siguienteIdentificador() {
        return String.valueOf(secuencia.incrementAndGet());
    }

    private void actualizarSecuencia(String id) {
        if (id != null && id.matches("\\d+")) {
            secuencia.accumulateAndGet(Integer.parseInt(id), Math::max);
        }
    }

    private int compararPorId(ExpedienteDTO a, ExpedienteDTO b) {
        return Integer.compare(idNumerico(a.getIdentificador()), idNumerico(b.getIdentificador()));
    }

    private int idNumerico(String id) {
        return id != null && id.matches("\\d+")
                ? Integer.parseInt(id)
                : Integer.MAX_VALUE;
    }

    private ExpedienteDTO copiar(ExpedienteDTO origen) {
        ExpedienteDTO copia = new ExpedienteDTO(
                origen.getIdentificador(),
                origen.getTitular(),
                origen.getDni(),
                origen.getEstado(),
                origen.getTipo(),
                origen.getFechaSolicitud(),
                origen.getImporte(),
                origen.getActivo(),
                origen.getDocumentos() == null ? null : List.copyOf(origen.getDocumentos()));
        copia.setObservaciones(origen.getObservaciones());
        copia.setNumeroSeguridadSocial(origen.getNumeroSeguridadSocial());
        copia.setSolicitante(copiarSolicitante(origen.getSolicitante()));
        return copia;
    }

    private SolicitanteDTO copiarSolicitante(SolicitanteDTO origen) {
        return origen == null
                ? null
                : new SolicitanteDTO(
                        origen.getNombre(),
                        origen.getApellidos(),
                        origen.getDocumentoIdentidad());
    }
}
