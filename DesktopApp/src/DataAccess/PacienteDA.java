package DataAccess;

import entidades.Paciente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteDA {

    /**
     * Obtiene la lista de pacientes realizando un JOIN entre las tablas "pacientes" y "persona".
     * @return List<Paciente> lista de pacientes
     */
    public List<Paciente> obtenerPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        // Se utiliza "nombre_completo" porque es el nombre real de la columna en la tabla "persona"
        String sql = "SELECT pa.id_paciente, pa.email, pa.password, pa.created_at, pa.updated_at, "
                   + "p.id AS persona_id, p.nombre_completo, p.dni, p.sexo "
                   + "FROM pacientes pa "
                   + "JOIN persona p ON pa.id_persona = p.id";
        
        try (Connection conn = DBConnectionManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                // Asigna los valores: en la entidad usas "getNombre()", pero en BD la columna es "nombre_completo"
                paciente.setId(rs.getInt("persona_id"));
                paciente.setNombre(rs.getString("nombre_completo"));
                paciente.setDni(rs.getString("dni"));
                // Se asume que el setter de sexo recibe un char
                paciente.setSexo(rs.getString("sexo").charAt(0));
                paciente.setEmail(rs.getString("email"));
                paciente.setPassword(rs.getString("password"));
                if (rs.getTimestamp("created_at") != null) {
                    paciente.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                }
                if (rs.getTimestamp("updated_at") != null) {
                    paciente.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
                }
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerPacientes: " + e.getMessage());
            e.printStackTrace();
        }
        return pacientes;
    }
    
    /**
     * Inserta un nuevo paciente realizando primero la inserción en la tabla persona y luego en la tabla pacientes.
     * Se utiliza una transacción para asegurar que ambas operaciones se completen correctamente.
     *
     * @param paciente Objeto Paciente con la información a insertar (debe tener nombre, dni, sexo, email y password)
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean insertarPaciente(Paciente paciente) {
        // Se inserta en la columna "nombre_completo" para que coincida con la estructura de la tabla Persona.
        String sqlPersona = "INSERT INTO persona (nombre_completo, dni, sexo) VALUES (?, ?, ?)";
        String sqlPaciente = "INSERT INTO pacientes (id_persona, email, password) VALUES (?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // Inserción en la tabla persona.
            try (PreparedStatement psPersona = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, paciente.getNombre());
                psPersona.setString(2, paciente.getDni());
                psPersona.setString(3, String.valueOf(paciente.getSexo()));
                
                int filasAfectadas = psPersona.executeUpdate();
                if (filasAfectadas == 0) {
                    throw new SQLException("Error, no se pudo insertar en la tabla persona.");
                }
                
                // Obtener el ID generado para la persona insertada.
                int idPersona;
                try (ResultSet generatedKeys = psPersona.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPersona = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("No se obtuvo el ID generado para persona.");
                    }
                }
                
                // Inserción en la tabla pacientes utilizando el ID obtenido.
                try (PreparedStatement psPaciente = conn.prepareStatement(sqlPaciente)) {
                    psPaciente.setInt(1, idPersona);
                    psPaciente.setString(2, paciente.getEmail());
                    psPaciente.setString(3, paciente.getPassword());
                    
                    int filasInsertadas = psPaciente.executeUpdate();
                    if (filasInsertadas == 0) {
                        throw new SQLException("Error, no se pudo insertar en la tabla pacientes.");
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
            System.err.println("Error en insertarPaciente: " + e.getMessage());
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
     * Actualiza la información de un paciente en ambas tablas: persona y pacientes.
     * Se utiliza una transacción para asegurar que ambas operaciones se completen correctamente.
     *
     * @param paciente Objeto Paciente con la nueva información (el id en la entidad indica qué registro actualizar)
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizarPaciente(Paciente paciente) {
        String sqlUpdatePersona = "UPDATE persona SET nombre_completo = ?, dni = ?, sexo = ? WHERE id = ?";
        String sqlUpdatePaciente = "UPDATE pacientes SET email = ?, password = ? WHERE id_persona = ?";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // Actualizar la tabla persona.
            try (PreparedStatement psPersona = conn.prepareStatement(sqlUpdatePersona)) {
                psPersona.setString(1, paciente.getNombre());
                psPersona.setString(2, paciente.getDni());
                psPersona.setString(3, String.valueOf(paciente.getSexo()));
                psPersona.setInt(4, paciente.getId());
                
                int filasActualizadasPersona = psPersona.executeUpdate();
                if (filasActualizadasPersona == 0) {
                    throw new SQLException("No se pudo actualizar la tabla persona para el paciente con id: " + paciente.getId());
                }
            }
            
            // Actualizar la tabla pacientes.
            try (PreparedStatement psPaciente = conn.prepareStatement(sqlUpdatePaciente)) {
                psPaciente.setString(1, paciente.getEmail());
                psPaciente.setString(2, paciente.getPassword());
                psPaciente.setInt(3, paciente.getId());
                
                int filasActualizadasPaciente = psPaciente.executeUpdate();
                if (filasActualizadasPaciente == 0) {
                    throw new SQLException("No se pudo actualizar la tabla pacientes para el paciente con id: " + paciente.getId());
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
            System.err.println("Error en actualizarPaciente: " + e.getMessage());
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
     * Elimina un paciente de la base de datos. Se elimina primero de la tabla pacientes
     * y luego de la tabla persona, utilizando el id de la persona.
     *
     * @param idPersona El id de la persona (obtenido de Paciente.getId()) que se desea eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminarPaciente(int idPersona) {
        String sqlDeletePaciente = "DELETE FROM pacientes WHERE id_persona = ?";
        String sqlDeletePersona = "DELETE FROM persona WHERE id = ?";
        Connection conn = null;
        
        try {
            conn = DBConnectionManager.getConnection();
            conn.setAutoCommit(false);
            
            // Eliminar de la tabla pacientes.
            try (PreparedStatement psPaciente = conn.prepareStatement(sqlDeletePaciente)) {
                psPaciente.setInt(1, idPersona);
                psPaciente.executeUpdate();
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
            System.err.println("Error en eliminarPaciente: " + e.getMessage());
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
    
    // Puedes agregar aquí otros métodos adicionales si lo requieres.
}