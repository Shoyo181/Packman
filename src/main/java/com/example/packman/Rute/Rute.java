package com.example.packman.Rute;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Rute extends Node {
    /***        Objekt variabler        ***/
    private int id;                             // ref til tilesets
    private Rectangle tile;                     // hvordan ruta ser ut
    private boolean walkable;                   // om packman kan gå på ruta
    private boolean ghostWalk;                  // om spøkelser kan gå på ruta
    //private boolean door;                     // om det er en dør -- kanskje unødvendig når vi har en egen klasse for dette?
    //private boolean home;                     // hjem til spøkelser
    //private boolean canContainIkkeLevende;    // om ruta har fått en rolle, har ett element i seg osv.
    private boolean ledigForElement;            // om rute er ledig for spawning av ett element
    private RuteType type;                      // type av rute (ingenting, gulv, vegg, dør, hjem)
    private Rectangle[][] utseende;              // utseende til rute
    private GridPane utseendePanel;


    /***        Konstruktører        ***/
    public Rute() {
    }
    // Overload
    public Rute(int id, Rectangle tile, boolean walkable, boolean ghostWalk) {
        this.id = id;
        this.tile = new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
        this.walkable = walkable;
        this.ghostWalk = ghostWalk;
    }

    public Rute(int id, RuteType type, Rectangle tile) {
        this.id = id;
        this.type = type;
        sorterVerdier(type);
        this.tile = new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
    }
    public Rute(int id, RuteType type, Rectangle tile, Rectangle[][] utseende) {
        this.id = id;
        this.type = type;
        sorterVerdier(type);
        this.tile = new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
        this.utseende = utseende;
        utseendePanel = new GridPane();
        // setter utseende slik at det kan vise noe visuelt
        for (int i = 0; i < utseende.length; i++) {
            for (int j = 0; j < utseende[i].length; j++) {
                utseendePanel.add(utseende[i][j], i, j);
            }
        }
    }

    public Rectangle[][] getUtseende(){
        return utseende;
    }
    public GridPane getUtseendePanel(){
        return utseendePanel;
    }

    public int getRuteId(){
        return id;
    }
    public Rectangle getTile(){
        return tile;
    }
    public void setRuteStr(double r){
        tile.setHeight(r);
        tile.setWidth(r);
    }
    public void setRuteX(double x){
        tile.setX(x);
    }
    public void setRuteY(double y){
        tile.setY(y);
    }
    public Rute getRute(){
        return this;
    }
    public RuteType getType() {
        return type;
    }
    public void setLedigForElement(boolean b){
        ledigForElement = b;
    }
    public boolean getLedigForElement(){
        return ledigForElement;
    }


    public Rute kopierRute(){
        return new Rute(id, type, tile);
    }

    public Rectangle kopierTile(){
        return new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
    }


    // metode for å sortere boolean verdier basert på enum
    private void sorterVerdier(RuteType t){
        switch (t) {
            case INGEN:
                break;
            case GULV:
                walkable = true;
                ghostWalk = true;
                ledigForElement = true;
                break;
            case VEGG:
                walkable = false;
                ghostWalk = false;
                ledigForElement = false;
                break;
            case DØR, HJEM:
                walkable = false;
                ghostWalk = true;
                ledigForElement = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }

    public enum RuteType {
        INGEN,
        GULV,
        VEGG,
        DØR,
        HJEM
    }
}