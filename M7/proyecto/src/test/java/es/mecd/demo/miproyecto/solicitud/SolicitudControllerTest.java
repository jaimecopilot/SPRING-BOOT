package es.mecd.demo.miproyecto.solicitud;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.common.exception.OperacionNoPermitidaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SolicitudController.class)
@AutoConfigureMockMvc(addFilters = false)
class SolicitudControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private SolicitudService solicitudService;

    @Test
    void crearDebeDevolver201ConLocation() throws Exception {
        SolicitudResponseDTO creada = new SolicitudResponseDTO();
        creada.setIdentificador("1");
        creada.setEstado("BORRADOR");
        when(solicitudService.crear(any())).thenReturn(creada);
        SolicitudRequestDTO request = new SolicitudRequestDTO();
        request.setAlumnoId(1L);
        request.setBecaId(1L);
        mockMvc.perform(post("/api/v1/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estado").value("BORRADOR"));
    }

    @Test
    void cambiarEstadoDebeDevolver200() throws Exception {
        SolicitudResponseDTO actualizada = new SolicitudResponseDTO();
        actualizada.setIdentificador("1");
        actualizada.setEstado("ENVIADA");
        when(solicitudService.cambiarEstado(eq(1L), any())).thenReturn(actualizada);
        CambioEstadoRequestDTO request = new CambioEstadoRequestDTO();
        request.setEstado("ENVIADA");
        mockMvc.perform(patch("/api/v1/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENVIADA"));
    }

    @Test
    void cambiarEstadoDebeDevolver409CuandoEstadoNoPermitido() throws Exception {
        when(solicitudService.cambiarEstado(eq(1L), any()))
                .thenThrow(new OperacionNoPermitidaException("No se puede cambiar el estado"));
        CambioEstadoRequestDTO request = new CambioEstadoRequestDTO();
        request.setEstado("ENVIADA");
        mockMvc.perform(patch("/api/v1/solicitudes/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("OPERACION_NO_PERMITIDA"));
    }
}
