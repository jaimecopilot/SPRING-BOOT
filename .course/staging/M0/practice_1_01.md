---
title: "Módulo 0 - Guía práctica"
author: "Curso Spring Boot 2026"
lang: es-ES
toc-title: "Índice"
---

# Módulo 0 - Guía práctica: del entorno vacío a la primera aplicación Spring Boot ejecutable

## Qué vas a construir

En este módulo no vamos a utilizar un proyecto “mágico” preparado de antemano. Empezaremos verificando Java y Maven, compilaremos un programa Java sin Spring, crearemos un proyecto Spring Boot, estudiaremos su estructura y terminaremos con una aplicación web que:

- arranca desde IDE y terminal;
- expone `GET /hola` y `GET /adios`;
- contiene tests ejecutables;
- se empaqueta como JAR;
- se puede ejecutar con `java -jar`;
- permite practicar breakpoints, Step Over y Step Into sin depender de contenidos de módulos posteriores.

El proyecto final del módulo está bajo `M0/proyecto/`. El pequeño programa Java previo está bajo `M0/ejemplos/HolaMinisterio.java`.

> **Baseline del curso:** Java 17 como nivel de lenguaje, Maven 3.9.x y Spring Boot 3.5.16.

---

# Práctica 0.1 - Verificar el entorno y ejecutar Java sin Spring Boot

## Objetivo

Antes de atribuir un error a Spring necesitamos demostrar que la plataforma Java funciona por sí sola. Esta práctica termina con un programa compilado mediante `javac` y ejecutado mediante `java`.

## Paso 1 - Abrir una terminal adecuada

### Windows

Puedes utilizar **PowerShell** o **CMD**. Para esta primera verificación es preferible usar una terminal normal, no una consola que mantenga una configuración de entorno distinta.

PowerShell:

```powershell
PS C:\Users\Alumno>
```

CMD:

```cmd
C:\Users\Alumno>
```

### Linux

Abre el terminal de tu distribución. En Ubuntu, normalmente puedes utilizar `Ctrl+Alt+T`.

### macOS

Abre Terminal desde Spotlight o Aplicaciones > Utilidades.


### Cómo elegir la terminal correcta en cada sistema

En Windows tienes varias opciones. Para diagnosticar el entorno del sistema, empieza por **PowerShell** o **CMD**. Git Bash y terminales incluidas por herramientas pueden tener su propio `PATH`, de modo que una prueba hecha allí no siempre representa la configuración que verá otra aplicación.

En Linux, una terminal gráfica suele arrancar un shell como Bash. En macOS es frecuente usar Zsh.

Antes de continuar, escribe un comando trivial de cada entorno:

PowerShell:

```powershell
$PSVersionTable.PSVersion
```

CMD:

```cmd
ver
```

Linux/macOS:

```bash
echo "$SHELL"
```

No necesitamos una versión concreta del shell. Sólo queremos saber qué intérprete está procesando nuestros comandos, porque la sintaxis de variables cambia entre PowerShell, CMD y Bash/Zsh.

### Qué significa el prompt

Ejemplos:

```text
PS C:\Users\Ana>
C:\Users\Ana>
ana@ubuntu:~$
ana@macbook ~ %
```

El prompt no forma parte del comando. Cuando la guía muestra:

```bash
java -version
```

debes escribir únicamente `java -version`.

### Error frecuente: ejecutar como administrador “por si acaso”

Para verificar versiones, compilar y ejecutar el ejercicio Java no necesitas privilegios de administrador.

Trabajar siempre como administrador puede ocultar errores de permisos y crear archivos pertenecientes a otro usuario. Usa elevación sólo cuando la instalación o una operación del sistema realmente la requiera.

### Qué estamos verificando

La terminal nos muestra el entorno que ve el sistema operativo. Un IDE puede tener configurada una JDK diferente; por eso todavía no queremos depender de él.

### Errores habituales

| Situación | Por qué puede confundir | Qué hacer |
|---|---|---|
| Terminal abierta antes de cambiar `PATH` | Puede conservar variables antiguas | Cerrar y abrir una terminal nueva |
| Git Bash en Windows resuelve otra ruta | Puede cargar configuración de shell propia | Contrastar con PowerShell/CMD |
| Terminal como administrador | Introduce privilegios innecesarios | Usar usuario normal salvo instalación |
| Estar en “otra carpeta” | Los comandos de versión no dependen del directorio actual | Ejecutarlos igualmente |

### Pregunta

¿Por qué no empezamos comprobando Java desde el IDE?

### Respuesta razonada

Porque queremos separar dos niveles. Primero verificamos que el sistema puede localizar y ejecutar Java. Después verificaremos que el IDE utiliza una JDK coherente. Si empezamos por el IDE y algo falla, todavía no sabríamos si el problema está en Java o en la configuración del propio IDE.

## Paso 2 - Verificar el runtime Java

Ejecuta:

```bash
java -version
```

### Qué hace

El shell busca un ejecutable `java` en `PATH` y lo ejecuta con el argumento `-version`.

### Qué debes observar

