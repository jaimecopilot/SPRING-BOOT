M6 - VALIDACION LOCAL FINAL
===========================

VALIDAR_M6.bat
- Gate estatico.
- clean test 6.1 -> 6.9.
- observable JWT manual 6.5.
- gate final 6.9.
- Resultado final de aceptacion: PASS (ejecucion local del usuario, 2026-09-16).

VALIDAR_M6_HTTP.bat
- Arranca realmente 6.1 -> 6.9.
- Espera readiness HTTP + datos iniciales.
- Ejecuta observables HTTP reales.
- Resultado final V10: PASS, 85 comprobaciones HTTP.

Los BAT permanecen abiertos con PAUSE. Los mensajes de los scripts se mantienen en ASCII para evitar caracteres corruptos en consolas Windows.
