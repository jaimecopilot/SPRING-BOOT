# TRAZABILIDAD M4 - CONTRATO FUERTE R1

> Este informe se genera a partir de `.course/traceability/M4.json` y de los siete manifiestos por punto. No es una tabla decorativa: resume el contrato máquina-legible que validan los gates de M4.

## Cómo puedes auditarlo tú

1. Comprueba `schema_version: 3`, `status: COMPLETE` y las políticas en `M4.json`.
2. Recorre cualquiera de los 92 pasos desde teoría hasta observable y vuelve desde el artefacto al paso.
3. Revisa la tabla M3 -> M4: ningún fichero de `M3/proyecto` puede desaparecer sin una transición explícita.
4. Ejecuta `python .course/traceability/validate_m4.py .`, `validate_m4_human.py` y `validate_m4_mutation.py`.
5. Ejecuta `M4/proyecto/mvnw.cmd clean verify` en Windows o `./mvnw clean verify` en Linux/macOS.

## Resumen verificable

- Esquema: **v3**. Estado: **COMPLETE**.
- Conceptos teóricos: **35/35**.
- Pasos prácticos: **92/92**.
- Artefactos inventariados en `M4/proyecto`: **54**.
- Artefactos de `M3/proyecto` contabilizados en M4: **38/38**.
- Artefactos M3 desaparecidos sin justificar: **0**.
- Estados temporales abiertos: **0** (4.1.11 cierra con RESTORE).

Cadena obligatoria: **concepto teórico -> paso práctico -> acción -> artefacto/símbolo -> comando -> observable -> verificación**, con recorrido inverso desde el proyecto.

## Teoría -> práctica

| Concepto | Título | Pasos que lo aplican |
|---|---|---|
| `M4-4.1-T1` | El problema de la persistencia | `M4-P-41-S01`, `M4-P-41-S02`, `M4-P-41-S03` |
| `M4-4.1-T2` | Configuración de Spring Boot para JPA | `M4-P-41-S04`, `M4-P-41-S05`, `M4-P-41-S06` |
| `M4-4.1-T3` | Entidades JPA | `M4-P-41-S07`, `M4-P-41-S08` |
| `M4-4.1-T4` | Repositorios Spring Data JPA | `M4-P-41-S09`, `M4-P-41-S10`, `M4-P-41-S11` |
| `M4-4.1-T5` | Transacciones y buenas prácticas | `M4-P-41-S12`, `M4-P-41-S13` |
| `M4-4.2-T1` | Qué es una entidad y su ciclo de vida | `M4-P-42-S01`, `M4-P-42-S02`, `M4-P-42-S03` |
| `M4-4.2-T2` | Generación de identificadores | `M4-P-42-S04`, `M4-P-42-S05`, `M4-P-42-S06` |
| `M4-4.2-T3` | Mapeo de columnas | `M4-P-42-S07`, `M4-P-42-S08` |
| `M4-4.2-T4` | Anotaciones avanzadas | `M4-P-42-S09`, `M4-P-42-S10`, `M4-P-42-S11` |
| `M4-4.2-T5` | Buenas prácticas y errores comunes | `M4-P-42-S12`, `M4-P-42-S13` |
| `M4-4.3-T1` | La jerarquía de repositorios | `M4-P-43-S01`, `M4-P-43-S02`, `M4-P-43-S03` |
| `M4-4.3-T2` | Métodos derivados | `M4-P-43-S04`, `M4-P-43-S05`, `M4-P-43-S06` |
| `M4-4.3-T3` | Consultas con @Query | `M4-P-43-S07`, `M4-P-43-S08`, `M4-P-43-S09` |
| `M4-4.3-T4` | Paginación y ordenación | `M4-P-43-S10`, `M4-P-43-S11`, `M4-P-43-S12` |
| `M4-4.3-T5` | Consultas dinámicas con Specifications | `M4-P-43-S13`, `M4-P-43-S14` |
| `M4-4.4-T1` | Los cuatro tipos de relaciones | `M4-P-44-S01`, `M4-P-44-S02`, `M4-P-44-S03` |
| `M4-4.4-T2` | @ManyToOne | `M4-P-44-S04`, `M4-P-44-S05`, `M4-P-44-S06` |
| `M4-4.4-T3` | @OneToMany | `M4-P-44-S07`, `M4-P-44-S08` |
| `M4-4.4-T4` | Cascade y orphanRemoval | `M4-P-44-S09`, `M4-P-44-S10`, `M4-P-44-S11` |
| `M4-4.4-T5` | Rendimiento y problemas comunes | `M4-P-44-S12`, `M4-P-44-S13` |
| `M4-4.5-T1` | JPQL en profundidad | `M4-P-45-S01`, `M4-P-45-S02`, `M4-P-45-S03` |
| `M4-4.5-T2` | Joins en JPQL | `M4-P-45-S04`, `M4-P-45-S05`, `M4-P-45-S06` |
| `M4-4.5-T3` | Funciones y expresiones | `M4-P-45-S07`, `M4-P-45-S08` |
| `M4-4.5-T4` | Agregaciones y proyecciones | `M4-P-45-S09`, `M4-P-45-S10`, `M4-P-45-S11` |
| `M4-4.5-T5` | Buenas prácticas y errores | `M4-P-45-S12`, `M4-P-45-S13` |
| `M4-4.6-T1` | Qué es una transacción | `M4-P-46-S01`, `M4-P-46-S02`, `M4-P-46-S03` |
| `M4-4.6-T2` | Propagación | `M4-P-46-S04`, `M4-P-46-S05`, `M4-P-46-S06` |
| `M4-4.6-T3` | Aislamiento y concurrencia | `M4-P-46-S07`, `M4-P-46-S08` |
| `M4-4.6-T4` | Rollback y excepciones | `M4-P-46-S09`, `M4-P-46-S10`, `M4-P-46-S11` |
| `M4-4.6-T5` | Buenas prácticas y errores | `M4-P-46-S12`, `M4-P-46-S13` |
| `M4-4.7-T1` | Por qué testear JPA es distinto | `M4-P-47-S01`, `M4-P-47-S02`, `M4-P-47-S03` |
| `M4-4.7-T2` | @DataJpaTest en profundidad | `M4-P-47-S04`, `M4-P-47-S05`, `M4-P-47-S06` |
| `M4-4.7-T3` | Probar consultas | `M4-P-47-S07`, `M4-P-47-S08` |
| `M4-4.7-T4` | Probar relaciones y transacciones | `M4-P-47-S09`, `M4-P-47-S10`, `M4-P-47-S11` |
| `M4-4.7-T5` | Buenas prácticas y errores | `M4-P-47-S12`, `M4-P-47-S13` |

## Práctica -> acción -> artefacto -> verificación

