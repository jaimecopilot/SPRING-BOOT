Eso mejora la reproducibilidad entre alumnos, compañeros y CI.

El wrapper no sustituye a la JDK: Maven sigue necesitando Java para funcionar.

### Pregunta

¿Qué ventaja tiene que Spring Initializr genere también `mvnw` y `mvnw.cmd`? ¿Qué ocurre si un desarrollador no tiene Maven instalado globalmente?

### Respuesta razonada

El Wrapper convierte la versión de Maven en parte de la infraestructura reproducible del proyecto. Si el sistema dispone de Java y de las herramientas necesarias para descargar la distribución la primera vez, el desarrollador puede ejecutar el build mediante `mvnw`/`mvnw.cmd` aunque no haya instalado `mvn` globalmente. Además evita que cada miembro del equipo utilice una versión distinta sin darse cuenta.

## Bloque 3 - El IDE

### 3.1 Qué aporta un IDE

Un IDE integra en una sola interfaz muchas herramientas de desarrollo:

- resaltado de sintaxis;
- autocompletado contextual;
- navegación a definiciones y usos;
- detección temprana de errores;
- importación de proyectos Maven;
- ejecución y depuración;
- breakpoints e inspección de variables;
- refactorizaciones;
- integración con Git;
- ejecución de tests;
- terminal incorporada;
- formateo y generación de código.

Es importante entender qué hace realmente el IDE. Cuando pulsas “Run”, el IDE no inventa un segundo lenguaje Java. Configura y lanza procesos utilizando la JDK y el modelo del proyecto. Cuando importa Maven, interpreta el POM y sincroniza dependencias con el sistema de construcción.

### 3.2 IntelliJ IDEA, Eclipse y VS Code

Los tres entornos son válidos para este curso.

| Entorno | Fortalezas | Consideraciones |
|---|---|---|
| IntelliJ IDEA | Excelente navegación, refactorización y depuración Java | Algunas funciones Spring avanzadas pertenecen a Ultimate; Java/Maven básico funciona perfectamente |
| Eclipse | Maduro, gratuito, muy extendido en Java empresarial | Interfaz y atajos propios; integración Maven mediante m2e |
| VS Code | Ligero y multiplataforma | Necesita extensiones Java/Spring para ofrecer una experiencia comparable |

La recomendación pedagógica es mantener un mismo IDE durante la mayor parte del curso. Cambiar constantemente introduce ruido: menús, atajos, configuración de SDK, integración Maven y depurador cambian, aunque Java y Spring sigan siendo los mismos.


### 3.3 El IDE no sustituye a JDK ni a Maven

Cuando pulsas **Run**, el IDE termina lanzando una JVM. Cuando “recarga Maven”, está leyendo el `pom.xml` y sincronizando el modelo del proyecto.

Por eso puedes tener configuraciones incoherentes como:

```text
Terminal: Java 17
IDE:      Java 21
Maven:    Java 11
```

y obtener errores que parecen contradecirse.

En M0 buscamos que terminal, Maven e IDE estén alineados con Java 17.

### 3.4 Índices y modelo del proyecto

Los IDEs mantienen índices para ofrecer autocompletado, navegación, búsqueda de usos y refactorización.

Después de importar un proyecto Maven es normal que durante unos instantes aparezcan símbolos sin resolver mientras se descargan dependencias y se construyen índices.

Antes de cambiar imports a ciegas, comprueba:

- que el POM sea válido;
- que Maven haya terminado de resolver;
- que el IDE use la JDK correcta;
- que el proyecto haya sido importado como Maven.

### 3.5 Configuración versionada y configuración local

Versionamos `pom.xml`, fuentes, recursos y tests.

No dependemos de una preferencia privada del IDE para que el proyecto compile.

Un proyecto profesional debe poder construirse en una terminal limpia y en CI.

### 3.6 Elegir IDE

No existe una única respuesta correcta.

- IntelliJ ofrece una experiencia Java muy integrada.
- Eclipse es maduro y habitual en entornos Java empresariales.
- VS Code es ligero y obtiene soporte Java/Spring mediante extensiones.

El código y Maven son los mismos; cambian menús, atajos y herramientas visuales.

### Pregunta

Si el proyecto compila desde la terminal pero el IDE marca errores, ¿qué sospecharías primero?

### Respuesta razonada

Sospecharía una diferencia entre el modelo del proyecto que usa Maven y el que ha importado el IDE: SDK incorrecto, proyecto Maven sin recargar, índice corrupto, dependencias todavía no sincronizadas o nivel de lenguaje distinto. El hecho de que Maven compile es una evidencia fuerte de que el código y el POM son coherentes; habría que revisar la configuración local del IDE antes de cambiar el código a ciegas.

## Bloque 4 - Versiones recomendadas

