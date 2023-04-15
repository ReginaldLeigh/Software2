package software2.software2.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 1400, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void setCustomer(Customer customer) { modifiedCustomer = customer; }

    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        Division division = divisionDropdown.getSelectionModel().getSelectedItem();

        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneNumField.getText();
        int divisionID = division.getDivisionID();

        DBCustomersDAO.updateCustomer(new Customer(id, name, address, postalCode, phone, divisionID));
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

//    @FXML
//    private void setIDField() {
//        idField.setText(String.valueOf(DBCustomersDAO.getNewCustomerID()));
//    }

    @FXML
    private void setDivisionDropdown(Country country) {
        ObservableList<Division> divisions = country.getDivisions();
        divisionDropdown.setItems(divisions);
        divisionDropdown.getSelectionModel().selectFirst();
    }

    @FXML
    private void onActionSelect(ActionEvent event) {
        Country country = countryDropdown.getSelectionModel().getSelectedItem();
        setDivisionDropdown(country);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idField.setText(String.valueOf(modifiedCustomer.getId()));
        nameField.setText(modifiedCustomer.getName());
        addressField.setText(modifiedCustomer.getAddress());
        postalCodeField.setText(modifiedCustomer.getPostalCode());
        phoneNumField.setText(modifiedCustomer.getPhone());

        ObservableList<Division> divisions = DBDivisionsDAO.getAllDivisions();
        divisionDropdown.setItems(divisions);
        divisionDropdown.setVisibleRowCount(5);

        for (Division division: divisions) {
            if (division.getDivisionID() == modifiedCustomer.getDivisionID()) {
                divisionDropdown.setValue(division);
                break;
            }
        }

        countryDropdown.setItems(DBCountriesDAO.getAllCountries());
        countryDropdown.setPromptText("Please select a country...");
        countryDropdown.setVisibleRowCount(5);
    }
}
