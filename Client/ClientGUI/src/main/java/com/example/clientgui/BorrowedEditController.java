package com.example.clientgui;

import Entites.Book;
import connectionModule.ConnectionModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

public class BorrowedEditController {

    @FXML
    public void initialize(){

        try {
           bookCombo.setConverter(new StringConverter<Book>() {
               @Override
               public String toString(Book book) {
                   return book.getId() + ". '" + book.getName() + "' " + book.getAuthor();
               }

               @Override
               public Book fromString(String s) {
                   int id = Integer.parseInt(s.substring(0, s.indexOf('.'-1)));
                   try {
                       var books = ConnectionModule.getAllBooks();
                       for(var item: books){
                            if(item.getId() == id)
                                return item;
                       }
                   } catch (Exception e) {
                       throw new RuntimeException(e);
                   }
                   return null;
               }
           });

           var listAll = ConnectionModule.getAllBooks();
           var listBorrowed = ConnectionModule.getAllBorrowedBooks();
           var listOwn = ConnectionModule.getAllBorrowedBooks(Client.user.getId());

            ObservableList<Book> books = FXCollections.observableArrayList();
            for(var item: listAll){

                for(var borrowedItem: listBorrowed)
                    if(item.getId() == borrowedItem.getBook().getId())
                        item.setAmount(item.getAmount() - borrowedItem.getBook().getAmount());

                boolean isNeedToShow = item.getAmount() > 0;

                if(isNeedToShow)
                    for(var borrowedItem: listOwn)
                        if(item.getId() == borrowedItem.getBook().getId())
                            isNeedToShow = false;

                if (isNeedToShow)
                    books.add(item);
            }

            if(books.isEmpty()){
                AlertManager.getInstance().WarningLog("Пользователь уже имеет по экземпляру каждой книги из тех, которые остались в библиотеке", "");
                Client.changingWindowUtility.showWindow(Client.changingWindowUtility.borrowedView, Client.changingWindowUtility.borrowedW, Client.changingWindowUtility.borrowedH, "Взятые книги");
            }

            bookCombo.setItems(books);
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка", "");
        }
    }
    @FXML
    private ComboBox<Book> bookCombo;

    @FXML
    void onCancel(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.borrowedView, Client.changingWindowUtility.borrowedW, Client.changingWindowUtility.borrowedH, "Взятые книги");
    }

    @FXML
    void onSave(ActionEvent event) {
        Book book = bookCombo.getValue();
        if(book == null){
            AlertManager.getInstance().ErrorLog("Ошибка", "Выберите книгу");
            return;
        }

        try {
            ConnectionModule.addBookToUser(Client.user.getId(), book.getId());
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.borrowedView, Client.changingWindowUtility.borrowedW, Client.changingWindowUtility.borrowedH, "Взятые книги");
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка соединения", "");
            return;
        }
    }

}
