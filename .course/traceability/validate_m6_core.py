#!/usr/bin/env python3
from pathlib import Path
import argparse, hashlib, json, re, subprocess

EXPECTED_COMMIT = "0f76aa08385605a48eccbcb3b1be6bf18b9bce1e"
EXPECTED_TREE = "e9194f0ec6b595d3dbc7dbf921eef8a815fe9ae1"
EXPECTED_STEPS = {'6.1':12,'6.2':12,'6.3':14,'6.4':12,'6.5':12,'6.6':13,'6.7':12,'6.8':12,'6.9':13}
EXPECTED_CODE_MARKERS={'bash':45,'http':1,'java':160,'json':11,'properties':5,'sql':1,'text':12,'xml':4}

TEXT_SUFFIXES={'.java','.properties','.xml','.md','.cmd','.yml','.yaml','.json','.py','.html'}
TEXT_NAMES={'mvnw','.gitignore'}
def continuity_bytes(p):
    data=p.read_bytes()
    if p.suffix.lower() in TEXT_SUFFIXES or p.name in TEXT_NAMES:
        data=data.replace(b'\r\n',b'\n').replace(b'\r',b'\n')
    return data

def sha_text(s: str) -> str:
    return hashlib.sha256(s.encode('utf-8')).hexdigest()

def git_blob_sha(data: bytes) -> str:
    return hashlib.sha1(b"blob " + str(len(data)).encode() + b"\0" + data).hexdigest()

def sections(text: str, pattern: str, keyfn):
    ms=list(re.finditer(pattern,text,re.M)); out={}
    for i,m in enumerate(ms):
        end=ms[i+1].start() if i+1<len(ms) else len(text)
        out[keyfn(m)]=text[m.start():end].strip()
    return out

