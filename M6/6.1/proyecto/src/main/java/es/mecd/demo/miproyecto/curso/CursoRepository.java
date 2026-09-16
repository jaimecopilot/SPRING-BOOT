package es.mecd.demo.miproyecto.curso;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Repositorio JPA de cursos. */
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Optional<Curso> findByNombreIgnoreCase(String nombre);

    Optional<Curso> findByNombre(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombre(String nombre);

    @Query("select distinct c from Curso c left join fetch c.alumnos")
    List<Curso> findAllConAlumnos();

    @Query(value = """
            select c.*
            from cursos c
            where (
                select count(*)
                from alumnos a
                where a.curso_id = c.id
            ) > (
                select avg(conteo.cantidad)
                from (
                    select c2.id, count(a2.id) as cantidad
                    from cursos c2
                    left join alumnos a2 on a2.curso_id = c2.id
                    group by c2.id
                ) conteo
            )
            """, nativeQuery = true)
    List<Curso> buscarCursosConMasAlumnosQueLaMedia();
}
