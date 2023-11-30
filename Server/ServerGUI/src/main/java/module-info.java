module com.example.servergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires Entities;

    opens ServerGui to javafx.fxml;
    exports ServerGui;
}