package com.SkyWatch.SkyWatchFrontEnd.Controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Controlador MVC para manejar las rutas de login y home.
 */
@Controller
public class AuthController {


    private final RabbitTemplate rabbitTemplate;

    public AuthController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Muestra la página de login.
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // devuelve login.html
    }

    /**
     * Página para usuarios con rol dron.
     */
    @GetMapping("/dron")
    public String dron() {
        return "dron"; // devuelve dron.html
    }

    /**
     * Página principal después de autenticarse.
     */
    @GetMapping("/home")
    public String home(Model model) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<RutaDTO[]> response = restTemplate.getForEntity(
                    "http://mscontrolador:8080/apiRutas/rutas",
                    RutaDTO[].class
            );

            List<RutaDTO> rutas = Arrays.asList(response.getBody());

            model.addAttribute("rutas", rutas);
            return "home";
        } catch (RuntimeException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }




    }

    @PostMapping("/api/cola/delete")
    public ResponseEntity<Void> enviar(@RequestBody Map<String, Object> mensaje) {

        rabbitTemplate.convertAndSend(
                "cola.delete",
                mensaje
        );

        return ResponseEntity.ok().build();
    }


}
