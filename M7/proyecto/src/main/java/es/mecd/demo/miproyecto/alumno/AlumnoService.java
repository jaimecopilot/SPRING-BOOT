package es.mecd.demo.miproyecto.alumno;

import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import es.mecd.demo.miproyecto.common.exception.RecursoNoEncontradoException;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlumnoService {
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    public AlumnoService(AlumnoRepository alumnoRepository, CursoRepository cursoRepository) {
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
    }
    @Transactional(readOnly = true)
    public Page<AlumnoResponseDTO> listar(String curso, Pageable pageable) {
        return alumnoRepository.buscar(curso, pageable).map(this::toDTO);
    }
    @Transactional(readOnly = true)
    public AlumnoResponseDTO consultar(Long id) {
        return alumnoRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno", id));
    }
    @Transactional
    public AlumnoResponseDTO crear(AlumnoRequestDTO request) {
        if (alumnoRepository.existsByDni(request.getDni())) {
            throw new RecursoDuplicadoException("Alumno", "dni", request.getDni());
        }
        Curso curso = cursoRepository.findByNombre(request.getCurso())
                .orElseGet(() -> cursoRepository.save(new Curso(request.getCurso())));
        return toDTO(alumnoRepository.save(new Alumno(
                request.getNombre(), request.getApellidos(), request.getDni(),
                request.getFechaNacimiento(), curso)));
    }
    @Transactional
    public AlumnoResponseDTO actualizar(Long id, AlumnoRequestDTO request) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno", id));
        Curso curso = cursoRepository.findByNombre(request.getCurso())
                .orElseGet(() -> cursoRepository.save(new Curso(request.getCurso())));
        alumno.setNombre(request.getNombre());
        alumno.setApellidos(request.getApellidos());
        alumno.setDni(request.getDni());
        alumno.setFechaNacimiento(request.getFechaNacimiento());
        alumno.setCurso(curso);
        return toDTO(alumnoRepository.save(alumno));
    }
    @Transactional
    public void eliminar(Long id) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alumno", id));
        alumnoRepository.delete(alumno);
    }
    private AlumnoResponseDTO toDTO(Alumno alumno) {
        AlumnoResponseDTO dto = new AlumnoResponseDTO();
        dto.setIdentificador(alumno.getId() == null ? null : alumno.getId().toString());
        dto.setNombre(alumno.getNombre());
        dto.setApellidos(alumno.getApellidos());
        dto.setDni(alumno.getDni());
        dto.setFechaNacimiento(alumno.getFechaNacimiento());
        dto.setCurso(alumno.getCurso() == null ? null : alumno.getCurso().getNombre());
        dto.setEdad(Period.between(alumno.getFechaNacimiento(), LocalDate.now()).getYears());
        return dto;
    }
}
