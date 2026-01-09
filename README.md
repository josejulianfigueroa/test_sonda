# API de Búsqueda de Operacion

API REST desarrollada con **Spring Boot 3.2.5** para Servicio que implementa la búsqueda de un numero de registro y devuelve su operación asociada.

## 🏠 Acceso a la API

| Endpoint | Descripción | Método |
|----------|-------------|--------|
| `http://localhost:8080/swagger-ui.html` | **Swagger UI** - Documentación interactiva | `GET` |
| `http://localhost:8080/v3/api-docs` | Esquema OpenAPI JSON | `GET` |


## 🔗 Endpoints Disponibles

| Método | Endpoint | Descripción | Código Esperado |
|--------|----------|-------------|-----------------|

| `GET` | `api/operacion?numeroRegistro=228160` | **Obtener operacion por numeroRegistro**<br>Ej: `/api/operacion?numeroRegistro=228160` | `200 OK`        |


### Ejemplo Request/Response - GET /api/operacion

**Request:**

http://localhost:8080/api/operacion?numeroRegistro=228160


**Response (200):**
{
"numeroRegistro": 228160,
"operacion": 2091
}


## 🛡️ Manejo de Excepciones

| Error | Código | Descripción |
|-------|--------|-------------|
| `400 Bad Request` | TablasReferenciasException |
| `500 Internal Server Error` | Error interno del servidor |

**Swagger UI** muestra esquemas completos de `Operacion` y manejo de errores en cada endpoint.

## 🚀 Configuración OpenAPI

Clase `OpenApiConfig` define metadatos:

@OpenAPIDefinition(
info = @Info(
title = "Búsqueda de Operacion API",
version = "1.0.0",
description = "Servicio que implementa la búsqueda de un numero de registro y devuelve su operación asociada",
contact = @Contact(name = "Sonda Test", email = "support@example.com")
)
)

## 🛠️ Tecnologías

- **Spring Boot 3.2.5** + **Java 21**
- **SpringDoc OpenAPI** (Swagger UI integrado)
- **Lombok** (builder pattern)

## 📝 Uso Rápido

1. **Ejecutar**: `mvn spring-boot:run`
2. **Abrir Swagger**: `http://localhost:8080/swagger-ui.html`
3. **Probar endpoints** directamente en la interfaz Swagger UI.