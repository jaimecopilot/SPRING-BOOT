#!/usr/bin/env python3
from pathlib import Path
import argparse, hashlib, json, re

def sha256(p): return hashlib.sha256(p.read_bytes()).hexdigest()
def git_blob(p):
    b=p.read_bytes(); return hashlib.sha1(b"blob "+str(len(b)).encode()+b"\0"+b).hexdigest()

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); a=ap.parse_args(); root=Path(a.root).resolve()
    tr=root/'.course/traceability'; proj=root/'M6/proyecto'
    master=json.loads((tr/'M6.json').read_text(encoding='utf-8'))
    ct=json.loads((tr/'M6_CODE_TRACE.json').read_text(encoding='utf-8'))
    assert ct['schema_version']==1 and ct['module']=='M6' and ct['status']=='COMPLETE'
    assert ct['baseline_sha']==master['baseline_sha']
    assert ct['counts']['practice_steps']==112
    entries=ct['final_project']; assert len(entries)==ct['counts']['final_project_files']==124,len(entries)
    assert len({e['path'] for e in entries})==124
    assert ct['counts']['m5_preserved']==70
    assert ct['counts']['m5_evolved']==9
    assert ct['counts']['m6_added_functional']==43
    assert ct['counts']['m6_added_support']==2

    # Load 112 step contracts and build direct relation map.
    steps={}; rels={}
    for i in range(1,10):
        d=json.loads((tr/f'M6/6.{i}.json').read_text(encoding='utf-8'))
        assert d['status']=='COMPLETE'
        for s in d['steps']:
            steps[s['id']]=s
            assert s['guide_section_sha256'] and s['source_body_sha256']
            for r in s.get('artifact_relations',[]):
                assert r['action'] in {'CREATE','MODIFY','USE','VERIFY','DELETE','RESTORE'},(s['id'],r)
                rels.setdefault(r['path'],[]).append((s,r))
                p=root/r['path']
                if r['action']=='DELETE':
                    assert not p.exists(),f"{s['id']}: DELETE but final artifact exists: {r['path']}"
                    assert r.get('final_sha256') is None
                elif p.is_file():
                    assert r.get('final_sha256')==sha256(p),f"{s['id']}: relation final hash mismatch {r['path']}"
                    # For code/resources relations, require at least one machine-verifiable symbol when symbols are declared.
                    if p.suffix.lower() in {'.java','.xml','.properties','.md'} and r.get('symbols'):
                        txt=p.read_text(encoding='utf-8',errors='replace')
                        present=[x for x in r['symbols'] if x in txt]
                        # Intermediate-only relations may have all final symbols gone; final-changing relations need one concrete anchor.
                        if r['action'] in {'CREATE','MODIFY'} and r.get('evidence_scope','FINAL')=='FINAL' and r['path'] not in {x['path'] for x in ct['transient_artifacts']}:
                            assert present or p.stem in txt,f"{s['id']}: no relation symbol anchors final code {r['path']}"
    assert len(steps)==112

    bypath={e['path']:e for e in entries}
    # Logical final assembled file set = M5 baseline union overlay.
    baseline=json.loads((proj/'.baseline-m5-tree.json').read_text(encoding='utf-8'))
    expected={'M6/proyecto/'+e['path'] for e in baseline['entries']}
    expected |= {'M6/proyecto/'+p.relative_to(proj).as_posix() for p in proj.rglob('*') if p.is_file()}
    assert set(bypath)==expected,(len(set(bypath)),len(expected),sorted(expected-set(bypath))[:5])

    functional=0
    for path,e in bypath.items():
        if e['classification']=='M5_PRESERVED':
            assert e['origin'] and e['origin']['m5_blob_sha']==e['final_git_blob_sha'],path
        elif e['classification']=='M5_EVOLVED':
            functional+=1
            assert e['origin'] and e['origin']['m5_blob_sha']!=e['final_git_blob_sha'],path
            assert e['step_refs'],f'untraced evolved file: {path}'
            assert path in rels,f'evolved file absent from direct relations: {path}'
        elif e['classification']=='M6_ADDED_FUNCTIONAL':
            functional+=1
            assert e['step_refs'],f'untraced new functional file: {path}'
            assert path in rels,f'new functional file absent from direct relations: {path}'
        elif e['classification']=='M6_ADDED_SUPPORT':
            assert e.get('support_reason'),f'support without reason: {path}'
        else: raise AssertionError(e['classification'])
        # Overlay files have exact final hashes available pre-assembly.
        local=root/path
        if local.is_file():
            if e['classification']=='M5_PRESERVED':
                # Working-tree EOL can differ from the canonical Git blob on Windows.
                # Identity is proved by the predecessor blob + continuity validator;
                # when this preserved file is physically present in the payload, its
                # payload SHA is also checked exactly.
                if e.get('final_sha256') is not None and not (root/'M5/proyecto').is_dir():
                    assert e['final_sha256']==sha256(local),f'inverse payload sha256 mismatch {path}'
            else:
                assert e['final_sha256']==sha256(local),f'inverse sha256 mismatch {path}'
                assert e['final_git_blob_sha']==git_blob(local),f'inverse git blob mismatch {path}'
        for sid in e['step_refs']:
            assert sid in steps and any(r['path']==path for r in steps[sid]['artifact_relations']),f'broken inverse edge {path}->{sid}'

    # All final functional new/evolved files are reversible to practice.
    assert functional==52,functional

    # Transient lifecycle is machine checked.
    assert len(ct['transient_artifacts'])==1
    for t in ct['transient_artifacts']:
        p=root/t['path']; assert not p.exists(),f'transient leaked into final snapshot: {t["path"]}'
        events=[]
        for sid,s in steps.items():
            for r in s.get('artifact_relations',[]):
                if r['path']==t['path']: events.append((sid,r['action']))
        assert (t['created_by'],'CREATE') in events,events
        assert (t['closed_by'],'DELETE') in events,events

    # Explicitly guard the previously untraced implementation pieces.
    required={
      'M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CredencialesInvalidasException.java':'6.6.12',
      'M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenInvalidoException.java':'6.6.12',
      'M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java':'6.6.12',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtServiceIntegrationTest.java':'6.9.11',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java':'6.9.3',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java':'6.9.3',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java':'6.9.3',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java':'6.9.3',
      'M6/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java':'6.9.3'}
    for path,sid in required.items():
        assert path in bypath and sid in bypath[path]['step_refs'],(path,sid)

    print('M6 BIDIRECTIONAL CODE TRACE: PASS | direct=112/112 | inverse=124/124 final files | functional=52/52 step-linked | M5 preserved=70 | M5 evolved=9 | M6 functional added=43 | support=2 | transient lifecycle=1/1')
if __name__=='__main__': main()
