package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.curso.Curso;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/** Entidad JPA principal del módulo. */
@Entity
@Table(name = "alumnos", uniqueConstraints = @UniqueConstraint(name = "uk_alumno_dni", columnNames = "dni"))
public class Alumno {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100) private String nombre;
    @Column(nullable = false, length = 150) private String apellidos;
    @Column(nullable = false, unique = true, length = 9) private String dni;
    @Column(name = "fecha_nacimiento") private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private EstadoAlumno estado = EstadoAlumno.ACTIVO;
    @Embedded private Direccion direccion;
    @Lob private String observaciones;
    @Lob private byte[] foto;
    @Version private Long version;
    @Column(nullable = false) private boolean eliminado;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    protected Alumno() { }
    public Alumno(
            String nombre,
            String apellidos,
            String dni,
            LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Alumno(
            String nombre,
            String apellidos,
            String dni,
            LocalDate fechaNacimiento,
            Curso curso) {
        this(nombre, apellidos, dni, fechaNacimiento);
        this.curso = curso;
    }
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public EstadoAlumno getEstado() { return estado; }
    public void setEstado(EstadoAlumno estado) { this.estado = estado; }
    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public byte[] getFoto() { return foto == null ? null : foto.clone(); }
    public void setFoto(byte[] foto) { this.foto = foto == null ? null : foto.clone(); }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }
    @Transient public int getEdad() {
        return fechaNacimiento == null ? 0 : java.time.Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alumno otro)) return false;
        return id != null && Objects.equals(id, otro.id);
    }
    @Override public int hashCode() { return getClass().hashCode(); }
}
