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

    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 1400, 800);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

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

            DBCustomersDAO.addCustomer(new Customer(id, name, address, postalCode, phone, divisionID, countryID));
            switchScene(event, "/software2/software2/view/mainmenu.fxml");
        }
    }

    @FXML
    private void setIDField() {
        idField.setText(String.valueOf(DBCustomersDAO.getNewCustomerID()));
    }

    @FXML
    private void setDivisionDropdown(Country country) {
        ObservableList<Division> divisions = country.getDivisions();
        divisionDropdown.setItems(divisions);
        divisionDropdown.getSelectionModel().selectFirst();
    }

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

    @FXML
    private void onActionSelect(ActionEvent event) {
        Country country = countryDropdown.getSelectionModel().getSelectedItem();
        setDivisionDropdown(country);
    }

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
