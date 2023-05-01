package software2.software2.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import software2.software2.DAO.DBCountriesDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.DAO.DBDivisionsDAO;
import software2.software2.model.Country;
import software2.software2.model.Customer;
import software2.software2.model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Presents form which allows user to update customer in the system
 */
public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneNumField;
    @FXML
    private ComboBox<Country> countryDropdown;
    @FXML
    private ComboBox<Division> divisionDropdown;

    private static Customer modifiedCustomer;


    Parent scene;
    Stage stage;

    /** Moves user to a different page within the application.
     @param event An ActionEvent.
     @param resource The file path for the next FXML resource to be loaded.
     */
    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 1400, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets customer to be updated
     * @param customer a Customer
     */
    public static void setCustomer(Customer customer) { modifiedCustomer = customer; }

    /**
     * Returns user to the Main Menu
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

    /**
     * Creates new Customer and updates information in database.
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        Division division = divisionDropdown.getSelectionModel().getSelectedItem();
        Country country = countryDropdown.getSelectionModel().getSelectedItem();
        boolean blankCheck = blankCheck();

        if (blankCheck) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phone = phoneNumField.getText();
            int divisionID = division.getDivisionID();
            int countryID = country.getCountryID();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added customer!");
            alert.setHeaderText("Success");
            alert.setTitle("Customers");
            alert.showAndWait();

            DBCustomersDAO.updateCustomer(new Customer(id, name, address, postalCode, phone, divisionID, countryID));
            switchScene(event, "/software2/software2/view/mainmenu.fxml");
        }
    }

    /**
     * Sets information into Division combo box
     * @param country a Country object
     */
    @FXML
    private void setDivisionDropdown(Country country) {
        ObservableList<Division> divisions = country.getDivisions();
        divisionDropdown.setItems(divisions);
        divisionDropdown.getSelectionModel().selectFirst();
    }

    /**
     * Validates information has been entered in all form fields
     * @return Returns boolean
     */
    private boolean blankCheck() {
        String fieldName = "";

        if (nameField.getText() == "") {
            fieldName = "Name";
        } else if (addressField.getText() == "") {
            fieldName = "Address";
        } else if (postalCodeField.getText() == "") {
            fieldName = "Postal Code";
        } else if (phoneNumField.getText() == "") {
            fieldName = "Phone Number";
        } else if (countryDropdown.getValue() == null) {
            fieldName = "Country";
        } else if (divisionDropdown.getValue() == null) {
            fieldName = "Division";
        }

        if (fieldName != "") {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid value for the " + fieldName + " field");
            alert.setTitle("Add Customer");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Adds division information to combo box based on user selected Country
     * @param event
     */
    @FXML
    private void onActionSelect(ActionEvent event) {
        Country country = countryDropdown.getSelectionModel().getSelectedItem();
        setDivisionDropdown(country);
    }

    /**
     * On startup, adds current customer information to form fields
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idField.setText(String.valueOf(modifiedCustomer.getId()));
        nameField.setText(modifiedCustomer.getName());
        addressField.setText(modifiedCustomer.getAddress());
        postalCodeField.setText(modifiedCustomer.getPostalCode());
        phoneNumField.setText(modifiedCustomer.getPhone());

        ObservableList<Country> countries = DBCountriesDAO.getAllCountries();
        countryDropdown.setItems(countries);
        countryDropdown.setVisibleRowCount(5);

        // grab country data from Customer
        for (Country country: countries) {
            if (country.getCountryID() == modifiedCustomer.getCountryID()) {
                countryDropdown.setValue(country);
                setDivisionDropdown(country);
                break;
            }
        }

        // set divisions based on current country
        ObservableList<Division> divisions = DBDivisionsDAO.getAllDivisions();

        divisionDropdown.setVisibleRowCount(5);

        for (Division division: divisions) {
            if (division.getDivisionID() == modifiedCustomer.getDivisionID()) {
                divisionDropdown.setValue(division);
                break;
            }
        }
    }
}
