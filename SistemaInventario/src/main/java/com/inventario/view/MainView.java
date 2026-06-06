package com.inventario.view;

import com.inventario.controller.InventarioController;
import com.inventario.dao.ProductoDAOImpl;
import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    private InventarioController controller;
    private JTextField txtNombre, txtCantidad, txtPrecio;
    private JButton btnGuardar, btnExportar;

    public MainView() {
        // Se inyecta la dependencia aquí
        controller = new InventarioController(new ProductoDAOImpl());
        configurarUI();
    }

    private void configurarUI() {
        setTitle("Sistema de Inventario Seguro MVC");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        add(new JLabel(" Nombre del Producto:"));
        txtNombre = new JTextField();
        add(txtNombre);

        add(new JLabel(" Cantidad:"));
        txtCantidad = new JTextField();
        add(txtCantidad);

        add(new JLabel(" Precio unitario:"));
        txtPrecio = new JTextField();
        add(txtPrecio);

        btnGuardar = new JButton("Guardar Producto");
        btnGuardar.addActionListener(e -> guardar());
        add(btnGuardar);

        btnExportar = new JButton("Exportar a Excel");
        btnExportar.addActionListener(e -> exportar());
        add(btnExportar);
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
        // AQUÍ ESTÁ EL TRUCO: imprimimos el error real en la consola
        ex.printStackTrace(); 
        JOptionPane.showMessageDialog(this, "Error técnico: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
    }
}
    private void exportar() {
        controller.exportarInventario();
        JOptionPane.showMessageDialog(this, "Archivo Inventario_Reporte.xlsx generado en la raíz del proyecto.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainView().setVisible(true));
    }
}