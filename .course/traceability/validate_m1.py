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

# Este gate describe el primer checkpoint industrial. Se ampliará acumulativamente
# al cerrar 1.2, 1.3, 1.4 y 1.5; nunca se rebajan comprobaciones ya cerradas.
if manifest.get("status") != "IN_PROGRESS":
    bad("M1 must remain IN_PROGRESS until all 58 steps are complete")
if manifest.get("expected_total_steps_when_complete") != 58:
    bad("M1 expected final total must remain 58")
if manifest.get("current_traced_steps") != 10:
    bad("M1.1 checkpoint must currently expose exactly 10 traced steps")
if manifest.get("step_manifests") != [".course/traceability/M1/1.1.json"]:
    bad("M1.1 checkpoint must currently contain only the 1.1 step manifest")

expected_headings = [
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
actual_headings = re.findall(r"^## Paso \d+ - .+$", practice, re.M)
if actual_headings != expected_headings:
    bad(f"M1.1 practical headings changed: {actual_headings}")
if "# Práctica 1.2" in practice or "# Punto 1.2" in theory:
    bad("M1.2 content must not enter the M1.1 checkpoint")

# M1.1 is deliberately observational: after all temporary experiments the
# executable snapshot must be byte-for-byte identical to approved M0.
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
        bad(f"M1.1 snapshot file set differs from M0: missing={sorted(set(f0)-set(f1))}, extra={sorted(set(f1)-set(f0))}")
    for rel in sorted(set(f0) & set(f1)):
        if not filecmp.cmp(f0[rel], f1[rel], shallow=False):
            bad(f"M1.1 final snapshot must still equal approved M0: content differs at {rel}")

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
        bad(f"missing M1.1 contract file: {rel}")
        continue
    txt = p.read_text(encoding="utf-8")
    for token in tokens:
        if token not in txt:
            bad(f"{rel}: missing contract token {token}")

# No laboratorio temporal puede filtrarse al snapshot final.
temporary_forbidden = {
    "M1/proyecto/src/main/resources/application.properties": ["server.port=9090"],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java": ["JacksonAutoConfiguration.class"],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java": ["/info-json", "infoJson", "LinkedHashMap", "java.util.Map"],
}
for rel, tokens in temporary_forbidden.items():
    txt = (root / rel).read_text(encoding="utf-8")
    for token in tokens:
        if token in txt:
            bad(f"temporary M1.1 residue leaked into final snapshot: {rel} -> {token}")

# El alumno debe poder ejecutar realmente todo lo que el contrato 1.1 promete.
for token in [
    "--debug", "CONDITIONS EVALUATION REPORT", "Positive matches", "Negative matches", "Exclusions",
    "./mvnw dependency:tree", "java -jar target/mi-proyecto-0.0.1-SNAPSHOT.jar",
    "server.port=9090", "curl -i http://localhost:9090/hola", "curl -i http://localhost:8080/hola",
    "JacksonAutoConfiguration.class", "@GetMapping(\"/info-json\")", "./mvnw test",
    "IntelliJ IDEA", "Eclipse", "VS Code"
]:
    if token not in practice:
        bad(f"M1.1 practical guide missing high-value token: {token}")

for token in [
    "Spring Framework", "Spring Boot", "Auto-configuración", "Positive matches", "Negative matches",
    "spring-boot-starter-web", "dependencias transitivas", "servidor embebido", "back off"
]:
    if token.lower() not in theory.lower():
        bad(f"M1.1 theory missing high-value concept token: {token}")

# La matriz editorial previa debe seguir fijando la escala total y el checkpoint actual.
audit = (root / ".course/source-audit/M1.md").read_text(encoding="utf-8")
for token in ["**58**", "M1-P-11-S10", "M1-T-11-AUTOCONFIG", "server.port=9090", "excluyendo Jackson"]:
    if token not in audit:
        bad(f"M1 source audit lost required contract token: {token}")

if fail:
    print("M1 CHECKPOINT 1.1: FAIL")
    for x in fail:
        print(" -", x)
    raise SystemExit(1)

print("M1 CHECKPOINT 1.1: PASS | 5 theory concepts | 10/58 steps | snapshot=M0 restored")
