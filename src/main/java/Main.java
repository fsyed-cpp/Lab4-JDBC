package main.java;


import java.sql.*;
import java.util.Scanner;

/**
 * My Database is called "School", so the table names are prefixed by 'school.'
 */
public class Main {

    static final String username = "root";

    // ENTER YOUR PASSWORD HERE
    static final String password = "";

    public static void main(String[] args) throws SQLException {

        // #1
        // Ex: Pomona, Ontario, 7/16/2020
        Main.displayTrips();

        // #2
        // Ex: Delete 11
        Main.deleteTrip();
        Main.insertTripOffering();
        Main.changeDriverForTrip("Steve Bevi", "12", "7/18/2020", "6:00PM");
        Main.changeBusIdForTrip("7", "12", "7/18/2020", "6:00PM");

        // #3
        Main.displayStopsFromTrip("12");

        // #4 (7/16 + 7 days = 7/23 to give weekly schedule)
        Main.getDriverWeeklySchedule("Doug Powell", "7/16/2020","7/23/2020");

        // #5
        Main.insertDriver();

        // #6
        Main.insertBus();

        // #7
        Main.deleteBus("11");

        // #8
        Main.insertActualTripStopInfo();
    }

    // MARK: - Queries

    // #1
    public static void displayTrips() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);

        boolean shouldRepeat = true;
        while (shouldRepeat) {

            System.out.print("Please enter a start location: ");
            String startLocation = scanner.nextLine();
            System.out.print("Please enter a destination: ");
            String destination = scanner.nextLine();
            System.out.print("Please enter a date: ");
            String date = scanner.nextLine();

            // #1
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

            System.out.println("Would you like to view more trips from a start loc, destination, and date? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    // #2: Delete trip by specified Trip #
    public static void deleteTrip() throws SQLException {
        Connection con = getConnection();

        Scanner scanner = new Scanner(System.in);

        boolean shouldRepeat = true;
        while (shouldRepeat) {

            System.out.print("Please enter a trip number to delete: ");
            String tripNumber = scanner.nextLine();
            String query = "DELETE FROM school.Trip WHERE TripNumber = " + tripNumber;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);

            System.out.println("Deleted Trip from Trip #" + tripNumber);

            System.out.println("Would you like to delete another trip? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void insertTripOffering() throws SQLException {

        Connection con = getConnection();
        String query = "INSERT INTO `school`.`tripoffering` (`TripNumber`, `Date`, `ScheduledStartTime`, `ScheduledArrivalTime`, `DriverName`, `BusID`) VALUES ('12', '7/18/2020', '6:00PM', '7:15PM', 'Floyd Aflack', '5');";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.execute(query);

        System.out.println("Inserted a new Trip offering!");
    }

    public static void changeDriverForTrip(String driverName, String tripNumber, String date, String scheduledStartTime) throws SQLException {

        Connection con = getConnection();
        String query = "UPDATE school.TripOffering SET DriverName = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, driverName);
        stmt.setString(2, tripNumber);
        stmt.setString(3, date);
        stmt.setString(4, scheduledStartTime);

        stmt.execute();

        System.out.println("Updated trip offering where it starts " + date + scheduledStartTime + " to the driver " + driverName);
    }

    public static void changeBusIdForTrip(String busId, String tripNumber, String date, String scheduledStartTime) throws SQLException {

        Connection con = getConnection();
        String query = "UPDATE school.TripOffering SET BusId = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, busId);
        stmt.setString(2, tripNumber);
        stmt.setString(3, date);
        stmt.setString(4, scheduledStartTime);

        stmt.execute();

        System.out.println("Updated trip offering where it starts " + date + scheduledStartTime + " to the busId " + busId);
    }

    public static void displayStopsFromTrip(String tripNumber) throws SQLException {

        Connection con = getConnection();
        String query = "SELECT * FROM school.TripStopInfo WHERE TripNumber = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, tripNumber);

        ResultSet resultSet = stmt.executeQuery();
        System.out.println("Displaying Stops from Trip #" + tripNumber);
        while (resultSet.next()) {
            System.out.print("Trip number: " + resultSet.getString("TripNumber") + " ");
            System.out.print("StopNumber: " + resultSet.getString("StopNumber") + " ");
            System.out.print("SequenceNumber: " + resultSet.getString("SequenceNumber") + " ");
            System.out.print("DrivingTime: " + resultSet.getString("DrivingTime") +  " ");
        }
        System.out.println();
    }

    public static void getDriverWeeklySchedule(String driverName, String startDate, String endDate) throws SQLException {

        Connection con = getConnection();
        String query = "SELECT * FROM school.tripoffering WHERE DriverName = ? AND Date between ? and ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setString(1, driverName);
        stmt.setString(2, startDate);
        stmt.setString(3, endDate);
        ResultSet resultSet = stmt.executeQuery();

        int tripNumber= 1;
        System.out.println("Weekly schedule for " + driverName + " starting from date " + startDate);
        while (resultSet.next()) {
            System.out.println("Trip #" + tripNumber + " for driver " + driverName);

            System.out.print("Trip number: " + resultSet.getString("TripNumber") + " ");
            System.out.print("Date: " + resultSet.getString("Date") + " ");
            System.out.print("ScheduledStartTime: " + resultSet.getString("ScheduledStartTime") + " ");
            System.out.print("ScheduledArrivalTime: " + resultSet.getString("ScheduledArrivalTime") + " ");
            System.out.print("BusId: " + resultSet.getString("BusID") + " ");
            tripNumber++;
        }
        System.out.println();
    }

    public static void insertDriver() throws SQLException {

        Connection con = getConnection();
        String query = "INSERT INTO `school`.`driver` (`DriverName`, `DriverTelephoneNumber`) VALUES ('Faisal Syed', '111-111-1111');";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.execute(query);

        System.out.println("Inserted a new Driver named Faisal!");
    }

    public static void insertBus() throws SQLException {

        Connection con = getConnection();
        String query = "INSERT INTO `school`.`bus` (`BusID`, `Model`, `Year`) VALUES ('11', '21b', '2019');";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.execute(query);

        System.out.println("Inserted a new Bus with BusId 11 and Model 21b and year of 2019");
    }

    public static void deleteBus(String busId) throws SQLException {
        Connection con = getConnection();
        String query = "DELETE FROM school.bus WHERE BusId = " + busId;
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.execute(query);

        System.out.println("Deleted Bus #" + busId);
    }

    public static void insertActualTripStopInfo() throws SQLException {

        Connection con = getConnection();
        String query = "INSERT INTO `school`.`actualtripstopinfo` (`TripNumber`, `Date`, `ScheduledStartTime`, `StopNumber`, `ScheduledArrivalTime`, `ActualStartTime`, `ActualArrivalTime`, `NumberOfPassengerIn`, `NumberOfPassengerOut`) VALUES ('1', '7/17/2020', '3:00PM', '1', '5:15PM', '4:15PM', '5:20PM', '10', '12');";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.execute(query);

        System.out.println("Inserted a new actual trip stop info!");
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
