
package com.inventario.view;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
/**
 *
 * @author Luis Daniel
 */
public class BuscarEquiposView extends JFrame {
    // Cambia esta ruta por la ubicación real de tu logo en NetBeans
    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public BuscarEquiposView() {
        // 1. Configuración básica del JFrame
        setTitle("Buscar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 650); // Proporción similar a la ventana original
        setLocationRelativeTo(null); // Centrar la ventana en pantalla
        setResizable(false);

        // 2. Panel Principal (Fondo Azul Institucional)
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74)); // #0D284A
        panelPrincipal.setBorder(new EmptyBorder(25, 35, 25, 35));
        setContentPane(panelPrincipal);

        // 3. TÍTULO SUPERIOR ("Buscar Equipos")
        JLabel lblTitulo = new JLabel("Buscar Equipos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // ==========================================
        // 4. PANEL SUPERIOR DE CONTROL (Logo + Filtros + Botones)
        // ==========================================
        JPanel panelControl = new JPanel(new BorderLayout(15, 0));
        panelControl.setOpaque(false);

        // --- IZQUIERDA: LOGO ---
        JLabel lblLogo = new JLabel();
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Logo Colegio ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        panelControl.add(lblLogo, BorderLayout.WEST);

        // --- CENTRO: CAMPOS DE BÚSQUEDA ---
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Ingrese información
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblInfo = new JLabel("Ingrese información:", SwingConstants.RIGHT);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblInfo.setForeground(Color.WHITE);
        panelBusqueda.add(lblInfo, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        JTextField txtInformacion = new JTextField(15);
        txtInformacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInformacion.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        panelBusqueda.add(txtInformacion, gbc);

        // Fila 2: Buscar por
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblBuscarPor = new JLabel("Buscar por:", SwingConstants.RIGHT);
        lblBuscarPor.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblBuscarPor.setForeground(Color.WHITE);
        panelBusqueda.add(lblBuscarPor, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        String[] opciones = {"ID del Equipo", "Número de Serie", "Tipo de Equipo", "Marca", "Modelo"};
        JComboBox<String> cmbBuscarPor = new JComboBox<>(opciones);
        cmbBuscarPor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbBuscarPor.setBackground(new Color(235, 241, 245));
        panelBusqueda.add(cmbBuscarPor, gbc);

        panelControl.add(panelBusqueda, BorderLayout.CENTER);

        // --- DERECHA: BOTONES (Buscar / Limpiar) ---
        JPanel panelAcciones = new JPanel(new GridLayout(2, 1, 0, 15));
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(new EmptyBorder(5, 10, 5, 0));

        JButton btnBuscar = crearBotonEstilizado("Buscar");
        JButton btnLimpiar = crearBotonEstilizado("Limpiar");

        panelAcciones.add(btnBuscar);
        panelAcciones.add(btnLimpiar);
        panelControl.add(panelAcciones, BorderLayout.EAST);

        panelPrincipal.add(panelControl, BorderLayout.CENTER);

        // ==========================================
        // 5. PANEL INFERIOR (Tabla + Botones de Navegación)
        // ==========================================
        JPanel panelInferior = new JPanel(new BorderLayout(0, 15));
        panelInferior.setOpaque(false);

        // --- TABLA DE DATOS ---
        String[] columnas = {"ID del Equipo", "Número de Se...", "Tipo de Equipo", "Marca", "Modelo", "Estado", "Ubicación"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaEquipos = new JTable(modeloTabla);
        
        // Estilos para asemejar al contenedor gris original
        tablaEquipos.setBackground(new Color(212, 212, 212)); // Fondo gris suave de la grilla
        tablaEquipos.setFillsViewportHeight(true);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setGridColor(new Color(189, 195, 199));

        // Estilos de la cabecera (Header)
        JTableHeader header = tablaEquipos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setPreferredSize(new Dimension(850, 260));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        // --- BOTONES DE NAVEGACIÓN (Menu / Salir) ---
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);

        JButton btnMenu = crearBotonEstilizado("Menu");
        JButton btnSalir = crearBotonEstilizado("Salir");

        panelNavegacion.add(btnMenu);
        panelNavegacion.add(btnSalir);
        panelInferior.add(panelNavegacion, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    // Método helper para crear botones con la estética exacta (Borde gris, fuente oscura y fondo claro)
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        // Borde redondeado y estilizado como los botones nativos de Windows/Swing clásicos
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 18, 6, 18)
        ));
        return boton;
    }

    public static void main(String[] args) {
        // Ejecución segura de la interfaz en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            new BuscarEquiposView().setVisible(true);
        });
    }
}
