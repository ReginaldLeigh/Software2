package software2.software2.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBContactsDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.DAO.DBUsersDAO;
import software2.software2.helper.helperFunctions;
import software2.software2.model.Appointment;
import software2.software2.model.Contact;
import software2.software2.model.Customer;
import software2.software2.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AddAppointmentController implements Initializable {

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
    private ComboBox<String> startMins;
    @FXML
    private DatePicker endDatepicker;
    @FXML
    private ComboBox<LocalTime> endHours;
    @FXML
    private ComboBox<String> endMins;
    @FXML
    private ComboBox<Customer> customerDropdown;
    @FXML
    private ComboBox<Contact> contactDropdown;
    @FXML
    private ComboBox<User> userDropdown;


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
        Customer customer = customerDropdown.getSelectionModel().getSelectedItem();
        Contact contact = contactDropdown.getSelectionModel().getSelectedItem();
        User user = userDropdown.getSelectionModel().getSelectedItem();

        int apptId = Integer.parseInt(idField.getText());
        String title = titleField.getText();
        String description = descriptionField.getText();
        String location = locationField.getText();
        String type = typeField.getText();
        int customerId = customer.getId();
        int userId = user.getId();
        int contactId = contact.getId();

        LocalDate startDate = startDatepicker.getValue();
        LocalTime startTime = startHours.getValue();
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime utcStart = helperFunctions.convertToUTC(start);

        LocalDate endDate = endDatepicker.getValue();
        LocalTime endTime = endHours.getValue();
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        LocalDateTime utcEnd = helperFunctions.convertToUTC(end);

        if (timeCheck(start, end) && checkOfficeHrs(start, end) && !checkOverlap(customerId, apptId, start, end)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added appointment!");
            alert.setHeaderText("Success");
            alert.setTitle("Appointments");
            alert.showAndWait();
            DBAppointmentsDAO.addAppointment(new Appointment(apptId, title, description, location, type, utcStart, utcEnd, customerId, userId, contactId));
            switchScene(event, "/software2/software2/view/mainmenu.fxml");
        }
    }

    public void setIdField() {
        idField.setText(String.valueOf(DBAppointmentsDAO.getNewAppointmentID()));
    }

    private boolean checkOverlap(int cust_id, int appt_id, LocalDateTime Astart, LocalDateTime Aend) {
        boolean overlap = false;
        ObservableList<Appointment> appointments = DBAppointmentsDAO.getCustomerAppointments(cust_id);

        for (Appointment appointment: appointments) {
            LocalDateTime Bstart = appointment.getStart();
            LocalDateTime Bend = appointment.getEnd();

            LocalDateTime AstartEST = helperFunctions.convertToEST(Astart);
            LocalDateTime AendEST = helperFunctions.convertToEST(Aend);
            LocalDateTime BstartEST = helperFunctions.convertToEST(Bstart);
            LocalDateTime BendEST = helperFunctions.convertToEST(Bend);

            if ((AstartEST.isAfter(BstartEST) || AstartEST.isEqual(BstartEST)) && (AstartEST.isBefore(BendEST))) {
                overlap = true;
            } else if (AendEST.isAfter(BstartEST) && (AendEST.isBefore(BendEST) || AendEST.isEqual(BendEST))) {
                overlap = true;
            } else if ((AstartEST.isBefore(BstartEST) || AstartEST.isEqual(BstartEST)) && (AendEST.isAfter(BendEST) || AendEST.isEqual(BendEST))) {
                overlap = true;
            }
        }

        if (overlap) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Customer has conflicting appointments. Please select another time");
            alert.setTitle("Appointments");
            alert.showAndWait();
        }

        return overlap;
    }

    public boolean checkOfficeHrs(LocalDateTime start, LocalDateTime end) {
        boolean isOpen = true;
        LocalTime openTime = LocalTime.of(8, 0);
        LocalTime closeTime = LocalTime.of(22, 0);
        LocalDate openDate = start.toLocalDate();
        LocalDate closeDate = end.toLocalDate();

        LocalDateTime open = LocalDateTime.of(openDate, openTime);
        LocalDateTime close = LocalDateTime.of(openDate, closeTime);

        if (start.isBefore(open) || end.isAfter(close)) {
            isOpen = false;
        } else if (start.getDayOfWeek() == DayOfWeek.SATURDAY || start.getDayOfWeek() == DayOfWeek.SUNDAY) {
            isOpen = false;
        } else if (end.getDayOfWeek() == DayOfWeek.SATURDAY || end.getDayOfWeek() == DayOfWeek.SUNDAY) {
            isOpen = false;
        }

        if (!isOpen) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected appointment occurs outside of office hours (Monday - Friday 08:00 to 22:00 EST). Please try again");
            alert.setTitle("Appointments");
            alert.showAndWait();
        }

        return isOpen;
    }

    public boolean timeCheck(LocalDateTime start, LocalDateTime end) {
        boolean inOrder = true;
        if (start.isAfter(end)) {
            inOrder = false;
            Alert alert = new Alert(Alert.AlertType.ERROR, "Start time is scheduled after End time. Please select new options and try again.");
            alert.setTitle("Appointments");
            alert.showAndWait();
        }

        return inOrder;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setIdField();

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
