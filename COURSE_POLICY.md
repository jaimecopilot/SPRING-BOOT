# Política editorial del curso

La redacción de teoría y práctica la realiza el modelo a partir de las fuentes, comprendiendo su alcance y nivel de detalle. Los scripts y GitHub Actions se usan sólo para validación, trazabilidad, compilación, tests y generación de artefactos; no para redactar contenido didáctico.

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
- La teoría y la práctica no incluyen narración sobre checkpoints, trazabilidad, manifests, GUIDE/INHERITED/SUPPORT ni mecanismos internos de validación.
- La infraestructura auxiliar puede existir en el repositorio sin ocupar pasos didácticos siempre que no introduzca comportamiento funcional nuevo no enseñado.
