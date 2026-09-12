### Pregunta

¿Qué ventaja tiene que el parent gestione versiones? ¿Sería imposible declarar versiones manualmente?

### Respuesta razonada

No sería imposible, pero tendríamos que coordinar nosotros muchas combinaciones de versiones entre componentes relacionados. La gestión proporcionada por Spring Boot define un conjunto probado de dependencias. Podemos sobrescribir versiones cuando exista un motivo, pero dejar la gestión por defecto reduce incompatibilidades accidentales.

## Bloque 4 - Importación en el IDE

### IntelliJ IDEA

Al abrir la carpeta que contiene el `pom.xml`, IntelliJ detecta Maven e importa el modelo. Hay que comprobar:

- Project SDK = JDK 17 o compatible;
- language level coherente;
- Maven sincronizado;
- dependencias resueltas.

### Eclipse

El proyecto se importa como **Existing Maven Project**. Después conviene comprobar Installed JREs, Maven y el nivel de compilación.

### VS Code

Con *Extension Pack for Java* y *Spring Boot Extension Pack*, abrir la carpeta raíz permite al Java Language Server importar el proyecto Maven. Hay que esperar a que termine la indexación antes de interpretar como reales todos los errores visuales iniciales.

### Pregunta

¿Por qué debemos verificar el SDK del IDE incluso si `java -version` en la terminal es correcto?

### Respuesta razonada

Porque el IDE puede mantener su propia configuración de SDK por proyecto. La terminal y el IDE son procesos distintos. Es posible que el sistema resuelva Java 17 mientras el proyecto del IDE apunta a otra JDK. La solución no es asumir que ambos entornos son idénticos, sino comprobarlos.

## Bloque 5 - Primera ejecución

La clase principal tendrá esta forma:

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

Cuando la ejecutamos, Spring Boot arranca el contexto y, al existir Spring Web, levanta un servidor HTTP embebido.

Si todavía no hemos creado ningún controlador, visitar:

```text
http://localhost:8080/
```

puede devolver un 404. **Eso no significa que la aplicación no haya arrancado**. Significa que el servidor está funcionando pero no existe un manejador para `/`.

La evidencia de arranque se obtiene de los logs y de la capacidad del puerto para aceptar conexiones, no de asumir que toda URL debe devolver 200.

### Pregunta

¿Por qué un 404 puede ser una señal de que el servidor sí funciona?

### Respuesta razonada

Porque para devolver un 404 la petición ha llegado a un servidor que ha procesado la ruta y ha determinado que no existe un recurso o manejador asociado. Un error de conexión, en cambio, indicaría que no hay ningún proceso escuchando o que la red impide alcanzarlo.

## Ampliación conceptual - Spring Initializr como generador reproducible, no como asistente mágico

Spring Initializr se utiliza a menudo como si fuese una página que «crea un proyecto». Esa descripción es correcta pero incompleta. Pedagógicamente conviene entenderlo como un **generador de una configuración inicial reproducible**. Recibe una serie de decisiones y produce un conjunto coherente de archivos que expresan esas decisiones en Maven, Java y Spring Boot.

Las decisiones principales son:

- sistema de construcción;
- lenguaje;
- línea de Spring Boot;
- coordenadas Maven;
- nombre y descripción;
- paquete raíz;
- tipo de empaquetado;
- versión de Java;
- starters iniciales.

Cuando pulsas **Generate**, Initializr no crea una aplicación de negocio terminada. Crea una base que ya sabe cómo compilarse, testearse y arrancar. Ese punto de partida es valioso porque evita que cada alumno tenga que recordar de memoria la estructura completa de Maven y Spring Boot antes de haberla aprendido.

### La misma intención puede expresarse por distintos canales

La web de Initializr, los asistentes integrados en los IDE y una petición HTTP a `start.spring.io` son interfaces diferentes para la misma idea: enviar metadatos y obtener un proyecto.

**Web.** Resulta didáctica porque permite ver las opciones de forma explícita y revisar qué se ha seleccionado.

**IntelliJ IDEA.** Puede ofrecer un asistente de proyecto Spring que recopila los mismos metadatos desde el entorno de trabajo.

**Eclipse con Spring Tools.** Puede crear un `Spring Starter Project` mediante un asistente equivalente.

**VS Code.** El comando `Spring Initializr: Create a Maven Project` permite realizar la misma operación desde la Command Palette.

**Consola/API.** Initializr expone un servicio HTTP y por tanto puede automatizarse. Un ejemplo conceptual sería solicitar un `starter.zip` con parámetros. En un entorno de CI o una plantilla corporativa, esta vía puede ser más útil que una interfaz gráfica.

Lo importante es distinguir **canal** de **resultado**. Si los parámetros son equivalentes, el proyecto generado debe expresar las mismas decisiones esenciales. El IDE no añade una semántica distinta a Maven por el hecho de presentar un formulario.

### Pregunta

¿Por qué es importante enseñar también la vía de consola si la web es más cómoda para el primer proyecto?

