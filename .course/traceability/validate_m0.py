#!/usr/bin/env python3
from pathlib import Path
import json, re, sys

root=Path(sys.argv[1] if len(sys.argv)>1 else '.')
m=json.loads((root/'.course/traceability/M0.json').read_text(encoding='utf-8'))
steps=[]
for rel in m['step_files']:
    steps.extend(json.loads((root/rel).read_text(encoding='utf-8')))
final=json.loads((root/m['artifact_file']).read_text(encoding='utf-8'))
theory=(root/'M0/TEORIA.md').read_text(encoding='utf-8')
practice=(root/'M0/PRACTICA.md').read_text(encoding='utf-8')
fail=[]
def bad(x): fail.append(x)

if m.get('schema_version') != 3: bad(f"schema_version must be 3, got {m.get('schema_version')}")

# Teoría
ids=[c['id'] for c in m['theory_concepts']]
if len(ids)!=len(set(ids)): bad('duplicate theory concept id')
theory_ids=set(ids)
for c in m['theory_concepts']:
    if c['anchor'].lower() not in theory.lower(): bad(f"theory anchor missing: {c['id']} -> {c['anchor']}")

# Deriva IDs reales y headings de la guía.
derived=[]; headings={}
for pblock in re.finditer(r'(?ms)^# Práctica (0\.\d+) - .*?(?=^# Práctica |^# Empaquetado y verificación final del Módulo 0|\Z)',practice):
    p=pblock.group(1); matches=list(re.finditer(r'^## Paso (\d+) - (.+)$',pblock.group(0),re.M)); nums=[int(x.group(1)) for x in matches]
    if nums != list(range(1,13)): bad(f'{p}: steps {nums} != 1..12')
    pn=int(p.split('.')[1])
    for x in matches:
        sid=f'M0-P-{pn:02d}-S{int(x.group(1)):02d}'; derived.append(sid); headings[sid]=f"Paso {int(x.group(1))} - {x.group(2).strip()}"
for x in re.finditer(r'^## Paso final ([ABC]) - (.+)$',practice,re.M):
    sid=f'M0-P-FINAL-{x.group(1)}'; derived.append(sid); headings[sid]=f"Paso final {x.group(1)} - {x.group(2).strip()}"
if len(derived)!=51: bad(f'expected 51 guide steps, got {len(derived)}')
step_ids=[s['id'] for s in steps]
if len(steps)!=51: bad(f'expected 51 step contracts, got {len(steps)}')
if len(step_ids)!=len(set(step_ids)): bad('duplicate step contract id')
if set(step_ids)!=set(derived): bad(f'contract/guide IDs differ: missing={sorted(set(derived)-set(step_ids))}, extra={sorted(set(step_ids)-set(derived))}')

allowed_actions={'CREATE','MODIFY','USE','DELETE','RESTORE'}; allowed_status={'PERMANENT','TEMPORARY','OBSERVATION'}
workflow_ids={w['id'] for w in m['environment_workflows']}
for s in steps:
    sid=s['id']
    if sid in headings and s.get('guide_heading')!=headings[sid]: bad(f'{sid}: heading mismatch')
    if s.get('guide_heading','') not in practice: bad(f'{sid}: heading not found in PRACTICA.md')
    if s.get('status') not in allowed_status: bad(f'{sid}: invalid status {s.get("status")}')
    if not s.get('theory_refs'): bad(f'{sid}: no theory_refs')
    for ref in s.get('theory_refs',[]):
        if ref not in theory_ids: bad(f'{sid}: unknown theory ref {ref}')
    if not s.get('commands'): bad(f'{sid}: commands not explicit')
    if not s.get('observables'): bad(f'{sid}: observables not explicit')
    if not s.get('verification'): bad(f'{sid}: verification not explicit')
    for wid in s.get('environments',[]):
        if wid not in workflow_ids: bad(f'{sid}: unknown environment {wid}')
    temps=[]; restores=[]
    for a in s.get('artifacts',[]):
        if a.get('action') not in allowed_actions: bad(f'{sid}: invalid action {a.get("action")}')
        if not a.get('path'): bad(f'{sid}: artifact without path'); continue
        p=root/a['path']
        if not p.exists(): bad(f'{sid}: final path missing {a["path"]}')
        if a.get('state')=='temporary': temps.append(a)
        if a.get('action')=='RESTORE': restores.append(a)
        if a.get('action') in {'CREATE','MODIFY','RESTORE'} and not a.get('symbols'): bad(f'{sid}: {a["action"]} lacks symbols for {a["path"]}')
    if s.get('status')=='TEMPORARY':
        if not temps: bad(f'{sid}: TEMPORARY without temporary action')
        if not restores: bad(f'{sid}: TEMPORARY without RESTORE')
        if s.get('restores')!=sid: bad(f'{sid}: M0 temporary experiment must restore inside same step')
    elif temps: bad(f'{sid}: temporary action in non-TEMPORARY step')

