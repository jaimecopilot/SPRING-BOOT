

---

# Práctica 1.4 - Diseño de APIs REST

## Objetivo práctico

Diseñar y construir una primera API REST del recurso `Alumno` aplicando deliberadamente convenciones de recursos, URLs, métodos y códigos HTTP. El punto no implementa todavía el CRUD completo: deja una API con GET colección, GET individual, POST con 201 y filtro opcional por curso. El siguiente punto completará la persistencia en memoria y el resto de operaciones.

## Estado de partida

Debes conservar todo lo cerrado en 1.1–1.3. En particular siguen funcionando:

```text
GET /hola
GET /adios
GET /api/v1/expedientes/ejemplo
POST /api/v1/expedientes/eco
```

Y el proyecto contiene `ExpedienteDTO`, `SolicitanteDTO` y `ExpedienteController`.

Para trabajar usa la carpeta `M1/proyecto`.

Desde la carpeta `M1/proyecto`, en Linux/macOS:

```bash
./mvnw spring-boot:run
```

En Windows:

```powershell
mvnw.cmd spring-boot:run
```

### Desde IntelliJ IDEA

Abre `MiProyectoApplication.java` y ejecuta `Run 'MiProyectoApplication'`. Cuando modifiques clases, vuelve a ejecutar si el cambio no se recarga automáticamente.

### Desde Eclipse

Importa el proyecto como Maven y ejecuta `MiProyectoApplication` con `Run As > Java Application`.

### Desde VS Code

Abre la carpeta que contiene `pom.xml`, asegúrate de usar JDK 17 y ejecuta la clase principal desde el soporte Java/Spring o desde el terminal integrado.

## Paso 1 - Identificar los recursos y sus operaciones

Antes de escribir código, diseña el contrato. Para el dominio de alumnos queremos reconocer estas identidades:

```text
/api/v1/alumnos
/api/v1/alumnos/{id}
/api/v1/alumnos/{id}/documentos
```

Y estas operaciones deseadas a medio plazo:

| Operación | Método | URL |
|---|---|---|
| listar alumnos | GET | `/api/v1/alumnos` |
| consultar alumno | GET | `/api/v1/alumnos/{id}` |
| crear alumno | POST | `/api/v1/alumnos` |
| actualizar alumno | PUT | `/api/v1/alumnos/{id}` |
| eliminar alumno | DELETE | `/api/v1/alumnos/{id}` |
| listar documentos | GET | `/api/v1/alumnos/{id}/documentos` |

No implementes todavía PUT, DELETE ni el subrecurso `documentos`. La tabla es **diseño del contrato**; en 1.4 materializaremos GET colección, POST, GET individual y filtro. El CRUD completo llegará en 1.5.

### Qué debes observar

La misma URL de colección puede admitir GET y POST porque la operación la expresa el método HTTP. El ID individual forma parte de la identidad del recurso y, por tanto, aparece en el path.

### Pregunta

¿Por qué el ID de un alumno va en `/api/v1/alumnos/{id}` y no en `?id=...`?

### Respuesta razonada

Porque el ID identifica un recurso concreto. Un query parameter modifica una consulta sobre un recurso o colección; no es la forma más clara de expresar la identidad principal. `/alumnos/1` significa directamente “el recurso alumno 1”.

## Paso 2 - Crear el DTO AlumnoDTO

El package `dto` ya existe desde 1.3. Crea:

```text
M1/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java
```

con este contenido:

```java
package es.mecd.demo.miproyecto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlumnoDTO {

    @JsonProperty("id")
    private String identificador;
    private String nombre;
    private String apellidos;
    private String dni;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    private String curso;
    private List<String> documentos;

    public AlumnoDTO() {
    }

    public AlumnoDTO(
            String identificador,
            String nombre,
            String apellidos,
            String dni,
            LocalDate fechaNacimiento,
            String curso) {
        this.identificador = identificador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<String> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<String> documentos) {
        this.documentos = documentos;
    }
}
```

### Por qué reutilizamos las anotaciones de 1.3

`@JsonProperty("id")` desacopla el nombre Java del contrato JSON. `@JsonFormat` fija la fecha ISO. `@JsonInclude(NON_NULL)` evita enviar `documentos` cuando no tiene valor. No estamos aprendiendo anotaciones nuevas aquí: estamos reutilizando conscientemente el contrato JSON de 1.3 para diseñar otro recurso.

Comprueba compilación:

```bash
./mvnw test
```

### Pregunta

¿Qué ventaja aporta que `AlumnoDTO` y `ExpedienteDTO` sigan las mismas convenciones JSON?

