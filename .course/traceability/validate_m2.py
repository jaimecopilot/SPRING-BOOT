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

# 1) Invariantes estructurales comunes.
generic = root / ".course/traceability/validate_module.py"
cp = subprocess.run([sys.executable, str(generic), "M2", str(root)], text=True, capture_output=True)
if cp.returncode:
    bad("generic module validation failed:\n" + (cp.stdout + cp.stderr).strip())

# La vista humana debe ser una proyección exacta de los manifests, también
# mientras el módulo está IN_PROGRESS.
generator = root / ".course/traceability/generate_module_traceability.py"
cp = subprocess.run([sys.executable, str(generator), "M2", "--check"], text=True, capture_output=True)
if cp.returncode:
    bad("human traceability report is missing or out of date:\n" + (cp.stdout + cp.stderr).strip())

manifest = json.loads((root / ".course/traceability/M2.json").read_text(encoding="utf-8"))
theory = (root / "M2/TEORIA.md").read_text(encoding="utf-8")
practice = (root / "M2/PRACTICA.md").read_text(encoding="utf-8")

# 2) Checkpoint exacto de 2.1: 12/69, cinco conceptos y un solo manifiesto.
if manifest.get("status") != "IN_PROGRESS":
    bad("M2 2.1 checkpoint must be IN_PROGRESS")
if manifest.get("expected_total_steps_when_complete") != 69:
    bad("M2 expected final total must be 69")
if manifest.get("current_traced_steps") != 12:
    bad("M2 2.1 checkpoint must expose exactly 12 traced steps")
if manifest.get("step_manifests") != [".course/traceability/M2/2.1.json"]:
    bad("M2 2.1 checkpoint must declare only the 2.1 step manifest")
if len(manifest.get("theory_concepts", [])) != 5:
    bad("M2 2.1 checkpoint must expose exactly five theory concepts")

point_blocks = re.findall(r"^# Punto (2\.\d+) - ", theory, re.M)
if point_blocks != ["2.1"]:
    bad(f"published theory points at this checkpoint must be exactly ['2.1']; got {point_blocks}")
practice_blocks = re.findall(r"^# (?:Punto|Práctica) (2\.\d+) - ", practice, re.M)
if practice_blocks != ["2.1"]:
    bad(f"published practical blocks at this checkpoint must be exactly ['2.1']; got {practice_blocks}")
if re.search(r"^# (?:Punto|Práctica) 2\.[2-9]", theory + "\n" + practice, re.M):
    bad("future M2 content leaked beyond 2.1")

for name, text in [("TEORIA", theory), ("PRACTICA", practice)]:
    for token in ["filecite", ".course/tmp/", "turn456file", "turn457file"]:
        if token in text:
            bad(f"{name}: internal/source marker leaked into student material: {token}")

# 3) Herencia: M2 nace de M1 y en 2.1 sólo debe evolucionar AlumnoController.
def files_under(base):
    return {
        p.relative_to(base).as_posix(): p
        for p in base.rglob("*") if p.is_file()
    }

m1 = root / "M1/proyecto"
m2 = root / "M2/proyecto"
allowed_changed = {
    "src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java"
}
if not m1.exists() or not m2.exists():
    bad("M1/M2 project snapshot missing")
else:
    f1, f2 = files_under(m1), files_under(m2)
    if set(f1) != set(f2):
        bad(f"M2 2.1 project file set must equal M1; missing={sorted(set(f1)-set(f2))}, extra={sorted(set(f2)-set(f1))}")
    for rel in sorted(set(f1) & set(f2)):
        same = filecmp.cmp(f1[rel], f2[rel], shallow=False)
        if rel in allowed_changed:
            if same:
                bad(f"expected 2.1 evolution did not change {rel}")
        elif not same:
            bad(f"M1 artifact changed unexpectedly in M2 2.1: {rel}")

# 4) Contrato final del controlador tras 2.1.
controller = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java"
if not controller.exists():
    bad("M2 AlumnoController missing")
