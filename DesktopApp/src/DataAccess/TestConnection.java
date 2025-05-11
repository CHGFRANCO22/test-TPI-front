package DataAccess;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    
    public static void main(String[] args) {
        try {
            // Intenta obtener la conexión a la base de datos
            Connection conn = DBConnectionManager.getConnection();
            
            if (conn != null) {
                System.out.println("¡Conexión exitosa a la base de datos!");
                conn.close(); // Cerramos la conexión para liberar recursos
            } else {
                System.out.println("La conexión es nula.");
            }
        } catch (SQLException ex) {
            System.err.println("Error al conectar con la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}