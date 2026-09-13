---
title: "Módulo 2 - Práctica"
author: "Curso Spring Boot 2026"
lang: es-ES
toc-title: "Índice"
---

# Módulo 2 - Práctica

## Punto de partida

Esta guía continúa exactamente desde el proyecto final de M1. Antes de modificar nada, comprueba que siguen funcionando `/hola`, `/adios`, el ejemplo JSON de expedientes y el CRUD de alumnos.

Trabajaremos siempre desde:

```text
M2/proyecto
```

En Linux/macOS:

```bash
cd M2/proyecto
./mvnw test
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
cd M2\proyecto
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

Cuando la aplicación esté arriba, usa otra terminal para los `curl`.

---

# Punto 2.1 - Capas de la aplicación: Controlador

## Objetivo práctico

Vamos a refactorizar `AlumnoController` sin crear todavía la capa de servicio. Queremos distinguir con claridad el manejo HTTP de la lógica que más adelante saldrá del controlador, añadir ejemplos reales de cabeceras y parámetros, mejorar la respuesta del POST con `Location` y terminar con una representación sencilla con enlaces.

La práctica conserva el CRUD que ya funciona. Ninguna mejora de 2.1 debe borrar silenciosamente filtro, ordenación, PUT, PATCH o DELETE aprendidos en M1.

## Paso 1 - Repasar el estado actual

Abre:

```text
M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java
```

Debes reconocer:

- una colección mutable de alumnos en memoria;
- `GET /api/v1/alumnos`;
- `GET /api/v1/alumnos/{id}`;
- `POST /api/v1/alumnos`;
- `PUT /api/v1/alumnos/{id}`;
- `PATCH /api/v1/alumnos/{id}`;
- `DELETE /api/v1/alumnos/{id}`;
- filtro opcional por `curso`;
- ordenación opcional con `sort`.

Arranca la aplicación y comprueba el baseline:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
curl -i http://localhost:8080/api/v1/expedientes/ejemplo
curl -i http://localhost:8080/api/v1/alumnos
curl -i http://localhost:8080/api/v1/alumnos/1
```

Resultado esperado: todos esos endpoints siguen respondiendo según M0/M1.

### Pregunta

¿Ves lógica distinta del manejo HTTP dentro de algún método del controlador?

### Respuesta razonada

Sí. La generación de identificadores, la búsqueda en la colección, la sustitución de objetos y la persistencia en memoria no son decisiones del protocolo HTTP. En una arquitectura por capas acabarán fuera del controlador. En 2.1 todavía no creamos el servicio: primero hacemos visible esa frontera.

## Paso 2 - Identificar la lógica que debería ir en un servicio

Observa el POST heredado. En M1 ya mejoramos la asignación del ID para evitar la colisión que produciría `size() + 1` después de borrados:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    int siguienteId = alumnos.stream()
            .map(AlumnoDTO::getIdentificador)
            .filter(id -> id != null && id.matches("\\d+"))
            .mapToInt(Integer::parseInt)
            .max()
            .orElse(0) + 1;

    dto.setIdentificador(String.valueOf(siguienteId));
    alumnos.add(dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
}
```

Aquí hay tres responsabilidades:

1. **Generar un ID.** Es una decisión de la aplicación, no de HTTP.
2. **Guardar el alumno.** Es persistencia, aunque por ahora sea sólo una lista.
3. **Construir la respuesta.** Eso sí pertenece al controlador.

En la fuente original de esta práctica aparece `alumnos.size() + 1`. No lo recuperamos porque M1 ya corrigió ese problema: si borras un elemento, el tamaño deja de ser una estrategia segura para generar un ID. Conservamos la intención del ejercicio con la versión acumulativa correcta.

### Pregunta

¿Qué ganamos separando la lógica de negocio del manejo HTTP?

### Respuesta razonada

Podemos cambiar reglas o almacenamiento sin reescribir el contrato HTTP, probar la lógica sin simular peticiones y mantener los métodos del controlador centrados en traducir petición y respuesta.

## Paso 3 - Refactorizar el método crear

Extrae la parte de generación y guardado a un helper privado `guardarAlumno` y mejora la respuesta con `Location`.

Añade el import:

```java
import java.net.URI;
```

El método público queda:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    AlumnoDTO creado = guardarAlumno(dto);
    URI location = URI.create(
            "/api/v1/alumnos/" + creado.getIdentificador());
    return ResponseEntity.created(location).body(creado);
}
```

