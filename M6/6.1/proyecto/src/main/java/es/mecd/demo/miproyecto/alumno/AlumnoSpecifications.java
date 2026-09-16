package es.mecd.demo.miproyecto.alumno;

import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

/** Filtros dinámicos combinables para Alumno. */
public final class AlumnoSpecifications {
    private AlumnoSpecifications() {
    }

    public static Specification<Alumno> curso(String curso) {
        return (root, query, cb) -> curso == null || curso.isBlank()
                ? cb.conjunction()
                : cb.equal(
                        cb.lower(root.join("curso").get("nombre")),
                        curso.toLowerCase());
    }

    public static Specification<Alumno> estado(EstadoAlumno estado) {
        return (root, query, cb) -> estado == null
                ? cb.conjunction()
                : cb.equal(root.get("estado"), estado);
    }

    public static Specification<Alumno> dni(String dni) {
        return (root, query, cb) -> dni == null || dni.isBlank()
                ? cb.conjunction()
                : cb.equal(cb.lower(root.get("dni")), dni.toLowerCase());
    }

    public static Specification<Alumno> nacidoDespues(LocalDate fecha) {
        return (root, query, cb) -> fecha == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("fechaNacimiento"), fecha);
    }

    public static Specification<Alumno> nacidosEntre(
            LocalDate desde,
            LocalDate hasta) {
        return (root, query, cb) -> {
            if (desde != null && hasta != null) {
                return cb.between(root.get("fechaNacimiento"), desde, hasta);
            }
            if (desde != null) {
                return cb.greaterThanOrEqualTo(
                        root.get("fechaNacimiento"), desde);
            }
            if (hasta != null) {
                return cb.lessThanOrEqualTo(root.get("fechaNacimiento"), hasta);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Alumno> activos() {
        return (root, query, cb) -> cb.isFalse(root.get("eliminado"));
    }
}
