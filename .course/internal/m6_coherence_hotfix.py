from pathlib import Path
import argparse
import re

PRACTICA = Path("M6/PRACTICA.md")
TRAZA = Path("M6/TRAZABILIDAD.md")


def replace_once(text: str, old: str, new: str, label: str) -> str:
    count = text.count(old)
    if count != 1:
        raise SystemExit(f"ERROR {label}: esperado 1 bloque, encontrados {count}")
    return text.replace(old, new, 1)


def apply() -> None:
    s = PRACTICA.read_text(encoding="utf-8")

    old_64 = '''**Prueba.**

```bash
curl -i -X PUT -u admin:admin123 \\
  http://localhost:8080/api/v1/admin/usuarios/2/roles \\
  -H "Content-Type: application/json" \\
  -d '{"roles":["USER","GESTOR"]}'
```

El usuario debe quedar con el conjunto exacto de roles solicitado.

Prueba negativa:

```bash
curl -i -X PUT -u ana:ana123 \\
  http://localhost:8080/api/v1/admin/usuarios/2/roles \\
  -H "Content-Type: application/json" \\
  -d '{"roles":["ADMIN"]}'
```

La petición debe devolver 403.'''

    new_64 = '''En el estado acumulativo de 6.4 sólo están persistidos los roles `USER` y `ADMIN`. El usuario y el rol `GESTOR` que existieron temporalmente en memoria en 6.2 no se materializan en base de datos hasta 6.8. Por tanto, las pruebas de este snapshot deben usar únicamente roles que realmente existen en 6.4.

**Prueba negativa primero.** Ana (`id=2`) todavía tiene sólo `USER`, así que comprobamos el 403 antes de modificar sus roles:

```bash
curl -i -X PUT -u ana:ana123 \\
  http://localhost:8080/api/v1/admin/usuarios/2/roles \\
  -H "Content-Type: application/json" \\
  -d '{"roles":["ADMIN"]}'
```

La petición debe devolver `403 Forbidden` y no debe modificar los roles del usuario.

**Prueba positiva.** Ahora el administrador asigna al usuario 2 los dos roles que existen en este snapshot:

```bash
curl -i -X PUT -u admin:admin123 \\
  http://localhost:8080/api/v1/admin/usuarios/2/roles \\
  -H "Content-Type: application/json" \\
  -d '{"roles":["USER","ADMIN"]}'
```

La respuesta debe ser `200 OK` y el usuario debe quedar con `ADMIN` y `USER` (la respuesta puede mostrarlos ordenados). `GESTOR` se probará de forma persistente a partir de 6.8.'''

    s = replace_once(s, old_64, new_64, "6.4.12")

    s = replace_once(
        s,
        "Obtén refresh, úsalo una vez y comprueba que devuelve un par nuevo. Reutiliza el antiguo: 401.",
        "Obtén refresh, úsalo una vez y comprueba que devuelve un par nuevo. Reutiliza el antiguo: **400 `TOKEN_INVALIDO`**, que es el contrato aplicado por `AuthExceptionHandler` a los errores de refresh.",
        "6.8.8 refresh",
    )

    s = replace_once(
        s,
        '.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()',
        '.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()',
        "6.8.3 swagger",
    )

    old_init = '''Rol rolGestor = obtenerOCrearRol("GESTOR", "Gestor");
Usuario gestor = obtenerOCrearUsuario(
        "gestor", "gestor123", "gestor@educacion.gob.es");
gestor.getRoles().add(rolUser);
gestor.getRoles().add(rolGestor);
usuarioRepository.save(gestor);'''
    new_init = '''Rol rolGestor = obtenerOCrearRol(
        rolRepository, "GESTOR", "Gestor");
Usuario gestor = obtenerOCrearUsuario(
        usuarioRepository, "gestor", "gestor@educacion.gob.es");
prepararUsuario(
        gestor, "gestor123", passwordEncoder, rolUser, rolGestor);
usuarioRepository.save(gestor);'''
    s = replace_once(s, old_init, new_init, "6.8.5 initializer")

    PRACTICA.write_text(s, encoding="utf-8")

    ts = TRAZA.read_text(encoding="utf-8")
    marker = "## 9. Correcciones de coherencia runtime - 2026-09-17"
    if marker not in ts:
        ts += '''

## 9. Correcciones de coherencia runtime - 2026-09-17

- `6.4.12`: el ejemplo ejecutable usaba `GESTOR`, aunque el estado acumulativo 6.4 sólo persiste `USER` y `ADMIN` y la propia práctica retrasa la materialización de `GESTOR` hasta 6.8. Se corrige la prueba positiva para usar `USER` + `ADMIN` y se ejecuta primero la prueba negativa con Ana para evitar elevarla a ADMIN antes de comprobar el 403. El endpoint, DTO, servicio y snapshot 6.4 no cambian.
- `6.8.3`: el snippet documental del `SecurityFilterChain` omitía `/swagger-ui.html`; se alinea con el código ejecutable 6.8/6.9, que ya lo permite junto con `/swagger-ui/**` y `/v3/api-docs/**`.
- `6.8.5`: el snippet de inicialización de `GESTOR` se alinea con las firmas reales idempotentes de `UsuariosInicialesConfig` (`obtenerOCrearRol`, `obtenerOCrearUsuario` y `prepararUsuario`). No se adelanta `GESTOR`: sigue apareciendo persistentemente por primera vez en 6.8.
- `6.8.8`: la reutilización de un refresh consumido se documentaba como 401; el contrato real definido desde 6.6 y aplicado por `AuthExceptionHandler` es HTTP 400 con `codigo=TOKEN_INVALIDO`. Se corrige la expectativa documental.
- Estas reconciliaciones no crean pasos nuevos ni cambian la cobertura estructural: M6 conserva 9/9 puntos y 112/112 pasos.
'''
        TRAZA.write_text(ts, encoding="utf-8")


