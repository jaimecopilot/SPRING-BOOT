package es.mecd.demo.miproyecto.alumno;

import org.springframework.stereotype.Repository;
import java.util.HashSet;
import java.util.Set;

/** Soporte en memoria heredado: se persistirá en un módulo posterior. */
@Repository
public class DocumentoRepository {
    private final Set<String> alumnosConDocumentos = new HashSet<>();
    public boolean existePorAlumnoId(String id) { return alumnosConDocumentos.contains(id); }
    public void eliminarPorAlumnoId(String id) { alumnosConDocumentos.remove(id); }
}
