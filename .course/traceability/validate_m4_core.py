from __future__ import annotations
from pathlib import Path
import collections
import json
import re
from typing import Any

EXPECTED_STEPS = {'4.1': 13, '4.2': 13, '4.3': 14, '4.4': 13, '4.5': 13, '4.6': 13, '4.7': 13}
ALLOWED_ACTIONS = {'CREATE', 'MODIFY', 'USE', 'DELETE', 'RESTORE', 'VERIFY'}
ALLOWED_STATES = {'PERMANENT', 'TEMPORARY'}
ALLOWED_CLASSES = {'GUIDE', 'INHERITED', 'SUPPORT'}
ENV_IDS = {'M4-W-CONSOLE', 'M4-W-INTELLIJ', 'M4-W-ECLIPSE', 'M4-W-VSCODE'}
TEXTUAL_SUFFIXES = {'.java', '.xml', '.properties', '.cmd', '.md', '.yml', '.yaml', '.json', '.py'}


def _load_json(path: Path, overrides: dict[str, Any] | None, root: Path):
    rel = path.relative_to(root).as_posix()
    if overrides and rel in overrides:
        return overrides[rel]
    return json.loads(path.read_text(encoding='utf-8'))


def validate(root: Path, overrides: dict[str, Any] | None = None) -> list[str]:
    root = root.resolve()
    fail: list[str] = []
    def bad(msg: str): fail.append(msg)
    def check(cond: bool, msg: str):
        if not cond: bad(msg)

    theory_path = root/'M4/TEORIA.md'
    practice_path = root/'M4/PRACTICA.md'
    manifest_path = root/'.course/traceability/M4.json'
    for p in [theory_path, practice_path, manifest_path]:
        if not p.exists(): bad(f'missing required file: {p.relative_to(root)}')
    if fail: return fail

    theory = theory_path.read_text(encoding='utf-8')
    practice = practice_path.read_text(encoding='utf-8')
    m = _load_json(manifest_path, overrides, root)

    # Editorial/structural surface.
    check(len(re.findall(r'^# Punto 4\.[1-7] - ', theory, re.M)) == 7, 'TEORIA must expose 7 points')
    check(len(re.findall(r'^## Objetivos de aprendizaje$', theory, re.M)) == 7, 'TEORIA must expose 7 objectives')
    check(len(re.findall(r'^## Bloque [1-5] - ', theory, re.M)) == 35, 'TEORIA must expose 35 theory blocks')
    check(len(re.findall(r'^# Práctica 4\.[1-7] - ', practice, re.M)) == 7, 'PRACTICA must expose 7 points')
    check(len(re.findall(r'^## Paso \d+ - ', practice, re.M)) == 92, 'PRACTICA must expose 92 steps')
    check(practice.count('| Error | Causa | Solución |') == 7, 'PRACTICA must expose 7 real error tables')
    for anchor in ['### Consola M4', '### IntelliJ IDEA M4', '### Eclipse M4', '### VS Code M4']:
        check(anchor in practice, f'environment anchor missing: {anchor}')
    check('MODIFY -> VERIFY -> RESTORE' in practice, 'practice must explicitly close the persistence laboratory transition')
    check('jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE' in practice,
          'practice must state canonical H2 restore value')
    check('spring.jpa.hibernate.ddl-auto=create-drop' in practice,
          'practice must state canonical ddl-auto restore value')

    # Strong schema contract inherited from M3.
    check(m.get('schema_version') == 3, 'schema_version must be 3')
    check(m.get('module') == 'M4', 'module must be M4')
    check(m.get('status') == 'COMPLETE', 'status must be COMPLETE')
    check(m.get('predecessor') == 'M3', 'predecessor must be M3')
    policy = m.get('policy', {})
    for key in [
        'step_level_traceability_required', 'temporary_transitions_must_close',
        'cumulative_project_continuity_required', 'reverse_traceability_required',
        'negative_mutation_validation_required']:
        check(policy.get(key) is True, f'policy missing/false: {key}')

    concepts = m.get('theory_concepts', [])
    check(len(concepts) == 35, f'manifest must declare 35 concepts; got {len(concepts)}')
    theory_ids = [c.get('id') for c in concepts]
    check(len(theory_ids) == len(set(theory_ids)), 'duplicate theory concept id')
    for c in concepts:
        anchor = str(c.get('anchor', '')).strip()
        check(bool(anchor) and f'## {anchor}' in theory,
              f"theory anchor missing: {c.get('id')} -> {anchor}")

    env = m.get('environment_workflows', [])
    env_ids = {x.get('id') for x in env}
    check(env_ids == ENV_IDS, f'exact four M4 environment workflows required; got {sorted(env_ids)}')
    for w in env:
        check(w.get('anchor') in practice, f"environment workflow anchor missing: {w.get('id')}")

    expected_manifests = [f'.course/traceability/M4/{p}.json' for p in EXPECTED_STEPS]
    check(m.get('step_manifests') == expected_manifests, 'step_manifests must list exact seven point manifests')
    all_steps = []
    for point, count in EXPECTED_STEPS.items():
        p = root/f'.course/traceability/M4/{point}.json'
        check(p.exists(), f'missing point manifest: {p.relative_to(root)}')
        if not p.exists(): continue
        d = _load_json(p, overrides, root)
        check(d.get('module') == 'M4' and d.get('point') == point, f'{point}: invalid module/point')
        steps = d.get('steps', [])
        check(len(steps) == count, f'{point}: expected {count} steps; got {len(steps)}')
        all_steps.extend(steps)
    check(len(all_steps) == 92, f'exact 92 traceable steps required; got {len(all_steps)}')
    check(m.get('current_traced_steps') == 92, 'current_traced_steps must be 92')
    check(m.get('expected_total_steps_when_complete') == 92, 'expected_total_steps_when_complete must be 92')

    declared = [s.get('id') for s in all_steps]
    check(len(declared) == len(set(declared)), 'duplicate step id')
    concept_set = set(theory_ids)
    inventory_list = m.get('artifacts', [])
    inventory = {a.get('path'): a for a in inventory_list if a.get('path')}
    check(len(inventory) == len(inventory_list), 'duplicate artifact inventory path')

    # Every practice heading must correspond exactly to one step manifest.
    guide_headings = {}
    for block in re.finditer(r'(?ms)^# Práctica (4\.\d+) - .*?(?=^# Práctica 4\.|\Z)', practice):
        point = block.group(1)
        for mm in re.finditer(r'^## Paso (\d+) - (.+)$', block.group(0), re.M):
            guide_headings[(point, int(mm.group(1)))] = f"## Paso {mm.group(1)} - {mm.group(2)}"
    check(len(guide_headings) == 92, f'guide headings expected 92; got {len(guide_headings)}')

    step_relations: dict[str, set[str]] = {}
    step_actions: dict[tuple[str, str], set[str]] = {}
    for s in all_steps:
        sid = s.get('id')
        point = s.get('practice')
        num = s.get('number')
        check(s.get('heading') == guide_headings.get((point, num)),
              f'{sid}: heading mismatch with PRACTICA.md')
        refs = s.get('theory_refs', [])
        check(bool(refs), f'{sid}: no theory_refs')
        for ref in refs:
            check(ref in concept_set, f'{sid}: unknown theory ref {ref}')
        erefs = s.get('environment_refs', [])
        check(bool(erefs), f'{sid}: no environment_refs')
        for ref in erefs:
            check(ref in ENV_IDS, f'{sid}: unknown environment ref {ref}')
        state = s.get('state')
        check(state in ALLOWED_STATES, f'{sid}: invalid step state {state}')
        arts = s.get('artifacts', [])
        check(bool(arts), f'{sid}: no artifact evidence')
        check(bool(s.get('commands')), f'{sid}: no commands')
        check(bool(s.get('observables')), f'{sid}: no observables')
        check(bool(s.get('verification')), f'{sid}: no verification')
        has_close = False
        for a in arts:
            rel = a.get('path')
            action = a.get('action')
            astate = a.get('state')
            check(action in ALLOWED_ACTIONS, f'{sid}: invalid action {action}')
            check(astate in ALLOWED_STATES, f'{sid}: invalid artifact state {astate}')
            check(bool(rel), f'{sid}: artifact without path')
            if not rel: continue
            ap = root/rel
            if astate == 'PERMANENT' and action != 'DELETE':
                check(ap.exists(), f'{sid}: permanent artifact missing: {rel}')
                check(rel in inventory, f'{sid}: permanent artifact absent from reverse inventory: {rel}')
            symbols = a.get('symbols', [])
            if action in {'CREATE','MODIFY','RESTORE'}:
                check(bool(symbols), f'{sid}: {action} artifact without symbols: {rel}')
            if ap.is_file() and ap.suffix in TEXTUAL_SUFFIXES and astate == 'PERMANENT' and action != 'DELETE':
                txt = ap.read_text(encoding='utf-8', errors='ignore')
                for token in symbols:
                    check(token in txt, f'{sid}: permanent symbol missing in {rel}: {token}')
            step_relations.setdefault(rel, set()).add(sid)
            step_actions.setdefault((rel, sid), set()).add(action)
            if action in {'RESTORE','DELETE'}: has_close = True
        if state == 'TEMPORARY':
            check(has_close, f'{sid}: TEMPORARY step without RESTORE/DELETE')

    # Artifact inventory must cover every project file and reverse to guide/inheritance/support.
    project_files = {
        p.relative_to(root).as_posix()
        for p in (root/'M4/proyecto').rglob('*')
        if p.is_file() and 'target' not in p.parts
    }
    check(set(inventory) == project_files,
          f'artifact inventory mismatch: missing={sorted(project_files-set(inventory))}, extra={sorted(set(inventory)-project_files)}')
    for rel, a in inventory.items():
        cls = a.get('classification')
        check(cls in ALLOWED_CLASSES, f'invalid classification {cls}: {rel}')
        check((root/rel).exists(), f'classified artifact missing: {rel}')
        if cls == 'SUPPORT':
            check(bool(a.get('used_by')), f'SUPPORT without used_by: {rel}')
            check(bool(a.get('missing_effect')), f'SUPPORT without missing_effect: {rel}')
        for sid in a.get('evolution', []):
            if sid.startswith('M4-P-'):
                check(sid in declared, f'{rel}: evolution references unknown step {sid}')
        for token in a.get('symbols', []):
            p = root/rel
            if p.is_file() and p.suffix in TEXTUAL_SUFFIXES:
                txt = p.read_text(encoding='utf-8', errors='ignore')
                check(token in txt, f'{rel}: inventory symbol missing: {token}')

    # Cumulative M3 -> M4 continuity: every predecessor project file must survive.
    m3_files = {
        p.relative_to(root/'M3/proyecto').as_posix(): p
        for p in (root/'M3/proyecto').rglob('*')
        if p.is_file() and 'target' not in p.parts
    } if (root/'M3/proyecto').exists() else {}
    m4_files = {
        p.relative_to(root/'M4/proyecto').as_posix(): p
        for p in (root/'M4/proyecto').rglob('*')
        if p.is_file() and 'target' not in p.parts
    }
    check(bool(m3_files), 'M3/proyecto must exist for cumulative validation')
    lost = sorted(set(m3_files)-set(m4_files))
    check(not lost, f'M3 project artifacts disappeared in M4: {lost}')
    continuity = m.get('continuity_from_m3', [])
    c_by_m3 = {x.get('m3_path'): x for x in continuity}
    check(len(c_by_m3) == len(continuity), 'duplicate M3 continuity rows')
    check(len(continuity) == len(m3_files),
          f'continuity rows must cover all M3 project files: {len(continuity)}/{len(m3_files)}')
    for rel in m3_files:
        key='M3/proyecto/'+rel
        row=c_by_m3.get(key)
        check(row is not None, f'M3 continuity missing row: {key}')
        if row:
            check(row.get('m4_path') == 'M4/proyecto/'+rel, f'continuity path mismatch: {key}')
            check(row.get('status') in {'PRESERVED','MODIFIED_ADAPTED','RESTORED','RESTORED_ADAPTED'},
                  f'invalid continuity status: {key} -> {row.get("status")}')
            check(bool(row.get('reason')), f'continuity row without reason: {key}')

    # Explicitly require the six M3 regression tests that had disappeared in the first M4 publication.
    restored = [
        'alumno/AlumnoControllerTest.java','alumno/AlumnoServiceTest.java',
        'common/util/FechasUtilTest.java','expediente/ExpedienteControllerTest.java',
        'expediente/ExpedienteServiceTest.java','saludo/SaludoControllerTest.java']
    for rel in restored:
        check((root/'M4/proyecto/src/test/java/es/mecd/demo/miproyecto'/rel).exists(),
              f'restored inherited regression test missing: {rel}')

    # Markdown/code quality contract. M4-R1 intentionally adds one properties block for RESTORE.
    expected_lang = {
        'TEORIA': {'java': 88, 'properties': 5, 'text': 4, 'xml': 3, 'sql': 3, 'json': 1},
        'PRACTICA': {'java': 84, 'bash': 19, 'text': 12, 'properties': 4, 'xml': 2, 'json': 2, 'sql': 1},
    }
    def audit_markdown(name, text):
        lines=text.splitlines(); in_code=False; lang=''; block=[]; starts=collections.Counter(); blocks=0
        for number,line in enumerate(lines,1):
            if line.startswith('```'):
                if not in_code:
                    in_code=True; lang=line[3:].strip(); block=[]; starts[lang]+=1
                    check(bool(lang), f'{name}:{number}: fence without language')
                else:
                    blocks+=1; check(any(x.strip() for x in block), f'{name}:{number}: empty code block')
                    in_code=False; lang=''; block=[]
                continue
            if in_code:
                block.append(line)
                check(len(line)<=85, f'{name}:{number}: code >85 characters')
                if lang=='xml': check('`' not in line, f'{name}:{number}: backtick inside XML')
            if line.startswith('#') and number>1:
                check(lines[number-2].strip()=='', f'{name}:{number}: missing blank before heading')
        check(not in_code, f'{name}: unclosed fence')
        check(dict(starts)==expected_lang[name], f'{name}: unexpected fences {dict(starts)}')
        check(blocks==sum(expected_lang[name].values()), f'{name}: unexpected code block count {blocks}')
    audit_markdown('TEORIA', theory); audit_markdown('PRACTICA', practice)
    check(sum(expected_lang['TEORIA'].values())+sum(expected_lang['PRACTICA'].values())==228,
          'M4-R1 must contain 228 code blocks (227 source-preserved + 1 RESTORE enrichment)')
    for badchar in ('Ã','\ufffd'):
        check(badchar not in theory and badchar not in practice, f'mojibake detected: {badchar}')

    required = [
        'M4/README.md','M4/TEORIA.md','M4/PRACTICA.md','M4/M4_TEORIA.pdf','M4/M4_PRACTICA.pdf',
        '.course/source-audit/M4.md','.course/traceability/M4.json','.course/traceability/TRAZABILIDAD_M4.md',
        '.course/traceability/validate_m4_core.py','.course/traceability/validate_m4.py',
        '.course/traceability/validate_m4_human.py','.course/traceability/validate_m4_mutation.py',
        '.course/traceability/validate_m4_runtime.py','.github/workflows/validar-m4.yml']
    for rel in required: check((root/rel).exists(), f'missing required deliverable: {rel}')
    for rel in ['M4/TEORIA.preformat.md','M4/PRACTICA.preformat.md']:
        check(not (root/rel).exists(), f'intermediate artifact must not ship: {rel}')
    for rel in ['M4/M4_TEORIA.pdf','M4/M4_PRACTICA.pdf']:
        p=root/rel; check(p.exists() and p.stat().st_size>100_000, f'PDF missing/anomalous: {rel}')
    return fail
