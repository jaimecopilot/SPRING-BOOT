package es.mecd.demo.miproyecto.repository;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AlumnoRepository {

    private final Map<String, AlumnoDTO> almacen = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger();

    public AlumnoRepository() {
        guardar(new AlumnoDTO(
                "1", "Ana", "García López", "DNI-DEMO-01",
                LocalDate.of(2010, 5, 12), "5º Primaria"));
        guardar(new AlumnoDTO(
                "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                LocalDate.of(2009, 9, 3), "6º Primaria"));
    }

    public Optional<AlumnoDTO> buscarPorId(String id) {
        return Optional.ofNullable(almacen.get(id))
                .map(this::copiar);
    }

    public List<AlumnoDTO> listarTodos() {
        List<AlumnoDTO> resultado = almacen.values().stream()
                .map(this::copiar)
                .sorted(this::compararPorId)
                .toList();
        return new ArrayList<>(resultado);
    }

    public Optional<AlumnoDTO> buscarPorDni(String dni) {
        return almacen.values().stream()
                .filter(a -> Objects.equals(a.getDni(), dni))
                .findFirst()
                .map(this::copiar);
    }

    public boolean existePorDni(String dni) {
        return almacen.values().stream()
                .anyMatch(a -> Objects.equals(a.getDni(), dni));
    }

    public AlumnoDTO guardar(AlumnoDTO alumno) {
        AlumnoDTO copia = copiar(alumno);
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

    private int compararPorId(AlumnoDTO a, AlumnoDTO b) {
        return Integer.compare(idNumerico(a.getIdentificador()), idNumerico(b.getIdentificador()));
    }

    private int idNumerico(String id) {
        return id != null && id.matches("\\d+")
                ? Integer.parseInt(id)
                : Integer.MAX_VALUE;
    }

    private AlumnoDTO copiar(AlumnoDTO origen) {
        AlumnoDTO copia = new AlumnoDTO(
                origen.getIdentificador(),
                origen.getNombre(),
                origen.getApellidos(),
                origen.getDni(),
                origen.getFechaNacimiento(),
                origen.getCurso());
        if (origen.getDocumentos() != null) {
            copia.setDocumentos(List.copyOf(origen.getDocumentos()));
        }
        return copia;
    }
}
