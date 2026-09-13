# M4 - Guía para ejecutar el proyecto en Codespaces

Esta guía te explica cómo levantar un **Codespace** y ejecutar el proyecto de **Módulo 4** paso a paso.

> **Prerequisito:** Asegúrate de haber completado M0, M1, M2 y M3 antes de comenzar con M4.

---

## ¿Es gratis?

✅ **Sí, es completamente gratis** (hasta 60 horas/mes en cuentas personales).

---

## Paso 1: Abrir el Codespace

### Opción A: Crear un nuevo Codespace
1. Ve a la página principal del repositorio: https://github.com/jaimecopilot/SPRING-BOOT
2. Haz clic en el botón verde **`<> Code`**
3. Selecciona la pestaña **`Codespaces`**
4. Haz clic en **`Create codespace on main`**

### Opción B: Reutilizar el Codespace anterior
Si acabas de terminar M3:
1. Ve a https://github.com/codespaces
2. Selecciona tu Codespace existente
3. Presiona **Ctrl + Shift + P** y busca **"Terminal: New Terminal"** para abrir una nueva terminal

> **Nota:** GitHub tardará 30-60 segundos en crear un nuevo Codespace. Una vez listo, verás VS Code en el navegador.

---

## Paso 2: Verificar Java y Maven

Abre la terminal integrada:

### Abre la terminal
- Presiona **`Ctrl + ~`** (o **`Cmd + ~`** en Mac)
- O ve a **Terminal** → **New Terminal**

### Verifica que Java 17 está instalado
```bash
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" ...
```

---

## Paso 3: Navega al proyecto M4

Desde cualquier ubicación, navega a M4:

```bash
cd M4/proyecto
```

Si venías de M3, simplemente:
```bash
cd ../M4/proyecto
```

---

## Paso 4: Instala las dependencias (primera vez)

Ejecuta Maven para descargar todas las dependencias:

```bash
./mvnw clean install
```

> **Nota:** Esto puede tardar 2-3 minutos la primera vez. Después será más rápido.

---

## Paso 5: Inicia la aplicación Spring Boot

Ejecuta el servidor:

```bash
./mvnw spring-boot:run
```

Verás en la consola algo como:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v3.x.x)

Started MiProyectoApplication in X.XXX seconds
```

✅ **¡Listo! Tu aplicación está corriendo.**

---

## Paso 6: Accede a la aplicación

### Opción A: Usar el puerto forwarding automático
1. En la barra inferior de VS Code, verás un indicador de puerto (ej: `8080`)
2. Haz clic en él o presiona **`Ctrl + Shift + P`**
3. Busca **"Ports: Focus on Ports View"**
4. Haz clic en el icono de globo para abrir en el navegador

### Opción B: Acceso manual
- Copia la URL que Codespaces generó (algo como `https://username-xxxx.github.dev/`)
- Añade el puerto 8080: `https://username-xxxx.github.dev:8080`

---

## Paso 7: Prueba los endpoints de M4

Abre una **nueva terminal** (sin cerrar la anterior) y prueba los endpoints:

### Autenticación y Login
M4 introduce **Spring Security** y autenticación:

#### Registrar un nuevo usuario
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"jaime","password":"pass123","email":"jaime@example.com"}' \
  http://localhost:8080/api/v1/auth/register
```

#### Login y obtener token JWT
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"jaime","password":"pass123"}' \
  http://localhost:8080/api/v1/auth/login
```

Respuesta (guarda el token):
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Acceder a recursos protegidos con token
```bash
TOKEN="tu_token_aqui"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/alumnos
```

### Operaciones CRUD con seguridad
```bash
TOKEN="tu_token_aqui"

# GET alumnos
curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/v1/alumnos?page=0&size=10"

# POST crear alumno
curl -X POST \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"María","apellidos":"López","dni":"11111111C","fechaNacimiento":"2011-03-20","curso":"4º"}' \
  http://localhost:8080/api/v1/alumnos

# PUT actualizar alumno
curl -X PUT \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan","apellidos":"García","dni":"12345678A","fechaNacimiento":"2010-05-15","curso":"2º"}' \
  http://localhost:8080/api/v1/alumnos/1

# DELETE eliminar alumno
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/alumnos/1
```

---

## Paso 8: Usar Postman con autenticación

M4 incluye colecciones de Postman con configuración de seguridad:

### Opción A: Importar en Postman Desktop
1. Descarga Postman desde https://www.postman.com/downloads/
2. Ve a **File** → **Import**
3. Selecciona `M4/postman/M4.postman_collection.json` (si existe)
4. Importa también el entorno: `M4/postman/M4.local.postman_environment.json`
5. En Postman:
   - Ve a **Authorization** → **Bearer Token**
   - Pega el token obtenido del login
   - O usa variables de entorno

