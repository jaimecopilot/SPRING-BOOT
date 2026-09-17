# TRAZABILIDAD M6 - CIERRE FINAL BIDIRECCIONAL

## 1. Resumen verificable
- Fuente canónica: `SPRING BOOT PDF(4)(1).pdf`, M6 en páginas **1203-1550**; M7 comienza en 1551.
- Teoría final: **9/9 puntos, 45/45 bloques, 135/135 subapartados T**. Los 135 encabezados T coinciden con la fuente tras normalizar tipografía.
- Práctica final: **112/112 pasos**. Los 112 encabezados de paso coinciden con la fuente tras normalizar guiones/Markdown.
- Snapshots: **9/9** (`6.1/proyecto` ... `6.9/proyecto`) y evolución exclusivamente hacia delante.
- Estado temporal controlado: `InMemoryUserConfig.java` existe en 6.2 y desaparece en 6.3 al pasar a usuarios de base de datos.
- Validación local acumulativa: `VALIDAR_M6.bat` **PASS** (confirmada por ejecución local del usuario el 2026-09-16; log no conservado en este paquete).
- Validación HTTP real: `VALIDAR_M6_HTTP.bat` V10 **PASS, 85 comprobaciones HTTP**, arrancando realmente 6.1-6.9.
- Esta trazabilidad no se inserta en TEORIA/PRACTICA: es documentación de ingeniería del módulo.

## 2. Cadena de trazabilidad

Dirección directa: `PDF fuente -> teoría -> paso práctico -> artefacto/símbolo -> comando/observable -> validación`.

Dirección inversa: `artefacto final -> punto de introducción/evolución -> pasos prácticos -> concepto teórico -> página fuente`.

## 3. Teoría -> práctica

| Concepto | Fuente | Pasos prácticos |
|---|---:|---|
| `TC-6.1-1` Qué es Spring Security | p. 1204 | 6.1.1, 6.1.2, 6.1.9 |
| `TC-6.1-2` La cadena de filtros | p. 1206 | 6.1.4, 6.1.10 |
| `TC-6.1-3` Configuración moderna | p. 1208 | 6.1.5, 6.1.7, 6.1.8, 6.1.11, 6.1.12 |
| `TC-6.1-4` Autenticación en detalle | p. 1213 | 6.1.3, 6.1.6, 6.1.9 |
| `TC-6.1-5` Ecosistema de Spring Security | p. 1217 | cobertura transversal |
| `TC-6.2-1` El modelo de usuario en Spring Security | p. 1235 | 6.2.1, 6.2.2, 6.2.4, 6.2.7, 6.2.8, 6.2.9, 6.2.12 |
| `TC-6.2-2` Usuarios en memoria | p. 1238 | 6.2.4, 6.2.12 |
| `TC-6.2-3` Cifrado de contraseñas | p. 1242 | 6.2.3, 6.2.4, 6.2.10, 6.2.11 |
| `TC-6.2-4` Integración con la cadena de filtros | p. 1245 | 6.2.1, 6.2.5, 6.2.6, 6.2.9, 6.2.11, 6.2.12 |
| `TC-6.2-5` Testing y buenas prácticas | p. 1248 | 6.2.7, 6.2.8, 6.2.10, 6.2.11 |
| `TC-6.3-1` Por qué usuarios en base de datos | p. 1267 | 6.3.1, 6.3.2, 6.3.3, 6.3.4, 6.3.8 |
| `TC-6.3-2` UserDetailsService personalizado | p. 1271 | 6.3.4, 6.3.5, 6.3.9 |
| `TC-6.3-3` Integración con la configuración | p. 1275 | 6.3.6, 6.3.7, 6.3.8, 6.3.9, 6.3.12 |
| `TC-6.3-4` Registro de usuarios | p. 1279 | 6.3.10, 6.3.11, 6.3.12 |
| `TC-6.3-5` Buenas prácticas y tests | p. 1284 | 6.3.13, 6.3.14 |
| `TC-6.4-1` Autorización por URL vs por método | p. 1316 | 6.4.1, 6.4.2, 6.4.3, 6.4.4, 6.4.5, 6.4.6 |
| `TC-6.4-2` hasRole, hasAuthority, hasAnyRole | p. 1319 | 6.4.5, 6.4.12 |
| `TC-6.4-3` @PreAuthorize y @Secured | p. 1322 | 6.4.2, 6.4.3, 6.4.5, 6.4.9, 6.4.12 |
| `TC-6.4-4` Acceder al usuario autenticado | p. 1326 | 6.4.7, 6.4.8, 6.4.9 |
| `TC-6.4-5` Buenas prácticas y errores | p. 1330 | 6.4.6, 6.4.10, 6.4.11, 6.4.12 |
| `TC-6.5-1` Qué es un JWT | p. 1356 | 6.5.1 |
| `TC-6.5-2` Estructura de un JWT | p. 1359 | 6.5.1, 6.5.2, 6.5.3, 6.5.6, 6.5.10, 6.5.12 |
| `TC-6.5-3` La firma | p. 1362 | 6.5.2, 6.5.4, 6.5.5, 6.5.7, 6.5.11 |
| `TC-6.5-4` Cuándo usar JWT | p. 1365 | 6.5.8 |
| `TC-6.5-5` Errores y buenas prácticas | p. 1367 | 6.5.3, 6.5.7, 6.5.8, 6.5.9, 6.5.10, 6.5.12 |
| `TC-6.6-1` La librería JJWT | p. 1386 | 6.6.1 |
| `TC-6.6-2` La clave secreta | p. 1389 | 6.6.2, 6.6.3 |
| `TC-6.6-3` Generar un token | p. 1392 | 6.6.4, 6.6.10 |
| `TC-6.6-4` El endpoint de login | p. 1398 | 6.6.5, 6.6.6, 6.6.7, 6.6.8, 6.6.9 |
| `TC-6.6-5` Refresh tokens y buenas prácticas | p. 1402 | 6.6.6, 6.6.7, 6.6.11, 6.6.12, 6.6.13 |
| `TC-6.7-1` Por qué un filtro personalizado | p. 1434 | 6.7.1, 6.7.2, 6.7.5 |
| `TC-6.7-2` Extraer el token | p. 1436 | 6.7.2, 6.7.8, 6.7.9 |
| `TC-6.7-3` Construir el Authentication | p. 1439 | 6.7.1, 6.7.2, 6.7.6, 6.7.7, 6.7.12 |
| `TC-6.7-4` Registrar el filtro | p. 1442 | 6.7.3, 6.7.4, 6.7.7, 6.7.12 |
| `TC-6.7-5` Diagnóstico y buenas prácticas | p. 1445 | 6.7.8, 6.7.9, 6.7.10, 6.7.11 |
| `TC-6.8-1` La configuración completa | p. 1470 | 6.8.1, 6.8.2, 6.8.3, 6.8.7 |
| `TC-6.8-2` Endpoints públicos y privados | p. 1475 | 6.8.3, 6.8.4, 6.8.5 |
| `TC-6.8-3` Autorización con JWT | p. 1477 | 6.8.3, 6.8.4, 6.8.5, 6.8.12 |
| `TC-6.8-4` El flujo completo | p. 1480 | 6.8.6, 6.8.8, 6.8.9, 6.8.12 |
| `TC-6.8-5` Diagnóstico y buenas prácticas | p. 1483 | 6.8.7, 6.8.10, 6.8.11 |
| `TC-6.9-1` Por qué testear la seguridad | p. 1507 | 6.9.1, 6.9.3 |
| `TC-6.9-2` @WithMockUser y @WithUserDetails | p. 1510 | 6.9.2, 6.9.10 |
| `TC-6.9-3` SecurityMockMvcRequestPostProcessors | p. 1514 | 6.9.9, 6.9.11 |
| `TC-6.9-4` Tests de autenticación y autorización | p. 1518 | 6.9.4, 6.9.5, 6.9.6, 6.9.7, 6.9.8, 6.9.11 |
| `TC-6.9-5` Buenas prácticas y errores | p. 1523 | 6.9.12, 6.9.13 |

## 4. Guía -> proyecto: 112 cadenas directas

