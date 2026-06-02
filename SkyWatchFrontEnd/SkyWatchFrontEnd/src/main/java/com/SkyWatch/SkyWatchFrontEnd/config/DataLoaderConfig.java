package com.SkyWatch.SkyWatchFrontEnd.config;

import com.SkyWatch.SkyWatchFrontEnd.Repository.UserRepository;
import com.SkyWatch.SkyWatchFrontEnd.Model.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase independiente con metodo main.
 * Sirve para ejecutar código manualmente (ej: insertar usuarios).
 *
 * ⚠️ IMPORTANTE:
 * Aunque es "otro main", sigue levantando el contexto de Spring.
 */
@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Validamos si ya existe para no duplicarlo cada vez que enciendas la app
            if (userRepository.findByUsername("admin").isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User user = new User();
                user.setUsername("admin");
                user.setPassword(encoder.encode("1234"));
                user.setRole("ROLE_ADMIN");

                userRepository.save(user);
                System.out.println("✅ Usuario administrador inicial creado correctamente");
            } else {
                System.out.println("ℹ️ El usuario admin ya existe, saltando creación.");
            }

            if (userRepository.findByUsername("dron").isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User user = new User();
                user.setUsername("dron");
                user.setPassword(encoder.encode("1234"));
                user.setRole("ROLE_DRON");

                userRepository.save(user);
                System.out.println("✅ Usuario dron inicial creado correctamente");
            } else {
                System.out.println("ℹ️ El usuario dron ya existe, saltando creación.");
            }
        };
    }
}