| Paso | Teoría | Acción/estado | Artefacto y símbolos | Comando | Observable | Verificación |
|---|---|---|---|---|---|---|
| `M4-P-41-S01` Añadir las dependencias al pom.xml | `M4-4.1-T1` | MODIFY:PERMANENT | `M4/proyecto/pom.xml` :: `spring-boot-starter-data-jpa`, `3.5.16`, `17` | ./mvnw dependency:tree | Dependencias JPA y H2 resueltas por Maven | Verificar el observable del paso 4.1.1 y contrastarlo con PRACTICA.md |
| `M4-P-41-S02` Configurar application.properties | `M4-4.1-T1` | USE:PERMANENT | `M4/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource`, `spring.jpa`, `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` | ./mvnw spring-boot:run | Spring Boot conecta HikariCP/Hibernate con H2 | Verificar el observable del paso 4.1.2 y contrastarlo con PRACTICA.md |
| `M4-P-41-S03` Arrancar y verificar la conexión | `M4-4.1-T1` | VERIFY:PERMANENT | `M4/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` | ./mvnw spring-boot:run | Aplicación arranca y la consola H2 queda disponible | Verificar el observable del paso 4.1.3 y contrastarlo con PRACTICA.md |
| `M4-P-41-S04` Crear la entidad Alumno | `M4-4.1-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@Entity`, `@Id`, `@Column`, `Alumno` | ./mvnw test | Hibernate reconoce Alumno como entidad | Verificar el observable del paso 4.1.4 y contrastarlo con PRACTICA.md |
| `M4-P-41-S05` Arrancar y ver la tabla creada | `M4-4.1-T2` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw spring-boot:run | El log muestra CREATE TABLE y constraint UNIQUE | Verificar el observable del paso 4.1.5 y contrastarlo con PRACTICA.md |
| `M4-P-41-S06` Crear el repositorio Spring Data JPA | `M4-4.1-T2` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `JpaRepository<Alumno`, `Long>`, `AlumnoRepository` | ./mvnw test | Repositorio JPA arranca sin implementación manual | Verificar el observable del paso 4.1.6 y contrastarlo con PRACTICA.md |
| `M4-P-41-S07` Modificar el servicio para usar la entidad | `M4-4.1-T3` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `@Transactional`, `AlumnoService` | ./mvnw test | Servicio persiste y consulta mediante JpaRepository | Verificar el observable del paso 4.1.7 y contrastarlo con PRACTICA.md |
| `M4-P-41-S08` Modificar el controlador | `M4-4.1-T3` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` :: `AlumnoController` | curl http://localhost:8080/api/v1/alumnos | CRUD REST responde sobre datos persistentes | Verificar el observable del paso 4.1.8 y contrastarlo con PRACTICA.md |
| `M4-P-41-S09` Crear un CommandLineRunner para datos iniciales | `M4-4.1-T4` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` :: `CommandLineRunner`, `DatosInicialesConfig` | ./mvnw spring-boot:run | Se insertan datos iniciales sólo con tabla vacía | Verificar el observable del paso 4.1.9 y contrastarlo con PRACTICA.md |
| `M4-P-41-S10` Arrancar y probar | `M4-4.1-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` :: `AlumnoController` | curl http://localhost:8080/api/v1/alumnos | Endpoints devuelven códigos y JSON esperados | Verificar el observable del paso 4.1.10 y contrastarlo con PRACTICA.md |
| `M4-P-41-S11` Verificar la persistencia | `M4-4.1-T4` | MODIFY:TEMPORARY<br>VERIFY:TEMPORARY<br>RESTORE:PERMANENT | `M4/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto`<br>`M4/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto`<br>`M4/proyecto/src/main/resources/application-dev.properties` :: `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` | ./mvnw spring-boot:run<br>./mvnw test | H2 en fichero conserva el alumno entre reinicios durante la prueba<br>tras RESTORE, application-dev.properties vuelve a H2 en memoria y create-drop<br>la suite Maven queda verde después de restaurar la configuración canónica | confirmar que la guía contiene el cierre MODIFY -> VERIFY -> RESTORE<br>comprobar los dos valores canónicos en application-dev.properties<br>ejecutar ./mvnw test después de la restauración |
| `M4-P-41-S12` Errores comunes del ejercicio | `M4-4.1-T5` | VERIFY:PERMANENT | `M4/proyecto/src/main/resources/application-dev.properties` :: `H2`, `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` | ./mvnw test | Los fallos comunes quedan identificados y corregidos | Verificar el observable del paso 4.1.12 y contrastarlo con PRACTICA.md |
| `M4-P-41-S13` Reto resuelto — Consulta personalizada con @Query | `M4-4.1-T5` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Consulta personalizada devuelve el subconjunto esperado | Verificar el observable del paso 4.1.13 y contrastarlo con PRACTICA.md |
| `M4-P-42-S01` Repasar la entidad actual | `M4-4.2-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | La entidad base compila y es gestionada por JPA | Verificar el observable del paso 4.2.1 y contrastarlo con PRACTICA.md |
| `M4-P-42-S02` Añadir un enum de estado | `M4-4.2-T1` | CREATE:PERMANENT<br>CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` :: `EstadoAlumno`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@Enumerated(EnumType.STRING)`, `Alumno` | ./mvnw test | El estado se persiste como texto | Verificar el observable del paso 4.2.2 y contrastarlo con PRACTICA.md |
| `M4-P-42-S03` Añadir un campo calculado con @Transient | `M4-4.2-T1` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | La edad se calcula y no genera columna | Verificar el observable del paso 4.2.3 y contrastarlo con PRACTICA.md |
| `M4-P-42-S04` Crear una clase embebida Direccion | `M4-4.2-T2` | CREATE:PERMANENT<br>CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` :: `@Embeddable`, `Direccion`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@Embedded`, `Alumno` | ./mvnw test | Los campos de dirección se integran en ALUMNOS | Verificar el observable del paso 4.2.4 y contrastarlo con PRACTICA.md |
| `M4-P-42-S05` Añadir un campo de versión con @Version | `M4-4.2-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@Version`, `Alumno` | ./mvnw test | Hibernate mantiene versión para bloqueo optimista | Verificar el observable del paso 4.2.5 y contrastarlo con PRACTICA.md |
| `M4-P-42-S06` Añadir un campo @Lob para observaciones | `M4-4.2-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | El texto largo se mapea como LOB | Verificar el observable del paso 4.2.6 y contrastarlo con PRACTICA.md |
| `M4-P-42-S07` Arrancar y ver el SQL generado | `M4-4.2-T3` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw spring-boot:run | El SQL refleja enum, embebido, LOB y versión | Verificar el observable del paso 4.2.7 y contrastarlo con PRACTICA.md |
| `M4-P-42-S08` Actualizar el DTO y el servicio | `M4-4.2-T3` | MODIFY:PERMANENT<br>MODIFY:PERMANENT<br>MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` :: `AlumnoResponseDTO`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` :: `DireccionDTO` | ./mvnw test | API expone datos sin devolver la entidad directamente | Verificar el observable del paso 4.2.8 y contrastarlo con PRACTICA.md |
| `M4-P-42-S09` Arrancar y probar | `M4-4.2-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | curl http://localhost:8080/api/v1/alumnos | Creación y consulta siguen funcionando | Verificar el observable del paso 4.2.9 y contrastarlo con PRACTICA.md |
| `M4-P-42-S10` Ver el SQL de inserción | `M4-4.2-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw spring-boot:run | El INSERT contiene las columnas persistentes esperadas | Verificar el observable del paso 4.2.10 y contrastarlo con PRACTICA.md |
| `M4-P-42-S11` Añadir equals y hashCode | `M4-4.2-T4` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `equals`, `Alumno` | ./mvnw test | La igualdad no depende de campos mutables | Verificar el observable del paso 4.2.11 y contrastarlo con PRACTICA.md |
| `M4-P-42-S12` Errores comunes del ejercicio | `M4-4.2-T5` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | Errores de mapeo quedan cubiertos por validación | Verificar el observable del paso 4.2.12 y contrastarlo con PRACTICA.md |
| `M4-P-42-S13` Reto resuelto — Añadir un campo @Lob binario | `M4-4.2-T5` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | El binario se mapea como LOB sin romper el modelo | Verificar el observable del paso 4.2.13 y contrastarlo con PRACTICA.md |
| `M4-P-43-S01` Repasar el repositorio actual | `M4-4.3-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `JpaRepository`, `JpaSpecificationExecutor`, `AlumnoRepository` | ./mvnw test | Contrato de repositorio disponible | Verificar el observable del paso 4.3.1 y contrastarlo con PRACTICA.md |
| `M4-P-43-S02` Añadir métodos derivados | `M4-4.3-T1` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Spring Data deriva consultas del nombre | Verificar el observable del paso 4.3.2 y contrastarlo con PRACTICA.md |
| `M4-P-43-S03` Arrancar y verificar | `M4-4.3-T1` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | Consultas derivadas devuelven resultados esperados | Verificar el observable del paso 4.3.3 y contrastarlo con PRACTICA.md |
| `M4-P-43-S04` Añadir un método con @Query | `M4-4.3-T2` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | JPQL se valida y ejecuta | Verificar el observable del paso 4.3.4 y contrastarlo con PRACTICA.md |
| `M4-P-43-S05` Añadir un método con @Modifying | `M4-4.3-T2` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `@Modifying`, `AlumnoRepository` | ./mvnw test | Bulk update modifica filas dentro de transacción | Verificar el observable del paso 4.3.5 y contrastarlo con PRACTICA.md |
| `M4-P-43-S06` Usar el método @Modifying desde el servicio | `M4-4.3-T2` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `actualizarEstadoPorCurso`, `AlumnoService` | ./mvnw test | Servicio encapsula la operación @Modifying | Verificar el observable del paso 4.3.6 y contrastarlo con PRACTICA.md |
| `M4-P-43-S07` Añadir paginación | `M4-4.3-T3` | MODIFY:PERMANENT<br>MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `Pageable`, `Page`, `AlumnoRepository`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `Page`, `AlumnoService` | ./mvnw test | Repositorio produce páginas deterministas | Verificar el observable del paso 4.3.7 y contrastarlo con PRACTICA.md |
| `M4-P-43-S08` Probar la paginación | `M4-4.3-T3` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `page`, `AlumnoRepository` | curl 'http://localhost:8080/api/v1/alumnos?page=0&size=2' | Respuesta contiene metadatos de paginación | Verificar el observable del paso 4.3.8 y contrastarlo con PRACTICA.md |
| `M4-P-43-S09` Añadir Specifications | `M4-4.3-T3` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` :: `Specification<Alumno>`, `AlumnoSpecifications` | ./mvnw test | Filtros dinámicos se pueden combinar | Verificar el observable del paso 4.3.9 y contrastarlo con PRACTICA.md |
| `M4-P-43-S10` Crear una clase de Specifications | `M4-4.3-T4` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` :: `curso`, `dni`, `nacidosEntre`, `activos`, `AlumnoSpecifications` | ./mvnw test | Predicados JPA se construyen según parámetros | Verificar el observable del paso 4.3.10 y contrastarlo con PRACTICA.md |
| `M4-P-43-S11` Usar Specifications en el servicio | `M4-4.3-T4` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `Specification.allOf`, `AlumnoService` | ./mvnw test | Servicio aplica filtros opcionales sin concatenar JPQL | Verificar el observable del paso 4.3.11 y contrastarlo con PRACTICA.md |
| `M4-P-43-S12` Probar Specifications | `M4-4.3-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | curl 'http://localhost:8080/api/v1/alumnos?curso=5%C2%BA%20Primaria' | Filtro devuelve sólo alumnos compatibles | Verificar el observable del paso 4.3.12 y contrastarlo con PRACTICA.md |
| `M4-P-43-S13` Errores comunes del ejercicio | `M4-4.3-T5` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Errores de firmas, Page y @Modifying se detectan | Verificar el observable del paso 4.3.13 y contrastarlo con PRACTICA.md |
| `M4-P-43-S14` Reto resuelto — Consulta con agregación | `M4-4.3-T5` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `contarPorCurso`, `AlumnoRepository` | ./mvnw test | Agregación GROUP BY devuelve conteos por curso | Verificar el observable del paso 4.3.14 y contrastarlo con PRACTICA.md |
| `M4-P-44-S01` Crear la entidad Curso | `M4-4.4-T1` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` :: `@OneToMany`, `Curso` | ./mvnw test | Hibernate crea tabla CURSOS | Verificar el observable del paso 4.4.1 y contrastarlo con PRACTICA.md |
| `M4-P-44-S02` Modificar la entidad Alumno para añadir la relación | `M4-4.4-T1` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@ManyToOne`, `@JoinColumn`, `Alumno` | ./mvnw test | ALUMNOS referencia CURSOS con clave foránea | Verificar el observable del paso 4.4.2 y contrastarlo con PRACTICA.md |
| `M4-P-44-S03` Arrancar y ver el SQL generado | `M4-4.4-T1` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` :: `Curso`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw spring-boot:run | SQL generado contiene la relación | Verificar el observable del paso 4.4.3 y contrastarlo con PRACTICA.md |
| `M4-P-44-S04` Crear el repositorio de Curso | `M4-4.4-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` :: `JpaRepository<Curso`, `Long>`, `CursoRepository` | ./mvnw test | Curso dispone de persistencia Spring Data | Verificar el observable del paso 4.4.4 y contrastarlo con PRACTICA.md |
| `M4-P-44-S05` Crear el DTO de Curso | `M4-4.4-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` :: `CursoDTO` | ./mvnw test | La API no serializa directamente la colección LAZY | Verificar el observable del paso 4.4.5 y contrastarlo con PRACTICA.md |
| `M4-P-44-S06` Crear el servicio de Curso | `M4-4.4-T2` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` :: `CursoService` | ./mvnw test | Servicio accede a alumnos dentro de transacción | Verificar el observable del paso 4.4.6 y contrastarlo con PRACTICA.md |
| `M4-P-44-S07` Modificar el AlumnoService para asignar el curso | `M4-4.4-T3` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `obtenerOCrearCurso`, `AlumnoService` | ./mvnw test | Alumno se asocia a una entidad Curso gestionada | Verificar el observable del paso 4.4.7 y contrastarlo con PRACTICA.md |
| `M4-P-44-S08` Crear el controlador de Curso | `M4-4.4-T3` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` :: `api`, `v1`, `cursos`, `CursoController` | curl http://localhost:8080/api/v1/cursos | API de cursos responde JSON | Verificar el observable del paso 4.4.8 y contrastarlo con PRACTICA.md |
| `M4-P-44-S09` Añadir el método listarPorCurso al AlumnoService | `M4-4.4-T4` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | Servicio puede filtrar alumnos por relación | Verificar el observable del paso 4.4.9 y contrastarlo con PRACTICA.md |
| `M4-P-44-S10` Arrancar y probar | `M4-4.4-T4` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` :: `Curso`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | curl http://localhost:8080/api/v1/cursos | Relación funciona sin recursión JSON | Verificar el observable del paso 4.4.10 y contrastarlo con PRACTICA.md |
| `M4-P-44-S11` Detectar y resolver el problema N+1 | `M4-4.4-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Detalle carga curso en una sola consulta lógica | Verificar el observable del paso 4.4.11 y contrastarlo con PRACTICA.md |
| `M4-P-44-S12` Errores comunes del ejercicio | `M4-4.4-T5` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` :: `Curso`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `Alumno` | ./mvnw test | Se controlan owning side, LAZY y cascadas | Verificar el observable del paso 4.4.12 y contrastarlo con PRACTICA.md |
| `M4-P-44-S13` Reto resuelto — Eliminar un curso con cascade | `M4-4.4-T5` | MODIFY:PERMANENT<br>MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` :: `Curso`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` :: `CursoService` | ./mvnw test | El borrado de Curso respeta la cascada definida | Verificar el observable del paso 4.4.13 y contrastarlo con PRACTICA.md |
| `M4-P-45-S01` Repasar el repositorio actual | `M4-4.5-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Repositorio contiene consultas JPQL válidas | Verificar el observable del paso 4.5.1 y contrastarlo con PRACTICA.md |
| `M4-P-45-S02` Consulta con JOIN simple | `M4-4.5-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | JPQL navega la relación Alumno.curso | Verificar el observable del paso 4.5.2 y contrastarlo con PRACTICA.md |
| `M4-P-45-S03` Consulta con JOIN FETCH | `M4-4.5-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Detalle evita acceso LAZY posterior | Verificar el observable del paso 4.5.3 y contrastarlo con PRACTICA.md |
| `M4-P-45-S04` Consulta con función de string | `M4-4.5-T2` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` :: `AlumnoSpecifications` | ./mvnw test | Filtro ignora mayúsculas cuando procede | Verificar el observable del paso 4.5.4 y contrastarlo con PRACTICA.md |
| `M4-P-45-S05` Consulta con función de fecha | `M4-4.5-T2` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Consulta filtra por fecha | Verificar el observable del paso 4.5.5 y contrastarlo con PRACTICA.md |
| `M4-P-45-S06` Consulta con agregación | `M4-4.5-T2` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `count`, `group by`, `AlumnoRepository` | ./mvnw test | Agregación devuelve conteos | Verificar el observable del paso 4.5.6 y contrastarlo con PRACTICA.md |
| `M4-P-45-S07` Consulta con HAVING | `M4-4.5-T3` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Consulta agregada conserva semántica JPQL | Verificar el observable del paso 4.5.7 y contrastarlo con PRACTICA.md |
| `M4-P-45-S08` Consulta con proyección a DTO | `M4-4.5-T3` | USE:PERMANENT<br>USE:PERMANENT<br>USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` :: `CursoDTO`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` :: `AlumnoResumenDTO`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Resultados agregados se pueden proyectar | Verificar el observable del paso 4.5.8 y contrastarlo con PRACTICA.md |
| `M4-P-45-S09` Añadir los métodos al servicio | `M4-4.5-T4` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | Controlador no accede al repositorio directamente | Verificar el observable del paso 4.5.9 y contrastarlo con PRACTICA.md |
| `M4-P-45-S10` Añadir los endpoints | `M4-4.5-T4` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` :: `AlumnoController` | curl http://localhost:8080/api/v1/alumnos | Consultas quedan accesibles por HTTP | Verificar el observable del paso 4.5.10 y contrastarlo con PRACTICA.md |
| `M4-P-45-S11` Probar las consultas | `M4-4.5-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw spring-boot:run | Hibernate ejecuta SQL coherente con JPQL | Verificar el observable del paso 4.5.11 y contrastarlo con PRACTICA.md |
| `M4-P-45-S12` Errores comunes del ejercicio | `M4-4.5-T5` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | Errores de nombres de entidad/campo se detectan | Verificar el observable del paso 4.5.12 y contrastarlo con PRACTICA.md |
| `M4-P-45-S13` Reto resuelto — Consulta con subconsulta | `M4-4.5-T5` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `AlumnoRepository` | ./mvnw test | El reto se contrasta sin comprometer el arranque | Verificar el observable del paso 4.5.13 y contrastarlo con PRACTICA.md |
| `M4-P-46-S01` Repasar las anotaciones actuales | `M4-4.6-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `@Transactional`, `AlumnoService` | ./mvnw test | Operaciones de escritura quedan en una transacción | Verificar el observable del paso 4.6.1 y contrastarlo con PRACTICA.md |
| `M4-P-46-S02` Provocar un rollback | `M4-4.6-T1` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | Una excepción revierte cambios | Verificar el observable del paso 4.6.2 y contrastarlo con PRACTICA.md |
| `M4-P-46-S03` Probar el rollback | `M4-4.6-T1` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | No quedan cambios parciales | Verificar el observable del paso 4.6.3 y contrastarlo con PRACTICA.md |
| `M4-P-46-S04` Quitar el rollback temporal | `M4-4.6-T2` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | Proyecto final no conserva fallo artificial | Verificar el observable del paso 4.6.4 y contrastarlo con PRACTICA.md |
| `M4-P-46-S05` Añadir @Transactional a un nuevo método con REQUIRES_NEW | `M4-4.6-T2` | CREATE:PERMANENT<br>CREATE:PERMANENT<br>CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` :: `REQUIRES_NEW`, `AuditoriaService`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` :: `Auditoria`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` :: `AuditoriaRepository` | ./mvnw test | Auditoría puede usar transacción independiente | Verificar el observable del paso 4.6.5 y contrastarlo con PRACTICA.md |
| `M4-P-46-S06` Usar el servicio de auditoría desde el servicio de alumnos | `M4-4.6-T2` | MODIFY:PERMANENT<br>MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService`<br>`M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` :: `AuditoriaService` | ./mvnw test | Límites transaccionales quedan explícitos | Verificar el observable del paso 4.6.6 y contrastarlo con PRACTICA.md |
| `M4-P-46-S07` Provocar un rollback y verificar que la auditoría persiste | `M4-4.6-T3` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | La auditoría puede persistir aunque otra transacción falle | Verificar el observable del paso 4.6.7 y contrastarlo con PRACTICA.md |
| `M4-P-46-S08` Probar el rollback con auditoría | `M4-4.6-T3` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` :: `AuditoriaRepository` | ./mvnw test | Se verifica el efecto de REQUIRES_NEW | Verificar el observable del paso 4.6.8 y contrastarlo con PRACTICA.md |
| `M4-P-46-S09` Quitar el rollback temporal | `M4-4.6-T4` | USE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | No quedan excepciones artificiales en estado final | Verificar el observable del paso 4.6.9 y contrastarlo con PRACTICA.md |
| `M4-P-46-S10` Añadir bloqueo optimista | `M4-4.6-T4` | CREATE:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` :: `@Version`, `Alumno` | ./mvnw test | Actualizaciones concurrentes disponen de versión | Verificar el observable del paso 4.6.10 y contrastarlo con PRACTICA.md |
| `M4-P-46-S11` Probar el bloqueo optimista | `M4-4.6-T4` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` :: `PESSIMISTIC_WRITE`, `AlumnoRepository` | ./mvnw test | Estrategias de concurrencia están modeladas | Verificar el observable del paso 4.6.11 y contrastarlo con PRACTICA.md |
| `M4-P-46-S12` Errores comunes del ejercicio | `M4-4.6-T5` | VERIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | Se distinguen rollback, propagación y bloqueo | Verificar el observable del paso 4.6.12 y contrastarlo con PRACTICA.md |
| `M4-P-46-S13` Reto resuelto — Transacción con timeout | `M4-4.6-T5` | MODIFY:PERMANENT | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` :: `AlumnoService` | ./mvnw test | El reto documenta límite temporal sin degradar baseline | Verificar el observable del paso 4.6.13 y contrastarlo con PRACTICA.md |
| `M4-P-47-S01` Verificar la dependencia de test | `M4-4.7-T1` | VERIFY:PERMANENT | `M4/proyecto/pom.xml` :: `spring-boot-starter-test`, `spring-boot-starter-data-jpa`, `3.5.16`, `17` | ./mvnw dependency:tree | Dependencias de test están disponibles | Verificar el observable del paso 4.7.1 y contrastarlo con PRACTICA.md |
| `M4-P-47-S02` Crear la estructura de paquetes de test | `M4-4.7-T1` | MODIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | Maven descubre las clases de test | Verificar el observable del paso 4.7.2 y contrastarlo con PRACTICA.md |
| `M4-P-47-S03` Crear el test básico de AlumnoRepository | `M4-4.7-T1` | MODIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `@DataJpaTest`, `TestEntityManager`, `AlumnoRepositoryTest` | ./mvnw test | Persistencia real en H2 se verifica | Verificar el observable del paso 4.7.3 y contrastarlo con PRACTICA.md |
| `M4-P-47-S04` Probar el método findByCurso | `M4-4.7-T2` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | findByCurso devuelve el resultado esperado | Verificar el observable del paso 4.7.4 y contrastarlo con PRACTICA.md |
| `M4-P-47-S05` Probar el caso vacío | `M4-4.7-T2` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | Consulta inexistente devuelve vacío | Verificar el observable del paso 4.7.5 y contrastarlo con PRACTICA.md |
| `M4-P-47-S06` Probar el método existsByDni | `M4-4.7-T2` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `findByDni`, `AlumnoRepositoryTest` | ./mvnw test | Existencia y búsqueda por DNI se validan | Verificar el observable del paso 4.7.6 y contrastarlo con PRACTICA.md |
| `M4-P-47-S07` Probar el constraint unique del DNI | `M4-4.7-T3` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | Duplicado provoca DataIntegrityViolationException | Verificar el observable del paso 4.7.7 y contrastarlo con PRACTICA.md |
| `M4-P-47-S08` Probar una consulta con @Query | `M4-4.7-T3` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `repositorio`, `AlumnoRepositoryTest` | ./mvnw test | Consulta JPQL se ejecuta contra H2 | Verificar el observable del paso 4.7.8 y contrastarlo con PRACTICA.md |
| `M4-P-47-S09` Probar la relación entre Alumno y Curso | `M4-4.7-T4` | VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` :: `CursoRepositoryTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | FK y relación funcionan en test de integración | Verificar el observable del paso 4.7.9 y contrastarlo con PRACTICA.md |
| `M4-P-47-S10` Crear el test de CursoRepository | `M4-4.7-T4` | CREATE:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` :: `findByNombreIgnoreCase`, `CursoRepositoryTest` | ./mvnw test | Repositorio Curso se prueba de forma aislada | Verificar el observable del paso 4.7.10 y contrastarlo con PRACTICA.md |
| `M4-P-47-S11` Ejecutar los tests | `M4-4.7-T4` | VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT<br>VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` :: `AlumnoControllerTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` :: `AlumnoServiceTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` :: `FechasUtilTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` :: `ExpedienteControllerTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` :: `ExpedienteServiceTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` :: `SaludoControllerTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` :: `MiProyectoApplicationTest`<br>`M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` :: `CursoRepositoryTest` | ./mvnw clean verify | Tests y Checkstyle terminan sin fallos | Verificar el observable del paso 4.7.11 y contrastarlo con PRACTICA.md |
| `M4-P-47-S12` Errores comunes del ejercicio | `M4-4.7-T5` | VERIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `AlumnoRepositoryTest` | ./mvnw test | Errores comunes de contexto y datos quedan identificados | Verificar el observable del paso 4.7.12 y contrastarlo con PRACTICA.md |
| `M4-P-47-S13` Reto resuelto — Test para Specification | `M4-4.7-T5` | MODIFY:PERMANENT | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` :: `Specification.allOf`, `AlumnoRepositoryTest` | ./mvnw test | Specification combinada devuelve una fila esperada | Verificar el observable del paso 4.7.13 y contrastarlo con PRACTICA.md |

