# API de Gestión de Becas - Ministerio de Educación

API REST educativa para gestionar becas, alumnos, solicitudes, documentos, usuarios y roles. El proyecto del Módulo 7 es autónomo y no depende del código de M1-M6.

## Requisitos previos

- Java 17.
- Maven Wrapper incluido (`mvnw` / `mvnw.cmd`).

## Compilación y ejecución

Linux, macOS, Git Bash o WSL:

```bash
./mvnw clean package
java -jar target/api-becas-0.0.1-SNAPSHOT.jar
```

Windows CMD o PowerShell:

```bat
mvnw.cmd clean package
java -jar target\api-becas-0.0.1-SNAPSHOT.jar
```

También puede arrancarse directamente con el wrapper:

```bash
./mvnw spring-boot:run
```

El perfil por defecto es `dev`: utiliza H2 en memoria y carga datos y usuarios de prueba.

## Tests y cobertura

```bash
./mvnw clean verify
```

El informe JaCoCo queda en `target/site/jacoco/index.html`.

## Documentación de la API

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Consola H2: `http://localhost:8080/h2-console`

En Swagger UI, usa **Authorize** con el JWT de acceso obtenido en `POST /api/v1/auth/login` para probar los endpoints protegidos. El esquema OpenAPI se publica como `bearerAuth` de tipo HTTP Bearer con formato JWT.

## Usuarios de prueba (`dev`)

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | `ADMIN` |
| `gestor` | `gestor123` | `GESTOR` |
| `ciudadano` | `ciudadano123` | `CIUDADANO` |
| `user` | `user12345` | `USER` |
| `consultor` | `consultor123` | `CONSULTOR` |

## Endpoints principales

### Autenticación

| Método | URL | Acceso |
|---|---|---|
| POST | `/api/v1/auth/registro` | Público |
| POST | `/api/v1/auth/login` | Público |
| POST | `/api/v1/auth/refresh` | Público |

### Becas

| Método | URL | Acceso |
|---|---|---|
| GET | `/api/v1/becas` | Público |
| GET | `/api/v1/becas/{id}` | Público |
| POST | `/api/v1/becas` | ADMIN |
| PUT | `/api/v1/becas/{id}` | ADMIN |
| DELETE | `/api/v1/becas/{id}` | ADMIN |

### Solicitudes

| Método | URL | Acceso |
|---|---|---|
| GET | `/api/v1/solicitudes` | CIUDADANO, GESTOR, ADMIN, CONSULTOR |
| GET | `/api/v1/solicitudes/{id}` | CIUDADANO, GESTOR, ADMIN, CONSULTOR |
| POST | `/api/v1/solicitudes` | CIUDADANO |
| PATCH | `/api/v1/solicitudes/{id}/estado` | GESTOR, ADMIN |
| DELETE | `/api/v1/solicitudes/{id}` | CIUDADANO |

### Documentos

| Método | URL | Acceso |
|---|---|---|
| GET | `/api/v1/solicitudes/{id}/documentos` | Autenticado |
| POST | `/api/v1/solicitudes/{id}/documentos` | CIUDADANO |
| DELETE | `/api/v1/solicitudes/{id}/documentos/{docId}` | CIUDADANO |

### Usuarios

| Método | URL | Acceso |
|---|---|---|
| GET | `/api/v1/usuarios` | ADMIN |
| GET | `/api/v1/usuarios/{id}` | ADMIN |
| PATCH | `/api/v1/usuarios/{id}/roles` | ADMIN |

### Estadísticas

| Método | URL | Acceso |
|---|---|---|
| GET | `/api/v1/estadisticas/solicitudes-por-estado` | GESTOR, ADMIN |

## Postman

La colección y el entorno local están en `postman/`:

- `API_Becas_Ministerio.postman_collection.json`
- `API_Becas_Local.postman_environment.json`

## Estructura del código

```text
src/main/java/es/mecd/demo/miproyecto/
├── auth/        -> autenticación, JWT, usuarios y roles
├── alumno/      -> gestión de alumnos
├── beca/        -> gestión de becas
├── solicitud/   -> solicitudes y estadísticas
├── documento/   -> documentos
├── config/      -> seguridad, OpenAPI y datos iniciales
└── common/      -> DTOs de error y excepciones comunes
```

## Verificación final del módulo

Desde el paquete de cierre R6 de M7, ejecuta `EJECUTAR_M7_R6_FINAL.bat`. Ese gate vuelve a ejecutar validación estática, `mvnw.cmd -B clean verify`, cobertura JaCoCo, comprobación del JAR y smoke HTTP de autenticación, roles, CORS y OpenAPI.
