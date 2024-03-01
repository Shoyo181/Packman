package com.example.packman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PackmanGui extends Application {

    BorderPane mainPane;

    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}
