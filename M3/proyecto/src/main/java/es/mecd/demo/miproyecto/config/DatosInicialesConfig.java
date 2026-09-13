package es.mecd.demo.miproyecto.config;

import es.mecd.demo.miproyecto.alumno.AlumnoDTO;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.expediente.ExpedienteDTO;
import es.mecd.demo.miproyecto.expediente.ExpedienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.List;

/** Declara la carga reproducible de datos de demostración. */
@Configuration
public class DatosInicialesConfig {
    @Bean
    CommandLineRunner cargarDatos(AlumnoRepository alumnos, ExpedienteRepository expedientes) {
        return args -> {
            if (alumnos.contar() == 0) {
                alumnos.guardar(new AlumnoDTO(
                        "1", "Ana", "García López", "DNI-DEMO-01",
                        LocalDate.of(2010, 5, 12), "5º Primaria"));
                alumnos.guardar(new AlumnoDTO(
                        "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                        LocalDate.of(2009, 9, 3), "6º Primaria"));
            }
            if (expedientes.contar() == 0) {
                expedientes.guardar(new ExpedienteDTO(
                        "1", "Ana García", "12345678A", "EN_TRAMITE", "BECA",
                        LocalDate.of(2025, 1, 15), 1500.0, true, List.of("DNI.pdf")));
                expedientes.guardar(new ExpedienteDTO(
                        "2", "Luis Pérez", "87654321B", "RESUELTA", "AYUDA_LIBROS",
                        LocalDate.of(2025, 1, 10), 300.0, true, List.of("Solicitud.pdf")));
            }
        };
    }
}
