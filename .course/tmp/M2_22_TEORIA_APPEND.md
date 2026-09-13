

---

# Punto 2.2 - Capas de la aplicación: Servicio

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar qué responsabilidad tiene la capa de servicio y por qué no debe confundirse con el controlador.
2. Entender qué aporta `@Service` y cómo se relaciona con `@Component` y el escaneo de componentes.
3. Aplicar inyección de dependencias por constructor y razonar por qué es preferible a la inyección por campo.
4. Situar correctamente la transformación DTO ↔ Entidad dentro de una arquitectura por capas, aunque todavía no tengamos entidades JPA separadas.
5. Representar una violación de regla de negocio mediante una excepción específica y traducirla temporalmente a HTTP con `@ExceptionHandler`.
6. Reconocer la diferencia entre un error de negocio y un error técnico.
7. Refactorizar un controlador para que delegue en un servicio sin perder comportamiento ya construido en M1 y 2.1.

## Bloque 1 - Qué es un servicio y cuál es su responsabilidad

### 1.1 La capa donde vive la lógica de aplicación

En 2.1 convertimos el controlador en una frontera HTTP más clara. Aun así, el propio `AlumnoController` seguía haciendo trabajo que no pertenecía al protocolo: buscar alumnos, generar identificadores, modificar una colección en memoria y aplicar cambios parciales.

La capa de servicio existe para separar ese trabajo del transporte HTTP.

Un servicio recibe datos Java, aplica reglas de la aplicación y devuelve datos Java. No debería necesitar saber si la operación comenzó mediante HTTP, una tarea programada, una cola de mensajes o un test.

Podemos visualizar la separación inicial así:

```text
cliente HTTP
    |
    v
AlumnoController
    |  traduce HTTP <-> Java
    v
AlumnoService
    |  aplica lógica y coordina operaciones
    v
almacenamiento en memoria
```

En 2.2 el almacenamiento sigue siendo una lista dentro del servicio. Esa decisión es **temporal y pedagógica**. En 2.3 aislaremos el acceso a datos en un repositorio.

### 1.2 Qué debe hacer un servicio

Entre las responsabilidades habituales de un servicio están:

- aplicar reglas de negocio;
- validar condiciones que dependen del estado de la aplicación;
- coordinar varias operaciones;
- decidir qué hacer cuando un recurso no puede crearse o modificarse;
- transformar entre modelos de entrada/salida y modelos de dominio o persistencia;
- definir, más adelante, límites transaccionales.

Ejemplo: impedir que existan dos alumnos con el mismo DNI es una regla de negocio porque depende de los datos que ya existen.

```java
public AlumnoDTO crear(AlumnoDTO dto) {
    if (existePorDni(dto.getDni())) {
        throw new NegocioException(
                "Ya existe un alumno con el DNI " + dto.getDni());
    }

    // generación de ID y guardado
}
```

El servicio expresa la regla. No devuelve `409`, porque `409 Conflict` es una decisión HTTP y pertenece a la capa web.

### 1.3 Qué no debe hacer un servicio

Un servicio no debería contener dependencias de la capa web como:

```java
ResponseEntity
HttpServletRequest
@RequestParam
@PathVariable
```

Tampoco debería construir URLs HTTP ni decidir cabeceras de respuesta. Si encontramos algo como esto dentro de un servicio:

```java
return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
```

hemos mezclado capas. El servicio debe expresar el problema mediante un resultado Java o una excepción de negocio; el controlador decide cómo traducirlo al protocolo.

### 1.4 Un controlador fino

