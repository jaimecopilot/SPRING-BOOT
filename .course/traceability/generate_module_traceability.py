#!/usr/bin/env python3
"""Genera la vista humana de trazabilidad de un módulo M1+.

Uso:
    python .course/traceability/generate_module_traceability.py M1
    python .course/traceability/generate_module_traceability.py M1 --check

La fuente de verdad sigue siendo el manifest JSON y sus step manifests. Este
script sólo proyecta ese contrato en una vista legible por una persona.
"""
from pathlib import Path
import argparse
import json
import os
import sys

ROOT = Path(__file__).resolve().parents[2]


def esc(value):
    return str(value).replace("|", "\\|").replace("\n", " ")


def rel_link(path, base_dir):
    target = ROOT / path
    return os.path.relpath(target, ROOT / base_dir).replace("\\", "/")


def linked(path, base_dir):
    return f"[`{path}`]({rel_link(path, base_dir)})"


def step_display(step):
    return f"{step['practice']}.{step['number']}"


def load(module):
    manifest_path = ROOT / ".course/traceability" / f"{module}.json"
    if not manifest_path.exists():
        raise SystemExit(f"missing manifest: {manifest_path.relative_to(ROOT)}")
    manifest = json.loads(manifest_path.read_text(encoding="utf-8"))
    steps = []
    for rel in manifest.get("step_manifests", []):
        steps.extend(json.loads((ROOT / rel).read_text(encoding="utf-8"))["steps"])
    return manifest, steps


