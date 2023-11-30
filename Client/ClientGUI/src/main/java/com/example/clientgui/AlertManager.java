package com.example.clientgui;

import javafx.scene.control.Alert;

public class AlertManager implements InformationWriter {

    private AlertManager(){}

    private static AlertManager instance;

    public static AlertManager getInstance(){
        if(instance == null)
            instance = new AlertManager();

        return instance;
    }
    public void ErrorLog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    public void WarningLog(String header, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    public void InformationLog(String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait().ifPresent(rs -> {
        });
    }

}
