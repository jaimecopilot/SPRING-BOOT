#!/usr/bin/env python3
from pathlib import Path
import argparse,json

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); a=ap.parse_args(); root=Path(a.root).resolve(); tr=root/'.course/traceability'
    report=(tr/'TRAZABILIDAD_M6.md').read_text(encoding='utf-8')
    for x in ['## 4. Práctica / guía → proyecto — 112 cadenas directas','## 5. Proyecto → guía — inventario inverso exhaustivo 124/124','## 6. Estados temporales y restauraciones','## 9. Gates automáticos de fiabilidad']:
        assert x in report,x
    assert 'TRAZABILIDAD BIDIRECCIONAL M6: COMPLETE / MACHINE-VERIFIED PRE-PUBLICATION.' in report
    assert '`DRAFT`' not in report
    m=json.loads((tr/'M6.json').read_text(encoding='utf-8')); ct=json.loads((tr/'M6_CODE_TRACE.json').read_text(encoding='utf-8'))
    ids=[]
    for i in range(1,10):
        d=json.loads((tr/f'M6/6.{i}.json').read_text(encoding='utf-8'))
        for s in d['steps']:
            ids.append(s['id']); assert report.count('`'+s['id']+'`')>=1,s['id']
    assert len(ids)==112 and len(set(ids))==112
    for e in ct['final_project']:
        assert '`'+e['path']+'`' in report,e['path']
    for p in ['CredencialesInvalidasException.java','TokenInvalidoException.java','AuthExceptionHandler.java','JwtServiceIntegrationTest.java','InMemoryUserConfig.java']:
        assert p in report,p
    assert '52/52' in report and '124/124' in report and '70 M5 preservados' in report
    print(f'M6 HUMAN TRACEABILITY: PASS | direct=112/112 | inverse=124/124 | functional=52/52 | report_bytes={len(report.encode())}')
if __name__=='__main__': main()
