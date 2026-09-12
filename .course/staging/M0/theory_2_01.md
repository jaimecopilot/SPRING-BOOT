# Punto 0.2 - Creación del proyecto con Spring Initializr

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar qué es Spring Initializr y qué problema resuelve.
2. Interpretar cada campo importante del formulario.
3. Entender la estructura esencial de un `pom.xml` Spring Boot.
4. Distinguir dependencias directas y transitivas.
5. Importar un proyecto Maven correctamente en un IDE.
6. Entender por qué una aplicación puede arrancar bien y responder 404 en `/`.
7. Explicar el papel del Maven Wrapper y del plugin de Spring Boot.

## Bloque 1 - Qué es Spring Initializr

Spring Initializr es un servicio que genera el esqueleto inicial de un proyecto Spring. Puede utilizarse desde su interfaz web, desde integraciones de IDE y mediante API.

No genera la aplicación de negocio. Genera **la infraestructura inicial correcta**: estructura Maven/Gradle, metadatos, dependencias elegidas, clase principal, test básico, configuración vacía o mínima y Wrapper.

Sin una herramienta así tendríamos que crear a mano:

- `src/main/java`;
- `src/main/resources`;
- `src/test/java`;
- el POM;
- el plugin de Spring Boot;
- la clase principal;
- scripts Wrapper;
- `.gitignore`;
- configuración inicial.

Nada de eso es imposible, pero al principio distraería de los conceptos que queremos aprender.

### Tres formas de utilizarlo

**Web.** Entrar en `start.spring.io`, elegir opciones y descargar un ZIP.

**IDE.** Muchos IDEs incluyen asistentes que llaman al servicio o reproducen sus opciones.

**API/automatización.** Initializr dispone de endpoints que permiten generar proyectos desde herramientas o scripts. Es útil en automatización, aunque no necesitamos dominarlo para comenzar.

### Pregunta

¿Por qué un generador como Spring Initializr mejora la reproducibilidad y no sólo la comodidad?

### Respuesta razonada

Porque convierte una serie de decisiones implícitas y manuales en un conjunto de opciones declaradas: versión de Spring Boot, Java, sistema de construcción, coordenadas y dependencias. El resultado puede regenerarse o revisarse con mucha más facilidad que una estructura montada manualmente sin registro de sus decisiones.

## Bloque 2 - El formulario de Initializr

### 2.1 Project

Elegiremos **Maven**. Gradle también es una excelente herramienta, pero mezclar dos sistemas de construcción en un curso introductorio añadiría variabilidad innecesaria.

### 2.2 Language

Elegiremos **Java**. Spring Boot también admite Kotlin y Groovy, pero el itinerario está diseñado sobre Java.

### 2.3 Spring Boot

Para esta edición fijamos `3.5.16`. Es importante evitar accidentalmente versiones `SNAPSHOT` o `M` (milestone) si lo que buscamos es una base estable y reproducible.

### 2.4 Project Metadata

Usaremos:

```text
Group:       es.mecd.demo
Artifact:    mi-proyecto
Name:        mi-proyecto
Description: Proyecto acumulativo del curso Spring Boot 2026
Package:     es.mecd.demo.miproyecto
Packaging:   Jar
Java:        17
```

**Group** suele representar el espacio de nombres de la organización.

**Artifact** identifica el artefacto dentro del grupo y normalmente coincide con el directorio/proyecto.

**Name** es el nombre legible del proyecto.

**Package name** será la raíz de los paquetes Java y es importante para el escaneo de componentes.

**Packaging JAR** es apropiado para una aplicación Spring Boot autocontenida con servidor embebido.

### 2.5 Dependencias iniciales

Comenzamos con **Spring Web**. La regla pedagógica es deliberada: añadir sólo lo que podemos explicar y utilizar.

Spring Web aporta la infraestructura necesaria para construir aplicaciones web Spring MVC y, mediante el starter, incorpora el servidor embebido y otras dependencias coherentes.

