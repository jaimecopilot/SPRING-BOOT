package es.mecd.demo.miproyecto.solicitud;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.documento.Documento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes")
public class SolicitudBeca {

       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;

       @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "alumno_id", nullable = false)
private Alumno alumno;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "beca_id", nullable = false)
private Beca beca;

@Enumerated(EnumType.STRING)
@Column(nullable = false, length = 30)
private EstadoSolicitud estado = EstadoSolicitud.BORRADOR;

@Column(name = "fecha_solicitud", nullable = false)
private LocalDateTime fechaSolicitud;

@Column(name = "fecha_resolucion")
private LocalDateTime fechaResolucion;

@Column(name = "importe_concedido", precision = 10, scale = 2)
private BigDecimal importeConcedido;

@Column(length = 1000)
private String observaciones;

@OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL,
           orphanRemoval = true, fetch = FetchType.LAZY)
private List<Documento> documentos = new ArrayList<>();

public SolicitudBeca() {
}

public void addDocumento(Documento documento) {
    documentos.add(documento);
    documento.setSolicitud(this);
}

public void removeDocumento(Documento documento) {
    documentos.remove(documento);
    documento.setSolicitud(null);
}

public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

public Alumno getAlumno() { return alumno; }
public void setAlumno(Alumno alumno) { this.alumno = alumno; }

public Beca getBeca() { return beca; }
public void setBeca(Beca beca) { this.beca = beca; }

public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public BigDecimal getImporteConcedido() { return importeConcedido; }
    public void setImporteConcedido(BigDecimal importeConcedido) { this.importeConcedido = importeConcedido; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<Documento> getDocumentos() { return documentos; }
    public void setDocumentos(List<Documento> documentos) { this.documentos = documentos; }
}
