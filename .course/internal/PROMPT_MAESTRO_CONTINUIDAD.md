# PROMPT MAESTRO — CONTINUIDAD DEL PROYECTO SPRING BOOT 2026

Este documento es **infraestructura interna del proyecto**. No forma parte del material del alumno.

## 1. Misión

Crear una edición completa, actualizada, autocontenida y ejecutable del curso Spring Boot, formada por M0, M1, M2, M3, M4, M5, M6 y M7.

Cada módulo debe mantener sincronizados:

1. `TEORIA.md`: explicación didáctica completa.
2. `PRACTICA.md`: recorrido paso a paso reproducible.
3. `proyecto/`: snapshot ejecutable coherente con la práctica.
4. PDF de teoría y PDF de práctica generados a partir de los Markdown canónicos.

La redacción docente la realiza el LLM. Scripts y CI validan, ensamblan, compilan, prueban, generan PDF o comprueban trazabilidad; no inventan ni reescriben contenido docente.

## 2. Repositorio canónico

Repositorio nuevo: `jaimecopilot/SPRING-BOOT`.

Rama canónica publicada: `main`.

El repositorio `jaimecopilot/CURSO-SPRING-BOOT-2026` es sólo referencia histórica/técnica.

Antes de modificar nada, consultar el estado real de `main`; no reconstruir el estado desde un SHA escrito en este documento si GitHub contiene un estado posterior.

## 3. Fuentes originales

- `SPRING BOOT PDF MOD CERO.pdf`: M0.
- `SPRING BOOT PDF.pdf`: M1–M7.

Las fuentes son el suelo mínimo de alcance, profundidad y cadencia pedagógica, no un techo. Se puede corregir, reorganizar y enriquecer, pero no resumir de forma que se pierdan funciones didácticas presentes en la fuente.

La fuente primaria manda en alcance, orden, intención, preguntas y ejercicios. El repositorio histórico se usa para contrastar una materialización anterior, no para sustituir la fuente ni para copiarla acríticamente.

## 4. Filosofía pedagógica

Cuando la fuente explica qué hace algo, por qué se usa, cómo se ejecuta, qué resultado se espera, qué errores son frecuentes, cómo se diagnostican o qué pregunta debe hacerse el alumno, la nueva edición debe conservar esas funciones.

Un paso práctico, cuando proceda, debe mantener aproximadamente:

`qué hacemos → por qué → archivo/ruta → código/comando → explicación → ejecución → resultado esperado → errores/diagnóstico → pregunta → respuesta razonada`

No introducir comportamiento funcional mediante “magia”. Todo método, clase, dependencia, import o configuración necesario para un comportamiento visible debe enseñarse o identificarse claramente como infraestructura auxiliar.

Cuando la fuente contenga una contradicción, afirmación desactualizada o ejercicio no reproducible en la baseline actual, no corregirlo silenciosamente: registrar la decisión en `.course/source-audit/` y conservar la intención pedagógica mediante una solución ejecutable.

## 5. Preguntas y respuestas

- Conservar las preguntas originales.
- Conservar sus respuestas inmediatamente asociadas.
- No dejar respuestas huérfanas.
- Se pueden añadir preguntas específicas cuando mejoren el aprendizaje; no generarlas automáticamente de forma genérica.

## 6. Consola e IDE

Cuando una operación tenga diferencias relevantes, mantener recorridos suficientes para:

1. consola/terminal;
2. IntelliJ IDEA;
3. Eclipse;
4. VS Code.

Las diferencias operativas de los IDE no cuentan como duplicación prescindible.

## 7. Superficie del alumno

La raíz del repositorio debe permanecer limpia. El alumno debe encontrar sólo documentación de navegación y los módulos.

En la raíz, el único Markdown de alumno es `README.md`.

En cada `M<n>/`, los Markdown visibles deben limitarse normalmente a:

- `README.md`;
- `TEORIA.md`;
- `PRACTICA.md`.

La trazabilidad, auditorías, políticas de mantenimiento y contratos de continuidad pertenecen a `.course/`. No deben duplicarse dentro de la carpeta del módulo ni contaminar `TEORIA.md` o `PRACTICA.md`.

## 8. Baseline histórico de trazabilidad

Antes de diseñar o modificar la trazabilidad, consultar como baseline mínimo:

`jaimecopilot/CURSO-SPRING-BOOT-2026/E1/TRAZABILIDAD.md`

La nueva trazabilidad debe conservar como mínimo:

- resumen verificable;
- Guía → proyecto paso a paso;
- enlaces a artefactos reales;
- observable y forma de comprobación;
- `PERMANENT` / `TEMPORARY`;
- Proyecto → guía;
- `GUIDE` / `INHERITED` / `SUPPORT`;
- explicación de soporte y heredados;
- comprobación automática por CI.

Y debe mejorarla con:

`teoría → paso → acción → artefacto/símbolo → comando → observable → verificación`

y la inversa:

`artefacto/símbolo → origen → evoluciones → teoría → verificaciones`.

## 9. Contrato fuerte de trazabilidad

La capa canónica interna es `.course/traceability/`.

Cada paso práctico debe registrar:

- `id`;
- heading real;
- `theory_refs`;
- artefactos con ruta, acción y símbolos;
- estado temporal/permanente;
- comandos;
- observables;
- verificaciones.

