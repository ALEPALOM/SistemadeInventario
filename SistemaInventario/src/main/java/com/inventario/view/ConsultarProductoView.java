package com.inventario.view;

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

public class ConsultarProductoView extends JPanel {
    // Definimos el logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(ConsultarProductoView.class);
    
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public ConsultarProductoView() {
        configurarUI();
        cargarDatos(""); // Carga todos los productos al inicio
    }

    private void configurarUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // --- PANEL SUPERIOR (BÚSQUEDA) ---
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.X_AXIS));
        panelNorte.setBackground(Color.WHITE);

        JLabel lblBuscar = new JLabel("Buscar Producto: ");
        lblBuscar.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblBuscar.setForeground(new Color(50, 50, 50));

        txtBuscar = new JTextField();
        txtBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtBuscar.setPreferredSize(new Dimension(350, 35));
        txtBuscar.setMaximumSize(new Dimension(400, 35));
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        txtBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText()));

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(41, 128, 185));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setOpaque(true);
        btnBuscar.setMaximumSize(new Dimension(100, 35));
        btnBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText()));

        panelNorte.add(lblBuscar);
        panelNorte.add(Box.createRigidArea(new Dimension(10, 0)));
        panelNorte.add(txtBuscar);
        panelNorte.add(Box.createRigidArea(new Dimension(15, 0)));
        panelNorte.add(btnBuscar);
        panelNorte.add(Box.createHorizontalGlue());

        add(panelNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL (TABLA) ---
        String[] columnas = {"ID", "Nombre del Producto", "Cantidad", "Precio Unitario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.setRowHeight(30);
        tablaProductos.setGridColor(new Color(230, 230, 230));
        tablaProductos.setSelectionBackground(new Color(52, 152, 219));
        tablaProductos.setSelectionForeground(Color.WHITE);

        JTableHeader header = tablaProductos.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void cargarDatos(String filtro) {
        modeloTabla.setRowCount(0);
        
        String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("id");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("cantidad");
                fila[3] = rs.getString("precio");
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            // Registro profesional del error
            logger.error("Error al cargar los productos en ConsultarProductoView: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Error técnico al consultar el inventario. Verifique los logs.");
        }
    }
}