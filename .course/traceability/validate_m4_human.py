from pathlib import Path
import json
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else '.').resolve()
data = json.loads((root / '.course/traceability/M4.json').read_text(encoding='utf-8'))
human = (root / '.course/traceability/TRAZABILIDAD_M4.md').read_text(encoding='utf-8')
required = ('theory', 'action', 'artifact', 'symbol', 'command', 'observable', 'verification')
errors = []
concept_ids = {c['id'] for c in data.get('theory_concepts', [])}

for step in data['steps']:
    for key in required:
        if not str(step.get(key, '')).strip():
            errors.append(f"{step['id']}: falta {key}")
    label = f"{step['point']}.{step['step']}"
    if f'| {label} |' not in human:
        errors.append(f'{step["id"]}: no aparece en vista humana')
    theory = step['theory']
    if theory not in concept_ids:
        errors.append(f'{step["id"]}: concepto inexistente {theory}')
    # Every repository path explicitly named by the chain must resolve.
    for segment in (x.strip() for x in str(step.get('artifact', '')).split(' / ')):
        if segment.startswith(('M4/', '.course/', '.github/')) and not (root / segment).exists():
            errors.append(f'{step["id"]}: artefacto inexistente {segment}')

if human.count('\n| 4.') != 92:
    errors.append('La tabla humana no contiene exactamente 92 filas de pasos')
if len({s['id'] for s in data['steps']}) != 92:
    errors.append('IDs de pasos no son únicos')
if len(concept_ids) != 35:
    errors.append('IDs de conceptos no son únicos')

if errors:
    print('M4 HUMAN TRACEABILITY: FAIL')
    for error in errors:
        print(' -', error)
    raise SystemExit(1)
print('M4 HUMAN TRACEABILITY: PASS | reversible chains=92/92 | artifact paths=resolved')
