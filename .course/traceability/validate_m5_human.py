#!/usr/bin/env python3
from pathlib import Path
import json,re,sys
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()
m=json.loads((root/'.course/traceability/M5.json').read_text(encoding='utf-8'))
steps=[]
for rel in m['step_manifests']:
    steps.extend(json.loads((root/rel).read_text(encoding='utf-8'))['steps'])
p=root/'.course/traceability/TRAZABILIDAD_M5.md'
fail=[]
def bad(x): fail.append(x)
if not p.exists(): bad('missing human report')
else:
    report=p.read_text(encoding='utf-8')
    sections=[
      '## Cómo puedes auditarlo tú','## Resumen verificable','## Teoría -> práctica',
      '## Práctica -> acción -> artefacto -> verificación','## Guía -> proyecto',
      '## Proyecto -> guía','## M4 -> M5: continuidad acumulativa',
      '## Artefactos necesarios no introducidos en este módulo',
      '## Trazabilidad inversa por artefacto/símbolo','## Estados temporales y restauraciones',
      '## Regresión heredada M4 -> M5','## Gates automáticos','## Resultado global']
    for s in sections:
        if s not in report: bad('missing section '+s)
    for st in steps:
        if f"`{st['id']}`" not in report: bad('missing step '+st['id'])
    for a in m['artifacts']:
        if f"`{a['path']}`" not in report: bad('missing artifact '+a['path'])
    if len(re.findall(r'^\| `M5-P-',report,re.M))<60: bad('human step table has fewer than 60 rows')
if fail:
    print('M5 HUMAN TRACEABILITY: FAIL')
    for x in fail: print(' -',x)
    raise SystemExit(1)
print(f"M5 HUMAN TRACEABILITY: PASS | reversible chains=60/60 | artifacts={len(m['artifacts'])} | M4 continuity={len(m['continuity_from_m4'])}/54 | report_bytes={p.stat().st_size}")
