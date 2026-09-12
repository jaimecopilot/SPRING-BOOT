### Respuesta razonada

El servidor y Spring reciben ambas peticiones. En `/hola` encuentran un mapping y ejecutan un handler; en `/no-existe` no encuentran un recurso o manejador aplicable y producen una respuesta de no encontrado. El código de estado describe el resultado de esa petición, no si «hay servidor».

## Paso 10 - Enviar cabeceras y cookies para observar la petición

Nuestro controlador todavía no consume cabeceras ni cookies, pero `curl -v` permite observar que el cliente las envía.

Cabecera personalizada:

```bash
curl -v -H "X-Curso: Spring-Boot-2026" http://localhost:8080/hola
```

Cookie:

```bash
curl -v -b "preferencias=es-ES" http://localhost:8080/hola
```

Esto no cambia la respuesta porque el controlador no lee esos datos. Su finalidad aquí es distinguir **enviar información HTTP** de **haber escrito código que la procese**.

### Pregunta

¿Qué diferencia conceptual hay entre `-H` y `-b`?

### Respuesta razonada

`-H` permite añadir una cabecera arbitraria. `-b` está especializado en enviar cookies y construye la cabecera `Cookie` apropiada. Las cookies viajan mediante cabeceras HTTP, pero `curl` proporciona una opción específica para gestionarlas con más comodidad.

## Paso 11 - Tabla de errores del ciclo diario

### Diagnóstico antes de «probar cosas»

Cuando aparezca un problema, no cambies varias variables a la vez. Sigue una secuencia reproducible:

1. copia el mensaje de error exacto;
2. identifica si ocurre al **compilar**, **testear**, **arrancar** o **atender una petición**;
3. reproduce el problema con el comando más pequeño posible;
4. comprueba versión/ruta sólo de las herramientas implicadas;
5. cambia una cosa;
6. repite exactamente la misma prueba.

Ejemplos:

- Si `./mvnw test` falla al compilar, todavía no tiene sentido depurar una petición HTTP.
- Si el JAR arranca pero `/hola` devuelve 404, Java y Maven ya han superado una parte importante del diagnóstico; el foco pasa al mapeo web.
- Si el IDE falla pero `./mvnw test` funciona, el proyecto es probablemente correcto y debemos revisar el modelo/configuración del IDE.
- Si el puerto 8080 está ocupado, cambiar código Java del controlador no puede resolver la causa.

Esta disciplina parece lenta sólo la primera vez. En proyectos reales evita ciclos largos de cambios aleatorios que destruyen la evidencia del problema original.


| Problema | Evidencia | Diagnóstico inicial |
|---|---|---|
| Puerto 8080 ocupado | fallo durante arranque | localizar proceso/puerto |
| Breakpoint no se activa | petición no llega o no estás en Debug | verificar URL/modo/mapping |
| `curl` espera indefinidamente | hilo pausado o servidor bloqueado | mirar depurador/logs |
| test verde en IDE pero Maven falla | configuraciones diferentes | ejecutar y leer Maven |
| Maven no encuentra test | nombre/ruta/compilación | revisar `src/test/java` |
| JAR no aparece | package falló | revisar errores previos |
| 404 sólo en una ruta | mapping ausente | revisar anotación/ruta |
| IDE no refleja cambio | build/indexación/DevTools | recompilar o reiniciar |

## Paso 12 - Reto resuelto: localizar un fallo con el depurador

Vamos a provocar un error **en código que ya conocemos**, sin introducir validación de módulos posteriores.

### Estado temporal: introduce un defecto

Cambia temporalmente:

```java
private String construirMensaje(String destinatario) {
    return "Hola, " + destinatario;
}
```

por:

```java
private String construirMensaje(String destinatario) {
    return "Adiós, " + destinatario;
}
```

No cambies el test.

### Ejecuta sólo el test afectado

```bash
./mvnw -Dtest=SaludoControllerTest test
```

El test `saludarDebeDevolverElMensajeEsperado` debe fallar mostrando una diferencia entre el valor esperado y el real.

### Depura el test

1. coloca un breakpoint dentro de `construirMensaje`;
2. ejecuta el test en Debug desde el IDE;
3. observa `destinatario`;
4. evalúa la expresión que se devuelve;
5. identifica que la palabra incorrecta nace dentro del helper;
6. corrige el método a `"Hola, " + destinatario`;
7. ejecuta nuevamente todos los tests.

### Estado final obligatorio

El método debe quedar:

```java
private String construirMensaje(String destinatario) {
    return "Hola, " + destinatario;
}
```

Y:

```bash
./mvnw test
```

debe volver a pasar.

### Pregunta

¿Qué ha aportado el depurador que no aportaba ya el mensaje de AssertJ?

### Respuesta razonada

El mensaje del test nos dice **qué** resultado difiere. El depurador nos permite observar **cómo** se construye ese resultado: argumentos, flujo y expresión concreta. En este ejemplo el fallo es trivial; el valor del ejercicio está en aprender el procedimiento antes de aplicarlo a cadenas de llamadas más complejas.

