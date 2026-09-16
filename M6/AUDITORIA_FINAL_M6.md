# AUDITORÍA FINAL M6

## 1. Alcance y fuente

Fuente canónica revisada: `SPRING BOOT PDF(4)(1).pdf`.

M6 ocupa las páginas **1203-1550** inclusive; M7 comienza en la página 1551.

La revisión final no se ha basado sólo en conteos: se ha contrastado la estructura del PDF fuente con `TEORIA.md`, `PRACTICA.md`, los nueve snapshots de código y los observables runtime.

## 2. Fidelidad editorial

Resultado estructural:

- 9/9 puntos de M6 presentes.
- 45/45 bloques teóricos presentes.
- 135/135 subapartados teóricos `T` presentes y con encabezado coincidente con la fuente tras normalizar tipografía.
- 112/112 pasos prácticos presentes y con encabezado coincidente con la fuente tras normalizar Markdown.
- Las preguntas originales se conservan; cuando la edición aporta respuesta razonada, queda inmediatamente asociada a la pregunta.
- No se han añadido epígrafes internos de auditoría dentro de TEORIA/PRACTICA.

La única corrección editorial final realizada respecto de V10 fue restaurar literalmente el encabezado original de 6.9 paso 1 (`Añadir la dependencia spring-security-test`) y los tres encabezados de 6.7 que habían perdido artículos al pasar a Markdown.

## 3. Código y progresión acumulativa

La estructura física final es:

`6.1/proyecto -> 6.2/proyecto -> ... -> 6.9/proyecto`.

Se comprobó la evolución de ficheros entre snapshots. Casos clave:

- `InMemoryUserConfig.java` nace en 6.2 y se elimina en 6.3.
- `Usuario`, `Rol`, repositorios y `UsuarioDetailsService` nacen en 6.3.
- autorización por método, `UsuarioController`, `UsuarioService` y `UsuarioPrincipal` nacen en 6.4.
- el laboratorio JWT manual nace en 6.5 y queda aislado en tests.
- JJWT/login/refresh/logout nacen en 6.6.
- `JwtAuthenticationFilter` nace en 6.7.
- handlers JSON 401/403 y GESTOR completo se integran en 6.8.
- 6.9 añade la batería final de tests de seguridad.

No se ha creado un `M6/proyecto` paralelo: el estado final oficial es `6.9/proyecto`.

## 4. Validación funcional

Evidencia de aceptación local final:

- `VALIDAR_M6.bat`: **PASS** confirmado por el usuario tras las últimas correcciones.
- `VALIDAR_M6_HTTP.bat` V10: **PASS**, **85 comprobaciones HTTP reales**, arrancando realmente los snapshots 6.1-6.9.

El E2E cubrió entre otros: 200/401/403, Basic, usuarios en memoria y BD, registro, cambio de contraseña, ADMIN/USER/GESTOR, autorización por método, login JWT, Bearer, token inválido, refresh de un solo uso, logout, revocación, `UsuarioPrincipal`, SpEL por id y handlers JSON 401/403.

El log de la ejecución acumulativa final no se conserva en este paquete porque la ventana se cerró después del PASS; el resultado fue confirmado por el usuario. El log HTTP V10 tampoco se copia desde su PC al ZIP, pero su salida completa quedó revisada durante el cierre.

## 5. Código de ejemplo recuperado

Bloques fenced explícitos en la edición final:

- TEORIA.md: 94 bloques con lenguaje (`{'text': 5, 'java': 80, 'bash': 4, 'json': 2, 'xml': 1, 'properties': 2}`).
- PRACTICA.md: 228 bloques con lenguaje (`{'xml': 4, 'bash': 94, 'text': 31, 'properties': 7, 'java': 81, 'json': 10, 'sql': 1}`).

Estos conteos no se usan como sustituto de la fidelidad; sirven como control adicional para detectar pérdidas masivas de ejemplos. La comparación estructural 135/135 + 112/112 y la trazabilidad por artefacto son las pruebas principales.

## 6. PDF final

Se usa el pipeline canónico del curso: Pandoc + XeLaTeX, papel Letter, 0.7 in, DejaVu Serif 10 pt, DejaVu Sans Mono para código, cabecera `CURSO SPRING BOOT` / `AUTOR: JAIME GALLO`, pie con número de página e índice.

Los PDF finales se renderizaron completos y se revisaron en hojas de contacto:

- `M6_TEORIA.pdf`: 72 páginas.
- `M6_PRACTICA.pdf`: 72 páginas.

Controles técnicos:

- PDF abrible y no cifrado.
- No es PDF escaneado.
- No aparecen U+FFFD, área privada Unicode ni cuadrados blanco/negro en la extracción de texto.
- No se observaron páginas cortadas, bloques de código fuera de caja, superposiciones ni cabeceras/pies rotos en el render completo.

## 7. Trazabilidad

`TRAZABILIDAD.md` contiene:

- teoría -> práctica;
- los 112 pasos prácticos con página fuente, artefacto/evidencia y verificación;
- evolución física entre snapshots;
- proyecto final -> guía;
- estados temporales/restauraciones;
- gates de cierre.

## 8. Estado

**M6 cerrado técnicamente y documentalmente para entrega local.**

No se ha publicado nada en GitHub ni se ha creado/modificado ningún workflow, Action, pipeline, PR o rama durante este cierre.
