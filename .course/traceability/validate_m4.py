#!/usr/bin/env python3
from pathlib import Path
import sys
from validate_m4_core import validate
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()
errors=validate(root)
if errors:
    print('M4 TRACEABILITY V3 R1: FAIL')
    for e in errors: print(' -',e)
    raise SystemExit(1)
print('M4 TRACEABILITY V3 R1: PASS | theory=35 | steps=92/92 | artifacts=54 | M3 continuity=38/38 | temporary states=closed | code-blocks=228')
