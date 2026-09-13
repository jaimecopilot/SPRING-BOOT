---
title: "Módulo 2 - Teoría"
author: "Curso Spring Boot 2026"
lang: es-ES
toc-title: "Índice"
---

# Módulo 2 - Creación y estructura de proyectos Spring Boot

## Propósito del módulo

M1 terminó con una API REST funcional: conocemos HTTP, JSON, Jackson, diseño de recursos y un CRUD en memoria. M2 cambia el foco. Ya no basta con que el código responda correctamente: empezamos a organizarlo para que pueda crecer sin convertir el controlador en el lugar donde ocurre todo.

El módulo recorrerá progresivamente la arquitectura en capas. Primero formalizaremos la responsabilidad del **controlador**; después moveremos la lógica a un **servicio**; a continuación aislaremos el acceso a datos en un **repositorio**; estudiaremos cómo Spring conecta esas piezas mediante inversión de control e inyección de dependencias; aprenderemos a probarlas; y terminaremos con organización, documentación y herramientas de calidad.

> **Baseline del curso:** Java 17, Maven Wrapper 3.9.16 y Spring Boot 3.5.16.

---

# Punto 2.1 - Capas de la aplicación: Controlador

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar con precisión cuál es la responsabilidad de un controlador.
2. Diferenciar `@Controller` de `@RestController` y saber cuándo usar cada uno.
3. Usar correctamente las anotaciones de mapeo a nivel de clase y de método.
4. Extraer datos de la petición con `@PathVariable`, `@RequestParam`, `@RequestBody` y `@RequestHeader`.
5. Construir respuestas HTTP con `ResponseEntity`, controlando código de estado, cabeceras y cuerpo.
6. Identificar y corregir los anti-patrones más comunes en controladores.
7. Refactorizar un controlador para hacerlo más fino sin adelantar todavía la capa de servicio del punto 2.2.

## Bloque 1 - Qué es un controlador y cuál es su responsabilidad

### 1.1 El controlador es la frontera HTTP

Un controlador es la puerta de entrada HTTP de una aplicación Spring Boot. Recibe una petición, interpreta sus datos y decide qué operación de la aplicación debe invocarse. Cuando un navegador, una aplicación móvil u otro sistema llama a una URL, Spring MVC localiza el método de controlador cuyo *mapping* coincide con la ruta y el método HTTP.

Su responsabilidad principal puede resumirse así:

```text
HTTP -> controlador -> Java
Java -> controlador -> HTTP
```

El controlador traduce entre ambos mundos. De una petición puede obtener:

- variables incluidas en la ruta;
- parámetros de la *query string*;
- cabeceras;
- un cuerpo JSON convertido a un DTO.

Y al responder puede decidir:

- el código de estado;
- las cabeceras;
- el objeto que irá en el cuerpo.

Esto no convierte al controlador en el lugar donde debe vivir la lógica de negocio. El hecho de que sea el primero que recibe la petición no significa que deba resolver por sí solo todo el trabajo.

### 1.2 El controlador como traductor HTTP-Java

Supón esta petición:

```http
PUT /api/v1/alumnos/7?notificar=true HTTP/1.1
Content-Type: application/json
X-Canal: portal

{
  "nombre": "Ana",
  "curso": "6º Primaria"
}
```

En el lado Java podrían aparecer cuatro piezas distintas:

```java
@PutMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizar(
        @PathVariable String id,
        @RequestParam(defaultValue = "false") boolean notificar,
        @RequestHeader(value = "X-Canal", required = false) String canal,
        @RequestBody AlumnoDTO dto) {
    // delegación y construcción de respuesta
}
```

`id`, `notificar`, `canal` y `dto` llegan por mecanismos HTTP distintos. El controlador los hace accesibles como tipos Java. El camino de vuelta funciona al revés: devuelve un objeto Java y Spring MVC, con Jackson cuando procede, lo transforma en el cuerpo de la respuesta.

Esta separación hace posible que el resto del código no tenga que conocer detalles como cabeceras, rutas o códigos HTTP.

### 1.3 Lo que el controlador no debe hacer

Un diseño por capas necesita límites claros. Un controlador no debería:

- contener reglas de negocio complejas;
- acceder directamente a la base de datos;
- gestionar transacciones;
- construir JSON manualmente con concatenaciones de texto;
- exponer entidades de persistencia cuando existe un contrato DTO;
- capturar indiscriminadamente todas las excepciones;
- conservar estado mutable propio de una petición.

La última regla merece especial atención. Los controladores Spring son normalmente beans singleton: una misma instancia puede atender muchas peticiones. Un campo mutable usado como estado temporal de una petición podría ser leído o modificado por varios hilos.

