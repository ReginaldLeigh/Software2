package software2.software2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.DAO.DBCustomersDAO;
import software2.software2.database.JDBC;
import software2.software2.helper.helperFunctions;
import software2.software2.model.Appointment;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("view/mainmenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 800);
        ResourceBundle labels = helperFunctions.getResourceBundle();
        stage.setTitle(labels.getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void getAppointments() {
        for (Appointment appointment: DBAppointmentsDAO.getAllAppointments()) {
            System.out.println(appointment.getContactId());
            System.out.println(appointment.getId());
            System.out.println(appointment.getDescription());
            System.out.println(appointment.getTitle());
            System.out.println(appointment.getType());
            System.out.println(appointment.getStart());
            System.out.println(appointment.getEnd());
            System.out.println(appointment.getCustomerId());
            System.out.println(appointment.getUserId());
            System.out.println("");
        }
    }

    public static void main(String[] args) {
        JDBC.openConnection();
        // Locale.setDefault(new Locale("fr"));
        launch(args);
        JDBC.closeConnection();

    }
}
