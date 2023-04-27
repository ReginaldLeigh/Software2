package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Contact;
import software2.software2.model.Country;
import software2.software2.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsersDAO {
    private static User currentUser;
    public static ObservableList<User> getAllUsers() {
        //create a list to return
        ObservableList<User> users = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.users";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");

                //make an object instance
                User user = new User(id, username, password);

                //add to list
                users.add(user);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return users;
    }

    public static User getUser(int user_id) {
        //set up the sql
        String sql = "SELECT * FROM client_schedule.users " +
                "WHERE user_id = " + user_id;
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");

                //make an object instance
                User user = new User(id, username, password);

                //add to list
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return null;
    }

    // returns User of current session
    public static User getCurrentUser() { return currentUser; }
    public static void setCurrentUser(User user) { currentUser = user; }
}
