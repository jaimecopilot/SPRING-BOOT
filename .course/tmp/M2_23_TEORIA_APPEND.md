

---

# Punto 2.3 - Capas de la aplicación: Repositorio

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar cuál es la responsabilidad de un repositorio y por qué conviene aislar el acceso a datos.
2. Diferenciar `@Repository`, `@Service` y `@Component`, entendiendo qué expresa cada estereotipo.
3. Entender la idea de contrato público del repositorio y por qué en Spring Data suele materializarse mediante una interfaz.
4. Diseñar un repositorio en memoria con `ConcurrentHashMap`, `Optional`, listas defensivas y una estrategia de identificadores estable.
5. Refactorizar un servicio para que delegue el almacenamiento sin filtrar detalles de persistencia al controlador.
6. Razonar sobre copia defensiva, concurrencia, transformación DTO-modelo y los errores más frecuentes de esta capa.

## Bloque 1 - Qué es un repositorio y cuál es su responsabilidad

### 1.1 El repositorio es la frontera con los datos

En 2.1 separamos la frontera HTTP. En 2.2 sacamos del controlador las reglas y operaciones de aplicación. Todavía queda una responsabilidad mezclada: `AlumnoService` conoce **cómo** se almacenan los alumnos porque conserva una colección en memoria.

Un repositorio existe para aislar precisamente ese conocimiento. Es la capa que sabe cómo guardar y recuperar datos. El resto de la aplicación debería formular peticiones de este tipo:

```text
buscar el alumno 7
listar todos los alumnos
guardar este alumno
eliminar el alumno 7
```

pero no debería tener que saber si para cumplirlas hay que:

- buscar una clave en un `Map`;
- ejecutar SQL contra PostgreSQL;
- consultar MongoDB;
- leer un fichero;
- invocar un servicio remoto;
- o utilizar una colección en memoria durante un ejercicio.

La idea central es la **abstracción del almacenamiento**: el servicio expresa qué dato necesita; el repositorio resuelve cómo obtenerlo.

### 1.2 La analogía del bibliotecario y el archivero

Imagina una biblioteca. El bibliotecario atiende al usuario, aplica las reglas de préstamo y decide si una operación es válida. No necesita conocer la posición física de cada libro. Para eso existe el archivero, que sabe si el ejemplar está en una estantería, en una caja o en un depósito.

En esta analogía:

```text
controlador -> mostrador de entrada
servicio    -> bibliotecario / reglas
repositorio -> archivero / localización física
almacén     -> estanterías, cajas, base de datos...
```

Si la biblioteca reorganiza el depósito, no debería reescribir las reglas de préstamo. De forma equivalente, si una aplicación sustituye el almacenamiento en memoria por una base de datos, el cambio debería concentrarse en la capa de datos.

Nuestro repositorio de 2.3 seguirá siendo deliberadamente sencillo: una colección Java simulará el almacenamiento. La utilidad del ejercicio no está en fingir que un `Map` es una base de datos, sino en aprender una frontera que más adelante seguirá existiendo cuando aparezcan JPA e Hibernate.

### 1.3 Qué NO pertenece al repositorio

Separar capas no consiste sólo en mover código a carpetas diferentes. También implica que cada capa rechace responsabilidades ajenas.

Un repositorio **no debería decidir reglas de negocio**. Por ejemplo, no le corresponde resolver si:

- un DNI duplicado impide matricular un alumno;
- un expediente puede pasar de un estado a otro;
- un importe supera un límite permitido;
- una operación debe producir un conflicto de negocio.

Esas decisiones pertenecen al servicio.

Tampoco debería construir `ResponseEntity`, elegir un código HTTP ni leer `@RequestParam`. Eso pertenece al controlador.

La distribución que buscamos es:

```text
Cliente HTTP
    |
    v
Controlador  -> interpreta HTTP y construye HTTP
    |
    v
Servicio     -> aplica reglas y orquesta
    |
    v
Repositorio  -> guarda y recupera
    |
    v
Almacenamiento
```

### 1.4 El cambio de almacenamiento como prueba mental

Una buena forma de comprobar si la frontera está bien diseñada es imaginar una sustitución radical del almacenamiento.

Supón que hoy tenemos:

```text
ConcurrentHashMap
```

y mañana queremos:

```text
PostgreSQL
```

El controlador no debería cambiar. Las reglas de negocio del servicio tampoco deberían reescribirse por ese motivo. La implementación concreta de acceso a datos será la pieza que evolucione.

En una aplicación real puede haber ajustes de tipos, transacciones o consultas que obliguen a tocar más de una clase, pero el objetivo arquitectónico sigue siendo reducir al mínimo la propagación del cambio.