def gates() -> None:
    p = PRACTICA.read_text(encoding="utf-8")
    assert '-d \'{"roles":["USER","GESTOR"]}\'' not in p
    assert '-d \'{"roles":["USER","ADMIN"]}\'' in p
    assert 'Reutiliza el antiguo: **400 `TOKEN_INVALIDO`**' in p
    assert 'Reutiliza el antiguo: 401.' not in p
    assert '.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()' in p

    for n in ("6.4", "6.5", "6.6", "6.7"):
        x = Path(f"M6/{n}/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java").read_text(encoding="utf-8")
        assert 'new Rol("GESTOR"' not in x
        assert '"gestor", "gestor123"' not in x

    x = Path("M6/6.8/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java").read_text(encoding="utf-8")
    assert '"GESTOR"' in x
    assert '"gestor", "gestor@educacion.gob.es"' in x

    h = Path("M6/6.8/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java").read_text(encoding="utf-8")
    assert "HttpStatus.BAD_REQUEST" in h and '"TOKEN_INVALIDO"' in h
    print("STATIC COHERENCE: PASS")


def update_pages(pages: int) -> None:
    s = TRAZA.read_text(encoding="utf-8")
    s, n = re.subn(
        r"PDF: 72 páginas teoría \+ \d+ páginas práctica,",
        f"PDF: 72 páginas teoría + {pages} páginas práctica,",
        s,
        count=1,
    )
    if n != 1:
        raise SystemExit("ERROR: no se pudo actualizar el contador de páginas de práctica")
    TRAZA.write_text(s, encoding="utf-8")


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--pages", type=int)
    parser.add_argument("--gates-only", action="store_true")
    args = parser.parse_args()
    if args.pages is not None:
        update_pages(args.pages)
    elif args.gates_only:
        gates()
    else:
        apply()
        gates()
