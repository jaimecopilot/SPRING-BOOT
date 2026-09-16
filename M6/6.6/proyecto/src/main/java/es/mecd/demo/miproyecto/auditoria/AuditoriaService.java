package es.mecd.demo.miproyecto.auditoria;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** Servicio que registra auditoría en una transacción independiente. */
@Service
public class AuditoriaService {
    private final AuditoriaRepository repositorio;

    public AuditoriaService(AuditoriaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String operacion, String detalle) {
        repositorio.save(new Auditoria(operacion, detalle));
    }
}