Necesitamos una JDK compatible con el proyecto. Para reproducir exactamente el curso, utiliza Java 17. Una salida válida tendrá una primera línea equivalente a:

```text
openjdk version "17.x.x" ...
```

El proveedor puede ser Temurin, OpenJDK, Corretto, Oracle u otro compatible. La marca exacta no cambia los conceptos que vamos a estudiar.


### Interpretar la salida con más precisión

Una salida de Temurin/OpenJDK puede incluir:

```text
openjdk version "17.0.x" ...
OpenJDK Runtime Environment ...
OpenJDK 64-Bit Server VM ...
```

No todas las distribuciones imprimen exactamente las mismas palabras. Para este curso debes confirmar principalmente:

1. versión mayor 17;
2. arquitectura apropiada para tu equipo;
3. ausencia de errores de instalación;
4. coherencia posterior con `javac` y Maven.

Si aparece Java 21, el proyecto puede funcionar, pero no estarías reproduciendo exactamente el baseline pedagógico. Para evitar diferencias en ejemplos y bytecode, mantendremos Java 17 como JDK activa del curso.

### Localizar el ejecutable real

PowerShell:

```powershell
Get-Command java
```

CMD:

```cmd
where java
```

Linux/macOS:

```bash
which java
```

Haz esta comprobación si el número de versión no coincide con lo esperado. Saber qué archivo se ejecuta suele ser más útil que reinstalar Java sin diagnóstico.

### Si aparece “command not found” o “is not recognized”

No concluyas inmediatamente que Java no está instalado. Primero determina si existe una JDK y si `PATH` la incluye.

Windows:

```cmd
where java
```

Linux/macOS:

```bash
which java
```

Si no aparece una ruta, revisa la instalación y `PATH`.

### Si aparece Java 8 u 11

Probablemente hay varias instalaciones y la primera de `PATH` no es la que quieres usar. Localiza todas las rutas, corrige el orden o elimina referencias antiguas.

### Pregunta

¿`java -version` demuestra que tenemos un compilador?

### Respuesta razonada

No. Demuestra que podemos localizar un runtime Java. La herramienta que compila se llama `javac`; la comprobaremos de forma independiente.

## Paso 3 - Verificar el compilador

Ejecuta:

```bash
javac -version
```

Resultado esperado:

```text
javac 17.x.x
```

### Qué hace `javac`

`javac` transforma ficheros `.java` en clases `.class` que contienen bytecode.

### Qué debes comparar

Compara las dos versiones:

```text
java  -> 17.x
javac -> 17.x
```

No es buena señal obtener, por ejemplo:

```text
java  -> 11
javac -> 17
```

porque ejecución y compilación podrían estar resolviéndose desde instalaciones distintas.

### Diagnóstico útil

Windows:

```cmd
where java
where javac
```

Linux/macOS:

```bash
which java
which javac
```

### Pregunta

¿Qué problema podría aparecer si compilas con Java 17 pero ejecutas el `.class` con una JVM 11?

### Respuesta razonada

La JVM 11 puede rechazar un bytecode producido para una versión de clase posterior. El síntoma típico es un error de versión de clase, no un error de la lógica del programa. Por eso la coherencia entre compilador y runtime forma parte del diagnóstico.

## Paso 4 - Verificar Maven

Ejecuta:

```bash
mvn -version
```

Si no tienes Maven globalmente, no pasa nada para el proyecto final porque utilizaremos Maven Wrapper; este paso sigue siendo útil para conocer tu entorno.

Una salida de Maven contiene, entre otros datos:

```text
Apache Maven 3.9.x
Maven home: ...
Java version: 17.x.x
Java home: ...
OS name: ...
```


### Interpretar `mvn -version` línea por línea

Una salida típica incluye información equivalente a:

```text
Apache Maven 3.9.x
Maven home: ...
Java version: 17...
Java home: ...
Default locale: ...
OS name: ...
```

Comprueba dos versiones distintas:

- **Maven**: 3.9.x en nuestra referencia.
- **Java usada por Maven**: 17.

Es perfectamente posible que `java -version` muestre 17 y Maven indique otra versión si la configuración local está mezclada.

### Distinguir `Maven home` de `Java home`

`Maven home` apunta a Maven.

`Java home` de la salida Maven indica la instalación Java que está usando Maven.

No deben apuntar a la misma carpeta. Son herramientas distintas.

### Primera ejecución lenta

Si es la primera vez que Maven se ejecuta, puede descargar plugins y artefactos. Ver líneas de `Downloading from central` no indica un fallo.

Si la red está bloqueada, sí pueden aparecer timeouts o errores de resolución. En ese caso el problema no es el código Java: Maven no ha podido obtener lo que necesita.

### La línea más importante para el diagnóstico

Fíjate en:

```text
Java version: ...
Java home: ...
```

Maven puede estar utilizando una JDK distinta de la que obtienes al escribir directamente `java -version`.

### Si `mvn` no existe

Tienes dos alternativas válidas:

1. instalar Maven 3.9.x globalmente;
2. continuar y utilizar `mvnw`/`mvnw.cmd` dentro del proyecto.

En este curso preferiremos el Wrapper para los comandos del proyecto.

### Pregunta

