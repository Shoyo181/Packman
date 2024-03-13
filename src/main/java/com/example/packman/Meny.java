package com.example.packman;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Meny extends VBox {

    private Button campain, ekstra, editor;

    public Meny() {
        byggMeny();
    }

    private void byggMeny() {
        campain = new Button("Campain");
        editor = new Button("Editor");
        ekstra = new Button("Ekstra");


        //campain.setOnAction(e -> new BanePane("test", 1000));

        this.getChildren().addAll(campain, editor, ekstra);
    }


}
