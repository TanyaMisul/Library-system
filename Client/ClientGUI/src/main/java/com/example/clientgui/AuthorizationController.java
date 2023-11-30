package com.example.clientgui;

import Entites.UserType;
import connectionModule.ConnectionModule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthorizationController {

    @FXML
    private TextField loginInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    void onEnter(ActionEvent event) {
        var login = loginInput.getText();
        var password = passwordInput.getText();

        try {

            Client.login = login;
            Client.userType = ConnectionModule.singUp(login, password);
            if(Client.userType == UserType.UNDEFINED){
                AlertManager.getInstance().ErrorLog("Пользователь не найден!", "");
            }
            else{
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
            }
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка соединения", "");
        }
    }

    @FXML
    void onRegistration(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.registrationView, Client.changingWindowUtility.registrationW, Client.changingWindowUtility.registrationH, "Регистрация");
    }

}
