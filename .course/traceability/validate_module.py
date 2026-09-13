#!/usr/bin/env python3
"""Validador estructural reutilizable para módulos del curso.

Uso:
    python .course/traceability/validate_module.py M1 [repo-root]

El validador comprueba invariantes comunes. Los contratos semánticos/runtime
específicos de cada módulo pertenecen a su gate adicional y al CI.
"""
from pathlib import Path
import json
import re
import sys

if len(sys.argv) < 2:
    raise SystemExit("usage: validate_module.py M<n> [repo-root]")

module = sys.argv[1].upper()
if not re.fullmatch(r"M\d+", module):
    raise SystemExit(f"invalid module id: {module}")
module_number = int(module[1:])
root = Path(sys.argv[2] if len(sys.argv) > 2 else ".").resolve()
module_dir = root / module
manifest_path = root / ".course/traceability" / f"{module}.json"

fail = []
def bad(msg):
    fail.append(msg)

def load_json(path):
    try:
        return json.loads(path.read_text(encoding="utf-8"))
    except Exception as exc:
        bad(f"cannot read JSON {path.relative_to(root)}: {exc}")
        return {}

if not manifest_path.exists():
    raise SystemExit(f"missing manifest: {manifest_path}")

m = load_json(manifest_path)
theory_path = module_dir / "TEORIA.md"
practice_path = module_dir / "PRACTICA.md"
for p in (theory_path, practice_path):
    if not p.exists():
        bad(f"missing student document: {p.relative_to(root)}")

theory = theory_path.read_text(encoding="utf-8") if theory_path.exists() else ""
practice = practice_path.read_text(encoding="utf-8") if practice_path.exists() else ""

if m.get("module") != module:
    bad(f"manifest module must be {module}")
if m.get("schema_version") != 3:
    bad("schema_version must be 3")
policy = m.get("policy", {})
if not policy.get("step_level_traceability_required"):
    bad("step-level traceability policy missing")
if not policy.get("temporary_transitions_must_close"):
    bad("temporary transition policy missing")

concepts = m.get("theory_concepts", [])
theory_ids = [c.get("id") for c in concepts]
if len(theory_ids) != len(set(theory_ids)):
    bad("duplicate theory concept id")
for c in concepts:
    cid, anchor = c.get("id"), c.get("anchor")
    if not cid or not anchor:
        bad(f"invalid theory concept: {c}")
    elif anchor.lower() not in theory.lower():
        bad(f"theory anchor missing: {cid} -> {anchor}")

workflows = m.get("environment_workflows", [])
env_ids = {w.get("id") for w in workflows}
expected_env = {f"{module}-W-CONSOLE", f"{module}-W-INTELLIJ", f"{module}-W-ECLIPSE", f"{module}-W-VSCODE"}
if env_ids != expected_env:
    bad(f"exact four environment workflows required: {sorted(expected_env)}")
for w in workflows:
    if w.get("anchor") not in practice:
        bad(f"environment workflow missing from practice: {w.get('id')} -> {w.get('anchor')}")

steps = []
manifest_files = m.get("step_manifests", [])
if not manifest_files:
    bad("no step manifests declared")
for rel in manifest_files:
    p = root / rel
    if not p.exists():
        bad(f"step manifest missing: {rel}")
        continue
    data = load_json(p)
    if data.get("module") != module:
        bad(f"{rel}: module mismatch")
    steps.extend(data.get("steps", []))

# Derive every currently published practical step directly from PRACTICA.md.
# Current editions use '# Punto n.x - ...'; older editions used
# '# Práctica n.x - ...'. Both are valid student-facing headings.
derived = []
headings = {}
practice_re = re.compile(
    rf"(?ms)^# (?:Práctica|Punto) ({module_number}\.\d+) - .*?(?=^# (?:Práctica|Punto) |\Z)"
)
for pblock in practice_re.finditer(practice):
    pnum = pblock.group(1)
    compact = pnum.replace(".", "")
    for mm in re.finditer(r"^## Paso (\d+) - (.+)$", pblock.group(0), re.M):
        n = int(mm.group(1))
        sid = f"{module}-P-{compact}-S{n:02d}"
        derived.append(sid)
        headings[sid] = f"## Paso {n} - {mm.group(2)}"

declared = [s.get("id") for s in steps]
if len(declared) != len(set(declared)):
    bad("duplicate step id")
if set(derived) != set(declared):
    bad(
        "guide/manifest step id mismatch: "
        f"missing={sorted(set(derived)-set(declared))}, "
        f"extra={sorted(set(declared)-set(derived))}"
    )
current = m.get("current_traced_steps", len(declared))
if current != len(declared):
    bad(f"current_traced_steps={current} but manifests declare {len(declared)}")
