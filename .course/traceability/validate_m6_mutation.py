import json, tempfile, copy
from pathlib import Path
ROOT=Path(__file__).resolve().parents[2]
d=json.loads((ROOT/'.course/traceability/M6.json').read_text(encoding='utf-8'))
def ok(x):
    c=x['invariants']['chain']
    if x['invariants'].get('forbidden_parallel_final')!='M6/proyecto': return False
    if x['invariants'].get('official_final')!='M6/6.9/proyecto': return False
    for i,e in enumerate(c):
        parent='M5/proyecto' if i==0 else f"M6/{c[i-1]['point']}/proyecto"
        if e['parent']!=parent or e['destination']!=f"M6/{e['point']}/proyecto": return False
    return True
assert ok(d)
for mutation in ('bad_parent','bad_final','parallel_final'):
    x=copy.deepcopy(d)
    if mutation=='bad_parent': x['invariants']['chain'][4]['parent']='M6/6.9/proyecto'
    elif mutation=='bad_final': x['invariants']['official_final']='M6/proyecto'
    else: x['invariants']['forbidden_parallel_final']='ALLOW'
    assert not ok(x), mutation
print('M6 MUTATION TRACEABILITY: PASS | corrupted snapshot invariants rejected')
