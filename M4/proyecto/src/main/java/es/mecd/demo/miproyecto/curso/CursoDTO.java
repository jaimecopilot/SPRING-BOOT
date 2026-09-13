package es.mecd.demo.miproyecto.curso;

import com.fasterxml.jackson.annotation.JsonProperty;

/** DTO de curso con el número de alumnos calculado. */
public class CursoDTO {
    @JsonProperty("id")
    private Long id;
    private String nombre;
    private int numeroAlumnos;

    public CursoDTO() {
    }

    public CursoDTO(Long id, String nombre, int numeroAlumnos) {
        this.id = id;
        this.nombre = nombre;
        this.numeroAlumnos = numeroAlumnos;
    }

    public Long id() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public String getIdentificador() {
        return id == null ? null : id.toString();
    }

    public void setIdentificador(String identificador) {
        this.id = identificador == null ? null : Long.valueOf(identificador);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroAlumnos() {
        return numeroAlumnos;
    }

    public void setNumeroAlumnos(int numeroAlumnos) {
        this.numeroAlumnos = numeroAlumnos;
    }
}
