package es.mecd.demo.miproyecto.alumno;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.solicitud.SolicitudService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AlumnoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlumnoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AlumnoService alumnoService;

    @MockitoBean
    private SolicitudService solicitudService;

    @Test
    void retoListarSolicitudesAlumnoDebeResponder200() throws Exception {
        when(solicitudService.listarPorAlumno(anyLong(), any()))
                .thenReturn(new PageImpl<>(java.util.List.of()));

        mockMvc.perform(get("/api/v1/alumnos/1/solicitudes"))
                .andExpect(status().isOk());
    }
}
