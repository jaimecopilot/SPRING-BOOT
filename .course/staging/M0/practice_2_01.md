# Práctica 0.2 - Crear el proyecto Spring Boot

## Paso 1 - Abrir Spring Initializr

Abre:

```text
https://start.spring.io
```

No descargues todavía. Primero identifica cada decisión que vas a tomar.

### Pregunta

¿Por qué no empezamos simplemente descargando cualquier configuración que aparezca por defecto?

### Respuesta razonada

Porque la versión por defecto y otras opciones pueden cambiar con el tiempo. Para que el curso sea reproducible necesitamos elegir conscientemente el sistema de build, la versión de Spring Boot, Java y las coordenadas.

## Paso 2 - Configurar Project, Language y Spring Boot

Selecciona:

```text
Project:     Maven
Language:    Java
Spring Boot: 3.5.16
```

Si Initializr ya no muestra esa versión en su interfaz principal, puedes utilizar el proyecto incluido en esta entrega, cuyo POM fija la baseline del curso. Lo importante es no sustituir silenciosamente la versión por una milestone, snapshot o major distinta.


### Valores exactos de referencia para el curso

Configura:

```text
Project:       Maven
Language:      Java
Spring Boot:   3.5.16
Group:         es.mecd.demo
Artifact:      mi-proyecto
Name:          mi-proyecto
Description:   Proyecto acumulativo del curso Spring Boot
Package name:  es.mecd.demo.miproyecto
Packaging:     Jar
Java:          17
```

Añade:

```text
Spring Web
```

El test starter llegará como parte de la plantilla Spring Boot generada.

Si Initializr cambia ligeramente el aspecto de la página o presenta una versión patch posterior dentro de la misma línea compatible, no copies la interfaz visual de memoria: verifica siempre los valores técnicos resultantes en `pom.xml`.

### Por qué estos nombres importan

El `group` y el `package name` condicionan los packages Java.

El `artifact` participa en el nombre del JAR.

La versión Spring Boot fija el conjunto de dependencias gestionadas.

Java 17 fija el nivel mínimo de compilación que verificaremos en CI.

### Pregunta

¿Por qué el curso no utiliza automáticamente la major más reciente sólo porque sea más nueva?

### Respuesta razonada

Porque el objetivo es que los módulos 0-7 formen un recorrido coherente. Cambiar de major puede modificar APIs, servidor embebido, dependencias y comportamiento. Aprenderemos mejor sobre una baseline consistente y podremos estudiar una migración posteriormente como una operación explícita.

## Paso 3 - Rellenar Project Metadata

Utiliza:

```text
Group:       es.mecd.demo
Artifact:    mi-proyecto
Name:        mi-proyecto
Description: Proyecto acumulativo del curso Spring Boot 2026
Package:     es.mecd.demo.miproyecto
Packaging:   Jar
Java:        17
```

### Qué significa cada campo

**Group.** Espacio de nombres del artefacto.

**Artifact.** Identidad del artefacto dentro del group; también influye en el nombre del JAR.

**Package.** Raíz de paquetes Java.

**Packaging JAR.** Generaremos una aplicación ejecutable con servidor embebido.

**Java 17.** Nivel de lenguaje y baseline del curso.

### Pregunta

¿Por qué `groupId` y paquete Java se parecen pero no son exactamente el mismo concepto?

### Respuesta razonada

El `groupId` pertenece al modelo de coordenadas Maven. El package organiza tipos Java y condiciona visibilidad/escaneo. Es común derivar ambos del mismo dominio organizativo, pero Maven y Java los utilizan para fines distintos.

## Paso 4 - Añadir dependencias

Añade sólo:

```text
Spring Web
```

El proyecto generado incluirá además las dependencias de test habituales.

No añadas JPA, Security, Validation ni bases de datos todavía. Cada una tendrá su momento.

### Pregunta

¿Qué ventaja pedagógica tiene empezar sólo con Spring Web?

### Respuesta razonada

Cuando aparezca un nuevo comportamiento podremos relacionarlo con una dependencia que acabamos de introducir. Si cargáramos todo el ecosistema desde M0 perderíamos esa relación causa-efecto y Spring auto-configuraría piezas que aún no sabemos interpretar.

## Paso 5 - Generar y descargar

Pulsa **Generate**. Obtendrás un ZIP.

Guárdalo en una carpeta de trabajo y conserva el ZIP hasta comprobar que la extracción está completa.

## Paso 6 - Descomprimir y reconocer la raíz

Después de extraerlo, la carpeta raíz debe contener `pom.xml`.

Comprueba además que existen:

```text
mvnw
mvnw.cmd
.mvn/
src/
```

La carpeta que contiene `pom.xml` es la que debes abrir como proyecto.

### Error común

Abrir el directorio padre o abrir sólo `src/` hace que el IDE no vea el proyecto Maven completo.

## Paso 7 - Abrir el proyecto en el IDE

### IntelliJ

