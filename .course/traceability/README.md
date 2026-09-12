# Trazabilidad del curso

Esta carpeta contiene la trazabilidad **interna** entre teoría, práctica, código y verificación. No se muestra en las guías del alumno.

## Regla de autoría

El LLM redacta `TEORIA.md` y `PRACTICA.md`. Los scripts de esta carpeta **no redactan contenido didáctico**: validan contratos o renderizan informes internos desde datos de trazabilidad.

## Baseline humana

El nuevo sistema toma como mínimo de auditabilidad el informe histórico:

`jaimecopilot/CURSO-SPRING-BOOT-2026/E1/TRAZABILIDAD.md`

Ese patrón ya ofrecía Guía → proyecto, Proyecto → guía, clasificación `GUIDE/INHERITED/SUPPORT`, estados y soporte explicado. La nueva versión conserva todo ese valor y añade granularidad por paso:

`teoría → paso → acción → artefacto/símbolo → comando → observable → verificación`

## M0

- `M0.json`: índice del contrato máquina-legible v3.
- `M0/steps/*.json`: **51 contratos paso-a-paso**.
- `M0/artifacts.json`: inventario inverso del snapshot final y clasificación `GUIDE/INHERITED/SUPPORT`.
- `TRAZABILIDAD_M0.md`: informe humano completo generado desde el contrato.
- `generate_human_traceability.py`: renderiza el informe humano o comprueba con `--check` que no está desactualizado.
- `validate_m0.py`: valida guía, teoría, código, estados temporales, inventario y trazabilidad fuerte.

La trazabilidad inversa permite empezar por cualquier fichero funcional y conocer su origen pedagógico, evoluciones y verificaciones.

Ningún `SUPPORT` puede introducir comportamiento público nuevo.
