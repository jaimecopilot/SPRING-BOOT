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

# 1) Invariantes estructurales reutilizables.
generic = root / ".course/traceability/validate_module.py"
cp = subprocess.run([sys.executable, str(generic), "M1", str(root)], text=True, capture_output=True)
if cp.returncode:
    bad("generic module validation failed:\n" + (cp.stdout + cp.stderr).strip())

manifest = json.loads((root / ".course/traceability/M1.json").read_text(encoding="utf-8"))
theory = (root / "M1/TEORIA.md").read_text(encoding="utf-8")
practice = (root / "M1/PRACTICA.md").read_text(encoding="utf-8")

# 2) El módulo completo debe ser exactamente 58/58, no un checkpoint parcial.
if manifest.get("status") != "COMPLETE":
    bad("final M1 manifest must be COMPLETE")
if manifest.get("expected_total_steps_when_complete") != 58:
    bad("M1 expected final total must be 58")
if manifest.get("current_traced_steps") != 58:
    bad("final M1 must expose exactly 58 traced steps")
expected_manifests = [
    ".course/traceability/M1/1.1.json",
    ".course/traceability/M1/1.2.json",
    ".course/traceability/M1/1.3.json",
    ".course/traceability/M1/1.4.json",
    ".course/traceability/M1/1.5.json",
]
if manifest.get("step_manifests") != expected_manifests:
    bad(f"final M1 manifests must be {expected_manifests}")
if len(manifest.get("theory_concepts", [])) != 25:
    bad("final M1 must expose exactly 25 theory concepts")

blocks = re.findall(r"(?ms)^# Práctica (1\.\d+) - .*?(?=^# Práctica |\Z)", practice)
if blocks != ["1.1", "1.2", "1.3", "1.4", "1.5"]:
    bad(f"published practical blocks must be exactly 1.1-1.5; got {blocks}")
point_blocks = re.findall(r"^# Punto (1\.\d+) - ", theory, re.M)
if point_blocks != ["1.1", "1.2", "1.3", "1.4", "1.5"]:
    bad(f"published theory points must be exactly 1.1-1.5; got {point_blocks}")
if re.search(r"^# (?:Punto|Práctica) 2\.", theory + "\n" + practice, re.M):
    bad("M2 content leaked into M1")

# Los marcadores de auditoría/conversación nunca pertenecen al material del alumno.
for name, text in [("TEORIA", theory), ("PRACTICA", practice)]:
    for token in ["filecite", "turn291file", ".course/tmp/", "assemble-m1-"]:
        if token in text:
            bad(f"{name}: internal/source marker leaked into student material: {token}")

# 3) Herencia: todos los ficheros procedentes de M0 siguen byte a byte iguales.
# M1 añade exactamente cinco ficheros funcionales nuevos; 1.5 modifica sólo uno de ellos.
def files_under(base):
    return {
        p.relative_to(base).as_posix(): p
        for p in base.rglob("*") if p.is_file()
    }

m0 = root / "M0/proyecto"
m1 = root / "M1/proyecto"
expected_new = {
    "src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java",
    "src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java",
    "src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java",
    "src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java",
    "src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java",
}
if not m0.exists() or not m1.exists():
    bad("M0/M1 project snapshot missing")
else:
    f0, f1 = files_under(m0), files_under(m1)
    missing = set(f0) - set(f1)
    extra = set(f1) - set(f0)
    if missing:
        bad(f"M1 lost inherited M0 files: {sorted(missing)}")
    if extra != expected_new:
        bad(f"final M1 new functional file set must be exactly {sorted(expected_new)}; got {sorted(extra)}")
    for rel in sorted(set(f0) & set(f1)):
        if not filecmp.cmp(f0[rel], f1[rel], shallow=False):
            bad(f"inherited M0 artifact changed unexpectedly in M1: {rel}")

