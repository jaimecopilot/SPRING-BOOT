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
