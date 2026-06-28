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
    
    private JDesktopPane desktop;
    
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
        
          // 3. LA SOLUCIÓN: Inicializar el escritorio, hacerlo transparente 
        // y añadirlo al centro del diseño para no tapar tu imagen de fondo.
        desktop = new JDesktopPane();
        desktop.setOpaque(false); // Permite que se vea la imagen de fondo
        panelFondo.add(desktop, BorderLayout.CENTER); // Lo colocamos en medio de los menús
       
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
      // 1. Creamos el ítem, le asignamos la acción y luego lo agregamos
        JMenuItem btnAdministrar = crearMenuItem("Administrar Equipos", "src/iconos/admin.png", false);
        btnAdministrar.addActionListener(e -> abrirFormularioRegistro());
        popup.add(btnAdministrar);

        // 2. Hacemos lo mismo con el botón de Buscar
        JMenuItem btnBuscar = crearMenuItem("Buscar Equipos", "src/iconos/search.png", false);
        btnBuscar.addActionListener(e -> abrirFormularioConsulta());
        popup.add(btnBuscar);

        // 3. Este botón se queda sin acción hasta que crees su respectivo formulario
          JMenuItem btnListar = crearMenuItem("Mostrar Lista", "src/iconos/list.png", false);
        btnListar.addActionListener(e -> abrirFormularioLista());
        popup.add(btnListar);
        
        return popup;
    }

    private JPopupMenu crearPopupSoporte() {
        JPopupMenu popup = crearPopupBase();
        
        // 1. Creamos el ítem, le asignamos la acción y luego lo agregamos
        JMenuItem btnMantenimiento = crearMenuItem("Mantenimiento", "src/iconos/tools.png", false);
        btnMantenimiento.addActionListener(e -> abrirFormularioMantenimiento());
        popup.add(btnMantenimiento);
        
       
        popup.add(crearMenuItem("Historial Técnico", "src/iconos/history.png", false));
        return popup;
    }

    private JPopupMenu crearPopupInforme() {
        JPopupMenu popup = crearPopupBase();
        
            // 1. Creamos el ítem, le asignamos la acción y luego lo agregamos
        JMenuItem btnControlInventario = crearMenuItem("Control Inventario", "src/iconos/report.png", false);
        btnControlInventario.addActionListener(e -> abrirFormularioControlInventario());
        popup.add(btnControlInventario);
        
       
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
    
        private void abrirFormularioRegistro() {
        JInternalFrame frame = new JInternalFrame("Registro de Productos", true, true, true, true);
        frame.setSize(400, 450);
        
        MainView panelRegistro = new MainView();
        frame.add(panelRegistro);
        
        centrarFrame(frame);
        frame.setVisible(true);
        desktop.add(frame);
        frame.moveToFront();
    }
    
    private void abrirFormularioConsulta() {
        JInternalFrame frame = new JInternalFrame("Consultar Inventario", true, true, true, true);
        frame.setSize(650, 450);
        
        ConsultarProductoView panelConsulta = new ConsultarProductoView();
        frame.add(panelConsulta);
        
        centrarFrame(frame);
        frame.setVisible(true);
        desktop.add(frame);
        frame.moveToFront();
    }
    
    private void abrirFormularioLista(){
     JInternalFrame frame = new JInternalFrame("Mostrar lista", true, true, true, true);
        frame.setSize(700, 460);
        
        MostrarEquiposView panelListar = new MostrarEquiposView();
        frame.add(panelListar);
        
        desktop.add(frame);
        centrarFrame(frame);
        frame.setVisible(true);
        frame.moveToFront();
    
    }
    
    
   private void abrirFormularioMantenimiento(){
      JInternalFrame frame = new JInternalFrame("Mostrar matenimiento de equipos", true, true, true, true);
        frame.setSize(700, 460);
        
        MantenimientoView panelListar = new MantenimientoView();
        frame.add(panelListar);
        
        desktop.add(frame);
        centrarFrame(frame);
        frame.setVisible(true);
        frame.moveToFront();
       
   }
   
   private void abrirFormularioControlInventario(){
   
     JInternalFrame frame = new JInternalFrame("Mostrar control de inventario", true, true, true, true);
        frame.setSize(700, 460);
        
        ControlInventarioView panelListar = new ControlInventarioView();
        frame.add(panelListar);
        
        desktop.add(frame);
        centrarFrame(frame);
        frame.setVisible(true);
        frame.moveToFront();
   }
    // Método auxiliar para centrar cualquier ventana interna
    private void centrarFrame(JInternalFrame frame) {
        int x = (desktop.getWidth() - frame.getWidth()) / 2;
        int y = (desktop.getHeight() - frame.getHeight()) / 2;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        frame.setLocation(x, y);
    }
}
