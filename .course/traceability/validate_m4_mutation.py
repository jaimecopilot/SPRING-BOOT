#!/usr/bin/env python3
from copy import deepcopy
from pathlib import Path
import json, sys
from validate_m4_core import validate
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()
base=json.loads((root/'.course/traceability/M4.json').read_text(encoding='utf-8'))
p41=json.loads((root/'.course/traceability/M4/4.1.json').read_text(encoding='utf-8'))
mutants=[]
# 1 schema contract
x=deepcopy(base); x.pop('schema_version',None); mutants.append(('missing schema_version',{'.course/traceability/M4.json':x}))
# 2 theory relation
x=deepcopy(p41); x['steps'][0]['theory_refs']=[]; mutants.append(('step without theory',{'.course/traceability/M4/4.1.json':x}))
# 3 reverse inventory
x=deepcopy(base); x['artifacts']=x['artifacts'][1:]; mutants.append(('missing artifact inventory row',{'.course/traceability/M4.json':x}))
# 4 cumulative continuity
x=deepcopy(base); x['continuity_from_m3']=x['continuity_from_m3'][1:]; mutants.append(('missing M3 continuity row',{'.course/traceability/M4.json':x}))
# 5 temporary closure
x=deepcopy(p41); step=x['steps'][10]; step['artifacts']=[a for a in step['artifacts'] if a['action']!='RESTORE']; mutants.append(('temporary step without RESTORE',{'.course/traceability/M4/4.1.json':x}))
# 6 invalid symbol
x=deepcopy(p41); x['steps'][0]['artifacts'][0]['symbols']=['__SYMBOL_THAT_DOES_NOT_EXIST__']; mutants.append(('missing permanent symbol',{'.course/traceability/M4/4.1.json':x}))
fail=[]
for name,ov in mutants:
    errors=validate(root,ov)
    if not errors: fail.append(name)
if fail:
    print('M4 MUTATION GATE: FAIL - validator accepted mutants: '+', '.join(fail))
    raise SystemExit(1)
print(f'M4 MUTATION GATE: PASS | rejected={len(mutants)}/{len(mutants)} controlled corruptions')
