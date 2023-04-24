package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Contact;
import software2.software2.model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContactsDAO {
    public static ObservableList<Contact> getAllContacts() {
        //create a list to return
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.contacts";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                //make an object instance
                Contact contact = new Contact(id, name, email);

                //add to list
                contacts.add(contact);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return contacts;
    }
}
