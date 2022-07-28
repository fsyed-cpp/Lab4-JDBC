package main.java;


import java.sql.*;
import java.util.Scanner;

public class Main {

    static final String username = "root";

    // ENTER YOUR PASSWORD HERE
    static final String password = "";

    public static void main(String[] args) throws SQLException {

        //Scanner scan = new Scanner(System.in);
       // System.out.print("Please enter an Employee name: ");
       // String empName = scan.next();
        Main.displayTrips("Pomona", "Ontario", "7/16/2020");

       // Integer empId = getEmployeeIdFromName(empName);
       // System.out.println(empId);
    }

    // MARK: - Queries

    // #1
    public static void displayTrips(String startLocation, String destination, String date) throws SQLException {

        Connection con = getConnection();

        // Get all the trips for a given start location, destination and date
        String query = "SELECT T1.StartLocationName, T1.DestinationName, T2.Date, T2.ScheduledStartTime, T2.ScheduledArrivalTime, T2.Drivername, T2.BusID\n" +
                "FROM school.Trip T1, school.TripOffering T2\n" +
                "WHERE T1.TripNumber = T2.TripNumber AND T1.StartLocationName = ? AND T1.DestinationName = ? AND T2.Date = ?";

        // Sample test data
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, startLocation);
        stmt.setString(2, destination);
        stmt.setString(3, date);

        ResultSet resultSet = stmt.executeQuery();

        System.out.println("Displaying all trips that start at Pomona, end at Ontario, and started on 7/16/2020");
        int tripNumber = 1;
        while (resultSet.next()) {

            System.out.println("Trip " + tripNumber);

            System.out.print("StartLocation: ");
            System.out.print(resultSet.getString("StartLocationName"));
            System.out.print(" ");

            System.out.print("DestinationName: ");
            System.out.print(resultSet.getString("DestinationName"));
            System.out.print(" ");

            System.out.print("Date: ");
            System.out.print(resultSet.getString("Date"));
            System.out.print(" ");

            System.out.print("ScheduledStartTime: ");
            System.out.print(resultSet.getString("ScheduledStartTime"));
            System.out.print(" ");

            System.out.print("ScheduledArrivalTime: ");
            System.out.print(resultSet.getString("ScheduledArrivalTime"));
            System.out.print(" ");

            System.out.print("Drivername: ");
            System.out.print(resultSet.getString("Drivername"));
            System.out.print(" ");

            System.out.print("BusID:");
            System.out.print(resultSet.getString("BusID"));
            System.out.print(" ");

            System.out.println();
            tripNumber++;
        }
    }

    public static Integer getEmployeeIdFromName(String empName) throws SQLException {

        Connection con = getConnection();
        String query = "SELECT * FROM school.employee WHERE name = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, empName);
        ResultSet resultSet = stmt.executeQuery();

        Integer id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("id");
            break;
        }
        return id;
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

}
