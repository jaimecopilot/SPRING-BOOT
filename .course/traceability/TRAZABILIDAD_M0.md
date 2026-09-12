# Trazabilidad interna M0

> Documento interno. No forma parte de la teoría ni de la práctica del alumno.

## Cobertura

- Conceptos teóricos identificados: **26**.
- Pasos prácticos numerados: **51** (`0.1`–`0.4`: 12 pasos cada uno + cierre A/B/C).
- Recorridos operativos obligatorios: **4** (consola, IntelliJ IDEA, Eclipse y VS Code).
- Artefactos clasificados: **20**.

El contrato máquina-legible está en `M0.json` y el validador en `validate_m0.py`. Los IDs de paso se derivan de la propia guía; si se añade, elimina o renumera un paso sin actualizar el contrato, CI falla.

## Convención de IDs

- Teoría: `M0-T-<punto>-<concepto>`.
- Práctica: `M0-P-<punto>-S<paso>`; por ejemplo `M0-P-03-S07`.
- Cierre: `M0-P-FINAL-A`, `B`, `C`.
- Recorridos: `M0-W-CONSOLE`, `M0-W-INTELLIJ`, `M0-W-ECLIPSE`, `M0-W-VSCODE`.

Cada práctica tiene un contrato explícito con los conceptos teóricos que la preparan. El validador recorre `PRACTICA.md`, crea un ID estable para cada paso y comprueba que existen exactamente 51 pasos trazables.

## Teoría → práctica

| Bloque | Conceptos principales | Práctica que los materializa |
|---|---|---|
| 0.1 | JDK/JRE/JVM, bytecode, classpath, Maven, Wrapper, IDE, `JAVA_HOME`/`PATH`, diagnóstico | 0.1 pasos 1–12 |
| 0.2 | Initializr, metadata, POM, starters, primer arranque, importación Maven | 0.2 pasos 1–12 |
| 0.3 | estructura Maven/Spring Boot, main, configuración, component scan, controlador, HTTP | 0.3 pasos 1–12 |
| 0.4 | ciclo diario, logs, depuración, tests, `curl`, cuatro entornos | 0.4 pasos 1–12 + cierre A/B/C |

## Práctica → código → verificación

| Artefacto | Origen didáctico | Evolución | Verificación |
|---|---|---|---|
| `M0/ejemplos/HolaMinisterio.java` | `M0-P-01-S06` | `M0-P-01-S12` | `javac --release 17`, ejecución sin argumentos y con `Ana` |
| `M0/proyecto/pom.xml` | `M0-P-02-S05` | `M0-P-02-S12` | Maven Wrapper, test, package |
| `mvnw`, `mvnw.cmd`, `.mvn/wrapper/...` | `M0-P-02-S05` | — | `./mvnw -version` = Maven 3.9.16 |
| `MiProyectoApplication.java` | `M0-P-02-S05` | `M0-P-03-S04` | `contextLoads` + arranque real |
| `application.properties` | `M0-P-02-S05` | `M0-P-03-S05` + diagnóstico temporal 0.4 | log de arranque |
| `SaludoController.java` | `M0-P-03-S07` | `M0-P-03-S12`, `M0-P-04-S12` | test + HTTP real `/hola` y `/adios` |
| `SaludoControllerTest.java` | `M0-P-04-S07` | — | `./mvnw -Dtest=SaludoControllerTest test` |

Los estados temporales utilizados para aprender diagnóstico/depuración no se consideran estado final: la práctica ordena restaurarlos y el código del repositorio representa el estado final del módulo.

## Trazabilidad inversa

El inventario de `M0.json` permite partir de un fichero y responder:

1. ¿se enseña o es infraestructura?
2. si se enseña, ¿en qué paso se introduce?
3. ¿qué pasos lo evolucionan?
4. ¿qué prueba/observable demuestra que funciona?

Los ficheros funcionales (`.java`, `pom.xml`, propiedades y Maven Wrapper) no pueden quedar sin clasificación. La categoría `SUPPORT` exige una razón y no puede introducir comportamiento público nuevo.

## Cuatro entornos

La guía conserva y valida los cuatro recorridos:

- `M0-W-CONSOLE`: consola/terminal;
- `M0-W-INTELLIJ`: IntelliJ IDEA;
- `M0-W-ECLIPSE`: Eclipse;
- `M0-W-VSCODE`: VS Code.

Las diferencias operativas no se tratan como contenido descartable: forman parte de la enseñanza.

## Gates automáticos

`validate_m0.py` falla si:

- falta un concepto teórico declarado;
- falta o sobra un paso práctico;
- un origen GUIDE apunta a un paso inexistente;
- un artefacto funcional queda sin clasificar;
- falta uno de los cuatro recorridos;
- preguntas y respuestas dejan de estar emparejadas;
- aparece metatexto interno en las guías del alumno;
- guía y código divergen en los contratos de alto valor;
- la práctica enseña Maven Wrapper pero éste no existe.

GitHub Actions añade después pruebas ejecutables: compila `HolaMinisterio`, ejecuta Maven Wrapper, tests, package, JAR y llamadas HTTP reales.
