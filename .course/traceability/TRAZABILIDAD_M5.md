# TRAZABILIDAD M5

Cadena reversible: **concepto teórico -> paso práctico -> acción -> artefacto/símbolo -> comando -> observable -> verificación**.

## Cómo puedes auditarlo tú

1. Ejecuta `python .course/traceability/validate_m5.py .`.
2. Ejecuta `python .course/traceability/validate_m5_human.py .`.
3. Ejecuta `python .course/traceability/validate_m5_mutation.py .`.
4. Comprueba que M4 -> M5 contabiliza los 54 ficheros del snapshot anterior.

## Resumen verificable

- Conceptos teóricos: **25/25**.
- Pasos prácticos: **60/60**.
- Artefactos finales inventariados: **80**.
- Continuidad M4 -> M5: **54/54** ficheros contabilizados.
- Eliminaciones: **1**, `GlobalExceptionHandler.java`, sustituido explícitamente en 5.3.

## Teoría -> práctica

| Concepto | Punto | Pasos que lo aplican |
|---|---|---|
| `M5-5.1-T1` Por qué excepciones personalizadas | 5.1 | `M5-P-51-S01` |
| `M5-5.1-T2` Jerarquía de excepciones | 5.1 | `M5-P-51-S02`, `M5-P-51-S03`, `M5-P-51-S04`, `M5-P-51-S05`, `M5-P-51-S06`, `M5-P-51-S07` |
| `M5-5.1-T3` Información en las excepciones | 5.1 | `M5-P-51-S03`, `M5-P-51-S04`, `M5-P-51-S05`, `M5-P-51-S07`, `M5-P-51-S13` |
| `M5-5.1-T4` Lanzar excepciones desde el servicio | 5.1 | `M5-P-51-S08`, `M5-P-51-S09` |
| `M5-5.1-T5` Buenas prácticas y tests | 5.1 | `M5-P-51-S10`, `M5-P-51-S11`, `M5-P-51-S12`, `M5-P-51-S13` |
| `M5-5.2-T1` Por qué respuestas de error estándar | 5.2 | `M5-P-52-S01` |
| `M5-5.2-T2` Estructura de una respuesta de error | 5.2 | `M5-P-52-S02`, `M5-P-52-S03`, `M5-P-52-S04`, `M5-P-52-S08` |
| `M5-5.2-T3` Construcción de la respuesta | 5.2 | `M5-P-52-S05`, `M5-P-52-S07`, `M5-P-52-S08`, `M5-P-52-S12` |
| `M5-5.2-T4` Errores de validación | 5.2 | `M5-P-52-S06`, `M5-P-52-S07`, `M5-P-52-S09` |
| `M5-5.2-T5` Buenas prácticas y tests | 5.2 | `M5-P-52-S10`, `M5-P-52-S11`, `M5-P-52-S12` |
| `M5-5.3-T1` Qué es @RestControllerAdvice | 5.3 | `M5-P-53-S01`, `M5-P-53-S02` |
| `M5-5.3-T2` Estructura del manejador global | 5.3 | `M5-P-53-S02`, `M5-P-53-S03`, `M5-P-53-S04`, `M5-P-53-S05`, `M5-P-53-S06` |
| `M5-5.3-T3` Aplicación selectiva | 5.3 | `M5-P-53-S07`, `M5-P-53-S12` |
| `M5-5.3-T4` Casos especiales | 5.3 | `M5-P-53-S04`, `M5-P-53-S09`, `M5-P-53-S10`, `M5-P-53-S12` |
| `M5-5.3-T5` Buenas prácticas y tests | 5.3 | `M5-P-53-S06`, `M5-P-53-S08`, `M5-P-53-S09`, `M5-P-53-S11` |
| `M5-5.4-T1` Configuración externalizada | 5.4 | `M5-P-54-S01`, `M5-P-54-S02` |
| `M5-5.4-T2` Perfiles de Spring Boot | 5.4 | `M5-P-54-S03`, `M5-P-54-S04`, `M5-P-54-S05`, `M5-P-54-S06`, `M5-P-54-S07`, `M5-P-54-S10` |
| `M5-5.4-T3` @Profile y beans condicionales | 5.4 | `M5-P-54-S09`, `M5-P-54-S10` |
| `M5-5.4-T4` @ConfigurationProperties | 5.4 | `M5-P-54-S08` |
| `M5-5.4-T5` Orden de prioridad y buenas prácticas | 5.4 | `M5-P-54-S05`, `M5-P-54-S07`, `M5-P-54-S11`, `M5-P-54-S12` |
| `M5-5.5-T1` La política de mismo origen | 5.5 | `M5-P-55-S01`, `M5-P-55-S02` |
| `M5-5.5-T2` Peticiones simples y preflight | 5.5 | `M5-P-55-S04`, `M5-P-55-S06`, `M5-P-55-S07` |
| `M5-5.5-T3` Cómo configurar CORS en Spring Boot | 5.5 | `M5-P-55-S03`, `M5-P-55-S08` |
| `M5-5.5-T4` Casos prácticos | 5.5 | `M5-P-55-S04`, `M5-P-55-S05`, `M5-P-55-S08`, `M5-P-55-S09`, `M5-P-55-S11` |
| `M5-5.5-T5` Errores comunes y buenas prácticas | 5.5 | `M5-P-55-S07`, `M5-P-55-S10`, `M5-P-55-S11` |

## Práctica -> acción -> artefacto -> verificación

