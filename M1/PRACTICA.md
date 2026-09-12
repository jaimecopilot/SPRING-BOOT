---
title: "Módulo 1 - Guía práctica"
author: "Curso Spring Boot 2026"
lang: es-ES
toc-title: "Índice"
---

# Módulo 1 - Guía práctica: de una aplicación que funciona a una API que entendemos

## Qué vas a construir y comprobar

M1 continúa exactamente desde el proyecto final aprobado de M0. No comenzamos con un proyecto diferente ni repetimos Spring Initializr. Vamos a utilizar el código que ya funciona para descubrir qué infraestructura hay detrás y, en los siguientes puntos, transformarlo progresivamente en una API REST con JSON y un primer CRUD.

En el punto 1.1 no añadiremos funcionalidad permanente. Haremos varios experimentos controlados y al terminar el proyecto volverá al estado funcional heredado de M0. Esta disciplina es deliberada: un experimento temporal debe poder demostrarse y después cerrarse sin dejar configuración accidental.

El snapshot acumulativo del módulo está en `M1/proyecto/`.

> **Baseline del curso:** Java 17, Maven 3.9.x mediante Maven Wrapper 3.9.16 y Spring Boot 3.5.16.

---

# Práctica 1.1 - Observar Spring Boot: auto-configuración, starters y servidor embebido

## Objetivo

Al terminar esta práctica habrás demostrado sobre tu proyecto real que:

- Spring Boot ha preparado infraestructura que tú no configuraste manualmente;
- esa infraestructura depende del classpath, de condiciones y de propiedades;
- los starters explican por qué aparecen Spring MVC, Jackson y Tomcat;
- Tomcat forma parte del runtime de la aplicación y no necesita una instalación externa;
- los valores por defecto se pueden sobrescribir;
- el informe de condiciones permite diagnosticar decisiones de auto-configuración;
- una exclusión temporal de auto-configuración cambia comportamiento observable y puede restaurarse limpiamente.

## Requisitos previos

Debes partir del proyecto final de M0. Antes de comenzar, comprueba que existen al menos:

```text
M1/proyecto/
|-- pom.xml
|-- mvnw
|-- mvnw.cmd
|-- src/
|   |-- main/
|   |   |-- java/es/mecd/demo/miproyecto/
|   |   |   |-- MiProyectoApplication.java
|   |   |   `-- controller/SaludoController.java
|   |   `-- resources/application.properties
|   `-- test/
|       `-- java/es/mecd/demo/miproyecto/
|           |-- MiProyectoApplicationTests.java
|           `-- controller/SaludoControllerTest.java
```

`SaludoController` debe seguir exponiendo `GET /hola` y `GET /adios`.

---

## Paso 1 - Abrir el proyecto y arrancarlo

### Qué hacemos

Vamos a obtener una línea base antes de modificar nada. Necesitamos saber que el proyecto heredado funciona para poder atribuir después cualquier cambio al experimento que hagamos.

Desde la carpeta `M1/proyecto`, en Linux/macOS:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell o CMD:

```cmd
mvnw.cmd spring-boot:run
```

Espera a que aparezca en los logs una evidencia equivalente a:

```text
Tomcat started on port 8080
Started MiProyectoApplication
```

La redacción exacta y los tiempos pueden variar. Lo importante es comprobar que el contexto termina de arrancar y que Tomcat publica el puerto.

En otra terminal:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
```

Debes obtener `200` en ambos casos y los cuerpos heredados de M0.

### Desde IntelliJ IDEA

1. Abre `M1/proyecto` como proyecto Maven.
2. Comprueba que el Project SDK es Java 17.
3. Abre `MiProyectoApplication.java`.
4. Ejecuta el botón Run junto al método `main` o la clase.
5. Observa la ventana Run.

### Desde Eclipse

