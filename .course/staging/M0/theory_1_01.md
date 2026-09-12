---
title: "Módulo 0 - Teoría"
author: "Curso Spring Boot 2026"
lang: es-ES
toc-title: "Índice"
---

# Módulo 0 - Preparación del entorno y primer proyecto Spring Boot

## Propósito del módulo

Antes de escribir una API REST, configurar una base de datos o proteger un endpoint, necesitamos comprender y verificar el entorno sobre el que se ejecutará todo el curso. El objetivo de este módulo no es memorizar botones de un IDE: es entender qué piezas intervienen cuando escribimos, compilamos, ejecutamos, probamos y depuramos una aplicación Java y, después, una aplicación Spring Boot.

Al terminar el módulo deberías poder responder con seguridad a preguntas como estas: ¿qué diferencia hay entre JDK, JRE y JVM?, ¿qué hace Maven que no hace Java?, ¿qué contiene realmente un proyecto generado por Spring Initializr?, ¿por qué `@SpringBootApplication` es tan importante?, ¿qué ocurre desde que ejecutamos `main` hasta que Tomcat empieza a escuchar en el puerto 8080?, ¿cómo sabemos si un fallo está en Java, Maven, el IDE, Spring o nuestra propia aplicación?

### Baseline técnico de esta edición

Este curso utiliza **Java 17**, **Maven 3.9.x** y **Spring Boot 3.5.16** como baseline reproducible. Spring Boot 3.5.x mantiene Java 17 como versión mínima, lo que permite conservar el enfoque del material de partida y trabajar con una rama moderna de Spring Boot 3 durante todo el itinerario. El alumno puede tener instalada una JDK superior, pero los ejemplos y el proyecto del curso se compilan con nivel de lenguaje Java 17.

La elección de una versión concreta no significa que sea la única válida. Significa que todos los ejemplos, comandos y resultados del curso comparten un punto de referencia estable. Cuando una tecnología cambia, es más fácil aprender si primero dominamos un entorno conocido y después estudiamos las diferencias.

---

# Punto 0.1 - Instalación y verificación del entorno: JDK, Maven e IDE

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar con precisión qué son la JDK, el JRE y la JVM y cómo se relacionan.
2. Explicar para qué sirve Maven y qué problemas evita.
3. Explicar qué aporta un IDE y qué tareas siguen dependiendo realmente de Java y Maven.
4. Entender el papel de `JAVA_HOME` y `PATH`.
5. Interpretar `java -version`, `javac -version` y `mvn -version`.
6. Diagnosticar configuraciones mezcladas o incompletas.
7. Compilar y ejecutar mentalmente el ciclo fuente `.java` -> bytecode `.class` -> JVM.

## Bloque 1 - Las tres piezas del entorno Java

### 1.1 La JDK: el kit de desarrollo

**JDK** significa *Java Development Kit*. No es un único programa, sino un conjunto de herramientas, librerías y archivos necesarios para desarrollar software Java.

Entre sus componentes principales se encuentran:

- `javac`: compilador de Java. Traduce código fuente `.java` a bytecode `.class`.
- `java`: lanzador de la máquina virtual. Carga clases y ejecuta su bytecode.
- `jar`: crea y manipula archivos JAR.
- `javadoc`: genera documentación a partir de comentarios y declaraciones del código.
- `jshell`: entorno interactivo para evaluar expresiones y pequeños fragmentos Java.
- `jdb`: depurador de línea de comandos.
- La biblioteca estándar: colecciones, entrada/salida, fechas, redes, concurrencia y muchas otras APIs que forman parte de la plataforma Java.

Un error habitual al empezar consiste en pensar que “Java” es una sola aplicación. En realidad, cuando escribes `javac`, `java` o `jar`, estás ejecutando herramientas distintas que pertenecen a la misma instalación de la JDK.

### 1.2 JVM, JRE y JDK

Los tres conceptos forman capas:

| Concepto | Qué contiene | Para qué sirve |
|---|---|---|
| JVM | Máquina virtual de Java | Ejecuta bytecode |
| JRE | JVM + librerías necesarias para ejecutar | Ejecuta aplicaciones ya compiladas |
| JDK | JRE + compilador y herramientas de desarrollo | Desarrolla, compila, ejecuta, empaqueta y depura |

La relación conceptual se puede expresar como:

`JDK` contiene lo necesario para desarrollar y ejecutar; el entorno de ejecución contiene la JVM y las librerías que la aplicación necesita para funcionar.

En las distribuciones modernas de Java ya no siempre encontrarás un “JRE descargable” separado como ocurría en versiones antiguas. La distinción sigue siendo muy útil conceptualmente: **ejecutar** no es lo mismo que **desarrollar**. Para este curso instalaremos una JDK completa.

### 1.3 Del código fuente a la ejecución

Supón que tenemos:

```java
public class Hola {
    public static void main(String[] args) {
        System.out.println("Hola");
    }
}
```

El ciclo básico es:

