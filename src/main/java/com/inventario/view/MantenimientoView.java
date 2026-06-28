package com.inventario.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MantenimientoView extends JPanel {

    private final String RUTA_LOGO = "src/main/resources/icono.png";

    public MantenimientoView() {
        // 1. Configuración del layout del panel (this)
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(13, 40, 74));
        setBorder(new EmptyBorder(25, 40, 15, 40));

        // 2. SECCIÓN SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        panelSuperior.setOpaque(false);

        JLabel lblLogo = new JLabel();
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo Colegio ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        
        JPanel panelContenedorLogo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panelContenedorLogo.setOpaque(false);
        panelContenedorLogo.add(lblLogo);
        panelSuperior.add(panelContenedorLogo, BorderLayout.WEST);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Mantenimiento de equipos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulario.add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Siguiente equipo a atender:");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSubtitulo.setForeground(Color.WHITE);
        gbc.gridy = 1;
        panelFormulario.add(lblSubtitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 0, 5, 15);
        
        // Campos
        gbc.gridy = 2; addField(panelFormulario, gbc, "Número de serie", 0);
        gbc.gridy = 3; addField(panelFormulario, gbc, "Tipo de equipo", 0);
        gbc.gridy = 4; addField(panelFormulario, gbc, "Marca", 0);
        gbc.gridy = 5; addField(panelFormulario, gbc, "Modelo", 0);
        gbc.gridy = 6; addField(panelFormulario, gbc, "Ubicación", 0);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        // 3. SECCIÓN CENTRAL
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);

        JPanel panelBotonesControl = new JPanel(new GridLayout(1, 4, 25, 0));
        panelBotonesControl.setOpaque(false);
        panelBotonesControl.add(crearBotonEstilizado("Atender equipo"));
        panelBotonesControl.add(crearBotonEstilizado("Ver siguiente"));
        panelBotonesControl.add(crearBotonEstilizado("Dar de baja equipo"));
        panelBotonesControl.add(crearBotonEstilizado("Actualizar lista"));
        panelCentro.add(panelBotonesControl, BorderLayout.NORTH);

        String[] columnas = {"ID", "Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"};
        JTable tablaEquipos = new JTable(new DefaultTableModel(columnas, 0));
        tablaEquipos.setBackground(new Color(212, 212, 212));
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        panelCentro.add(scrollPane, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // 4. SECCIÓN INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.add(new JLabel("Error: Conexión BD fallida..."), BorderLayout.WEST);
        
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelNavegacion.setOpaque(false);
        panelNavegacion.add(crearBotonEstilizado("Menu"));
        panelNavegacion.add(crearBotonEstilizado("Salir"));
        panelInferior.add(panelNavegacion, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridx = 0;
        panel.add(crearEtiquetaFormulario(label), gbc);
        gbc.gridx = 1;
        panel.add(crearCampoTextoFormulario(), gbc);
    }

    private JLabel crearEtiquetaFormulario(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField crearCampoTextoFormulario() {
        JTextField campo = new JTextField(22);
        campo.setEditable(false);
        return campo;
    }

    private JButton crearBotonEstilizado(String texto) {
        return new JButton(texto);
    }
}