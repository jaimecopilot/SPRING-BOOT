#!/usr/bin/env python3
from pathlib import Path
import json, sys
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()
m=json.loads((root/'.course/traceability/M4.json').read_text(encoding='utf-8'))
report=(root/'.course/traceability/TRAZABILIDAD_M4.md').read_text(encoding='utf-8')
fail=[]
def check(c,msg):
    if not c: fail.append(msg)
required_sections=[
'## Cómo puedes auditarlo tú','## Resumen verificable','## Teoría -> práctica',
'## Práctica -> acción -> artefacto -> verificación','## Guía -> proyecto','## Proyecto -> guía',
'## M3 -> M4: continuidad acumulativa','## Artefactos necesarios no introducidos en este módulo',
'## Trazabilidad inversa por artefacto/símbolo','## Estados temporales y restauraciones',
'## Regresión heredada M3 -> M4','## Gates automáticos','## Resultado global']
for s in required_sections: check(s in report,f'human report missing section: {s}')
steps=[]
for rel in m['step_manifests']:
    steps += json.loads((root/rel).read_text(encoding='utf-8'))['steps']
check(len(steps)==92,'human validator expects 92 machine steps')
for s in steps:
    check(f"`{s['id']}`" in report,f"human report missing step {s['id']}")
for c in m['theory_concepts']:
    check(f"`{c['id']}`" in report,f"human report missing theory concept {c['id']}")
for a in m['artifacts']:
    check(f"`{a['path']}`" in report,f"human report missing artifact {a['path']}")
for c in m['continuity_from_m3']:
    check(f"`{c['m3_path']}`" in report and f"`{c['m4_path']}`" in report,
          f"human report missing continuity row {c['m3_path']}")
check('M3 project artifacts disappeared' not in report,'unexpected failure prose in human report')
check(len(report.encode('utf-8'))>70_000,'human report suspiciously small (<70 KB)')
if fail:
    print('M4 HUMAN TRACEABILITY R1: FAIL')
    for e in fail: print(' -',e)
    raise SystemExit(1)
print(f"M4 HUMAN TRACEABILITY R1: PASS | reversible chains={len(steps)}/92 | artifacts={len(m['artifacts'])} | M3 continuity={len(m['continuity_from_m3'])}/38 | report_bytes={len(report.encode('utf-8'))}")