if workflow_ids!={'M0-W-CONSOLE','M0-W-INTELLIJ','M0-W-ECLIPSE','M0-W-VSCODE'}: bad(f'unexpected workflows: {workflow_ids}')
for w in m['environment_workflows']:
    if w['anchor'] not in practice: bad(f"workflow anchor missing: {w['id']}")

# Proyecto → guía / soporte
paths=[a['path'] for a in final]
if len(paths)!=len(set(paths)): bad('duplicate final artifact path')
classified={a['path']:a for a in final}
step_set=set(step_ids)
for a in final:
    p=root/a['path']
    if not p.exists(): bad(f'missing classified artifact: {a["path"]}')
    cls=a.get('classification')
    if cls not in {'GUIDE','INHERITED','SUPPORT'}: bad(f'invalid classification {cls}: {a["path"]}')
    if cls=='SUPPORT':
        for k in ('reason','used_by','missing_effect'):
            if not a.get(k): bad(f'SUPPORT lacks {k}: {a["path"]}')
    if cls=='GUIDE' and a.get('origin','').startswith('M0-P-'):
        if a['origin'] not in step_set: bad(f'GUIDE origin invalid: {a["path"]} -> {a["origin"]}')
        else:
            st=next(s for s in steps if s['id']==a['origin'])
            if not any(x['path']==a['path'] and x['action']=='CREATE' for x in st.get('artifacts',[])): bad(f'GUIDE origin does not CREATE artifact: {a["path"]}')
    for evo in a.get('evolution',[]):
        ref=evo.split(' ',1)[0]
        if ref.startswith('M0-P-') and ref not in step_set: bad(f'unknown evolution {ref}: {a["path"]}')

functional=[]
for p in (root/'M0').rglob('*'):
    if p.is_file() and (p.suffix in {'.java','.xml','.properties','.cmd'} or p.name=='mvnw'): functional.append(p.relative_to(root).as_posix())
for rel in functional:
    if rel not in classified: bad(f'unclassified functional artifact: {rel}')
for s in steps:
    for a in s.get('artifacts',[]):
        p=root/a['path']
        if p.is_file() and a['path'] not in classified: bad(f'{s["id"]}: step file absent from final inventory: {a["path"]}')

# Las guías del alumno no muestran infraestructura interna.
for name,text in [('TEORIA',theory),('PRACTICA',practice)]:
    for patt in [r'(?i)\bcheckpoint\b',r'(?i)fidelidad didáctica',r'(?i)GUIDE/INHERITED/SUPPORT',r'(?i)\.course/traceability']:
        if re.search(patt,text): bad(f'{name}: internal meta visible: {patt}')
    q=len(re.findall(r'^### Pregunta(?: breve| de integración)?$',text,re.M)); a=len(re.findall(r'^### Respuesta(?: razonada)?$',text,re.M))
    if q!=a or q<10: bad(f'{name}: questions/answers invalid: {q}/{a}')

# Contratos de alto valor del snapshot final.
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
        if token not in txt: bad(f'{rel}: token missing: {token}')
for token in ['public class HolaMinisterio','args.length > 0','@SpringBootApplication','@RestController','@GetMapping("/hola")','@GetMapping("/adios")','saludarDebeDevolverElMensajeEsperado','despedirDebeDevolverElMensajeEsperado']:
    if token not in practice: bad(f'PRACTICA missing code concept: {token}')
for token in ['./mvnw test','./mvnw package','./mvnw spring-boot:run','mvnw.cmd test']:
    if token not in practice: bad(f'PRACTICA missing Wrapper command: {token}')

if fail:
    print('M0 STRONG TRACEABILITY: FAIL')
    for x in fail: print(' -',x)
    raise SystemExit(1)
print(f'M0 STRONG TRACEABILITY: PASS | theory={len(ids)} | steps={len(steps)} | temporary={sum(s["status"]=="TEMPORARY" for s in steps)} | artifacts={len(final)} | environments={len(workflow_ids)}')
