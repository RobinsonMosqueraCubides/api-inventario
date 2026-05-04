# API Inventario

Backend REST para un dashboard de gestion de inventario. La API permite autenticar usuarios, administrar productos y categorias, registrar movimientos de inventario con control de stock, consultar reportes, generar notificaciones y revisar auditoria.

## Stack

- Java 21
- Spring Boot 3.3
- Spring Web
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL
- Flyway
- Bean Validation
- Lombok

## Funcionalidades

- Autenticacion con JWT.
- Usuarios con roles `admin`, `supervisor` y `empleado`.
- CRUD de productos con SKU unico.
- CRUD de categorias.
- Registro transaccional de movimientos:
  - entrada
  - salida
  - ajuste
- Validacion para impedir stock negativo.
- Soft delete de productos mediante estado `Inactivo`.
- Auditoria de acciones relevantes.
- Notificaciones por stock bajo.
- Reportes de inventario, movimientos y stock bajo.
- Configuracion basica de empresa.
- CORS habilitado para el frontend local en Vite.

## Requisitos

- Java 21
- Maven 3.8+
- MySQL 8+
- Base de datos: `api_gestion_inventario`

La URL JDBC por defecto usa `createDatabaseIfNotExist=true`, asi que la base puede crearse automaticamente si el usuario de MySQL tiene permisos.

## Configuracion

El proyecto carga variables desde un archivo local `.env` en la raiz del backend.

Ejemplo:

```properties
DB_URL=jdbc:mysql://localhost:3306/api_gestion_inventario?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=root
DB_PASSWORD=change-me
JWT_SECRET=change-this-secret-for-a-long-private-value
JWT_EXPIRATION_MINUTES=480
CORS_ALLOWED_ORIGINS=http://127.0.0.1:5173,http://localhost:5173,http://127.0.0.1:5174,http://localhost:5174
SERVER_PORT=8080
```

El archivo `.env` no debe subirse al repositorio. Usa `.env.example` como plantilla.

## Ejecutar

```powershell
mvn.cmd spring-boot:run
```

API base:

```text
http://localhost:8080/api
```

Para compilar y ejecutar pruebas:

```powershell
mvn.cmd test
```

## Datos iniciales

Al iniciar, la aplicacion crea datos demo si no existen.

| Usuario | Password | Rol |
|---|---|---|
| `admin` | `demo123` | `admin` |
| `supervisor` | `demo123` | `supervisor` |
| `empleado` | `demo123` | `empleado` |

Tambien crea categorias y productos iniciales para probar el dashboard.

## Autenticacion

La mayoria de endpoints requieren JWT.

### Login

```http
POST /api/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "demo123"
}
```

Respuesta:

```json
{
  "token": "jwt-token",
  "user": {
    "id": 1,
    "username": "admin",
    "name": "Administrador",
    "email": "admin@demo.com",
    "role": "admin",
    "status": "Activo"
  }
}
```

Usa el token en las siguientes solicitudes:

```http
Authorization: Bearer <token>
```

### Usuario autenticado

```http
GET /api/auth/me
```

## Endpoints

### Auth

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| POST | `/api/auth/login` | Publico | Inicia sesion y devuelve JWT |
| GET | `/api/auth/me` | Autenticado | Devuelve el usuario actual |

### Productos

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/products` | Autenticado | Lista productos |
| GET | `/api/products/{id}` | Autenticado | Obtiene un producto |
| GET | `/api/products/low-stock` | Autenticado | Lista productos con stock bajo |
| POST | `/api/products` | admin, supervisor | Crea producto |
| PUT | `/api/products/{id}` | admin, supervisor | Actualiza producto |
| DELETE | `/api/products/{id}` | admin, supervisor | Marca producto como inactivo |

Body para crear/actualizar:

```json
{
  "name": "Mouse inalambrico",
  "sku": "ELE-MOU-044",
  "category": "Electronica",
  "stock": 4,
  "minStock": 10,
  "price": 85000,
  "status": "Activo"
}
```

Tambien puede enviarse `categoryId` en vez de `category`.

### Categorias

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/categories` | Autenticado | Lista categorias |
| GET | `/api/categories/{id}` | Autenticado | Obtiene una categoria |
| POST | `/api/categories` | admin, supervisor | Crea categoria |
| PUT | `/api/categories/{id}` | admin, supervisor | Actualiza categoria |
| DELETE | `/api/categories/{id}` | admin, supervisor | Marca categoria como inactiva |

