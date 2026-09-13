package es.mecd.demo.miproyecto.alumno;

import org.springframework.stereotype.Repository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/** Repositorio en memoria de alumnos. */
@Repository
public class AlumnoRepository {
    private final Map<String, AlumnoDTO> almacen = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger(0);

    public Optional<AlumnoDTO> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id)).map(this::copiar);
    }

    public List<AlumnoDTO> listarTodos() {
        return almacen.values().stream()
                .map(this::copiar)
                .sorted(Comparator.comparingInt(this::idNumerico))
                .toList();
    }

    public boolean existePorDni(String dni) {
        return almacen.values().stream()
                .anyMatch(a -> !a.isEliminado() && Objects.equals(a.getDni(), dni));
    }

    public boolean existePorDniYIdDistinto(String dni, String id) {
        return almacen.values().stream()
                .anyMatch(a -> !a.isEliminado()
                        && !Objects.equals(a.getIdentificador(), id)
                        && Objects.equals(a.getDni(), dni));
    }

    public AlumnoDTO guardar(AlumnoDTO alumno) {
        AlumnoDTO copia = copiar(alumno);
        almacen.put(copia.getIdentificador(), copia);
        actualizarSecuencia(copia.getIdentificador());
        return copiar(copia);
    }

    public boolean eliminar(String id) { return almacen.remove(id) != null; }
    public int contar() { return almacen.size(); }
    public String siguienteIdentificador() { return String.valueOf(secuencia.incrementAndGet()); }

    private void actualizarSecuencia(String id) {
        if (id != null && id.matches("\\d+")) {
            secuencia.accumulateAndGet(Integer.parseInt(id), Math::max);
        }
    }

    private int idNumerico(AlumnoDTO alumno) {
        String id = alumno.getIdentificador();
        return id != null && id.matches("\\d+") ? Integer.parseInt(id) : Integer.MAX_VALUE;
    }

    private AlumnoDTO copiar(AlumnoDTO original) {
        AlumnoDTO copia = new AlumnoDTO(
                original.getIdentificador(),
                original.getNombre(),
                original.getApellidos(),
                original.getDni(),
                original.getFechaNacimiento(),
                original.getCurso());
        copia.setEliminado(original.isEliminado());
        copia.setDocumentos(
                original.getDocumentos() == null ? null : List.copyOf(original.getDocumentos()));
        return copia;
    }
}
