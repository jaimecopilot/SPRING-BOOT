package es.mecd.demo.miproyecto.alumno;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    boolean existsByDni(String dni);
    Optional<Alumno> findByDni(String dni);

    @Query("SELECT a FROM Alumno a JOIN a.curso c "
            + "WHERE (:curso IS NULL OR LOWER(c.nombre) = LOWER(:curso))")
    Page<Alumno> buscar(@Param("curso") String curso, Pageable pageable);
}
