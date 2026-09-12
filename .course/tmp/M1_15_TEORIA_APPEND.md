

---

# Punto 1.5 - Primer CRUD con DTOs

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar el ciclo CRUD y relacionar Create/Read/Update/Delete con POST/GET/PUT-PATCH/DELETE.
2. Diferenciar actualización completa con PUT de actualización parcial con PATCH.
3. Explicar idempotencia y seguridad en el contexto de operaciones CRUD.
4. Elegir códigos HTTP coherentes para creación, consulta, actualización y borrado.
5. Entender qué significa persistencia en memoria y qué limitaciones tiene frente a una capa de datos real.
6. Implementar un CRUD completo sobre `AlumnoDTO` manteniendo el identificador de la URL como identidad del recurso.
7. Explicar por qué un DELETE puede devolver 204 la primera vez y 404 la segunda sin dejar de ser idempotente en estado final.
8. Reconocer la fragilidad pedagógica de un PATCH basado en `Map<String, Object>`.
9. Mantener filtrado y ordenación como parámetros opcionales de la colección.
10. Identificar qué responsabilidades del controlador deberán extraerse a servicio y repositorio en los módulos siguientes.

## Bloque 1 - El ciclo CRUD

### 1.1 Qué significa CRUD

CRUD resume cuatro operaciones básicas sobre un recurso:

```text
Create -> crear
Read   -> consultar
Update -> actualizar
Delete -> eliminar
```

En una API REST solemos proyectarlas sobre HTTP así:

| Operación | Método HTTP habitual | Recurso |
|---|---|---|
| Create | `POST` | colección |
| Read colección | `GET` | colección |
| Read individual | `GET` | elemento |
| Update completo | `PUT` | elemento |
| Update parcial | `PATCH` | elemento |
| Delete | `DELETE` | elemento |

La fuente insiste en una idea importante: el CRUD es el esqueleto de muchos sistemas de gestión. Filtros, seguridad, persistencia real, validación y reglas de negocio se apoyan después sobre estas operaciones básicas. fileciteturn291file2

En nuestro recurso Alumno:

```text
GET    /api/v1/alumnos
GET    /api/v1/alumnos/{id}
POST   /api/v1/alumnos
PUT    /api/v1/alumnos/{id}
PATCH  /api/v1/alumnos/{id}
DELETE /api/v1/alumnos/{id}
```

No son seis recursos distintos. Son dos identidades principales —colección y elemento— sobre las que aplicamos diferentes métodos.

### 1.2 Crear no es sólo devolver el cuerpo

En 1.4 nuestro POST devolvía 201, pero no modificaba la colección. Eso servía para separar dos conceptos:

- **contrato HTTP:** status y representación devuelta;
- **efecto sobre el estado del servidor:** persistencia.

En 1.5 conectamos ambas cosas. Cuando el POST sea correcto deberá:

1. recibir un `AlumnoDTO`;
2. asignarle un identificador si no lo tiene;
3. añadirlo a la colección mutable;
4. responder 201 con el recurso creado.

La lista en memoria sustituye temporalmente a una base de datos. Es útil para aprender el ciclo CRUD, pero desaparece al reiniciar la aplicación y no resuelve concurrencia, transacciones ni persistencia durable.

### 1.3 Read: colección e individuo no significan lo mismo

El GET de colección devuelve 200 aunque el resultado sea `[]`. El recurso colección existe; simplemente no hay elementos que satisfagan la consulta.

El GET individual puede devolver 404 porque la URL identifica un recurso concreto que quizá no exista.

La diferencia no es estética. Permite al cliente distinguir:

```text
colección válida sin resultados -> 200 + []
recurso individual inexistente  -> 404
```

### Pregunta

¿Un POST que responde 201 pero no añade el alumno a ningún almacenamiento completa realmente la operación Create?

### Respuesta razonada

No en el sentido funcional del CRUD. El status puede afirmar creación, pero si una consulta posterior no puede recuperar el recurso, falta el efecto de persistencia. En 1.4 aislamos deliberadamente el contrato HTTP; en 1.5 conectamos contrato y cambio de estado.

## Bloque 2 - PUT en detalle

### 2.1 PUT reemplaza una representación completa

La fuente define PUT como “reemplaza el recurso existente con esta representación”, no como “actualiza sólo los campos enviados”. fileciteturn291file0

Si el recurso tiene estos campos:

```text
id, nombre, apellidos, dni, fechaNacimiento, curso, documentos
```

un PUT conceptual debe representar el nuevo estado completo. Si queremos modificar sólo `curso`, PATCH expresa mejor la intención.

En nuestro ejercicio el identificador de la URL será la autoridad:

```text
PUT /api/v1/alumnos/3
```

Aunque el cuerpo trajera otro `id`, el recurso que se está reemplazando es el identificado por `/3`. Por eso el controlador fijará explícitamente:

```java
dto.setIdentificador(id);
```

antes de sustituir el elemento de la colección.