En nuestro proyecto todavía existe una lista en memoria dentro de `AlumnoController`. Es una **limitación pedagógica heredada de M1**, no el diseño final que queremos. En 2.1 la utilizaremos para aprender a reconocer responsabilidades; en 2.2 la lógica saldrá hacia el servicio y en 2.3 el almacenamiento quedará detrás del repositorio.

### Pregunta

¿Por qué es peligroso guardar en un campo del controlador información temporal de una petición concreta?

### Respuesta razonada

Porque la misma instancia del controlador puede atender peticiones concurrentes. Si dos peticiones escriben el mismo campo, una puede sobrescribir o leer el estado de la otra. Los datos propios de una petición deben viajar como parámetros y variables locales; el estado compartido necesita una capa y una estrategia diseñadas expresamente para ello.

## Bloque 2 - Anotaciones de mapeo en profundidad

### 2.1 `@Controller` y `@RestController`

Spring MVC ofrece dos anotaciones relacionadas pero con propósitos distintos.

`@Controller` marca una clase como controlador MVC. Por defecto, un `String` devuelto por sus métodos puede interpretarse como el nombre de una vista que debe renderizarse. Es la opción clásica para aplicaciones web con plantillas de servidor, por ejemplo Thymeleaf.

`@RestController` está orientado a APIs. Conceptualmente combina:

```java
@Controller
@ResponseBody
```

Por eso el valor devuelto por cada método se escribe como cuerpo de la respuesta. Si devolvemos un DTO o un `Map`, Spring MVC puede serializarlo como JSON.

Ejemplo de API REST:

```java
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
    // endpoints REST
}
```

Ejemplo conceptual de controlador de vistas:

```java
@Controller
public class PaginaController {

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }
}
```

En el segundo caso `"inicio"` puede ser un nombre de vista. En el primero, los valores devueltos forman parte de la respuesta HTTP de la API.

Regla práctica del curso:

```text
API REST -> @RestController
web con vistas renderizadas en servidor -> @Controller
```

### 2.2 `@RequestMapping` a nivel de clase

`@RequestMapping` puede definir un prefijo común para todos los endpoints de un controlador:

```java
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    @GetMapping
    public List<AlumnoDTO> listar() {
        // ...
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
        // ...
    }

    @PostMapping
    public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
        // ...
    }
}
```

Las rutas resultantes son:

```text
GET  /api/v1/alumnos
GET  /api/v1/alumnos/{id}
POST /api/v1/alumnos
```

Sin el mapping de clase tendríamos que repetir el prefijo en todos los métodos. Centralizarlo reduce duplicación y evita inconsistencias.

También existen atributos como `produces` y `consumes`:

```java
@RequestMapping(
        value = "/api/v1/alumnos",
        produces = "application/json")
```

No debemos añadirlos por rutina. Con `@RestController`, Jackson y negociación de contenido, Spring MVC ya dispone de información suficiente para muchos casos habituales.

### 2.3 Anotaciones especializadas de método

Para cada operación HTTP usamos anotaciones especializadas:

- `@GetMapping`;
- `@PostMapping`;
- `@PutMapping`;
- `@PatchMapping`;
- `@DeleteMapping`.

Son formas más legibles de expresar un `@RequestMapping` restringido a un método HTTP. Por ejemplo:

```java
@GetMapping("/{id}")
```

expresa de forma directa que la operación sólo atiende GET sobre la ruta relativa `/{id}`.

Las rutas de método se componen con el prefijo de la clase. En nuestro controlador:

```java
@RequestMapping("/api/v1/alumnos")
```

más:

```java
@GetMapping("/{id}")
```

produce:

```text
GET /api/v1/alumnos/{id}
```

### Pregunta

¿Por qué conviene poner `/api/v1/alumnos` a nivel de clase y no repetirlo en cada método?

### Respuesta razonada

Porque ese prefijo expresa el recurso gestionado por el controlador. Definirlo una sola vez reduce repetición, evita que una operación quede accidentalmente bajo otra ruta y permite cambiar la base del recurso en un único lugar.

## Bloque 3 - Extracción de datos de la petición

### 3.1 `@PathVariable`: datos que identifican la ruta

Una variable de ruta forma parte de la identidad del recurso:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    // ...
}
```

Si llamamos:

```text
GET /api/v1/alumnos/12345
```

`id` vale `"12345"`.

Si el nombre del parámetro Java es distinto, podemos explicitar la conexión:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(
        @PathVariable("id") String identificador) {
    // ...
}
```

También pueden coexistir varias variables:

```java
@GetMapping("/{id}/documentos/{docId}")
public ResponseEntity<DocumentoDTO> consultarDocumento(
        @PathVariable String id,
        @PathVariable String docId) {
    // ...
}
```

### 3.2 `@RequestParam`: filtros y opciones