El proyecto también incluirá `spring-boot-starter-test` como dependencia de test.


### 2.6 Cómo leer el formulario como decisiones técnicas

Los campos de Initializr no son simples datos administrativos.

**Group.** Suele representar organización o dominio invertido, por ejemplo `es.mecd.demo`.

**Artifact.** Identifica el artefacto Maven, por ejemplo `mi-proyecto`.

**Name.** Nombre humano del proyecto.

**Description.** Descripción inicial.

**Package name.** Paquete Java base. Normalmente allí se crea la clase principal y desde ese paquete Spring empieza el escaneo de componentes.

**Packaging.** Usamos JAR. El artefacto final podrá ejecutarse con `java -jar`.

**Java.** Elegimos 17 para mantener un baseline homogéneo.

Una decisión aparentemente pequeña como el package base afecta a la estructura que construiremos en los siguientes módulos.

### 2.7 Qué es un starter

Un *starter* expresa una capacidad de alto nivel.

Seleccionar **Spring Web** añade `spring-boot-starter-web`, que arrastra un conjunto coherente de dependencias para aplicaciones web.

La idea es declarar intención:

> “quiero una aplicación web”

en lugar de mantener manualmente una lista de bibliotecas y versiones compatibles.

### 2.8 Qué contiene el ZIP generado

Initializr produce un proyecto, no una carpeta llena de todas las dependencias binarias.

El ZIP incluye estructura, POM, wrapper, clase principal, configuración y test inicial. Maven descargará las dependencias cuando el proyecto se importe o compile.

Por eso el ZIP puede ser pequeño y la primera sincronización Maven tardar más.

### 2.9 Tres maneras de llegar al mismo proyecto

Spring Initializr no es únicamente una página web. El servicio genera la misma clase de proyecto desde distintos clientes.

**Desde la web**, completas el formulario y descargas un ZIP. Es la opción más transparente cuando estás aprendiendo porque puedes ver todas las decisiones.

**Desde un IDE**, IntelliJ, Eclipse con Spring Tools o VS Code pueden presentar un asistente propio. El IDE envía las opciones al servicio de Initializr y abre el proyecto resultante. Es cómodo, pero es importante entender que el IDE no está inventando una estructura diferente.

**Desde una petición HTTP**, Initializr expone endpoints que permiten automatizar la generación. No necesitamos depender de esa forma en el curso, pero entender que existe ayuda a separar el concepto de la interfaz gráfica: Spring Initializr es un servicio de generación de proyectos, no «una pantalla con botones».

La consecuencia pedagógica es importante. Si sabes leer un `pom.xml` y la estructura generada, podrás reconocer un proyecto aunque haya sido creado desde otra interfaz.

### 2.10 Qué significa cada dato de Project Metadata

Los campos del formulario no son decoración. Se convierten en nombres que después aparecen en Maven, Java y el sistema de archivos.

| Campo | Ejemplo del curso | Efecto principal |
|---|---|---|
| Group | `es.mecd.demo` | identidad organizativa y base habitual del paquete |
| Artifact | `mi-proyecto` | nombre Maven del artefacto y del JAR |
| Name | `mi-proyecto` | nombre legible del proyecto |
| Description | texto descriptivo | metadato humano |
| Package name | `es.mecd.demo.miproyecto` | paquete de la clase principal |
| Packaging | `Jar` | tipo de artefacto que construirá Maven |
| Java | `17` | nivel de lenguaje/bytecode esperado |

Hay dos errores frecuentes que parecen pequeños pero se arrastran durante todo el proyecto:

1. elegir un `package` distinto y después copiar código que presupone otro paquete;
2. cambiar `artifactId` y no darse cuenta de que cambia también el nombre del JAR final.

### 2.11 Dependencias: empezar pequeño es una decisión deliberada

En el primer proyecto incorporamos sólo lo que necesitamos. `Spring Web` aporta Spring MVC, el servidor embebido y la infraestructura necesaria para exponer endpoints HTTP. `Spring Boot Starter Test` llega normalmente como dependencia de test en el proyecto generado.

