package es.mecd.demo.miproyecto.curso;

import es.mecd.demo.miproyecto.alumno.AlumnoResponseDTO;
import es.mecd.demo.miproyecto.alumno.AlumnoService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** API REST de cursos. */
@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {
    private final CursoService service;
    private final AlumnoService alumnos;

    public CursoController(CursoService service, AlumnoService alumnos) {
        this.service = service;
        this.alumnos = alumnos;
    }

    @GetMapping
    public List<CursoDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> consultar(@PathVariable Long id) {
        return service.consultar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crear(
            @RequestParam(required = false) String nombre,
            @RequestBody(required = false) Map<String, String> body) {
        String valor = nombre;
        if ((valor == null || valor.isBlank()) && body != null) {
            valor = body.get("nombre");
        }
        CursoDTO creado = service.crear(valor);
        URI location = URI.create("/api/v1/cursos/" + creado.id());
        return ResponseEntity.created(location).body(creado);
    }

    @GetMapping("/{id}/alumnos")
    public List<AlumnoResponseDTO> listarAlumnos(@PathVariable Long id) {
        return alumnos.listarPorCurso(id);
    }

    @GetMapping("/estadisticas/mas-alumnos-que-media")
    public List<CursoDTO> cursosConMasAlumnosQueLaMedia() {
        return service.cursosConMasAlumnosQueLaMedia();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