1. Importa `M1/proyecto` mediante **File > Import > Maven > Existing Maven Projects** si aún no está importado.
2. Abre `MiProyectoApplication.java`.
3. **Run As > Java Application**.
4. Observa la vista Console.

### Desde VS Code

1. Abre la carpeta `M1/proyecto`.
2. Espera a que Java/Maven termine de cargar el proyecto.
3. Abre `MiProyectoApplication.java`.
4. Utiliza **Run Java** sobre `main` o la vista Spring Boot Dashboard si está disponible.
5. Observa la terminal o consola asociada.

### Qué debes razonar

Antes de seguir, anota qué piezas crees que han intervenido para que `/hola` funcione. Como mínimo intenta responder:

- ¿quién escucha en el puerto 8080?;
- ¿quién decide que `/hola` corresponde a un método Java?;
- ¿de dónde salen las librerías necesarias?;
- ¿qué parte has configurado tú explícitamente y qué parte no?;

No busques todavía la respuesta “correcta”. En los pasos siguientes contrastaremos tus hipótesis.

### Pregunta

¿Por qué comprobamos `/hola` y `/adios` antes de activar `--debug` o cambiar propiedades?

### Respuesta razonada

Porque necesitamos una referencia. Si después aparece un fallo, podremos distinguir si ya existía antes del experimento o si lo introdujimos nosotros. Una prueba de baseline reduce diagnósticos ambiguos.

---

## Paso 2 - Ver el informe de auto-configuración

### Qué hacemos

Vamos a arrancar la misma aplicación con `--debug`. Ese argumento hace que Spring Boot muestre información adicional de diagnóstico, incluido el **Conditions Evaluation Report**.

Detén primero la aplicación anterior con `Ctrl+C` o con Stop/Terminate en el IDE.

