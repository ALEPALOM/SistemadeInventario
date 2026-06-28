package com.inventario.view;

/**
 *
 * @author Luis Daniel
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PrincipalMenuView extends JFrame {

    private final String RUTA_FONDO = "src/main/resources/fondo.png";
    private Image imagenFondo;

    private final Color AZUL_OSCURO = new Color(13, 40, 74);
    private final Color DORADO = new Color(243, 156, 18);
    private final Color BLANCO_TEXTO = new Color(224, 230, 237);

    public PrincipalMenuView() {
        setTitle("SGE - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            File bgFile = new File(RUTA_FONDO);
            if (bgFile.exists()) imagenFondo = ImageIO.read(bgFile);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el fondo.");
        }

        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null)
                    g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(13, 40, 74, 100));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        setContentPane(panelFondo);

        // Menú superior
        JPanel contenedorIsla = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorIsla.setOpaque(false);
        contenedorIsla.setBorder(new EmptyBorder(25, 0, 0, 0));

        JPanel islaDinamica = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 220),
                                                       0, getHeight(), new Color(20, 60, 110, 220));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(243, 156, 18, 120));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
                g2.dispose();
            }
        };
        islaDinamica.setOpaque(false);

        islaDinamica.add(crearBotonMenu("INICIO", crearPopupInicio()));
        islaDinamica.add(crearBotonMenu("EQUIPOS", crearPopupEquipos()));
        islaDinamica.add(crearBotonMenu("SOPORTE", crearPopupSoporte()));
        islaDinamica.add(crearBotonMenu("INFORME", crearPopupInforme()));

        contenedorIsla.add(islaDinamica);
        panelFondo.add(contenedorIsla, BorderLayout.NORTH);

        // Widget inferior
        JPanel contenedorWidget = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        contenedorWidget.setOpaque(false);
        contenedorWidget.setBorder(new EmptyBorder(0, 0, 30, 30));

        JPanel widgetEstado = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 210),
                                                       0, getHeight(), new Color(20, 60, 110, 210));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        widgetEstado.setOpaque(false);
        widgetEstado.setLayout(new BoxLayout(widgetEstado, BoxLayout.Y_AXIS));
        widgetEstado.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTituloWidget = new JLabel("SESIÓN ACTIVA");
        lblTituloWidget.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTituloWidget.setForeground(DORADO);

        JLabel lblUsuario = new JLabel("Administrador SGE");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuario.setForeground(Color.WHITE);

        JLabel lblConexion = new JLabel("● Base de datos conectada");
        lblConexion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblConexion.setForeground(new Color(46, 204, 113));

        widgetEstado.add(lblTituloWidget);
        widgetEstado.add(Box.createVerticalStrut(5));
        widgetEstado.add(lblUsuario);
        widgetEstado.add(Box.createVerticalStrut(8));
        widgetEstado.add(lblConexion);

        contenedorWidget.add(widgetEstado);
        panelFondo.add(contenedorWidget, BorderLayout.SOUTH);
    }

    // Helpers
    private JButton crearBotonMenu(String texto, JPopupMenu popup) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(BLANCO_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setForeground(DORADO); }
            @Override public void mouseExited(MouseEvent e) { btn.setForeground(BLANCO_TEXTO); }
        });

        btn.addActionListener(e -> popup.show(btn, 10, btn.getHeight() + 8));
        return btn;
    }

    private JMenuItem crearMenuItem(String texto, String rutaIconoLocal, boolean esPeligro) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setForeground(Color.WHITE);
        item.setBackground(AZUL_OSCURO);
        item.setBorder(new EmptyBorder(8, 15, 8, 15));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Icon icono = obtenerIconoLocal(rutaIconoLocal, 18, 18);
        if (icono != null) item.setIcon(icono);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                item.setBackground(esPeligro ? new Color(231, 76, 60) : DORADO);
                item.setForeground(esPeligro ? Color.WHITE : AZUL_OSCURO);
            }
            @Override public void mouseExited(MouseEvent e) {
                item.setBackground(AZUL_OSCURO);
                item.setForeground(Color.WHITE);
            }
        });
        return item;
    }

    private JPopupMenu crearPopupBase() {
        JPopupMenu popup = new JPopupMenu() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 240),
                                                       0, getHeight(), new Color(20, 60, 110, 230));
                g2.setPaint(grad);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(243, 156, 18, 120));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        popup.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        popup.setLayout(new GridLayout(0, 1, 5, 5));
        return popup;
    }

      // Submenús
    private JPopupMenu crearPopupInicio() {
        JPopupMenu popup = crearPopupBase();
        popup.add(crearMenuItem("Panel de Control", "src/iconos/home.png", false));
        popup.add(crearMenuItem("Cambiar Usuario", "src/iconos/user.png", false));
        return popup;
    }

    private JPopupMenu crearPopupEquipos() {
        JPopupMenu popup = crearPopupBase();
        popup.add(crearMenuItem("Administrar Equipos", "src/iconos/admin.png", false));
        popup.add(crearMenuItem("Buscar Equipos", "src/iconos/search.png", false));
        popup.add(crearMenuItem("Mostrar Lista", "src/iconos/list.png", false));
        return popup;
    }

    private JPopupMenu crearPopupSoporte() {
        JPopupMenu popup = crearPopupBase();
        popup.add(crearMenuItem("Mantenimiento", "src/iconos/tools.png", false));
        popup.add(crearMenuItem("Historial Técnico", "src/iconos/history.png", false));
        return popup;
    }

    private JPopupMenu crearPopupInforme() {
        JPopupMenu popup = crearPopupBase();
        popup.add(crearMenuItem("Control Inventario", "src/iconos/report.png", false));
        popup.add(crearMenuItem("Exportar PDF/Excel", "src/iconos/export.png", false));
        popup.addSeparator();

        JMenuItem btnSalir = crearMenuItem("Salir del Sistema", "src/iconos/exit.png", true);
        btnSalir.addActionListener(e -> System.exit(0));
        popup.add(btnSalir);

        return popup;
    }

    // Cargar íconos locales
    private ImageIcon obtenerIconoLocal(String ruta, int ancho, int alto) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            try {
                Image img = ImageIO.read(archivo);
                return new ImageIcon(img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH));
            } catch (Exception e) {
                System.out.println("Error cargando icono: " + ruta);
            }
        }
        return null;
    }

    // Main
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new PrincipalMenuView().setVisible(true);
        });
    }
}
