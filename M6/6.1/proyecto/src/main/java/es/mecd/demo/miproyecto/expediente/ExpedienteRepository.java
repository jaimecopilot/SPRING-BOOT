package es.mecd.demo.miproyecto.expediente;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
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
        return Optional.ofNullable(almacen.get(id));
    }

    public List<ExpedienteDTO> listarTodos() {
        return new ArrayList<>(almacen.values());
    }

    public ExpedienteDTO guardar(ExpedienteDTO expediente) {
        almacen.put(expediente.getIdentificador(), expediente);
        actualizarSecuencia(expediente.getIdentificador());
        return expediente;
    }

    public boolean eliminar(String id) { return almacen.remove(id) != null; }
    public int contar() { return almacen.size(); }
    public String siguienteIdentificador() { return String.valueOf(secuencia.incrementAndGet()); }

    private void actualizarSecuencia(String id) {
        if (id != null && id.matches("\\d+")) {
            secuencia.accumulateAndGet(Integer.parseInt(id), Math::max);
        }
    }
}
