package com.inventario.view;

import java.io.File;
import com.inventario.util.ReporteManager;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.net.URL;

public class PrincipalMenuView extends JFrame {

    private final String RUTA_FONDO = "/fondo.png"; // Ruta interna en el JAR
    private Image imagenFondo;

    private final Color AZUL_OSCURO = new Color(13, 40, 74);
    private final Color DORADO = new Color(243, 156, 18);
    private final Color BLANCO_TEXTO = new Color(224, 230, 237);
    
    private JDesktopPane desktop;
    
    public PrincipalMenuView() {
        setTitle("SGE - Colegio Claretiano Huancayo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Carga de imagen de fondo usando getResource
        try {
            URL imgURL = getClass().getResource(RUTA_FONDO);
            if (imgURL != null) imagenFondo = ImageIO.read(imgURL);
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
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 220), 0, getHeight(), new Color(20, 60, 110, 220));
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
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 210), 0, getHeight(), new Color(20, 60, 110, 210));
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
        
        desktop = new JDesktopPane();
        desktop.setOpaque(false); 
        panelFondo.add(desktop, BorderLayout.CENTER); 
    }

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

    private JMenuItem crearMenuItem(String texto, String nombreIcono, boolean esPeligro) {
        JMenuItem item = new JMenuItem(texto);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setForeground(Color.WHITE);
        item.setBackground(AZUL_OSCURO);
        item.setBorder(new EmptyBorder(8, 15, 8, 15));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Carga de icono corregida para JAR
        Icon icono = obtenerIcono(nombreIcono, 18, 18);
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
                GradientPaint grad = new GradientPaint(0, 0, new Color(13, 40, 74, 240), 0, getHeight(), new Color(20, 60, 110, 230));
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

    // --- MÉTODOS DE POPUP E ICONOS CORREGIDOS ---
    private JPopupMenu crearPopupInicio() {
        JPopupMenu popup = crearPopupBase();
        JMenuItem btnP = crearMenuItem("Panel de Producto", "home.png", false);
        btnP.addActionListener (e -> abrirMainView());
        popup.add(btnP);
        JMenuItem btnLP = crearMenuItem("Listar Producto", "user.png", false);
        btnLP.addActionListener(e -> abrirConsultarProductoView());
        popup.add(btnLP);
        return popup;
    }
    private JPopupMenu crearPopupEquipos() {
        JPopupMenu popup = crearPopupBase();
        JMenuItem btnA = crearMenuItem("Administrar Equipos", "admin.png", false);
        btnA.addActionListener(e -> abrirAdministrarEquipos());
        popup.add(btnA);
        JMenuItem btnB = crearMenuItem("Buscar Equipos", "search.png", false);
        btnB.addActionListener(e -> abrirBuscarEquipos());
        popup.add(btnB);
        return popup;
    }
    private JPopupMenu crearPopupSoporte() {
        JPopupMenu popup = crearPopupBase();
        JMenuItem btnM = crearMenuItem("Mantenimiento", "tools.png", false);
        btnM.addActionListener(e -> abrirFormularioMantenimiento());
        popup.add(btnM);
        popup.add(crearMenuItem("Historial Técnico", "history.png", false));
        return popup;
    }
    private JPopupMenu crearPopupInforme() {
        JPopupMenu popup = crearPopupBase();
        JMenuItem btnControl = crearMenuItem("Control Inventario", "report.png", false);
        btnControl.addActionListener(e -> new ControlInventarioView().setVisible(true));
        popup.add(btnControl);
        JMenuItem btnExportar = crearMenuItem("Exportar PDF/Excel", "export.png", false);
        btnExportar.addActionListener(e -> mostrarSelectorExportacion());
        popup.add(btnExportar);
        popup.addSeparator();
        JMenuItem btnSalir = crearMenuItem("Salir", "exit.png", true);
        btnSalir.addActionListener(e -> System.exit(0));
        popup.add(btnSalir);
        return popup;
    }

    // MÉTODO DEFINITIVO PARA CARGAR ICONOS DESDE EL JAR
    private Icon obtenerIcono(String nombre, int w, int h) {
        URL url = getClass().getResource("/" + nombre);
        if (url != null) {
            return new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        }
        return null;
    }

    private void mostrarSelectorExportacion() {
        String[] opciones = {"Excel (.xlsx)", "PDF (.pdf)"};
        String tipo = (String) JOptionPane.showInputDialog(this, "Seleccione formato:", "Exportar", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (tipo != null) {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File(tipo.contains("PDF") ? "Reporte.pdf" : "Reporte.xlsx"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                ReporteManager rm = new ReporteManager();
                try {
                    if (tipo.contains("Excel")) rm.exportarExcel(fc.getSelectedFile());
                    else rm.exportarPDF(fc.getSelectedFile());
                    JOptionPane.showMessageDialog(this, "¡Éxito!");
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
            }
        }
    }
    
    private void abrirAdministrarEquipos() { new AdministrarEquiposView().setVisible(true); }
    private void abrirBuscarEquipos() { new BuscarEquiposView().setVisible(true); }
    private void abrirFormularioMantenimiento() { new MantenimientoView().setVisible(true); }
    private void abrirMainView(){ new MainView().setVisible(true);}
    private void abrirConsultarProductoView(){ new ConsultarProductoView().setVisible(true);}
}