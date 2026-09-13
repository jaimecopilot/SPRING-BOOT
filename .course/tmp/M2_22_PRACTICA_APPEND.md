

---

# Punto 2.2 - Capas de la aplicación: Servicio

## Objetivo práctico

Partimos exactamente del resultado de 2.1. El CRUD de alumnos funciona, conserva filtro, ordenación y paginación, devuelve `Location` al crear, expone las cabeceras de petición, permite promoción y ofrece el ejemplo con enlaces. El problema es arquitectónico: `AlumnoController` todavía conserva datos y lógica que no son HTTP.

En este punto moveremos esa responsabilidad a `AlumnoService`, introduciremos `NegocioException` para el DNI duplicado y convertiremos esa excepción en un 409. El reto final aplicará el mismo patrón a expedientes.

## Paso 1 - Repasar el estado actual

Arranca el proyecto sin modificarlo:

```bash
cd M2/proyecto
./mvnw spring-boot:run
```

En Windows:

```powershell
cd M2\proyecto
.\mvnw.cmd spring-boot:run
```

Comprueba al menos:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
curl -i http://localhost:8080/api/v1/alumnos
curl -i "http://localhost:8080/api/v1/alumnos?page=0&size=1"
curl -i http://localhost:8080/api/v1/alumnos/info-peticion \
  -H "User-Agent: MiCliente/1.0" \
  -H "Accept-Language: es-ES"
curl -i http://localhost:8080/api/v1/expedientes/ejemplo
```

Después abre `AlumnoController.java` y clasifica mentalmente cada parte:

- `@GetMapping`, `@PostMapping`, `ResponseEntity`, cabeceras y status: **HTTP**;
- lista de alumnos, búsqueda, generación de ID, actualización y promoción: **lógica/estado de aplicación**.

No cambies nada hasta poder explicar esa diferencia.

### Pregunta

¿Qué queremos conservar después de la refactorización?

### Respuesta razonada

El contrato observable. Los mismos endpoints deben seguir respondiendo con la misma semántica, pero el controlador dejará de saber cómo se almacenan, buscan o modifican alumnos. Refactorizar arquitectura no justifica perder comportamiento ya aprobado.

## Paso 2 - Crear el paquete `service`

Crea:

```text
M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service
```

Dentro vivirán las clases que coordinan lógica de aplicación.

### IntelliJ IDEA

En `src/main/java/es/mecd/demo/miproyecto`, botón derecho → **New → Package** → `service`.

### Eclipse

Botón derecho sobre `es.mecd.demo.miproyecto` → **New → Package** → `es.mecd.demo.miproyecto.service`.

### VS Code

Crea la carpeta `service` bajo el paquete raíz y asegúrate de que el archivo Java declare:

```java
package es.mecd.demo.miproyecto.service;
```

Desde consola puedes comprobar después que la estructura compila con:

```bash
./mvnw test
```

### Pregunta

¿Por qué no colocamos `AlumnoService` dentro de `controller`?

### Respuesta razonada

Porque el paquete debe expresar el rol de la clase. Un servicio no es infraestructura HTTP. Separarlo físicamente hace visible la arquitectura y evita que la capa web se convierta en un contenedor genérico de toda la aplicación.

## Paso 3 - Crear la excepción de negocio

Crea también:

```text
M2/proyecto/src/main/java/es/mecd/demo/miproyecto/exception
```

Y dentro `NegocioException.java`:

```java
package es.mecd.demo.miproyecto.exception;

public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

No tiene código HTTP. Su función es expresar que una regla de negocio no puede cumplirse.

Compila:

```bash
./mvnw test
```

### Pregunta

¿Por qué extendemos `RuntimeException` y no `Exception`?

### Respuesta razonada

Porque queremos una excepción no comprobada que pueda propagarse desde la lógica de negocio sin llenar todas las firmas con `throws`. Eso no significa ignorarla: la capa web la traducirá de forma controlada.

