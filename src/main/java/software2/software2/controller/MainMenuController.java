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
import software2.software2.model.Appointment;
import software2.software2.model.Customer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
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

    public void switchScene(ActionEvent event, String resource) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource(resource)), 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionAdd(ActionEvent event) throws IOException {
        if (customerBtn.isSelected()) {
            switchScene(event, "/software2/software2/view/addCustomer.fxml");
        } else {
            switchScene(event, "/software2/software2/view/addAppointment.fxml");
        }
    }

    @FXML
    private void onActionUpdate(ActionEvent event) throws IOException {
        ObservableList<String> row = (ObservableList<String>) mainTable.getSelectionModel().getSelectedItem();
        String id = row.get(0);

        if (customerBtn.isSelected()) {
            Customer customer = DBCustomersDAO.getCustomer(Integer.parseInt(id));
            UpdateCustomerController.setCustomer(customer);
            switchScene(event, "/software2/software2/view/updateCustomer.fxml");
        } else {
//            Appointment appointment = new Appointment();
        }

    }

    @FXML
    private void onActionDelete() throws SQLException {
        // Grabs selected item from tableview
        ObservableList<String> row = (ObservableList<String>) mainTable.getSelectionModel().getSelectedItem();
        String id = row.get(0);

        // if customer view toggle selected, look for id in customer DB table
        if (customerBtn.isSelected()) {
            Customer selectedCustomer = DBCustomersDAO.getCustomer(Integer.parseInt(id));

            // search appointments DB table for items associated with customer
            ObservableList<Appointment> appointments = DBCustomersDAO.searchAssociatedAppts(selectedCustomer);

            // if no appointments found, removes customer
            if (appointments.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove this customer?");
                alert.setTitle("Remove Customer");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DBCustomersDAO.deleteCustomer(selectedCustomer);

                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Success");
                    alert.setContentText("Customer has been successfully removed");
                    alert.showAndWait();

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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to remove this appointment?");
            alert.setTitle("Remove Appointment");
            Optional<ButtonType> result = alert.showAndWait();

            // remove appointment and display appointment view
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DBAppointmentsDAO.deleteAppointment(selectedAppointment);

                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Success");
                alert.setContentText("Appointment has been successfully removed");
                alert.showAndWait();

                setMainTable(DBAppointmentsDAO.getResultSet());
            }
        }
    }

    @FXML
    private void onActionLogout(ActionEvent event) throws IOException {
        switchScene(event, "/software2/software2/view/login.fxml");
    }

    // Dynamically adds data to mainmenu tableView based on database results
    public void setMainTable(ResultSet rs) {
        //create a list to return
        ObservableList<ObservableList> customers = FXCollections.observableArrayList();
        mainTable.getColumns().clear();
        mainTable.getItems().clear();

        try {
            //loops over each column in resultSet
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn<ObservableList, String> col = new TableColumn<ObservableList, String> (rs.getMetaData().getColumnLabel(i + 1));


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

            while (rs.next()) {
                // creates list of values based on database record values
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                customers.add(row);
            }

            mainTable.setItems(customers);
        }  catch (SQLException e) {
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setMainTable(DBAppointmentsDAO.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBCustomersDAO.setNewCustomerID();
    }
}
