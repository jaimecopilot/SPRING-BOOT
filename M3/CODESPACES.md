# M3 - Guía para ejecutar el proyecto en Codespaces

Esta guía te explica cómo levantar un **Codespace** y ejecutar el proyecto de **Módulo 3** paso a paso.

> **Prerequisito:** Asegúrate de haber completado M0, M1 y M2 antes de comenzar con M3.

---

## ¿Es gratis?

✅ **Sí, es completamente gratis** (hasta 60 horas/mes en cuentas personales).

---

## Paso 1: Abrir el Codespace

### Opción A: Crear un nuevo Codespace
1. Ve a la página principal del repositorio: https://github.com/jaimecopilot/SPRING-BOOT
2. Haz clic en el botón verde **`<> Code`**
3. Selecciona la pestaña **`Codespaces`**
4. Haz clic en **`Create codespace on main`**

### Opción B: Reutilizar el Codespace anterior
Si acabas de terminar M2:
1. Ve a https://github.com/codespaces
2. Selecciona tu Codespace existente
3. Presiona **Ctrl + Shift + P** y busca **"Terminal: New Terminal"** para abrir una nueva terminal

> **Nota:** GitHub tardará 30-60 segundos en crear un nuevo Codespace. Una vez listo, verás VS Code en el navegador.

---

## Paso 2: Verificar Java y Maven

Abre la terminal integrada:

### Abre la terminal
- Presiona **`Ctrl + ~`** (o **`Cmd + ~`** en Mac)
- O ve a **Terminal** → **New Terminal**

### Verifica que Java 17 está instalado
```bash
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" ...
```

---

## Paso 3: Navega al proyecto M3

Desde cualquier ubicación, navega a M3:

```bash
cd M3/proyecto
```

Si venías de M2, simplemente:
```bash
cd ../M3/proyecto
```

---

## Paso 4: Instala las dependencias (primera vez)

Ejecuta Maven para descargar todas las dependencias:

```bash
./mvnw clean install
```

> **Nota:** Esto puede tardar 2-3 minutos la primera vez. Después será más rápido.

---

## Paso 5: Inicia la aplicación Spring Boot

Ejecuta el servidor:

```bash
./mvnw spring-boot:run
```

Verás en la consola algo como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v3.x.x)

Started MiProyectoApplication in X.XXX seconds
```

✅ **¡Listo! Tu aplicación está corriendo.**

---

## Paso 6: Accede a la aplicación

### Opción A: Usar el puerto forwarding automático
1. En la barra inferior de VS Code, verás un indicador de puerto (ej: `8080`)
2. Haz clic en él o presiona **`Ctrl + Shift + P`**
3. Busca **"Ports: Focus on Ports View"**
4. Haz clic en el icono de globo para abrir en el navegador

### Opción B: Acceso manual
- Copia la URL que Codespaces generó (algo como `https://username-xxxx.github.dev/`)
- Añade el puerto 8080: `https://username-xxxx.github.dev:8080`

---

## Paso 7: Prueba los endpoints REST de M3

Abre una **nueva terminal** (sin cerrar la anterior) y prueba los endpoints:

### GET - Obtener todos los alumnos con paginación
```bash
curl -i "http://localhost:8080/api/v1/alumnos?page=0&size=10"
```

### GET - Obtener todos los alumnos con filtro, ordenación y paginación
```bash
curl -i "http://localhost:8080/api/v1/alumnos?nombre=Juan&sort=apellidos&page=0&size=10"
```

### GET - Obtener un alumno por ID
```bash
curl -i http://localhost:8080/api/v1/alumnos/1
```

### POST - Crear un nuevo alumno (201 Created con cabecera Location)
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"11111111C","fechaNacimiento":"2011-03-20","curso":"4º"}' \
  http://localhost:8080/api/v1/alumnos
```

### PUT - Reemplazar un alumno completo
```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","apellidos":"García","dni":"12345678A","fechaNacimiento":"2010-05-15","curso":"2º"}' \
  http://localhost:8080/api/v1/alumnos/1
