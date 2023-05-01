package software2.software2.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBCountriesDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.model.Appointment;
import software2.software2.model.Country;
import software2.software2.model.Customer;
import software2.software2.model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Presents form which allows user to add new customers to system.
 */
public class AddCustomerController implements Initializable {

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
     * Returns user to the Main Menu
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

    /**
     * Creates new Customer and pushes information into database.
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        Division division = divisionDropdown.getSelectionModel().getSelectedItem();
        Country country = countryDropdown.getSelectionModel().getSelectedItem();
        boolean blankCheck = blankCheck();

        if (blankCheck) {

            int id = DBCustomersDAO.getNewCustomerID();
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

            DBCustomersDAO.addCustomer(new Customer(id, name, address, postalCode, phone, divisionID, countryID));
            switchScene(event, "/software2/software2/view/mainmenu.fxml");
        }
    }

    /**
     * Sets Customer ID field
     */
    @FXML
    private void setIDField() {
        idField.setText(String.valueOf(DBCustomersDAO.getNewCustomerID()));
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
     * On startup, places all available countries into combo box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIDField();

        countryDropdown.setItems(DBCountriesDAO.getAllCountries());
        countryDropdown.setPromptText("Please select a country...");
        countryDropdown.setVisibleRowCount(5);

        divisionDropdown.setPromptText("Please select a division...");
        divisionDropdown.setVisibleRowCount(5);
    }
}
