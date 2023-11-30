package com.example.clientgui;

import Entites.Sex;
import Entites.Response;
import connectionModule.ConnectionModule;
import Entites.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class RegistrationController {

    @FXML
    public void initialize() {

        comboSex.setConverter(new StringConverter<Sex>() {
            @Override
            public String toString(Sex sex) {
                String text;
                if(sex == Sex.MAN)
                    text = "Муж.";
                else text ="Жен.";
                return text;
            }

            @Override
            public Sex fromString(String s) {
                Sex sex = Sex.MAN;
                if(s.contains("Ж"))
                    sex = Sex.WOMAN;
                return sex;
            }
        });
        ArrayList<Sex> list = new ArrayList<>();
        list.add(Sex.MAN);
        list.add(Sex.WOMAN);
        ObservableList<Sex> sexes = FXCollections.observableList(list);

        comboSex.setItems(sexes);
    }
    @FXML
    private TextField fullnameInput;

    @FXML
    private TextField lognInput;

    @FXML
    private TextField ageInput;

    @FXML
    private ComboBox<Sex> comboSex;

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
        var fullname = fullnameInput.getText();
        var sAge = ageInput.getText();
        var sex = comboSex.getValue();

        if(login.isEmpty() ||password.isEmpty() ||repeatPassword.isEmpty() ||fullname.isEmpty() ||sAge.isEmpty() || sex == null){
            AlertManager.getInstance().WarningLog("Поля должны быть заполнены", "Заполните все поля!");
            return;
        }
        if(!password.equals(repeatPassword)){
            AlertManager.getInstance().WarningLog("Ошибка", "Пароли должны совпадать!");
            return;
        }

        int age = 0;

        try
        {
            age = Integer.parseInt(sAge);
            if(age < 16 || age > 90)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e){
            AlertManager.getInstance().WarningLog("Ошибка!", "Введите целое число в графу возраста от 16 до 90");
            return;
        }

        try {

            if( ConnectionModule.registration(login, password, fullname, age, sex) == Response.SUCCESSFULLY){
                Client.userType = UserType.USER;
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
            }
            else{
                AlertManager.getInstance().ErrorLog("Ошибка", "Пользователь с таким логином уже существует");
            }

        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка соединения", "");
        }
    }

}
