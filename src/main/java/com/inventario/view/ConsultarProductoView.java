package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import com.inventario.util.DatabaseConnection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL; // Para la carga del logo
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Strings;

// CORRECCIÓN: Ahora hereda de JFrame para que sea una ventana emergente real
public class ConsultarProductoView extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ConsultarProductoView.class);
    
    private InventarioController controller;
    private JTextField txtBuscar;
    private JButton btnBuscar, btnActualizar, btnEliminar, btnCerrar;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    
    // Ruta del logo para el ejecutable
    private final String RUTA_LOGO = "icono.png";

    public ConsultarProductoView() {
        // Configuraciones de la ventana (JFrame)
        setTitle("Consultar Producto - Colegio Claretiano Huancayo");
        setSize(920, 650); // Tamaño estandarizado con tus demás ventanas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        this.controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
        cargarDatos(""); 
    }

    private void configurarUI() {
        // Panel Principal contenedor
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 40, 25, 40));
        setContentPane(panelPrincipal);

        // ==========================================
        // PANEL SUPERIOR (Logo + Título + Búsqueda)
        // ==========================================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        // --- IZQUIERDA: LOGO ---
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
            Image img = iconLogo.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        JPanel contenedorLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contenedorLogo.setOpaque(false);
        contenedorLogo.add(lblLogo);
        panelSuperior.add(contenedorLogo, BorderLayout.WEST);

        // --- CENTRO: TÍTULO Y BÚSQUEDA ---
        JPanel panelCentralFiltros = new JPanel(new GridBagLayout());
        panelCentralFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la Ventana
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 15, 10);
        JLabel lblTitulo = new JLabel("Consultar Productos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        panelCentralFiltros.add(lblTitulo, gbc);

        // Instrucción UX
        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 10, 10);
        JLabel lblInstruccion = new JLabel("Instrucción: Selecciona una fila de la tabla y elige una acción abajo.", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblInstruccion.setForeground(new Color(200, 200, 200));
        panelCentralFiltros.add(lblInstruccion, gbc);

        // Buscador
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.insets = new Insets(5, 10, 5, 10);
        JLabel lblBuscar = new JLabel("Buscar Producto:", SwingConstants.RIGHT);
        lblBuscar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBuscar.setForeground(Color.WHITE);
        panelCentralFiltros.add(lblBuscar, gbc);

        gbc.gridx = 1;
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelBusqueda.setOpaque(false);
        txtBuscar = crearCampoTexto();
        btnBuscar = crearBotonEstilizado("Buscar", 100);
        btnBuscar.addActionListener(e -> cargarDatos(txtBuscar.getText()));
        
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(Box.createHorizontalStrut(10));
        panelBusqueda.add(btnBuscar);
        panelCentralFiltros.add(panelBusqueda, gbc);

        panelCentralFiltros.setBorder(new EmptyBorder(0, 0, 0, 120));
        panelSuperior.add(panelCentralFiltros, BorderLayout.CENTER);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ==========================================
        // BLOQUE CENTRAL (Tabla JTable estilizada)
        // ==========================================
        String[] columnas = {"ID", "Nombre", "Cantidad", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductos = new JTable(modeloTabla);
        
        tablaProductos.setBackground(new Color(212, 212, 212)); 
        tablaProductos.setFillsViewportHeight(true);
        tablaProductos.setRowHeight(25);
        tablaProductos.setGridColor(new Color(189, 195, 199));
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tablaProductos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaProductos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        scrollPane.setPreferredSize(new Dimension(850, 300)); 
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // BLOQUE INFERIOR (Botones de Acción)
        // ==========================================
        JPanel panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelInferiorBotones.setOpaque(false);
        panelInferiorBotones.setBorder(new EmptyBorder(10, 0, 0, 0));

        btnActualizar = crearBotonEstilizado("Actualizar Seleccionado", 200);
        btnEliminar = crearBotonEstilizado("Eliminar Seleccionado", 180);
        btnCerrar = crearBotonEstilizado("Cerrar", 120);

        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnCerrar.addActionListener(e -> this.dispose());

        panelInferiorBotones.add(btnActualizar);
        panelInferiorBotones.add(btnEliminar);
        panelInferiorBotones.add(btnCerrar);

        panelPrincipal.add(panelInferiorBotones, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE DISEÑO CORPORATIVO ---

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(15);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(new Color(204, 204, 204));
        txt.setForeground(Color.BLACK);
        txt.setPreferredSize(new Dimension(250, 32));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return txt;
    }

    private JButton crearBotonEstilizado(String texto, int ancho) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(ancho, 35));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(4, 5, 4, 5)
        ));
        return btn;
    }

    // --- LÓGICA DE DATOS (TOTALMENTE INTACTA) ---

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