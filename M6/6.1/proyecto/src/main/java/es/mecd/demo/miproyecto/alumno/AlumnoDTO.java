package es.mecd.demo.miproyecto.alumno;

import java.time.LocalDate;
import java.util.List;

/** Modelo interno en memoria del recurso alumno. */
public class AlumnoDTO {
    private String identificador;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String curso;
    private List<String> documentos;
    private boolean eliminado;

    public AlumnoDTO() { }

    public AlumnoDTO(
            String identificador,
            String nombre,
            String apellidos,
            String dni,
            LocalDate fechaNacimiento,
            String curso) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
    }

    public String getIdentificador() { return identificador; }
    public void setIdentificador(String identificador) { this.identificador = identificador; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public List<String> getDocumentos() { return documentos; }
    public void setDocumentos(List<String> documentos) { this.documentos = documentos; }
    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }
}
