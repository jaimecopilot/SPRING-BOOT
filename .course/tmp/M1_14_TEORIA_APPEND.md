

---

# Punto 1.4 - Diseño de APIs REST

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar REST como estilo arquitectónico y no como protocolo, librería o framework.
2. Modelar un dominio HTTP en términos de recursos, colecciones y representaciones.
3. Diseñar URLs centradas en sustantivos, consistentes, versionadas y fáciles de evolucionar.
4. Elegir el método HTTP según la semántica de la operación y razonar sobre seguridad e idempotencia.
5. Seleccionar códigos de estado que describan el resultado real de una petición.
6. Diferenciar identificadores de recurso en path de filtros, ordenación y paginación en query parameters.
7. Entender por qué una colección vacía no es un error y cómo deben expresarse los filtros opcionales.
8. Comprender el papel de la documentación y del versionado en la evolución de una API.
9. Aplicar estos principios a un recurso real `Alumno` sin adelantar todavía el CRUD completo del punto 1.5.

## Bloque 1 - Qué es REST

### 1.1 REST es un estilo arquitectónico

REST significa *Representational State Transfer*. Roy Fielding lo describió en su tesis doctoral de 2000 como parte de un trabajo más amplio sobre arquitecturas de red. Para este curso importa una distinción fundamental: REST **no es HTTP**, no es una especificación de código, no es una dependencia Maven y no es una anotación de Spring.

REST es un conjunto de restricciones y principios para diseñar sistemas distribuidos. HTTP es el protocolo sobre el que normalmente materializamos esos principios cuando construimos una API web.

En nuestro proyecto esto significa que Spring MVC puede recibir una petición aunque nuestra URL esté mal diseñada. Que el código funcione técnicamente no garantiza que la API tenga un diseño REST coherente.

### 1.2 Recursos y representaciones

La unidad conceptual no es la operación, sino el **recurso**. Un alumno, un expediente o una colección de alumnos son recursos con identidad. La URL identifica el recurso y el método HTTP expresa qué queremos hacer con él.

```text
/api/v1/alumnos       -> colección de alumnos
/api/v1/alumnos/1     -> alumno identificado por 1
```

El servidor no envía el objeto Java en sí. Envía una **representación** del estado del recurso. En este curso esa representación será normalmente JSON.

Esta separación permite que el modelo interno evolucione sin obligar a que el cliente conozca clases Java. El contrato público es HTTP + representación, no la estructura de memoria del servidor.

### 1.3 Restricciones que dan sentido a REST

La descripción clásica de REST incluye varias restricciones arquitectónicas. Para empezar debemos reconocer al menos estas ideas:

- **cliente-servidor:** responsabilidades separadas;
- **sin estado de sesión en la petición (*stateless*):** cada petición contiene la información necesaria para procesarse;
- **cacheable:** las respuestas pueden declarar cuándo son reutilizables;
- **interfaz uniforme:** recursos, métodos y representaciones siguen reglas consistentes;
- **sistema en capas:** el cliente no necesita conocer toda la topología interna;
- **código bajo demanda:** restricción opcional en la definición clásica.

No vamos a implementar todas estas capacidades ahora. Sí utilizaremos su consecuencia práctica: una API debe ser predecible. El cliente no debería tener que memorizar una regla distinta para cada recurso.

### Pregunta

¿Por qué `/api/getAlumnos` es una URL menos REST que `/api/v1/alumnos` aunque ambas puedan devolver exactamente el mismo JSON?

### Respuesta razonada

Porque la primera modela una acción dentro de la URL y duplica información que ya aporta el método HTTP. La segunda identifica el recurso `alumnos`; después `GET` expresa que queremos consultarlo. Separar identidad del recurso y semántica de la operación hace que el contrato sea más uniforme y extensible.

## Bloque 2 - Diseño de URLs

### 2.1 La URL identifica; el método actúa

Una URL REST debe permitir reconocer qué recurso representa. Conviene utilizar sustantivos, normalmente en plural para colecciones:

```text
GET  /api/v1/alumnos
GET  /api/v1/alumnos/1
POST /api/v1/alumnos
```

Evita convertir la ruta en una llamada a método remota:

```text
/api/getAlumnos
/api/crearAlumno
/api/eliminarAlumno?id=1
```

Estas rutas pueden funcionar, pero trasladan al URI una semántica que HTTP ya proporciona mediante `GET`, `POST` o `DELETE`.

### 2.2 Colecciones, elementos y subrecursos

El patrón colección/elemento es una de las convenciones más útiles:

```text
/api/v1/alumnos            -> colección
/api/v1/alumnos/{id}       -> elemento
/api/v1/alumnos/{id}/documentos -> subrecurso relacionado
```

