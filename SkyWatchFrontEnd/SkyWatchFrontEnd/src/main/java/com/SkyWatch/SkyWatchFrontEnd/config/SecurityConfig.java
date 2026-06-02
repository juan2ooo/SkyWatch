package com.SkyWatch.SkyWatchFrontEnd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * Clase de configuración de seguridad.
 * Aquí defines quién puede acceder a qué rutas y cómo funciona el login.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    /**
     * Configuración principal del filtro de seguridad.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /**
                 * Configuración de autorización:
                 * qué rutas son públicas y cuáles requieren autenticación.
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/api/cola/delete").permitAll() // Permitir acceso a la API
                        .requestMatchers("/home/**").hasRole("ADMIN")
                        .requestMatchers("/dron/**").hasRole("DRON")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/cola/delete") // Desactivar CSRF para esta ruta
                )

                /**
                 * Configuración del login con formulario.
                 */
                .formLogin(form -> form
                        .loginPage("/login") // página personalizada
                        .successHandler(successHandler)
                        .permitAll()
                )

                /**
                 * Configuración del logout.
                 */
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }

    /**
     * Bean para encriptar contraseñas usando BCrypt.
     * Es obligatorio para que Spring Security funcione correctamente.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}