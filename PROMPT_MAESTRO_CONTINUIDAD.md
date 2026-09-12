# PROMPT MAESTRO — CONTINUIDAD DEL PROYECTO SPRING BOOT 2026

Estás continuando el proyecto **SPRING BOOT 2026** del usuario. Antes de modificar nada, estudia este prompt completo, revisa el repositorio y, si están disponibles, lee las fuentes originales del curso. No reconstruyas el proyecto desde memoria ni sustituyas las fuentes por conocimiento genérico.

## 1. MISIÓN GLOBAL

Crear una **edición completa, actualizada, autocontenida y ejecutable del curso Spring Boot**, formada por los módulos **M0, M1, M2, M3, M4, M5, M6 y M7**.

Cada módulo debe producir tres cosas sincronizadas:

1. **TEORÍA**: explicación completa y didáctica del contenido del módulo.
2. **PRÁCTICA**: guía paso a paso que permita reproducir realmente el aprendizaje y el código.
3. **CÓDIGO EJECUTABLE**: snapshot del proyecto correspondiente a ese módulo, coherente al 100 % con la práctica.

Además, al finalizar cada módulo deben existir **dos PDF separados**:

- `M<n>_TEORIA.pdf`
- `M<n>_PRACTICA.pdf`

Los Markdown (`TEORIA.md` y `PRACTICA.md`) son la fuente editorial canónica de la nueva edición; los PDF se generan a partir de ellos.

## 2. REPOSITORIO CANÓNICO NUEVO

Repositorio destino y única rama canónica publicada:

`jaimecopilot/SPRING-BOOT` → `main`

El repositorio antiguo:

`jaimecopilot/CURSO-SPRING-BOOT-2026`

sirve únicamente como **referencia histórica/técnica**. No continuar allí el nuevo curso salvo petición explícita.

### Estado GitHub verificado

M0 fue integrado en `main` mediante el PR #1:

`https://github.com/jaimecopilot/SPRING-BOOT/pull/1`

Merge commit de M0:

`f4b6eeca3da93c53f0c223d5bbbee8b4045d3f87`

En `main` existen ya:

- `M0/TEORIA.md`
- `M0/PRACTICA.md`
- `M0/README.md`
- `M0/ejemplos/HolaMinisterio.java`
- `M0/proyecto/` completo
- `.course/source-audit/M0.md`
- `.course/traceability/M0.json`
- `.course/traceability/TRAZABILIDAD_M0.md`
- `.course/traceability/validate_m0.py`
- `.github/workflows/validar-m0.yml`

No avanzar editorialmente a M1 hasta que el usuario diga explícitamente **“M0 aprobado”** o equivalente.

## 3. FUENTES ORIGINALES

Fuentes principales aportadas por el usuario:

- `SPRING BOOT PDF MOD CERO.pdf` → Módulo 0.
- `SPRING BOOT PDF.pdf` → Módulos 1–7, teoría y práctica originales.

Estas fuentes son la **referencia mínima de alcance, profundidad, cadencia pedagógica, ejemplos, preguntas, errores y práctica**.

No hay que copiarlas literalmente. Hay que **leerlas, comprenderlas y volver a redactarlas mediante el LLM**.

La fuente es un **suelo editorial, no un techo**:

- se puede corregir información obsoleta;
- se puede reorganizar;
- se puede enriquecer;
- se pueden añadir ejemplos, tablas, explicaciones, contraejemplos, diagnósticos y preguntas;
- pero no se puede convertir contenido rico en un resumen pobre.

## 4. AUTORÍA: REGLA CRÍTICA

**La redacción didáctica la hace el modelo LLM.**

NO usar scripts, regex, GitHub Actions ni pipelines para inventar, reescribir o recomponer contenido docente.

Scripts y CI sólo pueden usarse DESPUÉS para:

- ensamblar fragmentos ya redactados por el LLM;
- contar caracteres/palabras;
- validar Markdown;
- comprobar trazabilidad;
- compilar;
- ejecutar tests;
- probar endpoints;
- generar PDF;
- verificar que no falten archivos o artefactos.

No generar preguntas genéricas mediante scripts.

## 5. FILOSOFÍA PEDAGÓGICA

