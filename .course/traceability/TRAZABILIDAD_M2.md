# Trazabilidad humana — M2

> Generado automáticamente desde `M2.json`, los manifests de paso y el árbol real del proyecto. No es una segunda fuente de verdad: traduce el contrato máquina-legible a una vista auditable por una persona.

## Cómo puedes auditarlo tú

1. Abre la [guía práctica](../../M2/PRACTICA.md) y elige cualquier paso.
2. Busca su identificador en **Guía → proyecto** y sigue artefactos, símbolos, comandos, observables y verificaciones.
3. Abre la [teoría](../../M2/TEORIA.md) y localiza los conceptos referenciados por ese paso.
4. Usa **Proyecto → guía** para partir de un fichero final y reconstruir por qué existe y dónde evolucionó.
5. Revisa **Artefactos necesarios no introducidos en este módulo** para separar herencia/soporte de contenido nuevo.
6. Revisa **Estados temporales y restauraciones** para comprobar que ningún experimento quedó filtrado al snapshot final.
7. Ejecuta los gates automáticos del módulo para contrastar esta vista contra el árbol y el runtime reales.

## Resumen verificable

| Dato | Valor |
|---|---:|
| Estado | `IN_PROGRESS` |
| Esquema | v3 |
| Conceptos teóricos | 5 |
| Pasos prácticos | 12 |
| `PERMANENT` | 12 |
| `TEMPORARY` | 0 |
| Recorridos de entorno | 4 |
| Artefactos `GUIDE` | 2 |
| Artefactos `INHERITED` | 15 |
| Artefactos `SUPPORT` | 3 |
| Total previsto al completar | 69 |

## Teoría → práctica

| Concepto | Ancla en teoría | Pasos que lo materializan |
|---|---|---|
| `M2-T-21-ROLE` | ## Bloque 1 - Qué es un controlador y cuál es su responsabilidad | `2.1.1`, `2.1.2`, `2.1.3`, `2.1.7` |
| `M2-T-21-MAPPINGS` | ## Bloque 2 - Anotaciones de mapeo en profundidad | `2.1.4`, `2.1.8` |
| `M2-T-21-REQUEST-DATA` | ## Bloque 3 - Extracción de datos de la petición | `2.1.4`, `2.1.5`, `2.1.8`, `2.1.10` |
| `M2-T-21-RESPONSE` | ## Bloque 4 - Construcción de la respuesta | `2.1.3`, `2.1.6`, `2.1.8`, `2.1.9`, `2.1.10`, `2.1.11`, `2.1.12` |
| `M2-T-21-PRACTICES` | ## Bloque 5 - Anti-patrones y buenas prácticas | `2.1.1`, `2.1.2`, `2.1.3`, `2.1.5`, `2.1.6`, `2.1.7`, `2.1.8`, `2.1.11`, `2.1.12` |

## Guía → proyecto

