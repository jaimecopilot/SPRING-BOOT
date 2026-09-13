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

    /** Busca un alumno por identificador.
     * @param id identificador
     * @return copia del alumno o vacío
     */
    public Optional<AlumnoDTO> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id)).map(this::copiar);
    }

    /** Lista todos los alumnos sin exponer el estado interno.
     * @return copias ordenadas por identificador
     */
    public List<AlumnoDTO> listarTodos() {
        return almacen.values().stream()
                .map(this::copiar)
                .sorted(Comparator.comparingInt(this::idNumerico))
                .toList();
    }

    /** Busca por DNI.
     * @param dni documento
     * @return alumno o vacío
     */
    public Optional<AlumnoDTO> buscarPorDni(String dni) {
        return almacen.values().stream()
                .filter(a -> Objects.equals(a.getDni(), dni))
                .findFirst()
                .map(this::copiar);
    }

    /** Comprueba si existe un DNI.
     * @param dni documento
     * @return true si existe
     */
    public boolean existePorDni(String dni) {
        return almacen.values().stream().anyMatch(a -> Objects.equals(a.getDni(), dni));
    }

    /** Guarda o sustituye un alumno.
     * @param alumno alumno
     * @return copia almacenada
     */
    public AlumnoDTO guardar(AlumnoDTO alumno) {
        AlumnoDTO copia = copiar(alumno);
        almacen.put(copia.getIdentificador(), copia);
        actualizarSecuencia(copia.getIdentificador());
        return copiar(copia);
    }

    /** Elimina por ID.
     * @param id identificador
     * @return true si existía
     */
    public boolean eliminar(String id) { return almacen.remove(id) != null; }

    /** @return número de alumnos almacenados */
    public int contar() { return almacen.size(); }

    /** @return siguiente identificador monotónico */
    public String siguienteIdentificador() { return String.valueOf(secuencia.incrementAndGet()); }

    private void actualizarSecuencia(String id) {
        if (id != null && id.matches("\d+")) {
            secuencia.accumulateAndGet(Integer.parseInt(id), Math::max);
        }
    }

    private int idNumerico(AlumnoDTO alumno) {
        String id = alumno.getIdentificador();
        return id != null && id.matches("\d+") ? Integer.parseInt(id) : Integer.MAX_VALUE;
    }

    private AlumnoDTO copiar(AlumnoDTO original) {
        AlumnoDTO copia = new AlumnoDTO(
                original.getIdentificador(), original.getNombre(), original.getApellidos(), original.getDni(),
                original.getFechaNacimiento(), original.getCurso());
        copia.setDocumentos(original.getDocumentos() == null ? null : List.copyOf(original.getDocumentos()));
        return copia;
    }
}
