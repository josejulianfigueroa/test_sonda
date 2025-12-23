API de Comparación de Productos
API REST desarrollada con Spring Boot 3.2.5 para gestionar productos electrónicos y sus especificaciones. Incluye documentación automática con Swagger UI y datos de ejemplo precargados.

🏠 Acceso a la API
Endpoint	Descripción	Método
http://localhost:8080/api/	Página de bienvenida	GET
http://localhost:8080/swagger-ui.html	Swagger UI - Documentación interactiva	GET
http://localhost:8080/v3/api-docs	Esquema OpenAPI JSON	GET
📦 Datos de Ejemplo
La clase DataInitializer carga 10 productos al iniciar la aplicación (solo si la base de datos está vacía):

java
// Ejemplos precargados:
- Smartphone X1 (299.99) - 4GB RAM, 128GB
- Laptop Pro 14 (1299.00) - i7, 16GB RAM
- Auriculares BT (149.90) - ANC, 30h batería
- Smartwatch Sport (199.90) - GPS, 5ATM
  // ... + 6 productos más
  Nota: Los datos se persisten en H2 (base de datos en memoria por defecto).

🔗 Endpoints Disponibles
Método	Endpoint	Descripción	Código Esperado
POST	/api/model	Crear nuevo producto
Body: JSON con name, price, etc.	201 Created
GET	/api/model	Listar todos los productos
Retorna array de 10+ items	200 OK
GET	/api/model/{id}	Obtener producto por ID
Ej: /api/model/1	200 OK
DELETE	/api/model/{id}	Eliminar producto por ID
Ej: /api/model/5	204 No Content
DELETE	/api/erase	Eliminar TODOS los productos
¡⚠️ Destruye datos de ejemplo!	204 No Content

Ejemplo Request/Response - POST /api/model
Request:

json
{
"name": "iPhone 16",
"imageUrl": "https://example.com/iphone16.png",
"description": "Smartphone gama alta",
"price": 1299.99,
"rating": 4.8,
"specifications": "256GB, 8GB RAM, OLED"
}
Response (201):

json
{
"id": 44,
"name": "iPhone 16",
"imageUrl": "https://example.com/iphone16.png",
"description": "Smartphone gama alta",
"price": 1299.99,
"rating": 4.8,
"specifications": "256GB, 8GB RAM, OLED"
}
🛡️ Manejo de Excepciones
Error	Código	Descripción
400 Bad Request	Validación JSON inválido (ej: price negativo)
404 Not Found	Producto no existe (DELETE/GET por ID inválido)
500 Internal Server Error	Error interno del servidor
Swagger UI muestra esquemas completos de Model y manejo de errores en cada endpoint.

🚀 Configuración OpenAPI
Clase OpenApiConfig define metadatos:

java
@OpenAPIDefinition(
info = @Info(
title = "Item Comparison API",
version = "1.0.0",
description = "Simplified backend API...",
contact = @Contact(name = "Hackerrank Sample", email = "support@example.com")
)
)
🛠️ Tecnologías
Spring Boot 3.2.5 + Java 21

SpringDoc OpenAPI (Swagger UI integrado)

Spring Data JPA + H2 Database (en memoria)

Lombok (builder pattern)

Validaciones (@Valid en requests)

📝 Uso Rápido
Ejecutar: mvn spring-boot:run

Abrir Swagger: http://localhost:8080/swagger-ui.html

Probar endpoints directamente en la interfaz

Ver datos: GET /api/model

Limpiar: DELETE /api/erase

¡La documentación interactiva está lista en Swagger UI! 🎉

pasame el archivo README PARA DESCARGAR
1 step completed
text
# API de Comparación de Productos

API REST desarrollada con **Spring Boot 3.2.5** para gestionar productos electrónicos y sus especificaciones. Incluye documentación automática con **Swagger UI** y datos de ejemplo precargados.

## 🏠 Acceso a la API

| Endpoint | Descripción | Método |
|----------|-------------|--------|
| `http://localhost:8080/api/` | Página de bienvenida | `GET` |
| `http://localhost:8080/swagger-ui.html` | **Swagger UI** - Documentación interactiva | `GET` |
| `http://localhost:8080/v3/api-docs` | Esquema OpenAPI JSON | `GET` |

## 📦 Datos de Ejemplo

La clase `DataInitializer` carga **10 productos** al iniciar la aplicación (solo si la base de datos está vacía):

// Ejemplos precargados:

Smartphone X1 (299.99) - 4GB RAM, 128GB

Laptop Pro 14 (1299.00) - i7, 16GB RAM

Auriculares BT (149.90) - ANC, 30h batería

Smartwatch Sport (199.90) - GPS, 5ATM
// ... + 6 productos más

text

**Nota**: Los datos se persisten en **H2** (base de datos en memoria por defecto).

## 🔗 Endpoints Disponibles

| Método | Endpoint | Descripción | Código Esperado |
|--------|----------|-------------|-----------------|
| `POST` | `/api/model` | **Crear nuevo producto**<br>Body: JSON con `name`, `price`, etc. | `201 Created` |
| `GET` | `/api/model` | **Listar todos los productos**<br>Retorna array de 10+ items | `200 OK` |
| `GET` | `/api/model/{id}` | **Obtener producto por ID**<br>Ej: `/api/model/1` | `200 OK` |
| `DELETE` | `/api/model/{id}` | **Eliminar producto por ID**<br>Ej: `/api/model/5` | `204 No Content` |
| `DELETE` | `/api/erase` | **Eliminar TODOS los productos**<br>¡⚠️ Destruye datos de ejemplo! | `204 No Content` |

### Ejemplo Request/Response - POST /api/model

**Request:**
{
"name": "iPhone 16",
"imageUrl": "https://example.com/iphone16.png",
"description": "Smartphone gama alta",
"price": 1299.99,
"rating": 4.8,
"specifications": "256GB, 8GB RAM, OLED"
}

text

**Response (201):**
{
"id": 44,
"name": "iPhone 16",
"imageUrl": "https://example.com/iphone16.png",
"description": "Smartphone gama alta",
"price": 1299.99,
"rating": 4.8,
"specifications": "256GB, 8GB RAM, OLED"
}

text

## 🛡️ Manejo de Excepciones

| Error | Código | Descripción |
|-------|--------|-------------|
| `400 Bad Request` | Validación JSON inválido (ej: `price` negativo) |
| `404 Not Found` | Producto no existe (`DELETE/GET` por ID inválido) |
| `500 Internal Server Error` | Error interno del servidor |

**Swagger UI** muestra esquemas completos de `Model` y manejo de errores en cada endpoint.

## 🚀 Configuración OpenAPI

Clase `OpenApiConfig` define metadatos:

@OpenAPIDefinition(
info = @Info(
title = "Item Comparison API",
version = "1.0.0",
description = "Simplified backend API...",
contact = @Contact(name = "Hackerrank Sample", email = "support@example.com")
)
)

text

## 🛠️ Tecnologías

- **Spring Boot 3.2.5** + **Java 21**
- **SpringDoc OpenAPI** (Swagger UI integrado)
- **Spring Data JPA** + **H2 Database** (en memoria)
- **Lombok** (builder pattern)
- **Validaciones** (`@Valid` en requests)

## 📝 Uso Rápido

1. **Ejecutar**: `mvn spring-boot:run`
2. **Abrir Swagger**: `http://localhost:8080/swagger-ui.html`
3. **Probar endpoints** directamente en la interfaz
4. **Ver datos**: `GET /api/model`
5. **Limpiar**: `DELETE /api/erase`

¡La documentación interactiva está lista en Swagger UI! 🎉