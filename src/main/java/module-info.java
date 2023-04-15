module software2.software2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens software2.software2 to javafx.fxml;
    opens software2.software2.controller to javafx.fxml;
    opens software2.software2.model to javafx.fxml;
    exports software2.software2;
    exports software2.software2.model;
}