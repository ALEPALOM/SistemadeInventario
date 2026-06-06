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
        setSize(360, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con un diseño más limpio
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(50, 40, 50, 40));
        mainPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Bienvenido", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUser = createField("Usuario");
        txtPass = createPasswordField("Contraseña");

        // Botón profesional con alto contraste
        JButton btnLogin = new JButton("INGRESAR");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Colores fijos: Azul fuerte de fondo y texto blanco puro
        btnLogin.setBackground(new Color(0, 102, 204)); 
        btnLogin.setForeground(Color.WHITE);
        
        // Esto es clave para que Windows respete los colores que definimos
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> validar());

        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(new JLabel("Usuario:"));
        mainPanel.add(txtUser);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(new JLabel("Contraseña:"));
        mainPanel.add(txtPass);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnLogin);

        add(mainPanel);
    }

    private JTextField createField(String placeholder) {
        JTextField f = new JTextField();
        f.setMaximumSize(new Dimension(300, 35));
        return f;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField f = new JPasswordField();
        f.setMaximumSize(new Dimension(300, 35));
        return f;
    }

    private void validar() {
        // 1. Obtenemos los valores de los campos de texto
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());

        // 2. Llamamos al DAO para autenticar
        if (usuarioDAO.autenticar(user, pass)) {
            // Credenciales correctas: cerramos login y abrimos menú
            this.dispose();
            new MainMenuView().setVisible(true); 
        } else {
            // Credenciales incorrectas
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

        // --- AQUÍ ESTÁ EL TRUCO ---
        // Al ponerlo justo aquí, se ejecuta antes de que aparezca la ventana de Login
        new UsuarioDAO().registrarUsuario("admin", "123456");
        // --------------------------

        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}