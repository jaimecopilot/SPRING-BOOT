# M6 - trazabilidad final bidireccional

> Evidencia interna del curso. Este documento no forma parte del material pedagógico visible al alumno.

## Estado

- Baseline técnico anterior: `b16da32f3ba37b5c335a47ad44086706e0541b85`.
- Código funcional modificado por esta corrección editorial: **NO**.
- Cobertura teórica: **45 bloques / 135 subpuntos**.
- Cobertura práctica: **112/112 pasos**.
- Proyecto final limpio: **122 ficheros** = 70 preservados + 9 evolucionados + 43 nuevos funcionales.
- Publicación: pendiente del BAT local y de verificar SHA remoto.

## Cadena directa - 112/112

### 6.1.1 - Añadir la dependencia de Spring Security

- Fuente: p. 1220 / `Paso 1: Añadir la dependencia de Spring Security` / hash `175d628834d5facacabf2220fe0a8e69dadbefb42ef29548f90f0741fc582fd6`.
- Teoría: `TC-6.1-1`.
- Práctica: `## Paso 6.1.1 - Añadir la dependencia de Spring Security` / hash `489c7738ae165a7bbc65ffc6595843ec33cdede0621dd1170b98d0caebca25aa`.
- Acción: Añadir starter de Spring Security preservando todas las dependencias M5.
- Artefacto final: `M6/proyecto/pom.xml`.
- Símbolos: `pom.xml`, `spring-boot-starter-security`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: Dependencia resuelta y compilación sin pérdida de dependencias heredadas.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.2 - Arrancar y ver el “susto”

- Fuente: p. 1220 / `Paso 2: Arrancar y ver el "susto"` / hash `a12a2a2586a767aa3bca8e12555f652d7e69de9ba203097b810b03baec96121c`.
- Teoría: `TC-6.1-1`.
- Práctica: `## Paso 6.1.2 - Arrancar y ver el “susto”` / hash `9dd3de9bd33b08f93e57fe07cc0ccb65a3a29ddb9effa38b0bfb3c574043d967`.
- Acción: Arrancar la aplicación y observar la protección por defecto.
- Artefacto final: `M6/proyecto/pom.xml`.
- Símbolos: `SecurityConfig`.
- Comando: `./mvnw spring-boot:run`.
- Observable: Endpoint antes público queda protegido y aparece contraseña dev.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.3 - Autenticarse con la contraseña generada

- Fuente: p. 1221 / `Paso 3: Autenticarse con la contraseña generada` / hash `170d32d43bc30215257f38f27f352f6fc0353eb05aaf4928ed6be465226b4978`.
- Teoría: `TC-6.1-4`.
- Práctica: `## Paso 6.1.3 - Autenticarse con la contraseña generada` / hash `1b9e7240637252065116833713cbd7d7bb8b025c57548638be0e33a13260d6cf`.
- Acción: Usar el usuario generado del arranque para autenticar una petición HTTP Basic.
- Artefacto final: `M6/proyecto/pom.xml`.
- Símbolos: n/a.
- Comando: `curl -i -u user:<PASSWORD_DEL_LOG> http://localhost:8080/hola`.
- Observable: Credenciales válidas atraviesan la autenticación.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.4 - Ver los filtros en acción

- Fuente: p. 1222 / `Paso 4: Ver los filtros en acción` / hash `555551cec8ab85b2b2ec8a2286b33deb50848cd4ac7de7625a627ac879bfbf9e`.
- Teoría: `TC-6.1-2`.
- Práctica: `## Paso 6.1.4 - Ver los filtros en acción` / hash `2a9a285d2cff3f6bc7ad7ba6859974dcffab598f3342c042bf57b4fe2fea0f4b`.
- Acción: Activar DEBUG temporal y relacionar la petición con la cadena de filtros.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: n/a.
- Comando: `curl -i http://localhost:8080/hola`.
- Observable: Logs muestran filtros/decisiones de Spring Security.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.5 - Crear `SecurityConfig`

- Fuente: p. 1223 / `Paso 5: Crear la clase SecurityConfig` / hash `2904204bd7823a217d2f3508129e03c6dc9c0c1a81c08d620a6caec2c64443a2`.
- Teoría: `TC-6.1-3`.
- Práctica: `## Paso 6.1.5 - Crear `SecurityConfig`` / hash `ef26313d7e8c57fa85aca09c23ded15083227d992ba1a5e6ade76ee8e783cee2`.
- Acción: Crear SecurityConfig con un bean SecurityFilterChain.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `SecurityConfig`, `SecurityFilterChain`, `WebSecurityConfigurerAdapter`, `filterChain`.
- Comando: `./mvnw -B test`.
- Observable: Configuración moderna declarativa presente; no WebSecurityConfigurerAdapter.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.6 - Ver el comportamiento sin usuarios definidos por nosotros

- Fuente: p. 1224 / `Paso 6: Ver el comportamiento sin usuarios` / hash `00f6807dc9f7adf4d02334d44a186a473530be98c68e17c2be12a8f997b8102d`.
- Teoría: `TC-6.1-4`.
- Práctica: `## Paso 6.1.6 - Ver el comportamiento sin usuarios definidos por nosotros` / hash `9505bb3d2eee1b1a52ebd67f87c0b1fbf6b1abc17e9db03137311fac95b2eaf5`.
- Acción: Observar el estado sin UserDetailsService propio.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `UserDetailsService`.
- Comando: `curl -i -u user:<PASSWORD_DEL_LOG> http://localhost:8080/hola`.
- Observable: Cadena y origen de usuarios quedan conceptualmente separados.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.7 - Permitir todos los endpoints temporalmente

- Fuente: p. 1225 / `Paso 7: Permitir todos los endpoints temporalmente` / hash `58293d2d2f355bbe77f5b6db90675665b9bf7c759ddf603f50b67ab9d67d9321`.
- Teoría: `TC-6.1-3`.
- Práctica: `## Paso 6.1.7 - Permitir todos los endpoints temporalmente` / hash `41872fb21ed404f481cde1786e2b9fcba9abbc228d2973e51cf8e021f39b9e2d`.
- Acción: Introducir anyRequest().permitAll() como estado temporal controlado.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `filterChain`.
- Comando: `curl -i http://localhost:8080/api/v1/alumnos`.
- Observable: Endpoints accesibles anónimamente durante el experimento.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.8 - Configurar reglas específicas por endpoint

- Fuente: p. 1226 / `Paso 8: Configurar reglas específicas por endpoint` / hash `3a49d798dcd5c8230adb64d93dddd8e4c0b613fb2fa0f56854c3922297f41404`.
- Teoría: `TC-6.1-3`.
- Práctica: `## Paso 6.1.8 - Configurar reglas específicas por endpoint` / hash `de6b73354ea0812a29b82f6df30d41382c1b69c27af15d30c5f2ae923efefefb`.
- Acción: Cerrar permitAll global y restaurar reglas públicas/específicas.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `authenticated()`, `filterChain`, `permitAll()`.
- Comando: `curl -i http://localhost:8080/api/v1/alumnos`.
- Observable: La protección reaparece; permitAll global ha sido eliminado.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.9 - Ver la diferencia entre 401 y 403

- Fuente: p. 1228 / `Paso 9: Ver la diferencia entre 401 y 403` / hash `564434e9943c99a726733976d6e73765c0dabb380a9df0a23873a1fea5523a7f`.
- Teoría: `TC-6.1-1`, `TC-6.1-4`.
- Práctica: `## Paso 6.1.9 - Ver la diferencia entre 401 y 403` / hash `2363a68164cbbd5ac76e48275262aa156933f5e4c8d48596ec31ffb2e1bc5413`.
- Acción: Introducir y cerrar una regla de rol temporal para observar 401 y 403.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `ADMIN`, `authenticated()`.
- Comando: `curl -i http://localhost:8080/api/v1/alumnos && curl -i -u user:<PASSWORD_DEL_LOG> http://localhost:8080/api/v1/alumnos`.
- Observable: 401 sin identidad y 403 con identidad sin rol; luego regla temporal retirada.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.10 - Depurar con logs

- Fuente: p. 1228 / `Paso 10: Depurar con logs` / hash `d6540c396979fdf93ca7806424731f6f5096bca592d13c97ed03ec3adb09760f`.
- Teoría: `TC-6.1-2`.
- Práctica: `## Paso 6.1.10 - Depurar con logs` / hash `61b12081251838e8b376bf4cfb1d7540d7d33e591a5861f3f975967157247f05`.
- Acción: Diagnosticar decisiones de la cadena mediante logs y restaurar logging.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: n/a.
- Comando: `curl -i http://localhost:8080/api/v1/alumnos`.
- Observable: Logs permiten localizar autenticación/autorización y DEBUG se restaura.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.11 - Errores comunes del ejercicio

- Fuente: p. 1229 / `Paso 11: Errores comunes del ejercicio` / hash `7fa2abd908b91b38b38b6ed9150f50d7eaadcc85cff6292a79db933349f026b5`.
- Teoría: `TC-6.1-3`.
- Práctica: `## Paso 6.1.11 - Errores comunes del ejercicio` / hash `1a90eeeb0a3385ecf6a241188168916f053d2ea078f1f429b2efa7c5c3008740`.
- Acción: Comprobar anti-patrones y restauraciones antes de cerrar el punto.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `WebSecurityConfigurerAdapter`, `anyRequest().permitAll()`.
- Comando: `./mvnw -B test`.
- Observable: No sobreviven estados pedagógicos inseguros.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.1.12 - Reto resuelto: endpoint público sin autenticación

- Fuente: p. 1230 / `Paso 12: Reto resuelto — Endpoint público sin autenticación` / hash `50f77d84a26afc83f458645f737d3a19477ad567c54430fd86e8567f25557930`.
- Teoría: `TC-6.1-3`.
- Práctica: `## Paso 6.1.12 - Reto resuelto: endpoint público sin autenticación` / hash `870aa28b8d99947ed459cbc13fd5954382bb8d1d75ca67c4f914d7fdf210db11`.
- Acción: Crear /api/v1/public/info y verificar público vs protegido.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/info/PublicInfoController.java`.
- Símbolos: `InMemoryUserDetailsManager`, `PublicController`, `PublicInfoController`, `info`, `permitAll()`.
- Comando: `curl -i http://localhost:8080/api/v1/public/info && curl -i http://localhost:8080/api/v1/alumnos`.
- Observable: 200 en ruta pública; ruta protegida exige autenticación.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.1.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.1 - Repasar el estado actual

- Fuente: p. 1251 / `Paso 1: Repasar el estado actual` / hash `6ecbc4c0eb8abd97c45b7da316e7b3584d9e542dcdaae3a9ced874f81d6605fe`.
- Teoría: `TC-6.2-1`, `TC-6.2-4`.
- Práctica: `## Paso 6.2.1 - Repasar el estado actual` / hash `e64fd9d0e3f8ffdf05bf7cc8b4e7c9d2b8d360a156264d409a691f852f7ab30c`.
- Acción: Verificar que 6.1 está cerrado y que M5/CORS siguen siendo el baseline acumulativo.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `SecurityFilterChain`, `permitAll()`, `pom.xml`, `spring-boot-starter-security`.
- Comando: `./mvnw -B test`.
- Observable: No existe permitAll global; Security y CORS son evolución del baseline.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.2 - Añadir un endpoint que devuelva el perfil del usuario

- Fuente: p. 1252 / `Paso 2: Añadir un endpoint que devuelva el perfil del usuario` / hash `d993b2e70ed37f41dc92c29794bdbbed366ffce8a14adf21c6739969ddc2236d`.
- Teoría: `TC-6.2-1`.
- Práctica: `## Paso 6.2.2 - Añadir un endpoint que devuelva el perfil del usuario` / hash `349aac4f9d6e3ee0ddc10d4d24f5b0caea4efe3d994addaec1d7fc4903bd072c`.
- Acción: Crear PerfilController para exponer Authentication y authorities.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `Authentication`, `PerfilController`, `anyRequest().authenticated()`, `authentication`, `perfil`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/perfil`.
- Observable: El perfil refleja username y authorities del Authentication resuelto.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.3 - Definir el `PasswordEncoder`

- Fuente: p. 1253 / `Paso 3: Definir el PasswordEncoder` / hash `9a8e54c0052d8f005604cd28ecb4816a873555f296e8b1bb05bb780910ba05d1`.
- Teoría: `TC-6.2-3`.
- Práctica: `## Paso 6.2.3 - Definir el `PasswordEncoder`` / hash `b9f802f0460159e241558d75d79fb681ab0d94d7cdfe03ec5e85d2e9a59fb8fa`.
- Acción: Declarar PasswordEncoder estable con PasswordEncoderFactories.createDelegatingPasswordEncoder().
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `@Bean`, `NoSuchBeanDefinitionException: PasswordEncoder`, `PasswordEncoder`, `SecurityConfig`, `ana123`, `encode`, `matches`, `passwordEncoder`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: Existe un encoder delegante reutilizable en los puntos siguientes.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.4 - Definir el `UserDetailsService`

- Fuente: p. 1254 / `Paso 4: Definir el UserDetailsService` / hash `bc0fb627863633ba2b2e39fe82712ad264bb0ea16d45031f6f52475455660685`.
- Teoría: `TC-6.2-1`, `TC-6.2-2`, `TC-6.2-3`.
- Práctica: `## Paso 6.2.4 - Definir el `UserDetailsService`` / hash `2cca0a08ae560e93fd9afd78f625c14d5e8a882cc196f81bec49f82c0f707169`.
- Acción: Introducir UserDetailsService temporal con ana y admin mediante InMemoryUserDetailsManager.
- Artefacto final: `InMemoryUserConfig.java` (transitorio).
- Símbolos: `"ADMIN"`, `.roles("ROLE_ADMIN")`, `InMemoryUserConfig`, `ROLE_`, `ROLE_ADMIN`, `ROLE_USER`, `UserDetailsService`, `admin`, `ana`, `roles(...)`, `userDetailsService`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: ana y admin quedan disponibles con hashes y roles controlados.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.5 - Cerrar los endpoints

- Fuente: p. 1256 / `Paso 5: Cerrar los endpoints` / hash `c50d87c70111743fab88a8ddc64a6b5f0f5d923e45198a3f0d0643ad019bc68c`.
- Teoría: `TC-6.2-4`.
- Práctica: `## Paso 6.2.5 - Cerrar los endpoints` / hash `a4e73dd8d033b13f2a79d7a0ef6198929df93cb8fb2c18b24e0e5c26e9e4c0ee`.
- Acción: Cerrar rutas por defecto y declarar reglas públicas, ADMIN y GESTOR conservando CORS.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `SecurityFilterChain`, `anyRequest()`, `cors(Customizer.withDefaults())`, `filterChain`, `permitAll()`, `sameOrigin()`.
- Comando: `./mvnw -B test`.
- Observable: Rutas públicas, autenticadas y por rol quedan ordenadas explícitamente; CORS permanece habilitado.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.6 - Arrancar y probar sin autenticación

