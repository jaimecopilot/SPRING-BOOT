1. descarga una JDK 17 x64/ARM64 acorde con el equipo;
2. instala la JDK en una ruta estable;
3. si el instalador ofrece `Set JAVA_HOME`, actívalo;
4. si ofrece añadir Java a `PATH`, actívalo o configúralo manualmente;
5. descarga Maven binario si vas a utilizar también Maven global;
6. descomprime Maven en una carpeta estable;
7. añade `...\\apache-maven-3.9.x\\bin` a `PATH`;
8. cierra las terminales abiertas;
9. abre una terminal nueva y ejecuta `java -version`, `javac -version`, `mvn -version`.

En Windows, `JAVA_HOME` se puede configurar desde **Variables de entorno**. La variable debe apuntar a la raíz de la JDK, por ejemplo:

```text
C:\\Program Files\\Eclipse Adoptium\\jdk-17...
```

Y `PATH` debe contener una entrada equivalente a:

```text
%JAVA_HOME%\\bin
```

No introduzcas comillas como parte del valor de `JAVA_HOME`. Si una herramienta tiene problemas con espacios, se corrige la invocación o configuración concreta; no se cambia arbitrariamente la ruta añadiendo comillas persistentes al valor.

### 5.8 Instalación guiada en Debian/Ubuntu

El gestor de paquetes puede resolver casi todo el entorno:

```bash
sudo apt update
sudo apt install openjdk-17-jdk maven
```

Después verifica:

```bash
java -version
javac -version
mvn -version
```

Si existen varias instalaciones Java:

```bash
update-alternatives --config java
update-alternatives --config javac
```

No selecciones `java` y `javac` de ramas diferentes. Si necesitas definir `JAVA_HOME`, primero localiza la JDK real en `/usr/lib/jvm` y apunta a su raíz.

En otras distribuciones cambia el gestor, no el concepto: `dnf` en Fedora/RHEL, `pacman` en Arch, etc.

### 5.9 Instalación guiada en macOS

Con Homebrew:

```bash
brew install --cask temurin@17
brew install maven
```

Enumera las JDK disponibles:

```bash
/usr/libexec/java_home -V
```

Y obtiene la ruta de Java 17:

```bash
/usr/libexec/java_home -v 17
```

