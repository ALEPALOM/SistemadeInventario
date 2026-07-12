package com.inventario.dao;

import com.inventario.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.sql.*;

public class UsuarioDAO {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

    // Método para registrar usuario adaptado a las columnas reales de la base de datos
    public void registrarUsuario(String nombreCompleto, String username, String password, int idRol) {
        // Validaciones con Guava
        Preconditions.checkArgument(!Strings.isNullOrEmpty(nombreCompleto), "El nombre completo no puede estar vacío");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "El nombre de usuario no puede estar vacío");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "La contraseña no puede estar vacía");
        Preconditions.checkArgument(idRol > 0, "Debe especificar un rol válido");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        // Ajustado a las columnas: nombre_completo, usuario, contrasena_hash, id_rol
        String sql = "INSERT INTO usuarios (nombre_completo, usuario, contrasena_hash, id_rol, estado) VALUES (?, ?, ?, ?, 'Activo')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreCompleto);
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setInt(4, idRol);
            stmt.executeUpdate();
            
            logger.info("Usuario registrado exitosamente en el sistema: {}", username);
            
        } catch (SQLException e) {
            logger.error("Error al registrar el usuario en SQL Server: {}", username, e);
            throw new RuntimeException("Error al registrar usuario en la base de datos", e);
        }
    }

    // Método para validar login corregido
    public boolean autenticar(String username, String password) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "El usuario no puede ser nulo para autenticar");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "La contraseña no puede ser nula para autenticar");

        // Ajustado a la columna 'contrasena_hash' y 'usuario'
        String sql = "SELECT contrasena_hash FROM usuarios WHERE usuario = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Obtenemos el hash real de la base de datos
                    String hashedPassword = rs.getString("contrasena_hash");
                    
                    // BCrypt realiza la comparación segura
                    boolean isValid = BCrypt.checkpw(password, hashedPassword);
                    
                    if (isValid) {
                        logger.info("Inicio de sesión exitoso. Usuario: {}", username);
                    } else {
                        logger.warn("Intento de inicio de sesión fallido (contraseña incorrecta). Usuario: {}", username);
                    }
                    
                    return isValid;
                } else {
                    logger.warn("Intento de inicio de sesión fallido (usuario no existe). Usuario: {}", username);
                }
            }
            
        } catch (Exception e) {
            logger.error("Error técnico durante la autenticación del usuario: {}", username, e);
        }
        
        return false;
    }
}