# M4 - Ejecutar el proyecto en GitHub Codespaces

Esta guía corresponde al **Módulo 4 real del curso: persistencia con JPA/Hibernate**. M4 no introduce Spring Security ni JWT. El proyecto acumulativo añade persistencia JPA, H2 para desarrollo/test, PostgreSQL para producción, relaciones `Alumno`-`Curso`, consultas JPQL/Specifications, transacciones y `@DataJpaTest`.

## 1. Crear o abrir un Codespace

Desde la página principal del repositorio, abre **Code -> Codespaces** y crea o reutiliza un Codespace sobre `main`.

Abre una terminal y sitúate en el proyecto:

```bash
cd M4/proyecto
```

## 2. Verificar Java y Maven Wrapper

```bash
java -version
./mvnw --version
```

El contrato de M4 usa **Java 17** y **Maven Wrapper 3.9.16**. Si GitHub hubiera perdido el bit ejecutable del wrapper, puedes recuperarlo localmente con:

```bash
chmod +x mvnw
```

No sustituyas el wrapper por un Maven global distinto.

## 3. Ejecutar la suite completa

```bash
./mvnw clean verify
```

La ejecución debe compilar el proyecto acumulativo, ejecutar los tests heredados de M3 y las pruebas nuevas de JPA de M4, y terminar sin violaciones de Checkstyle.

También puedes ejecutar únicamente los tests:

```bash
./mvnw test
```

## 4. Arrancar la aplicación

```bash
./mvnw spring-boot:run
```

El perfil de desarrollo usa H2. La aplicación debe arrancar en el puerto 8080 salvo que hayas cambiado explícitamente la configuración.

En otra terminal:

```bash
curl -i http://localhost:8080/hola
curl -i http://localhost:8080/adios
curl -i http://localhost:8080/api/v1/alumnos
curl -i http://localhost:8080/api/v1/cursos
```

Los endpoints de M3 siguen formando parte del proyecto acumulativo; M4 cambia la persistencia de alumnos a JPA/Hibernate y añade el modelo de cursos.

## 5. OpenAPI

Con la aplicación arrancada:

```bash
curl -i http://localhost:8080/v3/api-docs
```

Debe responder el documento OpenAPI del proyecto acumulativo.

## 6. H2 y persistencia temporal

El estado canónico de desarrollo de M4 vuelve a una base H2 en memoria y a recrear el esquema para mantener las prácticas reproducibles:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
```

En la práctica 4.1 se cambia **temporalmente** a H2 en fichero y `ddl-auto=update` para observar persistencia entre reinicios. Ese cambio no es el estado final del módulo: al terminar el paso hay que restaurar los dos valores anteriores y eliminar `data/testdb.mv.db` si se creó.

## 7. Pruebas JPA que debes reconocer

Los tests específicos de M4 incluyen `AlumnoRepositoryTest` y `CursoRepositoryTest`. Además, M4 conserva o adapta las regresiones heredadas de M3 para controlador de alumnos, servicio de alumnos, fechas, expedientes y saludos.

Puedes ejecutar una clase concreta, por ejemplo:

```bash
./mvnw -Dtest=AlumnoRepositoryTest test
```

## 8. Consultas y relaciones relevantes

En `AlumnoRepository` encontrarás métodos derivados, JPQL con `@Query`, actualizaciones con `@Modifying`, paginación y bloqueo. `AlumnoSpecifications` contiene los filtros dinámicos. La relación principal de M4 es `Curso` 1:N `Alumno` / `Alumno` N:1 `Curso`.

Para inspeccionar SQL generado, usa los logs del perfil de desarrollo mientras ejecutas las operaciones descritas en `PRACTICA.md`.

## 9. Validaciones internas de trazabilidad

Desde la raíz del repositorio, no desde `M4/proyecto`:

```bash
cd ../..
python .course/traceability/validate_m4.py .
python .course/traceability/validate_m4_human.py .
python .course/traceability/validate_m4_mutation.py .
```

Los tres deben pasar. El último es una prueba negativa: introduce corrupciones controladas en memoria y verifica que el validador fuerte las rechaza.

## 10. Qué debe quedar funcionando al terminar

- Java 17 y Maven Wrapper 3.9.16.
- Tests heredados de M3 más tests JPA de M4.
- `Alumno` persistido con JPA/Hibernate.
- `Curso` y relación bidireccional correctamente mapeados.
- Consultas derivadas, JPQL y Specifications.
- Transacciones, auditoría y control de concurrencia explicados en la práctica.
- H2 de desarrollo restaurado al estado canónico.
- Endpoints acumulativos y `/v3/api-docs` operativos.
- Trazabilidad M3 -> M4 completa y reversible.
