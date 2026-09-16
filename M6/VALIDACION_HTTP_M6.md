# M6 - Gate HTTP real extremo a extremo

Este gate complementa `VALIDAR_M6.bat`. El primer validador comprueba compilación y tests; `VALIDAR_M6_HTTP.bat` arranca **realmente** cada snapshot Spring Boot `6.1` a `6.9` en un puerto independiente y realiza peticiones HTTP contra `localhost`.

La selección de observables sigue la práctica del M6. En particular, 6.7 reproduce ahora literalmente los casos esenciales del PDF: 401 sin token, 200 con token, 200 público, `UsuarioPrincipal` con `ROLE_USER`, ADMIN -> 200, USER -> 403, token inválido -> 401, cabecera sin `Bearer` -> 401 y el reto SpEL con los IDs deterministas admin=1 / ana=2. En 6.8 también se verifica el reto `/api/v1/perfil/usuario`.

El script genera un log principal `VALIDAR_M6_HTTP_YYYYMMDD_HHMMSS.log` y una carpeta `VALIDAR_M6_HTTP_APPS_YYYYMMDD_HHMMSS` con los logs de empaquetado y de arranque de cada aplicación. La ventana permanece abierta al terminar.

Desde esta revisión el gate **no se detiene en la primera versión que falla**. Registra el fallo de esa versión, detiene esa aplicación y continúa con las siguientes para obtener en una sola ejecución la lista completa de versiones con problemas.

## Ejecución

1. Extraer el ZIP completo.
2. Ejecutar `VALIDAR_M6_HTTP.bat` desde la carpeta `M6_REPARADO_FINAL`.
3. Un cierre correcto termina con `M6 HTTP REAL: PASS`.
4. Si falla, enviar el log principal completo: al final aparecerá el resumen de versiones que han fallado y cada `[FAIL]` mostrará el observable exacto.

Este gate no sustituye la lectura pedagógica de los 112 pasos: automatiza los observables HTTP esenciales que demuestran que los mecanismos enseñados funcionan realmente en cada snapshot.


## Corrección V10: barrera de preparación real

El servidor HTTP de Spring Boot puede aceptar conexiones antes de que hayan terminado los `CommandLineRunner`. El gate V10 no considera una versión preparada sólo porque responda `/api/v1/public/info`: para 6.3-6.6 espera además a que `ana` autentique, y para 6.8-6.9 espera a que `gestor` autentique (es el último usuario inicial). Así se evita confundir una carrera de arranque con un 401 funcional.
