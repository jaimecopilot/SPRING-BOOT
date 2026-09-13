#!/usr/bin/env python3
from pathlib import Path
import json,re,subprocess,sys
root=Path(sys.argv[1] if len(sys.argv)>1 else '.').resolve()
fail=[]
def bad(x): fail.append(x)
# generic contract
generic=root/'.course/traceability/validate_module.py'
if generic.exists():
 cp=subprocess.run([sys.executable,str(generic),'M2',str(root)],text=True,capture_output=True)
 if cp.returncode: bad('generic module validation failed:\n'+(cp.stdout+cp.stderr).strip())
manifest=json.loads((root/'.course/traceability/M2.json').read_text(encoding='utf-8'))
theory=(root/'M2/TEORIA.md').read_text(encoding='utf-8')
practice=(root/'M2/PRACTICA.md').read_text(encoding='utf-8')
if manifest.get('status')!='COMPLETE': bad('M2 must be COMPLETE')
if manifest.get('current_traced_steps')!=69: bad('M2 must expose 69 traced steps')
if len(manifest.get('theory_concepts',[]))!=30: bad('M2 must expose 30 theory concepts')
if len(manifest.get('step_manifests',[]))!=6: bad('M2 must expose six practice manifests')
if re.findall(r'^# Punto (2\.\d+) - ',theory,re.M)!=[f'2.{i}' for i in range(1,7)]: bad('theory must contain points 2.1-2.6 exactly once')
if re.findall(r'^# Punto (2\.\d+) - ',practice,re.M)!=[f'2.{i}' for i in range(1,7)]: bad('practice must contain points 2.1-2.6 exactly once')
if len(re.findall(r'^## Bloque ',theory,re.M))!=30: bad('theory must contain 30 blocks')
if len(re.findall(r'^## Paso \d+ - ',practice,re.M))!=69: bad('practice must contain 69 steps')
# final feature-oriented project
required=[
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java',
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java',
'M2/proyecto/config/checkstyle/checkstyle.xml','M2/proyecto/README.md',
'M2/proyecto/src/main/resources/application-dev.properties','M2/proyecto/src/main/resources/application-prod.properties']
for p in required:
 if not (root/p).exists(): bad('missing final M2 artifact: '+p)
for old in ['controller/AlumnoController.java','controller/ExpedienteController.java','controller/SaludoController.java','dto/AlumnoDTO.java','dto/ExpedienteDTO.java','dto/SolicitanteDTO.java','service/AlumnoService.java','service/ExpedienteService.java','repository/AlumnoRepository.java','repository/ExpedienteRepository.java','exception/NegocioException.java']:
 if (root/'M2/proyecto/src/main/java/es/mecd/demo/miproyecto'/old).exists(): bad('old layer-oriented artifact remains: '+old)
# high-value contracts
checks={
'M2/proyecto/pom.xml':['3.5.16','<java.version>17</java.version>','springdoc-openapi-starter-webmvc-ui','2.8.13','maven-checkstyle-plugin','3.6.0'],
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java':['ConcurrentHashMap','AtomicInteger','Optional<AlumnoDTO>','siguienteIdentificador','List.copyOf'],
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java':['NegocioException','repositorio.existePorDni','repositorio.siguienteIdentificador','promocionar'],
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java':['@Configuration','CommandLineRunner','alumnos.contar() == 0','expedientes.contar() == 0'],
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java':['Jackson2ObjectMapperBuilderCustomizer','WRITE_DATES_AS_TIMESTAMPS'],
'M2/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java':['@WebMvcTest','@MockitoBean','MockMvc','Location'],
'M2/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java':['DateTimeFormatter','dd/MM/yyyy'],
}
for p,tokens in checks.items():
 text=(root/p).read_text(encoding='utf-8')
 for token in tokens:
  if token not in text: bad(f'{p} missing token: {token}')
for token in ['ConcurrentHashMap','Optional','@Repository','@Configuration','@Bean','CommandLineRunner','@Value','@MockitoBean','MockMvc','Checkstyle','OpenAPI','FechasUtil']:
 if token.lower() not in (theory+'\n'+practice).lower(): bad('student material lost high-value source concept: '+token)
for marker in ['filecite','turn6','.course/tmp/']:
 if marker in theory or marker in practice: bad('internal marker leaked to student material: '+marker)
if (root/'.course/tmp/M2_23_TEORIA_APPEND.md').exists(): bad('temporary M2.3 append remains')
if fail:
 print('M2 FINAL CONTRACT: FAIL')
 for x in fail: print(' -',x)
 raise SystemExit(1)
print('M2 FINAL CONTRACT: PASS | COMPLETE | theory=30 | steps=69/69')
