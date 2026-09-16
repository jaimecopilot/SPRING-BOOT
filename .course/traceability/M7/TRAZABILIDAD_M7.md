# M7 — TRAZABILIDAD FINAL R6

- Fuente normativa: `SPRING BOOT PDF.pdf`, páginas 1551–1808.
- Modelo de entrega: proyecto global **standalone**, nuevo e independiente.
- Diseño teórico: **32 bloques**.
- Preparación autónoma: **8 PREP**.
- Pasos fuente: **77**.
- Archivos funcionales del proyecto: **98**.
- Regla: `fuente → diseño → práctica → acción → fichero/símbolo → prueba/comando → resultado`, con relación inversa por fichero.

## PREP autónomos

- **PREP-01** — Crear un proyecto Spring Boot nuevo → `.gitignore`, `.mvn/wrapper/maven-wrapper.properties`, `mvnw`, `mvnw.cmd`, `pom.xml`, `src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java`, `src/main/resources/application.properties`, `src/main/resources/application-dev.properties`, `src/main/resources/application-test.properties`
- **PREP-02** — Crear el soporte mínimo de Alumno → `src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/Curso.java`, `src/main/java/es/mecd/demo/miproyecto/alumno/CursoRepository.java`, `src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java`
- **PREP-03** — Crear el contrato común de errores → `src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java`, `src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorTecnicoException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/OperacionNoPermitidaException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/ValidacionNegocioException.java`, `src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java`
- **PREP-04** — Crear Usuario y Rol → `src/main/java/es/mecd/demo/miproyecto/auth/LoginRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/auth/LoginResponseDTO.java`, `src/main/java/es/mecd/demo/miproyecto/auth/RefreshRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/auth/RegistroRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/auth/RegistroResponseDTO.java`, `src/main/java/es/mecd/demo/miproyecto/auth/Rol.java`, `src/main/java/es/mecd/demo/miproyecto/auth/RolRepository.java`, `src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java`, `src/main/java/es/mecd/demo/miproyecto/auth/UsuarioRepository.java`
- **PREP-05** — Crear autenticación JWT dentro de M7 → `src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`, `src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java`, `src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`, `src/main/java/es/mecd/demo/miproyecto/auth/CredencialesInvalidasException.java`, `src/main/java/es/mecd/demo/miproyecto/auth/JwtAccessDeniedHandler.java`, `src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationEntryPoint.java`, `src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java`, `src/main/java/es/mecd/demo/miproyecto/auth/JwtConfig.java`, `src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java`, `src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java`, `src/main/java/es/mecd/demo/miproyecto/auth/TokenInvalidoException.java`, `src/main/java/es/mecd/demo/miproyecto/auth/UsuarioDetailsService.java`, `src/test/java/es/mecd/demo/miproyecto/auth/AuthServiceTest.java`
- **PREP-06** — Crear seguridad base y CORS → `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`
- **PREP-07** — Crear datos mínimos de desarrollo → `src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`, `src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesBecasConfig.java`
- **PREP-08** — Verificar el punto de partida autónomo → `src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java`

## 77 pasos fuente

