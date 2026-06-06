package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainView extends JPanel {
    // Definimos el logger para esta clase
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

        btnGuardar = new JButton("Guardar Producto");
        btnGuardar.setBackground(new Color(41, 128, 185));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setOpaque(true);
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(250, 40));
        btnGuardar.addActionListener(e -> guardar());
        
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(41, 128, 185));
            }
        });
        
        add(btnGuardar);
        add(Box.createRigidArea(new Dimension(0, 15)));

        btnExportar = new JButton("Exportar a Excel");
        btnExportar.setBackground(new Color(39, 174, 96));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setOpaque(true);
        btnExportar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExportar.setMaximumSize(new Dimension(250, 40));
        btnExportar.addActionListener(e -> exportar());

        btnExportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExportar.setBackground(new Color(46, 204, 113));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExportar.setBackground(new Color(39, 174, 96));
            }
        });

        add(btnExportar);
    }

    private JPanel createField(String labelText, JTextField field, Font labelFont, Font fieldFont) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(250, 60)); 

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(fieldFont);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(250, 35));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);

        return panel;
    }

    private void guardar() {
        String nombre = txtNombre.getText();
        String cantidadStr = txtCantidad.getText();
        String precioStr = txtPrecio.getText();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
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
            // AQUÍ REGISTRAMOS EL ERROR DE FORMA PROFESIONAL
            logger.error("Error técnico al intentar guardar el producto: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error técnico registrado en el sistema. Contacte al administrador.");
        }
    }

    private void exportar() {
        try {
            controller.exportarInventario();
            JOptionPane.showMessageDialog(this, "Archivo Inventario_Reporte.xlsx generado.");
        } catch (Exception ex) {
            logger.error("Error al exportar inventario: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al generar el archivo.");
        }
    }
}