### Respuesta razonada

El cliente puede aprender una regla y aplicarla a toda la API. Si un identificador se expone como `id` y las fechas usan `yyyy-MM-dd` en un recurso, mantener la misma convención en los demás reduce errores, documentación y lógica especial en consumidores.

## Paso 3 - Crear AlumnoController con GET colección y POST básico

Crea:

```text
M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java
```

Empieza con este estado:

```java
package es.mecd.demo.miproyecto.controller;

import es.mecd.demo.miproyecto.dto.AlumnoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {

    private final List<AlumnoDTO> alumnos = List.of(
            new AlumnoDTO(
                    "1", "Ana", "García López", "DNI-DEMO-01",
                    LocalDate.of(2010, 5, 12), "5º Primaria"),
            new AlumnoDTO(
                    "2", "Luis", "Pérez Ruiz", "DNI-DEMO-02",
                    LocalDate.of(2009, 9, 3), "6º Primaria")
    );

    @GetMapping
    public List<AlumnoDTO> listar() {
        return alumnos;
    }

    @PostMapping
    public AlumnoDTO crear(@RequestBody AlumnoDTO dto) {
        return dto;
    }
}
```

Los valores `DNI-DEMO-01` y `DNI-DEMO-02` son datos explícitamente ficticios para el curso; en este punto no existe todavía validación de DNI.

### Qué significa este estado

`@RequestMapping("/api/v1/alumnos")` establece el recurso y la versión. `@GetMapping` sin ruta adicional corresponde a la colección. `@PostMapping` usa la misma URL con otra semántica HTTP.

El POST **sólo devuelve el DTO recibido**. No lo guarda. La lista está creada con `List.of(...)` y es inmutable. Esa limitación es deliberada en 1.4: primero estudiaremos el contrato REST; 1.5 añadirá persistencia en memoria.

### Pregunta

¿Por qué no deberíamos añadir ya `alumnos.add(dto)` a este paso?

### Respuesta razonada

Porque estaríamos adelantando una decisión que la secuencia pedagógica reserva para el CRUD de 1.5. En 1.4 queremos observar el contrato HTTP de creación independientemente de la persistencia. Además, `List.of(...)` es inmutable, por lo que añadir directamente produciría `UnsupportedOperationException`.

## Paso 4 - Arrancar y probar el GET de colección

Arranca:

```bash
./mvnw spring-boot:run
```

En otra terminal:

```bash
curl -i http://localhost:8080/api/v1/alumnos
```

Debes obtener `200 OK`, `Content-Type: application/json` y un array con dos alumnos.

Comprueba varias propiedades:

- el identificador externo se llama `id`;
- `fechaNacimiento` tiene formato `yyyy-MM-dd`;
- `documentos` no aparece cuando es `null`;
- el resultado es una colección JSON, no un texto construido a mano.

### Pregunta

¿Por qué una colección se representa como array aunque sólo tuviera un alumno?

### Respuesta razonada

Porque el contrato del endpoint es “colección de alumnos”. La cardinalidad actual no debe cambiar el tipo estructural de la respuesta. Un cliente puede tratar siempre la respuesta como lista, tenga cero, uno o muchos elementos.

## Paso 5 - Probar el POST de creación y observar el 200 inicial

Sin modificar todavía el controlador, ejecuta:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"DNI-DEMO-03","fechaNacimiento":"2011-03-20","curso":"4º Primaria"}'
```

El cuerpo debe volver serializado, pero observa el código: con un DTO devuelto directamente Spring responde normalmente `200 OK`.

Vuelve a ejecutar:

```bash
curl http://localhost:8080/api/v1/alumnos
```

El nuevo alumno **no aparece**. El POST aún no persiste.

### Pregunta

¿Que el POST devuelva el JSON enviado significa que el recurso se ha guardado?

### Respuesta razonada

No. Una respuesta puede contener el mismo objeto sin que haya ninguna persistencia. Hay que distinguir serialización, código de estado y efecto sobre el estado del servidor. En este paso sólo estamos haciendo eco del DTO.

## Paso 6 - Analizar por qué 200 no describe una creación

`200 OK` indica que la petición se ha procesado correctamente, pero HTTP dispone de un código más específico para una creación: `201 Created`.

Compara conceptualmente:

```text
POST /api/v1/alumnos -> 200 OK       # éxito genérico
POST /api/v1/alumnos -> 201 Created  # creación declarada explícitamente
```

El cuerpo puede ser idéntico; lo que cambia es el contrato HTTP.

No confundas esta mejora con persistencia. En el siguiente paso cambiaremos el **status**, no la lista.

### Pregunta

¿Por qué merece la pena distinguir 200 y 201 si ambos están en la familia 2xx?

### Respuesta razonada

Porque un cliente no debería deducir la semántica leyendo texto o conociendo implementación interna. `201` comunica explícitamente que la operación representa una creación. Los códigos de estado son parte de la interfaz pública.

## Paso 7 - Corregir el POST para devolver 201 Created

Añade los imports:

```java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
```

Y sustituye el método `crear` por:

```java
@PostMapping
public ResponseEntity<AlumnoDTO> crear(@RequestBody AlumnoDTO dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
}
```

Reinicia y repite el POST:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"DNI-DEMO-03","fechaNacimiento":"2011-03-20","curso":"4º Primaria"}'
```

