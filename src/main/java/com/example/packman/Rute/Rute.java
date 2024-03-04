package com.example.packman.Rute;

import javafx.scene.shape.Rectangle;

public class Rute {
    /***        Objekt variabler        ***/
    private int id;                 // ref til tilesets
    private Rectangle tile;         // hvordan ruta ser ut
    private boolean walkable;       // om packman kan gå på ruta
    private boolean ghostWalk;      // om spøkelser kan gå på ruta
    private boolean door;           // om det er en dør -- kanskje unødvendig når vi har en egen klasse for dette?
    private boolean home;           // hjem til spøkelser

    /***        Konstruktører        ***/
    public Rute() {
    }
    // Overload
    public Rute(int id, Rectangle tile, boolean walkable, boolean ghostWalk, boolean door, boolean home) {
        this.id = id;
        this.tile = tile;
        this.walkable = walkable;
        this.ghostWalk = ghostWalk;
        this.door = door;
        this.home = home;
    }
    public Rectangle getTile(){
        return tile;
    }
    public void setRuteStr(double r){
        tile.setHeight(r);
        tile.setWidth(r);
    }
    public Rute kopierRute(){
        return new Rute(id, tile, walkable, ghostWalk, door, home);
    }
    public Rectangle kopierTile(){
        return new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
    }
    public void setRuteX(double x){
        tile.setX(x);
    }
    public void setRuteY(double y){
        tile.setY(y);
    }

}