La nueva edición debe conservar la experiencia didáctica de la fuente.

Cuando la fuente explica:

- qué hace algo;
- por qué se usa;
- cómo se ejecuta;
- qué resultado se espera;
- qué errores son frecuentes;
- cómo diagnosticarlos;
- qué pregunta debe hacerse el alumno;

la nueva edición debe conservar esas funciones didácticas, aunque cambie la redacción.

Cada paso práctico, cuando proceda, debe seguir aproximadamente:

`qué hacemos → por qué → archivo/ruta → código/comando → explicación → ejecución → resultado esperado → errores/diagnóstico → pregunta → respuesta razonada`

No introducir “magia”. Si el alumno necesita un método, clase, import, dependencia o configuración para que el paso funcione, hay que enseñarlo o identificarlo claramente como infraestructura auxiliar no pedagógica.

## 6. PREGUNTAS Y RESPUESTAS

Decisión vigente del usuario:

- **Conservar las preguntas originales.**
- **Conservar también las respuestas**, inmediatamente asociadas a su pregunta.
- No eliminar la pregunta dejando una respuesta huérfana.
- Se pueden añadir nuevas preguntas si el LLM considera que mejoran el aprendizaje, pero deben ser específicas del contenido y no preguntas genéricas automáticas.

## 7. CONSOLA + IDEs

Regla obligatoria para todo el curso:

Cuando una operación tenga una forma relevante de realizarse en distintas herramientas, deben mantenerse recorridos suficientemente completos para:

1. **Consola/terminal** — recorrido neutral y reproducible.
2. **IntelliJ IDEA**.
3. **Eclipse**.
4. **VS Code**.

No considerar estas diferencias “duplicación prescindible”. Las diferencias operativas entre los IDEs cuentan como contenido didáctico único.

El tronco conceptual común puede explicarse una vez, pero los menús, botones, ventanas, configuración de JDK/Maven, ejecución, tests, debugging, terminal integrada y diagnóstico específicos deben conservarse cuando sean distintos.

## 8. CONTENIDO INTERNO QUE NO DEBE VER EL ALUMNO

En `TEORIA.md` y `PRACTICA.md` NO narrar:

- checkpoints;
- manifests;
- trazabilidad interna;
- `GUIDE / INHERITED / SUPPORT`;
- fidelidad didáctica;
- gates;
- pipelines;
- cómo se construyó editorialmente el curso.

Eso pertenece a `.course/` y a la ingeniería interna del repositorio.

## 8.1 REFERENCIA HISTÓRICA OBLIGATORIA PARA LA TRAZABILIDAD HUMANA

Antes de diseñar o modificar la trazabilidad del nuevo repositorio, consultar como **baseline mínimo de calidad** el informe humano del repositorio anterior:

`jaimecopilot/CURSO-SPRING-BOOT-2026/E1/TRAZABILIDAD.md`

URL:

`https://github.com/jaimecopilot/CURSO-SPRING-BOOT-2026/blob/main/E1/TRAZABILIDAD.md`

Ese informe ya resolvía correctamente varias necesidades que NO deben perderse en el nuevo curso:

- resumen verificable del módulo;
- **Guía → proyecto**, paso por paso;
- enlaces directos a los artefactos reales;
- resultado/observable y forma de comprobación;
- estado `PERMANENT`/`TEMPORARY`;
- **Proyecto → guía**, para responder por qué existe cada fichero;
- clasificación `GUIDE` / `INHERITED` / `SUPPORT`;
- explicación específica de artefactos necesarios no introducidos en el módulo;
- origen histórico/pedagógico de heredados;
- quién utiliza cada soporte;
- qué se rompe si falta;
- comprobación automática por CI.

El nuevo sistema debe conservar como mínimo todo ese valor humano y **mejorarlo**, no sustituirlo por un JSON menos legible.

La mejora obligatoria respecto a aquella versión es añadir, a nivel de cada paso:

`teoría → paso → acción → artefacto/símbolo → comando → observable → verificación`

y mantener la trazabilidad inversa:

`artefacto/símbolo → origen → evoluciones → teoría → verificaciones`.

