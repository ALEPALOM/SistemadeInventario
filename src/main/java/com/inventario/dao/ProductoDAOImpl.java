package com.inventario.dao;

import com.inventario.model.Producto;
import com.inventario.util.DatabaseConnection;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings; // Importación necesaria para eliminarProducto
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
        // Consideraciones de seguridad
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

    // --- NUEVO MÉTODO: ACTUALIZAR PRODUCTO ---
    @Override
    public void actualizarProducto(Producto p) {
        Preconditions.checkNotNull(p, "Error de seguridad: Producto a actualizar es nulo");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(p.getId()), "El ID del producto no puede estar vacío");
        
        String nombreSeguro = StringUtils.trimToEmpty(p.getNombre());
        
        if (nombreSeguro.isEmpty() || p.getCantidad() < 0 || p.getPrecio() < 0) {
            logger.warn("Intento de actualización con datos inválidos para el ID: {}", p.getId());
            throw new IllegalArgumentException("Datos inválidos para actualizar.");
        }

        String sql = "UPDATE Productos SET nombre = ?, cantidad = ?, precio = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreSeguro);
            stmt.setInt(2, p.getCantidad());
            stmt.setDouble(3, p.getPrecio());
            stmt.setString(4, p.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Producto actualizado exitosamente: ID {}", p.getId());
            } else {
                logger.warn("Intento de actualizar un producto que no existe. ID: {}", p.getId());
                throw new RuntimeException("No se encontró el producto con el ID especificado.");
            }
            
        } catch (SQLException e) {
            logger.error("Error al actualizar en SQL Server", e);
            throw new RuntimeException("Error al actualizar en la base de datos", e);
        }
    }

    // --- NUEVO MÉTODO: ELIMINAR PRODUCTO ---
    @Override
    public void eliminarProducto(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "Error de seguridad: El ID a eliminar está vacío");

        String sql = "DELETE FROM Productos WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, id);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.info("Producto eliminado exitosamente de la base de datos: ID {}", id);
            } else {
                logger.warn("Intento de eliminar un producto que no existe. ID: {}", id);
                throw new RuntimeException("No se encontró el producto a eliminar.");
            }
            
        } catch (SQLException e) {
            logger.error("Error al eliminar en SQL Server", e);
            throw new RuntimeException("Error al eliminar de la base de datos", e);
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
    
    // --- MÉTODO REQUERIDO EL TEST DE SEGURIDAD ---
   
    public void buscarProducto(String id) {
        String sql = "SELECT * FROM Productos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeQuery();
        } catch (SQLException e) {
            logger.error("Error al buscar producto: " + e.getMessage());
            throw new RuntimeException("Error en la búsqueda", e);
        }
    }
}