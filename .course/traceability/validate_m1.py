#!/usr/bin/env python3
from pathlib import Path
import filecmp
import json
import re
import subprocess
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else ".").resolve()
fail = []

def bad(msg):
    fail.append(msg)

# Primero deben cumplirse todos los invariantes reutilizables.
generic = root / ".course/traceability/validate_module.py"
cp = subprocess.run([sys.executable, str(generic), "M1", str(root)], text=True, capture_output=True)
if cp.returncode:
    bad("generic module validation failed:\n" + (cp.stdout + cp.stderr).strip())

manifest = json.loads((root / ".course/traceability/M1.json").read_text(encoding="utf-8"))
theory = (root / "M1/TEORIA.md").read_text(encoding="utf-8")
practice = (root / "M1/PRACTICA.md").read_text(encoding="utf-8")

# Checkpoint acumulativo 1.1 + 1.2. Al avanzar se amplía, nunca se rebajan
# comprobaciones de puntos ya cerrados.
if manifest.get("status") != "IN_PROGRESS":
    bad("M1 must remain IN_PROGRESS until all 58 steps are complete")
if manifest.get("expected_total_steps_when_complete") != 58:
    bad("M1 expected final total must remain 58")
if manifest.get("current_traced_steps") != 22:
    bad("M1.2 checkpoint must expose exactly 22 traced steps")
expected_manifests = [
    ".course/traceability/M1/1.1.json",
    ".course/traceability/M1/1.2.json",
]
if manifest.get("step_manifests") != expected_manifests:
    bad(f"M1.2 checkpoint manifests must be {expected_manifests}")
if len(manifest.get("theory_concepts", [])) != 10:
    bad("M1.2 checkpoint must expose exactly 10 theory concepts")

expected_11 = [
    "## Paso 1 - Abrir el proyecto y arrancarlo",
    "## Paso 2 - Ver el informe de auto-configuración",
    "## Paso 3 - Leer el informe con calma",
    "## Paso 4 - Demostrar que Tomcat está embebido",
    "## Paso 5 - Cambiar una configuración por defecto y restaurarla",
    "## Paso 6 - Observar los starters y las dependencias transitivas",
    "## Paso 7 - Probar `/hola` y reconstruir toda la cadena",
    "## Paso 8 - Explicar el papel de Spring Boot usando evidencias del proyecto",
    "## Paso 9 - Diagnosticar errores frecuentes sin cambiar varias cosas a la vez",
    "## Paso 10 - Reto resuelto: excluir temporalmente Jackson y demostrar el efecto",
]
expected_12 = [
    "## Paso 1 - Arrancar la aplicación y formular hipótesis",
    "## Paso 2 - Probar GET con el navegador",
    "## Paso 3 - Probar una ruta que no existe",
    "## Paso 4 - Probar GET con curl y ver las cabeceras",
    "## Paso 5 - Probar una ruta inexistente con curl",
    "## Paso 6 - Probar un método HTTP no soportado",
    "## Paso 7 - Ver las cabeceras que envía el cliente con curl",
    "## Paso 8 - Observar la misma petición con DevTools",
    "## Paso 9 - Razonar sobre lo observado",
    "## Paso 10 - Diagnosticar errores comunes de comunicación HTTP",
    "## Paso 11 - Resumir lo observado con pruebas reproducibles",
    "## Paso 12 - Reto resuelto: provocar un 400 Bad Request y restaurar el proyecto",
]
blocks = re.findall(r"(?ms)^# Práctica (1\.\d+) - .*?(?=^# Práctica |\Z)", practice)
if blocks != ["1.1", "1.2"]:
    bad(f"published practical blocks must be exactly 1.1 and 1.2; got {blocks}")
all_headings = re.findall(r"^## Paso \d+ - .+$", practice, re.M)
if all_headings != expected_11 + expected_12:
    bad(f"M1.2 practical headings changed or out of order: {all_headings}")
if "# Práctica 1.3" in practice or "# Punto 1.3" in theory:
    bad("M1.3 content must not enter before its traceability checkpoint exists")

# 1.1 y 1.2 son deliberadamente observacionales. Tras cerrar todos los
# experimentos temporales el snapshot ejecutable debe seguir idéntico a M0.
def files_under(base):
    return {
        p.relative_to(base).as_posix(): p
        for p in base.rglob("*") if p.is_file()
    }

m0 = root / "M0/proyecto"
m1 = root / "M1/proyecto"
if not m0.exists() or not m1.exists():
    bad("M0/M1 project snapshot missing")
