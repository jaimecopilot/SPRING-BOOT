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

## 4. Filosofía pedagógica

Cuando la fuente explica qué hace algo, por qué se usa, cómo se ejecuta, qué resultado se espera, qué errores son frecuentes, cómo se diagnostican o qué pregunta debe hacerse el alumno, la nueva edición debe conservar esas funciones.

Un paso práctico, cuando proceda, debe mantener aproximadamente:

`qué hacemos → por qué → archivo/ruta → código/comando → explicación → ejecución → resultado esperado → errores/diagnóstico → pregunta → respuesta razonada`

No introducir comportamiento funcional mediante “magia”. Todo método, clase, dependencia, import o configuración necesario para un comportamiento visible debe enseñarse o identificarse claramente como infraestructura auxiliar.

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

En `M0/`, los Markdown visibles son únicamente:

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

## 10. Informe humano

Debe existir un informe humano interno por módulo, para M0:

`.course/traceability/TRAZABILIDAD_M0.md`

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

No mantener una copia de este informe dentro de `M0/`: esa duplicación confunde la superficie del alumno y crea riesgo de divergencia.

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
- un temporal no se cierra mediante `RESTORE/DELETE`;
- falta un observable o una verificación;
- comportamiento funcional público se esconde como `SUPPORT`;
- desaparecen consola, IntelliJ, Eclipse o VS Code;
- reaparecen Markdown internos en la superficie del alumno;
- la vista humana está desincronizada;
- falla compilación, tests, empaquetado o HTTP real.

## 12. Baseline técnico

Mientras no exista una razón técnica fuerte explicada al usuario:

- Java 17;
- Maven Wrapper 3.9.16 / Maven 3.9.x;
- Spring Boot 3.5.16.

## 13. Estado durable de M0

M0 contiene teoría, práctica, ejemplo Java, proyecto Spring Boot, tests, Wrapper y trazabilidad fuerte a nivel de paso.

La práctica M0 expone 51 pasos trazables: 12 en 0.1, 12 en 0.2, 12 en 0.3, 12 en 0.4 y 3 cierres finales.

La trazabilidad de M0 se compone de:

- `.course/traceability/M0.json`;
- `.course/traceability/M0/0.1.json`;
- `.course/traceability/M0/0.2.json`;
- `.course/traceability/M0/0.3.json`;
- `.course/traceability/M0/0.4.json`;
- `.course/traceability/M0/final.json`;
- `.course/traceability/TRAZABILIDAD_M0.md`;
- `.course/traceability/validate_m0.py`;
- `.course/traceability/generate_human_traceability.py`;
- `.github/workflows/validar-m0.yml`.

El SHA exacto de `main`, PR y último resultado de CI deben consultarse en GitHub en cada nueva conversación; no fijarlos aquí para evitar que el contrato quede obsoleto.

## 14. Regla de avance

No comenzar M1 hasta que el usuario apruebe explícitamente M0 como patrón editorial y técnico.

## 15. Honestidad operativa

No declarar cerrado, validado, publicado o trazable sin comprobarlo realmente. Si un gate falla, explicar qué falló y corregir la causa; no debilitar el criterio para obtener un verde artificial.
