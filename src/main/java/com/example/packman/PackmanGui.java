package com.example.packman;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.editor.Editor;
import com.example.packman.editor.MapEditor;
import com.example.packman.editor.TileEditor;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
        campain.setStyle("-fx-background-color: transparent; -fx-text-fill:#ffe148; -fx-font-family: Consolas" );
        buttonStretch(campain);
        editor = new Button("Editor");
        editor.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        buttonStretch(editor);
        ekstra = new Button("Ekstra");
        ekstra.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        buttonStretch(ekstra);
        v.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #1f2031, #042796); -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; -fx-spacing: 50px; -fx-padding: 10px;");

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
        bane = new BanePane("testIgjen", WIN_X, WIN_Y);

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

    public void byggEditorMeny() {

        tileEdit = new Button("Tile-Editor");
        tileEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas;");
        buttonStretch(tileEdit);
        mapEdit = new Button("Map-Editor");
        mapEdit.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        buttonStretch(mapEdit);
        Button goBack1 = new Button("Back");
        buttonStretch(goBack1);
        goBack1.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        goBack1.setOnAction(e -> mainPane.setCenter(menu));


        tileEdit.setOnAction(e -> {
            edit = new TileEditor(WIN_X, WIN_Y);
            mainPane.setCenter(edit);
            HBox backButton = new HBox();
            backButton.setStyle("-fx-background-color: #000046;");
            Button goBack2 = new Button("Back");
            goBack2.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas; -fx-font-size: 16px;");
            backButton.getChildren().add(goBack2);
            goBack2.setOnAction(e1 -> {
                mainPane.setCenter(editorMenu);
                mainPane.setTop(null);
            });

            mainPane.setTop(backButton);


        });

        mapEdit.setOnAction(e -> {
            edit = new MapEditor(WIN_X, WIN_Y);
            mainPane.setCenter(edit);
            Button goBack3 = new Button("Back");
            HBox backButton2 = new HBox();
            backButton2.setStyle("-fx-background-color: #000046;");
            backButton2.getChildren().add(goBack3);
            goBack3.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas; -fx-font-size: 16px;");
            goBack3.setOnAction(e1 -> {
                mainPane.setCenter(editorMenu);
                mainPane.setTop(null);
            });

        mainPane.setTop(backButton2);


        });




        editorMenu = new VBox();
        editorMenu.getChildren().addAll(tileEdit, mapEdit, goBack1);
        mainPane.setCenter(editorMenu);
        editorMenu.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #1f2031, #042796);  -fx-border-color: black; -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; -fx-spacing: 50px; -fx-padding: 10px;");
        editorMenu.setAlignment(Pos.CENTER);
    }



    public void buttonStretch (Button b) {
        b.setOnMouseEntered(e -> {
            ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.5), b);
            scaleIn.setToX(1.2);
            scaleIn.setToY(1.2);
            scaleIn.play();
        });

        b.setOnMouseExited(e -> {
            ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.5), b);
            scaleOut.setToX(1.0);
            scaleOut.setToY(1.0);
            scaleOut.play();
        });
    }



    public static void main(String[] args) {
        launch();
    }
}