### 2.2 Idempotencia de PUT

PUT es idempotente: repetir la misma petición deja el recurso en el mismo estado final que ejecutarla una sola vez. fileciteturn291file0

Si enviamos cinco veces:

```text
PUT /api/v1/alumnos/3
{ ... representación completa ... }
```

no debemos crear cinco alumnos ni acumular cambios. Cada ejecución vuelve a establecer el mismo estado del recurso 3.

Esto es especialmente útil ante reintentos de red: si el cliente no sabe si recibió la respuesta, puede repetir la operación sin duplicar el recurso.

### 2.3 PUT existente e inexistente

Para nuestro CRUD introductorio utilizaremos:

```text
recurso existente   -> 200 OK + recurso actualizado
recurso inexistente -> 404 Not Found
```

La fuente admite también 204 para una actualización correcta sin cuerpo, pero en este ejercicio devolveremos 200 para poder observar el recurso resultante. fileciteturn285file4

### Pregunta

¿Por qué no usamos `alumnos.add(dto)` para implementar PUT?

### Respuesta razonada

Porque PUT opera sobre una identidad existente y debe reemplazar ese recurso, no crear otro elemento al final de la colección. Añadir siempre produciría duplicados y rompería la idempotencia esperada.

## Bloque 3 - PATCH y DELETE en detalle

### 3.1 PATCH modifica parcialmente

PATCH expresa una modificación parcial: los campos no enviados conservan su valor actual. La fuente lo contrasta explícitamente con PUT. fileciteturn291file1

En este primer ejercicio recibiremos:

```java
Map<String, Object> cambios
```

Y sólo admitiremos campos concretos que sepamos convertir con seguridad, por ejemplo:

```text
nombre
apellidos
dni
curso
```

Este `Map` es una simplificación pedagógica, no un diseño final recomendado. Tiene varias limitaciones:

- pierde tipado estático;
- obliga a comprobar nombres manualmente;
- puede requerir conversiones explícitas;
- dificulta validación y documentación.

Más adelante podremos usar DTOs específicos de entrada y validación formal.

### 3.2 Idempotencia de PATCH: cuidado con las generalizaciones

La fuente indica correctamente que PATCH **no es idempotente en general**. fileciteturn291file1

Sin embargo, una operación concreta como:

```json
{"curso":"6º Primaria"}
```

implementada como simple asignación sí puede dejar el mismo estado final al repetirse. La regla importante es no concluir que **todo PATCH** es idempotente. Un PATCH como “incrementa el contador en 1” no lo sería.

### 3.3 DELETE elimina el recurso

DELETE trabaja sobre la URL individual:

```text
DELETE /api/v1/alumnos/3
```

En nuestro almacenamiento en memoria utilizaremos `removeIf`:

```java
boolean eliminado = alumnos.removeIf(
        a -> a.getIdentificador().equals(id));
```

Resultado:

```text
se eliminó algo -> 204 No Content
no existía       -> 404 Not Found
```

La fuente propone precisamente ese comportamiento y pregunta si sigue siendo idempotente cuando la segunda llamada devuelve 404. fileciteturn291file5

Sí: la idempotencia describe el **estado final**. Después de la primera y de la segunda llamada el recurso está ausente. El status puede ser diferente porque la segunda petición observa un estado previo diferente.

### Pregunta

Si DELETE devuelve 204 la primera vez y 404 la segunda, ¿por qué seguimos considerándolo idempotente?

### Respuesta razonada

Porque ambas secuencias terminan con el mismo estado del servidor: el recurso no existe. Idempotencia no significa “misma respuesta HTTP en cada repetición”, sino “mismo efecto final sobre el estado”.

## Bloque 4 - Códigos de estado en el CRUD

### 4.1 Tabla de contrato

La fuente resume los códigos habituales del CRUD así: GET colección 200; GET individual 200/404; POST 201; PUT y PATCH 200 o 204/404; DELETE 204/404. fileciteturn285file1

En nuestro ejercicio fijamos:

| Operación | Éxito | Recurso inexistente | Formato/tipo inválido |
|---|---:|---:|---:|
| GET colección | 200 | — | — |
| GET individual | 200 | 404 | — |
| POST | 201 | — | 400 / 415 |
| PUT | 200 | 404 | 400 / 415 |
| PATCH | 200 | 404 | 400 / 415 |
| DELETE | 204 | 404 | — |

También veremos `405 Method Not Allowed` cuando la ruta existe pero el método solicitado no está mapeado.

### 4.2 400 no es lo mismo que 415

Estos dos errores aparecen antes de que nuestro método pueda trabajar correctamente con el objeto:

- `400 Bad Request`: JSON mal formado o no convertible al tipo esperado;
- `415 Unsupported Media Type`: el cliente envía un cuerpo con un tipo que el endpoint no acepta, por ejemplo `text/plain` cuando esperamos JSON.

No debemos escribir lógica manual para producirlos si Spring MVC/Jackson ya pueden detectarlos en la capa correcta.

