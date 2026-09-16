package es.mecd.demo.miproyecto.auditoria;

import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio JPA para entradas de auditoría. */
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}
