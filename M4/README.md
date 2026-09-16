# Módulo 4 — Persistencia de datos con JPA / Hibernate

## Qué hace este módulo

M4 sustituye la persistencia en memoria del proyecto de M3 por una capa de persistencia relacional real basada en JPA, Hibernate y Spring Data JPA, manteniendo el contrato REST ya construido.

## Qué aprenderás

- Diferencias entre JPA, Hibernate y Spring Data JPA.
- Modelado de entidades y mapeo objeto-relacional.
- Repositorios JPA y operaciones de persistencia.
- Relaciones entre entidades y su impacto en el modelo.
- Consultas derivadas y JPQL.
- Transacciones y límites de consistencia.
- Testing de persistencia con `@DataJpaTest`.

## Qué construyes

Evolucionas la API de M3 para que los datos dejen de vivir sólo en memoria y pasen a gestionarse mediante entidades y repositorios JPA, preparando la aplicación para una gestión de errores y configuración más completa en M5.

## Material del módulo

- [`TEORIA.md`](TEORIA.md) — fundamentos de JPA/Hibernate y persistencia.
- [`PRACTICA.md`](PRACTICA.md) — recorrido práctico completo del módulo.
- [`M4_TEORIA.pdf`](M4_TEORIA.pdf) y [`M4_PRACTICA.pdf`](M4_PRACTICA.pdf) — versiones maquetadas.
- [`proyecto/`](proyecto/) — snapshot ejecutable final de M4.
- [`CODESPACES.md`](CODESPACES.md) — ejecución en GitHub Codespaces.

## Continuidad

M4 parte del cierre de M3 y conserva su API REST. M5 reutiliza esta base persistente para trabajar errores, configuración y CORS.

[Volver al índice general](../README.md)
