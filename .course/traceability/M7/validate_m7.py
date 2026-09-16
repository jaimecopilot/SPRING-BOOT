from pathlib import Path
import json,re,sys,xml.etree.ElementTree as ET
ROOT=Path(__file__).resolve().parents[2]
M7=ROOT/'M7'; P=M7/'proyecto'
errors=[]
def fail(x): errors.append(x)

# Documentation structure
teo=(M7/'TEORIA.md').read_text(encoding='utf-8')
pra=(M7/'PRACTICA.md').read_text(encoding='utf-8')
blocks=len(re.findall(r'(?m)^#{2,4} ⏱ BLOQUE \d+|^⏱ BLOQUE \d+',teo))
# Current canonical practice uses 7.x.yy numbering.
steps=len(re.findall(r'(?m)^### 🔧 Paso 7\.\d+\.\d{2} — ',pra))
preps=len(re.findall(r'(?m)^### 🧰 PREP-\d{2} — ',pra))
if blocks!=32: fail(f'TEORIA design blocks {blocks} != 32')
if steps!=77: fail(f'PRACTICA source steps {steps} != 77')
if preps!=8: fail(f'PRACTICA prep steps {preps} != 8')
for forbidden in ['Pregunta:', 'Adaptación de la edición', 'Precondición de aceptación R6', 'PDF presupone']:
    if forbidden.lower() in pra.lower(): fail(f'forbidden editorial residue in PRACTICA: {forbidden}')
if '\uf0b7' in teo or '\uf0b7' in pra: fail('private-use bullet U+F0B7 remains in docs')

# Traceability and NO_MAGIC_FILES
trace_path=ROOT/'.course/traceability/M7.json'
tr=json.loads(trace_path.read_text(encoding='utf-8'))
if tr['depends_on_previous_module_code'] is not False: fail('trace says M7 depends on previous module code')
if tr['counts']['source_practice_steps']!=77 or tr['counts']['standalone_prep_steps']!=8: fail('trace counts mismatch')
actual=set()
for p in P.rglob('*'):
    if not p.is_file(): continue
    rel=p.relative_to(P).as_posix()
    if rel.startswith('target/'): continue
    actual.add(rel)
mapped=set(tr['inverse_project_file_to_steps'])
unmapped=sorted(actual-mapped)
stale=sorted(mapped-actual)
if unmapped: fail('NO_MAGIC_FILES unmapped: '+', '.join(unmapped))
if stale: fail('trace references missing project files: '+', '.join(stale))
if tr['counts']['project_files']!=len(actual): fail(f'trace project_files {tr["counts"]["project_files"]} != actual {len(actual)}')

# POM
try: ET.parse(P/'pom.xml')
except Exception as e: fail(f'pom XML invalid: {e}')
pom=(P/'pom.xml').read_text(encoding='utf-8')
for a in ['spring-boot-starter-web','spring-boot-starter-data-jpa','spring-boot-starter-security','spring-boot-starter-validation','h2','springdoc-openapi-starter-webmvc-ui','jjwt-api','spring-security-test','jacoco-maven-plugin','0.8.11']:
    if a not in pom: fail(f'pom missing {a}')

# Java package/path and internal imports
java=list((P/'src').rglob('*.java'))
class_paths={}
for p in java:
    s=p.read_text(encoding='utf-8')
    if s.count('{')!=s.count('}'): fail(f'brace mismatch {p.relative_to(P)}')
    m=re.search(r'(?m)^package\s+([\w.]+);',s)
    if not m: fail(f'missing package {p.relative_to(P)}'); continue
    pkg=m.group(1); expected='/'.join(pkg.split('.'))+'/'+p.name
    main_root=(P/'src/main/java').resolve(); test_root=(P/'src/test/java').resolve(); resolved=p.resolve()
    if resolved.is_relative_to(main_root): source_root=main_root; is_main=True
    elif resolved.is_relative_to(test_root): source_root=test_root; is_main=False
    else: fail(f'Java file outside source roots: {p.relative_to(P)}'); continue
    rel=resolved.relative_to(source_root).as_posix()
    if rel!=expected: fail(f'package/path mismatch {p.relative_to(P)} package={pkg}')
    if is_main: class_paths[pkg+'.'+p.stem]=p
for p in java:
    s=p.read_text(encoding='utf-8')
    for imp in re.findall(r'(?m)^import\s+(es\.mecd\.demo\.miproyecto\.[\w.]+);',s):
        if imp not in class_paths: fail(f'unresolved internal import {imp} in {p.relative_to(P)}')

