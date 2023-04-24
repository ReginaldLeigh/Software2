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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBContactsDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.DAO.DBUsersDAO;
import software2.software2.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class UpdateAppointmentController implements Initializable {

    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    @FXML
    private DatePicker startDatepicker;
    @FXML
    private ComboBox<LocalTime> startHours;
    @FXML
    private DatePicker endDatepicker;
    @FXML
    private ComboBox<LocalTime> endHours;
    @FXML
    private ComboBox<Customer> customerDropdown;
    @FXML
    private ComboBox<Contact> contactDropdown;
    @FXML
    private ComboBox<User> userDropdown;


    Parent scene;
    Stage stage;

    private static Appointment modifiedAppointment;

    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 1400, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void setAppointment(Appointment appointment) { modifiedAppointment = appointment; }

    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }

    @FXML
    private void onActionSave(ActionEvent event) throws IOException {
        Customer customer = customerDropdown.getSelectionModel().getSelectedItem();
        Contact contact = contactDropdown.getSelectionModel().getSelectedItem();
        User user = userDropdown.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatepicker.getValue();
        LocalDate endDate = endDatepicker.getValue();
        LocalTime startTime = startHours.getValue();
        LocalTime endTime = endHours.getValue();


        int id = Integer.parseInt(idField.getText());
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        int customerId = customer.getId();
        int userId = user.getId();
        int contactId = contact.getId();

        DBAppointmentsDAO.addAppointment(new Appointment(id, title, description, location, type, start, end, customerId, userId, contactId));
        switchScene(event, "/software2/software2/view/mainmenu.fxml");
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idField.setText(String.valueOf(modifiedAppointment.getId()));
        titleField.setText(modifiedAppointment.getTitle());
        descriptionField.setText(modifiedAppointment.getDescription());
        locationField.setText(modifiedAppointment.getLocation());
        typeField.setText(modifiedAppointment.getType());
        startDatepicker.setValue(modifiedAppointment.getStart().toLocalDate());
        startHours.setValue(modifiedAppointment.getStart().toLocalTime());
        endDatepicker.setValue(modifiedAppointment.getEnd().toLocalDate());
        endHours.setValue(modifiedAppointment.getEnd().toLocalTime());
        customerDropdown.setValue(DBCustomersDAO.getCustomer(modifiedAppointment.getCustomerId()));


        LocalTime start = LocalTime.of(0,0);
        LocalTime end = LocalTime.of(23,50);

        while(start.isBefore(end.plusSeconds(1))) {
            startHours.getItems().add(start);
            endHours.getItems().add(start);

            if (start.getMinute() == 50 && start.getHour() == 23) {
                break;
            }

            start = start.plusMinutes(10);
        }

        startHours.setVisibleRowCount(5);
        endHours.setVisibleRowCount(5);

        customerDropdown.setItems(DBCustomersDAO.getAllCustomers());
        contactDropdown.setItems(DBContactsDAO.getAllContacts());
        userDropdown.setItems(DBUsersDAO.getAllUsers());
    }
}