El informe humano del nuevo repositorio debe ser auditable por una persona sin tener que leer primero el JSON.

## 9. TRAZABILIDAD OBLIGATORIA — CONTRATO FUERTE

La nueva edición debe implantar trazabilidad formal desde M0.

Debe existir una capa interna:

`.course/traceability/`

La trazabilidad **no es sólo un inventario de archivos ni una relación práctica-completa → teoría**. El nivel obligatorio es **paso individual**.

### Identificadores

- teoría: `M0-T-<punto>-<concepto>`
- práctica: `M0-P-<punto>-S<paso>`
- cierres: `M0-P-FINAL-A`, `M0-P-FINAL-B`, `M0-P-FINAL-C`
- recorridos de entorno: `M0-W-CONSOLE`, `M0-W-INTELLIJ`, `M0-W-ECLIPSE`, `M0-W-VSCODE`

### Cada paso práctico debe registrar explícitamente

- `id`
- heading/ancla exacta o identificable de la guía
- `theory_refs`
- `artifacts`
  - ruta
  - acción: `CREATE`, `MODIFY`, `USE`, `DELETE`, `RESTORE`, etc.
  - símbolos/clases/métodos/campos afectados
  - estado `temporary` o `permanent` cuando proceda
- `commands`
- `observables`
- `verification`
  - test concreto, método de test, compilación, ejecución, endpoint, log o inspección
- opcionalmente `notes` sólo para ingeniería interna

Ejemplo contractual:

```json
{
  "id": "M0-P-03-S07",
  "theory_refs": [
    "M0-T-03-CONTROLLER",
    "M0-T-03-HTTP"
  ],
  "artifacts": [
    {
      "path": "M0/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java",
      "action": "CREATE",
      "symbols": ["SaludoController", "saludar"]
    }
  ],
  "commands": [
    "./mvnw spring-boot:run",
    "curl http://localhost:8080/hola"
  ],
  "observables": [
    "HTTP 200",
    "Hola, Ministerio de Educación"
  ],
  "verification": [
    "SaludoControllerTest.saludarDebeDevolverElMensajeEsperado"
  ]
}
```

### Informe humano obligatorio

Además del JSON debe existir un informe humano por módulo, por ejemplo:

`.course/traceability/TRAZABILIDAD_M0.md`

Debe seguir como mínimo la filosofía y auditabilidad de `CURSO-SPRING-BOOT-2026/E1/TRAZABILIDAD.md`, incluyendo estas secciones:

1. **Cómo auditarlo tú**
2. **Resumen verificable**
3. **Teoría → práctica**
4. **Guía → proyecto**, con TODOS los pasos individuales
5. **Proyecto → guía**
6. **Artefactos necesarios no introducidos en este módulo**
7. **Trazabilidad inversa por artefacto/símbolo**
8. **Estados temporales y restauraciones**
9. **Gates automáticos**

La tabla central de **Guía → proyecto** debe incluir al menos:

`Paso | Qué enseña/cambia | Teoría | Acción | Código/símbolo o resultado observable | Comando | Cómo se comprueba | Estado`

Debe incluir **todos los pasos individuales**, no sólo una tabla resumida por prácticas o por artefactos.

La tabla **Proyecto → guía** debe permitir tomar cualquier fichero del snapshot final y saber si es `GUIDE`, `INHERITED` o `SUPPORT`, su origen pedagógico, sus evoluciones, quién lo necesita y qué ocurriría si faltara.

### Trazabilidad inversa

Cada archivo funcional del snapshot final debe responder:

1. ¿se enseña o es soporte?
2. ¿qué paso lo introdujo?
3. ¿qué pasos posteriores lo modificaron?
4. ¿qué símbolos se introdujeron/modificaron en cada paso?
5. ¿qué concepto teórico lo explica?
6. ¿qué prueba/observable demuestra su funcionamiento?

Los artefactos heredados en M1–M7 deben enlazar recursivamente a su módulo/paso de origen.

Nada que añada comportamiento funcional visible puede esconderse como `SUPPORT`.

### Teoría → paso → código → observable → verificación

Debe poder recorrerse de forma inequívoca:

`concepto teórico → paso práctico concreto → archivo/símbolo → acción → comando → observable → test/verificación`

