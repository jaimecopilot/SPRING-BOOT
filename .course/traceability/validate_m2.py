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

# 1) Invariantes estructurales comunes y sincronía de la vista humana.
generic = root / ".course/traceability/validate_module.py"
cp = subprocess.run([sys.executable, str(generic), "M2", str(root)], text=True, capture_output=True)
if cp.returncode:
    bad("generic module validation failed:\n" + (cp.stdout + cp.stderr).strip())

generator = root / ".course/traceability/generate_module_traceability.py"
cp = subprocess.run([sys.executable, str(generator), "M2", "--check"], text=True, capture_output=True)
if cp.returncode:
    bad("human traceability report is missing or out of date:\n" + (cp.stdout + cp.stderr).strip())

manifest = json.loads((root / ".course/traceability/M2.json").read_text(encoding="utf-8"))
theory = (root / "M2/TEORIA.md").read_text(encoding="utf-8")
practice = (root / "M2/PRACTICA.md").read_text(encoding="utf-8")

# 2) Checkpoint exacto 2.2.
if manifest.get("status") != "IN_PROGRESS":
    bad("M2 2.2 checkpoint must be IN_PROGRESS")
if manifest.get("expected_total_steps_when_complete") != 69:
    bad("M2 expected final total must be 69")
if manifest.get("current_traced_steps") != 23:
    bad("M2 2.2 checkpoint must expose exactly 23 traced steps")
expected_manifests = [
    ".course/traceability/M2/2.1.json",
    ".course/traceability/M2/2.2.json",
]
if manifest.get("step_manifests") != expected_manifests:
    bad(f"M2 2.2 manifests must be {expected_manifests}")
if len(manifest.get("theory_concepts", [])) != 10:
    bad("M2 2.2 checkpoint must expose exactly ten theory concepts")

point_blocks = re.findall(r"^# Punto (2\.\d+) - ", theory, re.M)
if point_blocks != ["2.1", "2.2"]:
    bad(f"published theory points must be exactly 2.1-2.2; got {point_blocks}")
practice_blocks = re.findall(r"^# (?:Punto|Práctica) (2\.\d+) - ", practice, re.M)
if practice_blocks != ["2.1", "2.2"]:
    bad(f"published practical points must be exactly 2.1-2.2; got {practice_blocks}")
if re.search(r"^# (?:Punto|Práctica) 2\.[3-9]", theory + "\n" + practice, re.M):
    bad("future M2 content leaked beyond 2.2")
for name, text in [("TEORIA", theory), ("PRACTICA", practice)]:
    for token in ["filecite", ".course/tmp/", "turn456file", "turn457file", "turn534file"]:
        if token in text:
            bad(f"{name}: internal/source marker leaked into student material: {token}")

# 3) Herencia exacta desde M1: dos controladores evolucionan y aparecen tres ficheros.
def files_under(base):
    return {p.relative_to(base).as_posix(): p for p in base.rglob("*") if p.is_file()}

m1 = root / "M1/proyecto"
m2 = root / "M2/proyecto"
expected_extra = {
    "src/main/java/es/mecd/demo/miproyecto/service/AlumnoService.java",
    "src/main/java/es/mecd/demo/miproyecto/service/ExpedienteService.java",
    "src/main/java/es/mecd/demo/miproyecto/exception/NegocioException.java",
}
expected_changed = {
    "src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java",
    "src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java",
}
if not m1.exists() or not m2.exists():
    bad("M1/M2 project snapshot missing")
else:
    f1, f2 = files_under(m1), files_under(m2)
    missing = set(f1) - set(f2)
    extra = set(f2) - set(f1)
    if missing:
        bad(f"M2 lost inherited M1 files: {sorted(missing)}")
    if extra != expected_extra:
        bad(f"M2 2.2 extra functional files must be exactly {sorted(expected_extra)}; got {sorted(extra)}")
    for rel in sorted(set(f1) & set(f2)):
        same = filecmp.cmp(f1[rel], f2[rel], shallow=False)
        if rel in expected_changed:
            if same:
                bad(f"expected M2 evolution did not change {rel}")
        elif not same:
            bad(f"inherited M1 artifact changed unexpectedly in M2 2.2: {rel}")

