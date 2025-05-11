package paneles;

import DataAccess.TurnoDA;
import entidades.Turno;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;

public class PanelTurnos extends JPanel {
    private TurnoDA turnoDA;
    private JTable tablaTurnos;
    private DefaultTableModel modeloTabla;
    
    public PanelTurnos() {
        turnoDA = new TurnoDA();
        setLayout(new BorderLayout());
        
        // Configuramos el modelo de la tabla con columnas importantes
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Paciente", "Profesional", "Especialidad", "Fecha Turno", "Estado"}, 0);
        tablaTurnos = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaTurnos);
        add(scroll, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        add(panelBotones, BorderLayout.SOUTH);
        
        // Acción de refrescar: carga los datos y los muestra en la tabla
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        // Acción para agregar un nuevo turno: se muestra un diálogo para ingresar la información.
        btnAgregar.addActionListener(e -> {
            Turno nuevo = mostrarDialogoTurno(null);
            if (nuevo != null) {
                boolean ok = turnoDA.insertarTurno(nuevo);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Turno agregado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar turno.");
                }
                cargarDatos();
            }
        });
        
        // Acción para editar: se toma el turno seleccionado, se muestra el diálogo con los datos y se actualiza.
        btnEditar.addActionListener(e -> {
            int filaSeleccionada = tablaTurnos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un turno para editar.");
                return;
            }
            int turnoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            // Buscamos el turno por su ID (aquí se recorre la lista obtenida, ya que el DAO tiene el método obtenerTurnos())
            List<Turno> turnos = turnoDA.obtenerTurnos();
            Turno turnoSeleccionado = null;
            for (Turno t : turnos) {
                if (t.getId() == turnoId) {
                    turnoSeleccionado = t;
                    break;
                }
            }
            if (turnoSeleccionado != null) {
                Turno actualizado = mostrarDialogoTurno(turnoSeleccionado);
                if (actualizado != null) {
                    boolean ok = turnoDA.actualizarTurno(actualizado);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Turno actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar turno.");
                    }
                    cargarDatos();
                }
            }
        });
        
        // Acción para eliminar: se elimina el turno seleccionado
        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaTurnos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un turno para eliminar.");
                return;
            }
            int turnoId = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el turno?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = turnoDA.eliminarTurno(turnoId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Turno eliminado.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar turno.");
                }
                cargarDatos();
            }
        });
        
        // Cargamos los datos iniciales al arrancar el panel.
        cargarDatos();
    }
    
    // Método que consulta la base de datos a través del DAO y carga los turnos en la tabla.
    private void cargarDatos() {
        List<Turno> turnos = turnoDA.obtenerTurnos();
        modeloTabla.setRowCount(0); // Limpia las filas anteriores
        for (Turno t : turnos) {
            modeloTabla.addRow(new Object[]{
                t.getId(),
                t.getIdPaciente(),
                t.getIdProfesional(),
                t.getIdEspecialidad(),
                t.getFechaTurno(),
                t.getEstado()
            });
        }
    }
    
    // Método que muestra un diálogo para ingresar/editar la información de un turno.
    // Si el parámetro "turno" es null, se asume que se va a agregar; si no, editará el turno dado.
    private Turno mostrarDialogoTurno(Turno turno) {
    // Campos para ID Profesional y ID Especialidad
    JTextField tfIdProfesional = new JTextField();
    JTextField tfIdEspecialidad = new JTextField();
    
    // Selector de fecha (JSpinner con SpinnerDateModel)
    JSpinner spinnerFecha = new JSpinner(new SpinnerDateModel());
    // Configuramos el editor para que muestre solo la fecha en formato "yyyy-MM-dd"
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd");
    spinnerFecha.setEditor(dateEditor);
    
    // ComboBox para seleccionar el horario. Se definen los turnos fijos:
    // Turnos de la mañana: desde 08:00 hasta 11:30 (8 opciones cada media hora)
    // Turnos de la tarde: desde 16:00 hasta 19:30 (8 opciones cada media hora)
    String[] timeSlots = {
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
        "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30"
    };
    JComboBox<String> cbHorarios = new JComboBox<>(timeSlots);
    
    // Si es edición, precargamos los valores actuales
    if (turno != null) {
        tfIdProfesional.setText(String.valueOf(turno.getIdProfesional()));
        tfIdEspecialidad.setText(String.valueOf(turno.getIdEspecialidad()));
        
        // Extraer la parte de la fecha (sin hora) de fechaTurno
        java.util.Date dateValue = java.sql.Timestamp.valueOf(turno.getFechaTurno());
        spinnerFecha.setValue(dateValue);
        
        // Extraer la hora en formato "HH:mm". Si la representación es "HH:mm:ss", usamos substring.
        String timeFormatted = turno.getFechaTurno().toLocalTime().toString();
        if (timeFormatted.length() > 5) {
            timeFormatted = timeFormatted.substring(0, 5);
        }
        cbHorarios.setSelectedItem(timeFormatted);
    }
    
    // Creamos el panel que contendrá los campos
    JPanel panel = new JPanel(new GridLayout(0, 2));
    panel.add(new JLabel("ID Profesional:"));
    panel.add(tfIdProfesional);
    panel.add(new JLabel("ID Especialidad:"));
    panel.add(tfIdEspecialidad);
    panel.add(new JLabel("Fecha del Turno (yyyy-MM-dd):"));
    panel.add(spinnerFecha);
    panel.add(new JLabel("Horario:"));
    panel.add(cbHorarios);
    
    // Indicamos de forma informativa que el estado se asigna automáticamente a "confirmado"
    panel.add(new JLabel("* Estado se asigna autom. a 'confirmado'"));
    panel.add(new JLabel("* ID Paciente se asigna como 0"));
    
    int result = JOptionPane.showConfirmDialog(
            this, panel, 
            turno == null ? "Agregar Turno" : "Editar Turno", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE);
    
    if (result == JOptionPane.OK_OPTION) {
        try {
            int idProfesional = Integer.parseInt(tfIdProfesional.getText().trim());
            int idEspecialidad = Integer.parseInt(tfIdEspecialidad.getText().trim());
            
            // Obtener la fecha seleccionada y convertirla en LocalDate
            java.util.Date selectedDate = (java.util.Date) spinnerFecha.getValue();
            java.time.LocalDate localDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
            
            // Obtener el horario seleccionado del combo y convertirlo en LocalTime
            String timeSlot = (String) cbHorarios.getSelectedItem();
            java.time.LocalTime localTime = java.time.LocalTime.parse(timeSlot);
            
            // Combinar la fecha y la hora para formar LocalDateTime
            java.time.LocalDateTime fechaTurno = java.time.LocalDateTime.of(localDate, localTime);
            
            // Estado se asigna automáticamente y el ID paciente se fija en 0 (o el valor que desees)
            String estado = "confirmado";
            int idPaciente = 0;
            
            if (turno == null) {
                turno = new Turno();
            }
            turno.setIdProfesional(idProfesional);
            turno.setIdEspecialidad(idEspecialidad);
            turno.setFechaTurno(fechaTurno);
            turno.setEstado(estado);
            turno.setIdPaciente(idPaciente);
            return turno;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la entrada de datos: " + ex.getMessage());
            return null;
        }
    } else {
        return null;
    }
}
}