Una configuración típica de shell puede derivar `JAVA_HOME` dinámicamente:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
export PATH="$JAVA_HOME/bin:$PATH"
```

Si lo añades a `.zshrc` o `.bashrc`, abre una nueva terminal o recarga el archivo antes de verificar.

### 5.10 IDE: instalación no equivale a configuración

Después de instalar IntelliJ, Eclipse o VS Code todavía hay que comprobar la JDK que utilizará el proyecto.

- IntelliJ puede tener varios SDK registrados.
- Eclipse distingue JRE/JDK instalados y nivel de compilación.
- VS Code delega gran parte de Java en extensiones y el Java Language Server.

Por eso el cierre de la instalación no es «el icono del IDE aparece en el escritorio», sino «terminal, Maven e IDE coinciden en una plataforma Java compatible».

### Pregunta

¿Por qué en Windows es relativamente habitual configurar variables manualmente mientras que en Linux/macOS un gestor de paquetes suele reducir ese trabajo?

### Respuesta razonada

Un gestor de paquetes instala programas en ubicaciones conocidas y crea enlaces o registros coherentes con las convenciones del sistema. En Windows, especialmente cuando se descargan instaladores o ZIP desde distintos proveedores, es más frecuente que cada herramienta quede en una ruta independiente y que el usuario tenga que coordinar `JAVA_HOME` y `PATH`. No es una limitación de Java, sino una diferencia en los mecanismos habituales de instalación.

## Bloque 6 - Variables de entorno

### 6.1 `JAVA_HOME`

`JAVA_HOME` debe representar la carpeta raíz de la JDK que queremos que utilicen herramientas como Maven, Gradle o determinados IDEs.

Ejemplo conceptual en Windows:

```text
C:\Program Files\Eclipse Adoptium\jdk-17...
```

Ejemplo en Linux:

```text
/usr/lib/jvm/java-17-openjdk-amd64
```

Ejemplo en macOS:

```text
/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home
```

**No debe apuntar a `bin`**. La carpeta `bin` vive dentro de `JAVA_HOME`.

### 6.2 `PATH`

`PATH` es una lista ordenada de directorios donde el sistema busca ejecutables. Cuando escribes:

```bash
java -version
```

el shell busca un ejecutable llamado `java` siguiendo el orden configurado.

Por eso pueden producirse situaciones aparentemente contradictorias:

- `JAVA_HOME` apunta a Java 17;
- pero el primer `java` de `PATH` pertenece a Java 11.

Entonces `java -version` y `mvn -version` pueden incluso mostrar JDK diferentes, dependiendo de cómo esté configurado Maven.

### 6.3 Diagnóstico

En Windows:

```cmd
echo %JAVA_HOME%
where java
where javac
where mvn
```

En PowerShell:

```powershell
echo $env:JAVA_HOME
Get-Command java
Get-Command javac
Get-Command mvn
```

En Linux/macOS:

```bash
echo "$JAVA_HOME"
which java
which javac
which mvn
```

No basta con saber que un comando existe: queremos saber **qué ejecutable exacto** se ha resuelto.


### 6.4 Las variables de entorno se heredan al crear procesos

Hay un detalle que explica muchos “lo he cambiado y sigue sin funcionar”.

Cuando abres una terminal o un IDE, ese proceso recibe una copia del entorno disponible en ese momento. Si después cambias `JAVA_HOME`, la terminal que ya estaba abierta puede seguir viendo el valor anterior.

Después de cambiar variables:

1. cierra terminales antiguas;
2. abre una terminal nueva;
3. vuelve a verificar;
4. reinicia el IDE si sigue viendo la configuración anterior.

### 6.5 Diagnóstico cruzado: no confíes en un solo comando

Si sospechas un conflicto de Java, recoge varias evidencias.

PowerShell:

```powershell
java -version
javac -version
mvn -version
$env:JAVA_HOME
Get-Command java
Get-Command javac
Get-Command mvn
```

CMD:

```cmd
java -version
javac -version
mvn -version
echo %JAVA_HOME%
where java
where javac
where mvn
```

Linux/macOS:

```bash
java -version
javac -version
mvn -version
echo "$JAVA_HOME"
which java
which javac
which mvn
```

Así separas cuatro preguntas diferentes: qué ejecutable resuelve el shell, qué versión anuncia, qué JDK ve Maven y qué ruta declara `JAVA_HOME`.

### Pregunta

Si `java -version` responde “comando no encontrado”, ¿demuestra eso que Java no está instalado?

### Respuesta razonada

No. Sólo demuestra que el shell no ha encontrado un ejecutable `java` en las rutas que está consultando. Java podría estar correctamente instalado en otra carpeta y faltar únicamente la entrada correspondiente de `PATH`. Para distinguir ambos casos hay que localizar la instalación y comprobar las variables y rutas configuradas.

## Bloque 7 - Las tres verificaciones esenciales

### 7.1 `java -version`

Comprueba qué JVM se ejecutará desde esa terminal. Una salida moderna suele indicar:

- versión;
- distribución o proveedor;
- arquitectura;
- información de la VM.

No compila nada. Sólo nos informa del runtime que el shell encuentra.

### 7.2 `javac -version`

Comprueba el compilador. Si `java` funciona pero `javac` no existe, debemos investigar si estamos usando una instalación incompleta, un `PATH` incoherente o una distribución que no incluye herramientas de desarrollo en la ruta esperada.

Lo importante es que las versiones sean coherentes.

### 7.3 `mvn -version`

Maven muestra algo especialmente útil: además de su propia versión, muestra la versión de Java con la que se está ejecutando y su `Java home`.

Por tanto, si:

```text
java -version -> 17
mvn -version  -> Java 11
```

no debemos ignorarlo. Significa que dos herramientas están resolviendo Java de manera distinta.

### Pregunta

¿Qué tres comandos ejecutarías siempre al preparar un equipo para este curso y qué te demuestra cada uno?

### Respuesta razonada

`java -version` demuestra qué runtime encuentra el shell; `javac -version` demuestra que existe un compilador compatible; `mvn -version` demuestra que Maven está disponible y revela qué Java utiliza Maven. Juntos permiten detectar la mayoría de los problemas de entorno antes de atribuirlos al proyecto.

## Resumen del Punto 0.1

- La JDK proporciona compilador, runtime y herramientas de desarrollo.
- La JVM ejecuta bytecode.
- Maven automatiza dependencias y construcción.
- El IDE organiza el trabajo, pero no sustituye a Java ni a Maven.
- `JAVA_HOME` y `PATH` cumplen funciones diferentes.
- Las versiones deben ser coherentes entre terminal, Maven e IDE.
- Verificar el entorno antes de programar evita diagnósticos falsos.

---
