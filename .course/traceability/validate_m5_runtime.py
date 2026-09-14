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
    raise SystemExit('Uso: validate_m5_runtime.py <repo-root>')
root = Path(sys.argv[1]).resolve()
project = root / 'M5' / 'proyecto'
jar = project / 'target' / 'mi-proyecto-0.0.1-SNAPSHOT.jar'
if not jar.exists():
    raise SystemExit(f'ERROR: no existe JAR: {jar}')

log_path = Path(tempfile.gettempdir()) / 'spring-boot-m5-runtime.log'
log = open(log_path, 'wb')
proc = subprocess.Popen(
    ['java', '-jar', str(jar)],
    cwd=project,
    stdout=log,
    stderr=subprocess.STDOUT,
)


def request(path, method='GET', body=None, headers=None, timeout=5):
    req = urllib.request.Request(
        'http://127.0.0.1:8080' + path,
        data=None if body is None else body.encode('utf-8'),
        method=method,
        headers=headers or {},
    )
    try:
        with urllib.request.urlopen(req, timeout=timeout) as response:
            return (
                response.status,
                response.read().decode('utf-8'),
                {k.lower(): v for k, v in response.headers.items()},
            )
    except urllib.error.HTTPError as error:
        return (
            error.code,
            error.read().decode('utf-8'),
            {k.lower(): v for k, v in error.headers.items()},
        )


def assert_trace(error):
    trace_id = error.get('traceId')
    assert isinstance(trace_id, str) and trace_id.strip(), error


try:
    ready = False
    for _ in range(60):
        if proc.poll() is not None:
            break
        try:
            status, _, _ = request('/hola', timeout=2)
            if status == 200:
                ready = True
                break
        except Exception:
            pass
        time.sleep(1)
    if not ready:
        log.flush()
        tail = log_path.read_text(encoding='utf-8', errors='replace')[-6000:]
        raise AssertionError(
            'La aplicación no quedó disponible. Log final:\n' + tail)

    status, text, _ = request('/hola')
    assert status == 200 and text == 'Hola, Ministerio de Educación', (status, text)

    status, text, _ = request('/api/v1/alumnos?page=0&size=20')
    assert status == 200, (status, text)
    alumnos = json.loads(text)
    assert len(alumnos['content']) == 2, alumnos
    assert alumnos['totalElements'] == 2 and alumnos['totalPages'] == 1, alumnos

    status, text, _ = request('/api/v1/info')
    assert status == 200 and json.loads(text)['entorno'] == 'dev', (status, text)

    duplicate = json.dumps({
        'nombre': 'Otra Ana',
        'apellidos': 'Duplicada',
        'dni': '12345678A',
        'fechaNacimiento': '2010-05-12',
        'curso': '5º Primaria',
    }, ensure_ascii=False)
    status, text, _ = request(
        '/api/v1/alumnos',
        method='POST',
        body=duplicate,
        headers={'Content-Type': 'application/json; charset=utf-8'},
    )
    assert status == 409, (status, text)
    error = json.loads(text)
    assert error['codigo'] == 'RECURSO_DUPLICADO', error
    assert error['status'] == 409 and error['path'] == '/api/v1/alumnos', error
    assert_trace(error)

    invalid = json.dumps({
        'nombre': '',
        'apellidos': '',
        'dni': '1',
        'fechaNacimiento': None,
        'curso': '',
    })
    status, text, _ = request(
        '/api/v1/alumnos',
        method='POST',
        body=invalid,
        headers={'Content-Type': 'application/json'},
    )
    assert status == 400, (status, text)
    error = json.loads(text)
    assert error['codigo'] == 'VALIDACION' and error['status'] == 400, error
    assert isinstance(error.get('errors'), list) and len(error['errors']) >= 4, error
    assert_trace(error)

    status, text, _ = request('/api/v1/ficheros/manual.pdf')
    assert status == 404, (status, text)
    error = json.loads(text)
    assert error['codigo'] == 'FICHERO_NO_ENCONTRADO', error
    assert_trace(error)

    allowed_origin = 'http://localhost:3000'
    status, _, headers = request(
        '/api/v1/alumnos?page=0&size=1',
        headers={'Origin': allowed_origin},
    )
    assert status == 200, status
    assert headers.get('access-control-allow-origin') == allowed_origin, headers

    status, _, headers = request(
        '/api/v1/alumnos',
        method='OPTIONS',
        headers={
            'Origin': allowed_origin,
            'Access-Control-Request-Method': 'POST',
            'Access-Control-Request-Headers': 'Content-Type',
        },
    )
    assert status in {200, 204}, status
    assert headers.get('access-control-allow-origin') == allowed_origin, headers
    methods = headers.get('access-control-allow-methods', '')
    assert 'POST' in methods and 'GET' in methods, headers

    status, text, headers = request(
        '/api/v1/alumnos?page=0&size=1',
        headers={'Origin': 'http://localhost:9999'},
    )
    # Spring MVC 6.2 rechaza explícitamente el origen no autorizado
    # mediante DefaultCorsProcessor y responde 403 "Invalid CORS request".
    # El requisito funcional es doble: rechazo HTTP + ausencia de ACAO.
    assert status == 403, (status, text)
    assert 'access-control-allow-origin' not in headers, headers

    status, text, _ = request('/v3/api-docs')
    assert status == 200, (status, text[:500])
    paths = json.loads(text)['paths']
    for path in [
        '/api/v1/alumnos',
        '/api/v1/alumnos/{id}',
        '/api/v1/cursos',
        '/api/v1/expedientes',
        '/api/v1/ficheros/{nombre}',
    ]:
        assert path in paths, (path, list(paths))

    print('M5 RUNTIME/OPENAPI/CORS: PASS')
finally:
    if proc.poll() is None:
        proc.terminate()
        try:
            proc.wait(timeout=10)
        except subprocess.TimeoutExpired:
            proc.kill()
            proc.wait(timeout=10)
    log.close()
    if os.environ.get('M5_KEEP_RUNTIME_LOG') != '1':
        for intento in range(20):
            try:
                log_path.unlink(missing_ok=True)
                break
            except PermissionError:
                if intento == 19:
                    print(
                        'M5 RUNTIME: aviso: no se pudo borrar el log temporal '
                        f'{log_path}; la validación funcional ya había pasado.',
                        file=sys.stderr,
                    )
                else:
                    time.sleep(0.25)
            except OSError as error:
                print(
                    'M5 RUNTIME: aviso al limpiar el log temporal: '
                    f'{error}',
                    file=sys.stderr,
                )
                break
