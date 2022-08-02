package main.java;


import java.sql.*;
import java.util.Scanner;

/**
 * My Database is called "School", so the table names are prefixed by 'school.'
 */
public class Main {

    static final String username = "root";

    // ENTER YOUR PASSWORD HERE
    static final String password = "Deys@1357";

    public static void main(String[] args) throws SQLException {

        // #1
        // Ex: Pomona, Ontario, 7/16/2020
        Main.displayTrips();

        // #2
        // Ex: Delete 11
        Main.deleteTrip();
        Main.insertTripOffering();
        Main.changeDriverForTrip();
        Main.changeBusIdForTrip();

        // #3
        // Ex: 12
        Main.displayStopsFromTrip();

        // #4 (7/16 + 7 days = 7/23 to give weekly schedule)
        // Ex: "Doug Powell", "7/16/2020","7/23/2020"
        Main.getDriverWeeklySchedule();

        // #5
        Main.insertDriver();

        // #6
        Main.insertBus();

        // #7
        // Ex: 11
        Main.deleteBus();

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

            System.out.println("Displaying all trips that start at " + startLocation + ", end at " + destination + ", and started on " + date);
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
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Please provide all necessary data for the trip offering you wish to add.");
            System.out.println("Enter the trip number:");
            String tripNum = scanner.nextLine();
            System.out.println("Enter the date the trip is offered:");
            String date = scanner.nextLine();
            System.out.println("Enter the starting time for the trip:");
            String Stime = scanner.nextLine();
            System.out.println("Enter the arrival time for the trip:");
            String Atime = scanner.nextLine();
            System.out.println("Enter the name of the driver:");
            String Driver = scanner.nextLine();
            System.out.println("Lastly, enter the bus ID for the trip:");
            String busID = scanner.nextLine();
            String query = "INSERT INTO `school`.`tripoffering` (`TripNumber`, `Date`, `ScheduledStartTime`, `ScheduledArrivalTime`, `DriverName`, `BusID`) VALUES " +
                    "('" + tripNum + "', '" + date + "', '" + Stime + "', '" + Atime + "', '" + Driver + "', '" + busID + "');";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);
            System.out.println("Inserted a new Trip offering!");
            System.out.println("Would you like to insert another trip? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void changeDriverForTrip() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the values to identify the trip");
            System.out.println("Enter the trip number you wish to update:");
            String tripNumber = scanner.nextLine();
            System.out.println("Enter the date for the trip:");
            String date = scanner.nextLine();
            System.out.println("Enter the start time for the trip:");
            String scheduledStartTime = scanner.nextLine();
            System.out.println("Lastly, enter the name of the driver you wish to change to:");
            String driverName = scanner.nextLine();

            String query = "UPDATE school.TripOffering SET DriverName = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, driverName);
            stmt.setString(2, tripNumber);
            stmt.setString(3, date);
            stmt.setString(4, scheduledStartTime);
            stmt.execute();

            System.out.println("Updated trip offering where it starts " + date + scheduledStartTime + " to the driver " + driverName);
            System.out.println("Would you like to change another driver? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void changeBusIdForTrip() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the values to identify the trip");
            System.out.println("Enter the trip number you wish to update:");
            String tripNumber = scanner.nextLine();
            System.out.println("Enter the date for the trip:");
            String date = scanner.nextLine();
            System.out.println("Enter the start time for the trip:");
            String scheduledStartTime = scanner.nextLine();
            System.out.println("Lastly, enter the busID you wish to change to:");
            String busId = scanner.nextLine();

            String query = "UPDATE school.TripOffering SET BusId = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, busId);
            stmt.setString(2, tripNumber);
            stmt.setString(3, date);
            stmt.setString(4, scheduledStartTime);

            stmt.execute();

