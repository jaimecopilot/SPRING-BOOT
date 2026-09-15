# M6 - Spring Security y JWT

Este módulo añade seguridad a la API acumulativa del curso: autenticación, autorización por URL y método, usuarios y roles persistentes, JWT stateless, refresh, logout/revocación, CORS y pruebas de seguridad.

## Requisitos

- Java 17.
- Maven Wrapper incluido en `proyecto/`.
- Perfil `dev` para pruebas locales con H2.
- En producción, `JWT_SECRET` debe proporcionarse como variable de entorno y no existe fallback embebido.

## Ejecutar y verificar

```bash
cd proyecto
./mvnw clean verify
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

En Windows utiliza `mvnw.cmd`.

## Flujo de seguridad final

```text
registro/login -> access token + refresh token
Bearer access -> JwtAuthenticationFilter -> SecurityContext
SecurityContext -> reglas URL + @PreAuthorize -> recurso / 401 / 403
refresh -> token de un solo uso -> nuevo par
logout -> revocación temporal del JTI del access token
```

## Puntos principales

- `SecurityFilterChain` y `@EnableMethodSecurity`.
- Passwords con `PasswordEncoder`.
- Usuarios y roles en base de datos.
- JWT con JJWT 0.13.0.
- `SessionCreationPolicy.STATELESS`.
- Respuestas 401/403 con el contrato de error común.
- Rutas OpenAPI y CORS compatibles con la seguridad.
- Tests de servicio, controlador e integración.
