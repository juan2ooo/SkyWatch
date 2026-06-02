package com.SkyWatch.SkyWatchFrontEnd.Repository;

import com.SkyWatch.SkyWatchFrontEnd.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio JPA para acceder a la tabla de usuarios.
 * Spring Data JPA implementa automáticamente esta interfaz.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Método personalizado para buscar un usuario por username.
     * Spring genera la query automáticamente.
     */
    Optional<User> findByUsername(String username);
}
