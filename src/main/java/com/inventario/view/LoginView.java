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
        setTitle("Iniciar sesión - Colegio Claretiano Huancayo");
        setSize(760, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(1, 35, 70)); // Azul de la imagen

        // ===================== PANEL IZQUIERDO (LOGO) =====================
        JPanel leftPanel = createLeftPanel();

        // ===================== PANEL DERECHO (FORMULARIO) =====================
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(45, 55, 45, 55));

        JLabel title = new JLabel("Iniciar sesión");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(new Color(1, 35, 70));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUser = new JTextField();
        txtPass = new JPasswordField();

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setMaximumSize(new Dimension(290, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 122, 224));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 102, 204));
            }
        });

        btnLogin.addActionListener(e -> validar());

        // Ensamblaje
        rightPanel.add(title);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        rightPanel.add(createField("Usuario", txtUser, "/user.png"));   // ← Cambia el nombre
        rightPanel.add(Box.createRigidArea(new Dimension(0, 22)));
        rightPanel.add(createField("Contraseña", txtPass, "/pass.png")); // ← Cambia el nombre
        rightPanel.add(Box.createRigidArea(new Dimension(0, 45)));
        rightPanel.add(btnLogin);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(1, 35, 70));
        leftPanel.setPreferredSize(new Dimension(340, 480));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // Logo principal
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icono.png"));
            Image scaled = logoIcon.getImage().getScaledInstance(165, 180, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel.setText("Colegio Claretiano Huancayo");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombre1 = new JLabel("Colegio Claretiano", JLabel.CENTER);
        nombre1.setFont(new Font("SansSerif", Font.BOLD, 23));
        nombre1.setForeground(Color.WHITE);
        nombre1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombre2 = new JLabel("Huancayo", JLabel.CENTER);
        nombre2.setFont(new Font("SansSerif", Font.PLAIN, 19));
        nombre2.setForeground(new Color(180, 220, 255));
        nombre2.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        leftPanel.add(nombre1);
        leftPanel.add(nombre2);
        leftPanel.add(Box.createVerticalGlue());

        return leftPanel;
    }

    // Método para crear campos con tus iconos reales
    private JPanel createField(String labelText, JTextField field, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(290, 75));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(28, 28, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledIcon));
            label.setText("   " + labelText);
        } catch (Exception e) {
            label.setText(labelText); // fallback si no carga el icono
        }

        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));

        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(290, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 7)));
        panel.add(field);

        return panel;
    }

    private void validar() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", 
                "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioDAO.autenticar(user, pass)) {
            this.dispose();
            new MainMenuView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", 
                "Acceso denegado", JOptionPane.ERROR_MESSAGE);
            txtPass.setText("");
            txtPass.requestFocus();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}