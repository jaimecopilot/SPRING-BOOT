# Práctica 0.3 - Entender la estructura y crear los primeros endpoints

## Paso 1 - Abrir la raíz del proyecto

Confirma que estás en la carpeta que contiene:

```text
pom.xml
mvnw
mvnw.cmd
src/
```

## Paso 2 - Explorar la estructura completa

```text
src/main/java
src/main/resources
src/test/java
```

Después ejecuta una construcción para observar `target/`:

Linux/macOS:

```bash
./mvnw package
```

Windows:

```cmd
mvnw.cmd package
```

Ahora `target/` existirá.


### Recorre cada elemento y clasifícalo

No te limites a mirar el árbol. Para cada ruta, responde “fuente”, “recurso”, “test” o “generado”.

```text
pom.xml
src/main/java/
src/main/resources/
src/test/java/
target/
.mvn/
mvnw
mvnw.cmd
```

**`pom.xml`**: definición Maven versionada.

**`src/main/java`**: código Java que formará parte de la aplicación.

**`src/main/resources`**: configuración y recursos que estarán en el classpath.

**`src/test/java`**: código de pruebas.

**`target`**: salida generada. Si todavía no existe, aparecerá al compilar.

**`.mvn` + wrappers**: infraestructura para ejecutar Maven de forma reproducible.

### Experimento controlado con `target`

Ejecuta:

Linux/macOS:

```bash
./mvnw package
```

Windows:

```cmd
mvnw.cmd package
```

Observa que aparece `target/`.

Después ejecuta:

```bash
./mvnw clean
```

o:

```cmd
mvnw.cmd clean
```

`target/` se elimina.

Tu código de `src/` permanece intacto. Esa es la diferencia entre fuente y producto del build.

### Inventario razonado de la raíz

Al comparar tu proyecto con el árbol de la guía pueden existir pequeñas diferencias según la versión de Initializr. Por ejemplo, algunas generaciones incluyen `HELP.md` y otras pueden no hacerlo. No conviertas una diferencia documental en un falso error de runtime.

Clasifica cada elemento en una de estas categorías:

| Categoría | Ejemplos | ¿Se versiona normalmente? |
|---|---|---:|
| descriptor/build | `pom.xml`, `.mvn/`, `mvnw`, `mvnw.cmd` | sí |
| producción | `src/main/java`, `src/main/resources` | sí |
| tests | `src/test/java` | sí |
| documentación | `README.md`, `HELP.md` si existe | sí |
| artefactos generados | `target/` | no |
| configuración local del IDE | depende del IDE/equipo | sólo si el equipo lo decide y aporta valor común |

Esta clasificación importa porque el repositorio debe contener lo necesario para reconstruir el proyecto, no los resultados generados por una máquina concreta.


### Pregunta

¿Qué diferencia esencial hay entre `src/` y `target/`?

### Respuesta razonada

`src/` contiene fuentes que mantenemos. `target/` contiene productos generados por Maven: clases, informes y artefactos. Podemos borrar `target/` y reconstruirlo; no deberíamos perder trabajo humano.

## Paso 3 - Analizar `pom.xml`

### No leas el POM como XML genérico: léelo por responsabilidades

Haz una segunda pasada y marca físicamente estas cinco zonas:

1. **`parent`**: de dónde hereda convenciones Spring Boot;
2. **coordenadas del proyecto**: `groupId`, `artifactId`, `version`;
3. **`properties`**: nivel Java y otras decisiones globales;
4. **`dependencies`**: capacidades que se incorporan;
5. **`build/plugins`**: herramientas que participan en la construcción.

Después ejecuta, sin modificar el archivo:

```bash
./mvnw dependency:tree
```

En Windows:

```cmd
mvnw.cmd dependency:tree
```

**Qué debes observar:** Spring Web no es un único JAR. Bajo el starter aparecerán dependencias transitivas. El árbol ayuda a responder «¿por qué está esta librería en mi proyecto?» sin buscar manualmente en carpetas.

