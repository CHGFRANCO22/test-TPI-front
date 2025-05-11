package DataAccess;

import entidades.AgendaMedico;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendaMedicoDA {

    /**
     * Recupera todos los registros de la tabla agenda_medicos.
     * @return Lista de AgendaMedico.
     */
    public List<AgendaMedico> obtenerAgendaMedicos() {
        List<AgendaMedico> agendaList = new ArrayList<>();
        String sql = "SELECT id, id_profesional, fecha, hora_inicio, hora_fin, created_at, updated_at FROM agenda_medicos";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                AgendaMedico agenda = new AgendaMedico();
                agenda.setId(rs.getInt("id"));
                agenda.setIdProfesional(rs.getInt("id_profesional"));
                
                Date sqlFecha = rs.getDate("fecha");
                if (sqlFecha != null) {
                    agenda.setFecha(sqlFecha.toLocalDate());
                }
                
                Time sqlHoraInicio = rs.getTime("hora_inicio");
                if (sqlHoraInicio != null) {
                    agenda.setHoraInicio(sqlHoraInicio.toLocalTime());
                }
                
                Time sqlHoraFin = rs.getTime("hora_fin");
                if (sqlHoraFin != null) {
                    agenda.setHoraFin(sqlHoraFin.toLocalTime());
                }
                
                Timestamp tsCreated = rs.getTimestamp("created_at");
                if (tsCreated != null) {
                    agenda.setCreatedAt(tsCreated.toLocalDateTime());
                }
                
                Timestamp tsUpdated = rs.getTimestamp("updated_at");
                if (tsUpdated != null) {
                    agenda.setUpdatedAt(tsUpdated.toLocalDateTime());
                }
                
                agendaList.add(agenda);
            }

        } catch (SQLException e) {
            System.err.println("Error en obtenerAgendaMedicos: " + e.getMessage());
            e.printStackTrace();
        }
        return agendaList;
    }

    /**
     * Recupera la agenda para un profesional en una fecha determinada.
     * @param idProfesional El id del profesional.
     * @param fecha La fecha a consultar.
     * @return Lista de AgendaMedico para ese profesional y fecha.
     */
    public List<AgendaMedico> obtenerAgendaMedicosPorProfesionalYFecha(int idProfesional, LocalDate fecha) {
        List<AgendaMedico> agendaList = new ArrayList<>();
        String sql = "SELECT id, id_profesional, fecha, hora_inicio, hora_fin, created_at, updated_at " +
                     "FROM agenda_medicos " +
                     "WHERE id_profesional = ? AND fecha = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idProfesional);
            ps.setDate(2, Date.valueOf(fecha));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AgendaMedico agenda = new AgendaMedico();
                    agenda.setId(rs.getInt("id"));
                    agenda.setIdProfesional(rs.getInt("id_profesional"));
                    
                    Date sqlFecha = rs.getDate("fecha");
                    if (sqlFecha != null) {
                        agenda.setFecha(sqlFecha.toLocalDate());
                    }
                    
                    Time sqlHoraInicio = rs.getTime("hora_inicio");
                    if (sqlHoraInicio != null) {
                        agenda.setHoraInicio(sqlHoraInicio.toLocalTime());
                    }
                    
                    Time sqlHoraFin = rs.getTime("hora_fin");
                    if (sqlHoraFin != null) {
                        agenda.setHoraFin(sqlHoraFin.toLocalTime());
                    }
                    
                    Timestamp tsCreated = rs.getTimestamp("created_at");
                    if (tsCreated != null) {
                        agenda.setCreatedAt(tsCreated.toLocalDateTime());
                    }
                    
                    Timestamp tsUpdated = rs.getTimestamp("updated_at");
                    if (tsUpdated != null) {
                        agenda.setUpdatedAt(tsUpdated.toLocalDateTime());
                    }
                    
                    agendaList.add(agenda);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerAgendaMedicosPorProfesionalYFecha: " + e.getMessage());
            e.printStackTrace();
        }
        return agendaList;
    }

    /**
     * Inserta un nuevo registro en la agenda de médicos.
     * @param agenda Objeto AgendaMedico con los datos a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     */
    public boolean insertarAgendaMedico(AgendaMedico agenda) {
        String sql = "INSERT INTO agenda_medicos (id_profesional, fecha, hora_inicio, hora_fin) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, agenda.getIdProfesional());
            ps.setDate(2, Date.valueOf(agenda.getFecha()));
            ps.setTime(3, Time.valueOf(agenda.getHoraInicio()));
            ps.setTime(4, Time.valueOf(agenda.getHoraFin()));

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        agenda.setId(rs.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error en insertarAgendaMedico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un registro existente en la tabla agenda_medicos.
     * @param agenda Objeto AgendaMedico con los nuevos datos.
     * @return true si la actualización fue exitosa; false en caso contrario.
     */
    public boolean actualizarAgendaMedico(AgendaMedico agenda) {
        String sql = "UPDATE agenda_medicos SET id_profesional = ?, fecha = ?, hora_inicio = ?, hora_fin = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, agenda.getIdProfesional());
            ps.setDate(2, Date.valueOf(agenda.getFecha()));
            ps.setTime(3, Time.valueOf(agenda.getHoraInicio()));
            ps.setTime(4, Time.valueOf(agenda.getHoraFin()));
            ps.setInt(5, agenda.getId());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo actualizar el registro con id: " + agenda.getId());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en actualizarAgendaMedico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un registro de la tabla agenda_medicos por su id.
     * @param id El id del registro a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     */
    public boolean eliminarAgendaMedico(int id) {
        String sql = "DELETE FROM agenda_medicos WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el registro con id: " + id);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en eliminarAgendaMedico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}