| Paso | Qué enseña/cambia | Teoría | Acción / artefacto / símbolo | Comando | Observable | Verificación | Estado |
|---|---|---|---|---|---|---|---|
| `2.1.1`<br>`M2-P-21-S01` | Verificar el CRUD y las regresiones heredadas de M1 antes de refactorizar el controlador. | `M2-T-21-ROLE`<br>`M2-T-21-PRACTICES` | **VERIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`@RequestMapping("/api/v1/alumnos")`, `@GetMapping`, `@PostMapping`, `@PutMapping("/{id}")`, `@PatchMapping("/{id}")`, `@DeleteMapping("/{id}")`<br>_PERMANENT_ | `GET /hola`<br>`GET /adios`<br>`GET /api/v1/expedientes/ejemplo`<br>`GET /api/v1/alumnos`<br>`GET /api/v1/alumnos/1` | El baseline M0/M1 sigue respondiendo antes de introducir cambios de 2.1. | CI ejecuta regresiones de saludo, expedientes y CRUD de alumnos sobre el snapshot M2. | ✅ `PERMANENT` |
| `2.1.2`<br>`M2-P-21-S02` | Separar conceptualmente generación de ID y persistencia de la construcción de la respuesta HTTP. | `M2-T-21-ROLE`<br>`M2-T-21-PRACTICES` | **VERIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`alumnos.add(dto)`, `ResponseEntity`<br>_PERMANENT_ | `inspeccionar crear() y distinguir lógica de aplicación de semántica HTTP` | La guía clasifica generación de ID y almacenamiento como lógica que migrará al servicio en 2.2. | El snapshot final mantiene esa lógica encapsulada en helper privado, sin crear todavía AlumnoService. | ✅ `PERMANENT` |
| `2.1.3`<br>`M2-P-21-S03` | Extraer guardarAlumno, conservar la generación segura de ID heredada de M1 y devolver 201 con Location. | `M2-T-21-ROLE`<br>`M2-T-21-RESPONSE`<br>`M2-T-21-PRACTICES` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`import java.net.URI`, `private AlumnoDTO guardarAlumno`, `int siguienteId = alumnos.stream()`, `URI location = URI.create(`, `ResponseEntity.created(location).body(creado)`<br>_PERMANENT_ | `POST /api/v1/alumnos con JSON válido`<br>`GET /api/v1/alumnos/3` | POST devuelve 201, cabecera Location=/api/v1/alumnos/3 y el recurso queda consultable. | CI valida status, Location, id generado y persistencia en memoria. | ✅ `PERMANENT` |
| `2.1.4`<br>`M2-P-21-S04` | Leer User-Agent y Accept-Language como metadatos HTTP y devolverlos como JSON. | `M2-T-21-REQUEST-DATA`<br>`M2-T-21-MAPPINGS` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`import org.springframework.web.bind.annotation.RequestHeader`, `@GetMapping("/info-peticion")`, `public Map<String, String> infoPeticion`, `Accept-Language`, `desconocido`<br>_PERMANENT_ | `GET /api/v1/alumnos/info-peticion con User-Agent y Accept-Language`<br>`GET /api/v1/alumnos/info-peticion sin Accept-Language` | La respuesta refleja las cabeceras recibidas y usa desconocido para una cabecera opcional ausente. | CI valida JSON con cabeceras explícitas y el fallback del idioma. | ✅ `PERMANENT` |
| `2.1.5`<br>`M2-P-21-S05` | Añadir page/size con defaults y conservar curso/sort del contrato acumulativo de M1. | `M2-T-21-REQUEST-DATA`<br>`M2-T-21-PRACTICES` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`@RequestParam(required = false) String curso`, `@RequestParam(required = false) String sort`, `@RequestParam(defaultValue = "0") int page`, `@RequestParam(defaultValue = "20") int size`, `.skip((long) page * size)`, `.limit(size)`<br>_PERMANENT_ | `GET colección sin query`<br>`GET ?page=0&size=1`<br>`GET ?page=1&size=1`<br>`GET ?sort=nombre`<br>`GET combinando curso/sort/page/size` | Las páginas devuelven subconjuntos distintos y filtro/orden de M1 siguen disponibles. | CI compara IDs de page 0/1 y prueba que sort/curso no se han perdido. | ✅ `PERMANENT` |
| `2.1.6`<br>`M2-P-21-S06` | Introducir POST /promocionar para observar 204 No Content y una mutación sin representación de respuesta. | `M2-T-21-RESPONSE`<br>`M2-T-21-PRACTICES` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`@PostMapping("/promocionar")`, `public ResponseEntity<Void> promocionar`, `(promocionado)`, `ResponseEntity.noContent().build()`<br>_PERMANENT_ | `POST /api/v1/alumnos/promocionar`<br>`GET /api/v1/alumnos` | POST devuelve 204 sin cuerpo; una consulta posterior muestra el efecto en los cursos. | CI valida status 204, cuerpo vacío y sufijo promocionado en una consulta posterior. | ✅ `PERMANENT` |
| `2.1.7`<br>`M2-P-21-S07` | Extraer buscarPorId y reutilizarlo en GET, PUT y PATCH para hacer los métodos HTTP más declarativos. | `M2-T-21-ROLE`<br>`M2-T-21-PRACTICES` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`private Optional<AlumnoDTO> buscarPorId`, `return buscarPorId(id)`, `alumnos.indexOf(existente)`<br>_PERMANENT_ | `./mvnw test` | La búsqueda por ID queda centralizada y los endpoints conservan 200/404. | Compilación y runtime prueban GET/PUT/PATCH existente e inexistente. | ✅ `PERMANENT` |
| `2.1.8`<br>`M2-P-21-S08` | Ejecutar una regresión completa del CRUD y de los endpoints nuevos tras la refactorización. | `M2-T-21-MAPPINGS`<br>`M2-T-21-REQUEST-DATA`<br>`M2-T-21-RESPONSE`<br>`M2-T-21-PRACTICES` | **VERIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`@GetMapping`, `@PostMapping`, `@PutMapping("/{id}")`, `@PatchMapping("/{id}")`, `@DeleteMapping("/{id}")`, `@GetMapping("/info-peticion")`<br>_PERMANENT_ | `GET colección`<br>`GET /1`<br>`POST`<br>`PUT /1`<br>`PATCH /1`<br>`GET /info-peticion`<br>`GET paginado`<br>`DELETE del recurso creado` | Todos los endpoints heredados y nuevos responden después de la refactorización. | CI recorre el contrato HTTP sobre una instancia recién arrancada. | ✅ `PERMANENT` |
| `2.1.9`<br>`M2-P-21-S09` | Consolidar 200/201/204/404 y Location como parte explícita del contrato del controlador. | `M2-T-21-RESPONSE` | **VERIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`ResponseEntity.created(location).body(creado)`, `ResponseEntity.ok`, `ResponseEntity.notFound().build()`, `ResponseEntity.noContent().build()`<br>_PERMANENT_ | `comparar status de GET/POST/promocionar/PUT/PATCH/DELETE` | Las operaciones devuelven códigos coherentes y POST creación incluye Location. | Assertions HTTP del workflow cubren los status de la matriz de 2.1. | ✅ `PERMANENT` |
| `2.1.10`<br>`M2-P-21-S10` | Distinguir el error de formato JSON real de Bean Validation y comprobar 400/415 sin fijar un cuerpo interno inestable. | `M2-T-21-REQUEST-DATA`<br>`M2-T-21-RESPONSE` | **VERIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`@RequestBody AlumnoDTO dto`<br>_PERMANENT_ | `POST JSON con coma final`<br>`POST text/plain a colección` | JSON mal formado produce 400; media type incompatible produce 415. | CI exige ambos status sin acoplarse al JSON de error por defecto de Spring Boot. | ✅ `PERMANENT` |
| `2.1.11`<br>`M2-P-21-S11` | Diagnosticar mappings, media type, JSON, parámetros, mutabilidad, Location, 204, PATCH y regresiones de M1. | `M2-T-21-PRACTICES`<br>`M2-T-21-RESPONSE` | Observación/análisis sin mutación persistente | `revisar síntoma-causa-comprobación antes de modificar código` | La guía incluye causas y soluciones para 405, 415, 400, nulls, inmutabilidad, Location, 204, PATCH, sort y generación de ID. | Los errores de protocolo de alto valor y las regresiones acumulativas están cubiertos por CI. | ✅ `PERMANENT` |
| `2.1.12`<br>`M2-P-21-S12` | Añadir una representación pedagógica con alumno y _links sin introducir todavía una dependencia HATEOAS específica. | `M2-T-21-RESPONSE`<br>`M2-T-21-PRACTICES` | **MODIFY** [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java)<br>`import java.util.LinkedHashMap`, `@GetMapping("/{id}/con-enlaces")`, `public ResponseEntity<Map<String, Object>> consultarConEnlaces`, `respuesta.put("alumno", alumno)`, `respuesta.put("_links"`, `documentos`, `/api/v1/cursos/`<br>_PERMANENT_ | `GET /api/v1/alumnos/1/con-enlaces`<br>`GET /api/v1/alumnos/999/con-enlaces` | ID existente devuelve 200 con alumno y _links; ID inexistente devuelve 404. | CI valida claves alumno/_links y los enlaces self/documentos/curso, además del 404. | ✅ `PERMANENT` |