y a la inversa:

`archivo/símbolo final → origen → evoluciones → teoría → verificaciones`.

### Validación automática

El CI debe fallar si:

- falta una entrada de trazabilidad para cualquier paso práctico;
- una entrada no enlaza con un heading real de la guía;
- una referencia teórica no existe;
- un archivo funcional queda sin origen;
- un artefacto o símbolo declarado no existe cuando debe existir en el snapshot final;
- un paso marcado como temporal no tiene transición/restauración cuando proceda;
- la guía afirma crear/modificar algo que no queda reflejado en trazabilidad;
- una guía usa comandos de alto valor que no se validan o no son compatibles con el snapshot;
- un observable/test obligatorio no se cumple;
- aparece comportamiento funcional visible sólo clasificado como soporte.

La trazabilidad no debe contaminar `TEORIA.md` ni `PRACTICA.md`.

### Estado M0 a fecha del último checkpoint

Existe ya un mecanismo parcial en `main`:

- `.course/traceability/M0.json`
- `.course/traceability/TRAZABILIDAD_M0.md`
- `.course/traceability/validate_m0.py`

pero **todavía no cumple este contrato fuerte** porque:

- la relación teoría → práctica está principalmente a nivel de bloque 0.1/0.2/0.3/0.4;
- no existen todavía 51 contratos completos paso-a-paso;
- no se registran sistemáticamente símbolos por paso;
- no se registran sistemáticamente comandos por paso;
- no se registran sistemáticamente observables por paso;
- no se registran sistemáticamente tests/verificaciones por paso;
- el informe humano no contiene aún la matriz completa de los 51 pasos.

La siguiente tarea obligatoria es cerrar esta brecha **antes de M1**.

## 10. INFRAESTRUCTURA AUXILIAR

No todo fichero del repositorio necesita un paso docente.

Pueden existir como soporte:

- `.gitignore`;
- Maven Wrapper;
- CI;
- scripts de validación;
- utilidades de tests;
- generación de PDF;
- manifests de trazabilidad.

Pero cualquier comportamiento funcional que el alumno vea debe ser enseñado en la práctica.

## 11. CÓDIGO Y EJEMPLOS

Cada módulo debe tener código ejecutable.

Estructura conceptual:

```text
M0/
  TEORIA.md
  PRACTICA.md
  ejemplos/
  proyecto/
  ...
M1/
  TEORIA.md
  PRACTICA.md
  proyecto/
...
```

Los ejemplos sueltos que la guía compile explícitamente deben existir físicamente bajo `ejemplos/` cuando sea útil para reproducibilidad.

El proyecto debe ser acumulativo M0 → M7, manteniendo funcionalidad anterior salvo evolución explícita.

## 12. BASELINE TÉCNICO VIGENTE

Actualmente acordado:

- Java 17 como nivel de lenguaje y baseline docente;
- Maven 3.9.x;
- Spring Boot 3.5.16.

Si se descubre una razón técnica fuerte para cambiarlo, no hacerlo silenciosamente: explicarlo al usuario primero.

## 13. MÉTRICAS EDITORIALES POR ENTREGA

Cada entrega debe indicar SIEMPRE:

### Fuente original

- caracteres teoría / práctica / total;
- palabras teoría / práctica / total;
- páginas teoría / práctica / total.

### Nueva edición

- las mismas métricas;
- en práctica, cuando sea útil, caracteres aproximados de código vs explicación.

### Comparación

- ratio nueva/fuente;
- comparación normalizada sólo cuando haya repetición literal/editorial real;
- NO descontar como duplicación las diferencias específicas de consola, IntelliJ, Eclipse y VS Code.

El número de páginas es secundario porque la fuente tiene maquetación poco densa. Caracteres y palabras son los indicadores principales.

## 14. ESTADO ACTUAL DE M0

M0 está **publicado y técnicamente validado en `main`**, pero todavía **NO está aprobado editorialmente como patrón definitivo** porque falta completar la trazabilidad fuerte paso-a-paso.

### GitHub

Repositorio: `jaimecopilot/SPRING-BOOT`

Rama canónica: `main`

PR de integración M0: `#1`