`File > Open` y selecciona la carpeta del `pom.xml`. Espera a que Maven termine de importar.

### Eclipse

`File > Import > Maven > Existing Maven Projects`, selecciona la raíz y termina la importación.

### VS Code

`File > Open Folder`, selecciona la raíz. Espera al Java Language Server y a la importación Maven.

### IntelliJ IDEA: importación detallada

1. `File > Open`.
2. Selecciona la carpeta que contiene `pom.xml`, no el ZIP ni la carpeta padre.
3. Si pregunta si debe confiar en el proyecto, revisa la ruta y acepta si es tu proyecto.
4. Espera a que Maven termine la sincronización.
5. Abre la ventana Maven y comprueba que aparece `mi-proyecto`.
6. Revisa el SDK del proyecto: Java 17.

Si ves imports Spring en rojo durante unos segundos, espera a que termine la resolución. Si persisten, recarga Maven.

### Eclipse: importación detallada

1. `File > Import`.
2. `Maven > Existing Maven Projects`.
3. Elige la carpeta raíz que contiene `pom.xml`.
4. Eclipse detectará el POM.
5. Finaliza la importación y espera a m2e.
6. Revisa `Project > Properties > Java Build Path` / configuración JRE si existe alguna incoherencia.

### VS Code: importación detallada

1. Instala `Extension Pack for Java`.
2. Instala el soporte Spring Boot que utilizarás durante el curso.
3. `File > Open Folder`.
4. Abre la carpeta raíz.
5. Espera a que Java Language Server importe el proyecto.
6. Comprueba el runtime detectado con `Java: Configure Java Runtime`.

### Verificación independiente del IDE

Desde la raíz ejecuta:

Linux/macOS:

```bash
./mvnw -version
```

Windows:

```cmd
mvnw.cmd -version
```

El IDE puede ser distinto, pero este comando debe seguir describiendo un proyecto Maven ejecutable.


## Paso 8 - Verificar JDK y modelo Maven

Antes de ejecutar:

1. comprueba JDK 17/compatible;
2. comprueba que el POM no aparece con errores;
3. espera a que las dependencias hayan sido descargadas;
4. abre `MiProyectoApplication.java` y confirma que los imports de Spring se resuelven.

Si `org.springframework...` aparece como inexistente, no empieces cambiando imports: primero verifica que Maven ha sincronizado.

## Paso 9 - Ejecutar desde el IDE

Ejecuta `MiProyectoApplication.main`.

Busca en los logs evidencias equivalentes a:

```text
Tomcat started on port 8080
Started MiProyectoApplication
```

La sintaxis exacta varía por versión.

### Pregunta

¿Que aparezca el banner de Spring Boot demuestra por sí solo que el servidor ha terminado de arrancar?

### Respuesta razonada

No. El banner aparece al principio del proceso. La aplicación todavía puede fallar después al crear beans o al enlazar el puerto. Debemos buscar la finalización correcta del contexto y del servidor.

## Paso 10 - Probar la raíz

Abre:

```text
http://localhost:8080/
```

En este momento todavía no hemos creado un endpoint `/`. Es razonable recibir un 404.

### Pregunta

¿Por qué un 404 no es equivalente a “Spring Boot no funciona”?

### Respuesta razonada

Porque un 404 significa que el servidor ha atendido la petición y no ha encontrado un manejador para esa ruta. Si Spring Boot no hubiera arrancado o nadie escuchara en 8080, el navegador mostraría un fallo de conexión, no una respuesta HTTP 404 generada por la aplicación.

## Paso 11 - Diagnóstico de creación/importación

| Síntoma | Causa | Acción |
|---|---|---|
| `pom.xml` rojo | importación Maven incompleta | recargar Maven |
| JDK no válida | SDK del IDE incorrecto | seleccionar JDK 17/compatible |
| 8080 ocupado | otro proceso escucha | parar proceso o cambiar temporalmente puerto |
| dependencias no descargan | red/proxy/repositorio | revisar error Maven, no editar Java |
| `main` no arranca | SDK o configuración | comparar con `./mvnw spring-boot:run` |
| `/` devuelve 404 | no existe mapping `/` | comportamiento esperado |

## Paso 12 - Reto resuelto: añadir DevTools manualmente

Abre `pom.xml` y localiza `<dependencies>`.

Añade:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Explicación

`groupId` identifica el grupo Spring Boot.

`artifactId` identifica DevTools.

`runtime` expresa que no necesitamos compilar nuestro código contra clases de DevTools.

`optional` evita propagar esta dependencia como requisito transitivo si nuestro proyecto se utilizara como dependencia de otro.

Guarda el POM y recarga Maven.

Comprueba desde terminal:

Linux/macOS:

```bash
./mvnw dependency:tree
```

Windows:

```cmd
mvnw.cmd dependency:tree
```

Busca `spring-boot-devtools`.

### Pregunta

¿Por qué añadimos la dependencia editando el POM en vez de volver a generar el proyecto completo?

### Respuesta razonada

