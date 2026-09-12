# Punto 0.3 - Estructura estándar de un proyecto Spring Boot

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Reconocer la estructura Maven estándar.
2. Distinguir código de producción, recursos y tests.
3. Explicar el papel de la clase principal.
4. Entender el alcance del escaneo de componentes.
5. Explicar `application.properties`.
6. Entender qué es un controlador REST básico.
7. Razonar el flujo de una petición HTTP hasta un método Java.

## Bloque 1 - Vista general

Una estructura mínima puede verse así:

```text
mi-proyecto/
|-- .mvn/
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
|-- src/
|   |-- main/
|   |   |-- java/
|   |   |   `-- es/mecd/demo/miproyecto/
|   |   |       `-- MiProyectoApplication.java
|   |   `-- resources/
|   |       `-- application.properties
|   `-- test/
|       `-- java/
|           `-- es/mecd/demo/miproyecto/
|               `-- MiProyectoApplicationTests.java
`-- target/              # aparece al construir; no se versiona
```

Spring Boot no exige de forma absoluta que cada proyecto tenga exactamente esta estructura, pero Maven y Spring trabajan especialmente bien cuando seguimos sus convenciones.

### 1.1 Por qué importa el paquete de la clase principal

`@SpringBootApplication` incluye escaneo de componentes desde el paquete donde está la clase principal hacia sus subpaquetes.

Si `MiProyectoApplication` está en:

```text
es.mecd.demo.miproyecto
```

entonces un controlador en:

```text
es.mecd.demo.miproyecto.controller
```

queda dentro del árbol natural de escaneo.

Un controlador colocado en un paquete hermano fuera de ese árbol puede no ser detectado automáticamente.


### 1.2 Fuentes frente a artefactos generados

Una distinción fundamental:

```text
src/      -> lo que escribimos y versionamos
target/   -> lo que Maven genera
```

`target/` puede contener clases compiladas, recursos copiados, resultados intermedios, informes y el JAR final.

Borrar `target/` no borra el código fuente. Maven puede reconstruirlo. Por eso normalmente no se versiona.

### 1.3 Paquetes Java y carpetas

El package:

```java
package es.mecd.demo.miproyecto.controller;
```

corresponde a una ruta parecida a:

```text
src/main/java/es/mecd/demo/miproyecto/controller/
```

El paquete forma parte del nombre completo de la clase y evita colisiones.

### 1.4 Source sets de Maven

Maven reconoce por convención:

```text
src/main/java
src/main/resources
src/test/java
src/test/resources
```

Producción y test quedan separados desde el principio.

### Pregunta

¿Qué pasaría si colocáramos el controlador en `es.mecd.otro.controller` mientras la clase principal permanece en `es.mecd.demo.miproyecto`?

### Respuesta razonada

Por defecto, el escaneo iniciado por `@SpringBootApplication` no recorrería ese paquete hermano. La clase compilaría, pero Spring podría no registrarla como bean; entonces su endpoint no existiría. Podríamos personalizar el escaneo, pero en un proyecto nuevo es más claro mantener la clase principal en un paquete raíz común.

## Bloque 2 - Archivos de la raíz

### `pom.xml`

Define el proyecto Maven, dependencias y plugins.

### `.gitignore`

Evita versionar salidas generadas y configuración local del IDE. Por ejemplo `target/`, `.idea/` o ficheros temporales.

### Maven Wrapper

`mvnw`, `mvnw.cmd` y `.mvn/wrapper/` permiten ejecutar el build con una versión controlada de Maven.

### `target/`

Es un directorio generado. Contendrá clases compiladas, resultados de test, JAR y otros productos del build. Se puede borrar y regenerar.

### `HELP.md` y otros archivos opcionales de Initializr

Dependiendo de la versión y opciones de Spring Initializr, el proyecto puede incluir documentación auxiliar como `HELP.md`. No forma parte del runtime de Spring Boot y su presencia o ausencia no altera la aplicación. Es una ayuda para la persona que abre el proyecto.

Esto ilustra una distinción que utilizaremos durante todo el curso:

- hay archivos que **construyen o configuran la aplicación**;
- hay archivos que **ayudan al desarrollo**;
- y hay archivos **generados** que no deben versionarse.

No debemos atribuir la misma importancia a todos sólo porque aparezcan en la raíz.

### El ciclo Maven completo y qué directorio modifica

Las fases que verás con más frecuencia forman parte de un ciclo mayor:

```text
validate -> compile -> test -> package -> verify -> install -> deploy
```

- `validate`: comprueba que el proyecto tiene una estructura utilizable;
- `compile`: compila producción hacia `target/classes`;
- `test`: compila y ejecuta tests;
- `package`: genera el artefacto, normalmente un JAR;
- `verify`: ejecuta verificaciones posteriores al empaquetado si hay plugins configurados;
- `install`: copia el artefacto al repositorio Maven local;
- `deploy`: publica el artefacto en un repositorio remoto configurado.

Al pedir una fase, Maven ejecuta las anteriores necesarias. Por eso `package` no significa «sólo comprimir»: antes habrá compilado y ejecutado tests, salvo que se hayan desactivado explícitamente.

`clean` pertenece a otro ciclo y elimina `target`. Es útil para comprobar que una construcción no depende accidentalmente de restos de compilaciones anteriores.

### Pregunta

¿Por qué no deberíamos editar manualmente clases dentro de `target/classes`?

### Respuesta razonada

Porque son productos derivados. En el siguiente `clean` o `compile` pueden desaparecer o ser sustituidos por lo generado desde `src/main`. La fuente de verdad del código es `src`, no la salida del compilador.

## Bloque 3 - `src/main/java` y `src/main/resources`

### Código Java

`src/main/java` contiene código de producción.

Comenzaremos con una organización muy sencilla:

```text
es.mecd.demo.miproyecto
`-- controller
```

