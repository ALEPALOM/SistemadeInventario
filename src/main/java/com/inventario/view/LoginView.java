package com.inventario.view;

import com.inventario.dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginView() {
        setTitle("Sistema de Inventario - Acceso");
        setSize(380, 480); // Un poco más ancho y alto para mayor elegancia
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con diseño inmaculado
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 40, 50, 40));
        mainPanel.setBackground(Color.WHITE); // Fondo completamente blanco

        // Fuentes modernas
        Font titleFont = new Font("SansSerif", Font.BOLD, 26);
        Font labelFont = new Font("SansSerif", Font.BOLD, 12);
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 14);

        // Título
        JLabel title = new JLabel("Bienvenido", JLabel.CENTER);
        title.setFont(titleFont);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(50, 50, 50)); // Gris muy oscuro, más elegante que el negro puro

        // Instanciamos los campos antes de pasarlos al contenedor
        txtUser = new JTextField();
        txtPass = new JPasswordField();

        // Botón profesional con estilo Flat Design
        JButton btnLogin = new JButton("INGRESAR");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setBackground(new Color(41, 128, 185)); // Mismo azul profesional del registro
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(280, 40)); // Tamaño uniforme

        // Efecto Hover para el botón
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(52, 152, 219)); // Azul más claro al pasar el ratón
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(41, 128, 185)); // Vuelve al color original
            }
        });
        
        btnLogin.addActionListener(e -> validar());

        // Ensamblado del panel
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Espacio bajo el título
        mainPanel.add(createFieldContainer("Usuario:", txtUser, labelFont, fieldFont));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio entre campos
        mainPanel.add(createFieldContainer("Contraseña:", txtPass, labelFont, fieldFont));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40))); // Espacio antes del botón
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    // Método para crear grupos de Etiqueta + Campo de texto con estilo uniforme
    private JPanel createFieldContainer(String labelText, JTextField field, Font labelFont, Font fieldFont) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(280, 65)); 

        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setForeground(new Color(100, 100, 100)); // Etiquetas en gris moderno
        
        field.setFont(fieldFont);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(280, 35));
        
        // Borde personalizado: Línea gris clara con padding interno para que el texto no se pegue
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);

        return panel;
    }

    private void validar() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        if (usuarioDAO.autenticar(user, pass)) {
            this.dispose();
            new MainMenuView().setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(this, 
                "Credenciales incorrectas.", 
                "Acceso denegado", 
                JOptionPane.ERROR_MESSAGE);
            
            txtPass.setText(""); 
            txtPass.requestFocus();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        // IMPORTANTE: Si ya creaste el usuario "admin" en la base de datos, 
        // comenta esta línea poniéndole '//' al inicio para evitar errores de duplicidad.
        // new UsuarioDAO().registrarUsuario("admin", "123456");

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}