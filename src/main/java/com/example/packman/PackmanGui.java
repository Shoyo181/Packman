package com.example.packman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class PackmanGui extends Application {

    final int WIN = 500;
    BorderPane mainPane;
    Rute[][] grid;

    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, WIN, WIN);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public GridPane mapSetUp(int b, int h){
        // setup, g returneres, grid to dim tabell som tar vare på alle rektangler for nå
        GridPane g = new GridPane();
        grid = new Rute[b][h];

        // hvor stor en rute skal være
        int rute;
        if(h < b){
            rute = (WIN-100)/b;
        }else{
            rute = (WIN-100)/h;
        }

        return g;
    }
















    public static void main(String[] args) {
        launch();
    }
}
