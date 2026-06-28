package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Importaciones de librerías
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import java.io.File;

public class MainView extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(MainView.class);
    
    private InventarioController controller;
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JButton btnGuardar, btnExportar;

    public MainView() {
        controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
    }

    private void configurarUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setBackground(Color.WHITE);

        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        add(createField("Nombre del Producto:", txtNombre = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createField("Cantidad:", txtCantidad = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createField("Precio Unitario:", txtPrecio = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 30)));

        btnGuardar = crearBoton("Guardar Producto", new Color(41, 128, 185), new Color(52, 152, 219));
        btnGuardar.addActionListener(e -> guardar());
        add(btnGuardar);
        
        add(Box.createRigidArea(new Dimension(0, 15)));

        btnExportar = crearBoton("Exportar a Excel", new Color(39, 174, 96), new Color(46, 204, 113));
        btnExportar.addActionListener(e -> exportar());
        add(btnExportar);
    }

    private JPanel createField(String labelText, JTextField field, Font labelFont, Font fieldFont) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(300, 60)); 

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(fieldFont);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(300, 35));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);

        return panel;
    }

    private JButton crearBoton(String texto, Color colorNormal, Color colorHover) {
        JButton boton = new JButton(texto);
        boton.setBackground(colorNormal);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(300, 40));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { boton.setBackground(colorHover); }
            public void mouseExited(java.awt.event.MouseEvent evt) { boton.setBackground(colorNormal); }
        });
        return boton;
    }

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

    private void exportar() {
        try {
            File directorio = new File("reportes");
            FileUtils.forceMkdir(directorio); 
            controller.exportarInventario();
            JOptionPane.showMessageDialog(this, "Archivo Inventario_Reporte.xlsx generado.");
        } catch (Exception ex) {
            logger.error("Error al exportar inventario: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al generar el archivo.");
        }
    }
}