package dataAccessPackage;

import exceptionPackage.ConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnection {

    private static Connection connection;

    private static final String URL =
            "jdbc:mysql://localhost:8889/scout_unit?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private SingletonConnection() {
    }

    public static Connection getInstance() throws ConnectionException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            throw new ConnectionException("Pilote MySQL JDBC introuvable.", e);
        } catch (SQLException e) {
            throw new ConnectionException("Impossible de se connecter à la base de données.", e);
        }
    }

    public static void closeInstance() throws ConnectionException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new ConnectionException("Impossible de fermer la connexion à la base de données.", e);
        }
    }
}
