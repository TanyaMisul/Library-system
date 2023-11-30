package com.example.clientgui;

import Entites.Sex;
import Entites.User;
import Entites.UserType;
import connectionModule.ConnectionModule;
import Entites.UserStatus;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class UserManagementController {

    @FXML
    public void initialize(){
        if(Client.userType == UserType.ADMIN){
            btnStatus.setDisable(!Client.login.equals("admin"));
            btnEdit.setDisable(!Client.login.equals("admin"));
            btnAddEmployee.setDisable(!Client.login.equals("admin"));
        }

        columnFullName.setCellValueFactory(userStringCellDataFeatures -> new ObservableValueBase<>() {
            @Override
            public String getValue() {
                return userStringCellDataFeatures.getValue().getProfile().getFullName();
            }
        });
        columnAge.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userIntegerCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return String.valueOf(userIntegerCellDataFeatures.getValue().getProfile().getAge());
                    }
                };
            }
        });
        columnSex.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        String text;
                        if(userStringCellDataFeatures.getValue().getProfile().getSex() == Sex.MAN)
                            text = "Муж.";
                        else text ="Жен.";
                        return text;
                    }
                };
            }
        });

        columnStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        String text;
                        if(userStringCellDataFeatures.getValue().getStatus() == UserStatus.BANNED)
                            text = "Заблокирован";
                        else text ="";
                        return text;
                    }
                };
            }
        });

        updatePage();
    }

    private void updatePage(){
        if(Client.userType == UserType.ADMIN){
            btnStatus.setDisable(!Client.login.equals("admin"));
            btnEdit.setDisable(!Client.login.equals("admin"));
        }

        ObservableList<User> users = FXCollections.observableArrayList();

        try {
            var list = ConnectionModule.getAllUsers();
            for (var item: list) {
                users.add(item);
            }
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка", "Ошбика соединения");
        }
        usersTable.setItems(null);
        usersTable.refresh();
        usersTable.setItems(users);
    }

    @FXML
    private Button btnStatus;

    @FXML
    private Button btnAddEmployee;
    @FXML
    private Button btnEdit;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, String> columnFullName;

    @FXML
    private TableColumn<User, String> columnAge;

    @FXML
    private TableColumn<User, String> columnSex;
    @FXML
    private TableColumn<User, String> columnStatus;
    @FXML
    void onEdit(ActionEvent event) {
        var user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null){
            AlertManager.getInstance().ErrorLog("Выберите пользователя в таблице!", "");
            return;
        }

        Client.user = user;
        Client.profile = user.getProfile();
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.profileView, Client.changingWindowUtility.profileW, Client.changingWindowUtility.profileH, "Редактирование профиля");
    }

    @FXML
    void onGoBack(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
    }

    @FXML
    void onBooks(ActionEvent event) {
        var user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null){
            AlertManager.getInstance().ErrorLog("Выберите пользователя в таблице!", "");
            return;
        }

        Client.user = user;
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.borrowedView, Client.changingWindowUtility.borrowedW, Client.changingWindowUtility.borrowedH, "Взятые книги");
    }

    @FXML
    void onAddEmployee(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.addEmployeeView, Client.changingWindowUtility.addEmployeeW, Client.changingWindowUtility.addEmployeeH, "Добавление библиотекаря");
    }

    @FXML
    void onChangeAccess(ActionEvent event) {
        var user = usersTable.getSelectionModel().getSelectedItem();
        if(user == null){
            AlertManager.getInstance().ErrorLog("Выберите пользователя в таблице!", "");
            return;
        }

        user.setStatus(user.getStatus() == UserStatus.BANNED? UserStatus.NOT_BANNED:UserStatus.BANNED);
        try {
            ConnectionModule.updateProfile(user.getId(), user);
            updatePage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
