package com.example.packman;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.editor.Editor;
import com.example.packman.editor.MapEditor;
import com.example.packman.editor.TileEditor;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class PackmanGui extends Application {

    final int WIN_X = 1200;
    final int WIN_Y = 850;
    BorderPane mainPane;
    VBox menu, editorMenu;
    BanePane bane;
    Levende.Retning r = Levende.Retning.INGEN;
    Editor edit;

    Button campain, ekstra, editor, tileEdit, mapEdit;
    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        menu = byggMeny();
        mainPane.setCenter(byggMeny());


        Scene scene = new Scene(mainPane, WIN_X, WIN_Y);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.show();
    }

    public VBox byggMeny() {
        // metode for å bygge meny
        VBox v = new VBox();
        campain = new Button("Campain");
        editor = new Button("Editor");
        ekstra = new Button("Ekstra");

        campain.setOnAction(e -> {
            byggBane();
            mainPane.setCenter(bane);
            bane.requestFocus();
        });

        editor.setOnAction(e -> {
            byggEditorMeny();
            mainPane.setCenter(editorMenu);
        });

        v.getChildren().addAll(campain, editor, ekstra);
        return v;
    }
    public void byggBane() {
        bane = new BanePane("test", WIN_X, WIN_Y);

        bane.setOnKeyPressed(e -> {
            if( e.getCode() == KeyCode.UP)
                r = Levende.Retning.OPP;
            if( e.getCode() == KeyCode.DOWN)
                r = Levende.Retning.NED;
            if( e.getCode() == KeyCode.LEFT)
                r = Levende.Retning.VENSTRE;
            if( e.getCode() == KeyCode.RIGHT)
                r = Levende.Retning.HØYRE;
            if ( e.getCode() == KeyCode.SPACE)
                bane.start();
            if ( e.getCode() == KeyCode.ESCAPE)
                bane.pause();
            if ( e.getCode() == KeyCode.W)
                r = Levende.Retning.INGEN;

            System.out.println("PacMan er i retning: " + r);
            bane.oppdaterPacManRetning(r);
        });

    }

    public void byggEditorMeny(){
        tileEdit = new Button("Tile-Editor");
        mapEdit = new Button("Map-Editor");

        tileEdit.setOnAction(e -> {
            edit = new TileEditor(WIN_X, WIN_Y);
            mainPane.setCenter(edit);
        });

        mapEdit.setOnAction(e -> {
            edit = new MapEditor(WIN_X, WIN_Y);
            mainPane.setCenter(edit);
        });



        editorMenu = new VBox();
        editorMenu.getChildren().addAll(tileEdit, mapEdit);
        mainPane.setCenter(editorMenu);
    }


    public static void main(String[] args) {
        launch();
    }
}
