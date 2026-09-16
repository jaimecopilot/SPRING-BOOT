# M7 R6 — EVIDENCIA FINAL RECONSTRUIDA

Estado: **PASS**

Esta evidencia se reconstruye a partir del transcript completo de la ejecución Windows aportado por el usuario y de los artefactos canónicos del paquete exacto ejecutado. El runner completó todos los gates funcionales y falló únicamente al intentar comprimir `RUNTIME_OUTPUT` mientras H2/Spring mantenían archivos bloqueados.

## Gates confirmados

- PACKAGE MANIFEST: PASS — 117 archivos inmutables.
- STATIC / TRACEABILITY: PASS — 32 bloques, 8 PREP, 77 pasos, 98 archivos de proyecto, NO_MAGIC_FILES PASS.
- PRACTICA_REPLAY_GATE: PASS — 7/7 checkpoints; 96 artefactos comunes idénticos byte a byte; 0 diferencias.
- Maven `clean verify`: BUILD SUCCESS.
- Tests: 35; failures=0; errors=0; skipped=0.
- JaCoCo LINE: 75.5 % (requisito >=70 %).
- JAR: `api-becas-0.0.1-SNAPSHOT.jar`, 63,140,670 bytes.
- Aplicación real: arrancada en puerto 18077.
- E2E: 98 checks formales PASS. El transcript contiene 99 líneas visibles `[PASS]` porque la fixture SQL `ENVIADA` se emite como PASS adicional fuera de la matriz de 98 checks.
- E2E FAIL: 0.

## Incidencia final

Tras el último PASS de carga (`100 peticiones -> 100`), el runner lanzó `PermissionError(13, 'Permission denied')` dentro de `save()` al intentar crear el ZIP de evidencia antes de liberar archivos de H2. No fue un fallo de Maven, tests, aplicación ni E2E.

La evidencia funcional completa se conserva en `M7_R6_WINDOWS_EXECUTION_FULL.log`.
