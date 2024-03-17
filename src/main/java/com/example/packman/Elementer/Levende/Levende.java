package com.example.packman.Elementer.Levende;

import com.example.packman.Elementer.Elementer;
import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Levende extends Elementer {
    protected double startPosX;
    protected double startPosY;
    protected double currentPosX;
    protected double currentPosY;
    protected Rute currentRute;
    protected double speed;
    protected Retning retning;
    protected int radius;
    protected ArrayList<Rectangle> veggList;

    protected Circle lev;
    protected Rectangle levHitBox;

    //variabler for pixel plassering av levende
    protected int plasStartX, plasStartY, plasEndX, plasEndY;


    public Levende(Rute[][] grid) {
        super(grid);
        retning = Retning.INGEN;
        speed = 2;
        radius = (ruteStr / 2);
        lev = new Circle();
       /*
        levHitBox = new Rectangle();
        levHitBox.setWidth(ruteStr);
        levHitBox.setHeight(ruteStr);
        levHitBox.setFill(Color.RED);
*/
        byggVeggList();

        lev.setRadius(radius);
    }

    protected void byggHitBox() {
        levHitBox.setX(startPosX - radius);
        levHitBox.setY(startPosY - radius);
        System.out.println("x (s): " + startPosX + ", y (s): " + startPosY);

    }


    // metoder for å sjekke om noe kolliderer ved å gå en retning på banen.

    public void setRetning(Retning r) {
        if(r == null) {
            return;
        }
        retning = r;
    }
    public Retning getRetning() {
        return retning;
    }
    public void setHitBox(double x, double y){
        currentPosX = x;
        currentPosY = y;

        double topX = x - ruteStr/2;
        double topY = y - ruteStr/2;
        /*
        levHitBox.setX(topX);
        levHitBox.setY(topY);
        levHitBox.setWidth(ruteStr);
        levHitBox.setHeight(ruteStr);

        */
        lev.setCenterX(x);
        lev.setCenterY(y);

        //System.out.println("x: " + topX + ", y: " + topY);
        //System.out.println("x (c): " + x + ", y (c): " + y);
    }

    public void byggVeggList(){
        veggList = new ArrayList<>();
        for(int x = 0; x < gridBredde; x++){
            for(int y = 0; y < gridHøyde; y++){
                if( grid[x][y].getType() != Rute.RuteType.GULV ){
                    veggList.add(grid[x][y].getTile());
                }
            }
        }
    }


    public boolean sjekkRetningGammel(Retning r){
       // gjør ferdig så for vi har metode til å oversette x og y til posisjon i grid
       int x = sjekkPosisjonIGridX();
       int y = sjekkPosisjonIGridY();
       System.out.println("x: " + x + ", y: " + y);
       if(r == null) {
           return false;
       }
       switch (r){
           case NED:
               if( getTypeUnder(x, y) == Rute.RuteType.GULV)
                   return true;
               break;
           case OPP:
               if( getTypeOver(x, y) == Rute.RuteType.GULV)
                   return true;
               break;
           case HØYRE:
               if( getTypeHøyre(x, y) == Rute.RuteType.GULV)
                   return true;
               break;
           case VENSTRE:
               if( getTypeVenstre(x, y) == Rute.RuteType.GULV)
                   return true;
               break;
           case INGEN:
               break;
           default:
               System.out.println("Ugyldig retning");
       }
       return false;
   }

    public boolean sjekkRetning(Retning r){
        // sjekker først om retning er satt
        if(r == null) {
            return false;
        }
        // sjekker først hvliken retningn levende går siden vi regner ut kollisjon forskjelldig for hver retning
        // setter verdi fra senter for nå - bearbeider de etter
        int x = sjekkPosisjonIGridX();
        int y = sjekkPosisjonIGridY();
        System.out.println("x: " + x + ", y: " + y);
        if(r == Retning.OPP){
            //må vi regne fra top y , x ikke farlig
            y = (int) (((currentPosY - ruteStr/2) - speed)/ruteStr )+1;
        }
        if(r == Retning.NED){
            //må vi regne fra bottom y , x ikke farlig
            y = (int) (((currentPosY + ruteStr/2) + speed)/ruteStr );
        }
        if(r == Retning.HØYRE){
            //må vi regne fra right x , y ikke farlig
            x = (int) (((currentPosX + ruteStr/2) + speed)/ruteStr );
        }
        if(r == Retning.VENSTRE){
            //må vi regne fra left x , y ikke farlig
            System.out.println("currentPosX - ruteStr/2: " + (currentPosX - ruteStr/2));
            System.out.println( "x i double:" + (((currentPosX - ruteStr/2) - speed)/ruteStr ));
            x = (int) (((currentPosX - ruteStr/2) - speed)/ruteStr )+1;
        }
        System.out.println("x: " + x + ", y: " + y);

        switch (r){
            case NED:
                if( getTypeUnder(x, y) == Rute.RuteType.GULV)
                    return true;
                break;
            case OPP:
                if( getTypeOver(x, y) == Rute.RuteType.GULV)
                    return true;
                break;
            case HØYRE:
                if( getTypeHøyre(x, y) == Rute.RuteType.GULV)
                    return true;
                break;
            case VENSTRE:
                if( getTypeVenstre(x, y) == Rute.RuteType.GULV)
                    return true;
                break;
            case INGEN:
                break;
            default:
                System.out.println("Ugyldig retning");
        }
        return false;
    }



    // metode som oversetter grid til kordinater
    public void sjekkKordinatIGrid(int x, int y) {


    }

    // Metode som sjekker hvilken posisjon levende er i banen (pixel)
    // unødvendig - siden vi har variabel for det.. kanskje nyttig med mtode for å regne offset fra senter rute?
    public double sjekkPosX() {
        System.out.println("sjekkPosX(): " + ((currentPosX + ruteStr/2)/ruteStr));
        return (currentPosX + ruteStr/2)/ruteStr;
    }
    public double sjekkPosY() {
        System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (currentPosY + ruteStr/2)/ruteStr;
    }

    // Metode som sjekker hvilken grid pos levende er i nå
    public int sjekkPosisjonIGridX() {
        //System.out.println("sjekkPosX(): " + ((currentPosX + ruteStr/2)/ruteStr));
        return (int) ((currentPosX + ruteStr/2)/ruteStr);
    }
    public int sjekkPosisjonIGridY() {
        //System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (int) ((currentPosY + ruteStr/2)/ruteStr);
    }
    public int sjekkPosisjonIGridIndexX() {
        //System.out.println("sjekkPosX(): " + ((currentPosX + ruteStr/2)/ruteStr));
        return (int) ((currentPosX + radius)/ruteStr)-1;
    }
    public int sjekkPosisjonIGridIndexY() {
        //System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (int) ((currentPosY + radius)/ruteStr)-1;
    }

    // bygger nye metoder for å sjekke om levende kolliderer på banen.


    public boolean sjekkKollisjon() {

        if(retning == null){
            return false;
        }
        // disse returnerer uten å tenke på index
        Vector2D xAndY = new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY());

        Rectangle r;
        Rute rute;
        double rY, rX;
        switch (retning){
            case OPP:
                rute = getRuteOver(xAndY.getX(), xAndY.getY());
                r = rute.getTile();
                rX = r.getX();
                rY = r.getY() + r.getHeight();



                System.out.println("UP    - TileY: " + rY + " ElementY: " + (currentPosY - radius));
                System.out.println("UP    - TileX: " + rX + " ElementX: " + (currentPosX + radius));
                //System.out.println("TileY: " + rY + " ElementY: " + (currentPosY - ruteStr/2));
                double rEkstra = rX + 64;
                // hvis dette ikke er sant betyr det at spiller endten går til venstre eller så er det ledig plass å gå på
                if(currentPosY - radius  <= rY && Rute.RuteType.GULV != rute.getType()){  // problemer siden rute over forandrer seg............
                    // selvom dette funker kan man fortsatt gå igjennom veggen hvis elementet er halveis forbi ruta
                    // så vi må regne ut x også
                    return true;
                }
                //det betyr at vi må ha en if til som sjekker tl venstre for spiller og vi må hente ny rute


                break;
            case NED:
                rute = getRuteUnder(xAndY.getX(), xAndY.getY());
                r = rute.getTile();
                rY = r.getY();
                System.out.println("DOWN  - TileY: " + rY + " ElementY: " + (currentPosY + radius));





                if(currentPosY + radius  >= rY && rute.getType() != Rute.RuteType.GULV){
                    return true;
                }
                break;
            case HØYRE:
                rute = getRuteHøyre(xAndY.getX(), xAndY.getY());
                r = rute.getTile();
                rX = r.getX();
                System.out.println("RIGHT - TileX: " + rX + " ElementX: " + (currentPosX + radius));
                if(currentPosX + radius  >= rX && rute.getType() != Rute.RuteType.GULV){
                    return true;
                }
                break;
            case VENSTRE:
                rute = getRuteVenstre(xAndY.getX(), xAndY.getY());
                r = rute.getTile();
                rX = r.getX() + r.getWidth();
                System.out.println("LEFT  - TileX: " + rX + " ElementX: " + (currentPosX - radius));
                if(currentPosX - radius  <= rX && rute.getType() != Rute.RuteType.GULV){
                    return true;
                }
                break;
            case INGEN:
                break;
            default:
                System.out.println("Ugyldig retning");
        }


        return false;
    }


    public boolean kollisjonFunnetRute(Rute r) {
        //test - ga ingen mening
        if (lev.getBoundsInParent().intersects(r.getBoundsInParent())) {
            System.out.println("Kollisjon funnet");
            return true;
        }
        System.out.println("Ingen kollisjon funnet");
        return false;
    }
    public boolean sjekkKollisjon(double nestX, double nestY){

        Circle levTest = new Circle(nestX, nestY, radius -1);

        for(Rectangle vegg: veggList){
            if(levTest.getBoundsInParent().intersects(vegg.getBoundsInParent())){
                //System.out.println("Kollisjon funnet");
                return true;
            }
            //System.out.println("Ingen kollisjon funnet");
        }
        return false;
    }

    public boolean sjekkRetningLedig(Retning r){
        if(r == null){
            return true;
        }
        switch (r){
            case OPP:
                if (sjekkKollisjon(currentPosX, currentPosY - speed))
                    return false;
                break;
            case NED:
                if (sjekkKollisjon(currentPosX, currentPosY + speed))
                    return false;
                break;
            case HØYRE:
                if (sjekkKollisjon(currentPosX + speed, currentPosY))
                    return false;
                break;
            case VENSTRE:
                if (sjekkKollisjon(currentPosX - speed, currentPosY))
                    return false;
                break;
            case INGEN:
                break;
            default:
                System.out.println("Ugyldig retning");
        }


        return true;
    }


    public enum Retning {
        INGEN,
        OPP,
        NED,
        HØYRE,
        VENSTRE

    }
}
