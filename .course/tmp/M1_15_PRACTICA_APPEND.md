

---

# Práctica 1.5 - Primer CRUD con DTOs

## Objetivo práctico

Completar el CRUD de `Alumno` iniciado en 1.4. El controlador dejará de ser sólo una demostración de contrato REST: el POST guardará en memoria, PUT reemplazará un alumno, PATCH modificará campos concretos y DELETE eliminará. Cerraremos el módulo combinando filtrado y ordenación opcionales.

El almacenamiento sigue siendo **en memoria**. Al reiniciar la aplicación se recuperan únicamente los dos alumnos iniciales. Esta limitación es deliberada: persistencia real, servicios y repositorios llegan en módulos posteriores.

## Estado de partida

Conserva íntegros los puntos 1.1–1.4. En `M1/proyecto` deben seguir funcionando:

```text
GET  /hola
GET  /adios
GET  /api/v1/expedientes/ejemplo
POST /api/v1/expedientes/eco
GET  /api/v1/alumnos
GET  /api/v1/alumnos/{id}
POST /api/v1/alumnos
```

En 1.4 el POST de alumnos devuelve 201, pero **no persiste**. Ese será el primer cambio de 1.5.

Desde la carpeta `M1/proyecto`, en Linux/macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
mvnw.cmd spring-boot:run
```

### Desde IntelliJ IDEA

Ejecuta `MiProyectoApplication` con `Run`. Tras modificar el controlador, reinicia si la recarga automática no aplica el cambio.

### Desde Eclipse

Ejecuta `MiProyectoApplication` con `Run As > Java Application` y usa una terminal para las pruebas HTTP.

### Desde VS Code

Ejecuta la clase principal desde el soporte Java/Spring o usa el terminal integrado con Maven Wrapper.

## Paso 1 - Repasar el estado actual

Antes de modificar nada, demuestra qué existe ya:

```bash
curl -i http://localhost:8080/api/v1/alumnos
curl -i http://localhost:8080/api/v1/alumnos/1
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"DNI-DEMO-03","fechaNacimiento":"2011-03-20","curso":"4º Primaria"}'
```

El GET de colección debe mostrar dos alumnos. El GET individual debe devolver 200 para `/1`. El POST debe devolver 201 y el DTO recibido.

Ahora vuelve a listar:

```bash
curl http://localhost:8080/api/v1/alumnos
```

Siguen existiendo sólo dos alumnos. Ésta es la limitación que vamos a resolver.

### Pregunta

¿Qué falta para que el POST sea una creación funcional y no sólo una respuesta 201 con el cuerpo recibido?

### Respuesta razonada

Hace falta modificar un almacenamiento mutable. El controlador debe asignar identidad al nuevo recurso, añadirlo a la colección y permitir recuperarlo con GET posteriormente. El status por sí solo no persiste nada.

## Paso 2 - Convertir la lista en mutable

Abre:

```text
M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java
```

Añade:

```java
import java.util.ArrayList;
```

Y cambia la inicialización:

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

`List.of(...)` crea una lista inmutable. Envolverla en `new ArrayList<>(...)` crea una colección mutable con los mismos datos iniciales.

Comprueba que nada anterior se ha roto:

```bash
./mvnw test
```

### Pregunta

¿Por qué no basta con mantener `List.of(...)` y llamar después a `add`?

### Respuesta razonada

Porque las listas creadas con `List.of` no admiten cambios estructurales. `add`, `remove` o operaciones equivalentes lanzarían `UnsupportedOperationException`. El CRUD necesita una colección mutable mientras usemos este almacenamiento pedagógico.

## Paso 3 - Modificar el POST para que guarde el alumno

Sustituye `crear` por:

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

La fuente utiliza una generación de ID deliberadamente simple para obtener `3` tras los dos alumnos iniciales. Aquí usamos `máximo + 1` en vez de `size()+1`: sigue siendo una simplificación en memoria, pero evita una colisión inmediata si antes se ha borrado un elemento.

**No es un generador de IDs de producción.** Dos peticiones concurrentes podrían calcular el mismo valor; una base de datos o un repositorio real resolverán esa responsabilidad más adelante.

Reinicia y crea:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez","dni":"DNI-DEMO-03","fechaNacimiento":"2010-07-15","curso":"5º Primaria"}'
```

Debe devolver `201 Created` y:

```json
{"id":"3", ...}
```

Comprueba persistencia en memoria:

```bash
curl http://localhost:8080/api/v1/alumnos/3
curl http://localhost:8080/api/v1/alumnos
```

