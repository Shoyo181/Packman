package com.example.packman.Rute;

import com.example.packman.misc.IkkeLevendeType;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Rute extends StackPane {
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
    private double ruteStr;
    IkkeLevendeType ikkeLevendeType;


    /***        Konstruktører        ***/
    public Rute() {
    }
    // Overload

    public Rute(int id, RuteType type, Rectangle tile) {
        this.id = id;
        this.type = type;
        sorterVerdier(type);
        this.tile = new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
    }
    public Rute(int id, RuteType type, Rectangle tile, Rectangle[][] utseende, double ruteStr) {
        this.id = id;
        this.type = type;
        this.ruteStr = ruteStr;
        sorterVerdier(type);
        this.tile = new Rectangle(tile.getHeight(), tile.getWidth(), tile.getFill());
        this.utseende = utseende;
        byggRute();
        //System.out.println("En ny Rute er bygget");
    }

    public void byggRute(){
        //metode som setter igang stackpane og legger tile og utseende på plass

        //først setter vi høyden til alle rektangler
        tile.setHeight(ruteStr);
        tile.setWidth(ruteStr);

        //System.out.println("ruteStr: " + ruteStr);
        //System.out.println("ruteStr/16: " + ruteStr/16);
        //System.out.println("ruteStr/16 * 16: " + (ruteStr/16) *16);

        for(int i = 0; i < utseende.length; i++){
            for(int j = 0; j < utseende[i].length; j++){
                utseende[i][j].setHeight(ruteStr/16);
                utseende[i][j].setWidth(ruteStr/16);
                utseende[i][j].setStrokeWidth(0);
            }
        }

        utseendePanel = new GridPane();
        // setter utseende slik at det kan vise noe visuelt
        for (int i = 0; i < utseende.length; i++) {
            for (int j = 0; j < utseende[i].length; j++) {
                utseendePanel.add(utseende[i][j], i, j);
            }
        }
        new StackPane();
        getChildren().add(tile);
        getChildren().add(utseendePanel);
    }

    public void setAlt(Rute r){
        this.ruteStr = r.getRuteStr();
        this.id = r.getRuteId();
        this.type = r.getType();
        this.tile = r.getTile();
        this.utseende = r.getUtseende();
        this.utseendePanel = r.getUtseendePanel();
        new StackPane();
        getChildren().add(tile);
        getChildren().add(utseendePanel);
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
        return (Rute) this;
    }
    public int getRuteStr(){
        return (int) ruteStr;
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
    public void setElementType(IkkeLevendeType t){
        ikkeLevendeType = t;
    }
    public IkkeLevendeType getElementType(){
        return ikkeLevendeType;
    }


    public Rute kopierRute(){
        return new Rute(this.id, this.type, this.tile, this.utseende, this.ruteStr);
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