## Proyecto → guía

| Artefacto | Clasificación | Origen | Evolución | Símbolos relevantes | Quién lo usa | Qué se rompe si falta |
|---|---|---|---|---|---|---|
| [`M2/TEORIA.md`](../../M2/TEORIA.md) | `GUIDE` | `M2-THEORY` | — | — | student theory | Se pierde la explicación conceptual de M2. |
| [`M2/PRACTICA.md`](../../M2/PRACTICA.md) | `GUIDE` | `M2-PRACTICE` | — | — | student practice<br>traceability tooling | Se pierde el recorrido reproducible de M2. |
| [`M2/README.md`](../../M2/README.md) | `SUPPORT` | — | — | — | student navigation | Se pierde la navegación del módulo. |
| [`M2/proyecto/.gitignore`](../../M2/proyecto/.gitignore) | `INHERITED` | — | — | — | repository hygiene | Podrían versionarse artefactos generados. |
| [`M2/proyecto/.mvn/wrapper/maven-wrapper.properties`](../../M2/proyecto/.mvn/wrapper/maven-wrapper.properties) | `INHERITED` | `M0-P-02-S05` | — | `apache-maven-3.9.16-bin.zip` | Maven Wrapper | Se pierde la versión Maven reproducible. |
| [`M2/proyecto/mvnw`](../../M2/proyecto/mvnw) | `INHERITED` | `M0-P-02-S05` | — | `Maven Wrapper` | Unix shell<br>CI | Se pierde la ejecución Maven reproducible en Unix. |
| [`M2/proyecto/mvnw.cmd`](../../M2/proyecto/mvnw.cmd) | `INHERITED` | `M0-P-02-S05` | — | `Maven Wrapper Windows` | Windows shell | Se pierde la ejecución Maven reproducible en Windows. |
| [`M2/proyecto/pom.xml`](../../M2/proyecto/pom.xml) | `INHERITED` | `M0-P-02-S05` | `M0-P-02-S12` | `spring-boot-starter-parent`<br>`spring-boot-starter-web`<br>`spring-boot-starter-test`<br>`spring-boot-maven-plugin`<br>`java.version` | Maven<br>IDE importers<br>M2 project | El proyecto deja de resolver dependencias, compilar y empaquetarse. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java) | `INHERITED` | `M0-P-02-S05` | — | `MiProyectoApplication`<br>`@SpringBootApplication`<br>`SpringApplication.run` | Spring Boot startup | La aplicación pierde su punto de entrada. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java) | `INHERITED` | `M0-P-03-S07` | `M0-P-04-S12` | `SaludoController`<br>`@GetMapping("/hola")`<br>`@GetMapping("/adios")`<br>`construirMensaje` | M0/M1 regression<br>HTTP runtime | Desaparecen endpoints heredados de regresión. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java) | `INHERITED` | `M1-P-13-S03` | `M1-P-13-S08`<br>`M1-P-13-S09`<br>`M1-P-13-S12` | `ExpedienteController`<br>`@RequestMapping("/api/v1/expedientes")`<br>`@GetMapping("/ejemplo")`<br>`@PostMapping("/eco")` | M1 regression<br>JSON runtime | Se pierde el contrato JSON heredado de M1. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `INHERITED` | `M1-P-14-S03` | `M1-P-14-S07`<br>`M1-P-14-S08`<br>`M1-P-14-S12`<br>`M1-P-15-S02`<br>`M1-P-15-S03`<br>`M1-P-15-S04`<br>`M1-P-15-S05`<br>`M1-P-15-S06`<br>`M1-P-15-S12`<br>`M2-P-21-S03`<br>`M2-P-21-S04`<br>`M2-P-21-S05`<br>`M2-P-21-S06`<br>`M2-P-21-S07`<br>`M2-P-21-S12` | `public class AlumnoController`<br>`@RequestMapping("/api/v1/alumnos")`<br>`@RequestParam(defaultValue = "0") int page`<br>`@RequestParam(defaultValue = "20") int size`<br>`@GetMapping("/info-peticion")`<br>`@PostMapping("/promocionar")`<br>`@GetMapping("/{id}/con-enlaces")`<br>`private AlumnoDTO guardarAlumno`<br>`private Optional<AlumnoDTO> buscarPorId`<br>`ResponseEntity.created(location).body(creado)` | M2 practice 2.1<br>HTTP runtime<br>M1 regression | Se pierde el CRUD heredado y las evoluciones de frontera HTTP de 2.1. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java) | `INHERITED` | `M1-P-14-S02` | — | `public class AlumnoDTO`<br>`@JsonProperty("id")`<br>`@JsonFormat(pattern = "yyyy-MM-dd")`<br>`private String identificador`<br>`private String curso` | AlumnoController<br>Jackson | Desaparece el contrato de datos del recurso Alumno. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java) | `INHERITED` | `M1-P-13-S02` | — | `public class ExpedienteDTO`<br>`@JsonProperty("id")`<br>`@JsonIgnore`<br>`private SolicitanteDTO solicitante` | ExpedienteController<br>Jackson | Se pierde el DTO de expedientes heredado. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java) | `INHERITED` | `M1-P-13-S12` | — | `public class SolicitanteDTO`<br>`@JsonProperty("dni")`<br>`private String documentoIdentidad` | ExpedienteDTO | Se pierde el DTO anidado del solicitante. |
| [`M2/proyecto/src/main/resources/application.properties`](../../M2/proyecto/src/main/resources/application.properties) | `INHERITED` | `M0-P-02-S05` | `M0-P-03-S05`<br>`M0-P-04-S02` | `spring.application.name=mi-proyecto` | Spring Boot configuration | Se pierde la configuración base. |
| [`M2/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTests.java`](../../M2/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTests.java) | `INHERITED` | `Initializr` | — | `MiProyectoApplicationTests`<br>`contextLoads` | Maven test | Se pierde el smoke test del contexto. |
| [`M2/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java`](../../M2/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java) | `INHERITED` | `M0-P-04-S07` | — | `SaludoControllerTest`<br>`saludarDebeDevolverElMensajeEsperado`<br>`despedirDebeDevolverElMensajeEsperado` | Maven test<br>M0 regression | Se pierde la regresión automática del saludo. |
| [`.course/source-audit/M2.md`](../source-audit/M2.md) | `SUPPORT` | — | — | — | editorial audit<br>maintainers | Se pierde la matriz maestra de alcance y correcciones de fuente de M2. |
| [`.course/traceability/M2/2.1.json`](M2/2.1.json) | `SUPPORT` | — | — | — | traceability validator<br>human report generator | Se pierde la trazabilidad de los 12 pasos de 2.1. |

