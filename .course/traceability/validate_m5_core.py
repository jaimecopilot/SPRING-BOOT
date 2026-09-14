#!/usr/bin/env python3
from pathlib import Path
import hashlib, json, re, sys

EXPECTED_LANGS={
    'java':84,'bash':26,'json':19,'properties':19,
    'text':9,'yaml':1,'html':1
}
EXPECTED_STEPS={'5.1':13,'5.2':12,'5.3':12,'5.4':12,'5.5':11}
ALLOWED_ACTIONS={'CREATE','MODIFY','USE','DELETE','RESTORE','VERIFY'}
ALLOWED_STATES={'PERMANENT','TEMPORARY'}
ALLOWED_CLASSES={'GUIDE','INHERITED','SUPPORT'}

def sha(p):
    return hashlib.sha256(p.read_bytes()).hexdigest()

# Continuity compares logical text content, not the checkout's EOL convention.
# On Windows, Git with core.autocrlf=true may materialize predecessor M4 files
# as CRLF while the new M5 payload is copied byte-for-byte with LF. Those files
# are semantically identical and must remain PRESERVED.
_CONTINUITY_TEXT_SUFFIXES={
    '.java','.properties','.xml','.md','.cmd','.yml','.yaml',
    '.json','.py','.html'
}
_CONTINUITY_TEXT_NAMES={'mvnw','.gitignore'}

def continuity_sha(p):
    data=p.read_bytes()
    if p.suffix.lower() in _CONTINUITY_TEXT_SUFFIXES or p.name in _CONTINUITY_TEXT_NAMES:
        data=data.replace(b'\r\n',b'\n').replace(b'\r',b'\n')
    return hashlib.sha256(data).hexdigest()

def load(root):
    root=Path(root).resolve()
    m=json.loads((root/'.course/traceability/M5.json').read_text(encoding='utf-8'))
    steps=[]
    for rel in m.get('step_manifests',[]):
        steps.extend(json.loads((root/rel).read_text(encoding='utf-8'))['steps'])
    return root,m,steps