Más adelante aparecerán servicios, repositorios, DTO, entidades, seguridad y otras áreas, pero no necesitamos anticiparlas todas en M0.

### Recursos

`src/main/resources` contiene recursos que se incorporan al classpath de la aplicación. El archivo central en este momento es:

```properties
spring.application.name=mi-proyecto
```

`application.properties` permite externalizar configuración: puertos, logs, conexiones, perfiles, seguridad y muchas otras opciones que iremos introduciendo cuando sean necesarias.

### Configuración externalizada

La idea importante es separar **código** de **configuración**. Un puerto, una URL de base de datos o un nivel de log no deberían requerir recompilar lógica de negocio si pueden expresarse como configuración.

### `application.properties`, YAML y perfiles

Spring Boot permite expresar configuración tanto en `application.properties` como en `application.yml`. En este curso utilizaremos principalmente `.properties` para que las primeras prácticas mantengan una sintaxis sencilla, pero debes reconocer ambas posibilidades.

Una propiedad como:

```properties
server.port=8081
```

cambia el puerto sin recompilar el código. Esa separación entre código y configuración se vuelve crítica cuando una misma aplicación debe ejecutarse en desarrollo, test y producción.

Los perfiles permiten especializar valores mediante archivos como:

```text
application-dev.properties
application-test.properties
application-prod.properties
```

No vamos a utilizar todavía perfiles para resolver problemas de negocio; se estudiarán con más profundidad cuando el curso introduzca configuración avanzada. Aquí basta con entender la arquitectura: el código permanece y cambia la configuración activa.

**Advertencia importante:** configuración externalizada no significa que sea correcto guardar secretos en Git. Contraseñas, tokens y claves deben venir de mecanismos apropiados, por ejemplo variables de entorno o gestores de secretos, cuando lleguemos a escenarios que los necesiten.

### Pregunta