## Guía -> proyecto

Cada artefacto funcional queda clasificado. `GUIDE` significa que M4 lo crea o evoluciona pedagógicamente; `INHERITED`, que llega de M3 y se conserva/adapta; `SUPPORT`, que es necesario para ejecutar/verificar aunque no sea el objeto principal del paso.

| Artefacto M4 | Clase | Origen | Evolución | Símbolos |
|---|---|---|---|---|
| `M4/proyecto/.gitignore` | INHERITED | `M3-CUMULATIVE` | - | - |
| `M4/proyecto/.mvn/wrapper/maven-wrapper.properties` | INHERITED | `M3-CUMULATIVE` | - | - |
| `M4/proyecto/README.md` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | - |
| `M4/proyecto/config/checkstyle/checkstyle.xml` | INHERITED | `M3-CUMULATIVE` | - | - |
| `M4/proyecto/mvnw` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | - |
| `M4/proyecto/mvnw.cmd` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | - |
| `M4/proyecto/pom.xml` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S01`, `M4-P-47-S01` | `spring-boot-starter-data-jpa`, `3.5.16`, `17` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | INHERITED | `M3-CUMULATIVE` | - | `MiProyectoApplication` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | GUIDE | `M4-P-41-S04` | `M4-P-41-S05`, `M4-P-42-S01`, `M4-P-42-S02`, `M4-P-42-S03`, `M4-P-42-S04`, `M4-P-42-S05`, `M4-P-42-S06`, `M4-P-42-S07`, `M4-P-42-S09`, `M4-P-42-S10`, `M4-P-42-S11`, `M4-P-42-S12`, `M4-P-42-S13`, `M4-P-44-S02`, `M4-P-44-S03`, `M4-P-44-S10`, `M4-P-44-S12`, `M4-P-46-S10` | `Alumno` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S08`, `M4-P-41-S10`, `M4-P-45-S10` | `AlumnoController` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | INHERITED | `M3-CUMULATIVE` | - | `AlumnoDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S06`, `M4-P-41-S13`, `M4-P-43-S01`, `M4-P-43-S02`, `M4-P-43-S04`, `M4-P-43-S05`, `M4-P-43-S07`, `M4-P-43-S08`, `M4-P-43-S12`, `M4-P-43-S13`, `M4-P-43-S14`, `M4-P-44-S11`, `M4-P-45-S01`, `M4-P-45-S02`, `M4-P-45-S03`, `M4-P-45-S05`, `M4-P-45-S06`, `M4-P-45-S07`, `M4-P-45-S08`, `M4-P-45-S11`, `M4-P-45-S12`, `M4-P-45-S13`, `M4-P-46-S11` | `AlumnoRepository` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | `AlumnoRequestDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-42-S08` | `AlumnoResponseDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | GUIDE | `M4-P-45-S08` | - | `AlumnoResumenDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S07`, `M4-P-42-S08`, `M4-P-43-S06`, `M4-P-43-S07`, `M4-P-43-S11`, `M4-P-44-S07`, `M4-P-44-S09`, `M4-P-45-S09`, `M4-P-46-S01`, `M4-P-46-S02`, `M4-P-46-S03`, `M4-P-46-S04`, `M4-P-46-S06`, `M4-P-46-S07`, `M4-P-46-S09`, `M4-P-46-S12`, `M4-P-46-S13` | `AlumnoService` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | GUIDE | `M4-P-43-S09` | `M4-P-43-S10`, `M4-P-45-S04` | `AlumnoSpecifications` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | GUIDE | `M4-P-42-S04` | - | `Direccion` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | GUIDE | `M4-P-42-S08` | - | `DireccionDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | `DocumentoRepository` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | GUIDE | `M4-P-42-S02` | - | `EstadoAlumno` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | GUIDE | `M4-P-46-S05` | - | `Auditoria` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | GUIDE | `M4-P-46-S05` | `M4-P-46-S08` | `AuditoriaRepository` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | GUIDE | `M4-P-46-S05` | `M4-P-46-S06` | `AuditoriaService` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` | INHERITED | `M3-CUMULATIVE` | - | `GlobalExceptionHandler` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | INHERITED | `M3-CUMULATIVE` | - | `NegocioException` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | INHERITED | `M3-CUMULATIVE` | - | `FechasUtil` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S09` | `DatosInicialesConfig` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | INHERITED | `M3-CUMULATIVE` | - | `JacksonConfig` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | GUIDE | `M4-P-44-S01` | `M4-P-44-S03`, `M4-P-44-S10`, `M4-P-44-S12`, `M4-P-44-S13` | `Curso` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | GUIDE | `M4-P-44-S08` | - | `CursoController` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | GUIDE | `M4-P-44-S05` | `M4-P-45-S08` | `CursoDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | GUIDE | `M4-P-44-S04` | - | `CursoRepository` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | GUIDE | `M4-P-44-S06` | `M4-P-44-S13` | `CursoService` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | INHERITED | `M3-CUMULATIVE` | - | `ExpedienteController` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | INHERITED | `M3-CUMULATIVE` | - | `ExpedienteDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | INHERITED | `M3-CUMULATIVE` | - | `ExpedienteRepository` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | INHERITED | `M3-CUMULATIVE` | - | `ExpedienteRequestDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | INHERITED | `M3-CUMULATIVE` | - | `ExpedienteService` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | INHERITED | `M3-CUMULATIVE` | - | `SolicitanteDTO` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | INHERITED | `M3-CUMULATIVE` | - | `InfoAplicacionController` |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | INHERITED | `M3-CUMULATIVE` | - | `SaludoController` |
| `M4/proyecto/src/main/resources/application-dev.properties` | GUIDE | `M3-CUMULATIVE` | `M4-P-41-S02`, `M4-P-41-S03`, `M4-P-41-S11`, `M4-P-41-S12` | `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` |
| `M4/proyecto/src/main/resources/application-prod.properties` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` |
| `M4/proyecto/src/main/resources/application.properties` | INHERITED | `M3-CUMULATIVE` | `M4-R1-CONTINUITY` | `spring.profiles.active` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | GUIDE | `M4-P-47-S11` | - | `MiProyectoApplicationTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `AlumnoControllerTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | GUIDE | `M3-CUMULATIVE` | `M4-P-43-S03`, `M4-P-47-S02`, `M4-P-47-S03`, `M4-P-47-S04`, `M4-P-47-S05`, `M4-P-47-S06`, `M4-P-47-S07`, `M4-P-47-S08`, `M4-P-47-S09`, `M4-P-47-S11`, `M4-P-47-S12`, `M4-P-47-S13` | `AlumnoRepositoryTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `AlumnoServiceTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `FechasUtilTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | GUIDE | `M4-P-47-S09` | `M4-P-47-S10`, `M4-P-47-S11` | `CursoRepositoryTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `ExpedienteControllerTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `ExpedienteServiceTest` |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | INHERITED | `M3-CUMULATIVE` | `M4-P-47-S11`, `M4-R1-CONTINUITY` | `SaludoControllerTest` |

## Proyecto -> guía

Esta es la trazabilidad inversa. Desde cada fichero final se puede volver a los pasos que lo crean, modifican, usan o verifican.

| Artefacto final | Pasos/acciones que lo trazan |
|---|---|
| `M4/proyecto/.gitignore` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/.mvn/wrapper/maven-wrapper.properties` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/README.md` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/config/checkstyle/checkstyle.xml` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/mvnw` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/mvnw.cmd` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/pom.xml` | `M4-P-41-S01` (MODIFY), `M4-P-47-S01` (VERIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | `M4-P-41-S04` (CREATE), `M4-P-41-S05` (VERIFY), `M4-P-42-S01` (USE), `M4-P-42-S02` (CREATE), `M4-P-42-S03` (CREATE), `M4-P-42-S04` (CREATE), `M4-P-42-S05` (CREATE), `M4-P-42-S06` (CREATE), `M4-P-42-S07` (VERIFY), `M4-P-42-S09` (VERIFY), `M4-P-42-S10` (VERIFY), `M4-P-42-S11` (CREATE), `M4-P-42-S12` (VERIFY), `M4-P-42-S13` (CREATE), `M4-P-44-S02` (CREATE), `M4-P-44-S03` (VERIFY), `M4-P-44-S10` (VERIFY), `M4-P-44-S12` (VERIFY), `M4-P-46-S10` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | `M4-P-41-S08` (MODIFY), `M4-P-41-S10` (VERIFY), `M4-P-45-S10` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | `M4-P-41-S06` (MODIFY), `M4-P-41-S13` (MODIFY), `M4-P-43-S01` (USE), `M4-P-43-S02` (MODIFY), `M4-P-43-S04` (MODIFY), `M4-P-43-S05` (MODIFY), `M4-P-43-S07` (MODIFY), `M4-P-43-S08` (VERIFY), `M4-P-43-S12` (VERIFY), `M4-P-43-S13` (VERIFY), `M4-P-43-S14` (MODIFY), `M4-P-44-S11` (VERIFY), `M4-P-45-S01` (USE), `M4-P-45-S02` (USE), `M4-P-45-S03` (USE), `M4-P-45-S05` (USE), `M4-P-45-S06` (USE), `M4-P-45-S07` (USE), `M4-P-45-S08` (USE), `M4-P-45-S11` (VERIFY), `M4-P-45-S12` (VERIFY), `M4-P-45-S13` (MODIFY), `M4-P-46-S11` (VERIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | `M4-P-42-S08` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | `M4-P-45-S08` (USE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | `M4-P-41-S07` (MODIFY), `M4-P-42-S08` (MODIFY), `M4-P-43-S06` (MODIFY), `M4-P-43-S07` (MODIFY), `M4-P-43-S11` (MODIFY), `M4-P-44-S07` (MODIFY), `M4-P-44-S09` (MODIFY), `M4-P-45-S09` (MODIFY), `M4-P-46-S01` (USE), `M4-P-46-S02` (USE), `M4-P-46-S03` (VERIFY), `M4-P-46-S04` (USE), `M4-P-46-S06` (MODIFY), `M4-P-46-S07` (VERIFY), `M4-P-46-S09` (USE), `M4-P-46-S12` (VERIFY), `M4-P-46-S13` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | `M4-P-43-S09` (CREATE), `M4-P-43-S10` (CREATE), `M4-P-45-S04` (USE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | `M4-P-42-S04` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | `M4-P-42-S08` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | `M4-P-42-S02` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | `M4-P-46-S05` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | `M4-P-46-S05` (CREATE), `M4-P-46-S08` (VERIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | `M4-P-46-S05` (CREATE), `M4-P-46-S06` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | `M4-P-41-S09` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | `M4-P-44-S01` (CREATE), `M4-P-44-S03` (VERIFY), `M4-P-44-S10` (VERIFY), `M4-P-44-S12` (VERIFY), `M4-P-44-S13` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | `M4-P-44-S08` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | `M4-P-44-S05` (CREATE), `M4-P-45-S08` (USE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | `M4-P-44-S04` (CREATE) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | `M4-P-44-S06` (CREATE), `M4-P-44-S13` (MODIFY) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/resources/application-dev.properties` | `M4-P-41-S02` (USE), `M4-P-41-S03` (VERIFY), `M4-P-41-S11` (MODIFY), `M4-P-41-S11` (VERIFY), `M4-P-41-S11` (RESTORE), `M4-P-41-S12` (VERIFY) |
| `M4/proyecto/src/main/resources/application-prod.properties` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/main/resources/application.properties` | `M3-CUMULATIVE` (INHERITED) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | `M4-P-43-S03` (VERIFY), `M4-P-47-S02` (MODIFY), `M4-P-47-S03` (MODIFY), `M4-P-47-S04` (VERIFY), `M4-P-47-S05` (VERIFY), `M4-P-47-S06` (VERIFY), `M4-P-47-S07` (VERIFY), `M4-P-47-S08` (VERIFY), `M4-P-47-S09` (VERIFY), `M4-P-47-S11` (VERIFY), `M4-P-47-S12` (VERIFY), `M4-P-47-S13` (MODIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | `M4-P-47-S09` (VERIFY), `M4-P-47-S10` (CREATE), `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | `M4-P-47-S11` (VERIFY) |
| `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | `M4-P-47-S11` (VERIFY) |

## M3 -> M4: continuidad acumulativa

El criterio es exhaustivo sobre `M3/proyecto`: todo fichero debe reaparecer en `M4/proyecto`. Si cambia, queda como adaptación; los tests heredados que habían desaparecido en la primera publicación de M4 se restauran en R1.

| M3 | M4 | Estado | Pasos M4 | Justificación |
|---|---|---|---|---|
| `M3/proyecto/.gitignore` | `M4/proyecto/.gitignore` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/.mvn/wrapper/maven-wrapper.properties` | `M4/proyecto/.mvn/wrapper/maven-wrapper.properties` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/README.md` | `M4/proyecto/README.md` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/config/checkstyle/checkstyle.xml` | `M4/proyecto/config/checkstyle/checkstyle.xml` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/mvnw` | `M4/proyecto/mvnw` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/mvnw.cmd` | `M4/proyecto/mvnw.cmd` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/pom.xml` | `M4/proyecto/pom.xml` | **MODIFIED_ADAPTED** | `M4-P-41-S01`, `M4-P-47-S01` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | **MODIFIED_ADAPTED** | `M4-P-41-S08`, `M4-P-41-S10`, `M4-P-45-S10` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | **MODIFIED_ADAPTED** | `M4-P-41-S06`, `M4-P-41-S13`, `M4-P-43-S01`, `M4-P-43-S02`, `M4-P-43-S04`, `M4-P-43-S05`, `M4-P-43-S07`, `M4-P-43-S08`, `M4-P-43-S12`, `M4-P-43-S13`, `M4-P-43-S14`, `M4-P-44-S11`, `M4-P-45-S01`, `M4-P-45-S02`, `M4-P-45-S03`, `M4-P-45-S05`, `M4-P-45-S06`, `M4-P-45-S07`, `M4-P-45-S08`, `M4-P-45-S11`, `M4-P-45-S12`, `M4-P-45-S13`, `M4-P-46-S11` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | **MODIFIED_ADAPTED** | `M4-P-42-S08` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | **MODIFIED_ADAPTED** | `M4-P-41-S07`, `M4-P-42-S08`, `M4-P-43-S06`, `M4-P-43-S07`, `M4-P-43-S11`, `M4-P-44-S07`, `M4-P-44-S09`, `M4-P-45-S09`, `M4-P-46-S01`, `M4-P-46-S02`, `M4-P-46-S03`, `M4-P-46-S04`, `M4-P-46-S06`, `M4-P-46-S07`, `M4-P-46-S09`, `M4-P-46-S12`, `M4-P-46-S13` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | **MODIFIED_ADAPTED** | `M4-P-41-S09` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | **PRESERVED** | - | Artefacto heredado sin cambios desde M3. |
| `M3/proyecto/src/main/resources/application-dev.properties` | `M4/proyecto/src/main/resources/application-dev.properties` | **MODIFIED_ADAPTED** | `M4-P-41-S02`, `M4-P-41-S03`, `M4-P-41-S11`, `M4-P-41-S11`, `M4-P-41-S11`, `M4-P-41-S12` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/resources/application-prod.properties` | `M4/proyecto/src/main/resources/application-prod.properties` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/main/resources/application.properties` | `M4/proyecto/src/main/resources/application.properties` | **MODIFIED_ADAPTED** | - | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | **RESTORED_ADAPTED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura y adapta a JPA sin perder el comportamiento de regresión de M3. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | **MODIFIED_ADAPTED** | `M4-P-43-S03`, `M4-P-47-S02`, `M4-P-47-S03`, `M4-P-47-S04`, `M4-P-47-S05`, `M4-P-47-S06`, `M4-P-47-S07`, `M4-P-47-S08`, `M4-P-47-S09`, `M4-P-47-S11`, `M4-P-47-S12`, `M4-P-47-S13` | Artefacto heredado evolucionado para JPA/Hibernate o para el cierre técnico de M4. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | **RESTORED_ADAPTED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura y adapta a JPA sin perder el comportamiento de regresión de M3. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | **RESTORED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura sin cambiar su contrato M3. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | **RESTORED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura sin cambiar su contrato M3. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | **RESTORED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura sin cambiar su contrato M3. |
| `M3/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | **RESTORED** | `M4-P-47-S11` | Test heredado que faltaba en la primera publicación de M4; R1 lo restaura sin cambiar su contrato M3. |