- **7.1.1** · p. 1570 · Crear el documento de diseño → `docs/diseno-api.md`
- **7.1.2** · p. 1573 · Revisar el diseño con una checklist → `docs/diseno-api.md`
- **7.1.3** · p. 1574 · Identificar dependencias entre puntos → `docs/diseno-api.md`
- **7.1.4** · p. 1575 · Crear el paquete base del proyecto → — (STRUCTURE_CREATED_BY_STEPS)
- **7.2.1** · p. 1583 · Crear el enum EstadoSolicitud → `src/main/java/es/mecd/demo/miproyecto/solicitud/EstadoSolicitud.java`
- **7.2.2** · p. 1584 · Crear la entidad Beca → `src/main/java/es/mecd/demo/miproyecto/beca/Beca.java`
- **7.2.3** · p. 1589 · Crear la entidad SolicitudBeca → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBeca.java`
- **7.2.4** · p. 1595 · Crear la entidad Documento → `src/main/java/es/mecd/demo/miproyecto/documento/Documento.java`
- **7.2.5** · p. 1599 · Ampliar el Alumno con la relación inversa (opcional) → — (NO_CHANGE_BY_DESIGN) · Evidencia: NO_CHANGE_BY_DESIGN: relación inversa opcional no añadida
- **7.2.6** · p. 1600 · Arrancar y ver el esquema generado → `src/main/resources/application-dev.properties` · Evidencia: mvnw.cmd spring-boot:run
- **7.2.7** · p. 1602 · Verificar en la consola de H2 → `src/main/resources/application-dev.properties` · Evidencia: H2 console: comprobar tablas/relaciones
- **7.2.8** · p. 1603 · Añadir datos iniciales → `src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesBecasConfig.java`
- **7.2.9** · p. 1605 · Verificar los datos iniciales → `src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesBecasConfig.java` · Evidencia: H2: comprobar datos iniciales
- **7.2.10** · p. 1606 · Errores comunes del ejercicio → — (DOCUMENTATION_ONLY) · Evidencia: Checklist de errores comunes en PRACTICA.md
- **7.2.11** · p. 1608 · Verificar las relaciones con un test rápido → `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRelacionesTest.java` · Evidencia: @DataJpaTest con TestEntityManager; relación SolicitudBeca → Documento/Alumno/Beca persistida
- **7.2.12** · p. 1610 · Reto resuelto — Añadir un campo codigo a Beca → `src/main/java/es/mecd/demo/miproyecto/beca/Beca.java`, `src/main/java/es/mecd/demo/miproyecto/beca/BecaRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/beca/BecaResponseDTO.java`, `src/main/java/es/mecd/demo/miproyecto/beca/BecaService.java`, `src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesBecasConfig.java`
- **7.3.1** · p. 1618 · Crear el BecaRepository → `src/main/java/es/mecd/demo/miproyecto/beca/BecaRepository.java`
- **7.3.2** · p. 1622 · Crear el DocumentoRepository → `src/main/java/es/mecd/demo/miproyecto/documento/DocumentoRepository.java`
- **7.3.3** · p. 1624 · Crear el SolicitudBecaRepository (parte 1) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`
- **7.3.4** · p. 1627 · Añadir consultas con @Query y JOIN FETCH al SolicitudBecaRepository (parte 2) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`
- **7.3.5** · p. 1630 · Añadir @EntityGraph para listados simples (parte 3) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`
- **7.3.6** · p. 1631 · Añadir consultas de agregación → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`
- **7.3.7** · p. 1633 · Arrancar y verificar → — (EXECUTION_ONLY) · Evidencia: mvnw.cmd spring-boot:run
- **7.3.8** · p. 1633 · Verificar las consultas con un test → `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepositoryTest.java`
- **7.3.9** · p. 1640 · Verificar las consultas en los logs → — (EXECUTION_ONLY) · Evidencia: Revisar SQL Hibernate
- **7.3.10** · p. 1640 · Errores comunes del ejercicio → — (DOCUMENTATION_ONLY) · Evidencia: Checklist de errores comunes en PRACTICA.md
- **7.3.11** · p. 1642 · Reto resuelto — Consulta con filtro de texto libre → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepositoryTest.java`
- **7.3.12** · p. 1644 · Verificar la estructura completa → — (VERIFICATION_ONLY)
- **7.4.1** · p. 1654 · Crear los DTOs de Beca → `src/main/java/es/mecd/demo/miproyecto/beca/BecaRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/beca/BecaResponseDTO.java`
- **7.4.2** · p. 1657 · Crear el BecaService → `src/main/java/es/mecd/demo/miproyecto/beca/BecaService.java`
- **7.4.3** · p. 1663 · Crear los DTOs de Solicitud → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/CambioEstadoRequestDTO.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/AlumnoResumenDTO.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/BecaResumenDTO.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudResponseDTO.java`
- **7.4.4** · p. 1670 · Crear el SolicitudService (parte 1: consultar y listar) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`
- **7.4.5** · p. 1673 · Crear el SolicitudService (parte 2: crear solicitud) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`
- **7.4.6** · p. 1676 · Crear el SolicitudService (parte 3: cambiar estado con validación de transiciones) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`
- **7.4.7** · p. 1680 · Crear el SolicitudService (parte 4: cancelar) → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`
- **7.4.8** · p. 1682 · Añadir el método toDTO al SolicitudService → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`
- **7.4.9** · p. 1684 · Crear el DocumentoResponseDTO y el DocumentoService → `src/main/java/es/mecd/demo/miproyecto/documento/DocumentoResponseDTO.java`, `src/main/java/es/mecd/demo/miproyecto/documento/DocumentoService.java`
- **7.4.10** · p. 1692 · Escribir tests del SolicitudService → `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudServiceTest.java`
- **7.4.11** · p. 1696 · Errores comunes del ejercicio → — (DOCUMENTATION_ONLY) · Evidencia: Checklist de errores comunes en PRACTICA.md
- **7.4.12** · p. 1697 · Reto resuelto — Método para obtener la solicitud más reciente de un alumno → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudServiceTest.java`
- **7.5.1** · p. 1706 · Crear el BecaController → `src/main/java/es/mecd/demo/miproyecto/beca/BecaController.java`
- **7.5.2** · p. 1713 · Crear el SolicitudController → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudController.java`
- **7.5.3** · p. 1719 · Crear el DocumentoController → `src/main/java/es/mecd/demo/miproyecto/documento/DocumentoController.java`
- **7.5.4** · p. 1723 · Crear el DTO de cambio de roles → `src/main/java/es/mecd/demo/miproyecto/auth/CambioRolesRequestDTO.java`
- **7.5.5** · p. 1724 · Crear el UsuarioService → `src/main/java/es/mecd/demo/miproyecto/auth/UsuarioService.java`, `src/main/java/es/mecd/demo/miproyecto/auth/UsuarioResponseDTO.java`, `src/test/java/es/mecd/demo/miproyecto/auth/UsuarioServiceTest.java`
- **7.5.6** · p. 1728 · Crear el UsuarioController → `src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`
- **7.5.7** · p. 1732 · Arrancar y probar los endpoints → — (EXECUTION_ONLY) · Evidencia: curl: matriz de endpoints
- **7.5.8** · p. 1733 · Verificar OpenAPI → — (EXECUTION_ONLY) · Evidencia: GET /swagger-ui.html
- **7.5.9** · p. 1734 · Escribir tests del BecaController → `src/test/java/es/mecd/demo/miproyecto/beca/BecaControllerTest.java`
- **7.5.10** · p. 1738 · Escribir tests del SolicitudController → `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudControllerTest.java`
- **7.5.11** · p. 1742 · Errores comunes del ejercicio → — (DOCUMENTATION_ONLY) · Evidencia: Checklist de errores comunes en PRACTICA.md
- **7.5.12** · p. 1743 · Reto resuelto — Endpoint para consultar las solicitudes de un alumno → `src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`, `src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java`
- **7.6.1** · p. 1753 · Añadir los roles CIUDADANO y GESTOR al inicializador → `src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`, `src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`
- **7.6.2** · p. 1755 · Actualizar el SecurityConfig → `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`
- **7.6.3** · p. 1761 · Añadir @PreAuthorize en las operaciones contextuales → `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudController.java`
- **7.6.4** · p. 1763 · Añadir @PreAuthorize en el BecaController → `src/main/java/es/mecd/demo/miproyecto/beca/BecaController.java`
- **7.6.5** · p. 1764 · Añadir @PreAuthorize en el DocumentoController → `src/main/java/es/mecd/demo/miproyecto/documento/DocumentoController.java`
- **7.6.6** · p. 1765 · Arrancar y probar con curl → — (EXECUTION_ONLY) · Evidencia: curl con JWT por rol
- **7.6.7** · p. 1767 · Probar con un token inválido → — (EXECUTION_ONLY) · Evidencia: curl con JWT inválido -> 401
- **7.6.8** · p. 1768 · Verificar CORS → `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`
- **7.6.9** · p. 1769 · Escribir tests de seguridad → `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudSecurityTest.java`
- **7.6.10** · p. 1771 · Escribir tests del BecaController con roles → `src/test/java/es/mecd/demo/miproyecto/beca/BecaControllerSecurityTest.java`
- **7.6.11** · p. 1773 · Errores comunes del ejercicio → — (DOCUMENTATION_ONLY) · Evidencia: Checklist de errores comunes en PRACTICA.md
- **7.6.12** · p. 1775 · Reto resuelto — Añadir un rol CONSULTOR → `src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`, `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudSecurityTest.java`
- **7.7.1** · p. 1785 · Ejecutar todos los tests → `src/test/java/es/mecd/demo/miproyecto/ApiBecasAcceptanceIntegrationTest.java`, `src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepositoryTest.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudServiceTest.java`, `src/test/java/es/mecd/demo/miproyecto/beca/BecaControllerTest.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudControllerTest.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudSecurityTest.java`, `src/test/java/es/mecd/demo/miproyecto/beca/BecaControllerSecurityTest.java`, `src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java`, `src/test/java/es/mecd/demo/miproyecto/auth/AuthServiceTest.java`, `src/test/java/es/mecd/demo/miproyecto/auth/UsuarioServiceTest.java`, `src/test/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRelacionesTest.java` · Evidencia: mvnw.cmd -B clean test
- **7.7.2** · p. 1786 · Ejecutar los tests con cobertura → `pom.xml` · Evidencia: mvnw.cmd -B clean verify + JaCoCo
- **7.7.3** · p. 1788 · Arrancar la aplicación → — (EXECUTION_ONLY) · Evidencia: mvnw.cmd spring-boot:run
- **7.7.4** · p. 1788 · Probar el escenario del ciudadano → — (EXECUTION_ONLY) · Evidencia: Smoke CIUDADANO
- **7.7.5** · p. 1790 · Probar el escenario del gestor → — (EXECUTION_ONLY) · Evidencia: Smoke GESTOR con fixture ENVIADA
- **7.7.6** · p. 1792 · Probar el escenario del administrador → — (EXECUTION_ONLY) · Evidencia: Smoke ADMIN
- **7.7.7** · p. 1793 · Probar errores y seguridad → — (EXECUTION_ONLY) · Evidencia: Matriz 401/403/409/400
- **7.7.8** · p. 1794 · Revisar la documentación de OpenAPI → `src/main/java/es/mecd/demo/miproyecto/config/OpenApiConfig.java` · Evidencia: Swagger UI expone bearerAuth/Authorize; /v3/api-docs documenta JWT y sólo los endpoints públicos quedan sin requisito de seguridad
- **7.7.9** · p. 1795 · Preparar la colección de Postman → `postman/API_Becas_Ministerio.postman_collection.json`, `postman/API_Becas_Local.postman_environment.json` · Evidencia: Importar colección/entorno Postman
- **7.7.10** · p. 1796 · Prueba de carga ligera con ab → — (EXECUTION_ONLY) · Evidencia: ab -n 100 -c 10 http://localhost:8080/api/v1/becas
- **7.7.11** · p. 1798 · Actualizar el README → `README.md`
- **7.7.12** · p. 1803 · Errores comunes antes de la entrega → `README.md` · Evidencia: Checklist de errores antes de entrega
- **7.7.13** · p. 1804 · Reto resuelto — Añadir un endpoint de estadísticas → `src/main/java/es/mecd/demo/miproyecto/solicitud/EstadisticasController.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudService.java`, `src/main/java/es/mecd/demo/miproyecto/solicitud/SolicitudBecaRepository.java`, `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` · Evidencia: GET /api/v1/estadisticas/solicitudes-por-estado; GESTOR/ADMIN 200 y CONSULTOR 403 en E2E real