### 4.3 404 y 405 tampoco son equivalentes

```text
GET /api/v1/alumnos/999 -> 404
POST /api/v1/alumnos/1  -> 405
```

En el primer caso existe el patrón de endpoint, pero el recurso 999 no. En el segundo la URL individual está reconocida para otros métodos, pero POST no está permitido allí.

### 4.4 Consistencia

La fuente subraya que una API predecible debe utilizar los mismos códigos para resultados equivalentes. fileciteturn291file3

No tendría sentido que un GET individual inexistente devolviera 404 en un recurso y 200 con `null` en otro sin una razón contractual explícita. La consistencia permite al cliente construir manejo genérico de errores.

### Pregunta

¿Por qué un cliente se beneficia de que todos los recursos usen 404 de forma consistente cuando no existe un elemento individual?

### Respuesta razonada

Porque puede implementar una única política para “recurso no encontrado”. Si cada endpoint inventa un cuerpo o status distinto, el cliente necesita excepciones específicas y el contrato se vuelve más difícil de integrar.

## Bloque 5 - Consolidación del Módulo 1

### 5.1 Lo que ya sabemos construir

Al cerrar M1 habremos recorrido una cadena completa:

```text
Spring Boot y auto-configuración
        ↓
cliente-servidor + HTTP
        ↓
JSON + Jackson
        ↓
diseño REST
        ↓
CRUD con DTOs
```

Eso significa que ya podemos explicar no sólo que un endpoint “funciona”, sino por qué existe, qué contrato HTTP tiene, cómo se representa en JSON y cómo cambia el estado en memoria.

### 5.2 Lo que todavía no hemos resuelto

Nuestro `AlumnoController` final de M1 será deliberadamente monolítico. Tendrá:

- almacenamiento en memoria;
- generación pedagógica de IDs;
- búsqueda;
- modificación;
- borrado;
- manejo HTTP;
- filtrado y ordenación.

Funciona como ejercicio, pero reúne demasiadas responsabilidades. Esa limitación prepara el siguiente módulo: separar controlador, servicio y repositorio.

Tampoco tenemos todavía:

- base de datos;
- transacciones;
- validación declarativa de entrada;
- reglas de negocio robustas;
- tratamiento global de excepciones;
- seguridad;
- concurrencia controlada;
- generación de IDs de producción.

### 5.3 La generación de ID es intencionadamente simple

La fuente pedagógica utiliza una idea equivalente a `size()+1` para que el nuevo alumno obtenga el ID 3. La conservaremos como simplificación visible, pero con una advertencia: después de borrar elementos puede producir colisiones.

Una versión algo más robusta dentro del mismo ejercicio puede calcular el máximo ID numérico actual y sumar uno. Aun así sigue siendo almacenamiento local, no una estrategia válida para concurrencia o producción.

### 5.4 Filtro y ordenación siguen siendo consulta

El último reto combinará:

```text
?curso=...
?sort=nombre
?sort=apellidos
```

Ambos siguen modificando la vista de la colección, por lo que pertenecen a query parameters.

La edición corrige una contradicción del ejemplo histórico: si `sort` es opcional, no usaremos `defaultValue="nombre"`, porque eso ordenaría siempre aunque el cliente no hubiera solicitado ordenación. Sin `sort`, conservaremos el orden actual de la colección.

### 5.5 El final de M1 es el punto de partida de M2

El objetivo no es terminar con un controlador “perfecto”. El objetivo es llegar a un estado funcional cuya siguiente debilidad resulte evidente: la lógica del CRUD no debería vivir toda dentro del controlador.

Eso permitirá que M2 introduzca capas de aplicación sobre algo que ya comprendemos funcionalmente.

### Pregunta

¿Por qué tiene sentido construir primero un CRUD sencillo dentro del controlador si luego vamos a refactorizarlo?

### Respuesta razonada

Porque primero aislamos el comportamiento observable y el contrato HTTP. Cuando después movamos lógica a servicio y repositorio podremos comprobar que el comportamiento no cambia. Refactorizar resulta mucho más comprensible cuando sabemos exactamente qué estamos preservando.

## Resumen del punto 1.5

- CRUD significa Create, Read, Update y Delete.
- POST crea; GET consulta; PUT reemplaza; PATCH modifica parcialmente; DELETE elimina.
- PUT es idempotente y debe representar una actualización completa.
- PATCH es parcial y no debe asumirse idempotente de forma universal.
- DELETE puede responder 204 y después 404 sin perder idempotencia de estado final.
- Los códigos HTTP forman parte del contrato, no son decoración.
- El almacenamiento en una `ArrayList` sirve para aprender, pero no es persistencia de producción.
- El POST final debe guardar el alumno y devolver 201.
- PUT/PATCH/DELETE deben devolver 404 si el ID no existe.
- El listado final conserva filtrado y añade ordenación opcional.
- El controlador resultante será funcional, pero deliberadamente listo para ser refactorizado en M2.