| Paso | Concepto | Acción/estado | Artefacto / símbolo | Comando | Observable | Verificación |
|---|---|---|---|---|---|---|
| `M5-P-51-S01` Repasar el estado actual | `M5-5.1-T1` | USE:PERMANENT<br>USE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` :: `NegocioException` | `./mvnw test` | Se identifica la excepción genérica heredada de M4. | Contrastar el observable del paso 5.1.1 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S02` Crear el paquete exception | `M5-5.1-T2` | SIN ARTEFACTO | — | `mkdir -p src/main/java/es/mecd/demo/miproyecto/common/exception` | El paquete de excepciones queda definido como frontera común. | Contrastar el observable del paso 5.1.2 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S03` Crear la clase base AplicacionException | `M5-5.1-T2`, `M5-5.1-T3` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java` :: `AplicacionException`, `codigo`, `getCodigo` | `./mvnw test` | Existe una base RuntimeException con código funcional estable. | Contrastar el observable del paso 5.1.3 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S04` Crear RecursoNoEncontradoException | `M5-5.1-T2`, `M5-5.1-T3` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java` :: `RecursoNoEncontradoException`, `RECURSO_NO_ENCONTRADO`, `getRecurso`, `getId` | `./mvnw test` | La excepción transporta recurso e identificador. | Contrastar el observable del paso 5.1.4 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S05` Crear RecursoDuplicadoException | `M5-5.1-T2`, `M5-5.1-T3` | CREATE:PERMANENT<br>MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java` :: `RecursoDuplicadoException`, `RECURSO_DUPLICADO`, `getCampo`, `getValor`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` :: `NegocioException`, `AplicacionException` | `./mvnw test` | La duplicidad queda diferenciada del resto de errores de negocio. | Contrastar el observable del paso 5.1.5 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S06` Crear OperacionNoPermitidaException | `M5-5.1-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/OperacionNoPermitidaException.java` :: `OperacionNoPermitidaException`, `OPERACION_NO_PERMITIDA` | `./mvnw test` | Una regla de negocio puede impedir una operación válida técnicamente. | Contrastar el observable del paso 5.1.6 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S07` Crear ErrorTecnicoException | `M5-5.1-T2`, `M5-5.1-T3` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorTecnicoException.java` :: `ErrorTecnicoException`, `ERROR_TECNICO`, `Throwable` | `./mvnw test` | La causa técnica queda encadenada sin ser el contrato HTTP. | Contrastar el observable del paso 5.1.7 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S08` Refactorizar el AlumnoService | `M5-5.1-T4` | MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `RecursoDuplicadoException`, `RecursoNoEncontradoException`, `ValidacionNegocioException` | `./mvnw test -Dtest=AlumnoServiceTest` | AlumnoService lanza excepciones semánticas en reglas de negocio. | Contrastar el observable del paso 5.1.8 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S09` Refactorizar el CursoService | `M5-5.1-T4` | MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` :: `RecursoDuplicadoException`, `RecursoNoEncontradoException`, `OperacionNoPermitidaException` | `./mvnw test` | CursoService diferencia duplicado, inexistente y operación prohibida. | Contrastar el observable del paso 5.1.9 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S10` Escribir tests para las excepciones | `M5-5.1-T5` | MODIFY:PERMANENT<br>CREATE:PERMANENT | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` :: `RecursoDuplicadoException`, `assertThrows`<br>`M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/AplicacionExceptionTest.java` :: `AplicacionExceptionTest`, `RECURSO_DUPLICADO`, `ERROR_TECNICO` | `./mvnw test -Dtest=AlumnoServiceTest,AplicacionExceptionTest` | Los tests validan tipo, código, datos y causa. | Contrastar el observable del paso 5.1.10 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S11` Ejecutar los tests | `M5-5.1-T5` | VERIFY:PERMANENT | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/AplicacionExceptionTest.java` :: `AplicacionExceptionTest` | `./mvnw test` | La suite acumulativa se mantiene verde. | Contrastar el observable del paso 5.1.11 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S12` Errores comunes del ejercicio | `M5-5.1-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java` :: `AplicacionException`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java` :: `RecursoDuplicadoException` | `./mvnw test` | Los errores comunes quedan cubiertos por compilación y tests. | Contrastar el observable del paso 5.1.12 con PRACTICA.md y el artefacto final. |
| `M5-P-51-S13` Reto resuelto — Excepción para validación de negocio | `M5-5.1-T3`, `M5-5.1-T5` | CREATE:PERMANENT<br>MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidacionNegocioException.java` :: `ValidacionNegocioException`, `VALIDACION_NEGOCIO`, `getCampo`, `getMotivo`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `validarNombre`, `ValidacionNegocioException` | `./mvnw test` | La regla de nombre sin dígitos produce una excepción funcional específica. | Contrastar el observable del paso 5.1.13 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S01` Repasar el estado actual | `M5-5.2-T1` | USE:TEMPORARY | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` :: `GlobalExceptionHandler` | `./mvnw test` | Se constata que M4 devolvía mapas genéricos de error. | Contrastar el observable del paso 5.2.1 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S02` Crear el paquete dto.error | `M5-5.2-T2` | SIN ARTEFACTO | — | `mkdir -p src/main/java/es/mecd/demo/miproyecto/common/dto/error` | Se crea el espacio de DTOs de error. | Contrastar el observable del paso 5.2.2 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S03` Crear la clase ValidationError | `M5-5.2-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java` :: `ValidationError`, `rejectedValue`, `message` | `./mvnw test` | Cada error de campo tiene estructura tipada. | Contrastar el observable del paso 5.2.3 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S04` Crear la clase ErrorResponse | `M5-5.2-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `ErrorResponse`, `timestamp`, `status`, `codigo`, `mensaje`, `path`, `errors` | `./mvnw test` | La API dispone de un contrato de error estable y tipado. | Contrastar el observable del paso 5.2.4 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S05` Refactorizar el GlobalExceptionHandler | `M5-5.2-T3` | MODIFY:TEMPORARY<br>USE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` :: `GlobalExceptionHandler`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `ErrorResponse` | `./mvnw test` | El manejador transitorio usa ErrorResponse antes de separarse en 5.3. | Contrastar el observable del paso 5.2.5 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S06` Añadir manejadores para errores de parámetros | `M5-5.2-T4` | MODIFY:TEMPORARY | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` :: `GlobalExceptionHandler` | `./mvnw test` | JSON ilegible, parámetro faltante y tipo incorrecto quedan modelados como 400. | Contrastar el observable del paso 5.2.6 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S07` Arrancar y probar errores | `M5-5.2-T3`, `M5-5.2-T4` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `ErrorResponse` | `./mvnw spring-boot:run`<br>`curl -i http://localhost:8080/api/v1/alumnos` | Las respuestas de error mantienen Content-Type JSON y campos comunes. | Contrastar el observable del paso 5.2.7 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S08` Verificar el error 404 | `M5-5.2-T2`, `M5-5.2-T3` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java` :: `RECURSO_NO_ENCONTRADO` | `curl -i http://localhost:8080/api/v1/alumnos/99999` | Un recurso inexistente se representa con código funcional y 404 tras el handler final. | Contrastar el observable del paso 5.2.8 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S09` Verificar el error de validación | `M5-5.2-T4` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java` :: `ValidationError` | `curl -i -X POST http://localhost:8080/api/v1/alumnos` | La validación devuelve una colección errors. | Contrastar el observable del paso 5.2.9 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S10` Escribir tests para las respuestas de error | `M5-5.2-T5` | CREATE:PERMANENT | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` :: `ErrorHandlingTest`, `VALIDACION`, `RECURSO_DUPLICADO`, `ERROR_INTERNO` | `./mvnw test -Dtest=ErrorHandlingTest` | Los códigos 400, 409 y 500 se verifican por MockMvc. | Contrastar el observable del paso 5.2.10 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S11` Errores comunes del ejercicio | `M5-5.2-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `ErrorResponse`<br>`M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` :: `ErrorHandlingTest` | `./mvnw test` | La forma del error está protegida contra regresiones. | Contrastar el observable del paso 5.2.11 con PRACTICA.md y el artefacto final. |
| `M5-P-52-S12` Reto resuelto — Añadir un campo traceId a las respuestas de error | `M5-5.2-T3`, `M5-5.2-T5` | MODIFY:PERMANENT<br>CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `traceId`, `getTraceId`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java` :: `newTraceId`, `setTraceId` | `./mvnw test` | Cada respuesta de error incorpora un traceId. | Contrastar el observable del paso 5.2.12 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S01` Repasar el estado actual | `M5-5.3-T1` | USE:TEMPORARY<br>USE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` :: `GlobalExceptionHandler`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `ErrorResponse` | `./mvnw test` | Se parte del advice monolítico transitorio creado en 5.2. | Contrastar el observable del paso 5.3.1 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S02` Crear el ValidationExceptionHandler | `M5-5.3-T1`, `M5-5.3-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` :: `RestControllerAdvice`, `Order(1)`, `MethodArgumentNotValidException` | `./mvnw test` | Los errores de entrada quedan aislados en un advice de prioridad 1. | Contrastar el observable del paso 5.3.2 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S03` Crear el BusinessExceptionHandler | `M5-5.3-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` :: `RestControllerAdvice`, `Order(2)`, `RecursoNoEncontradoException`, `RecursoDuplicadoException` | `./mvnw test` | Las excepciones funcionales se traducen en un advice de prioridad 2. | Contrastar el observable del paso 5.3.3 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S04` Crear el GenericExceptionHandler | `M5-5.3-T2`, `M5-5.3-T4` | CREATE:PERMANENT<br>MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` :: `RestControllerAdvice`, `Order(3)`, `ERROR_INTERNO`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java` :: `newTraceId`, `build` | `./mvnw test` | El último advice oculta detalles técnicos y registra traceId. | Contrastar el observable del paso 5.3.4 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S05` Eliminar el GlobalExceptionHandler antiguo | `M5-5.3-T2` | DELETE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` ::  | `./mvnw test` | El GlobalExceptionHandler monolítico deja de existir y queda sustituido por tres handlers ordenados. | Contrastar el observable del paso 5.3.5 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S06` Arrancar y probar el orden | `M5-5.3-T2`, `M5-5.3-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` :: `Order(1)`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` :: `Order(2)`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` :: `Order(3)` | `./mvnw test`<br>`curl -i http://localhost:8080/api/v1/alumnos/99999` | La selección de handler sigue prioridad y especificidad. | Contrastar el observable del paso 5.3.6 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S07` Aplicar un manejador a un paquete concreto | `M5-5.3-T3` | CREATE:PERMANENT<br>CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoExceptionHandler.java` :: `basePackages`, `Order(0)`, `AlumnoEspecificoException`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoEspecificoException.java` :: `AlumnoEspecificoException` | `./mvnw test` | Un advice puede restringirse al paquete alumno y tener prioridad 0. | Contrastar el observable del paso 5.3.7 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S08` Escribir tests para el orden | `M5-5.3-T5` | MODIFY:PERMANENT | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` :: `ErrorHandlingTest`, `isConflict`, `isInternalServerError` | `./mvnw test -Dtest=ErrorHandlingTest` | MockMvc verifica validación, negocio y error genérico. | Contrastar el observable del paso 5.3.8 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S09` Verificar el traceId | `M5-5.3-T4`, `M5-5.3-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` :: `traceId`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` :: `traceId` | `./mvnw test` | El mismo traceId se incluye en respuesta y log del error genérico. | Contrastar el observable del paso 5.3.9 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S10` Añadir un manejador para errores de tipo | `M5-5.3-T4` | MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` :: `MethodArgumentTypeMismatchException`, `TIPO_INCORRECTO` | `./mvnw test` | Un tipo de parámetro incorrecto produce 400 con código TIPO_INCORRECTO. | Contrastar el observable del paso 5.3.10 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S11` Errores comunes del ejercicio | `M5-5.3-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` :: `ValidationExceptionHandler`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` :: `BusinessExceptionHandler`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` :: `GenericExceptionHandler` | `./mvnw test` | Los errores comunes de prioridad o duplicidad de handlers quedan cubiertos. | Contrastar el observable del paso 5.3.11 con PRACTICA.md y el artefacto final. |
| `M5-P-53-S12` Reto resuelto — Manejador específico para ficheros | `M5-5.3-T3`, `M5-5.3-T4` | CREATE:PERMANENT<br>CREATE:PERMANENT<br>CREATE:PERMANENT<br>CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroController.java` :: `FicheroController`, `/api/v1/ficheros`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroNoEncontradoException.java` :: `FicheroNoEncontradoException`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandler.java` :: `basePackages`, `FICHERO_NO_ENCONTRADO`<br>`M5/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java` :: `FicheroExceptionHandlerTest`, `FICHERO_NO_ENCONTRADO` | `./mvnw test -Dtest=FicheroExceptionHandlerTest` | El subdominio fichero demuestra un advice selectivo completo. | Contrastar el observable del paso 5.3.12 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S01` Repasar el estado actual | `M5-5.4-T1` | USE:PERMANENT<br>USE:PERMANENT<br>USE:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.application.name`<br>`M5/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource.url`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `spring.datasource.url` | `./mvnw test` | Se inventaría la configuración heredada de M4 antes de reorganizarla. | Contrastar el observable del paso 5.4.1 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S02` Reorganizar application.properties | `M5-5.4-T1` | MODIFY:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.application.name`, `spring.profiles.active`, `spring.jpa.open-in-view` | `./mvnw test` | application.properties conserva sólo propiedades comunes y el perfil por defecto. | Contrastar el observable del paso 5.4.2 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S03` Crear application-dev.properties | `M5-5.4-T2` | MODIFY:PERMANENT | `M5/proyecto/src/main/resources/application-dev.properties` :: `jdbc:h2:mem:testdb`, `app.entorno=dev`, `cors.allowed-origins` | `./mvnw test` | El perfil dev usa H2, logging de desarrollo y propiedades funcionales. | Contrastar el observable del paso 5.4.3 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S04` Crear application-test.properties | `M5-5.4-T2` | CREATE:PERMANENT | `M5/proyecto/src/main/resources/application-test.properties` :: `jdbc:h2:mem:m5test`, `app.entorno=test`, `cors.allowed-origins` | `./mvnw test` | Existe un perfil test aislado y reproducible. | Contrastar el observable del paso 5.4.4 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S05` Crear application-prod.properties | `M5-5.4-T2`, `M5-5.4-T5` | MODIFY:PERMANENT | `M5/proyecto/src/main/resources/application-prod.properties` :: `DB_HOST`, `DB_USER`, `DB_PASSWORD`, `app.entorno=prod` | `./mvnw test` | Producción externaliza credenciales y evita show-sql. | Contrastar el observable del paso 5.4.5 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S06` Activar el perfil dev | `M5-5.4-T2` | MODIFY:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active=dev` | `./mvnw spring-boot:run` | El arranque normal activa dev de forma explícita. | Contrastar el observable del paso 5.4.6 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S07` Probar con el perfil prod | `M5-5.4-T2`, `M5-5.4-T5` | MODIFY:TEMPORARY<br>VERIFY:TEMPORARY<br>MODIFY:TEMPORARY<br>VERIFY:TEMPORARY<br>RESTORE:PERMANENT<br>RESTORE:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active`<br>`M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `spring.datasource.url`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `spring.datasource.url`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `jdbc:postgresql://${DB_HOST:localhost}`<br>`M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active=dev` | `java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod`<br>`./mvnw test` | Se observa prod con H2 temporal y se restaura PostgreSQL y dev antes de cerrar el paso. | Probar prod; restaurar application-prod.properties a PostgreSQL y application.properties a dev; ejecutar tests. |
| `M5-P-54-S08` Crear la clase AppProperties | `M5-5.4-T4` | CREATE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/AppProperties.java` :: `ConfigurationProperties`, `prefix = "app"`, `Validated`, `nombreOficina`, `version`, `entorno` | `./mvnw test` | Las propiedades app.* quedan tipadas y validadas. | Contrastar el observable del paso 5.4.8 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S09` Añadir un bean condicional por perfil | `M5-5.4-T3` | CREATE:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/PerfilConfig.java` :: `Profile("dev")`, `Profile("prod")`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` :: `Profile("dev")` | `./mvnw test` | Los beans pueden existir sólo bajo perfiles determinados. | Contrastar el observable del paso 5.4.9 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S10` Arrancar con distintos perfiles | `M5-5.4-T2`, `M5-5.4-T3` | VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active=dev`<br>`M5/proyecto/src/main/resources/application-test.properties` :: `app.entorno=test`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `app.entorno=prod` | `./mvnw spring-boot:run`<br>`./mvnw spring-boot:run -Dspring-boot.run.profiles=test` | dev/test/prod producen configuraciones observables diferentes sin recompilar. | Contrastar el observable del paso 5.4.10 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S11` Errores comunes del ejercicio | `M5-5.4-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `DB_PASSWORD` | `./mvnw test` | Los errores comunes de perfil, placeholder y secreto quedan identificados. | Contrastar el observable del paso 5.4.11 con PRACTICA.md y el artefacto final. |
| `M5-P-54-S12` Reto resuelto — Test con perfil específico | `M5-5.4-T5` | CREATE:PERMANENT | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/AppPropertiesTest.java` :: `ActiveProfiles("test")`, `AppPropertiesTest`, `cargaElPerfilTest` | `./mvnw test -Dtest=AppPropertiesTest` | El contexto arranca con el perfil test y carga AppProperties. | Contrastar el observable del paso 5.4.12 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S01` Repasar el estado actual | `M5-5.5-T1` | USE:PERMANENT<br>USE:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` :: `/api/v1/alumnos`<br>`M5/proyecto/src/main/resources/application.properties` :: `spring.profiles.active=dev` | `curl http://localhost:8080/api/v1/alumnos` | La API funciona sin que curl aplique la Same-Origin Policy. | Contrastar el observable del paso 5.5.1 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S02` Simular una petición desde otro origen | `M5-5.5-T1` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` :: `/api/v1/alumnos` | `curl -i http://localhost:8080/api/v1/alumnos -H "Origin: http://localhost:3000"` | Antes de CORS, la respuesta no autoriza explícitamente el origen. | Contrastar el observable del paso 5.5.2 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S03` Crear la clase CorsConfig | `M5-5.5-T3` | CREATE:TEMPORARY | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `CorsConfig`, `WebMvcConfigurer`, `addCorsMappings` | `./mvnw test` | Se introduce la configuración global de CORS. | Contrastar el observable del paso 5.5.3 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S04` Arrancar y probar con curl | `M5-5.5-T2`, `M5-5.5-T4` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `allowedOrigins`, `allowedMethods`, `allowCredentials` | `curl -i http://localhost:8080/api/v1/alumnos -H "Origin: http://localhost:3000"` | El origen permitido recibe Access-Control-Allow-Origin. | Contrastar el observable del paso 5.5.4 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S05` Probar con un origen no permitido | `M5-5.5-T4` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `allowedOrigins` | `curl -i http://localhost:8080/api/v1/alumnos -H "Origin: http://localhost:9999"` | Un origen no permitido no recibe autorización CORS. | Contrastar el observable del paso 5.5.5 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S06` Simular un preflight | `M5-5.5-T2` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `OPTIONS`, `maxAge` | `curl -i -X OPTIONS http://localhost:8080/api/v1/alumnos` | El preflight autorizado informa métodos, cabeceras y max-age. | Contrastar el observable del paso 5.5.6 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S07` Probar con un método no permitido | `M5-5.5-T2`, `M5-5.5-T5` | VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `allowedMethods` | `curl -i -X OPTIONS http://localhost:8080/api/v1/alumnos -H "Access-Control-Request-Method: TRACE"` | TRACE no forma parte de los métodos autorizados. | Contrastar el observable del paso 5.5.7 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S08` Configurar CORS con perfiles | `M5-5.5-T3`, `M5-5.5-T4` | MODIFY:PERMANENT<br>MODIFY:PERMANENT<br>MODIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `Value("${cors.allowed-origins}")`, `allowedOrigins`<br>`M5/proyecto/src/main/resources/application-dev.properties` :: `cors.allowed-origins=http://localhost:3000,http://localhost:4200`<br>`M5/proyecto/src/main/resources/application-prod.properties` :: `cors.allowed-origins=${CORS_ALLOWED_ORIGINS:` | `./mvnw test` | Los orígenes permitidos quedan externalizados por perfil. | Contrastar el observable del paso 5.5.8 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S09` Manejar CORS en errores | `M5-5.5-T4` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `addCorsMappings`<br>`M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` :: `ErrorResponse` | `curl -i http://localhost:8080/api/v1/alumnos/99999 -H "Origin: http://localhost:3000"` | También una respuesta de error conserva las cabeceras CORS. | Contrastar el observable del paso 5.5.9 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S10` Errores comunes del ejercicio | `M5-5.5-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` :: `allowCredentials`, `exposedHeaders`, `maxAge`<br>`M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java` :: `CorsConfigTest` | `./mvnw test -Dtest=CorsConfigTest` | La configuración evita combinaciones inseguras y está cubierta por tests. | Contrastar el observable del paso 5.5.10 con PRACTICA.md y el artefacto final. |
| `M5-P-55-S11` Reto resuelto — Probar CORS con un front-end real | `M5-5.5-T4`, `M5-5.5-T5` | CREATE:PERMANENT<br>MODIFY:TEMPORARY<br>VERIFY:TEMPORARY<br>RESTORE:PERMANENT | `M5/frontend-cors/index.html` :: `consultarAlumnos`, `crearAlumno`, `fetch`<br>`M5/proyecto/src/main/resources/application-dev.properties` :: `cors.allowed-origins`<br>`M5/proyecto/src/main/resources/application-dev.properties` :: `cors.allowed-origins`<br>`M5/proyecto/src/main/resources/application-dev.properties` :: `cors.allowed-origins=http://localhost:3000,http://localhost:4200` | `python3 -m http.server 3000`<br>`./mvnw test` | El navegador consume la API desde localhost:3000 y la prueba de bloqueo se revierte. | Servir index.html; comprobar éxito; provocar bloqueo; restaurar allowed-origins; ejecutar tests. |

