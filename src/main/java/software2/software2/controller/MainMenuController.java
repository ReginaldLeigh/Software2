package software2.software2.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.model.Appointment;
import software2.software2.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> descCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> startTimeCol;
    @FXML
    private TableColumn<Appointment, String> endTimeCol;
    @FXML
    private TableColumn<Appointment, Integer> custIDCol;
    @FXML
    private TableColumn<Appointment, Integer> userIDCol;

    Parent scene;
    Stage stage;


    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 500, 500);
        stage.setScene(scene);
        stage.show();
    }

//    @FXML
//    private void setAppointmentsTable(ObservableList<Appointment> appointments) {
//        appointmentsTable.setItems(appointments);
//        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
//        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
//        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
//        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
//        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
//        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
//        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
//        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
//    }

    @FXML
    private void setAppointmentsTable(ObservableList<Appointment> appointments) {
        appointmentsTable.setItems(appointments);
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
//        DBAppointmentsDAO.addAppointment(new Appointment(4, "test1", "new test", "georgia", "test", "0110-01-01", "0110-01-01", 3, 1, 3));
        switchScene(event, "/software2/software2/view/addCustomer.fxml");
    }

    @FXML
    private void onActionAddAppt(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/addAppointment.fxml");
    }

    @FXML
    private void onActionUpdate(ActionEvent event) throws IOException {
        Customer customer = new Customer(4,"name", "address", "postal", "phone", 1);
        UpdateCustomerController.setCustomer(customer);
        switchScene(event, "/software2/software2/view/updateCustomer.fxml");
    }

    @FXML
    private void onActionUpdateAppt() {
        DBAppointmentsDAO.updateAppointment(new Appointment(4, "test2", "new test", "georgia", "test", "0110-01-01", "0110-01-01", 3, 1, 3));
    }

    @FXML
    private void onActionDelete() {
        DBAppointmentsDAO.deleteAppointment(new Appointment(4, "test2", "new test", "georgia", "test", "0110-01-01", "0110-01-01", 3, 1, 3));
    }

    @FXML
    private void onActionDeleteAppt() {
        DBAppointmentsDAO.deleteAppointment(new Appointment(4, "test2", "new test", "georgia", "test", "0110-01-01", "0110-01-01", 3, 1, 3));
    }

    @FXML
    private void onActionLogout(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAppointmentsTable(DBAppointmentsDAO.getAllAppointments());
        DBCustomersDAO.setNewCustomerID();
        System.out.println();
    }
}