El alumno 3 debe existir hasta que reinicies o lo borres.

### Pregunta

¿Por qué ignoramos un `id` que pudiera enviar el cliente en el POST y asignamos uno en el servidor?

### Respuesta razonada

Porque en este diseño la identidad del nuevo recurso la controla el servidor. Permitir que cualquier cliente elija libremente el ID facilita colisiones e inconsistencias. La estrategia concreta de generación es simplificada, pero la responsabilidad está situada en el lado correcto.

## Paso 4 - Añadir el endpoint PUT

Añade el import:

```java
import org.springframework.web.bind.annotation.PutMapping;
```

Y el método:

```java
@PutMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizar(
        @PathVariable String id,
        @RequestBody AlumnoDTO dto) {

    for (int i = 0; i < alumnos.size(); i++) {
        if (alumnos.get(i).getIdentificador().equals(id)) {
            dto.setIdentificador(id);
            alumnos.set(i, dto);
            return ResponseEntity.ok(dto);
        }
    }

    return ResponseEntity.notFound().build();
}
```

Prueba un alumno existente:

```bash
curl -i -X PUT http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana","apellidos":"García López","dni":"DNI-DEMO-01","fechaNacimiento":"2010-05-12","curso":"6º Primaria"}'
```

Debe devolver 200. Comprueba que el ID sigue siendo `1` y que el recurso completo ha sido reemplazado.

Prueba un ID inexistente:

```bash
curl -i -X PUT http://localhost:8080/api/v1/alumnos/999 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Nadie","apellidos":"Ejemplo","dni":"DNI-DEMO-X","fechaNacimiento":"2010-01-01","curso":"1º Primaria"}'
```

Debe devolver 404.

### Pregunta

¿Por qué `dto.setIdentificador(id)` usa el ID de la URL aunque el JSON pudiera contener otro?

### Respuesta razonada

Porque la URL identifica el recurso que estamos reemplazando. Si permitiéramos que el cuerpo cambiara silenciosamente su identidad, una petición a `/alumnos/1` podría terminar modificando la identidad a otro valor. El path es la autoridad para la operación.

## Paso 5 - Añadir el endpoint PATCH

Añade:

```java
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.Map;
```

Y el método:

```java
@PatchMapping("/{id}")
public ResponseEntity<AlumnoDTO> actualizarParcial(
        @PathVariable String id,
        @RequestBody Map<String, Object> cambios) {

    for (AlumnoDTO alumno : alumnos) {
        if (alumno.getIdentificador().equals(id)) {
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
            return ResponseEntity.ok(alumno);
        }
    }

    return ResponseEntity.notFound().build();
}
```

Este `Map<String, Object>` es una simplificación pedagógica. No da el mismo tipado ni validación que un DTO específico de PATCH. Lo usamos sólo para ver claramente qué significa actualizar campos presentes y conservar los ausentes.

Prueba:

```bash
curl -i -X PATCH http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{"curso":"6º Primaria B"}'
```

Sólo debe cambiar `curso`.

Prueba otro campo:

```bash
curl -i -X PATCH http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana María"}'
```

### Pregunta

¿Qué tendrías que enviar con PUT para cambiar sólo el curso sin perder el resto del estado?

### Respuesta razonada

Tendrías que enviar la representación completa del recurso con todos sus campos y el nuevo curso. PATCH permite expresar directamente que sólo cambia un subconjunto. Nuestro `Map` hace visible esa diferencia, aunque más adelante usaremos diseños más tipados.

## Paso 6 - Añadir el endpoint DELETE

Añade:

```java
import org.springframework.web.bind.annotation.DeleteMapping;
```

Y:

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable String id) {
    boolean eliminado = alumnos.removeIf(
            a -> a.getIdentificador().equals(id));

    if (eliminado) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
}
```

Prueba:

```bash
curl -i -X DELETE http://localhost:8080/api/v1/alumnos/2
```

Debe devolver `204 No Content`.

Repite exactamente la misma petición:

```bash
curl -i -X DELETE http://localhost:8080/api/v1/alumnos/2
```

Ahora devuelve 404 porque el recurso ya no existe.

### Pregunta

¿Sigue siendo DELETE idempotente si la primera respuesta es 204 y la segunda 404?

### Respuesta razonada

Sí. Tras una o varias ejecuciones el estado final es el mismo: el alumno 2 no existe. Idempotencia se refiere al efecto sobre el estado, no a que todas las respuestas deban tener el mismo código.

## Paso 7 - Probar el CRUD completo con curl

Reinicia primero la aplicación para recuperar el estado inicial de dos alumnos. Ejecuta esta secuencia en orden.

### 1. Listar

```bash
curl http://localhost:8080/api/v1/alumnos
```

### 2. Consultar

```bash
curl http://localhost:8080/api/v1/alumnos/1
```

### 3. Crear

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez","dni":"DNI-DEMO-03","fechaNacimiento":"2010-07-15","curso":"5º Primaria"}'
```

