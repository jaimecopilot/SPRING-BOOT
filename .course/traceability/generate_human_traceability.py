#!/usr/bin/env python3
from pathlib import Path
import argparse, json, sys

ROOT = Path(__file__).resolve().parents[2]
MANIFEST = ROOT / ".course/traceability/M0.json"
INTERNAL = ROOT / ".course/traceability/TRAZABILIDAD_M0.md"

def esc(s):
    return str(s).replace("|", "\\|").replace("\n", " ")

def rel_link(path, base):
    p = Path(path)
    target = ROOT / p
    try:
        rel = target.relative_to(ROOT / base)
        return rel.as_posix()
    except ValueError:
        import os
        return os.path.relpath(target, ROOT / base).replace("\\", "/")

def linked(path, base):
    label = path
    href = rel_link(path, base)
    return f"[`{label}`]({href})"

def step_display(step):
    if step["practice"] == "FINAL":
        return step["id"].replace("M0-P-FINAL-", "FINAL-")
    return f'{step["practice"]}.{step["number"]}'

def render(base):
    m = json.loads(MANIFEST.read_text(encoding="utf-8"))
    steps = []
    for rel in m["step_manifests"]:
        steps.extend(json.loads((ROOT / rel).read_text(encoding="utf-8"))["steps"])
    arts = m["artifacts"]
    theory = m["theory_concepts"]

    out = []
    out += [
        "# Trazabilidad humana — M0",
        "",
        "> Generado automáticamente desde `M0.json`, las guías y el árbol real del proyecto. "
        "No crea una segunda fuente de verdad: traduce el contrato máquina-legible a una vista auditable por una persona.",
        "",
        "La filosofía mínima de esta vista toma como referencia histórica "
        "[`E1/TRAZABILIDAD.md` del repositorio anterior]"
        "(https://github.com/jaimecopilot/CURSO-SPRING-BOOT-2026/blob/main/E1/TRAZABILIDAD.md), "
        "y la amplía con teoría, acciones, símbolos, comandos y observables por paso.",
        "",
        "## Cómo puedes auditarlo tú",
        "",
        f"1. Abre la [guía práctica]({rel_link('M0/PRACTICA.md', base)}) y elige cualquier paso.",
        "2. Busca su identificador en **Guía → proyecto** y sigue los enlaces de artefactos, símbolos, comandos y verificación.",
        f"3. Abre la [teoría]({rel_link('M0/TEORIA.md', base)}) y comprueba los conceptos enlazados en la columna **Teoría**.",
        "4. Usa **Proyecto → guía** para partir de cualquier fichero o package docente del snapshot final y averiguar por qué existe.",
        "5. Revisa **Artefactos necesarios no introducidos en este módulo** para distinguir soporte de contenido docente.",
        "6. CI ejecuta el validador de trazabilidad, regenera esta vista en modo `--check`, compila, prueba, empaqueta y llama a los endpoints reales.",
        "",
        "## Resumen verificable",
        "",
        "| Dato | Valor |",
        "|---|---:|",
        f"| Esquema | v{m['schema_version']} |",
        f"| Conceptos teóricos declarados | {len(theory)} |",
        f"| Pasos prácticos declarados | {len(steps)} |",
        f"| `PERMANENT` | {sum(1 for s in steps if s['state']=='PERMANENT')} |",
        f"| `TEMPORARY` | {sum(1 for s in steps if s['state']=='TEMPORARY')} |",
        f"| Recorridos de entorno | {len(m['environment_workflows'])} |",
        f"| Artefactos `GUIDE` | {sum(1 for a in arts if a['classification']=='GUIDE')} |",
        f"| Artefactos `INHERITED` | {sum(1 for a in arts if a['classification']=='INHERITED')} |",
        f"| Artefactos `SUPPORT` | {sum(1 for a in arts if a['classification']=='SUPPORT')} |",
        f"| Artefactos funcionales sin clasificar | 0 (condición exigida por el validador) |",
        "",
        "## Teoría → práctica",
        "",
        "| Concepto | Ancla en teoría | Pasos que lo materializan |",
        "|---|---|---|",
    ]
    for t in theory:
        refs = [step_display(s) for s in steps if t["id"] in s["theory_refs"]]
        out.append(f"| `{t['id']}` | {esc(t['anchor'])} | {', '.join('`'+r+'`' for r in refs) or '—'} |")

    out += [
        "",
        "## Guía → proyecto",
        "",
        "| Paso | Qué enseña/cambia | Teoría | Acción / código / símbolo | Comando | Resultado observable | Cómo se comprueba | Estado |",
        "|---|---|---|---|---|---|---|---|",
    ]
    for s in steps:
        theory_cell = "<br>".join(f"`{x}`" for x in s["theory_refs"])
        arows = []
        for a in s["artifacts"]:
            sym = ", ".join(f"`{x}`" for x in a.get("symbols", []))
            p = linked(a["path"], base)
            arows.append(f"**{a['action']}** {p}" + (f"<br>{sym}" if sym else "") + (f"<br>_{a['state']}_" if a.get("state") else ""))
        action_cell = "<br>".join(arows) if arows else "Observación/operación sin cambio persistente de fichero"
        cmd_cell = "<br>".join(f"`{esc(x)}`" for x in s["commands"]) or "—"
        obs_cell = "<br>".join(esc(x) for x in s["observables"]) or "—"
        ver_cell = "<br>".join(esc(x) for x in s["verification"]) or "—"
        state = "🟡 `TEMPORARY` (cerrado/restaurado)" if s["state"] == "TEMPORARY" else "✅ `PERMANENT`"
        out.append(
            f"| `{step_display(s)}`<br>`{s['id']}` | {esc(s['summary'])} | {theory_cell} | "
            f"{action_cell} | {cmd_cell} | {obs_cell} | {ver_cell} | {state} |"
        )

    out += [
        "",
        "## Proyecto → guía",
        "",
        "| Artefacto | Clasificación | Origen | Evolución | Símbolos relevantes | Quién lo usa | Qué se rompe si falta |",
        "|---|---|---|---|---|---|---|",
    ]
    for a in arts:
        p = linked(a["path"], base)
        origin = f"`{a['origin']}`" if a.get("origin") else "—"
        evo = "<br>".join(f"`{x}`" for x in a.get("evolution", [])) or "—"
        syms = "<br>".join(f"`{x}`" for x in a.get("symbols", [])) or "—"
        users = "<br>".join(esc(x) for x in a.get("used_by", [])) or "—"
        out.append(f"| {p} | `{a['classification']}` | {origin} | {evo} | {syms} | {users} | {esc(a.get('missing_effect','—'))} |")

    support = [a for a in arts if a["classification"] in ("SUPPORT","INHERITED")]
    out += [
        "",
        "## Artefactos necesarios no introducidos en este módulo",
        "",
        "M0 no hereda código funcional de un módulo anterior. Los elementos de esta sección son soporte de repositorio, auditoría o pruebas y no introducen comportamiento público no enseñado.",
        "",
        "| Artefacto | Tipo | Motivo / usuario | Si faltara |",
        "|---|---|---|---|",
    ]
    for a in support:
        p = linked(a["path"], base)
        why = "; ".join(a.get("used_by", [])) or "soporte interno"
        out.append(f"| {p} | `{a['classification']}` | {esc(why)} | {esc(a.get('missing_effect','—'))} |")

    out += [
        "",
        "## Trazabilidad inversa por artefacto/símbolo",
        "",
        "Esta vista permite partir del snapshot final y localizar los pasos que crean, modifican, usan, verifican, eliminan o restauran cada pieza.",
        "",
        "| Artefacto | Símbolo | Pasos relacionados |",
        "|---|---|---|",
    ]
    pairs = {}
    for s in steps:
        for a in s["artifacts"]:
            keypath = a["path"]
            syms = a.get("symbols") or ["(artefacto completo)"]
            for sym in syms:
                pairs.setdefault((keypath, sym), []).append(f"{step_display(s)}:{a['action']}")
    for (path, sym), refs in sorted(pairs.items()):
        p = linked(path, base)
        out.append(f"| {p} | `{esc(sym)}` | " + ", ".join(f"`{r}`" for r in refs) + " |")

    temporary = [s for s in steps if s["state"] == "TEMPORARY"]
    out += [
        "",
        "## Estados temporales y restauraciones",
        "",
        "| Paso | Estado temporal | Cierre / restauración / evidencia final |",
        "|---|---|---|",
    ]
    for s in temporary:
        temps=[]
        closes=[]
        for a in s["artifacts"]:
            text=f"{a['action']} `{a['path']}`" + (": "+", ".join(f"`{x}`" for x in a.get("symbols",[])) if a.get("symbols") else "")
            if a["action"] in {"CREATE","MODIFY"} and a.get("state")=="TEMPORARY":
                temps.append(text)
            if a["action"] in {"RESTORE","DELETE"}:
                closes.append(text)
        close_cell="<br>".join(closes) or "<br>".join(esc(x) for x in s["verification"])
        out.append(f"| `{step_display(s)}` | {'<br>'.join(temps) or 'operación temporal'} | {close_cell} |")

    out += [
        "",
        "## Cuatro recorridos operativos",
        "",
        "| ID | Entorno | Ancla en la práctica |",
        "|---|---|---|",
    ]
    names={"M0-W-CONSOLE":"Consola/terminal","M0-W-INTELLIJ":"IntelliJ IDEA","M0-W-ECLIPSE":"Eclipse","M0-W-VSCODE":"VS Code"}
    for w in m["environment_workflows"]:
        out.append(f"| `{w['id']}` | {names.get(w['id'],w['id'])} | `{esc(w['anchor'])}` |")

    out += [
        "",
        "## Gates automáticos",
        "",
        "El validador y GitHub Actions deben impedir que esta documentación se convierta en una declaración decorativa. Entre otras cosas comprueban:",
        "",
        "- exactamente 51 contratos de paso y correspondencia con los headings reales de `PRACTICA.md`;",
        "- referencias a conceptos teóricos existentes y anclas presentes en `TEORIA.md`;",
        "- campos obligatorios por paso: teoría, evidencia, observable y verificación;",
        "- artefactos permanentes presentes en el inventario inverso y símbolos declarados de creación/modificación/restauración presentes en el snapshot final;",
        "- orígenes `GUIDE` asociados a una acción `CREATE` real y evoluciones asociadas a `MODIFY/RESTORE`, evitando confundir análisis o verificación con modificación;",
        "- estados `TEMPORARY` cerrados explícitamente mediante `RESTORE` o `DELETE`;",
        "- artefactos funcionales clasificados y comportamiento público no escondido como `SUPPORT`;",
        "- preguntas/respuestas emparejadas y ausencia de metatexto interno en las guías del alumno;",
        "- superficie Markdown del alumno limpia: sólo `README.md` en raíz y `README.md`/`TEORIA.md`/`PRACTICA.md` en `M0/`;",
        "- presencia de consola, IntelliJ IDEA, Eclipse y VS Code;",
        "- Maven Wrapper, ejemplo Java, tests, `package`, JAR, 404 esperado, configuración de puerto alternativo y endpoints HTTP reales.",
        "",
        "La vista humana se mantiene únicamente en `.course/traceability/TRAZABILIDAD_M0.md` y se regenera desde `M0.json`; CI usa `generate_human_traceability.py --check` para detectar cualquier desincronización.",
        "",
    ]
    return "\n".join(out)

def main():
    p = argparse.ArgumentParser()
    p.add_argument("--check", action="store_true")
    args = p.parse_args()
    internal = render(Path(".course/traceability"))
    if args.check:
        if not INTERNAL.exists():
            print(f"HUMAN TRACEABILITY: FAIL\n - missing {INTERNAL.relative_to(ROOT)}")
            return 1
        if INTERNAL.read_text(encoding="utf-8") != internal:
            print(f"HUMAN TRACEABILITY: FAIL\n - out of date: {INTERNAL.relative_to(ROOT)}")
            return 1
        print("HUMAN TRACEABILITY: PASS")
        return 0
    INTERNAL.write_text(internal,encoding="utf-8")
    print(f"generated {INTERNAL.relative_to(ROOT)}")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