Como ejercicio de diagnóstico adicional puedes generar el POM efectivo:

```bash
./mvnw help:effective-pom
```

La salida es larga. No la copies en tu `pom.xml`. Úsala para comprobar que muchas versiones y configuraciones llegan por herencia/gestión aunque no estén escritas explícitamente en nuestro archivo.

**Error frecuente:** pensar que una dependencia sin `<version>` está incompleta. En un proyecto Spring Boot con su `parent`, muchas versiones están gestionadas deliberadamente.


Abre el fichero completo y localiza:

1. `<parent>`;
2. coordenadas del proyecto;
3. `<properties>`;
4. `<dependencies>`;
5. `<build><plugins>`.

El POM final de M0 es:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.16</version>
        <relativePath/>
    </parent>

    <groupId>es.mecd.demo</groupId>
    <artifactId>mi-proyecto</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>mi-proyecto</name>
    <description>Proyecto acumulativo del curso Spring Boot 2026</description>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### Pregunta

¿Por qué no aparece una versión explícita junto a `spring-boot-starter-web`?

### Respuesta razonada

Porque el parent de Spring Boot aporta gestión de dependencias. El proyecto hereda un conjunto de versiones compatible. Añadir versiones manualmente sin necesidad puede romper esa coherencia.

## Paso 4 - Analizar la clase principal

Abre:

`src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java`

Debe ser:

```java
package es.mecd.demo.miproyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiProyectoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiProyectoApplication.class, args);
    }
}
```

### Qué debes entender

`@SpringBootApplication` es una anotación compuesta que activa la configuración principal de una aplicación Spring Boot, incluyendo auto-configuración y escaneo de componentes según sus convenciones.

`SpringApplication.run(...)` inicia el proceso de creación del contexto.

La ubicación del package `es.mecd.demo.miproyecto` es deliberada: los futuros paquetes estarán debajo.

### Pregunta

¿Por qué no colocamos `MiProyectoApplication` dentro de `...miproyecto.app.inicio`?

### Respuesta razonada

Podríamos, pero reduciríamos el árbol de paquetes que se escanea de forma natural. Situarla en una raíz común simplifica el descubrimiento de controllers, services, repositories y configuración que añadiremos después.

## Paso 5 - Analizar `application.properties`

Abre:

`src/main/resources/application.properties`

Contenido:

```properties
spring.application.name=mi-proyecto
```

El puerto no aparece porque estamos utilizando el valor por defecto 8080.

Para experimentar **temporalmente** podrías añadir:

```properties
server.port=8081
```

Arrancar, comprobar `http://localhost:8081`, parar y **eliminar después esa línea** para que el curso continúe en 8080.

### Pregunta

¿Por qué indicamos explícitamente que este cambio de puerto es temporal?

### Respuesta razonada

Porque los pasos posteriores y sus comandos esperan 8080. Un ejercicio puede alterar el estado para observar un concepto, pero debe dejar claro cuándo volver al estado base para no crear errores inexplicables más adelante.

## Paso 6 - Crear el paquete `controller`

Dentro de:

```text
src/main/java/es/mecd/demo/miproyecto/
```

crea:

```text
controller/
```

El package resultante es:

```java
package es.mecd.demo.miproyecto.controller;
```

Al estar debajo del package de la clase principal, Spring lo escaneará.

## Paso 7 - Crear `SaludoController`

Crea:

`src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java`

con este contenido completo:

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


### Lee el archivo de arriba abajo

La versión final de este paso es:

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

En este punto todavía no estamos diseñando una arquitectura por capas. El objetivo es comprender el camino mínimo:

```text
HTTP GET /hola
   -> Spring MVC localiza @GetMapping
   -> invoca saludar()
   -> devuelve String
   -> Spring escribe la respuesta HTTP
```

### Qué hace `@RestController`