### Respuesta razonada

Porque la consola demuestra que el proyecto puede generarse y construirse sin interacción manual. Esa propiedad es fundamental para automatización, CI/CD y reproducibilidad. La web es excelente para aprender los campos; la línea de comandos enseña que esos campos son datos y que el proceso puede automatizarse.

## Ampliación conceptual - Qué significa realmente importar un proyecto Maven

«Abrir una carpeta» y «importar un proyecto Maven» pueden parecer la misma operación, pero el IDE debe construir un **modelo del proyecto** a partir de `pom.xml`.

Ese modelo contiene, entre otras cosas:

- versión de Java;
- rutas de código fuente;
- dependencias directas y transitivas;
- plugins;
- configuración de tests;
- recursos;
- classpath de compilación;
- classpath de test.

Cuando modificas `pom.xml`, el modelo del IDE puede quedar temporalmente desactualizado hasta que Maven se recarga. Por eso aparecen situaciones como «Maven compila pero el IDE pinta imports en rojo». No es necesariamente una contradicción en el código; puede ser una divergencia entre el **modelo real que Maven calcula** y el **modelo que el IDE tiene en memoria**.

### IntelliJ IDEA

IntelliJ mantiene un modelo interno del proyecto y una integración Maven. El `Project SDK` determina la JDK principal del proyecto, pero una configuración de ejecución concreta puede especificar otra. Además, el importador Maven puede tener su propia JDK configurada. En un diagnóstico serio conviene saber qué capa estamos mirando.

### Eclipse

Eclipse utiliza su modelo de proyecto Java y la integración m2e para Maven. El classpath se refleja mediante contenedores administrados y la configuración de la JRE/JDK puede depender del workspace y del proyecto. `Maven > Update Project` fuerza una sincronización del modelo Maven.

### VS Code

VS Code se apoya en extensiones y en el Java Language Server. La carpeta abierta, el runtime Java configurado y el estado de importación del workspace determinan lo que el editor conoce del proyecto. Si el Language Server no ha terminado de importar, el editor puede mostrar errores transitorios aunque Maven todavía no haya fallado.

### Pregunta

¿Qué evidencia pesa más para saber si el proyecto compila: un subrayado rojo del editor o un `./mvnw clean test` terminado con éxito?

### Respuesta razonada

El build Maven ejecutado con el wrapper es una evidencia reproducible del estado del proyecto. El editor aporta una señal muy útil, pero puede estar desincronizado. Si Maven pasa y el IDE no, primero se investiga la configuración o sincronización del IDE; no se modifica código correcto para satisfacer un índice local desactualizado.

## Ampliación conceptual - El POM efectivo y la idea de herencia

El `pom.xml` que vemos no contiene necesariamente toda la configuración que Maven utiliza. Maven combina el POM del proyecto con:

- POM padre;
- propiedades;
- gestión de dependencias;
- perfiles activos;
- defaults de plugins;
- configuración global o de usuario cuando corresponda.

El resultado es el **POM efectivo**. Por eso:

```bash
./mvnw help:effective-pom
```

puede mostrar mucho más XML que nuestro archivo original.

En Spring Boot, el parent y su gestión de dependencias permiten omitir versiones en muchos starters y librerías compatibles. Esa omisión no es falta de precisión; significa que la versión se resuelve mediante una política centralizada.

La ventaja pedagógica es importante: al añadir `spring-boot-starter-web`, no necesitamos elegir manualmente versiones independientes para Spring MVC, Tomcat, Jackson, logging y decenas de artefactos relacionados. Spring Boot proporciona un conjunto probado de versiones coherentes.

### Dependencia directa y dependencia transitiva

Si nuestro POM declara A y A necesita B, Maven puede descargar B aunque no figure directamente en nuestro `<dependencies>`. B es transitiva.

Esto explica por qué `dependency:tree` es tan importante. Permite responder preguntas como:

- ¿por qué está esta librería en el classpath?;
- ¿qué dependencia la introdujo?;
- ¿qué versión terminó resolviéndose?;
- ¿hay exclusiones?;
- ¿existe una convergencia de versiones inesperada?

### Pregunta

Si una clase está disponible aunque no hayas declarado su librería directamente, ¿es correcto asumir que siempre estará disponible?

### Respuesta razonada

No. Puede estar llegando de forma transitiva. Si la dependencia que la introduce cambia o deja de incluirla, nuestro código puede romperse. Cuando una librería forma parte explícita del contrato de nuestro código, conviene entender de dónde procede y declarar las dependencias de manera consciente.

## Resumen del Punto 0.2

- Initializr crea una base reproducible, no la lógica de negocio.
- Cada campo del formulario tiene consecuencias en el proyecto.
- El POM describe construcción y dependencias.
- El parent de Spring Boot gestiona un conjunto coherente de versiones.
- El Wrapper reduce diferencias entre equipos.
- Un 404 inicial puede ser perfectamente correcto.

---
