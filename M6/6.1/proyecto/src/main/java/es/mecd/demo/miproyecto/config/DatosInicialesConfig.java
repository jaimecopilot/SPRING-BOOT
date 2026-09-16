package es.mecd.demo.miproyecto.config;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.curso.Curso;
import es.mecd.demo.miproyecto.curso.CursoRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/** Datos de ejemplo para el perfil de desarrollo. */
@Configuration
@Profile("dev")
public class DatosInicialesConfig {
    @Bean
    CommandLineRunner cargar(CursoRepository cursos, AlumnoRepository alumnos) {
        return args -> {
            if (alumnos.count() == 0) {
                Curso quinto = cursos.save(new Curso("5º Primaria"));
                Curso sexto = cursos.save(new Curso("6º Primaria"));
                alumnos.save(new Alumno(
                        "Ana", "García López", "12345678A",
                        LocalDate.of(2010, 5, 12), quinto));
                alumnos.save(new Alumno(
                        "Luis", "Pérez Ruiz", "87654321B",
                        LocalDate.of(2009, 9, 3), sexto));
            }
        };
    }
}
