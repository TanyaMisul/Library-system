package com.example.clientgui;

import Entites.Book;
import Entites.UserType;
import connectionModule.ConnectionModule;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookManagementController {

    boolean isShowAll;

    @FXML
    public void initialize(){
        isShowAll  = true;

        if(Client.userType != UserType.ADMIN){
            usersButton.setText("Профиль");
            btnAdd.setVisible(false);
            btnEdit.setVisible(false);
            btnDel.setVisible(false);
            btnDecrease.setVisible(false);
            inputCount.setVisible(false);
        }
        else {
            labelShow.setVisible(false);
            btnShow.setVisible(false);
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
        amountColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Book, String> bookIntegerCellDataFeatures) {
                return new ObservableValueBase<String>() {
                    @Override
                    public String getValue() {
                        return String.valueOf(bookIntegerCellDataFeatures.getValue().getAmount());
                    }
                };
            }
        });

        updatePage();
    }

    private void updatePage(){
        ObservableList<Book> listEntities = FXCollections.observableArrayList();
        try {
            List<Book> list = null;
            if(isShowAll)
                list = ConnectionModule.getAllBooks();
            else {
                var l = ConnectionModule.getAllBorrowedBooks();
                list = new ArrayList<>();
                for(var i : l){
                    list.add(i.getBook());
                }
                for(var book : list){
                    book.setAmount(1);
                }
            }

            for (var item: list) {
                listEntities.add(item);
            }
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка", "Ошибка соединения");
        }
        typesTable.setItems(listEntities);
    }
    @FXML
    private Button btnDecrease;

    @FXML
    private TextField inputCount;
    @FXML
    private TableView<Book> typesTable;
    @FXML
    private TableColumn<Book, String> nameColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> amountColumn;

    @FXML
    private Label labelShow;
    @FXML
    private Button btnShow;
    @FXML
    private Button usersButton;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDel;

    @FXML
    void onAbout(ActionEvent event) {
        String textInfo = "";
        switch (Client.userType){
            case ADMIN -> {
                textInfo = "Вы администратор.";
            }
            case USER -> {
                textInfo = "Вы пользователь.";
            }
        }
        AlertManager.getInstance().InformationLog("Информация о проекте", textInfo);
    }

    @FXML
    void onExit(ActionEvent event) {
        try {
            ConnectionModule.exit();
            Client.userType = UserType.UNDEFINED;
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.authorizationView, Client.changingWindowUtility.authorizationW, Client.changingWindowUtility.authorizationH, "Авторизация");
        } catch (IOException e) {
            AlertManager.getInstance().ErrorLog("Ошибка", "Ошбика соединения");
        }
    }

    @FXML
    void onAdd(ActionEvent event) {
        Client.book = null;
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookEditView, Client.changingWindowUtility.bookEditW, Client.changingWindowUtility.bookEditH, "Добавление вида услуги");
    }

    @FXML
    void onEdit(ActionEvent event) {
        Book book = typesTable.getSelectionModel().getSelectedItem();
        if(book == null)
            return;

        Client.book = book;
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookEditView, Client.changingWindowUtility.bookEditW, Client.changingWindowUtility.bookEditH, "Редактирование вида услуги");
    }

    @FXML
    void onDel(ActionEvent event) {
        Book book = typesTable.getSelectionModel().getSelectedItem();
        if(book != null){
            try {
                ConnectionModule.deleteBook(book.getId());
            } catch (Exception e) {
                AlertManager.getInstance().ErrorLog("Ошибка", "Ошбика соединения");
            }
        }
        updatePage();
    }

    @FXML
    void onChangeShow(ActionEvent event) {
        isShowAll = !isShowAll;
        btnShow.setText(isShowAll? "Все книги": "Мои книги");
        updatePage();
    }

    @FXML
    void onDecrease(ActionEvent event) {
        Book book = typesTable.getSelectionModel().getSelectedItem();
        if(book == null)
            return;
        String text = inputCount.getText();
        if(text.isEmpty()){
            AlertManager.getInstance().ErrorLog("Ошибка!", "Введите количество");
            return;
        }

        int amount = -1;

        try{
            amount = Integer.valueOf(text);
            if(amount>=book.getAmount())
                throw new NumberFormatException();
        }
        catch (NumberFormatException e){
            if(Client.book == null)
                AlertManager.getInstance().ErrorLog("Ошибка", "В поле должно быть число");
            else
                AlertManager.getInstance().ErrorLog("Ошибка", "В поле должно быть число меньше чем " + book.getAmount());
            return;
        }

        book.setAmount(book.getAmount() - amount);
        try {
            ConnectionModule.editBook(book);
            updatePage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onUsersManagement(ActionEvent event) {
        if(Client.userType == UserType.ADMIN)
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Управление пользователями");
        else{
            Client.profile = null;
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.profileView, Client.changingWindowUtility.profileW, Client.changingWindowUtility.profileH, "Профиль");
        }
    }

}
