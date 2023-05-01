package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Acts as the connection between the database and any information regarding appointments
 */
public class DBAppointmentsDAO {
    private static int newAppointmentID = 0;

    public static int getNewAppointmentID() {
        return newAppointmentID;
    }

    public static void setNewAppointmentID() {
        String sql = "SELECT MAX(Appointment_ID) FROM client_schedule.appointments";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentID = rs.getInt("MAX(Appointment_ID)");
                newAppointmentID = appointmentID + 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all appointments from the database
     * @return a list of appointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.appointments";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            rs.getMetaData();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                //make an object instance
                Appointment appointment = new Appointment(id, title, description, location, type, start.toLocalDateTime(), end.toLocalDateTime(), customerId, userId, contactId);

                //add to list
                appointments.add(appointment);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return appointments;
    }

    /**
     * Inserts a new appointment into the database
     * @param appointment the Appointment to be added
     */
    public static void addAppointment(Appointment appointment) {

        //set up the sql
        String sql = "INSERT INTO client_schedule.appointments " +
                "(Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) " +
                "VALUES (" +
                appointment.getId() + ", '" +
                appointment.getTitle() + "', '" +
                appointment.getDescription() + "', '" +
                appointment.getLocation() + "', '" +
                appointment.getType() + "', '" +
                Timestamp.valueOf(appointment.getStart()) + "', '" +
                Timestamp.valueOf(appointment.getEnd()) + "', " +
                appointment.getCustomerId() + ", " +
                appointment.getUserId() + ", " +
                appointment.getContactId() + ")"
                ;
        try {
            //make the prepared statement
            Statement statement = JDBC.getConnection().createStatement();


            //make the query ==> resultSet
            statement.executeUpdate(sql);

            System.out.println(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an appointment within the database
     * @param appointment the Appointment to be updated
     */
    public static void updateAppointment(Appointment appointment) {

        //set up the sql
        String sql = "UPDATE client_schedule.appointments " +
                "SET Title = '" + appointment.getTitle() + "', " +
                "Description = '" + appointment.getDescription() + "', " +
                "Location = '" + appointment.getLocation() + "', " +
                "Type = '" + appointment.getType() + "', " +
                "Start = '" + Timestamp.valueOf(appointment.getStart()) + "', " +
                "End = '" + Timestamp.valueOf(appointment.getEnd()) + "', " +
                "Customer_ID = '" + appointment.getCustomerId() + "', " +
                "User_ID = '" + appointment.getUserId() + "', " +
                "Contact_ID = '" + appointment.getContactId() + "' " +
                "WHERE Appointment_ID = " + appointment.getId()
                ;
        try {
            //make the prepared statement
            Statement statement = JDBC.getConnection().createStatement();

            //make the query ==> resultSet
            statement.executeUpdate(sql);

            System.out.println(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes an appointment from the database
     * @param appointment the Appointment to be deleted
     */
    public static void deleteAppointment(Appointment appointment) {

        //set up the sql
        String sql = "DELETE FROM client_schedule.appointments " +
                "WHERE Appointment_ID = " + appointment.getId()
                ;
        try {
            //make the prepared statement
            Statement statement = JDBC.getConnection().createStatement();

            //make the query ==> resultSet
            statement.executeUpdate(sql);

            System.out.println(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a ResultsSet consisting of all appointments
     * @return a ResultSet
     * @throws SQLException
     */
    public static ResultSet getResultSet() throws SQLException {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT a.Appointment_ID as 'Appointment ID', " +
                "a.Title, " +
                "a.Description, " +
                "a.Location, " +
                "b.Contact_Name as 'Contact Name', " +
                "a.Type, " +
                "a.Start as 'Start Date', " +                            // Start and End are repeated to allow
                "a.Start as 'Start Time', " +                            // for column creation in main page tableView
                "a.End as 'End Date', " +
                "a.End as 'End Time', " +
                "a.Customer_ID as 'Customer ID', " +
                "a.User_ID as 'User ID' " +
                "FROM client_schedule.appointments a " +
                "LEFT JOIN client_schedule.contacts b ON a.contact_id = b.contact_id";

        //make the prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //make the query ==> resultSet
        return ps.executeQuery();
    }

    public static Appointment getAppointment(int appointment_id) {
        //set up the sql
        String sql = "SELECT * FROM client_schedule.appointments WHERE appointment_id = " + appointment_id;
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                //make an object instance
                Appointment appointment = new Appointment(id, title, description, location, type, start.toLocalDateTime(), end.toLocalDateTime(), customerId, userId, contactId);

                return appointment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return null;
    }

    /**
     * Returns a list of all appointments associated with a customer
     * @param customer_id the customer ID
     * @return a list of associated appointments
     */
    public static ObservableList<Appointment> getCustomerAppointments(int customer_id) {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.appointments WHERE customer_id = " + customer_id;
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            rs.getMetaData();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                //make an object instance
                Appointment appointment = new Appointment(id, title, description, location, type, start.toLocalDateTime(), end.toLocalDateTime(), customerId, userId, contactId);

                //add to list
                appointments.add(appointment);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return appointments;
    }

//    /**
//     * Returns a ResultSet consisting of all appointments of a certain type
//     * @param type the appointment type
//     * @return a ResultSet
//     * @throws SQLException
//     */
//    public static ResultSet getApptByType(String type) throws SQLException {
//        //create a list to return
//        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
//        //set up the sql
//        String sql = "SELECT a.Appointment_ID as 'Appointment ID', " +
//                "a.Title, " +
//                "a.Description, " +
//                "a.Location, " +
//                "b.Contact_Name as 'Contact Name', " +
//                "a.Type, " +
//                "a.Start as 'Start Date', " +                            // Start and End are repeated to allow
//                "a.Start as 'Start Time', " +                            // for column creation in main page tableView
//                "a.End as 'End Date', " +
//                "a.End as 'End Time', " +
//                "a.Customer_ID as 'Customer ID', " +
//                "a.User_ID as 'User ID' " +
//                "FROM client_schedule.appointments a " +
//                "LEFT JOIN client_schedule.contacts b ON a.contact_id = b.contact_id " +
//                "WHERE a.Type = '" + type + "'";
    /**
     * Returns a ResultSet consisting of all appointments of a certain type
     * @param type the appointment type
     * @return a ResultSet
     * @throws SQLException
     */
    public static ResultSet getApptByMonthAndType(String type) throws SQLException {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT a.Start as 'Month', " +
                "a.Type, " +
                "COUNT(a.Type), " +
                "FROM client_schedule.appointments a " +
                "GROUP BY a.start, a.type "
                ;

        //make the prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //make the query ==> resultSet
        return ps.executeQuery();
    }

    /**
     * Returns a list of all appointment types
     * @return a list of appointment types
     * @throws SQLException
     */
    public static ObservableList<String> getApptTypes() throws SQLException {
        ObservableList<String> apptTypes = FXCollections.observableArrayList();

        //set up the sql
        String sql = "SELECT DISTINCT(type) FROM client_schedule.appointments ";

        //make the prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //make the query ==> resultSet
        ResultSet rs = ps.executeQuery();

        rs.getMetaData();

        while (rs.next()) {
            apptTypes.add(rs.getString("Type"));
        }

        return apptTypes;
    }

    /**
     * Returns a ResultSet of all appointment associated with a contact
     * @param contactId the contact ID
     * @return a ResultSet
     * @throws SQLException
     */
    public static ResultSet getApptByContact(int contactId) throws SQLException {
        //set up the sql
        String sql = "SELECT a.Appointment_ID as 'Appointment ID', " +
                "a.Title, " +
                "a.Description, " +
                "a.Location, " +
                "b.Contact_Name as 'Contact Name', " +
                "a.Type, " +
                "a.Start as 'Start Date', " +                            // Start and End are repeated to allow
                "a.Start as 'Start Time', " +                            // for column creation in main page tableView
                "a.End as 'End Date', " +
                "a.End as 'End Time', " +
                "a.Customer_ID as 'Customer ID', " +
                "a.User_ID as 'User ID' " +
                "FROM client_schedule.appointments a " +
                "LEFT JOIN client_schedule.contacts b ON a.contact_id = b.contact_id " +
                "WHERE b.contact_id = " + contactId;

        //make the prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //make the query ==> resultSet
        return ps.executeQuery();
    }
}
