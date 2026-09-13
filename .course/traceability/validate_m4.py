from pathlib import Path
import collections
import json
import re
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else '.').resolve()
theory_path = root / 'M4' / 'TEORIA.md'
practice_path = root / 'M4' / 'PRACTICA.md'
theory = theory_path.read_text(encoding='utf-8')
practice = practice_path.read_text(encoding='utf-8')
data = json.loads((root / '.course/traceability/M4.json').read_text(encoding='utf-8'))
errors = []


def check(condition, message):
    if not condition:
        errors.append(message)


check(len(re.findall(r'^# Punto 4\.[1-7] - ', theory, re.M)) == 7, 'TEORIA: 7 puntos')
check(len(re.findall(r'^## Objetivos de aprendizaje$', theory, re.M)) == 7, 'TEORIA: 7 objetivos')
check(len(re.findall(r'^## Bloque [1-5] - ', theory, re.M)) == 35, 'TEORIA: 35 bloques')
check(len(re.findall(r'^# Práctica 4\.[1-7] - ', practice, re.M)) == 7, 'PRACTICA: 7 puntos')
check(len(re.findall(r'^## Paso \d+ - ', practice, re.M)) == 92, 'PRACTICA: 92 pasos')
check(practice.count('| Error | Causa | Solución |') == 7, 'PRACTICA: 7 tablas de errores')
check(data.get('counts') == {'theory': 35, 'steps': 92}, 'JSON: counts 35/92')
check(len(data.get('theory_concepts', [])) == 35, 'JSON: 35 conceptos')
check(len(data.get('steps', [])) == 92, 'JSON: 92 pasos')

expected_steps = {'4.1': 13, '4.2': 13, '4.3': 14, '4.4': 13, '4.5': 13, '4.6': 13, '4.7': 13}
for point, count in expected_steps.items():
    actual = len([s for s in data['steps'] if s['point'] == point])
    check(actual == count, f'{point}: {count} pasos')
    point_data = json.loads((root / f'.course/traceability/M4/{point}.json').read_text(encoding='utf-8'))
    check(point_data.get('counts') == {'theory': 5, 'steps': count}, f'{point}: JSON 5/{count}')
    check(len(point_data.get('theory_concepts', [])) == 5, f'{point}: 5 conceptos en JSON de punto')
    check(len(point_data.get('steps', [])) == count, f'{point}: {count} pasos en JSON de punto')
    main_concepts = [c for c in data['theory_concepts'] if c['point'] == point]
    main_steps = [s for s in data['steps'] if s['point'] == point]
    check(point_data.get('theory_concepts') == main_concepts, f'{point}: conceptos desincronizados con M4.json')
    check(point_data.get('steps') == main_steps, f'{point}: pasos desincronizados con M4.json')

expected_lang = {
    'TEORIA': {'java': 88, 'properties': 5, 'text': 4, 'xml': 3, 'sql': 3, 'json': 1},
    'PRACTICA': {'java': 84, 'bash': 19, 'text': 12, 'properties': 3, 'xml': 2, 'json': 2, 'sql': 1},
}

def audit_markdown(name, text):
    lines = text.splitlines()
    in_code = False
    lang = ''
    block_lines = []
    starts = collections.Counter()
    blocks = 0
    for number, line in enumerate(lines, 1):
        if line.startswith('```'):
            if not in_code:
                in_code = True
                lang = line[3:].strip()
                block_lines = []
                starts[lang] += 1
                check(bool(lang), f'{name}:{number}: fence sin lenguaje')
            else:
                blocks += 1
                check(any(x.strip() for x in block_lines), f'{name}:{number}: bloque de código vacío')
                in_code = False
                lang = ''
                block_lines = []
            continue
        if in_code:
            block_lines.append(line)
            check(len(line) <= 85, f'{name}:{number}: código >85 caracteres')
            if lang == 'xml':
                check('`' not in line, f'{name}:{number}: backtick dentro de XML')
            check(line.strip() not in {'java', 'json', 'bash', 'text', 'xml', 'sql', 'properties'},
                  f'{name}:{number}: marcador de lenguaje incrustado dentro del código')
        if line.startswith('#') and number > 1:
            check(lines[number - 2].strip() == '', f'{name}:{number}: falta blanco antes de heading')
    check(not in_code, f'{name}: fence sin cerrar')
    check(dict(starts) == expected_lang[name], f'{name}: lenguajes/fences inesperados: {dict(starts)}')
    check(blocks == sum(expected_lang[name].values()), f'{name}: número de bloques de código inesperado: {blocks}')