Linux/macOS:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--debug
```

Windows:

```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--debug
```

Si tu shell interpreta de forma especial algún carácter, utiliza la terminal integrada de tu IDE o configura `--debug` como argumento de programa.

### IntelliJ IDEA

1. **Run > Edit Configurations**.
2. Selecciona la configuración de `MiProyectoApplication`.
3. En **Program arguments**, escribe:

```text
--debug
```

4. Ejecuta de nuevo.

### Eclipse

1. **Run > Run Configurations**.
2. Selecciona la configuración Java de `MiProyectoApplication`.
3. Pestaña **Arguments**.
4. En **Program arguments**, escribe `--debug`.
5. Ejecuta.

### VS Code

Si usas una configuración `launch.json`, añade temporalmente:

```json
"args": "--debug"
```

También puedes evitar tocar configuración del editor y ejecutar con Maven desde la terminal integrada.

### Qué debes localizar

Busca en la consola:

```text
CONDITIONS EVALUATION REPORT
```

Después localiza secciones equivalentes a:

```text
Positive matches
Negative matches
Exclusions
```

No necesitas leer todavía cientos de líneas. El objetivo de este paso es demostrar que Spring Boot puede explicar muchas de sus decisiones.

### Pregunta

¿Un `Negative match` significa que la aplicación tiene un error?

### Respuesta razonada

No. Significa que una configuración candidata no cumplió alguna condición. Si no utilizamos una tecnología concreta, es perfectamente normal que su auto-configuración no se active. El informe describe decisiones, no sólo fallos.

---

## Paso 3 - Leer el informe con calma

### Qué hacemos

Ahora sí vamos a buscar evidencias concretas relacionadas con nuestro proyecto web.

En **Positive matches**, busca términos como:

```text
DispatcherServletAutoConfiguration
ServletWebServerFactoryAutoConfiguration
Tomcat
```

La redacción exacta de cada condición puede variar entre versiones. No copies mecánicamente una salida de Internet: utiliza el informe producido por **tu** aplicación.

Lo que quieres demostrar es la relación entre clases disponibles y configuración activada.

Por ejemplo, conceptualmente puedes encontrar condiciones equivalentes a:

```text
... matched because a required web class is present
... matched because this is a servlet web application
... matched because Tomcat classes are available
```

Después ve a **Negative matches** y busca alguna tecnología que no formes parte de este proyecto. Es normal encontrar numerosas configuraciones que no se aplican porque falta una clase, una propiedad o un bean.

### Construye una tabla de observación

Rellena una tabla como esta con ejemplos reales de tu consola:

| Configuración | Positive/Negative | Condición que observaste | Qué significa |
|---|---|---|---|
| DispatcherServlet... |  |  |  |
| ServletWebServer... |  |  |  |
| Tomcat... |  |  |  |
| Otra no usada |  |  |  |

### Qué debes entender

El informe nos permite pasar de:

> “Spring Boot hace magia”

A:

> “Spring Boot evaluó condiciones concretas sobre este classpath y este contexto, y por eso activó o descartó configuraciones”.

### Pregunta

¿Qué cambiaría en este informe si elimináramos del POM una dependencia que introduce clases web esenciales?

### Respuesta razonada

Cambiaría el classpath y, por tanto, podrían dejar de cumplirse condiciones que dependen de esas clases. Algunas configuraciones web pasarían a no aplicarse. Esta es una de las conexiones más importantes entre Maven y el runtime de Spring Boot.

---

## Paso 4 - Demostrar que Tomcat está embebido

La fuente original propone comprobar si existe un comando `tomcat` instalado. Esa observación es útil, pero por sí sola no demuestra qué servidor utiliza nuestra aplicación. Vamos a hacer una verificación más fuerte.

### 4.1 Comprobar si existe una instalación externa

Linux/macOS:

```bash
which tomcat
```

Windows PowerShell:

```powershell
Get-Command tomcat -ErrorAction SilentlyContinue
```

Si no aparece nada, es una situación habitual. Si aparece una ruta, tampoco invalida el ejercicio: sólo significa que tu máquina tiene alguna instalación o comando externo relacionado.

### 4.2 Demostrar que Tomcat pertenece al proyecto

Ejecuta:

Linux/macOS:

```bash
./mvnw dependency:tree
```

Windows:

```cmd
mvnw.cmd dependency:tree
```

Busca `tomcat` en la salida. Debes encontrar dependencias de Tomcat introducidas por el stack web del proyecto.

Puedes filtrar visualmente o utilizar las herramientas de tu sistema. Por ejemplo:

Linux/macOS:

```bash
./mvnw dependency:tree | grep -i tomcat
```

PowerShell:

```powershell
mvnw.cmd dependency:tree | Select-String -Pattern "tomcat"
```

### 4.3 Demostrar el modelo ejecutable

Empaqueta:

```bash
./mvnw clean package
```

En Windows:

```cmd
mvnw.cmd clean package
```

Después arranca el JAR generado:

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar
```

Y comprueba:

```bash
curl -i http://localhost:8080/hola
```

Si el JAR arranca el servidor y atiende la petición sin desplegarse manualmente en un Tomcat externo, has demostrado el modelo de servidor embebido de forma mucho más sólida que comprobando únicamente la ausencia de un programa instalado.

### Pregunta

Si tu ordenador sí tiene un Tomcat instalado externamente, ¿cómo sabemos que este ejercicio no lo está utilizando?

### Respuesta razonada

Porque el árbol de dependencias del proyecto contiene el runtime de Tomcat y el JAR ejecutable arranca directamente con `java -jar`. No hemos copiado un WAR a un `webapps/` externo ni hemos iniciado una instalación global. El ciclo de vida del servidor pertenece al proceso de la aplicación.

---

## Paso 5 - Cambiar una configuración por defecto y restaurarla

### Objetivo del experimento

Spring Boot utiliza por defecto el puerto 8080 en este proyecto. Vamos a sobrescribirlo con configuración externa y después volver al baseline.

Detén la aplicación.

Abre:

