from pathlib import Path
import json
import os
import subprocess
import sys
import tempfile
import time
import urllib.error
import urllib.request

if len(sys.argv) != 2:
    raise SystemExit('Uso: validate_m4_runtime.py <repo-root>')
root = Path(sys.argv[1]).resolve()
project = root / 'M4' / 'proyecto'
jar = project / 'target' / 'mi-proyecto-0.0.1-SNAPSHOT.jar'
if not jar.exists():
    raise SystemExit(f'ERROR: no existe JAR: {jar}')
log_path = Path(tempfile.gettempdir()) / 'spring-boot-m4-runtime.log'
log = open(log_path, 'wb')
proc = subprocess.Popen(['java', '-jar', str(jar)], cwd=project, stdout=log, stderr=subprocess.STDOUT)

def request(path, method='GET', body=None, headers=None, timeout=5):
    req = urllib.request.Request(
        'http://127.0.0.1:8080' + path,
        data=None if body is None else body.encode('utf-8'),
        method=method,
        headers=headers or {},
    )
    try:
        with urllib.request.urlopen(req, timeout=timeout) as response:
            return response.status, response.read().decode('utf-8')
    except urllib.error.HTTPError as error:
        return error.code, error.read().decode('utf-8')

try:
    ready = False
    for _ in range(60):
        if proc.poll() is not None:
            break
        try:
            status, _ = request('/hola', timeout=2)
            if status == 200:
                ready = True
                break
        except Exception:
            pass
        time.sleep(1)
    if not ready:
        log.flush()
        tail = log_path.read_text(encoding='utf-8', errors='replace')[-5000:]
        raise AssertionError('La aplicación no quedó disponible. Log final:\n' + tail)

    status, text = request('/api/v1/alumnos?page=0&size=20')
    assert status == 200, (status, text)
    alumnos = json.loads(text)
    assert len(alumnos['content']) == 2, alumnos
    assert alumnos['totalElements'] == 2 and alumnos['totalPages'] == 1, alumnos

    status, text = request('/api/v1/cursos')
    assert status == 200, (status, text)
    cursos = json.loads(text)
    assert len(cursos) == 2, cursos

    status, text = request('/api/v1/alumnos/request-info?x=1')
    assert status == 200 and json.loads(text)['method'] == 'GET', (status, text)

    status, text = request('/api/v1/info')
    assert status == 200 and json.loads(text)['entorno'] == 'dev', (status, text)

    status, text = request('/v3/api-docs')
    assert status == 200, (status, text[:500])
    paths = json.loads(text)['paths']
    for path in ['/api/v1/alumnos', '/api/v1/alumnos/{id}', '/api/v1/cursos', '/api/v1/expedientes']:
        assert path in paths, (path, list(paths))

    status, _ = request(
        '/api/v1/alumnos',
        method='POST',
        body='{"nombre":""}',
        headers={'Content-Type': 'application/json'},
    )
    assert status == 400, status
    print('M4 RUNTIME/OPENAPI: PASS')
finally:
    if proc.poll() is None:
        proc.terminate()
        try:
            proc.wait(timeout=10)
        except subprocess.TimeoutExpired:
            proc.kill()
            proc.wait(timeout=10)
    log.close()
    if os.environ.get('M4_KEEP_RUNTIME_LOG') != '1':
        # Windows puede mantener el handle del log unos instantes después de
        # terminar la JVM. La limpieza de un temporal nunca debe convertir
        # una validación funcional correcta en un fallo de publicación.
        for intento in range(20):
            try:
                log_path.unlink(missing_ok=True)
                break
            except PermissionError:
                if intento == 19:
                    print(
                        'M4 RUNTIME: aviso: no se pudo borrar el log temporal '
                        f'{log_path}; la validación funcional ya había pasado.',
                        file=sys.stderr,
                    )
                else:
                    time.sleep(0.25)
            except OSError as error:
                print(
                    'M4 RUNTIME: aviso al limpiar el log temporal: '
                    f'{error}',
                    file=sys.stderr,
                )
                break