### Opción B: Script de Postman para obtener token automáticamente
En la pestaña de **Pre-request Script** de una petición:
```javascript
const loginRequest = {
  url: pm.environment.get("baseUrl") + "/api/v1/auth/login",
  method: "POST",
  header: {
    "Content-Type": "application/json"
  },
  body: {
    mode: "raw",
    raw: JSON.stringify({
      username: pm.environment.get("username"),
      password: pm.environment.get("password")
    })
  }
};

pm.sendRequest(loginRequest, (err, response) => {
  if (!err) {
    let jsonResponse = response.json();
    pm.environment.set("token", jsonResponse.token);
  }
});
```

---

## Paso 9: Explorar la estructura de M4

En VS Code, navega por los archivos del proyecto:

```
M4/proyecto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── es/mecd/demo/miproyecto/
│   │   │       ├── auth/
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── JwtProvider.java (generador de tokens)
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── LoginRequest.java
│   │   │       │   └── LoginResponse.java
│   │   │       ├── security/
│   │   │       │   ├── SecurityConfig.java (@EnableWebSecurity)
│   │   │       │   ├── UserDetailsServiceImpl.java
│   │   │       │   └── JwtAuthenticationProvider.java
│   │   │       ├── usuario/
│   │   │       │   ├── Usuario.java (entidad con roles)
│   │   │       │   ├── UsuarioRepository.java
│   │   │       │   ├── Rol.java (enum o entidad)
│   │   │       │   └── UsuarioDTO.java
│   │   │       ├── alumno/
│   │   │       │   ├── Alumno.java
│   │   │       │   ├── AlumnoController.java (con @Secured)
│   │   │       │   ├── AlumnoService.java
│   │   │       │   ├── AlumnoRepository.java
│   │   │       │   ├── AlumnoRequestDTO.java
│   │   │       │   └── AlumnoResponseDTO.java
│   │   │       ├── config/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── JwtExceptionFilter.java
│   │   │       └── MiProyectoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
│       └── java/...
├── postman/ (si existe)
│   ├── M4.postman_collection.json
│   └── M4.local.postman_environment.json
└── pom.xml
```

**M4 introduce:**
- **Spring Security:** Framework de seguridad
- **JWT (JSON Web Tokens):** Autenticación stateless
- **Autenticación:** Login, registro, tokens
- **Autorización:** Roles y permisos (`@Secured`, `@PreAuthorize`)
- **Filtros de seguridad:** `JwtAuthenticationFilter`
- **Usuarios y roles:** Entidades para gestionar acceso
- **PasswordEncoder:** Bcrypt para hashing de contraseñas
- **Excepciones de seguridad:** `AuthenticationException`, `AccessDeniedException`

---

## Paso 10: Características de seguridad

### Anotaciones clave
```java
@RestController
@RequestMapping("/api/v1/alumnos")
public class AlumnoController {
  
  // Solo usuarios autenticados
  @GetMapping
  @Secured("ROLE_USER")
  public List<Alumno> getAll() { ... }
  
  // Solo administradores
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void delete(@PathVariable Long id) { ... }
  
  // Sin autenticación
  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) { ... }
}
```

### Configuración de Spring Security
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeRequests()
        .antMatchers("/api/v1/auth/**").permitAll()
        .anyRequest().authenticated()
      .and()
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}
```

---

## Paso 11: Ejecutar tests

M4 incluye tests para seguridad. Ejecútalos con:

```bash
./mvnw test
```

O un test específico:

```bash
./mvnw test -Dtest=AuthControllerTest
```

Ver cobertura de tests:

```bash
./mvnw test jacoco:report
```

---

## Paso 12: Probar flujo completo de seguridad

### Escenario 1: Sin token (acceso denegado)
```bash
curl -i http://localhost:8080/api/v1/alumnos
# Respuesta: 401 Unauthorized
```

### Escenario 2: Con token inválido (acceso denegado)
```bash
curl -H "Authorization: Bearer invalid.token.here" \
  http://localhost:8080/api/v1/alumnos