## Artefactos necesarios no introducidos en este módulo

Aquí aparecen piezas heredadas de módulos anteriores y soporte interno. Su presencia puede ser necesaria para ejecutar, auditar o mantener el módulo, pero no se presentan como comportamiento nuevo introducido silenciosamente.

| Artefacto | Tipo | Usuario/motivo | Si faltara |
|---|---|---|---|
| [`M2/README.md`](../../M2/README.md) | `SUPPORT` | student navigation | Se pierde la navegación del módulo. |
| [`M2/proyecto/.gitignore`](../../M2/proyecto/.gitignore) | `INHERITED` | repository hygiene | Podrían versionarse artefactos generados. |
| [`M2/proyecto/.mvn/wrapper/maven-wrapper.properties`](../../M2/proyecto/.mvn/wrapper/maven-wrapper.properties) | `INHERITED` | Maven Wrapper | Se pierde la versión Maven reproducible. |
| [`M2/proyecto/mvnw`](../../M2/proyecto/mvnw) | `INHERITED` | Unix shell; CI | Se pierde la ejecución Maven reproducible en Unix. |
| [`M2/proyecto/mvnw.cmd`](../../M2/proyecto/mvnw.cmd) | `INHERITED` | Windows shell | Se pierde la ejecución Maven reproducible en Windows. |
| [`M2/proyecto/pom.xml`](../../M2/proyecto/pom.xml) | `INHERITED` | Maven; IDE importers; M2 project | El proyecto deja de resolver dependencias, compilar y empaquetarse. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java) | `INHERITED` | Spring Boot startup | La aplicación pierde su punto de entrada. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java) | `INHERITED` | M0/M1 regression; HTTP runtime | Desaparecen endpoints heredados de regresión. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java) | `INHERITED` | M1 regression; JSON runtime | Se pierde el contrato JSON heredado de M1. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `INHERITED` | M2 practice 2.1; HTTP runtime; M1 regression | Se pierde el CRUD heredado y las evoluciones de frontera HTTP de 2.1. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java) | `INHERITED` | AlumnoController; Jackson | Desaparece el contrato de datos del recurso Alumno. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java) | `INHERITED` | ExpedienteController; Jackson | Se pierde el DTO de expedientes heredado. |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java) | `INHERITED` | ExpedienteDTO | Se pierde el DTO anidado del solicitante. |
| [`M2/proyecto/src/main/resources/application.properties`](../../M2/proyecto/src/main/resources/application.properties) | `INHERITED` | Spring Boot configuration | Se pierde la configuración base. |
| [`M2/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTests.java`](../../M2/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTests.java) | `INHERITED` | Maven test | Se pierde el smoke test del contexto. |
| [`M2/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java`](../../M2/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java) | `INHERITED` | Maven test; M0 regression | Se pierde la regresión automática del saludo. |
| [`.course/source-audit/M2.md`](../source-audit/M2.md) | `SUPPORT` | editorial audit; maintainers | Se pierde la matriz maestra de alcance y correcciones de fuente de M2. |
| [`.course/traceability/M2/2.1.json`](M2/2.1.json) | `SUPPORT` | traceability validator; human report generator | Se pierde la trazabilidad de los 12 pasos de 2.1. |