Los *query parameters* no suelen identificar por sí mismos el recurso; normalmente modifican cómo consultamos una colección:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam(required = false) String curso) {
    // ...
}
```

Ejemplo:

```text
GET /api/v1/alumnos?curso=5º%20Primaria
```

`required = false` hace que `curso` sea opcional. Si no aparece, el valor será `null`.

Para paginación podemos proporcionar valores por defecto:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam(required = false) String curso,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
    // ...
}
```

Si el cliente no envía `page` ni `size`, Spring utiliza `0` y `20`.

También es posible recibir varios valores con el mismo nombre:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam List<String> cursos) {
    // ...
}
```

Una petición como:

```text
/api/v1/alumnos?cursos=5º&cursos=6º
```

puede convertirse en una lista con dos elementos.

### 3.3 `@RequestBody`: el cuerpo HTTP convertido a Java

`@RequestBody` pide a Spring MVC que convierta el cuerpo a un objeto Java:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(
        @RequestBody AlumnoDTO dto) {
    // ...
}
```

Con `Content-Type: application/json`, Jackson deserializa el JSON a `AlumnoDTO`. Si el JSON no puede analizarse, la petición falla antes de que dispongamos de un DTO válido y Spring MVC devuelve un 400.

En nuestro curso utilizamos cuerpo principalmente con POST, PUT y PATCH. Aunque HTTP no prohíbe de forma absoluta un cuerpo en todas las demás operaciones, diseñar un GET dependiente de `@RequestBody` sería una mala elección para una API interoperable y no será nuestro modelo.

### 3.4 `@RequestHeader`: metadatos de la petición

Las cabeceras transportan metadatos. Podemos leer una concreta:

```java
@GetMapping("/info-peticion")
public Map<String, String> infoPeticion(
        @RequestHeader(value = "User-Agent", required = false) String userAgent) {
    // ...
}
```

O varias:

```java
@GetMapping("/cabeceras")
public Map<String, String> cabeceras(
        @RequestHeader Map<String, String> cabeceras) {
    return cabeceras;
}
```

En la práctica de 2.1 utilizaremos `User-Agent` y `Accept-Language` para observar esta frontera sin introducir todavía seguridad ni autenticación.

### Pregunta

¿Qué diferencia conceptual hay entre `@PathVariable` y `@RequestParam`?

### Respuesta razonada

Una variable de ruta suele formar parte de la identificación o jerarquía del recurso, como `/alumnos/7`. Un parámetro de query suele modificar la consulta o la representación, como `?curso=5º&page=0`. No es una regla puramente sintáctica: expresa el diseño del contrato HTTP.

## Bloque 4 - Construcción de la respuesta

### 4.1 `ResponseEntity`: status, cabeceras y cuerpo

`ResponseEntity<T>` representa una respuesta HTTP completa. Permite controlar tres dimensiones que no debemos confundir:

```text
status + headers + body
```

Ejemplo de consulta:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    return buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

Si el recurso existe obtenemos 200 con cuerpo. Si no existe obtenemos 404 sin cuerpo.

Constructores frecuentes:

```java
ResponseEntity.ok(cuerpo);                    // 200
ResponseEntity.created(uri).body(cuerpo);    // 201 + Location
ResponseEntity.noContent().build();          // 204
ResponseEntity.notFound().build();           // 404
ResponseEntity.badRequest().body(cuerpo);    // 400
ResponseEntity.status(HttpStatus.CONFLICT)
        .body(cuerpo);                        // 409
```

También podemos añadir cabeceras explícitas:

```java
return ResponseEntity
        .status(HttpStatus.CREATED)
        .header("X-Custom-Header", "valor")
        .body(alumno);
```

### 4.2 La cabecera `Location` en una creación

