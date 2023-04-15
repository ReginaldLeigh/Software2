package software2.software2.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import software2.software2.DAO.DBUsersDAO;
import software2.software2.helper.helperFunctions;
import software2.software2.model.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label timeZoneLabel;

    @FXML
    private Button submitBtn;


    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 1400, 800);
        stage.setScene(scene);
        stage.show();
    }

    private String getLocalTimeZone() {
        return helperFunctions.getSystemZoneId().toString();
    }
    

    private void setLoginLabels() {
        ResourceBundle labels = helperFunctions.getResourceBundle();
        usernameLabel.setText(labels.getString("username"));
        passwordLabel.setText(labels.getString("password"));
        submitBtn.setText(labels.getString("submit"));
        timeZoneLabel.setText(labels.getString("timezone") + ": " + getLocalTimeZone());
    }

    @FXML
    void onActionLogin(ActionEvent event) throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();
        boolean usernameFound = false;
        boolean passwordMatch = false;

        // Search user list for matching username
        for (User user: DBUsersDAO.getAllUsers()) {
            if (Objects.equals(username, user.getUsername())) {
                usernameFound = true;
            }

            // If match found, check password
            if (usernameFound && Objects.equals(password, user.getPassword())) {
                passwordMatch = true;
            }
        }

        // Display error message if username/password information not found
        if (!usernameFound || !passwordMatch) {
            ResourceBundle labels = helperFunctions.getResourceBundle();
            Alert alert = new Alert(Alert.AlertType.ERROR, labels.getString("error"));
            alert.setTitle(labels.getString("alertTitle"));
            alert.showAndWait();
        }

        // If username/password match, allow user access
        if (usernameFound && passwordMatch) {
            switchScene(event, "/software2/software2/view/mainmenu.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLoginLabels();
        getLocalTimeZone();
    }
}