- Fuente: p. 1257 / `Paso 6: Arrancar y probar sin autenticación` / hash `c0821f54ccba688851965daf28adcd905f63e456f6aa6fae91c6b1297967c24c`.
- Teoría: `TC-6.2-4`.
- Práctica: `## Paso 6.2.6 - Arrancar y probar sin autenticación` / hash `5d8afa19a8cfdfcf3410fe185ef629ad44e7c64c0e281f9f3933f7ff8c58efa8`.
- Acción: Comprobar 401 anónimo y 200 en la ruta pública.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/info/PublicInfoController.java`.
- Símbolos: `200 OK`, `401 Unauthorized`, `SecurityFilterChain`, `WWW-Authenticate`.
- Comando: `curl -i http://localhost:8080/api/v1/alumnos && curl -i http://localhost:8080/api/v1/public/info`.
- Observable: alumnos anónimo=401; public/info=200.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.7 - Autenticarse con el usuario `ana`

- Fuente: p. 1258 / `Paso 7: Autenticarse con el usuario ana` / hash `3d430bcf524edd1db88997f773aa371e710714366f2dafc777d12391e653981d`.
- Teoría: `TC-6.2-1`, `TC-6.2-5`.
- Práctica: `## Paso 6.2.7 - Autenticarse con el usuario `ana`` / hash `6fc2dc96e2d82fcd015b6bd3d1dffc618eb3073d20892472d4b67ed50d08e78f`.
- Acción: Autenticar a ana y comprobar ROLE_USER.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `ROLE_`, `ana`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/alumnos && curl -i -u ana:ana123 http://localhost:8080/api/v1/perfil`.
- Observable: ana accede a ruta autenticada y muestra ROLE_USER.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.8 - Autenticarse con el usuario `admin`

- Fuente: p. 1259 / `Paso 8: Autenticarse con el usuario admin` / hash `d22cfedafbc44c1d6eb7fd665df1643ebc5d6de5cf68b6f7789843eb295ee5f5`.
- Teoría: `TC-6.2-1`, `TC-6.2-5`.
- Práctica: `## Paso 6.2.8 - Autenticarse con el usuario `admin`` / hash `f914fb5c003b957af2f334c72577bdab5f33b11e0b73070dec2833b77d38390e`.
- Acción: Autenticar a admin y comprobar ROLE_ADMIN + ROLE_USER.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `ROLE_ADMIN`, `ROLE_USER`, `admin`.
- Comando: `curl -i -u admin:admin123 http://localhost:8080/api/v1/perfil`.
- Observable: admin muestra ROLE_ADMIN y ROLE_USER.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.9 - Probar la autorización por rol

- Fuente: p. 1259 / `Paso 9: Probar la autorización por rol` / hash `7e605f0c51d99ef0ccab9921897ef051a8c19b217dc7071a54918411f3bec739`.
- Teoría: `TC-6.2-1`, `TC-6.2-4`.
- Práctica: `## Paso 6.2.9 - Probar la autorización por rol` / hash `0adfc1e8f0e99baf068c298540cde4eef61a1e87d334f880245cfcbfbcfd4124`.
- Acción: Demostrar 403 para ana frente a una ruta ADMIN y ausencia de 403 para admin.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `403 Forbidden`, `404 Not Found`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/admin/usuarios && curl -i -u admin:admin123 http://localhost:8080/api/v1/admin/usuarios`.
- Observable: ana=403; admin supera autorización y puede llegar a 404 si el mapping aún no existe.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.10 - Probar con credenciales incorrectas

- Fuente: p. 1260 / `Paso 10: Probar con credenciales incorrectas` / hash `5f56afd5b72281c269e4d9d98f717b93dbd6b95499465dddf80422d00c73672b`.
- Teoría: `TC-6.2-3`, `TC-6.2-5`.
- Práctica: `## Paso 6.2.10 - Probar con credenciales incorrectas` / hash `a0895daf580e5f8a34c0b34ecb478112131e141c96aa75f4f3ec95ecc645fcb6`.
- Acción: Demostrar 401 indistinguible para contraseña incorrecta y usuario inexistente.
- Artefacto final: `InMemoryUserConfig.java` (transitorio).
- Símbolos: n/a.
- Comando: `curl -i -u ana:contraseñaIncorrecta http://localhost:8080/api/v1/alumnos && curl -i -u pedro:loQueSea http://localhost:8080/api/v1/alumnos`.
- Observable: ambos fallos de credenciales devuelven 401 sin enumerar usuarios.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.11 - Errores comunes del ejercicio

- Fuente: p. 1261 / `Paso 11: Errores comunes del ejercicio` / hash `9ba9bba7aefa370e50f97b97413f6dfb1abdc465e92e1480914e7c1f13a1ab92`.
- Teoría: `TC-6.2-3`, `TC-6.2-4`, `TC-6.2-5`.
- Práctica: `## Paso 6.2.11 - Errores comunes del ejercicio` / hash `23e9f10ec7dfa33e6fc1695946f4717fa49fe60536cb93de6b3b34b4b0b1964b`.
- Acción: Diagnosticar fallos de encoder, beans, roles, imports, logs y CORS sin rebajar el contrato.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `.cors(...)`, `.roles(...)`, `@Bean`, `NoOpPasswordEncoder`, `NoSuchBeanDefinitionException: PasswordEncoder`, `OPTIONS`, `PasswordEncoder`, `UserDetailsService`, `passwordEncoder.encode(...)`.
- Comando: `./mvnw -B test`.
- Observable: Los errores se resuelven sin introducir NoOpPasswordEncoder, abrir rutas o borrar tests.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.2.12 - Reto resuelto: tercer usuario con roles combinados

- Fuente: p. 1262 / `Paso 12: Reto resuelto — Añadir un tercer usuario con roles combinados` / hash `29e5777a925515c3f2672d7bf8fa7ca42e63669f12cb779e8618df434f28b743`.
- Teoría: `TC-6.2-1`, `TC-6.2-2`, `TC-6.2-4`.
- Práctica: `## Paso 6.2.12 - Reto resuelto: tercer usuario con roles combinados` / hash `089977909b8d8abf57ad39053b2a194b72c8dd56690b4b875588d924907146ec`.
- Acción: Añadir gestor con GESTOR+USER y endpoint autorizado por GESTOR o ADMIN.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/GestorController.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `DelegatingPasswordEncoder`, `GESTOR`, `GestorController`, `InMemoryUserConfig`, `PasswordEncoder`, `PerfilController`, `USER`, `UserDetailsService`, `admin`, `ana`, `curl`, `documentos`, `gestor`, `hasAnyRole("GESTOR", "ADMIN")`, `hasRole("ADMIN")`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/gestor/documentos && curl -i -u gestor:gestor123 http://localhost:8080/api/v1/gestor/documentos && curl -i -u admin:admin123 http://localhost:8080/api/v1/gestor/documentos`.
- Observable: ana=403; gestor=200; admin=200 para /api/v1/gestor/documentos.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.2.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.1 - Crear el paquete `auth`

- Fuente: p. 1290 / `Paso 1: Crear el paquete auth` / hash `fb0d6678ba27673ed6613536670193eca3397fa225dfba32964188aec0f100eb`.
- Teoría: `TC-6.3-1`.
- Práctica: `## Paso 6.3.1 - Crear el paquete `auth`` / hash `f244a19672178617605b06d79ca85dcf899c0303f9b34ba78291cddc885c1cd4`.
- Acción: Consolidar la funcionalidad de seguridad en el paquete auth sin reiniciar el proyecto.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `GestorController`, `auth`.
- Comando: `find src/main/java/es/mecd/demo/miproyecto/auth -maxdepth 1 -type f`.
- Observable: auth agrupa la nueva capacidad y conserva GestorController heredado de 6.2.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.2 - Crear la entidad `Rol`

- Fuente: p. 1290 / `Paso 2: Crear la entidad Rol` / hash `1cec683c8160513d98209491f1a5e132f2fd3951ad25d6da6e723d44e7c5633e`.
- Teoría: `TC-6.3-1`.
- Práctica: `## Paso 6.3.2 - Crear la entidad `Rol`` / hash `a59bbf1dcdc21d14b1d133ca1c0de58464a5f44f4e3a3c5fe19e1ab507b7b4b6`.
- Acción: Modelar Rol como entidad y lado inverso de la relación many-to-many.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Rol.java`.
- Símbolos: `@JoinTable`, `Rol`, `Usuario.roles`, `getDescripcion`, `getId`, `getNombre`, `getUsuarios`, `setDescripcion`, `setId`, `setNombre`, `setUsuarios`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: roles tiene nombre único y relación inversa sin JoinTable duplicada.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.3 - Crear la entidad `Usuario`

- Fuente: p. 1292 / `Paso 3: Crear la entidad Usuario` / hash `a2d32c455dc40f225dcdeebe5f39a0b2bc72f8027ebb8c468cee2749ca2a4d68`.
- Teoría: `TC-6.3-1`.
- Práctica: `## Paso 6.3.3 - Crear la entidad `Usuario`` / hash `4266911c247b42d6296750f8eecffd7f65e651bc4810700b9792343f77dcc3eb`.
- Acción: Modelar Usuario con credenciales, estado y Set<Rol> mediante usuarios_roles.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java`.
- Símbolos: `List`, `Set`, `Usuario`, `activo`, `email`, `fechaCreacion`, `id`, `password`, `roles`, `username`, `usuarios`, `usuarios_roles`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: usuarios define usuarios_roles y almacena sólo hash, email, estado y roles.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.4 - Crear los repositorios

- Fuente: p. 1295 / `Paso 4: Crear los repositorios` / hash `d4659a20ae92b42d1e76f2721f710a73885d3bb9400f5996b0b3131d0d6e7699`.
- Teoría: `TC-6.3-1`, `TC-6.3-2`.
- Práctica: `## Paso 6.3.4 - Crear los repositorios` / hash `fbe05353dbdff9bec06967acda66718f10c9988f2bed922061c9d6b242ab9d85`.
- Acción: Crear repositorios Spring Data para usuarios y roles.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RolRepository.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioRepository.java`.
- Símbolos: `RolRepository`, `UsuarioRepository`, `username`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: repositorios resuelven búsqueda/duplicados mediante Spring Data.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.5 - Crear el `UserDetailsService` personalizado

- Fuente: p. 1296 / `Paso 5: Crear el UserDetailsService personalizado` / hash `74db391086366739ed4a111cfa305a065f0d49d173b56a6418759628337ad5ae`.
- Teoría: `TC-6.3-2`.
- Práctica: `## Paso 6.3.5 - Crear el `UserDetailsService` personalizado` / hash `09787f1ef1db20ebfbaf074b0de917cac17238ecab4d72ccbc0885ae514adb98`.
- Acción: Sustituir la carga en memoria por UsuarioDetailsService respaldado por UsuarioRepository.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioDetailsService.java`.
- Símbolos: `ADMIN`, `ROLE_`, `ROLE_ADMIN`, `UserDetailsService`, `UsuarioDetailsService`, `loadUserByUsername`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: roles persistidos se transforman a ROLE_* y una identidad inactiva queda bloqueada/deshabilitada.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.6 - Eliminar los usuarios en memoria

- Fuente: p. 1299 / `Paso 6: Eliminar los usuarios en memoria` / hash `6059e5bb26864127ae1f16b82ff0b40936afc15e5016f8b4c4a1de28578f5bda`.
- Teoría: `TC-6.3-3`.
- Práctica: `## Paso 6.3.6 - Eliminar los usuarios en memoria` / hash `fcd3ced59efe82aa1c08a11b2f0dbc8a30a2e5903f110189c5f7b97aaf7a8290`.
- Acción: Eliminar físicamente InMemoryUserConfig y cerrar T6.2-A.
- Artefacto final: `InMemoryUserConfig.java` (transitorio).
- Símbolos: n/a.
- Comando: `grep -R "InMemoryUserDetailsManager" src/main/java || true`.
- Observable: InMemoryUserDetailsManager desaparece del código de producción; T6.2-A queda cerrado.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.7 - Crear el inicializador de usuarios

- Fuente: p. 1299 / `Paso 7: Crear el inicializador de usuarios` / hash `d00919e0360487e963adb5182285c80a26558927b401e67f9ce0ca2eca1c8d21`.
- Teoría: `TC-6.3-3`.
- Práctica: `## Paso 6.3.7 - Crear el inicializador de usuarios` / hash `2ac1c1721673ec4d21b38fcb3ff4c1cdf94e064160edb6bbc08a46669756852a`.
- Acción: Sembrar roles/usuarios con algoritmo idempotente preservando el reto GESTOR de 6.2.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`.
- Símbolos: `GESTOR`, `UsuariosInicialesConfig`, `admin`, `ana`, `cargarUsuarios`, `count() > 0`, `gestor`.
- Comando: `./mvnw spring-boot:run`.
- Observable: reinicios no duplican identidades y GESTOR no se pierde al migrar a DB.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.8 - Arrancar y verificar

- Fuente: p. 1301 / `Paso 8: Arrancar y verificar` / hash `09fdea11a09b3495a3313d12b502423d7431e00743d37a8f94b7948bbfe64a19`.
- Teoría: `TC-6.3-1`, `TC-6.3-3`.
- Práctica: `## Paso 6.3.8 - Arrancar y verificar` / hash `43b310c29923696dd4e2827078bc03c1e226a1b0471d714eb6b56b9837c227ae`.
- Acción: Verificar tablas usuarios, roles y usuarios_roles y sus relaciones.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Rol.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java`.
- Símbolos: `rol_id`, `roles`, `usuario_id`, `usuarios`, `usuarios_roles`.
- Comando: `SELECT * FROM usuarios; SELECT * FROM roles; SELECT * FROM usuarios_roles;`.
- Observable: existen usuarios, roles y pares de la tabla intermedia.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.9 - Probar la autenticación

- Fuente: p. 1302 / `Paso 9: Probar la autenticación` / hash `c0f746e2e024aaaa76ab5ed360aff6b0a4e5d01b6cdf77cc328bf7eda5597611`.
- Teoría: `TC-6.3-2`, `TC-6.3-3`.
- Práctica: `## Paso 6.3.9 - Probar la autenticación` / hash `964f37a8119184d33b2e09691f700e1855df22233c5df6fca9faeccf8eb91e25`.
- Acción: Autenticar ana/admin/gestor desde persistencia.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioDetailsService.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `InMemoryUserDetailsManager`, `ROLE_ADMIN`, `ROLE_USER`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/perfil`.
- Observable: ana/admin/gestor conservan sus comportamientos pero proceden de DB.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.10 - Crear los DTOs de registro

- Fuente: p. 1302 / `Paso 10: Crear el endpoint de registro` / hash `37007d99771f224594efaf9db4c200d7935d12711f0411dc49d566c068fba16a`.
- Teoría: `TC-6.3-4`.
- Práctica: `## Paso 6.3.10 - Crear los DTOs de registro` / hash `7941433e231c408f50e02b2e7b5ad22ac6a5a2b0fdf592bb20fe95ad56e3e89b`.
- Acción: Crear DTOs de registro sin exponer password.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RegistroRequestDTO.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioResponseDTO.java`.
- Símbolos: `RegistroRequestDTO`, `UsuarioResponseDTO`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: la representación de salida carece de campo password.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.11 - Crear `AuthService` y `AuthController`

- Fuente: p. 1304 / `Paso 11: Crear el AuthService y el AuthController` / hash `e90de42a663f1fecb556d67ac37e0432e22ca2f1170d2f7323544e3b70b45e3d`.
- Teoría: `TC-6.3-4`.
- Práctica: `## Paso 6.3.11 - Crear `AuthService` y `AuthController`` / hash `2ac4a5fd132b02a1ca059bd1118a4f03e6afc28fa9b2faa7eee90f98e3a7ac28`.
- Acción: Crear registro transaccional y controlador POST /api/v1/auth/registro.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`.
- Símbolos: `201 Created`, `@Deprecated`, `AuthController`, `AuthService`, `AuthService.registrar(...)`, `NegocioException`, `OperacionNoPermitidaException`, `RecursoDuplicadoException`, `RecursoNoEncontradoException`, `USER`, `registrar`, `toDTO`.
- Comando: `./mvnw -B test`.
- Observable: registro cifra, asigna USER, aplica unicidad y responde DTO.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.12 - Configurar el endpoint público y probar

- Fuente: p. 1308 / `Paso 12: Configurar el endpoint público y probar` / hash `cf2ea058020b3886776496a88b817f6fe9ddd3976d0e200037fb95a970a8fd99`.
- Teoría: `TC-6.3-3`, `TC-6.3-4`.
- Práctica: `## Paso 6.3.12 - Configurar el endpoint público y probar` / hash `21e23feabf69167c87000b87d44b31aeee2fd809378b61997cfbed93e12989df`.
- Acción: Permitir sólo el POST de registro y verificar alta + autenticación de pedro.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `201 Created`, `ROLE_USER`, `SecurityConfig`.
- Comando: `curl -i -X POST http://localhost:8080/api/v1/auth/registro -H "Content-Type: application/json" -d '{"username":"pedro","password":"pedro1234","email":"pedro@educacion.gob.es"}'`.
- Observable: registro anónimo=201; pedro se autentica como USER; otras rutas auth aún no se abren.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.13 - Errores comunes del ejercicio

