# Política editorial del curso

La redacción de teoría y práctica la realiza el modelo a partir de las fuentes, comprendiendo su alcance y nivel de detalle. Los scripts y GitHub Actions se usan sólo para validación, trazabilidad, compilación, tests y generación de artefactos; no para redactar contenido didáctico.

## Principio editorial central

La fuente original no se copia literalmente, pero actúa como referencia mínima de alcance, profundidad y cadencia pedagógica.

Eso significa que, cuando la fuente dedica espacio a explicar qué hace un elemento, por qué se utiliza, cómo se ejecuta, qué resultado se espera, qué errores son frecuentes y cómo se diagnostican, la nueva edición debe conservar esas funciones didácticas. Puede reorganizarlas, corregirlas, enriquecerlas o expresarlas mejor, pero no reducirlas a un resumen que pierda valor formativo.

La fuente es por tanto un suelo editorial, no un techo. La nueva edición puede añadir explicaciones, ejemplos, tablas, contraejemplos, preguntas, respuestas, diagnósticos, pruebas y conexiones entre conceptos cuando mejoren el aprendizaje.

La reducción de texto sólo se considera legítima cuando elimina repetición real o contenido editorial redundante sin perder información única. Las variantes por IDE pueden consolidarse en un tronco común siempre que las diferencias operativas de IntelliJ IDEA, Eclipse y VS Code sigan explicadas con suficiente detalle.

## Entregas

Cada entrega debe indicar siempre dos grupos de métricas.

### Métricas principales de fidelidad

- caracteres de teoría en la fuente original;
- caracteres de práctica en la fuente original;
- caracteres totales de la fuente original;
- palabras de teoría, práctica y total;
- caracteres de teoría, práctica y total en la nueva edición;
- palabras de teoría, práctica y total en la nueva edición;
- en la práctica, cuando sea útil, separación aproximada entre volumen de código y volumen explicativo;
- una comparación normalizada cuando la fuente repita contenido editorialmente, por ejemplo variantes de una misma práctica para distintos IDE. Esta normalización sólo puede descontar repetición real; nunca contenido específico que aporte información nueva.

### Métricas secundarias de maquetación

- páginas de teoría en la fuente original;
- páginas de práctica en la fuente original;
- páginas totales de la fuente original;
- páginas de teoría en la nueva edición;
- páginas de práctica en la nueva edición;
- páginas totales de la nueva edición.

El número de páginas no se usa como medida principal de fidelidad porque depende de tipografía, márgenes, espaciado, tamaño de los bloques de código y densidad de maquetación.

## Contenido didáctico

- Se conservan las preguntas y se incluyen sus respuestas razonadas.
- El código necesario para reproducir cada paso debe quedar completo o mediante modificaciones inequívocas.
- Cada práctica debe explicar qué se hace, por qué se hace, cómo se verifica y, cuando sea relevante, los errores frecuentes y su diagnóstico.
- La teoría debe preparar los conceptos que la práctica utilizará; no se introduce comportamiento funcional importante antes de haberlo explicado.
- La teoría y la práctica no incluyen narración sobre checkpoints, trazabilidad, manifests, GUIDE/INHERITED/SUPPORT ni mecanismos internos de validación.
- La infraestructura auxiliar puede existir en el repositorio sin ocupar pasos didácticos siempre que no introduzca comportamiento funcional nuevo no enseñado.