¿Qué ventaja tendrá, más adelante, poder utilizar `application-dev.properties` y `application-prod.properties`?

### Respuesta razonada

Permitirá mantener el mismo código y variar configuración por entorno: base de datos, logging, URLs externas o credenciales gestionadas externamente. Así evitamos bifurcar el código sólo porque desarrollo y producción utilicen infraestructuras distintas.

## Bloque 4 - `src/test/java`

El código de test está separado del código de producción. Maven compila y ejecuta ambos con classpaths relacionados pero distintos.

Un test inicial típico es:

```java
@SpringBootTest
class MiProyectoApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

Aunque el método esté vacío, `@SpringBootTest` hace que el test intente crear el contexto de Spring. Si la configuración principal está rota, el test puede fallar antes de ejecutar cualquier lógica de negocio.

Más adelante aprenderemos pruebas mucho más específicas. En M0 nos interesa primero entender que un build serio puede ejecutar pruebas de forma automática.

### Pregunta

¿Por qué el código de producción no debería depender de clases que sólo existen en `src/test/java`?

### Respuesta razonada

Porque el artefacto de producción se construye sin incluir las clases de test como dependencias de runtime. Una dependencia de producción hacia código de test rompería el empaquetado o requeriría convertir una ayuda de test en parte artificial de la aplicación. La dirección correcta es que los tests dependan del código que prueban.


### 4.1 `@SpringBootApplication` como anotación compuesta

Para M0 basta con usarla, pero conviene saber qué concentra conceptualmente:

- configuración Spring;
- auto-configuración;
- escaneo de componentes.

Puedes recordarlo como:

```text
@SpringBootApplication
   ~ configuración + auto-configuración + component scan
