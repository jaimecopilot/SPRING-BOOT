package es.mecd.demo.miproyecto.beca;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.mecd.demo.miproyecto.auth.JwtService;
import es.mecd.demo.miproyecto.common.exception.RecursoDuplicadoException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BecaController.class)
@AutoConfigureMockMvc(addFilters = false)
class BecaControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private JwtService jwtService;
    @MockitoBean private BecaService becaService;

    @Test
    void consultarDebeDevolver200CuandoExiste() throws Exception {
        BecaResponseDTO dto = new BecaResponseDTO();
        dto.setIdentificador("1");
        dto.setNombre("Beca General");
        when(becaService.consultar(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/v1/becas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Beca General"));
    }

    @Test
    void crearDebeDevolver201ConLocationCuandoDatosValidos() throws Exception {
        BecaResponseDTO creada = new BecaResponseDTO();
        creada.setIdentificador("1");
        creada.setNombre("Beca General");
        when(becaService.crear(any())).thenReturn(creada);
        BecaRequestDTO request = requestValido();
        mockMvc.perform(post("/api/v1/becas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nombre").value("Beca General"));
    }

    @Test
    void crearDebeDevolver400CuandoFaltanCampos() throws Exception {
        BecaRequestDTO request = new BecaRequestDTO();
        request.setNombre("");
        mockMvc.perform(post("/api/v1/becas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.codigo").value("VALIDACION"));
    }

    @Test
    void crearDebeDevolver409CuandoNombreDuplicado() throws Exception {
        when(becaService.crear(any()))
                .thenThrow(new RecursoDuplicadoException("Beca", "nombre", "Beca General"));
        mockMvc.perform(post("/api/v1/becas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestValido())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.codigo").value("RECURSO_DUPLICADO"));
    }

    @Test
    void eliminarDebeDevolver204CuandoExiste() throws Exception {
        mockMvc.perform(delete("/api/v1/becas/1")).andExpect(status().isNoContent());
    }

    private BecaRequestDTO requestValido() {
        BecaRequestDTO request = new BecaRequestDTO();
        request.setCodigo("TEST-2026");
        request.setNombre("Beca General");
        request.setImporteMaximo(new BigDecimal("1500.00"));
        request.setAnio(2026);
        return request;
    }
}