### 4.1 Java 17 como nivel mínimo del curso

Java 17 es una versión LTS y constituye el mínimo de nuestra baseline Spring Boot 3.5.x. El proyecto declarará:

```xml
<properties>
    <java.version>17</java.version>
</properties>
```

Esto expresa qué nivel de Java usa el proyecto. Aunque una JDK más nueva pueda compilarlo, el código del curso no dependerá de características posteriores a Java 17 salvo que se indique explícitamente.

### 4.1.1 Qué significa que Java 17 sea LTS

**LTS** significa *Long Term Support*. Una versión LTS recibe mantenimiento durante un periodo prolongado por parte de distintos distribuidores y se convierte con frecuencia en una base estable para organizaciones que no quieren migrar la plataforma cada pocos meses.

Para aprender Spring Boot importa más la estabilidad de la baseline que perseguir la versión Java más reciente. Java 17 nos ofrece:

- compatibilidad con Spring Boot 3.x;
- una base moderna del lenguaje;
- disponibilidad amplia en Windows, Linux y macOS;
- una versión suficientemente consolidada para que errores del entorno no se confundan con novedades experimentales.

Tener instalada Java 21 o una versión superior no es necesariamente un problema. El proyecto puede compilarse con una JDK más nueva manteniendo `java.version=17`, siempre que no utilicemos APIs o sintaxis posteriores que rompan esa compatibilidad. En el curso, cuando queramos aprovechar una característica que requiera una versión superior, lo indicaremos de forma explícita.

### 4.1.2 Distribuciones de JDK

«Java 17» no identifica a un único instalador. Existen varias distribuciones compatibles construidas a partir de OpenJDK.

| Distribución | Rasgo práctico | Situación habitual |
|---|---|---|
| Eclipse Temurin | gratuita, multiplataforma, muy extendida | opción recomendada para homogeneizar el curso |
| OpenJDK de la distribución Linux | integrada con el gestor de paquetes | muy cómoda en Linux |
| Oracle JDK | distribución de Oracle | organizaciones que ya trabajan con ella |
| Amazon Corretto | gratuita y mantenida por Amazon | frecuente en entornos AWS |

El código Java no debería depender de que el proveedor sea Temurin, Oracle o Corretto. Lo que sí debemos evitar es mezclar ejecutables de proveedores/versiones distintos sin saberlo. Si `java`, `javac`, Maven y el IDE apuntan a instalaciones diferentes, los problemas se vuelven difíciles de reproducir.

### Pregunta breve

Si dos alumnos usan Java 17 pero uno tiene Temurin y otro Corretto, ¿debería cambiar el código del curso?

### Respuesta

No. Ambas distribuciones implementan la plataforma Java compatible. Puede haber diferencias operativas o de empaquetado, pero el código del curso no debe depender del proveedor. Lo importante es mantener una versión compatible y saber qué instalación utiliza cada herramienta.

### 4.2 Qué ocurre con una JDK demasiado antigua

Si intentas construir una aplicación moderna con una JVM o un compilador que no soportan las versiones de bytecode requeridas, aparecen mensajes como:

- `UnsupportedClassVersionError`;
- `Unsupported class file major version ...`;
- errores al cargar plugins de Maven;
- fallos de compilación porque el `release` solicitado no existe.

La dificultad es que el síntoma puede aparecer lejos de la causa. Por eso `java -version`, `javac -version` y `mvn -version` son herramientas de diagnóstico, no simples comprobaciones ceremoniales.

### 4.3 Maven

Aunque Spring Boot 3.5.x soporta Maven desde versiones anteriores a 3.9, en el curso utilizaremos Maven 3.9.x para disponer de una línea moderna y homogénea. El Wrapper del proyecto fija una distribución 3.9.x, de modo que el alumno no tenga que perseguir diferencias de versión entre equipos.

### Pregunta

¿Qué pasaría si instalaras Java 8 y después intentaras compilar este proyecto? ¿Por qué el error puede resultar confuso?

### Respuesta razonada

Java 8 no puede cumplir el nivel mínimo del proyecto ni cargar clases compiladas para Java 17. El error puede aparecer al iniciar Maven, al ejecutar un plugin o al cargar una dependencia, no necesariamente en una línea de nuestro código. Por eso el mensaje puede hablar de versiones de clase o del plugin y hacer pensar que el problema está en Spring cuando la raíz es la JVM utilizada.

## Bloque 5 - Instalación por sistema operativo

### 5.1 Windows

En Windows es habitual instalar una distribución JDK mediante un instalador y Maven mediante un ZIP o un gestor de paquetes.

Para una JDK basada en Temurin, el instalador suele ofrecer opciones equivalentes a:

- configurar `JAVA_HOME`;
- añadir Java al `PATH`.

