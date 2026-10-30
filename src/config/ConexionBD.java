package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar el patrón Singleton para la conexión a MySQL.
 */
public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Por defecto en XAMPP suele estar vacío

    public static Connection obtenerConexion() throws SQLException {
        try {
            // Carga explícita del driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error: Driver JDBC no encontrado.", e);
        }
    }
}