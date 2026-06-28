package com.inventario.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AdministrarEquiposView extends JFrame {

    // Cambia esta ruta por la ubicación real de tu logo en NetBeans
    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public AdministrarEquiposView() {
        // 1. Configuración básica del JFrame
        setTitle("Administrar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 720); // Tamaño ideal para mantener las proporciones exactas
        setLocationRelativeTo(null); // Centrar ventana en pantalla
        setResizable(false);

        // 2. Panel Principal con el Fondo Azul Institucional (#0D284A)
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 40, 20, 40));
        setContentPane(panelPrincipal);

        // 3. TÍTULO SUPERIOR ("Administrar Equipos")
        // Usamos una pequeña trampa de margen a la izquierda para simular el descentrado visual de la captura
        JLabel lblTitulo = new JLabel("Administrar Equipos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 100, 10, 0)); 
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ==========================================
        // 4. BLOQUE SUPERIOR DE CONTROL (Logo + Formulario + Botones)
        // ==========================================
        JPanel panelControl = new JPanel(new BorderLayout(25, 0));
        panelControl.setOpaque(false);

        // --- IZQUIERDA: LOGO ---
        JLabel lblLogo = new JLabel();
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
            Image img = iconLogo.getImage().getScaledInstance(160, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        JPanel contenedorLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        contenedorLogo.setOpaque(false);
        contenedorLogo.add(lblLogo);
        panelControl.add(contenedorLogo, BorderLayout.WEST);

        // --- CENTRO: FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Número de Serie
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(crearEtiqueta("Número de Serie:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtSerie = crearCampoTexto();
        panelFormulario.add(txtSerie, gbc);

        // Fila 2: Tipo de Equipo
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(crearEtiqueta("Tipo de Equipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtTipo = crearCampoTexto();
        panelFormulario.add(txtTipo, gbc);

        // Fila 3: Marca
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(crearEtiqueta("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField txtMarca = crearCampoTexto();
        panelFormulario.add(txtMarca, gbc);

        // Fila 4: Modelo
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(crearEtiqueta("Modelo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField txtModelo = crearCampoTexto();
        panelFormulario.add(txtModelo, gbc);

        // Fila 5: Estado (JCombobox blanco)
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(crearEtiqueta("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        String[] estados = {"Operativo", "Mantenimiento", "De Baja"};
        JComboBox<String> cmbEstado = new JComboBox<>(estados);
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbEstado.setBackground(Color.WHITE);
        panelFormulario.add(cmbEstado, gbc);

        // Fila 6: Ubicación
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(crearEtiqueta("Ubicación:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JTextField txtUbicacion = crearCampoTexto();
        panelFormulario.add(txtUbicacion, gbc);

        panelControl.add(panelFormulario, BorderLayout.CENTER);

        // --- DERECHA: GRILLA DE BOTONES DE ACCIÓN (2x3) ---
        JPanel panelAccionesGrid = new JPanel(new GridLayout(3, 2, 15, 12));
        panelAccionesGrid.setOpaque(false);
        panelAccionesGrid.setBorder(new EmptyBorder(10, 10, 10, 0));

        panelAccionesGrid.add(crearBotonAccion("Registrar Equipo"));
        panelAccionesGrid.add(crearBotonAccion("Editar Equipo"));
        panelAccionesGrid.add(crearBotonAccion("Eliminar Equipo"));
        panelAccionesGrid.add(crearBotonAccion("Ver Registros"));
        panelAccionesGrid.add(crearBotonAccion("Guardar Cambios"));
        panelAccionesGrid.add(crearBotonAccion("restaurar")); // En minúscula respetando tu captura original

        panelControl.add(panelAccionesGrid, BorderLayout.EAST);
        panelPrincipal.add(panelControl, BorderLayout.CENTER);

        // ==========================================
        // 5. BLOQUE INFERIOR (Tabla JTable + Navegación)
        // ==========================================
        JPanel panelInferior = new JPanel(new BorderLayout(0, 15));
        panelInferior.setOpaque(false);

        // --- CONFIGURACIÓN DE LA TABLA ---
        String[] columnas = {"ID del Equipo", "Número de Serie", "Tipo de Equipo", "Marca", "Modelo", "Estado", "Ubicación"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaEquipos = new JTable(modeloTabla);
        
        // Colores planos calcados de la grilla
        tablaEquipos.setBackground(new Color(212, 212, 212)); 
        tablaEquipos.setFillsViewportHeight(true);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setGridColor(new Color(189, 195, 199));

        // Cabeceras (Header)
        JTableHeader header = tablaEquipos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setPreferredSize(new Dimension(880, 240));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        // --- BOTONES DE NAVEGACIÓN INFERIOR (Menu / Salir) ---
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);

        panelNavegacion.add(crearBotonAccion("Menu"));
        panelNavegacion.add(crearBotonAccion("Salir"));
        panelInferior.add(panelNavegacion, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    // Métodos Helper para mantener la consistencia del diseño visual
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(140, 25));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(12);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(new Color(204, 204, 204)); // Gris idéntico al de tu captura
        txt.setForeground(Color.BLACK);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return txt;
    }

    private JButton crearBotonAccion(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdministrarEquiposView().setVisible(true);
        });
    }
}