## Trazabilidad inversa por artefacto/símbolo

| Artefacto | Símbolo | Pasos relacionados |
|---|---|---|
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `(promocionado)` | `2.1.6:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `.limit(size)` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `.skip((long) page * size)` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `/api/v1/cursos/` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@DeleteMapping("/{id}")` | `2.1.1:VERIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@GetMapping` | `2.1.1:VERIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@GetMapping("/info-peticion")` | `2.1.4:MODIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@GetMapping("/{id}/con-enlaces")` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@PatchMapping("/{id}")` | `2.1.1:VERIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@PostMapping` | `2.1.1:VERIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@PostMapping("/promocionar")` | `2.1.6:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@PutMapping("/{id}")` | `2.1.1:VERIFY`, `2.1.8:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestBody AlumnoDTO dto` | `2.1.10:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestMapping("/api/v1/alumnos")` | `2.1.1:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestParam(defaultValue = "0") int page` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestParam(defaultValue = "20") int size` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestParam(required = false) String curso` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `@RequestParam(required = false) String sort` | `2.1.5:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `Accept-Language` | `2.1.4:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `ResponseEntity` | `2.1.2:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `ResponseEntity.created(location).body(creado)` | `2.1.3:MODIFY`, `2.1.9:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `ResponseEntity.noContent().build()` | `2.1.6:MODIFY`, `2.1.9:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `ResponseEntity.notFound().build()` | `2.1.9:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `ResponseEntity.ok` | `2.1.9:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `URI location = URI.create(` | `2.1.3:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `alumnos.add(dto)` | `2.1.2:VERIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `alumnos.indexOf(existente)` | `2.1.7:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `desconocido` | `2.1.4:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `documentos` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `import java.net.URI` | `2.1.3:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `import java.util.LinkedHashMap` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `import org.springframework.web.bind.annotation.RequestHeader` | `2.1.4:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `int siguienteId = alumnos.stream()` | `2.1.3:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `private AlumnoDTO guardarAlumno` | `2.1.3:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `private Optional<AlumnoDTO> buscarPorId` | `2.1.7:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `public Map<String, String> infoPeticion` | `2.1.4:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `public ResponseEntity<Map<String, Object>> consultarConEnlaces` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `public ResponseEntity<Void> promocionar` | `2.1.6:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `respuesta.put("_links"` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `respuesta.put("alumno", alumno)` | `2.1.12:MODIFY` |
| [`M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java`](../../M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java) | `return buscarPorId(id)` | `2.1.7:MODIFY` |

## Estados temporales y restauraciones

| Paso | Mutación temporal | Cierre / evidencia final |
|---|---|---|
| — | No hay pasos `TEMPORARY` declarados | — |

## Gates automáticos

La trazabilidad sólo se considera válida si los gates de sólo lectura pueden demostrarla. El contrato automático comprueba, entre otras cosas:

- correspondencia exacta entre headings reales de la práctica y contratos de paso;
- referencias a conceptos teóricos existentes y anclas presentes en la teoría;
- artefactos permanentes clasificados, presentes y con los símbolos declarados;
- orígenes y evoluciones coherentes con acciones `CREATE` / `MODIFY` / `RESTORE`;
- cierre explícito de estados `TEMPORARY`;
- herencia sin pérdida silenciosa de comportamiento aprobado;
- preguntas y respuestas emparejadas y ausencia de metatexto interno en el material del alumno;
- consola, IntelliJ IDEA, Eclipse y VS Code cuando el recorrido lo requiere;
- compilación, tests, empaquetado y observables HTTP reales del módulo;
- sincronización exacta de esta vista mediante `generate_module_traceability.py --check`.

Esta vista vive sólo bajo `.course/traceability/`; no se duplica dentro de la carpeta del alumno.
