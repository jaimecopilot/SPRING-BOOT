#!/usr/bin/env python3
from pathlib import Path
import copy,json,shutil,sys,tempfile
HERE=Path(__file__).resolve().parent
sys.path.insert(0,str(HERE))
from validate_m5_core import validate
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()

# Each controlled corruption must be rejected by the core validator.
def edit_json(path,func):
    d=json.loads(path.read_text(encoding='utf-8')); func(d); path.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')

def m_schema(r): edit_json(r/'.course/traceability/M5.json',lambda m:m.__setitem__('schema_version',2))
def m_step(r):
    p=r/'.course/traceability/M5/5.1.json'; d=json.loads(p.read_text(encoding='utf-8')); d['steps']=d['steps'][1:]; p.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
def m_concept(r): edit_json(r/'.course/traceability/M5.json',lambda m:m['theory_concepts'].pop())
def m_continuity(r): edit_json(r/'.course/traceability/M5.json',lambda m:m['continuity_from_m4'].pop())
def m_temp(r):
    p=r/'.course/traceability/M5/5.5.json'; d=json.loads(p.read_text(encoding='utf-8'))
    d['steps'][-1]['artifacts']=[a for a in d['steps'][-1]['artifacts'] if a['action']!='RESTORE']
    p.write_text(json.dumps(d,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
def m_artifact(r):
    p=r/'M5/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java'; p.unlink()

tests=[('schema',m_schema),('step',m_step),('concept',m_concept),('continuity',m_continuity),('temporary-close',m_temp),('artifact',m_artifact)]
rejected=0
for label,fn in tests:
    with tempfile.TemporaryDirectory(prefix='m5-mutation-') as td:
        tr=Path(td)/'repo'
        # copy only relevant trees; symlinks disabled
        tr.mkdir()
        for name in ['M4','M5','.course']:
            src=root/name
            if src.exists(): shutil.copytree(src,tr/name)
        fn(tr)
        fail,_,_=validate(tr)
        if fail: rejected+=1
        else:
            print('M5 MUTATION: corruption NOT rejected:',label)
if rejected!=len(tests):
    print(f'M5 MUTATION GATE: FAIL | rejected={rejected}/{len(tests)}')
    raise SystemExit(1)
print(f'M5 MUTATION GATE: PASS | rejected={rejected}/{len(tests)} controlled corruptions')
