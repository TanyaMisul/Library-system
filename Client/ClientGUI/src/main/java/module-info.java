module com.example.clientgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires ConnectionModule;
    requires Entities;
    requires mail;
    requires jfreechart;
    requires jcommon;
    opens com.example.clientgui to javafx.fxml;
    exports com.example.clientgui;
}