## Paso 4 - Crear `AlumnoService`

Crea:

```text
M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service/AlumnoService.java
```

Empieza con el estereotipo:

```java
@Service
public class AlumnoService {
    // ...
}
```

Mueve al servicio la colección inicial que estaba en el controlador. Conservamos los mismos datos del checkpoint 2.1 para que el refactor sea observable como equivalente:

```java
private final List<AlumnoDTO> alumnos = new ArrayList<>(List.of(
        new AlumnoDTO(
                "1", "Ana", "García López", "DNI-DEMO-01",
                LocalDate.of(2010, 5, 12), "5º Primaria"),
        new AlumnoDTO(
                "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                LocalDate.of(2009, 9, 3), "6º Primaria")
));
```

### Mover listado, filtro, ordenación y paginación

La lógica que antes estaba en el controlador queda en:

```java
public List<AlumnoDTO> listar(
        String curso,
        String sort,
        int page,
        int size) {

    var stream = alumnos.stream();

    if (curso != null && !curso.isBlank()) {
        stream = stream.filter(
                a -> a.getCurso().equalsIgnoreCase(curso));
    }

    if ("nombre".equalsIgnoreCase(sort)) {
        stream = stream.sorted(
                (a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
    } else if ("apellidos".equalsIgnoreCase(sort)) {
        stream = stream.sorted(
                (a, b) -> a.getApellidos().compareToIgnoreCase(b.getApellidos()));
    }

    return stream
            .skip((long) page * size)
            .limit(size)
            .toList();
}
```

### Consulta

```java
public Optional<AlumnoDTO> consultar(String id) {
    return alumnos.stream()
            .filter(a -> a.getIdentificador().equals(id))
            .findFirst();
}
```

### Creación con regla de DNI único

```java
public AlumnoDTO crear(AlumnoDTO dto) {
    if (existePorDni(dto.getDni())) {
        throw new NegocioException(
                "Ya existe un alumno con el DNI " + dto.getDni());
    }

    int siguienteId = alumnos.stream()
            .map(AlumnoDTO::getIdentificador)
            .filter(id -> id != null && id.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0) + 1;

    dto.setIdentificador(String.valueOf(siguienteId));
    alumnos.add(dto);
    return dto;
}
```

La fuente original utiliza en este punto `alumnos.size() + 1`. No lo recuperamos: el curso ya corrigió esa estrategia en M1 porque puede colisionar después de un borrado. La regla pedagógica se conserva y la implementación acumulativa sigue siendo segura para este modelo en memoria.

### PUT, PATCH y DELETE

Mueve también las operaciones heredadas:

```java
public Optional<AlumnoDTO> actualizar(String id, AlumnoDTO dto) {
    return consultar(id).map(existente -> {
        dto.setIdentificador(id);
        alumnos.set(alumnos.indexOf(existente), dto);
        return dto;
    });
}
```

```java
public Optional<AlumnoDTO> actualizarParcial(
        String id,
        Map<String, Object> cambios) {

    return consultar(id).map(alumno -> {
        if (cambios.containsKey("nombre")) {
            alumno.setNombre((String) cambios.get("nombre"));
        }
        if (cambios.containsKey("apellidos")) {
            alumno.setApellidos((String) cambios.get("apellidos"));
        }
        if (cambios.containsKey("dni")) {
            alumno.setDni((String) cambios.get("dni"));
        }
        if (cambios.containsKey("curso")) {
            alumno.setCurso((String) cambios.get("curso"));
        }
        return alumno;
    });
}
```

```java
public boolean eliminar(String id) {
    return alumnos.removeIf(
            a -> a.getIdentificador().equals(id));
}
```

El campo `dni` del PATCH se conserva porque forma parte del contrato acumulativo de M1, aunque el ejemplo resumido de la fuente 2.2 no lo vuelva a mostrar.

### Mover la promoción

La regla aplicada a los cursos también deja de vivir en HTTP:

```java
public void promocionar() {
    alumnos.forEach(
            a -> a.setCurso(a.getCurso() + " (promocionado)"));
}
```

Y la utilidad privada:

```java
private boolean existePorDni(String dni) {
    return alumnos.stream()
            .anyMatch(a -> Objects.equals(a.getDni(), dni));
}
```

Ejecuta:

```bash
./mvnw test
```

### Pregunta

¿Por qué `crear` lanza `NegocioException` en lugar de devolver un 409?

### Respuesta razonada

Porque el servicio expresa una regla de la aplicación, no una respuesta HTTP. La capa web decidirá que, en esta API, esa situación se representa con `409 Conflict`.

## Paso 5 - Refactorizar el controlador para que use el servicio

Elimina del `AlumnoController`:

- la lista de alumnos;
- `guardarAlumno`;
- `buscarPorId`;
- la lógica de filtro, ordenación y paginación;
- las mutaciones directas de la colección.

Añade la dependencia:

```java
private final AlumnoService service;

public AlumnoController(AlumnoService service) {
    this.service = service;
}
```

El listado queda reducido a delegación:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam(required = false) String curso,
        @RequestParam(required = false) String sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {

    return service.listar(curso, sort, page, size);
}
```

El POST conserva el contrato HTTP de 2.1:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    AlumnoDTO creado = service.crear(dto);
    URI location = URI.create(
            "/api/v1/alumnos/" + creado.getIdentificador());
    return ResponseEntity.created(location).body(creado);
}
```

