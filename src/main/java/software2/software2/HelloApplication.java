package software2.software2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.*;
import software2.software2.database.JDBC;
import software2.software2.helper.helperFunctions;

import java.io.IOException;
import java.util.Locale;

public class HelloApplication extends Application {




    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
//        Locale.setDefault(new Locale("fr"));
//        Locale current = Locale.getDefault();
//        Locale.
//        System.out.println(current.getDisplayLanguage());
////        launch();
        //System.out.println(helperFunctions.getLocalLanguage());
    }
}