# 4) Contratos de separación controller/service.
controller_path = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java"
service_path = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service/AlumnoService.java"
exp_controller_path = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java"
exp_service_path = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/service/ExpedienteService.java"
exception_path = root / "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/exception/NegocioException.java"

contracts = {
    controller_path: [
        "private final AlumnoService service", "public AlumnoController(AlumnoService service)",
        "return service.listar(curso, sort, page, size)", "AlumnoDTO creado = service.crear(dto)",
        "ResponseEntity.created(location).body(creado)", "service.consultar(id)",
        "service.actualizar(id, dto)", "service.actualizarParcial(id, cambios)", "service.eliminar(id)",
        "service.promocionar()", "@GetMapping(\"/info-peticion\")", "@GetMapping(\"/{id}/con-enlaces\")",
        "@ExceptionHandler(NegocioException.class)", "HttpStatus.CONFLICT", "\"status\", 409",
    ],
    service_path: [
        "@Service", "public class AlumnoService", "private final List<AlumnoDTO> alumnos",
        "public List<AlumnoDTO> listar", ".skip((long) page * size)", ".limit(size)",
        "public Optional<AlumnoDTO> consultar", "public AlumnoDTO crear", "throw new NegocioException",
        "Ya existe un alumno con el DNI", "int siguienteId = alumnos.stream()", ".max()",
        "public Optional<AlumnoDTO> actualizar", "public Optional<AlumnoDTO> actualizarParcial",
        "public boolean eliminar", "public void promocionar", "private boolean existePorDni",
    ],
    exp_controller_path: [
        "private final ExpedienteService service", "public ExpedienteController(ExpedienteService service)",
        "@GetMapping(\"/ejemplo\")", "return service.ejemplo()", "@PostMapping(\"/eco\")", "return service.eco(dto)",
        "@GetMapping(\"/{id}\")", "@PutMapping(\"/{id}\")", "@DeleteMapping(\"/{id}\")",
        "ResponseEntity.created(location).body(creado)",
    ],
    exp_service_path: [
        "@Service", "public class ExpedienteService", "private final List<ExpedienteDTO> expedientes",
        "public List<ExpedienteDTO> listar", "public Optional<ExpedienteDTO> consultar", "public ExpedienteDTO crear",
        "public Optional<ExpedienteDTO> actualizar", "public boolean eliminar", "public ExpedienteDTO ejemplo",
        "setNumeroSeguridadSocial", "setSolicitante(new SolicitanteDTO", "public ExpedienteDTO eco",
    ],
    exception_path: [
        "public class NegocioException extends RuntimeException", "public NegocioException(String mensaje)",
        "public NegocioException(String mensaje, Throwable causa)",
    ],
}
for path, tokens in contracts.items():
    if not path.exists():
        bad(f"missing 2.2 contract file: {path.relative_to(root)}")
        continue
    text = path.read_text(encoding="utf-8")
    for token in tokens:
        if token not in text:
            bad(f"{path.relative_to(root)} missing 2.2 contract token: {token}")

if controller_path.exists():
    c = controller_path.read_text(encoding="utf-8")
    for forbidden in [
        "new ArrayList<>(List.of(", "private final List<AlumnoDTO> alumnos", "int siguienteId = alumnos.stream()",
        "private AlumnoDTO guardarAlumno", "private Optional<AlumnoDTO> buscarPorId", ".skip((long) page * size)",
    ]:
        if forbidden in c:
            bad(f"AlumnoController still owns service responsibility: {forbidden}")

if service_path.exists():
    s = service_path.read_text(encoding="utf-8")
    for forbidden in ["ResponseEntity", "HttpStatus", "@RequestParam", "@PathVariable", "@RequestHeader", "@ExceptionHandler"]:
        if forbidden in s:
            bad(f"AlumnoService leaked HTTP concern: {forbidden}")
    if "alumnos.size() + 1" in s:
        bad("AlumnoService reintroduced unsafe source size()+1 ID generation")

