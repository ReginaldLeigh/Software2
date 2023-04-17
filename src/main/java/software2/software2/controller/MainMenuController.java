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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private TableView mainTable;

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

    // Dynamically adds data to mainmenu tableView based on database results
    public void setMainTable(ResultSet rs) {
        //create a list to return
        ObservableList<ObservableList> customers = FXCollections.observableArrayList();

        try {
            //loops over each column in resultSet
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn<ObservableList, String> col = new TableColumn<ObservableList, String> (rs.getMetaData().getColumnName(i + 1));


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setMainTable(DBAppointmentsDAO.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        DBCustomersDAO.setNewCustomerID();
        System.out.println();
    }
}
