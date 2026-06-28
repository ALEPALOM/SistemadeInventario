package com.inventario.view;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {

    // Cambia estas rutas por las ubicaciones reales de tus imágenes en tu PC
    private final String RUTA_LOGO = "src/main/resources/icono.png";
    private final String RUTA_ICONO_USER = "src/main/resources/pass.png";
    private final String RUTA_ICONO_PASS = "src/main/resources/user.png";

    public LoginView() {
        // Configuración básica de la ventana (JFrame)
        setTitle("Iniciar Sesión - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // Panel Principal con el fondo azul #0D284A
        JPanel principalPanel = new JPanel(new BorderLayout());
        principalPanel.setBackground(new Color(13, 40, 74));
        principalPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        setContentPane(principalPanel);

        // 1. TÍTULO SUPERIOR ("Iniciar sesión")
        JLabel lblTitulo = new JLabel("Iniciar sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(0, 0, 20, 0));
        principalPanel.add(lblTitulo, BorderLayout.NORTH);

        // Contenedor para el Logo y el Formulario (Centro)
        JPanel centroPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centroPanel.setOpaque(false);
        principalPanel.add(centroPanel, BorderLayout.CENTER);

        // 2. PARTE IZQUIERDA: LOGO
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setOpaque(false);
        
        JLabel lblLogo = new JLabel();
        // Cargar imagen de manera segura
        if (new File(RUTA_LOGO).exists()) {
            ImageIcon iconLogo = new ImageIcon(RUTA_LOGO);
            // Escalar imagen si es necesario (ejemplo a 180px de ancho)
            Image img = iconLogo.getImage().getScaledInstance(180, -1, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } else {
            lblLogo.setText("[Logo Colegio]");
            lblLogo.setForeground(Color.LIGHT_GRAY);
        }
        logoPanel.add(lblLogo);
        centroPanel.add(logoPanel);

        // 3. PARTE DERECHA: FORMULARIO (GridBagLayout para control total de alineación)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); // Margen entre componentes
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- FILA 1: Etiqueta "Usuario" ---
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setForeground(Color.WHITE);
        formPanel.add(lblUsuario, gbc);

        // --- FILA 2: Icono Usuario + Input ---
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblIconoUser = new JLabel();
         colocalIcono(lblIconoUser, RUTA_ICONO_USER);
        formPanel.add(lblIconoUser, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        JTextField txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(new Color(240, 240, 240));
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtUsuario, gbc);

        // --- FILA 3: Etiqueta "Contraseña" ---
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblContrasena.setForeground(Color.WHITE);
        formPanel.add(lblContrasena, gbc);

        // --- FILA 4: Icono Candado + Input ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel lblIconoPass = new JLabel();
        colocalIcono(lblIconoPass, RUTA_ICONO_PASS);
        formPanel.add(lblIconoPass, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        JPasswordField txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtContrasena.setBackground(new Color(240, 240, 240));
        txtContrasena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(txtContrasena, gbc);

        // --- FILA 5: Botón "Iniciar sesión" ---
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 5, 8, 5); // Más espacio arriba del botón
        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(235, 235, 235));
        btnLogin.setForeground(new Color(30, 30, 30));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        formPanel.add(btnLogin, gbc);

        centroPanel.add(formPanel);
    }

    // Método auxiliar para cargar e instalar los iconos de 30x30 px de manera limpia
    private void colocalIcono(JLabel label, String ruta) {
        if (new File(ruta).exists()) {
            ImageIcon icon = new ImageIcon(ruta);
            Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } else {
            label.setText("O"); // Marcador en caso de que no encuentre el archivo
            label.setForeground(Color.CYAN);
        }
    }

    public static void main(String[] args) {
        // Ejecutar la interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}