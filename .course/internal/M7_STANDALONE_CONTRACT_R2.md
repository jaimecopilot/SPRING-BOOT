# M7 — Contrato R2 STANDALONE

## Regla arquitectónica

`M7/proyecto` es un proyecto Spring Boot nuevo e independiente. No se copia ni se usa como baseline ningún árbol de M1–M6.

## Fuente

- PDF original: páginas 1551–1808.
- Diseño: 32 bloques.
- Pasos originales: 77.
- Preparación autónoma editorial: PREP-01..08.

La preparación autónoma existe porque el PDF original da por creados en módulos anteriores `Alumno`, `Usuario/Rol`, seguridad JWT y el manejo global de errores. En esta edición esas piezas se crean dentro de M7 antes del paso 7.1.1.

## Trazabilidad

Todo archivo funcional de `M7/proyecto` debe estar vinculado a uno o más PREP o pasos 7.x. `NO_MAGIC_FILES` debe ser PASS.

## Cierre R2

R2 es un gate de compilación/tests/cobertura. No se declarará Maven PASS ni JaCoCo PASS hasta ejecutar `EJECUTAR_M7_R2.bat` en Windows.
