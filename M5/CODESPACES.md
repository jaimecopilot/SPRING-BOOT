# M5 - Ejecutar el proyecto en GitHub Codespaces

M5 parte del proyecto acumulativo de M4-R1. Abre el repositorio en un Codespace, entra en `M5/proyecto` y usa siempre el Maven Wrapper incluido.

```bash
cd M5/proyecto
./mvnw -version
./mvnw clean verify
./mvnw spring-boot:run
```

Con el perfil `dev` la API usa H2 en memoria y escucha en el puerto 8080. Para probar el contrato de error:

```bash
curl -i http://localhost:8080/api/v1/alumnos/99999
```

Para provocar validación:

```bash
curl -i -X POST http://localhost:8080/api/v1/alumnos \
  -H 'Content-Type: application/json' \
  -d '{"nombre":""}'
```

Para probar CORS desde el origen permitido:

```bash
curl -i http://localhost:8080/api/v1/alumnos \
  -H 'Origin: http://localhost:3000'
```

Y para simular el preflight:

```bash
curl -i -X OPTIONS http://localhost:8080/api/v1/alumnos \
  -H 'Origin: http://localhost:3000' \
  -H 'Access-Control-Request-Method: POST' \
  -H 'Access-Control-Request-Headers: content-type'
```

El reto del punto 5.5 incluye `M5/frontend-cors/index.html`. Desde `M5/frontend-cors` puedes levantarlo con:

```bash
python3 -m http.server 3000
```

No cambies permanentemente el perfil para probar `prod`: usa un argumento de arranque y vuelve después a `dev`.
