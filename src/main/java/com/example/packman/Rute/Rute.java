package com.example.packman.Rute;

import javafx.scene.shape.Rectangle;

public class Rute {
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


    /***        Konstruktører        ***/
    public Rute() {
    }
    // Overload
    public Rute(int id, Rectangle tile, boolean walkable, boolean ghostWalk) {
        this.id = id;
        this.tile = tile;
        this.walkable = walkable;
        this.ghostWalk = ghostWalk;

    }

    public Rute(int id, Rectangle tile, RuteType type) {
        this.id = id;
        this.tile = tile;
        this.type = type;
        sorterVerdier(type);
    }

    public Rectangle getTile(){
        return tile;
    }
    public void setRuteStr(double r){
        tile.setHeight(r);
        tile.setWidth(r);
    }
    public Rute kopierRute(){
        return new Rute(id, tile, type);
    }
    public Rute getRute(){
        return this;
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

    public RuteType getType() {
        return type;
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
    public enum RuteType {
        INGEN,
        GULV,
        VEGG,
        DØR,
        HJEM
    }
}