else:
    text = controller.read_text(encoding="utf-8")
    tokens = [
        "@RequestMapping(\"/api/v1/alumnos\")",
        "@RequestParam(required = false) String curso",
        "@RequestParam(required = false) String sort",
        "@RequestParam(defaultValue = \"0\") int page",
        "@RequestParam(defaultValue = \"20\") int size",
        ".skip((long) page * size)",
        ".limit(size)",
        "import java.net.URI",
        "private AlumnoDTO guardarAlumno",
        "int siguienteId = alumnos.stream()",
        ".mapToInt(Integer::parseInt)",
        ".max()",
        "ResponseEntity.created(location).body(creado)",
        "import org.springframework.web.bind.annotation.RequestHeader",
        "@GetMapping(\"/info-peticion\")",
        "Accept-Language",
        "@PostMapping(\"/promocionar\")",
        "(promocionado)",
        "private Optional<AlumnoDTO> buscarPorId",
        "@PutMapping(\"/{id}\")",
        "@PatchMapping(\"/{id}\")",
        "@DeleteMapping(\"/{id}\")",
        "@GetMapping(\"/{id}/con-enlaces\")",
        "new LinkedHashMap<>()",
        "respuesta.put(\"_links\"",
        "\"documentos\", \"/api/v1/alumnos/\" + id + \"/documentos\"",
        "\"curso\", \"/api/v1/cursos/\" + alumno.getCurso()"
    ]
    for token in tokens:
        if token not in text:
            bad(f"AlumnoController missing 2.1 contract token: {token}")
    if "alumnos.size() + 1" in text:
        bad("2.1 must not reintroduce the source's unsafe size()+1 ID generation")

# 5) 2.1 no debe adelantar todavía service/repository ni otras capas.
for forbidden_path in [
    "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service",
    "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/repository",
    "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/entity",
]:
    if (root / forbidden_path).exists():
        bad(f"future-layer package leaked into M2 2.1: {forbidden_path}")
for p in (root / "M2/proyecto/src/main/java").rglob("*.java"):
    text = p.read_text(encoding="utf-8")
    for forbidden in ["@Service", "@Repository", "@Entity", "JpaRepository"]:
        if forbidden in text:
            bad(f"future M2 layer leaked into 2.1: {p.relative_to(root)} -> {forbidden}")

# 6) Contratos pedagógicos de alto valor: ejemplos de la fuente no pueden desaparecer.
theory_tokens = [
    "@Controller", "@RestController", "@ResponseBody", "@RequestMapping",
    "@GetMapping", "@PostMapping", "@PutMapping", "@PatchMapping", "@DeleteMapping",
    "@PathVariable", "@RequestParam", "@RequestBody", "@RequestHeader",
    "ResponseEntity", "Location", "204 No Content", "Serialización automática",
    "controlador gordo", "AlumnoRepository", "inyección por constructor"
]
for token in theory_tokens:
    if token.lower() not in theory.lower():
        bad(f"M2 2.1 theory missing source-value token: {token}")

practice_tokens = [
    "## Paso 1 - Repasar el estado actual",
    "## Paso 12 - Reto resuelto: endpoint que devuelve un alumno con enlaces HATEOAS",
    "info-peticion", "User-Agent", "Accept-Language", "page", "size", "sort",
    "promocionar", "Location", "buscarPorId", "guardarAlumno", "400 Bad Request",
    "415 Unsupported Media Type", "_links", "IntelliJ IDEA", "Eclipse", "VS Code",
    "./mvnw test", "./mvnw package"
]
for token in practice_tokens:
    if token not in practice:
        bad(f"M2 2.1 practice missing high-value token: {token}")

# 7) La auditoría fuente fija la estructura global antes de redactar.
audit = (root / ".course/source-audit/M2.md").read_text(encoding="utf-8")
for token in [
    "**69**", "2.1", "2.6", "M2-P-21-S01", "M2-P-21-S12",
    "M2-T-21-ROLE", "M2-T-21-RESPONSE", "M2-T-21-PRACTICES",
    "2.6.11", "2.6.12", "2.7", "@MockitoBean", "springdoc"
]:
    if token not in audit:
        bad(f"M2 source audit lost required contract token: {token}")

if fail:
    print("M2 2.1 CONTRACT: FAIL")
    for item in fail:
        print(" -", item)
    raise SystemExit(1)

print("M2 2.1 CONTRACT: PASS | status=IN_PROGRESS | theory=5 | steps=12/69 | human-report=SYNC | changed-functional-files=1")