### Pregunta

¿Qué capa debería conocer si los datos están en un `ConcurrentHashMap`, en PostgreSQL o en un fichero?

### Respuesta razonada

El repositorio. El controlador conoce HTTP y el servicio conoce las operaciones y reglas de la aplicación. Si ambos empiezan a depender de detalles del almacenamiento, cambiar la tecnología de datos obligará a modificar varias capas y la abstracción habrá dejado de cumplir su función.

## Bloque 2 - La anotación `@Repository`

### 2.1 `@Repository` como estereotipo

Spring permite marcar la clase con:

```java
@Repository
public class AlumnoRepository {
    // acceso a datos
}
```

`@Repository` es una especialización de `@Component`, igual que `@Service` comunica que una clase pertenece a la capa de servicio. Las tres participan en el escaneo de componentes, pero expresan roles diferentes:

- `@Component`: componente genérico que no encaja mejor en otro estereotipo.
- `@Service`: lógica de aplicación y de negocio.
- `@Repository`: acceso a datos.

Elegir el estereotipo correcto aporta información arquitectónica a quien lee el código. Además, `@Repository` tiene significado específico en el ecosistema de acceso a datos de Spring: puede participar en la traducción de determinadas excepciones de persistencia a la jerarquía de excepciones de acceso a datos de Spring.

En nuestro repositorio en memoria no existe todavía una base de datos que produzca esas excepciones. Aun así usamos `@Repository` porque describe correctamente el rol y prepara el diseño para la evolución posterior.

### 2.2 Cómo lo descubre Spring

El mecanismo es el mismo que vimos con `@Service`. La clase principal está bajo:

```text
es.mecd.demo.miproyecto
```

y `@SpringBootApplication` inicia el escaneo de ese paquete y sus subpaquetes.

Cuando Spring encuentra:

```text
es.mecd.demo.miproyecto.repository.AlumnoRepository
```

puede:

1. detectar el estereotipo `@Repository`;
2. construir el bean;
3. resolver sus dependencias;
4. registrarlo en el contexto;
5. inyectarlo donde se solicite, por ejemplo en `AlumnoService`.

Por eso el servicio no escribe:

```java
new AlumnoRepository()
```

sino que declara la dependencia en su constructor:

```java
private final AlumnoRepository repositorio;

public AlumnoService(AlumnoRepository repositorio) {
    this.repositorio = repositorio;
}
```

Spring compone el grafo de objetos al arrancar.

### 2.3 Bean singleton y estado compartido

Como nuestros repositorios son beans de aplicación, una misma instancia puede ser utilizada por múltiples peticiones concurrentes. Eso importa mucho en un repositorio en memoria: la estructura `almacen` es estado compartido.

Un `HashMap` normal no está diseñado para escrituras concurrentes sin coordinación. Por eso el ejemplo utiliza `ConcurrentHashMap` como estructura de almacenamiento.

Esto no significa que cualquier operación formada por varios pasos se vuelva automáticamente atómica. `ConcurrentHashMap` protege sus operaciones y su estructura interna, pero una secuencia como “comprobar algo y después modificar otra cosa” puede necesitar coordinación adicional. En 2.3 aprenderemos la diferencia entre **usar una estructura concurrente** y **demostrar que toda la lógica compuesta es thread-safe**.

### 2.4 Repositorio como interfaz y como clase

En este punto escribimos una clase concreta:

```java
@Repository
public class AlumnoRepository {
    // ...
}
```

porque todavía no hemos introducido Spring Data JPA. Sin embargo, debemos aprender ya la idea de **contrato público**: el servicio utiliza operaciones como `buscarPorId`, `listarTodos`, `guardar` o `eliminar`, no accede directamente al `Map`.

Más adelante veremos una forma habitual en Spring Data:

```java
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
```

En ese escenario Spring genera una implementación. No adelantaremos JPA ahora, pero sí conservaremos el principio: el consumidor depende del contrato del repositorio y no de sus detalles internos.

### Pregunta

¿Por qué es útil que el repositorio exponga una interfaz pública clara aunque en 2.3 sea una clase concreta?

### Respuesta razonada

Porque el servicio sólo necesita conocer operaciones de datos con significado estable. Si mañana cambia la implementación interna —de un `Map` a JPA, por ejemplo— el servicio no debería empezar a manipular SQL, mapas o detalles de infraestructura. Un contrato claro limita el acoplamiento.

## Bloque 3 - Diseño de un repositorio en memoria

### 3.1 Por qué usamos un `Map`

Una colección de alumnos puede modelarse con una lista, pero para un repositorio resulta natural indexar por identificador:

