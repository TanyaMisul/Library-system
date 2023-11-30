package com.example.clientgui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;

public class ChangingWindowUtility {

    public final String addEmployeeView = "AddEmployeeView.fxml";
    public final double addEmployeeW = 227;
    public final double addEmployeeH = 166;
    public final String authorizationView = "AuthorizationView.fxml";
    public final double authorizationW = 228;
    public final double authorizationH = 123;

    public final String registrationView = "RegistrationView.fxml";
    public final double registrationW = 227;
    public final double registrationH = 273;

    public final String profileView = "ProfileView.fxml";
    public final double profileW = 279;
    public final double profileH = 172;

    public final String borrowedView = "BorrowedManagementView.fxml";
    public final double borrowedW = 780;
    public final double borrowedH = 641;

    public final String bookEditView = "BookEditView.fxml";
    public final double bookEditW = 279;
    public final double bookEditH = 209;

    public final String bookView = "BookManagementView.fxml";
    public final double bookW = 690;
    public final double bookH = 389;

    public final String borrowEdit = "BorrowedEditView.fxml";
    public final double borrowEditW = 285;
    public final double borrowEditH = 134;

    public final String userView = "UserManagementView.fxml";
    public final double userW = 556;
    public final double userH = 400;
    private final Stage stage;
    public <ControllerType> Pair<ControllerType, Scene> getScene(String viewName, double width, double height) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource( viewName));
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <ControllerType, ItemType> Pair<ControllerType, ItemType> getItem(String itemName) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource(itemName));
            ItemType item = fxmlLoader.load();
            ControllerType controller = fxmlLoader.getController();
            return new Pair<>(controller, item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showWindow(Scene scene, String title) {
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void showWindow(String name, double width, double height, String title) {
        stage.setTitle(title);
        var scene = getScene(name, width, height).getValue();
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public ChangingWindowUtility(Stage stage) {
        this.stage = stage;
    }
}
