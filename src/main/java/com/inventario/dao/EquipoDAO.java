package com.inventario.dao;

import com.inventario.model.Equipo;
import com.inventario.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EquipoDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(EquipoDAO.class);

    // 1. REGISTRAR
    public boolean registrarEquipo(Equipo eq) {
        String sql = "INSERT INTO equipos (numero_serie, id_tipo, marca, modelo, id_estado, id_ubicacion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eq.getNumeroSerie());
            ps.setInt(2, eq.getIdTipo());
            ps.setString(3, eq.getMarca());
            ps.setString(4, eq.getModelo());
            ps.setInt(5, eq.getIdEstado());
            ps.setInt(6, eq.getIdUbicacion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            logger.error("Error al registrar equipo", e);
            JOptionPane.showMessageDialog(null, "Error al registrar: " + e.getMessage());
            return false;
        }
    }

    // 2. LISTAR ACTIVOS
    public List<Equipo> listarEquipos() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos WHERE activo = 1"; 
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEquipo(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar equipos", e);
        }
        return lista;
    }

    // 3. ACTUALIZAR
    public boolean actualizarEquipo(Equipo eq) {
        String sql = "UPDATE equipos SET numero_serie=?, id_tipo=?, marca=?, modelo=?, id_estado=?, id_ubicacion=? WHERE id_equipo=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, eq.getNumeroSerie());
            ps.setInt(2, eq.getIdTipo());
            ps.setString(3, eq.getMarca());
            ps.setString(4, eq.getModelo());
            ps.setInt(5, eq.getIdEstado());
            ps.setInt(6, eq.getIdUbicacion());
            ps.setInt(7, eq.getIdEquipo());
            ps.execute();
            return true;
        } catch (SQLException e) {
            logger.error("Error al actualizar equipo", e);
            return false;
        }
    }

    // 4. ELIMINAR (Lógico)
    public boolean eliminarEquipo(int id) {
        String sql = "UPDATE equipos SET activo = 0 WHERE id_equipo = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            logger.error("Error al eliminar equipo", e);
            return false;
        }
    }
    
    // 5. BUSCAR POR SERIE
    public Equipo buscarEquipoPorSerie(String serie) {
        String sql = "SELECT * FROM equipos WHERE numero_serie = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, serie);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapearEquipo(rs);
        } catch (SQLException e) {
            logger.error("Error al buscar equipo por serie", e);
        }
        return null;
    }

    // 6. RESTAURAR
    public boolean restaurarEquipo(int id) {
        String sql = "UPDATE equipos SET activo = 1 WHERE id_equipo = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            logger.error("Error al restaurar equipo", e);
            return false;
        }
    }
    
    // 7. BUSCAR EQUIPOS (Dinámico)
    public List<Equipo> buscarEquipos(String criterio, String valor) {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos WHERE activo = 1 AND " + criterio + " LIKE ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + valor + "%"); 
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearEquipo(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al buscar equipos", e);
        }
        return lista;
    }
    
    // 8. LISTAR EQUIPOS EN MANTENIMIENTO
    public List<Equipo> listarEquiposEnMantenimiento() {
        List<Equipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM equipos WHERE activo = 1 AND id_estado = 2"; 
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEquipo(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar equipos en mantenimiento", e);
        }
        return lista;
    }

    // 9. ACTUALIZAR ESTADO
    public boolean actualizarEstadoEquipo(String serie, int nuevoEstado) {
        String sql = "UPDATE equipos SET id_estado = ? WHERE numero_serie = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nuevoEstado);
            ps.setString(2, serie);
            ps.execute();
            return true;
        } catch (SQLException e) {
            logger.error("Error al actualizar estado del equipo", e);
            return false;
        }
    }

    // MÉTODO AUXILIAR DE MAPEO (Centraliza la creación del objeto Equipo)
    private Equipo mapearEquipo(ResultSet rs) throws SQLException {
        Equipo eq = new Equipo();
        eq.setIdEquipo(rs.getInt("id_equipo"));
        eq.setNumeroSerie(rs.getString("numero_serie"));
        eq.setIdTipo(rs.getInt("id_tipo"));
        eq.setMarca(rs.getString("marca"));
        eq.setModelo(rs.getString("modelo"));
        eq.setIdEstado(rs.getInt("id_estado"));
        eq.setIdUbicacion(rs.getInt("id_ubicacion"));
        return eq;
    }
}