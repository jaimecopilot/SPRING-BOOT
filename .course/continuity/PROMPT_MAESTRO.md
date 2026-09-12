# PROMPT MAESTRO — CURSO SPRING BOOT 2026

Continúa el proyecto editorial/técnico `jaimecopilot/SPRING-BOOT` respetando estas reglas como contrato de trabajo.

## Misión

Crear una edición completa del curso Spring Boot, módulos M0–M7, con dos obras separadas por módulo (`TEORIA.md` y `PRACTICA.md`), código ejecutable acumulativo y dos PDF derivados de los Markdown. El corpus fuente es el suelo editorial: no debe resumirse mecánicamente. El LLM debe leerlo, comprender su intención pedagógica y redactar de nuevo con igual o mayor profundidad, pudiendo corregir, reorganizar y enriquecer sin empobrecer.

## Autoría

El contenido docente lo escribe el LLM. No usar scripts/regex/pipelines para redactar, mover o sintetizar teoría, práctica, preguntas, respuestas o explicaciones. Los scripts sólo pueden validar, medir, renderizar PDF o generar informes internos de trazabilidad a partir de contratos ya redactados.

## Preguntas y respuestas

Conservar las preguntas útiles de la fuente y sus respuestas. Nunca dejar respuestas huérfanas. Se pueden añadir preguntas nuevas si tienen intención pedagógica real; no generar una pregunta genérica por paso.

## Consola e IDE

Cuando una operación dependa de la herramienta, conservar cuatro recorridos reales: consola/terminal, IntelliJ IDEA, Eclipse y VS Code. El tronco conceptual puede compartirse, pero no eliminar instrucciones específicas de cada entorno. Las diferencias operativas cuentan como contenido único, no como duplicación descartable.

## Código y práctica

La guía conduce al código, no al revés. Un alumno que siga la práctica debe poder reconstruir el snapshot final sin inventar piezas. Cuando se crea una clase debe mostrarse el estado necesario de forma inequívoca; si se modifica, indicar exactamente qué sustituir/añadir. Infraestructura auxiliar puede existir sin paso docente sólo si no introduce comportamiento público ni conceptos nuevos.

## Repositorio

Repositorio canónico: `jaimecopilot/SPRING-BOOT`.
Rama publicada: `main`.
Repositorio histórico `jaimecopilot/CURSO-SPRING-BOOT-2026`: sólo referencia.

M0 fue integrado por PR #1. Merge SHA de esa primera integración: `f4b6eeca3da93c53f0c223d5bbbee8b4045d3f87`.

M0 contiene teoría, práctica, `HolaMinisterio.java`, proyecto Spring Boot, Maven Wrapper, tests, auditoría editorial y CI. Baseline técnica: Java 17, Spring Boot 3.5.16, Maven Wrapper 3.9.16.

## Métricas editoriales

Informar en cada entrega de fuente vs nueva edición: caracteres, palabras y páginas, separando teoría/práctica. Las páginas son secundarias porque la fuente tiene maquetación poco densa. En práctica, cuando sea útil, distinguir código de explicación. No perseguir igualdad artificial de caracteres: el objetivo es conservar alcance, profundidad, ejemplos, preguntas, diagnóstico y cadencia pedagógica.

M0 fuente aproximada: teoría 166.748 caracteres / 23.505 palabras / 173 páginas; práctica 113.744 / 16.015 / 120; total 280.492 / 39.520 / 293.
M0 nueva edición actual aproximada: teoría 103.898 / 15.155 / 49; práctica 95.221 / 13.878 / 54; total 199.119 / 29.033 / 103.

## Trazabilidad — contrato fuerte

La trazabilidad es interna: no contaminar `TEORIA.md` ni `PRACTICA.md` con checkpoint, fidelidad, `GUIDE/INHERITED/SUPPORT` ni explicación del mecanismo.

Referencia humana mínima obligatoria: `https://github.com/jaimecopilot/CURSO-SPRING-BOOT-2026/blob/main/E1/TRAZABILIDAD.md`.

Ese E1 histórico ya ofrecía resumen verificable, Guía→proyecto paso a paso, Proyecto→guía fichero a fichero, enlaces a código/evidencia, estado `PERMANENT/TEMPORARY`, clasificación `GUIDE/INHERITED/SUPPORT` y explicación de artefactos necesarios no introducidos. El nuevo sistema debe conservar como mínimo todo eso y mejorarlo.

Cadena obligatoria por cada paso práctico:

`teoría → paso → acción → artefacto/símbolo → comando → observable → verificación`

Y a la inversa:

`artefacto/símbolo → origen → evoluciones → teoría → verificaciones`.

Cada paso debe registrar: ID, heading, `theory_refs`, artefactos, `CREATE/MODIFY/USE/DELETE/RESTORE`, símbolos, estado temporal/permanente cuando proceda, comandos, observables y verificaciones concretas.

Informe humano obligatorio por módulo con secciones equivalentes a: Cómo auditarlo, Resumen verificable, Teoría→práctica, Guía→proyecto (todos los pasos), Proyecto→guía, artefactos no introducidos/soporte, trazabilidad inversa, estados temporales/restauraciones y gates automáticos.

`SUPPORT` debe indicar por qué existe, quién lo usa y qué se rompe si falta. Nunca puede esconder comportamiento público. En M1–M7, `INHERITED` debe resolver recursivamente al módulo/paso original.

## Estado exacto actual M0

Se está cerrando la trazabilidad fuerte en la rama `m0-traceability-strong`, partiendo de `main`.

El índice `.course/traceability/M0.json` usa esquema v3 y referencia contratos divididos en `.course/traceability/M0/steps/*.json` más `.course/traceability/M0/artifacts.json`.

Objetivo del cierre: exactamente 51 contratos (4 prácticas × 12 pasos + final A/B/C), 26 conceptos teóricos, cuatro recorridos de entorno, inventario inverso completo, tres estados temporales/restaurados (`M0-P-03-S05`, `M0-P-04-S02`, `M0-P-04-S12`), informe humano generado y verificado, y CI verde con trazabilidad + Java + Wrapper + tests + package + JAR + HTTP `/hola` y `/adios`.

No empezar M1 hasta que esta trazabilidad fuerte esté integrada en `main` y el usuario apruebe explícitamente M0 como patrón editorial.

## Honestidad operativa

No afirmar que algo está en `main` si sólo está en un branch; no decir que CI está verde antes de comprobar el run exacto; no confundir generación de infraestructura interna con autoría didáctica; reportar siempre branch, PR, HEAD/merge SHA y estado real de CI.
