package software2.software2.controller;

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
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

    @FXML
    private TextField IDField;
    private TextField nameField;
    private TextField addressField;
    private TextField postalCodeField;
    private TextField phoneNumField;
    private ComboBox divisionDropdown;
    private ComboBox countryDropdown;

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
        int id = Integer.parseInt(IDField.getText());
        String title = nameField.getText();
        String description;
        String location;
        String type;
        String start;
        String end;
        int customerId;
        int userId;
        int contactId;

        DBCustomersDAO.addCustomer(new Customer(4, "test1", "new test", "georgia", "test", 999));
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