- Fuente: p. 1309 / `Paso 13: Errores comunes del ejercicio` / hash `9264ca090d3fb8c5d942b1932a0b93284d66b7ce2a36080ea75de2de40a4b1a8`.
- Teoría: `TC-6.3-5`.
- Práctica: `## Paso 6.3.13 - Errores comunes del ejercicio` / hash `5cf1bdcc943d5c5e677a7c97e24f532d3c9d8c9a94283af80ed0e68ba7efed42`.
- Acción: Comprobar duplicados, encoding, roles, transacciones y ausencia de una segunda fuente de usuarios.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `@JoinTable`, `ADMIN`, `InMemoryUserConfig`, `LazyInitializationException`, `ROLE_`, `ROLE_ADMIN`, `Set`, `UserDetailsService`, `UsernameNotFoundException`, `Usuario`, `encode`, `hasRole`.
- Comando: `./mvnw -B test`.
- Observable: fallos esperables conservan el contrato de seguridad y errores M5.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.13 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.3.14 - Reto resuelto: cambiar la contraseña

- Fuente: p. 1311 / `Paso 14: Reto resuelto — Endpoint para cambiar la contraseña` / hash `026e6310af86e4c86ee955ba19e4b27400e293ed8daeadb91b626f12004c6481`.
- Teoría: `TC-6.3-5`.
- Práctica: `## Paso 6.3.14 - Reto resuelto: cambiar la contraseña` / hash `ef8516148b2306a0b66cadda13cf86dd8db548d7a71bfb628d5f6b3f7323d27c`.
- Acción: Implementar cambio autenticado de contraseña en /api/v1/perfil/password.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CambioPasswordRequestDTO.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `204 No Content`, `401`, `@ManyToMany`, `CambioPasswordRequestDTO`, `PerfilController`, `Rol`, `Usuario`, `UsuarioDetailsService`, `cambiarPassword`, `save`.
- Comando: `curl -i -X PUT -u pedro:pedro1234 http://localhost:8080/api/v1/perfil/password -H "Content-Type: application/json" -d '{"passwordActual":"pedro1234","passwordNueva":"pedro5678"}'`.
- Observable: cambio correcto=204; nueva contraseña autentica; antigua deja de autenticar.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.3.14 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.1 - Repasar el estado actual

- Fuente: p. 1335 / `Paso 1: Repasar el estado actual` / hash `6779b87d7cec3d5db245ba66842d79aaa226cf9d5bc60741703fc3196db906e1`.
- Teoría: `TC-6.4-1`.
- Práctica: `## Paso 6.4.1 - Repasar el estado actual` / hash `22c8e4b149e395d8277e70c01868a2b43651af8d932eef75516c738550cdf3cb`.
- Acción: Verificar el estado persistente de 6.3 y la regla administrativa vigente.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `ADMIN`, `GESTOR`, `USER`, `UsuarioDetailsService`.
- Comando: `grep -R "InMemoryUserDetailsManager" src/main/java || true`.
- Observable: usuarios DB activos, in-memory cerrado y admin URL ya protegida.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.2 - Activar la seguridad por método

- Fuente: p. 1336 / `Paso 2: Activar la seguridad por método` / hash `1b40df9960899a6822d920541ce978fc8a1274d17f17173866c5c5c7eab8d90a`.
- Teoría: `TC-6.4-1`, `TC-6.4-3`.
- Práctica: `## Paso 6.4.2 - Activar la seguridad por método` / hash `764923714fb273afa3d9a8c95dcc675eb28910bedd88df63dbe9a68036466595`.
- Acción: Activar @EnableMethodSecurity para que las anotaciones de autorización sean efectivas.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `@EnableMethodSecurity`, `@PreAuthorize`, `SecurityConfig`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: @PreAuthorize pasa a ser una regla ejecutable mediante proxies Spring.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.3 - Crear un `UsuarioController` de administración

- Fuente: p. 1336 / `Paso 3: Crear un UsuarioController de administración` / hash `d98ea227e1c1311ac5dcbb17a830249a672d26ee63ab4770c4489964d9a298fd`.
- Teoría: `TC-6.4-1`, `TC-6.4-3`.
- Práctica: `## Paso 6.4.3 - Crear un `UsuarioController` de administración` / hash `1c59f2defaf53af78653bbef91d38b5836d919cb98dfba656b2c14608b65ce56`.
- Acción: Crear UsuarioController bajo /api/v1/admin/usuarios con @PreAuthorize ADMIN.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`.
- Símbolos: `SecurityFilterChain`, `UsuarioController`, `consultar`, `listar`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: administración exige ADMIN también a nivel de método.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.4 - Crear el `UsuarioService`

- Fuente: p. 1338 / `Paso 4: Crear el UsuarioService` / hash `4e8f8766613d8d721e6b0f6fe8538a0a7c955aa97f789b4537c1b4c581ca8611`.
- Teoría: `TC-6.4-1`.
- Práctica: `## Paso 6.4.4 - Crear el `UsuarioService`` / hash `348cabf9e0c1111eb1decac8143981e268ae29665be6d15d95563eb8d61f2483`.
- Acción: Crear UsuarioService para listar, consultar y proyectar usuarios.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioService.java`.
- Símbolos: `@PreAuthorize`, `RolRepository`, `UsuarioRepository`, `UsuarioService`, `consultar`, `listarTodos`, `toDTO`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: listado/consulta devuelven DTO sin password.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.5 - Añadir reglas de URL y de método combinadas

- Fuente: p. 1340 / `Paso 5: Añadir reglas de URL y de método combinadas` / hash `28acf9629fcae8cd3d45ad70c555e3eb42943c3a9dce30c8a7ab322768db2fc8`.
- Teoría: `TC-6.4-1`, `TC-6.4-2`, `TC-6.4-3`.
- Práctica: `## Paso 6.4.5 - Añadir reglas de URL y de método combinadas` / hash `08a1193a2e6f2a7fa19d7e68618c315fd4be11cff658f92a7d256378a0abc64f`.
- Acción: Combinar hasRole ADMIN en URL con @PreAuthorize en métodos.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `@PreAuthorize`, `filterChain`.
- Comando: `./mvnw -B test`.
- Observable: USER queda bloqueado en URL y ADMIN atraviesa ambas capas.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.6 - Probar la autorización con distintos usuarios

- Fuente: p. 1341 / `Paso 6: Probar la autorización con distintos usuarios` / hash `87c4899b43347b95f637f603b5fcd20876f6eb0f9fd5b55af68dff69ced74dff`.
- Teoría: `TC-6.4-1`, `TC-6.4-5`.
- Práctica: `## Paso 6.4.6 - Probar la autorización con distintos usuarios` / hash `60b2c9f0e620946dac4db33afa330b129df6eb2007c15b776b20ecb4e99be23f`.
- Acción: Demostrar 401 anónimo, 403 USER y 200 ADMIN.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`.
- Símbolos: n/a.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/admin/usuarios && curl -i -u admin:admin123 http://localhost:8080/api/v1/admin/usuarios`.
- Observable: anónimo=401, ana=403, admin=200.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.7 - Crear un endpoint para consultar el propio perfil

- Fuente: p. 1342 / `Paso 7: Crear un endpoint para consultar el propio perfil` / hash `111355b30e1e596fc1e81cc369404bacadcdf0fc2b900172ee74f906dcd462bf`.
- Teoría: `TC-6.4-4`.
- Práctica: `## Paso 6.4.7 - Crear un endpoint para consultar el propio perfil` / hash `e70e621420e11525afcd60cf67e380f1b1c8383e4e2f7d2e7c80d691ed00bb18`.
- Acción: Evolucionar PerfilController para inyectar @AuthenticationPrincipal UserDetails.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `@AuthenticationPrincipal`, `Authentication`, `PerfilController`, `UserDetails`, `getName()`, `perfil`.
- Comando: `curl -i -u ana:ana123 http://localhost:8080/api/v1/perfil`.
- Observable: principal autenticado se obtiene sin consultar manualmente SecurityContextHolder.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.8 - Crear `UsuarioPrincipal` para 6.7

- Fuente: p. 1344 / `Paso 8: Crear el UsuarioPrincipal (para el punto 6.7)` / hash `be8a99ac5e72f99e6d483440a96f3880584ef2037f731c54e87a0689be4e4b95`.
- Teoría: `TC-6.4-4`.
- Práctica: `## Paso 6.4.8 - Crear `UsuarioPrincipal` para 6.7` / hash `4a78892a1dc511766d9eea7b5867de9ce651d74b10c2563d1a61d5ac7b6dcd95`.
- Acción: Crear UsuarioPrincipal enriquecido como artefacto preparado para 6.7 sin activarlo todavía.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java`.
- Símbolos: `User`, `UsuarioDetailsService`, `UsuarioPrincipal`, `getAuthorities`, `getEmail`, `getId`, `getPassword`, `getPassword()`, `getUsername`, `isAccountNonExpired`, `isAccountNonLocked`, `isCredentialsNonExpired`, `isEnabled`, `null`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: clase con id/email/authorities existe pero UsuarioDetailsService aún devuelve User estándar.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.9 - Probar la expresión de autorización

- Fuente: p. 1347 / `Paso 9: Probar la expresión de autorización` / hash `246d5990f650a8fd4f5bc9e44fcc0bb1d16015c3870a8156a280b27b3bfbd116`.
- Teoría: `TC-6.4-3`, `TC-6.4-4`.
- Práctica: `## Paso 6.4.9 - Probar la expresión de autorización` / hash `ea235aef221584d6c54e7d2cf7bde6125b1218499fc40b55ebd9b27ed4aef191`.
- Acción: Documentar/probar negativamente que principal.id no existe aún y restaurar la expresión temporal.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `SpelEvaluationException`, `User`, `UsuarioPrincipal`, `getId()`, `principal.id`.
- Comando: `prueba negativa temporal de authentication.principal.id; restaurar después`.
- Observable: principal.id falla en este estado y ninguna expresión rota sobrevive al paso.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.10 - Escribir tests de autorización

- Fuente: p. 1347 / `Paso 10: Escribir tests de autorización` / hash `d406e4b92597154aef677d206ffbc6156d78b986822038d1e20577d235c1aeb4`.
- Teoría: `TC-6.4-5`.
- Práctica: `## Paso 6.4.10 - Escribir tests de autorización` / hash `ebb4c8b8b334364b1ce82e84107f9f26e428fc2af07c6e733fbc207cd5efbadf`.
- Acción: Crear tests 401/403/200 modernizados con @MockitoBean.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java`.
- Símbolos: `@MockBean`, `@MockitoBean`, `JwtAuthenticationFilter`, `JwtService`, `SecurityConfig`, `TokenRevocationService`, `UsuarioControllerTest`.
- Comando: `./mvnw -B -Dtest=UsuarioControllerTest test`.
- Observable: slice tests expresan tres resultados distintos usando API de mocks moderna.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.11 - Errores comunes del ejercicio

- Fuente: p. 1349 / `Paso 11: Errores comunes del ejercicio` / hash `bf082f00bd128197cbc1612cb5e9bf106224f7b43177c787b134a85ae410c4fb`.
- Teoría: `TC-6.4-5`.
- Práctica: `## Paso 6.4.11 - Errores comunes del ejercicio` / hash `8ca37be330be392630e9936a26a3f43ea73e68e109978d9a85a01abf45978666`.
- Acción: Diagnosticar prefijos, proxies, self-invocation y diferencias 401/403.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `@EnableMethodSecurity`, `@MockBean`, `@MockitoBean`, `@PreAuthorize`, `ROLE_ADMIN`, `UsuarioDetailsService`, `UsuarioPrincipal`, `anyRequest().authenticated()`, `hasAuthority("ADMIN")`, `hasRole`, `hasRole("ADMIN")`, `hasRole("ROLE_ADMIN")`, `id`.
- Comando: `./mvnw -B test`.
- Observable: errores de prefijos/proxy/reglas quedan identificados y no se corrigen abriendo endpoints.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.4.12 - Reto resuelto: endpoint de cambio de rol

- Fuente: p. 1351 / `Paso 12: Reto resuelto — Endpoint de cambio de rol` / hash `b891cfa68ed03f66a67fb2def6db7428d36938c18b7ac6636f320f0b16b227d4`.
- Teoría: `TC-6.4-2`, `TC-6.4-3`, `TC-6.4-5`.
- Práctica: `## Paso 6.4.12 - Reto resuelto: endpoint de cambio de rol` / hash `72d7995728a5adf4fd8ccff5daa3d8e7d4af4493f9423edf65740f23a40930fa`.
- Acción: Permitir sólo a ADMIN sustituir el conjunto de roles de un usuario.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CambioRolesRequestDTO.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioService.java`.
- Símbolos: `@AuthenticationPrincipal`, `@EnableMethodSecurity`, `@MockitoBean`, `CambioRolesRequestDTO`, `UsuarioPrincipal`, `UsuarioService`, `cambiarRoles`, `getRoles`, `save`, `setRoles`.
- Comando: `curl -i -X PUT -u admin:admin123 http://localhost:8080/api/v1/admin/usuarios/2/roles -H "Content-Type: application/json" -d '{"roles":["USER","GESTOR"]}'`.
- Observable: sólo ADMIN puede cambiar roles y todos los nombres deben existir.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.4.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.1 - Entender la estructura con un ejemplo