Consulta, PUT, PATCH y DELETE delegan del mismo modo:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    return service.consultar(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

```java
@PutMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizar(
        @PathVariable String id,
        @RequestBody AlumnoDTO dto) {
    return service.actualizar(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

```java
@PatchMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizarParcial(
        @PathVariable String id,
        @RequestBody Map<String, Object> cambios) {
    return service.actualizarParcial(id, cambios)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable String id) {
    return service.eliminar(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
}
```

`/info-peticion` permanece en el controlador porque leer cabeceras sí es trabajo HTTP.

La promoción pasa a ser:

```java
@PostMapping("/promocionar")
public ResponseEntity<Void> promocionar() {
    service.promocionar();
    return ResponseEntity.noContent().build();
}
```

Y el endpoint con enlaces consulta el alumno mediante el servicio, pero sigue construyendo los enlaces en la capa web.

Finalmente añade el manejador local:

```java
@ExceptionHandler(NegocioException.class)
public ResponseEntity<Map<String, Object>> handleNegocio(
        NegocioException ex) {

    Map<String, Object> error = Map.of(
            "status", 409,
            "error", "Conflict",
            "message", ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
}
```

### Pregunta

¿Qué información conserva ahora el controlador sobre el almacenamiento de alumnos?

### Respuesta razonada

Ninguna. Sabe que existe un servicio capaz de listar, consultar, crear, actualizar y eliminar, pero no sabe si el servicio usa una lista, un repositorio, una base de datos o un sistema remoto.

## Paso 6 - Arrancar y probar el CRUD completo

Primero ejecuta los tests y empaqueta:

```bash
./mvnw test
./mvnw -DskipTests package
```

Arranca:

```bash
./mvnw spring-boot:run
```

En otra terminal prueba el recorrido acumulativo:

```bash
# baseline
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios

# colección, paginación y filtro
curl -i http://localhost:8080/api/v1/alumnos
curl -i "http://localhost:8080/api/v1/alumnos?page=0&size=1"
curl -i "http://localhost:8080/api/v1/alumnos?sort=nombre&page=0&size=20"

# consulta
curl -i http://localhost:8080/api/v1/alumnos/1

# creación
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Carlos","apellidos":"Zeta","dni":"DNI-DEMO-03","fechaNacimiento":"2011-01-02","curso":"5º Primaria"}'

# actualización completa
curl -i -X PUT http://localhost:8080/api/v1/alumnos/3 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Aaron","apellidos":"Abarca","dni":"DNI-DEMO-03","fechaNacimiento":"2011-01-02","curso":"5º Primaria"}'

# actualización parcial
curl -i -X PATCH http://localhost:8080/api/v1/alumnos/3 \
  -H "Content-Type: application/json" \
  -d '{"curso":"6º Primaria B"}'

# enlaces
curl -i http://localhost:8080/api/v1/alumnos/1/con-enlaces

# borrar
curl -i -X DELETE http://localhost:8080/api/v1/alumnos/3
```

Los códigos 200/201/204/404 deben seguir teniendo el mismo significado que antes del refactor.

### Pregunta

¿Cómo sabemos que la refactorización no ha roto el contrato externo?

### Respuesta razonada

Porque ejecutamos las mismas operaciones HTTP y observamos los mismos resultados relevantes. La estructura interna ha cambiado, pero el cliente no necesita conocer esa reorganización.

## Paso 7 - Probar el error de negocio

Reinicia la aplicación para recuperar los datos iniciales y envía un alumno con el DNI que ya existe:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Otra Ana","apellidos":"Duplicada","dni":"DNI-DEMO-01","fechaNacimiento":"2010-01-01","curso":"5º Primaria"}'
```

Debe responder:

```text
HTTP/1.1 409
```

con un JSON que contenga:

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un alumno con el DNI DNI-DEMO-01"
}
```

Comprueba después:

```bash
curl http://localhost:8080/api/v1/alumnos
```

La colección sigue teniendo dos alumnos: la operación rechazada no debe haber mutado el estado.

### Pregunta

¿Por qué 409 es más informativo aquí que 400?

### Respuesta razonada

El JSON tiene formato válido y podría convertirse a `AlumnoDTO`. El problema aparece al aplicar una regla dependiente del estado actual: ese DNI ya está ocupado. La petición entra en conflicto con el dominio, no con la sintaxis del mensaje.

## Paso 8 - Probar un error técnico de entrada

Ahora envía JSON mal formado:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez",}'
```

Debe producir:

```text
HTTP/1.1 400
```

En este caso `AlumnoService.crear` ni siquiera debe recibir un DTO válido: Jackson falla durante la deserialización en la frontera web.

Prueba también un media type incompatible:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: text/plain" \
  --data 'hola'
```

Resultado esperado:

```text
HTTP/1.1 415
```

### Pregunta

¿Qué demuestra la diferencia entre 400/415 y 409?

### Respuesta razonada

Que existen fallos en capas distintas. 400 y 415 se detectan al interpretar la petición HTTP; 409 aparece después, cuando el servicio recibe datos válidos y aplica una regla del negocio.

## Paso 9 - Analizar la separación de capas

Revisa el código final y completa esta matriz:

| Responsabilidad | Controlador | Servicio |
|---|---:|---:|
| rutas y métodos HTTP | sí | no |
| `@PathVariable`, `@RequestParam`, `@RequestHeader` | sí | no |
| `ResponseEntity` y status | sí | no |
| construcción de `Location` | sí | no |
| búsqueda de alumnos | no | sí |
| filtro, orden y paginación en memoria | no | sí |
| generación de IDs | no | sí |
| regla de DNI duplicado | no | sí |
| promoción | no | sí |
| estado de la colección en memoria | no | sí, temporalmente hasta 2.3 |

Usa una búsqueda textual para comprobar que `AlumnoService.java` no contiene `ResponseEntity`, `@RequestParam`, `@PathVariable` ni `HttpStatus`.

### Pregunta

¿Qué archivo tendrá que cambiar principalmente cuando 2.3 sustituya la lista por un repositorio?

### Respuesta razonada

`AlumnoService` cambiará para delegar el acceso a datos, y aparecerá `AlumnoRepository`. El controlador debería permanecer prácticamente igual porque ya depende de la abstracción de servicio y no de la forma de almacenamiento.

## Paso 10 - Errores comunes del ejercicio

| Síntoma | Causa probable | Comprobación |
|---|---|---|
| `NoSuchBeanDefinitionException: AlumnoService` | falta `@Service` o la clase está fuera del escaneo | revisar paquete y anotación |
| `service` es `null` | se ha intentado construir/inicializar incorrectamente | usar inyección por constructor |
| DNI duplicado devuelve 500 | falta el `@ExceptionHandler` local | comprobar manejador de `NegocioException` |
| DNI duplicado se crea igualmente | la regla no está en `AlumnoService.crear` | probar POST repetido |
| el controlador sigue teniendo `List<AlumnoDTO>` | no se completó la separación | buscar `ArrayList`/`List.of` en el controlador |
| el servicio importa `ResponseEntity` | lógica HTTP filtrada al servicio | eliminar dependencia web del servicio |
| desaparecen `sort`, `page` o `size` | se copió literalmente un controlador simplificado | restaurar contrato acumulativo 2.1 |
| reaparece `size() + 1` | se copió la generación de ID de la fuente | conservar máximo numérico + 1 |
| `UnsupportedOperationException` | se creó una lista no mutable | usar `new ArrayList<>(List.of(...))` |

Corrige la causa, no el síntoma. Después ejecuta:

```bash
./mvnw test
./mvnw -DskipTests package
```

### Pregunta

¿Por qué una copia literal del ejemplo de la fuente podría ser incorrecta en nuestro proyecto acumulativo?

### Respuesta razonada

Porque el curso ya ha evolucionado. 2.1 añadió comportamiento que debe sobrevivir y M1 corrigió la generación de IDs. La fuente fija la intención pedagógica y el alcance mínimo, pero el snapshot final debe incorporar también las decisiones válidas de módulos anteriores.

## Paso 11 - Reto resuelto: servicio para el recurso Expediente

Aplicaremos el mismo patrón al recurso que nació en M1.3.

### 11.1 Crear `ExpedienteService`

Crea:

```text
M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service/ExpedienteService.java
```

Anótalo:

```java
@Service
public class ExpedienteService {
```

El servicio mantiene provisionalmente dos expedientes en memoria y ofrece:

```java
public List<ExpedienteDTO> listar()
public Optional<ExpedienteDTO> consultar(String id)
public ExpedienteDTO crear(ExpedienteDTO dto)
public Optional<ExpedienteDTO> actualizar(String id, ExpedienteDTO dto)
public boolean eliminar(String id)
```

La creación utiliza también máximo numérico + 1:

```java
int siguienteId = expedientes.stream()
        .map(ExpedienteDTO::getIdentificador)
        .filter(id -> id != null && id.matches("\\d+"))
        .mapToInt(Integer::parseInt)
        .max()
        .orElse(0) + 1;
```

No perdemos los dos laboratorios permanentes de M1.3. La construcción del ejemplo pasa al servicio:

```java
public ExpedienteDTO ejemplo() {
    ExpedienteDTO dto = new ExpedienteDTO(
            "12345",
            "Ana García López",
            "12345678A",
            "EN_TRAMITE",
            "BECA",
            LocalDate.of(2025, 1, 15),
            1500.00,
            true,
            List.of("DNI.pdf", "Notas.pdf"));
    dto.setNumeroSeguridadSocial("DEMO-NO-REAL");
    dto.setSolicitante(new SolicitanteDTO(
            "Ana", "García López", "12345678A"));
    return dto;
}
```

Y el eco conserva la prueba de Jackson:

```java
public ExpedienteDTO eco(ExpedienteDTO dto) {
    return dto;
}
```

### 11.2 Refactorizar `ExpedienteController`

Inyecta el servicio:

```java
private final ExpedienteService service;

public ExpedienteController(ExpedienteService service) {
    this.service = service;
}
```

Añade el CRUD básico:

```java
@GetMapping
public List<ExpedienteDTO> listar() {
    return service.listar();
}
```

```java
@GetMapping("/{id}")
public ResponseEntity<ExpedienteDTO> consultar(@PathVariable String id) {
    return service.consultar(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

```java
@PostMapping
public ResponseEntity<ExpedienteDTO> crear(@RequestBody ExpedienteDTO dto) {
    ExpedienteDTO creado = service.crear(dto);
    URI location = URI.create(
            "/api/v1/expedientes/" + creado.getIdentificador());
    return ResponseEntity.created(location).body(creado);
}
```

```java
@PutMapping("/{id}")
public ResponseEntity<ExpedienteDTO> actualizar(
        @PathVariable String id,
        @RequestBody ExpedienteDTO dto) {
    return service.actualizar(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable String id) {
    return service.eliminar(id)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
}
```

Y conserva los endpoints de M1.3 delegando:

```java
@GetMapping("/ejemplo")
public ExpedienteDTO ejemplo() {
    return service.ejemplo();
}
```

```java
@PostMapping("/eco")
public ExpedienteDTO eco(@RequestBody ExpedienteDTO dto) {
    return service.eco(dto);
}
```

### 11.3 Probar el resultado

```bash
./mvnw test
./mvnw -DskipTests package
./mvnw spring-boot:run
```

En otra terminal:

```bash
# contrato heredado M1.3
curl -i http://localhost:8080/api/v1/expedientes/ejemplo
curl -i -X POST http://localhost:8080/api/v1/expedientes/eco \
  -H "Content-Type: application/json" \
  -d '{"id":"77","titular":"Lucía Pérez","fechaSolicitud":"2025-01-20"}'

# nuevo recorrido de servicio
curl -i http://localhost:8080/api/v1/expedientes
curl -i http://localhost:8080/api/v1/expedientes/1
curl -i -X POST http://localhost:8080/api/v1/expedientes \
  -H "Content-Type: application/json" \
  -d '{"titular":"Marta Ruiz","dni":"11111111H","estado":"EN_TRAMITE","tipo":"BECA","fechaSolicitud":"2025-02-01","importe":800.0,"activo":true}'
curl -i http://localhost:8080/api/v1/expedientes/3
curl -i -X DELETE http://localhost:8080/api/v1/expedientes/3
curl -i http://localhost:8080/api/v1/expedientes/3
```

Debes observar 200, 201 + `Location`, 204 y 404 de forma coherente.

### Pregunta

¿Qué demuestra aplicar el mismo patrón a alumnos y expedientes?

### Respuesta razonada

Que la capa de servicio no es una solución específica de un único controlador. Es una regla arquitectónica reutilizable: la web traduce HTTP y el servicio concentra lógica y coordinación. El siguiente paso será hacer igualmente reutilizable el acceso a datos mediante repositorios.

## Resultado esperado de 2.2

Al cerrar el punto:

```text
es.mecd.demo.miproyecto
├── controller
│   ├── AlumnoController.java
│   ├── ExpedienteController.java
│   └── SaludoController.java
├── service
│   ├── AlumnoService.java
│   └── ExpedienteService.java
├── exception
│   └── NegocioException.java
└── dto
    ├── AlumnoDTO.java
    ├── ExpedienteDTO.java
    └── SolicitanteDTO.java
```

Y se cumplen estas condiciones:

- el controlador de alumnos no conserva la lista ni genera IDs;
- `AlumnoService` contiene la lógica en memoria y la regla de DNI duplicado;
- el error de negocio se expresa con `NegocioException` y HTTP lo convierte en 409;
- 400/415 continúan distinguiendo errores de entrada;
- no se pierde filtro, ordenación, paginación, cabeceras, promoción ni enlaces de 2.1;
- `ExpedienteController` delega en `ExpedienteService` y conserva `/ejemplo` y `/eco`;
- todavía no existe `repository/`: se introducirá exclusivamente en 2.3.