```

### 4.2 Por qué importa la ubicación de la clase principal

Si `MiProyectoApplication` está en `es.mecd.demo.miproyecto`, Spring busca componentes en ese paquete y descendientes.

Por eso `es.mecd.demo.miproyecto.controller` funciona de forma natural.

Una clase `@RestController` colocada fuera de ese árbol puede no ser detectada. Este detalle explica muchos 404 aparentemente misteriosos.

## Bloque 5 - Primer controlador REST

Usaremos:

```java
package es.mecd.demo.miproyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaludoController {

    @GetMapping("/hola")
    public String saludar() {
        return construirMensaje("Ministerio de Educación");
    }

    @GetMapping("/adios")
    public String despedir() {
        return "Adiós, Ministerio de Educación";
    }

    private String construirMensaje(String destinatario) {
        return "Hola, " + destinatario;
    }
}
```

### 5.1 `@RestController`

Marca la clase como un componente web cuyos métodos pueden manejar peticiones y escribir directamente el valor de retorno en la respuesta HTTP.

En este momento nuestros métodos devuelven `String`. Más adelante devolveremos DTO y Spring utilizará Jackson para serializarlos a JSON.

### 5.2 `@GetMapping`

Asocia un método Java con peticiones HTTP GET para una ruta.

```java
@GetMapping("/hola")
```

significa: cuando llegue una petición GET a `/hola`, este método es candidato a atenderla.

### 5.3 Flujo de una petición

Para:

```bash
curl http://localhost:8080/hola
```

el flujo simplificado es:

1. `curl` abre una conexión HTTP al puerto 8080.
2. Tomcat recibe la petición.
3. Spring MVC la procesa.
4. El mecanismo de mapeo encuentra `GET /hola`.
5. Spring invoca `SaludoController.saludar()`.
6. El método llama a `construirMensaje(...)`.
7. El `String` devuelto se escribe en la respuesta HTTP.
8. `curl` muestra el cuerpo.

El método privado está aquí también por una razón didáctica: en el siguiente punto podremos observar con un depurador la diferencia entre **Step Over** y **Step Into** sin necesitar todavía DTO, validaciones o servicios de módulos posteriores.

### Pregunta

¿Qué método HTTP y qué ruta tendríamos que usar para obtener la despedida?

### Respuesta razonada

`GET /adios`, porque `@GetMapping("/adios")` asocia esa combinación de método y ruta con `despedir()`. Una petición POST a la misma ruta no es equivalente: el método HTTP forma parte del contrato.

## Ampliación conceptual - Estructura estándar, classpath y empaquetado

La estructura Maven no es sólo una convención estética. Cada árbol participa de manera distinta en el build.

### `src/main/java`

Contiene código de producción. Maven lo compila durante `compile` y sus `.class` terminan formando parte del artefacto.

### `src/main/resources`

Contiene recursos de producción. Maven los copia al classpath del resultado. Por eso `application.properties` queda accesible a Spring Boot sin convertirse en una clase Java.

### `src/test/java`

Contiene código de test. Se compila después del código principal, dispone del classpath de test y no se incluye como código de producción en el JAR final.

### `src/test/resources`

Cumple para los tests el papel que `src/main/resources` cumple para producción. Permite tener configuraciones y datos específicos de pruebas sin contaminar el artefacto final.

### `target`

Es salida generada. Puede contener:

- clases compiladas;
- recursos copiados;
- clases de test;
- informes de Surefire;
- JAR;
- metadatos intermedios.

Debe poder borrarse y regenerarse. Por eso no es un lugar correcto para guardar trabajo manual.

### Qué ocurre en `package`

De forma simplificada:

```text
fuentes -> compile -> tests -> resources -> package -> JAR
```

En un proyecto Spring Boot, el plugin puede reempaquetar el JAR para incluir una estructura ejecutable con dependencias y lanzador. Eso permite:

```bash
java -jar ...
```

sin construir manualmente un classpath con decenas de JAR.

### Pregunta

¿Por qué `target/` se ignora normalmente en Git aunque contenga el JAR que acabamos de construir?

### Respuesta razonada

Porque es un resultado derivable. El repositorio debe guardar las fuentes y la configuración necesarias para reconstruirlo. Versionar artefactos intermedios o binarios generados introduce ruido, conflictos y riesgo de que el binario deje de corresponder al código fuente.

## Ampliación conceptual - Escaneo de componentes y ubicación del paquete raíz

`@SpringBootApplication` combina varias ideas. Una de ellas es el escaneo de componentes a partir del paquete donde vive la clase principal.

Si la clase principal está en:

```text
es.mecd.demo.miproyecto
```

una estructura natural es:

```text
es.mecd.demo.miproyecto.controller
es.mecd.demo.miproyecto.service
es.mecd.demo.miproyecto.repository
es.mecd.demo.miproyecto.config
```

El árbol descendente queda bajo el alcance habitual del component scan.

Si, en cambio, colocamos un controlador en un paquete hermano como:

```text
es.mecd.demo.otro.controller
```

no debemos asumir que será descubierto automáticamente por la aplicación cuya raíz está en `es.mecd.demo.miproyecto`.

Este detalle conecta estructura de paquetes con comportamiento observable: un controlador perfectamente compilado puede no registrar ningún endpoint si Spring no lo descubre.

### Pregunta

¿Por qué este tipo de error puede resultar engañoso para un principiante?

### Respuesta razonada

Porque Java compila la clase y el IDE no muestra necesariamente ningún error. El problema aparece en runtime: el bean no existe en el contexto y la ruta devuelve 404. Es un buen ejemplo de diferencia entre corrección de compilación y configuración del framework.

## Resumen del Punto 0.3

- Maven separa producción, recursos y tests mediante convenciones claras.
- La clase principal debe situarse en una raíz de paquetes adecuada.
- `@SpringBootApplication` inicia configuración, auto-configuración y escaneo.
- Los recursos se integran en el classpath.
- Un controlador REST convierte una ruta HTTP en una llamada a código Java.
- La estructura no es decoración: condiciona compilación, escaneo, tests y empaquetado.

---

