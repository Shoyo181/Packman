/**
 * GUI for Packman
 * Håndterer fremvisning av hele programmet.
 * @author Kjartan Øyen
 * @author Mie Rønningen
 */
package com.example.packman;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.editor.Editor;
import com.example.packman.editor.MapEditor;
import com.example.packman.editor.TileEditor;
import com.example.packman.misc.Vector2D;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PackmanGui extends Application {

    final int WIN_X = 1200;
    final int WIN_Y = 850;
    BorderPane mainPane;
    VBox menu, editorMenu;
    HBox venstre, høyre;
    BanePane bane;
    Levende.Retning r = Levende.Retning.INGEN;
    Editor edit;
    GridPane campMenu;

    Button campain, ekstra, editor, tileEdit, mapEdit;
    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        menu = byggMeny();
        mainPane.setCenter(byggMeny());

        Scene scene = new Scene(mainPane, WIN_X, WIN_Y);
        stage.setTitle("Kjartan og Mies packman");
        stage.setScene(scene);

        stage.show();
    }

    public VBox byggMeny()  {
        // metode for å bygge meny
        VBox v = new VBox();
        Label title = new Label("Packman");
        title.setStyle("-fx-font-size: 100px; -fx-font-family: 'MS Gothic'; -fx-text-fill: #ffe148");
        ImageView pacmanB = null;
        try {
            pacmanB = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Pacman05.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pacmanB.setPreserveRatio(true);
        pacmanB.setFitWidth(100);
        pacmanB.setFitHeight(100);
        HBox pacmanBilde = new HBox();
        pacmanBilde.getChildren().add(pacmanB);
        pacmanBilde.setAlignment(Pos.TOP_CENTER);


        campain = new Button("Campain");
        campain.setStyle("-fx-background-color: transparent; -fx-text-fill:#ffe148; -fx-font-family: Consolas" );
        buttonStretch(campain);
        editor = new Button("Editor");
        editor.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        buttonStretch(editor);
        ekstra = new Button("Ekstra");
        ekstra.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffe148; -fx-font-family: Consolas");
        buttonStretch(ekstra);
        v.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #181818); -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; -fx-spacing: 50px; -fx-padding: 10px;");

        campain.setOnAction(e -> {

            campainMenu();

            mainPane.setCenter(campMenu);

        });

        editor.setOnAction(e -> {
            byggEditorMeny();
            mainPane.setCenter(editorMenu);

        });

        v.getChildren().addAll(title, pacmanBilde, campain, editor, ekstra);
        return v;
    }
    public void campainMenu() {
        /*
         * Lager meny for ulike map, med bilder som viser layout
         * av hvordan banene ser ut. I tillegg en onclick på knappene,
         * slik at man velger hvilken map man vil spille på.
         */
        Button b1 = new Button("Bane 1");
        buttonStretch(b1);
        b1.setStyle("-fx-background-color: transparent; -fx-text-fill:#ffe148; -fx-font-family: Consolas" );
        b1.setOnAction(e -> {
            byggBane2();
            mainPane.setCenter(bane);
            Vector2D strIgjen = bane.getVinduStrIgjen();
            høyre = new HBox();
            venstre = new HBox();
            høyre.setMinWidth(strIgjen.getX());
            venstre.setMinWidth(strIgjen.getX());


            mainPane.setRight(høyre);
            mainPane.setLeft(venstre);

            bane.requestFocus();
            mainPane.setStyle("-fx-background-color: #181818");
        });
        ImageView kart2BildeView = null;
        try {
            kart2BildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Kart2.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        kart2BildeView.setFitWidth(200);
            kart2BildeView.setFitHeight(200);

        Button b2 = new Button("Bane 2");
        buttonStretch(b2);
        b2.setStyle("-fx-background-color: transparent; -fx-text-fill:#ffe148; -fx-font-family: Consolas" );

        ImageView grøntBildeView = null;
        try {
            grøntBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/GrøntMap.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        grøntBildeView.setFitWidth(200);
        grøntBildeView.setFitHeight(200);
        b2.setOnAction(e -> {

            byggBane();
            mainPane.setCenter(bane);
            Vector2D strIgjen = bane.getVinduStrIgjen();
            høyre = new HBox();
            venstre = new HBox();
            høyre.setMinWidth(strIgjen.getX());
            venstre.setMinWidth(strIgjen.getX());


            mainPane.setRight(høyre);
            mainPane.setLeft(venstre);
            bane.requestFocus();
            mainPane.setStyle("-fx-background-color: #181818");
        });
        /*Button b3 = new Button("Level 3");
        b3.setStyle("-fx-background-color: transparent; -fx-text-fill:#ffe148; -fx-font-family: Consolas" );*/

        //cM.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #181818); -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; -fx-spacing: 50px; -fx-padding: 10px;");
        //cM.getChildren().addAll(b1, b2, b3, kart2BildeView);


        campMenu = new GridPane();

        campMenu.setHgap(10);
        campMenu.setVgap(20);
        campMenu.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #181818); -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; ");

        campMenu.add(b1, 0, 0);
        campMenu.add(kart2BildeView, 1, 0);
        campMenu.add(b2, 0, 1);
        campMenu.add(grøntBildeView, 1, 1);
       // campMenu.add(b3, 0, 2);

    }



    public void byggBane() {
        /*
         * Bygger første map, og setter knappene trykkbare.
         */

        bane = new BanePane("GrøntMap", WIN_X, WIN_Y);
        bane.setPadding(new Insets(5));
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
    public void byggBane2() {
        /*
         * Bygger andre map og lager knappene trykkbare.
         */
        bane = new BanePane("Kart2", WIN_X, WIN_Y);
        bane.setPadding(new Insets(5));
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

        /*
         * Meny for tile- og map-editor.
         */

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
            backButton.setStyle("-fx-background-color: #181818;");
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
            backButton2.setStyle("-fx-background-color: #181818;");
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
        editorMenu.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #000000, #181818);  -fx-border-color: black; -fx-border-width: 1px; -fx-alignment: center; -fx-font-family: 'Droid Sans Mono'; -fx-font-size: 40px; -fx-spacing: 50px; -fx-padding: 10px;");
        editorMenu.setAlignment(Pos.CENTER);
    }

    public void buttonStretch (Button b) {
        /*
         * Lager metode for buttons med stretch når man holder over knappene.
         */
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