# 4) Contrato de código final.
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
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/ExpedienteDTO.java": [
        "@JsonInclude(JsonInclude.Include.NON_NULL)", "@JsonProperty(\"id\")",
        "@JsonFormat(pattern = \"yyyy-MM-dd\")", "@JsonIgnore",
        "private String numeroSeguridadSocial", "private SolicitanteDTO solicitante",
        "public ExpedienteDTO()", "getSolicitante", "setSolicitante"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/SolicitanteDTO.java": [
        "public class SolicitanteDTO", "@JsonProperty(\"dni\")",
        "private String documentoIdentidad", "public SolicitanteDTO()"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/ExpedienteController.java": [
        "@RequestMapping(\"/api/v1/expedientes\")", "@GetMapping(\"/ejemplo\")",
        "@PostMapping(\"/eco\")", "@RequestBody ExpedienteDTO dto",
        "setNumeroSeguridadSocial", "setSolicitante(new SolicitanteDTO"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/dto/AlumnoDTO.java": [
        "public class AlumnoDTO", "@JsonInclude(JsonInclude.Include.NON_NULL)",
        "@JsonProperty(\"id\")", "@JsonFormat(pattern = \"yyyy-MM-dd\")",
        "private String identificador", "private String curso", "private List<String> documentos",
        "public AlumnoDTO()", "getIdentificador", "setIdentificador", "getCurso", "setCurso"
    ],
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/AlumnoController.java": [
        "import java.util.ArrayList", "import java.util.Map",
        "private final List<AlumnoDTO> alumnos = new ArrayList<>(List.of(",
        "@RequestParam(required = false) String curso", "@RequestParam(required = false) String sort",
        "int siguienteId = alumnos.stream()", ".mapToInt(Integer::parseInt)", ".max()",
        "dto.setIdentificador(String.valueOf(siguienteId))", "alumnos.add(dto)",
        "@PutMapping(\"/{id}\")", "dto.setIdentificador(id)", "alumnos.set(i, dto)",
        "@PatchMapping(\"/{id}\")", "@RequestBody Map<String, Object> cambios",
        "cambios.containsKey(\"nombre\")", "cambios.containsKey(\"apellidos\")",
        "cambios.containsKey(\"dni\")", "cambios.containsKey(\"curso\")",
        "@DeleteMapping(\"/{id}\")", "alumnos.removeIf", "ResponseEntity.noContent().build()",
        "\"nombre\".equalsIgnoreCase(sort)", "getNombre().compareToIgnoreCase",
        "\"apellidos\".equalsIgnoreCase(sort)", "getApellidos().compareToIgnoreCase"
    ],
    "M1/proyecto/.mvn/wrapper/maven-wrapper.properties": ["apache-maven-3.9.16-bin.zip"],
}
for rel, tokens in contracts.items():
    p = root / rel
    if not p.exists():
        bad(f"missing final M1 contract file: {rel}")
        continue
    txt = p.read_text(encoding="utf-8")
    for token in tokens:
        if token not in txt:
            bad(f"{rel}: missing contract token {token}")

# No puede quedar ningún laboratorio temporal de 1.1/1.2.
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

# M1 no debe adelantar todavía capas de M2 ni persistencia real.
for forbidden_path in [
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/service",
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/repository",
    "M1/proyecto/src/main/java/es/mecd/demo/miproyecto/entity",
]:
    if (root / forbidden_path).exists():
        bad(f"future-layer package leaked into M1: {forbidden_path}")
for p in (root / "M1/proyecto/src/main/java").rglob("*.java"):
    text=p.read_text(encoding="utf-8")
    for forbidden in ["@Service", "@Repository", "@Entity", "JpaRepository"]:
        if forbidden in text:
            bad(f"future M2+ behavior leaked into {p.relative_to(root)}: {forbidden}")

