package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

/**
 * Acts as the connection between the database and any information regarding countries
 */
public class DBCountriesDAO {

    /**
     * Retrieves all countries from the database
     * @return a list of all countries
     */
    public static ObservableList<Country> getAllCountries() {
        //create a list to return
        ObservableList<Country> countries = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.countries";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int id = rs.getInt("Country_ID");
                String name = rs.getString("Country");

                //make an object instance
                Country country = new Country(id, name);

                //add to list
                countries.add(country);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return countries;
    }
}
