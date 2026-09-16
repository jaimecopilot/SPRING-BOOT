# Módulo 7 — Proyecto global: API REST de gestión de becas

## Qué hace este módulo

M7 es el proyecto integrador del curso. A diferencia de M0–M6, no continúa el código acumulativo anterior: parte de un proyecto Spring Boot nuevo y construye una aplicación completa de gestión de becas aplicando de forma conjunta arquitectura REST, persistencia, validación, gestión de errores, seguridad, documentación y testing.

## Qué aprenderás

- Diseñar una API REST completa antes de implementarla.
- Modelar becas, solicitudes, alumnos y documentos con JPA.
- Construir repositorios con consultas, `JOIN FETCH`, `EntityGraph` y agregaciones.
- Diseñar DTOs, servicios y reglas de negocio.
- Implementar controladores y contratos HTTP coherentes.
- Proteger la API con Spring Security, roles y JWT.
- Documentar la API con OpenAPI y Postman.
- Validar el sistema mediante tests automáticos, escenarios E2E y una prueba ligera de carga.

## Qué construyes

Construyes desde cero una API REST de gestión de becas con:

- catálogo de becas;
- solicitudes y cambios de estado;
- documentos asociados a solicitudes;
- usuarios, roles y autenticación JWT;
- permisos diferenciados para CIUDADANO, GESTOR, ADMIN y CONSULTOR;
- estadísticas por estado;
- OpenAPI/Swagger y colección Postman;
- tests de repositorio, servicio, controlador, seguridad y aceptación.

La práctica contiene una preparación inicial standalone y 77 pasos canónicos distribuidos entre los puntos 7.1–7.7.

## Material del módulo

- [`TEORIA.md`](TEORIA.md) — diseño y explicación del sistema completo.
- [`PRACTICA.md`](PRACTICA.md) — construcción reproducible paso a paso.
- [`M7_TEORIA.pdf`](M7_TEORIA.pdf) y [`M7_PRACTICA.pdf`](M7_PRACTICA.pdf) — versiones maquetadas.
- [`proyecto/`](proyecto/) — aplicación final ejecutable, con tests, documentación y Postman.

## Validación

El cierre R6 del módulo fue validado con 35 tests automáticos, cobertura de línea JaCoCo del 75,5 %, replay completo de la práctica y 98 comprobaciones E2E formales sobre el JAR real.

## Continuidad

M7 es **standalone**: reutiliza los conocimientos de M0–M6, pero crea un proyecto nuevo e independiente para demostrar que el alumno puede construir una API completa desde cero.

[Volver al índice general](../README.md)
