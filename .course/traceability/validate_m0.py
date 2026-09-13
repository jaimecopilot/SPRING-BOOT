#!/usr/bin/env python3
from pathlib import Path
import json, re, subprocess, sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else ".").resolve()
manifest_path = root / ".course/traceability/M0.json"
m = json.loads(manifest_path.read_text(encoding="utf-8"))
steps=[]
for rel in m.get("step_manifests",[]):
    steps.extend(json.loads((root/rel).read_text(encoding="utf-8"))["steps"])
theory = (root / "M0/TEORIA.md").read_text(encoding="utf-8")
practice = (root / "M0/PRACTICA.md").read_text(encoding="utf-8")
fail=[]

def bad(msg): fail.append(msg)

if m.get("schema_version") != 3: bad("schema_version must be 3")
if not m.get("policy",{}).get("step_level_traceability_required"): bad("step-level traceability policy missing")

concepts = m.get("theory_concepts",[])
theory_ids=[c["id"] for c in concepts]
if len(theory_ids) != len(set(theory_ids)): bad("duplicate theory concept id")
for c in concepts:
    if c["anchor"].lower() not in theory.lower():
        bad(f"theory anchor missing: {c['id']} -> {c['anchor']}")

inventory_list=m.get("artifacts",[])
inventory={a["path"]:a for a in inventory_list}
if len(inventory) != len(inventory_list): bad("duplicate artifact inventory path")
inventory_paths=set(inventory)

derived=[]
headings={}
for pblock in re.finditer(r'(?ms)^# Práctica (0\.\d+) - .*?(?=^# Práctica |^# Empaquetado y verificación final del Módulo 0|\Z)', practice):
    p=pblock.group(1)
    for mm in re.finditer(r'^## Paso (\d+) - (.+)$', pblock.group(0), re.M):
        n=int(mm.group(1))
        sid=f"M0-P-{int(p.split('.')[1]):02d}-S{n:02d}"
        derived.append(sid)
        headings[sid]=f"## Paso {n} - {mm.group(2)}"
for mm in re.finditer(r'^## Paso final ([ABC]) - (.+)$', practice, re.M):
    sid=f"M0-P-FINAL-{mm.group(1)}"
    derived.append(sid)
    headings[sid]=f"## Paso final {mm.group(1)} - {mm.group(2)}"

declared=[s["id"] for s in steps]
if len(derived) != 51: bad(f"guide must expose 51 traceable steps; got {len(derived)}")
if len(declared) != 51: bad(f"manifest must declare 51 steps; got {len(declared)}")
if set(derived) != set(declared):
    bad(f"guide/manifest step id mismatch: missing={sorted(set(derived)-set(declared))}, extra={sorted(set(declared)-set(derived))}")
if len(declared) != len(set(declared)): bad("duplicate step id")

allowed_actions={"CREATE","MODIFY","USE","DELETE","RESTORE","VERIFY"}
allowed_states={"PERMANENT","TEMPORARY"}
env_ids={w["id"] for w in m.get("environment_workflows",[])}
textual_suffixes={".java",".xml",".properties",".cmd",".md",".yml",".yaml",".json",".py"}
skip_literal_symbols={"Maven Wrapper","Maven Wrapper Windows","target/"}

for s in steps:
    sid=s["id"]
    if sid in headings and s.get("heading") != headings[sid]:
        bad(f"{sid}: heading mismatch: manifest={s.get('heading')!r}, guide={headings[sid]!r}")
    refs=s.get("theory_refs",[])
    if not refs: bad(f"{sid}: no theory_refs")
    for ref in refs:
        if ref not in theory_ids: bad(f"{sid}: unknown theory ref {ref}")
    for ref in s.get("environment_refs",[]):
        if ref not in env_ids: bad(f"{sid}: unknown environment ref {ref}")
    if s.get("state") not in allowed_states: bad(f"{sid}: invalid state {s.get('state')}")
    if not (s.get("artifacts") or s.get("commands") or s.get("observables")):
        bad(f"{sid}: no trace evidence")
    if not s.get("observables"): bad(f"{sid}: no observables")
    if not s.get("verification"): bad(f"{sid}: no verification")
    for a in s.get("artifacts",[]):
        action=a.get("action")
        state=a.get("state")
        rel=a.get("path")
        if action not in allowed_actions: bad(f"{sid}: invalid action {action}")
        if state not in allowed_states: bad(f"{sid}: invalid artifact state {state}")
        if not rel: bad(f"{sid}: artifact without path")
        if not rel: continue
        p=root/rel
        if state=="PERMANENT" and action!="DELETE" and not p.exists():
            bad(f"{sid}: permanent artifact missing: {rel}")
        if state=="PERMANENT" and action!="DELETE" and rel not in inventory_paths:
            bad(f"{sid}: permanent artifact absent from reverse inventory: {rel}")
        if action in {"CREATE","MODIFY","RESTORE","VERIFY"} and not a.get("symbols"):
            bad(f"{sid}: {action} artifact without symbols: {rel}")
        if state=="PERMANENT" and action in {"CREATE","MODIFY","RESTORE","VERIFY"} and p.is_file() and p.suffix in textual_suffixes:
            txt=p.read_text(encoding="utf-8")
            for token in a.get("symbols",[]):
                if token in skip_literal_symbols: continue
                if token not in txt:
                    bad(f"{sid}: declared permanent symbol/token missing in {rel}: {token}")
    if s.get("state")=="TEMPORARY":
        has_close=any(a.get("action") in {"RESTORE","DELETE"} for a in s.get("artifacts",[]))
        if not has_close:
            bad(f"{sid}: TEMPORARY step without RESTORE/DELETE transition")