Ahora debes ver `201`.

Vuelve a listar. El alumno continúa sin guardarse: el objetivo de este paso era corregir el contrato HTTP sin introducir todavía persistencia.

### Pregunta

¿Qué aporta `ResponseEntity<AlumnoDTO>` frente a devolver directamente `AlumnoDTO`?

### Respuesta razonada

Permite controlar la respuesta HTTP completa, especialmente el status y, cuando lo necesitemos, cabeceras. El cuerpo sigue siendo un `AlumnoDTO`, pero ya no dependemos sólo del status por defecto de Spring MVC.

## Paso 8 - Añadir el endpoint GET individual

Añade:

```java
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
```

Y el método:

```java
@GetMapping("/{id}")
public ResponseEntity<AlumnoDTO> consultar(@PathVariable String id) {
    Optional<AlumnoDTO> encontrado = alumnos.stream()
            .filter(a -> a.getIdentificador().equals(id))
            .findFirst();

    return encontrado
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

Prueba un ID existente:

```bash
curl -i http://localhost:8080/api/v1/alumnos/1
```

Debe devolver 200 y el alumno.

### Qué hace `@PathVariable`

En la URL `/api/v1/alumnos/1`, el fragmento `1` forma parte del path. `@PathVariable String id` lo entrega al método como parámetro Java. El método busca el alumno y utiliza `ResponseEntity` porque existen dos resultados HTTP válidos: 200 o 404.

### Pregunta

¿Por qué `consultar` necesita controlar el status mientras `listar` puede devolver directamente una lista?

### Respuesta razonada

Una colección válida existe aunque esté vacía y puede responder 200 con `[]`. Un recurso individual concreto puede no existir; esa ausencia debe expresarse como 404. `ResponseEntity` permite representar ambos casos de forma explícita.

## Paso 9 - Probar un ID inexistente

Ejecuta:

```bash
curl -i http://localhost:8080/api/v1/alumnos/999
```

Debes observar `404 Not Found`.

No fijes en tus tests todos los campos del cuerpo de error por defecto de Spring Boot como si fueran nuestro contrato propio. Lo estable que estamos enseñando aquí es el status 404 del recurso inexistente.

Compara:

```bash
curl -i http://localhost:8080/api/v1/alumnos/1
curl -i http://localhost:8080/api/v1/alumnos/999
```

Misma familia de URL, distinto resultado según la existencia del recurso.

### Pregunta

¿Por qué no devolvemos 200 con cuerpo `null` para el alumno 999?

### Respuesta razonada

Porque la URL identifica un recurso individual que no existe. Un 200 afirma éxito de la consulta del recurso; 404 comunica explícitamente la ausencia y permite al cliente tomar decisiones sin interpretar un cuerpo ambiguo.

## Paso 10 - Auditar el diseño actual de la API

Revisa lo construido:

| Aspecto | Estado | Evidencia |
|---|---|---|
| URL en plural | correcto | `/api/v1/alumnos` |
| minúsculas | correcto | `alumnos` |
| versionado | correcto | `/api/v1/` |
| GET colección | correcto | `GET /api/v1/alumnos` |
| POST creación | correcto en contrato | `POST /api/v1/alumnos` -> 201 |
| GET individual | correcto | `GET /api/v1/alumnos/{id}` -> 200/404 |
| ID en path | correcto | `/alumnos/1` |
| DTO con Jackson | correcto | `id`, fecha ISO, no nulos omitidos |
| persistencia de POST | pendiente | se completa en 1.5 |
| PUT/PATCH/DELETE | pendiente | se completa en 1.5 |

La API es deliberadamente incompleta como CRUD, pero las operaciones existentes ya deben respetar sus convenciones.

### Pregunta

¿Una API tiene que implementar todos los métodos HTTP para poder estar bien diseñada?

### Respuesta razonada

No. Debe implementar las operaciones que necesita el dominio y hacerlo con semántica coherente. Una API parcial puede estar bien diseñada; lo incorrecto sería fingir soporte para operaciones que no existen o usar rutas y códigos inconsistentes.

## Paso 11 - Reconocer errores comunes de diseño

Usa esta tabla como auditoría rápida:

| Problema | Diseño problemático | Alternativa coherente |
|---|---|---|
| verbo en URL | `/api/getAlumnos` | `GET /api/v1/alumnos` |
| singular para colección | `/api/v1/alumno` | `/api/v1/alumnos` |
| mayúsculas | `/api/v1/Alumnos` | `/api/v1/alumnos` |
| guion bajo | `/api/v1/tipos_alumno` | `/api/v1/tipos-alumno` |
| ID como filtro | `/api/v1/alumnos?id=1` | `/api/v1/alumnos/1` |
| sin versión | `/api/alumnos` | `/api/v1/alumnos` |
| POST con éxito genérico | `ResponseEntity.ok(dto)` | `status(CREATED).body(dto)` |
| no encontrado como éxito | `ResponseEntity.ok(null)` | `notFound().build()` |
| extensión en ruta | `/api/v1/alumnos.json` | `/api/v1/alumnos` |

### Diagnóstico operativo

Si recibes 404, confirma ruta e ID. Si recibes 405, revisa el método HTTP. Si recibes 415, revisa `Content-Type`. Si recibes 400 al enviar JSON, valida sintaxis, tipos y formato de fecha antes de cambiar el controlador.

### Pregunta

¿Qué error de diseño de la tabla puede seguir “funcionando” técnicamente y, aun así, merece corregirse?

### Respuesta razonada

Prácticamente todos. Por ejemplo `/api/getAlumnos` puede mapearse y responder 200, pero mezcla la acción con la identidad del recurso. La calidad del diseño de una API no se reduce a “el servidor responde”.

## Paso 12 - Reto resuelto: filtrar por curso con query parameter

El recurso sigue siendo la colección `/api/v1/alumnos`, pero queremos permitir una vista filtrada. Añade:

```java
import org.springframework.web.bind.annotation.RequestParam;
```

Y sustituye `listar()` por:

```java
@GetMapping
public List<AlumnoDTO> listar(@RequestParam(required = false) String curso) {
    if (curso == null || curso.isBlank()) {
        return alumnos;
    }

    return alumnos.stream()
            .filter(a -> a.getCurso().equalsIgnoreCase(curso))
            .toList();
}
```

Prueba sin filtro:

```bash
curl http://localhost:8080/api/v1/alumnos
```

Deben aparecer los dos alumnos iniciales.

Para valores con espacios o el carácter `º`, deja que curl codifique el parámetro:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "curso=5º Primaria"
```

