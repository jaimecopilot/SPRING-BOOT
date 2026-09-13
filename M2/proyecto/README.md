# Mi Proyecto — Módulo 2

Proyecto acumulativo del curso Spring Boot 2026 al finalizar M2.

## Requisitos

- Java 17
- Maven Wrapper incluido (Maven 3.9.16)

## Compilar y probar

```bash
./mvnw test
./mvnw verify
```

En Windows usa `mvnw.cmd`.

## Ejecutar

```bash
./mvnw spring-boot:run
```

## Perfiles

Desarrollo:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Producción de ejemplo:

```bash
./mvnw -DskipTests package
java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Arquitectura

```text
HTTP -> Controller -> Service -> Repository -> memoria
```

El código se organiza por funcionalidad (`alumno`, `expediente`, `info`, `saludo`) y mantiene configuración y utilidades comunes separadas.

## Endpoints principales

- `GET /hola`
- `GET /adios`
- `GET|POST /api/v1/alumnos`
- `GET|PUT|PATCH|DELETE /api/v1/alumnos/{id}`
- `GET /api/v1/alumnos/info-peticion`
- `POST /api/v1/alumnos/promocionar`
- `GET|POST /api/v1/expedientes`
- `GET|PUT|DELETE /api/v1/expedientes/{id}`
- `GET /api/v1/expedientes/ejemplo`
- `POST /api/v1/expedientes/eco`
- `GET /api/v1/info`

## Documentación API

- OpenAPI JSON: `/v3/api-docs`
- Swagger UI: `/swagger-ui.html`

## Calidad y documentación

```bash
./mvnw checkstyle:check
./mvnw javadoc:javadoc
```

Checkstyle forma parte de `verify`. Los tests unitarios y web están bajo `src/test/java`.
