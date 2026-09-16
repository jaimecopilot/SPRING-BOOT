from pathlib import Path
ROOT=Path(__file__).resolve().parents[2]
p=ROOT/'.course/traceability/TRAZABILIDAD_M6.md'
t=p.read_text(encoding='utf-8')
for needle in ['Dirección directa e inversa','Práctica -> snapshot de código (112/112)','Proyecto -> guía','M6/6.9/proyecto','M6/proyecto','gestor','spring-security-test','HTTP Basic','logout/revocación']:
    assert needle in t, needle
print('M6 HUMAN TRACEABILITY: PASS')
