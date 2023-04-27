package software2.software2.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.helper.helperFunctions;
import software2.software2.model.Appointment;
import software2.software2.model.Customer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private TableView mainTable;
    @FXML
    private RadioButton appointmentBtn;
    @FXML
    private RadioButton customerBtn;
    @FXML
    private Button AddBtn;
    @FXML
    private Button DeleteBtn;
    @FXML
    private Button UpdateBtn;
    Parent scene;
    Stage stage;

    public void switchScene(ActionEvent event, String resource, int width, int height) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), width, height);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        if (customerBtn.isSelected()) {
            switchScene(event, "/software2/software2/view/addCustomer.fxml", 500, 400);
        } else {
            switchScene(event, "/software2/software2/view/addAppointment.fxml", 700, 550);
        }
    }

    @FXML
    private void onActionUpdate(ActionEvent event) throws IOException {
        ObservableList<String> row = (ObservableList<String>) mainTable.getSelectionModel().getSelectedItem();

        if (row == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an item to update");
            alert.setTitle("Main Menu");
            alert.showAndWait();
        } else {
            String id = row.get(0);

            if (customerBtn.isSelected()) {
                Customer customer = DBCustomersDAO.getCustomer(Integer.parseInt(id));
                UpdateCustomerController.setCustomer(customer);
                switchScene(event, "/software2/software2/view/updateCustomer.fxml", 500, 400);
            } else {
                Appointment appointment = DBAppointmentsDAO.getAppointment(Integer.parseInt(id));
                UpdateAppointmentController.setAppointment(appointment);
                switchScene(event, "/software2/software2/view/updateAppointment.fxml", 700, 550);
            }
        }
    }

    @FXML
    private void onActionDelete() throws SQLException {
        // Grabs selected item from tableview
        ObservableList<String> row = (ObservableList<String>) mainTable.getSelectionModel().getSelectedItem();

        if (row == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select an item to delete");
            alert.setTitle("Main Menu");
            alert.showAndWait();
        } else {
            String id = row.get(0);

            // if customer view toggle selected, look for id in customer DB table
            if (customerBtn.isSelected()) {
                Customer selectedCustomer = DBCustomersDAO.getCustomer(Integer.parseInt(id));

                // search appointments DB table for items associated with customer
                ObservableList<Appointment> appointments = DBCustomersDAO.searchAssociatedAppts(selectedCustomer);

                // if no appointments found, removes customer
                if (appointments.size() == 0) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove this customer?" + "\n" +
                            "Customer ID: " + String.valueOf(selectedCustomer.getId()));
                    alert.setTitle("Remove Customer");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        DBCustomersDAO.deleteCustomer(selectedCustomer);

                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Success");
                        alert.setContentText("Customer " + String.valueOf(selectedCustomer.getId()) + " has been successfully removed");
                        alert.showAndWait();

                        DBCustomersDAO.setNewCustomerID();
                        setMainTable(DBCustomersDAO.getResultSet());
                    }
                } else {
                    // display warning alert to user
                    Alert alert = new Alert(Alert.AlertType.WARNING, "All associated appointments must be deleted before removing customer");
                    alert.setTitle("Remove Customer");
                    alert.showAndWait();
                }
            } else {
                // grab appointment data from appointment DB table
                Appointment selectedAppointment = DBAppointmentsDAO.getAppointment(Integer.parseInt(id));

                // display confirmation alert to user
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove the following appointment?" + "\n" +
                        "Appointment ID: " + String.valueOf(selectedAppointment.getId()) + "\n" +
                        "Appointment Type: " + selectedAppointment.getType());
                alert.setTitle("Remove Appointment");
                Optional<ButtonType> result = alert.showAndWait();

                // remove appointment and display appointment view
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DBAppointmentsDAO.deleteAppointment(selectedAppointment);

                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Success");
                    alert.setContentText("Appointment " + String.valueOf(selectedAppointment.getId()) + " has been successfully removed");
                    alert.showAndWait();

                    DBAppointmentsDAO.setNewAppointmentID();
                    setMainTable(DBAppointmentsDAO.getResultSet());
                }
            }
        }
    }

    @FXML
    private void onActionLogout(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/login.fxml", 400, 400);
    }


    public void resetTableColumns(ResultSet rs) {
        mainTable.getColumns().clear();
        mainTable.getItems().clear();

        try {
            //loops over each column in resultSet
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn<ObservableList, String> col = new TableColumn<ObservableList, String>(rs.getMetaData().getColumnLabel(i + 1));
                col.setStyle("-fx-alignment: CENTER;");

                col.setCellValueFactory(param -> {
                    String value;

                    if (param.getValue().get(j) == null) {
                        value = "";
                    } else {
                        value = param.getValue().get(j).toString();
                    }

                    return new SimpleStringProperty(value);
                });

                mainTable.getColumns().addAll(col);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMainTable(ResultSet rs) {
        try {
            // create list to be used in tableView
            ObservableList<ObservableList> records = FXCollections.observableArrayList();

            if (customerBtn.isSelected()) {
                resetTableColumns(rs);

                while (rs.next()) {
                    int id = rs.getInt("Customer ID");
                    String name = rs.getString("Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal Code");
                    String phone = rs.getString("Phone");
                    String createDate = rs.getString("Create Date");
                    String createdBy = rs.getString("Created By");
                    String lastUpdate = rs.getString("Last Update");
                    String lastUpdatedBy = rs.getString("Last Updated By");
                    String division = rs.getString("Division");

                    // creates list of values based on resultSet records
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(String.valueOf(id));
                    row.add(name);
                    row.add(address);
                    row.add(postalCode);
                    row.add(phone);
                    row.add(createDate);
                    row.add(createdBy);
                    row.add(lastUpdate);
                    row.add(lastUpdatedBy);
                    row.add(division);

                    records.add(row);
                }
            } else {
                resetTableColumns(rs);

                while (rs.next()) {
                    int id = rs.getInt("Appointment ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    Timestamp startDateTime = rs.getTimestamp("Start Time");
                    Timestamp endDateTime = rs.getTimestamp("End Time");
                    int customerId = rs.getInt("Customer ID");
                    int userId = rs.getInt("User ID");
                    String contact = rs.getString("Contact Name");

                    LocalDateTime start = startDateTime.toLocalDateTime();
                    LocalDateTime end = endDateTime.toLocalDateTime();
                    LocalDate startDate = start.toLocalDate();
                    LocalTime startTime = start.toLocalTime();
                    LocalDate endDate = end.toLocalDate();
                    LocalTime endTime = end.toLocalTime();


                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(String.valueOf(id));
                    row.add(title);
                    row.add(description);
                    row.add(location);
                    row.add(contact);
                    row.add(type);
                    row.add(startDate.toString());
                    row.add(startTime.toString());
                    row.add(endDate.toString());
                    row.add(endTime.toString());
                    row.add(String.valueOf(customerId));
                    row.add(String.valueOf(userId));

                    records.add(row);
                }
            }

            // insert records into tableView
            mainTable.setItems(records);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onCustomerSelect(ActionEvent event) throws SQLException {
        setMainTable(DBCustomersDAO.getResultSet());
        AddBtn.setText("Add Customer");
        DeleteBtn.setText("Delete Customer");
        UpdateBtn.setText("Update Customer");
    }

    @FXML
    private void onApptSelect(ActionEvent event) throws SQLException {
        setMainTable(DBAppointmentsDAO.getResultSet());
        AddBtn.setText("Add Appointment");
        DeleteBtn.setText("Delete Appointment");
        UpdateBtn.setText("Update Appointment");
    }

    public void filterMonth(ResultSet rs) {
        try {
            // create list to be used in tableView
            ObservableList<ObservableList> records = FXCollections.observableArrayList();
            LocalDate today = LocalDate.now();
            Month month = today.getMonth();

            resetTableColumns(rs);

            while (rs.next()) {
                int id = rs.getInt("Appointment ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp startDateTime = rs.getTimestamp("Start Time");
                Timestamp endDateTime = rs.getTimestamp("End Time");
                int customerId = rs.getInt("Customer ID");
                int userId = rs.getInt("User ID");
                String contact = rs.getString("Contact Name");

                LocalDateTime start = startDateTime.toLocalDateTime();
                LocalDateTime end = endDateTime.toLocalDateTime();
                LocalDate startDate = start.toLocalDate();
                LocalTime startTime = start.toLocalTime();
                LocalDate endDate = end.toLocalDate();
                LocalTime endTime = end.toLocalTime();

                if (startDate.getMonth() == month) {

                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(String.valueOf(id));
                    row.add(title);
                    row.add(description);
                    row.add(location);
                    row.add(contact);
                    row.add(type);
                    row.add(startDate.toString());
                    row.add(startTime.toString());
                    row.add(endDate.toString());
                    row.add(endTime.toString());
                    row.add(String.valueOf(customerId));
                    row.add(String.valueOf(userId));

                    records.add(row);
                }
            }
            // insert records into tableView
            mainTable.setItems(records);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void filterWeek(ResultSet rs) {
        try {
            // create list to be used in tableView
            ObservableList<ObservableList> records = FXCollections.observableArrayList();
            LocalDate today = LocalDate.now();
            int currWeek = today.get(WeekFields.of(helperFunctions.getLocale()).weekOfYear());

            resetTableColumns(rs);

            while (rs.next()) {
                int id = rs.getInt("Appointment ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp startDateTime = rs.getTimestamp("Start Time");
                Timestamp endDateTime = rs.getTimestamp("End Time");
                int customerId = rs.getInt("Customer ID");
                int userId = rs.getInt("User ID");
                String contact = rs.getString("Contact Name");

                LocalDateTime start = startDateTime.toLocalDateTime();
                LocalDateTime end = endDateTime.toLocalDateTime();
                LocalDate startDate = start.toLocalDate();
                LocalTime startTime = start.toLocalTime();
                LocalDate endDate = end.toLocalDate();
                LocalTime endTime = end.toLocalTime();

                int apptWeek = startDate.get(WeekFields.of(helperFunctions.getLocale()).weekOfYear());
                if (apptWeek == currWeek) {

                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(String.valueOf(id));
                    row.add(title);
                    row.add(description);
                    row.add(location);
                    row.add(contact);
                    row.add(type);
                    row.add(startDate.toString());
                    row.add(startTime.toString());
                    row.add(endDate.toString());
                    row.add(endTime.toString());
                    row.add(String.valueOf(customerId));
                    row.add(String.valueOf(userId));

                    records.add(row);
                }
            }


            // insert records into tableView
            mainTable.setItems(records);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onActionMonth() throws SQLException {
        filterMonth(DBAppointmentsDAO.getResultSet());
    }

    @FXML
    private void onActionWeek() throws SQLException {
        filterWeek(DBAppointmentsDAO.getResultSet());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setMainTable(DBAppointmentsDAO.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBCustomersDAO.setNewCustomerID();
        DBAppointmentsDAO.setNewAppointmentID();
    }
}