Cuando POST crea un recurso, 201 comunica la creación, pero `Location` añade información útil: dónde puede consultarse el recurso recién creado.

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    AlumnoDTO creado = guardarAlumno(dto);
    URI location = URI.create(
            "/api/v1/alumnos/" + creado.getIdentificador());
    return ResponseEntity.created(location).body(creado);
}
```

Una respuesta posible es:

```http
HTTP/1.1 201 Created
Location: /api/v1/alumnos/3
Content-Type: application/json
```

El cuerpo puede contener además el DTO creado.

En aplicaciones que necesitan construir URLs absolutas teniendo en cuenta el contexto de la petición existen utilidades como `ServletUriComponentsBuilder`. En este ejercicio mantendremos la URI relativa de la fuente porque hace visible el concepto sin añadir otra abstracción antes de tiempo.

### 4.3 204 significa éxito sin representación

`204 No Content` indica que la operación ha tenido éxito y que no hay cuerpo de respuesta:

```java
return ResponseEntity.noContent().build();
```

Por definición no debemos acompañar un 204 de un JSON informativo. Si necesitamos devolver un resultado, elegiremos una respuesta con cuerpo y un status apropiado.

### 4.4 Serialización automática: devolver objetos, no construir JSON

Cuando el controlador devuelve un DTO, una lista o un `Map`, Spring MVC puede delegar en Jackson la serialización. No necesitamos hacer algo como:

```java
return "{\"nombre\":\"Ana\"}";
```

Además de ser incómodo, construir JSON a mano rompe fácilmente escapes, formatos y consistencia.

Preferimos:

```java
return Map.of("nombre", "Ana");
```

o devolver directamente un DTO.

Las reglas de Jackson aprendidas en M1 (`@JsonProperty`, `@JsonFormat`, `@JsonInclude`, `@JsonIgnore`) siguen formando parte del contrato.

### Pregunta

¿Por qué es mejor devolver objetos que construir manualmente el JSON en el controlador?

### Respuesta razonada

Porque separa el modelo Java de la representación textual y deja la serialización a una librería especializada. Esto evita errores de escape, aplica de forma consistente las anotaciones de Jackson y hace el código más fácil de probar y modificar.

## Bloque 5 - Anti-patrones y buenas prácticas

### 5.1 El controlador gordo

Un controlador se vuelve “gordo” cuando acumula reglas, persistencia, transformaciones y decisiones que deberían vivir en otras capas.

Ejemplo deliberadamente problemático:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    for (AlumnoDTO a : alumnos) {
        if (a.getDni().equals(dto.getDni())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    dto.setIdentificador(String.valueOf(alumnos.size() + 1));
    alumnos.add(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
}
```

El método decide una regla de duplicados, genera el ID, persiste y además construye la respuesta. Cada cambio de negocio obliga a modificar la frontera HTTP.

El diseño al que nos dirigimos en 2.2 será parecido a:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    AlumnoDTO creado = servicio.crear(dto);
    URI location = URI.create(
            "/api/v1/alumnos/" + creado.getIdentificador());
    return ResponseEntity.created(location).body(creado);
}
```

En 2.1 **todavía no crearemos `AlumnoService`**. Para hacer visible la frontera sin adelantarnos, extraeremos temporalmente la lógica a métodos privados del mismo controlador. Es una etapa pedagógica, no la arquitectura final.

### 5.2 Acceder directamente al repositorio desde el controlador

Otro anti-patrón frecuente es saltarse la capa de servicio:

```java
@RestController
public class AlumnoController {

    @Autowired
    private AlumnoRepository repository; // anti-patrón en esta arquitectura

    @GetMapping("/{id}")
    public ResponseEntity<Alumno> consultar(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
```

Este ejemplo mezcla la frontera HTTP con acceso a datos y además expone una entidad. Si cambian las reglas o la tecnología de persistencia, el controlador queda acoplado a ellas.

En M2 construiremos el camino:

```text
Controller -> Service -> Repository
```

El controlador manejará HTTP; el servicio, lógica y coordinación; el repositorio, almacenamiento.

### 5.3 Buenas prácticas que aplicaremos

Un controlador mantenible tiende a cumplir estas reglas:

- métodos públicos cortos y declarativos;
- dependencias inyectadas por constructor;
- ausencia de estado mutable propio de una petición;
- ausencia de `try-catch` repetidos para política global de errores;
- ausencia de reglas de negocio;
- DTOs como contrato de entrada/salida;
- status HTTP coherentes;
- cabeceras significativas cuando el protocolo las necesita;
- documentación del contrato.

La fuente propone como orientación que un método de controlador ronde 5-10 líneas cuando el caso lo permita. No lo trataremos como una ley mecánica: el criterio real es que el método muestre claramente la traducción HTTP y la delegación, no que persigamos un contador de líneas.

### Pregunta

¿Qué ventaja aporta la inyección por constructor frente a inyectar directamente en un campo?

### Respuesta razonada

Hace explícitas las dependencias necesarias para construir el objeto, permite declarar los campos `final`, facilita instanciar la clase en tests y evita depender de que el contenedor modifique campos después de la construcción. La veremos funcionando en 2.2 y 2.4.

## Resumen del punto 2.1

Al terminar este punto debes poder explicar y reconocer:

- el controlador como frontera `HTTP <-> Java`;
- la diferencia entre `@Controller` y `@RestController`;
- el prefijo común definido por `@RequestMapping`;
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@PatchMapping` y `@DeleteMapping`;
- `@PathVariable`, `@RequestParam`, `@RequestBody` y `@RequestHeader`;
- `ResponseEntity` como combinación de status, cabeceras y cuerpo;
- 201 con `Location` para una creación;
- 204 para éxito sin cuerpo;
- serialización automática de objetos con Jackson;
- el anti-patrón de controlador gordo;
- el anti-patrón de acceder directamente al repositorio;
- el objetivo inmediato: dejar el controlador preparado para mover la lógica a `AlumnoService` en 2.2.

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

