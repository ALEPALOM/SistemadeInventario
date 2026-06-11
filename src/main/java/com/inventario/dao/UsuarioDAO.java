package com.inventario.dao;

import com.inventario.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.sql.*;

public class UsuarioDAO {

    // Definimos el logger para registrar intentos de acceso
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

    // Método para registrar usuario (lo llamarás al crear usuarios)
    public void registrarUsuario(String username, String password) {
        // USO DE GUAVA: Validación antes de procesar
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "El nombre de usuario no puede estar vacío");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "La contraseña no puede estar vacía");

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO Usuarios (username, password) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            
            // Registro exitoso en Logs
            logger.info("Usuario registrado exitosamente en el sistema: {}", username);
            
        } catch (SQLException e) {
            // Reemplazo de e.printStackTrace() por Logback
            logger.error("Error al registrar el usuario en SQL Server: {}", username, e);
            throw new RuntimeException("Error al registrar usuario en la base de datos", e);
        }
    }

    // Método para validar login
    public boolean autenticar(String username, String password) {
        // Validación con Guava para evitar inyección o errores de null
        Preconditions.checkArgument(!Strings.isNullOrEmpty(username), "El usuario no puede ser nulo para autenticar");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(password), "La contraseña no puede ser nula para autenticar");

        String sql = "SELECT password FROM Usuarios WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                // BCrypt.checkpw es la forma correcta de comparar
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
            
        } catch (Exception e) {
            logger.error("Error técnico durante la autenticación del usuario: {}", username, e);
        }
        
        return false;
    }
}
