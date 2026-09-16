import subprocess, sys
scripts=['validate_m6_core.py','validate_m6_snapshots.py','validate_m6_human.py']
from pathlib import Path
HERE=Path(__file__).resolve().parent
for s in scripts:
    subprocess.check_call([sys.executable,str(HERE/s)])
print('M6 TRACEABILITY STATIC SUITE: PASS')
