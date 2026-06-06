package com.inventario.view;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.InputStream;

public class MainMenuView extends JFrame {
    private JDesktopPane desktop;

    public MainMenuView() {
        setTitle("Sistema - Colegio Claretiano");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 1. Menú superior
        JMenuBar menuBar = new JMenuBar();
        JMenu menuEquipos = new JMenu("EQUIPOS");
        
        JMenuItem itemRegistro = new JMenuItem("Registrar Producto");
        itemRegistro.addActionListener(e -> abrirFormularioRegistro());
        
        JMenuItem itemConsultar = new JMenuItem("Consultar Inventario");
        itemConsultar.addActionListener(e -> abrirFormularioConsulta());

        menuEquipos.add(itemRegistro);
        menuEquipos.add(itemConsultar);
        menuBar.add(menuEquipos);
        setJMenuBar(menuBar);

        // 2. Escritorio personalizado (JDesktopPane) con imagen de fondo
        desktop = new JDesktopPane() {
            private Image img;
            {
                // Carga la imagen desde resources
                java.net.URL url = getClass().getResource("/fondo.png");
                if (url != null) {
                    try {
                        img = ImageIO.read(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                setOpaque(false); // Hace que el desktop sea transparente
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (img != null) {
                    // Creamos un contexto gráfico de alta calidad
                    Graphics2D g2d = (Graphics2D) g;
                    
                    // Activamos el suavizado de interpolación (Bicubic = Alta Calidad)
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, 
                                        RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Dibujamos la imagen con este contexto mejorado
                    g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        // 3. ESTA ES LA LÍNEA QUE FALTABA: Asignar el escritorio como el panel principal
        setContentPane(desktop);
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
        frame.setSize(600, 450);
        
        ConsultarProductoView panelConsulta = new ConsultarProductoView();
        frame.add(panelConsulta);
        
        centrarFrame(frame);
        frame.setVisible(true);
        desktop.add(frame);
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