Indica a Spring que esta clase participa en la capa web y que el valor devuelto por sus métodos se escribe en la respuesta, en lugar de interpretarse como nombre de una vista.

### Qué hace `@GetMapping`

Asocia una petición HTTP GET con una ruta.

```java
@GetMapping("/hola")
```

no significa “ejecutar este método al arrancar”. El método se ejecuta cuando llega una petición GET compatible.

### Qué no hemos añadido todavía

No hay servicio, repositorio, DTO, validación ni base de datos.

Eso es deliberado. Si añadimos conceptos antes de enseñarlos, el proyecto deja de ser trazable pedagógicamente.

### Explicación completa

`@RestController` hace que Spring registre esta clase como componente web y trate el valor de retorno de sus métodos de handler como cuerpo de respuesta.

`@GetMapping("/hola")` registra un mapping GET.

`saludar()` no recibe argumentos todavía. En módulos posteriores aprenderemos path variables, query parameters, cabeceras y cuerpos JSON.

`construirMensaje(...)` es un método Java privado. No es un endpoint. Spring no lo invoca directamente. Lo añadimos porque separa una pequeña operación y nos permitirá practicar `Step Into` de manera real en 0.4.

`despedir()` demuestra que una misma clase puede registrar más de una ruta.

### Errores frecuentes

**Falta `@RestController`.** La clase puede compilar, pero Spring no la registra como controlador.

**Falta `@GetMapping`.** La clase existe como bean, pero ese método no tiene ruta HTTP.

**Package fuera del escaneo.** El bean puede no descubrirse.

**Import incorrecto.** Debe ser `org.springframework.web.bind.annotation...`.

### Pregunta

¿Por qué `construirMensaje` no puede visitarse en el navegador?

### Respuesta razonada

Porque ser método de una clase controller no convierte automáticamente todos sus métodos en endpoints. Sólo `saludar` y `despedir` tienen anotaciones de mapping. `construirMensaje` es una implementación interna normal de Java.

## Paso 8 - Arrancar la aplicación

Desde terminal:

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

Espera a la finalización del arranque.

## Paso 9 - Probar `/hola` en navegador

Abre:

```text
http://localhost:8080/hola
```

Resultado esperado:

```text
Hola, Ministerio de Educación
```

Si obtienes 404, comprueba:

- clase bajo el package correcto;
- `@RestController`;
- `@GetMapping("/hola")`;
- aplicación reiniciada/recargada;
- estás usando el puerto correcto.

## Paso 10 - Probar con `curl`

```bash
curl http://localhost:8080/hola
```

Después:

```bash
curl -i http://localhost:8080/hola
```

Observa que ahora aparecen status y cabeceras, además del cuerpo.

Prueba también:

```bash
curl -i http://localhost:8080/adios
```

### Pregunta

¿Qué información adicional ves con `-i`?

### Respuesta razonada

Ves las cabeceras de la respuesta y la línea de estado HTTP. Esto permite distinguir el cuerpo de la semántica HTTP que lo acompaña, algo esencial cuando más adelante trabajemos con 201, 204, 400, 404 y otros estados.

## Paso 11 - Diagnóstico de estructura/endpoints

| Síntoma | Posible causa | Comprobación |
|---|---|---|
| 404 en `/hola` | mapping no registrado | package/anotaciones/logs |
| no compila `GetMapping` | dependencia/import | POM y Maven |
| 8080 no abre | app no arrancó | final de logs |
| responde en 8081 | quedó cambio temporal | revisar properties |
| DevTools no recarga | IDE/build no detecta cambios | recompilar/reiniciar |
| `/hola` bien, `/adios` 404 | falta segundo mapping | revisar método |

## Paso 12 - Reto resuelto: consolidar `/adios`

Si has seguido el código completo del Paso 7, `/adios` ya está incorporado como estado final. El reto consiste en entender la modificación como evolución del controlador.

La pieza añadida es:
