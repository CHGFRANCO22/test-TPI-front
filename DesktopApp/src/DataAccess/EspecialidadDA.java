package DataAccess;

import entidades.Especialidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDA {

    /**
     * Recupera todas las especialidades de la base de datos.
     * @return Lista de Especialidad.
     */
    public List<Especialidad> obtenerEspecialidades() {
        List<Especialidad> especialidades = new ArrayList<>();
        String sql = "SELECT id, nombre, created_at, updated_at FROM especialidades";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setId(rs.getInt("id"));
                esp.setNombre(rs.getString("nombre"));
                Timestamp tsCreated = rs.getTimestamp("created_at");
                if (tsCreated != null) {
                    esp.setCreatedAt(tsCreated.toLocalDateTime());
                }
                Timestamp tsUpdated = rs.getTimestamp("updated_at");
                if (tsUpdated != null) {
                    esp.setUpdatedAt(tsUpdated.toLocalDateTime());
                }
                especialidades.add(esp);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtener especialidades: " + e.getMessage());
            e.printStackTrace();
        }
        return especialidades;
    }

    /**
     * Inserta una nueva especialidad en la base de datos.
     * @param especialidad Objeto Especialidad con el nombre a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarEspecialidad(Especialidad especialidad) {
        String sql = "INSERT INTO especialidades (nombre) VALUES (?)";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, especialidad.getNombre());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        especialidad.setId(rs.getInt(1));
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error en insertar especialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Actualiza una especialidad existente.
     * @param especialidad Objeto Especialidad con el id y el nuevo nombre.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarEspecialidad(Especialidad especialidad) {
        String sql = "UPDATE especialidades SET nombre = ? WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialidad.getNombre());
            ps.setInt(2, especialidad.getId());

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo actualizar la especialidad con id: " + especialidad.getId());
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en actualizar especialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina una especialidad de la base de datos.
     * @param id El id de la especialidad a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarEspecialidad(int id) {
        String sql = "DELETE FROM especialidades WHERE id = ?";
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new SQLException("No se pudo eliminar la especialidad con id: " + id);
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error en eliminar especialidad: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}