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

public class MantenimientoView extends JFrame {

    // Cambia esta ruta por la ubicación real de tu logo en tu proyecto de NetBeans
    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public MantenimientoView() {
        // 1. Configuración básica del JFrame
        setTitle("Mantenimiento de Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 700); // Dimensiones óptimas para igualar la proporción
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // 2. Panel Principal (Fondo Azul #0D284A)
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 40, 15, 40));
        setContentPane(panelPrincipal);

        // ==========================================
        // 3. SECCIÓN SUPERIOR: Logo + Formulario
        // ==========================================
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        panelSuperior.setOpaque(false);

        // --- IZQUIERDA: Escudo / Logo ---
        JLabel lblLogo = new JLabel();
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo Colegio ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        // Alineación superior para el logo
        JPanel panelContenedorLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelContenedorLogo.setOpaque(false);
        panelContenedorLogo.add(lblLogo);
        panelSuperior.add(panelContenedorLogo, BorderLayout.WEST);

        // --- CENTRO/DERECHA: Formulario de Mantenimiento ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título de la sección
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblTitulo = new JLabel("Mantenimiento de equipos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(Color.WHITE);
        panelFormulario.add(lblTitulo, gbc);

        // Subtítulo
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 15, 15);
        JLabel lblSubtitulo = new JLabel("Siguiente equipo a atender:");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubtitulo.setForeground(Color.WHITE);
        panelFormulario.add(lblSubtitulo, gbc);

        // Restablecer valores para las filas de campos
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 15);

        // Fila 1: Número de serie
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(crearEtiquetaFormulario("Número de serie"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField txtSerie = crearCampoTextoFormulario();
        panelFormulario.add(txtSerie, gbc);

        // Fila 2: Tipo de equipo
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(crearEtiquetaFormulario("Tipo de equipo"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField txtTipo = crearCampoTextoFormulario();
        panelFormulario.add(txtTipo, gbc);

        // Fila 3: Marca
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(crearEtiquetaFormulario("Marca"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JTextField txtMarca = crearCampoTextoFormulario();
        panelFormulario.add(txtMarca, gbc);

        // Fila 4: Modelo
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(crearEtiquetaFormulario("Modelo"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JTextField txtModelo = crearCampoTextoFormulario();
        panelFormulario.add(txtModelo, gbc);

        // Fila 5: Ubicación
        gbc.gridx = 0; gbc.gridy = 6;
        panelFormulario.add(crearEtiquetaFormulario("Ubicación"), gbc);
        gbc.gridx = 1; gbc.gridy = 6;
        JTextField txtUbicacion = crearCampoTextoFormulario();
        panelFormulario.add(txtUbicacion, gbc);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ==========================================
        // 4. SECCIÓN CENTRAL: Botones de Operación + Tabla
        // ==========================================
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);

        // --- FILA HORIZONTAL DE BOTONES ---
        JPanel panelBotonesControl = new JPanel(new GridLayout(1, 4, 25, 0));
        panelBotonesControl.setOpaque(false);
        panelBotonesControl.setBorder(new EmptyBorder(10, 0, 5, 0));

        panelBotonesControl.add(crearBotonEstilizado("Atender equipo"));
        panelBotonesControl.add(crearBotonEstilizado("Ver siguiente"));
        panelBotonesControl.add(crearBotonEstilizado("Dar de baja equipo"));
        panelBotonesControl.add(crearBotonEstilizado("Actualizar lista"));
        panelCentro.add(panelBotonesControl, BorderLayout.NORTH);

        // --- TABLA JTABLE ---
        String[] columnas = {"ID", "Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaEquipos = new JTable(modeloTabla);
        
        // Estilo idéntico al contenedor gris
        tablaEquipos.setBackground(new Color(212, 212, 212)); 
        tablaEquipos.setFillsViewportHeight(true);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setGridColor(new Color(189, 195, 199));

        // Estilo de las cabeceras
        JTableHeader header = tablaEquipos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // ==========================================
        // 5. SECCIÓN INFERIOR: Error Log + Navegación
        // ==========================================
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Mensaje de Error de Base de Datos (Igual al de la captura)
        JLabel lblError = new JLabel("Error: The TCP/IP connection to the host localhost, port 1433 has faile...");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setForeground(new Color(40, 45, 50)); // Tono oscuro sutil sobre el fondo azul
        panelInferior.add(lblError, BorderLayout.WEST);

        // Botones de Navegación del Sistema (Menu / Salir)
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);
        panelNavegacion.add(crearBotonEstilizado("Menu"));
        panelNavegacion.add(crearBotonEstilizado("Salir"));
        panelInferior.add(panelNavegacion, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    // Métodos Helper para evitar redundancia y mantener limpio el código
    private JLabel crearEtiquetaFormulario(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 15));
        etiqueta.setForeground(Color.WHITE);
        etiqueta.setPreferredSize(new Dimension(150, 25));
        return etiqueta;
    }

    private JTextField crearCampoTextoFormulario() {
        JTextField campo = new JTextField(22);
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBackground(new Color(204, 204, 204)); // Gris mate idéntico a las cajas de salida de tu imagen
        campo.setForeground(Color.BLACK);
        campo.setEditable(false); // Simulando los campos de consulta de la captura
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return campo;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MantenimientoView().setVisible(true);
        });
    }
}