if exp_service_path.exists():
    s = exp_service_path.read_text(encoding="utf-8")
    for forbidden in ["ResponseEntity", "HttpStatus", "@RequestParam", "@PathVariable", "@ExceptionHandler"]:
        if forbidden in s:
            bad(f"ExpedienteService leaked HTTP concern: {forbidden}")
    if "expedientes.size() + 1" in s:
        bad("ExpedienteService must use cumulative max numeric ID strategy")

# 5) No adelantar todavía repository/JPA.
for forbidden_path in [
    "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/repository",
    "M2/proyecto/src/main/java/es/mecd/demo/miproyecto/entity",
]:
    if (root / forbidden_path).exists():
        bad(f"future layer leaked into M2 2.2: {forbidden_path}")
for p in (root / "M2/proyecto/src/main/java").rglob("*.java"):
    text = p.read_text(encoding="utf-8")
    for forbidden in ["@Repository", "@Entity", "JpaRepository"]:
        if forbidden in text:
            bad(f"future M2/JPA behavior leaked into {p.relative_to(root)}: {forbidden}")

# 6) Contratos didácticos acumulativos de alto valor.
theory_tokens = [
    "@Controller", "@RestController", "@ResponseBody", "@RequestMapping", "@RequestHeader",
    "ResponseEntity", "Location", "204 No Content", "controlador gordo",
    "Qué es un servicio y cuál es su responsabilidad", "@Service", "@Component", "Bean y ciclo de vida",
    "Inyección de dependencias por constructor", "private final AlumnoService service", "@Autowired",
    "Transformación DTO ↔ Entidad", "toDTO", "toEntity", "Excepciones de negocio", "NegocioException",
    "409 Conflict", "400 Bad Request", "500 Internal Server Error", "@ExceptionHandler"
]
for token in theory_tokens:
    if token.lower() not in theory.lower():
        bad(f"M2 theory missing source-value token: {token}")

practice_tokens = [
    "# Punto 2.1 - Capas de la aplicación: Controlador",
    "# Punto 2.2 - Capas de la aplicación: Servicio",
    "## Paso 11 - Reto resuelto: servicio para el recurso Expediente",
    "NegocioException", "AlumnoService", "ExpedienteService", "DNI-DEMO-01", "409",
    "ResponseEntity", "@ExceptionHandler", "./mvnw test", "./mvnw -DskipTests package",
    "IntelliJ IDEA", "Eclipse", "VS Code", "/api/v1/expedientes/ejemplo", "/api/v1/expedientes/eco"
]
for token in practice_tokens:
    if token not in practice:
        bad(f"M2 practice missing high-value token: {token}")

# 7) Auditoría fuente global sigue fijando el alcance.
audit = (root / ".course/source-audit/M2.md").read_text(encoding="utf-8")
for token in [
    "**69**", "2.1", "2.2", "2.6", "M2-P-21-S12", "M2-P-22-S11",
    "M2-T-22-ROLE", "M2-T-22-SERVICE", "M2-T-22-CONSTRUCTOR-DI", "M2-T-22-DTO-ENTITY",
    "M2-T-22-BUSINESS-EXCEPTIONS", "2.6.11", "2.6.12", "2.7", "@MockitoBean", "springdoc"
]:
    if token not in audit:
        bad(f"M2 source audit lost required contract token: {token}")

# 8) Ensambladores temporales de 2.2 no pueden sobrevivir.
for rel in [
    ".course/tmp/M2_22_TEORIA_APPEND.md",
    ".course/tmp/M2_22_PRACTICA_APPEND.md",
    ".github/workflows/assemble-m2-22.yml",
    ".github/workflows/debug-m2-runtime.yml",
]:
    if (root / rel).exists():
        bad(f"temporary M2 artifact remains: {rel}")

if fail:
    print("M2 2.2 CONTRACT: FAIL")
    for item in fail:
        print(" -", item)
    raise SystemExit(1)

print("M2 2.2 CONTRACT: PASS | status=IN_PROGRESS | theory=10 | steps=23/69 | human-report=SYNC | changed-functional-files=5")
