#!/usr/bin/env python3
from pathlib import Path
import argparse, json, sys

def cell(value):
    if value is None: return "—"
    if isinstance(value, list):
        return "—" if not value else "<br>".join(str(x).replace("|", "\\|") for x in value)
    return str(value).replace("|", "\\|")

def code_list(values):
    return "—" if not values else "<br>".join(f"`{v}`" for v in values)

def link_for(path):
    return f"[`{path}`](../../{path})"

def artifact_cell(items):
    if not items: return "—"
    rows=[]
    for a in items:
        suffix="" if a.get("state","permanent")=="permanent" else f" · `{a['state'].upper()}`"
        rows.append(f"`{a['action']}` {link_for(a['path'])}{suffix}<br>símbolos: {code_list(a.get('symbols',[]))}")
    return "<br><br>".join(rows)

def load_contract(root):
    data=json.loads((root/".course/traceability/M0.json").read_text(encoding="utf-8"))
    steps=[]
    for rel in data["step_files"]:
        steps.extend(json.loads((root/rel).read_text(encoding="utf-8")))
    data["step_contracts"]=steps
    data["final_artifacts"]=json.loads((root/data["artifact_file"]).read_text(encoding="utf-8"))
    return data

def render(root):
    data=load_contract(root)
    theory={t["id"]:t for t in data["theory_concepts"]}
    refs={k:[] for k in theory}
    for s in data["step_contracts"]:
        for ref in s["theory_refs"]: refs.setdefault(ref,[]).append(s["id"])
    L=[
      "# Trazabilidad humana — M0","",
      "> Generado automáticamente desde `M0.json` y los contratos paso-a-paso. No crea una segunda fuente didáctica de verdad.","",
      "## Cómo puedes auditarlo tú","",
      "1. Abre [PRACTICA.md](../../M0/PRACTICA.md) y elige cualquier paso.",
      "2. Busca su ID en **Guía → proyecto** y comprueba teoría, acción, artefactos/símbolos, comando, observable y verificación.",
      "3. Usa **Proyecto → guía** para responder por qué existe cualquier fichero del snapshot.",
      "4. Revisa **Artefactos necesarios no introducidos como contenido funcional** para conocer cada `SUPPORT`.",
      "5. Revisa **Estados temporales y restauraciones** para comprobar que ningún experimento contamina el estado final.",
      "6. CI comprueba esta vista y ejecuta además build, tests, JAR y HTTP reales.","",
      "Como baseline histórico se conserva el patrón de [E1/TRAZABILIDAD.md](https://github.com/jaimecopilot/CURSO-SPRING-BOOT-2026/blob/main/E1/TRAZABILIDAD.md), ampliado ahora con teoría, acciones, símbolos, comandos y observables por paso.","",
      "## Resumen verificable","","| Dato | Valor |","|---|---:|",
      f"| Esquema | v{data['schema_version']} |",
      f"| Conceptos teóricos | {len(data['theory_concepts'])} |",
      f"| Pasos prácticos trazados | {len(data['step_contracts'])} |",
      f"| `PERMANENT` | {sum(s['status']=='PERMANENT' for s in data['step_contracts'])} |",
      f"| `TEMPORARY` | {sum(s['status']=='TEMPORARY' for s in data['step_contracts'])} |",
      f"| `OBSERVATION` | {sum(s['status']=='OBSERVATION' for s in data['step_contracts'])} |",
      f"| Recorridos de entorno | {len(data['environment_workflows'])} |",
      f"| Ficheros `GUIDE` | {sum(a['classification']=='GUIDE' for a in data['final_artifacts'])} |",
      f"| Ficheros `INHERITED` | {sum(a['classification']=='INHERITED' for a in data['final_artifacts'])} |",
      f"| Ficheros `SUPPORT` | {sum(a['classification']=='SUPPORT' for a in data['final_artifacts'])} |","",
      "## Teoría → práctica","","| Concepto teórico | Ancla en TEORIA.md | Pasos que lo materializan |","|---|---|---|"
    ]
    for tid,t in theory.items(): L.append(f"| `{tid}` | {cell(t['anchor'])} | {code_list(refs.get(tid,[]))} |")
    L += ["","## Guía → proyecto","","Esta tabla responde, para **cada uno de los 51 pasos**, qué enseña, qué teoría lo prepara, qué cambia/usa, qué comando se ejecuta, qué debe observarse y cómo se comprueba.","","| Paso | Qué enseña/cambia | Teoría | Acción / código / símbolo | Comando | Observable | Cómo se comprueba | Estado |","|---|---|---|---|---|---|---|---|"]
    for s in data["step_contracts"]:
        state="↩️ `TEMPORARY`" if s["status"]=="TEMPORARY" else f"✅ `{s['status']}`"
        L.append(f"| `{s['id']}`<br>{cell(s['guide_heading'])} | {cell(s['teaches'])} | {code_list(s['theory_refs'])} | {artifact_cell(s['artifacts'])} | {code_list(s['commands'])} | {cell(s['observables'])} | {cell(s['verification'])} | {state} |")
    L += ["","## Proyecto → guía","","Esta tabla responde a: **«este fichero existe; ¿qué paso lo justifica o por qué está aquí?»**","","| Fichero | Clasificación | Origen | Evolución | Justificación / razón |","|---|---|---|---|---|"]
    for a in data["final_artifacts"]:
        L.append(f"| {link_for(a['path'])} | `{a['classification']}` | `{a.get('origin','—')}` | {code_list(a.get('evolution',[]))} | {cell(a.get('justification') or a.get('reason','—'))} |")
    support=[a for a in data["final_artifacts"] if a["classification"] in {"SUPPORT","INHERITED"}]
    L += ["","## Artefactos necesarios no introducidos como contenido funcional en este módulo","","M0 no tiene `INHERITED` por ser el primer módulo. Los siguientes `SUPPORT` no pueden introducir comportamiento público nuevo.","","| Artefacto | Tipo | Por qué existe aquí | Quién lo usa | Qué ocurre si falta |","|---|---|---|---|---|"]
    for a in support:
        L.append(f"| {link_for(a['path'])} | `{a['classification']}` | {cell(a.get('reason','—'))} | {cell(a.get('used_by',[]))} | {cell(a.get('missing_effect','—'))} |")
    uses={}
    for s in data["step_contracts"]:
        for a in s["artifacts"]: uses.setdefault(a["path"],[]).append((s["id"],a["action"],a.get("symbols",[]),a.get("state","permanent")))
    L += ["","## Trazabilidad inversa por artefacto/símbolo","","| Artefacto | Paso | Acción | Símbolos | Estado de esa acción |","|---|---|---|---|---|"]
    for path,rows in sorted(uses.items()):
        for sid,action,symbols,state in rows: L.append(f"| {link_for(path)} | `{sid}` | `{action}` | {code_list(symbols)} | `{state.upper()}` |")
    temps=[s for s in data["step_contracts"] if s["status"]=="TEMPORARY"]
    L += ["","## Estados temporales y restauraciones","","| Paso | Estado temporal | Restauración | Evidencia final |","|---|---|---|---|"]
    for s in temps:
        ta=[a for a in s["artifacts"] if a.get("state")=="temporary"]
        ra=[a for a in s["artifacts"] if a["action"]=="RESTORE"]
        L.append(f"| `{s['id']}` — {cell(s['guide_heading'])} | {artifact_cell(ta)} | {artifact_cell(ra)} | {cell(s['verification'])} |")
    L += ["","## Cuatro recorridos operativos",""]
    for w in data["environment_workflows"]: L.append(f"- `{w['id']}` → {w['anchor']}")
    L += ["","## Gates automáticos","","`validate_m0.py` falla si falta un contrato de paso, un heading real, una referencia teórica, comandos/observables/verificaciones, una clasificación de fichero, una restauración temporal o uno de los cuatro recorridos. También comprueba preguntas/respuestas, ausencia de metatexto y contratos de código de alto valor.","","GitHub Actions compila `HolaMinisterio`, verifica Maven Wrapper 3.9.16, ejecuta tests, empaqueta el JAR, lo arranca y comprueba `/hola` y `/adios` por HTTP real.",""]
    return "\n".join(L)

def main():
    ap=argparse.ArgumentParser(); ap.add_argument("root",nargs="?",default="."); ap.add_argument("--check",action="store_true"); args=ap.parse_args()
    root=Path(args.root); dest=root/".course/traceability/TRAZABILIDAD_M0.md"; rendered=render(root)
    if args.check:
        if not dest.exists() or dest.read_text(encoding="utf-8")!=rendered:
            print("HUMAN TRACEABILITY: FAIL - TRAZABILIDAD_M0.md no coincide con el contrato v3"); sys.exit(1)
        print("HUMAN TRACEABILITY: PASS")
    else:
        dest.write_text(rendered,encoding="utf-8"); print(dest)
if __name__=="__main__": main()
