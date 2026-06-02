package com.skyWatch.msProof.Application.Services;

import com.skyWatch.msProof.Application.Ports.In.MessageListenerPort;
import com.skyWatch.msProof.Application.Ports.Out.DeliveryRepositoryPort;
import com.skyWatch.msProof.Domain.Delivery;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DeliveryService implements MessageListenerPort {

    private final DeliveryRepositoryPort repositoryPort;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String GOOGLE_SCRIPT_URL = "https://script.google.com/macros/s/AKfycbyK-J9rD3WXU3bdoJuxvlmSJGupShft9CiNcB2QcNjRgp-tpeqGjSemMpFhPvBKzkBl/exec";
    private final String IMAGES_DIRECTORY = "C:/SkyWatch/Deliveries/Images/";

    public DeliveryService(
            DeliveryRepositoryPort repositoryPort,
            ObjectMapper objectMapper,
            RestTemplate restTemplate
    ) {
        this.repositoryPort = repositoryPort;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public void savePhoto(String message) {

        try {
            // Convert JSON string → Domain object
            // Mapeo manual si los campos del JSON no coinciden exactamente con los del objeto (idRuta, idDron, foto)
            Map<String, Object> map = objectMapper.readValue(message, Map.class);

            Delivery delivery = new Delivery();
            delivery.setRouteId((Integer) map.get("idRuta"));
            delivery.setDroneId((Integer) map.get("idDron"));
            delivery.setEmail((String) map.get("email"));
            String fotoBase64 = (String) map.get("foto");

            System.out.println("Processing delivery...");
            System.out.println("RouteId: " + delivery.getRouteId());
            System.out.println("DroneId: " + delivery.getDroneId());

            // 1. Guardar imagen localmente
            String filePath = saveImageLocally(fotoBase64, delivery.getRouteId(), delivery.getDroneId());
            delivery.setImage(filePath);

            // 2. Enviar correo via Google Script API
            sendEmail(delivery.getEmail(), fotoBase64);

            // 3. Call output port (persistence)
            boolean saved = repositoryPort.save(delivery);

            if (saved) {
                System.out.println("Delivery processed successfully");
            } else {
                System.out.println("Failed to process delivery");
            }

        } catch (Exception e) {
            System.out.println("Error processing message");
            e.printStackTrace();
        }
    }

    private String saveImageLocally(String base64Image, int routeId, int droneId) throws Exception {
        Path path = Paths.get(IMAGES_DIRECTORY);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        String fileName = "delivery_" + routeId + "_" + droneId + "_" + UUID.randomUUID().toString() + ".png";
        File file = new File(IMAGES_DIRECTORY + fileName);
        
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }

        return file.getAbsolutePath();
    }

    private void sendEmail(String email, String base64Image) {
        try {
            Map<String, String> payload = new HashMap<>();
            payload.put("email", email);
            payload.put("image", base64Image);
            payload.put("fileName", "prueba_entrega.png");

            restTemplate.postForObject(GOOGLE_SCRIPT_URL, payload, String.class);
            System.out.println("Email request sent successfully to " + email);
        } catch (Exception e) {
            System.out.println("Failed to send email request: " + e.getMessage());
        }
    }
}
