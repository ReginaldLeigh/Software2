package software2.software2.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.database.JDBC;
import software2.software2.model.Country;
import software2.software2.model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDivisionsDAO {

    public static ObservableList<Division> getAllDivisions() {
        //create a list to return
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        //set up the sql
        String sql = "SELECT * FROM client_schedule.first_level_divisions";
        try {
            //make the prepared statement
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            //make the query ==> resultSet
            ResultSet rs = ps.executeQuery();

            //cycle through the resultSet
            while(rs.next()) {
                //pull out the data
                int divisionID = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryID = rs.getInt("Country_ID");

                //make an object instance
                Division division = new Division(divisionID, divisionName, countryID);

                //add to list
                divisions.add(division);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
        //return the list
        return divisions;
    }
}