# Respuesta: 401 Unauthorized
```

### Escenario 3: Con token válido (acceso permitido)
```bash
TOKEN="token_obtenido_del_login"
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/alumnos
# Respuesta: 200 OK con lista de alumnos
```

### Escenario 4: Usuario sin permisos (acceso denegado)
```bash
TOKEN="token_usuario_sin_admin"
curl -X DELETE \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/alumnos/1
# Respuesta: 403 Forbidden
```

---

## Detener la aplicación

- En la terminal donde corre Spring Boot, presiona **`Ctrl + C`**
- El servidor se detendrá

---

## Solución de problemas

### ❌ "mvnw: command not found"
**Solución:** Asegúrate de estar en la carpeta correcta:
```bash
pwd  # Debería mostrar: .../M4/proyecto
```

### ❌ "Java not found"
**Solución:** Codespaces debería tener Java preinstalado. Si no, cierra el Codespace y abre uno nuevo.

### ❌ Puerto 8080 ya está en uso
**Solución:** Detén el servidor anterior (Ctrl + C) o Spring Boot usará otro puerto automáticamente.

### ❌ Token expirado o inválido
**Solución:** Obtén un nuevo token haciendo login de nuevo:
```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"username":"jaime","password":"pass123"}' \
  http://localhost:8080/api/v1/auth/login
```

### ❌ Error "Access Denied" aunque tengo token
**Solución:** Verifica que tu usuario tenga el rol necesario. Puede que necesites `ROLE_ADMIN` en lugar de `ROLE_USER`.

### ❌ Falla la descarga de dependencias
**Solución:** Intenta:
```bash
./mvnw clean
./mvnw install
```

---

## Consejos útiles

- 💾 **Tu trabajo se guarda automáticamente** en Codespaces
- 🔄 **Vuelve al Codespace anterior:** Ve a https://github.com/codespaces y selecciona tu Codespace
- ⏱️ **Libre 60 horas/mes:** Codespaces se pausa automáticamente después de 30 min de inactividad
- 📊 **Accede a H2 Console:** http://localhost:8080/h2-console (si está habilitada)
- 🧪 **Ejecuta tests con cobertura:** `./mvnw test jacoco:report`
- 🔑 **Guarda tokens en variables:** En curl puedes hacer `TOKEN=$(curl -s ... | jq -r '.token')`
- 📋 **Usa Postman para gestionar tokens:** Es más cómodo que línea de comandos

---

## ¿Qué hace M4?

M4 introduce **seguridad y autenticación** con Spring Security:

### Conceptos principales
- **Autenticación:** Verificar identidad del usuario (login)
- **Autorización:** Verificar permisos del usuario (roles)
- **JWT:** Tokens para estado sin sesión
- **Spring Security:** Framework completo de seguridad

### Flujo de seguridad
1. Usuario se registra → se guarda con contraseña hasheada
2. Usuario hace login → recibe JWT token
3. Usuario envía peticiones con token en header `Authorization: Bearer <token>`
4. Servidor valida token con cada petición
5. Si token válido → endpoint devuelve datos
6. Si token inválido/expirado → 401 Unauthorized
7. Si usuario sin permisos → 403 Forbidden

### Características
- ✅ Registro de usuarios
- ✅ Login con JWT
- ✅ Validación de token en cada petición
- ✅ Roles y autorización (`@Secured`, `@PreAuthorize`)
- ✅ Hashing de contraseñas con Bcrypt
- ✅ Endpoints públicos vs protegidos
- ✅ Manejo de excepciones de seguridad

**Conceptos clave:**
- `@EnableWebSecurity`, `SecurityFilterChain`
- `PasswordEncoder`, `BCryptPasswordEncoder`
- `JwtProvider` (generar/validar tokens)
- `UserDetailsService`, `UserDetails`
- Roles y autoridades
- Filtros de autenticación

Consulta **TEORIA.md** y **PRACTICA.md** en esta carpeta para detalles.

---

## Próximos pasos (después de M4)

Felicidades por completar M0 → M4. Ahora dominan:

✅ Fundamentos de Spring Boot  
✅ Arquitectura en capas  
✅ Persistencia con JPA  
✅ APIs REST completas  
✅ **Seguridad y autenticación**

Pueden explorar:
- 📦 **Microservicios:** Spring Cloud, Eureka, Config Server
- 📡 **APIs asincrónicas:** WebSockets, RabbitMQ, Kafka
- 🧪 **Testing avanzado:** MockMvc, Testcontainers, WireMock
- 📚 **Documentación:** Swagger/OpenAPI, JavaDoc
- 🚀 **Despliegue:** Docker, Kubernetes, Cloud (AWS, GCP, Azure)
- 🔍 **Monitoreo:** Actuator, Prometheus, Grafana
- 🗄️ **Escalabilidad:** Caché (Redis), Load Balancing

---

**¡Felicidades por llegar a M4! 🎉🚀🔐**