- Fuente: p. 1371 / `Paso 1: Entender la estructura con un ejemplo` / hash `3168f2f0095b3c7d1867a9a5197ab8fd04f0f4bb8cf9b629e1bf7b4e23068544`.
- Teoría: `TC-6.5-1`, `TC-6.5-2`.
- Práctica: `## Paso 6.5.1 - Entender la estructura con un ejemplo` / hash `5ce43f751a15ad0039415bbf8d6f891e93f2bd664f38de0c864128a60417c9ea`.
- Acción: Inspeccionar header, payload y signature de un JWT didáctico sin confundir decodificación con confianza.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `jwt.io`.
- Comando: `inspección del token didáctico; jwt.io sólo con tokens de ejemplo`.
- Observable: header/payload son legibles y signature es una tercera parte independiente.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.2 - Crear una clase para generar un JWT manualmente

- Fuente: p. 1373 / `Paso 2: Crear una clase para generar un JWT manualmente` / hash `b0850911c0e78aa8a8636c0664d4edd93f7e3d1abe0f7565d63c14b52b0aa29a`.
- Teoría: `TC-6.5-2`, `TC-6.5-3`.
- Práctica: `## Paso 6.5.2 - Crear una clase para generar un JWT manualmente` / hash `877af0b872d4d34a7e7b6c7a78fa27bdbb045a3dc79ad67f1c3b981e1a78fbbe`.
- Acción: Crear JwtManual en código de pruebas usando Base64Url y HmacSHA256 de Java estándar.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `DIDACTIC_SECRET`, `JwtManual`, `base64UrlEncode`, `firmarHMACSHA256`, `main`.
- Comando: `./mvnw -B -DskipTests test-compile`.
- Observable: se genera un compacto header.payload.signature con Base64Url sin padding.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.3 - Decodificar el token generado

- Fuente: p. 1375 / `Paso 3: Decodificar el token generado` / hash `de790767cc04d2d1554ad0d6605c58a5fe1b9642fd51a1aaddc96771f1c7e468`.
- Teoría: `TC-6.5-2`, `TC-6.5-5`.
- Práctica: `## Paso 6.5.3 - Decodificar el token generado` / hash `82cb3730291dc50a5d8e61a20072dcfc6dc8f7ab632f27054747224281ddf1ed`.
- Acción: Decodificar localmente la segunda parte del token.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `decodificarPayload`, `main`.
- Comando: `./mvnw -B -Dtest=JwtManualTest test`.
- Observable: payload JSON se obtiene sin conocer la clave.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.4 - Modificar el payload sin actualizar la firma

- Fuente: p. 1375 / `Paso 4: Modificar el payload sin actualizar la firma` / hash `1a7f75252620919b18a147e6294074c7a1ef0a8fbd5ea7e5ded0abca5d9db261`.
- Teoría: `TC-6.5-3`.
- Práctica: `## Paso 6.5.4 - Modificar el payload sin actualizar la firma` / hash `f0f74ea00cae5be37238d60a1475729e5d3419cec0f9700e064ffd15ca9eb28c`.
- Acción: Manipular el payload conservando la firma para demostrar pérdida de integridad.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: n/a.
- Comando: `./mvnw -B -Dtest=JwtManualTest test`.
- Observable: el token manipulado sigue decodificando pero falla verificación.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.5 - Verificar el token con Java

- Fuente: p. 1376 / `Paso 5: Verificar el token con Java` / hash `521d236e94db276792c8bb7a00e659217b7e798e5bebebac77111cf64fb56052`.
- Teoría: `TC-6.5-3`.
- Práctica: `## Paso 6.5.5 - Verificar el token con Java` / hash `e96b188e8c0f9157d528d5a41d946a46ad883735fb471720f11aeefb69eadfc5`.
- Acción: Recalcular HMAC y comparar firmas en Java.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManualTest.java`.
- Símbolos: `JwtManual.verificar(jwt, secret)`, `MessageDigest.isEqual`, `exp`, `false`, `header.payload`, `main`, `true`, `verificar`.
- Comando: `./mvnw -B -Dtest=JwtManualTest test`.
- Observable: token intacto=true y manipulado=false.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.6 - Probar con distintos payloads

- Fuente: p. 1378 / `Paso 6: Probar con distintos payloads` / hash `eb9486cc14f1b973b671d1c7c2c7161d05ea313364fc2c0cc5aabedd4bb205fd`.
- Teoría: `TC-6.5-2`.
- Práctica: `## Paso 6.5.6 - Probar con distintos payloads` / hash `88d57a8f0a0219798baabed27873ea5d2649fd7c5d158bf63db057bf1a36a20e`.
- Acción: Añadir claims sub/email/roles/iat/exp y observar tamaño y legibilidad.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `email`, `exp`, `iat`, `roles`, `sub`.
- Comando: `ejecutar JwtManual.main o decoder local`.
- Observable: claims adicionales aparecen en payload y aumentan el compacto.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.7 - Reflexionar sobre la seguridad

- Fuente: p. 1378 / `Paso 7: Reflexionar sobre la seguridad` / hash `0b37acd2848012caf7f73741871d38559f8b012836b5857d64a976ae11fbb743`.
- Teoría: `TC-6.5-3`, `TC-6.5-5`.
- Práctica: `## Paso 6.5.7 - Reflexionar sobre la seguridad` / hash `a00240af9cfe840fe9865cef1670a6429787d2d964d5023705a20fe19a30e9be`.
- Acción: Documentar amenazas: lectura pública, robo de token, fuga de clave y datos sensibles.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: n/a.
- Comando: `revisión de checklist de seguridad`.
- Observable: queda explícito que firma no cifra y que TLS/gestión de claves siguen siendo obligatorios.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.8 - Comparar con un token real

- Fuente: p. 1379 / `Paso 8: Comparar con un token real` / hash `97da3ba69846e50939177105981c4763e93bd0eb9bf5bdf145c7fb9537463c5a`.
- Teoría: `TC-6.5-4`, `TC-6.5-5`.
- Práctica: `## Paso 6.5.8 - Comparar con un token real` / hash `170f523775506b094d49ae32f7a4579588d9e1d6d65c5a32b5da6caede68196c`.
- Acción: Comparar el ejercicio manual con la futura implementación JJWT.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: n/a.
- Comando: `comparación estructural con JJWT 6.6`.
- Observable: JJWT se identifica como sustituto de la ruta manual de producción.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.9 - Errores comunes del ejercicio

- Fuente: p. 1379 / `Paso 9: Errores comunes del ejercicio` / hash `1e0a5340c5c3986e13bf79c4053b16c5f3e171299c92c0eb79f9f5ebf8ab6bff`.
- Teoría: `TC-6.5-5`.
- Práctica: `## Paso 6.5.9 - Errores comunes del ejercicio` / hash `f11b188283864db350f948faeeba698187ebcefd2fd1eee23141b4554cb53325`.
- Acción: Diagnosticar Base64Url, formato, algoritmo, charset y errores de confianza.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `HmacSHA256`, `IllegalArgumentException`, `NoSuchAlgorithmException`, `header.payload`.
- Comando: `./mvnw -B -Dtest=JwtManualTest test`.
- Observable: errores comunes tienen causa, observable y corrección.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.10 - Experimentar con la expiración

- Fuente: p. 1380 / `Paso 10: Experimentar con la expiración` / hash `fb1c134ab1cd7c49b274a2cb95b708ae1b1556b8b840976423c5639f79b25c9d`.
- Teoría: `TC-6.5-2`, `TC-6.5-5`.
- Práctica: `## Paso 6.5.10 - Experimentar con la expiración` / hash `57c799da67d96c5b82a77e33ef9357280fc6d4f7f6512663328ba71919735d5c`.
- Acción: Usar exp vencido para demostrar que firma válida no equivale a token temporalmente válido.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `exp`, `true`, `verificar`.
- Comando: `generar/decodificar payload con exp=1; no confundir con verificación temporal`.
- Observable: exp vencido es visible; la utilidad manual no lo valida y eso se documenta.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.11 - Verificar el token con una clave incorrecta

- Fuente: p. 1381 / `Paso 11: Verificar el token con una clave incorrecta` / hash `f4d257dfbff8d368944608736097e080a158a5674de51a9f8dbf270f4c98308a`.
- Teoría: `TC-6.5-3`.
- Práctica: `## Paso 6.5.11 - Verificar el token con una clave incorrecta` / hash `08efc7169f007d90533a87b4ee5e7c4ac659bed438e9070c357ecde5152efc3a`.
- Acción: Comprobar que una clave distinta invalida la firma HS256.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManualTest.java`.
- Símbolos: `false`.
- Comando: `./mvnw -B -Dtest=JwtManualTest test`.
- Observable: secret incorrecto=false.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.5.12 - Reto resuelto: decodificar un token sin verificar la firma

- Fuente: p. 1381 / `Paso 12: Reto resuelto — Decodificar un token sin verificar la firma` / hash `47a66ff8702ee4dbb7481d01bb922691cedb4e4f6bbca37ea5926fd40e419b63`.
- Teoría: `TC-6.5-2`, `TC-6.5-5`.
- Práctica: `## Paso 6.5.12 - Reto resuelto: decodificar un token sin verificar la firma` / hash `b3d13f9a80248b2d22bf49c078b43613e2c64ff633cedb469293be7d13068720`.
- Acción: Implementar decodificación local sin firma sólo para inspección.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java`.
- Símbolos: `JwtManual`, `decodificarPayload`, `exp`.
- Comando: `ejecutar JwtManual.decodificarPayload(jwt)`.
- Observable: payload puede inspeccionarse sin firma, pero no se usa para autorización.
- Verificación: `MANUAL+AUTOMATABLE`.
- Post-estado: El estado documentado por 6.5.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.1 - Añadir las dependencias de JJWT

- Fuente: p. 1407 / `Paso 1: Añadir las dependencias de JJWT` / hash `37b94b49c1f480f3f86d6f58d6d839d0c6422b1c6d79d7c01f975eb6f342f871`.
- Teoría: `TC-6.6-1`.
- Práctica: `## Paso 6.6.1 - Añadir las dependencias de JJWT` / hash `409333936f8d428b03b4644b1eea3852e23c2bb9bde17258ad34192a74b98597`.
- Acción: Añadir JJWT 0.13.0 con API en compile e impl/Jackson en runtime, preservando todas las dependencias heredadas.
- Artefacto final: `M6/proyecto/pom.xml`.
- Símbolos: `Claims`, `Decoders`, `Jwts`, `Keys`, `jjwt-api`, `jjwt-impl`, `jjwt-jackson`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: POM conserva M5 y resuelve jjwt-api/impl/jackson 0.13.0 con scopes correctos.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.2 - Configurar las propiedades de JWT

- Fuente: p. 1408 / `Paso 2: Configurar las propiedades de JWT` / hash `f5b588c0c8667e751be1cb08cb9a2138a2d6873621782a17e53677dfa984d752`.
- Teoría: `TC-6.6-2`.
- Práctica: `## Paso 6.6.2 - Configurar las propiedades de JWT` / hash `04b99eb8dd0d134824422352d287b876693314622331f07e0b68e40779ade624`.
- Acción: Externalizar secret y duraciones por perfil, sin secreto por defecto en producción.
- Artefacto final: `M6/proyecto/src/main/resources/application-dev.properties`, `M6/proyecto/src/main/resources/application-prod.properties`, `M6/proyecto/src/main/resources/application-test.properties`.
- Símbolos: `${JWT_SECRET}`, `JWT_SECRET`, `JwtConfig`, `jwt.expiration`, `jwt.refresh-expiration`, `jwt.secret`.
- Comando: `grep -R "^jwt\." src/main/resources/application-*.properties`.
- Observable: dev/test usan secretos Base64 válidos; prod exige JWT_SECRET y no contiene fallback sensible.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.3 - Crear la configuración de la clave

