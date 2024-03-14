/* Super klasse som har generelle metoder til begge editor typene, siden de er veldig like
 *
 *
 *
 */

package com.example.packman.editor;

import javafx.geometry.Pos;
<<<<<<< Updated upstream
=======
import javafx.scene.control.Button;
>>>>>>> Stashed changes
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Editor extends BorderPane {
    protected int vinduStrX;
    protected int vinduStrY;
    protected int pxPerRute = 16;
    protected int ruteStr;
    protected VBox pallet, info, midt;

    protected Rectangle[][] grid;         // for å vise grid
    protected Rectangle[][] tile;         // der hvor farge lagres

    protected GridPane gridPane;
    protected GridPane tilePane;
    protected StackPane canvas;


    public Editor( int vinduStrX, int vinduStrY){
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;

        grid = new Rectangle[pxPerRute][pxPerRute];
        tile = new Rectangle[pxPerRute][pxPerRute];
        regnUtRuteStr();

        byggPallet();
        setLeft(pallet);
        byggInfo();
        setRight(info);

    }


    public void regnUtRuteStr(){
        int breddeIgjen = vinduStrX - 150 - 250;
        ruteStr = breddeIgjen / pxPerRute;
        System.out.println("ruteStr: " + ruteStr);
    }

    public void byggPallet(){
        // 150 x px
        pallet = new VBox();
        pallet.setMinWidth(150);
        pallet.setMaxHeight(150);
        pallet.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 1px;");
        pallet.setMaxHeight(vinduStrY);


        //hva pallet viser er definert i subklasser
    }


    public void byggCanvas(){
        // 800 x px orginalt
        midt = new VBox();
        midt.setMinWidth(800);

        canvas = new StackPane();
        leggTilGrid();
        byggTileTab();

        tilePane = byggGrid(tile);
        gridPane = byggGrid(grid);

        canvas.getChildren().addAll(tilePane, gridPane);

        midt.getChildren().add(canvas);

        System.out.println("Canvas er bygget");

    }
    public void byggTileTab(){
        // bygger tabellen for tile tabellen
        System.out.println("ruteStr: " + ruteStr);
        for(int i = 0; i < pxPerRute; i++){
            for(int j = 0; j < pxPerRute; j++){
                tile[i][j] = new Rectangle(ruteStr, ruteStr);
                tile[i][j].setFill(Color.TRANSPARENT);
            }
        }
    }


    public void leggTilGrid(){
        // grid skal bare vise grid i editor
        for(int i = 0; i < pxPerRute; i++){
            for(int j = 0; j < pxPerRute; j++){
                grid[i][j] = new Rectangle(ruteStr -1, ruteStr -1);
            }
        }

        for(int i = 0; i < pxPerRute; i++){
            for(int j = 0; j < pxPerRute; j++){
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
        for(int i = 0; i < pxPerRute; i++){
            for(int j = 0; j < pxPerRute; j++){
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
        info.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 1px;");
        info.setMaxHeight(vinduStrY);

        //knapper og comboxer er definert i subklasser
    }
}