---

## Recorridos completos por entorno: consola, IntelliJ IDEA, Eclipse y VS Code

La práctica anterior ha explicado el ciclo diario con un recorrido común. A partir de aquí repetimos ese mismo ciclo desde cuatro entornos distintos porque el alumno debe poder trasladar lo aprendido a la herramienta que utilice en su puesto de trabajo. No son cuatro proyectos distintos: es **el mismo proyecto**, con el mismo `pom.xml`, las mismas clases, los mismos tests y los mismos endpoints. Lo que cambia es cómo se inicia cada acción y dónde se observa su resultado.

> Regla de trabajo: si un comportamiento funciona en un IDE pero falla desde Maven Wrapper, o al revés, no supongas que el código es correcto o incorrecto por intuición. Compara JDK, argumentos, perfil, directorio de trabajo, variables de entorno y modelo Maven. Dos lanzadores diferentes pueden estar ejecutando el mismo código con configuraciones diferentes.

### Recorrido A - Ciclo diario exclusivamente desde consola

Este recorrido demuestra que el proyecto no depende del IDE. Es especialmente importante para CI/CD, servidores, diagnóstico remoto y automatización.

#### A1. Sitúate en la raíz correcta

La terminal debe estar en la carpeta que contiene `pom.xml` y los wrappers:

```text
mi-proyecto/
|-- pom.xml
|-- mvnw
|-- mvnw.cmd
`-- src/
```

Compruébalo antes de ejecutar nada:

Linux/macOS:

```bash
pwd
ls
```

Windows PowerShell:

```powershell
Get-Location
Get-ChildItem
```

Windows CMD:

```cmd
cd
dir
```

Si ejecutas Maven desde otra carpeta, el error no significa que Maven esté roto: simplemente no encuentra el proyecto.

#### A2. Verifica Java y Maven en la misma consola

Linux/macOS:

```bash
java -version
javac -version
./mvnw -version
```

Windows:

```cmd
java -version
javac -version
mvnw.cmd -version
```

La salida del wrapper debe indicar una JVM compatible con Java 17. Si `java -version` y Maven muestran JDK distintas, investiga antes de continuar: el comando Maven es el que determina con qué Java se compilará el proyecto.

#### A3. Compila sin ejecutar tests

Linux/macOS:

```bash
./mvnw compile
```

Windows:

```cmd
mvnw.cmd compile
```

Este paso separa los errores de compilación de los errores de test. Si `compile` falla, todavía no tiene sentido diagnosticar HTTP ni breakpoints.

#### A4. Ejecuta la suite de tests

```bash
./mvnw test
```

En Windows:

```cmd
mvnw.cmd test
```

Busca dos cosas: que Surefire ejecute los tests y que el build termine con `BUILD SUCCESS`. Si el build falla, revisa primero el primer error útil y luego el resumen.

#### A5. Arranca con el plugin de Spring Boot

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

No cierres esa consola. El proceso ocupa el primer plano y seguirá escribiendo logs.

#### A6. Lee el arranque antes de abrir el navegador

Confirma una secuencia razonable:

1. aparece el banner;
2. se inicializa el contexto;
3. Tomcat anuncia el puerto;
4. aparece el mensaje de aplicación iniciada;
5. no existe un stack trace posterior que invalide el arranque.

El banner por sí solo no es prueba suficiente.

#### A7. Prueba HTTP desde una segunda consola

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
curl -i http://localhost:8080/no-existe
```

Compara 200 y 404. Ambos demuestran que hay servidor; lo que cambia es si Spring encuentra un manejador para la ruta.

Para observar la conversación completa:

```bash
curl -v http://localhost:8080/hola
```

En PowerShell, usa `curl.exe` si necesitas evitar cualquier alias o función con el mismo nombre.

#### A8. Para la aplicación de forma ordenada

Vuelve a la consola donde se está ejecutando Spring Boot y pulsa:

```text
Ctrl+C
```

Después comprueba desde la segunda consola que la conexión ya no funciona:

```bash
curl -i http://localhost:8080/hola
```

Una conexión rechazada en este momento es el resultado esperado.

#### A9. Empaqueta y ejecuta el JAR

```bash
./mvnw package
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar
```

Windows:

```cmd
mvnw.cmd package
java -jar target\mi-proyecto-0.0.1-SNAPSHOT.jar
```

Prueba de nuevo los endpoints. Este camino ya no utiliza `spring-boot:run`: ejecuta el artefacto que distribuiríamos.

