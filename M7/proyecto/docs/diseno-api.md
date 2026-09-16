# Diseño de la API de Gestión de Becas

Este documento corresponde al paso 7.1.1. En este módulo, `Alumno`, `Usuario/Rol`, JWT y el contrato común de errores se preparan dentro del propio proyecto antes de comenzar los pasos 7.1–7.7.

## Recursos

- **Beca:** tipo de beca.
- **Alumno:** alumno que solicita la beca.
- **SolicitudBeca:** solicitud de un alumno para una beca.
- **Documento:** adjunto de una solicitud.
- **Usuario:** identidad con roles.

## Relaciones

- Solicitud → Alumno (`@ManyToOne`).
- Solicitud → Beca (`@ManyToOne`).
- Solicitud → Documento (`@OneToMany`).
- Usuario → Rol (`@ManyToMany`).

## Endpoints base (24)

| Método | URL | Actor | Código |
|---|---|---|---:|
| POST | /api/v1/auth/registro | Público | 201 |
| POST | /api/v1/auth/login | Público | 200 |
| POST | /api/v1/auth/refresh | Público | 200 |
| GET | /api/v1/becas | Público | 200 |
| GET | /api/v1/becas/{id} | Público | 200 |
| POST | /api/v1/becas | ADMIN | 201 |
| PUT | /api/v1/becas/{id} | ADMIN | 200 |
| DELETE | /api/v1/becas/{id} | ADMIN | 204 |
| GET | /api/v1/alumnos | Autenticado | 200 |
| GET | /api/v1/alumnos/{id} | Autenticado | 200 |
| POST | /api/v1/alumnos | CIUDADANO | 201 |
| PUT | /api/v1/alumnos/{id} | CIUDADANO | 200 |
| DELETE | /api/v1/alumnos/{id} | ADMIN | 204 |
| GET | /api/v1/solicitudes | Autenticado | 200 |
| GET | /api/v1/solicitudes/{id} | Autenticado | 200 |
| POST | /api/v1/solicitudes | CIUDADANO | 201 |
| PATCH | /api/v1/solicitudes/{id}/estado | GESTOR | 200 |
| DELETE | /api/v1/solicitudes/{id} | CIUDADANO | 204 |
| GET | /api/v1/solicitudes/{id}/documentos | Autenticado | 200 |
| POST | /api/v1/solicitudes/{id}/documentos | CIUDADANO | 201 |
| DELETE | /api/v1/solicitudes/{id}/documentos/{docId} | CIUDADANO | 204 |
| GET | /api/v1/usuarios | ADMIN | 200 |
| GET | /api/v1/usuarios/{id} | ADMIN | 200 |
| PATCH | /api/v1/usuarios/{id}/roles | ADMIN | 200 |

Los retos añaden `GET /api/v1/alumnos/{id}/solicitudes` y `GET /api/v1/estadisticas/solicitudes-por-estado`.

## Reglas de negocio

1. Un alumno no puede tener dos solicitudes activas para la misma beca en el mismo año.
2. Solo se modifica una solicitud en `BORRADOR` o `ENVIADA`.
3. Solo se cambia el estado desde `ENVIADA` o `EN_REVISION`.
4. Una solicitud aprobada no se modifica ni elimina.
5. Aprobar exige `importeConcedido`.
6. El importe concedido no supera el máximo de la beca.
7. Los documentos se eliminan en cascada con la solicitud.
8. Una beca con solicitudes no se elimina.
9. El año de la beca debe ser el actual o el siguiente.
10. Solo se añaden documentos en `BORRADOR` o `ENVIADA`.
11. Un documento no supera 10 MiB.
12. Los tipos permitidos son PDF, JPG/JPEG y PNG.

## Visibilidad simplificada

No se crea relación `Usuario → Alumno`; cualquier usuario autenticado puede consultar solicitudes, tal como aclara la fuente.
