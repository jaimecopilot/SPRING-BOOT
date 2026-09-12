# Trazabilidad del curso

Esta carpeta contiene la trazabilidad **interna y verificable** entre teoría, práctica, código y comprobación. No se inserta metatexto de trazabilidad dentro de `TEORIA.md` ni `PRACTICA.md`.

## Baseline humano

La referencia histórica mínima es [`CURSO-SPRING-BOOT-2026/E1/TRAZABILIDAD.md`](https://github.com/jaimecopilot/CURSO-SPRING-BOOT-2026/blob/main/E1/TRAZABILIDAD.md): conserva Guía → proyecto, Proyecto → guía, estados, clasificación y soporte. El nuevo esquema v3 lo amplía con teoría, acción, símbolos, comandos, observables y verificaciones **por paso individual**.

## M0

- `M0.json`: índice máquina-legible, conceptos teóricos, inventario inverso y políticas.
- `M0/0.1.json` … `M0/0.4.json` + `M0/final.json`: contratos de los **51 pasos**.
- `generate_human_traceability.py`: genera las vistas humanas a partir del contrato.
- `M0/TRAZABILIDAD.md`: vista humana principal, situada junto al módulo como en el repositorio anterior.
- `TRAZABILIDAD_M0.md`: espejo interno de la misma información con enlaces relativos adaptados.
- `validate_m0.py`: gate mecánico de consistencia guía ↔ teoría ↔ código ↔ trazabilidad.

Cada paso registra `theory_refs`, artefactos y acción (`CREATE/MODIFY/USE/RESTORE/VERIFY`), símbolos, comandos, observables, verificación y estado `PERMANENT`/`TEMPORARY`.

La trazabilidad no redacta contenido didáctico. La autoría de teoría/práctica corresponde al modelo; los scripts sólo validan y generan vistas de auditoría desde contratos ya definidos.
