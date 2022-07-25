package main.java;

import java.sql.*;

public class Main {

    static final String username = "root";

    // ENTER YOUR PASSWORD HERE
    static final String password = "";

    public static void main(String[] args) throws SQLException {

        Statement statement = createStatement();
        try (ResultSet resultSet = statement.executeQuery("SELECT * FROM school.employee")) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
        }
    }

    // MARK: - DB Connection

    public static Connection getConnection() throws RuntimeException {
        Connection connection;
        try {
            String connectionUrlString = "jdbc:mysql://localhost:3306";
            connection = DriverManager.getConnection(connectionUrlString, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public static Statement createStatement() throws SQLException {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(("Caught an exception creating statement: " + e));
            throw new SQLException(e);
        }
        return statement;
    }
}
