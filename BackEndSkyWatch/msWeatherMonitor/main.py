from fastapi import FastAPI
from pydantic import BaseModel
from typing import Dict
import requests
import uvicorn
from prometheus_fastapi_instrumentator import Instrumentator  # Importa la librería que expone métricas de FastAPI en formato Prometheus

app = FastAPI()

# Instrumenta la app para recolectar métricas HTTP automáticamente y las expone en GET /metrics
Instrumentator().instrument(app).expose(app)

API_KEY = "AIzaSyBJ4GbY0ZLCZCxrwL27CU0_4aizGiyOc0U"

class Coordenadas(BaseModel):
    lat: float
    lon: float

@app.post("/apiRutas/meteorologico")
def obtener_clima_rutas(puntos: Dict[str, Coordenadas]):
    respuesta_final = {}
    autorizacion = "A"
    noAuth = True

    for nombre_punto, coords in puntos.items():
        url_google_api = f"https://weather.googleapis.com/v1/currentConditions:lookup?key={API_KEY}&location.latitude={coords.lat}&location.longitude={coords.lon}"
        
        try:
            response = requests.get(url_google_api)
            response.raise_for_status()
            data = response.json()

            viento_data = data.get("wind", {})
            speed_data = viento_data.get("speed", {})
            velocidad = speed_data.get("value", 0)
            unidad = speed_data.get("unit", "KILOMETERS_PER_HOUR")

            if velocidad > 50 and noAuth:
                autorizacion = "NA"
                noAuth = False

            respuesta_final[nombre_punto] = {
                "coordenada": {
                    "lon": coords.lon,
                    "lat": coords.lat
                },
                "valores_consultados": {
                    "velocidad": velocidad,
                    "unidad": unidad
                },
            }

        except requests.exceptions.RequestException as e:
            respuesta_final[nombre_punto] = {
                "error": "No se pudo consultar el clima para este punto",
                "detalle": str(e)
            }

    respuesta_final["autorizacion"] = autorizacion
    return respuesta_final

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8083)