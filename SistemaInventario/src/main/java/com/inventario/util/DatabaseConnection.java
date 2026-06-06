package com.inventario.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // URL sin complicaciones de seguridad
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=InventarioDB;encrypt=false;trustServerCertificate=true;";
    
    // DEFINIMOS EL USUARIO Y LA CONTRASEÑA AQUÍ
    private static final String USER = "sa";
    private static final String PASSWORD = "alejandro586776chacar"; // Asegúrate de que esta sea la clave real que pusiste en SSMS

    public static Connection getConnection() throws SQLException {
        // Pasamos usuario y contraseña explícitamente
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}