```text
src/main/resources/application.properties
```

Su estado base contiene:

```properties
spring.application.name=mi-proyecto
```

Añade temporalmente:

```properties
server.port=9090
```

El archivo queda momentáneamente:

```properties
spring.application.name=mi-proyecto
server.port=9090
```

Arranca la aplicación.

Comprueba:

```bash
curl -i http://localhost:9090/hola
```

Debe responder `200`.

Ahora prueba:

```bash
curl -i http://localhost:8080/hola
```

Si no existe otro proceso escuchando en 8080, esa conexión ya no debe llegar a nuestra aplicación.

### Qué has demostrado

El servidor no está rígidamente fijado a 8080 en código Java. Spring Boot proporciona un valor convencional y `server.port` lo sobrescribe.

### Restauración obligatoria

Detén la aplicación y elimina la línea temporal:

```properties
server.port=9090
```

El estado final vuelve a ser:

```properties
spring.application.name=mi-proyecto
```

Arranca de nuevo y verifica:

```bash
curl -i http://localhost:8080/hola
```

Debe volver a responder `200`.

### Pregunta

¿Por qué no dejamos 9090 como nuevo puerto “ya que también funciona”?

### Respuesta razonada

Porque el objetivo era demostrar una sobrescritura, no cambiar el contrato del curso. Los siguientes ejercicios parten de 8080. Mantener cambios de laboratorio sin necesidad hace que las instrucciones futuras diverjan entre alumnos.

---

## Paso 6 - Observar los starters y las dependencias transitivas

Abre `pom.xml` y localiza como mínimo:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

El proyecto también conserva `spring-boot-devtools` como ayuda de desarrollo heredada de M0.

### Ver el árbol real

Ejecuta:

```bash
./mvnw dependency:tree
```

Localiza en el árbol elementos relacionados con:

- Spring MVC / Spring Web;
- Jackson;
- Tomcat;
- logging;
- JUnit y otras herramientas de test bajo el starter de test.

No necesitas memorizar el árbol entero. Queremos responder a una pregunta concreta: **¿por qué podemos importar y usar determinadas clases aunque no exista una dependencia directa para cada una en nuestro POM?**

### En IntelliJ IDEA

Puedes utilizar la ventana Maven y su vista de dependencias. Según la versión del IDE, también puedes navegar desde el POM a la dependencia o utilizar un diagrama de dependencias Maven.

### En Eclipse

Abre `pom.xml` y utiliza las vistas **Dependency Hierarchy** / **Dependencies** del editor Maven cuando estén disponibles.

### En VS Code

La vista Maven puede mostrar dependencias; para una evidencia reproducible entre todos los entornos, `dependency:tree` sigue siendo la referencia común.

### Pregunta

¿Por qué preferimos `dependency:tree` como evidencia común del curso aunque un IDE tenga una vista gráfica más cómoda?

### Respuesta razonada

Porque el comando pertenece al sistema de construcción y produce una evidencia independiente del IDE. Las vistas gráficas son útiles, pero pueden variar entre herramientas y versiones. El árbol de Maven describe la resolución que utiliza el proyecto.

---

## Paso 7 - Probar `/hola` y reconstruir toda la cadena

Con la aplicación otra vez en 8080, ejecuta:

```bash
curl -i http://localhost:8080/hola
```

No te limites a observar el texto. Reconstruye el recorrido:

1. `curl` actúa como cliente y abre una conexión al puerto 8080.
2. Tomcat embebido recibe la petición HTTP.
3. La infraestructura web de Spring procesa la petición.
4. El `DispatcherServlet` participa como *front controller* de Spring MVC.
5. El sistema de mappings encuentra un método compatible con `GET /hola`.
6. Spring invoca `SaludoController.saludar()`.
7. El método devuelve un `String`.
8. La infraestructura MVC escribe esa representación en la respuesta.
9. Tomcat envía la respuesta HTTP al cliente.
10. `curl` muestra cabeceras y cuerpo.

