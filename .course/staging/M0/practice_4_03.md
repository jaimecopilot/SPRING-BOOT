```bash
./mvnw test
```

Si uno funciona y otro falla, revisa la configuración de Maven/JDK que usa IntelliJ.

#### B11. Crear y revisar una configuración de ejecución

En `Run > Edit Configurations` revisa:

- Main class;
- JRE/JDK;
- working directory;
- program arguments;
- VM options;
- environment variables.

Para probar otro puerto, añade como **Program arguments**:

```text
--server.port=8081
```

Arranca y verifica con `curl`.

#### B12. Diagnóstico específico de IntelliJ

| Síntoma | Comprobación |
|---|---|
| imports Spring rojos | recargar Maven y esperar indexación |
| Run usa otra Java | Project SDK y JRE de la Run Configuration |
| Maven Tool Window usa configuración distinta | Maven settings / JDK for importer |
| breakpoint gris o no se activa | modo Debug, clase realmente cargada y ruta ejecutada |
| cambios no se reflejan | recompilar, reiniciar proceso o revisar DevTools |

**Pregunta:** si `./mvnw test` funciona en la terminal integrada pero IntelliJ marca errores de compilación, ¿qué sospechas primero?

**Respuesta razonada:** el código y el POM ya tienen una evidencia fuerte a favor. Revisaría el modelo importado por IntelliJ, su SDK y la sincronización Maven antes de modificar código que Maven ha demostrado que compila.

---

### Recorrido C - Ciclo diario completo en Eclipse

Utiliza Eclipse IDE for Enterprise Java and Web Developers o una instalación equivalente con soporte Maven y Java.

#### C1. Importar como proyecto Maven

1. `File > Import`.
2. `Maven > Existing Maven Projects`.
3. Selecciona la carpeta que contiene `pom.xml`.
4. Comprueba que Eclipse detecta el POM.
5. Pulsa **Finish**.
6. Espera a que m2e resuelva dependencias.

Si el proyecto aparece con errores mientras Maven todavía trabaja, espera a que finalice la resolución antes de cambiar imports.

#### C2. Verificar la JDK de Eclipse

Abre `Window > Preferences > Java > Installed JREs`. Debe existir una JDK 17 y estar seleccionada cuando corresponda.

También revisa `Project > Properties > Java Build Path` y el nivel de compilación si aparece una discrepancia.

El nombre histórico del menú habla de “JRE”, pero para desarrollar queremos una instalación completa de JDK.

#### C3. Forzar la actualización Maven cuando haga falta

Clic derecho sobre el proyecto:

`Maven > Update Project...`

Marca el proyecto y confirma. Utiliza esta acción cuando hayas cambiado el POM y Eclipse no haya actualizado el classpath.

#### C4. Arrancar la aplicación

Abre `MiProyectoApplication.java`.

- clic derecho;
- `Run As > Java Application` o `Spring Boot App` si Spring Tools lo ofrece.

Observa la vista **Console**. Busca las mismas evidencias de arranque que en terminal.

#### C5. Parar la aplicación

En la vista **Console**, pulsa el botón **Terminate** (cuadrado rojo). Asegúrate de terminar el proceso correcto si tienes varias consolas abiertas.

Después prueba:

```bash
curl -i http://localhost:8080/hola
```

Debe fallar la conexión si no hay otro proceso en ese puerto.

#### C6. Crear breakpoint

Abre `SaludoController.java` y haz doble clic en el margen izquierdo junto a la línea que quieres detener. También puedes utilizar `Run > Toggle Breakpoint`.

Verifica que aparece la marca visual del breakpoint.

#### C7. Arrancar en Debug

Clic derecho sobre `MiProyectoApplication.java`:

`Debug As > Java Application` o `Spring Boot App`.

Cuando llames a `/hola`, Eclipse cambia a la perspectiva **Debug** o te propone hacerlo. Observa:

- Debug/Threads;
- Variables;
- Breakpoints;
- editor en la línea detenida.

#### C8. Recorrer el código

Los comandos habituales son:

- Step Into;
- Step Over;
- Step Return;
- Resume.

Los atajos exactos pueden variar por keymap/OS; el menú y los botones muestran la acción semántica que debes aprender.

Entra en `construirMensaje`, inspecciona `destinatario` y vuelve al método llamador.

#### C9. Inspeccionar y evaluar expresiones

En la vista **Variables** puedes desplegar valores. La vista **Expressions** permite añadir expresiones como:

```java
"Hola, " + destinatario
```

Esto es útil para comprobar hipótesis sin añadir `System.out.println` temporales por todo el código.

#### C10. Ejecutar tests

Clic derecho sobre `SaludoControllerTest.java`:

`Run As > JUnit Test`.

Para depurar:

`Debug As > JUnit Test`.

La vista JUnit muestra tests pasados y fallidos. Si falla uno, abre su stack trace y navega a la línea correspondiente.

#### C11. Ejecutar Maven desde Eclipse

Clic derecho sobre el proyecto:

`Run As > Maven test`

o crea una configuración `Maven Build...` con goals:

```text
test
```

Para empaquetar:

```text
package
```

Aunque Eclipse lance Maven mediante su integración, sigue siendo importante ejecutar también el wrapper desde una terminal externa o integrada disponible en tu instalación.

#### C12. Diagnóstico específico de Eclipse

| Síntoma | Acción |
|---|---|
| Maven dependencies no aparecen | `Maven > Update Project` |
| Java compliance incorrecto | Properties / Installed JREs |
| proyecto conserva errores antiguos | `Project > Clean` después de resolver la causa |
| breakpoint no se detiene | comprobar Debug As y ruta llamada |
| vista Console muestra otro proceso | seleccionar consola correcta |
| test falla sólo en Eclipse | ejecutar `mvnw test` y comparar JDK/configuración |

**Pregunta:** ¿por qué `Project > Clean` no debería ser tu primera respuesta ante cualquier error?

**Respuesta razonada:** limpiar recompila estado generado, pero no corrige una JDK incorrecta, una dependencia ausente o un POM erróneo. Debe usarse después de identificar una causa relacionada con estado de build, no como sustituto del diagnóstico.

---

### Recorrido D - Ciclo diario completo en VS Code

VS Code es un editor extensible. Para que actúe como entorno Java/Spring necesita las extensiones adecuadas y un Java Language Server correctamente inicializado.

#### D1. Preparar extensiones

Instala al menos:

- **Extension Pack for Java**;
- **Spring Boot Extension Pack** o las extensiones Spring equivalentes que utilices.

Reinicia VS Code si una extensión lo solicita.

#### D2. Abrir la carpeta correcta

Utiliza `File > Open Folder` y abre la carpeta que contiene `pom.xml`.

No abras sólo `src/main/java`: el Java Language Server necesita ver el proyecto Maven completo.

#### D3. Verificar el runtime Java

Abre la Command Palette y ejecuta:

```text
Java: Configure Java Runtime
```

Comprueba que JDK 17 está disponible para el proyecto. Después abre el terminal integrado y ejecuta:

```bash
java -version
./mvnw -version
```

Una vez más, compara lo que ve el editor con lo que ve la terminal.

#### D4. Esperar la importación Java/Maven

Al abrir un proyecto por primera vez, VS Code puede tardar en importar dependencias e indexar. No interpretes inmediatamente los imports rojos como errores de código.

Si la importación queda en mal estado, la Command Palette incluye comandos de limpieza/reinicio del workspace Java. Úsalos sólo después de verificar que el POM y la red son correctos.

#### D5. Arrancar la aplicación

Abre `MiProyectoApplication.java`. Sobre `main` suelen aparecer enlaces **Run | Debug**.

Pulsa **Run**. La salida puede aparecer en terminal, Debug Console o panel relacionado según la configuración/extensión.

Confirma puerto y mensaje de arranque.

#### D6. Parar el proceso

Utiliza el botón Stop de la sesión activa o termina el proceso en el terminal si lo has arrancado allí. Verifica con `curl` que el puerto ha quedado libre.

#### D7. Crear breakpoint y depurar

Haz clic en el margen izquierdo de `SaludoController.java` para crear el breakpoint. Luego inicia con **Debug** sobre `main` o desde **Run and Debug**.

Llama a `/hola`. Cuando se detenga, el panel **Run and Debug** muestra:

- VARIABLES;
- WATCH;
- CALL STACK;
- BREAKPOINTS.

#### D8. Step Over, Step Into, Step Out y Continue

Utiliza la barra flotante/controles del depurador. Entra en `construirMensaje`, inspecciona `destinatario` y continúa.

En **WATCH** puedes añadir:

```java
"Hola, " + destinatario
```

#### D9. Ejecutar tests

Abre el panel **Testing**. VS Code detectará los tests JUnit si el proyecto está importado correctamente.

- ejecuta todos los tests;
- ejecuta un test individual;
- utiliza **Debug Test** para depurarlo.

Si los tests no aparecen, verifica primero que `./mvnw test` funciona. Después investiga la extensión y el Java Language Server.

#### D10. Ejecutar Maven desde terminal integrada

```bash
./mvnw test
./mvnw package
```

En Windows:

```cmd
mvnw.cmd test
mvnw.cmd package
```

El terminal integrado es especialmente útil en VS Code porque mantiene a la vista editor y build reproducible.

#### D11. Configuración de ejecución con `launch.json`

VS Code puede generar configuración de depuración bajo `.vscode/launch.json`. No es necesario dominarla en M0, pero conviene entender que puede fijar argumentos, working directory o variables.

Si defines un argumento para otro puerto, verifica siempre que la configuración local del editor no se convierte en requisito oculto del proyecto. El proyecto debe seguir arrancando con Maven Wrapper sin esos metadatos.

#### D12. Diagnóstico específico de VS Code

| Síntoma | Acción |
|---|---|
| imports Java/Spring no resueltos | esperar/importar proyecto, comprobar POM |
| tests no aparecen | comprobar Maven y Java Test Runner |
| Run usa otra JDK | `Java: Configure Java Runtime` |
| debugger no se detiene | sesión Debug activa y breakpoint habilitado |
| workspace parece corrupto | limpiar workspace Java tras comprobar configuración |
| terminal y editor ven Java distintas | revisar entorno del proceso VS Code |

**Pregunta:** ¿por qué en VS Code es especialmente importante diferenciar “editor” de “entorno Java”?

**Respuesta razonada:** VS Code por sí mismo es un editor generalista. El soporte de proyecto Java, Maven, tests y Spring procede de extensiones y del Java Language Server. Si una de esas piezas no está inicializada, el código puede ser correcto y Maven puede funcionar aunque el editor todavía no lo represente correctamente.

---

### Tabla de equivalencias rápida de los cuatro recorridos

| Acción | Consola | IntelliJ IDEA | Eclipse | VS Code |
|---|---|---|---|---|
| Abrir proyecto | `cd` a la raíz | `File > Open` | `Import > Maven > Existing Maven Projects` | `File > Open Folder` |
| Ver Java | `java -version` | Project SDK + terminal | Installed JREs + terminal | Configure Java Runtime + terminal |
| Recargar Maven | nuevo build / wrapper | Reload Maven Projects | Maven > Update Project | reimportar Java/Maven workspace |
| Arrancar | `mvnw spring-boot:run` | Run sobre `main` | Run As | Run sobre `main` |
| Depurar | JDWP avanzado, no necesario en M0 | Debug | Debug As | Run and Debug |
| Breakpoint | no aplica visualmente | margen del editor | doble clic margen | margen del editor |
| Tests | `mvnw test` | Run test | Run As > JUnit Test | Testing panel |
| Package | `mvnw package` | Maven Lifecycle/package | Maven Build/package | terminal Maven |
| Parar | `Ctrl+C` | Stop | Terminate | Stop / terminal |
| Probar HTTP | `curl` | terminal integrada / navegador | terminal / navegador | terminal integrada / navegador |

### Pregunta de integración

Si el proyecto compila, pasa tests y arranca desde consola, pero falla al pulsar Run en un IDE, ¿qué conclusión inicial es más razonable?

### Respuesta razonada

La evidencia apunta primero a una diferencia de configuración del entorno de ejecución del IDE: JDK, argumentos, variables, directorio de trabajo o sincronización Maven. No demuestra que el IDE sea el culpable con certeza, pero sí evita empezar a modificar un código que ya ha superado un build reproducible fuera del IDE.

# Empaquetado y verificación final del Módulo 0

## Paso final A - Ejecutar la suite

Linux/macOS:

```bash
./mvnw test
```

Windows:

```cmd
mvnw.cmd test
```

Todos los tests deben pasar.

## Paso final B - Empaquetar

```bash
./mvnw package
```

Debe aparecer un JAR bajo `target/`.

## Paso final C - Arrancar el JAR

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar
```

En otra terminal:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
```

Esto prueba algo distinto de `spring-boot:run`: el artefacto generado puede ejecutarse como aplicación empaquetada.

## Resultado esperado global del Módulo 0

Al terminar debes poder demostrar, no sólo afirmar, que:

- sabes qué JDK está usando el sistema;
- dispones de compilador;
- entiendes cómo Maven selecciona Java;
- has compilado y ejecutado Java sin Spring;
- has creado/importado un proyecto Maven Spring Boot;
- entiendes las partes principales del POM;
- sabes qué contienen `src/main`, `resources`, `src/test` y `target`;
- tienes `GET /hola` y `GET /adios` funcionando;
- puedes leer el arranque y detectar un fallo de puerto;
- sabes parar el proceso;
- sabes poner un breakpoint y diferenciar Step Over de Step Into;
- puedes ejecutar tests desde IDE y Maven;
- puedes empaquetar y arrancar el JAR;
- puedes inspeccionar respuestas HTTP mediante `curl`.

# Estado final de los ficheros que has construido

## `MiProyectoApplication.java`

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

## `SaludoController.java`

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
