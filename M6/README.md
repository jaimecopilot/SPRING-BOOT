# Módulo 6 — Seguridad con Spring Security y JWT

## Qué hace este módulo

M6 incorpora seguridad a la aplicación acumulativa de M5. El objetivo es proteger la API, autenticar usuarios, autorizar operaciones según roles y utilizar JWT para trabajar de forma stateless.

## Qué aprenderás

- Diferencia entre autenticación y autorización y entre respuestas `401` y `403`.
- Funcionamiento de la cadena de filtros de Spring Security.
- Configuración moderna mediante `SecurityFilterChain`.
- Uso de `AuthenticationManager`, `UserDetailsService` y `PasswordEncoder`.
- Modelado de usuarios y roles.
- Login y emisión de tokens JWT.
- Validación de tokens mediante filtros personalizados.
- Autorización por rutas, roles y reglas de seguridad.
- Pruebas de seguridad.

## Qué construyes

Evolucionas la API de M5 hasta disponer de una aplicación protegida con Spring Security y JWT, con autenticación, roles, filtros, endpoints protegidos y una batería de pruebas de seguridad.

## Organización del módulo

M6 conserva snapshots acumulativos por punto:

- `6.1/` a `6.9/` — estados sucesivos del proyecto durante la construcción.
- [`TEORIA.md`](TEORIA.md) — explicación completa del diseño de seguridad.
- [`PRACTICA.md`](PRACTICA.md) — práctica guiada del módulo.
- [`M6_TEORIA.pdf`](M6_TEORIA.pdf) y [`M6_PRACTICA.pdf`](M6_PRACTICA.pdf) — versiones maquetadas.
- [`TRAZABILIDAD.md`](TRAZABILIDAD.md) — correspondencia teoría/práctica/código.

## Continuidad

M6 continúa desde M5 y cierra la evolución acumulativa M0→M6. M7 cambia deliberadamente de modelo: es un proyecto global nuevo y standalone que reutiliza los conocimientos adquiridos, no el código de M6.

[Volver al índice general](../README.md)

