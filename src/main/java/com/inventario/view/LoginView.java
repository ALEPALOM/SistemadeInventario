package com.inventario.view;

import com.inventario.dao.UsuarioDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginView() {
        setTitle("Iniciar sesión - Colegio Claretiano Huancayo");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con gradiente azul
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(1, 35, 70), getWidth(), getHeight(), new Color(20, 60, 120));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;

        // ===================== PANEL IZQUIERDO (LOGO) =====================
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        
        // Logo con sombra y efecto
        JPanel logoContainer = new JPanel();
        logoContainer.setOpaque(false);
        logoContainer.setLayout(new BoxLayout(logoContainer, BoxLayout.Y_AXIS));
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icono.png"));
            Image scaled = logoIcon.getImage().getScaledInstance(180, 190, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 100));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Línea decorativa debajo del logo
        JPanel linePanel = new JPanel();
        linePanel.setOpaque(false);
        linePanel.setMaximumSize(new Dimension(120, 3));
        linePanel.setPreferredSize(new Dimension(120, 3));
        JLabel line = new JLabel("━━━━━━━━━━━━━━━━");
        line.setForeground(new Color(255, 215, 0, 180));
        line.setFont(new Font("Arial", Font.PLAIN, 16));
        linePanel.add(line);
        linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoContainer.add(Box.createVerticalGlue());
        logoContainer.add(logoLabel);
        logoContainer.add(Box.createRigidArea(new Dimension(0, 15)));
        logoContainer.add(linePanel);
        logoContainer.add(Box.createVerticalGlue());
        
        leftPanel.add(logoContainer);
        
        gbc.gridx = 0;
        mainPanel.add(leftPanel, gbc);

        // ===================== PANEL DERECHO (FORMULARIO) =====================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);
        
        // Tarjeta blanca con sombra para el formulario
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1),
            BorderFactory.createEmptyBorder(45, 45, 45, 45)
        ));
        cardPanel.setMaximumSize(new Dimension(380, 450));
        cardPanel.setPreferredSize(new Dimension(380, 450));
        
        // Sombra de la tarjeta
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200, 30), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Título
        JLabel title = new JLabel("Bienvenido");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(1, 35, 70));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Inicie sesión para continuar");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(120, 120, 140));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Campo Usuario con icono
        txtUser = new JTextField();
        JPanel userPanel = createModernFieldWithIcon("Usuario", txtUser, "/user.png");
        
        // Campo Contraseña con icono
        txtPass = new JPasswordField();
        JPanel passPanel = createModernFieldWithIcon("Contraseña", txtPass, "/pass.png");
        
        // Botón Iniciar sesión
        JButton btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(100, 100, 115));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setOpaque(true);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setMaximumSize(new Dimension(320, 48));
        btnLogin.setPreferredSize(new Dimension(320, 48));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Bordes redondeados para el botón
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Hover efecto
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(120, 120, 140));
                btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(100, 100, 115));
            }
        });
        
        btnLogin.addActionListener(e -> validar());
        
        // Botón de limpiar
        JButton btnClear = new JButton("Limpiar");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnClear.setBackground(new Color(240, 240, 245));
        btnClear.setForeground(new Color(100, 100, 115));
        btnClear.setFocusPainted(false);
        btnClear.setBorderPainted(false);
        btnClear.setOpaque(true);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClear.setMaximumSize(new Dimension(100, 30));
        btnClear.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnClear.addActionListener(e -> {
            txtUser.setText("");
            txtPass.setText("");
            txtUser.requestFocus();
        });
        
        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(btnClear);
        
        // Ensamblaje de la tarjeta
        cardPanel.add(title);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 35)));
        cardPanel.add(userPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(passPanel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(buttonPanel);
        
        rightPanel.add(cardPanel);
        
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        mainPanel.add(rightPanel, gbc);
        
        add(mainPanel);
    }
    
    private JPanel createModernFieldWithIcon(String labelText, JTextField field, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(350, 80));
        panel.setPreferredSize(new Dimension(350, 80));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel para el label con icono
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Cargar icono
        JLabel iconLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledIcon = icon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        } catch (Exception e) {
            iconLabel.setForeground(new Color(100, 100, 115));
            iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        }
        
        JLabel textLabel = new JLabel(" " + labelText);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        textLabel.setForeground(new Color(60, 60, 80));
        
        labelPanel.add(iconLabel);
        labelPanel.add(textLabel);
        
        // Campo de texto estilizado
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(350, 42));
        field.setPreferredSize(new Dimension(350, 42));
        field.setBackground(new Color(248, 249, 250));
        field.setForeground(new Color(50, 50, 60));
        field.setCaretColor(new Color(1, 35, 70));
        
        // Borde redondeado
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        // Efecto focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 115), 2),
                    BorderFactory.createEmptyBorder(9, 14, 9, 14)
                ));
                field.setBackground(Color.WHITE);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 230), 1),
                    BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));
                field.setBackground(new Color(248, 249, 250));
            }
        });
        
        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar('*');
        }
        
        panel.add(labelPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        return panel;
    }

    private void validar() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos.", 
                "Campos requeridos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (usuarioDAO.autenticar(user, pass)) {
            this.dispose();
            new MainMenuView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Credenciales incorrectas.\nPor favor intente nuevamente.", 
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
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}