# Práctica 0.4 - Ciclo de trabajo diario sin depender de módulos posteriores

## Requisitos previos

Debes tener el proyecto del punto 0.3 con:

```text
GET /hola
GET /adios
```

y el método privado `construirMensaje(...)`.

A diferencia de ejemplos que dependen de DTO o validaciones todavía no estudiadas, toda esta práctica utiliza exclusivamente código construido en M0.

## Paso 1 - Arrancar desde el IDE

### IntelliJ IDEA

1. Abre `MiProyectoApplication.java`.
2. Pulsa el icono Run junto a `main` o utiliza la configuración de ejecución creada por el IDE.
3. Observa la ventana Run.

### Eclipse

1. Abre `MiProyectoApplication.java`.
2. Ejecuta `Run As > Java Application` o la opción Spring Boot equivalente si está disponible.
3. Observa Console.

### VS Code

1. Abre `MiProyectoApplication.java`.
2. Utiliza `Run` sobre `main` o el panel Spring Boot si lo tienes instalado.
3. Observa Terminal/Debug Console según el modo.


### IntelliJ IDEA: ejecución y configuración

1. Abre `MiProyectoApplication.java`.
2. Localiza el icono verde junto a la clase o `main`.
3. Elige **Run**.
4. Abre la ventana **Run** si no aparece automáticamente.
5. Comprueba que el command line utiliza la JDK esperada.
6. Abre `Run > Edit Configurations` y observa, sin cambiar nada todavía:
   - clase principal;
   - módulo/classpath;
   - JRE;
   - argumentos de programa;
   - variables de entorno.

Ese diálogo explica por qué dos configuraciones aparentemente iguales pueden ejecutar con JDK o argumentos diferentes.

### Eclipse: ejecución y configuración

1. En **Package Explorer**, abre la clase principal.
2. `Run As > Java Application`.
3. Observa **Console**.
4. Abre `Run > Run Configurations`.
5. Localiza la configuración creada.
6. Revisa:
   - Main class;
   - proyecto;
   - JRE;
   - Arguments;
   - Environment.

### VS Code: ejecución y configuración

1. Abre la clase principal.
2. Usa `Run` sobre el método `main`.
3. Observa la salida.
4. Abre **Run and Debug**.
5. Si el editor crea una configuración `launch.json`, entiende que es configuración del IDE, no parte del código Spring.
6. Comprueba el runtime desde `Java: Configure Java Runtime`.

### Verificación funcional común

En los tres IDE, después de arrancar:

```bash
curl -i http://localhost:8080/hola
```

debe devolver `200` y el texto esperado.

El botón cambia. La aplicación no.

### Qué debes anotar

- versión de Java indicada en el arranque;
- puerto;
- tiempo aproximado;
- línea que indica que la aplicación está iniciada.

### Pregunta

¿Qué factores pueden hacer que el primer arranque tarde más que los siguientes?

### Respuesta razonada

Entre otros, descarga inicial de dependencias, calentamiento del sistema de archivos, indexación del IDE, carga de clases y cachés. No debemos comparar tiempos sin controlar esas variables ni interpretar unos cientos de milisegundos como una propiedad esencial del código.

## Paso 2 - Leer los logs con intención

No leas el log como una pared de texto. Busca primero:

1. inicio de la aplicación;
2. versión de Spring Boot;
3. perfil activo si se informa;
4. servidor web y puerto;
5. finalización del arranque;
6. cualquier `WARN` o `ERROR` relevante.

Ahora visita:

```text
http://localhost:8080/hola
```

Es posible que, con logging normal, no veas una línea detallando cada petición.

### Experimento temporal

Añade a `application.properties`:

```properties
logging.level.org.springframework.web=DEBUG
```

Reinicia y vuelve a llamar `/hola`.

Observa la diferencia. Después **elimina esta línea** y vuelve a dejar:

```properties
spring.application.name=mi-proyecto
```

### Pregunta

¿Por qué no dejamos DEBUG global activado permanentemente?

### Respuesta razonada

Porque genera mucha más información, aumenta ruido y puede tener coste. Un nivel detallado debe utilizarse con intención, idealmente acotado al paquete que estamos diagnosticando. El objetivo del logging es encontrar señal, no maximizar líneas.

## Paso 3 - Parar desde el IDE

Utiliza Stop en tu IDE.

Comprueba:

- el proceso termina;
- dejan de aparecer logs;
- el botón Stop queda inactivo;
- `http://localhost:8080/hola` deja de responder.

Si la URL sigue respondiendo, puede existir otro proceso en el mismo puerto.

Linux/macOS:

```bash
lsof -i :8080
```

Windows:

```cmd
netstat -ano | findstr :8080
```

### Pregunta

Si cierras una ventana del IDE pero `/hola` sigue respondiendo, ¿qué conclusión puedes sacar?

### Respuesta razonada

