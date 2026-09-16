from pathlib import Path
import json, argparse, hashlib, os
ap=argparse.ArgumentParser(); ap.add_argument('--require-physical',action='store_true'); a=ap.parse_args()
ROOT=Path(__file__).resolve().parents[2]
d=json.loads((ROOT/'.course/traceability/M6.json').read_text(encoding='utf-8'))
chain=d['invariants']['chain']
assert not (ROOT/'M6/proyecto').exists(), 'M6/proyecto está prohibido'
for i,x in enumerate(chain):
    expected_parent='M5/proyecto' if i==0 else f"M6/{chain[i-1]['point']}/proyecto"
    assert x['parent']==expected_parent,(x,expected_parent)
    assert x['destination']==f"M6/{x['point']}/proyecto"
missing=[]
for x in chain:
    if not (ROOT/x['destination']).exists(): missing.append(x['destination'])
if a.require_physical and missing:
    raise SystemExit('FALTAN SNAPSHOTS FISICOS: '+', '.join(missing))
print('M6 SNAPSHOT CHAIN: PASS (specification)')
if missing: print('Physical snapshots pending:', ', '.join(missing))
else: print('Physical snapshots present: 9/9')
