package es.mecd.demo.miproyecto.alumno;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Repositorio mínimo para demostrar dependencias en DELETE. */
@Repository
public class DocumentoRepository {
    private final Map<String, String> documentos = new ConcurrentHashMap<>();

    public DocumentoRepository() {
        documentos.put("doc1", "1");
        documentos.put("doc2", "1");
        documentos.put("doc3", "2");
    }

    public boolean existePorAlumnoId(String id) {
        return documentos.values().stream().anyMatch(id::equals);
    }

    public void eliminarPorAlumnoId(String id) {
        documentos.entrySet().removeIf(entry -> entry.getValue().equals(id));
    }
}