expected_total = m.get("expected_total_steps_when_complete")
status = m.get("status")
if expected_total is not None:
    if len(declared) > expected_total:
        bad(f"declared steps exceed expected total {expected_total}")
    if status == "COMPLETE" and len(declared) != expected_total:
        bad(f"COMPLETE module must expose exactly {expected_total} steps; got {len(declared)}")

inventory_list = m.get("artifacts", [])
inventory = {a.get("path"): a for a in inventory_list}
if None in inventory:
    bad("artifact inventory entry without path")
if len(inventory) != len(inventory_list):
    bad("duplicate artifact inventory path")

# Maven Wrapper is cumulative course infrastructure inherited by every module.
# Classify the canonical wrapper files centrally when a module snapshot contains
# them, so modules do not have to pretend they were pedagogically created there.
canonical_wrapper = {
    f"{module}/proyecto/mvnw": {
        "path": f"{module}/proyecto/mvnw",
        "classification": "INHERITED",
        "origin": "M0-MAVEN-WRAPPER",
        "evolution": [],
        "symbols": [],
    },
    f"{module}/proyecto/mvnw.cmd": {
        "path": f"{module}/proyecto/mvnw.cmd",
        "classification": "INHERITED",
        "origin": "M0-MAVEN-WRAPPER",
        "evolution": [],
        "symbols": [],
    },
    f"{module}/proyecto/.mvn/wrapper/maven-wrapper.properties": {
        "path": f"{module}/proyecto/.mvn/wrapper/maven-wrapper.properties",
        "classification": "INHERITED",
        "origin": "M0-MAVEN-WRAPPER",
        "evolution": [],
        "symbols": [],
    },
}
for rel, entry in canonical_wrapper.items():
    if (root / rel).exists() and rel not in inventory:
        inventory[rel] = entry

inventory_paths = set(inventory)

allowed_actions = {"CREATE", "MODIFY", "USE", "DELETE", "RESTORE", "VERIFY"}
allowed_states = {"PERMANENT", "TEMPORARY"}
textual_suffixes = {".java", ".xml", ".properties", ".cmd", ".md", ".yml", ".yaml", ".json", ".py"}
skip_literal_symbols = {"Maven Wrapper", "Maven Wrapper Windows", "target/"}

step_relations = {}
step_actions = {}
for s in steps:
    sid = s.get("id")
    if sid in headings and s.get("heading") != headings[sid]:
        bad(f"{sid}: heading mismatch: manifest={s.get('heading')!r}, guide={headings[sid]!r}")
    refs = s.get("theory_refs", [])
    if not refs:
        bad(f"{sid}: no theory_refs")
    for ref in refs:
        if ref not in theory_ids:
            bad(f"{sid}: unknown theory ref {ref}")
    for ref in s.get("environment_refs", []):
        if ref not in env_ids:
            bad(f"{sid}: unknown environment ref {ref}")
    if s.get("state") not in allowed_states:
        bad(f"{sid}: invalid state {s.get('state')}")
    if not (s.get("artifacts") or s.get("commands") or s.get("observables")):
        bad(f"{sid}: no trace evidence")
    if not s.get("observables"):
        bad(f"{sid}: no observables")
    if not s.get("verification"):
        bad(f"{sid}: no verification")

    for a in s.get("artifacts", []):
        rel = a.get("path")
        action = a.get("action")
        state = a.get("state")
        if action not in allowed_actions:
            bad(f"{sid}: invalid action {action}")
        if state not in allowed_states:
            bad(f"{sid}: invalid artifact state {state}")
        if not rel:
            bad(f"{sid}: artifact without path")
            continue
        step_relations.setdefault(rel, set()).add(sid)
        step_actions.setdefault((rel, sid), set()).add(action)
        p = root / rel
        if state == "PERMANENT" and action != "DELETE" and not p.exists():
            bad(f"{sid}: permanent artifact missing: {rel}")
        if state == "PERMANENT" and action != "DELETE" and rel not in inventory_paths:
            bad(f"{sid}: permanent artifact absent from reverse inventory: {rel}")
        if action in {"CREATE", "MODIFY", "RESTORE", "VERIFY"} and not a.get("symbols"):
            bad(f"{sid}: {action} artifact without symbols: {rel}")
        if state == "PERMANENT" and action in {"CREATE", "MODIFY", "RESTORE", "VERIFY"} and p.is_file() and p.suffix in textual_suffixes:
            txt = p.read_text(encoding="utf-8")
            for token in a.get("symbols", []):
                if token in skip_literal_symbols:
                    continue
                if token not in txt:
                    bad(f"{sid}: declared permanent symbol/token missing in {rel}: {token}")
    if s.get("state") == "TEMPORARY":
        if not any(a.get("action") in {"RESTORE", "DELETE"} for a in s.get("artifacts", [])):
            bad(f"{sid}: TEMPORARY step without RESTORE/DELETE transition")

