package com.SkyWatch.SkyWatchFrontEnd.Model;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "users" en la base de datos.
 * Esta clase NO es de Spring Security, es simplemente tu modelo de datos.
 */
@Entity
@Table(name = "users")
public class User {

    /**
     * Identificador único del usuario (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Contraseña del usuario (debe almacenarse encriptada con BCrypt).
     */
    @Column(nullable = false)
    private String password;

    /**
     * Rol del usuario (ej: ROLE_USER, ROLE_ADMIN).
     * Spring Security trabaja con este formato.
     */
    @Column(nullable = false)
    private String role;

    // ===== GETTERS Y SETTERS =====

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}