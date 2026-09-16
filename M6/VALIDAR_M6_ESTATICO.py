from pathlib import Path
import re, sys

ROOT = Path(__file__).resolve().parent
errors=[]
def ok(cond,msg):
    if not cond: errors.append(msg)

theory=(ROOT/'TEORIA.md').read_text(encoding='utf-8')
practice=(ROOT/'PRACTICA.md').read_text(encoding='utf-8')
ok(len(re.findall(r'^# Punto 6\.[1-9]\b', theory, re.M))==9, 'TEORIA: no hay exactamente 9 puntos')
ok(len(re.findall(r'^## Bloque [1-5]\b', theory, re.M))==45, 'TEORIA: no hay exactamente 45 bloques')
ok(len(re.findall(r'^## Paso \d+\b', practice, re.M))==112, 'PRACTICA: no hay exactamente 112 pasos')
ok('0.13.0' not in practice and '900000' not in practice, 'PRACTICA: quedan valores divergentes 0.13.0/900000')
for i in range(1,10):
    v=f'6.{i}'
    p=ROOT/v/'proyecto'
    ok((p/'pom.xml').exists(), f'{v}: falta pom.xml')
    ok((p/'src/main/java').exists(), f'{v}: falta src/main/java')
    ok((p/'src/test/java').exists(), f'{v}: falta src/test/java')
for i in range(6,10):
    v=f'6.{i}'
    pom=(ROOT/v/'proyecto/pom.xml').read_text(encoding='utf-8')
    ok('0.12.3' in pom, f'{v}: JJWT no está en 0.12.3')
    props='\n'.join(x.read_text(encoding='utf-8') for x in (ROOT/v/'proyecto/src/main/resources').glob('application-*.properties'))
    ok('3600000' in props, f'{v}: no aparece expiración access 3600000')
manual=(ROOT/'6.5/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java').read_text(encoding='utf-8')
for token in ['JWT generado:', 'Token válido:', 'Token modificado válido:', 'Con clave incorrecta:', 'adulterarPayload']:
    ok(token in manual, f'6.5 JwtManual: falta observable/función {token}')
sec7=(ROOT/'6.7/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java').read_text(encoding='utf-8')
ok('HttpStatusEntryPoint' in sec7 and 'UNAUTHORIZED' in sec7, '6.7: falta entrypoint 401 temporal')
auth9=(ROOT/'6.9/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthControllerSecurityTest.java').read_text(encoding='utf-8')
ok('@SpringBootTest' in auth9 and '@AutoConfigureMockMvc' in auth9, '6.9: AuthControllerSecurityTest no es integración real')
ok('addFilters = false' not in auth9, '6.9: AuthControllerSecurityTest desactiva filtros')
ok('logout_debeRevocarElAccessToken' in auth9, '6.9: falta test de revocación real')
for i in range(4,10):
    v=f'6.{i}'
    p=ROOT/v/'proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java'
    if p.exists():
        t=p.read_text(encoding='utf-8')
        ok('listar_debeDevolver401_cuandoNoAutenticado' in t, f'{v}: falta caso 401 anónimo')
ok(not list(ROOT.glob('6.*.zip')), 'Raíz M6: quedan ZIPs anidados')

if errors:
    print('M6 STATIC GATE: FAIL')
    for e in errors: print(' -',e)
    sys.exit(1)
print('M6 STATIC GATE: PASS | theory_points=9 | theory_blocks=45 | practice_steps=112 | snapshots=9')
