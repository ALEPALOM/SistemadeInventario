package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import javax.swing.*;
import java.awt.*;
import java.net.URL; // Para la carga del logo
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Importaciones de librerías
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class MainView extends JFrame { 
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    
    private InventarioController controller;
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JButton btnGuardar, btnExportar, btnCerrar;
    
    // Ruta del logo para el ejecutable
    private final String RUTA_LOGO = "icono.png";

    public MainView() {
        setTitle("Panel de Producto - Colegio Claretiano Huancayo");
        setSize(920, 650); // Tamaño estandarizado con tus demás ventanas
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
    }

    private void configurarUI() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 20));
        panelPrincipal.setBackground(new Color(13, 40, 74));
        panelPrincipal.setBorder(new EmptyBorder(35, 50, 35, 50));
        setContentPane(panelPrincipal);

        // SECCIÓN SUPERIOR: Logo y Formulario
        JPanel panelSuperior = new JPanel(new BorderLayout(30, 0));
        panelSuperior.setOpaque(false);

        // Carga del Logo
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

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 15);

        // Título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 35, 0);
        JLabel lblTitulo = new JLabel("Registro de Producto", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitulo.setForeground(Color.WHITE);
        panelFormulario.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 15);

        // Inicializar campos de texto con el estilo
        txtNombre = crearCampoTexto();
        txtCantidad = crearCampoTexto();
        txtPrecio = crearCampoTexto();

        // Agregar campos al GridBagLayout
        addField(panelFormulario, gbc, "Nombre del Producto:", txtNombre, 1);
        addField(panelFormulario, gbc, "Cantidad:", txtCantidad, 2);
        addField(panelFormulario, gbc, "Precio Unitario:", txtPrecio, 3);

        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // SECCIÓN INFERIOR: Botones
        JPanel panelInferiorBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        panelInferiorBotones.setOpaque(false);
        
        btnGuardar = crearBotonEstilizado("Guardar Producto");
        btnExportar = crearBotonEstilizado("Exportar a Excel");
        btnCerrar = crearBotonEstilizado("Cerrar");

        btnGuardar.addActionListener(e -> guardar());
        btnExportar.addActionListener(e -> exportar());
        btnCerrar.addActionListener(e -> this.dispose());

        panelInferiorBotones.add(btnGuardar);
        panelInferiorBotones.add(btnExportar);
        panelInferiorBotones.add(btnCerrar);

        panelPrincipal.add(panelInferiorBotones, BorderLayout.SOUTH);
    }

    // --- MÉTODOS DE DISEÑO (ESTILO CORPORATIVO) ---

    private void addField(JPanel p, GridBagConstraints gbc, String label, JTextField txt, int y) {
        gbc.gridx = 0; gbc.gridy = y; p.add(crearEtiqueta(label), gbc);
        gbc.gridx = 1; p.add(txt, gbc);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto, SwingConstants.RIGHT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(200, 25));
        return lbl;
    }

    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField(15);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBackground(new Color(204, 204, 204));
        txt.setForeground(Color.BLACK);
        txt.setPreferredSize(new Dimension(250, 32));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(127, 140, 141), 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)
        ));
        return txt;
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(160, 35));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(133, 146, 158), 2, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return btn;
    }

    // --- LÓGICA DE DATOS ---

    private void guardar() {
        String nombre = txtNombre.getText();
        String cantidadStr = txtCantidad.getText();
        String precioStr = txtPrecio.getText();

        if (Strings.isNullOrEmpty(nombre) || Strings.isNullOrEmpty(cantidadStr) || Strings.isNullOrEmpty(precioStr)) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);
            controller.registrarProducto(nombre, cantidad, precio);
            
            JOptionPane.showMessageDialog(this, "¡Éxito! Producto guardado.");
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Cantidad y precio deben ser números.");
        } catch (Exception ex) {
            logger.error("Error al guardar: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error técnico registrado en los logs.");
        }
    }

    // MODIFICADO: Selector de ubicación mejorado
    private void exportar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione dónde guardar el reporte de productos");
        fileChooser.setSelectedFile(new File("Inventario_Reporte.xlsx"));
        
        int seleccionUsuario = fileChooser.showSaveDialog(this);
        
        if (seleccionUsuario == JFileChooser.APPROVE_OPTION) {
            File archivoDestino = fileChooser.getSelectedFile();
            try {
                // Aseguramos que la carpeta reportes exista por si el controlador la requiere
                File directorioTemporal = new File("reportes");
                if (!directorioTemporal.exists()) {
                    directorioTemporal.mkdirs();
                }
                
                // Ejecutamos la lógica de tu controlador original
                controller.exportarInventario();
                
                // Buscamos dónde guardó el controlador el archivo (en la raíz o en reportes)
                File archivoEnRaiz = new File("Inventario_Reporte.xlsx");
                File archivoEnCarpeta = new File(directorioTemporal, "Inventario_Reporte.xlsx");
                
                File archivoEncontrado = null;
                
                if (archivoEnCarpeta.exists()) {
                    archivoEncontrado = archivoEnCarpeta;
                } else if (archivoEnRaiz.exists()) {
                    archivoEncontrado = archivoEnRaiz;
                }
                
                // Si encontramos el archivo, lo movemos a donde escogiste
                if (archivoEncontrado != null) {
                    if (archivoDestino.exists()) {
                        archivoDestino.delete(); 
                    }
                    FileUtils.moveFile(archivoEncontrado, archivoDestino);
                    JOptionPane.showMessageDialog(this, "Reporte exportado correctamente en:\n" + archivoDestino.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, "Error: No se ubicó el archivo base de exportación.\nRevisa la configuración interna del controlador.");
                }
            } catch (Exception ex) {
                logger.error("Error al exportar inventario: {}", ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error en el proceso de almacenamiento del archivo.");
            }
        }
    }
}