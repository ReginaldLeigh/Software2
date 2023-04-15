package software2.software2;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import software2.software2.DAO.DBCountriesDAO;
import software2.software2.model.Country;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Country> countryList = DBCountriesDAO.getAllCountries();
        for(Country country: countryList) {
            System.out.println(country.getCountryID() + " - " + country.getCountryName());
        }
    }
}