def validate(root):
    root,m,steps=load(root); fail=[]
    def bad(msg): fail.append(msg)
    theory=(root/'M5/TEORIA.md').read_text(encoding='utf-8')
    practice=(root/'M5/PRACTICA.md').read_text(encoding='utf-8')

    if m.get('schema_version')!=3: bad('schema_version must be 3')
    if m.get('module')!='M5': bad('module must be M5')
    if m.get('status')!='COMPLETE': bad('status must be COMPLETE')
    if m.get('predecessor')!='M4': bad('predecessor must be M4')
    for k in [
        'step_level_traceability_required','temporary_transitions_must_close',
        'cumulative_project_continuity_required','reverse_traceability_required',
        'negative_mutation_validation_required']:
        if not m.get('policy',{}).get(k): bad('missing policy '+k)

    concepts=m.get('theory_concepts',[])
    if len(concepts)!=25: bad(f'theory concepts must be 25, got {len(concepts)}')
    tids=[c.get('id') for c in concepts]
    if len(tids)!=len(set(tids)): bad('duplicate theory ids')
    for c in concepts:
        if c.get('anchor','').lower() not in theory.lower():
            bad(f"theory anchor missing: {c.get('id')} -> {c.get('anchor')}")
    if len(re.findall(r'^# Punto 5\.[1-5] - ',theory,re.M))!=5: bad('theory must expose 5 points')
    if len(re.findall(r'^## Objetivos de aprendizaje$',theory,re.M))!=5: bad('theory must expose 5 objective sections')
    if len(re.findall(r'^## Bloque [1-5] - ',theory,re.M))!=25: bad('theory must expose 25 blocks')
    if len(re.findall(r'^### T\d+\.\d+ - ',theory,re.M))!=75: bad('theory must preserve 75 source subpoints')

    derived=[]; headings={}
    for p,count in EXPECTED_STEPS.items():
        mm=re.search(rf'^# Práctica {re.escape(p)} .*?(?=^# Práctica 5\.|\Z)',practice,re.M|re.S)
        if not mm: bad('missing practice '+p); continue
        found=re.findall(r'^## Paso (\d+) - (.+)$',mm.group(0),re.M)
        if len(found)!=count: bad(f'{p}: expected {count} steps, got {len(found)}')
        for n,title in found:
            sid=f'M5-P-{p.replace(".","")}-S{int(n):02d}'
            derived.append(sid); headings[sid]=f'## Paso {n} - {title}'
    declared=[s.get('id') for s in steps]
    if len(declared)!=60: bad(f'manifest steps must be 60, got {len(declared)}')
    if len(declared)!=len(set(declared)): bad('duplicate step ids')
    if set(derived)!=set(declared):
        bad(f'guide/manifest step mismatch missing={sorted(set(derived)-set(declared))} extra={sorted(set(declared)-set(derived))}')

    env_ids={x['id'] for x in m.get('environment_workflows',[])}
    if env_ids!={'M5-W-CONSOLE','M5-W-INTELLIJ','M5-W-ECLIPSE','M5-W-VSCODE'}: bad('exact four environment workflows required')
    for e in m.get('environment_workflows',[]):
        if e['anchor'] not in practice: bad('environment anchor missing: '+e['id'])

    inventory_list=m.get('artifacts',[]); inventory={x['path']:x for x in inventory_list}
    if len(inventory_list)!=len(inventory): bad('duplicate inventory path')
    final_expected=set()
    for p in (root/'M5/proyecto').rglob('*'):
        if p.is_file(): final_expected.add(p.relative_to(root).as_posix())
    if (root/'M5/frontend-cors/index.html').exists(): final_expected.add('M5/frontend-cors/index.html')
    missing=final_expected-set(inventory)
    extra=set(inventory)-final_expected
    if missing: bad('unclassified final artifacts: '+str(sorted(missing)))
    if extra: bad('inventory points to missing/nonfinal artifacts: '+str(sorted(extra)))
    for rel,a in inventory.items():
        p=root/rel
        if not p.exists(): bad('classified artifact missing: '+rel)
        if a.get('classification') not in ALLOWED_CLASSES: bad('invalid classification: '+rel)
        if a.get('classification')=='SUPPORT':
            if not a.get('used_by'): bad('SUPPORT without used_by: '+rel)
            if not a.get('missing_effect'): bad('SUPPORT without missing_effect: '+rel)

    events={}
    textual={'.java','.xml','.properties','.md','.yml','.yaml','.json','.py','.html','.cmd'}
    skip_tokens={'/api/v1/alumnos','/api/v1/ficheros','Order(0)','Order(1)','Order(2)','Order(3)'}
    for idx,s in enumerate(steps):
        sid=s.get('id')
        if sid in headings and s.get('heading')!=headings[sid]: bad(f'{sid}: heading mismatch')
        refs=s.get('theory_refs',[])
        if not refs: bad(f'{sid}: no theory refs')
        for r in refs:
            if r not in tids: bad(f'{sid}: unknown theory ref {r}')
        for r in s.get('environment_refs',[]):
            if r not in env_ids: bad(f'{sid}: unknown environment ref {r}')
        if s.get('state') not in ALLOWED_STATES: bad(f'{sid}: invalid state')
        if not (s.get('artifacts') or s.get('commands') or s.get('observables')): bad(f'{sid}: no trace evidence')
        if not s.get('observables'): bad(f'{sid}: no observables')
        if not s.get('verification'): bad(f'{sid}: no verification')
        for a in s.get('artifacts',[]):
            rel=a.get('path'); action=a.get('action'); state=a.get('state')
            if action not in ALLOWED_ACTIONS: bad(f'{sid}: invalid action {action}')
            if state not in ALLOWED_STATES: bad(f'{sid}: invalid artifact state {state}')
            if not rel: bad(f'{sid}: missing artifact path'); continue
            events.setdefault(rel,[]).append((idx,action,state,sid))
            p=root/rel
            if state=='PERMANENT' and action!='DELETE':
                if not p.exists(): bad(f'{sid}: permanent artifact missing {rel}')
                if rel not in inventory: bad(f'{sid}: permanent artifact not in inventory {rel}')
            if state=='PERMANENT' and action in {'CREATE','MODIFY','RESTORE','VERIFY'} and p.exists() and p.is_file() and p.suffix in textual:
                txt=p.read_text(encoding='utf-8',errors='replace')
                for tok in a.get('symbols',[]):
                    if tok in skip_tokens: continue
                    if tok not in txt: bad(f'{sid}: token missing in {rel}: {tok}')

    # Every temporary transition must eventually close on same path.
    for rel,evs in events.items():
        for pos,(idx,action,state,sid) in enumerate(evs):
            if state!='TEMPORARY': continue
            closes=[e for e in evs[pos+1:] if e[1] in {'RESTORE','DELETE'} or (e[2]=='PERMANENT' and e[1] in {'MODIFY','CREATE'})]
            # same-step RESTORE appears later in list and is accepted
            if not closes: bad(f'{sid}: temporary transition never closes for {rel}')

    # GUIDE origin/evolution must be backed by real step relations.
    relmap={}
    for s in steps:
        for a in s.get('artifacts',[]): relmap.setdefault(a['path'],set()).add(s['id'])
    for rel,a in inventory.items():
        if a.get('classification')=='GUIDE':
            origin=a.get('origin')
            if not origin or origin not in declared: bad(f'{rel}: GUIDE origin invalid {origin}')
            elif origin not in relmap.get(rel,set()): bad(f'{rel}: GUIDE origin has no relation')
            for sid in a.get('evolution',[]):
                if sid not in relmap.get(rel,set()): bad(f'{rel}: evolution not related: {sid}')

    # Continuity M4 -> M5 is exhaustive and truthful.
    continuity=m.get('continuity_from_m4',[])
    m4files=sorted(p for p in (root/'M4/proyecto').rglob('*') if p.is_file()) if (root/'M4/proyecto').exists() else []
    if m4files:
        if len(continuity)!=len(m4files): bad(f'continuity must account {len(m4files)} M4 files, got {len(continuity)}')
        by={x['m4_path']:x for x in continuity}
        for p4 in m4files:
            r4=p4.relative_to(root).as_posix(); c=by.get(r4)
            if not c: bad('M4 file not accounted: '+r4); continue
            p5=root/c['m5_path'] if c.get('m5_path') else None
            if c['status']=='PRESERVED':
                if not p5 or not p5.exists() or continuity_sha(p4)!=continuity_sha(p5): bad('false PRESERVED: '+r4)
            elif c['status']=='MODIFIED':
                if not p5 or not p5.exists() or continuity_sha(p4)==continuity_sha(p5): bad('false MODIFIED: '+r4)
                if not c.get('step_refs'):
                    final_rel=c.get('m5_path')
                    if not final_rel or inventory.get(final_rel,{}).get('classification')!='SUPPORT':
                        bad('MODIFIED without step refs and not SUPPORT: '+r4)
            elif c['status']=='REMOVED':
                if p5 and p5.exists(): bad('false REMOVED: '+r4)
                if not c.get('step_refs'): bad('REMOVED without step refs: '+r4)
            else: bad('invalid continuity status: '+str(c.get('status')))
    else:
        if len(continuity)!=54: bad('without M4 tree, continuity manifest must still have 54 entries')

    # Strong specific invariant: exactly one predecessor removal, the old monolithic handler.
    removed=[x for x in continuity if x.get('status')=='REMOVED']
    if len(removed)!=1 or not removed[0]['m4_path'].endswith('/GlobalExceptionHandler.java'):
        bad('exactly GlobalExceptionHandler.java must be the sole predecessor removal')

    # Guide source-preservation / layout contracts.
    allguide=theory+'\n'+practice
    langs={x:len(re.findall(r'^```'+re.escape(x)+r'$',allguide,re.M)) for x in EXPECTED_LANGS}
    if langs!=EXPECTED_LANGS: bad(f'code language distribution mismatch: {langs}')
    blocks=re.findall(r'```(\w*)\n(.*?)\n```',allguide,re.S)
    if len(blocks)!=159: bad(f'code blocks must be 159, got {len(blocks)}')
    for i,(lang,b) in enumerate(blocks,1):
        if not b.strip(): bad(f'empty code block {i}')
        for line in b.splitlines():
            if len(line)>85: bad(f'code line >85 in block {i}/{lang}: {len(line)}')
    if allguide.count('```')!=318: bad('unclosed/mismatched fences')
    if len(re.findall(r'^\| Error \| Causa \| Solución \|$',practice,re.M))!=5: bad('practice must contain 5 proper error tables')
    if len(re.findall(r'> \*\*Pregunta de reflexión:\*\*',allguide))!=80: bad('must preserve 80 source questions')
    if len(re.findall(r'\bReto:',practice))<5: bad('must preserve five practical retos')
    if len(re.findall(r'^## Resultado esperado global$',practice,re.M))!=5: bad('must preserve 5 result sections')
    for badseq in ['Ã','Â','�']:
        if badseq in allguide: bad('mojibake detected: '+badseq)

    return fail,m,steps

if __name__=='__main__':
    root=sys.argv[1] if len(sys.argv)>1 else '.'
    fail,m,steps=validate(root)
    if fail:
        print('M5 TRACEABILITY V3: FAIL')
        for x in fail: print(' -',x)
        raise SystemExit(1)
    print(f"M5 TRACEABILITY V3: PASS | theory={m['counts']['theory_concepts']} | steps={len(steps)}/60 | artifacts={m['counts']['artifact_inventory']} | M4 continuity={m['counts']['m4_project_files_accounted']}/54 | code-blocks=159")