def validate(root: Path, continuity=True):
    root=root.resolve(); m6=root/'M6'; proj=m6/'proyecto'; tr=root/'.course/traceability'
    m=json.loads((tr/'M6.json').read_text(encoding='utf-8'))
    assert m['schema_version']==3
    assert m['module']=='M6' and m['status']=='COMPLETE' and m['predecessor']=='M5'
    assert m['baseline_sha']==EXPECTED_COMMIT
    assert len(m['theory_concepts'])==45 and all(x.get('status')=='COMPLETE' for x in m['theory_concepts'])
    assert m['current_traced_steps']==112 and m['expected_total_steps_when_complete']==112
    for k in ('bidirectional_code_trace_required','functional_file_origin_required','relation_hash_validation_required','transient_lifecycle_required'):
        assert m['policy'].get(k),k
    assert m.get('code_trace')=='.course/traceability/M6_CODE_TRACE.json'
    assert (tr/'M6_CODE_TRACE.json').is_file()

    src=m['source']
    expected_src={
      'page_start':1203,'page_end':1550,'pages':348,'next_module_page':1551,
      'characters_with_whitespace':323414,'characters_without_whitespace':256304,
      'words':38245,'lines':9535,'theory_main_blocks':45,'theory_subpoints':135,
      'practical_steps':112,'code_example_blocks':239,
      'source_text_sha256':'3e5eda24a5313923bc52688b55bd391ee5197247d59035a843708d62009ea5c0'
    }
    for k,v in expected_src.items(): assert src.get(k)==v,(k,src.get(k),v)
    assert src.get('source_metrics_status')=='VERIFIED_EXACT_PDFTOTEXT_LAYOUT'
    assert src.get('code_markers')==EXPECTED_CODE_MARKERS,(src.get('code_markers'),EXPECTED_CODE_MARKERS)
    sm=src['section_metrics']
    assert sum(x['characters'] for x in sm.values())==src['characters_with_whitespace']
    assert sum(x['words'] for x in sm.values())==src['words']
    assert sum(x['subpoints'] for x in sm.values())==135
    assert sum(x['steps'] for x in sm.values())==112
    assert sum(x['code_blocks'] for x in sm.values())==239

    # Exact step manifests + replay contract.
    allsteps=[]
    for sec,n in EXPECTED_STEPS.items():
        d=json.loads((tr/f'M6/{sec}.json').read_text(encoding='utf-8'))
        assert d['schema_version']==3 and d['module']=='M6' and d['section']==sec and d['status']=='COMPLETE'
        assert d['expected_steps']==n and len(d.get('steps',[]))==n
        for i,s in enumerate(d['steps'],1):
            assert s['id']==f'{sec}.{i}'
            for k in ('title','theory','action','artifacts','command','observable','verification','guide_heading',
                      'source_anchor','source_body_sha256','guide_section_sha256','artifact_relations','replay','status'):
                assert s.get(k),f'{s["id"]}: missing {k}'
            assert s['status']=='COMPLETE'
            rp=s['replay']
            for k in ('precondition','operation','command','expected_observable','postcondition','verification'):
                assert rp.get(k),f'{s["id"]}: replay missing {k}'
            allsteps.append(s)
    assert len(allsteps)==112 and len({s['id'] for s in allsteps})==112

    theory=(m6/'TEORIA.md').read_text(encoding='utf-8')
    practice=(m6/'PRACTICA.md').read_text(encoding='utf-8')
    assert len(re.findall(r'^## TC-6\.[1-9]-[1-5] — ',theory,re.M))==45
    assert len(re.findall(r'^### 6\.[1-9]-T[1-5]\.[1-3] — ',theory,re.M))==135
    assert len(re.findall(r'^## Paso 6\.[1-9]\.\d+ — ',practice,re.M))==112
    assert theory.count('```')%2==0 and practice.count('```')%2==0
    for bad in ('Ã','Â','�'):
        assert bad not in theory+practice,bad
    assert 'DRAFT' not in theory and 'DRAFT' not in practice

    # Source-floor fidelity: exact final metrics + hashes for all 247 guide units.
    fm=json.loads((m6/'M6_FIDELITY_METRICS.json').read_text(encoding='utf-8'))
    assert fm['source_pages']=='1203-1550' and fm['source_page_count']==348
    assert fm['source_total_chars']==323414 and fm['source_total_words']==38245
    assert fm['source_explicit_code_labels']==239 and fm['source_explicit_code_labels_by_language']==EXPECTED_CODE_MARKERS
    assert fm['theory_subpoints']==135 and fm['practice_steps']==112
    assert fm['final_theory_chars']==len(theory) and fm['final_theory_words']==len(theory.split())
    assert fm['final_practice_chars']==len(practice) and fm['final_practice_words']==len(practice.split())
    assert fm['final_theory_chars']>=fm['source_theory_chars'] and fm['final_theory_words']>=fm['source_theory_words']
    assert fm['final_practice_chars']>=fm['source_practice_chars'] and fm['final_practice_words']>=fm['source_practice_words']
    assert m['fidelity']['policy']=='SOURCE_FLOOR_PLUS_MODERNIZATION'
    assert m['fidelity']['status']=='VERIFIED_BY_HASHED_GUIDE_SECTIONS'
    for k in ('final_theory_chars','final_theory_words','final_practice_chars','final_practice_words'):
        assert m['fidelity'][k]==fm[k]

    fi=json.loads((m6/'M6_SOURCE_FIDELITY.json').read_text(encoding='utf-8'))
    assert len(fi['theory_subpoints'])==135 and len(fi['practice_steps'])==112
    th=sections(theory,r'^### 6\.([1-9])-T([1-5])\.([1-3]) — (.+)$',lambda x:f'6.{x.group(1)}-T{x.group(2)}.{x.group(3)}')
    pr=sections(practice,r'^## Paso 6\.([1-9])\.(\d+) — (.+)$',lambda x:f'6.{x.group(1)}.{int(x.group(2))}')
    assert len(th)==135 and len(pr)==112
    for e in fi['theory_subpoints']:
        assert e['status']=='PRESERVED_AND_EXPANDED'
        assert e['id'] in th and sha_text(th[e['id']])==e['guide_section_sha256'],e['id']
        assert len(th[e['id']].split())==e['guide_words'],e['id']
    for e in fi['practice_steps']:
        assert e['status']=='PRESERVED_AND_EXPANDED'
        assert e['id'] in pr and sha_text(pr[e['id']])==e['guide_section_sha256'],e['id']
        assert len(pr[e['id']].split())==e['guide_words'],e['id']
    by_step={x['id']:x for x in fi['practice_steps']}
    for s in allsteps:
        f=by_step[s['id']]
        assert s['source_body_sha256']==f['source_body_sha256']
        assert s['guide_section_sha256']==f['guide_section_sha256']
        assert s['source_words']==f['source_words'] and s['guide_words']==f['guide_words']

    # Final Security/JWT contract.
    pom=(proj/'pom.xml').read_text(encoding='utf-8')
    for x in ('<version>3.5.16</version>','<java.version>17</java.version>','<jjwt.version>0.13.0</jjwt.version>',
              '<artifactId>spring-boot-starter-security</artifactId>','<artifactId>spring-security-test</artifactId>',
              '<artifactId>jjwt-api</artifactId>','<artifactId>jjwt-impl</artifactId>','<artifactId>jjwt-jackson</artifactId>'):
        assert x in pom,x
    security=(proj/'src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java').read_text(encoding='utf-8')
    for x in ('SecurityFilterChain','@EnableMethodSecurity','SessionCreationPolicy.STATELESS','.cors(Customizer.withDefaults())',
              'addFilterBefore','UsernamePasswordAuthenticationFilter.class','JwtAuthenticationEntryPoint','JwtAccessDeniedHandler',
              '/api/v1/auth/login','/api/v1/auth/refresh','/api/v1/auth/registro','/api/v1/admin/**','/api/v1/gestor/**',
              '.anyRequest().authenticated()'):
        assert x in security,x
    for forbidden in ('WebSecurityConfigurerAdapter','.httpBasic(', '"/api/v1/auth/**"'):
        assert forbidden not in security,forbidden

    main_text='\n'.join(p.read_text(encoding='utf-8') for p in (proj/'src/main/java').rglob('*.java'))
    test_text='\n'.join(p.read_text(encoding='utf-8') for p in (proj/'src/test/java').rglob('*.java'))
    for forbidden in ('InMemoryUserDetailsManager','NoOpPasswordEncoder','WebSecurityConfigurerAdapter'):
        assert forbidden not in main_text,forbidden
    assert '@MockBean' not in test_text
    for rel in (
      'src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java',
      'src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java',
      'src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java',
      'src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java',
      'src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java'):
        txt=(proj/rel).read_text(encoding='utf-8'); assert '@AutoConfigureMockMvc(addFilters = false)' in txt,rel
    assert 'JwtAuthenticationFilter extends OncePerRequestFilter' in main_text
    assert 'UsuarioPrincipal' in main_text and 'ROLE_' in main_text and 'MessageDigest.isEqual' in test_text

    # Final runtime profile contract must match the final 6.7 guide/code, not the temporary 6.2 shape.
    perfil=(proj/'src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java').read_text(encoding='utf-8')
    assert '"username", user.getUsername()' in perfil
    assert '"email", user.getEmail()' in perfil
    assert '"authorities"' in perfil
    runtime_validator=(tr/'validate_m6_runtime.py').read_text(encoding='utf-8')
    assert "b['username']=='ana'" in runtime_validator
    assert "b['email']=='ana@educacion.gob.es'" in runtime_validator
    assert "'ROLE_USER' in b['authorities']" in runtime_validator
    assert "b['usuario']=='ana'" not in runtime_validator

    # Regression contract: M6 does not teach or justify a module-version bump.
    # Preserve the M5 app.version while evolving these profiles only for JWT/security.
    dev_props=(proj/'src/main/resources/application-dev.properties').read_text(encoding='utf-8')
    test_props=(proj/'src/main/resources/application-test.properties').read_text(encoding='utf-8')
    prod_props=(proj/'src/main/resources/application-prod.properties').read_text(encoding='utf-8')
    assert 'app.version=5.0.0' in dev_props
    assert 'app.version=5.0.0' in test_props
    assert 'app.version=${APP_VERSION:5.0.0}' in prod_props
    assert 'app.version=6.0.0' not in dev_props+test_props
    assert '${APP_VERSION:6.0.0}' not in prod_props

    jwt=(proj/'src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java').read_text(encoding='utf-8')
    for x in ('TIPO_ACCESS','TIPO_REFRESH','.subject(','.id(UUID.randomUUID()','.issuedAt(','.expiration(',
              '.signWith(clave, Jwts.SIG.HS256)','parseSignedClaims','verifyWith(clave)'):
        assert x in jwt,x
    assert '.signWith(clave)\n' not in jwt,'implicit JJWT signing algorithm forbidden'
    assert '.claim("roles", roles)' in jwt
    refresh_block=jwt[jwt.index('public String generarRefreshToken'):jwt.index('public String extraerUsername')]
    assert '.claim("roles"' not in refresh_block

    filt=(proj/'src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java').read_text(encoding='utf-8')
    for x in ('Authorization','Bearer ','esAccessTokenValido','UsuarioPrincipal','SecurityContextHolder','isAccessRevoked'):
        assert x in filt,x
    assert 'usuarioRepository' not in filt
    for name,code in [('JwtAuthenticationEntryPoint.java','NO_AUTENTICADO'),('JwtAccessDeniedHandler.java','ACCESO_DENEGADO')]:
        assert code in (proj/f'src/main/java/es/mecd/demo/miproyecto/auth/{name}').read_text(encoding='utf-8')
    writer=(proj/'src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java').read_text(encoding='utf-8')
    for x in ('ErrorResponse','setTraceId','setTimestamp','setStatus','setCodigo','setMensaje','setPath'): assert x in writer,x

    prod=(proj/'src/main/resources/application-prod.properties').read_text(encoding='utf-8')
    assert 'jwt.secret=${JWT_SECRET}' in prod and 'jwt.secret=${JWT_SECRET:' not in prod
    for profile in ('application-dev.properties','application-test.properties'):
        t=(proj/'src/main/resources'/profile).read_text(encoding='utf-8')
        assert all(x in t for x in ('jwt.secret=','jwt.expiration=','jwt.refresh-expiration='))

    def junit_count(p): return len(re.findall(r'^\s*@Test\s*$',p.read_text(encoding='utf-8'),re.M))
    security_tests=sum(junit_count(p) for p in (proj/'src/test/java/es/mecd/demo/miproyecto').rglob('*.java')
                       if '/auth/' in p.as_posix() or '/jwt/' in p.as_posix() or p.name=='PublicControllerSecurityTest.java')
    assert security_tests==26,security_tests
    assert (m6/'M6_TEORIA.pdf').stat().st_size>10000 and (m6/'M6_PRACTICA.pdf').stat().st_size>10000
    assert (root/'.course/source-audit/M6.md').stat().st_size>2000
    assert (tr/'TRAZABILIDAD_M6.md').stat().st_size>100000
    assert not (root/'.github/workflows/validar-m6.yml').exists(),'M6 must not introduce GitHub Actions workflow'

    long=[]
    for p in (proj/'src').rglob('*.java'):
        for i,line in enumerate(p.read_text(encoding='utf-8').splitlines(),1):
            if len(line)>120: long.append((str(p.relative_to(root)),i,len(line)))
    assert not long,long[:5]

    # Full 79-entry continuity manifest is mandatory even in payload preflight.
    baseline=json.loads((proj/'.baseline-m5-tree.json').read_text(encoding='utf-8'))
    assert baseline['project_tree_sha']==EXPECTED_TREE and baseline['blob_count']==79
    cont=m['continuity_from_m5']; entries=cont.get('entries',[])
    assert cont.get('accounted')==79 and len(entries)==79
    assert {e['m5_path'].replace('M5/proyecto/','') for e in entries}=={e['path'] for e in baseline['entries']}
    allow=set(cont['modified_allowlist']); assert len(allow)==9
    assert cont['preserved_required']==70 and cont['evolved_required']==9

    if continuity:
        m5=root/'M5/proyecto'; assert m5.is_dir(),'M5/proyecto missing: run validator on assembled repository'
        tree=subprocess.check_output(['git','-C',str(root),'rev-parse','HEAD:M5/proyecto'],text=True).strip()
        assert tree==EXPECTED_TREE,(tree,EXPECTED_TREE)
        preserved=evolved=0
        by={e['path']:e for e in baseline['entries']}
        for e in entries:
            rel=e['m5_path'].replace('M5/proyecto/',''); srcp=m5/rel; dst=proj/rel
            assert srcp.is_file(),f'M5 baseline missing {rel}'
            assert dst.is_file(),f'M6 inherited file missing {rel}'
            stage=subprocess.check_output(['git','-C',str(root),'ls-files','-s','--',f'M5/proyecto/{rel}'],text=True).strip().split()
            assert len(stage)>=2 and stage[1]==by[rel]['sha'],f'M5 index blob changed: {rel}'
            if rel in allow:
                assert continuity_bytes(srcp)!=continuity_bytes(dst),f'allowlisted M6 evolution did not change: {rel}'
                evolved+=1
            else:
                assert continuity_bytes(srcp)==continuity_bytes(dst),f'M6 inherited physical drift: {rel}'
                preserved+=1
        assert (preserved,evolved)==(70,9),(preserved,evolved)
        inherited_tests=[e for e in baseline['entries'] if e['path'].startswith('src/test/java/') and e['path'].endswith('.java')]
        assert len(inherited_tests)==14 and all((proj/e['path']).is_file() for e in inherited_tests)

    bad=[x for x in m.get('temporary_states',[]) if not str(x.get('status','')).startswith('CLOSED')]
    assert not bad,bad
    return {'steps':112,'theory':45,'subpoints':135,'artifacts':len(m['artifacts']),'continuity':continuity}

def main():
    ap=argparse.ArgumentParser(); ap.add_argument('root',nargs='?',default='.'); ap.add_argument('--skip-continuity',action='store_true')
    a=ap.parse_args(); r=validate(Path(a.root),continuity=not a.skip_continuity)
    print(f"M6 FINAL CONTRACT: PASS | theory={r['theory']}/45 | subpoints={r['subpoints']}/135 | steps={r['steps']}/112 | fidelity=HASHED | continuity={'70/70 preserved + 9 evolved' if r['continuity'] else 'DEFERRED_TO_ASSEMBLY'} | temporary=closed | GitHubActions=NONE")
if __name__=='__main__': main()
