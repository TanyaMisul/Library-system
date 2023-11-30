package com.example.clientgui;

import connectionModule.ConnectionModule;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        try {

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            ConnectionModule.connectToServer();
            var userType = ConnectionModule.singUp("admin", "admin");
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}