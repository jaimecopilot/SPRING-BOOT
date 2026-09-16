package es.mecd.demo.miproyecto.alumno;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AlumnoRequestDTO {
    @NotBlank @Size(max = 100) private String nombre;
    @NotBlank @Size(max = 150) private String apellidos;
    @NotBlank @Size(min = 9, max = 9) private String dni;
    @NotNull private LocalDate fechaNacimiento;
    @NotBlank private String curso;
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
}