### Relación con Spring Boot

Tú no escribiste código para:

- crear un socket servidor;
- instanciar y configurar Tomcat manualmente;
- registrar a mano un `DispatcherServlet`;
- construir desde cero el pipeline MVC;
- coordinar todas las versiones de esas librerías.

Eso no significa que esas piezas no existan. Significa que Spring Boot y el ecosistema Spring las han preparado a partir del proyecto y sus convenciones.

### Pregunta

¿Por qué este endpoint no demuestra todavía de forma suficiente el comportamiento de Jackson?

### Respuesta razonada

Porque devuelve un `String` simple. Jackson está disponible en el proyecto gracias al stack web, pero una respuesta de texto no obliga a demostrar la serialización de un objeto Java complejo a JSON. En el paso 10 crearemos un probe temporal que sí lo haga.

---

## Paso 8 - Explicar el papel de Spring Boot usando evidencias del proyecto

Sin modificar archivos, completa este mapa con evidencias que ya has observado:

| Idea | Evidencia del proyecto |
|---|---|
| Auto-configuración | `--debug` y Conditions Evaluation Report |
| Starters | `spring-boot-starter-web` en `pom.xml` |
| Dependencias transitivas | `dependency:tree` muestra MVC, Jackson, Tomcat, etc. |
| Servidor embebido | JAR ejecutable + Tomcat en dependencias + puerto HTTP |
| Configuración flexible | `server.port=9090` cambia el puerto |
| Component scan | `SaludoController` bajo el paquete raíz es descubierto |
| Runtime observable | `/hola`, `/adios`, logs y códigos HTTP |

Ahora intenta explicar el proyecto en un párrafo sin utilizar la frase “Spring Boot hace magia”.

Una respuesta técnicamente útil debería parecerse conceptualmente a:

> El POM declara una capacidad web mediante un starter. Maven resuelve sus dependencias. Spring Boot detecta el entorno web y activa configuraciones apropiadas. El servidor embebido arranca dentro del proceso y Spring MVC descubre el controlador. Las propiedades permiten sobrescribir valores por defecto.

No memorices esa frase; utiliza tus propias palabras.

### Pregunta

¿Qué parte de esta cadena fallaría primero si Maven no hubiera descargado correctamente el starter web?

### Respuesta razonada

El problema aparecería ya en el classpath: clases necesarias no estarían disponibles. Podríamos ver errores de compilación/importación o, según el cambio, dejarían de cumplirse condiciones de auto-configuración. No tendría sentido empezar diagnosticando el puerto antes de confirmar las dependencias.

---

## Paso 9 - Diagnosticar errores frecuentes sin cambiar varias cosas a la vez

Utiliza esta tabla como punto de partida:

| Síntoma | Hipótesis probable | Primera comprobación útil |
|---|---|---|
| No aparece el informe de condiciones | `--debug` no llegó como argumento de programa | revisar configuración de ejecución/comando |
| `Port 8080 already in use` | otro proceso ocupa el puerto | identificar proceso o usar temporalmente otro puerto |
| `/hola` devuelve 404 | controlador no registrado o ruta incorrecta | revisar mapping, paquetes y logs |
| El IDE no reconoce imports Spring | Maven no cargó correctamente dependencias | ejecutar Wrapper / recargar Maven |
| La app termina al arrancar | fallo de contexto o no es el tipo de aplicación esperado | leer el primer error real del log |
| Una auto-configuración no aparece como positiva | no se cumple alguna condición | buscarla en Negative matches |
| `dependency:tree` no muestra una pieza esperada | dependencia ausente/excluida | revisar POM y árbol |
| 9090 sigue activo después del ejercicio | propiedad temporal no restaurada o proceso viejo | revisar `application.properties` y procesos |

### Método de diagnóstico

