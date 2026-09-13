# Pipeline PDF único del curso

Todos los módulos M0–M7 deben generar sus dos PDF desde los Markdown canónicos con la misma plantilla.

Tipografías:

- texto: `DejaVu Serif`
- código: `DejaVu Sans Mono`

Encabezado obligatorio en todas las páginas:

- izquierda: `CURSO SPRING BOOT`
- derecha: `AUTOR: JAIME GALLO`

Pie: número de página centrado.

Comando de referencia:

```bash
pandoc M<n>/TEORIA.md \
  -o M<n>/M<n>_TEORIA.pdf \
  --pdf-engine=xelatex \
  --toc \
  -V geometry:margin=0.7in \
  -V fontsize=10pt \
  -V mainfont="DejaVu Serif" \
  -V monofont="DejaVu Sans Mono" \
  -H .course/pdf/course_header.tex
```

Para práctica se sustituye `TEORIA` por `PRACTICA`.

Antes de publicar se deben revisar visualmente portada, índice, páginas intermedias con tablas/código y últimas páginas; también debe verificarse el encabezado en todas las páginas.
