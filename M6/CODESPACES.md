# M6 — Codespaces / Linux

## Validación completa

```bash
cd M6/proyecto
chmod +x mvnw
./mvnw --version
./mvnw -B clean verify
./mvnw -B javadoc:javadoc
./mvnw -B -DskipTests package
cd ../..
python .course/traceability/validate_m6.py .
python .course/traceability/validate_m6_human.py .
python .course/traceability/validate_m6_mutation.py .
python .course/traceability/validate_m6_runtime.py .
```

El proyecto final exige Maven Wrapper 3.9.16 y Java 17. En producción configure `JWT_SECRET`; el perfil dev incluye sólo una clave didáctica local.
