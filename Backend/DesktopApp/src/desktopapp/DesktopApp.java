package desktopapp;

import javax.swing.*;
import java.awt.*;
import paneles.PanelTurnos;  // Importa tu panel de turnos

public class DesktopApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear el JFrame principal con un título
            JFrame mainFrame = new JFrame("Administración - Salud Total");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(900, 600);
            mainFrame.setLocationRelativeTo(null); // Centrar la ventana

            // Crear un contenedor tipo pestañas (JTabbedPane)
            JTabbedPane tabbedPane = new JTabbedPane();
            
            // Agregar una pestaña para Turnos usando tu clase PanelTurnos
            tabbedPane.addTab("Turnos", new PanelTurnos());
            
            // Si en el futuro creas más paneles, podrías agregarlos de esta forma:
            // tabbedPane.addTab("Agenda Médicos", new PanelAgendaMedicos());
            // tabbedPane.addTab("Pacientes", new PanelPacientes());
            // etc.
            
            // Agregar el TabbedPane al contenedor principal del JFrame
            mainFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            
            // Hacer visible la ventana
            mainFrame.setVisible(true);
        });
    }
}