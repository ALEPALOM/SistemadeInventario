package com.inventario.view;

import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;
import java.awt.*;
import java.io.File;
import java.net.URL; // Importación necesaria para buscar dentro del JAR
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MantenimientoView extends JFrame {

    // Ruta optimizada para el archivo ejecutable JAR
    private final String RUTA_LOGO = "icono.png";
    private JTextField txtSerie, txtTipo, txtMarca, txtModelo, txtUbicacion;
    private JTable tablaEquipos;
    private DefaultTableModel modeloTabla;
    private EquipoDAO equipoDAO = new EquipoDAO();
    private List<Equipo> listaMantenimiento;
    private int indiceActual = 0;

    public MantenimientoView() {
        setTitle("Mantenimiento de equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 40, 15, 40));
        setContentPane(panelPrincipal);

        // SECCIÓN SUPERIOR
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        panelSuperior.setOpaque(false);

        // Carga de imagen adaptada para funcionar dentro del JAR
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        }
        panelSuperior.add(lblLogo, BorderLayout.WEST);

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
        
        txtSerie = crearCampoTextoFormulario();
        txtTipo = crearCampoTextoFormulario();
        txtMarca = crearCampoTextoFormulario();
        txtModelo = crearCampoTextoFormulario();
        txtUbicacion = crearCampoTextoFormulario();

        addField(panelFormulario, gbc, "Número de serie", txtSerie, 2);
        addField(panelFormulario, gbc, "Tipo de equipo", txtTipo, 3);
        addField(panelFormulario, gbc, "Marca", txtMarca, 4);
        addField(panelFormulario, gbc, "Modelo", txtModelo, 5);
        addField(panelFormulario, gbc, "Ubicación", txtUbicacion, 6);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // SECCIÓN CENTRAL
        JPanel panelCentro = new JPanel(new BorderLayout(0, 15));
        panelCentro.setOpaque(false);

        JPanel panelBotonesControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotonesControl.setOpaque(false);
        
        JButton btnAtender = crearBotonEstilizado("Atender equipo");
        JButton btnSiguiente = crearBotonEstilizado("Ver siguiente");
        JButton btnBaja = crearBotonEstilizado("Dar de baja");
        JButton btnActualizar = crearBotonEstilizado("Actualizar lista");

        btnAtender.addActionListener(e -> atenderEquipo());
        btnSiguiente.addActionListener(e -> mostrarSiguiente());
        btnBaja.addActionListener(e -> darDeBaja());
        btnActualizar.addActionListener(e -> cargarListaMantenimiento());

        panelBotonesControl.add(btnAtender);
        panelBotonesControl.add(btnSiguiente);
        panelBotonesControl.add(btnBaja);
        panelBotonesControl.add(btnActualizar);
        panelCentro.add(panelBotonesControl, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"}, 0);
        tablaEquipos = new JTable(modeloTabla);
        tablaEquipos.setBackground(new Color(212, 212, 212));
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setPreferredSize(new Dimension(850, 200));
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // SECCIÓN INFERIOR
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);
        JButton btnMenu = crearBotonEstilizado("Menu");
        JButton btnSalir = crearBotonEstilizado("Salir");
        
        btnMenu.addActionListener(e -> this.dispose());
        btnSalir.addActionListener(e -> System.exit(0));
        
        panelNavegacion.add(btnMenu); panelNavegacion.add(btnSalir);
        panelInferior.add(panelNavegacion, BorderLayout.EAST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        cargarListaMantenimiento();
    }

    private void cargarListaMantenimiento() {
        listaMantenimiento = equipoDAO.listarEquiposEnMantenimiento(); 
        modeloTabla.setRowCount(0);
        for (Equipo eq : listaMantenimiento) {
            modeloTabla.addRow(new Object[]{eq.getIdEquipo(), eq.getNumeroSerie(), eq.getIdTipo(), eq.getMarca(), eq.getModelo(), eq.getIdEstado(), eq.getIdUbicacion()});
        }
        indiceActual = 0;
        mostrarSiguiente();
    }

    private void mostrarSiguiente() {
        if (listaMantenimiento != null && !listaMantenimiento.isEmpty()) {
            if (indiceActual >= listaMantenimiento.size()) indiceActual = 0;
            Equipo eq = listaMantenimiento.get(indiceActual);
            txtSerie.setText(eq.getNumeroSerie());
            txtTipo.setText(String.valueOf(eq.getIdTipo()));
            txtMarca.setText(eq.getMarca());
            txtModelo.setText(eq.getModelo());
            txtUbicacion.setText(String.valueOf(eq.getIdUbicacion()));
            indiceActual++;
        } else {
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "No hay equipos en mantenimiento.");
        }
    }

    private void atenderEquipo() {
        if (!txtSerie.getText().isEmpty()) {
            if(equipoDAO.actualizarEstadoEquipo(txtSerie.getText(), 1)) {
                JOptionPane.showMessageDialog(this, "Equipo atendido correctamente.");
                cargarListaMantenimiento();
            }
        }
    }

    private void darDeBaja() {
        if (!txtSerie.getText().isEmpty()) {
            if(equipoDAO.actualizarEstadoEquipo(txtSerie.getText(), 3)) {
                JOptionPane.showMessageDialog(this, "Equipo dado de baja.");
                cargarListaMantenimiento();
            }
        }
    }

    private void limpiarCampos() {
        txtSerie.setText(""); txtTipo.setText(""); txtMarca.setText(""); 
        txtModelo.setText(""); txtUbicacion.setText("");
    }

    private void addField(JPanel p, GridBagConstraints gbc, String label, JTextField f, int y) {
        gbc.gridx = 0; gbc.gridy = y; p.add(crearEtiquetaFormulario(label), gbc);
        gbc.gridx = 1; p.add(f, gbc);
    }
    private JLabel crearEtiquetaFormulario(String t) { JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 15)); l.setForeground(Color.WHITE); l.setPreferredSize(new Dimension(160, 25)); return l; }
    private JTextField crearCampoTextoFormulario() { JTextField c = new JTextField(20); c.setPreferredSize(new Dimension(250, 32)); c.setEditable(false); return c; }
    private JButton crearBotonEstilizado(String t) { JButton b = new JButton(t); b.setFont(new Font("Segoe UI", Font.BOLD, 13)); b.setPreferredSize(new Dimension(145, 35)); return b; }
}