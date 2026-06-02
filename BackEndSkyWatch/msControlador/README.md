# Documentación del Proyecto: msControlador

Este microservicio forma parte del sistema SkyWatch y se encarga de la gestión y control de rutas.

## Información General

- **Puerto del Servidor:** `8080` (Configurado en `application.properties`).
- **Tecnologías Principales:** Java, Spring Boot, Spring Data JPA, RabbitMQ, PostgreSQL.

## Estructura del Proyecto

El proyecto sigue una **Arquitectura Hexagonal (Puertos y Adaptadores)**, organizada de la siguiente manera:

- `src/main/java/com/SkyDeliver/msControlador/`
    - `Domain/`: Contiene las entidades de negocio (`Route`, `Point`, `NoFlyZone`) y excepciones de dominio. Es el núcleo de la lógica sin dependencias externas.
    - `Application/`:
        - `Ports/`: Define las interfaces de entrada (`In`) y salida (`Out`).
        - `Service/`: Implementación de los casos de uso (`RouteUseCase`).
        - `Dto/`: Objetos de transferencia de datos para las peticiones y respuestas.
    - `Infrastructure/`:
        - `Adapters/`: 
            - `In/`: Controladores REST y consumidores de mensajería (`RouteControllerEndPoints`).
            - `Out/`: Implementaciones de persistencia (JPA) y clientes de APIs externas.
        - `Configuration/`: Configuraciones de seguridad, RabbitMQ y beans de la aplicación.
        - `Mappers/`: Conversiones entre entidades de dominio y entidades de persistencia/DTOs.

## Endpoints API REST

La ruta base para los endpoints es `/apiRutas/rutas`.

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **POST** | `/apiRutas/rutas` | Crea una nueva ruta. Requiere un JSON con `lon1`, `lat1`, `lon2`, `lat2`. |
| **GET** | `/apiRutas/rutas` | Obtiene una lista de todas las rutas registradas. |
| **GET** | `/apiRutas/rutas/{id}` | Obtiene los detalles de una ruta específica por su ID. |

## Ejemplos de Uso (Postman / cURL)

### 1. Crear una nueva ruta
**Método:** `POST`  
**URL:** `http://localhost:8080/apiRutas/rutas`  
**Cuerpo (JSON):**
```json
{
  "lon1": -74.006,
  "lat1": 40.7128,
  "lon2": -73.9352,
  "lat2": 40.7306
}
```

### 2. Obtener todas las rutas
**Método:** `GET`  
**URL:** `http://localhost:8080/apiRutas/rutas`

### 3. Obtener una ruta por ID
**Método:** `GET`  
**URL:** `http://localhost:8080/apiRutas/rutas/1` (Reemplazar `1` por un ID válido)

---

## Mensajería (RabbitMQ)

El microservicio escucha mensajes de la cola `cola.delete` para procesar la eliminación de rutas de forma asíncrona.

- **Consumidor:** `RouteControllerEndPoints#consumir(DeleteMessage)`

## Configuración de Infraestructura

- **Base de Datos:** PostgreSQL (puerto `5433` por defecto o configurado por variable de entorno).
- **Broker de Mensajería:** RabbitMQ (host `rabbitmq` o configurable).
- **Actuator:** Endpoints de monitoreo expuestos, incluyendo soporte para Prometheus.