```

### PATCH - Actualizar solo algunos campos
```bash
curl -X PATCH -H "Content-Type: application/json" \
  -d '{"curso":"3º"}' \
  http://localhost:8080/api/v1/alumnos/1
```

### DELETE - Eliminar un alumno
```bash
curl -X DELETE http://localhost:8080/api/v1/alumnos/1
```

---

## Paso 8: Usar Postman para pruebas más avanzadas

M3 incluye colecciones de Postman preconfiguradas en `M3/postman/`:

### Opción A: Importar en Postman Desktop
1. Descarga Postman desde https://www.postman.com/downloads/
2. Ve a **File** → **Import**
3. Selecciona `M3/postman/M3.postman_collection.json`
4. Importa también el entorno: `M3/postman/M3.local.postman_environment.json`
5. Selecciona el entorno "M3 local" en la esquina superior derecha
6. Ejecuta las peticiones preconfiguradas

### Opción B: Usar Postman Web (sin instalación)
1. Ve a https://web.postman.co/
2. Inicia sesión con tu cuenta
3. Importa las colecciones (JSON)
4. Prueba los endpoints

---

## Paso 9: Explorar la estructura de M3

En VS Code, navega por los archivos del proyecto:

```
M3/proyecto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── es/mecd/demo/miproyecto/
│   │   │       ├── alumno/
│   │   │       │   ├── Alumno.java (entidad)
│   │   │       │   ├── AlumnoController.java (REST completo)
│   │   │       │   ├── AlumnoService.java
│   │   │       │   ├── AlumnoRepository.java
│   │   │       │   ├── AlumnoRequestDTO.java
│   │   │       │   ├── AlumnoResponseDTO.java
│   │   │       │   └── AlumnoMapper.java
│   │   │       ├── expediente/
│   │   │       │   ├── Expediente.java
│   │   │       │   ├── ExpedienteController.java
│   │   │       │   ├── ExpedienteService.java
│   │   │       │   ├── ExpedienteRepository.java
│   │   │       │   ├── ExpedienteRequestDTO.java
│   │   │       │   └── ExpedienteResponseDTO.java
│   │   │       ├── config/
│   │   │       │   └── GlobalExceptionHandler.java (manejo de errores)
│   │   │       └── MiProyectoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/...
├── postman/
│   ├── M3.postman_collection.json
│   └── M3.local.postman_environment.json
└── pom.xml
```

**M3 introduce:**
- **Operaciones REST completas:** GET (lectura), POST (crear), PUT (reemplazar), PATCH (actualizar), DELETE (eliminar)
- **DTOs de entrada y salida:** Validación y serialización
- **Filtrado, ordenación y paginación:** Parámetros de query avanzados
- **Códigos de estado HTTP:** 200, 201, 204, 400, 404, 409, 500
- **Cabeceras HTTP:** Location, Content-Type, Custom headers
- **Manejo global de excepciones:** `@ControllerAdvice`
- **Bean Validation:** `@Valid`, `@NotBlank`, `@NotNull`, etc.

---

## Paso 10: Ejecutar tests

M3 incluye tests exhaustivos. Ejecútalos con:

```bash
./mvnw test
```

O un test específico:

```bash
./mvnw test -Dtest=AlumnoControllerTest
```

Ver cobertura de tests:

```bash
./mvnw test jacoco:report
```

---

## Paso 11: Validación con Bean Validation

M3 usa anotaciones de validación en los DTOs:

### Probar validación fallida
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"nombre":"","apellidos":"López","dni":"11111111C","fechaNacimiento":"2011-03-20","curso":"4º"}' \
  http://localhost:8080/api/v1/alumnos
```

Recibirás un error `400 Bad Request` con detalles de validación.

---

## Paso 12: Manejo de errores global

M3 incluye `GlobalExceptionHandler` que captura excepciones y devuelve respuestas consistentes:

### Probar error 404 (no encontrado)
```bash
curl -i http://localhost:8080/api/v1/alumnos/9999
```

