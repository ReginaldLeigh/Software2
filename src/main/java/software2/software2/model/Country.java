package software2.software2.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import software2.software2.DAO.DBDivisionsDAO;

public class Country {
    private int countryID;
    private String countryName;
    private ObservableList<Division> division = FXCollections.observableArrayList();
    //private LocalDateTime dateTime;

    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
        //this.dateTime = dateTime;
    }

    public int getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }

//    public LocalDateTime getDateTime() {
//        return dateTime;
//    }

    public ObservableList<Division> getDivisions() {
        ObservableList<Division> allDivisions = DBDivisionsDAO.getAllDivisions();
        ObservableList<Division> firstLevelDivisions = FXCollections.observableArrayList();

        for (Division division : allDivisions) {
            if (division.getCountryID() == countryID) {
                firstLevelDivisions.add(division);
            }
        }

        return firstLevelDivisions;
    }

    @Override
    public String toString() {
        return (countryName);
    }
}