# 5) Contratos didácticos acumulativos de alto valor.
practice_tokens = [
    "--debug", "CONDITIONS EVALUATION REPORT", "./mvnw dependency:tree",
    "server.port=9090", "JacksonAutoConfiguration.class",
    "curl -i http://localhost:8080/no-existe", "400 Bad Request", "415 Unsupported Media Type",
    "@PostMapping(\"/eco\")", "Map<String, Object>",
    "@JsonProperty(\"id\")", "@JsonFormat(pattern = \"yyyy-MM-dd\")",
    "@JsonInclude(JsonInclude.Include.NON_NULL)", "@JsonIgnore", "SolicitanteDTO",
    "@RequestMapping(\"/api/v1/alumnos\")", "201 Created", "@GetMapping(\"/{id}\")",
    "@RequestParam(required = false) String curso",
    "new ArrayList<>(List.of(", "alumnos.add(dto)", "@PutMapping(\"/{id}\")",
    "@PatchMapping(\"/{id}\")", "@DeleteMapping(\"/{id}\")",
    "@RequestParam(required = false) String sort", "removeIf", "204 No Content",
    "POST http://localhost:8080/api/v1/alumnos/1", "405 Method Not Allowed",
    "./mvnw test", "./mvnw -DskipTests package", "IntelliJ IDEA", "Eclipse", "VS Code"
]
for token in practice_tokens:
    if token not in practice:
        bad(f"M1 practical guide lost high-value token: {token}")

theory_tokens = [
    "Spring Framework", "Spring Boot", "Auto-configuración", "servidor embebido",
    "Cliente y servidor", "petición-respuesta", "Front-end y back-end",
    "405 Method Not Allowed", "415 Unsupported Media Type", "curl -v", "DevTools",
    "Qué es JSON", "Sintaxis de JSON", "Convenciones de JSON en APIs REST",
    "Serialización", "Deserialización", "@JsonProperty", "@JsonFormat", "@JsonInclude", "@JsonIgnore",
    "FAIL_ON_UNKNOWN_PROPERTIES", "Qué es REST", "Diseño de URLs",
    "Métodos HTTP y códigos de estado", "Paginación, filtrado y ordenación",
    "Documentación y evolución", "El ciclo CRUD", "PUT en detalle",
    "PATCH y DELETE en detalle", "Códigos de estado en el CRUD", "Consolidación del Módulo 1",
    "idempotente", "201 Created", "204 No Content", "Map<String, Object>"
]
for token in theory_tokens:
    if token.lower() not in theory.lower():
        bad(f"M1 theory missing high-value concept token: {token}")

# 6) La matriz editorial original debe seguir demostrando que el cierre estaba previsto desde el inicio.
audit = (root / ".course/source-audit/M1.md").read_text(encoding="utf-8")
for token in [
    "**58**", "M1-P-11-S10", "M1-P-12-S12", "M1-P-13-S12", "M1-P-14-S12", "M1-P-15-S12",
    "M1-T-11-AUTOCONFIG", "M1-T-12-CLIENT-SERVER", "M1-T-13-JACKSON", "M1-T-14-REST", "M1-T-15-CRUD",
    "ExpedienteDTO", "SolicitanteDTO", "AlumnoDTO", "AlumnoController", "ArrayList", "@PutMapping", "@PatchMapping", "@DeleteMapping"
]:
    if token not in audit:
        bad(f"M1 source audit lost required contract token: {token}")

# 7) No deben sobrevivir ensambladores temporales.
if (root / ".course/tmp").exists() and any((root / ".course/tmp").rglob("*")):
    bad("temporary editorial assembly files remain under .course/tmp")
for p in (root / ".github/workflows").glob("assemble-m1-*.yml"):
    bad(f"temporary assembly workflow remains: {p.relative_to(root)}")
if (root / ".github/workflows/finalize-m1-manifest.yml").exists():
    bad("temporary final manifest migration workflow remains")

if fail:
    print("M1 FINAL CONTRACT: FAIL")
    for x in fail:
        print(" -", x)
    raise SystemExit(1)

print("M1 FINAL CONTRACT: PASS | status=COMPLETE | theory=25 | steps=58 | new-functional-files=5")