audit_markdown('TEORIA', theory)
audit_markdown('PRACTICA', practice)
check(sum(expected_lang['TEORIA'].values()) + sum(expected_lang['PRACTICA'].values()) == 227,
      'Contrato interno: 227 bloques de código')
check(len(re.findall(r'Pregunta', theory + '\n' + practice, re.I)) == 120, 'Guías: 120 apariciones editoriales de Pregunta')
check(len(re.findall(r'Reto', theory + '\n' + practice, re.I)) == 20, 'Guías: 20 retos')
check(len(re.findall(r'Errores comunes', theory + '\n' + practice, re.I)) == 15, 'Guías: 15 apariciones/secciones de Errores comunes')
check(len(re.findall(r'Resultado esperado', practice, re.I)) == 7, 'PRACTICA: 7 resultados esperados')

for bad in ('Ã', '\ufffd'):
    check(bad not in theory and bad not in practice, f'Guías: mojibake detectado ({bad})')

# Every traceability concept must point to an actual theory block title.
concept_ids = {c['id'] for c in data.get('theory_concepts', [])}
for concept in data.get('theory_concepts', []):
    anchor = str(concept.get('anchor', '')).strip()
    check(bool(anchor) and f'## {anchor}' in theory,
          f"{concept.get('id')}: anchor teórico no encontrado: {anchor}")

# Every traceability step must point to an actual practice step title.
for step in data.get('steps', []):
    check(step.get('theory') in concept_ids, f"{step.get('id')}: concepto inexistente {step.get('theory')}")
    heading = f"## Paso {step.get('step')} - {step.get('title')}"
    point = step.get('point')
    point_heading = f'# Práctica {point} - '
    point_pos = practice.find(point_heading)
    check(point_pos >= 0 and practice.find(heading, point_pos) >= 0,
          f"{step.get('id')}: heading práctico no encontrado")

for rel in ['M4/M4_TEORIA.pdf', 'M4/M4_PRACTICA.pdf']:
    pdf = root / rel
    check(pdf.exists() and pdf.stat().st_size > 100_000, f'{rel}: PDF ausente/anómalo')

required_files = [
    'M4/README.md', 'M4/TEORIA.md', 'M4/PRACTICA.md',
    'M4/M4_TEORIA.pdf', 'M4/M4_PRACTICA.pdf', 'M4/proyecto/pom.xml',
    'M4/proyecto/mvnw', 'M4/proyecto/mvnw.cmd',
    '.course/source-audit/M4.md', '.course/traceability/M4.json',
    '.course/traceability/TRAZABILIDAD_M4.md',
    '.course/traceability/validate_m4.py',
    '.course/traceability/validate_m4_human.py',
    '.course/traceability/validate_m4_runtime.py',
    '.github/workflows/validar-m4.yml',
]
for rel in required_files:
    check((root / rel).exists(), f'Falta {rel}')

# Intermediate authoring files must never be shipped as canonical module files.
for rel in ['M4/TEORIA.preformat.md', 'M4/PRACTICA.preformat.md']:
    check(not (root / rel).exists(), f'No debe publicarse artefacto intermedio: {rel}')

if errors:
    print('M4 FINAL CONTRACT: FAIL')
    for error in errors:
        print(' -', error)
    raise SystemExit(1)
print('M4 FINAL CONTRACT: PASS | COMPLETE | theory=35 | steps=92/92 | code-blocks=227 | tables=7 | guides=QA')
