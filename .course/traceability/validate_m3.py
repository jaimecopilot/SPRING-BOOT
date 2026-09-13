#!/usr/bin/env python3
from pathlib import Path
import json
import re
import subprocess
import sys

root = Path(sys.argv[1] if len(sys.argv) > 1 else '.').resolve()
fail = []

def bad(message):
    fail.append(message)

# Reuse the common structural/traceability contract first.
generic = root / '.course/traceability/validate_module.py'
if generic.exists():
    cp = subprocess.run(
        [sys.executable, str(generic), 'M3', str(root)],
        text=True,
        capture_output=True,
    )
    if cp.returncode:
        bad('generic module validation failed:\n' + (cp.stdout + cp.stderr).strip())

manifest = json.loads((root / '.course/traceability/M3.json').read_text(encoding='utf-8'))
theory = (root / 'M3/TEORIA.md').read_text(encoding='utf-8')
practice = (root / 'M3/PRACTICA.md').read_text(encoding='utf-8')

if manifest.get('status') != 'COMPLETE':
    bad('M3 must be COMPLETE')
if manifest.get('current_traced_steps') != 72:
    bad('M3 must expose 72 traced steps')
if len(manifest.get('theory_concepts', [])) != 30:
    bad('M3 must expose 30 theory concepts')
if len(manifest.get('step_manifests', [])) != 6:
    bad('M3 must expose six practice manifests')

expected_points = [f'3.{i}' for i in range(1, 7)]
if re.findall(r'^# Punto (3\.\d+) - ', theory, re.M) != expected_points:
    bad('theory must contain points 3.1-3.6 exactly once')
if re.findall(r'^# Práctica (3\.\d+) - ', practice, re.M) != expected_points:
    bad('practice must contain practices 3.1-3.6 exactly once')
if len(re.findall(r'^## Bloque ', theory, re.M)) != 30:
    bad('theory must contain 30 blocks')
if len(re.findall(r'^## Paso \d+ - ', practice, re.M)) != 72:
    bad('practice must contain 72 steps')

required = [
    'M3/proyecto/pom.xml',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java',
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java',
    'M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java',
    'M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java',
    'M3/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java',
    'M3/postman/M3.postman_collection.json',
    'M3/postman/M3.local.postman_environment.json',
]
for rel in required:
    if not (root / rel).exists():
        bad('missing final M3 artifact: ' + rel)

checks = {
    'M3/proyecto/pom.xml': [
        '3.5.16', '<java.version>17</java.version>',
        'spring-boot-starter-validation', 'springdoc-openapi-starter-webmvc-ui',
        '2.8.13', 'maven-checkstyle-plugin', '3.6.0',
    ],
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java': [
        'ServletUriComponentsBuilder', 'DateTimeFormat', '@CookieValue',
        '@RequestHeader', 'eliminarCascada', '/request-info',
    ],
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java': [
        'fechaDesde', 'fechaHasta', 'existePorDniYIdDistinto',
        'eliminarEnCascada', 'LocalDate.parse', 'toResponse',
    ],
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java': [
        '@NotBlank', '@Size', '@NotNull', '@Past',
    ],
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GlobalExceptionHandler.java': [
        '@RestControllerAdvice', 'MethodArgumentNotValidException',
        'HttpMessageNotReadableException', 'NegocioException',
    ],
    'M3/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java': [
        '@Valid', '@NotNull', 'SolicitanteDTO',
    ],
    'M3/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java': [
        '@WebMvcTest', '@MockitoBean', 'MockMvc', 'Location',
    ],
    'M3/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java': [
        '@WebMvcTest', '@MockitoBean', 'validaDtoAnidado',
    ],
    'M3/postman/M3.postman_collection.json': ['GET alumnos', 'pm.test'],
    'M3/postman/M3.local.postman_environment.json': ['baseUrl'],
}
for rel, tokens in checks.items():
    text = (root / rel).read_text(encoding='utf-8')
    for token in tokens:
        if token not in text:
            bad(f'{rel} missing token: {token}')

high_value = [
    '@RequestParam', '@PathVariable', 'Stream', 'skip(', 'limit(',
    'ServletUriComponentsBuilder', 'Location', '@RequestBody', '201',
    'PUT', 'PATCH', 'DELETE', 'soft delete', '@Valid', '@NotBlank',
    'MethodArgumentNotValidException', '@CookieValue', '@RequestHeader', 'Postman',
]
student = theory + '\n' + practice
for token in high_value:
    if token.lower() not in student.lower():
        bad('student material lost high-value source concept: ' + token)

for marker in ['filecite', '.course/traceability', 'GUIDE/INHERITED/SUPPORT']:
    if marker in theory or marker in practice:
        bad('internal marker leaked to student material: ' + marker)

# Protect quality properties that caused earlier M2 failures.
for path in (root / 'M3/proyecto/src').rglob('*.java'):
    for number, line in enumerate(path.read_text(encoding='utf-8').splitlines(), 1):
        if len(line) > 120:
            bad(f'Java line >120: {path.relative_to(root)}:{number}')
        if line != line.rstrip():
            bad(f'trailing whitespace: {path.relative_to(root)}:{number}')

if fail:
    print('M3 FINAL CONTRACT: FAIL')
    for item in fail:
        print(' -', item)
    raise SystemExit(1)

print('M3 FINAL CONTRACT: PASS | COMPLETE | theory=30 | steps=72/72')