Que probablemente el proceso Java sigue vivo o existe otra instancia escuchando en ese puerto. Cerrar una ventana de interfaz no es una prueba suficiente de que el proceso haya terminado; hay que comprobar el proceso o el puerto.

## Paso 4 - Arrancar desde terminal

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

Prueba `/hola` desde otra terminal.

Para parar, utiliza `Ctrl+C`.

### Pregunta

¿Qué ventaja tiene saber arrancar sin el IDE?

### Respuesta razonada

Nos permite distinguir el proyecto de una configuración gráfica local y reproduce mejor cómo trabajará un pipeline o un servidor. También es una vía de diagnóstico: si Maven arranca correctamente y el IDE no, el problema probablemente está en la configuración del IDE.

## Paso 5 - Poner un breakpoint

Abre `SaludoController.java` y coloca un breakpoint en:

```java
@GetMapping("/hola")
public String saludar() {
    return construirMensaje("Ministerio de Educación"); // breakpoint
}
```

Arranca en **Debug** desde tu IDE.

En otra terminal ejecuta:

```bash
curl http://localhost:8080/hola
```

La petición quedará esperando mientras el hilo está pausado.


### Colocar el breakpoint según el IDE

**IntelliJ:** haz clic en el margen izquierdo de la línea. Aparece un punto rojo. Arranca con **Debug**.

**Eclipse:** doble clic en el margen o usa `Toggle Breakpoint`. Arranca con **Debug As**.

**VS Code:** haz clic en el margen. Usa **Run and Debug** o `Debug` sobre `main`.

El breakpoint debe estar en una línea ejecutable. Ponerlo en una llave o declaración que no genere una instrucción utilizable puede hacer que el IDE lo mueva o lo marque como no verificado.

### Qué ocurre cuando haces la petición

1. `curl` abre conexión.
2. Tomcat acepta la petición.
3. Spring MVC resuelve `/hola`.
4. El hilo entra en `saludar()`.
5. El depurador suspende ese hilo.
6. `curl` sigue esperando.
7. Al pulsar Continue/Resume, el hilo termina y la respuesta vuelve al cliente.

Este experimento conecta tres perspectivas: cliente HTTP, servidor y depurador.

### Qué observar

- clase actual: `SaludoController`;
- método: `saludar`;
- pila de llamadas;
- hilo HTTP que atiende la petición;
- línea actual.

No hay todavía un `id` ni un DTO: no hemos enseñado esos conceptos. La práctica observa exactamente el código que el alumno ya ha construido.

### Pregunta

¿Por qué `curl` parece quedarse “colgado” cuando el breakpoint se activa?

### Respuesta razonada

Porque la petición HTTP está siendo atendida por un hilo cuya ejecución hemos pausado. El cliente espera a que el servidor termine el procesamiento y envíe la respuesta. Al continuar la depuración, la respuesta llega y `curl` termina.

## Paso 6 - Comparar Step Over y Step Into

Sitúate en:

```java
return construirMensaje("Ministerio de Educación");
```

Primero repite la petición y prueba **Step Over**. El depurador ejecutará `construirMensaje` sin detenerse dentro, salvo otros breakpoints.

Vuelve a disparar `/hola`, detente otra vez y utiliza **Step Into**. Ahora debes entrar en:

```java
private String construirMensaje(String destinatario) {
    return "Hola, " + destinatario;
}
```

Inspecciona:

```text
destinatario = "Ministerio de Educación"
```

Evalúa, si tu IDE lo permite:

```java
"Hola, " + destinatario
```

Después usa Step Out o Continue.

### Pregunta

¿Qué información nueva obtienes con Step Into respecto de Step Over?

### Respuesta razonada

Step Into muestra el interior del método llamado y su estado local. En este caso podemos comprobar el parámetro `destinatario` y la expresión que construye la respuesta. Step Over sólo nos confirma el efecto final desde el método llamador.

## Paso 7 - Crear y ejecutar un test del controlador

Crea:

`src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java`

```java
package es.mecd.demo.miproyecto.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SaludoControllerTest {

    private final SaludoController controller = new SaludoController();

    @Test
    void saludarDebeDevolverElMensajeEsperado() {
        assertThat(controller.saludar())
                .isEqualTo("Hola, Ministerio de Educación");
    }

    @Test
    void despedirDebeDevolverElMensajeEsperado() {
        assertThat(controller.despedir())
                .isEqualTo("Adiós, Ministerio de Educación");
    }
}
```


### Ejecutar la misma prueba en cada IDE

**IntelliJ**

- abre `SaludoControllerTest`;
- usa el triángulo junto a la clase para ejecutar todos;
- usa el triángulo junto a un método para ejecutar sólo uno;
- prueba `Debug` sobre un test para detenerte dentro del código productivo.

**Eclipse**

- clic derecho sobre la clase;
- `Run As > JUnit Test`;
- para depurar, `Debug As > JUnit Test`.

**VS Code**