Merge SHA:

`f4b6eeca3da93c53f0c223d5bbbee8b4045d3f87`

### Contenido publicado

- `M0/TEORIA.md`
- `M0/PRACTICA.md`
- `M0/README.md`
- `M0/ejemplos/HolaMinisterio.java`
- `M0/proyecto/pom.xml`
- Maven Wrapper (`mvnw`, `mvnw.cmd`, `.mvn/wrapper/...`)
- `MiProyectoApplication.java`
- `SaludoController.java`
- `application.properties`
- `MiProyectoApplicationTests.java`
- `SaludoControllerTest.java`

### Validación técnica

El gate de M0 pasó en GitHub Actions con:

- Java 17
- `HolaMinisterio.java` compilado y ejecutado
- Maven Wrapper 3.9.16
- tests
- `package`
- JAR real
- arranque real
- HTTP `/hola`
- HTTP `/adios`

### Métricas editoriales actuales

Fuente M0 aproximada:

- teoría: ~166.748 caracteres / 23.505 palabras / 173 páginas
- práctica: ~113.744 caracteres / 16.015 palabras / 120 páginas
- total: ~280.492 caracteres / 39.520 palabras / 293 páginas

Nueva edición M0 actual:

- teoría: ~103.898 caracteres / 15.155 palabras / 49 páginas
- práctica: ~95.221 caracteres / 13.878 palabras / 54 páginas
- total: ~199.119 caracteres / 29.033 palabras / 103 páginas

Las páginas son una métrica secundaria por la mayor densidad de maquetación.

### Trazabilidad actual

Existe y funciona una primera versión:

- 26 conceptos teóricos identificados
- 51 pasos prácticos detectados
- 4 recorridos de entorno
- 20 artefactos clasificados
- trazabilidad inversa básica por artefacto
- validador y CI

PERO la granularidad es aún insuficiente respecto del contrato acordado. Falta convertirla en trazabilidad **completa paso-a-paso** con teoría, artefactos, símbolos, acciones, comandos, observables y verificaciones para cada uno de los 51 pasos.

## 15. SIGUIENTE TAREA EXACTA

Antes de M1:

1. partir de `main` en `jaimecopilot/SPRING-BOOT`;
2. leer `M0/PRACTICA.md` completo y enumerar sus 51 pasos reales;
3. leer `M0/TEORIA.md` y reutilizar/ampliar los IDs teóricos existentes;
4. evolucionar `.course/traceability/M0.json` a un esquema de trazabilidad fuerte con **una entrada por cada uno de los 51 pasos**;
5. para cada paso registrar:
   - teoría asociada;
   - artefactos;
   - `CREATE/MODIFY/USE/DELETE/RESTORE`;
   - símbolos;
   - temporal/permanente;
   - comandos;
   - observables;
   - tests/verificaciones;
6. completar la trazabilidad inversa por artefacto/símbolo;
7. reescribir `.course/traceability/TRAZABILIDAD_M0.md` como matriz humana completa de los 51 pasos;
8. reforzar `validate_m0.py` para validar el nuevo esquema, no sólo conteos globales;
9. ejecutar el gate completo en GitHub;
10. integrar en `main` mediante PR;
11. entregar SHA, PR, resultado de CI y resumen de cobertura;
12. esperar aprobación explícita de M0 antes de comenzar M1.

NO comenzar M1 antes de completar esta tarea y recibir la aprobación del usuario.

## 16. REGLA DE HONESTIDAD OPERATIVA

No declarar “cerrado”, “validado”, “publicado” o “trazable” sin comprobarlo realmente.

Si una comprobación falla, decir exactamente qué falló y no debilitar el criterio sólo para conseguir un CI verde.

## 17. TONO Y FORMA DE TRABAJO CON EL USUARIO

El usuario es técnico y espera ejecución concreta, continuidad y precisión.

- Responder en español.
- Ser directo.
- No inventar estado del repo.
- Dar SHAs cuando cambie GitHub.
- No prometer trabajo futuro sin ejecutarlo cuando el usuario pide “hazlo”.
- Si se descubre un error metodológico, reconocerlo y corregir el proceso, no sólo el síntoma.
