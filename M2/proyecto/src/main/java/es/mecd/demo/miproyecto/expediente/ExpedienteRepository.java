package es.mecd.demo.miproyecto.expediente;

import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Repositorio en memoria de expedientes. */
@Repository
public class ExpedienteRepository {
    private final Map<String, ExpedienteDTO> almacen = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger(0);

    public Optional<ExpedienteDTO> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id)).map(this::copiar);
    }
    public List<ExpedienteDTO> listarTodos() {
        return almacen.values().stream().map(this::copiar)
                .sorted(Comparator.comparingInt(this::idNumerico)).toList();
    }
    public ExpedienteDTO guardar(ExpedienteDTO expediente) {
        ExpedienteDTO copia = copiar(expediente);
        almacen.put(copia.getIdentificador(), copia);
        actualizarSecuencia(copia.getIdentificador());
        return copiar(copia);
    }
    public boolean eliminar(String id) { return almacen.remove(id) != null; }
    public int contar() { return almacen.size(); }
    public String siguienteIdentificador() { return String.valueOf(secuencia.incrementAndGet()); }

    private void actualizarSecuencia(String id) {
        if (id != null && id.matches("\\d+")) secuencia.accumulateAndGet(Integer.parseInt(id), Math::max);
    }
    private int idNumerico(ExpedienteDTO expediente) {
        String id = expediente.getIdentificador();
        return id != null && id.matches("\\d+") ? Integer.parseInt(id) : Integer.MAX_VALUE;
    }
    private ExpedienteDTO copiar(ExpedienteDTO original) {
        ExpedienteDTO copia = new ExpedienteDTO(
                original.getIdentificador(), original.getTitular(), original.getDni(), original.getEstado(),
                original.getTipo(), original.getFechaSolicitud(), original.getImporte(), original.getActivo(),
                original.getDocumentos() == null ? null : List.copyOf(original.getDocumentos()));
        copia.setObservaciones(original.getObservaciones());
        copia.setNumeroSeguridadSocial(original.getNumeroSeguridadSocial());
        if (original.getSolicitante() != null) {
            SolicitanteDTO s = original.getSolicitante();
            copia.setSolicitante(new SolicitanteDTO(s.getNombre(), s.getApellidos(), s.getDocumentoIdentidad()));
        }
        return copia;
    }
}
