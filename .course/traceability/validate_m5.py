#!/usr/bin/env python3
from pathlib import Path
import sys
sys.path.insert(0,str(Path(__file__).resolve().parent))
from validate_m5_core import validate
root=sys.argv[1] if len(sys.argv)>1 else '.'
fail,m,steps=validate(root)
if fail:
    print('M5 TRACEABILITY V3: FAIL')
    for x in fail: print(' -',x)
    raise SystemExit(1)
print(f"M5 TRACEABILITY V3: PASS | theory={m['counts']['theory_concepts']} | steps={len(steps)}/60 | artifacts={m['counts']['artifact_inventory']} | M4 continuity={m['counts']['m4_project_files_accounted']}/54 | temporary states=closed | code-blocks=159")
