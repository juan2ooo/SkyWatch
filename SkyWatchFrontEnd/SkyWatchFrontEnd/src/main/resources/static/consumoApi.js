

const map = L.map('map').setView([6.2442, -75.5812], 13);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

let pointA = null;
let pointB = null;
let markerA = null;
let markerB = null;

// --- CONFIGURACIÓN DEL CACHÉ ---
const CACHE_KEY_ZONAS = 'noFlyZones_Medellin';
const CACHE_KEY_TIEMPO = 'noFlyZones_Timestamp';
const CACHE_TTL = 24 * 60 * 60 * 1000; // Tiempo de vida del caché en milisegundos (24 horas)

const overpassQuery = `
  [out:json][timeout:25];
  area["name"="Medellín"]->.searchArea;
  (
    way["aeroway"="aerodrome"](area.searchArea);
    way["amenity"="hospital"](area.searchArea);
    way["landuse"="military"](area.searchArea);
    way["amenity"="prison"](area.searchArea);
    way["aeroway"="helipad"](area.searchArea);
    relation["aeroway"="aerodrome"](area.searchArea);
    relation["amenity"="hospital"](area.searchArea);
    relation["landuse"="military"](area.searchArea);
    relation["amenity"="prison"](area.searchArea);
    relation["aeroway"="helipad"](area.searchArea);
  );
  out geom;
`;

// Función principal optimizada con estrategia de caché
async function cargarNoFlyZones() {
  const ahora = Date.now();
  const datosCacheados = localStorage.getItem(CACHE_KEY_ZONAS);
  const timestampCache = localStorage.getItem(CACHE_KEY_TIEMPO);

  // 1. Validar si el caché existe y aún está vigente
  if (datosCacheados && timestampCache && (ahora - timestampCache < CACHE_TTL)) {
    console.log('Cargando No Fly Zones desde el caché local (LocalStorage)...');
    dibujarZonasRestringidas(JSON.parse(datosCacheados));
    return; // Detenemos la ejecución aquí, evitamos la petición HTTP
  }

  // 2. Si no hay caché o ya expiró, consultamos la API
  console.log('El caché no existe o ha expirado. Consultando Overpass API...');
  const url = 'https://overpass-api.de/api/interpreter';

  try {
    const response = await fetch(url, {
      method: 'POST',
      body: new URLSearchParams({ data: overpassQuery })
    });

    if (!response.ok) {
      throw new Error(`Error en la solicitud a Overpass: ${response.statusText}`);
    }

    const data = await response.json();

    // 3. Guardar el resultado en caché para las próximas visitas
    localStorage.setItem(CACHE_KEY_ZONAS, JSON.stringify(data.elements));
    localStorage.setItem(CACHE_KEY_TIEMPO, ahora.toString());

    // 4. Pintar en el mapa
    dibujarZonasRestringidas(data.elements);

  } catch (error) {
    console.error('Hubo un problema al cargar las No Fly Zones:', error);

    // Estrategia de respaldo: Si la API falla pero tenemos caché viejo, lo usamos en vez de dejar el mapa vacío
    if (datosCacheados) {
      console.warn('Usando datos de caché expirados debido a un fallo en la red.');
      dibujarZonasRestringidas(JSON.parse(datosCacheados));
    }
  }
}

// Función para procesar los elementos devueltos y dibujarlos en Leaflet
function dibujarZonasRestringidas(elementos) {
  elementos.forEach(elemento => {
    if (elemento.geometry && elemento.geometry.length > 0) {
      const coordenadas = elemento.geometry.map(punto => [punto.lat, punto.lon]);

      const estiloNoFly = {
        color: '#ff3333',
        weight: 2,
        fillColor: '#ff3333',
        fillOpacity: 0.35
      };

      const poligono = L.polygon(coordenadas, estiloNoFly).addTo(map);

      let tipoZona = elemento.tags.amenity || elemento.tags.landuse || elemento.tags.aeroway || 'Zona Restringida';
      let nombreZona = elemento.tags.name || 'Sin Nombre';

      poligono.bindPopup(`<strong>NO FLY ZONE</strong><br>Tipo: ${tipoZona}<br>Nombre: ${nombreZona}`);
    }
  });
}

// Inicializar la carga
cargarNoFlyZones();








