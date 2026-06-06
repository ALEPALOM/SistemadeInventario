package com.inventario.view;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {
    private JDesktopPane desktop;

    public MainMenuView() {
        setTitle("Sistema - Colegio Claretiano");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Menú superior
        JMenuBar menuBar = new JMenuBar();
        JMenu menuEquipos = new JMenu("EQUIPOS");
        JMenuItem itemRegistro = new JMenuItem("Registrar Producto");
        
        itemRegistro.addActionListener(e -> abrirFormularioRegistro());
        
        menuEquipos.add(itemRegistro);
        menuBar.add(menuEquipos);
        setJMenuBar(menuBar);

        // Escritorio para ventanas internas
        desktop = new JDesktopPane();
        desktop.setBackground(new Color(20, 30, 50)); // Azul oscuro institucional
        add(desktop);
    }

    private void abrirFormularioRegistro() {
    JInternalFrame frame = new JInternalFrame("Registro de Productos", true, true, true, true);
    // Le damos un poco más de alto por el nuevo botón de Exportar
    frame.setSize(400, 450); 
    
    // Instanciamos el panel
    MainView panelRegistro = new MainView();
    frame.add(panelRegistro); 
    
    // --- LÓGICA PARA CENTRAR LA VENTANA ---
    // Calculamos el centro exacto restando el tamaño del frame al tamaño del escritorio
    int x = (desktop.getWidth() - frame.getWidth()) / 2;
    int y = (desktop.getHeight() - frame.getHeight()) / 2;
    
    // Si por alguna razón el cálculo da negativo (pantalla muy chica), evitamos que se salga
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    
    frame.setLocation(x, y);
    // --------------------------------------
    
    frame.setVisible(true);
    desktop.add(frame);
    frame.moveToFront();
    }
}