1. El programador escribe `Hola.java`.
2. `javac Hola.java` analiza el código, comprueba sintaxis y tipos y genera `Hola.class`.
3. `Hola.class` contiene bytecode, no código máquina específico de Windows, Linux o macOS.
4. `java Hola` arranca la JVM, carga la clase y busca `public static void main(String[] args)`.
5. La JVM interpreta y/o compila dinámicamente el bytecode para la plataforma concreta.

Este modelo explica una de las ideas históricas de Java: compilar una vez a bytecode y ejecutar sobre una JVM compatible en distintas plataformas.


### 1.4 Las herramientas que realmente forman la JDK

Decir que “la JDK sirve para programar en Java” es correcto, pero demasiado genérico para diagnosticar problemas. Conviene asociar cada herramienta con una fase concreta del trabajo:

| Herramienta | Entrada | Salida / efecto | Cuándo la usarás |
|---|---|---|---|
| `javac` | `.java` | `.class` | compilación |
| `java` | clase o JAR | proceso JVM | ejecución |
| `jar` | clases y recursos | `.jar` | empaquetado / inspección |
| `javadoc` | fuentes documentadas | HTML | documentación API |
| `jshell` | expresiones Java | resultado interactivo | experimentación rápida |
| `jdb` | proceso/clases | sesión de depuración | diagnóstico de bajo nivel |
| `jps` | procesos Java | PID y nombre | localizar JVM activas |
| `jstack` | PID | stack traces de hilos | bloqueos y cuelgues |
| `jcmd` | PID + comando | información diagnóstica | inspección de una JVM |
| `keytool` | almacenes/certificados | claves/certificados | HTTPS y seguridad |

No necesitas memorizar todas estas herramientas en M0, pero sí entender que una instalación de JDK es mucho más que el ejecutable `java`.

Una comprobación sencilla es localizar físicamente los ejecutables.

Windows PowerShell:

```powershell
Get-Command java
Get-Command javac
```

Windows CMD:

```cmd
where java
where javac
```

Linux/macOS:

```bash
which java
which javac
```

El resultado debe apuntar a la instalación que quieres utilizar. Si `java -version` dice 17 pero el primer ejecutable resuelto pertenece a una JDK anterior, hay una incoherencia que puede reaparecer al abrir otra terminal o al cambiar de shell.

### 1.5 Qué es realmente el bytecode

`javac` no genera código máquina específico para tu procesador. Genera **bytecode Java**, almacenado normalmente en archivos `.class`.

Ese bytecode puede ejecutarse en sistemas operativos distintos siempre que exista una JVM compatible con la versión del bytecode y con las dependencias necesarias.

Cuando aparece:

```text
UnsupportedClassVersionError
```

normalmente significa que el `.class` fue generado para una versión de Java más nueva que la JVM usada para ejecutarlo.

```text
Hola.java
   |
   | javac
   v
Hola.class  ----->  JVM  ----->  código nativo ejecutado por la CPU
```

La JVM puede interpretar bytecode y compilar dinámicamente partes frecuentes mediante JIT. Por eso describir Java simplemente como “interpretado” es una simplificación.

### 1.6 Classpath: dónde busca Java las clases

Cuando ejecutas:

```bash
java -cp src HolaMinisterio
```

`-cp` significa `--class-path`: la lista de lugares donde la JVM busca clases y recursos.

En el ejercicio mínimo puede ser una sola carpeta. En una aplicación Spring Boot el classpath contiene muchas clases propias y dependencias; Maven y el plugin de Spring Boot lo preparan para no tener que gestionar manualmente cientos de JAR.

Errores como `ClassNotFoundException` o `NoClassDefFoundError` suelen estar relacionados con lo que estaba —o no— disponible en ese classpath.

### 1.7 Biblioteca estándar frente a dependencias externas

`String`, `LocalDate`, `ArrayList` o `HttpClient` forman parte de Java o de sus módulos estándar.

`SpringApplication`, `RestController` u `ObjectMapper` pertenecen a bibliotecas externas y llegan mediante dependencias.

Esta diferencia explica por qué puedes usar `LocalDate` sin añadir nada al POM pero necesitas Spring Web para `@RestController`.

### Pregunta

¿Podrías compilar y ejecutar Java sólo con la JDK, sin Maven ni IDE? ¿Qué perderías en cada caso?

### Respuesta razonada

Sí. La JDK basta para escribir un fichero con cualquier editor, compilarlo con `javac` y ejecutarlo con `java`. Sin Maven perderías la automatización de dependencias, el ciclo de construcción, la ejecución organizada de tests y el empaquetado reproducible. Sin IDE perderías ayudas de productividad como autocompletado, navegación, refactorizaciones, depuración visual y gestión cómoda del proyecto. Ni Maven ni el IDE sustituyen a la JDK: se apoyan en ella.

## Bloque 2 - Maven: construcción y dependencias

### 2.1 El problema que Maven resuelve