```java
private final Map<String, AlumnoDTO> almacen =
        new ConcurrentHashMap<>();
```

La clave es el ID y el valor es el objeto almacenado.

Una consulta individual puede expresarse de forma directa:

```java
public Optional<AlumnoDTO> buscarPorId(String id) {
    return Optional.ofNullable(almacen.get(id));
}
```

La versión final del curso añade además una copia defensiva antes de devolver el objeto, algo que veremos enseguida.

### 3.2 `ConcurrentHashMap`: qué aporta y qué no

`ConcurrentHashMap` permite que distintos hilos realicen accesos concurrentes sin corromper la estructura interna del mapa. Es una elección mucho más adecuada que un `HashMap` mutable compartido por un bean singleton.

Sin embargo, conviene evitar una conclusión excesiva:

```text
ConcurrentHashMap != toda la aplicación es automáticamente thread-safe
```

Por ejemplo, si una regla hiciera:

```text
1. comprobar que no existe un DNI
2. calcular un ID
3. guardar
```

esas tres decisiones forman una operación compuesta. Cada llamada al mapa puede ser segura individualmente y, aun así, dos hilos podrían intercalarse entre pasos si no existe una estrategia adicional.

Para la generación de identificadores usamos un `AtomicInteger`. La secuencia es monotónica y no retrocede al borrar elementos. Así evitamos el problema de `contar() + 1`: si existen `1`, `2`, `3`, borramos `2` y usamos el tamaño `2` para generar el siguiente ID, volveríamos a producir `3` y colisionaríamos.

### 3.3 Operaciones públicas del repositorio

El contrato en memoria incluye operaciones con nombres explícitos:

```java
Optional<AlumnoDTO> buscarPorId(String id)
List<AlumnoDTO> listarTodos()
Optional<AlumnoDTO> buscarPorDni(String dni)
boolean existePorDni(String dni)
AlumnoDTO guardar(AlumnoDTO alumno)
boolean eliminar(String id)
int contar()
String siguienteIdentificador()
```

Cada método responde a una necesidad de acceso a datos.

`guardar` se comporta como una operación de inserción o sustitución por clave:

```java
almacen.put(alumno.getIdentificador(), alumno);
```

Si la clave no existía, aparece una entrada. Si existía, el valor se sustituye.

`eliminar` puede convertir el resultado de `remove` en un booleano:

```java
return almacen.remove(id) != null;
```

### 3.4 `Optional` y listas: ausencia sin `null`

Para una consulta que puede encontrar cero o un elemento utilizamos:

```java
Optional<AlumnoDTO>
```

Esto obliga al consumidor a reconocer la posibilidad de ausencia. El servicio o el controlador pueden trabajar con `map`, `orElse`, `orElseThrow` u otras operaciones de `Optional` sin depender de un `null` inesperado.

Para consultas múltiples devolvemos una lista. Si no hay resultados, una lista vacía comunica mejor el contrato que `null`:

```text
resultado múltiple sin coincidencias -> []
resultado único sin coincidencia     -> Optional.empty()
```

### 3.5 No exponer la estructura interna

La fuente original propone:

```java
return new ArrayList<>(almacen.values());
```

Eso es mejor que devolver directamente la colección interna del mapa, porque el llamador puede hacer `clear()` sobre su lista sin vaciar el mapa.

Pero hay una sutileza importante: esa copia sólo crea **otra colección de referencias**. Si `AlumnoDTO` es mutable, modificar uno de esos objetos podría seguir afectando al objeto almacenado si ambos apuntaran a la misma instancia.

Por eso nuestra edición aplica una defensa más fuerte:

```java
almacen.values().stream()
        .map(this::copiar)
        .toList();
```

y `buscarPorId` también devuelve una copia. Del mismo modo, `guardar` conserva una copia propia. Así protegemos tanto la colección como los elementos mutables.

No estamos afirmando que éste sea un sistema de persistencia de producción; estamos haciendo visible qué significa realmente no exponer el estado interno.

### 3.6 Orden determinista

`ConcurrentHashMap` no promete orden de inserción. Si simplemente convertimos `values()` en una lista, el orden observable puede variar.

Nuestro contrato acumulativo ya utiliza paginación y espera resultados predecibles. Por eso el repositorio ordena el snapshot por ID numérico antes de entregarlo. Si el usuario no pide un `sort` funcional, el servicio recibe así un orden base estable `1, 2, 3...`.

La ordenación por `nombre` o `apellidos` sigue perteneciendo al servicio porque es una decisión de la consulta de aplicación que ya existía antes del repositorio.

### Pregunta