for w in m.get("environment_workflows",[]):
    if w["anchor"] not in practice: bad(f"environment workflow missing: {w['id']}")
if env_ids != {"M0-W-CONSOLE","M0-W-INTELLIJ","M0-W-ECLIPSE","M0-W-VSCODE"}:
    bad("exact four environment workflows required")

for rel,a in inventory.items():
    p=root/rel
    if not p.exists(): bad(f"classified artifact missing: {rel}")
    if a["classification"] not in {"GUIDE","INHERITED","SUPPORT"}:
        bad(f"invalid classification: {rel}")
    if a["classification"]=="SUPPORT":
        if not a.get("used_by"): bad(f"SUPPORT without used_by: {rel}")
        if not a.get("missing_effect"): bad(f"SUPPORT without missing_effect: {rel}")
    if a["classification"]=="GUIDE":
        origin=a.get("origin")
        if origin and origin.startswith("M0-P-") and origin not in declared:
            bad(f"GUIDE origin not real: {rel} -> {origin}")

functional=[]
for p in (root/"M0").rglob("*"):
    if p.is_file() and (p.suffix in {".java",".xml",".properties",".cmd"} or p.name=="mvnw"):
        functional.append(p.relative_to(root).as_posix())
for rel in functional:
    if rel not in inventory: bad(f"unclassified functional artifact: {rel}")

for rel,a in inventory.items():
    if a["classification"]!="GUIDE": continue
    p=root/rel
    if not p.exists() or not p.is_file() or p.suffix not in {".java",".xml",".properties"}: continue
    txt=p.read_text(encoding="utf-8")
    for token in a.get("symbols",[]):
        if token in skip_literal_symbols: continue
        if token not in txt:
            bad(f"{rel}: final symbol/token missing: {token}")

step_relations={}
step_actions={}
for s in steps:
    for a in s.get("artifacts",[]):
        step_relations.setdefault(a["path"],set()).add(s["id"])
        step_actions.setdefault((a["path"],s["id"]),set()).add(a["action"])
for rel,a in inventory.items():
    if a["classification"]!="GUIDE": continue
    origin=a.get("origin")
    if origin and origin.startswith("M0-P-"):
        if origin not in step_relations.get(rel,set()):
            bad(f"{rel}: inventory origin {origin} has no step relation")
        elif "CREATE" not in step_actions.get((rel,origin),set()):
            bad(f"{rel}: pedagogical origin {origin} is not a CREATE action")
    for sid in a.get("evolution",[]):
        if sid and sid.startswith("M0-P-"):
            if sid not in step_relations.get(rel,set()):
                bad(f"{rel}: inventory evolution {sid} has no step relation")
            elif not (step_actions.get((rel,sid),set()) & {"MODIFY","RESTORE"}):
                bad(f"{rel}: inventory evolution {sid} does not modify/restore the artifact")

public_behavior_paths={
    "M0/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java",
    "M0/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java",
    "M0/proyecto/pom.xml",
    "M0/proyecto/src/main/resources/application.properties"
}
for rel in public_behavior_paths:
    if inventory.get(rel,{}).get("classification")!="GUIDE":
        bad(f"public/functional behavior cannot be SUPPORT: {rel}")