Si se usan opciones gráficas, conviene verificar después desde una terminal nueva; las terminales abiertas antes del cambio pueden conservar el entorno antiguo.

Maven, si se instala manualmente, se descomprime en una ruta estable y su directorio `bin` debe estar accesible desde `PATH`.

### 5.2 Linux

En Debian/Ubuntu, una opción habitual es:

```bash
sudo apt update
sudo apt install openjdk-17-jdk
sudo apt install maven
```

El sistema de alternativas permite comprobar qué ejecutable Java está activo:

```bash
update-alternatives --config java
```

En Fedora/RHEL se utilizan normalmente paquetes `java-17-openjdk-devel`; en Arch, paquetes equivalentes a `jdk17-openjdk`. Los nombres concretos dependen de la distribución.

### 5.3 macOS

Homebrew permite gestionar JDK, Maven y aplicaciones de desarrollo desde un sistema común. Por ejemplo:

```bash
brew install --cask temurin@17
brew install maven
```

macOS incorpora además:

```bash
/usr/libexec/java_home -v 17
```

que puede ayudar a localizar la ruta de una JDK instalada.

### 5.4 Qué significa realmente «instalar» cada pieza

Las tres herramientas del entorno no se instalan del mismo modo y conviene entender esa diferencia, porque muchos fallos se producen al asumir que todo funciona como una aplicación gráfica.

**La JDK** instala un árbol de directorios que contiene ejecutables, módulos, librerías y archivos de configuración. El dato esencial no es que exista una entrada en el menú Inicio, sino que podamos localizar su carpeta raíz y que `java` y `javac` pertenezcan a la misma instalación.

**Maven** es más sencillo: una distribución binaria es básicamente un directorio con `bin`, `boot`, `conf` y `lib`. Cuando se instala desde un ZIP, no hay un proceso mágico de registro en el sistema. Descomprimir Maven no basta: el shell tiene que poder encontrar `mvn` mediante `PATH`. Por eso dos ordenadores pueden contener exactamente los mismos archivos y, sin embargo, sólo uno reconocer el comando.

**El IDE** es una aplicación de escritorio, pero internamente mantiene su propio modelo del proyecto. Puede conocer una JDK distinta a la que encuentra la terminal, utilizar el Maven Wrapper del proyecto aunque haya un Maven global instalado y mantener índices que necesitan recargarse cuando cambia el `pom.xml`.

Esta distinción permite diagnosticar con una pregunta muy simple: **¿qué capa está fallando?**

| Síntoma | Primera capa que conviene comprobar |
|---|---|
| `java` no existe | sistema operativo, `PATH`, instalación JDK |
| `javac` no existe pero `java` sí | JRE/JDK o `PATH` mezclado |
| `mvn` no existe | instalación Maven o `PATH` |
| Maven usa otra Java | `JAVA_HOME` y configuración de Maven |
| terminal funciona, IDE no | SDK/JDK configurada en el IDE y modelo Maven |
| IDE funciona, terminal no | entorno del sistema; el IDE puede estar usando una JDK configurada internamente |

### 5.5 Rutas habituales y cómo no confundirlas

No memorices las rutas como si fueran obligatorias: son ejemplos que te ayudan a reconocer la estructura.

En Windows podrías encontrar:

```text
C:\Program Files\Eclipse Adoptium\jdk-17...
C:\tools\apache-maven-3.9.x
```

En Linux:

```text
/usr/lib/jvm/java-17-openjdk-amd64
/usr/share/maven
```

En macOS:

```text
/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
/opt/homebrew/Cellar/maven/...
```

La regla que sí debes recordar es estructural:

```text
JAVA_HOME
`-- bin
    |-- java
    `-- javac
```

Si `JAVA_HOME` termina en `bin`, has bajado un nivel de más. Si `PATH` contiene sólo `JAVA_HOME` pero no su `bin`, el shell no encontrará los ejecutables.

### 5.6 Qué hacer cuando hay varias JDK instaladas

Tener varias JDK no es un problema. Es habitual mantener Java 17 para un proyecto y Java 21 para otro. El problema aparece cuando no sabemos cuál está usando cada herramienta.

En Windows, `where java` puede mostrar más de una ruta. En Linux/macOS, `which java` muestra la que resolverá el shell y herramientas como `readlink -f $(which java)` ayudan a seguir enlaces simbólicos. En macOS, `/usr/libexec/java_home -V` enumera las JDK conocidas por el sistema.

Ante una discrepancia, recoge estas cinco evidencias antes de cambiar nada:

```text
1. java -version
2. javac -version
3. mvn -version
4. JAVA_HOME
5. ruta física de java/javac/mvn
```

Con esas cinco piezas se puede reconstruir casi siempre qué instalación está usando realmente el equipo.

### 5.7 Instalación guiada en Windows

Una instalación manual típica sigue esta secuencia:
