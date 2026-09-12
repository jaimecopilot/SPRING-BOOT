Ejecutar tests desde Maven es importante porque es reproducible fuera del IDE y es el mismo tipo de operación que realizará un pipeline de CI.

En este módulo tenemos dos clases con objetivos distintos:

- `MiProyectoApplicationTests`: verifica que el contexto puede cargarse.
- `SaludoControllerTest`: verifica directamente el comportamiento Java del controlador sin introducir todavía MockMvc.

La segunda prueba es deliberadamente simple:

```java
assertThat(controller.saludar())
        .isEqualTo("Hola, Ministerio de Educación");
```

Más adelante aprenderemos tests HTTP y tests aislados de capas; en M0 queremos dominar primero el ciclo ejecutar -> observar -> corregir -> repetir.

### Pregunta

¿Qué ventaja tiene poder ejecutar los mismos tests desde terminal además de desde el IDE?

### Respuesta razonada

La terminal elimina dependencia de una configuración gráfica concreta y permite automatizar. Un servidor de CI puede clonar el repositorio y ejecutar exactamente el mismo comando. Si una prueba sólo funciona cuando hacemos clic en una configuración privada del IDE, no es suficientemente reproducible.

## Bloque 6 - `curl` como instrumento de observación HTTP

`curl` permite enviar peticiones desde terminal y ver la respuesta sin depender de un navegador.

Petición básica:

```bash
curl http://localhost:8080/hola
```

Incluir cabeceras de respuesta:

```bash
curl -i http://localhost:8080/hola
```

Modo detallado de conexión y protocolo:

```bash
curl -v http://localhost:8080/hola
```

Algunas opciones que utilizaremos durante el curso:

| Opción | Propósito |
|---|---|
| `-X` | indicar explícitamente un método HTTP |
| `-H` | añadir una cabecera |
| `-d` | enviar datos en el cuerpo |
| `-i` | incluir cabeceras de respuesta en la salida |
| `-v` | mostrar detalle de la conversación HTTP y conexión |
| `-b` | enviar cookies |
| `-u` | autenticación básica |
| `-o` | guardar respuesta en un fichero |
| `-s` | reducir salida de progreso |

No necesitamos usar todas en M0; las introducimos para reconocerlas cuando aparezcan.


### 6.1 Método, URL, cabeceras y cuerpo

Una petición HTTP se puede pensar como:

```text
método + URL + cabeceras + cuerpo opcional
```

Ejemplo de sintaxis que utilizaremos en módulos posteriores:

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Ana"}' \
  http://localhost:8080/ejemplo