Comprueba `201` e `id: "3"`.

### 4. Consultar el creado

```bash
curl http://localhost:8080/api/v1/alumnos/3
```

### 5. Reemplazar con PUT

```bash
curl -i -X PUT http://localhost:8080/api/v1/alumnos/3 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez","dni":"DNI-DEMO-03","fechaNacimiento":"2010-07-15","curso":"6º Primaria"}'
```

### 6. Modificar parcialmente con PATCH

```bash
curl -i -X PATCH http://localhost:8080/api/v1/alumnos/3 \
  -H "Content-Type: application/json" \
  -d '{"curso":"6º Primaria B"}'
```

### 7. Eliminar

```bash
curl -i -X DELETE http://localhost:8080/api/v1/alumnos/3
```

Debe devolver 204.

### 8. Verificar ausencia

```bash
curl -i http://localhost:8080/api/v1/alumnos/3
```

Debe devolver 404.

### Pregunta

¿Qué prueba del ciclo demuestra mejor que el POST ha pasado de “eco” a creación real?

### Respuesta razonada

Poder consultar después `/api/v1/alumnos/3`. El 201 es parte del contrato, pero el GET posterior demuestra que el estado del servidor cambió y que el recurso puede recuperarse por su identidad.

## Paso 8 - Observar los códigos de estado

Recopila las evidencias del CRUD:

| Operación | Código esperado |
|---|---:|
| GET colección | 200 |
| GET individual existente | 200 |
| GET individual inexistente | 404 |
| POST crear | 201 |
| PUT existente | 200 |
| PUT inexistente | 404 |
| PATCH existente | 200 |
| PATCH inexistente | 404 |
| DELETE existente | 204 |
| DELETE inexistente | 404 |

Añade también los errores de protocolo que ya conocemos:

| Situación | Código esperado |
|---|---:|
| JSON mal formado | 400 |
| método no soportado | 405 |
| cuerpo con media type no soportado | 415 |

### Pregunta

¿Por qué conviene verificar los códigos de estado por separado del cuerpo JSON?

### Respuesta razonada

Porque expresan dimensiones distintas del contrato. El status resume el resultado de la operación para cualquier cliente HTTP; el cuerpo representa datos o detalles adicionales. Un cuerpo correcto con un status incorrecto sigue siendo una API incorrecta.

## Paso 9 - Probar un error de formato

Provoca un JSON inválido:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","apellidos":"Sánchez",}'
```

Debe devolver `400 Bad Request`.

No acoples tu comprobación al texto exacto del parser o a todos los campos del JSON de error de Spring Boot. El contrato estable que queremos demostrar aquí es el status 400.

También puedes comprobar una fecha incompatible:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Pedro","fechaNacimiento":"15/07/2010"}'
```

Debe fallar al deserializar `LocalDate` según nuestro contrato `yyyy-MM-dd`.

### Pregunta

¿Por qué este 400 puede aparecer sin que escribamos un `if` específico en `crear()`?

### Respuesta razonada

Porque Spring MVC y Jackson deben convertir primero el cuerpo HTTP en `AlumnoDTO`. Si el JSON no se puede parsear o convertir a los tipos declarados, la petición falla antes de que el método reciba un DTO válido.

## Paso 10 - Probar un método no soportado

La URL individual admite GET, PUT, PATCH y DELETE, pero no POST. Ejecuta:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos/1 \
  -H "Content-Type: application/json" \
  -d '{}'
