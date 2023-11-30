package com.example.clientgui;

import Entites.Sex;
import Entites.UserType;
import connectionModule.ConnectionModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class ProfileController {

    @FXML
    public void initialize() {
        sexCombo.setConverter(new StringConverter<Sex>() {
            @Override
            public String toString(Sex sex) {
                String text;
                if (sex == Sex.MAN)
                    text = "Муж.";
                else text = "Жен.";
                return text;
            }

            @Override
            public Sex fromString(String s) {
                Sex sex = Sex.MAN;
                if (s.contains("Ж"))
                    sex = Sex.WOMAN;
                return sex;
            }
        });
        ArrayList<Sex> list = new ArrayList<>();
        list.add(Sex.MAN);
        list.add(Sex.WOMAN);
        ObservableList<Sex> sexes = FXCollections.observableList(list);

        sexCombo.setItems(sexes);

        if (Client.profile == null) {
            try {
                Client.profile = ConnectionModule.getProfile().getProfile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        fullnameInput.setText(Client.profile.getFullName());
        ageInput.setText(String.valueOf(Client.profile.getAge()));
        sexCombo.setValue(Client.profile.getSex());
    }

    @FXML
    private TextField fullnameInput;

    @FXML
    private ComboBox<Sex> sexCombo;

    @FXML
    private TextField ageInput;

    @FXML
    void onCancel(ActionEvent event) {
        if (Client.userType == UserType.ADMIN)
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Управление пользователями");
        else
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
    }

    @FXML
    void onOK(ActionEvent event) {
        String fullname = fullnameInput.getText();
        String sAge = ageInput.getText();
        Sex sex = sexCombo.getValue();
        if (fullname.isEmpty() || sAge.isEmpty() || sex == null) {
            AlertManager.getInstance().ErrorLog("Ошибка!", "Введите все поля");
            return;
        }

        int age = 0;

        try {
            age = Integer.parseInt(sAge);
            if (age < 16 || age > 90)
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            AlertManager.getInstance().WarningLog("Ошибка!", "Введите целое число в графу возраста от 16 до 90");
            return;
        }

        Client.user.getProfile().setAge(age);
        Client.user.getProfile().setFullName(fullname);
        Client.user.getProfile().setSex(sex);

        try {
            if (Client.userType == UserType.ADMIN){
                ConnectionModule.updateProfile(Client.user.getId(), Client.user);
            } else {
                ConnectionModule.updateCurrentProfile(Client.profile);
            }

            if (Client.userType == UserType.ADMIN)
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Управление пользователями");
            else
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Предоставляемые услуги");
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка!", "");
        }
    }

}