Y el helper:

```java
private AlumnoDTO guardarAlumno(AlumnoDTO dto) {
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

Reinicia y prueba:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez","dni":"22222222D","fechaNacimiento":"2010-07-15","curso":"5º Primaria"}'
```

Debes observar algo equivalente a:

```text
HTTP/1.1 201
Location: /api/v1/alumnos/3
```

Además, el cuerpo contiene el alumno con `id` 3 y puedes consultarlo:

```bash
curl -i http://localhost:8080/api/v1/alumnos/3
```

### Pregunta

¿Qué ventaja tiene que el cliente reciba `Location`?

### Respuesta razonada

No necesita reconstruir la URL del recurso creado a partir de conocimiento externo. El servidor comunica dónde está el nuevo recurso como parte del contrato HTTP.

## Paso 4 - Añadir un endpoint con `@RequestHeader`

Añade:

```java
import org.springframework.web.bind.annotation.RequestHeader;
```

Y el endpoint:

```java
@GetMapping("/info-peticion")
public Map<String, String> infoPeticion(
        @RequestHeader(value = "User-Agent", required = false) String userAgent,
        @RequestHeader(value = "Accept-Language", required = false) String idioma) {

    return Map.of(
            "userAgent", userAgent != null ? userAgent : "desconocido",
            "idioma", idioma != null ? idioma : "desconocido"
    );
}
```

Prueba con cabeceras explícitas:

```bash
curl -i http://localhost:8080/api/v1/alumnos/info-peticion \
  -H "User-Agent: MiCliente/1.0" \
  -H "Accept-Language: es-ES"
```

Resultado esperado:

```json
{
  "userAgent": "MiCliente/1.0",
  "idioma": "es-ES"
}
```

Prueba también sin esas cabeceras personalizadas:

```bash
curl -i http://localhost:8080/api/v1/alumnos/info-peticion
```

`curl` enviará su propio `User-Agent`; `Accept-Language` puede quedar como `desconocido` si no se envía.

### Pregunta

¿Para qué puede ser útil leer `User-Agent`?

### Respuesta razonada

Permite observar qué tipo de cliente declara realizar la petición y puede servir para diagnóstico o compatibilidad. No debe tratarse como una identidad segura: es una cabecera que el cliente puede modificar.

## Paso 5 - Añadir `@RequestParam` con valores por defecto sin perder M1

La fuente añade `page` y `size`. Nuestro baseline M1 ya tenía `curso` y `sort`, por lo que **los conservamos** y añadimos paginación.

