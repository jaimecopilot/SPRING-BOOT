# Módulo 5 — Gestión de errores y configuración avanzada

## Qué hace este módulo

M5 toma la aplicación persistente de M4 y mejora su comportamiento ante fallos y su capacidad de configuración. El objetivo es que la API responda de forma consistente, configurable y preparada para integrarse con clientes externos.

## Qué aprenderás

- Diseño de excepciones de aplicación y de negocio.
- Manejo global de errores y respuestas HTTP coherentes.
- Representación estructurada de errores y validaciones.
- Configuración externalizada y uso de perfiles.
- Separación entre configuración de desarrollo, prueba y ejecución.
- Configuración de CORS para permitir clientes front-end controlados.
- Testing de errores, configuración y comportamiento transversal.

## Qué construyes

Evolucionas el proyecto de M4 para que tenga una política de errores clara, configuración flexible y soporte CORS, manteniendo la persistencia JPA y el contrato REST existentes. Esta versión sirve como base para incorporar seguridad en M6.

## Material del módulo

- [`TEORIA.md`](TEORIA.md) — conceptos de errores y configuración avanzada.
- [`PRACTICA.md`](PRACTICA.md) — construcción paso a paso.
- [`M5_TEORIA.pdf`](M5_TEORIA.pdf) y [`M5_PRACTICA.pdf`](M5_PRACTICA.pdf) — versiones maquetadas.
- [`proyecto/`](proyecto/) — snapshot ejecutable final de M5.
- [`frontend-cors/`](frontend-cors/) — apoyo para las pruebas de CORS.
- [`CODESPACES.md`](CODESPACES.md) — ejecución en GitHub Codespaces.

## Continuidad

M5 continúa desde M4. M6 conserva esta base y añade autenticación y autorización con Spring Security y JWT.

[Volver al índice general](../README.md)
