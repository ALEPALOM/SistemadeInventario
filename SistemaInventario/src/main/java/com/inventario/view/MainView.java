package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class MainView extends JPanel {
    private InventarioController controller;
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JButton btnGuardar, btnExportar;

    public MainView() {
        // Se inyecta la dependencia aquí
        controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
    }

    private void configurarUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(40, 40, 40, 40)); // Más espacio interno
        setBackground(Color.WHITE);

        // Fuente elegante
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Campos
        add(createField("Nombre del Producto:", txtNombre = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createField("Cantidad:", txtCantidad = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(createField("Precio Unitario:", txtPrecio = new JTextField(), labelFont, fieldFont));
        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- BOTÓN GUARDAR MODERNO ---
        btnGuardar = new JButton("Guardar Producto");
        btnGuardar.setBackground(new Color(41, 128, 185)); // Azul profesional
        btnGuardar.setForeground(Color.WHITE);            // Letra blanca
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);                
        btnGuardar.setBorderPainted(false);               
        btnGuardar.setOpaque(true);                       
        btnGuardar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGuardar.setMaximumSize(new Dimension(250, 40)); // Mantiene el tamaño uniforme
        btnGuardar.addActionListener(e -> guardar()); // Vuelve a vincular la acción de guardar
        
        // Efecto hover (cambia de color al pasar el ratón)
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(52, 152, 219)); // Azul más claro
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGuardar.setBackground(new Color(41, 128, 185)); // Vuelve al original
            }
        });
        
        add(btnGuardar);
        add(Box.createRigidArea(new Dimension(0, 15))); // Espacio entre botones

        // --- BOTÓN EXPORTAR MODERNO ---
        btnExportar = new JButton("Exportar a Excel");
        btnExportar.setBackground(new Color(39, 174, 96)); // Verde profesional
        btnExportar.setForeground(Color.WHITE);
        btnExportar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setOpaque(true);
        btnExportar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExportar.setMaximumSize(new Dimension(250, 40));
        btnExportar.addActionListener(e -> exportar());

        // Efecto hover para el botón exportar
        btnExportar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnExportar.setBackground(new Color(46, 204, 113)); // Verde más claro
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExportar.setBackground(new Color(39, 174, 96)); // Vuelve al original
            }
        });

        add(btnExportar);
    }

    // --- MÉTODO FALTANTE PARA CREAR LOS CAMPOS UNIFORMES ---
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
            
            // Intentamos guardar
            controller.registrarProducto(nombre, cantidad, precio);
            
            JOptionPane.showMessageDialog(this, "¡Éxito! Producto guardado.");
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Cantidad y precio deben ser números.");
        } catch (Exception ex) {
            ex.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "Error técnico: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
    }

    private void exportar() {
        controller.exportarInventario();
        JOptionPane.showMessageDialog(this, "Archivo Inventario_Reporte.xlsx generado en la raíz del proyecto.");
    }
}