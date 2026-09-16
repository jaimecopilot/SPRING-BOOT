package es.mecd.demo.miproyecto.documento;

import es.mecd.demo.miproyecto.solicitud.SolicitudBeca;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
public class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudBeca solicitud;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private Long tamano;

    @Lob
    @Column(name = "contenido")
    private byte[] contenido;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    public Documento() {
    }

    public Documento(String nombre, String tipo, Long tamano, byte[] contenido) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.tamano = tamano;
        this.contenido = contenido == null ? null : contenido.clone();
        this.fechaSubida = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public SolicitudBeca getSolicitud() { return solicitud; }
    public void setSolicitud(SolicitudBeca solicitud) { this.solicitud = solicitud; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Long getTamano() { return tamano; }
    public void setTamano(Long tamano) { this.tamano = tamano; }
    public byte[] getContenido() { return contenido == null ? null : contenido.clone(); }
    public void setContenido(byte[] contenido) { this.contenido = contenido == null ? null : contenido.clone(); }
    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
