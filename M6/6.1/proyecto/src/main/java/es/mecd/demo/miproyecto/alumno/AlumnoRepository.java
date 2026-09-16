package es.mecd.demo.miproyecto.alumno;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Repositorio Spring Data JPA de alumnos. */
public interface AlumnoRepository
        extends JpaRepository<Alumno, Long>, JpaSpecificationExecutor<Alumno> {

    Optional<Alumno> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdNot(String dni, Long id);

    List<Alumno> findByCursoNombreIgnoreCaseAndEliminadoFalse(String curso);

    List<Alumno> findByCursoIdAndEliminadoFalse(Long cursoId);

    List<Alumno> findByEstadoAndEliminadoFalse(EstadoAlumno estado);

    List<Alumno> findByCursoNombreIgnoreCaseAndEstadoAndEliminadoFalse(
            String curso,
            EstadoAlumno estado);

    List<Alumno> findByFechaNacimientoBetweenAndEliminadoFalse(
            LocalDate desde,
            LocalDate hasta);

    List<Alumno> findByNombreContainingIgnoreCaseAndEliminadoFalse(String texto);

    List<Alumno> findByCursoNombreIgnoreCaseAndEliminadoFalseOrderByApellidosAsc(
            String curso);

    long countByCursoNombreIgnoreCaseAndEliminadoFalse(String curso);

    Page<Alumno> findByEliminadoFalse(Pageable pageable);

    @Query("select a from Alumno a join fetch a.curso "
            + "where a.id = :id and a.eliminado = false")
    Optional<Alumno> buscarDetalle(@Param("id") Long id);

    @Query("select a from Alumno a "
            + "where lower(a.curso.nombre) = lower(:curso) "
            + "and a.fechaNacimiento > :fecha "
            + "and a.eliminado = false")
    List<Alumno> buscarPorCursoYNacidosDespues(
            @Param("curso") String curso,
            @Param("fecha") LocalDate fecha);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Alumno a set a.estado = :estado "
            + "where lower(a.curso.nombre) = lower(:curso)")
    int actualizarEstadoPorCurso(
            @Param("curso") String curso,
            @Param("estado") EstadoAlumno estado);

    @Query("select a from Alumno a join a.curso c "
            + "where lower(c.nombre) = lower(:nombre) "
            + "and a.eliminado = false")
    List<Alumno> buscarPorNombreCurso(@Param("nombre") String nombre);

    @Query("select a from Alumno a join fetch a.curso c "
            + "where lower(c.nombre) = lower(:nombre) "
            + "and a.eliminado = false")
    List<Alumno> buscarPorNombreCursoConCurso(@Param("nombre") String nombre);

    @Query("select a from Alumno a "
            + "where lower(a.nombre) = lower(:nombre) "
            + "and a.eliminado = false")
    List<Alumno> buscarPorNombreIgnoreCase(@Param("nombre") String nombre);

    @Query("select a from Alumno a "
            + "where year(a.fechaNacimiento) = :anio "
            + "and a.eliminado = false")
    List<Alumno> buscarPorAnioNacimiento(@Param("anio") int anio);

    @Query("select a.curso.nombre, count(a) from Alumno a "
            + "where a.eliminado = false group by a.curso.nombre")
    List<Object[]> contarPorCurso();

    @Query("select a.curso.nombre, count(a) from Alumno a "
            + "where a.eliminado = false "
            + "group by a.curso.nombre having count(a) > :minimo")
    List<Object[]> contarCursosConMasDe(@Param("minimo") long minimo);

    @Query("select new es.mecd.demo.miproyecto.alumno.AlumnoResumenDTO("
            + "a.id, concat(a.nombre, ' ', a.apellidos), a.curso.nombre) "
            + "from Alumno a where lower(a.curso.nombre) = lower(:curso) "
            + "and a.eliminado = false")
    List<AlumnoResumenDTO> buscarResumenPorCurso(@Param("curso") String curso);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Alumno a where a.id = :id")
    Optional<Alumno> findByIdConBloqueo(@Param("id") Long id);
}