- abre el panel **Testing** o usa los enlaces sobre el método;
- ejecuta la clase completa o un test individual;
- usa **Debug Test** cuando quieras recorrerlo con el depurador.

### Qué debes aprender de un fallo

Un test fallido no es simplemente una “X roja”. Lee:

- nombre del test;
- valor esperado;
- valor real;
- stack trace;
- primera línea de tu código relevante.

La salida del test es una herramienta de diagnóstico antes de abrir el depurador.

### Por qué este test es deliberadamente simple

Instanciamos directamente el controlador. Todavía no estamos probando routing HTTP ni cargando Spring. Eso llegará cuando el curso enseñe herramientas de test web. En M0 sólo necesitamos aprender:

- qué es un test;
- dónde vive;
- cómo ejecutarlo;
- cómo leer un fallo;
- cómo depurarlo.

Ejecuta la clase desde el IDE.

### Pregunta

¿Este test demuestra que `GET /hola` está correctamente registrado en Spring MVC?

### Respuesta razonada

No. Demuestra el comportamiento Java de los métodos del controlador. No arranca Spring ni envía una petición HTTP. Es importante no atribuir a una prueba garantías que no proporciona. Más adelante utilizaremos tests específicos de la capa web.

## Paso 8 - Ejecutar todos los tests desde terminal

Linux/macOS:

```bash
./mvnw test
```

Windows:

```cmd
mvnw.cmd test
```

Debes ver el build correcto y todos los tests verdes.

Para ejecutar una clase concreta:

Linux/macOS:

```bash
./mvnw -Dtest=SaludoControllerTest test
```

Windows:

```cmd
mvnw.cmd -Dtest=SaludoControllerTest test
```

### Pregunta

¿Por qué es valioso que el test pueda ejecutarse sin abrir el IDE?

### Respuesta razonada

Porque convierte la prueba en parte del build reproducible. Un compañero, un servidor de CI o una máquina de integración puede ejecutar el mismo comando y obtener una señal objetiva sin depender de nuestros botones, plugins o configuraciones locales.

## Paso 9 - Probar la API con `curl`

Con la aplicación arrancada, empieza por la petición mínima:

```bash
curl http://localhost:8080/hola
```

**Qué hace:** envía un `GET /hola` y muestra principalmente el cuerpo de la respuesta.

**Por qué se hace:** queremos separar la prueba HTTP del navegador y observar la API desde una herramienta de terminal reproducible.

Ahora incluye las cabeceras de respuesta:

```bash
curl -i http://localhost:8080/hola
```

Después activa el modo detallado:

```bash
curl -v http://localhost:8080/hola
```

Compara las tres ejecuciones:

- la primera prioriza el cuerpo;
- `-i` añade las cabeceras de la respuesta;
- `-v` enseña además información de conexión y de la petición que envía el cliente.

No uses `-v` por costumbre. Úsalo cuando tengas una pregunta de diagnóstico que justifique el ruido adicional.

Prueba el segundo endpoint:

```bash
curl -i http://localhost:8080/adios
```

Y una ruta que no existe:

```bash
curl -i http://localhost:8080/no-existe
```

### Construye una pequeña matriz de observación

Ejecuta estas variantes y anota qué información aporta cada una:

```bash
curl http://localhost:8080/hola
curl -i http://localhost:8080/hola
curl -v http://localhost:8080/hola
curl -s http://localhost:8080/hola
curl -o salida.txt http://localhost:8080/hola
```

Comprueba el contenido de `salida.txt`. Las dos últimas opciones muestran que cURL sirve también para automatización: `-s` reduce salida de progreso y `-o` guarda el cuerpo.

**Windows PowerShell:** si `curl` no se comporta como esperas, ejecuta:

```powershell
Get-Command curl
```

Si necesitas garantizar el binario cURL, utiliza `curl.exe`.

### Qué debes observar en una respuesta

No te quedes sólo con el cuerpo. Identifica:

- código de estado;
- `Content-Type`;
- cabeceras relevantes;
- cuerpo;
- diferencia entre un mapping existente y una ruta inexistente.

### Prueba otro puerto sin modificar el código

Empaqueta el proyecto y arráncalo así:

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar --server.port=8081
```

Después:

```bash
curl -i http://localhost:8081/hola
```

Esto demuestra que el puerto puede suministrarse como configuración de ejecución. No hemos tenido que recompilar el controlador.

### Errores frecuentes

| Síntoma | Causa posible | Qué comprobar |
|---|---|---|
| conexión rechazada | aplicación parada o puerto incorrecto | logs y puerto |
| `404` en `/hola` | mapping ausente / app distinta | controlador y URL |
| `curl` no es el binario esperado en PowerShell | alias/resolución del shell | `Get-Command curl` |
| la petición queda esperando durante Debug | hilo pausado en breakpoint | ventana del depurador |

### Pregunta

¿Qué diferencia hay entre el 200 de `/hola` y el 404 de `/no-existe` si ambos llegan al mismo servidor?