- Fuente: p. 1409 / `Paso 3: Crear la configuración de la clave` / hash `59766095dfed6e11802653d535ad6ad97eb788b1407d69ea188aa7b1bc7047f0`.
- Teoría: `TC-6.6-2`.
- Práctica: `## Paso 6.6.3 - Crear la configuración de la clave` / hash `0b7ddbb762144e68e871c50c7709b385a1d8355e7f7a62847fe452198204e76b`.
- Acción: Construir SecretKey desde Base64 y fallar cerrado si la clave es inválida o débil.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtConfig.java`.
- Símbolos: `JwtConfig`, `jwtSecretKey`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: SecretKey HMAC se crea; configuración inválida impide arrancar.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.4 - Crear el `JwtService`

- Fuente: p. 1410 / `Paso 4: Crear el JwtService` / hash `e4b03aaae61cc320cf97216da80afcabd58b0f386616249c802062e27663c315`.
- Teoría: `TC-6.6-3`.
- Práctica: `## Paso 6.6.4 - Crear el `JwtService`` / hash `60d1a91b9d69b8891b31330d771a8df91d85810140d4c511a794e12381de8638`.
- Acción: Sustituir la ruta manual por JwtService con access/refresh, firma, claims, jti y validación de tipo/expiración.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java`.
- Símbolos: `JwtManual`, `JwtService`, `Long`, `Number`, `esAccessTokenValido`, `esRefreshTokenValido`, `esValido`, `extraerAuthorities`, `extraerClaims`, `extraerId`, `extraerUsername`, `generarRefreshToken`, `generarRefreshToken(Usuario)`, `generarToken`, `generarToken(Usuario)`, `getExpirationMs`, `jti`, `longValue()`, `parsearClaims`, `tipo=access`, `tipo=refresh`.
- Comando: `./mvnw -B -Dtest=JwtServiceIntegrationTest test`.
- Observable: access contiene identidad/roles y refresh omite roles; ambos están firmados y expiran; T6.5-A queda cerrado funcionalmente.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.5 - Crear los DTOs de login

- Fuente: p. 1416 / `Paso 5: Crear los DTOs de login` / hash `aeaec0bc18d97d6660a491af1bf409231f1692a4aac03d26281465f805456616`.
- Teoría: `TC-6.6-4`.
- Práctica: `## Paso 6.6.5 - Crear los DTOs de login` / hash `0234bee0cd7acdd208a2e967802cf04b75fa5f6d9720199cfc96f3b1afa3cf26`.
- Acción: Crear LoginRequestDTO, LoginResponseDTO y RefreshRequestDTO con validación explícita.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/LoginRequestDTO.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/LoginResponseDTO.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RefreshRequestDTO.java`.
- Símbolos: `@JsonProperty`, `LoginRequestDTO`, `LoginResponseDTO`, `RefreshRequestDTO`, `expires_in`, `expires_in=3600000`, `getAccessToken`, `getExpiresIn`, `getPassword`, `getRefreshToken`, `getTokenType`, `getUsername`, `setPassword`, `setRefreshToken`, `setUsername`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: DTOs tienen campos y validaciones esperados; password no aparece en respuestas.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.6 - Ampliar `AuthService` con login y refresh

- Fuente: p. 1419 / `Paso 6: Ampliar el AuthService con login y refresh` / hash `08776142a53c2d0c11822360efa38d552456020391cc30ed2a69dc8c685ad7e6`.
- Teoría: `TC-6.6-4`, `TC-6.6-5`.
- Práctica: `## Paso 6.6.6 - Ampliar `AuthService` con login y refresh` / hash `a92aa35e5fe88bceeb9b45a44f07ee8f2daedca807888cdd1e8cc110bb595aed`.
- Acción: Integrar AuthenticationManager, generar pares de tokens y rotar refresh tokens de un solo uso.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenRevocationService.java`.
- Símbolos: `AuthService`, `AuthService.login`, `AuthenticationException`, `AuthenticationManager`, `TokenRevocationService.consumeRefresh`, `jti`, `jwtService.esValido()`, `login`, `refresh`, `tipo=refresh`.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: credenciales válidas generan par; refresh se rota y la reutilización se rechaza.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.7 - Ampliar `AuthController`

- Fuente: p. 1422 / `Paso 7: Ampliar el AuthController` / hash `0ecb1bf77fd9c2771422158b14ea64520acdad7b2bc57b2e4ecd8db741de6dfe`.
- Teoría: `TC-6.6-4`, `TC-6.6-5`.
- Práctica: `## Paso 6.6.7 - Ampliar `AuthController`` / hash `b6c21237406aeb74ffc1c7455d720a7f1f35c65f6c86f429db0c62d5646911dc`.
- Acción: Exponer registro, login, refresh y logout desde AuthController.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`.
- Símbolos: `AuthController`, `login`, `refresh`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: rutas REST quedan disponibles con contratos separados.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.8 - Configurar la seguridad para permitir login

- Fuente: p. 1423 / `Paso 8: Configurar la seguridad para permitir login` / hash `953a2fe2eba2034a31339026a4335656fa001023816f3f9028b4dff73f340d03`.
- Teoría: `TC-6.6-4`.
- Práctica: `## Paso 6.6.8 - Configurar la seguridad para permitir login` / hash `f3accee2419ca26e1a2858f164bde728d8d184f82c89277dfadc890f7e65dac4`.
- Acción: Abrir sólo POST registro/login/refresh; mantener logout y el resto protegidos.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: n/a.
- Comando: `./mvnw -B test`.
- Observable: login/refresh/registro son públicos por método; logout sigue autenticado.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.9 - Arrancar y probar el login

- Fuente: p. 1423 / `Paso 9: Arrancar y probar el login` / hash `c08d689d12ba5702538463c8f4c3030d59a20687e6f83633470add87e4d867c9`.
- Teoría: `TC-6.6-4`.
- Práctica: `## Paso 6.6.9 - Arrancar y probar el login` / hash `06bb3e06e50c22e75bdd382847104970d7cc761411860ec7b68dd378eb28ea14`.
- Acción: Probar login real y observar access token, refresh token y expires_in en segundos.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: `CREDENCIALES_INVALIDAS`.
- Comando: `curl -i -X POST http://localhost:8080/api/v1/auth/login -H "Content-Type: application/json" -d @login.json`.
- Observable: 200 con access_token/refresh_token/expires_in; credenciales erróneas=401 uniforme.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.10 - Decodificar el token

- Fuente: p. 1424 / `Paso 10: Decodificar el token` / hash `a00ab62de87776b6ca48a1d0b57dcf9bd17d3ddfd3eb2a2332cb027210565b67`.
- Teoría: `TC-6.6-3`.
- Práctica: `## Paso 6.6.10 - Decodificar el token` / hash `74327230f882247c10b2a706daec1f1a1c44f22f5d15a723d2373fd607c7a48f`.
- Acción: Decodificar claims para distinguir sub/iat/exp/jti de claims privados sin confundir lectura con confianza.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java`.
- Símbolos: `exp`, `iat`, `jti`, `sub`, `tipo`, `tipo=access`.
- Comando: `decoder local/JwtService.extraerClaims sobre token de desarrollo`.
- Observable: claims son legibles pero sólo se usan tras verificación JJWT.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.11 - Probar el refresh

- Fuente: p. 1425 / `Paso 11: Probar el refresh` / hash `c717042244ee4a428578c2e7fab51fb7bd77eb6992e1adf18ed1293a241ee2a3`.
- Teoría: `TC-6.6-5`.
- Práctica: `## Paso 6.6.11 - Probar el refresh` / hash `a283b5292dffbeda2d7ebe3560c672793a95da2537f37e7bc9199acc4a2751d4`.
- Acción: Consumir un refresh una sola vez, emitir un par nuevo y rechazar la reutilización.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: `TOKEN_INVALIDO`.
- Comando: `curl -i -X POST http://localhost:8080/api/v1/auth/refresh -H "Content-Type: application/json" -d @refresh.json`.
- Observable: primer refresh=200; segundo uso del mismo refresh=401 TOKEN_INVALIDO.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.12 - Errores comunes del ejercicio

- Fuente: p. 1426 / `Paso 12: Errores comunes del ejercicio` / hash `8d15fec882a24a6fb3b7b7ab4915148a966ae95b24012a5079f31fc37c6e85df`.
- Teoría: `TC-6.6-5`.
- Práctica: `## Paso 6.6.12 - Errores comunes del ejercicio` / hash `c001ce730a5b6b3cec12aed611c4d07affd7a6c8ddc48ad03ba81759b50c164e`.
- Acción: Diagnosticar claves débiles, credenciales, expiración, scopes públicos y errores de unidad temporal.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CredencialesInvalidasException.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenInvalidoException.java`.
- Símbolos: `${JWT_SECRET}`, `CredencialesInvalidasException`, `esRefreshTokenValido`, `expires_in`, `jti`, `parseSignedClaims`.
- Comando: `./mvnw -B test`.
- Observable: errores comunes quedan cubiertos sin abrir rutas ni degradar claves.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.6.13 - Reto resuelto: endpoint para logout

- Fuente: p. 1427 / `Paso 13: Reto resuelto — Endpoint para logout` / hash `6585eddc75f836befc6a83b207c41cba3276d729694e70d9cd61ad9602d9807c`.
- Teoría: `TC-6.6-5`.
- Práctica: `## Paso 6.6.13 - Reto resuelto: endpoint para logout` / hash `f4a01ae7e5bfa68018fcf21c3a78e1f0fcb6112b61e5f7f16db6202a7d957664`.
- Acción: Implementar logout mediante revocación temporal por jti y comprobar que el access token deja de autenticar.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenRevocationService.java`.
- Símbolos: `204 No Content`, `TokenBlacklistService`, `TokenRevocationService`, `add`, `contains`, `jti → exp`, `logout`.
- Comando: `curl -i -X POST http://localhost:8080/api/v1/auth/logout -H "Authorization: Bearer $TOKEN"`.
- Observable: logout=204 y reutilizar el access token revocado en endpoint protegido=401.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.6.13 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.1 - Verificar `JwtService` y `UsuarioPrincipal`

- Fuente: p. 1450 / `Paso 1: Verificar el JwtService y el UsuarioPrincipal` / hash `856f209bbe021dbd4732f2187ebcd7e5ba29cd4464f85aa6940aee1889134cd2`.
- Teoría: `TC-6.7-1`, `TC-6.7-3`.
- Práctica: `## Paso 6.7.1 - Verificar `JwtService` y `UsuarioPrincipal`` / hash `9c5b3686a77cf80bddd2232e1d59b69b9408cfd40ed53938bbfc60b104b2f9ef`.
- Acción: Verificar el contrato de JwtService y el UsuarioPrincipal enriquecido antes de insertarlos en la cadena.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java`.
- Símbolos: `(Long id, String username, String email, Collection<GrantedAuthority>)`, `JwtService`, `UsuarioPrincipal`, `extraerAuthorities`, `extraerClaims`.
- Comando: `./mvnw -B -DskipTests compile`.
- Observable: servicio/principal exponen los datos necesarios para autenticación stateless.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.2 - Crear `JwtAuthenticationFilter`

- Fuente: p. 1450 / `Paso 2: Crear el JwtAuthenticationFilter` / hash `de9587a24aa3940386bbd0930558fda948e012dc843b098ec66eac1c83823a76`.
- Teoría: `TC-6.7-1`, `TC-6.7-2`, `TC-6.7-3`.
- Práctica: `## Paso 6.7.2 - Crear `JwtAuthenticationFilter`` / hash `eca2807784563f2fcdbadb5c956fa8595cba9d239d07efa82c8d7f26a0f31dbe`.
- Acción: Crear OncePerRequestFilter tolerante a ausencia de token, validar sólo access tokens y construir Authentication desde claims.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java`.
- Símbolos: `Authorization`, `Bearer `, `JwtAuthenticationFilter`, `SecurityContextHolder`, `UsernamePasswordAuthenticationToken`, `UsuarioPrincipal`, `doFilterInternal`, `filterChain.doFilter`, `jti`.
- Comando: `./mvnw -B -Dtest=JwtFilterIntegrationTest test`.
- Observable: una petición con access válido crea Authentication; refresh/no válido no lo hace.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.3 - Registrar el filtro en la cadena

- Fuente: p. 1455 / `Paso 3: Registrar el filtro en la cadena` / hash `af2596a42ef9188a388f0dfc36886f9975be37ca9094f5c9a399e98cf08b29fd`.
- Teoría: `TC-6.7-4`.
- Práctica: `## Paso 6.7.3 - Registrar el filtro en la cadena` / hash `1bb0cf9bb2a4ce08ab8b6dc80bf99fdde4e3191b47723053f92e89f446ca9443`.
- Acción: Registrar JwtAuthenticationFilter antes de UsernamePasswordAuthenticationFilter.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `filterChain`.
- Comando: `grep -n "addFilterBefore" src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Observable: filtro se ejecuta antes del filtro username/password.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.4 - Desactivar HTTP Basic

- Fuente: p. 1457 / `Paso 4: Desactivar HTTP Basic` / hash `e268b78debae30448ef4215f7583dca01fe419f0a07fa738b49d75ee922a0a2d`.
- Teoría: `TC-6.7-4`.
- Práctica: `## Paso 6.7.4 - Desactivar HTTP Basic` / hash `29b11a52c7bb07adcd0c3567f1b08460952e326f724a530b7fa5d158ad951ea0`.
- Acción: Eliminar HTTP Basic y cerrar definitivamente T6.2-B.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `.httpBasic(`, `SecurityConfig`.
- Comando: `grep -R "httpBasic" src/main/java || true`.
- Observable: no existe httpBasic en SecurityConfig; HTTP Basic queda cerrado.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.5 - Arrancar y probar

- Fuente: p. 1457 / `Paso 5: Arrancar y probar` / hash `ad133fc4af1825c9811554cc48c7f78768f4ac15bb52d24a51050cc991c247de`.
- Teoría: `TC-6.7-1`.
- Práctica: `## Paso 6.7.5 - Arrancar y probar` / hash `ad31fe22acc3078285395c239afe17d9ef7c3791aefb0cffbb9de04671e25ca4`.
- Acción: Probar endpoint protegido con Bearer válido y sin token.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`.
- Símbolos: n/a.
- Comando: `curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/perfil`.
- Observable: Bearer válido=200 y anónimo=401 en recurso privado.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.6 - Verificar `UsuarioPrincipal`

- Fuente: p. 1459 / `Paso 6: Verificar el UsuarioPrincipal` / hash `0c13bcd7127f2e598a49f67cb742d5d41f57e3dc58953b78d34e46ff9b7b3a07`.
- Teoría: `TC-6.7-3`.
- Práctica: `## Paso 6.7.6 - Verificar `UsuarioPrincipal`` / hash `39a81d04225f2e891c28b3725c1237765a0b99387118477216de681bbd92d365`.
- Acción: Activar UsuarioPrincipal como principal real con id, username, email y authorities; cerrar T6.4-A.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java`.
- Símbolos: `PerfilController`, `UsuarioPrincipal`, `perfil`.
- Comando: `curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/perfil/usuario`.
- Observable: @AuthenticationPrincipal recibe UsuarioPrincipal con id/email/roles.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.7 - Probar con rol ADMIN

- Fuente: p. 1460 / `Paso 7: Probar con rol ADMIN` / hash `16e30532b395119a62264c43bab848a7f1f7425bcabff2c566633ef14a38cc2f`.
- Teoría: `TC-6.7-3`, `TC-6.7-4`.
- Práctica: `## Paso 6.7.7 - Probar con rol ADMIN` / hash `b52608c710a56bbf843117f1a88555c36335db0dfcdeb445c398797fd7633af2`.
- Acción: Comprobar que ROLE_ADMIN del token gobierna las reglas administrativas sin consultar DB en cada petición.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerCompleteSecurityTest.java`.
- Símbolos: n/a.
- Comando: `curl -i -H "Authorization: Bearer $ADMIN_TOKEN" http://localhost:8080/api/v1/admin/usuarios`.
- Observable: ADMIN accede; USER obtiene 403.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.8 - Probar con un token inválido

- Fuente: p. 1461 / `Paso 8: Probar con un token inválido` / hash `99e750ba754f7ebd321a988530256a0c05a326880d140de0224d4afb9d6b4391`.
- Teoría: `TC-6.7-2`, `TC-6.7-5`.
- Práctica: `## Paso 6.7.8 - Probar con un token inválido` / hash `24071375980e46261386f8c66cfd91719123370e229332ffe9036a7b21f141f4`.
- Acción: Rechazar tokens mal firmados/expirados/no-access sin convertirlos en Authentication.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`.
- Símbolos: n/a.
- Comando: `curl -i -H "Authorization: Bearer tokenInvalido" http://localhost:8080/api/v1/perfil`.
- Observable: token inválido no autentica y termina en 401 uniforme.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.9 - Probar con cabecera mal formada

- Fuente: p. 1462 / `Paso 9: Probar con la cabecera mal formada` / hash `a76a9017c6cba76a28495043f091ce6d0d44cb1eccce5dc88b7a8efc94bee7b4`.
- Teoría: `TC-6.7-2`, `TC-6.7-5`.
- Práctica: `## Paso 6.7.9 - Probar con cabecera mal formada` / hash `16e9afcf6d437f32e77f8e34509d25f9b6c6abb5f5a1b62ac19e7073f1780b70`.
- Acción: Dejar pasar cabeceras ausentes o mal formadas para que la autorización produzca el 401 uniforme.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`.
- Símbolos: `Bearer `.
- Comando: `curl -i -H "Authorization: Token $TOKEN" http://localhost:8080/api/v1/perfil`.
- Observable: cabecera no Bearer no genera Authentication y la cadena decide el resultado.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.10 - Depurar el filtro

