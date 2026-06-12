package com.inventario.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class AdministrarEquiposView extends JFrame {
    
    // Componentes
    private JLabel L_logo;
    private JLabel L_mantenimiento;
    private JButton btnCargarBD;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnGuardar;
    private JButton btnInsertar;
    private JButton btnMenu1;
    private JButton btnRegresar;
    private JButton btnRestaurarUltimo;
    private JComboBox<String> cmbEstado;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JScrollPane jScrollPane1;
    private JLabel lblInfo;
    private JTable tblResultados;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtSerie;
    private JTextField txtTipo;
    private JTextField txtUbicacion;

    public AdministrarEquiposView() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Administrar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con fondo azul sólido (mismo color que la imagen)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(1, 35, 70)); // Azul sólido
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // ==================== PANEL SUPERIOR (TÍTULO CON LOGO) ====================
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(1, 35, 70));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        // Panel para logo y título
        JPanel logoTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        logoTitlePanel.setBackground(new Color(1, 35, 70));
        
        // Logo
        L_logo = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icono.png"));
            Image scaled = logoIcon.getImage().getScaledInstance(60, 65, Image.SCALE_SMOOTH);
            L_logo.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            L_logo.setText(""); // Vacío si no hay imagen
        }
        
        // Título principal
        JLabel titleLabel = new JLabel("Colegio Claretiano Huancayo");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        
        logoTitlePanel.add(L_logo);
        logoTitlePanel.add(titleLabel);
        
        // Subtítulo
        L_mantenimiento = new JLabel("ADMINISTRAR EQUIPOS");
        L_mantenimiento.setFont(new Font("Segoe UI", Font.BOLD, 18));
        L_mantenimiento.setForeground(new Color(255, 215, 0));
        L_mantenimiento.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Línea decorativa
        JPanel linePanel = new JPanel();
        linePanel.setBackground(new Color(1, 35, 70));
        JLabel line = new JLabel("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        line.setForeground(new Color(255, 215, 0, 100));
        line.setFont(new Font("Arial", Font.PLAIN, 16));
        linePanel.add(line);
        linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        topPanel.add(logoTitlePanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        topPanel.add(L_mantenimiento);
        topPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        topPanel.add(linePanel);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ==================== PANEL CENTRAL ====================
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(1, 35, 70));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.BOTH;

        // ==================== SECCIÓN FORMULARIO (IZQUIERDA) ====================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(25, 30, 25, 30)
        ));
        formPanel.setPreferredSize(new Dimension(480, 580));
        
        GridBagConstraints formGbc = new GridBagConstraints();
        formGbc.insets = new Insets(10, 12, 10, 12);
        formGbc.fill = GridBagConstraints.HORIZONTAL;
        formGbc.anchor = GridBagConstraints.WEST;
        
        // Título del formulario
        JLabel formTitle = new JLabel("DATOS DEL EQUIPO");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(1, 35, 70));
        formGbc.gridx = 0; formGbc.gridy = 0;
        formGbc.gridwidth = 2;
        formPanel.add(formTitle, formGbc);
        
        // Línea separadora
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(200, 200, 200));
        formGbc.gridy = 1;
        formGbc.insets = new Insets(5, 12, 15, 12);
        formPanel.add(separator, formGbc);
        
        formGbc.insets = new Insets(10, 12, 10, 12);
        formGbc.gridwidth = 1;
        
        // Número de Serie
        formGbc.gridx = 0; formGbc.gridy = 2;
        jLabel2 = new JLabel("Número de Serie:");
        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel2.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel2, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 2;
        formGbc.weightx = 1.0;
        txtSerie = new JTextField();
        txtSerie.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSerie.setPreferredSize(new Dimension(250, 38));
        styleTextField(txtSerie);
        formPanel.add(txtSerie, formGbc);
        
        // Tipo de Equipo
        formGbc.gridx = 0; formGbc.gridy = 3;
        formGbc.weightx = 0;
        jLabel3 = new JLabel("Tipo de Equipo:");
        jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel3.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel3, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 3;
        formGbc.weightx = 1.0;
        txtTipo = new JTextField();
        txtTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleTextField(txtTipo);
        formPanel.add(txtTipo, formGbc);
        
        // Marca
        formGbc.gridx = 0; formGbc.gridy = 4;
        jLabel4 = new JLabel("Marca:");
        jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel4.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel4, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 4;
        txtMarca = new JTextField();
        txtMarca.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleTextField(txtMarca);
        formPanel.add(txtMarca, formGbc);
        
        // Modelo
        formGbc.gridx = 0; formGbc.gridy = 5;
        jLabel5 = new JLabel("Modelo:");
        jLabel5.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel5.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel5, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 5;
        txtModelo = new JTextField();
        txtModelo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleTextField(txtModelo);
        formPanel.add(txtModelo, formGbc);
        
        // Estado
        formGbc.gridx = 0; formGbc.gridy = 6;
        jLabel6 = new JLabel("Estado:");
        jLabel6.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel6.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel6, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 6;
        cmbEstado = new JComboBox<>(new String[]{"Operativo", "Mantenimiento", "Dañado", "En reparación"});
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbEstado.setBackground(Color.WHITE);
        cmbEstado.setPreferredSize(new Dimension(250, 38));
        cmbEstado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        formPanel.add(cmbEstado, formGbc);
        
        // Ubicación
        formGbc.gridx = 0; formGbc.gridy = 7;
        jLabel7 = new JLabel("Ubicación:");
        jLabel7.setFont(new Font("Segoe UI", Font.BOLD, 13));
        jLabel7.setForeground(new Color(60, 60, 80));
        formPanel.add(jLabel7, formGbc);
        
        formGbc.gridx = 1; formGbc.gridy = 7;
        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        styleTextField(txtUbicacion);
        formPanel.add(txtUbicacion, formGbc);
        
        // ==================== BOTONES DEL FORMULARIO ====================
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 12, 12));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        btnInsertar = createStyledButton("Registrar Equipo", new Color(40, 167, 69));
        btnEditar = createStyledButton("Editar Equipo", new Color(0, 123, 255));
        btnEliminar = createStyledButton("Eliminar Equipo", new Color(220, 53, 69));
        btnGuardar = createStyledButton("Guardar Cambios", new Color(23, 162, 184));
        btnCargarBD = createStyledButton("Ver Registros", new Color(102, 102, 102));
        btnRestaurarUltimo = createStyledButton("Restaurar", new Color(255, 193, 7));
        
        buttonPanel.add(btnInsertar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCargarBD);
        buttonPanel.add(btnRestaurarUltimo);
        
        formGbc.gridx = 0; formGbc.gridy = 8;
        formGbc.gridwidth = 2;
        formGbc.insets = new Insets(15, 12, 5, 12);
        formPanel.add(buttonPanel, formGbc);
        
        // ==================== PANEL TABLA (DERECHA) ====================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Título de la tabla
        JPanel tableTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        tableTitlePanel.setBackground(Color.WHITE);
        JLabel tableTitle = new JLabel("LISTA DE EQUIPOS REGISTRADOS");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(1, 35, 70));
        tableTitlePanel.add(tableTitle);
        
        tablePanel.add(tableTitlePanel, BorderLayout.NORTH);
        tablePanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        
        // Configuración de la tabla
        String[] columnas = {"ID", "Número de Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblResultados = new JTable(model);
        tblResultados.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblResultados.setRowHeight(35);
        tblResultados.setIntercellSpacing(new Dimension(10, 5));
        tblResultados.setSelectionBackground(new Color(0, 123, 255, 50));
        tblResultados.setSelectionForeground(new Color(1, 35, 70));
        
        // Estilo del header
        JTableHeader header = tblResultados.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(1, 35, 70));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        // Alternar colores de filas
        tblResultados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        
        jScrollPane1 = new JScrollPane(tblResultados);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        tablePanel.add(jScrollPane1, BorderLayout.CENTER);
        
        // ==================== ENSAMBLAJE CENTRAL ====================
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.38;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(formPanel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.62;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(tablePanel, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // ==================== PANEL INFERIOR (BOTONES MENU Y SALIR) ====================
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(1, 35, 70));
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JPanel buttonBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonBottomPanel.setBackground(new Color(1, 35, 70));
        
        btnMenu1 = createStyledButton("Menú Principal", new Color(108, 117, 125));
        btnRegresar = createStyledButton("Salir", new Color(220, 53, 69));
        
        buttonBottomPanel.add(btnMenu1);
        buttonBottomPanel.add(btnRegresar);
        
        // Label de información
        lblInfo = new JLabel("Sistema listo para operar");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(200, 220, 255));
        
        bottomPanel.add(buttonBottomPanel, BorderLayout.EAST);
        bottomPanel.add(lblInfo, BorderLayout.WEST);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(new Color(250, 251, 252));
        field.setCaretColor(new Color(1, 35, 70));
        
        // Efecto focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(1, 35, 70), 2),
                    BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
                field.setBackground(Color.WHITE);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                field.setBackground(new Color(250, 251, 252));
            }
        });
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(145, 42));
        
        // Bordes redondeados
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new AdministrarEquiposView().setVisible(true));
    }
}