package com.inventario.view;

import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;

import java.awt.*;
import java.io.File;
import java.net.URL; // Importación necesaria para el logo en el JAR
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MostrarEquiposView extends JFrame { 

    // Ruta optimizada para el archivo ejecutable JAR
    private final String RUTA_LOGO = "icono.png";
    
    // Variables globales para acceder desde los botones
    private DefaultTableModel modeloTabla;
    private JComboBox<String> cmbCriterio;
    private JComboBox<String> cmbOrden;
    private EquipoDAO equipoDAO = new EquipoDAO();

    public MostrarEquiposView() {
        // 1. Configuración de ESTE marco independiente
        setTitle("Mostrar Equipos - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // No cierra todo el sistema
        setSize(920, 650); // Tamaño base
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel Principal contenedor
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(25, 40, 25, 40));
        setContentPane(panelPrincipal);

        // ==========================================
        // 2. BLOQUE SUPERIOR (Logo + Título + Criterios de Selección)
        // ==========================================
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        // --- IZQUIERDA: LOGO ---
        // Carga de imagen adaptada para funcionar dentro del JAR
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
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
        gbc.insets = new Insets(0, 10, 5, 10);
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
        cmbCriterio = crearComboBox(criterios); // Asignado a variable global
        panelCentralFiltros.add(cmbCriterio, gbc);

        // Fila 2: Ordenar por
        gbc.gridx = 0; gbc.gridy = 2;
        panelCentralFiltros.add(crearEtiqueta("Ordenar por:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        String[] ordenes = {"Ascendente", "Descendente"};
        cmbOrden = crearComboBox(ordenes); // Asignado a variable global
        panelCentralFiltros.add(cmbOrden, gbc);

        // Añadir el panel de filtros con un margen derecho para balancear el escudo
        panelCentralFiltros.setBorder(new EmptyBorder(0, 0, 0, 120));
        panelSuperior.add(panelCentralFiltros, BorderLayout.CENTER);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // ==========================================
        // 3. BLOQUE CENTRAL (Tabla JTable)
        // ==========================================
        String[] columnas = {"ID del Equipo", "Número de Serie", "Tipo de Equipo", "Marca", "Modelo", "Estado", "Ubicación"};
        modeloTabla = new DefaultTableModel(columnas, 0); // Asignado a variable global
        JTable tablaEquipos = new JTable(modeloTabla);
        
        tablaEquipos.setBackground(new Color(212, 212, 212)); 
        tablaEquipos.setFillsViewportHeight(true);
        tablaEquipos.setRowHeight(22);
        tablaEquipos.setGridColor(new Color(189, 195, 199));

        JTableHeader header = tablaEquipos.getTableHeader();
        header.setBackground(new Color(225, 225, 225));
        header.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablaEquipos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(127, 140, 141), 1));
        
        // ESCUDO ANTI-APLASTAMIENTO PARA LA TABLA
        scrollPane.setPreferredSize(new Dimension(850, 300)); 
        
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // ==========================================
        // 4. BLOQUE INFERIOR (Fila de 5 Botones de Acción)
        // ==========================================
        JPanel panelInferiorBotones = new JPanel(new BorderLayout());
        panelInferiorBotones.setOpaque(false);
        panelInferiorBotones.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Grupo Izquierda: Cargar Datos, Ordenar Lista, Limpiar Tabla
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelIzquierdo.setOpaque(false);
        
        JButton btnCargar = crearBotonEstilizado("Cargar Datos");
        JButton btnOrdenar = crearBotonEstilizado("Ordenar Lista");
        JButton btnLimpiar = crearBotonEstilizado("Limpiar Tabla");

        // --- ASIGNACIÓN DE FUNCIONALIDAD ---
        btnCargar.addActionListener(e -> cargarDatos());
        btnOrdenar.addActionListener(e -> ordenarLista());
        btnLimpiar.addActionListener(e -> limpiarTabla());

        panelIzquierdo.add(btnCargar);
        panelIzquierdo.add(btnOrdenar);
        panelIzquierdo.add(btnLimpiar);
        panelInferiorBotones.add(panelIzquierdo, BorderLayout.WEST);

        // Grupo Derecha: Menu, Salir
        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelDerecho.setOpaque(false);
        
        JButton btnMenu = crearBotonEstilizado("Menu");
        JButton btnSalir = crearBotonEstilizado("Salir");
        
        btnMenu.addActionListener(e -> this.dispose());
        btnSalir.addActionListener(e -> { if(JOptionPane.showConfirmDialog(this, "¿Salir del Sistema?")==0) System.exit(0); });
        
        panelDerecho.add(btnMenu);
        panelDerecho.add(btnSalir);
        panelInferiorBotones.add(panelDerecho, BorderLayout.EAST);

        panelPrincipal.add(panelInferiorBotones, BorderLayout.SOUTH);
        
        // Opcional: Cargar los datos automáticamente al abrir la ventana
        cargarDatos();
    }

    // ==========================================
    // MÉTODOS LÓGICOS DE LOS BOTONES
    // ==========================================
    
    private void cargarDatos() {
        modeloTabla.setRowCount(0); // Limpiamos la tabla primero
        List<Equipo> lista = equipoDAO.listarEquipos();
        for (Equipo eq : lista) {
            modeloTabla.addRow(new Object[]{
                eq.getIdEquipo(), eq.getNumeroSerie(), eq.getIdTipo(), 
                eq.getMarca(), eq.getModelo(), eq.getIdEstado(), eq.getIdUbicacion()
            });
        }
    }

    private void ordenarLista() {
        List<Equipo> lista = equipoDAO.listarEquipos();
        String criterio = cmbCriterio.getSelectedItem().toString();
        boolean ascendente = cmbOrden.getSelectedItem().toString().equals("Ascendente");

        // Lógica de ordenamiento en memoria usando una función Lambda
        lista.sort((eq1, eq2) -> {
            int comparacion = 0;
            switch(criterio) {
                case "ID del Equipo": 
                    comparacion = Integer.compare(eq1.getIdEquipo(), eq2.getIdEquipo()); 
                    break;
                case "Número de Serie": 
                    comparacion = eq1.getNumeroSerie().compareToIgnoreCase(eq2.getNumeroSerie()); 
                    break;
                case "Tipo de Equipo": 
                    comparacion = Integer.compare(eq1.getIdTipo(), eq2.getIdTipo()); 
                    break;
                case "Marca": 
                    comparacion = eq1.getMarca().compareToIgnoreCase(eq2.getMarca()); 
                    break;
                case "Modelo": 
                    comparacion = eq1.getModelo().compareToIgnoreCase(eq2.getModelo()); 
                    break;
            }
            return ascendente ? comparacion : -comparacion; // Invierte el resultado si es descendente
        });

        // Actualizar la tabla con la lista ya ordenada
        modeloTabla.setRowCount(0);
        for (Equipo eq : lista) {
            modeloTabla.addRow(new Object[]{
                eq.getIdEquipo(), eq.getNumeroSerie(), eq.getIdTipo(), 
                eq.getMarca(), eq.getModelo(), eq.getIdEstado(), eq.getIdUbicacion()
            });
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0); // Borra todas las filas de la vista actual
    }

    // ==========================================
    // MÉTODOS DE DISEÑO Y ESTILIZADO
    // ==========================================
    
    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(160, 25)); // Escudo
        return lbl;
    }

    private JComboBox<String> crearComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        combo.setBackground(Color.WHITE);
        combo.setForeground(Color.BLACK);
        combo.setPreferredSize(new Dimension(180, 32)); // Escudo anti-aplastamiento
        return combo;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(140, 35)); // Escudo anti-aplastamiento
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));
        return btn;
    }
}