## Guía -> proyecto

Cada artefacto `GUIDE` tiene un origen en un paso real de M5. Los `INHERITED` proceden de M4-R1; los `SUPPORT` existen para construir, ejecutar o documentar el módulo sin presentarse como contenido nuevo de la guía.

| Artefacto | Clasificación | Origen | Evolución |
|---|---|---|---|
| `M5/frontend-cors/index.html` | GUIDE | `M5-P-55-S11` | — |
| `M5/proyecto/.gitignore` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/.mvn/wrapper/maven-wrapper.properties` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/README.md` | SUPPORT | `M5-SUPPORT` | — |
| `M5/proyecto/config/checkstyle/checkstyle.xml` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/mvnw` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/mvnw.cmd` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/pom.xml` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoEspecificoException.java` | GUIDE | `M5-P-53-S07` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoExceptionHandler.java` | GUIDE | `M5-P-53-S07` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | GUIDE | `M5-P-51-S01` | `M5-P-51-S08`, `M5-P-51-S13` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` | GUIDE | `M5-P-52-S04` | `M5-P-52-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java` | GUIDE | `M5-P-52-S03` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java` | GUIDE | `M5-P-51-S03` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` | GUIDE | `M5-P-53-S03` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java` | GUIDE | `M5-P-52-S12` | `M5-P-53-S04` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorTecnicoException.java` | GUIDE | `M5-P-51-S07` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` | GUIDE | `M5-P-53-S04` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | GUIDE | `M5-P-51-S01` | `M5-P-51-S05` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/OperacionNoPermitidaException.java` | GUIDE | `M5-P-51-S06` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java` | GUIDE | `M5-P-51-S05` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java` | GUIDE | `M5-P-51-S04` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidacionNegocioException.java` | GUIDE | `M5-P-51-S13` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` | GUIDE | `M5-P-53-S02` | `M5-P-53-S10` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/AppProperties.java` | GUIDE | `M5-P-54-S08` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` | GUIDE | `M5-P-55-S03` | `M5-P-55-S08` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/PerfilConfig.java` | GUIDE | `M5-P-54-S09` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | GUIDE | `M5-P-51-S09` | `M5-P-51-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroController.java` | GUIDE | `M5-P-53-S12` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandler.java` | GUIDE | `M5-P-53-S12` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroNoEncontradoException.java` | GUIDE | `M5-P-53-S12` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/main/resources/application-dev.properties` | GUIDE | `M5-P-54-S01` | `M5-P-54-S03`, `M5-P-55-S08`, `M5-P-55-S11`, `M5-P-55-S11` |
| `M5/proyecto/src/main/resources/application-prod.properties` | GUIDE | `M5-P-54-S01` | `M5-P-54-S05`, `M5-P-54-S07`, `M5-P-54-S07`, `M5-P-55-S08` |
| `M5/proyecto/src/main/resources/application-test.properties` | GUIDE | `M5-P-54-S04` | — |
| `M5/proyecto/src/main/resources/application.properties` | INHERITED | `M4-R1-CUMULATIVE` | `M5-P-54-S02`, `M5-P-54-S06`, `M5-P-54-S07`, `M5-P-54-S07` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | GUIDE | `M5-P-51-S10` | `M5-P-51-S10` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/AplicacionExceptionTest.java` | GUIDE | `M5-P-51-S10` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` | GUIDE | `M5-P-52-S10` | `M5-P-53-S08` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/AppPropertiesTest.java` | GUIDE | `M5-P-54-S12` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java` | GUIDE | `M5-P-55-S10` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java` | GUIDE | `M5-P-53-S12` | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | INHERITED | `M4-R1-CUMULATIVE` | — |

## Proyecto -> guía

Para cualquier fichero final, busca su fila anterior y después el paso de origen/evolución. Desde ese paso puedes volver a los `theory_refs`. Esta es la ruta inversa proyecto -> guía -> práctica -> teoría.

## M4 -> M5: continuidad acumulativa

| M4 | M5 | Estado | Paso(s) | Motivo |
|---|---|---|---|---|
| `M4/proyecto/.gitignore` | `M5/proyecto/.gitignore` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/.mvn/wrapper/maven-wrapper.properties` | `M5/proyecto/.mvn/wrapper/maven-wrapper.properties` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/README.md` | `M5/proyecto/README.md` | MODIFIED | — | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/config/checkstyle/checkstyle.xml` | `M5/proyecto/config/checkstyle/checkstyle.xml` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/mvnw` | `M5/proyecto/mvnw` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/mvnw.cmd` | `M5/proyecto/mvnw.cmd` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/pom.xml` | `M5/proyecto/pom.xml` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | PRESERVED | `M5-P-55-S01`, `M5-P-55-S02` | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | MODIFIED | `M5-P-51-S01`, `M5-P-51-S08`, `M5-P-51-S13` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` | — | REMOVED | `M5-P-52-S01`, `M5-P-52-S05`, `M5-P-52-S06`, `M5-P-53-S01`, `M5-P-53-S05` | Sustituido de forma explícita por la arquitectura M5 de handlers ordenados. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | MODIFIED | `M5-P-51-S01`, `M5-P-51-S05` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | PRESERVED | `M5-P-54-S09` | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | MODIFIED | `M5-P-51-S09` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/main/resources/application-dev.properties` | `M5/proyecto/src/main/resources/application-dev.properties` | MODIFIED | `M5-P-54-S01`, `M5-P-54-S03`, `M5-P-55-S08`, `M5-P-55-S11` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/main/resources/application-prod.properties` | `M5/proyecto/src/main/resources/application-prod.properties` | MODIFIED | `M5-P-54-S01`, `M5-P-54-S05`, `M5-P-54-S07`, `M5-P-54-S10`, `M5-P-54-S11`, `M5-P-55-S08` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/main/resources/application.properties` | `M5/proyecto/src/main/resources/application.properties` | PRESERVED | `M5-P-54-S01`, `M5-P-54-S02`, `M5-P-54-S06`, `M5-P-54-S07`, `M5-P-54-S10`, `M5-P-54-S11`, `M5-P-55-S01` | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | MODIFIED | `M5-P-51-S10` | Artefacto heredado evolucionado por los pasos de M5. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | PRESERVED | — | Artefacto heredado sin cambios desde M4-R1. |

## Artefactos necesarios no introducidos en este módulo

Los artefactos clasificados como `INHERITED` o `SUPPORT` no se presentan como creaciones pedagógicas de M5. Su procedencia y efecto de ausencia quedan inventariados en `M5.json`.

## Trazabilidad inversa por artefacto/símbolo

| Artefacto | Símbolos finales trazados | Pasos relacionados |
|---|---|---|
| `M5/frontend-cors/index.html` | `consultarAlumnos`, `crearAlumno`, `fetch` | `M5-P-55-S11` |
| `M5/proyecto/.gitignore` | — | — |
| `M5/proyecto/.mvn/wrapper/maven-wrapper.properties` | — | — |
| `M5/proyecto/README.md` | — | — |
| `M5/proyecto/config/checkstyle/checkstyle.xml` | — | — |
| `M5/proyecto/mvnw` | — | — |
| `M5/proyecto/mvnw.cmd` | — | — |
| `M5/proyecto/pom.xml` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | `/api/v1/alumnos` | `M5-P-55-S01`, `M5-P-55-S02` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoEspecificoException.java` | `AlumnoEspecificoException` | `M5-P-53-S07` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoExceptionHandler.java` | `basePackages`, `Order(0)`, `AlumnoEspecificoException` | `M5-P-53-S07` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | `AlumnoService`, `RecursoDuplicadoException`, `RecursoNoEncontradoException`, `ValidacionNegocioException`, `validarNombre` | `M5-P-51-S01`, `M5-P-51-S08`, `M5-P-51-S13` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` | `ErrorResponse`, `timestamp`, `status`, `codigo`, `mensaje`, `path`, `errors`, `traceId`, `getTraceId` | `M5-P-52-S04`, `M5-P-52-S05`, `M5-P-52-S07`, `M5-P-52-S11`, `M5-P-52-S12`, `M5-P-53-S01`, `M5-P-53-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java` | `ValidationError`, `rejectedValue`, `message` | `M5-P-52-S03`, `M5-P-52-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java` | `AplicacionException`, `codigo`, `getCodigo` | `M5-P-51-S03`, `M5-P-51-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` | `RestControllerAdvice`, `Order(2)`, `RecursoNoEncontradoException`, `RecursoDuplicadoException`, `BusinessExceptionHandler`, `ErrorResponse` | `M5-P-53-S03`, `M5-P-53-S06`, `M5-P-53-S11`, `M5-P-55-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java` | `newTraceId`, `setTraceId`, `build` | `M5-P-52-S12`, `M5-P-53-S04` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorTecnicoException.java` | `ErrorTecnicoException`, `ERROR_TECNICO`, `Throwable` | `M5-P-51-S07` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` | `RestControllerAdvice`, `Order(3)`, `ERROR_INTERNO`, `traceId`, `GenericExceptionHandler` | `M5-P-53-S04`, `M5-P-53-S06`, `M5-P-53-S09`, `M5-P-53-S11` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | `NegocioException`, `AplicacionException` | `M5-P-51-S01`, `M5-P-51-S05` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/OperacionNoPermitidaException.java` | `OperacionNoPermitidaException`, `OPERACION_NO_PERMITIDA` | `M5-P-51-S06` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java` | `RecursoDuplicadoException`, `RECURSO_DUPLICADO`, `getCampo`, `getValor` | `M5-P-51-S05`, `M5-P-51-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java` | `RecursoNoEncontradoException`, `RECURSO_NO_ENCONTRADO`, `getRecurso`, `getId` | `M5-P-51-S04`, `M5-P-52-S08` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidacionNegocioException.java` | `ValidacionNegocioException`, `VALIDACION_NEGOCIO`, `getCampo`, `getMotivo` | `M5-P-51-S13` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` | `RestControllerAdvice`, `Order(1)`, `MethodArgumentNotValidException`, `MethodArgumentTypeMismatchException`, `TIPO_INCORRECTO`, `ValidationExceptionHandler` | `M5-P-53-S02`, `M5-P-53-S06`, `M5-P-53-S10`, `M5-P-53-S11` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/AppProperties.java` | `ConfigurationProperties`, `prefix = "app"`, `Validated`, `nombreOficina`, `version`, `entorno` | `M5-P-54-S08` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` | `allowedOrigins`, `allowedMethods`, `allowCredentials`, `OPTIONS`, `maxAge`, `Value("${cors.allowed-origins}")`, `addCorsMappings`, `exposedHeaders` | `M5-P-55-S03`, `M5-P-55-S04`, `M5-P-55-S05`, `M5-P-55-S06`, `M5-P-55-S07`, `M5-P-55-S08`, `M5-P-55-S09`, `M5-P-55-S10` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | `Profile("dev")` | `M5-P-54-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/PerfilConfig.java` | `Profile("dev")`, `Profile("prod")` | `M5-P-54-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | `RecursoDuplicadoException`, `RecursoNoEncontradoException`, `OperacionNoPermitidaException` | `M5-P-51-S09` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroController.java` | `FicheroController`, `/api/v1/ficheros` | `M5-P-53-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandler.java` | `basePackages`, `FICHERO_NO_ENCONTRADO` | `M5-P-53-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroNoEncontradoException.java` | `FicheroNoEncontradoException` | `M5-P-53-S12` |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | — | — |
| `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | — | — |
| `M5/proyecto/src/main/resources/application-dev.properties` | `spring.datasource.url`, `jdbc:h2:mem:testdb`, `app.entorno=dev`, `cors.allowed-origins`, `cors.allowed-origins=http://localhost:3000,http://localhost:4200` | `M5-P-54-S01`, `M5-P-54-S03`, `M5-P-55-S08`, `M5-P-55-S11` |
| `M5/proyecto/src/main/resources/application-prod.properties` | `spring.datasource.url`, `DB_HOST`, `DB_USER`, `DB_PASSWORD`, `app.entorno=prod`, `jdbc:postgresql://${DB_HOST:localhost}`, `cors.allowed-origins=${CORS_ALLOWED_ORIGINS:` | `M5-P-54-S01`, `M5-P-54-S05`, `M5-P-54-S07`, `M5-P-54-S10`, `M5-P-54-S11`, `M5-P-55-S08` |
| `M5/proyecto/src/main/resources/application-test.properties` | `jdbc:h2:mem:m5test`, `app.entorno=test`, `cors.allowed-origins` | `M5-P-54-S04`, `M5-P-54-S10` |
| `M5/proyecto/src/main/resources/application.properties` | `spring.application.name`, `spring.profiles.active`, `spring.jpa.open-in-view`, `spring.profiles.active=dev` | `M5-P-54-S01`, `M5-P-54-S02`, `M5-P-54-S06`, `M5-P-54-S07`, `M5-P-54-S10`, `M5-P-54-S11`, `M5-P-55-S01` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | `RecursoDuplicadoException`, `assertThrows` | `M5-P-51-S10` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/AplicacionExceptionTest.java` | `AplicacionExceptionTest`, `RECURSO_DUPLICADO`, `ERROR_TECNICO` | `M5-P-51-S10`, `M5-P-51-S11` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` | `ErrorHandlingTest`, `VALIDACION`, `RECURSO_DUPLICADO`, `ERROR_INTERNO`, `isConflict`, `isInternalServerError` | `M5-P-52-S10`, `M5-P-52-S11`, `M5-P-53-S08` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/AppPropertiesTest.java` | `ActiveProfiles("test")`, `AppPropertiesTest`, `cargaElPerfilTest` | `M5-P-54-S12` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java` | `CorsConfigTest` | `M5-P-55-S10` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | — | — |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java` | `FicheroExceptionHandlerTest`, `FICHERO_NO_ENCONTRADO` | `M5-P-53-S12` |
| `M5/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | — | — |

