# MsProofSkyWatch - Microservicio de Pruebas de Entrega

Este microservicio se encarga de procesar las pruebas de entrega de paquetes realizadas por drones. Escucha mensajes de una cola de RabbitMQ, guarda las imágenes localmente, envía notificaciones por correo electrónico y persiste la información en una base de datos MongoDB.

## 🚀 Tecnologías
- **Java 17**
- **Spring Boot 3**
- **RabbitMQ** (Mensajería asíncrona)
- **MongoDB** (Persistencia)
- **Google Script API** (Envío de correos)

## 📁 Estructura de Carpetas
El proyecto sigue una arquitectura hexagonal (Ports & Adapters):

```text
src/main/java/com/skyWatch/msProof/
├── Application/
│   ├── Ports/
│   │   ├── In/          # Interfaces de entrada (MessageListenerPort)
│   │   └── Out/         # Interfaces de salida (DeliveryRepositoryPort)
│   └── Services/        # Lógica de negocio (DeliveryService)
├── Domain/              # Modelos de dominio (Delivery)
├── Infrastructure/
│   ├── Adapters/
│   │   ├── In/          # Consumidor de RabbitMQ (RabbitMQConsumerAdapter)
│   │   └── Out/         # Implementación de persistencia MongoDB
│   └── Configuration/   # Configuración de Beans
└── MsProofApplication.java
```

## 🛠️ Funcionamiento y Endpoint
Este microservicio **no expone endpoints REST** tradicionales para su lógica principal. Funciona como un **consumidor de eventos**.

### Interacción con RabbitMQ
- **Cola**: `fotos.queue`
- **Formato esperado**: JSON

### Ejemplo de Mensaje (Carga útil)
Para probar el sistema, se debe enviar un mensaje a la cola `fotos.queue` con la siguiente estructura:

```json
{
  "idRuta": 101,
  "idDron": 5,
  "email": "usuario@ejemplo.com",
  "foto": "iVBORw0KGgoAAAANSUhEUGAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg=="
}
```
*(La foto debe estar codificada en Base64)*.

## 🧪 Cómo probarlo con Postman
Aunque es un consumidor de RabbitMQ, si tienes habilitado el plugin `rabbitmq_management`, puedes usar la API de RabbitMQ para simular la llegada de un mensaje desde Postman:

1. **Método**: `POST`
2. **URL**: `http://localhost:15672/api/exchanges/%2f/amq.default/publish`
3. **Autenticación**: Basic Auth (Usuario: `guest`, Contraseña: `guest`)
4. **Cuerpo (JSON)**:
```json
{
  "properties": {},
  "routing_key": "fotos.queue",
  "payload": "{\"idRuta\": 101, \"idDron\": 5, \"email\": \"tu-correo@gmail.com\", \"foto\": \"iVBORw0KGgoAAAANSUhEUGAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==\"}",
  "payload_encoding": "string"
}
```

## ⚙️ Configuración Relevante (`application.properties`)
- **Puerto del Servidor**: `8081`
- **RabbitMQ**: `localhost:5672`
- **MongoDB**: `mongodb://localhost:27017/deliveriesdb`
- **Directorio de Imágenes**: `C:/SkyWatch/Deliveries/Images/` (Asegúrate de tener permisos de escritura).

## 📝 Notas Adicionales
- El servicio decodifica el Base64 y genera un archivo `.png` con un nombre único: `delivery_{routeId}_{droneId}_{UUID}.png`.
- Se realiza una petición POST a una URL de Google Script para gestionar el envío del correo electrónico con la imagen adjunta.
