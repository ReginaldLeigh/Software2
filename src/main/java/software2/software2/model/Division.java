package software2.software2.model;

/**
 * Class used to create Division objects throughout the application.
 * Each first level division can only be associated with one country.
 */
public class Division {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public Division(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString() {
        return(divisionName);
    }
}
