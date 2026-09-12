## Paso 8 - Ejecutar el programa

Ejecuta:

```bash
java -cp src HolaMinisterio
```

Resultado esperado:

```text
Hola, Ministerio de Educación
```

### Desglose

- `java`: arranca la JVM.
- `-cp src`: define el classpath y le indica dónde buscar clases.
- `HolaMinisterio`: nombre binario de la clase, sin `.class`.

### Errores frecuentes

**`Could not find or load main class`**: revisa classpath, nombre y existencia del `.class`.

**`Main method not found`**: revisa la firma exacta de `main`.

**Error de versión de clase**: comprueba que la JVM no sea más antigua que el bytecode generado.

### Pregunta

¿Qué papel cumple `-cp src`?

### Respuesta razonada

Define dónde buscar clases. Como `javac` ha dejado `HolaMinisterio.class` dentro de `src`, la JVM necesita que esa carpeta forme parte del classpath para localizar la clase solicitada.

## Paso 9 - Confirmar nuevamente Maven

Ejecuta:

```bash
mvn -version
```

Ahora ya puedes interpretar el resultado con más criterio: versión de Maven, Java usada por Maven y ruta de Java.

Si `mvn` sigue sin existir, anótalo, pero no bloquees el curso: el proyecto incluirá Wrapper.

## Paso 10 - Configurar el IDE

El objetivo de este paso no es personalizar colores ni instalar decenas de plugins. Queremos demostrar que el IDE utiliza una JDK compatible y entiende el proyecto con las mismas reglas que la terminal.

### IntelliJ IDEA

1. Abre IntelliJ IDEA.
2. En `File > Project Structure`, revisa `Project SDK` y selecciona JDK 17.
3. Comprueba el nivel de lenguaje del proyecto.
4. Abre la configuración de Maven y revisa qué JDK utiliza el importer/runner si tu versión muestra esa opción.
5. Si acabas de modificar el POM, recarga el modelo Maven.
6. Abre la terminal integrada y ejecuta `java -version` y `mvn -version`.

**Qué debes observar:** el proyecto no muestra errores de SDK y la JDK seleccionada es coherente con la baseline del curso.

### Eclipse

1. Abre `Window > Preferences > Java > Installed JREs`.
2. Comprueba que JDK 17 está registrada; si no, usa `Add > Standard VM` y selecciona la raíz de la JDK.
3. Revisa `Java > Compiler` y el nivel de compatibilidad del proyecto.
4. Revisa la integración Maven/m2e.
5. Si el modelo está desactualizado, ejecuta `Maven > Update Project`.
6. Desde la terminal integrada o externa, vuelve a ejecutar `java -version` y `mvn -version`.

**Qué debes observar:** Eclipse resuelve el proyecto Maven y no intenta compilarlo con un JRE/JDK antiguo.

### VS Code

1. Instala `Extension Pack for Java`.
2. Instala `Spring Boot Extension Pack`.
3. Ejecuta `Java: Configure Java Runtime` desde la paleta de comandos.
4. Comprueba que la JDK del proyecto es 17.
5. Si el language server conserva un modelo obsoleto, ejecuta `Java: Clean Java Language Server Workspace` y vuelve a abrir la carpeta.
6. En la terminal integrada, ejecuta `java -version` y `mvn -version`.

**Qué debes observar:** Java Language Server reconoce el proyecto y las extensiones no muestran errores causados por una JDK incompatible.

### Verificación cruzada

Que el IDE diga «JDK 17» y que su terminal integrada muestre Java 11 son dos evidencias incompatibles. No lo ignores. Puede ocurrir porque:

- el IDE tenga una JDK configurada internamente;
- la terminal integrada herede un `PATH` antiguo;
- Maven use `JAVA_HOME` mientras el shell resuelve otro `java`;
- el IDE se abrió antes de cambiar las variables de entorno.

Si hay discrepancias, anota estas cuatro evidencias:

```text
JDK del proyecto en el IDE
java -version en terminal integrada
mvn -version en terminal integrada
ruta física de java/JAVA_HOME
```

Corrige una sola causa y repite las mismas comprobaciones.

### Errores frecuentes

| Problema | Causa probable | Acción |
|---|---|---|
| clases Java básicas en rojo | SDK no configurado | seleccionar JDK 17 |
| dependencias Spring en rojo | modelo Maven no sincronizado | recargar Maven |
| compila en terminal pero no en IDE | SDK/modelo distinto | comparar configuración |
| el IDE sigue viendo Java antigua | proceso abierto antes del cambio | reiniciar IDE/terminal |
| VS Code no resuelve imports | Java Language Server obsoleto | limpiar workspace Java |

### Pregunta

¿Por qué un proyecto podría funcionar en terminal pero no desde el botón Run del IDE?

### Respuesta razonada

Porque el botón Run puede utilizar otra JDK, otra configuración de classpath o un modelo Maven desactualizado. La terminal y el IDE son clientes distintos de las mismas herramientas. Si difieren, debemos comparar qué JDK y qué modelo de proyecto utiliza cada uno, no asumir que el código está mal.

## Paso 11 - Tabla de diagnóstico del entorno

| Síntoma | Causa probable | Acción |
|---|---|---|
| `java` no encontrado | `PATH` no contiene Java | corregir `PATH` |
| `javac` no encontrado | JDK no accesible | instalar/configurar JDK completa |
| `mvn` no encontrado | Maven global ausente | instalarlo o usar Wrapper |
| `java` = 11 y `javac` = 17 | instalaciones mezcladas | unificar rutas |
| Maven usa Java antiguo | `JAVA_HOME`/configuración Maven | corregir selección de JDK |
| `JAVA_HOME` termina en `bin` | ruta incorrecta | apuntar a raíz JDK |
| IDE marca clases inexistentes | Maven no sincronizado | recargar proyecto |
| IDE usa otro SDK | configuración local | seleccionar JDK correcta |

## Paso 12 - Reto resuelto: argumento de línea de comandos

Sustituye el contenido por:

```java
public class HolaMinisterio {
    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("Hola, " + args[0]
                    + ", bienvenido al Ministerio de Educación");
        } else {
            System.out.println("Hola, Ministerio de Educación");
        }
    }
}
```

### Qué hemos añadido

`args.length > 0` comprueba que exista al menos un argumento antes de acceder a `args[0]`. Sin esa condición, ejecutar sin argumentos provocaría un acceso fuera de los límites del array.

Recompila:

```bash
javac src/HolaMinisterio.java
```

Ejecuta con argumento:

```bash
java -cp src HolaMinisterio Ana
```

Resultado:

```text
Hola, Ana, bienvenido al Ministerio de Educación
```

Ejecuta sin argumento:

```bash
java -cp src HolaMinisterio
```

Resultado:

```text
Hola, Ministerio de Educación
```

### Pregunta

¿Qué ocurre si ejecutas `java -cp src HolaMinisterio Ana Maria`?

### Respuesta razonada

El shell entrega normalmente dos argumentos: `args[0] = "Ana"` y `args[1] = "Maria"`. Nuestro programa utiliza sólo `args[0]`, por lo que saludaría a Ana. Si quisiéramos tratar el nombre completo como un argumento habría que citarlo según el shell, por ejemplo `"Ana Maria"`, o procesar todos los elementos.

## Resultado esperado de la práctica 0.1

Debes haber demostrado que:

- el runtime funciona;
- el compilador funciona;
- las versiones son coherentes;
- sabes interpretar Maven;
- sabes localizar `JAVA_HOME` y `PATH`;
- puedes compilar y ejecutar Java sin Spring;
- puedes distinguir un problema de entorno de un problema de aplicación.

---
