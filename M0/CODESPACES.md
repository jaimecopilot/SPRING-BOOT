# M0 - Guía para ejecutar el proyecto en Codespaces

Esta guía te explica cómo levantar un **Codespace** y ejecutar el proyecto de **Módulo 0** paso a paso.

## ¿Es gratis?

✅ **Sí, es completamente gratis** (hasta 60 horas/mes en cuentas personales).

---

## Paso 1: Abrir el Codespace

### Opción A: Desde la rama main del repositorio
1. Ve a la página principal del repositorio: https://github.com/jaimecopilot/SPRING-BOOT
2. Haz clic en el botón verde **`<> Code`**
3. Selecciona la pestaña **`Codespaces`**
4. Haz clic en **`Create codespace on main`**

### Opción B: Directamente desde esta carpeta (M0)
1. Ve a la carpeta M0: https://github.com/jaimecopilot/SPRING-BOOT/tree/main/M0
2. Repite los pasos anteriores

> **Nota:** GitHub tardará 30-60 segundos en crear el Codespace. Una vez listo, verás VS Code en el navegador.

---

## Paso 2: Verificar Java y Maven

Una vez que Codespaces haya abierto, abre la terminal integrada:

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

### Verifica Maven Wrapper
```bash
ls -la M0/proyecto/
```

Busca los archivos `mvnw` y `mvnw.cmd`.

---

## Paso 3: Navega al proyecto M0

```bash
cd M0/proyecto
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

## Paso 7: Prueba la aplicación (opcional)

Abre una **nueva terminal** (sin cerrar la anterior) y prueba con curl:

```bash
curl -i http://localhost:8080/
```

O si has creado endpoints específicos en M0, pruébalos aquí.

---

## Detener la aplicación

- En la terminal donde corre Spring Boot, presiona **`Ctrl + C`**
- El servidor se detendrá

---

## Pasos futuros (M1, M2, M3)

Cuando termines M0 y quieras continuar con M1:

1. Cierra el Codespace o abre una nueva terminal
2. Navega a la carpeta del siguiente módulo:
   ```bash
   cd ../M1/proyecto
   ```
3. Repite desde el **Paso 4**

---

## Solución de problemas

### ❌ "mvnw: command not found"
**Solución:** Asegúrate de estar en la carpeta correcta:
```bash
pwd  # Debería mostrar: .../M0/proyecto
```

### ❌ "Java not found"
**Solución:** Codespaces debería tener Java preinstalado. Si no, cierra el Codespace y abre uno nuevo.

### ❌ Puerto 8080 ya está en uso
**Solución:** Spring Boot usará automáticamente otro puerto. Revisa la salida de la consola para ver cuál.

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
- 📂 **Explora el proyecto:** Abre `src/main/java` para ver el código fuente

---

## ¿Qué hace M0?

M0 es la base del curso. Contiene:
- Conceptos fundamentales de Spring Boot
- Primeras anotaciones (`@SpringBootApplication`, `@RestController`)
- Endpoints básicos
- Estructura de un proyecto Maven

Consulta **TEORIA.md** y **PRACTICA.md** en esta carpeta para detalles.

---

**¡Listo para empezar! 🚀**
