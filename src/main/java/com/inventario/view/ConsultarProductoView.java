package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import com.inventario.util.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;

public class ConsultarProductoView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ConsultarProductoView.class);
    
    private InventarioController controller;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnActualizar, btnEliminar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public ConsultarProductoView() {
        this.controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
        cargarDatos(""); 
    }

    private void configurarUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // --- PANEL SUPERIOR ---
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS)); // Cambiado a Y_AXIS para la instrucción
        panelNorte.setBackground(Color.WHITE);

        // Guía visual para el usuario (UX/UI)
        JLabel lblInstruccion = new JLabel("Instrucción: Selecciona una fila de la tabla y luego elige una acción (Actualizar o Eliminar).");
        lblInstruccion.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblInstruccion.setForeground(Color.GRAY);
        lblInstruccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBusqueda.setBackground(Color.WHITE);
        
        txtBuscar = new JTextField(30);
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(41, 128, 185));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setOpaque(true);
        btnBuscar.setContentAreaFilled(true); // ¡Forzamos el color!
        btnBuscar.setBorderPainted(false);
        btnBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText()));

        panelBusqueda.add(new JLabel("Buscar Producto: "));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        panelNorte.add(lblInstruccion);
        panelNorte.add(panelBusqueda);
        add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = {"ID", "Nombre", "Cantidad", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setRowHeight(35);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // --- PANEL INFERIOR (CRUD) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);

        btnActualizar = new JButton("Actualizar Seleccionado");
        btnActualizar.setBackground(new Color(230, 126, 34)); // Naranja
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setOpaque(true);
        btnActualizar.setContentAreaFilled(true);
        btnActualizar.setBorderPainted(false);
        btnActualizar.addActionListener(e -> actualizarProducto());

        btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setBackground(new Color(192, 57, 43)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setOpaque(true);
        btnEliminar.setContentAreaFilled(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.addActionListener(e -> eliminarProducto());

        panelSur.add(btnActualizar);
        panelSur.add(btnEliminar);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void cargarDatos(String filtro) {
        modeloTabla.setRowCount(0);
        String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getString("id"), rs.getString("nombre"), rs.getInt("cantidad"), rs.getDouble("precio")});
            }
        } catch (Exception e) {
            logger.error("Error al cargar datos: {}", e.getMessage());
        }
    }

    private void actualizarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) { 
            JOptionPane.showMessageDialog(this, "Por favor, primero haz CLIC sobre el producto que deseas actualizar.", "Selección requerida", JOptionPane.INFORMATION_MESSAGE); 
            return; 
        }

        String id = modeloTabla.getValueAt(fila, 0).toString();
        String nom = JOptionPane.showInputDialog(this, "Nuevo Nombre:", modeloTabla.getValueAt(fila, 1));
        String cant = JOptionPane.showInputDialog(this, "Nueva Cantidad:", modeloTabla.getValueAt(fila, 2));
        String prec = JOptionPane.showInputDialog(this, "Nuevo Precio:", modeloTabla.getValueAt(fila, 3));

        if (!Strings.isNullOrEmpty(nom)) {
            try {
                controller.actualizarProducto(id, nom, Integer.parseInt(cant), Double.parseDouble(prec));
                cargarDatos("");
            } catch (Exception e) { logger.error("Error al actualizar: {}", e.getMessage()); }
        }
    }

    private void eliminarProducto() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) { 
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila para eliminar.", "Selección requerida", JOptionPane.INFORMATION_MESSAGE); 
            return; 
        }
        
        String id = modeloTabla.getValueAt(fila, 0).toString();
        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.eliminarProducto(id);
            cargarDatos("");
        }
    }
}