El método final de listado será:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam(required = false) String curso,
        @RequestParam(required = false) String sort,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {

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

Prueba:

```bash
# Sin parámetros: página 0, tamaño 20
curl http://localhost:8080/api/v1/alumnos

# Primera página de un elemento
curl "http://localhost:8080/api/v1/alumnos?page=0&size=1"

# Segunda página de un elemento
curl "http://localhost:8080/api/v1/alumnos?page=1&size=1"

# Conservamos el contrato de ordenación de M1
curl "http://localhost:8080/api/v1/alumnos?sort=nombre&page=0&size=20"

# Se pueden combinar filtro, orden y paginación
curl "http://localhost:8080/api/v1/alumnos?curso=5%C2%BA%20Primaria&sort=nombre&page=0&size=10"
```

Con los dos alumnos iniciales, `page=0&size=1` y `page=1&size=1` devuelven elementos distintos.

> Esta paginación con `skip`/`limit` es pedagógica. Todavía no proporciona metadatos como número total de elementos o páginas. Más adelante veremos modelos de paginación más completos.

### Pregunta

¿Por qué paginar una colección grande?

### Respuesta razonada

Para no transferir ni procesar todos los elementos en cada petición. Reduce tamaño de respuesta, memoria, trabajo de serialización y tiempo de red. En una aplicación real la paginación debe trasladarse también a la capa de datos, no hacerse después de cargarlo todo en memoria.

## Paso 6 - Añadir un endpoint que devuelve 204

Para observar `204 No Content`, añade el endpoint propuesto por la fuente:

```java
@PostMapping("/promocionar")
public ResponseEntity<Void> promocionar() {
    alumnos.forEach(
            a -> a.setCurso(a.getCurso() + " (promocionado)"));
    return ResponseEntity.noContent().build();
}
```

Prueba:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos/promocionar
```

Resultado esperado:

```text
HTTP/1.1 204
```

No debe aparecer cuerpo. Después comprueba el efecto:

```bash
curl http://localhost:8080/api/v1/alumnos
```

Los cursos contienen el sufijo `(promocionado)`.

> Es un ejercicio para estudiar el status y la frontera del controlador. No pretende modelar todavía una regla de promoción escolar real; esa lógica pertenecería a un servicio.

### Pregunta

¿Por qué un 204 no debe incluir cuerpo?

### Respuesta razonada

Porque el propio status comunica que la operación terminó correctamente sin representación que devolver. Añadir un cuerpo contradice la semántica del código y algunos clientes pueden ignorarlo.

## Paso 7 - Refactorizar el controlador para que sea más fino

Extrae la búsqueda repetida a un helper:

```java
private Optional<AlumnoDTO> buscarPorId(String id) {
    return alumnos.stream()
            .filter(a -> a.getIdentificador().equals(id))
            .findFirst();
}
```

Usa ese helper en la consulta:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    return buscarPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

Y en PUT:

```java
@PutMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizar(
        @PathVariable String id,
        @RequestBody AlumnoDTO dto) {

    return buscarPorId(id)
            .map(existente -> {
                dto.setIdentificador(id);
                alumnos.set(alumnos.indexOf(existente), dto);
                return ResponseEntity.ok(dto);
            })
            .orElse(ResponseEntity.notFound().build());
}
```

También puedes usar el mismo helper como punto de entrada para PATCH y para el reto con enlaces. El objetivo no es esconder código para reducir líneas a cualquier precio: es identificar una operación reutilizable y darle un nombre.

### Pregunta

¿Qué ventaja aporta `buscarPorId`?

### Respuesta razonada

Centraliza una operación repetida y hace que los métodos HTTP expresen mejor su intención. En 2.2 ese tipo de operación ya no quedará como helper privado del controlador: se moverá a una dependencia de servicio.

## Paso 8 - Probar el controlador refactorizado

Reinicia la aplicación para partir de un estado limpio y recorre el contrato:

```bash
# Listar
curl -i http://localhost:8080/api/v1/alumnos

# Consultar
curl -i http://localhost:8080/api/v1/alumnos/1

# Crear
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"33333333E","fechaNacimiento":"2011-03-20","curso":"4º Primaria"}'

# Actualizar
curl -i -X PUT http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","apellidos":"García López","dni":"12345678A","fechaNacimiento":"2010-05-12","curso":"6º Primaria"}'

# Actualizar parcialmente
curl -i -X PATCH http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{"curso":"6º Primaria B"}'

# Información de cabeceras
curl -i http://localhost:8080/api/v1/alumnos/info-peticion \
  -H "User-Agent: MiCliente/1.0" \
  -H "Accept-Language: es-ES"

# Paginación
curl -i "http://localhost:8080/api/v1/alumnos?page=0&size=1"

# Eliminar el alumno recién creado (id 3)
curl -i -X DELETE http://localhost:8080/api/v1/alumnos/3
```

El CRUD heredado debe seguir funcionando y los nuevos endpoints deben convivir con él.

### Pregunta

¿Por qué conviene hacer esta regresión antes de pasar a 2.2?

### Respuesta razonada

Porque una refactorización pretende cambiar estructura sin perder comportamiento. Si detectamos una regresión ahora, sabemos que nació en 2.1 y no la confundiremos con la introducción posterior del servicio.

## Paso 9 - Analizar los códigos de estado devueltos

Con una instancia limpia, la matriz principal debe ser:

| Operación | Resultado | Código | Cabecera especial |
|---|---|---:|---|
| GET colección | éxito | 200 | - |
| GET individual | existe | 200 | - |
| GET individual | no existe | 404 | - |
| POST crear | creado | 201 | `Location` |
| POST promocionar | éxito sin cuerpo | 204 | - |
| PUT | existe | 200 | - |
| PUT | no existe | 404 | - |
| PATCH | existe | 200 | - |
| PATCH | no existe | 404 | - |
| DELETE | existe | 204 | - |
| DELETE | no existe | 404 | - |

Comprueba específicamente `Location`:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Lucía","apellidos":"Ramos","dni":"44444444F","fechaNacimiento":"2011-01-10","curso":"4º Primaria"}'
```

