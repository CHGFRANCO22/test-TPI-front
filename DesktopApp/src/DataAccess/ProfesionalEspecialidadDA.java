package DataAccess;

import entidades.ProfesionalEspecialidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesionalEspecialidadDA {

    /**
     * Recupera todas las asociaciones entre profesionales y especialidades.
     * @return Lista de ProfesionalEspecialidad.
     */
    public List<ProfesionalEspecialidad> obtenerProfesionalEspecialidades() {
        List<ProfesionalEspecialidad> lista = new ArrayList<>();
        String sql = "SELECT id, id_profesional, id_especialidad, created_at, updated_at FROM profesional_especialidad";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                ProfesionalEspecialidad pe = new ProfesionalEspecialidad();
                pe.setId(rs.getInt("id"));
                pe.setIdProfesional(rs.getInt("id_profesional"));
                pe.setIdEspecialidad(rs.getInt("id_especialidad"));
                
                Timestamp tsCreated = rs.getTimestamp("created_at");
                if (tsCreated != null) {
                    pe.setCreatedAt(tsCreated.toLocalDateTime());
                }
                Timestamp tsUpdated = rs.getTimestamp("updated_at");
                if (tsUpdated != null) {
                    pe.setUpdatedAt(tsUpdated.toLocalDateTime());
                }
                lista.add(pe);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerProfesionalEspecialidades: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Inserta una nueva asociación en la tabla profesional_especialidad.
     * @param pe Objeto ProfesionalEspecialidad con id_profesional e id_especialidad.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarProfesionalEspecialidad(ProfesionalEspecialidad pe) {
        String sql = "INSERT INTO profesional_especialidad (id_profesional, id_especialidad) VALUES (?, ?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            ps.setInt(1, pe.getIdProfesional());
            ps.setInt(2, pe.getIdEspecialidad());
            
            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        pe.setId(rs.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error en insertarProfesionalEspecialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza una asociación existente.
     * @param pe Objeto ProfesionalEspecialidad con el id y nuevos valores para id_profesional e id_especialidad.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarProfesionalEspecialidad(ProfesionalEspecialidad pe) {
        String sql = "UPDATE profesional_especialidad SET id_profesional = ?, id_especialidad = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, pe.getIdProfesional());
            ps.setInt(2, pe.getIdEspecialidad());
            ps.setInt(3, pe.getId());
            
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo actualizar el registro con id: " + pe.getId());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en actualizarProfesionalEspecialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una asociación de la tabla profesional_especialidad por su id.
     * @param id El id del registro a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarProfesionalEspecialidad(int id) {
        String sql = "DELETE FROM profesional_especialidad WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el registro con id: " + id);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en eliminarProfesionalEspecialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Opcional: Elimina la asociación según la combinación de id_profesional e id_especialidad.
     * @param idProfesional El id del profesional.
     * @param idEspecialidad El id de la especialidad.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPorProfesionalYEspecialidad(int idProfesional, int idEspecialidad) {
        String sql = "DELETE FROM profesional_especialidad WHERE id_profesional = ? AND id_especialidad = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, idProfesional);
            ps.setInt(2, idEspecialidad);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar el registro para profesional id: " + idProfesional + " y especialidad id: " + idEspecialidad);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en eliminarPorProfesionalYEspecialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}