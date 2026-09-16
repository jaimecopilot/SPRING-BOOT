package es.mecd.demo.miproyecto.alumno;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class AlumnoResponseDTO {
    @JsonProperty("id") private String identificador;
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String curso;
    private int edad;
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
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}
