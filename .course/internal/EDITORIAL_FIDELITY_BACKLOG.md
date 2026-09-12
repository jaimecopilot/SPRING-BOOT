# Backlog de fidelidad editorial global — M0–M7

Documento interno. No forma parte del material del alumno.

## Decisión

La auditoría editorial exhaustiva **PDF fuente → edición nueva** se ejecutará de forma homogénea cuando estén redactados M0–M7. No sustituye la trazabilidad técnica de cada módulo: la complementa.

La decisión permite avanzar módulo a módulo sin reabrir continuamente material ya cerrado, pero evita que el cierre final del curso se limite a comprobar conceptos, pasos y runtime.

## Objetivo del cierre editorial final

Para cada módulo se construirá una matriz exhaustiva de unidades pedagógicas de la fuente. Cada unidad deberá tener un destino explícito en la edición nueva:

- `CONSERVADO`: permanece sustancialmente igual;
- `REESCRITO-EQUIVALENTE`: cambia la redacción o el ejemplo, pero conserva su función didáctica;
- `AMPLIADO`: se conserva y se desarrolla más;
- `RETIRADO-JUSTIFICADO`: se elimina sólo con justificación editorial/técnica registrada.

Ningún elemento significativo de la fuente podrá quedar sin clasificación.

## Qué se inventariará

Como mínimo, por páginas y subapartados de la fuente:

1. explicaciones conceptuales significativas;
2. fragmentos y ejemplos de código;
3. comandos y sesiones de consola;
4. tablas y comparaciones;
5. diagramas o elementos visuales con función pedagógica;
6. ejemplos de peticiones/respuestas/JSON/XML u otros formatos;
7. preguntas, respuestas y ejercicios de reflexión;
8. errores frecuentes, diagnóstico y soluciones;
9. retos resueltos;
10. resultados esperados y conclusiones.

La comprobación será bidireccional: fuente → edición y edición → fuente/ampliación.

## Regla para CI/gates finales

La futura matriz de fidelidad deberá poder validarse automáticamente al menos en cuanto a cobertura estructural: toda unidad catalogada en la fuente debe tener estado y destino; toda retirada debe tener justificación; toda referencia a un destino debe resolver a un ancla, paso o artefacto existente.

El LLM seguirá siendo responsable de juzgar equivalencia pedagógica. Los scripts no redactarán ni decidirán la calidad didáctica.

## Hallazgos ya conocidos que deben reexaminarse

### M0

M0 fue aprobado técnica y pedagógicamente con la trazabilidad disponible, pero debe pasar igualmente por esta auditoría de granularidad fina al cierre M0–M7. La revisión final no presupone que el volumen de páginas o palabras garantice fidelidad.

### M1

La revisión manual previa al cierre detectó ejemplos de la fuente cuya función no está garantizada por la trazabilidad conceptual actual. Deben revisarse expresamente, entre otros:

- configuración Spring MVC clásica con XML/`DispatcherServlet` frente a `spring-boot-starter-web`;
- anatomía completa de URL con protocolo, host, puerto, ruta, query y fragmento;
- batería completa de ejemplos `curl` y herramientas alternativas como HTTPie/Insomnia;
- comparación de una misma estructura en JSON y XML;
- ejemplo de respuesta paginada con metadatos;
- mapas/ejemplos concretos de diseño REST que hayan sido sustituidos por el dominio `alumnos`.

Que un concepto general esté cubierto no basta para declarar que esos ejemplos están conservados.

## Métricas

Páginas, palabras y caracteres se usarán sólo como señales cuantitativas. **Nunca** demostrarán por sí solas fidelidad pedagógica. El criterio definitivo será la cobertura de unidades didácticas y su función.

## Momento de ejecución

1. cerrar técnicamente cada módulo M0–M7;
2. integrar cada módulo aprobado en `main`;
3. al completar M7, ejecutar auditoría editorial global y homogénea M0–M7;
4. corregir omisiones/inconsistencias;
5. regenerar PDFs definitivos;
6. ejecutar gates técnicos y editoriales finales sobre el curso completo.