## Estados temporales y restauraciones

- `M5-P-52-S01`: `USE` temporal sobre `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-52-S05`: `MODIFY` temporal sobre `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-52-S06`: `MODIFY` temporal sobre `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-53-S01`: `USE` temporal sobre `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-54-S07`: `MODIFY` temporal sobre `M5/proyecto/src/main/resources/application.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-54-S07`: `VERIFY` temporal sobre `M5/proyecto/src/main/resources/application.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-54-S07`: `MODIFY` temporal sobre `M5/proyecto/src/main/resources/application-prod.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-54-S07`: `VERIFY` temporal sobre `M5/proyecto/src/main/resources/application-prod.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-55-S03`: `CREATE` temporal sobre `M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-55-S11`: `MODIFY` temporal sobre `M5/proyecto/src/main/resources/application-dev.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.
- `M5-P-55-S11`: `VERIFY` temporal sobre `M5/proyecto/src/main/resources/application-dev.properties`. El validador exige un cierre posterior `RESTORE`, `DELETE` o evolución permanente del mismo artefacto.

## Regresión heredada M4 -> M5

- Los **54/54** ficheros del proyecto M4-R1 están contabilizados.
- `GlobalExceptionHandler.java` es la única retirada y está enlazada a `M5-P-53-S05`.
- Los 9 tests heredados de M4-R1 permanecen; M5 añade pruebas de excepciones, error HTTP, perfiles, CORS y advice selectivo.

## Gates automáticos

- Estructura fuente: 5 puntos, 25 bloques, 75 subpuntos, 60 pasos.
- Código fuente en guías: 159 bloques y ninguna línea de bloque > 85 caracteres.
- Trazabilidad v3: IDs, anchors, pasos, símbolos, inventario y continuidad.
- Cierre de temporales, incluida prueba de `prod` y bloqueo CORS.
- Mutation gate: corrupciones controladas deben ser rechazadas.
- Maven/Checkstyle/Javadoc/runtime/OpenAPI en BAT/CI.

## Resultado global

**M5 queda preparado para cierre sólo cuando los validadores, Maven, tests, PDFs, runtime y CI estén verdes.**
