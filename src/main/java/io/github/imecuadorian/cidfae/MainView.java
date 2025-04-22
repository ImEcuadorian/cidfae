package io.github.imecuadorian.cidfae;

import io.github.imecuadorian.cidfae.controller.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class MainView extends Application {

    private MainController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/io/github/imecuadorian/cidfae/main-view.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Test Management - FAE");
        stage.setScene(scene);
        stage.show();
    }
}