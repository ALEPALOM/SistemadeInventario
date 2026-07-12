package com.inventario.view;

import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AdministrarEquiposView extends JFrame {

    private JTextField txtSerie, txtTipo, txtMarca, txtModelo, txtUbicacion;
    private JComboBox<String> cmbEstado;
    private JTable tablaEquipos;
    private DefaultTableModel modeloTabla;
    
    private EquipoDAO equipoDAO = new EquipoDAO();
    private Equipo equipo = new Equipo();
    private int idEquipoSeleccionado = -1;

    // Ruta actualizada para buscar dentro del JAR
    private final String RUTA_LOGO = "icono.png";

    public AdministrarEquiposView() {
        setTitle("Administrar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ventana independiente
        
        // Reducimos ligeramente la altura total para que no choque con tu barra de tareas de Windows
        setSize(980, 680); 
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(20, 40, 20, 40));
        setContentPane(panelPrincipal);

        JLabel lblTitulo = new JLabel("Administrar Equipos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 10, 0)); 
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelControl = new JPanel(new BorderLayout(25, 0));
        panelControl.setOpaque(false);

        // Logo con carga adaptada para el JAR
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
            Image img = iconLogo.getImage().getScaledInstance(150, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        panelControl.add(lblLogo, BorderLayout.WEST);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 10); // Más espacio para que no se peguen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSerie = crearCampoTexto();
        txtTipo = crearCampoTexto();
        txtMarca = crearCampoTexto();
        txtModelo = crearCampoTexto();
        cmbEstado = new JComboBox<>(new String[]{"Operativo", "Mantenimiento", "De Baja"});
        cmbEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbEstado.setBackground(Color.WHITE);
        cmbEstado.setPreferredSize(new Dimension(200, 32)); // EVITA QUE SE APLASTE
        txtUbicacion = crearCampoTexto();

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(crearEtiqueta("Número de Serie:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelFormulario.add(txtSerie, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(crearEtiqueta("Tipo de Equipo (ID):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelFormulario.add(txtTipo, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(crearEtiqueta("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelFormulario.add(txtMarca, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(crearEtiqueta("Modelo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelFormulario.add(txtModelo, gbc);
        gbc.gridx = 0; gbc.gridy = 4; panelFormulario.add(crearEtiqueta("Estado:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panelFormulario.add(cmbEstado, gbc);
        gbc.gridx = 0; gbc.gridy = 5; panelFormulario.add(crearEtiqueta("Ubicación (ID):"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; panelFormulario.add(txtUbicacion, gbc);

        panelControl.add(panelFormulario, BorderLayout.CENTER);

        // Botones
        JPanel panelAccionesGrid = new JPanel(new GridLayout(3, 2, 15, 12));
        panelAccionesGrid.setOpaque(false);
        panelAccionesGrid.setBorder(new EmptyBorder(10, 10, 10, 0));

        JButton btnRegistrar = crearBotonAccion("Registrar Equipo"); btnRegistrar.addActionListener(e -> registrarEquipo());
        JButton btnEditar = crearBotonAccion("Editar Equipo"); btnEditar.addActionListener(e -> cargarEquipoParaEdicion());
        JButton btnEliminar = crearBotonAccion("Eliminar Equipo"); btnEliminar.addActionListener(e -> eliminarEquipo());
        JButton btnVer = crearBotonAccion("Ver Registros"); btnVer.addActionListener(e -> listarEquipos());
        JButton btnGuardar = crearBotonAccion("Guardar Cambios"); btnGuardar.addActionListener(e -> guardarCambiosEdicion());
        JButton btnRestaurar = crearBotonAccion("Restaurar"); btnRestaurar.addActionListener(e -> restaurarEquipoManual());

        panelAccionesGrid.add(btnRegistrar); panelAccionesGrid.add(btnEditar);
        panelAccionesGrid.add(btnEliminar); panelAccionesGrid.add(btnVer);
        panelAccionesGrid.add(btnGuardar); panelAccionesGrid.add(btnRestaurar);

        panelControl.add(panelAccionesGrid, BorderLayout.EAST);
        panelPrincipal.add(panelControl, BorderLayout.CENTER);

        // Tabla inferior
        JPanel panelInferior = new JPanel(new BorderLayout(0, 15));
        panelInferior.setOpaque(false);

        String[] columnas = {"ID del Equipo", "Número de Serie", "Tipo de Equipo", "Marca", "Modelo", "Estado", "Ubicación"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEquipos = new JTable(modeloTabla);
        
        tablaEquipos.setBackground(new Color(212, 212, 212)); 
        tablaEquipos.setFillsViewportHeight(true);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setGridColor(new Color(189, 195, 199));

        JTableHeader header = tablaEquipos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        // Reducimos la altura de la tabla a 180 para que el formulario respire y no se aplaste
        scrollPane.setPreferredSize(new Dimension(880, 180)); 
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        // Navegación
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);

        JButton btnMenu = crearBotonAccion("Menu");
        JButton btnSalir = crearBotonAccion("Salir");

        btnMenu.addActionListener(e -> { this.dispose(); });
        btnSalir.addActionListener(e -> { if(JOptionPane.showConfirmDialog(this, "¿Salir?")==0) System.exit(0); });

        panelNavegacion.add(btnMenu); panelNavegacion.add(btnSalir);
        panelInferior.add(panelNavegacion, BorderLayout.SOUTH);

        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        listarEquipos();
    }

    // --- MÉTODOS DE DISEÑO PROTEGIDOS CONTRA APLASTAMIENTO ---
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
        txt.setBackground(new Color(204, 204, 204));
        txt.setForeground(Color.BLACK);
        // MAGIA: Esto impide que el cuadro de texto se convierta en una línea delgada
        txt.setPreferredSize(new Dimension(200, 32)); 
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
        // MODIFICADO: Ancho aumentado a 160 (más largos) y altura a 30 (menos altos)
        btn.setPreferredSize(new Dimension(160, 30)); 
        // MODIFICADO: Márgenes internos ajustados para dar más espacio a los textos largos
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(4, 5, 4, 5)
        ));
        return btn;
    }

    // --- LÓGICA DE DATOS ---
    private void listarEquipos() { modeloTabla.setRowCount(0); for (Equipo eq : equipoDAO.listarEquipos()) { modeloTabla.addRow(new Object[]{eq.getIdEquipo(), eq.getNumeroSerie(), eq.getIdTipo(), eq.getMarca(), eq.getModelo(), eq.getIdEstado(), eq.getIdUbicacion()}); } }
    private void registrarEquipo() { if (camposEstanVacios()) return; String serie = txtSerie.getText().trim(); Equipo existente = equipoDAO.buscarEquipoPorSerie(serie); if (existente != null) { if (JOptionPane.showConfirmDialog(this, "Equipo dado de baja. ¿Restaurar?", "Info", JOptionPane.YES_NO_OPTION) == 0) { equipoDAO.restaurarEquipo(existente.getIdEquipo()); listarEquipos(); limpiarCampos(); } } else { try { Equipo nuevo = new Equipo(); nuevo.setNumeroSerie(serie); nuevo.setIdTipo(Integer.parseInt(txtTipo.getText())); nuevo.setMarca(txtMarca.getText()); nuevo.setModelo(txtModelo.getText()); nuevo.setIdEstado(cmbEstado.getSelectedIndex() + 1); nuevo.setIdUbicacion(Integer.parseInt(txtUbicacion.getText())); if(equipoDAO.registrarEquipo(nuevo)){ listarEquipos(); limpiarCampos(); } } catch (Exception e) { JOptionPane.showMessageDialog(this, "Error numérico"); } } }
    private void cargarEquipoParaEdicion() { int fila = tablaEquipos.getSelectedRow(); if (fila != -1) { idEquipoSeleccionado = Integer.parseInt(tablaEquipos.getValueAt(fila, 0).toString()); txtSerie.setText(tablaEquipos.getValueAt(fila, 1).toString()); txtTipo.setText(tablaEquipos.getValueAt(fila, 2).toString()); txtMarca.setText(tablaEquipos.getValueAt(fila, 3).toString()); txtModelo.setText(tablaEquipos.getValueAt(fila, 4).toString()); cmbEstado.setSelectedIndex(Integer.parseInt(tablaEquipos.getValueAt(fila, 5).toString()) - 1); txtUbicacion.setText(tablaEquipos.getValueAt(fila, 6).toString()); } }
    private void guardarCambiosEdicion() { if (idEquipoSeleccionado != -1) { equipo.setIdEquipo(idEquipoSeleccionado); equipo.setNumeroSerie(txtSerie.getText()); equipo.setIdTipo(Integer.parseInt(txtTipo.getText())); equipo.setMarca(txtMarca.getText()); equipo.setModelo(txtModelo.getText()); equipo.setIdEstado(cmbEstado.getSelectedIndex() + 1); equipo.setIdUbicacion(Integer.parseInt(txtUbicacion.getText())); if(equipoDAO.actualizarEquipo(equipo)){ listarEquipos(); limpiarCampos(); } } }
    private void eliminarEquipo() { int fila = tablaEquipos.getSelectedRow(); if (fila != -1) { int id = Integer.parseInt(tablaEquipos.getValueAt(fila, 0).toString()); if(JOptionPane.showConfirmDialog(this, "¿Dar de baja?") == 0) { equipoDAO.eliminarEquipo(id); listarEquipos(); } } }
    private void restaurarEquipoManual() { String serie = JOptionPane.showInputDialog(this, "Serie a restaurar:"); if (serie != null) { Equipo eq = equipoDAO.buscarEquipoPorSerie(serie); if (eq != null && equipoDAO.restaurarEquipo(eq.getIdEquipo())) { JOptionPane.showMessageDialog(this, "Restaurado"); listarEquipos(); } } }
    private void limpiarCampos() { txtSerie.setText(""); txtTipo.setText(""); txtMarca.setText(""); txtModelo.setText(""); txtUbicacion.setText(""); idEquipoSeleccionado = -1; }
    private boolean camposEstanVacios() { return txtSerie.getText().isEmpty() || txtTipo.getText().isEmpty() || txtMarca.getText().isEmpty() || txtModelo.getText().isEmpty() || txtUbicacion.getText().isEmpty(); }
}