package com.inventario.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ControlInventarioView extends JPanel {

    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public ControlInventarioView() {
        // 1. Configuración del layout (this)
        setLayout(new BorderLayout(0, 20));
        setBackground(new Color(13, 40, 74));
        setBorder(new EmptyBorder(35, 50, 35, 50));

        // 2. BLOQUE SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout(30, 0));
        panelSuperior.setOpaque(false);

        // Logo
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

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        JLabel lblTitulo = new JLabel("Control de Inventario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        panelFormulario.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 8, 15);

        // Campos
        gbc.gridy = 1; addField(panelFormulario, gbc, "Buscar equipos por marca:", true);
        gbc.gridy = 2; addField(panelFormulario, gbc, "Equipos Operativos:", false);
        gbc.gridy = 3; addField(panelFormulario, gbc, "Equipos dado de baja:", false);
        gbc.gridy = 4; addField(panelFormulario, gbc, "Total por marca:", false);
        gbc.gridy = 5; addField(panelFormulario, gbc, "Total de Equipos:", false);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // 3. BLOQUE INFERIOR (Botones)
        JPanel panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelInferiorBotones.setOpaque(false);
        panelInferiorBotones.add(crearBotonEstilizado("Ver registro"));
        panelInferiorBotones.add(crearBotonEstilizado("Generar Reporte"));
        panelInferiorBotones.add(crearBotonEstilizado("Menu"));
        panelInferiorBotones.add(crearBotonEstilizado("Salir"));

        add(panelInferiorBotones, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, boolean editable) {
        gbc.gridx = 0;
        panel.add(crearEtiqueta(label), gbc);
        gbc.gridx = 1;
        JTextField txt = crearCampoTexto();
        txt.setEditable(editable);
        panel.add(txt, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.LEFT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(240, 25));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(15);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txt.setBackground(new Color(204, 204, 204));
        return txt;
    }

    private JButton crearBotonEstilizado(String texto) {
        return new JButton(texto);
    }
}