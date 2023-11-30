package com.example.clientgui;

import Entites.UserType;
import connectionModule.ConnectionModule;
import Entites.Book;
import Entites.User;
import Entites.UserProfile;
import javafx.application.Application;
import javafx.stage.Stage;

public class Client extends Application {

    public static ChangingWindowUtility changingWindowUtility;
    public  static UserType userType;

    public static UserProfile profile;
    public static String login;

    public static User user;
    public static Book book;

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        try {
            ConnectionModule.connectToServer();
            //var userType = ConnectionModule.singUp("admin", "admin");
            // В контексте авторизации доступны singUp, Register, checkIfLoginExist после этого все кроме них
            changingWindowUtility = new ChangingWindowUtility(stage);
            changingWindowUtility.showWindow(changingWindowUtility.authorizationView, changingWindowUtility.authorizationW, changingWindowUtility.authorizationH, "Авторизация");


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}