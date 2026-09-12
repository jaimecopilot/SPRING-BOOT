# Punto 0.4 - Ciclo de trabajo diario: arrancar, leer logs, parar, depurar, testear y probar APIs

## Objetivos de aprendizaje

Al finalizar este punto serás capaz de:

1. Explicar qué ocurre internamente al arrancar Spring Boot.
2. Reconocer las líneas importantes de un log de arranque.
3. Parar una aplicación de forma normal y distinguirlo de una terminación forzada.
4. Utilizar breakpoints y las operaciones fundamentales del depurador.
5. Ejecutar tests desde IDE y terminal.
6. Utilizar `curl` para inspeccionar una API.
7. Diagnosticar problemas habituales del ciclo diario.
8. Traducir los mismos conceptos entre IntelliJ, Eclipse y VS Code.

## Bloque 1 - Qué ocurre al arrancar Spring Boot

La aplicación empieza como cualquier programa Java: la JVM invoca `main`.

```java
public static void main(String[] args) {
    SpringApplication.run(MiProyectoApplication.class, args);
}
```

A partir de `SpringApplication.run(...)` se inicia una secuencia de alto nivel:

1. Se prepara y crea un `ApplicationContext`.
2. Se recopilan fuentes de configuración: propiedades, variables de entorno, argumentos, etc.
3. Se analiza la clase principal y se aplica la configuración asociada a `@SpringBootApplication`.
4. Se escanean componentes en el árbol de paquetes.
5. Se registran definiciones de beans.
6. Se crean beans y se resuelven dependencias.
7. Se evalúan condiciones de auto-configuración según clases y propiedades disponibles.
8. Al estar presente Spring Web, se crea la infraestructura web y se prepara el servidor embebido.
9. Tomcat empieza a escuchar en el puerto configurado, 8080 por defecto.
10. Se ejecutan callbacks de arranque como `CommandLineRunner` si existen.
11. El proceso permanece vivo esperando peticiones.

No todo ocurre estrictamente en una sola línea temporal simple; Spring dispone de varias fases internas. Para empezar, este modelo nos permite relacionar síntomas con etapas: un fallo al crear un bean ocurre antes de que el servidor quede listo; un conflicto de puerto aparece cuando el servidor intenta enlazar su socket.

### Arrancar mediante Maven

```bash
./mvnw spring-boot:run
```

Maven utiliza el plugin de Spring Boot para ejecutar la aplicación durante desarrollo.

### Arrancar el JAR

Primero:

```bash
./mvnw package
```

