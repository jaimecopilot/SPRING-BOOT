package es.mecd.demo.miproyecto.config;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.alumno.Curso;
import es.mecd.demo.miproyecto.alumno.CursoRepository;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.beca.BecaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class DatosInicialesBecasConfig {
    @Bean @Profile("dev")
    CommandLineRunner cargarDatos(BecaRepository becas, CursoRepository cursos, AlumnoRepository alumnos) {
        return args -> {
            int anio = Year.now().getValue();
            if (becas.count() == 0) {
                becas.save(new Beca("BECA-GEN-" + anio, "Beca General",
                        "Ayuda económica general para estudiantes", new BigDecimal("1500.00"), anio));
                becas.save(new Beca("LIBROS-" + anio, "Ayuda de Libros",
                        "Ayuda para la compra de libros de texto", new BigDecimal("300.00"), anio));
                becas.save(new Beca("COMEDOR-" + anio, "Beca de Comedor",
                        "Ayuda para el servicio de comedor escolar", new BigDecimal("800.00"), anio));
            }
            if (alumnos.count() == 0) {
                Curso curso = cursos.findByNombre("5º Primaria").orElseGet(() -> cursos.save(new Curso("5º Primaria")));
                alumnos.save(new Alumno("Ana", "García", "12345678A", LocalDate.of(2010, 5, 12), curso));
            }
        };
    }
}