```

### 6.2 Redirecciones

Para seguir redirecciones:

```bash
curl -L http://localhost:8080/alguna-ruta
```

### 6.3 Windows PowerShell

Si `curl` parece comportarse de forma extraña, prueba explícitamente:

```powershell
curl.exe -i http://localhost:8080/hola
```

### 6.4 Guardar y silenciar salidas

```bash
curl -o respuesta.txt http://localhost:8080/hola
curl -s http://localhost:8080/hola
```

Las opciones pueden combinarse; eso es más importante que memorizarlas aisladas.

### 6.5 Flags de `curl` que conviene reconocer desde el principio

No necesitamos memorizar todo `curl`, pero sí entender qué dimensión de la petición o respuesta modifica cada opción.

| Opción | Qué hace | Cuándo ayuda |
|---|---|---|
| `-i` | incluye cabeceras de respuesta | comprobar status, `Content-Type`, `Location`, etc. |
| `-v` | muestra conversación detallada | diagnosticar conexión, cabeceras y redirecciones |
| `-X` | fija explícitamente el método | pruebas de POST/PUT/PATCH/DELETE |
| `-H` | añade una cabecera | `Content-Type`, `Authorization`, cabeceras propias |
| `-d` | envía datos | cuerpos JSON o formularios |
| `-b` | envía cookies | reproducir sesiones/estado del cliente |
| `-u` | construye autenticación básica | ejercicios de seguridad posteriores |
| `-o` | guarda el cuerpo en archivo | respuestas grandes/binarias |
| `-s` | reduce salida de progreso | scripts y automatización |
| `-L` | sigue redirecciones | observar el destino final de respuestas 3xx |

Algunas opciones se usarán mucho más adelante. Presentarlas ahora sirve para que, cuando aparezcan, el comando no parezca una secuencia arbitraria de letras.

En PowerShell existe una peculiaridad histórica: `curl` puede resolverse a un alias/comando distinto según la versión del entorno. Si necesitas garantizar el ejecutable de cURL, utiliza `curl.exe` y comprueba con `Get-Command curl` qué se está resolviendo.

### Pregunta

¿Qué diferencia hay entre `curl -i` y `curl -v`?

### Respuesta razonada

`-i` añade las cabeceras de la **respuesta** a la salida que normalmente mostraría sólo el cuerpo. `-v` activa una traza más detallada de la operación, incluyendo información de conexión y cabeceras intercambiadas. Para comprobar rápidamente un `Content-Type` o status puede bastar `-i`; para diagnosticar la conversación completa es más útil `-v`.

## Bloque 7 - El mismo flujo en tres IDEs

### IntelliJ IDEA

- Abrir la clase principal.
- Usar Run para ejecución normal y Debug para depuración.
- Los logs aparecen en la ventana Run/Debug.
- Los breakpoints se colocan en el margen del editor.
- Los tests se pueden ejecutar desde el icono junto a clase/método.
- Maven puede ejecutarse desde su ventana de herramientas o desde la terminal integrada.

### Eclipse

- Ejecutar la clase principal con Run As / Java Application o configuración equivalente.
- Depurar con Debug As.
- Logs en Console.
- Breakpoints desde el margen.
- Tests con Run As / JUnit Test.
- Maven se integra mediante el soporte Maven del IDE.

### VS Code

- Requiere las extensiones Java/Spring apropiadas.
- Puede ejecutar desde los enlaces Run/Debug sobre `main`, desde el panel de Spring Boot o desde configuraciones de depuración.
- Logs aparecen en terminal/debug console según el modo.
- Breakpoints se sitúan en el margen.
- Test Explorer permite ejecutar pruebas.
- La terminal integrada es especialmente útil para Maven y `curl`.


### IntelliJ IDEA: flujo detallado

- El triángulo junto a `main` permite ejecutar.
- Debug arranca el mismo programa bajo depurador.
- **Run** muestra salida.
- **Debug** expone variables, stack, watches y breakpoints.
- **Run > Edit Configurations** permite revisar argumentos y entorno.
- Los tests pueden lanzarse desde el icono de margen.

Si Maven funciona en terminal pero IntelliJ muestra dependencias rojas, primero recarga el modelo Maven.

### Eclipse: flujo detallado

- `Run As > Java Application` ejecuta la clase principal.
- `Debug As > Java Application` la ejecuta con depurador.
- **Console** muestra logs.
- La perspectiva Debug expone variables, breakpoints y stack.
- `Run As > JUnit Test` ejecuta tests.
- m2e mantiene la integración Maven.

### VS Code: flujo detallado

Con las extensiones Java/Spring:

- `Run` / `Debug` aparecen sobre `main`;
- **Run and Debug** muestra Variables, Watch, Call Stack y Breakpoints;
- **Testing** ejecuta pruebas;
- la terminal integrada es natural para Maven y `curl`.

### Mismo concepto, distintas etiquetas

| Concepto | IntelliJ | Eclipse | VS Code |
|---|---|---|---|
| salida | Run / Debug | Console | Terminal / Debug Console |
| ejecutar | Run | Run As | Run |
| depurar | Debug | Debug As | Debug |
| pila | Frames | Debug view | Call Stack |
| watch | Watches | Expressions | Watch |
| tests | gutter / Run | JUnit | Testing |

### Arranque: equivalencias operativas

| Acción | IntelliJ IDEA | Eclipse | VS Code |
|---|---|---|---|
| ejecutar clase principal | icono Run / `Shift+F10` según keymap | `Run As > Java Application` / `Ctrl+F11` | enlace `Run` o Spring Boot Dashboard |
| depurar | Debug / `Shift+F9` según keymap | `Debug As > Java Application` | enlace `Debug` / Run and Debug |
| detener | Stop | Stop | Stop / `Shift+F5` |
| editar configuración | Run/Debug Configurations | Run Configurations | `launch.json` o configuración generada |

Los atajos pueden variar con el sistema operativo o keymap. La guía los presenta como referencia, pero el concepto importante es la acción.

### Depuración: equivalencias

| Acción | IntelliJ IDEA | Eclipse | VS Code |
|---|---|---|---|
| Step Over | `F8` habitual | `F6` | `F10` |
| Step Into | `F7` habitual | `F5` | `F11` |
| Step Out/Return | `Shift+F8` | `F7` | `Shift+F11` |
| Resume | `F9` | `F8` | `F5` |
| evaluar expresión | Evaluate Expression | Inspect/Expressions | Debug Console / Watch |

No memorices los números de tecla como conocimiento Spring. Aprende qué efecto necesitas y consulta el keymap si trabajas en otro IDE.

### Ejecutar tests desde los tres IDEs

**IntelliJ IDEA:** el icono de ejecución junto a una clase o método JUnit permite lanzar una prueba concreta. También puedes ejecutar una carpeta de tests o usar la ventana Maven.

**Eclipse:** `Run As > JUnit Test` ejecuta la selección. La vista JUnit muestra pruebas verdes/rojas y permite navegar al fallo.

**VS Code:** la vista Testing y los enlaces sobre los métodos permiten ejecutar o depurar pruebas. Las extensiones Java proporcionan el runner.

En los tres casos debes poder volver a terminal y ejecutar:

```bash
./mvnw test
```

Si el IDE muestra verde pero Maven falla, la discrepancia es información: quizá el IDE está ejecutando un subconjunto, otra configuración o una JDK distinta.

### Cómo leer un fallo de test

Un test fallido aporta varias capas de información:

1. nombre del test;
2. tipo de fallo o excepción;
3. valor esperado y valor real si es una aserción;
4. stack trace;
5. ubicación de la primera línea relevante de nuestro código.

No corrijas un test sólo para ponerlo verde si el comportamiento esperado era correcto. Primero decide cuál de las dos cosas está equivocada: la implementación o la expectativa.

### Diagnóstico rápido del ciclo diario

| Síntoma | Hipótesis inicial | Primera comprobación |
|---|---|---|
| no arranca | build/configuración/puerto | primer `ERROR` y causa raíz |
| arranca pero endpoint falla | mapping/URL/lógica | `curl -i` y breakpoint |
| breakpoint no salta | no estás en Debug o la ruta no pasa por ahí | modo de ejecución y URL |
| tests IDE sí, Maven no | entornos distintos | JDK y comando Maven |
| 8080 ocupado | proceso anterior | localizar PID/puerto |
| cambio no aparece | proceso viejo/build/DevTools | recompilar/reiniciar y verificar PID |

El objetivo no es tener una tabla para cada posible error, sino adquirir una disciplina: **clasifica la fase que falla antes de cambiar código**.

### Pregunta

Si cambias de IntelliJ a VS Code, ¿cambia Spring Boot o cambia la interfaz con la que lanzas y depuras el mismo programa?

### Respuesta razonada

Spring Boot y el bytecode de la aplicación no cambian por usar otro editor. Cambia la interfaz del IDE, sus atajos y su integración con JDK/Maven. Por eso conviene aprender primero conceptos como breakpoint, Step Into, test y proceso Maven; luego se traducen al botón concreto de cada herramienta.

## Ampliación conceptual - El mismo ciclo de trabajo en cuatro interfaces

Para este curso vamos a tratar **consola, IntelliJ IDEA, Eclipse y VS Code** como cuatro interfaces distintas sobre las mismas operaciones fundamentales. El alumno debe reconocer la operación antes que el botón.

### Construir

- Consola: Maven Wrapper.
- IntelliJ: ventana Maven o acciones de build.
- Eclipse: integración Maven/m2e o Maven Build.
- VS Code: terminal integrada y acciones Java/Maven proporcionadas por extensiones.

### Ejecutar

- Consola: `spring-boot:run` o `java -jar`.
- IntelliJ: Run Configuration.
- Eclipse: Run As / Spring Boot App.
- VS Code: Run sobre `main` o Run and Debug.

### Depurar

Los cuatro conceptos importantes son independientes del IDE:

- breakpoint;
- stack de llamadas;
- variables;
- control del flujo.

Los nombres Step Over, Step Into, Step Out y Resume aparecen con ligeras variaciones visuales, pero representan las mismas operaciones del depurador.

### Testear

- Consola: `mvnw test`.
- IntelliJ: runner JUnit integrado.
- Eclipse: JUnit view.
- VS Code: Testing view / Java Test Runner.

El criterio de éxito no debe ser «sale verde en mi IDE», sino que el test tenga una aserción significativa y que el build reproducible también pase.

### Probar HTTP

El navegador resulta cómodo para GET sencillos. `curl` es más general porque permite elegir método, cabeceras, cuerpo, autenticación, cookies y modo detallado. Más adelante aparecerán clientes especializados, pero aprender `curl` proporciona una referencia universal.

### Pregunta

¿Por qué insistimos en aprender la operación conceptual antes que el botón concreto del IDE?

### Respuesta razonada

Porque los menús cambian entre herramientas y versiones, mientras que compilar, ejecutar, depurar, testear y enviar una petición HTTP son operaciones estables. Entender el concepto permite trasladarse entre IntelliJ, Eclipse, VS Code, terminal, CI y servidores sin empezar de cero.

## Resumen del Punto 0.4

- `main` es el punto de entrada y `SpringApplication.run` inicia el contexto Spring.
- Los logs permiten distinguir etapas de arranque y diagnosticar fallos.
- Un cierre normal no es equivalente a matar un proceso a la fuerza.
- El depurador permite observar estado y flujo de ejecución.
- Maven ofrece una forma reproducible de ejecutar tests.
- `curl` hace visible el contrato HTTP.
- Los IDEs cambian la interfaz, no los fundamentos.

---

# Síntesis del Módulo 0

Al terminar la teoría de este módulo deberías manejar un modelo mental completo del entorno:

```text
Código Java
   |
   v
JDK / javac -----------> bytecode
   |                        |
   |                        v
   +----------------------> JVM

pom.xml ---> Maven ---> dependencias + compile + test + package
                |
                v
          aplicación Spring Boot
                |
                v
      Tomcat embebido / HTTP :8080
```

El IDE rodea este flujo con una interfaz productiva, pero el proyecto debe seguir siendo comprensible y ejecutable desde terminal.