El identificador va en el path cuando forma parte de la identidad del recurso. En cambio, una condición como `curso=5º Primaria` no identifica un alumno concreto: modifica la consulta sobre la colección y encaja mejor como query parameter.

### 2.3 Convenciones de nomenclatura

Un contrato consistente suele utilizar:

- minúsculas;
- sustantivos en plural para colecciones;
- guiones medios si un nombre necesita varias palabras;
- ausencia de extensiones como `.json` en la ruta;
- jerarquías sólo cuando expresan una relación real.

Por ejemplo:

```text
/api/v1/tipos-alumno
```

es preferible a:

```text
/api/v1/Tipos_Alumno.json
```

### 2.4 Versionado

Nuestro curso utiliza el prefijo `/api/v1/`. No es la única estrategia posible, pero hace visible qué contrato consume el cliente:

```text
/api/v1/alumnos
```

El objetivo del versionado no es crear una versión por cada pequeño cambio. Sirve para gestionar cambios incompatibles de forma controlada. Añadir un campo opcional suele ser compatible; cambiar el significado de un campo existente o eliminarlo puede no serlo.

### Pregunta

¿Por qué `GET /api/v1/alumnos?id=1` no expresa tan bien la identidad como `GET /api/v1/alumnos/1`?

### Respuesta razonada

Porque el primer formato trata `id=1` como criterio de consulta sobre una colección. El segundo identifica directamente el recurso alumno 1. Los query parameters son adecuados para modificar una consulta —por ejemplo filtros—, mientras que el path representa la identidad o jerarquía del recurso.

## Bloque 3 - Métodos HTTP y códigos de estado

### 3.1 La semántica de los métodos

HTTP ya define verbos con significado. Utilizarlos correctamente evita inventar contratos propios:

| Método | Intención habitual | ¿Seguro? | ¿Idempotente? |
|---|---|---:|---:|
| `GET` | consultar | sí | sí |
| `POST` | crear/procesar | no | no, en general |
| `PUT` | reemplazar completamente | no | sí |
| `PATCH` | modificar parcialmente | no | no, en general |
| `DELETE` | eliminar | no | sí |

**Seguro** significa que la intención de la operación no es modificar el estado del servidor. **Idempotente** significa que repetir la misma operación produce el mismo estado final que ejecutarla una vez.

La idempotencia importa especialmente ante reintentos. Repetir un `GET` después de un problema de red no debería crear nada. Repetir un `POST` puede producir un segundo recurso si la API no introduce mecanismos adicionales.

### 3.2 El código de estado forma parte del contrato

El cuerpo no es toda la respuesta. El código HTTP permite al cliente interpretar el resultado sin analizar primero un texto arbitrario.

En este punto utilizaremos principalmente:

- `200 OK`: consulta correcta;
- `201 Created`: creación correcta;
- `400 Bad Request`: petición que no puede convertirse/procesarse por su formato;
- `404 Not Found`: recurso individual inexistente;
- `405 Method Not Allowed`: ruta existente pero método no admitido;
- `415 Unsupported Media Type`: representación enviada con un tipo no soportado.

Más adelante aparecerán otros códigos según nuevas necesidades.

### 3.3 `ResponseEntity` cuando necesitamos controlar HTTP

Devolver directamente un DTO es cómodo. Spring serializa el objeto y, si todo va bien, responde con 200. Esa convención es apropiada para muchos GET, pero no expresa una creación correctamente: un POST que crea un recurso debe comunicar `201 Created`.

`ResponseEntity<T>` permite controlar código, cabeceras y cuerpo:

```java
return ResponseEntity.status(HttpStatus.CREATED).body(dto);
```

También permite representar ausencia:

```java
return ResponseEntity.notFound().build();
```

No usaremos `ResponseEntity` por costumbre en todos los métodos; lo utilizaremos cuando el contrato HTTP necesite información que un cuerpo por sí solo no expresa.

### Pregunta

¿Por qué no basta con devolver `null` y dejar que el cliente deduzca que un alumno no existe?

### Respuesta razonada

Porque `null` no expresa por sí mismo el significado HTTP. Un cliente necesita distinguir un recurso inexistente de una respuesta correcta cuyo contenido admita ausencia. `404 Not Found` hace explícito el resultado en la capa correcta del protocolo.

## Bloque 4 - Paginación, filtrado y ordenación

### 4.1 Los query parameters modifican una consulta

Una colección puede crecer mucho. El cliente suele necesitar seleccionar una vista concreta sin crear una ruta nueva para cada combinación.

Ejemplos:

```text
GET /api/v1/alumnos?curso=5%C2%BA%20Primaria
GET /api/v1/alumnos?page=0&size=20
GET /api/v1/alumnos?sort=apellidos,asc
```

