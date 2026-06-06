package com.inventario.dao;

import com.inventario.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UsuarioDAO {

    // Método para registrar usuario (lo llamarás al crear usuarios)
    public void registrarUsuario(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO Usuarios (username, password) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para validar login
    public boolean autenticar(String username, String password) {
    String sql = "SELECT password FROM Usuarios WHERE username = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            String hashedPassword = rs.getString("password");
            // CORRECCIÓN: BCrypt.checkpw es la forma correcta de comparar
            // Asegúrate de que el hash en BD empiece con $2a$
            return BCrypt.checkpw(password, hashedPassword);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
}