Una aplicación real rara vez usa sólo la biblioteca estándar. Necesitará frameworks, drivers, clientes HTTP, librerías de test, utilidades, etc. Si gestionáramos todo manualmente tendríamos que:

1. localizar cada JAR;
2. elegir una versión compatible;
3. descargarlo;
4. incluirlo en el classpath;
5. repetir el proceso para sus dependencias transitivas;
6. conseguir que otro desarrollador reproduzca exactamente el mismo entorno.

Maven cambia el enfoque: en vez de copiar bibliotecas manualmente, **declaramos** qué necesita el proyecto y Maven resuelve cómo obtenerlo.

El archivo central es `pom.xml`, *Project Object Model*.

### 2.2 Coordenadas Maven

Un artefacto Maven se identifica principalmente mediante:

- `groupId`: organización o espacio de nombres;
- `artifactId`: nombre del artefacto;
- `version`: versión concreta.

Por ejemplo:

```text
org.springframework.boot:spring-boot-starter-web:3.5.16
```

Estas coordenadas permiten a Maven localizar artefactos en repositorios como Maven Central y almacenarlos en la caché local, normalmente bajo `~/.m2/repository`.

### 2.3 Dependencias transitivas

Si declaras una dependencia de alto nivel, ésta puede depender de otras. Maven construye un grafo y descarga las necesarias. Gracias a ello, cuando añadamos `spring-boot-starter-web` no tendremos que enumerar manualmente todas las bibliotecas que Spring MVC, Jackson y el servidor embebido necesitan.

Esto no significa que “Maven adivine”. Sigue reglas deterministas de resolución y utiliza la información publicada en los POM de los artefactos.

### 2.4 Ciclo de vida

Las fases de Maven están ordenadas. Algunas importantes son:

- `validate`: comprueba que el proyecto tiene la estructura básica esperada;
- `compile`: compila código de producción;
- `test`: ejecuta tests;
- `package`: construye el artefacto, por ejemplo un JAR;
- `verify`: ejecuta verificaciones adicionales;
- `install`: instala el artefacto en el repositorio local;
- `deploy`: publica el artefacto en un repositorio remoto configurado.

Cuando ejecutas:

```bash
mvn package
```

no se ejecuta sólo `package`: Maven recorre las fases anteriores necesarias.

### 2.5 Maven Wrapper

Un proyecto puede incluir `mvnw`, `mvnw.cmd` y `.mvn/wrapper/`. El objetivo es que el proyecto pueda fijar una distribución concreta de Maven y que el desarrollador no dependa exclusivamente de una instalación global.

En Linux/macOS se utiliza habitualmente:

```bash
./mvnw test
```

En Windows:

```cmd
mvnw.cmd test
```

El Wrapper mejora la reproducibilidad: dos desarrolladores y un servidor de integración continua pueden ejecutar el mismo proyecto con la misma línea de Maven.


### 2.6 Repositorio local: `~/.m2/repository`

Maven mantiene una caché local de dependencias. En Linux/macOS suele estar bajo `~/.m2/repository`; en Windows, dentro de la carpeta `.m2` del usuario.

Por eso la primera compilación puede tardar bastante más que las siguientes.

Si declaras `spring-boot-starter-web`, Maven resuelve esa coordenada y recorre su grafo de dependencias transitivas.

No conviene borrar toda `.m2` como reacción automática ante cualquier error. Es mejor identificar primero qué artefacto o resolución está fallando.

### 2.7 Fases, goals y plugins

Tres conceptos distintos:

**Fase.** Un punto del ciclo de vida (`compile`, `test`, `package`, `verify`, `install`).

**Plugin.** Un conjunto de tareas.

**Goal.** Una tarea concreta de un plugin.

```bash
./mvnw spring-boot:run
```

invoca un goal del plugin de Spring Boot.

```bash
./mvnw package
```

invoca una fase del ciclo de vida.

Comprender esta diferencia evita pensar que todos los comandos Maven son “fases”.

### 2.8 POM efectivo y versiones gestionadas

El `pom.xml` que escribes se combina con configuración heredada y gestión de dependencias.

Por eso algunas dependencias Spring Boot no necesitan declarar una `<version>` explícita.

Puedes ver el POM resultante con:

```bash
./mvnw help:effective-pom
```

La salida es extensa, pero demuestra que Maven está trabajando con más configuración de la que aparece escrita directamente en tu archivo.

### 2.9 Scope de dependencias

Los scopes que más reconocerás son:

- `compile`: disponible al compilar y ejecutar;
- `runtime`: necesaria principalmente en ejecución;
- `test`: sólo para tests;
- `provided`: disponible para compilar, pero aportada por el entorno.

`spring-boot-starter-test` debe permanecer en `test`, porque no forma parte del comportamiento productivo de la aplicación.

### 2.10 Wrapper frente a Maven global

El Maven Wrapper permite que el proyecto use una distribución conocida de Maven sin depender de la versión global instalada por cada desarrollador.
