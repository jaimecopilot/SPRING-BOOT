package es.mecd.demo.miproyecto.solicitud;

public class AlumnoResumenDTO {
       private String id;
       private String nombreCompleto;
       private String dni;

       public String getId() { return id; }
       public void setId(String id) { this.id = id; }

       public String getNombreCompleto() { return nombreCompleto; }
       public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

       public String getDni() { return dni; }
       public void setDni(String dni) { this.dni = dni; }
}
