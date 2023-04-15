package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Appointment;
import software2.software2.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCustomersDAO {
    private static int newCustomerID = 0;

    public static int getNewCustomerID() {
        return newCustomerID;
    }

    public static void setNewCustomerID() {
        String sql = "SELECT MAX(Customer_ID) FROM client_schedule.customers";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int customerID = rs.getInt("MAX(Customer_ID)");
                newCustomerID = customerID + 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Customer> getAllCustomers() {
        //create a list to return
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.customers";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionID = rs.getInt("Division_ID");

                //make an object instance
                Customer customer = new Customer(id, name, address, postalCode, phone, divisionID);

                //add to list
                customers.add(customer);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return customers;
    }

    public static void addCustomer(Customer customer) {

        //set up the sql
        String sql = "INSERT INTO client_schedule.customers " +
                "(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                "VALUES (" +
                customer.getId() + ", '" +
                customer.getName() + "', '" +
                customer.getAddress() + "', '" +
                customer.getPostalCode() + "', '" +
                customer.getPhone() + "', " +
                customer.getDivisionID() + ")"
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

    public static void updateCustomer(Customer customer) {

        //set up the sql
        String sql = "UPDATE client_schedule.customers SET " +
                "Customer_Name = '" + customer.getName() + "', " +
                "Address = '" + customer.getAddress() + "', " +
                "Postal_Code = '" + customer.getPostalCode() + "', " +
                "Phone = '" + customer.getPhone() + "', " +
                "Division_ID = '" + customer.getDivisionID() + "' " +
                "WHERE Customer_ID = " + customer.getId()
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

    public static void deleteCustomer(Customer customer) {

        //set up the sql
        String sql = "DELETE FROM client_schedule.customers " +
                "WHERE Customer_ID = " + customer.getId()
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

    public static ObservableList<Appointment> searchAssociatedAppts(Customer customer) {
        ObservableList<Appointment> appointments = DBAppointmentsDAO.getAllAppointments();
        ObservableList<Appointment> associatedAppts = FXCollections.observableArrayList();

        for (Appointment appt: appointments) {
            if (appt.getCustomerId() == customer.getId()) {
                associatedAppts.add(appt);
            }
        }

        return associatedAppts;
    }
}
