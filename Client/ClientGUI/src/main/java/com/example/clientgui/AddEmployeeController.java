package com.example.clientgui;

import Entites.Response;
import connectionModule.ConnectionModule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AddEmployeeController {

    @FXML
    public void initialize() {
    }

    @FXML
    private TextField lognInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private PasswordField repeatPasswordInput;
    @FXML
    void onCancel(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.authorizationView, Client.changingWindowUtility.authorizationW, Client.changingWindowUtility.authorizationH, "Авторизация");
    }

    @FXML
    void onEnter(ActionEvent event) {
        var login = lognInput.getText();
        var password = passwordInput.getText();
        var repeatPassword = repeatPasswordInput.getText();

        if(login.isEmpty() ||password.isEmpty() ||repeatPassword.isEmpty()){
            AlertManager.getInstance().WarningLog("Поля должны быть заполнены", "Заполните все поля!");
            return;
        }
        if(!password.equals(repeatPassword)){
            AlertManager.getInstance().WarningLog("Ошибка", "Пароли должны совпадать!");
            return;
        }


        try {

            if( ConnectionModule.addAdmin(login, password) == Response.SUCCESSFULLY){
                AlertManager.getInstance().InformationLog("Успешно", "Новый библиотекарь зарегистрирован");
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Пользователи");
            }
            else{
                AlertManager.getInstance().ErrorLog("Ошибка", "Пользователь с таким логином уже существует");
            }

        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка соединения", "");
        }
    }

}