def render(module):
    m, steps = load(module)
    arts = m.get("artifacts", [])
    theory = m.get("theory_concepts", [])
    base = Path(".course/traceability")
    module_dir = module

    out = [
        f"# Trazabilidad humana — {module}",
        "",
        f"> Generado automáticamente desde `{module}.json`, los manifests de paso y el árbol real del proyecto. "
        "No es una segunda fuente de verdad: traduce el contrato máquina-legible a una vista auditable por una persona.",
        "",
        "## Cómo puedes auditarlo tú",
        "",
        f"1. Abre la [guía práctica]({rel_link(f'{module_dir}/PRACTICA.md', base)}) y elige cualquier paso.",
        "2. Busca su identificador en **Guía → proyecto** y sigue artefactos, símbolos, comandos, observables y verificaciones.",
        f"3. Abre la [teoría]({rel_link(f'{module_dir}/TEORIA.md', base)}) y localiza los conceptos referenciados por ese paso.",
        "4. Usa **Proyecto → guía** para partir de un fichero final y reconstruir por qué existe y dónde evolucionó.",
        "5. Revisa **Artefactos necesarios no introducidos en este módulo** para separar herencia/soporte de contenido nuevo.",
        "6. Revisa **Estados temporales y restauraciones** para comprobar que ningún experimento quedó filtrado al snapshot final.",
        "7. Ejecuta los gates automáticos del módulo para contrastar esta vista contra el árbol y el runtime reales.",
        "",
        "## Resumen verificable",
        "",
        "| Dato | Valor |",
        "|---|---:|",
        f"| Estado | `{m.get('status','—')}` |",
        f"| Esquema | v{m.get('schema_version','—')} |",
        f"| Conceptos teóricos | {len(theory)} |",
        f"| Pasos prácticos | {len(steps)} |",
        f"| `PERMANENT` | {sum(1 for s in steps if s.get('state')=='PERMANENT')} |",
        f"| `TEMPORARY` | {sum(1 for s in steps if s.get('state')=='TEMPORARY')} |",
        f"| Recorridos de entorno | {len(m.get('environment_workflows', []))} |",
        f"| Artefactos `GUIDE` | {sum(1 for a in arts if a.get('classification')=='GUIDE')} |",
        f"| Artefactos `INHERITED` | {sum(1 for a in arts if a.get('classification')=='INHERITED')} |",
        f"| Artefactos `SUPPORT` | {sum(1 for a in arts if a.get('classification')=='SUPPORT')} |",
        f"| Total previsto al completar | {m.get('expected_total_steps_when_complete','—')} |",
        "",
        "## Teoría → práctica",
        "",
        "| Concepto | Ancla en teoría | Pasos que lo materializan |",
        "|---|---|---|",
    ]

    for concept in theory:
        refs = [step_display(s) for s in steps if concept["id"] in s.get("theory_refs", [])]
        out.append(
            f"| `{concept['id']}` | {esc(concept['anchor'])} | "
            f"{', '.join('`'+r+'`' for r in refs) or '—'} |"
        )

    out += [
        "",
        "## Guía → proyecto",
        "",
        "| Paso | Qué enseña/cambia | Teoría | Acción / artefacto / símbolo | Comando | Observable | Verificación | Estado |",
        "|---|---|---|---|---|---|---|---|",
    ]
    for step in steps:
        theory_cell = "<br>".join(f"`{x}`" for x in step.get("theory_refs", [])) or "—"
        actions = []
        for art in step.get("artifacts", []):
            syms = ", ".join(f"`{esc(x)}`" for x in art.get("symbols", []))
            text = f"**{art['action']}** {linked(art['path'], base)}"
            if syms:
                text += f"<br>{syms}"
            text += f"<br>_{art.get('state', step.get('state','—'))}_"
            actions.append(text)
        action_cell = "<br>".join(actions) or "Observación/análisis sin mutación persistente"
        cmd_cell = "<br>".join(f"`{esc(x)}`" for x in step.get("commands", [])) or "—"
        obs_cell = "<br>".join(esc(x) for x in step.get("observables", [])) or "—"
        ver_cell = "<br>".join(esc(x) for x in step.get("verification", [])) or "—"
        state = "🟡 `TEMPORARY`" if step.get("state") == "TEMPORARY" else "✅ `PERMANENT`"
        out.append(
            f"| `{step_display(step)}`<br>`{step['id']}` | {esc(step.get('summary',''))} | {theory_cell} | "
            f"{action_cell} | {cmd_cell} | {obs_cell} | {ver_cell} | {state} |"
        )

    out += [
        "",
        "## Proyecto → guía",
        "",
        "| Artefacto | Clasificación | Origen | Evolución | Símbolos relevantes | Quién lo usa | Qué se rompe si falta |",
        "|---|---|---|---|---|---|---|",
    ]
    for art in arts:
        origin = f"`{art['origin']}`" if art.get("origin") else "—"
        evolution = "<br>".join(f"`{x}`" for x in art.get("evolution", [])) or "—"
        syms = "<br>".join(f"`{esc(x)}`" for x in art.get("symbols", [])) or "—"
        users = "<br>".join(esc(x) for x in art.get("used_by", [])) or "—"
        out.append(
            f"| {linked(art['path'], base)} | `{art.get('classification','—')}` | {origin} | {evolution} | "
            f"{syms} | {users} | {esc(art.get('missing_effect','—'))} |"
        )

    inherited_support = [a for a in arts if a.get("classification") in {"INHERITED", "SUPPORT"}]
    out += [
        "",
        "## Artefactos necesarios no introducidos en este módulo",
        "",
        "Aquí aparecen piezas heredadas de módulos anteriores y soporte interno. Su presencia puede ser necesaria para ejecutar, "
        "auditar o mantener el módulo, pero no se presentan como comportamiento nuevo introducido silenciosamente.",
        "",
        "| Artefacto | Tipo | Usuario/motivo | Si faltara |",
        "|---|---|---|---|",
    ]
    for art in inherited_support:
        users = "; ".join(art.get("used_by", [])) or "soporte interno"
        out.append(
            f"| {linked(art['path'], base)} | `{art.get('classification')}` | {esc(users)} | "
            f"{esc(art.get('missing_effect','—'))} |"
        )

    pairs = {}
    for step in steps:
        for art in step.get("artifacts", []):
            syms = art.get("symbols") or ["(artefacto completo)"]
            for sym in syms:
                pairs.setdefault((art["path"], sym), []).append(
                    f"{step_display(step)}:{art['action']}"
                )

    out += [
        "",
        "## Trazabilidad inversa por artefacto/símbolo",
        "",
        "| Artefacto | Símbolo | Pasos relacionados |",
        "|---|---|---|",
    ]
    for (path, sym), refs in sorted(pairs.items()):
        out.append(
            f"| {linked(path, base)} | `{esc(sym)}` | "
            + ", ".join(f"`{r}`" for r in refs)
            + " |"
        )

    temporary = [s for s in steps if s.get("state") == "TEMPORARY"]
    out += [
        "",
        "## Estados temporales y restauraciones",
        "",
        "| Paso | Mutación temporal | Cierre / evidencia final |",
        "|---|---|---|",
    ]
    if not temporary:
        out.append("| — | No hay pasos `TEMPORARY` declarados | — |")
    for step in temporary:
        opens = []
        closes = []
        for art in step.get("artifacts", []):
            text = f"{art['action']} `{art['path']}`"
            if art.get("symbols"):
                text += ": " + ", ".join(f"`{esc(x)}`" for x in art["symbols"])
            if art.get("state") == "TEMPORARY" and art.get("action") in {"CREATE", "MODIFY"}:
                opens.append(text)
            if art.get("action") in {"RESTORE", "DELETE"}:
                closes.append(text)
        close_cell = "<br>".join(closes) or "<br>".join(esc(x) for x in step.get("verification", []))
        out.append(
            f"| `{step_display(step)}` / `{step['id']}` | {'<br>'.join(opens) or 'operación temporal'} | {close_cell or '—'} |"
        )

    out += [
        "",
        "## Gates automáticos",
        "",
        "La trazabilidad sólo se considera válida si los gates de sólo lectura pueden demostrarla. El contrato automático comprueba, entre otras cosas:",
        "",
        "- correspondencia exacta entre headings reales de la práctica y contratos de paso;",
        "- referencias a conceptos teóricos existentes y anclas presentes en la teoría;",
        "- artefactos permanentes clasificados, presentes y con los símbolos declarados;",
        "- orígenes y evoluciones coherentes con acciones `CREATE` / `MODIFY` / `RESTORE`;",
        "- cierre explícito de estados `TEMPORARY`;",
        "- herencia sin pérdida silenciosa de comportamiento aprobado;",
        "- preguntas y respuestas emparejadas y ausencia de metatexto interno en el material del alumno;",
        "- consola, IntelliJ IDEA, Eclipse y VS Code cuando el recorrido lo requiere;",
        "- compilación, tests, empaquetado y observables HTTP reales del módulo;",
        "- sincronización exacta de esta vista mediante `generate_module_traceability.py --check`.",
        "",
        "Esta vista vive sólo bajo `.course/traceability/`; no se duplica dentro de la carpeta del alumno.",
        "",
    ]
    return "\n".join(out)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("module", help="Módulo, por ejemplo M1")
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    module = args.module.upper()
    if not module.startswith("M") or not module[1:].isdigit() or module == "M0":
        raise SystemExit("this generic generator is intended for M1+")
    output = ROOT / ".course/traceability" / f"TRAZABILIDAD_{module}.md"
    rendered = render(module)
    if args.check:
        if not output.exists():
            print(f"{module} HUMAN TRACEABILITY: FAIL - missing {output.relative_to(ROOT)}")
            return 1
        current = output.read_text(encoding="utf-8")
        if current != rendered:
            print(f"{module} HUMAN TRACEABILITY: FAIL - out of date: {output.relative_to(ROOT)}")
            return 1
        print(f"{module} HUMAN TRACEABILITY: PASS")
        return 0
    output.write_text(rendered, encoding="utf-8")
    print(f"generated {output.relative_to(ROOT)}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
