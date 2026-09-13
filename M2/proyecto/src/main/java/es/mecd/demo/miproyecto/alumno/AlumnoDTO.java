package es.mecd.demo.miproyecto.alumno;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

/** DTO utilizado por la API de alumnos. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlumnoDTO {

    @JsonProperty("id")
    private String identificador;
    private String nombre;
    private String apellidos;
    private String dni;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String curso;
    private List<String> documentos;

    /** Constructor requerido por Jackson. */
    public AlumnoDTO() { }

    /** Construye un alumno.
     * @param identificador identificador
     * @param nombre nombre
     * @param apellidos apellidos
     * @param dni documento de identidad
     * @param fechaNacimiento fecha de nacimiento
     * @param curso curso
     */
    public AlumnoDTO(String identificador, String nombre, String apellidos, String dni,
                     LocalDate fechaNacimiento, String curso) {
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
}
