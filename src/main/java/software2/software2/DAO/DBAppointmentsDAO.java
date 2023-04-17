package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAppointmentsDAO {
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
                String start = rs.getString("Start");
                String end = rs.getString("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                //make an object instance
                Appointment appointment = new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId);

                //add to list
                appointments.add(appointment);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return appointments;
    }

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
                appointment.getStart() + "', '" +
                appointment.getEnd() + "', " +
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

    public static void updateAppointment(Appointment appointment) {

        //set up the sql
        String sql = "UPDATE client_schedule.appointments " +
                "SET Title = '" + appointment.getTitle() + "', " +
                "Description = '" + appointment.getDescription() + "', " +
                "Location = '" + appointment.getLocation() + "', " +
                "Type = '" + appointment.getType() + "', " +
                "Start = '" + appointment.getStart() + "', " +
                "End = '" + appointment.getEnd() + "', " +
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

    public static ObservableList<Appointment> setAppointments() {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.appointments";
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
                String start = rs.getString("Start");
                String end = rs.getString("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                //make an object instance
                Appointment appointment = new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId);

                //add to list
                appointments.add(appointment);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return appointments;
    }

    public static ResultSet getResultSet() throws SQLException {
        //create a list to return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.appointments";

        //make the prepared statement
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        //make the query ==> resultSet
        return ps.executeQuery();
    }
}
