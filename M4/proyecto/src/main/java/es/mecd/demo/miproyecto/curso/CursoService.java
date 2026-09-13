package es.mecd.demo.miproyecto.curso;

import es.mecd.demo.miproyecto.common.exception.NegocioException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Servicio transaccional de cursos. */
@Service
public class CursoService {
    private final CursoRepository repositorio;

    public CursoService(CursoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> listar() {
        return repositorio.findAllConAlumnos().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CursoDTO> consultar(Long id) {
        return repositorio.findById(id).map(this::toDto);
    }

    @Transactional
    public CursoDTO crear(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio");
        }
        if (repositorio.existsByNombreIgnoreCase(nombre)) {
            throw new NegocioException(
                    "Ya existe un curso con el nombre " + nombre);
        }
        return toDto(repositorio.save(new Curso(nombre)));
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            return false;
        }
        repositorio.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<CursoDTO> cursosConMasAlumnosQueLaMedia() {
        return repositorio.buscarCursosConMasAlumnosQueLaMedia().stream()
                .map(this::toDto)
                .toList();
    }

    private CursoDTO toDto(Curso curso) {
        return new CursoDTO(
                curso.getId(),
                curso.getNombre(),
                curso.getAlumnos().size());
    }
}