## NO_MAGIC_FILES

- Estado estático: **PASS** cuando `validate_m7.py` confirma que todos los archivos del proyecto están en el mapa inverso.
- Archivos funcionales esperados: **98**.
- Sin origen permitido: **0**.

## Reconciliaciones explícitas

- **12 reglas atómicas**: se preserva la enumeración real aunque un resumen de la fuente diga 10.
- **Beca.codigo**: el reto 7.2.12 se propaga por entidad, DTO, servicio, datos y tests.
- **Orden 7.2**: antes de crear los repositorios de 7.3 se usa `EntityManager`/`TestEntityManager` para que los pasos sean ejecutables en orden.
- **BORRADOR → ENVIADA**: el escenario final prepara una solicitud `ENVIADA` como dato de aceptación; no se inventa un endpoint.
- **Ownership**: no se inventa una relación Usuario→Alumno que el modelo no puede verificar.
- **@MockitoBean**: se usa en los slices MVC compatibles con Spring Boot 3.5.
- **Auth allowlist**: `registro`, `login` y `refresh` son los únicos POST públicos de autenticación; no se permite `/auth/**`.
- **CONSULTOR**: puede consultar solicitudes pero no modificar estados.
- **OpenAPI JWT**: 7.7.8 materializa `OpenApiConfig` con `bearerAuth` para que Swagger UI pueda usar `Authorize` en endpoints protegidos.
- **Postman**: no existe `logout`; la colección sólo contiene endpoints reales.

## Gate técnico R6

- Validador estático: debe pasar 32 bloques, 8 PREP, 77 pasos y NO_MAGIC_FILES.
- Maven: `mvnw.cmd -B clean verify`.
- Tests: 0 failures, 0 errors.
- JaCoCo LINE: >= 70 %.
- JAR: exactamente uno, arrancable.
- Smoke HTTP: becas públicas, autenticación JWT, roles, CORS, OpenAPI y `bearerAuth`.
- PDF: teoría y práctica presentes e íntegros.
- El PASS definitivo de R6 se obtiene con el runner Windows del paquete FINAL CLOSURE.