¿Por qué `new ArrayList<>(almacen.values())` no basta por sí solo para garantizar que el llamador no pueda modificar un `AlumnoDTO` almacenado?

### Respuesta razonada

Porque la nueva lista puede contener las mismas referencias a objetos mutables que existen en el mapa. Copiar la colección impide modificar la estructura interna a través de `clear`, `add` o `remove`, pero no aísla los objetos. Si queremos proteger también los elementos, debemos devolver copias de esos objetos o utilizar modelos inmutables.

## Bloque 4 - Transformación DTO ↔ Entidad en el servicio

### 4.1 El repositorio idealmente no depende del contrato HTTP

Un DTO representa datos que cruzan una frontera de la aplicación. En una arquitectura con persistencia real, la capa de datos suele trabajar con entidades o modelos de almacenamiento, no con el DTO que hemos diseñado para la API.

La razón es el desacoplamiento. Imagina que una entidad tuviera campos internos de auditoría y el DTO sólo expusiera una selección. Si el repositorio devolviera directamente el DTO de la API, cualquier cambio del contrato externo podría propagarse hasta el almacenamiento.

En M2 todavía no existe una entidad `Alumno` separada. Por eso el repositorio en memoria utiliza `AlumnoDTO` como **modelo provisional de almacenamiento**. Es una simplificación consciente, no una afirmación de que entidad y DTO deban ser siempre la misma clase.

### 4.2 El patrón `toDTO` / `toEntity`

Para dejar visible el punto de transformación, `AlumnoService` mantiene dos métodos privados:

```java
private AlumnoDTO toDTO(AlumnoDTO entidad) {
    return entidad;
}

private AlumnoDTO toEntity(AlumnoDTO dto) {
    return dto;
}
```

Hoy parecen triviales porque ambos lados usan la misma clase. Su valor es pedagógico: señalan **dónde** ocurrirá la conversión cuando los tipos sean distintos.

Más adelante una conversión real podrá parecerse a:

```java
private AlumnoDTO toDTO(Alumno entidad) {
    AlumnoDTO dto = new AlumnoDTO();
    dto.setIdentificador(entidad.getId().toString());
    dto.setNombre(entidad.getNombre());
    dto.setApellidos(entidad.getApellidos());
    return dto;
}
```

No creamos esa entidad en 2.3 porque todavía no hemos introducido JPA.

Cuando una aplicación crece, este mapeo también puede extraerse a un componente dedicado o apoyarse en herramientas como MapStruct. Para aprender la frontera, los métodos explícitos son más transparentes.

### 4.3 Flujo completo de una consulta

Tras el refactor, una consulta individual recorre:

```text
1. Cliente -> GET /api/v1/alumnos/1
2. AlumnoController.consultar("1")
3. AlumnoService.consultar("1")
4. AlumnoRepository.buscarPorId("1")
5. repositorio consulta el almacenamiento
6. repositorio entrega el modelo/copia
7. servicio aplica toDTO
8. controlador construye ResponseEntity
9. Jackson serializa el DTO a JSON
10. respuesta -> cliente
```

El recorrido de escritura va en sentido complementario:

```text
JSON -> controlador -> DTO -> servicio -> toEntity -> repositorio -> almacenamiento
```

Cada capa conoce a la siguiente, no a todas las capas del sistema.

### 4.4 Qué cambia y qué permanece

Antes de 2.3:

```text
AlumnoController -> AlumnoService -> List<AlumnoDTO>
```

Después:

```text
AlumnoController -> AlumnoService -> AlumnoRepository -> ConcurrentHashMap
```

El controlador no necesita enterarse. Sus rutas, parámetros, `ResponseEntity`, `Location`, 404 y 409 permanecen como estaban.

El servicio pierde el detalle `List` y gana una dependencia `AlumnoRepository`. Conserva reglas como el rechazo de DNI duplicado, el filtro, la ordenación o la promoción porque esas decisiones no son responsabilidad del repositorio.

### Pregunta

Si mañana sustituimos el almacenamiento en memoria por una base de datos, ¿qué capa debería concentrar el cambio y cuáles deberían conservar su responsabilidad?

### Respuesta razonada

El cambio debe concentrarse en la capa de repositorio y, cuando aparezca un modelo de entidad distinto, en el mapeo asociado al servicio. El controlador debe seguir manejando HTTP y el servicio debe seguir expresando reglas de negocio. Ese reparto evita que la tecnología de persistencia invada toda la aplicación.

## Bloque 5 - Buenas prácticas y errores comunes

### 5.1 Nombres que expresan intención

Un repositorio es más fácil de entender cuando sus métodos describen el dato que obtienen o modifican:

