/**
 * Dette er en super klasse som brukes som basis for alle generelle metoder
 * for editorene vi har laget, siden de har mange likhetstrekk.
 *
 */

package com.example.packman.editor;

import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Editor extends BorderPane {
    protected int vinduStrX;
    protected int vinduStrY;
    protected int ruteStr;
    protected VBox pallet, info, midt;

    protected Rectangle[][] grid;         // For å vise grid.
    protected Rectangle[][] tile;         // Der hvor farge lagres.
    protected int pxPerRute = 16;
    protected int bredde = 0, høyde = 0;
    protected GridPane gridPane;
    protected GridPane tilePane;
    protected StackPane canvas;

    public Editor(){

    }

    public Editor( int vinduStrX, int vinduStrY){
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;

        byggPallet();
        setLeft(pallet);
        byggInfo();
        setRight(info);

    }
    /**
     * Metoden regnUtRuteStr kalkulerer hvor mange ruter det er plass til i begge retninger.
     */

    public void regnUtRuteStr(){
        // TODO: Oppdater slik at den funker begge veier - gjør det ikke nå..
        int breddeIgjen = vinduStrX - 150 - 250;
        ruteStr = breddeIgjen / bredde;
        ruteStr = ruteStr /16;
        ruteStr = ruteStr * 16;
        System.out.println("ruteStr: " + ruteStr);


        grid = new Rectangle[bredde][høyde];
        tile = new Rectangle[bredde][høyde];


    }
    /**
     * Metode for å bygge pallet.
     * Hva det dette inneholder defineres i subklasser.
     */
    public void byggPallet(){
        // 150 x px
        pallet = new VBox();
        pallet.setMinWidth(150);
        pallet.setStyle("-fx-background-color: #181818; -fx-border-width: 1px; -fx-text-fill: #ffffff; -fx-font-family: Consolas; -fx-padding: 10px;");
        pallet.setMaxHeight(vinduStrY);
        pallet.setSpacing(10);

    }

    /**
     * Metode for å bygge selve canvaset .
     */
    public void byggCanvas(){
        // 800 x px orginalt
        // Innparameter er hvor manger ruter den skal ha.
        midt = new VBox();
        midt.setMinHeight(vinduStrY);



        canvas = new StackPane();
        leggTilGrid();
        byggTileTab();
        //canvas.setStyle("-fx-background-color: #000046;");


        tilePane = byggGrid(tile);
        //tilePane.setAlignment(Pos.CENTER);
        //tilePane.setStyle("-fx-background-color: white; -fx-padding: 20px;");
        gridPane = byggGrid(grid);
        //gridPane.setAlignment(Pos.CENTER);
        //gridPane.setStyle("-fx-background-color: white; -fx-padding: 20px;");

        canvas.getChildren().addAll(tilePane, gridPane);

        midt.getChildren().add(canvas);

        System.out.println("Canvas er bygget");

    }
    /**
     * Metode for å bygge tabellen for å vise tiles.
     */
    public void byggTileTab(){

        System.out.println("ruteStr: " + ruteStr);
        for(int i = 0; i < bredde; i++){
            for(int j = 0; j < høyde; j++){
                tile[i][j] = new Rectangle(ruteStr, ruteStr);
                tile[i][j].setFill(Color.TRANSPARENT);
            }
        }
    }


    public void leggTilGrid(){
        // grid skal bare vise grid i editor
        for(int i = 0; i < bredde; i++){
            for(int j = 0; j < høyde; j++){
                grid[i][j] = new Rectangle(ruteStr -1, ruteStr -1);
            }
        }

        for(int i = 0; i < bredde; i++){
            for(int j = 0; j < høyde; j++){
                grid[i][j].setStroke(Color.BLACK);
                grid[i][j].setFill(Color.TRANSPARENT);
                grid[i][j].setStrokeWidth(1);
            }
        }
    }

    public GridPane byggGrid(Rectangle[][] t){
        // 800 x px
        // gris skal være 16 x 16 og alle rutene skal være like store
        GridPane g = new GridPane();
        // legger alle rutene inn i gridpane
        for(int i = 0; i < t.length; i++){
            for(int j = 0; j < t[i].length; j++){
                g.add(t[i][j], i, j);
            }
        }
        return g;
    }

    public void byggInfo(){
        // 250 x px
        // metode som kanskje ikke trenger å være her, er forskjellig fra type editorer
        info = new VBox();
        info.setMinWidth(250);
        info.setMaxHeight(150);
        info.setStyle("-fx-background-color: #181818 ; -fx-border-width: 1px; -fx-text-fill: white; -fx-font-family: Consolas; -fx-padding: 10px;");
        info.setMaxHeight(vinduStrY);
        info.setSpacing(10);

        //knapper og comboxer er definert i subklasser
    }
}