## Artefactos necesarios no introducidos en este módulo


## Trazabilidad inversa por artefacto/símbolo

El validador comprueba que los símbolos declarados en artefactos permanentes existen realmente en los ficheros textuales finales y que ningún artefacto funcional queda fuera del inventario. Los símbolos son evidencia literal; la clasificación y los pasos aportan el contexto pedagógico.

- `M4/proyecto/pom.xml` -> `spring-boot-starter-data-jpa`, `3.5.16`, `17` -> `M4-P-41-S01`, `M4-P-47-S01`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` -> `MiProyectoApplication` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` -> `Alumno` -> `M4-P-41-S04`, `M4-P-41-S05`, `M4-P-42-S01`, `M4-P-42-S02`, `M4-P-42-S03`, `M4-P-42-S04`, `M4-P-42-S05`, `M4-P-42-S06`, `M4-P-42-S07`, `M4-P-42-S09`, `M4-P-42-S10`, `M4-P-42-S11`, `M4-P-42-S12`, `M4-P-42-S13`, `M4-P-44-S02`, `M4-P-44-S03`, `M4-P-44-S10`, `M4-P-44-S12`, `M4-P-46-S10`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` -> `AlumnoController` -> `M4-P-41-S08`, `M4-P-41-S10`, `M4-P-45-S10`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` -> `AlumnoDTO` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` -> `AlumnoRepository` -> `M4-P-41-S06`, `M4-P-41-S13`, `M4-P-43-S01`, `M4-P-43-S02`, `M4-P-43-S04`, `M4-P-43-S05`, `M4-P-43-S07`, `M4-P-43-S08`, `M4-P-43-S12`, `M4-P-43-S13`, `M4-P-43-S14`, `M4-P-44-S11`, `M4-P-45-S01`, `M4-P-45-S02`, `M4-P-45-S03`, `M4-P-45-S05`, `M4-P-45-S06`, `M4-P-45-S07`, `M4-P-45-S08`, `M4-P-45-S11`, `M4-P-45-S12`, `M4-P-45-S13`, `M4-P-46-S11`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` -> `AlumnoRequestDTO` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` -> `AlumnoResponseDTO` -> `M4-P-42-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` -> `AlumnoResumenDTO` -> `M4-P-45-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` -> `AlumnoService` -> `M4-P-41-S07`, `M4-P-42-S08`, `M4-P-43-S06`, `M4-P-43-S07`, `M4-P-43-S11`, `M4-P-44-S07`, `M4-P-44-S09`, `M4-P-45-S09`, `M4-P-46-S01`, `M4-P-46-S02`, `M4-P-46-S03`, `M4-P-46-S04`, `M4-P-46-S06`, `M4-P-46-S07`, `M4-P-46-S09`, `M4-P-46-S12`, `M4-P-46-S13`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` -> `AlumnoSpecifications` -> `M4-P-43-S09`, `M4-P-43-S10`, `M4-P-45-S04`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` -> `Direccion` -> `M4-P-42-S04`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` -> `DireccionDTO` -> `M4-P-42-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` -> `DocumentoRepository` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` -> `EstadoAlumno` -> `M4-P-42-S02`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` -> `Auditoria` -> `M4-P-46-S05`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` -> `AuditoriaRepository` -> `M4-P-46-S05`, `M4-P-46-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` -> `AuditoriaService` -> `M4-P-46-S05`, `M4-P-46-S06`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java` -> `GlobalExceptionHandler` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` -> `NegocioException` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` -> `FechasUtil` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` -> `DatosInicialesConfig` -> `M4-P-41-S09`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` -> `JacksonConfig` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` -> `Curso` -> `M4-P-44-S01`, `M4-P-44-S03`, `M4-P-44-S10`, `M4-P-44-S12`, `M4-P-44-S13`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` -> `CursoController` -> `M4-P-44-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` -> `CursoDTO` -> `M4-P-44-S05`, `M4-P-45-S08`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` -> `CursoRepository` -> `M4-P-44-S04`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` -> `CursoService` -> `M4-P-44-S06`, `M4-P-44-S13`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` -> `ExpedienteController` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` -> `ExpedienteDTO` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` -> `ExpedienteRepository` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` -> `ExpedienteRequestDTO` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` -> `ExpedienteService` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` -> `SolicitanteDTO` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` -> `InfoAplicacionController` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` -> `SaludoController` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/resources/application-dev.properties` -> `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` -> `M4-P-41-S02`, `M4-P-41-S03`, `M4-P-41-S11`, `M4-P-41-S11`, `M4-P-41-S11`, `M4-P-41-S12`
- `M4/proyecto/src/main/resources/application-prod.properties` -> `spring.datasource.url`, `spring.jpa.hibernate.ddl-auto` -> `M3-CUMULATIVE`
- `M4/proyecto/src/main/resources/application.properties` -> `spring.profiles.active` -> `M3-CUMULATIVE`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` -> `MiProyectoApplicationTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` -> `AlumnoControllerTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` -> `AlumnoRepositoryTest` -> `M4-P-43-S03`, `M4-P-47-S02`, `M4-P-47-S03`, `M4-P-47-S04`, `M4-P-47-S05`, `M4-P-47-S06`, `M4-P-47-S07`, `M4-P-47-S08`, `M4-P-47-S09`, `M4-P-47-S11`, `M4-P-47-S12`, `M4-P-47-S13`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` -> `AlumnoServiceTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` -> `FechasUtilTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` -> `CursoRepositoryTest` -> `M4-P-47-S09`, `M4-P-47-S10`, `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` -> `ExpedienteControllerTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` -> `ExpedienteServiceTest` -> `M4-P-47-S11`
- `M4/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` -> `SaludoControllerTest` -> `M4-P-47-S11`

## Estados temporales y restauraciones

- `M4-P-41-S11` cambia temporalmente H2 a fichero y `ddl-auto=update`, verifica persistencia entre reinicios y **restaura** `application-dev.properties` a `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` + `create-drop`.
- El fichero `data/testdb.mv.db` se considera temporal de laboratorio y debe eliminarse antes de avanzar.
- El validador falla si un paso marcado `TEMPORARY` no contiene una acción `RESTORE` o `DELETE`.

## Regresión heredada M3 -> M4

R1 restaura las pruebas de controlador, servicio, utilidades, expedientes y saludo que estaban en M3. Las pruebas de alumnos se adaptan a Spring Data JPA/`Page` y a las dependencias del servicio, pero conservan los contratos HTTP y de negocio heredados.

- `AlumnoControllerTest`: contrato REST heredado, adaptado a `Page`.
- `AlumnoServiceTest`: regresión de filtrado/duplicados/creación, adaptada con mocks JPA.
- `FechasUtilTest`, `ExpedienteControllerTest`, `ExpedienteServiceTest`, `SaludoControllerTest`: restaurados como regresión acumulativa.
- `AlumnoRepositoryTest`: evoluciona al repositorio JPA y `CursoRepositoryTest` añade la cobertura nueva de M4.

## Gates automáticos

- `validate_m4.py`: esquema v3, 35 conceptos, 92 pasos, acciones/estados, símbolos, inventario, continuidad M3->M4 y cierre temporal.
- `validate_m4_human.py`: sincronización de este informe con los manifiestos y presencia de todas las secciones/IDs/rutas.
- `validate_m4_mutation.py`: prueba negativa; mutila de forma controlada manifiestos en memoria y exige que el core detecte cada defecto.
- Maven Wrapper 3.9.16 + `clean verify` + Checkstyle + Javadoc + package + runtime/OpenAPI.
- CI Linux ejecuta `chmod +x M4/proyecto/mvnw` antes de usar el wrapper para evitar falsos fallos por permisos al publicar desde Windows.

## Resultado global

**M4-R1 sólo puede declararse CERRADO cuando todos los gates anteriores pasen en local y en GitHub Actions.** El informe humano no sustituye a los JSON ni a los validadores; es una vista auditable de la misma verdad.