Tras la separación, un método del controlador puede quedar así:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    return service.consultar(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

El controlador conoce la ruta y los códigos HTTP. No conoce cómo se busca un alumno.

El servicio correspondiente puede ser:

```java
public Optional<AlumnoDTO> consultar(String id) {
    return alumnos.stream()
            .filter(a -> a.getIdentificador().equals(id))
            .findFirst();
}
```

El servicio conoce la operación de consulta, pero no sabe que un resultado vacío terminará convertido en un 404.

### Pregunta

¿Por qué no conviene devolver `ResponseEntity` desde `AlumnoService`?

### Respuesta razonada

Porque acoplaría la lógica de aplicación al protocolo HTTP. Un servicio podría necesitar reutilizarse desde un test, una tarea de fondo o cualquier otro canal. Si devuelve objetos del paquete `spring-web`, deja de ser una capa independiente y obliga a los consumidores no HTTP a conocer decisiones de transporte.

## Bloque 2 - La anotación `@Service`

### 2.1 `@Service` es un estereotipo de Spring

Un servicio que deba ser gestionado por Spring se anota con:

```java
@Service
public class AlumnoService {
    // ...
}
```

`@Service` es una especialización de `@Component`. Eso significa que participa en el mismo mecanismo de detección automática de componentes, pero comunica además una intención arquitectónica: esta clase pertenece a la capa de servicio.

Conceptualmente:

```text
@Component
  ├─ @Controller / @RestController
  ├─ @Service
  └─ @Repository
```

Las anotaciones especializadas no existen sólo por estética. Ayudan a leer la arquitectura y algunas de ellas pueden recibir tratamiento específico de Spring. Por ejemplo, `@Repository` participa en mecanismos relacionados con acceso a datos y traducción de excepciones, algo que veremos en 2.3.

### 2.2 Cómo encuentra Spring el servicio

Nuestra clase principal está en el paquete raíz:

```text
es.mecd.demo.miproyecto
```

y contiene `@SpringBootApplication`. El escaneo de componentes parte de ese paquete y examina sus subpaquetes. Por eso una clase situada en:

```text
es.mecd.demo.miproyecto.service.AlumnoService
```

puede ser encontrada automáticamente.

No necesitamos escribir:

```java
new AlumnoService();
```

ni registrar manualmente la instancia en el controlador.

Al arrancar, Spring detecta la clase, crea un bean y puede suministrarlo a otras clases que lo necesiten.

### 2.3 Bean y ciclo de vida

Un bean es un objeto cuya creación y ciclo de vida gestiona el contenedor de Spring. Para nuestro servicio, el recorrido relevante es:

1. Spring descubre la definición de `AlumnoService`.
2. Crea una instancia llamando a su constructor.
3. Resuelve las dependencias exigidas por ese constructor.
4. Registra el objeto para que otros beans puedan utilizarlo.
5. Mantiene su ciclo de vida hasta el cierre del contexto.

Por defecto, los beans de aplicación son de alcance singleton: existe una instancia por contexto de Spring. Esto explica por qué debemos ser prudentes con estado mutable compartido. Nuestra lista en memoria sirve para aprender las capas, pero no representa una estrategia de almacenamiento concurrente ni persistente de producción.

### 2.4 `@Service` frente a `@Component`

Esto compilaría y Spring podría detectarlo:

```java
@Component
public class AlumnoService {
}
```

pero pierde información semántica. La alternativa preferible es:

```java
@Service
public class AlumnoService {
}
```

porque un lector puede reconocer inmediatamente el rol de la clase.

### Pregunta

Si `@Service` funciona como una especialización de `@Component`, ¿por qué no anotar todas las clases simplemente con `@Component`?

### Respuesta razonada

Porque los estereotipos expresan intención arquitectónica. `@Service`, `@Repository` y `@Controller` hacen visible el rol de una clase y permiten que framework, herramientas y personas razonen sobre ella con más precisión. Usar siempre la anotación genérica elimina información útil sin aportar ventaja.

## Bloque 3 - Inyección de dependencias por constructor

### 3.1 Una clase no debería fabricar sus propias dependencias

La inyección de dependencias consiste en recibir desde fuera los colaboradores que una clase necesita.

Una versión acoplada sería:

```java
public class AlumnoController {
    private final AlumnoService service = new AlumnoService();
}
```

Ese controlador ha decidido cómo construir el servicio. Si mañana `AlumnoService` necesita un repositorio, configuración u otras dependencias, el controlador tendría que conocer también esos detalles.

Con inyección por constructor:

```java
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final AlumnoService service;

    public AlumnoController(AlumnoService service) {
        this.service = service;
    }
}
```

El controlador declara lo que necesita, pero no decide cómo crearlo. Spring encuentra el bean `AlumnoService` y lo entrega al construir `AlumnoController`.

### 3.2 Por qué el campo es `final`

La dependencia se declara:

```java
private final AlumnoService service;
```

El uso de `final` expresa que el colaborador necesario para funcionar se fija durante la construcción y no se reasigna después.

Esto aporta varias ventajas:

- hace explícitas las dependencias obligatorias;
- evita un objeto parcialmente inicializado;
- facilita tests unitarios sin arrancar todo Spring;
- reduce mutabilidad accidental;
- hace visibles dependencias circulares durante el arranque.

### 3.3 Inyección por constructor frente a inyección por campo

Spring también permite escribir:

```java
@Autowired
private AlumnoService service;
```

pero no será el patrón principal del curso. La inyección por campo oculta la dependencia en el interior de la clase, impide usar `final` de forma natural y complica la construcción directa en tests.

Con constructor podemos probar una clase de esta manera:

```java
AlumnoService servicio = /* doble o instancia de prueba */;
AlumnoController controller = new AlumnoController(servicio);
```

La relación está visible en la API de construcción de la clase.

### 3.4 Un solo constructor no necesita `@Autowired`

En una clase gestionada por Spring con un único constructor, no es necesario anotarlo con `@Autowired`:

```java
public AlumnoController(AlumnoService service) {
    this.service = service;
}
```

Spring utiliza ese constructor automáticamente.

### 3.5 Preparación para 2.3

En 2.2 `AlumnoService` todavía no necesita otro bean porque conserva la lista en memoria. En 2.3 aparecerá una dependencia real:

```java
private final AlumnoRepository repositorio;

public AlumnoService(AlumnoRepository repositorio) {
    this.repositorio = repositorio;
}
```

Ese cambio demostrará por qué separar capas ahora resulta útil: el controlador seguirá dependiendo del servicio y no tendrá que saber que ha aparecido un repositorio.

### Pregunta

¿Qué ventaja concreta ofrece la inyección por constructor cuando escribimos tests?

### Respuesta razonada

Permite construir el objeto bajo prueba pasando explícitamente un doble o mock de sus dependencias, sin manipular campos privados ni necesitar que Spring haga una inyección posterior. Además, si falta una dependencia obligatoria, el propio constructor impide crear un objeto incompleto.

## Bloque 4 - Transformación DTO ↔ Entidad

### 4.1 DTO y entidad no representan necesariamente lo mismo

Un DTO describe datos que cruzan una frontera de la aplicación. Una entidad de persistencia representa datos según el modelo que utiliza la capa de almacenamiento. Aunque a veces tengan campos parecidos, cumplen responsabilidades distintas.

En M2 todavía **no existe una entidad JPA `Alumno` separada**. La colección en memoria utiliza `AlumnoDTO`, por lo que DTO y objeto almacenado coinciden de forma provisional.

Eso no cambia el principio arquitectónico que aprenderemos:

```text
controlador -> DTO
servicio    -> transforma / aplica reglas
repositorio -> entidad o modelo de almacenamiento
```

En Módulo 4, cuando aparezcan entidades JPA, esta frontera será física y no sólo conceptual.

### 4.2 Patrón `toDTO` / `toEntity`

Cuando los modelos son distintos es habitual encapsular la transformación en métodos del servicio o en un componente de mapeo dedicado.

Ejemplo conceptual:

```java
private AlumnoDTO toDTO(Alumno entidad) {
    AlumnoDTO dto = new AlumnoDTO();
    dto.setIdentificador(entidad.getIdentificador());
    dto.setNombre(entidad.getNombre());
    dto.setApellidos(entidad.getApellidos());
    return dto;
}
```

Y en dirección contraria:

```java
private Alumno toEntity(AlumnoDTO dto) {
    Alumno entidad = new Alumno();
    entidad.setNombre(dto.getNombre());
    entidad.setApellidos(dto.getApellidos());
    return entidad;
}
```

**Este código es ilustrativo:** todavía no creamos la clase `Alumno` porque hacerlo adelantaría contenido de persistencia del Módulo 4.

### 4.3 Por qué no convertir en el controlador

Si el controlador conociera entidades de persistencia, cambios internos del modelo de datos podrían obligarnos a cambiar también el contrato HTTP. Mantener DTOs en la frontera permite que API y persistencia evolucionen con menor acoplamiento.

Tampoco queremos trasladar esa transformación al futuro repositorio, porque el repositorio debe centrarse en acceso a datos y no conocer las decisiones de representación de una API concreta.

### 4.4 El flujo que construiremos progresivamente

Al terminar 2.3, una consulta tendrá conceptualmente este recorrido:

```text
GET /api/v1/alumnos/1
        |
        v
AlumnoController.consultar("1")
        |
        v
AlumnoService.consultar("1")
        |
        v
AlumnoRepository.buscarPorId("1")
        |
        v
modelo de almacenamiento
        |
        v
servicio transforma
        |
        v
AlumnoDTO
        |
        v
ResponseEntity -> JSON
```

En 2.2 nos detenemos un paso antes del repositorio. La lista está en el servicio para hacer visible la separación controlador/negocio sin introducir aún una tercera capa.

### Pregunta

¿Por qué no creamos ya una entidad `Alumno` sólo para poder escribir `toDTO` y `toEntity` reales?

### Respuesta razonada

Porque adelantaríamos conceptos de persistencia que todavía no hemos explicado y aumentaríamos complejidad sin necesidad. La fuente introduce aquí el patrón conceptual y deja la transformación real para cuando exista un modelo de entidad distinto. Mantener ese orden permite aprender una separación cada vez.

## Bloque 5 - Excepciones de negocio

### 5.1 Un error de negocio no es lo mismo que un error técnico

Una regla de negocio puede impedir una operación aunque la aplicación esté funcionando perfectamente.

Ejemplos:

- intentar crear un alumno con un DNI que ya existe;
- intentar solicitar dos veces la misma ayuda cuando el dominio lo prohíbe;
- intentar realizar una transición de estado no permitida.

En cambio, una caída de base de datos, un fichero corrupto o un error inesperado de programación son problemas técnicos.

La distinción importa porque el significado para el cliente es diferente:

```text
violación de regla del dominio -> normalmente respuesta 4xx
fallo interno inesperado       -> normalmente respuesta 5xx
```

En este punto implementaremos la primera regla explícita: **no crear dos alumnos con el mismo DNI**.

### 5.2 `NegocioException`

La fuente propone una excepción específica:

```java
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }

    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
```

Al extender `RuntimeException` es una excepción no comprobada (*unchecked*). El método que la lanza no necesita declararla con `throws`.

El servicio la utiliza así:

```java
if (existePorDni(dto.getDni())) {
    throw new NegocioException(
            "Ya existe un alumno con el DNI " + dto.getDni());
}
```

El servicio expresa **qué regla se ha violado**, no qué status HTTP debe utilizarse.

### 5.3 Manejo local con `@ExceptionHandler`

Todavía no hemos llegado al manejo global de errores del Módulo 5. Para no convertir la excepción de negocio en un 500, introducimos temporalmente un manejador local en `AlumnoController`:

```java
@ExceptionHandler(NegocioException.class)
public ResponseEntity<Map<String, Object>> handleNegocio(
        NegocioException ex) {

    Map<String, Object> error = Map.of(
            "status", 409,
            "error", "Conflict",
            "message", ex.getMessage()
    );

    return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(error);
}
```

Una creación duplicada puede responder:

```http
HTTP/1.1 409 Conflict
Content-Type: application/json
```

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe un alumno con el DNI DNI-DEMO-01"
}
```

El manejo es local porque estamos aprendiendo la separación progresivamente. Más adelante `@RestControllerAdvice` permitirá centralizar errores para varios controladores.

### 5.4 400, 404, 409 y 500 no significan lo mismo

En nuestro recorrido aparecen varios tipos de fallo:

- **400 Bad Request**: el cuerpo no puede convertirse correctamente al formato esperado, por ejemplo JSON mal formado.
- **404 Not Found**: la operación apunta a un recurso que no existe.
- **409 Conflict**: la petición tiene formato válido, pero entra en conflicto con el estado actual o una regla del dominio; nuestro DNI duplicado es el ejemplo.
- **500 Internal Server Error**: se produjo un fallo interno no tratado como una situación esperable del contrato.

Un buen diseño no convierte todas las excepciones en 200 ni todas en 500. El código HTTP debe reflejar la semántica de la situación.

### 5.5 Separación resultante

Al cerrar 2.2 tendremos esta distribución:

```text
controller/
  AlumnoController.java
  ExpedienteController.java