Body:

```json
{
  "name": "Electronica",
  "description": "Equipos y accesorios tecnologicos",
  "status": "Activa"
}
```

Una categoria con productos activos no puede eliminarse.

### Movimientos

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/movements` | Autenticado | Lista movimientos |
| GET | `/api/movements/{id}` | Autenticado | Obtiene un movimiento |
| POST | `/api/movements` | Autenticado | Registra movimiento |

Body para entrada o salida:

```json
{
  "productId": 1,
  "type": "Salida",
  "quantity": 2,
  "note": "Entrega a ventas"
}
```

Body para ajuste:

```json
{
  "productId": 1,
  "type": "Ajuste",
  "adjustment": -3,
  "note": "Conteo fisico"
}
```

Reglas:

- No permite movimientos sobre productos inactivos.
- No permite stock negativo.
- Actualiza stock y crea movimiento en la misma transaccion.
- Registra auditoria automaticamente.
- Genera notificacion si el producto queda en stock bajo.
- El rol `empleado` solo puede registrar salidas.

### Usuarios

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/users` | admin | Lista usuarios |
| GET | `/api/users/{id}` | admin | Obtiene usuario |
| POST | `/api/users` | admin | Crea usuario |
| PUT | `/api/users/{id}` | admin | Actualiza usuario |
| PATCH | `/api/users/{id}/status` | admin | Cambia estado |

Body:

```json
{
  "username": "empleado2",
  "password": "demo123",
  "name": "Empleado Dos",
  "email": "empleado2@demo.com",
  "role": "empleado",
  "status": "Activo"
}
```

### Reportes

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/reports/inventory` | admin, supervisor | Resumen de inventario por categoria |
| GET | `/api/reports/movements` | admin, supervisor | Movimientos agrupados por tipo |
| GET | `/api/reports/low-stock` | admin, supervisor | Productos bajo stock minimo |

### Notificaciones

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/notifications` | Autenticado | Lista notificaciones |
| PATCH | `/api/notifications/{id}/read` | Autenticado | Marca una notificacion como leida |
| PATCH | `/api/notifications/read-all` | Autenticado | Marca todas como leidas |

### Auditoria

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/audit` | admin, supervisor | Lista registros de auditoria |
| GET | `/api/audit/{id}` | admin, supervisor | Obtiene un registro de auditoria |

### Configuracion de empresa

| Metodo | Endpoint | Acceso | Descripcion |
|---|---|---|---|
| GET | `/api/settings/company` | Autenticado | Obtiene configuracion de empresa |
| PUT | `/api/settings/company` | Autenticado | Actualiza configuracion de empresa |

Body:

```json
{
  "companyName": "Inventario Pro",
  "nit": "123456789",
  "email": "contacto@empresa.com",
  "phone": "3000000000",
  "address": "Bogota, Colombia"
}
```

## Ejemplos con PowerShell

Login:

```powershell
$login = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
  -Method Post `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"demo123"}'
```

Listar productos:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/products" `
  -Headers @{ Authorization = "Bearer $($login.token)" }
```

Registrar salida:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/movements" `
  -Method Post `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $($login.token)" } `
  -Body '{"productId":1,"type":"Salida","quantity":1,"note":"Prueba desde PowerShell"}'
```

## Estructura principal

```text
src/main/java/com/inventory
  application/service
  domain/model
  infrastructure/adapter/in/rest
  infrastructure/adapter/out/persistence
  infrastructure/config
src/main/resources/db/migration
```

## Base de datos

Flyway administra el esquema inicial con:

```text
src/main/resources/db/migration/V1__init_schema.sql
```

Tablas principales:

- `roles`
- `users`
- `categories`
- `products`
- `movements`
- `notifications`
- `audit_logs`
- `company_settings`
- `flyway_schema_history`

## Integracion con frontend

El frontend debe apuntar a:

```text
VITE_API_BASE_URL=http://localhost:8080/api
```

Origenes CORS permitidos por defecto:

- `http://127.0.0.1:5173`
- `http://localhost:5173`
- `http://127.0.0.1:5174`
- `http://localhost:5174`

## Notas de seguridad

- No subir `.env`.
- Cambiar `JWT_SECRET` en entornos reales.
- Cambiar las contrasenas demo antes de usar en produccion.
- Usar HTTPS fuera de desarrollo local.
