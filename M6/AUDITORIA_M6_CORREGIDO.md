# Auditoría M6 - corrección de formato y validación

## 1. Plantilla PDF restaurada

Los PDF de M6 se generan directamente desde los Markdown canónicos, sin un Markdown intermedio con portada/título propio.

Pipeline canónico del curso:

- motor: Pandoc + XeLaTeX;
- papel: Letter (612 x 792 pt);
- margen: 0.7 in;
- fuente de texto: DejaVu Serif, 10 pt;
- fuente de código: DejaVu Sans Mono;
- cabecera izquierda: `CURSO SPRING BOOT`;
- cabecera derecha: `AUTOR: JAIME GALLO`;
- pie: número de página centrado;
- índice generado con `--toc`.

## 2. Reparaciones de Markdown y tipografía

Se ha restaurado la jerarquía visual de encabezados para que Pandoc aplique los tamaños correspondientes a H1/H2/H3 como en los módulos anteriores.

En TEORIA.md se han normalizado los encabezados `Bloque` y los separadores de los títulos de punto al patrón del curso. Se han reconstruido los saltos de párrafo y los listados que habían quedado pegados durante la extracción del PDF fuente.

Se han eliminado tres clases de glifos incompatibles con la tipografía canónica que provocaban cuadrados en el PDF:

- U+F0B7 (viñeta privada procedente de una fuente tipo Wingdings);
- el pictograma de portátil usado como separador;
- el pictograma de cruz dentro de un comentario de código.

Las viñetas U+F0B7 que contenían explicación textual dentro de bloques de código se han convertido en listas Markdown reales fuera del bloque. No se ha eliminado la explicación asociada.

## 3. Verificación visual

Los PDF finales se han rasterizado completos y se han revisado en hojas de contacto:

- M6_TEORIA.pdf: 72 páginas;
- M6_PRACTICA.pdf: 72 páginas.

Las fuentes DejaVu están embebidas y no aparecen caracteres de sustitución, cuadrados blancos/negros ni caracteres del área privada Unicode en la extracción final del PDF.

## 4. VALIDAR_M6.bat

El BAT se ha cambiado para que nunca desaparezca sin dejar evidencia:

- crea siempre `VALIDAR_M6_YYYYMMDD_HHMMSS.log` en la misma carpeta;
- guarda en ese log la salida completa del gate estático, Maven, JwtManual y el gate final;
- si falla, muestra en pantalla las últimas 80 líneas del log;
- tanto en PASS como en FAIL muestra la ruta exacta del log;
- termina con `pause`, por lo que la ventana no se cierra automáticamente.

El gate estático ha sido actualizado al encabezado canónico `## Bloque` y en esta entrega devuelve:

`M6 STATIC GATE: PASS | theory_points=9 | theory_blocks=45 | practice_steps=112 | snapshots=9`

La validación Maven completa debe ejecutarse en Windows con acceso a las dependencias. El resultado quedará persistido en el log aunque haya un fallo.
