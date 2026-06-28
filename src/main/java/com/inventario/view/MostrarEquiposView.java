package com.inventario.view;

/**
 *
 * @author Luis Daniel
 */
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MostrarEquiposView extends JPanel {

    // Cambia esta ruta por la ubicación real de tu logo en NetBeans
    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public MostrarEquiposView() {
        // 1. Configuración de ESTE panel (ya no es un JFrame)
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(13, 40, 74));
        setBorder(new EmptyBorder(25, 40, 25, 40));

        // ==========================================
        // 2. BLOQUE SUPERIOR (Logo + Título + Criterios de Selección)
        // ==========================================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        // --- IZQUIERDA: LOGO ---
        JLabel lblLogo = new JLabel();
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
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

        // --- CENTRO: TÍTULO Y FILTROS ---
        JPanel panelCentralFiltros = new JPanel(new GridBagLayout());
        panelCentralFiltros.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la Ventana
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 05, 10);
        JLabel lblTitulo = new JLabel("Mostrar Equipos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        panelCentralFiltros.add(lblTitulo, gbc);

        // Restablecer valores para las opciones de búsqueda
        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 10, 6, 10);

        // Fila 1: Seleccionar criterio
        gbc.gridx = 0; gbc.gridy = 1;
        panelCentralFiltros.add(crearEtiqueta("Seleccionar criterio:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        String[] criterios = {"ID del Equipo", "Número de Serie", "Tipo de Equipo", "Marca", "Modelo"};
        JComboBox<String> cmbCriterio = crearComboBox(criterios);
        panelCentralFiltros.add(cmbCriterio, gbc);

        // Fila 2: Ordenar por
        gbc.gridx = 0; gbc.gridy = 2;
        panelCentralFiltros.add(crearEtiqueta("Ordenar por:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        String[] ordenes = {"Ascendente", "Descendente"};
        JComboBox<String> cmbOrden = crearComboBox(ordenes);
        panelCentralFiltros.add(cmbOrden, gbc);

        // Añadir el panel de filtros con un margen derecho para balancear el escudo
        panelCentralFiltros.setBorder(new EmptyBorder(0, 0, 0, 120));
        panelSuperior.add(panelCentralFiltros, BorderLayout.CENTER);
        
        // AÑADIMOS EL PANEL SUPERIOR DIRECTAMENTE A ESTA CLASE
        add(panelSuperior, BorderLayout.NORTH);

        // ==========================================
        // 3. BLOQUE CENTRAL (Tabla JTable)
        // ==========================================
        String[] columnas = {"ID del Equipo", "Número de Se...", "Tipo de Equipo", "Marca", "Modelo", "Estado", "Ubicación"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaEquipos = new JTable(modeloTabla);
        
        // Configuración de colores idéntica a tu grilla vacía
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
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        
        // AÑADIMOS LA TABLA DIRECTAMENTE A ESTA CLASE
        add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 4. BLOQUE INFERIOR (Fila de 5 Botones de Acción)
        // ==========================================
        JPanel panelInferiorBotones = new JPanel(new BorderLayout());
        panelInferiorBotones.setOpaque(false);
        panelInferiorBotones.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Grupo Izquierda: Cargar Datos, Ordenar Lista, Limpiar Tabla
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelIzquierdo.setOpaque(false);
        panelIzquierdo.add(crearBotonEstilizado("Cargar Datos"));
        panelIzquierdo.add(crearBotonEstilizado("Ordenar Lista"));
        panelIzquierdo.add(crearBotonEstilizado("Limpiar Tabla"));
        panelInferiorBotones.add(panelIzquierdo, BorderLayout.WEST);

        // Grupo Derecha: Menu, Salir
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelDerecho.setOpaque(false);
        panelDerecho.add(crearBotonEstilizado("Menu"));
        panelDerecho.add(crearBotonEstilizado("Salir"));
        panelInferiorBotones.add(panelDerecho, BorderLayout.EAST);

        // AÑADIMOS LOS BOTONES DIRECTAMENTE A ESTA CLASE
        add(panelInferiorBotones, BorderLayout.SOUTH);
    }

    // Métodos Helper para homogeneizar fuentes, bordes y estilos
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(160, 25));
        return lbl;
    }

    private JComboBox<String> crearComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(Color.BLACK);
        combo.setPreferredSize(new Dimension(180, 28));
        return combo;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        return btn;
    }
}