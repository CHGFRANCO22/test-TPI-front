package servicios;

import DataAccess.AgendaMedicoDA;
import entidades.AgendaMedico;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AgendaMedicoServ {

    // Instancia el DAO para trabajar con la base de datos
    private AgendaMedicoDA agendaMedicoDA = new AgendaMedicoDA();

    /**
     * Verifica si el profesional está disponible en un determinado día y rango horario.
     * Se consulta la agenda para el profesional y se revisa si el rango solicitado
     * se solapa con algún bloque ya reservado.
     *
     * @param idProfesional El id del profesional.
     * @param fecha         La fecha a consultar.
     * @param horaInicio    La hora de inicio del bloque solicitado.
     * @param horaFin       La hora de fin del bloque solicitado.
     * @return true si no hay solapamiento (disponible); false si existe un conflicto.
     */
    public boolean isMedicoDisponible(int idProfesional, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        List<AgendaMedico> agendas = agendaMedicoDA.obtenerAgendaMedicosPorProfesionalYFecha(idProfesional, fecha);
        for (AgendaMedico agenda : agendas) {
            // Se verifica el solapamiento: si la hora de inicio solicitado es anterior a la hora de fin existente
            // y la hora de fin solicitado es posterior a la hora de inicio existente, se considera solapado.
            if (horaInicio.isBefore(agenda.getHoraFin()) && horaFin.isAfter(agenda.getHoraInicio())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Intenta reservar un bloque de atención para un profesional.
     * Se verifica primero la disponibilidad en la agenda; si está libre se inserta el bloque en la base de datos.
     *
     * @param idProfesional El id del profesional.
     * @param fecha         La fecha del bloque a reservar.
     * @param horaInicio    La hora de inicio del bloque.
     * @param horaFin       La hora de fin del bloque.
     * @return Un mensaje indicando si la reserva fue exitosa o si hubo conflicto.
     */
    public String reservarBloqueAtencion(int idProfesional, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        if (isMedicoDisponible(idProfesional, fecha, horaInicio, horaFin)) {
            AgendaMedico nuevoBloque = new AgendaMedico();
            nuevoBloque.setIdProfesional(idProfesional);
            nuevoBloque.setFecha(fecha);
            nuevoBloque.setHoraInicio(horaInicio);
            nuevoBloque.setHoraFin(horaFin);
            
            boolean exito = agendaMedicoDA.insertarAgendaMedico(nuevoBloque);
            if (exito) {
                return "Bloque de atención reservado correctamente.";
            } else {
                return "Error al reservar el bloque de atención, intente nuevamente.";
            }
        } else {
            return "El profesional no está disponible en ese horario.";
        }
    }
    
    // Puedes agregar métodos adicionales, como actualización o cancelación de bloques, según tus necesidades.
}