Si `java -version` dice 17 pero `mvn -version` dice Java 11, ¿qué dato revisarías?

### Respuesta razonada

Revisaría primero `JAVA_HOME` y la configuración con la que Maven selecciona Java. El shell puede encontrar `java` mediante `PATH`, mientras Maven puede arrancar utilizando una ruta configurada de forma diferente.

## Paso 5 - Verificar `JAVA_HOME`

### PowerShell

```powershell
echo $env:JAVA_HOME
```

### CMD

```cmd
echo %JAVA_HOME%
```

### Linux/macOS

```bash
echo "$JAVA_HOME"
```

Debe apuntar a la raíz de la JDK, no a `bin`.

Ejemplo conceptual:

```text
C:\Program Files\Eclipse Adoptium\jdk-17...
```

No:

```text
C:\Program Files\Eclipse Adoptium\jdk-17...\bin
```

### Comprueba también `PATH`

Windows:

```cmd
echo %PATH%
```

PowerShell:

```powershell
echo $env:PATH
```

Linux/macOS:

```bash
echo "$PATH"
```

### Pregunta

¿`JAVA_HOME` y `PATH` son dos nombres para lo mismo?

### Respuesta razonada

No. `JAVA_HOME` suele contener una única ruta a la raíz de la JDK. `PATH` es una lista de directorios donde el shell busca ejecutables. Habitualmente se añade `$JAVA_HOME/bin` o `%JAVA_HOME%\bin` a `PATH`, pero cumplen funciones distintas.

## Paso 6 - Crear el programa Java mínimo

Crea una carpeta de trabajo y dentro una carpeta `src`.

Linux/macOS:

```bash
mkdir -p HolaMinisterio/src
cd HolaMinisterio
```

Windows CMD:

```cmd
mkdir HolaMinisterio\src
cd HolaMinisterio
```

Crea `src/HolaMinisterio.java` con este primer estado:

```java
public class HolaMinisterio {
    public static void main(String[] args) {
        System.out.println("Hola, Ministerio de Educación");
    }
}
```

### Explicación línea por línea

#### `public class HolaMinisterio {`

- `public`: la clase es accesible públicamente.
- `class`: declara un tipo clase.
- `HolaMinisterio`: debe coincidir con el nombre del fichero público `HolaMinisterio.java`.
- `{`: abre el cuerpo de la clase.

Si el archivo se llamara `Saludo.java` pero la clase pública fuera `HolaMinisterio`, `javac` informaría de que la clase pública debe declararse en un archivo con el mismo nombre.

#### `public static void main(String[] args) {`

- `public`: la JVM debe poder acceder al método.
- `static`: no necesita crear primero un objeto `HolaMinisterio`.
- `void`: no devuelve un valor a la JVM.
- `main`: nombre del punto de entrada convencional.
- `String[] args`: argumentos recibidos desde la línea de comandos.

Cuando más adelante ejecutemos:

```bash
java ... HolaMinisterio Ana
```

`args[0]` contendrá `"Ana"`.

#### `System.out.println(...)`

- `System`: clase de la biblioteca estándar.
- `out`: flujo de salida estándar.
- `println`: imprime y añade un salto de línea.
- el texto entre comillas dobles es un `String` literal.
- `;` finaliza la sentencia.

Errores frecuentes:

```java
system.out.println("...");   // System mal escrito
System.out.println('texto'); // comillas simples no representan un String
System.out.println("texto")  // falta ;
```

#### Las dos llaves `}`

Una cierra `main`; la otra cierra la clase.

### Pregunta

¿Por qué estamos haciendo este ejercicio sin Spring Boot?

### Respuesta razonada

Porque queremos aislar la plataforma Java. Si este programa no compila o no se ejecuta, todavía no tiene sentido depurar Spring, Maven Web o un controlador. La prueba reduce el espacio de posibles causas.

## Paso 7 - Compilar el programa

Desde la carpeta `HolaMinisterio`:

```bash
javac src/HolaMinisterio.java
```

Si todo va bien, `javac` puede no mostrar ningún mensaje. Comprueba que existe:

```text
src/HolaMinisterio.class
```

Linux/macOS:

```bash
ls src
```

Windows:

```cmd
dir src
```

### Qué ha ocurrido

El compilador ha:

1. leído el código fuente;
2. comprobado sintaxis;
3. comprobado tipos;
4. generado bytecode.

### Errores frecuentes

| Mensaje aproximado | Causa probable |
|---|---|
| `file not found` | ruta o nombre equivocado |
| `';' expected` | falta `;` |
| `cannot find symbol` | identificador mal escrito/no disponible |
| `unclosed string literal` | falta cerrar una cadena |
| `reached end of file while parsing` | llaves desequilibradas |
| `class ... is public, should be declared...` | fichero y clase pública no coinciden |

### Pregunta

¿Por qué una compilación correcta puede no imprimir absolutamente nada?

### Respuesta razonada

Porque la finalidad normal de `javac` es producir clases, no un informe de éxito. La ausencia de errores y la aparición del `.class` son las evidencias. En herramientas de línea de comandos es común que el caso correcto sea silencioso.
