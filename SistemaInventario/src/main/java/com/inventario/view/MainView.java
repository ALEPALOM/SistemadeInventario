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
        try {
            String nombre = txtNombre.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            double precio = Double.parseDouble(txtPrecio.getText());
            
            controller.registrarProducto(nombre, cantidad, precio);
            JOptionPane.showMessageDialog(this, "Producto registrado correctamente.");
            
            txtNombre.setText("");
            txtCantidad.setText("");
            txtPrecio.setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad y precio deben ser numéricos.", "Error de Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
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