Acciones admitidas incluyen `CREATE`, `MODIFY`, `USE`, `DELETE`, `RESTORE` y `VERIFY`.

Una evolución de un artefacto no puede inventarse a partir de un paso que sólo lo lee o verifica: el origen debe corresponder a una creación real y las evoluciones a modificaciones/restauraciones reales.

Los pasos temporales deben cerrar explícitamente su transición mediante `RESTORE` o `DELETE` cuando proceda.

En M1–M7, los artefactos heredados deben resolver recursivamente su origen en módulos anteriores. Un snapshot nuevo no puede perder silenciosamente comportamiento o tests aprobados del módulo anterior.

## 10. Informe humano

Debe existir un informe humano interno por módulo, por ejemplo:

`.course/traceability/TRAZABILIDAD_M1.md`

Debe poder auditarse sin leer primero el JSON y contener como mínimo:

1. Cómo puedes auditarlo tú.
2. Resumen verificable.
3. Teoría → práctica.
4. Guía → proyecto, con todos los pasos.
5. Proyecto → guía.
6. Artefactos necesarios no introducidos en el módulo.
7. Trazabilidad inversa por artefacto/símbolo.
8. Estados temporales y restauraciones.
9. Gates automáticos.

No mantener copias de estos informes dentro de `M<n>/`.

## 11. Validación automática

El CI debe fallar, entre otros casos, si:

- no hay exactamente una entrada por cada paso real de la guía;
- un heading no coincide;
- una referencia teórica no existe;
- un artefacto funcional queda sin clasificar;
- un artefacto permanente usado por un paso falta del inventario inverso;
- un símbolo permanente declarado para creación/modificación/restauración no existe en el snapshot final;
- un origen `GUIDE` no corresponde a una acción `CREATE`;
- una evolución declarada no corresponde a `MODIFY/RESTORE`;
- un `INHERITED` no resuelve su origen histórico;
- un temporal no se cierra mediante `RESTORE/DELETE`;
- falta un observable o una verificación;
- comportamiento funcional público se esconde como `SUPPORT`;
- desaparecen consola, IntelliJ IDEA, Eclipse o VS Code cuando sean necesarios;
- reaparecen Markdown internos en la superficie del alumno;
- la vista humana está desincronizada;
- falla compilación, tests, empaquetado o HTTP real.

El workflow publicado debe operar normalmente con `contents: read`. No usar un CI que se autocorrija para convertir un estado roto en verde.

Desde M1 existe además `.course/traceability/validate_module.py` como capa reutilizable de invariantes estructurales. Los módulos nuevos deben reutilizarla y añadir sólo un gate semántico específico para sus contratos y observables propios, evitando copiar y divergir validadores completos por módulo.

## 12. Baseline técnico

Mientras no exista una razón técnica fuerte explicada al usuario:

- Java 17;
- Maven Wrapper 3.9.16 / Maven 3.9.x;
- Spring Boot 3.5.16.

## 13. Estado durable de M0

M0 está **aprobado explícitamente por el usuario el 2026-09-13** como patrón editorial y técnico del curso.

M0 contiene teoría, práctica, ejemplo Java, proyecto Spring Boot, tests, Wrapper y trazabilidad fuerte a nivel de paso.

La práctica M0 expone 51 pasos trazables: 12 en 0.1, 12 en 0.2, 12 en 0.3, 12 en 0.4 y 3 cierres finales.

La auditoría posterior a su trazabilidad corrigió orígenes/evoluciones semánticas, estados temporales y limpió la superficie Markdown del alumno. El último estado publicado debe verificarse siempre consultando `main` y Actions.

## 14. Estado durable de M1

M1 está autorizado y en desarrollo en la rama `m1-edicion-fuerte`.

Antes de redactar teoría/práctica se creó:

`.course/source-audit/M1.md`

Ese documento fija la matriz maestra de M1:

- cinco puntos 1.1–1.5;
- 58 pasos (10 + 12 + 12 + 12 + 12);
- catálogo teórico inicial;
- herencia exacta desde el snapshot M0;
- acciones previstas paso a paso;
- estados temporales y restauraciones;
- contradicciones/correcciones detectadas en la fuente;
- snapshot final esperado;
- gate funcional mínimo.

El flujo de producción de M1 debe seguir `1.1 → 1.2 → 1.3 → 1.4 → 1.5`, sincronizando teoría, práctica, código y contrato por bloque, sin rediseñar desde cero la infraestructura ya validada en M0.

Los puntos observacionales 1.1 y 1.2 se diseñan de modo que sus experimentos de puerto, Jackson y `/eco` sean temporales y se restauren: no deben alterar silenciosamente el snapshot final antes de que 1.3 introduzca JSON/DTOs de forma permanente.

## 15. Regla de avance

M1 puede continuar porque M0 está aprobado.

**No comenzar M2 hasta que el usuario apruebe explícitamente M1** como continuación válida del patrón.

## 16. Honestidad operativa

No declarar cerrado, validado, publicado o trazable sin comprobarlo realmente. Si un gate falla, explicar qué falló y corregir la causa; no debilitar el criterio para obtener un verde artificial.

El SHA exacto de `main`, la rama de trabajo, PR y último resultado de CI deben consultarse en GitHub en cada nueva conversación; no fijarlos aquí para evitar que el contrato quede obsoleto.
