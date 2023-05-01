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
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBContactsDAO;
import software2.software2.DAO.DBCountriesDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.helper.helperFunctions;
import software2.software2.model.Appointment;
import software2.software2.model.Contact;
import software2.software2.model.Country;
import software2.software2.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls user functions for the Reports menu of the application.
 * Allows users to view a variety of reports based on selection.
 */
public class ReportController implements Initializable {
    @FXML
    private TableView mainTable;
    @FXML
    private RadioButton typeBtn;
    @FXML
    private RadioButton monthBtn;
    @FXML
    private RadioButton countryBtn;
    @FXML
    private RadioButton contactBtn;
    @FXML
    private Label itemLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private ComboBox itemDropdown;
    Parent scene;
    Stage stage;

    /** Moves user to a different page within the application.
     @param event An ActionEvent.
     @param resource The file path for the next FXML resource to be loaded.
     */
    public void switchScene(ActionEvent event, String resource, int width, int height) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), width, height);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Returns user to the Login page
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionLogout(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/login.fxml", 400, 400);
    }

    /**
     * Returns user to the Main Menu
     * @param event an ActionEvent
     * @throws IOException
     */
    @FXML
    private void onActionCancel(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/mainmenu.fxml", 1400, 600);
    }

    /**
     * Resets TableColumns in the TableView
     * @param rs a ResultSet
     */
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

    /**
     * Adds and displays new data in the TableView
     * @param rs a ResultSet
     */
    public void setMainTable(ResultSet rs) {
        try {
            // create list to be used in tableView
            ObservableList<ObservableList> records = FXCollections.observableArrayList();

            if (countryBtn.isSelected()) {
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
                    String country = rs.getString("Country");

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
                    row.add(country);

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

                    if (monthBtn.isSelected()) {
                        Month month = (Month) itemDropdown.getSelectionModel().getSelectedItem();
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
                    } else {
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
            }

            // insert records into tableView
            mainTable.setItems(records);
            totalLabel.setText("Total Items: " + records.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays report based on selection
     * @throws SQLException
     */
    @FXML
    private void onComboSelect() throws SQLException {
        try {
            if (contactBtn.isSelected()) {
                Contact contact = (Contact) itemDropdown.getSelectionModel().getSelectedItem();
                setMainTable(DBAppointmentsDAO.getApptByContact(contact.getId()));
            } else if (typeBtn.isSelected()) {
                String type = (String) itemDropdown.getSelectionModel().getSelectedItem();
                setMainTable(DBAppointmentsDAO.getApptByType(type));
            } else if (monthBtn.isSelected()) {
                setMainTable(DBAppointmentsDAO.getResultSet());
            } else if (countryBtn.isSelected()) {
                Country country = (Country) itemDropdown.getSelectionModel().getSelectedItem();
                setMainTable(DBCustomersDAO.getCustByCountry(country.getCountryID()));
            }
        } catch (NullPointerException e) {
            // Do Nothing
            // Prevent error during radio switch
        }
    }

    /**
     * Changes label and combo box for Contact report
     */
    @FXML
    private void onContactSelect() {
        itemDropdown.getItems().clear();
        itemDropdown.setVisibleRowCount(3);
        itemLabel.setText("Contact");
        totalLabel.setText("");
        itemDropdown.setItems(DBContactsDAO.getAllContacts());
    }

    /**
     * Changes label and combo box for Monthly report
     */
    @FXML
    private void onMonthSelect() {
        itemDropdown.getItems().clear();
        itemDropdown.setVisibleRowCount(3);
        itemLabel.setText("Month");
        totalLabel.setText("");

        ObservableList<Month> monthsList = FXCollections.observableArrayList();
        Month[] months = Month.values();
        for (Month month: months) {
            monthsList.add(month);
        }

        itemDropdown.setItems(monthsList);
    }

    /**
     * Changes label and combo box for Type report
     */
    @FXML
    private void onTypeSelect() throws SQLException {
        itemDropdown.getItems().clear();
        itemDropdown.setVisibleRowCount(3);
        itemLabel.setText("Appointment Type");
        totalLabel.setText("");
        itemDropdown.setItems(DBAppointmentsDAO.getApptTypes());
    }

    /**
     * Changes label and combo box for Country report
     */
    @FXML
    private void onCountrySelect() throws SQLException {
        itemDropdown.getItems().clear();
        itemDropdown.setVisibleRowCount(3);
        itemLabel.setText("Country");
        totalLabel.setText("");
        itemDropdown.setItems(DBCountriesDAO.getAllCountries());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
