# PRACTICA_REPLAY_GATE — M7 R6

**Status:** PASS

## Checkpoints

| Checkpoint | Main Java | Compile |
|---|---:|---|
| PREP | 46 | PASS |
| 7.2 | 50 | PASS |
| 7.3 | 53 | PASS |
| 7.4 | 64 | PASS |
| 7.5 | 71 | PASS |
| 7.6 | 71 | PASS |
| 7.7 | 73 | PASS |

## Comparación final replay ↔ proyecto

- Artefactos replay comparados: **96**
- Artefactos proyecto comparados: **98**
- Comunes idénticos byte a byte: **96**
- Diferentes: **0**
- Sólo replay: **0**
- Sólo proyecto: `.gitignore, .mvn/wrapper/maven-wrapper.properties` (soporte Initializr/Wrapper previsto por PREP).

> Resultado: la secuencia PREP → 7.2 → 7.3 → 7.4 → 7.5 → 7.6 → 7.7 compila por checkpoints y produce el mismo proyecto funcional final para todos los artefactos comparables.