```

Debe devolver `405 Method Not Allowed`.

Compara con:

```bash
curl -i http://localhost:8080/api/v1/alumnos/999
```

Éste devuelve 404: el método GET sí existe para esa forma de URL, pero el alumno 999 no.

### Pregunta

¿Qué información diferente aporta 405 respecto a 404?

### Respuesta razonada

405 indica que la ruta se reconoce para otros métodos pero el método solicitado no está soportado. 404, en nuestro GET individual, expresa que el recurso identificado no existe. Son fallos distintos y el cliente puede reaccionar de manera distinta.

## Paso 11 - Errores comunes del ejercicio

Usa esta tabla antes de cambiar código al azar:

| Síntoma | Causa probable | Comprobación / solución |
|---|---|---|
| `UnsupportedOperationException` al crear/borrar | sigue `List.of(...)` | usar `new ArrayList<>(List.of(...))` |
| POST devuelve 201 pero el GET no muestra el nuevo | falta `alumnos.add(dto)` | comprobar el método `crear` |
| IDs se repiten | generación demasiado simple | revisar estrategia; recordar que sigue siendo didáctica |
| PUT cambia el ID | se confía en el cuerpo | imponer `dto.setIdentificador(id)` |
| PUT/PATCH de ID inexistente | búsqueda no contempla ausencia | devolver 404 |
| PATCH no cambia un campo | clave no contemplada en el `Map` | revisar nombre y conversión |
| DELETE no elimina | ID no coincide | inspeccionar `removeIf` |
| POST a `/{id}` devuelve 405 | método equivocado | usar PUT/PATCH según intención |
| POST/PUT/PATCH devuelve 415 | `Content-Type` ausente/incorrecto | enviar `application/json` |
| POST/PUT devuelve 400 | JSON o tipos incompatibles | validar sintaxis y fechas |

### Límite importante

Un `Map<String, Object>` permite enseñar PATCH con poco código, pero no es un contrato de entrada robusto. Tampoco `ArrayList` dentro del controlador es una arquitectura final. Esas limitaciones son parte del aprendizaje: en M2 empezaremos a separar responsabilidades.

### Pregunta

¿Por qué es útil conocer estas limitaciones antes de introducir service y repository?

### Respuesta razonada

Porque podremos distinguir un refactor arquitectónico de un cambio de comportamiento. Sabremos qué contrato debe seguir funcionando mientras movemos responsabilidades a otras capas.

## Paso 12 - Reto resuelto: filtrar por curso y ordenar

El filtro `curso` de 1.4 permanece. Añadiremos un segundo query parameter realmente opcional: `sort`.

Sustituye `listar()` por:

```java
@GetMapping
public List<AlumnoDTO> listar(
        @RequestParam(required = false) String curso,
        @RequestParam(required = false) String sort) {

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

    return stream.toList();
}
```

La fuente histórica muestra `defaultValue="nombre"` y a la vez describe `sort` como opcional. Aquí hacemos ambas cosas coherentes: si no hay `sort`, no ordenamos y conservamos el orden actual de la colección.

Prueba sin parámetros:

```bash
curl http://localhost:8080/api/v1/alumnos
```

Filtra usando codificación segura:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "curso=5º Primaria"
```

Ordena por nombre:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "sort=nombre"
```

Ordena por apellidos:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "sort=apellidos"
```

Combina ambos:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "curso=5º Primaria" \
  --data-urlencode "sort=nombre"
```

Un valor de `sort` no reconocido deja el orden actual sin convertirlo en error en este ejercicio introductorio.

### Pregunta

¿Qué ventaja tiene mantener `curso` y `sort` como query parameters en lugar de crear rutas nuevas para cada combinación?

### Respuesta razonada

El recurso sigue siendo la misma colección. Los parámetros sólo modifican la vista solicitada y pueden combinarse sin multiplicar endpoints como `/alumnos-ordenados-por-nombre` o `/alumnos-5-primaria`.

## Resultado esperado al cerrar 1.5 y M1

El proyecto debe conservar todo lo construido anteriormente y `AlumnoController` debe ofrecer estas seis combinaciones principales método/ruta:

```text
GET    /api/v1/alumnos          -> 200; filtro y ordenación opcionales
GET    /api/v1/alumnos/{id}     -> 200 / 404
POST   /api/v1/alumnos          -> 201; asigna ID y guarda en memoria
PUT    /api/v1/alumnos/{id}     -> 200 / 404; reemplazo completo
PATCH  /api/v1/alumnos/{id}     -> 200 / 404; actualización parcial
DELETE /api/v1/alumnos/{id}     -> 204 / 404
```

Verificación reproducible:

```bash
./mvnw test
./mvnw -DskipTests package
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar
```

Y después recorre el ciclo CRUD del paso 7.

Comprueba también que no has roto el contenido anterior:

```bash
curl http://localhost:8080/hola
curl http://localhost:8080/adios
curl http://localhost:8080/api/v1/expedientes/ejemplo
```

Al cerrar M1 debes ser capaz de explicar la cadena completa:

```text
Spring Boot -> HTTP -> JSON/Jackson -> diseño REST -> CRUD
```

El siguiente módulo podrá refactorizar este comportamiento hacia capas de controlador, servicio y repositorio sin tener que redescubrir qué debe hacer la API.
