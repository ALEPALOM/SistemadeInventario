package com.inventario.view;

import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;
import java.awt.*;
import java.net.URL;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class BuscarEquiposView extends JFrame {

    // Ruta optimizada para buscar el recurso dentro del archivo JAR
    private final String RUTA_LOGO = "icono.png";
    private JTextField txtInformacion;
    private JComboBox<String> cmbBuscarPor;
    private JTable tablaEquipos;
    private DefaultTableModel modeloTabla;
    private EquipoDAO equipoDAO = new EquipoDAO();

    public BuscarEquiposView() {
        setTitle("Buscar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setSize(920, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 35, 25, 35));
        setContentPane(panelPrincipal);

        JLabel lblTitulo = new JLabel("Buscar Equipos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelControl = new JPanel(new BorderLayout(15, 0));
        panelControl.setOpaque(false);

        // Logo con carga adaptada utilizando getResource para soporte del ejecutable
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        panelControl.add(lblLogo, BorderLayout.WEST);

        // Campos Búsqueda
        JPanel panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtInformacion = new JTextField(15);
        txtInformacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInformacion.setPreferredSize(new Dimension(250, 32)); 
        txtInformacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));

        cmbBuscarPor = new JComboBox<>(new String[]{"ID del Equipo", "Número de Serie", "Marca", "Modelo"});
        cmbBuscarPor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbBuscarPor.setBackground(Color.WHITE);
        cmbBuscarPor.setPreferredSize(new Dimension(250, 32)); 

        gbc.gridx = 0; gbc.gridy = 0; panelBusqueda.add(crearLabel("Ingrese información:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelBusqueda.add(txtInformacion, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelBusqueda.add(crearLabel("Buscar por:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelBusqueda.add(cmbBuscarPor, gbc);

        panelControl.add(panelBusqueda, BorderLayout.CENTER);

        // Botones Acciones
        JPanel panelAcciones = new JPanel(new GridLayout(2, 1, 0, 15));
        panelAcciones.setOpaque(false);
        
        JButton btnBuscar = crearBotonEstilizado("Buscar");
        JButton btnLimpiar = crearBotonEstilizado("Limpiar");
        
        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnLimpiar.addActionListener(e -> { txtInformacion.setText(""); modeloTabla.setRowCount(0); });
        
        panelAcciones.add(btnBuscar); 
        panelAcciones.add(btnLimpiar);

        // SOLUCIÓN: Envolvemos los botones en un GridBagLayout para evitar que se estiren
        JPanel wrapperBotones = new JPanel(new GridBagLayout());
        wrapperBotones.setOpaque(false);
        wrapperBotones.setBorder(new EmptyBorder(0, 20, 0, 0)); // Separación de la izquierda
        wrapperBotones.add(panelAcciones);
        
        panelControl.add(wrapperBotones, BorderLayout.EAST);
        
        panelPrincipal.add(panelControl, BorderLayout.CENTER);

        // Tabla
        modeloTabla = new DefaultTableModel(new String[]{"ID", "N° Serie", "ID Tipo", "Marca", "Modelo", "Estado", "Ubicación"}, 0);
        tablaEquipos = new JTable(modeloTabla);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setBackground(new Color(212, 212, 212)); 
        tablaEquipos.setGridColor(new Color(189, 195, 199));
        
        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setPreferredSize(new Dimension(850, 200)); 
        
        // Navegación
        JPanel panelInferior = new JPanel(new BorderLayout(0, 15));
        panelInferior.setOpaque(false);
        panelInferior.add(scrollPane, BorderLayout.CENTER);

        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavegacion.setOpaque(false);
        
        JButton btnMenu = crearBotonEstilizado("Menu");
        JButton btnSalir = crearBotonEstilizado("Salir");
        
        btnMenu.addActionListener(e -> { this.dispose(); });
        btnSalir.addActionListener(e -> { if(JOptionPane.showConfirmDialog(this, "¿Salir?")==0) System.exit(0); });
        
        panelNavegacion.add(btnMenu); panelNavegacion.add(btnSalir);
        panelInferior.add(panelNavegacion, BorderLayout.SOUTH);
        
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
    }

    private void realizarBusqueda() {
        String valor = txtInformacion.getText().trim();
        String seleccion = cmbBuscarPor.getSelectedItem().toString();
        String criterio = "";
        
        switch(seleccion) {
            case "ID del Equipo": criterio = "id_equipo"; break;
            case "Número de Serie": criterio = "numero_serie"; break;
            case "Marca": criterio = "marca"; break;
            case "Modelo": criterio = "modelo"; break;
        }

        modeloTabla.setRowCount(0);
        List<Equipo> resultados = equipoDAO.buscarEquipos(criterio, valor);
        for (Equipo eq : resultados) {
            modeloTabla.addRow(new Object[]{eq.getIdEquipo(), eq.getNumeroSerie(), eq.getIdTipo(), eq.getMarca(), eq.getModelo(), eq.getIdEstado(), eq.getIdUbicacion()});
        }
    }

    private JLabel crearLabel(String t) { 
        JLabel l = new JLabel(t, SwingConstants.RIGHT); 
        l.setForeground(Color.WHITE); 
        l.setFont(new Font("Segoe UI", Font.BOLD, 15)); 
        l.setPreferredSize(new Dimension(160, 25)); 
        return l; 
    }
    
    private JButton crearBotonEstilizado(String t) { 
        JButton b = new JButton(t); 
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(Color.WHITE); 
        b.setPreferredSize(new Dimension(130, 35)); 
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return b; 
    }
}