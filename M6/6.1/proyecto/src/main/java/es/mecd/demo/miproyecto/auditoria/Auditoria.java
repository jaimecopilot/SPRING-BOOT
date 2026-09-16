package es.mecd.demo.miproyecto.auditoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/** Entrada persistente de auditoría. */
@Entity
@Table(name = "auditorias")
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String operacion;

    @Column(nullable = false, length = 500)
    private String detalle;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    protected Auditoria() {
    }

    public Auditoria(String operacion, String detalle) {
        this.operacion = operacion;
        this.detalle = detalle;
    }

    public Long getId() {
        return id;
    }

    public String getOperacion() {
        return operacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