No añadimos JPA, Security, Validation, Actuator o bases de datos «por si acaso». Cada starter adicional:

- amplía el classpath;
- puede activar auto-configuraciones;
- añade conceptos que el alumno todavía no necesita;
- puede alterar los logs y el comportamiento de arranque.

Un proyecto pequeño hace visibles las relaciones causa-efecto. Cuando en módulos posteriores añadamos una dependencia, podremos observar qué capacidad nueva aparece por esa decisión.

### Pregunta

¿Por qué no conviene marcar diez starters “por si acaso” al crear el primer proyecto?

### Respuesta razonada

Porque cada starter modifica el classpath y puede activar auto-configuración adicional. Si añadimos tecnologías que todavía no comprendemos, aparecen beans, logs, configuración y posibles errores sin contexto. Empezar con el conjunto mínimo hace que cada incorporación posterior tenga una causa pedagógica clara.

## Bloque 3 - El `pom.xml`

Un POM de Spring Boot puede incluir:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
    <relativePath/>
</parent>
```

El parent proporciona gestión de versiones y configuración coherente para muchos plugins y dependencias del ecosistema.

Las coordenadas del proyecto:

```xml
<groupId>es.mecd.demo</groupId>
<artifactId>mi-proyecto</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

La versión de Java:

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

Dependencias:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Y el plugin:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

El plugin permite, entre otras tareas, ejecutar la aplicación desde Maven y reempaquetar el JAR para que pueda arrancarse con `java -jar`.


### 3.1 La sección `parent`

Una declaración típica de esta edición es:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.16</version>
    <relativePath/>
</parent>
```

El parent aporta convenciones y gestión de versiones.

### 3.2 Coordenadas del proyecto

```xml
<groupId>es.mecd.demo</groupId>
<artifactId>mi-proyecto</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

`SNAPSHOT` identifica una versión de desarrollo que todavía puede cambiar.

### 3.3 Nivel Java

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

Esto declara qué nivel Java espera el proyecto. No instala la JDK ni corrige una JVM antigua.

### 3.4 Dependencias iniciales

`spring-boot-starter-web` aporta la capacidad web.

`spring-boot-starter-test` aporta herramientas de test y queda limitado al scope `test`.

### 3.5 Plugin Spring Boot

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

Permite, entre otras cosas, crear el JAR ejecutable y usar el goal `spring-boot:run`.

El POM es la receta reproducible de construcción del proyecto.

### 3.6 Un `pom.xml` completo como mapa del proyecto

Un POM generado por Initializr para nuestro primer proyecto tiene una forma parecida a ésta. La versión exacta puede variar, pero la función de cada bloque permanece estable:

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
    <description>Primer proyecto Spring Boot del curso</description>

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

No memorices el XML. Aprende a localizar zonas:

- **parent**: gestión y convenciones heredadas de Spring Boot;
- **coordenadas**: quién es nuestro artefacto;
- **properties**: decisiones globales como Java 17;
- **dependencies**: qué necesita la aplicación para compilar/ejecutar/testear;
- **build/plugins**: cómo participa Maven en la construcción.

La ausencia de `<version>` en muchos starters no significa que Maven «adivine». La versión queda gestionada por el ecosistema de dependencias de Spring Boot. Esa gestión evita que cada alumno combine versiones incompatibles de Spring Framework, Jackson, Tomcat y otras librerías.

### 3.7 Cómo investigar el POM sin modificarlo

Dos comandos son especialmente útiles cuando algo no encaja:

```bash
./mvnw dependency:tree
./mvnw help:effective-pom
```

`dependency:tree` muestra la jerarquía de dependencias, incluidas las transitivas. `help:effective-pom` muestra el POM efectivo después de aplicar herencia, propiedades y configuración. El segundo puede ser muy largo; se utiliza como herramienta de diagnóstico, no como archivo que haya que editar.