El recurso sigue siendo la colección `alumnos`; los parámetros modifican cómo se consulta o representa esa colección.

### 4.2 Filtrado

Un filtro expresa una condición, no una identidad:

```text
?curso=5º Primaria
```

Si el filtro es opcional, ausencia de parámetro significa “no filtrar por ese criterio”. Una colección que no contiene coincidencias debe poder representarse naturalmente como `[]` con una petición válida; no es equivalente a que la ruta no exista.

### 4.3 Paginación

Una convención habitual utiliza `page` y `size`:

```text
?page=0&size=20
```

El cliente indica qué ventana quiere consultar. Aunque en 1.4 sólo implementaremos el filtro `curso`, necesitamos comprender la convención porque el diseño REST debe anticipar colecciones que crecerán.

### 4.4 Ordenación

La ordenación suele expresarse también con query parameters:

```text
?sort=fecha,desc
```

Puede combinarse con filtrado y paginación sin multiplicar endpoints:

```text
/api/v1/becas?estado=EN_TRAMITE&sort=fecha,desc&page=0&size=20
```

La clave es la consistencia: si `curso` filtra alumnos mediante query parameter, otros recursos deberían seguir una regla equivalente para filtros comparables.

### Pregunta

¿Por qué un filtro que no encuentra resultados debería devolver `[]` con 200 en lugar de 404?

### Respuesta razonada

Porque la colección y la ruta sí existen, y la consulta es válida. El resultado de aplicar el criterio es una colección con cero elementos. `404` expresa que no se encontró el recurso identificado por la URL, no que una consulta válida sobre una colección produzca cero coincidencias.

## Bloque 5 - Documentación y evolución

### 5.1 El contrato debe poder descubrirse

Una API necesita explicar, como mínimo:

- qué recursos y endpoints existen;
- métodos HTTP admitidos;
- parámetros de path y query;
- estructura de los cuerpos JSON;
- códigos de éxito y error;
- ejemplos relevantes.

Puede hacerse con documentación manual o con especificaciones que permitan generar documentación, como OpenAPI. En este punto no añadiremos todavía una dependencia documental: primero necesitamos aprender a diseñar correctamente el contrato que después documentaremos.

### 5.2 Documentar comportamiento, no sólo rutas

Una lista de URLs es insuficiente. Por ejemplo, para `POST /api/v1/alumnos` necesitamos saber que la entrada es JSON y que la creación correcta responde 201. Para `GET /api/v1/alumnos/{id}` debemos documentar tanto 200 como 404.

El código puede compilar aunque la documentación esté equivocada. Por eso los ejemplos ejecutables y los tests de contrato son importantes: reducen la distancia entre lo documentado y lo que el servidor hace realmente.

### 5.3 Evolución compatible

Una API publicada tiene consumidores. Cambiarla obliga a pensar en compatibilidad:

- añadir capacidades opcionales suele ser menos disruptivo;
- renombrar o eliminar campos puede romper clientes;
- cambiar códigos de estado altera lógica del consumidor;
- cambiar una URL obliga a actualizar integraciones.

El versionado es una herramienta para gestionar cambios incompatibles, no una excusa para no diseñar con cuidado.

### 5.4 Diseño antes de código

La práctica de 1.4 empieza deliberadamente sin programar. Primero enumeraremos recursos, rutas, métodos y resultados. Sólo después crearemos `AlumnoDTO` y `AlumnoController`.

Ese orden importa: si dejamos que el primer método Java que se nos ocurra determine la API, convertimos decisiones internas en contrato público por accidente.

### Pregunta

¿Qué riesgo existe si publicamos una API y luego cambiamos libremente nombres de campos, rutas y códigos de estado?

### Respuesta razonada

Que los clientes ya integrados dejan de poder interpretar el contrato. Una API es una frontera entre sistemas; evolucionarla exige tratar compatibilidad, documentación y versionado como parte del diseño, no como tareas posteriores al código.

## Resumen del punto 1.4

- REST es un estilo arquitectónico centrado en recursos y representaciones.
- Las URLs identifican recursos; los métodos HTTP expresan operaciones.
- Colecciones en plural, minúsculas y versionado coherente facilitan un contrato predecible.
- `GET`, `POST`, `PUT`, `PATCH` y `DELETE` tienen semántica distinta, incluida seguridad e idempotencia.
- Los códigos HTTP describen el resultado real; 200, 201 y 404 no son intercambiables.
- Los identificadores individuales encajan en el path; filtros, paginación y ordenación encajan en query parameters.
- Una consulta válida sin coincidencias devuelve una colección vacía, no un 404.
- Documentación y evolución forman parte del contrato de una API.
- En la práctica construiremos `AlumnoDTO` y una primera API de alumnos sin adelantar el CRUD completo de 1.5.
