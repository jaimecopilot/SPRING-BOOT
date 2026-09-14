package es.mecd.demo.miproyecto.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio de roles de seguridad. */
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
