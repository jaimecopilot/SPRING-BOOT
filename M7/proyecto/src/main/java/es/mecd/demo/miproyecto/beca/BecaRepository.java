package es.mecd.demo.miproyecto.beca;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BecaRepository extends JpaRepository<Beca, Long> {
    Optional<Beca> findByNombre(String nombre);
    Optional<Beca> findByCodigo(String codigo);
    boolean existsByNombre(String nombre);
    boolean existsByCodigo(String codigo);
    List<Beca> findByActivaTrue();
    List<Beca> findByAnioAndActivaTrue(Integer anio);
    List<Beca> findByAnioBetween(Integer anioDesde, Integer anioHasta);

    @Query("SELECT b FROM Beca b "
            + "WHERE (:activa IS NULL OR b.activa = :activa) "
            + "AND (:anio IS NULL OR b.anio = :anio)")
    Page<Beca> buscarConFiltros(
            @Param("activa") Boolean activa,
            @Param("anio") Integer anio,
            Pageable pageable);
}