Después:

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar
```

En este caso ejecutamos el artefacto empaquetado, un escenario más parecido a despliegue.


### 1.1 Las etapas del arranque con algo más de precisión

Cuando Spring Boot arranca, no sólo “levanta Tomcat”. Conviene distinguir:

**Preparación del entorno.** Se reúnen propiedades y argumentos.

**Creación del contexto.** Se prepara el contenedor de Spring.

**Descubrimiento y registro.** Se procesan configuraciones y componentes.

**Creación de beans.** Se instancian componentes y resuelven dependencias.

**Auto-configuración.** Se activan configuraciones según classpath, beans y propiedades.

**Inicialización web.** Se prepara Spring MVC y el servidor embebido.

**Publicación del puerto.** Tomcat enlaza el puerto y queda listo.

**Callbacks posteriores.** Se ejecutan runners si existen.

Esta clasificación permite diagnosticar: un error creando un bean y un puerto ocupado pertenecen a etapas distintas.

### 1.2 Tres formas de ejecutar y qué valida cada una

**Desde el IDE.** Valida la configuración local y facilita depuración.

**Con `spring-boot:run`.** Valida Maven y el plugin Spring Boot.

**Con `java -jar`.** Valida el artefacto ya empaquetado.

Las tres son útiles porque responden a preguntas distintas.

### 1.3 Argumentos desde línea de comandos

Puedes cambiar configuración sin editar código:

```bash
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar --server.port=8081
```

Esto anticipa una idea clave de Spring Boot: la configuración puede externalizarse.

### Pregunta

¿Qué diferencia conceptual hay entre `./mvnw spring-boot:run` y `java -jar ...`?

### Respuesta razonada

En el primer caso Maven y el plugin participan directamente en la ejecución desde el proyecto. En el segundo ejecutamos el JAR ya empaquetado mediante la JVM. Ambos pueden iniciar la misma aplicación, pero ejercitan caminos distintos: uno depende del proyecto Maven y otro demuestra que el artefacto distribuible es autónomo.

## Bloque 2 - Leer los logs

Una línea de log suele contener información como:

- fecha y hora;
- nivel (`INFO`, `WARN`, `ERROR`, etc.);
- identificador de proceso;
- hilo;
- logger o clase;
- mensaje.

Al arrancar nos interesan especialmente mensajes equivalentes a:

```text
Tomcat started on port 8080 (http)
Started MiProyectoApplication in ... seconds
```

La redacción exacta puede cambiar entre versiones. Lo importante es reconocer las evidencias: servidor iniciado y contexto completado.

### Niveles

- `TRACE`: detalle extremo.
- `DEBUG`: información útil para diagnóstico durante desarrollo.
- `INFO`: funcionamiento normal relevante.
- `WARN`: situación sospechosa o potencialmente problemática.
- `ERROR`: fallo.

No debemos confundir “muchos logs” con “mejor diagnóstico”. Aumentar a DEBUG/TRACE globalmente puede ocultar la señal entre miles de líneas. Es preferible activar detalle para paquetes concretos cuando exista una hipótesis.

Ejemplo temporal:

```properties
logging.level.es.mecd.demo=DEBUG
logging.level.org.springframework.web=DEBUG
```

Después de la investigación conviene retirar el cambio si no forma parte de la configuración deseada.

### Problemas reconocibles en logs

**Puerto ocupado.** El servidor no puede enlazar 8080. Hay que detener el otro proceso o elegir otro puerto.

**Bean ausente.** Puede deberse a falta de anotación, paquete fuera del escaneo o configuración condicional no satisfecha.

**Dependencia circular.** Dos componentes dependen uno del otro de manera que el contenedor no puede construirlos según el modelo permitido.

**ClassNotFound / NoClassDefFound.** Puede existir una dependencia ausente o una diferencia entre classpaths.

**Excepción de aplicación.** El stack trace muestra la cadena de llamadas. Conviene localizar el primer punto relevante de nuestro código, no leer sólo la última línea.


### 2.1 Anatomía de una línea de log

Un log puede parecerse a:

```text
2026-09-12T18:00:00.123+02:00 INFO 12345 --- [main] e.m.d.m.MiProyectoApplication : Started ...
```

Puedes leer:

- fecha/hora;
- nivel;
- PID;
- hilo;
- logger;
- mensaje.

Durante el arranque predomina `main`. Durante peticiones aparecerán hilos de trabajo del servidor.

### 2.2 Cómo leer un stack trace

Cuando aparece una excepción:

1. identifica el tipo;
2. busca `Caused by:` si existe;
3. localiza la primera línea de tu paquete;
4. relaciona clase y línea con tu código;
5. revisa la cadena de llamadas;
6. busca la causa raíz.

No es necesario comprender todavía todas las clases internas del framework para extraer información útil.

### 2.3 `WARN` no significa automáticamente “aplicación rota”

Un `WARN` merece atención, pero puede coexistir con un arranque correcto.

La evidencia debe combinar:

```text
logs + proceso vivo + respuesta funcional
```

### Pregunta

¿Qué dos evidencias buscarías antes de afirmar que la aplicación ha arrancado correctamente?

### Respuesta razonada

Una evidencia del servidor web escuchando y otra de que el `ApplicationContext` ha completado el arranque. Si además una petición a `/hola` devuelve la respuesta esperada, obtenemos una verificación funcional externa y no sólo una lectura de logs.

## Bloque 3 - Parar correctamente

En una terminal, `Ctrl+C` envía una señal de interrupción al proceso. La JVM y Spring pueden ejecutar su secuencia normal de cierre: parar servidor, cerrar contexto y liberar recursos.

Una terminación forzada como:

```bash
kill -9 <PID>
```

no ofrece esa oportunidad. Debe reservarse para procesos que no responden.

En Windows pueden utilizarse herramientas como `tasklist` y `taskkill` para localizar y terminar procesos.

Para descubrir quién usa el puerto 8080:

Linux/macOS:

```bash
lsof -i :8080
```

Windows:

```cmd
netstat -ano | findstr :8080
```

### 3.1 Parada ordenada frente a terminación forzada

Cuando pulsas Stop en el IDE o `Ctrl+C` en una terminal, la aplicación recibe una señal de terminación y tiene oportunidad de cerrar recursos de forma ordenada. El servidor deja de aceptar trabajo, el contexto de Spring se cierra y los recursos administrados pueden liberar conexiones o ejecutar lógica de destrucción.

Una terminación forzada, como `kill -9` en sistemas Unix, no concede esa oportunidad. El sistema operativo elimina el proceso inmediatamente. Puede ser necesaria si un proceso está completamente bloqueado, pero no debe ser el mecanismo normal de parada.

Por eso el orden de preferencia es:

1. Stop del IDE o `Ctrl+C`;
2. terminación normal por PID si hace falta;
3. fuerza bruta sólo como último recurso.

### 3.2 Cómo demostrar que la aplicación realmente se ha parado

Que la consola deje de escribir no es suficiente. Puedes verificarlo intentando acceder al endpoint:

```bash
curl -i http://localhost:8080/hola
```

Si no hay otro proceso escuchando, la conexión fallará. También puedes comprobar el puerto con herramientas del sistema (`netstat`, `ss`, `lsof`, según plataforma). Esta comprobación es especialmente útil antes de diagnosticar un «Port 8080 already in use».

### Pregunta

¿Qué diferencia hay entre `Ctrl+C` y una terminación forzada? ¿Por qué importa?

### Respuesta razonada

`Ctrl+C` solicita terminar y permite normalmente un cierre ordenado. Una señal de terminación forzada detiene el proceso sin garantizar callbacks de cierre ni liberación ordenada de recursos por la aplicación. En desarrollo puede parecer equivalente, pero en sistemas con conexiones, buffers o trabajo pendiente la diferencia es importante.

## Bloque 4 - Depuración paso a paso

### 4.1 Breakpoint

Un breakpoint ordena al depurador pausar cuando la ejecución llega a una ubicación concreta.

Con el programa detenido podemos:

- inspeccionar variables;
- ver la pila de llamadas;
- evaluar expresiones;
- avanzar una línea;
- entrar en una llamada;
- salir del método actual;
- continuar hasta el siguiente breakpoint.

### 4.2 Step Over, Step Into y Step Out

Supón:

```java
public String saludar() {
    return construirMensaje("Ministerio de Educación");
}
```

**Step Over** ejecuta la llamada a `construirMensaje` como una unidad y se detiene en la siguiente línea disponible del contexto actual.

**Step Into** entra dentro de `construirMensaje` para ver sus instrucciones.

**Step Out** completa el resto del método actual y vuelve al método que lo llamó.

**Continue/Resume** reanuda la ejecución hasta el siguiente breakpoint o hasta terminar.

### 4.3 Atajos típicos

| Acción | IntelliJ | Eclipse | VS Code |
|---|---|---|---|
| Step Over | F8 | F6 | F10 |
| Step Into | F7 | F5 | F11 |
| Step Out | Shift+F8 | F7 / Step Return | Shift+F11 |
| Resume/Continue | F9 | F8 | F5 |
| Detener | Ctrl+F2 | botón Stop / Ctrl+F2 según configuración | Shift+F5 |

Los atajos pueden variar por keymap o sistema operativo. Lo importante es dominar la operación, no memorizar una tecla como si fuera parte de Java.


### 4.4 Pila de llamadas y contexto actual

La **call stack** responde a “¿cómo hemos llegado hasta esta línea?”.

En una petición Spring MVC verás frames del framework y de tu código. Cambiar de frame permite inspeccionar variables de distintos niveles.

### 4.5 Watches y evaluación de expresiones

Durante una pausa puedes evaluar expresiones sin modificar el fuente.

Un *watch* mantiene visible una expresión mientras avanzas paso a paso.

### 4.6 Breakpoints condicionales

En métodos muy invocados, detenerse siempre puede resultar incómodo. Un breakpoint condicional sólo se activa cuando una expresión es verdadera.

Aprenderemos el mecanismo ahora y lo utilizaremos más cuando existan identificadores, colecciones y bucles.

### 4.7 Estrategia de depuración

Una secuencia útil:

1. reproducir el fallo;
2. elegir un primer punto observable;
3. comprobar entradas;
4. avanzar hasta el primer estado inesperado;
5. corregir la causa;
6. repetir la prueba.

Depurar no consiste en entrar en todo, sino en reducir el espacio de búsqueda.

### 4.8 Herramientas concretas que encontrarás en los IDEs

Los tres IDEs expresan los mismos conceptos con nombres ligeramente distintos.

**IntelliJ IDEA** ofrece las vistas Variables, Watches, Frames y Evaluate Expression. Un breakpoint se activa desde el margen; los breakpoints condicionales se editan desde las propiedades del punto de ruptura. `Evaluate Expression` permite ejecutar expresiones Java usando el contexto en pausa.

**Eclipse** organiza la información en la perspectiva Debug, con vistas Variables, Breakpoints, Debug y Expressions. `Inspect`/`Display` y la vista Expressions cumplen el papel de evaluación y vigilancia de valores.

**VS Code** muestra Variables, Watch, Call Stack y Breakpoints en Run and Debug, y dispone de Debug Console para evaluar expresiones durante una pausa.

Lo importante no es memorizar una interfaz concreta. Debes saber formular la intención:

- quiero detenerme aquí;
- quiero avanzar sin entrar;
- quiero entrar en este método;
- quiero volver al llamador;
- quiero vigilar esta expresión;
- quiero que el breakpoint sólo se active si se cumple una condición;
- quiero saber por qué cadena de llamadas he llegado hasta aquí.

Cuando conoces la intención, cambiar de IDE es un problema de localizar el botón equivalente, no de reaprender la depuración.

### Pregunta

¿Cuándo usarías Step Into en vez de Step Over?

### Respuesta razonada

Cuando sospecho que el comportamiento incorrecto está dentro del método llamado y necesito observar su lógica. Si la llamada está suficientemente probada o no me interesa su interior, Step Over evita entrar y mantiene la depuración centrada en el nivel actual.

## Bloque 5 - Tests en el ciclo diario

Desde terminal:

```bash
./mvnw test
```
