package es.mecd.demo.miproyecto.solicitud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface SolicitudBecaRepository extends JpaRepository<SolicitudBeca, Long> {

    // Consultas derivadas simples

    List<SolicitudBeca> findByAlumnoId(Long alumnoId);

    List<SolicitudBeca> findByBecaId(Long becaId);

    List<SolicitudBeca> findByEstado(EstadoSolicitud estado);

    Page<SolicitudBeca> findByEstado(EstadoSolicitud estado, Pageable pageable);

    List<SolicitudBeca> findByFechaSolicitudBetween(LocalDateTime desde, LocalDateTime hasta);

    boolean existsByAlumnoIdAndBecaIdAndFechaSolicitudBetween(
            Long alumnoId, Long becaId,
            LocalDateTime desde, LocalDateTime hasta);

    boolean existsByBecaId(Long becaId);

@Query("SELECT s FROM SolicitudBeca s "
            + "JOIN FETCH s.alumno "
            + "JOIN FETCH s.beca "
            + "WHERE s.id = :id")
    Optional<SolicitudBeca> findByIdConRelaciones(@Param("id") Long id);

    @Query("SELECT s FROM SolicitudBeca s JOIN FETCH s.alumno JOIN FETCH s.beca")
    List<SolicitudBeca> findAllConRelaciones();

    @Query(
            value = "SELECT s FROM SolicitudBeca s "
                    + "JOIN FETCH s.alumno a "
                    + "JOIN FETCH s.beca b "
                    + "WHERE (:estado IS NULL OR s.estado = :estado) "
                    + "AND (:alumnoId IS NULL OR a.id = :alumnoId) "
                    + "AND (:becaId IS NULL OR b.id = :becaId)",
            countQuery = "SELECT COUNT(s) FROM SolicitudBeca s "
                    + "WHERE (:estado IS NULL OR s.estado = :estado) "
                    + "AND (:alumnoId IS NULL OR s.alumno.id = :alumnoId) "
                    + "AND (:becaId IS NULL OR s.beca.id = :becaId)")
    Page<SolicitudBeca> buscarConFiltros(
            @Param("estado") EstadoSolicitud estado,
            @Param("alumnoId") Long alumnoId,
            @Param("becaId") Long becaId,
            Pageable pageable);

@EntityGraph(attributePaths = {"alumno", "beca"})
       Page<SolicitudBeca> findAll(Pageable pageable);

       @EntityGraph(attributePaths = {"alumno", "beca"})
       Page<SolicitudBeca> findByAlumnoId(Long alumnoId, Pageable pageable);

       @EntityGraph(attributePaths = {"alumno", "beca"})
       Page<SolicitudBeca> findByBecaId(Long becaId, Pageable pageable);

@Query("SELECT s.estado, COUNT(s) FROM SolicitudBeca s GROUP BY s.estado")
       List<Object[]> contarPorEstado();

       @Query("SELECT s.beca.nombre, COUNT(s) FROM SolicitudBeca s " +
              "GROUP BY s.beca.nombre ORDER BY COUNT(s) DESC")
       List<Object[]> contarPorBeca();

    @Query("SELECT s FROM SolicitudBeca s " +
           "JOIN FETCH s.alumno " +
           "JOIN FETCH s.beca " +
           "WHERE LOWER(s.observaciones) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<SolicitudBeca> buscarPorTextoEnObservaciones(@Param("texto") String texto);

    Optional<SolicitudBeca> findFirstByAlumnoIdOrderByFechaSolicitudDesc(Long alumnoId);
}