Porque un proyecto Maven está diseñado para evolucionar. El POM es la declaración del build; añadir una dependencia es una modificación normal. Regenerar el proyecto podría sobrescribir trabajo y no enseña cómo se mantienen proyectos reales.

## Variante adicional - Crear el mismo proyecto desde IntelliJ, Eclipse, VS Code y consola

La web de Spring Initializr es la referencia principal del curso porque expone los campos con claridad. Aun así, debes saber crear **el mismo proyecto** desde tu entorno habitual. El resultado debe conservar las mismas decisiones técnicas: Maven, Java, Spring Boot, coordenadas, paquete, JAR y Spring Web.

### IntelliJ IDEA

1. Abre `File > New > Project`.
2. Selecciona el asistente de Spring/Initializr disponible en tu edición.
3. Elige Java y Maven.
4. Configura JDK 17.
5. Introduce `es.mecd.demo` como Group y `mi-proyecto` como Artifact.
6. Usa `es.mecd.demo.miproyecto` como paquete.
7. Selecciona JAR.
8. Añade **Spring Web**.
9. Finaliza la creación.
10. Abre `pom.xml` y comprueba que las decisiones coinciden con la baseline del curso.

Si la edición de IntelliJ que utilizas no ofrece el asistente de Spring, genera el ZIP desde la web y ábrelo con `File > Open`. La ausencia del asistente no cambia el proyecto Maven.

### Eclipse + Spring Tools

1. `File > New > Spring Starter Project`.
2. Selecciona Maven y Java.
3. Comprueba JDK 17.
4. Introduce nombre, group, artifact y package.
5. Elige JAR.
6. En la pantalla de dependencias selecciona **Spring Web**.
7. Finaliza el asistente.
8. Espera la importación Maven.
9. Abre `pom.xml` y verifica las coordenadas y la dependencia.

Si `Spring Starter Project` no aparece, comprueba que Spring Tools esté instalado o utiliza la web y `Import > Maven > Existing Maven Projects`.

### VS Code

1. Abre la Command Palette.
2. Ejecuta `Spring Initializr: Create a Maven Project`.
3. Selecciona la línea Spring Boot indicada por el curso si está disponible.
4. Elige Java.
5. Introduce `es.mecd.demo` como Group.
6. Introduce `mi-proyecto` como Artifact.
7. Selecciona JAR y Java 17.
8. Añade Spring Web.
9. Elige una carpeta donde generar el proyecto.
10. Abre la carpeta generada y espera la importación del Java Language Server.

La secuencia exacta de preguntas puede variar con la versión de la extensión. La verificación final siempre es el POM generado.

### Consola mediante Spring Initializr

La API de Initializr permite automatizar la creación. Una forma práctica es descargar el ZIP con `curl`. Como los parámetros admitidos pueden evolucionar, consulta primero los metadatos del servicio si estás automatizando en un entorno real. El patrón conceptual es:

```bash
curl -G https://start.spring.io/starter.zip \
  --data-urlencode type=maven-project \
  --data-urlencode language=java \
  --data-urlencode groupId=es.mecd.demo \
  --data-urlencode artifactId=mi-proyecto \
  --data-urlencode name=mi-proyecto \
  --data-urlencode packageName=es.mecd.demo.miproyecto \
  --data-urlencode packaging=jar \
  --data-urlencode javaVersion=17 \
  --data-urlencode dependencies=web \
  -o mi-proyecto.zip
```

Después extrae el ZIP y abre la carpeta que contiene `pom.xml`.

**Importante:** para la edición final del curso el proyecto de referencia del repositorio fija el POM exacto. Este ejemplo de API enseña el mecanismo; si el servicio retira una versión histórica concreta de Spring Boot, no debes interpretar que el curso ha cambiado automáticamente de baseline.

### Verificación común de las cuatro vías

Independientemente del canal, abre `pom.xml` y confirma:

```text
groupId        = es.mecd.demo
artifactId     = mi-proyecto
java.version   = 17
packaging      = jar
Spring Web     = presente
```

Luego ejecuta desde la raíz:

```bash
./mvnw -version
./mvnw test
```

En Windows:

```cmd
mvnw.cmd -version
mvnw.cmd test
```

Si los cuatro canales producen un proyecto equivalente, el aprendizaje importante es que **Initializr no pertenece a un IDE**. Los IDEs consumen el mismo servicio o importan el mismo resultado.

### Pregunta

¿Qué archivo compararías primero para decidir si dos proyectos creados con canales distintos tienen realmente la misma configuración de build?

### Respuesta razonada

`pom.xml`, porque describe coordenadas, parent, Java, dependencias y plugins Maven. Después compararía estructura de fuentes y configuración. Que las carpetas se vean distintas en el panel de un IDE no significa que el proyecto Maven sea diferente.

---

## Resultado esperado de la práctica 0.2

Debes tener un proyecto Spring Boot 3.5.16 importado, capaz de arrancar y con Spring Web, DevTools y soporte de test declarados en el POM.

---
