package com.inventario.dao;

import com.inventario.model.Producto;
import com.inventario.util.DatabaseConnection;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAOImpl implements ProductoDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductoDAOImpl.class);

    @Override
    public void agregarProducto(Producto p) {
        // Consideraciones de seguridad (se mantienen)
        Preconditions.checkNotNull(p, "Error de seguridad: Producto nulo");
        String nombreSeguro = StringUtils.trimToEmpty(p.getNombre());
        
        if (nombreSeguro.isEmpty() || p.getCantidad() < 0 || p.getPrecio() < 0) {
            logger.warn("Intento de registro con datos inválidos o maliciosos.");
            throw new IllegalArgumentException("Datos inválidos. Verifique nombre, cantidad y precio.");
        }

        // Conexión a SQL Server usando Consultas Preparadas (Evita Inyección SQL)
        String sql = "INSERT INTO Productos (id, nombre, cantidad, precio) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getId());
            stmt.setString(2, nombreSeguro);
            stmt.setInt(3, p.getCantidad());
            stmt.setDouble(4, p.getPrecio());
            
            stmt.executeUpdate();
            logger.info("Producto registrado exitosamente en la Base de Datos: {}", nombreSeguro);
            
        } catch (SQLException e) {
            logger.error("Error al ejecutar el guardado en SQL Server", e);
            throw new RuntimeException("Error al guardar en la base de datos", e);
        }
    }

    @Override
    public List<Producto> obtenerTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, cantidad, precio FROM Productos";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio")
                );
                lista.add(p);
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener el inventario de SQL Server", e);
        }
        
        return lista;
    }
}