package com.inventario.view;

import org.apache.poi.xssf.usermodel.*; // Para Excel
import java.io.*;
import com.inventario.dao.EquipoDAO;
import com.inventario.model.Equipo;
import java.awt.*;
import java.awt.event.*;
import java.net.URL; // Importación necesaria para buscar en el JAR
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ControlInventarioView extends JFrame {

    // Ruta optimizada para el archivo ejecutable JAR
    private final String RUTA_LOGO = "icono.png";
    private JTextField txtMarca, txtOperativos, txtBaja, txtPorMarca, txtTotal;
    private EquipoDAO equipoDAO = new EquipoDAO();

    public ControlInventarioView() {
        setTitle("Control de Inventario - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(920, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(35, 50, 35, 50));
        setContentPane(panelPrincipal);

        JPanel panelSuperior = new JPanel(new BorderLayout(30, 0));
        panelSuperior.setOpaque(false);

        // Carga de imagen adaptada para funcionar dentro del JAR
        JLabel lblLogo = new JLabel();
        URL logoURL = getClass().getResource("/" + RUTA_LOGO);
        if (logoURL != null) {
            ImageIcon iconLogo = new ImageIcon(logoURL);
            Image img = iconLogo.getImage().getScaledInstance(160, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[ Escudo ]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        panelSuperior.add(lblLogo, BorderLayout.WEST);

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

        txtMarca = crearCampoTexto();
        txtOperativos = crearCampoTexto();
        txtBaja = crearCampoTexto();
        txtPorMarca = crearCampoTexto();
        txtTotal = crearCampoTexto();

        // Lógica de búsqueda automática
        txtMarca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarTotalPorMarca(txtMarca.getText());
            }
        });

        gbc.gridy = 1; addField(panelFormulario, gbc, "Buscar equipos por marca:", txtMarca, true);
        gbc.gridy = 2; addField(panelFormulario, gbc, "Equipos Operativos:", txtOperativos, false);
        gbc.gridy = 3; addField(panelFormulario, gbc, "Equipos dado de baja:", txtBaja, false);
        gbc.gridy = 4; addField(panelFormulario, gbc, "Total por marca:", txtPorMarca, false);
        gbc.gridy = 5; addField(panelFormulario, gbc, "Total de Equipos:", txtTotal, false);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelInferiorBotones.setOpaque(false);
        
        JButton btnRegistro = crearBotonEstilizado("Ver registro");
        JButton btnReporte = crearBotonEstilizado("Generar Reporte");
        JButton btnMenu = crearBotonEstilizado("Menu");
        JButton btnSalir = crearBotonEstilizado("Salir");

        btnRegistro.addActionListener(e -> new MostrarEquiposView().setVisible(true));
        btnReporte.addActionListener(e -> generarReporteExcel());
        btnMenu.addActionListener(e -> this.dispose());
        btnSalir.addActionListener(e -> System.exit(0));

        panelInferiorBotones.add(btnRegistro);
        panelInferiorBotones.add(btnReporte);
        panelInferiorBotones.add(btnMenu);
        panelInferiorBotones.add(btnSalir);

        panelPrincipal.add(panelInferiorBotones, BorderLayout.SOUTH);
        cargarTotales();
    }

    private void cargarTotales() {
        List<Equipo> lista = equipoDAO.listarEquipos();
        txtOperativos.setText(String.valueOf(lista.stream().filter(e -> e.getIdEstado() == 1).count()));
        txtBaja.setText(String.valueOf(lista.stream().filter(e -> e.getIdEstado() == 3).count()));
        txtTotal.setText(String.valueOf(lista.size()));
    }

    private void actualizarTotalPorMarca(String marca) {
        List<Equipo> lista = equipoDAO.buscarEquipos("marca", marca);
        txtPorMarca.setText(String.valueOf(lista.size()));
    }
    
    private void generarReporteExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar reporte de inventario");
        fileChooser.setSelectedFile(new File("ReporteInventario.xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivoAGuardar = fileChooser.getSelectedFile();
            
            // Declaramos el libro y la hoja DENTRO del bloque para evitar errores de alcance
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet("Inventario");
                
                // Encabezados
                String[] cols = {"ID", "Serie", "Tipo", "Marca", "Modelo", "Estado", "Ubicación"};
                var row = sheet.createRow(0);
                for(int i = 0; i < cols.length; i++) {
                    row.createCell(i).setCellValue(cols[i]);
                }
                
                // Datos
                List<Equipo> lista = equipoDAO.listarEquipos();
                int r = 1;
                for (Equipo e : lista) {
                    var f = sheet.createRow(r++);
                    f.createCell(0).setCellValue(e.getIdEquipo());
                    f.createCell(1).setCellValue(e.getNumeroSerie());
                    f.createCell(2).setCellValue(e.getIdTipo());
                    f.createCell(3).setCellValue(e.getMarca());
                    f.createCell(4).setCellValue(e.getModelo());
                    f.createCell(5).setCellValue(e.getIdEstado());
                    f.createCell(6).setCellValue(e.getIdUbicacion());
                }
                
                // Escritura del archivo
                try (FileOutputStream out = new FileOutputStream(archivoAGuardar)) {
                    workbook.write(out);
                    JOptionPane.showMessageDialog(this, "Reporte guardado con éxito.");
                }
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al generar el archivo: " + e.getMessage());
            }
        }
    }

    // Métodos auxiliares de diseño (manteniendo tu estilo)
    private void addField(JPanel p, GridBagConstraints gbc, String label, JTextField txt, boolean ed) {
        gbc.gridx = 0; p.add(crearEtiqueta(label), gbc);
        gbc.gridx = 1; txt.setEditable(ed); p.add(txt, gbc);
    }
    private JLabel crearEtiqueta(String t) {
        JLabel l = new JLabel(t); l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(Color.WHITE); l.setPreferredSize(new Dimension(240, 25)); return l;
    }
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(15);
        txt.setPreferredSize(new Dimension(250, 32));
        txt.setBackground(new Color(204, 204, 204)); return txt;
    }
    private JButton crearBotonEstilizado(String t) {
        JButton b = new JButton(t);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Asegura la negrita
        b.setPreferredSize(new Dimension(145, 35));     // Asegura el tamaño
        return b;
    }
}