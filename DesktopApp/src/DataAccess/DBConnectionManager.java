package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
    // Configura la URL, usuario y contraseña según tu sistema y configuración de MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/salud_total_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";       // Ejemplo: "root"
    private static final String PASSWORD = "39863928";   // Tu contraseña

    /**
     * Obtiene una conexión con la base de datos.
     * @return Connection - conexión establecida con la base de datos.
     * @throws SQLException en caso de error al conectarse
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}