- Fuente: p. 1462 / `Paso 10: Depurar el filtro` / hash `6011d725d91f00c6512d3d281aa6825b6fed5f2424831062834dd9e0e1668b93`.
- Teoría: `TC-6.7-5`.
- Práctica: `## Paso 6.7.10 - Depurar el filtro` / hash `fa6b18e55d83b5d84aab1b96385409828ae3202dc64595a7ba04c7f3583cafab`.
- Acción: Usar logs DEBUG sin imprimir el token completo ni secretos.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java`.
- Símbolos: n/a.
- Comando: `activar logging de seguridad sólo en diagnóstico`.
- Observable: logs muestran username/ruta/causa sin credencial completa.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.11 - Errores comunes del ejercicio

- Fuente: p. 1463 / `Paso 11: Errores comunes del ejercicio` / hash `12b4a5748a2417e5e029ef8e42ea57fa4b5a9b32a12765eb7f1a08fc7eaf27aa`.
- Teoría: `TC-6.7-5`.
- Práctica: `## Paso 6.7.11 - Errores comunes del ejercicio` / hash `5477c8411680fb71a4a2e802d84d6ed57a9ad777de9dbe9cabcc147c30620e06`.
- Acción: Diagnosticar orden de filtro, contexto, roles y revocación sin introducir sesiones.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `addFilterBefore`, `doFilter`, `esAccessTokenValido`, `jti`, `tipo=access`.
- Comando: `./mvnw -B test`.
- Observable: no aparece sesión ni estado de autenticación persistido.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.7.12 - Reto resuelto: SpEL con ID del usuario

- Fuente: p. 1464 / `Paso 12: Reto resuelto — Expresión SpEL con el ID del usuario` / hash `ec3611d262be36a9f2a456514eed2912ae84422f014cb18c55d206bd11f68cea`.
- Teoría: `TC-6.7-3`, `TC-6.7-4`.
- Práctica: `## Paso 6.7.12 - Reto resuelto: SpEL con ID del usuario` / hash `3fea95c27a4668101388c6f9a2100180c0b6dd86d6b8fadc01dc10e3c4641597`.
- Acción: Hacer ejecutable la expresión SpEL por id usando UsuarioPrincipal y proteger acceso propio o ADMIN.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `STATELESS`, `UsuarioPrincipal`, `perfilPorId`.
- Comando: `curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/perfil/1`.
- Observable: propietario o ADMIN puede consultar por id; otro USER=403.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.7.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.1 - Crear `JwtAuthenticationEntryPoint`

- Fuente: p. 1487 / `Paso 1: Crear el JwtAuthenticationEntryPoint` / hash `e8314170eb9c76e507143f4175224c1aa882691707c3b9e637b19e56473e087d`.
- Teoría: `TC-6.8-1`.
- Práctica: `## Paso 6.8.1 - Crear `JwtAuthenticationEntryPoint`` / hash `aa0ba0cacef02903148fca281c4491ecb57cecb182365eccdac9acf44affe1a4`.
- Acción: Crear AuthenticationEntryPoint que escribe 401 JSON con ErrorResponse/traceId.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationEntryPoint.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java`.
- Símbolos: `@RestControllerAdvice`, `AuthenticationEntryPoint`, `ErrorResponse`, `JwtAuthenticationEntryPoint`, `SecurityErrorWriter`, `codigo=NO_AUTENTICADO`, `commence`, `mensaje`, `path`, `status`, `timestamp`, `traceId`.
- Comando: `curl -i http://localhost:8080/api/v1/perfil`.
- Observable: 401 JSON contiene status/codigo/mensaje/path/timestamp/traceId.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.2 - Crear `JwtAccessDeniedHandler`

- Fuente: p. 1489 / `Paso 2: Crear el JwtAccessDeniedHandler` / hash `a31d9b322fa824e1a8680d6194539a8494b362116b1037e0e3e750e5e6537a40`.
- Teoría: `TC-6.8-1`.
- Práctica: `## Paso 6.8.2 - Crear `JwtAccessDeniedHandler`` / hash `b5d9e9bdc55d5bf9742add393f198daec124ad17fa81437bbf41bd5c717a1e5f`.
- Acción: Crear AccessDeniedHandler que escribe 403 JSON con el mismo contrato.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAccessDeniedHandler.java`, `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java`.
- Símbolos: `AccessDeniedHandler`, `JwtAccessDeniedHandler`, `codigo=ACCESO_DENEGADO`, `handle`.
- Comando: `curl -i -H "Authorization: Bearer $USER_TOKEN" http://localhost:8080/api/v1/admin/usuarios`.
- Observable: 403 JSON mantiene el mismo esquema y código ACCESO_DENEGADO.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.3 - Actualizar `SecurityConfig`

- Fuente: p. 1491 / `Paso 3: Actualizar el SecurityConfig` / hash `fba4cc9d16f0e5c248c6aa37565db17623e641955f1cc406f039563b96989e44`.
- Teoría: `TC-6.8-1`, `TC-6.8-2`, `TC-6.8-3`.
- Práctica: `## Paso 6.8.3 - Actualizar `SecurityConfig`` / hash `78639d8fcad6cff257f2c3c42bda9df6ddb493cc8787b7958fee37f0e220a30e`.
- Acción: Integrar STATELESS, filtro JWT, matchers mínimos, handlers y orden de reglas en SecurityConfig.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java`.
- Símbolos: `SecurityConfig`, `anyRequest().authenticated()`, `filterChain`, `passwordEncoder`.
- Comando: `./mvnw -B test`.
- Observable: STATELESS y addFilterBefore activos; públicos mínimos antes de anyRequest.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.4 - Añadir endpoint de gestor

- Fuente: p. 1493 / `Paso 4: Añadir un endpoint de gestor` / hash `9c7497333209017ab1c45c91e6b66d9366e9a58469cac424ccac911ceb55d363`.
- Teoría: `TC-6.8-2`, `TC-6.8-3`.
- Práctica: `## Paso 6.8.4 - Añadir endpoint de gestor` / hash `ec7331b62a01173c2c22543e84116cca1aad90fd6e438606ae9962f69d6b852f`.
- Acción: Conservar endpoint GESTOR protegido y hacerlo funcionar con authorities del token.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/GestorController.java`.
- Símbolos: `GestorController`, `SecurityConfig`, `documentos`.
- Comando: `curl -i -H "Authorization: Bearer $GESTOR_TOKEN" http://localhost:8080/api/v1/gestor/documentos`.
- Observable: GESTOR y ADMIN=200; USER=403.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.5 - Añadir rol GESTOR al inicializador

- Fuente: p. 1494 / `Paso 5: Añadir el rol GESTOR al inicializador` / hash `c947211b6f84d5b963ceff30c21c87a055a74ee039e8ea8ed2582cdd80f112b6`.
- Teoría: `TC-6.8-2`, `TC-6.8-3`.
- Práctica: `## Paso 6.8.5 - Añadir rol GESTOR al inicializador` / hash `f7b7ad467b53d0f6977d8236ccb224a3963e430f026142f5a96e4fb25be5e753`.
- Acción: Conservar inicialización GESTOR al migrar completamente a JWT.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java`.
- Símbolos: `UsuariosInicialesConfig`.
- Comando: `curl -i -H "Authorization: Bearer $GESTOR_TOKEN" http://localhost:8080/api/v1/gestor/documentos`.
- Observable: rol GESTOR sobrevive a la evolución acumulativa.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.6 - Probar el flujo completo

- Fuente: p. 1495 / `Paso 6: Probar el flujo completo` / hash `3c3816391733ea56c3b9e2cdca34ae22cba7d4700ed399fa106d76a9001590ac`.
- Teoría: `TC-6.8-4`.
- Práctica: `## Paso 6.8.6 - Probar el flujo completo` / hash `5584cafcafe6e230dd47e6a3efea8e644758fa0885d7e2c078e892984eea20d2`.
- Acción: Probar registro→login→petición→refresh→logout de extremo a extremo.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: n/a.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: flujo completo funciona sin sesión HTTP.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.7 - Probar sin token

- Fuente: p. 1497 / `Paso 7: Probar sin token` / hash `a1dce5d7f35971f0c8393e7df0e358da760a34e60aa1ef18072d056a17b1f422`.
- Teoría: `TC-6.8-1`, `TC-6.8-5`.
- Práctica: `## Paso 6.8.7 - Probar sin token` / hash `e2a5ac983257b7bf3392e745d6bd1bb55b59542b8888077baff43e1ac73a9fb5`.
- Acción: Verificar que ausencia de token produce 401 JSON y no HTML/redirección.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java`.
- Símbolos: `traceId`.
- Comando: `curl -i http://localhost:8080/api/v1/perfil`.
- Observable: anónimo privado=401 NO_AUTENTICADO.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.8 - Probar refresh token

- Fuente: p. 1498 / `Paso 8: Probar el refresh token` / hash `8e7358e89bc94c958817cea8ac585e9d40b1a44138dcff88795a25424305ff5f`.
- Teoría: `TC-6.8-4`.
- Práctica: `## Paso 6.8.8 - Probar refresh token` / hash `30493c94fa78ebfddbd8dbe9bd4b87a52607959129389318e36d62e1fbb9ca33`.
- Acción: Verificar rotación real del refresh y rechazo de replay.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: n/a.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: refresh válido rota; replay=401.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.9 - Probar logout

- Fuente: p. 1498 / `Paso 9: Probar el logout` / hash `92534d04c6410df98f6e37ae2c98c815256ff3b2f21279901214d1aa162c8335`.
- Teoría: `TC-6.8-4`.
- Práctica: `## Paso 6.8.9 - Probar logout` / hash `e542b8edc7909bf617324b4f7353a55b296de43bee0c0b77d16bac22fb82ea37`.
- Acción: Verificar revocación de access tras logout.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: `jti`.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: access revocado ya no autentica.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.10 - Escribir tests de integración

- Fuente: p. 1499 / `Paso 10: Escribir tests de integración` / hash `a2b5175441b92a93091f55e7b6aebdcc2dc826448bde752754f0e408ceb45e72`.
- Teoría: `TC-6.8-5`.
- Práctica: `## Paso 6.8.10 - Escribir tests de integración` / hash `00ece4ec2131c4ea4c2d072f852fef1ebe2d0bd39e4fb3a9d768dcb4c4127f6c`.
- Acción: Crear tests de integración del flujo, filtros, roles y errores.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/GestorSecurityTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`.
- Símbolos: `@MockBean`, `@MockitoBean`, `@SpringBootTest`, `AuthControllerTest`.
- Comando: `./mvnw -B test`.
- Observable: tests cubren autenticación/autorización/error/flujo.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.11 - Errores comunes del ejercicio

- Fuente: p. 1501 / `Paso 11: Errores comunes del ejercicio` / hash `8b2c277ff49a5be6f75ce191ee82784121a2a8f1fee90d6ae0d0bcc4b5a8ec59`.
- Teoría: `TC-6.8-5`.
- Práctica: `## Paso 6.8.11 - Errores comunes del ejercicio` / hash `4415ccd1ca6b9a0a2b5765e90eea7871a3070506aea4eab972546c1579298889`.
- Acción: Diagnosticar 401/403, orden de matchers, expiración, CORS y stale claims sin degradar seguridad.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `.cors()`, `JwtAccessDeniedHandler`, `anyRequest`.
- Comando: `./mvnw -B test`.
- Observable: errores tienen causa y corrección trazadas.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.8.12 - Reto resuelto: consultar el propio usuario

- Fuente: p. 1502 / `Paso 12: Reto resuelto — Endpoint para consultar el propio usuario` / hash `0ed7606e85710153884acacd83cdbe1e75f34a6e57114cba134550cfee525d2a`.
- Teoría: `TC-6.8-3`, `TC-6.8-4`.
- Práctica: `## Paso 6.8.12 - Reto resuelto: consultar el propio usuario` / hash `d0e0f13ec6196bf1259ef69da84b466ccfd22f9634086f9ee2dcafb5f66ae2da`.
- Acción: Exponer /perfil/usuario desde claims validados, sin consulta DB por petición.
- Artefacto final: `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java`.
- Símbolos: `UsuarioPrincipal`, `usuario`.
- Comando: `curl -i -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/v1/perfil/usuario`.
- Observable: respuesta usa id/username/email del UsuarioPrincipal creado desde claims verificados.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.8.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.1 - Añadir `spring-security-test`

- Fuente: p. 1528 / `Paso 1: Añadir la dependencia spring-security-test` / hash `0cabcd4215a658674f5ddb7ab5c06e32df55271ea77a9875c9dd67fb3c45e389`.
- Teoría: `TC-6.9-1`.
- Práctica: `## Paso 6.9.1 - Añadir `spring-security-test`` / hash `e0c1c197c9428fe70645f64e0212e06dbc3ba961d33d71cc8c7b08495ecf5f9a`.
- Acción: Añadir spring-security-test en test scope sin introducir dependencias OAuth2 innecesarias.
- Artefacto final: `M6/proyecto/pom.xml`.
- Símbolos: `@WithMockUser`, `@WithUserDetails`, `scope=test`, `spring-security-test`.
- Comando: `./mvnw -B -DskipTests test-compile`.
- Observable: security-test queda sólo en test y no cambia runtime.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.1 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.2 - Crear test del `UsuarioController`

