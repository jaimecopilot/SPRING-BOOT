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

Mueve al servicio la colección inicial que estaba en el controlador. Conservamos los mismos datos del estado de 2.1 2.1 para que el refactor sea observable como equivalente:

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