### Probar error 409 (conflicto - DNI duplicado)
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"nombre":"Otro","apellidos":"Apellido","dni":"12345678A","fechaNacimiento":"2010-05-15","curso":"1º"}' \
  http://localhost:8080/api/v1/alumnos
```

---

## Detener la aplicación

- En la terminal donde corre Spring Boot, presiona **`Ctrl + C`**
- El servidor se detendrá

---

## Solución de problemas

### ❌ "mvnw: command not found"
**Solución:** Asegúrate de estar en la carpeta correcta:
```bash
pwd  # Debería mostrar: .../M3/proyecto
```

### ❌ "Java not found"
**Solución:** Codespaces debería tener Java preinstalado. Si no, cierra el Codespace y abre uno nuevo.

### ❌ Puerto 8080 ya está en uso
**Solución:** Detén el servidor anterior (Ctrl + C) o Spring Boot usará otro puerto automáticamente.

### ❌ Errores de validación inesperados
**Solución:** Verifica que los DTOs tienen las anotaciones correctas:
```java
@NotBlank(message = "El nombre es obligatorio")
private String nombre;
```

### ❌ Falla la descarga de dependencias
**Solución:** Intenta:
```bash
./mvnw clean
./mvnw install
```

---

## Consejos útiles

- 💾 **Tu trabajo se guarda automáticamente** en Codespaces
- 🔄 **Vuelve al Codespace anterior:** Ve a https://github.com/codespaces y selecciona tu Codespace
- ⏱️ **Libre 60 horas/mes:** Codespaces se pausa automáticamente después de 30 min de inactividad
- 📊 **Accede a H2 Console:** http://localhost:8080/h2-console (si está habilitada)
- 🧪 **Ejecuta tests con cobertura:** `./mvnw test jacoco:report`
- 📋 **Usa jq para formatear JSON:** `curl http://localhost:8080/api/v1/alumnos | jq`
- 📑 **Documentación automática:** Muchos proyectos Spring Boot incluyen Swagger/OpenAPI en `/swagger-ui.html`

---

## ¿Qué hace M3?

M3 es el **módulo completo de APIs REST** con Spring Boot:

### Operaciones CRUD
- **CREATE (POST):** Crea recursos, devuelve 201 con cabecera Location
- **READ (GET):** Lee recursos individuales o colecciones con filtrado/paginación
- **UPDATE (PUT/PATCH):** Actualiza completa o parcialmente
- **DELETE:** Elimina recursos

### Características avanzadas
- **Validación:** Bean Validation con `@Valid`
- **Filtrado:** Por campos específicos
- **Ordenación:** Múltiples criterios
- **Paginación:** Con metadatos (total, página, tamaño)
- **Códigos de estado:** Semánticamente correctos
- **Manejo de errores:** Global y consistente
- **DTOs:** Entrada/salida separados
- **Cabeceras:** Location, Content-Type personalizadas

**Conceptos clave:**
- `@RestController` con métodos completos
- `@RequestBody`, `@PathVariable`, `@RequestParam`
- `ResponseEntity<T>` para control fino de respuestas
- `@Valid` y validadores personalizados
- `@ControllerAdvice` para manejo global
- Relaciones bidireccionales con DTOs

Consulta **TEORIA.md** y **PRACTICA.md** en esta carpeta para detalles.

---

## Próximos pasos (después de M3)

Felicidades por completar M0 → M3. Ahora dominan:

✅ Fundamentos de Spring Boot  
✅ Arquitectura en capas  
✅ Persistencia con JPA  
✅ **APIs REST completas**

Pueden explorar:
- 🔐 **Seguridad:** Spring Security, JWT, OAuth2
- 📦 **Microservicios:** Spring Cloud, Eureka, Config Server
- 📡 **APIs asincrónicas:** WebSockets, RabbitMQ, Kafka
- 🧪 **Testing avanzado:** MockMvc, Testcontainers
- 📚 **Documentación:** Swagger/OpenAPI, JavaDoc
- 🚀 **Despliegue:** Docker, Kubernetes, Cloud (AWS, GCP, Azure)

---

**¡Felicidades por llegar a M3! 🎉🚀**
