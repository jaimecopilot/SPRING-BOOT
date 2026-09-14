#!/usr/bin/env python3
from pathlib import Path
import argparse,xml.etree.ElementTree as ET
ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); a=ap.parse_args(); root=Path(a.root).resolve()
reports=root/'M6/proyecto/target/surefire-reports'
files=list(reports.glob('TEST-*.xml'))
assert files,f'No Surefire XML reports in {reports}'
total=fail=err=skip=0
for f in files:
    r=ET.parse(f).getroot(); total+=int(r.attrib.get('tests',0)); fail+=int(r.attrib.get('failures',0)); err+=int(r.attrib.get('errors',0)); skip+=int(r.attrib.get('skipped',0))
assert (total,fail,err,skip)==(58,0,0,0),(total,fail,err,skip)
print(f'M6 TESTS: PASS | tests={total} | failures={fail} | errors={err} | skipped={skip}')
