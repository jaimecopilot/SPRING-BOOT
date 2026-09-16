from pathlib import Path
import subprocess, os, sys, argparse
ap=argparse.ArgumentParser(); ap.add_argument('--final-only',action='store_true'); a=ap.parse_args()
ROOT=Path(__file__).resolve().parents[2]
points=['6.1','6.2','6.3','6.4','6.5','6.6','6.7','6.8','6.9']
if a.final_only: points=['6.9']
for pt in points:
    proj=ROOT/'M6'/pt/'proyecto'
    if not proj.exists(): raise SystemExit(f'Falta snapshot {proj}')
    cmd=[str(proj/'mvnw.cmd'),'-B','test'] if os.name=='nt' else ['bash',str(proj/'mvnw'),'-B','test']
    subprocess.check_call(cmd,cwd=proj,shell=False)
final=ROOT/'M6'/'6.9'/'proyecto'
cmd=[str(final/'mvnw.cmd'),'-B','clean','verify'] if os.name=='nt' else ['bash',str(final/'mvnw'),'-B','clean','verify']
subprocess.check_call(cmd,cwd=final,shell=False)
print('M6 MAVEN SNAPSHOT/FINAL: PASS')
