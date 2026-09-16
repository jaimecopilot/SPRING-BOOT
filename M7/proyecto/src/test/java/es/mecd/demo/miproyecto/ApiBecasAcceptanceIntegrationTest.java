package es.mecd.demo.miproyecto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.mecd.demo.miproyecto.alumno.Alumno;
import es.mecd.demo.miproyecto.alumno.AlumnoRepository;
import es.mecd.demo.miproyecto.auth.UsuarioRepository;
import es.mecd.demo.miproyecto.beca.Beca;
import es.mecd.demo.miproyecto.beca.BecaRepository;
import es.mecd.demo.miproyecto.documento.DocumentoRepository;
import es.mecd.demo.miproyecto.solicitud.EstadoSolicitud;
import es.mecd.demo.miproyecto.solicitud.SolicitudBeca;
import es.mecd.demo.miproyecto.solicitud.SolicitudBecaRepository;
import java.time.Year;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ApiBecasAcceptanceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AlumnoRepository alumnos;
    @Autowired
    private BecaRepository becas;
    @Autowired
    private SolicitudBecaRepository solicitudes;
    @Autowired
    private DocumentoRepository documentos;
    @Autowired
    private UsuarioRepository usuarios;

    @Test
    void flujoPrincipalDebeCubrirCiudadanoGestorYConsultor() throws Exception {
        mockMvc.perform(get("/api/v1/becas"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"ciudadano\",\"password\":\"ciudadano123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());

        Alumno alumno = alumnos.findAll().get(0);
        Beca beca = becas.findAll().get(0);
        String crearSolicitud = "{\"alumnoId\":" + alumno.getId()
                + ",\"becaId\":" + beca.getId()
                + ",\"observaciones\":\"Solicitud de aceptación\"}";

        mockMvc.perform(post("/api/v1/solicitudes")
                        .with(user("ciudadano").roles("CIUDADANO"))
                        .contentType("application/json")
                        .content(crearSolicitud))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("BORRADOR"));

        SolicitudBeca solicitud = solicitudes.findAll().get(0);
        MockMultipartFile fichero = new MockMultipartFile(
                "fichero", "dni.pdf", "application/pdf", "PDF".getBytes());
        mockMvc.perform(multipart("/api/v1/solicitudes/{id}/documentos", solicitud.getId())
                        .file(fichero)
                        .with(user("ciudadano").roles("CIUDADANO")))
                .andExpect(status().isCreated());

        org.junit.jupiter.api.Assertions.assertEquals(1, documentos.count());

        mockMvc.perform(get("/api/v1/solicitudes")
                        .with(user("consultor").roles("CONSULTOR")))
                .andExpect(status().isOk());

        solicitud.setEstado(EstadoSolicitud.ENVIADA);
        solicitudes.saveAndFlush(solicitud);
        mockMvc.perform(patch("/api/v1/solicitudes/{id}/estado", solicitud.getId())
                        .with(user("gestor").roles("GESTOR"))
                        .contentType("application/json")
                        .content("{\"estado\":\"EN_REVISION\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_REVISION"));

        mockMvc.perform(patch("/api/v1/solicitudes/{id}/estado", solicitud.getId())
                        .with(user("gestor").roles("GESTOR"))
                        .contentType("application/json")
                        .content("{\"estado\":\"APROBADA\",\"importeConcedido\":1200.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        mockMvc.perform(get("/api/v1/estadisticas/solicitudes-por-estado")
                        .with(user("gestor").roles("GESTOR")))
                .andExpect(status().isOk());
    }

    @Test
    void administracionRegistroYErroresDebenRespetarElContrato() throws Exception {
        mockMvc.perform(post("/api/v1/auth/registro")
                        .contentType("application/json")
                        .content("{\"username\":\"nuevo_ciudadano\","
                                + "\"password\":\"nuevo12345\","
                                + "\"email\":\"nuevo@educacion.gob.es\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roles[0]").value("CIUDADANO"));

        mockMvc.perform(get("/api/v1/usuarios")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        int anio = Year.now().getValue();
        String nuevaBeca = "{\"codigo\":\"ACEPT-" + anio
                + "\",\"nombre\":\"Beca aceptación\","
                + "\"descripcion\":\"Integración M7\","
                + "\"importeMaximo\":900.00,\"anio\":" + anio + "}";
        mockMvc.perform(post("/api/v1/becas")
                        .with(user("admin").roles("ADMIN"))
                        .contentType("application/json")
                        .content(nuevaBeca))
                .andExpect(status().isCreated());

        Beca creada = becas.findByCodigo("ACEPT-" + anio).orElseThrow();
        mockMvc.perform(delete("/api/v1/becas/{id}", creada.getId())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/solicitudes"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/v1/becas")
                        .with(user("gestor").roles("GESTOR"))
                        .contentType("application/json")
                        .content(nuevaBeca))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/v1/solicitudes/999999")
                        .with(user("ciudadano").roles("CIUDADANO")))
                .andExpect(status().isNotFound());

        org.junit.jupiter.api.Assertions.assertTrue(usuarios.existsByUsername("nuevo_ciudadano"));
    }
}
