package com.example.clientgui;

import Entites.Book;
import connectionModule.ConnectionModule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BookEditController {
    @FXML
    public void initialize(){
        if(Client.book != null) {
            nameInput.setText(Client.book.getName());
            authorInput.setText(Client.book.getAuthor());
            genreInput.setText(Client.book.getGenre());
            amountInput.setText(String.valueOf(Client.book.getAmount()));
        }
    }
    @FXML
    private TextField nameInput;
    @FXML
    private TextField authorInput;
    @FXML
    private TextField genreInput;
    @FXML
    private TextField amountInput;

    @FXML
    void onCancel(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
    }

    @FXML
    void onOK(ActionEvent event) {
        String name = nameInput.getText();
        String author = authorInput.getText();
        String genre = genreInput.getText();
        String amountText = amountInput.getText();

        if(name.isEmpty() || author.isEmpty()|| genre.isEmpty()|| amountText.isEmpty()){
            AlertManager.getInstance().ErrorLog("Ошибка!", "Введите все поля");
            return;
        }

        int countBorowed = 0;
        if(Client.book != null){
            try {
                var list = ConnectionModule.getAllBorrowedBooksForAllUsers();
                for(var book : list){
                    if(Client.book.getId() == book.getBook().getId()){
                        countBorowed++;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        int amount = -1;

        try{
            amount = Integer.valueOf(amountText);
            if(amount<=countBorowed)
                throw new NumberFormatException();
        }
        catch (NumberFormatException e){
            if(Client.book == null)
                AlertManager.getInstance().ErrorLog("Ошибка", "В поле количество должно быть число");
            else
                AlertManager.getInstance().ErrorLog("Ошибка", "В поле количество должно быть число больше чем" + countBorowed + "(кол-во уже взятых книг этой группы)");
            return;
        }
        try {
            if(Client.book == null){
                ConnectionModule.createBook(new Book(0, name, author, genre, amount));
            }
            else{
                Client.book.setName(name);
                Client.book.setAmount(amount);
                Client.book.setAuthor(author);
                Client.book.setGenre(genre);
                ConnectionModule.editBook(Client.book);
            }
            Client.changingWindowUtility.showWindow(Client.changingWindowUtility.bookView, Client.changingWindowUtility.bookW, Client.changingWindowUtility.bookH, "Книги");
        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка!", "");
        }
    }

}
