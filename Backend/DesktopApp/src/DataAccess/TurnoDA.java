package DataAccess;

import entidades.Turno;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TurnoDA {

    /**
     * Obtiene todos los turnos registrados en la tabla turnos.
     * @return Lista de Turno.
     */
    public List<Turno> obtenerTurnos() {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT id, id_paciente, id_profesional, id_especialidad, fecha_turno, " +
                     "fecha_solicitud, fecha_confirmacion, estado, created_at, updated_at FROM turnos";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Turno turno = new Turno();
                turno.setId(rs.getInt("id"));
                turno.setIdPaciente(rs.getInt("id_paciente"));
                turno.setIdProfesional(rs.getInt("id_profesional"));
                turno.setIdEspecialidad(rs.getInt("id_especialidad"));
                
                Timestamp tsFechaTurno = rs.getTimestamp("fecha_turno");
                if (tsFechaTurno != null) {
                    turno.setFechaTurno(tsFechaTurno.toLocalDateTime());
                }
                
                Timestamp tsFechaSolicitud = rs.getTimestamp("fecha_solicitud");
                if (tsFechaSolicitud != null) {
                    turno.setFechaSolicitud(tsFechaSolicitud.toLocalDateTime());
                }
                
                Timestamp tsFechaConfirmacion = rs.getTimestamp("fecha_confirmacion");
                if (tsFechaConfirmacion != null) {
                    turno.setFechaConfirmacion(tsFechaConfirmacion.toLocalDateTime());
                }
                
                turno.setEstado(rs.getString("estado"));
                
                Timestamp tsCreated = rs.getTimestamp("created_at");
                if (tsCreated != null) {
                    turno.setCreatedAt(tsCreated.toLocalDateTime());
                }
                
                Timestamp tsUpdated = rs.getTimestamp("updated_at");
                if (tsUpdated != null) {
                    turno.setUpdatedAt(tsUpdated.toLocalDateTime());
                }
                
                turnos.add(turno);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTurnos: " + e.getMessage());
            e.printStackTrace();
        }
        return turnos;
    }

    /**
     * Inserta un nuevo turno en la tabla turnos.
     * @param turno Objeto Turno con los datos a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     */
    public boolean insertarTurno(Turno turno) {
        String sql = "INSERT INTO turnos (id_paciente, id_profesional, id_especialidad, fecha_turno, estado) VALUES (?, ?, ?, ?, ?)";
        // Nota: El campo fecha_solicitud se asigna automáticamente con CURRENT_TIMESTAMP
        // y fecha_confirmacion se deja en NULL inicialmente.
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            ps.setInt(1, turno.getIdPaciente());
            ps.setInt(2, turno.getIdProfesional());
            ps.setInt(3, turno.getIdEspecialidad());
            ps.setTimestamp(4, Timestamp.valueOf(turno.getFechaTurno()));
            ps.setString(5, turno.getEstado());
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if(rs.next()){
                        turno.setId(rs.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error en insertarTurno: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza un turno existente en la tabla turnos.
     * @param turno Objeto Turno con los nuevos datos.
     * @return true si la actualización fue exitosa; false en caso contrario.
     */
    public boolean actualizarTurno(Turno turno) {
        String sql = "UPDATE turnos SET id_paciente = ?, id_profesional = ?, id_especialidad = ?, " +
                     "fecha_turno = ?, fecha_confirmacion = ?, estado = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, turno.getIdPaciente());
            ps.setInt(2, turno.getIdProfesional());
            ps.setInt(3, turno.getIdEspecialidad());
            ps.setTimestamp(4, Timestamp.valueOf(turno.getFechaTurno()));
            // Si fecha_confirmacion es null se maneja como NULL
            if (turno.getFechaConfirmacion() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(turno.getFechaConfirmacion()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            ps.setString(6, turno.getEstado());
            ps.setInt(7, turno.getId());
            
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo actualizar el turno con id: " + turno.getId());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en actualizarTurno: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina un turno de la tabla turnos por su id.
     * @param id El id del turno a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     */
    public boolean eliminarTurno(int id) {
        String sql = "DELETE FROM turnos WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el turno con id: " + id);
            }
            return true;
        } catch(SQLException e) {
            System.err.println("Error en eliminarTurno: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}