service/
  AlumnoService.java
  ExpedienteService.java
exception/
  NegocioException.java
dto/
  AlumnoDTO.java
  ExpedienteDTO.java
  SolicitanteDTO.java
```

`AlumnoController` manejará HTTP. `AlumnoService` será propietario provisional de la colección y de la regla de DNI duplicado. `ExpedienteService` repetirá el patrón como reto resuelto. En 2.3 el almacenamiento saldrá del servicio hacia `repository/`.

### Pregunta

¿Por qué es mejor lanzar una excepción de negocio que devolver `null` cuando el DNI está duplicado?

### Respuesta razonada

Porque `null` no explica qué ocurrió ni obliga al llamador a distinguir ausencia de un error de regla. Una excepción específica transporta intención, mensaje y causa, puede probarse directamente y después traducirse en un único punto a una respuesta HTTP coherente.

## Resumen del punto 2.2

- La **capa de servicio** contiene lógica de aplicación y reglas de negocio.
- `@Service` es un estereotipo de Spring y especializa `@Component`.
- El controlador recibe el servicio mediante **inyección por constructor** y un campo `final`.
- El servicio no devuelve `ResponseEntity` ni conoce códigos HTTP.
- DTO y entidad son responsabilidades distintas; la transformación real aparecerá cuando exista un modelo de persistencia separado.
- `NegocioException` representa una violación de regla de negocio.
- Un `@ExceptionHandler` local traduce provisionalmente el DNI duplicado a **409 Conflict**.
- La colección en memoria vive ahora en el servicio sólo hasta que 2.3 introduzca el repositorio.
