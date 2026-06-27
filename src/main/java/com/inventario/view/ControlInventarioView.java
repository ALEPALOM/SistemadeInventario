/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.inventario.view;

/**
 *
 * @author Luis Daniel
 */
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ControlInventarioView extends JFrame {

    // Cambia esta ruta por la ubicación real de tu logo en NetBeans
    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public ControlInventarioView() {
        // 1. Configuración básica del JFrame
        setTitle("Control de Inventario - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 620); // Proporciones idénticas a la ventana de la captura
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setResizable(false);

        // 2. Panel Principal (Fondo Azul #0D284A)
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(35, 50, 35, 50));
        setContentPane(panelPrincipal);

        // ==========================================
        // 3. BLOQUE SUPERIOR (Logo + Título + Formulario)
        // ==========================================
        JPanel panelSuperior = new JPanel(new BorderLayout(30, 0));
        panelSuperior.setOpaque(false);

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
        JPanel contenedorLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        contenedorLogo.setOpaque(false);
        contenedorLogo.add(lblLogo);
        panelSuperior.add(contenedorLogo, BorderLayout.WEST);

        // --- CENTRO: TÍTULO Y CONTENIDO DEL FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Gran Título Central
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        JLabel lblTitulo = new JLabel("Control de Inventario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        panelFormulario.add(lblTitulo, gbc);

        // Reset de restricciones para las filas de datos
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 15);

        // Fila 1: Buscar equipos por marca:
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(crearEtiqueta("Buscar equipos por marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        JTextField txtBuscarMarca = crearCampoTexto();
        txtBuscarMarca.setEditable(true); // Este campo sí recibe escritura
        panelFormulario.add(txtBuscarMarca, gbc);

        // Fila 2: Equipos Operativos:
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(crearEtiqueta("Equipos Operativos:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        JTextField txtOperativos = crearCampoTexto();
        panelFormulario.add(txtOperativos, gbc);

        // Fila 3: Equipos dado de baja:
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(crearEtiqueta("Equipos dado de baja:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        JTextField txtBaja = crearCampoTexto();
        panelFormulario.add(txtBaja, gbc);

        // Fila 4: Total por marca:
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(crearEtiqueta("Total por marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        JTextField txtTotalMarca = crearCampoTexto();
        panelFormulario.add(txtTotalMarca, gbc);

        // Fila 5: Total de Equipos:
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(crearEtiqueta("Total de Equipos:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        JTextField txtTotalEquipos = crearCampoTexto();
        panelFormulario.add(txtTotalEquipos, gbc);

        // Margen derecho decorativo en el panel central para equilibrar la composición respecto al escudo
        panelFormulario.setBorder(new EmptyBorder(0, 0, 0, 40));
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ==========================================
        // 4. BLOQUE INFERIOR (Fila de 4 Botones de Acción)
        // ==========================================
        JPanel panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelInferiorBotones.setOpaque(false);
        panelInferiorBotones.setBorder(new EmptyBorder(20, 0, 0, 15));

        panelInferiorBotones.add(crearBotonEstilizado("Ver registro"));
        panelInferiorBotones.add(crearBotonEstilizado("Generar Reporte"));
        panelInferiorBotones.add(crearBotonEstilizado("Menu"));
        panelInferiorBotones.add(crearBotonEstilizado("Salir"));

        panelPrincipal.add(panelInferiorBotones, BorderLayout.SOUTH);
    }

    // Métodos Helper para unificar fuentes, tamaños y colores
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.LEFT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(240, 25)); // Espacio suficiente para las etiquetas de texto largas
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(15);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBackground(new Color(204, 204, 204)); // Gris idéntico al de tu captura de pantalla
        txt.setForeground(Color.BLACK);
        txt.setEditable(false); // Por defecto deshabilitados para mostrar estadísticas calculadas
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return txt;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 18, 6, 18)
        ));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ControlInventarioView().setVisible(true);
        });
    }
}