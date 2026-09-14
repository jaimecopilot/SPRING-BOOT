#!/usr/bin/env python3
from pathlib import Path
import argparse, json, shutil, subprocess, sys, tempfile

def run_all(root: Path) -> bool:
    core=root/'.course/traceability/validate_m6_core.py'; bi=root/'.course/traceability/validate_m6_bidirectional.py'
    cmd=[sys.executable,str(core),str(root)]
    if not (root/'M5/proyecto').is_dir(): cmd.append('--skip-continuity')
    if subprocess.run(cmd,stdout=subprocess.DEVNULL,stderr=subprocess.STDOUT).returncode: return False
    return subprocess.run([sys.executable,str(bi),str(root)],stdout=subprocess.DEVNULL,stderr=subprocess.STDOUT).returncode==0

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); a=ap.parse_args(); src=Path(a.root).resolve()
    with tempfile.TemporaryDirectory(prefix='m6_mutation_') as td:
        work=Path(td)/'repo'; shutil.copytree(src,work,ignore=shutil.ignore_patterns('target','.git'))
        cases=[]
        def text(rel,fn):
            p=work/rel; old=p.read_bytes(); p.write_text(fn(old.decode('utf-8')),encoding='utf-8'); return p,old
        def restore(p,old): p.parent.mkdir(parents=True,exist_ok=True); p.write_bytes(old)
        # Existing contract/security/fidelity mutations.
        p=work/'.course/traceability/M6/6.9.json'; old=p.read_bytes(); d=json.loads(old.decode()); d['steps'].pop(); p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('missing_step',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java',lambda s:s.replace('SessionCreationPolicy.STATELESS','SessionCreationPolicy.IF_REQUIRED')); cases.append(('stateful_session',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/pom.xml',lambda s:s.replace('<jjwt.version>0.13.0</jjwt.version>','<jjwt.version>0.12.6</jjwt.version>')); cases.append(('jjwt_downgrade',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java',lambda s:s.replace('.signWith(clave, Jwts.SIG.HS256)','.signWith(clave)')); cases.append(('hs256_implicit',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/src/main/resources/application-prod.properties',lambda s:s.replace('jwt.secret=${JWT_SECRET}','jwt.secret=${JWT_SECRET:ZmFrZS1mYWxsYmFjay1zZWNyZXQ=}')); cases.append(('prod_secret_fallback',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/src/main/resources/application-test.properties',lambda s:s.replace('app.version=5.0.0','app.version=6.0.0')); cases.append(('untraced_app_version_bump',not run_all(work))); restore(p,old)
        p,old=text('.course/traceability/validate_m6_runtime.py',lambda s:s.replace("b['username']=='ana'","b['usuario']=='ana'")); cases.append(('runtime_profile_contract_drift',not run_all(work))); restore(p,old)
        p,old=text('M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java',lambda s:s.replace('return http.build();','http.httpBasic(Customizer.withDefaults());\n        return http.build();')); cases.append(('http_basic',not run_all(work))); restore(p,old)
        p=work/'M6/M6_SOURCE_FIDELITY.json'; old=p.read_bytes(); d=json.loads(old.decode()); d['practice_steps'][0]['guide_section_sha256']='0'*64; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('fidelity_hash',not run_all(work))); restore(p,old)
        p=work/'M6/TEORIA.md'; old=p.read_bytes(); s=old.decode(); a=s.index('### 6.1-T1.1'); b=s.index('### 6.1-T1.2'); p.write_text(s[:a]+s[b:],encoding='utf-8'); cases.append(('theory_truncated',not run_all(work))); restore(p,old)
        p=work/'.course/traceability/M6/6.1.json'; old=p.read_bytes(); d=json.loads(old.decode()); d['status']='DRAFT_TRACED'; d['steps'][0]['status']='DRAFT'; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('draft',not run_all(work))); restore(p,old)
        p=work/'.course/traceability/M6.json'; old=p.read_bytes(); d=json.loads(old.decode()); d['continuity_from_m5']['entries'].pop(); d['continuity_from_m5']['accounted']=78; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('continuity_78',not run_all(work))); restore(p,old)
        q=work/'.github/workflows/validar-m6.yml'; q.parent.mkdir(parents=True,exist_ok=True); q.write_text('name: forbidden\n',encoding='utf-8'); cases.append(('github_actions',not run_all(work))); q.unlink()
        # New bidirectional mutations.
        p=work/'.course/traceability/M6/6.6.json'; old=p.read_bytes(); d=json.loads(old.decode()); s=next(x for x in d['steps'] if x['id']=='6.6.12'); s['artifact_relations']=[r for r in s['artifact_relations'] if not r['path'].endswith('AuthExceptionHandler.java')]; s['artifacts']=[x for x in s['artifacts'] if not x.endswith('AuthExceptionHandler.java')]; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('direct_edge_removed',not run_all(work))); restore(p,old)
        p=work/'.course/traceability/M6_CODE_TRACE.json'; old=p.read_bytes(); d=json.loads(old.decode()); e=next(x for x in d['final_project'] if x['path'].endswith('TokenInvalidoException.java')); e['final_sha256']='0'*64; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('inverse_hash_corrupt',not run_all(work))); restore(p,old)
        p=work/'.course/traceability/M6_CODE_TRACE.json'; old=p.read_bytes(); d=json.loads(old.decode()); e=next(x for x in d['final_project'] if x['path'].endswith('JwtServiceIntegrationTest.java')); e['step_refs']=[]; e['relations']=[]; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('inverse_edge_removed',not run_all(work))); restore(p,old)
        q=work/'M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UntracedFunctional.java'; q.write_text('package es.mecd.demo.miproyecto.auth; public class UntracedFunctional {}\n',encoding='utf-8'); cases.append(('untraced_functional_file',not run_all(work))); q.unlink()
        p=work/'.course/traceability/M6/6.3.json'; old=p.read_bytes(); d=json.loads(old.decode()); s=next(x for x in d['steps'] if x['id']=='6.3.6'); next(r for r in s['artifact_relations'] if r['path'].endswith('InMemoryUserConfig.java'))['action']='MODIFY'; p.write_text(json.dumps(d,ensure_ascii=False,indent=2),encoding='utf-8'); cases.append(('transient_not_deleted',not run_all(work))); restore(p,old)
        p=work/'M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/InMemoryUserConfig.java'; p.write_text('package es.mecd.demo.miproyecto.config; public class InMemoryUserConfig {}\n',encoding='utf-8'); cases.append(('transient_leaked_final',not run_all(work))); p.unlink()
        rejected=sum(ok for _,ok in cases)
        if rejected!=len(cases): print('M6 MUTATION GATE: FAIL',cases); raise SystemExit(1)
        print(f'M6 MUTATION GATE: PASS | rejected={rejected}/{len(cases)} | '+', '.join(n for n,_ in cases))
if __name__=='__main__': main()