| Paso | Fuente | Encabezado | Teoría | Artefacto / acción | Evidencia | Verificación |
|---|---:|---|---|---|---|---|
| `6.1.1` | p. 1220 | Paso 1 - Añadir la dependencia de Spring Security | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `pom.xml` | starter-security; compilación Maven | E2E V10: público 200; protegido 401 |
| `6.1.2` | p. 1220 | Paso 2 - Arrancar y ver el "susto" | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `runtime` | 401/WWW-Authenticate al arrancar | E2E V10: público 200; protegido 401 |
| `6.1.3` | p. 1221 | Paso 3 - Autenticarse con la contraseña generada | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `runtime` | usuario generado user + password de log | E2E V10: público 200; protegido 401 |
| `6.1.4` | p. 1222 | Paso 4 - Ver los filtros en acción | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `application*.properties / logs` | DEBUG de org.springframework.security | E2E V10: público 200; protegido 401 |
| `6.1.5` | p. 1223 | Paso 5 - Crear la clase SecurityConfig | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` | SecurityFilterChain | E2E V10: público 200; protegido 401 |
| `6.1.6` | p. 1224 | Paso 6 - Ver el comportamiento sin usuarios | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `SecurityConfig.java + runtime` | sin usuarios propios -> 401 | E2E V10: público 200; protegido 401 |
| `6.1.7` | p. 1225 | Paso 7 - Permitir todos los endpoints temporalmente | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `SecurityConfig.java` | permitAll temporal | E2E V10: público 200; protegido 401 |
| `6.1.8` | p. 1226 | Paso 8 - Configurar reglas específicas por endpoint | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `SecurityConfig.java` | requestMatchers público/H2/Swagger + authenticated | E2E V10: público 200; protegido 401 |
| `6.1.9` | p. 1228 | Paso 9 - Ver la diferencia entre 401 y 403 | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `SecurityConfig.java` | 401 vs 403 | E2E V10: público 200; protegido 401 |
| `6.1.10` | p. 1228 | Paso 10 - Depurar con logs | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `runtime/logs` | traza DEBUG de filtros | E2E V10: público 200; protegido 401 |
| `6.1.11` | p. 1229 | Paso 11 - Errores comunes del ejercicio | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `PRACTICA.md` | tabla de errores y diagnóstico | E2E V10: público 200; protegido 401 |
| `6.1.12` | p. 1230 | Paso 12 - Reto resuelto — Endpoint público sin autenticación | TC-6.1-1, TC-6.1-2, TC-6.1-3, TC-6.1-4, TC-6.1-5 | `src/main/java/es/mecd/demo/miproyecto/info/PublicInfoController.java + SecurityConfig.java` | GET /api/v1/public/info -> 200 | E2E V10: público 200; protegido 401 |
| `6.2.1` | p. 1251 | Paso 1 - Repasar el estado actual | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `SecurityConfig.java / estado 6.1` | baseline | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.2` | p. 1252 | Paso 2 - Añadir un endpoint que devuelva el perfil del usuario | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java` | perfil autenticado | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.3` | p. 1253 | Paso 3 - Definir el PasswordEncoder | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `src/main/java/es/mecd/demo/miproyecto/config/InMemoryUserConfig.java` | BCryptPasswordEncoder | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.4` | p. 1254 | Paso 4 - Definir el UserDetailsService | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `InMemoryUserConfig.java` | InMemoryUserDetailsManager / ana / admin / gestor | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.5` | p. 1256 | Paso 5 - Cerrar los endpoints | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `SecurityConfig.java` | reglas USER/ADMIN/GESTOR | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.6` | p. 1257 | Paso 6 - Arrancar y probar sin autenticación | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `runtime` | anónimo -> 401 | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.7` | p. 1258 | Paso 7 - Autenticarse con el usuario ana | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `PerfilController.java + InMemoryUserConfig.java` | ana -> 200 | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.8` | p. 1259 | Paso 8 - Autenticarse con el usuario admin | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `InMemoryUserConfig.java` | admin -> 200 | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.9` | p. 1259 | Paso 9 - Probar la autorización por rol | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `SecurityConfig.java + auth/GestorController.java` | roles -> 200/403 | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.10` | p. 1260 | Paso 10 - Probar con credenciales incorrectas | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `runtime` | credenciales incorrectas -> 401 | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.11` | p. 1261 | Paso 11 - Errores comunes del ejercicio | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `PRACTICA.md` | errores comunes | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.2.12` | p. 1262 | Paso 12 - Reto resuelto — Añadir un tercer usuario con roles combinados | TC-6.2-1, TC-6.2-2, TC-6.2-3, TC-6.2-4, TC-6.2-5 | `InMemoryUserConfig.java + GestorController.java` | tercer usuario y roles combinados | E2E V10: 200/401/403 para ana/admin/gestor |
| `6.3.1` | p. 1290 | Paso 1 - Crear el paquete auth | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `src/main/java/es/mecd/demo/miproyecto/auth/` | paquete auth | E2E V10: BD, registro, cambio de contraseña |
| `6.3.2` | p. 1290 | Paso 2 - Crear la entidad Rol | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/Rol.java` | entidad Rol | E2E V10: BD, registro, cambio de contraseña |
| `6.3.3` | p. 1292 | Paso 3 - Crear la entidad Usuario | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/Usuario.java` | entidad Usuario | E2E V10: BD, registro, cambio de contraseña |
| `6.3.4` | p. 1295 | Paso 4 - Crear los repositorios | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/RolRepository.java + auth/UsuarioRepository.java` | repositorios | E2E V10: BD, registro, cambio de contraseña |
| `6.3.5` | p. 1296 | Paso 5 - Crear el UserDetailsService personalizado | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/UsuarioDetailsService.java` | UserDetailsService BD | E2E V10: BD, registro, cambio de contraseña |
| `6.3.6` | p. 1299 | Paso 6 - Eliminar los usuarios en memoria | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `DELETE config/InMemoryUserConfig.java + SecurityConfig.java` | retirada usuarios memoria | E2E V10: BD, registro, cambio de contraseña |
| `6.3.7` | p. 1299 | Paso 7 - Crear el inicializador de usuarios | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/UsuariosInicialesConfig.java` | admin/ana persistidos | E2E V10: BD, registro, cambio de contraseña |
| `6.3.8` | p. 1301 | Paso 8 - Arrancar y verificar | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `runtime/H2` | arranque y persistencia | E2E V10: BD, registro, cambio de contraseña |
| `6.3.9` | p. 1302 | Paso 9 - Probar la autenticación | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `UsuarioDetailsService.java` | Basic con usuarios BD | E2E V10: BD, registro, cambio de contraseña |
| `6.3.10` | p. 1302 | Paso 10 - Crear el endpoint de registro | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/RegistroRequestDTO.java + AuthService.java` | registro | E2E V10: BD, registro, cambio de contraseña |
| `6.3.11` | p. 1304 | Paso 11 - Crear el AuthService y el AuthController | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/AuthService.java + auth/AuthController.java` | servicio/controlador auth | E2E V10: BD, registro, cambio de contraseña |
| `6.3.12` | p. 1308 | Paso 12 - Configurar el endpoint público y probar | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `SecurityConfig.java + AuthController.java` | registro público | E2E V10: BD, registro, cambio de contraseña |
| `6.3.13` | p. 1309 | Paso 13 - Errores comunes del ejercicio | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `PRACTICA.md` | errores comunes | E2E V10: BD, registro, cambio de contraseña |
| `6.3.14` | p. 1311 | Paso 14 - Reto resuelto — Endpoint para cambiar la contraseña | TC-6.3-1, TC-6.3-2, TC-6.3-3, TC-6.3-4, TC-6.3-5 | `auth/CambioPasswordRequestDTO.java + AuthService.java + AuthController.java + PerfilController.java` | cambio contraseña | E2E V10: BD, registro, cambio de contraseña |
| `6.4.1` | p. 1335 | Paso 1 - Repasar el estado actual | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `estado 6.3` | baseline | E2E V10 + tests: roles y método |
| `6.4.2` | p. 1336 | Paso 2 - Activar la seguridad por método | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `SecurityConfig.java` | @EnableMethodSecurity | E2E V10 + tests: roles y método |
| `6.4.3` | p. 1336 | Paso 3 - Crear un UsuarioController de administración | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `auth/UsuarioController.java` | administración usuarios | E2E V10 + tests: roles y método |
| `6.4.4` | p. 1338 | Paso 4 - Crear el UsuarioService | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `auth/UsuarioService.java` | servicio usuarios | E2E V10 + tests: roles y método |
| `6.4.5` | p. 1340 | Paso 5 - Añadir reglas de URL y de método combinadas | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `SecurityConfig.java + UsuarioController.java` | URL + método | E2E V10 + tests: roles y método |
| `6.4.6` | p. 1341 | Paso 6 - Probar la autorización con distintos usuarios | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `runtime` | USER/ADMIN 401/403/200 | E2E V10 + tests: roles y método |
| `6.4.7` | p. 1342 | Paso 7 - Crear un endpoint para consultar el propio perfil | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `common/controller/PerfilController.java` | perfil propio | E2E V10 + tests: roles y método |
| `6.4.8` | p. 1344 | Paso 8 - Crear el UsuarioPrincipal (para el punto 6.7) | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `auth/UsuarioPrincipal.java` | principal personalizado | E2E V10 + tests: roles y método |
| `6.4.9` | p. 1347 | Paso 9 - Probar la expresión de autorización | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `PerfilController.java / @PreAuthorize` | expresión de autorización | E2E V10 + tests: roles y método |
| `6.4.10` | p. 1347 | Paso 10 - Escribir tests de autorización | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java` | tests autorización | E2E V10 + tests: roles y método |
| `6.4.11` | p. 1349 | Paso 11 - Errores comunes del ejercicio | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `PRACTICA.md` | errores comunes | E2E V10 + tests: roles y método |
| `6.4.12` | p. 1351 | Paso 12 - Reto resuelto — Endpoint de cambio de rol | TC-6.4-1, TC-6.4-2, TC-6.4-3, TC-6.4-4, TC-6.4-5 | `auth/CambioRolesRequestDTO.java + UsuarioService.java + UsuarioController.java` | cambio de rol | E2E V10 + tests: roles y método |
| `6.5.1` | p. 1371 | Paso 1 - Entender la estructura con un ejemplo | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java` | estructura JWT | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.2` | p. 1373 | Paso 2 - Crear una clase para generar un JWT manualmente | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | generación Base64/HMAC SHA-256 | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.3` | p. 1375 | Paso 3 - Decodificar el token generado | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | decodificación payload | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.4` | p. 1375 | Paso 4 - Modificar el payload sin actualizar la firma | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | payload alterado | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.5` | p. 1376 | Paso 5 - Verificar el token con Java | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | verificar firma | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.6` | p. 1378 | Paso 6 - Probar con distintos payloads | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | payloads variantes | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.7` | p. 1378 | Paso 7 - Reflexionar sobre la seguridad | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | reflexión de seguridad | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.8` | p. 1379 | Paso 8 - Comparar con un token real | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java / comparación` | token real | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.9` | p. 1379 | Paso 9 - Errores comunes del ejercicio | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `PRACTICA.md` | errores comunes | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.10` | p. 1380 | Paso 10 - Experimentar con la expiración | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | expiración | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.11` | p. 1381 | Paso 11 - Verificar el token con una clave incorrecta | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java` | clave incorrecta | VALIDAR_M6: JwtManual PASS + tests |
| `6.5.12` | p. 1381 | Paso 12 - Reto resuelto — Decodificar un token sin verificar la firma | TC-6.5-1, TC-6.5-2, TC-6.5-3, TC-6.5-4, TC-6.5-5 | `JwtManual.java + JwtManualTest.java` | decodificar sin verificar | VALIDAR_M6: JwtManual PASS + tests |
| `6.6.1` | p. 1407 | Paso 1 - Añadir las dependencias de JJWT | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `pom.xml` | JJWT 0.12.3 | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.2` | p. 1408 | Paso 2 - Configurar las propiedades de JWT | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `application-*.properties` | jwt.secret/expiration/refresh-expiration | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.3` | p. 1409 | Paso 3 - Crear la configuración de la clave | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `auth/JwtConfig.java` | clave HMAC | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.4` | p. 1410 | Paso 4 - Crear el JwtService | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `auth/JwtService.java` | emitir/verificar tokens | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.5` | p. 1416 | Paso 5 - Crear los DTOs de login | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `auth/LoginRequestDTO.java + LoginResponseDTO.java` | DTO login | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.6` | p. 1419 | Paso 6 - Ampliar el AuthService con login y refresh | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `auth/AuthService.java + RefreshRequestDTO.java` | login/refresh | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.7` | p. 1422 | Paso 7 - Ampliar el AuthController | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `auth/AuthController.java` | endpoints auth | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.8` | p. 1423 | Paso 8 - Configurar la seguridad para permitir login | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `config/SecurityConfig.java` | permitir login/refresh | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.9` | p. 1423 | Paso 9 - Arrancar y probar el login | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `runtime` | POST /auth/login -> 200 | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.10` | p. 1424 | Paso 10 - Decodificar el token | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `JwtService.java / token` | decodificar token | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.11` | p. 1425 | Paso 11 - Probar el refresh | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `AuthService.java + TokenRevocationService.java` | refresh one-use / inválido 400 | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.12` | p. 1426 | Paso 12 - Errores comunes del ejercicio | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `PRACTICA.md` | errores comunes | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.6.13` | p. 1427 | Paso 13 - Reto resuelto — Endpoint para logout | TC-6.6-1, TC-6.6-2, TC-6.6-3, TC-6.6-4, TC-6.6-5 | `AuthController.java + AuthService.java + TokenRevocationService.java` | logout/revocación | E2E V10: login/refresh/logout; refresh inválido 400 |
| `6.7.1` | p. 1450 | Paso 1 - Verificar el JwtService y el UsuarioPrincipal | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `auth/JwtService.java + auth/UsuarioPrincipal.java` | precondiciones | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.2` | p. 1450 | Paso 2 - Crear el JwtAuthenticationFilter | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `auth/JwtAuthenticationFilter.java` | filtro JWT | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.3` | p. 1455 | Paso 3 - Registrar el filtro en la cadena | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `config/SecurityConfig.java` | addFilterBefore | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.4` | p. 1457 | Paso 4 - Desactivar HTTP Basic | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `SecurityConfig.java` | HTTP Basic desactivado | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.5` | p. 1457 | Paso 5 - Arrancar y probar | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `runtime` | Bearer -> 200 / sin token -> 401 | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.6` | p. 1459 | Paso 6 - Verificar el UsuarioPrincipal | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `PerfilController.java` | UsuarioPrincipal con ROLE_USER | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.7` | p. 1460 | Paso 7 - Probar con rol ADMIN | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `SecurityConfig.java / UsuarioController.java` | ADMIN 200 USER 403 | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.8` | p. 1461 | Paso 8 - Probar con un token inválido | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `JwtAuthenticationFilter.java` | token inválido -> 401 | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.9` | p. 1462 | Paso 9 - Probar con la cabecera mal formada | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `JwtAuthenticationFilter.java` | cabecera sin Bearer -> 401 | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.10` | p. 1462 | Paso 10 - Depurar el filtro | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `logs` | diagnóstico filtro | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.11` | p. 1463 | Paso 11 - Errores comunes del ejercicio | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `PRACTICA.md` | errores comunes | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.7.12` | p. 1464 | Paso 12 - Reto resuelto: expresión SpEL con el ID del usuario | TC-6.7-1, TC-6.7-2, TC-6.7-3, TC-6.7-4, TC-6.7-5 | `PerfilController.java + @PreAuthorize` | SpEL id: ana id=2, admin override | E2E V10: filtro Bearer + SpEL + 401/403/200 |
| `6.8.1` | p. 1487 | Paso 1 - Crear el JwtAuthenticationEntryPoint | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `auth/JwtAuthenticationEntryPoint.java` | 401 JSON | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.2` | p. 1489 | Paso 2 - Crear el JwtAccessDeniedHandler | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `auth/JwtAccessDeniedHandler.java + auth/SecurityErrorWriter.java` | 403 JSON | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.3` | p. 1491 | Paso 3 - Actualizar el SecurityConfig | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `config/SecurityConfig.java` | handlers + filtro + stateless | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.4` | p. 1493 | Paso 4 - Añadir un endpoint de gestor | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `auth/GestorController.java` | endpoint GESTOR | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.5` | p. 1494 | Paso 5 - Añadir el rol GESTOR al inicializador | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `auth/UsuariosInicialesConfig.java` | gestor/gestor123 USER+GESTOR | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.6` | p. 1495 | Paso 6 - Probar el flujo completo | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `runtime` | flujo login/Bearer/roles | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.7` | p. 1497 | Paso 7 - Probar sin token | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `runtime` | sin token -> 401 JSON | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.8` | p. 1498 | Paso 8 - Probar el refresh token | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `AuthController.java + AuthService.java` | refresh | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.9` | p. 1498 | Paso 9 - Probar el logout | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `AuthController.java + TokenRevocationService.java` | logout + token revocado | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.10` | p. 1499 | Paso 10 - Escribir tests de integración | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `src/test/java/es/mecd/demo/miproyecto/auth/AuthControllerTest.java` | integración auth | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.11` | p. 1501 | Paso 11 - Errores comunes del ejercicio | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `PRACTICA.md` | errores comunes | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.8.12` | p. 1502 | Paso 12 - Reto resuelto — Endpoint para consultar el propio usuario | TC-6.8-1, TC-6.8-2, TC-6.8-3, TC-6.8-4, TC-6.8-5 | `common/controller/PerfilController.java` | /perfil/usuario desde claims | E2E V10: handlers JSON, GESTOR, refresh, logout |
| `6.9.1` | p. 1528 | Paso 1 - Añadir la dependencia spring-security-test | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `pom.xml` | spring-security-test | mvn clean test + E2E V10 seguridad final |
| `6.9.2` | p. 1529 | Paso 2 - Crear el test del UsuarioController | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java` | 401/403/200 | mvn clean test + E2E V10 seguridad final |
| `6.9.3` | p. 1532 | Paso 3 - Ejecutar los tests | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `mvnw clean test` | suite seguridad | mvn clean test + E2E V10 seguridad final |
| `6.9.4` | p. 1532 | Paso 4 - Testear el endpoint de gestor | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `auth/GestorSecurityTest.java` | GESTOR | mvn clean test + E2E V10 seguridad final |
| `6.9.5` | p. 1533 | Paso 5 - Testear el endpoint público | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `common/controller/PublicControllerSecurityTest.java` | público | mvn clean test + E2E V10 seguridad final |
| `6.9.6` | p. 1535 | Paso 6 - Testear el login | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `auth/AuthControllerSecurityTest.java` | login | mvn clean test + E2E V10 seguridad final |
| `6.9.7` | p. 1537 | Paso 7 - Testear el refresh | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `AuthControllerSecurityTest.java` | refresh | mvn clean test + E2E V10 seguridad final |
| `6.9.8` | p. 1539 | Paso 8 - Testear el logout | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `AuthControllerSecurityTest.java` | logout | mvn clean test + E2E V10 seguridad final |
| `6.9.9` | p. 1539 | Paso 9 - Testear con post-processors | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `UsuarioControllerIntegrationTest.java` | post-processors user()/jwt() | mvn clean test + E2E V10 seguridad final |
| `6.9.10` | p. 1540 | Paso 10 - Testear con @WithUserDetails | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `UsuarioControllerIntegrationTest.java` | @WithUserDetails | mvn clean test + E2E V10 seguridad final |
| `6.9.11` | p. 1543 | Paso 11 - Testear el filtro JWT | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `auth/JwtFilterIntegrationTest.java` | filtro JWT | mvn clean test + E2E V10 seguridad final |
| `6.9.12` | p. 1544 | Paso 12 - Errores comunes del ejercicio | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `PRACTICA.md` | errores comunes | mvn clean test + E2E V10 seguridad final |
| `6.9.13` | p. 1546 | Paso 13 - Reto resuelto — Test completo de autorización | TC-6.9-1, TC-6.9-2, TC-6.9-3, TC-6.9-4, TC-6.9-5 | `auth/UsuarioControllerCompleteSecurityTest.java` | autorización completa | mvn clean test + E2E V10 seguridad final |

## 5. Evolución física entre snapshots

La tabla siguiente se calcula sobre los ficheros reales del paquete final; `target/` queda excluido.

| Snapshot | Nuevos | Modificados | Eliminados |
|---|---|---|---|
| 6.1 | baseline M5 + Security 6.1 | - | - |
| 6.2 | `src/main/java/es/mecd/demo/miproyecto/auth/GestorController.java`<br>`src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/InMemoryUserConfig.java` | `src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` | - |
| 6.3 | `src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/CambioPasswordRequestDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/RegistroRequestDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/Rol.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/RolRepository.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioDetailsService.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioRepository.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioResponseDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java` | `src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` | `src/main/java/es/mecd/demo/miproyecto/config/InMemoryUserConfig.java` |
| 6.4 | `src/main/java/es/mecd/demo/miproyecto/auth/CambioRolesRequestDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuarioService.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java` | `pom.xml`<br>`src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` | - |
| 6.5 | `src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`<br>`src/test/java/es/mecd/demo/miproyecto/jwt/JwtManualTest.java` | - | `.vscode/launch.json`<br>`.vscode/settings.json` |
| 6.6 | `src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/CredencialesInvalidasException.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/JwtConfig.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/LoginRequestDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/LoginResponseDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/RefreshRequestDTO.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/TokenInvalidoException.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/TokenRevocationService.java` | `pom.xml`<br>`src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`<br>`src/main/resources/application-dev.properties`<br>`src/main/resources/application-prod.properties`<br>`src/main/resources/application-test.properties` | - |
| 6.7 | `src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java` | `src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`<br>`src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`<br>`src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java` | - |
| 6.8 | `src/main/java/es/mecd/demo/miproyecto/auth/JwtAccessDeniedHandler.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationEntryPoint.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/AuthControllerTest.java` | `src/main/java/es/mecd/demo/miproyecto/auth/GestorController.java`<br>`src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`<br>`src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`<br>`src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java`<br>`src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java` | - |
| 6.9 | `src/test/java/es/mecd/demo/miproyecto/auth/AuthControllerSecurityTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/GestorSecurityTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerCompleteSecurityTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerIntegrationTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java`<br>`src/test/java/es/mecd/demo/miproyecto/common/controller/PublicControllerSecurityTest.java` | - | `src/test/java/es/mecd/demo/miproyecto/auth/AuthControllerTest.java` |

## 6. Proyecto final -> guía (artefactos M6 principales)

| Artefacto | Origen | Evolución | Razón |
|---|---|---|---|
| `config/SecurityConfig.java` | 6.1.5 | evoluciona 6.2, 6.3, 6.4, 6.6, 6.7, 6.8 | cadena de seguridad |
| `info/PublicInfoController.java` | 6.1.12 | permanece | endpoint público |
| `config/InMemoryUserConfig.java` | 6.2.4 | DELETE 6.3.6 | estado temporal: usuarios memoria |
| `common/controller/PerfilController.java` | 6.2.2 | 6.3, 6.4, 6.7, 6.8 | perfil/principal/SpEL/claims |
| `auth/Rol.java` | 6.3.2 | permanece | modelo de rol |
| `auth/Usuario.java` | 6.3.3 | permanece | modelo de usuario |
| `auth/RolRepository.java` | 6.3.4 | permanece | persistencia rol |
| `auth/UsuarioRepository.java` | 6.3.4 | permanece | persistencia usuario |
| `auth/UsuarioDetailsService.java` | 6.3.5 | permanece | carga usuario Spring Security |
| `auth/UsuariosInicialesConfig.java` | 6.3.7 | evoluciona 6.8.5 | admin/ana; luego gestor |
| `auth/AuthService.java` | 6.3.11 | evoluciona 6.6.6 | registro/password/login/refresh/logout |
| `auth/AuthController.java` | 6.3.11 | evoluciona 6.6.7 | REST auth |
| `auth/UsuarioController.java` | 6.4.3 | permanece | administración y roles |
| `auth/UsuarioService.java` | 6.4.4 | permanece | servicio usuarios |
| `auth/UsuarioPrincipal.java` | 6.4.8 | usado por 6.7+ | principal con id/email/roles |
| `test/.../jwt/JwtManual.java` | 6.5.2 | permanece como laboratorio | JWT manual Base64/HMAC |
| `auth/JwtConfig.java` | 6.6.3 | permanece | clave JWT |
| `auth/JwtService.java` | 6.6.4 | usado 6.7+ | generación/verificación JWT |
| `auth/LoginRequestDTO.java` | 6.6.5 | permanece | credenciales login |
| `auth/LoginResponseDTO.java` | 6.6.5 | permanece | access/refresh token |
| `auth/RefreshRequestDTO.java` | 6.6.6 | permanece | refresh |
| `auth/TokenRevocationService.java` | 6.6.13 | usado 6.7+ | one-use refresh/logout/revocación |
| `auth/JwtAuthenticationFilter.java` | 6.7.2 | registrado 6.7.3 | Bearer -> SecurityContext |
| `auth/JwtAuthenticationEntryPoint.java` | 6.8.1 | permanece | 401 JSON |
| `auth/JwtAccessDeniedHandler.java` | 6.8.2 | permanece | 403 JSON |
| `auth/SecurityErrorWriter.java` | 6.8.2 | permanece | cuerpo JSON común |
| `auth/GestorController.java` | 6.2.12 | evoluciona 6.8.4 | endpoint GESTOR |
| `test/.../auth/UsuarioControllerSecurityTest.java` | 6.9.2 | final | 401/403/200 |
| `test/.../auth/GestorSecurityTest.java` | 6.9.4 | final | roles GESTOR |
| `test/.../common/controller/PublicControllerSecurityTest.java` | 6.9.5 | final | público 200 |
| `test/.../auth/AuthControllerSecurityTest.java` | 6.9.6-6.9.8 | final | login/refresh/logout |
| `test/.../auth/JwtFilterIntegrationTest.java` | 6.9.11 | final | filtro JWT |
| `test/.../auth/UsuarioControllerCompleteSecurityTest.java` | 6.9.13 | final | autorización completa |

Los demás ficheros del dominio (`alumno`, `curso`, `expediente`, errores, configuración base, etc.) son continuidad heredada de M5. M6 no los presenta como contenido nuevo salvo cuando un test o una integración de seguridad necesita adaptarlos.

## 7. Estados temporales y restauraciones

- 6.1.7 abre temporalmente `anyRequest().permitAll()` para observar la configuración; 6.1.8 restaura reglas selectivas.
- `InMemoryUserConfig.java`: CREATE en 6.2.4; DELETE en 6.3.6 al introducir `UsuarioDetailsService` y persistencia.
- HTTP Basic se mantiene hasta 6.6; en 6.7.4 se desactiva al entrar el filtro JWT.
- Los handlers mínimos de 6.7 se sustituyen en 6.8 por `JwtAuthenticationEntryPoint` y `JwtAccessDeniedHandler` JSON.
- El laboratorio JWT manual de 6.5 permanece aislado en `src/test/java` y no contamina la autenticación de producción.

## 8. Gates de cierre

- `VALIDAR_M6_ESTATICO.py`: estructura 9 puntos, 45 bloques, 112 pasos, 9 snapshots.
- `VALIDAR_M6.bat`: `clean test` 6.1-6.9, observable manual JWT 6.5 y gate final 6.9. Ejecución local final: PASS.
- `VALIDAR_M6_HTTP.bat`: arranca 6.1-6.9 y realiza 85 checks HTTP reales. Ejecución V10 final: PASS.
- PDF: 72 páginas teoría + 72 páginas práctica, Letter, cabecera/pie canónicos, sin caracteres U+FFFD/área privada/cuadrados detectados en extracción y sin anomalías visibles en render completo.


## 9. Correcciones de coherencia runtime - 2026-09-17

- `6.4.12`: el ejemplo ejecutable usaba `GESTOR`, aunque el estado acumulativo 6.4 sólo persiste `USER` y `ADMIN` y la propia práctica retrasa la materialización de `GESTOR` hasta 6.8. Se corrige la prueba positiva para usar `USER` + `ADMIN` y se ejecuta primero la prueba negativa con Ana para evitar elevarla a ADMIN antes de comprobar el 403. El endpoint, DTO, servicio y snapshot 6.4 no cambian.
- `6.8.3`: el snippet documental del `SecurityFilterChain` omitía `/swagger-ui.html`; se alinea con el código ejecutable 6.8/6.9, que ya lo permite junto con `/swagger-ui/**` y `/v3/api-docs/**`.
- `6.8.5`: el snippet de inicialización de `GESTOR` se alinea con las firmas reales idempotentes de `UsuariosInicialesConfig` (`obtenerOCrearRol`, `obtenerOCrearUsuario` y `prepararUsuario`). No se adelanta `GESTOR`: sigue apareciendo persistentemente por primera vez en 6.8.
- `6.8.8`: la reutilización de un refresh consumido se documentaba como 401; el contrato real definido desde 6.6 y aplicado por `AuthExceptionHandler` es HTTP 400 con `codigo=TOKEN_INVALIDO`. Se corrige la expectativa documental.
- Estas reconciliaciones no crean pasos nuevos ni cambian la cobertura estructural: M6 conserva 9/9 puntos y 112/112 pasos.