#### A10. Cambia el puerto por argumento

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar --server.port=8081
```

Y en otra consola:

```bash
curl -i http://localhost:8081/hola
```

La prueba demuestra que una propiedad puede externalizarse sin modificar el código ni recompilar.

#### A11. Diagnostica un puerto ocupado

Si 8080 ya está ocupado, localiza el proceso.

Linux/macOS:

```bash
lsof -i :8080
```

Linux también puede usar:

```bash
ss -ltnp | grep 8080
```

Windows:

```cmd
netstat -ano | findstr :8080
```

No mates procesos indiscriminadamente. Identifica primero el PID y confirma qué programa es.

#### A12. Ejecuta un único test

```bash
./mvnw -Dtest=SaludoControllerTest test
```

Y un método concreto:

```bash
./mvnw -Dtest=SaludoControllerTest#saludarDebeDevolverElMensajeEsperado test
```

Esto resulta útil cuando una suite grande falla y quieres reducir el ciclo de diagnóstico.

**Pregunta:** ¿Qué garantiza este recorrido que no garantiza trabajar sólo con botones del IDE?

**Respuesta razonada:** demuestra que el proyecto es reproducible con herramientas estándar y que build, tests, empaquetado y ejecución no dependen de metadatos locales del IDE. Es la aproximación más cercana a lo que hará un servidor de integración continua.

---

### Recorrido B - Ciclo diario completo en IntelliJ IDEA

Las rutas de menú pueden variar ligeramente entre versiones, pero la lógica es estable. Utiliza IntelliJ Community o Ultimate con una JDK 17 configurada para el proyecto.

#### B1. Abrir e importar el proyecto

1. `File > Open`.
2. Selecciona la carpeta que contiene `pom.xml`.
3. Confirma la confianza en el proyecto si IntelliJ lo solicita.
4. Espera a que termine la sincronización Maven.
5. Abre la ventana **Maven** y comprueba que aparece el proyecto.
6. En `File > Project Structure`, verifica que el **Project SDK** apunta a JDK 17.

Si los imports de Spring permanecen en rojo después de la sincronización, usa **Reload All Maven Projects** antes de editar el código.

#### B2. Ejecutar `MiProyectoApplication`

Abre `MiProyectoApplication.java`. Junto al método `main` aparecerá un triángulo verde.

1. Pulsa el triángulo.
2. Selecciona **Run 'MiProyectoApplication'**.
3. Observa la ventana **Run** en la parte inferior.
4. Busca el puerto de Tomcat y el mensaje `Started`.

IntelliJ crea una Run Configuration. Puedes abrirla desde `Run > Edit Configurations` para ver JDK, working directory, argumentos y variables de entorno.

#### B3. Probar desde la terminal integrada

Abre `View > Tool Windows > Terminal` y ejecuta:

```bash
curl -i http://localhost:8080/hola
```

También compara:

```bash
./mvnw -version
```

La terminal integrada hereda un entorno de proceso; si IntelliJ se abrió antes de cambiar `PATH` o `JAVA_HOME`, puede conservar valores antiguos. Reiniciar IntelliJ puede ser necesario.

#### B4. Parar correctamente

En la ventana **Run**, pulsa el botón cuadrado rojo **Stop**. Después ejecuta `curl` otra vez para comprobar que 8080 ha dejado de responder.

No cierres la ventana de IntelliJ como forma habitual de parar el servidor: perderías información sobre si el proceso terminó de forma ordenada.

#### B5. Crear un breakpoint

Abre `SaludoController.java` y haz clic en el margen izquierdo de la línea:

```java
return construirMensaje("Ministerio de Educación");
```

Aparece un círculo rojo. Ese indicador representa un breakpoint de línea.

#### B6. Arrancar en Debug

Pulsa de nuevo el triángulo de la clase principal, pero selecciona **Debug 'MiProyectoApplication'**. Cuando Tomcat termine de iniciar, llama:

```bash
curl http://localhost:8080/hola
```

La petición quedará detenida al alcanzar el breakpoint. En la ventana **Debug** observa:

- hilo actual;
- call stack;
- variables locales;
- botones Step Over, Step Into, Step Out y Resume.

#### B7. Entrar en el helper

Usa **Step Into** para entrar en `construirMensaje`. Comprueba el valor de `destinatario`. Luego utiliza **Step Out** para volver a `saludar` y **Resume** para terminar la petición.

Si la terminal parece quedarse esperando, recuerda que el servidor está deliberadamente pausado por el depurador.

#### B8. Evaluar una expresión

Con la ejecución detenida, utiliza **Evaluate Expression** y evalúa:

```java
"Hola, " + destinatario
```

No modifica necesariamente el programa; permite comprobar hipótesis sobre valores en ese instante.

#### B9. Ejecutar tests desde IntelliJ

Abre `SaludoControllerTest.java`.

- Triángulo junto a la clase: ejecuta todos los tests de la clase.
- Triángulo junto a un método: ejecuta sólo ese test.
- `Debug`: ejecuta el test bajo depurador.

En la ventana de resultados, distingue test pasado, fallado y no ejecutado. Para un fallo, lee diferencia esperado/real y stack trace.

#### B10. Ejecutar Maven desde la ventana Maven

En la herramienta **Maven**, navega por `Lifecycle` y ejecuta `test` o `package`. Esto debe producir el mismo build Maven que en terminal, aunque la interfaz sea gráfica.

Después compara con:
