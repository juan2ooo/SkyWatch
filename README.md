# 🌐 SkyWatch

Plataforma de microservicios para gestión de rutas de drones, validación meteorológica y procesamiento de entregas.

---

## ▶️ Cómo ejecutar

1. Descargar/clonar el proyecto

2. Dirigirse a la carpeta: BackEndSkyWatch

3. Montar imagenes y contenedor para el *Backend*, para lo anterior ejecutar:
   - docker compose up -d --build

4. Dirigase a la carpeta: SkyWatchFrontEnd/SkyWatchFrontEnd

5. Montar imagenes y contenedor para el *Frontend*, para lo anterior ejecutar:
   - docker compose up -d --build

6. Ingresar a: http://localhost:8085/login

7. Ingresar:
  - Para esto hay dos usuarios con dos roles distintos:
    - usr: admin
    - usr: dron
    - password para ambos: 1234

  admin: permite creaar rutas y consultarlas
  dron: permite hacer enviar la prueba de entrega y con esta elimina la ruta

  En la documentacion se detalla el paso a paso del flujo de la aplicacion 

---

## 🚀 Servicios y Puertos

| Servicio | Puerto |
|----------|--------|
| msControlador | 8080 |
| delivery-ms | 8081 |
| api-clima | 8083 |
| PostgreSQL | 5433 |
| MongoDB | 27017 |
| RabbitMQ | 5672 / 15672 |
| Prometheus | 9090 |
| Grafana | 3000 |
| FrontEnd | 8085 |

---

## 📦 Microservicios

### 1. msControlador (Rutas)
- CRUD de rutas
- Base URL: /apiRutas/rutas

#### Ejemplos

*Crear ruta*
json
POST http://localhost:8080/apiRutas/rutas

{
  "lon1": -74.006,
  "lat1": 40.7128,
  "lon2": -73.9352,
  "lat2": 40.7306
}

*Listar rutas*

GET http://localhost:8080/apiRutas/rutas

*Obtener por ID*

GET http://localhost:8080/apiRutas/rutas/1

---

### 2. api-clima (Meteorología)
- Valida condiciones de viento
- Regla: viento > 50 km/h → NA

#### Ejemplo
json
POST http://localhost:8083/apiRutas/meteorologico

{
  "punto_origen": {"lat": 6.2442, "lon": -75.5812},
  "punto_retorno": {"lat": 6.2518, "lon": -75.5636}
}

---

### 3. delivery-ms (Entregas)
- Consume eventos desde RabbitMQ
- Guarda imágenes y envía correo

#### Ejemplo de mensaje
json
{
  "idRuta": 101,
  "idDron": 5,
  "email": "usuario@ejemplo.com",
  "foto": "BASE64..."
}

#### Prueba con RabbitMQ
json
POST http://localhost:15672/api/exchanges/%2f/amq.default/publish

{
  "properties": {},
  "routing_key": "fotos.queue",
  "payload": "{\"idRuta\":101,\"idDron\":5,\"email\":\"tu-correo@gmail.com\",\"foto\":\"BASE64...\"}",
  "payload_encoding": "string"
}

---

## 🧪 Pruebas

- Postman para APIs
- RabbitMQ UI → http://localhost:15672

---

## 📌 Notas

- Arquitectura basada en microservicios
- Comunicación asíncrona con RabbitMQ
- Monitoreo con Prometheus y Grafana
