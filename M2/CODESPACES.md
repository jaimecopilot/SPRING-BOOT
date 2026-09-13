# M2 - Guía para ejecutar el proyecto en Codespaces

Esta guía te explica cómo levantar un **Codespace** y ejecutar el proyecto de **Módulo 2** paso a paso.

> **Prerequisito:** Asegúrate de haber completado M0 y M1 antes de comenzar con M2.

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
Si acabas de terminar M1:
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

## Paso 3: Navega al proyecto M2

Desde cualquier ubicación, navega a M2:

```bash
cd M2/proyecto
```

Si venías de M1, simplemente:
```bash
cd ../M2/proyecto
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

## Paso 7: Prueba los endpoints de M2

Abre una **nueva terminal** (sin cerrar la anterior) y prueba los endpoints:

### Ver todos los alumnos
```bash
curl -i http://localhost:8080/api/v1/alumnos
```

### Ver un alumno por ID
```bash
curl -i http://localhost:8080/api/v1/alumnos/1
```

### Crear un nuevo alumno
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","apellidos":"Pérez","dni":"12345678A","fechaNacimiento":"2010-05-15","curso":"1º"}' \
  http://localhost:8080/api/v1/alumnos
```

### Actualizar un alumno (PUT/PATCH)
```bash
curl -X PUT -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","apellidos":"García","dni":"12345678A","fechaNacimiento":"2010-05-15","curso":"2º"}' \
  http://localhost:8080/api/v1/alumnos/1
```

### Eliminar un alumno
```bash
curl -X DELETE http://localhost:8080/api/v1/alumnos/1
```

---

## Paso 8: Explorar la estructura de M2

En VS Code, navega por los archivos del proyecto:

```
M2/proyecto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── es/mecd/demo/miproyecto/
│   │   │       ├── alumno/
│   │   │       │   ├── Alumno.java (entidad JPA)
│   │   │       │   ├── AlumnoController.java
│   │   │       │   ├── AlumnoService.java
│   │   │       │   ├── AlumnoRepository.java (JpaRepository)
│   │   │       │   ├── AlumnoDTO.java
│   │   │       │   └── AlumnoMapper.java
│   │   │       ├── expediente/
│   │   │       │   ├── Expediente.java
│   │   │       │   ├── ExpedienteController.java
│   │   │       │   ├── ExpedienteService.java
│   │   │       │   └── ExpedienteRepository.java
│   │   │       └── MiProyectoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/... (tests)
└── pom.xml
```

**M2 introduce:**
- **JPA/Hibernate:** Mapeo objeto-relacional
- **DTOs:** Separación entre entidad y transferencia de datos
- **Relaciones:** One-to-Many, Many-to-One
- **Mappers/Conversores:** Convertir entre entidades y DTOs
- **Properties de configuración:** `application.properties`

---

## Paso 9: Ejecutar tests

M2 incluye tests unitarios e integración. Ejecútalos con:

```bash
./mvnw test
```

O un test específico:

```bash
./mvnw test -Dtest=AlumnoServiceTest
```

---

## Detener la aplicación

- En la terminal donde corre Spring Boot, presiona **`Ctrl + C`**
- El servidor se detendrá

---

## Pasos futuros (M3)

Cuando termines M2:

1. Cierra el servidor (Ctrl + C)
2. Navega a la siguiente carpeta:
   ```bash
   cd ../M3/proyecto
   ```
3. Repite desde el **Paso 4**

---

## Solución de problemas

### ❌ "mvnw: command not found"
**Solución:** Asegúrate de estar en la carpeta correcta:
```bash
pwd  # Debería mostrar: .../M2/proyecto
```

### ❌ "Java not found"
**Solución:** Codespaces debería tener Java preinstalado. Si no, cierra el Codespace y abre uno nuevo.

### ❌ Puerto 8080 ya está en uso
**Solución:** Detén el servidor anterior (Ctrl + C) o Spring Boot usará otro puerto automáticamente.

### ❌ Errores con la base de datos
**Solución:** M2 usa una BD en memoria (H2 por defecto). Verifica `application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
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
- 📂 **Explora relaciones:** Los DTOs en M2 mapean relaciones entre entidades

---

## ¿Qué hace M2?

M2 profundiza en la **persistencia de datos** y la **arquitectura real** de Spring Boot:

- **JPA/Hibernate:** ORM para mapear entidades a tablas
- **DTOs:** Separación clara entre modelo y API
- **Mappers:** Conversores entre entidades y DTOs
- **Relaciones:** One-to-Many, Many-to-One con anotaciones JPA
- **Propiedades de configuración:** Perfiles (dev, prod, test)
- **Tests:** Unitarios e integración

**Conceptos clave:**
- `@Entity`, `@Id`, `@GeneratedValue`
- `@OneToMany`, `@ManyToOne`
- `JpaRepository` (extends de Spring Data)
- DTOs con `@Valid` (validación)

Consulta **TEORIA.md** y **PRACTICA.md** en esta carpeta para detalles.

---

**¡Listo para M2! 🚀**
