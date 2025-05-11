package DataAccess;

import entidades.Profesional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProfesionalDA {

    /**
     * Obtiene la lista de profesionales realizando un JOIN entre las tablas "profesionales" y "persona".
     * @return List<Profesional> lista de profesionales
     */
    public List<Profesional> obtenerProfesionales() {
        List<Profesional> profesionales = new ArrayList<>();
        // La consulta utiliza "nombre_completo" para recuperar el nombre de la persona.
        String sql = "SELECT pr.id_profesional, pr.email, pr.password, pr.rol, pr.matricula, pr.telefono, pr.created_at, pr.updated_at, " +
                     "p.id AS persona_id, p.nombre_completo, p.dni, p.sexo " +
                     "FROM profesionales pr " +
                     "JOIN persona p ON pr.id_persona = p.id";
        
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Profesional profesional = new Profesional();
                // Datos de la tabla persona
                profesional.setId(rs.getInt("persona_id"));
                profesional.setNombre(rs.getString("nombre_completo"));
                profesional.setDni(rs.getString("dni"));
                profesional.setSexo(rs.getString("sexo").charAt(0));
                // Datos propios de la tabla profesionales
                profesional.setIdProfesional(rs.getInt("id_profesional"));
                profesional.setEmail(rs.getString("email"));
                profesional.setPassword(rs.getString("password"));
                profesional.setRol(rs.getString("rol"));
                profesional.setMatricula(rs.getString("matricula"));
                profesional.setTelefono(rs.getString("telefono"));
                if (rs.getTimestamp("created_at") != null) {
                    profesional.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    profesional.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                profesionales.add(profesional);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerProfesionales: " + e.getMessage());
            e.printStackTrace();
        }
        return profesionales;
    }

    /**
     * Inserta un nuevo profesional realizando primero la inserción en la tabla persona y luego en la tabla profesionales.
     * Se utiliza una transacción para asegurar que ambas operaciones se completen correctamente.
     *
     * @param profesional Objeto Profesional con la información a insertar (debe tener nombre, dni, sexo, email, password, rol, matricula y telefono)
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarProfesional(Profesional profesional) {
        String sqlPersona = "INSERT INTO persona (nombre_completo, dni, sexo) VALUES (?, ?, ?)";
        String sqlProfesional = "INSERT INTO profesionales (id_persona, email, password, rol, matricula, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            int idPersona;
            // Inserción en la tabla persona
            try (PreparedStatement psPersona = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, profesional.getNombre());
                psPersona.setString(2, profesional.getDni());
                psPersona.setString(3, String.valueOf(profesional.getSexo()));
                
                int filasAfectadas = psPersona.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new SQLException("Error, no se pudo insertar en la tabla persona.");
                }
                
                try (ResultSet generatedKeys = psPersona.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPersona = generatedKeys.getInt(1);
                        // Asignamos el id generado al objeto profesional
                        profesional.setId(idPersona);
                    } else {
                        throw new SQLException("No se obtuvo el ID generado para persona.");
                    }
                }
            }
            
            // Inserción en la tabla profesionales
            try (PreparedStatement psProfesional = conn.prepareStatement(sqlProfesional, Statement.RETURN_GENERATED_KEYS)) {
                psProfesional.setInt(1, profesional.getId()); // Ya asignado desde la inserción en persona
                psProfesional.setString(2, profesional.getEmail());
                psProfesional.setString(3, profesional.getPassword());
                psProfesional.setString(4, profesional.getRol());
                psProfesional.setString(5, profesional.getMatricula());
                psProfesional.setString(6, profesional.getTelefono());
                
                int filasInsertadas = psProfesional.executeUpdate();
                if (filasInsertadas == 0) {
                    throw new SQLException("Error, no se pudo insertar en la tabla profesionales.");
                }
                
                try (ResultSet generatedKeys = psProfesional.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        profesional.setIdProfesional(generatedKeys.getInt(1));
                    }
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Error en insertarProfesional: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Actualiza la información de un profesional en ambas tablas: persona y profesionales.
     * Se utiliza una transacción para asegurar que ambas operaciones se completen correctamente.
     *
     * @param profesional Objeto Profesional con la nueva información (el id en la entidad indica qué registro actualizar)
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarProfesional(Profesional profesional) {
        String sqlUpdatePersona = "UPDATE persona SET nombre_completo = ?, dni = ?, sexo = ? WHERE id = ?";
        String sqlUpdateProfesional = "UPDATE profesionales SET email = ?, password = ?, rol = ?, matricula = ?, telefono = ? WHERE id_persona = ?";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // Actualizar la tabla persona.
            try (PreparedStatement psPersona = conn.prepareStatement(sqlUpdatePersona)) {
                psPersona.setString(1, profesional.getNombre());
                psPersona.setString(2, profesional.getDni());
                psPersona.setString(3, String.valueOf(profesional.getSexo()));
                psPersona.setInt(4, profesional.getId());
                
                int filasActualizadasPersona = psPersona.executeUpdate();
                if (filasActualizadasPersona == 0) {
                    throw new SQLException("No se pudo actualizar la tabla persona para el profesional con id: " + profesional.getId());
                }
            }
            
            // Actualizar la tabla profesionales.
            try (PreparedStatement psProfesional = conn.prepareStatement(sqlUpdateProfesional)) {
                psProfesional.setString(1, profesional.getEmail());
                psProfesional.setString(2, profesional.getPassword());
                psProfesional.setString(3, profesional.getRol());
                psProfesional.setString(4, profesional.getMatricula());
                psProfesional.setString(5, profesional.getTelefono());
                psProfesional.setInt(6, profesional.getId());
                
                int filasActualizadasProfesional = psProfesional.executeUpdate();
                if (filasActualizadasProfesional == 0) {
                    throw new SQLException("No se pudo actualizar la tabla profesionales para el profesional con id_persona: " + profesional.getId());
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Error en actualizarProfesional: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Elimina un profesional de la base de datos. Se elimina primero de la tabla profesionales
     * y luego de la tabla persona, utilizando el id de la persona.
     *
     * @param idPersona El id de la persona (obtenido de Profesional.getId()) que se desea eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarProfesional(int idPersona) {
        String sqlDeleteProfesional = "DELETE FROM profesionales WHERE id_persona = ?";
        String sqlDeletePersona = "DELETE FROM persona WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // Eliminar de la tabla profesionales.
            try (PreparedStatement psProfesional = conn.prepareStatement(sqlDeleteProfesional)) {
                psProfesional.setInt(1, idPersona);
                psProfesional.executeUpdate();
            }
            
            // Eliminar de la tabla persona.
            try (PreparedStatement psPersona = conn.prepareStatement(sqlDeletePersona)) {
                psPersona.setInt(1, idPersona);
                int filasEliminadas = psPersona.executeUpdate();
                if (filasEliminadas == 0) {
                    throw new SQLException("No se pudo eliminar la persona con id: " + idPersona);
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            System.err.println("Error en eliminarProfesional: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}