            System.out.println("Updated trip offering where it starts " + date + scheduledStartTime + " to the busId " + busId);
            System.out.println("Would you like to change another Bus? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void displayStopsFromTrip() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the trip# that you wish to see the stops of:");
            String tripNumber = scanner.nextLine();
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
            System.out.println("Would you like to view more stops? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void getDriverWeeklySchedule() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the driver's name for their scheudle:");
            String driverName = scanner.nextLine();
            System.out.println("Next, enter the time frame in which you want their schedule");
            System.out.println("Enter the starting date:");
            String startDate = scanner.nextLine();
            System.out.println("Enter the end date:");
            String endDate = scanner.nextLine();
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
            System.out.println("Would you like to view more schedules? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void insertDriver() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the name of the driver you wish to add:");
            String driverName = scanner.nextLine();
            System.out.println("Enter the phone number of the driver:");
            String phoneNum = scanner.nextLine();
            String query = "INSERT INTO `school`.`driver` (`DriverName`, `DriverTelephoneNumber`) VALUES ('" + driverName + "', '" + phoneNum + "');";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);

            System.out.println("Inserted a new Driver named " + driverName + "!");
            System.out.println("Would you like to add more drivers? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void insertBus() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the busID of the bus you wish to add:");
            String busID = scanner.nextLine();
            System.out.println("Enter the model of the bus:");
            String busModel = scanner.nextLine();
            System.out.println("Enter the year of the bus:");
            String busYear = scanner.nextLine();
            String query = "INSERT INTO `school`.`bus` (`BusID`, `Model`, `Year`) VALUES ('" + busID + "', '" + busModel + "', '" + busYear + "');";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);

            System.out.println("Inserted a new Bus with BusId " + busID + " and Model " + busModel + " and year of " + busYear);
            System.out.println("Would you like to add more buses? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void deleteBus() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Enter the busID of the bus you wish to delete:");
            String busID = scanner.nextLine();
            String query = "DELETE FROM school.bus WHERE BusId = " + busID;
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);

            System.out.println("Deleted Bus #" + busID);
            System.out.println("Would you like to delete more buses? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
            }
        }
    }

    public static void insertActualTripStopInfo() throws SQLException {

        Connection con = getConnection();
        Scanner scanner = new Scanner(System.in);
        boolean shouldRepeat = true;

        while(shouldRepeat)
        {
            System.out.println("Please provide all necessary data for the actual trip stop you wish to add.");
            System.out.println("Enter the trip number:");
            String tripNum = scanner.nextLine();
            System.out.println("Enter the date the trip is offered:");
            String date = scanner.nextLine();
            System.out.println("Enter the scheduled starting time for the trip:");
            String Stime = scanner.nextLine();
            System.out.println("Enter the scheduled arrival time for the trip:");
            String Atime = scanner.nextLine();
            System.out.println("Enter the ACTUAL starting time for the trip:");
            String actStime = scanner.nextLine();
            System.out.println("Enter the ACTUAL arrival time for the trip:");
            String actAtime = scanner.nextLine();
            System.out.println("Enter the stop number:");
            String stopNum = scanner.nextLine();
            System.out.println("Enter the number of boarding passengers:");
            String passOn = scanner.nextLine();
            System.out.println("Enter the number of off-boarding passengers:");
            String passOff = scanner.nextLine();

            String query = "INSERT INTO `school`.`actualtripstopinfo` (`TripNumber`, `Date`, `ScheduledStartTime`, `StopNumber`, `ScheduledArrivalTime`, `ActualStartTime`, `ActualArrivalTime`, `NumberOfPassengerIn`, `NumberOfPassengerOut`)" +
                    "VALUES ('" + tripNum + "', '" + date + "', '" + Stime + "', '" + stopNum + "', '" + Atime + "', '" + actStime + "', '" + actAtime + "', '" + passOn + "', '" + passOff + "');";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.execute(query);

            System.out.println("Inserted a new actual trip stop info!");
            System.out.println("Would you like to add another? (Y/N)");
            String userResponse = scanner.nextLine();
            if (userResponse.contains("N")) {
                shouldRepeat = false;
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
}