- Fuente: p. 1529 / `Paso 2: Crear el test del UsuarioController` / hash `dfa9a975e77bda3825c9741adee5b4bbff0d67012a2550f6ace8ff4242ccda6a`.
- Teoría: `TC-6.9-2`.
- Práctica: `## Paso 6.9.2 - Crear test del `UsuarioController`` / hash `b36e1a9bea3bc2e2125ebbd6e8da2bba69eaa42929b879b1805d5a0057bf22b9`.
- Acción: Crear tests 401/403/200 del UsuarioController con identidades simuladas y contrato JSON.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java`.
- Símbolos: `@MockBean`, `@MockitoBean`, `ACCESO_DENEGADO`, `NO_AUTENTICADO`, `UsuarioController`, `UsuarioControllerSecurityTest`.
- Comando: `./mvnw -B -Dtest=UsuarioControllerSecurityTest test`.
- Observable: escenarios anónimo/USER/ADMIN producen 401/403/200.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.2 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.3 - Ejecutar los tests

- Fuente: p. 1532 / `Paso 3: Ejecutar los tests` / hash `35dde04a3b16b771ed91ebeb342e9348e5694a0dea85ed5ead8005d1df9c0d06`.
- Teoría: `TC-6.9-1`.
- Práctica: `## Paso 6.9.3 - Ejecutar los tests` / hash `d8ff69fb44974f8f1173668a811862ace75199d6368beb1010ad17fe81058756`.
- Acción: Ejecutar la suite acumulativa completa y Checkstyle.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java`.
- Símbolos: `@AutoConfigureMockMvc(addFilters = false)`, `@MockitoBean`, `@WebMvcTest`, `JwtAuthenticationFilter`, `JwtService`, `TokenRevocationService`, `skipped`.
- Comando: `./mvnw -B clean verify`.
- Observable: suite heredada M5 + tests M6 = 58 tests esperados, Checkstyle 0.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.3 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.4 - Testear endpoint de gestor

- Fuente: p. 1532 / `Paso 4: Testear el endpoint de gestor` / hash `fd26315937b6daa2ecc749a4cbd1869f6f025498761b74806891518ced7214dd`.
- Teoría: `TC-6.9-4`.
- Práctica: `## Paso 6.9.4 - Testear endpoint de gestor` / hash `e91e9f6e884405209f08503853739bfa8cebab0de74271c3a5732c1cc61fe267`.
- Acción: Testear gestor para USER/GESTOR/ADMIN.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/GestorSecurityTest.java`.
- Símbolos: `GestorSecurityTest`.
- Comando: `./mvnw -B -Dtest=GestorSecurityTest test`.
- Observable: USER=403, GESTOR=200, ADMIN=200.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.4 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.5 - Testear endpoint público

- Fuente: p. 1533 / `Paso 5: Testear el endpoint público` / hash `e77f1082e5c11c9d53504b0f0237f074980ad7fc6c6d0b4db6f256c23499acfb`.
- Teoría: `TC-6.9-4`.
- Práctica: `## Paso 6.9.5 - Testear endpoint público` / hash `5d064722dc56a8925286f0b562c4cd1b95dc56261e0df3027530e0a68e3fc6e9`.
- Acción: Testear endpoint público sin identidad.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/controller/PublicControllerSecurityTest.java`.
- Símbolos: `PublicControllerSecurityTest`.
- Comando: `./mvnw -B -Dtest=PublicControllerSecurityTest test`.
- Observable: público=200 sin autenticación.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.5 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.6 - Testear login

- Fuente: p. 1535 / `Paso 6: Testear el login` / hash `031a120317f33f9b91149c6ca752dc9822511560838540f0ead8a24e15c296b3`.
- Teoría: `TC-6.9-4`.
- Práctica: `## Paso 6.9.6 - Testear login` / hash `75aebd5d8e021f1032b2b9fa6a19864c80608a837ec6f9007a54e76f8682cec9`.
- Acción: Testear login real con AuthenticationManager y DB de test.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: `AuthControllerSecurityTest`, `AuthFlowIntegrationTest`, `CREDENCIALES_INVALIDAS`, `NegocioException`, `token_type=Bearer`.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: login correcto=200; credenciales incorrectas=401.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.6 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.7 - Testear refresh

- Fuente: p. 1537 / `Paso 7: Testear el refresh` / hash `fa9b15d4ce6908de8e07f4be7f325236ceabb8c43e0480fd4f4d226622eeb4d8`.
- Teoría: `TC-6.9-4`.
- Práctica: `## Paso 6.9.7 - Testear refresh` / hash `1e4ae0224ef551e4b56b70f492af3eb9f9909a1425eb4e5ac42c63ffbd909d04`.
- Acción: Testear refresh, rotación y replay.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: `TOKEN_INVALIDO`.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: refresh first=200 y replay=401.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.7 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.8 - Testear logout

- Fuente: p. 1539 / `Paso 8: Testear el logout` / hash `12506e0e66585aa11fc25cea6a4996d4336fe1c875a3810541df39d62a22eb08`.
- Teoría: `TC-6.9-4`.
- Práctica: `## Paso 6.9.8 - Testear logout` / hash `6532a6655f37368010fb27b6979a8ba7bd69c1abc0ffbac4b02fa4a499ea5a07`.
- Acción: Testear logout y token revocado.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java`.
- Símbolos: n/a.
- Comando: `./mvnw -B -Dtest=AuthFlowIntegrationTest test`.
- Observable: logout=204 y access anterior=401.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.8 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.9 - Testear con post-processors

- Fuente: p. 1539 / `Paso 9: Testear con post-processors` / hash `a21b9743fd31c43e6b7f13fb62ed63179f670f6c589ca471b765ddaaa5a86bbc`.
- Teoría: `TC-6.9-3`.
- Práctica: `## Paso 6.9.9 - Testear con post-processors` / hash `bf4a4faf17bb3745dc92911878dd221fd04867495dc61b7db6653e87b3ed4fc6`.
- Acción: Usar post-processors user() para autorización sin confundirlos con el filtro JWT.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java`.
- Símbolos: `@WithMockUser`, `UsuarioControllerSecurityTest`.
- Comando: `./mvnw -B -Dtest=UsuarioControllerSecurityTest test`.
- Observable: post-processors prueban autorización, no sustituyen test real del filtro.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.9 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.10 - Testear con `@WithUserDetails`

- Fuente: p. 1540 / `Paso 10: Testear con @WithUserDetails` / hash `ffb6ca203efbc79e2380cceb17a5e3e1399e44f88eaa592b207acac7228a6dde`.
- Teoría: `TC-6.9-2`.
- Práctica: `## Paso 6.9.10 - Testear con `@WithUserDetails`` / hash `1863ea36b4489c08c4009af3a98a9d339e7de5ac4e0c43aebf51d4a01a3bec0b`.
- Acción: Usar @WithUserDetails sobre el usuario real sembrado en base de datos.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerIntegrationTest.java`.
- Símbolos: `@SpringBootTest`, `@WithUserDetails`, `UserDetails`, `UsuarioControllerIntegrationTest`, `UsuarioDetailsService`.
- Comando: `./mvnw -B -Dtest=UsuarioControllerIntegrationTest test`.
- Observable: @WithUserDetails carga admin desde UsuarioDetailsService/DB de test.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.10 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.11 - Testear el filtro JWT

- Fuente: p. 1543 / `Paso 11: Testear el filtro JWT` / hash `8b2cae36f9c84e1e18fb28b9db2054334fba32927ec231988b3fe2bcb3b753c2`.
- Teoría: `TC-6.9-3`, `TC-6.9-4`.
- Práctica: `## Paso 6.9.11 - Testear el filtro JWT` / hash `c1c2955074b17cf903172191c71f560c2faa37218e140419b70080f140046079`.
- Acción: Testear filtro con tokens reales generados por JwtService para cubrir firma/claims/revocación.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java`, `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtServiceIntegrationTest.java`.
- Símbolos: `JwtFilterIntegrationTest`, `JwtService`, `jwt()`.
- Comando: `./mvnw -B -Dtest=JwtFilterIntegrationTest test`.
- Observable: Bearer real autentica y token inválido/revocado no lo hace.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.11 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.12 - Errores comunes del ejercicio

- Fuente: p. 1544 / `Paso 12: Errores comunes del ejercicio` / hash `b4aefbcb90aeaabfce20cfe48834e5d71659aa164eddb6cd30d0476575c85bf7`.
- Teoría: `TC-6.9-5`.
- Práctica: `## Paso 6.9.12 - Errores comunes del ejercicio` / hash `b96dd8524b0db4e9dad6c4d979a0b760cd5d51b6a3b9e4bfcbe9e24d6479ba42`.
- Acción: Evitar @MockBean, tests que sólo prueban mocks y confusión entre 401/403.
- Artefacto final: sin fichero final; paso de observación/verificación.
- Símbolos: `@SpringBootTest`, `@WithUserDetails`, `ROLE_`, `spring-security-test`.
- Comando: `grep -R "@MockBean" src/test || true`.
- Observable: cero @MockBean y cada test expresa una propiedad observable.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.12 queda aplicado o, si es temporal, registrado para su cierre explícito..

### 6.9.13 - Reto resuelto: test completo de autorización

- Fuente: p. 1546 / `Paso 13: Reto resuelto — Test completo de autorización` / hash `e3f10791f2f587aa839e80d5fceed615455e80bc5c2789d5497b99267fda3721`.
- Teoría: `TC-6.9-5`.
- Práctica: `## Paso 6.9.13 - Reto resuelto: test completo de autorización` / hash `19728f28a77faaf5dd5491b311e806f2086dd61eb8e2f0c60a47ea626596b5ef`.
- Acción: Construir test completo de autorización con cinco escenarios y observables de contrato.
- Artefacto final: `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerCompleteSecurityTest.java`.
- Símbolos: `ACCESO_DENEGADO`, `NO_AUTENTICADO`, `STATELESS`, `UsuarioControllerCompleteSecurityTest`, `jti`.
- Comando: `./mvnw -B -Dtest=UsuarioControllerCompleteSecurityTest test`.
- Observable: test reto cubre 401, USER 403, GESTOR 403, ADMIN 200 y contenido del listado.
- Verificación: `AUTOMATABLE+MANUAL`.
- Post-estado: El estado documentado por 6.9.13 queda aplicado o, si es temporal, registrado para su cierre explícito..

## Cadena inversa - proyecto final 122/122