for name,text in [("TEORIA",theory),("PRACTICA",practice)]:
    q=len(re.findall(r'^### Pregunta(?: breve| de integración)?$', text, re.M))
    a=len(re.findall(r'^### Respuesta(?: razonada)?$', text, re.M))
    if q != a or q < 10: bad(f"{name}: questions/answers invalid {q}/{a}")
    for patt in [r'(?i)\bcheckpoint\b',r'(?i)fidelidad didáctica',r'(?i)GUIDE/INHERITED/SUPPORT',r'(?i)\.course/traceability']:
        if re.search(patt,text): bad(f"{name}: internal meta visible: {patt}")

contracts={
"M0/ejemplos/HolaMinisterio.java":["public class HolaMinisterio","args.length > 0","bienvenido al Ministerio de Educación"],
"M0/proyecto/pom.xml":["spring-boot-starter-parent","3.5.16","<java.version>17</java.version>","spring-boot-starter-web","spring-boot-devtools","spring-boot-starter-test","spring-boot-maven-plugin"],
"M0/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java":["@SpringBootApplication","SpringApplication.run(MiProyectoApplication.class, args)"],
"M0/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java":["@RestController","@GetMapping(\"/hola\")","@GetMapping(\"/adios\")","construirMensaje(\"Ministerio de Educación\")"],
"M0/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java":["saludarDebeDevolverElMensajeEsperado","despedirDebeDevolverElMensajeEsperado","Hola, Ministerio de Educación","Adiós, Ministerio de Educación"],
"M0/proyecto/.mvn/wrapper/maven-wrapper.properties":["apache-maven-3.9.16-bin.zip"]
}
for rel,tokens in contracts.items():
    txt=(root/rel).read_text(encoding="utf-8")
    for token in tokens:
        if token not in txt: bad(f"{rel}: code token missing: {token}")

for token in [
    "public class HolaMinisterio","args.length > 0","java -version","javac -version","mvn -version",
    "@SpringBootApplication","@RestController","@GetMapping(\"/hola\")","@GetMapping(\"/adios\")",
    "saludarDebeDevolverElMensajeEsperado","despedirDebeDevolverElMensajeEsperado",
    "./mvnw test","./mvnw package","./mvnw clean","./mvnw spring-boot:run","mvnw.cmd test",
    "./mvnw dependency:tree","./mvnw help:effective-pom","server.port=8081",
    "logging.level.org.springframework.web=DEBUG","/no-existe"
]:
    if token not in practice: bad(f"practical guide missing high-value token: {token}")

root_markdown={p.name for p in root.glob("*.md")}
if root_markdown != {"README.md"}:
    bad(f"student repository root must contain only README.md as Markdown; got {sorted(root_markdown)}")
m0_markdown={p.name for p in (root/"M0").glob("*.md")}
if m0_markdown != {"README.md","TEORIA.md","PRACTICA.md","CODESPACES.md"}:
    bad(
        "M0 student surface must contain only "
        "README/TEORIA/PRACTICA/CODESPACES Markdown; "
        f"got {sorted(m0_markdown)}"
    )
if not (root/".course/internal/PROMPT_MAESTRO_CONTINUIDAD.md").exists():
    bad("missing internal continuity contract under .course/internal")

human_report=root/".course/traceability/TRAZABILIDAD_M0.md"
if not human_report.exists():
    bad("missing internal human traceability report")
else:
    report=human_report.read_text(encoding="utf-8")
    required_sections=[
        "## Cómo puedes auditarlo tú","## Resumen verificable","## Teoría → práctica","## Guía → proyecto",
        "## Proyecto → guía","## Artefactos necesarios no introducidos en este módulo",
        "## Trazabilidad inversa por artefacto/símbolo","## Estados temporales y restauraciones","## Gates automáticos"
    ]
    for section in required_sections:
        if section not in report: bad(f"human report missing section: {section}")
    for sid in declared:
        if f"`{sid}`" not in report: bad(f"human report missing step id: {sid}")

gen=root/".course/traceability/generate_human_traceability.py"
if not gen.exists():
    bad("missing human traceability generator")
else:
    cp=subprocess.run([sys.executable,str(gen),"--check"],cwd=root,text=True,capture_output=True)
    if cp.returncode:
        bad("human traceability report out of sync: " + (cp.stdout+cp.stderr).strip())

if fail:
    print("M0 TRACEABILITY V3: FAIL")
    for x in fail: print(" -",x)
    raise SystemExit(1)
print(f"M0 TRACEABILITY V3: PASS | theory={len(concepts)} | steps={len(declared)} | artifacts={len(inventory)} | environments={len(m['environment_workflows'])}")