# Reverse inventory and physical classification.
for rel, a in inventory.items():
    if not rel:
        continue
    p = root / rel
    if not p.exists():
        bad(f"classified artifact missing: {rel}")
    classification = a.get("classification")
    if classification not in {"GUIDE", "INHERITED", "SUPPORT"}:
        bad(f"invalid classification: {rel}")
    if classification == "SUPPORT":
        if not a.get("used_by"):
            bad(f"SUPPORT without used_by: {rel}")
        if not a.get("missing_effect"):
            bad(f"SUPPORT without missing_effect: {rel}")
    if classification in {"GUIDE", "INHERITED"} and p.is_file() and p.suffix in {".java", ".xml", ".properties"}:
        txt = p.read_text(encoding="utf-8")
        for token in a.get("symbols", []):
            if token in skip_literal_symbols:
                continue
            if token not in txt:
                bad(f"{rel}: final symbol/token missing: {token}")
        # Temporary symbols document experiments, but they must not leak into the final snapshot.
        for token in a.get("temporary_symbols", []):
            if token in txt:
                bad(f"{rel}: temporary symbol leaked into final snapshot: {token}")

# Every functional file in the module snapshot must be classified.
functional = []
project_dir = module_dir / "proyecto"
if project_dir.exists():
    for p in project_dir.rglob("*"):
        if p.is_file() and (p.suffix in {".java", ".xml", ".properties", ".cmd"} or p.name == "mvnw"):
            functional.append(p.relative_to(root).as_posix())
for rel in functional:
    if rel not in inventory:
        bad(f"unclassified functional artifact: {rel}")

# Current-module GUIDE origins/evolutions must be backed by actual step actions.
for rel, a in inventory.items():
    if not rel or a.get("classification") != "GUIDE":
        continue
    origin = a.get("origin")
    if origin and origin.startswith(f"{module}-P-"):
        if origin not in step_relations.get(rel, set()):
            bad(f"{rel}: inventory origin {origin} has no step relation")
        elif "CREATE" not in step_actions.get((rel, origin), set()):
            bad(f"{rel}: pedagogical origin {origin} is not a CREATE action")
    for sid in a.get("evolution", []):
        if sid and sid.startswith(f"{module}-P-"):
            if sid not in step_relations.get(rel, set()):
                bad(f"{rel}: inventory evolution {sid} has no step relation")
            elif not (step_actions.get((rel, sid), set()) & {"MODIFY", "RESTORE"}):
                bad(f"{rel}: inventory evolution {sid} does not modify/restore the artifact")

# Student-facing Markdown must stay minimal and free of internal maintenance vocabulary.
root_markdown = {p.name for p in root.glob("*.md")}
if root_markdown != {"README.md"}:
    bad(f"student repository root must contain only README.md as Markdown; got {sorted(root_markdown)}")
module_markdown = {p.name for p in module_dir.glob("*.md")}
if module_markdown != {"README.md", "TEORIA.md", "PRACTICA.md"}:
    bad(f"{module} student surface must contain only README/TEORIA/PRACTICA Markdown; got {sorted(module_markdown)}")
for name, text in [("TEORIA", theory), ("PRACTICA", practice)]:
    questions = len(re.findall(r"^### Pregunta(?: breve| de integración| final)?$", text, re.M))
    answers = len(re.findall(r"^### Respuesta(?: razonada)?$", text, re.M))
    if questions != answers:
        bad(f"{name}: questions/answers invalid {questions}/{answers}")
    if questions == 0:
        bad(f"{name}: no question/answer pedagogy detected")
    for patt in [r"(?i)\bcheckpoint\b", r"(?i)fidelidad didáctica", r"(?i)GUIDE/INHERITED/SUPPORT", r"(?i)\.course/traceability"]:
        if re.search(patt, text):
            bad(f"{name}: internal meta visible: {patt}")

if not (root / ".course/internal/PROMPT_MAESTRO_CONTINUIDAD.md").exists():
    bad("missing internal continuity contract under .course/internal")

if fail:
    print(f"{module} GENERIC TRACEABILITY V3: FAIL")
    for x in fail:
        print(" -", x)
    raise SystemExit(1)

print(
    f"{module} GENERIC TRACEABILITY V3: PASS | "
    f"status={status} | theory={len(concepts)} | steps={len(declared)} | "
    f"artifacts={len(inventory)} | environments={len(workflows)}"
)
