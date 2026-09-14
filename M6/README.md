# M6 — Spring Security y JWT

Estado editorial/técnico: **COMPLETE — 112/112 pasos y todos los manifiestos internos en COMPLETE; pendiente únicamente de ejecutar el gate ensamblado de publicación en una copia fresca de `main`**.

M6 evoluciona acumulativamente el proyecto final de M5 hacia una API protegida con Spring Security y JWT stateless. Conserva JPA, errores estándar, `traceId`, perfiles, CORS, OpenAPI y todos los artefactos M5; añade usuarios/roles persistentes, autorización por URL/método, login, access/refresh tokens, filtro JWT, errores 401/403 JSON, logout/revocación y testing de seguridad.

## Cobertura

- Fuente: páginas 1203–1550 (348 páginas); M7 empieza en 1551.
- Teoría: **45/45 bloques principales y 135/135 subpuntos**.
- Práctica: **112/112 pasos**.
- Trazabilidad: **schema v3, 112/112 cadenas reversibles**.
- Tests nuevos M6: **26**; total esperado tras ensamblar con M5: **58** (32 heredados + 26 nuevos).

## Stack final

- Java 17
- Spring Boot 3.5.16
- Maven Wrapper 3.9.16
- Spring Security con `SecurityFilterChain` y `@EnableMethodSecurity`
- JJWT 0.13.0
- JPA + H2/PostgreSQL
- `@MockitoBean`; no `@MockBean` en la solución
- API JWT final `STATELESS`; HTTP Basic eliminado

## Flujo final

```text
registro/login -> access + refresh
access Bearer -> JwtAuthenticationFilter -> UsuarioPrincipal -> SecurityContext
SecurityContext -> reglas URL + @PreAuthorize -> recurso / 401 / 403
refresh -> token de un solo uso -> nuevo par
logout -> revocación temporal del JTI del access token
```

## Comandos después del ensamblado

```bash
cd M6/proyecto
chmod +x mvnw
./mvnw -B clean verify
./mvnw -B javadoc:javadoc
./mvnw -B -DskipTests package
```

Desde la raíz:

```bash
python .course/traceability/validate_m6.py .
python .course/traceability/validate_m6_human.py .
python .course/traceability/validate_m6_mutation.py .
python .course/traceability/validate_m6_runtime.py .
```

## Producción

El perfil `prod` **no** contiene una clave JWT de fallback. Debe proporcionarse `JWT_SECRET` en Base64 y con al menos 256 bits efectivos. El blacklist/replay store incluido es deliberadamente en memoria para el curso y debe sustituirse por almacenamiento compartido (por ejemplo Redis) si varias instancias necesitan revocación coordinada.