else:
    f0, f1 = files_under(m0), files_under(m1)
    if set(f0) != set(f1):
        bad(f"M1.2 final snapshot file set differs from M0: missing={sorted(set(f0)-set(f1))}, extra={sorted(set(f1)-set(f0))}")
    for rel in sorted(set(f0) & set(f1)):
        if not filecmp.cmp(f0[rel], f1[rel], shallow=False):
            bad(f"M1.2 final snapshot must still equal approved M0: content differs at {rel}")

contracts = {
    "M1/proyecto/pom.xml": [
        "spring-boot-starter-parent", "3.5.16", "<java.version>17</java.version>",
        "spring-boot-starter-web", "spring-boot-devtools", "spring-boot-starter-test",
        "spring-boot-maven-plugin"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java": [
        "@SpringBootApplication", "SpringApplication.run(MiProyectoApplication.class, args)"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java": [
        "@RestController", "@GetMapping(\"/hola\")", "@GetMapping(\"/adios\")",
        "construirMensaje(\"Ministerio de Educación\")"
    ],
    "M1/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java": [
        "saludarDebeDevolverElMensajeEsperado", "despedirDebeDevolverElMensajeEsperado"
    ],
    "M1/proyecto/.mvn/wrapper/maven-wrapper.properties": ["apache-maven-3.9.16-bin.zip"],
}
for rel, tokens in contracts.items():
    p = root / rel
    if not p.exists():
        bad(f"missing M1.2 contract file: {rel}")
        continue
    txt = p.read_text(encoding="utf-8")
    for token in tokens:
        if token not in txt:
            bad(f"{rel}: missing contract token {token}")

# Ningún laboratorio temporal de 1.1/1.2 puede filtrarse al snapshot final.
temporary_forbidden = {
    "M1/proyecto/src/main/resources/application.properties": ["server.port=9090"],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java": ["JacksonAutoConfiguration.class"],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java": [
        "/info-json", "infoJson", "LinkedHashMap", "java.util.Map",
        "/eco", "@PostMapping", "@RequestBody"
    ],
}
for rel, tokens in temporary_forbidden.items():
    txt = (root / rel).read_text(encoding="utf-8")
    for token in tokens:
        if token in txt:
            bad(f"temporary M1.1/M1.2 residue leaked into final snapshot: {rel} -> {token}")

# Contratos didácticos de 1.1 que siguen siendo obligatorios.
for token in [
    "--debug", "CONDITIONS EVALUATION REPORT", "Positive matches", "Negative matches", "Exclusions",
    "./mvnw dependency:tree", "java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar",
    "server.port=9090", "curl -i http://localhost:9090/hola",
    "JacksonAutoConfiguration.class", "@GetMapping(\"/info-json\")", "./mvnw test",
    "IntelliJ IDEA", "Eclipse", "VS Code"
]:
    if token not in practice:
        bad(f"M1.1 practical guide lost high-value token: {token}")

# Contratos didácticos de 1.2.
for token in [
    "curl -i http://localhost:8080/no-existe",
    "curl -i -X POST http://localhost:8080/hola",
    "curl -v http://localhost:8080/hola",
    "F12", "Network", "404", "405 Method Not Allowed",
    "Connection refused", "400 Bad Request", "415 Unsupported Media Type",
    "@PostMapping(\"/eco\")", "@RequestBody", "Map<String, Object>",
    "Content-Type: application/json", "Content-Type: text/plain",
    "{\"mensaje\":\"hola\",}", "{\"mensaje\":\"hola\"}"
]:
    if token not in practice:
        bad(f"M1.2 practical guide missing high-value token: {token}")

for token in [
    "Spring Framework", "Spring Boot", "Auto-configuración", "Positive matches", "Negative matches",
    "spring-boot-starter-web", "dependencias transitivas", "servidor embebido",
    "Cliente y servidor", "petición-respuesta", "Front-end y back-end",
    "405 Method Not Allowed", "415 Unsupported Media Type", "curl -v", "DevTools"
]:
    if token.lower() not in theory.lower():
        bad(f"M1.1/M1.2 theory missing high-value concept token: {token}")

# La matriz editorial previa debe seguir fijando escala, decisiones y temporales.
audit = (root / ".course/source-audit/M1.md").read_text(encoding="utf-8")
for token in [
    "**58**", "M1-P-11-S10", "M1-P-12-S12", "M1-T-11-AUTOCONFIG",
    "M1-T-12-CLIENT-SERVER", "server.port=9090", "excluyendo Jackson", "POST /eco"
]:
    if token not in audit:
        bad(f"M1 source audit lost required contract token: {token}")

if fail:
    print("M1 CHECKPOINT 1.2: FAIL")
    for x in fail:
        print(" -", x)
    raise SystemExit(1)

print("M1 CHECKPOINT 1.2: PASS | 10 theory concepts | 22/58 steps | snapshot=M0 restored")