1. Reproduce el fallo.
2. Conserva el mensaje exacto.
3. Identifica la capa: Maven, arranque, configuración, MVC, HTTP.
4. Cambia una sola variable.
5. Repite la misma prueba.
6. Cuando resuelvas el fallo, restaura cualquier experimento que no forme parte del diseño final.

### Pregunta

¿Por qué “borrar cachés y reiniciar todo” no debería ser nuestra primera estrategia?

### Respuesta razonada

Porque puede hacer desaparecer evidencia y no enseña qué causó el problema. Algunas veces reiniciar o limpiar es útil, pero primero debemos intentar entender el síntoma y aislar la capa responsable.

---

## Paso 10 - Reto resuelto: excluir temporalmente Jackson y demostrar el efecto

### El problema del reto original

Nuestro endpoint `/hola` devuelve texto. Si excluimos Jackson y sólo volvemos a probar `/hola`, podríamos concluir erróneamente que “no ha pasado nada”. Para demostrar el efecto necesitamos un endpoint que obligue a representar un objeto estructurado como JSON.

Vamos a crear ese probe **sólo durante el experimento** y después restauraremos ambos archivos.

### Estado inicial que debes conservar

Antes de editar, ejecuta:

```bash
./mvnw test
```

Debe terminar con `BUILD SUCCESS`.

Guarda mentalmente o mediante tu control de versiones el estado actual de:

```text
src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java
src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java
```

### 10.1 Añadir un probe JSON temporal

En `SaludoController.java`, añade temporalmente estos imports:

```java
import java.util.LinkedHashMap;
import java.util.Map;
```

Y añade este método:

```java
@GetMapping("/info-json")
public Map<String, Object> infoJson() {
    Map<String, Object> info = new LinkedHashMap<>();
    info.put("aplicacion", "mi-proyecto");
    info.put("modulo", 1);
    info.put("jackson", true);
    return info;
}
```

Arranca la aplicación **sin excluir todavía Jackson** y ejecuta:

```bash
curl -i http://localhost:8080/info-json
```

Debes obtener una respuesta `200` con `Content-Type` JSON y un cuerpo estructurado equivalente a:

```json
{"aplicacion":"mi-proyecto","modulo":1,"jackson":true}
```

El orden exacto queda estable en este ejemplo por usar `LinkedHashMap`, pero lo importante es que un objeto Java se convierte en JSON.

### 10.2 Excluir temporalmente la auto-configuración de Jackson

Detén la aplicación.

Cambia temporalmente la anotación de `MiProyectoApplication` a:

```java
@SpringBootApplication(
        exclude = org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class)
```

No modifiques el método `main`.

Arranca de nuevo con `--debug` para que además puedas observar la exclusión:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--debug
```

Busca `JacksonAutoConfiguration` en el informe y comprueba que aparece relacionada con las exclusiones.

Ahora repite:

```bash
curl -i http://localhost:8080/info-json
```

El objetivo no es memorizar un texto de excepción concreto. Lo que debes demostrar es que el endpoint que antes se representaba como JSON **ya no mantiene el mismo camino de serialización automática proporcionado por la configuración normal**. Observa conjuntamente:

- el código HTTP;
- el `Content-Type` si existe;
- el cuerpo;
- y, sobre todo, el error relevante de los logs si la representación falla.

La respuesta exacta puede variar con la versión y con otros conversores presentes en el classpath. La evidencia principal es la diferencia reproducible antes/después y la exclusión visible en el informe de condiciones.

### 10.3 Comprobar que `/hola` no es una prueba suficiente de Jackson

Ejecuta también:

```bash
curl -i http://localhost:8080/hola
```

Puede seguir respondiendo correctamente porque un `String` de texto tiene un tratamiento diferente a un objeto que necesita serialización JSON. Esta comparación es parte del aprendizaje: **una prueba debe ejercitar realmente la pieza que afirma validar**.

### 10.4 Restaurar `MiProyectoApplication`

Devuelve la anotación a:

```java
@SpringBootApplication
```

### 10.5 Eliminar el probe temporal

Elimina de `SaludoController`:

- `LinkedHashMap`;
- `Map`;
- el método `infoJson()`;
- el mapping `/info-json`.

El controlador debe volver al estado heredado de M0.

### 10.6 Verificación de restauración

Ejecuta:

```bash
./mvnw test
```

Después arranca normalmente:

```bash
./mvnw spring-boot:run
```

Comprueba:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
curl -i http://localhost:8080/info-json
```

