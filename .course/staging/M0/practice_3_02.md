```java
@GetMapping("/adios")
public String despedir() {
    return "Adiós, Ministerio de Educación";
}
```

Comprueba:

```bash
curl http://localhost:8080/adios
```

Resultado:

```text
Adiós, Ministerio de Educación
```

### Pregunta

¿Podríamos crear `POST /adios` con `@PostMapping` y mantener a la vez `GET /adios`?

### Respuesta razonada

Sí. Método HTTP y ruta forman conjuntamente el mapping. Serían operaciones distintas. Que técnicamente pueda hacerse no significa que sea un buen diseño REST; ese criterio lo estudiaremos en el Módulo 1.

## Variante adicional - Crear paquetes y clases en los cuatro entornos

En la Práctica 0.3 creamos `controller` y `SaludoController`. La operación es conceptualmente la misma en todos los entornos, pero conviene dominar cómo se realiza en cada uno.

### Desde consola y editor de texto

Crea la ruta de paquete bajo `src/main/java`:

Linux/macOS:

```bash
mkdir -p src/main/java/es/mecd/demo/miproyecto/controller
```

PowerShell:

```powershell
New-Item -ItemType Directory -Force src/main/java/es/mecd/demo/miproyecto/controller
```

Después crea `SaludoController.java` con tu editor. La primera línea debe coincidir con la ruta lógica:

```java
package es.mecd.demo.miproyecto.controller;
```

Compila con Maven para comprobar que ruta, package, imports y sintaxis son válidos:

```bash
./mvnw test
```

### IntelliJ IDEA

1. Expande `src/main/java`.
2. Clic derecho sobre `es.mecd.demo.miproyecto`.
3. `New > Package`.
4. Escribe `controller`.
5. Clic derecho sobre el nuevo paquete.
6. `New > Java Class`.
7. Escribe `SaludoController`.
8. Pega o escribe el código del paso.

IntelliJ genera la declaración `package` a partir de la ubicación. Aun así, compruébala: el IDE facilita el trabajo, pero el package sigue siendo Java real.

### Eclipse

1. Expande `src/main/java`.
2. Clic derecho sobre el paquete raíz.
3. `New > Package`.
4. Crea `es.mecd.demo.miproyecto.controller`.
5. Clic derecho sobre el paquete.
6. `New > Class`.
7. Nombre: `SaludoController`.
8. Finaliza y escribe el código.

Eclipse también mantiene sincronía entre el package elegido y la declaración Java. Si mueves una clase, prefiere las funciones de refactorización a arrastrar archivos sin revisar el package.

### VS Code

Puedes trabajar desde el Explorer o desde la terminal integrada.

**Explorer:** crea las carpetas bajo `src/main/java/es/mecd/demo/miproyecto/` y después el archivo `SaludoController.java`.

**Java Project view:** según las extensiones instaladas, podrás crear clases/packages desde las acciones Java. Si no aparece esa opción, no es un problema: la estructura de carpetas y la declaración `package` son lo que Java necesita.

Después ejecuta:

```bash
./mvnw test
```

### Por qué enseñamos también la consola

La creación gráfica de una clase puede ocultar la relación entre:

```text
src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java
```

y:

```java
package es.mecd.demo.miproyecto.controller;
```

Crear una vez la estructura manualmente ayuda a comprender que el IDE no inventa packages: representa una convención de directorios y nombres que Java y Maven conocen.

### Pregunta

¿Un package Java y una carpeta son exactamente lo mismo?

### Respuesta razonada

No conceptualmente: el package es parte del nombre lógico de una clase Java. En la estructura convencional de Maven, ese nombre se refleja en directorios bajo `src/main/java`, y las herramientas esperan esa correspondencia. El IDE nos ayuda a mantenerla, pero el concepto pertenece a Java, no al IDE.

## Resultado esperado de la práctica 0.3

El proyecto contiene una clase principal, configuración, un controlador con dos endpoints y una estructura que Maven y Spring reconocen correctamente.

---
