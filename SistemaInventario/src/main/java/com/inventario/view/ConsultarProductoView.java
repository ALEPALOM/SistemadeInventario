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

public class ConsultarProductoView extends JPanel {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public ConsultarProductoView() {
        configurarUI();
        cargarDatos(""); // Carga todos los productos al inicio
    }

    private void configurarUI() {
        setLayout(new BorderLayout(10, 10)); // BorderLayout es ideal para tablas
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
        txtBuscar.setPreferredSize(new Dimension(350, 35)); // Fuerza un ancho de 350 píxeles
        txtBuscar.setMaximumSize(new Dimension(400, 35));   // Límite máximo
        txtBuscar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // El buscador reacciona al presionar ENTER
        txtBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText()));

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(41, 128, 185)); // Azul profesional
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
        panelNorte.add(Box.createHorizontalGlue()); // Empuja todo a la izquierda

        add(panelNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL (TABLA) ---
        // Definimos las columnas
        String[] columnas = {"ID", "Nombre del Producto", "Cantidad", "Precio Unitario"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Evita que editen la tabla directamente
            }
        };

        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tablaProductos.setRowHeight(30); // Filas más altas para verse moderno
        tablaProductos.setGridColor(new Color(230, 230, 230)); // Líneas suaves
        tablaProductos.setSelectionBackground(new Color(52, 152, 219));
        tablaProductos.setSelectionForeground(Color.WHITE);

        // Diseño del encabezado de la tabla
        JTableHeader header = tablaProductos.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(new Color(50, 50, 50));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Centrar el texto en las columnas de Cantidad y Precio
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(50); // ID más pequeño
        tablaProductos.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tablaProductos.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Método para consultar a SQL Server
    private void cargarDatos(String filtro) {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar
        
        // Asumiendo que tu tabla se llama "Productos"
        String sql = "SELECT * FROM Productos WHERE nombre LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + filtro + "%"); // El % permite buscar coincidencias parciales
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("id"); // Cambia "id" si tu columna se llama distinto
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("cantidad");
                fila[3] = rs.getString("precio");
                modeloTabla.addRow(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los productos: " + e.getMessage());
        }
    }
}