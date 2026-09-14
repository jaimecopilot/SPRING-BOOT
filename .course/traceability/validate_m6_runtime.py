#!/usr/bin/env python3
from pathlib import Path
import argparse, json, os, socket, subprocess, sys, tempfile, time, urllib.error, urllib.request

def free_port():
    s=socket.socket(); s.bind(('127.0.0.1',0)); p=s.getsockname()[1]; s.close(); return p

def request(url,method='GET',body=None,headers=None,timeout=5):
    data=None if body is None else json.dumps(body).encode('utf-8')
    h={'Accept':'application/json'}; h.update(headers or {})
    if body is not None: h['Content-Type']='application/json'
    req=urllib.request.Request(url,data=data,headers=h,method=method)
    try:
        with urllib.request.urlopen(req,timeout=timeout) as r:
            raw=r.read(); return r.status,dict(r.headers),json.loads(raw) if raw else None
    except urllib.error.HTTPError as e:
        raw=e.read()
        try: parsed=json.loads(raw) if raw else None
        except Exception: parsed=raw.decode('utf-8','replace')
        return e.code,dict(e.headers),parsed

def assert_error(status,body,expected):
    assert status==expected,(status,body)
    assert isinstance(body,dict),body
    for k in ['timestamp','status','codigo','mensaje','path','traceId']:
        assert k in body,(k,body)
    assert body['status']==expected
    assert body['traceId']

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); a=ap.parse_args(); root=Path(a.root).resolve()
    jar=root/'M6/proyecto/target/mi-proyecto-0.0.1-SNAPSHOT.jar'
    assert jar.is_file(),f'JAR missing: {jar}'
    port=free_port(); base=f'http://127.0.0.1:{port}'
    log=Path(tempfile.gettempdir())/f'm6-runtime-{port}.log'
    env=os.environ.copy(); env['SPRING_PROFILES_ACTIVE']='test'
    with open(log,'w',encoding='utf-8') as out:
        proc=subprocess.Popen(['java','-jar',str(jar),f'--server.port={port}','--spring.profiles.active=test'],stdout=out,stderr=subprocess.STDOUT,env=env)
    try:
        deadline=time.time()+75
        ready=False
        while time.time()<deadline:
            if proc.poll() is not None: break
            try:
                st,_,_=request(base+'/api/v1/public/info',timeout=2)
                if st==200: ready=True; break
            except Exception: pass
            time.sleep(1)
        if not ready:
            tail=log.read_text(encoding='utf-8',errors='replace')[-8000:] if log.exists() else ''
            raise AssertionError('application did not become ready\n'+tail)

        # public/private and stable errors
        st,h,b=request(base+'/api/v1/public/info'); assert st==200,(st,b)
        st,h,b=request(base+'/api/v1/perfil'); assert_error(st,b,401); assert b['codigo']=='NO_AUTENTICADO'

        # CORS preflight must survive Security
        st,h,b=request(base+'/api/v1/perfil',method='OPTIONS',headers={'Origin':'http://localhost:3000','Access-Control-Request-Method':'GET'})
        assert st in (200,204),(st,h,b)
        acao=h.get('Access-Control-Allow-Origin') or h.get('access-control-allow-origin'); assert acao=='http://localhost:3000',(acao,h)

        # OpenAPI preserved
        st,h,b=request(base+'/v3/api-docs'); assert st==200,(st,b)

        # Login user
        st,h,b=request(base+'/api/v1/auth/login','POST',{'username':'ana','password':'ana123'}); assert st==200,(st,b)
        access=b['access_token']; refresh=b['refresh_token']; assert b['token_type']=='Bearer' and b['expires_in']>0
        assert not any('JSESSIONID' in k.upper()+str(v).upper() for k,v in h.items())
        st,h,b=request(base+'/api/v1/perfil',headers={'Authorization':'Bearer '+access}); assert st==200,(st,b); assert b['username']=='ana' and b['email']=='ana@educacion.gob.es' and 'ROLE_USER' in b['authorities'],b
        st,h,b=request(base+'/api/v1/admin/usuarios',headers={'Authorization':'Bearer '+access}); assert_error(st,b,403); assert b['codigo']=='ACCESO_DENEGADO'

        # roles
        st,_,admin=request(base+'/api/v1/auth/login','POST',{'username':'admin','password':'admin123'}); assert st==200
        st,_,b=request(base+'/api/v1/admin/usuarios',headers={'Authorization':'Bearer '+admin['access_token']}); assert st==200,(st,b)
        st,_,gest=request(base+'/api/v1/auth/login','POST',{'username':'gestor','password':'gestor123'}); assert st==200
        st,_,b=request(base+'/api/v1/gestor/documentos',headers={'Authorization':'Bearer '+gest['access_token']}); assert st==200,(st,b)

        # register/login new user
        st,_,b=request(base+'/api/v1/auth/registro','POST',{'username':'pedro','password':'pedro1234','email':'pedro@educacion.gob.es'}); assert st==201,(st,b); assert 'password' not in b
        st,_,p=request(base+'/api/v1/auth/login','POST',{'username':'pedro','password':'pedro1234'}); assert st==200

        # refresh rotation/replay
        st,_,r=request(base+'/api/v1/auth/refresh','POST',{'refresh_token':refresh}); assert st==200,(st,r)
        st,_,bad=request(base+'/api/v1/auth/refresh','POST',{'refresh_token':refresh}); assert_error(st,bad,401); assert bad['codigo']=='TOKEN_INVALIDO'

        # logout blacklist on the newly refreshed access
        new_access=r['access_token']
        st,_,_=request(base+'/api/v1/auth/logout','POST',headers={'Authorization':'Bearer '+new_access}); assert st==204,st
        st,_,bad=request(base+'/api/v1/perfil',headers={'Authorization':'Bearer '+new_access}); assert_error(st,bad,401)

        # invalid login uniform 401
        st,_,bad=request(base+'/api/v1/auth/login','POST',{'username':'ana','password':'incorrecta'}); assert_error(st,bad,401); assert bad['codigo']=='CREDENCIALES_INVALIDAS'
        print('M6 RUNTIME: PASS | public/private | 401/403 JSON | CORS | OpenAPI | login | roles | register | refresh rotation | logout revocation | stateless')
    finally:
        if proc.poll() is None:
            proc.terminate()
            try: proc.wait(timeout=10)
            except subprocess.TimeoutExpired: proc.kill(); proc.wait(timeout=5)
        try: log.unlink()
        except OSError: pass
if __name__=='__main__': main()