```text
buscarPorId
buscarPorDni
listarTodos
guardar
eliminar
contar
```

Evita nombres vagos como `hacerConsulta`, `procesar` o `getData` si existe una operación de dominio mucho más precisa.

### 5.2 Devolver contratos seguros

Para resultados únicos, `Optional<T>` hace explícita la ausencia. Para resultados múltiples, una lista vacía evita comprobaciones de `null`.

No devuelvas el `Map` interno. Tampoco conviene devolver una vista viva de `values()`. Una capa superior no debería poder vaciar, reordenar o reemplazar el estado del repositorio por haber obtenido una referencia accidental.

Con objetos mutables, recuerda además la diferencia entre copiar **la colección** y copiar **los elementos**.

### 5.3 Mantener fuera la lógica de negocio

Esta implementación sería sospechosa:

```java
public AlumnoDTO guardar(AlumnoDTO alumno) {
    if (alumno.getDni().equals("PROHIBIDO")) {
        throw new NegocioException("...");
    }
    // guardar
}
```

La regla sobre qué DNI es aceptable no es una decisión de almacenamiento. Debe vivir en el servicio.

El repositorio sí puede defender sus invariantes técnicos —por ejemplo, exigir una clave necesaria para almacenar—, pero no debe convertirse en el lugar donde se toman decisiones del dominio.

### 5.4 Concurrencia: precisión en vez de falsas garantías

Usar `ConcurrentHashMap` evita los problemas básicos de una estructura no preparada para accesos concurrentes. Aun así, una prueba con diez GET paralelos sólo es un **smoke test**: demuestra que ese escenario concreto no falla, no constituye una prueba matemática de todas las intercalaciones posibles.

También hay que distinguir operaciones simples de operaciones compuestas. Si una regla necesita leer y después escribir basándose en lo leído, puede ser necesario usar operaciones atómicas específicas, sincronización, transacciones o restricciones de base de datos.

En nuestro ejercicio:

- el mapa es concurrente;
- la secuencia de IDs usa `AtomicInteger`;
- el repositorio no expone su estado mutable;
- el servicio conserva las reglas;
- pero no presentamos este almacén didáctico como sustituto de una base de datos transaccional.

### 5.5 Errores frecuentes

**Devolver `null` en lugar de `Optional`.** Obliga a propagar comprobaciones y facilita `NullPointerException`.

**Devolver la colección interna.** Permite que el consumidor modifique estado que pertenece al repositorio.

**Copiar sólo la lista y olvidar los objetos mutables.** La estructura queda aislada, pero no necesariamente cada elemento.

**Usar `HashMap` mutable compartido sin coordinación.** Un bean singleton puede recibir accesos desde varios hilos.

**Poner reglas de negocio en el repositorio.** Mezcla almacenamiento con decisiones de aplicación.

**Transformar el modelo de API dentro del repositorio.** Acopla la persistencia al contrato HTTP. La transformación pertenece al servicio o a un mapper dedicado.

**Capturar cualquier excepción y devolver `null`.** Oculta la causa real y convierte un error técnico en una ausencia aparente.

**Generar IDs con `contar() + 1`.** El tamaño no es un identificador. Los borrados crean huecos y pueden producir colisiones.

**Suponer que `ConcurrentHashMap` conserva orden.** No lo hace; si la API necesita un orden base reproducible, hay que definirlo explícitamente.

### Pregunta

¿Por qué no deberíamos capturar una excepción técnica del almacenamiento y devolver simplemente `null` desde el repositorio?

### Respuesta razonada

Porque `null` borraría información esencial. El servicio no podría distinguir “no existe el alumno” de “el almacenamiento ha fallado”. Una ausencia de datos y un fallo técnico son situaciones distintas y deben conservar significados distintos a medida que atraviesan las capas.

## Resumen de la teoría 2.3

En este punto hemos añadido la tercera capa del recorrido principal:

```text
HTTP -> Controller -> Service -> Repository -> Data
```

El repositorio:

- abstrae el almacenamiento;
- usa `@Repository` para expresar su rol;
- expone operaciones de datos con nombres claros;
- devuelve `Optional` o listas en lugar de `null`;
- evita exponer estado interno;
- utiliza una estructura concurrente apropiada para este ejercicio;
- no absorbe reglas de negocio ni conceptos HTTP.

También hemos afinado dos ideas que suelen simplificarse demasiado: una copia de la colección no implica una copia de sus objetos, y una estructura concurrente no vuelve atómica cualquier operación compuesta. En el siguiente recorrido práctico aplicaremos estos principios sin perder ningún comportamiento acumulado de 2.1 y 2.2.