No basta con ver un JSON correcto: el status y las cabeceras son parte del contrato.

## Paso 10 - Probar un error de validación

La fuente denomina este paso “error de validación”, pero el ejemplo concreto es un **error de formato JSON**, no Bean Validation. Mantendremos esa distinción.

Envía JSON mal formado:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro", "apellidos":"Sánchez",}'
```

Resultado esperado:

```text
HTTP/1.1 400
```

No fijamos como contrato el JSON interno exacto que Spring Boot use para representar el error, porque puede variar con configuración y versiones. Lo estable aquí es que Jackson no puede deserializar el cuerpo y la petición termina como 400.

Prueba también un tipo de contenido incorrecto:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: text/plain" \
  --data 'hola'
```

Debe producir 415 cuando el endpoint espera un `AlumnoDTO` desde JSON.

### Pregunta

¿Qué otros errores de formato pueden producir 400?

### Respuesta razonada

Por ejemplo, JSON sin cerrar, comillas incorrectas o un valor que Jackson no pueda convertir al tipo Java esperado, como una fecha incompatible con el formato configurado. Eso es diferente de una regla de negocio como “DNI duplicado”, que estudiaremos al introducir el servicio.

## Paso 11 - Errores comunes del ejercicio

| Síntoma | Causa probable | Comprobación / solución |
|---|---|---|
| 405 Method Not Allowed | método HTTP incorrecto | revisar `@GetMapping`, `@PostMapping`, etc. |
| 415 Unsupported Media Type | `Content-Type` incompatible o ausente en una petición con cuerpo | enviar `Content-Type: application/json` |
| 400 Bad Request | JSON mal formado o conversión imposible | revisar sintaxis y tipos |
| parámetro inesperadamente `null` | parámetro opcional no tratado | `required=false`, `defaultValue` y lógica defensiva |
| `UnsupportedOperationException` | colección inmutable | conservar `new ArrayList<>(List.of(...))` |
| POST devuelve 201 pero no `Location` | se usa `status(CREATED)` sin cabecera | usar `ResponseEntity.created(location)` |
| 204 devuelve contenido | se intenta enviar cuerpo | usar `noContent().build()` |
| PATCH no cambia nada | clave del `Map` no coincide | revisar nombres de propiedades |
| filtro/orden de M1 desaparece | se reemplazó `listar` por el ejemplo de 2.1 sin integrar | conservar `curso` y `sort` al añadir `page`/`size` |
| ID nuevo colisiona después de un borrado | se reintrodujo `size()+1` | conservar máximo ID numérico + 1 |

No cambies código al azar. Primero identifica qué capa está fallando: mapping, conversión HTTP-Java, lógica en memoria o serialización.

## Paso 12 - Reto resuelto: endpoint que devuelve un alumno con enlaces HATEOAS

El reto de la fuente introduce una aproximación sencilla a hipermedia sin añadir todavía una dependencia específica de Spring HATEOAS.

Añade:

```java
import java.util.LinkedHashMap;
```

Y el endpoint:

```java
@GetMapping("/{id}/con-enlaces")
public ResponseEntity<Map<String, Object>> consultarConEnlaces(
        @PathVariable String id) {

    return buscarPorId(id)
            .map(alumno -> {
                Map<String, Object> respuesta = new LinkedHashMap<>();
                respuesta.put("alumno", alumno);
                respuesta.put("_links", Map.of(
                        "self", "/api/v1/alumnos/" + id,
                        "documentos", "/api/v1/alumnos/" + id + "/documentos",
                        "curso", "/api/v1/cursos/" + alumno.getCurso()
                ));
                return ResponseEntity.ok(respuesta);
            })
            .orElse(ResponseEntity.notFound().build());
}
```

Prueba:

```bash
curl -i http://localhost:8080/api/v1/alumnos/1/con-enlaces
```

Obtendrás una estructura equivalente a:

```json
{
  "alumno": {
    "id": "1",
    "nombre": "Ana",
    "apellidos": "García López",
    "dni": "DNI-DEMO-01",
    "fechaNacimiento": "2010-05-12",
    "curso": "5º Primaria"
  },
  "_links": {
    "self": "/api/v1/alumnos/1",
    "documentos": "/api/v1/alumnos/1/documentos",
    "curso": "/api/v1/cursos/5º Primaria"
  }
}
```

Prueba también un ID inexistente:

```bash
curl -i http://localhost:8080/api/v1/alumnos/999/con-enlaces
```

Debe responder 404.

> Esto es una aproximación pedagógica. Los enlaces se representan con un `Map`; no estamos afirmando que sea una implementación completa de un estándar hipermedia ni introduciendo todavía Spring HATEOAS.

### Pregunta

¿Qué gana el cliente cuando la representación incluye enlaces?

### Respuesta razonada

La respuesta no sólo entrega datos; también describe rutas relacionadas que el cliente puede seguir. Reduce el conocimiento externo necesario para navegar. La utilidad real depende de que las relaciones y sus formatos estén definidos de forma consistente.

## Resultado esperado global de 2.1

Al terminar este punto, `AlumnoController` debe:

- conservar el CRUD de M1;
- conservar filtro y ordenación;
- añadir paginación pedagógica `page`/`size`;
- usar `@PathVariable`, `@RequestParam`, `@RequestBody` y `@RequestHeader`;
- devolver 201 con `Location` al crear;
- disponer de una operación 204 sin cuerpo;
- reutilizar `buscarPorId` y `guardarAlumno`;
- exponer `/info-peticion`;
- exponer `/{id}/con-enlaces`;
- seguir dejando visible que la lógica en memoria es provisional y está preparada para migrar a un servicio en 2.2.

Ejecuta al final:

```bash
./mvnw test
./mvnw package
```

En Windows:

```powershell
.\mvnw.cmd test
.\mvnw.cmd package
```

## Recorridos de entorno para editar y ejecutar 2.1

### Desde IntelliJ IDEA

1. Abre la carpeta `M2/proyecto` como proyecto Maven.
2. Comprueba que el Project SDK es Java 17.
3. Abre `AlumnoController.java` desde `src/main/java/.../controller`.
4. Usa **Code > Optimize Imports** si quedan imports obsoletos tras la refactorización.
5. Ejecuta `MiProyectoApplication` desde el icono de ejecución de la clase.
6. Usa la terminal integrada para lanzar los `curl` o Maven Wrapper.

### Desde Eclipse

1. Importa `M2/proyecto` como **Existing Maven Project**.
2. Comprueba Java 17 en las propiedades del proyecto.
3. Edita `AlumnoController.java` dentro de `src/main/java`.
4. Ejecuta **Run As > Spring Boot App** o la clase principal como Java Application.
5. Usa una terminal externa o integrada para las peticiones.

### Desde VS Code

1. Abre `M2/proyecto` como carpeta.
2. Espera a que Java/Maven importen el proyecto.
3. Edita `AlumnoController.java` y revisa el panel **Problems**.
4. Ejecuta `MiProyectoApplication` desde **Run and Debug** o mediante `./mvnw spring-boot:run`.
5. Usa la terminal integrada para `curl`, tests y package.

### Desde consola y editor de texto

No necesitas un IDE para completar el punto. Edita el fichero con tu editor, y usa Maven Wrapper como comprobación objetiva:

```bash
./mvnw test
./mvnw package
./mvnw spring-boot:run
```

Si algo compila en el editor pero falla con Maven Wrapper, el estado reproducible del proyecto es el que demuestra Maven, no el indicador visual del IDE.

## Cierre del punto 2.1

Hemos formalizado el controlador y, al mismo tiempo, hemos comprobado una limitación: aunque los métodos HTTP son ahora más claros, `AlumnoController` todavía sabe cómo generar IDs, buscar y modificar la colección. En **2.2 - Servicio** esas decisiones dejarán de estar en el controlador y aparecerá una dependencia explícita que podremos inyectar y probar de forma aislada.