Debe aparecer sólo Ana.

Prueba una categoría sin coincidencias:

```bash
curl -G http://localhost:8080/api/v1/alumnos \
  --data-urlencode "curso=3º Primaria"
```

Debe devolver:

```json
[]
```

con 200.

### Pregunta

¿Por qué `curso` va en query parameter y no como `/api/v1/alumnos/curso/5º Primaria`?

### Respuesta razonada

Porque `curso` es un criterio opcional aplicado a la colección, no la identidad de un alumno. La query expresa naturalmente “la colección de alumnos filtrada por curso” y permite combinar más criterios en el futuro sin crear una jerarquía artificial de rutas.

## Resultado esperado al cerrar 1.4

El snapshot debe conservar íntegro 1.3 y añadir exactamente:

```text
src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java
src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java
```

`AlumnoController` debe exponer:

```text
GET  /api/v1/alumnos          -> 200, lista; filtro opcional curso
GET  /api/v1/alumnos/{id}     -> 200 o 404
POST /api/v1/alumnos          -> 201, devuelve el DTO recibido
```

El POST todavía **no añade** el DTO a la lista. La lista continúa siendo inmutable mediante `List.of(...)`. Esa limitación se resolverá deliberadamente en 1.5.

Verificación final:

```bash
./mvnw test
./mvnw -DskipTests package
curl http://localhost:8080/api/v1/alumnos
curl -i http://localhost:8080/api/v1/alumnos/1
curl -i http://localhost:8080/api/v1/alumnos/999
```

Y confirma que los endpoints anteriores siguen funcionando:

```bash
curl http://localhost:8080/hola
curl http://localhost:8080/adios
curl http://localhost:8080/api/v1/expedientes/ejemplo
```
