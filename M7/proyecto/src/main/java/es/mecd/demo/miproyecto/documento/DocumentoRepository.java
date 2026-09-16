package es.mecd.demo.miproyecto.documento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

       List<Documento> findBySolicitudId(Long solicitudId);

       long countBySolicitudId(Long solicitudId);

       void deleteBySolicitudId(Long solicitudId);
}