# Canonical project artifacts and API
if not (P/'docs/diseno-api.md').exists(): fail('missing docs/diseno-api.md')
if (P/'docs/dise#U00f1o-api.md').exists(): fail('escaped design filename remains')
openapi_config=P/'src/main/java/es/mecd/demo/miproyecto/config/OpenApiConfig.java'
if not openapi_config.exists(): fail('missing config/OpenApiConfig.java for Swagger bearerAuth')
else:
    oas=openapi_config.read_text(encoding='utf-8')
    for marker in ['bearerAuth','SecurityScheme.Type.HTTP','.scheme("bearer")','.bearerFormat("JWT")']:
        if marker not in oas: fail(f'OpenApiConfig missing {marker}')
allmain='\n'.join(p.read_text(encoding='utf-8') for p in (P/'src/main/java').rglob('*.java'))
checks={
 'auth registro':'@PostMapping("/registro")','auth login':'@PostMapping("/login")','auth refresh':'@PostMapping("/refresh")',
 'becas base':'@RequestMapping("/api/v1/becas")','alumnos base':'@RequestMapping("/api/v1/alumnos")',
 'solicitudes base':'@RequestMapping("/api/v1/solicitudes")','usuarios base':'@RequestMapping("/api/v1/usuarios")',
 'docs base':'@RequestMapping("/api/v1/solicitudes/{solicitudId}/documentos")',
 'alumno reto':'@GetMapping("/{alumnoId}/solicitudes")','stats reto':'@GetMapping("/solicitudes-por-estado")'
}
for name,marker in checks.items():
    if marker not in allmain: fail(f'missing endpoint marker {name}: {marker}')
for marker in ['CIUDADANO','GESTOR','ADMIN','USER','CONSULTOR','TAMANO_MAXIMO = 10L * 1024 * 1024','"application/pdf", "image/jpeg", "image/png"','private String codigo;']:
    if marker not in allmain: fail(f'missing rule marker {marker}')
security=(P/'src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java').read_text(encoding='utf-8')
if '/api/v1/auth/**' in security: fail('SecurityConfig still permits /api/v1/auth/**')
if '/api/v1/public/**' in security: fail('SecurityConfig contains nonexistent /api/v1/public/**')
for ep in ['/api/v1/auth/registro','/api/v1/auth/login','/api/v1/auth/refresh']:
    if ep not in security: fail(f'SecurityConfig missing explicit auth allowlist endpoint {ep}')

# JSON and Postman
for p in list((P/'postman').glob('*.json'))+[trace_path,ROOT/'.course/source-audit/M7_SOURCE_INVENTORY.json']:
    try: json.loads(p.read_text(encoding='utf-8'))
    except Exception as e: fail(f'JSON invalid {p}: {e}')
post='\n'.join(p.read_text(encoding='utf-8') for p in (P/'postman').glob('*.json'))
if '/api/v1/auth/logout' in post or '"name": "Logout"' in post: fail('Postman contains nonexistent logout')

# Final PDFs
for pdf_name in ['M7_TEORIA.pdf','M7_PRACTICA.pdf']:
    pdf=M7/pdf_name
    if not pdf.exists(): fail(f'missing final PDF {pdf_name}')
    else:
        b=pdf.read_bytes()
        if len(b)<50_000 or not b.startswith(b'%PDF-') or b'%%EOF' not in b[-4096:]:
            fail(f'invalid or incomplete PDF {pdf_name}')

# Tests
ntests=sum(p.read_text(encoding='utf-8').count('@Test') for p in (P/'src/test/java').rglob('*.java'))
test_classes=len(list((P/'src/test/java').rglob('*.java')))
if ntests!=35: fail(f'static @Test markers {ntests} != expected R6 candidate 35')
if tr['counts']['test_classes']!=test_classes: fail(f'trace test_classes {tr["counts"]["test_classes"]} != actual {test_classes}')
if any('@MockBean' in p.read_text(encoding='utf-8') for p in (P/'src/test/java').rglob('*.java')): fail('@MockBean present; use @MockitoBean')

if errors:
    print('M7 R6 STATIC: FAIL')
    for e in errors: print(' -',e)
    sys.exit(1)
print('M7 R6 STATIC: PASS')
print(' - standalone project: YES')
print(f' - design blocks: {blocks}')
print(f' - original practice steps: {steps}')
print(f' - standalone prep steps: {preps}')
print(f' - project files: {len(actual)}, NO_MAGIC_FILES PASS')
print(f' - Java main classes: {len(list((P/"src/main/java").rglob("*.java")))}')
print(f' - test classes: {test_classes}')
print(f' - static @Test markers: {ntests}')
print(' - Maven/JaCoCo R6: PENDING Windows execution')
