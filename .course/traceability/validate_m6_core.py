from pathlib import Path
import json, re, sys
ROOT=Path(__file__).resolve().parents[2]
T=ROOT/'M6'/'TEORIA.md'; P=ROOT/'M6'/'PRACTICA.md'
J=ROOT/'.course'/'traceability'/'M6.json'
assert T.exists() and P.exists() and J.exists()
d=json.loads(J.read_text(encoding='utf-8'))
assert d['source']['theory_blocks']==45
assert d['source']['theory_subpoints']==135
assert d['source']['practice_steps']==112
assert len(d['theory_concepts'])==45
assert len(d['steps'])==112
counts={}
for s in d['steps']: counts[s['point']]=counts.get(s['point'],0)+1
assert counts=={'6.1':12,'6.2':12,'6.3':14,'6.4':12,'6.5':12,'6.6':13,'6.7':12,'6.8':12,'6.9':13}, counts
assert d['invariants']['forward_only'] is True
assert d['invariants']['forbidden_parallel_final']=='M6/proyecto'
assert d['invariants']['official_final']=='M6/6.9/proyecto'
assert not (ROOT/'M6'/'proyecto').exists(), 'M6/proyecto paralelo prohibido'
# Current docs structural gates.
txt=T.read_text(encoding='utf-8'); ptxt=P.read_text(encoding='utf-8')
assert len(re.findall(r'^# Punto 6\.[1-9] - ',txt,re.M))==9
assert len(re.findall(r'^## Bloque [1-5] - ',txt,re.M))==45
assert len(re.findall(r'^### T\d\.\d - ',txt,re.M))==135
assert len(re.findall(r'^# Práctica 6\.[1-9] - ',ptxt,re.M))==9
assert len(re.findall(r'^## Paso \d+ - ',ptxt,re.M))==112
print('M6 CORE TRACEABILITY: PASS | 45 theory blocks | 135 subpoints | 112 practice steps | forward-only snapshots')
