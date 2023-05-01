package software2.software2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import software2.software2.DAO.DBAppointmentsDAO;
import software2.software2.database.JDBC;
import software2.software2.helper.helperFunctions;
import software2.software2.model.Appointment;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/** Creates an application to schedule and organize appointments. */
public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("view/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        ResourceBundle labels = helperFunctions.getResourceBundle();
        stage.setTitle(labels.getString("title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        JDBC.openConnection();
//        Locale.setDefault(new Locale("fr"));
        launch(args);
        JDBC.closeConnection();

    }
}