| Fichero final | Clasificación | Paso(s) | Hash Git |
|---|---|---|---|
| `M6/proyecto/.gitignore` | M5_PRESERVED | heredado sin modificación M6 | `d1751292269b41725a2eb71f8a74b1e36ad53171` |
| `M6/proyecto/.mvn/wrapper/maven-wrapper.properties` | M5_PRESERVED | heredado sin modificación M6 | `78b60c17d0fd9600098f1cf4ace2594325c25c23` |
| `M6/proyecto/README.md` | M5_PRESERVED | heredado sin modificación M6 | `5d0e6ff49ca14b6be21c4719607a7c62cc1f6ee8` |
| `M6/proyecto/config/checkstyle/checkstyle.xml` | M5_PRESERVED | heredado sin modificación M6 | `58b0dcb59ef4c148f5b0a5b479fed2278c79e0be` |
| `M6/proyecto/mvnw` | M5_PRESERVED | heredado sin modificación M6 | `4ee2b546036be5dadf12be77f05cfc498209e75e` |
| `M6/proyecto/mvnw.cmd` | M5_PRESERVED | heredado sin modificación M6 | `bd523a180c242cac396bfa38d0d2cf5715e1cadd` |
| `M6/proyecto/pom.xml` | M5_EVOLVED | `6.1.1`, `6.1.2`, `6.1.3`, `6.6.1`, `6.9.1` | `86d74527cf6ab2ac3d1d196a98d5235f7a6cf3d4` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/MiProyectoApplication.java` | M5_PRESERVED | heredado sin modificación M6 | `473376d39a60c32aa41e7ba0e51c55ec6b753536` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Alumno.java` | M5_PRESERVED | heredado sin modificación M6 | `40bc020904ee8703db29607be480d0706e4c99ab` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoController.java` | M5_PRESERVED | heredado sin modificación M6 | `64049d6c8f4773ed41f963f1318bb3f2c10f9fe5` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `42ed77413c4d63c5f3871ddca4b3c709fb046c68` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoEspecificoException.java` | M5_PRESERVED | heredado sin modificación M6 | `440aef01626521efcc29ec9e16bd6d0a0f26806e` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoExceptionHandler.java` | M5_PRESERVED | heredado sin modificación M6 | `a6494b0d30b27b227a59e3fa5dac42f63f10ad24` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRepository.java` | M5_PRESERVED | heredado sin modificación M6 | `bc350f90c437575cbf7dae54a744703123c4fdc0` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoRequestDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `91cc175c5cff060e1f29ca90fd1521a9670e5290` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResponseDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `556881713f205ddb021e495d6bf41944cd4bcc49` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoResumenDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `d0833bfadcab097247669e3395774759c71e3e86` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoService.java` | M5_PRESERVED | heredado sin modificación M6 | `3ee66353ac8ed97d46867c7b2565114c04b3e25a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/AlumnoSpecifications.java` | M5_PRESERVED | heredado sin modificación M6 | `ea0574c17cdd3fa8ec7e4372e814cb047943da5b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/Direccion.java` | M5_PRESERVED | heredado sin modificación M6 | `fd8995acf8cc566ec535444e5f752d57fb19794d` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DireccionDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `af0e2b41fd2d956ca7cc20de8519462342003d42` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/DocumentoRepository.java` | M5_PRESERVED | heredado sin modificación M6 | `b12a68cab28f542dbba522caa6ff4cd5cdd8c0a8` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/alumno/EstadoAlumno.java` | M5_PRESERVED | heredado sin modificación M6 | `a419a6b447987c96c48a4fd43f32f4948fb74636` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/Auditoria.java` | M5_PRESERVED | heredado sin modificación M6 | `157fc045b1c4a995c8e8ea7bd2c2b5648d3bb662` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaRepository.java` | M5_PRESERVED | heredado sin modificación M6 | `7ca4873f129768dc360d5e17ac3d3fdcc64f05c3` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auditoria/AuditoriaService.java` | M5_PRESERVED | heredado sin modificación M6 | `e906e2d4fcd8009dc65d7e2acd0c70be1443171d` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthController.java` | M6_ADDED_FUNCTIONAL | `6.3.11`, `6.6.7`, `6.6.13` | `8343c318945f93e92f5256974499493ebe593086` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthExceptionHandler.java` | M6_ADDED_FUNCTIONAL | `6.6.12` | `577bcf6b9a5767a020ecb51838b3ca0c8205be0f` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/AuthService.java` | M6_ADDED_FUNCTIONAL | `6.3.11`, `6.3.14`, `6.6.6`, `6.6.11`, `6.6.13` | `fee5a6c471c6e649941e1d12c2d2198f4e9972cf` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CambioPasswordRequestDTO.java` | M6_ADDED_FUNCTIONAL | `6.3.14` | `e3bd0fa4b711b5a0f0791605a08a3997bb0a9836` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CambioRolesRequestDTO.java` | M6_ADDED_FUNCTIONAL | `6.4.12` | `7123fc8b951939b8cd2c3f940378f46c35d76712` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/CredencialesInvalidasException.java` | M6_ADDED_FUNCTIONAL | `6.6.12` | `93ebe87ea40ef938f549de8c4eca7560c48d84eb` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/GestorController.java` | M6_ADDED_FUNCTIONAL | `6.2.12`, `6.8.4` | `6d0b30143e195ab3f49164f39537ae3c73acdf7a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAccessDeniedHandler.java` | M6_ADDED_FUNCTIONAL | `6.8.2` | `ae8697145e9208e64791924159355e6c7554cb6c` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationEntryPoint.java` | M6_ADDED_FUNCTIONAL | `6.8.1` | `47e0d80a461c457d13346b3cf22d34c5ba223676` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtAuthenticationFilter.java` | M6_ADDED_FUNCTIONAL | `6.7.2`, `6.7.6`, `6.7.10` | `37666d8654ae2bc7f32661eb2069a1d63f9941d4` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtConfig.java` | M6_ADDED_FUNCTIONAL | `6.6.3` | `dfbd2dd143e14eb3a90fabe141c8648a7fd16091` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/JwtService.java` | M6_ADDED_FUNCTIONAL | `6.6.4`, `6.6.10`, `6.7.1` | `a0374ced8bfea2d36d7bcaba134297e30d927205` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/LoginRequestDTO.java` | M6_ADDED_FUNCTIONAL | `6.6.5` | `fc8655df7763f82705960142d3aa2f4c9070c5a4` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/LoginResponseDTO.java` | M6_ADDED_FUNCTIONAL | `6.6.5` | `574af0921240705b3ef5ff3bcdbdffc407332967` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RefreshRequestDTO.java` | M6_ADDED_FUNCTIONAL | `6.6.5` | `5834229bce436f06dac12917c5a86836c358261b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RegistroRequestDTO.java` | M6_ADDED_FUNCTIONAL | `6.3.10` | `4db4cb2f123d62fb7e6aff8f1174652ddf553e39` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Rol.java` | M6_ADDED_FUNCTIONAL | `6.3.2`, `6.3.8` | `6aebf67448c58387ed71faec74530d485e50b0e0` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/RolRepository.java` | M6_ADDED_FUNCTIONAL | `6.3.4` | `f92e7130792165652a1d2746ebf641a2e039fcd6` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/SecurityErrorWriter.java` | M6_ADDED_FUNCTIONAL | `6.8.1`, `6.8.2` | `d6eb7a75164628cc5c48ef921e85c24b9b28c8ac` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenInvalidoException.java` | M6_ADDED_FUNCTIONAL | `6.6.12` | `5c59dff7b2e53ae81f609c3cbce546cdfbb0d62e` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/TokenRevocationService.java` | M6_ADDED_FUNCTIONAL | `6.6.6`, `6.6.13` | `699b13d33f1491b6fcefbbdede3279fc48841224` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/Usuario.java` | M6_ADDED_FUNCTIONAL | `6.3.3`, `6.3.8`, `6.4.12` | `ec9ed4e168ed27e25731277efd9716a61ee1c2c7` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioController.java` | M6_ADDED_FUNCTIONAL | `6.4.3`, `6.4.5`, `6.4.6`, `6.4.12` | `9b9422af8f6ecca3668f91f55c60f4fa24c8b76a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioDetailsService.java` | M6_ADDED_FUNCTIONAL | `6.3.5`, `6.3.9` | `5c2f6d3232ce2beaedebeb2f0ec5c7579bd6282f` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioPrincipal.java` | M6_ADDED_FUNCTIONAL | `6.4.8`, `6.7.1`, `6.7.6`, `6.7.12` | `36ec30a02273698791d7f68bda136497409739be` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioRepository.java` | M6_ADDED_FUNCTIONAL | `6.3.4` | `38218c9032e9addf3989f3c6fb423c32eb603dcd` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioResponseDTO.java` | M6_ADDED_FUNCTIONAL | `6.3.10` | `686d876a5171110de4d36680bd0788aa3b3170b0` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuarioService.java` | M6_ADDED_FUNCTIONAL | `6.4.4`, `6.4.12` | `249a93b1384ff4eb293c887e2df2aae11be5573e` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/auth/UsuariosInicialesConfig.java` | M6_ADDED_FUNCTIONAL | `6.3.7`, `6.8.5` | `b1476cc837c222b2b42dfd61572cb0b8dad897b8` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/controller/PerfilController.java` | M6_ADDED_FUNCTIONAL | `6.2.2`, `6.2.7`, `6.2.8`, `6.3.9`, `6.3.14`, `6.4.7`, `6.7.12`, `6.8.12` | `c779067900a15ffb1ece2a9811e0b0dec2a307c1` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ErrorResponse.java` | M5_PRESERVED | heredado sin modificación M6 | `e3d185f307cdaa8bd3bd04e1f5af20a6fc873a40` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/dto/error/ValidationError.java` | M5_PRESERVED | heredado sin modificación M6 | `a3c85b0aa0344579191c75c6b1f4144a1e9130a1` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/AplicacionException.java` | M5_PRESERVED | heredado sin modificación M6 | `612a749d3ed2052fa718714255f41fa9f4880c05` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/BusinessExceptionHandler.java` | M5_PRESERVED | heredado sin modificación M6 | `7753e03122511e68b2988d3259e4aed5d3cecb7b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorResponseFactory.java` | M5_PRESERVED | heredado sin modificación M6 | `e0d21c5f3ad421e646cadaa88625e5f819493b21` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ErrorTecnicoException.java` | M5_PRESERVED | heredado sin modificación M6 | `754f1b477f04f6796fd4587cfd35b857aac21b10` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/GenericExceptionHandler.java` | M5_PRESERVED | heredado sin modificación M6 | `7dd35120415d5173277a0347df088769a24e389b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/NegocioException.java` | M5_PRESERVED | heredado sin modificación M6 | `4034c387d4a4df8050bb8f7140984da568bad13a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/OperacionNoPermitidaException.java` | M5_PRESERVED | heredado sin modificación M6 | `1163083273572056a98b6adfdbcda4199196a989` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoDuplicadoException.java` | M5_PRESERVED | heredado sin modificación M6 | `20fb1d04a3c28e816c896b91507f8d1f69d54121` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/RecursoNoEncontradoException.java` | M5_PRESERVED | heredado sin modificación M6 | `a21587df02ed9d09a58863aabfefe3ee179e47fc` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidacionNegocioException.java` | M5_PRESERVED | heredado sin modificación M6 | `fd6644f8541aa5f312fb2b825376581605cd6cf6` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/exception/ValidationExceptionHandler.java` | M5_PRESERVED | heredado sin modificación M6 | `74b662916f626bbf312b8823679a1f51f182291b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/common/util/FechasUtil.java` | M5_PRESERVED | heredado sin modificación M6 | `563db294b3b88533e9da5545428fb0e0707f355d` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/AppProperties.java` | M5_PRESERVED | heredado sin modificación M6 | `4ec21dca5e856902aba094cfe4b575921fae95be` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/CorsConfig.java` | M5_PRESERVED | `6.1.5`, `6.2.1` | `3cc5f98f75ef4a625baeb12362a27f79905b5e04` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/DatosInicialesConfig.java` | M5_PRESERVED | heredado sin modificación M6 | `795adb1463f4f2c6e4ca75c193ffa775e2062583` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/JacksonConfig.java` | M5_PRESERVED | heredado sin modificación M6 | `a3bf42cfcdb0a2ee447575dfdbef2d01a41668ca` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/PerfilConfig.java` | M5_PRESERVED | heredado sin modificación M6 | `8055bcdaf641a48ca69f8493b44db33082d0d20f` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/config/SecurityConfig.java` | M6_ADDED_FUNCTIONAL | `6.1.5`, `6.1.6`, `6.1.7`, `6.1.8`, `6.1.9`, `6.1.12`, `6.2.1`, `6.2.3`, `6.2.5`, `6.2.6`, `6.2.9`, `6.2.12`, `6.3.12`, `6.4.1`, `6.4.2`, `6.4.5`, `6.6.8`, `6.7.3`, `6.7.4`, `6.8.3` | `c7b8ea066059dc298b5f91431193019013fa638c` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/Curso.java` | M5_PRESERVED | heredado sin modificación M6 | `8927826ad6ce19995423b84a0efadc5250519531` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoController.java` | M5_PRESERVED | heredado sin modificación M6 | `1df9aaf735d5e0cd6a81e70ceaa90bda192be98a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `94c8aecd76ab447cea02b2acef81bbf8951f6800` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoRepository.java` | M5_PRESERVED | heredado sin modificación M6 | `aadbe79b428d9f83c99eaa5ec12b7bb44229b14e` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/curso/CursoService.java` | M5_PRESERVED | heredado sin modificación M6 | `96c23acfe918ef3ad392a7e1c6dc988d2c672c5a` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteController.java` | M5_PRESERVED | heredado sin modificación M6 | `d6b9c93a206f84aefeed1e21b5a6331094c764a9` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `3975ef6a60d4722d635d27ccdfd8bb630a3ee03f` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRepository.java` | M5_PRESERVED | heredado sin modificación M6 | `a86409d318cf1bd53abedf77d5726dafa5988845` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteRequestDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `b346e117c25daaaf3fa17928038f8d8da0f6dc0e` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/ExpedienteService.java` | M5_PRESERVED | heredado sin modificación M6 | `b912515973cf573f1964e6aa3bbf8c58be599969` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/expediente/SolicitanteDTO.java` | M5_PRESERVED | heredado sin modificación M6 | `72c80d2abdab85036653f33914fbe2acb23e2cc2` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroController.java` | M5_PRESERVED | heredado sin modificación M6 | `a29fc7818f0836f8c73ede8841d710f2e5b03fa9` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandler.java` | M5_PRESERVED | heredado sin modificación M6 | `d0c95d203e6ec8e83c3f12e41c18c3c8beb76abd` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/fichero/FicheroNoEncontradoException.java` | M5_PRESERVED | heredado sin modificación M6 | `34632d4a2262439072fbd807270531d6c568acce` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/info/InfoAplicacionController.java` | M5_PRESERVED | heredado sin modificación M6 | `6e47c2a18d0b5d014dc555f43378aba9348eb221` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/info/PublicInfoController.java` | M6_ADDED_FUNCTIONAL | `6.1.12`, `6.2.6` | `46cde999819c0dc5cce98ad970c03531ed80500b` |
| `M6/proyecto/src/main/java/es/mecd/demo/miproyecto/saludo/SaludoController.java` | M5_PRESERVED | heredado sin modificación M6 | `53d69a4fa1d585bf4c5b5feda851fcea422056a2` |
| `M6/proyecto/src/main/resources/application-dev.properties` | M5_EVOLVED | `6.6.2` | `72f89e5d1a6399b0dd2236d13ebc40c358cf7a2d` |
| `M6/proyecto/src/main/resources/application-prod.properties` | M5_EVOLVED | `6.6.2` | `937243988000c94e1d3c82ab023270d6887aab6c` |
| `M6/proyecto/src/main/resources/application-test.properties` | M5_EVOLVED | `6.6.2` | `41e7999ff6960ff0aff52cd4dd8137f9d1f7a462` |
| `M6/proyecto/src/main/resources/application.properties` | M5_PRESERVED | heredado sin modificación M6 | `53ab072c56041d770c40255f5f7ef1bfe39d58c2` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/MiProyectoApplicationTest.java` | M5_PRESERVED | heredado sin modificación M6 | `71fa69a713db9d3a39f567382a6732bc32a53130` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoControllerTest.java` | M5_EVOLVED | `6.9.3` | `bfc9a8ef87d0d29f0c76aa86c2a69a34d14078dc` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoRepositoryTest.java` | M5_PRESERVED | heredado sin modificación M6 | `379b53fdd1d2128837276fd7af1b143b677ce98e` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/alumno/AlumnoServiceTest.java` | M5_PRESERVED | heredado sin modificación M6 | `544ae44ddcc81d0a334e7294e8d757cc9fba46c0` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/AuthFlowIntegrationTest.java` | M6_ADDED_FUNCTIONAL | `6.6.9`, `6.6.11`, `6.8.6`, `6.8.8`, `6.8.9`, `6.9.6`, `6.9.7`, `6.9.8` | `ae41906a12b7a2c062998fa99d9f36fcb83e2dd0` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/GestorSecurityTest.java` | M6_ADDED_FUNCTIONAL | `6.8.10`, `6.9.4` | `34dd2125af1c11945315111172f26b7f317aac8b` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtFilterIntegrationTest.java` | M6_ADDED_FUNCTIONAL | `6.7.5`, `6.7.8`, `6.7.9`, `6.8.10`, `6.9.11` | `5086023428c82729886e49639f7b912ed64db8de` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/JwtServiceIntegrationTest.java` | M6_ADDED_FUNCTIONAL | `6.9.11` | `ae5ab3eeb79c188e4d3f845b89fcb6bcb08f7a62` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerCompleteSecurityTest.java` | M6_ADDED_FUNCTIONAL | `6.7.7`, `6.9.13` | `ac67d378a4f99c179215f193d366e96be4446ca1` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerIntegrationTest.java` | M6_ADDED_FUNCTIONAL | `6.9.10` | `515c3ca96e3ecb80ea5d214e99e0a3f9b8032a93` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerSecurityTest.java` | M6_ADDED_FUNCTIONAL | `6.8.7`, `6.9.2`, `6.9.3`, `6.9.9` | `e70c4b64a3dd32eedda6b3a6beb30b82e1e14f8d` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/auth/UsuarioControllerTest.java` | M6_ADDED_FUNCTIONAL | `6.4.10` | `d4568dceb8c4f73dfa0ea0447009da74467b7860` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/controller/PublicControllerSecurityTest.java` | M6_ADDED_FUNCTIONAL | `6.9.5` | `cc5dec347954da75ba2c189066ee700b13ec31e8` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/AplicacionExceptionTest.java` | M5_PRESERVED | heredado sin modificación M6 | `1710370dc6851f9c4eb1e401f05137b0e36b781a` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/exception/ErrorHandlingTest.java` | M5_EVOLVED | `6.9.3` | `a41602994fee23fa89e9a31e27638f6780013993` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/common/util/FechasUtilTest.java` | M5_PRESERVED | heredado sin modificación M6 | `734a1e59ce7ac930d8742a65274aac4216372bc0` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/config/AppPropertiesTest.java` | M5_PRESERVED | heredado sin modificación M6 | `597de9823a94d5bd59e2e7a985a3fd2c5d6536da` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/config/CorsConfigTest.java` | M5_EVOLVED | `6.9.3` | `8b892e3a289637f86b63cd3ea3746ddf12d0c056` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/curso/CursoRepositoryTest.java` | M5_PRESERVED | heredado sin modificación M6 | `40d192bc78fb72f302a14893ec185089021df155` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteControllerTest.java` | M5_EVOLVED | `6.9.3` | `7047a1272dfa5a33b3390fab3a0cb114b708f1b8` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/expediente/ExpedienteServiceTest.java` | M5_PRESERVED | heredado sin modificación M6 | `d726444efcd88301ce492899b6a9a740f3d8c6f1` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/fichero/FicheroExceptionHandlerTest.java` | M5_EVOLVED | `6.9.3` | `04e3891bc1e7e72a5e2003e50f284acdc3615cdd` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManual.java` | M6_ADDED_FUNCTIONAL | `6.5.1`, `6.5.2`, `6.5.3`, `6.5.4`, `6.5.5`, `6.5.6`, `6.5.7`, `6.5.8`, `6.5.9`, `6.5.10`, `6.5.11`, `6.5.12` | `edcc8e329887a1299368042984de80ff57bd2de7` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/jwt/JwtManualTest.java` | M6_ADDED_FUNCTIONAL | `6.5.5`, `6.5.11` | `8271ab3a7a6ad221f582e7adda30386ae0ff7a7e` |
| `M6/proyecto/src/test/java/es/mecd/demo/miproyecto/saludo/SaludoControllerTest.java` | M5_PRESERVED | heredado sin modificación M6 | `3917b87a745ed024b56aa3467c8aad53d31a0c84` |

## Ciclo transitorio cerrado

- `InMemoryUserConfig.java`: creado en 6.2.4, utilizado pedagógicamente y eliminado en 6.3.6; ausente del proyecto final.
