#!/usr/bin/env python3
from pathlib import Path
import json, re, sys

root=Path(sys.argv[1] if len(sys.argv)>1 else '.')
m=json.loads((root/'.course/traceability/M0.json').read_text(encoding='utf-8'))
theory=(root/'M0/TEORIA.md').read_text(encoding='utf-8')
practice=(root/'M0/PRACTICA.md').read_text(encoding='utf-8')
fail=[]
def bad(x): fail.append(x)

theory_ids={c['id'] for c in m['theory_concepts']}
if len(theory_ids)!=len(m['theory_concepts']): bad('duplicate theory concept id')
for c in m['theory_concepts']:
    if c['anchor'].lower() not in theory.lower(): bad(f"theory anchor missing: {c['id']} -> {c['anchor']}")

derived=[]
for pblock in re.finditer(r'(?ms)^# Práctica (0\.\d+) - .*?(?=^# Práctica |^# Cierre práctico del módulo 0|\Z)',practice):
    p=pblock.group(1)
    nums=[int(x) for x in re.findall(r'^## Paso (\d+) - ',pblock.group(0),re.M)]
    expected=m['practice_contracts'].get(p,{}).get('expected_steps')
    if expected is None: bad(f'practice without contract: {p}'); continue
    if nums != list(range(1,expected+1)): bad(f'{p}: steps {nums} != 1..{expected}')
    for ref in m['practice_contracts'][p]['theory_refs']:
        if ref not in theory_ids: bad(f'{p}: unknown theory ref {ref}')
    derived += [f"M0-P-{int(p.split('.')[1]):02d}-S{n:02d}" for n in nums]
for letter in m['final_steps']:
    if not re.search(rf'^## Paso final {letter} - ',practice,re.M): bad(f'final step missing: {letter}')
    derived.append(f'M0-P-FINAL-{letter}')
if len(derived)!=51: bad(f'expected 51 practical trace ids, got {len(derived)}')

for w in m['environment_workflows']:
    if w['anchor'] not in practice: bad(f"environment workflow missing: {w['id']}")

classified={a['path']:a for a in m['artifacts']}
for rel,a in classified.items():
    if not (root/rel).exists(): bad(f'missing classified artifact: {rel}')
    if a['classification']=='SUPPORT' and not a.get('reason'): bad(f'SUPPORT without reason: {rel}')
    if a['classification']=='GUIDE' and a.get('origin','').startswith('M0-P-') and a['origin'] not in derived:
        bad(f'GUIDE origin is not a real practical step: {rel} -> {a["origin"]}')
functional=[]
for p in (root/'M0').rglob('*'):
    if p.is_file() and (p.suffix in {'.java','.xml','.properties','.cmd'} or p.name=='mvnw'):
        functional.append(p.relative_to(root).as_posix())
for rel in functional:
    if rel not in classified: bad(f'unclassified functional artifact: {rel}')

for name,text in [('TEORIA',theory),('PRACTICA',practice)]:
    for patt in [r'(?i)\bcheckpoint\b',r'(?i)fidelidad didáctica',r'(?i)GUIDE/INHERITED/SUPPORT',r'(?i)\.course/traceability']:
        if re.search(patt,text): bad(f'{name}: internal meta visible: {patt}')
    q=len(re.findall(r'^### Pregunta(?: breve| de integración)?$',text,re.M))
    a=len(re.findall(r'^### Respuesta(?: razonada)?$',text,re.M))
    if q!=a or q<10: bad(f'{name}: questions/answers invalid: {q}/{a}')

contracts={
'M0/ejemplos/HolaMinisterio.java':['public class HolaMinisterio','args.length > 0','bienvenido al Ministerio de Educación'],
'M0/proyecto/pom.xml':['spring-boot-starter-parent','3.5.16','<java.version>17</java.version>','spring-boot-starter-web','spring-boot-devtools','spring-boot-starter-test','spring-boot-maven-plugin'],
'M0/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java':['@SpringBootApplication','SpringApplication.run(MiProyectoApplication.class, args)'],
'M0/proyecto/src/main/java/es/mecd/demo/miproyecto/controller/SaludoController.java':['@RestController','@GetMapping("/hola")','@GetMapping("/adios")','construirMensaje("Ministerio de Educación")'],
'M0/proyecto/src/test/java/es/mecd/demo/miproyecto/controller/SaludoControllerTest.java':['saludarDebeDevolverElMensajeEsperado','despedirDebeDevolverElMensajeEsperado','Hola, Ministerio de Educación','Adiós, Ministerio de Educación'],
'M0/proyecto/.mvn/wrapper/maven-wrapper.properties':['apache-maven-3.9.16-bin.zip']}
for rel,tokens in contracts.items():
    txt=(root/rel).read_text(encoding='utf-8')
    for token in tokens:
        if token not in txt: bad(f'{rel}: code token missing: {token}')
for token in ['public class HolaMinisterio','args.length > 0','@SpringBootApplication','@RestController','@GetMapping("/hola")','@GetMapping("/adios")','saludarDebeDevolverElMensajeEsperado','despedirDebeDevolverElMensajeEsperado']:
    if token not in practice: bad(f'practical guide missing code concept: {token}')
for token in ['./mvnw test','./mvnw package','./mvnw spring-boot:run','mvnw.cmd test']:
    if token not in practice: bad(f'practical guide missing wrapper command: {token}')

if fail:
    print('M0 TRACEABILITY: FAIL')
    for x in fail: print(' -',x)
    raise SystemExit(1)
print(f'M0 TRACEABILITY: PASS | theory concepts={len(m["theory_concepts"])} | practical steps={len(derived)} | classified artifacts={len(m["artifacts"])} | environments={len(m["environment_workflows"])}')
