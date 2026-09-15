# M6 - Codespaces / Linux

## Verificación

```bash
cd M6/proyecto
chmod +x mvnw
./mvnw --version
./mvnw -B clean verify
./mvnw -B javadoc:javadoc
./mvnw -B -DskipTests package
```

El proyecto requiere Java 17 y el Maven Wrapper incluido. Para ejecución local utiliza el perfil `dev`. En producción configura `JWT_SECRET` mediante variable de entorno.
