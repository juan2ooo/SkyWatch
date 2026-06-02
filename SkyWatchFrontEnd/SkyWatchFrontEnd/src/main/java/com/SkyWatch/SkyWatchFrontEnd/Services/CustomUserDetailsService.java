package com.SkyWatch.SkyWatchFrontEnd.Services;

import com.SkyWatch.SkyWatchFrontEnd.Repository.UserRepository;
import com.SkyWatch.SkyWatchFrontEnd.Model.User;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/**
 * Servicio que conecta Spring Security con la base de datos.
 *
 * Implementa UserDetailsService, que es una interfaz de Spring Security.
 * Su función es decirle a Spring cómo obtener un usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Inyección de dependencia del repositorio.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Este metodo es llamado automáticamente por Spring Security
     * cuando un usuario intenta iniciar sesión.
     *
     * @param username nombre de usuario ingresado en el login
     * @return UserDetails objeto que Spring usa internamente
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // Buscar el usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario no encontrado"));

        /**
         * Convertimos nuestra entidad User a un objeto UserDetails.
         * Spring Security necesita este formato.
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // contraseña encriptada
                .roles(user.getRole().replace("ROLE_", ""))
                // Spring automáticamente agrega "ROLE_"
                .build();
    }
}