map.on('click', function(e) {

  const lat = e.latlng.lat;
  const lon = e.latlng.lng;

  if (!pointA) {
    pointA = { lat, lon };

    markerA = L.marker([lat, lon])
      .addTo(map)
      .bindPopup("Punto A")
      .openPopup();

    document.getElementById("result").innerHTML =
      `Punto A: ${lat.toFixed(6)}, ${lon.toFixed(6)}`;

    return;
  }

  if (!pointB) {
    pointB = { lat, lon };

    markerB = L.marker([lat, lon])
      .addTo(map)
      .bindPopup("Punto B")
      .openPopup();

    document.getElementById("result").innerHTML +=
      `<br>Punto B: ${lat.toFixed(6)}, ${lon.toFixed(6)}`;
  }
});



function getColorViento(velocidad) {
  if (velocidad >= 50) return '#08519c';
  if (velocidad >= 30) return '#3182bd';
  if (velocidad >= 15) return '#6baed6';
  return '#bdd7e7';
}

async function validateFlight() {

  if (!pointA || !pointB) {
    alert("Debes seleccionar 2 puntos primero");
    return;
  }

  const body = {
    lon1: pointA.lon,
    lat1: pointA.lat,
    lon2: pointB.lon,
    lat2: pointB.lat
  };

  console.log("Body enviado:", body);

  const resultDiv = document.getElementById("result");
  resultDiv.innerHTML += "<br>Enviando datos a la API...";

  try {
    const response = await fetch("http://localhost:8080/apiRutas/rutas", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(body)
    });

    const data = await response.json();
    console.log("Respuesta API completa:", data);

    const tiempoData = data.tiempo;
    console.log("Contenido de tiempoData:", tiempoData);


    if (tiempoData) {
      const authStatus = tiempoData.autorizacion.toUpperCase();
      const isApproved = authStatus === "A";
      resultDiv.innerHTML += `<br>Estado del Vuelo: <b>${isApproved ? 'AUTORIZADO (A)' : 'NO AUTORIZADO (NA)'}</b>`;

      const fila = [
          data.id,
          data.status,
          data.start_lon,
          data.start_lat,
          data.end_lon,
          data.end_lat
      ];
      console.log("fila a insertar: ",fila)

      const tabla = $('#tablaRutas').DataTable()

      tabla.row.add(fila).draw();








      let primerPunto = true;

      // Iterar sobre las claves del diccionario
      for (const key in tiempoData) {
        if (key === "autorizacion") continue;

        const punto = tiempoData[key];

        // Extraemos las propiedades
        const lat = parseFloat(punto.coordenada.lon);
        const lng = parseFloat(punto.coordenada.lat);
        const velocidad = punto["valores_consultados"].velocidad;
        const unidad = punto["valores_consultados"].unidad;

        // Romper el flujo con un log si las coordenadas vienen mal de la API
        if (isNaN(lat) || isNaN(lng)) {
          console.warn(`⚠️ El ${key} no tiene coordenadas válidas:`, punto.coordenada);
          continue;
        }

        console.log(`📌 Pintando ${key} en coordenadas: [${lat}, ${lng}] con viento de ${velocidad} ${unidad}`);

        // Mover la cámara del mapa al primer punto válido encontrado para poder verlo
        if (primerPunto) {
          map.setView([lat, lng], 12);
          primerPunto = false;
        }

        // Dibujar en el mapa
        L.circleMarker([lat, lng], {
          radius: 12, // Lo hacemos un poco más grande para que sea bien visible
          fillColor: getColorViento(velocidad),
          color: "#000",
          weight: 1.5,
          opacity: 1,
          fillOpacity: 0.8
        }).addTo(map)
          .bindPopup(`<b>${key.toUpperCase()}</b><br>Viento: ${velocidad} ${unidad}`);
      }




    } else {
      resultDiv.innerHTML += `<br>Error: La estructura de respuesta no contiene 'tiempo'.`;
    }

  } catch (error) {
    // IMPORTANTE: Dejamos el error detallado aquí para saber exactamente qué falló en el parsing
    console.error("Error detallado en la ejecución:", error);
    document.getElementById("result").innerHTML += "<br>Error procesando datos o conectando con la API";
  }
}



    $(document).ready(function () {
        $('#tablaRutas').DataTable({
            pageLength: 10,
            language: {
                url: '//cdn.datatables.net/plug-ins/1.13.8/i18n/es-ES.json'
            }
        });
    });