Resultados esperados:

- `/hola` -> `200`;
- `/adios` -> `200`;
- `/info-json` -> `404`, porque el probe ya no pertenece al snapshot final.

Finalmente revisa `application.properties` y confirma que tampoco conserva `server.port=9090`.

### Qué has demostrado

Has hecho un experimento controlado completo:

```text
baseline verde
  -> crear observable específico
  -> medir comportamiento normal
  -> cambiar una sola pieza de auto-configuración
  -> medir diferencia
  -> restaurar
  -> volver a validar baseline
```

Ese patrón es más valioso que memorizar el nombre de una clase concreta: es una forma general de investigar frameworks.

### Pregunta

¿Por qué eliminamos `/info-json` aunque sería un endpoint útil para seguir experimentando?

### Respuesta razonada

Porque su única función en este punto era aislar el papel de Jackson. El curso introducirá JSON y DTOs de forma permanente en 1.3. Mantener un endpoint auxiliar antes de enseñarlo formalmente contaminaría la progresión pedagógica y haría que el snapshot final contuviera comportamiento que no pertenece todavía a su diseño.

---

## Resultado esperado global de la práctica 1.1

Al terminar debes poder comprobar simultáneamente:

- `./mvnw test` pasa;
- la aplicación arranca en 8080;
- `GET /hola` devuelve `200`;
- `GET /adios` devuelve `200`;
- `GET /info-json` devuelve `404` porque el probe temporal fue retirado;
- `application.properties` no contiene `server.port=9090`;
- `MiProyectoApplication` vuelve a usar `@SpringBootApplication` sin exclusiones;
- `SaludoController` vuelve a su estado heredado;
- `dependency:tree` demuestra que Tomcat, Spring Web/MVC y Jackson llegan a través del stack web;
- puedes localizar y explicar el *Conditions Evaluation Report*;
- puedes explicar con tus propias palabras la diferencia entre Spring Framework y Spring Boot, y el papel de auto-configuración, starters y servidor embebido.

## Checklist de cierre

Antes de pasar a 1.2 responde sí a todo:

- [ ] ¿He arrancado el proyecto desde el baseline M0?
- [ ] ¿He visto el informe de condiciones con `--debug`?
- [ ] ¿Sé explicar un positive match y un negative match sin tratarlos como “éxito/error” automáticamente?
- [ ] ¿He demostrado Tomcat mediante el árbol de dependencias y el JAR ejecutable?
- [ ] ¿He cambiado temporalmente el puerto y lo he restaurado?
- [ ] ¿He inspeccionado las dependencias transitivas del starter web?
- [ ] ¿Puedo reconstruir la cadena cliente -> Tomcat -> Spring MVC -> controlador -> respuesta?
- [ ] ¿He probado la exclusión temporal de Jackson con un observable que realmente necesita JSON?
- [ ] ¿He restaurado completamente los dos archivos modificados en el reto?
- [ ] ¿Los tests y endpoints heredados vuelven a estar verdes?

---

> En la práctica 1.2 utilizaremos este mismo proyecto para observar HTTP de forma explícita: petición, respuesta, cabeceras, métodos y códigos de estado. Allí el objetivo dejará de ser “qué configura Spring Boot” y pasará a ser “qué ocurre exactamente entre un cliente y este servidor”.
