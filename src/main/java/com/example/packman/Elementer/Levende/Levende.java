/**
 * Klasse for Levende.
 * Inneholder metoder som levene trenger. 
 * Spøkelser og pacman bruker denne.
 */
package com.example.packman.Elementer.Levende;

import com.example.packman.Elementer.Elementer;
import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import com.example.packman.misc.VectorDouble;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Levende extends Elementer {
    protected double startPosX;
    protected double startPosY;
    protected double currentPosX;
    protected double currentPosY;
    protected double kopiX, kopiY;
    protected boolean iTunell = false;
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

    public Circle getHitBox(){ return lev;}


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
        // bruker denne metode for å lage veggListe. slik at levende har noe å kollidere med
        veggList = new ArrayList<>();
        ArrayList<Vector2D> hjemPosisjoner = new ArrayList<>();
        ArrayList<Vector2D> dørPosisjoner = new ArrayList<>();
        for(int x = 0; x < gridBredde; x++){
            for(int y = 0; y < gridHøyde; y++){
                if( grid[x][y].getType() == Rute.RuteType.VEGG || grid[x][y].getType() == Rute.RuteType.DØR) {
                    veggList.add(grid[x][y].getTile());
                }
                if (grid[x][y].getType() == Rute.RuteType.HJEM) {
                    hjemPosisjoner.add(new Vector2D(x, y));
                }
                if (grid[x][y].getType() == Rute.RuteType.DØR) {
                    dørPosisjoner.add(new Vector2D(x, y));
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

    public Vector2D getPositionIGridIndex() {
        return new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY());
    }

    // metode som oversetter grid til kordinater

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
        return (int) ((currentPosX + radius)/ruteStr);
    }
    public int sjekkPosisjonIGridY() {
        //System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (int) ((currentPosY + radius)/ruteStr);
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
    public boolean sjekkKollisjon(double nestX, double nestY, ArrayList<Rectangle> liste) {

        Circle levTest = new Circle(nestX, nestY, radius -2);

        for(Rectangle vegg: liste){
            if(levTest.getBoundsInParent().intersects(vegg.getBoundsInParent())){
                //System.out.println("Kollisjon funnet");
                return true;
            }
            //System.out.println("Ingen kollisjon funnet");
        }
        return false;
    }

    public boolean sjekkRetningLedig(Retning r, ArrayList<Rectangle> liste){
        if(r == null){
            return true;
        }
        switch (r){
            case OPP:
                if (sjekkKollisjon(currentPosX, currentPosY - speed, liste))
                    return false;
                break;
            case NED:
                if (sjekkKollisjon(currentPosX, currentPosY + speed, liste))
                    return false;
                break;
            case HØYRE:
                if (sjekkKollisjon(currentPosX + speed, currentPosY, liste))
                    return false;
                break;
            case VENSTRE:
                if (sjekkKollisjon(currentPosX - speed, currentPosY, liste))
                    return false;
                break;
            case INGEN:
                break;
            default:
                System.out.println("Ugyldig retning");
        }
        return true;
    }

    public void setKordinatPosisjon(VectorDouble v){
        currentPosX = v.getX();
        currentPosY = v.getY();

    }
    public VectorDouble getKordinatPosisjon(){
        return new VectorDouble(currentPosX, currentPosY);
    }

    public void tunellHåndtering() {
        // metode for å håndtere tunellgjennomgang av pacman

        //legger alt i en if som søker om pacman er nærme kanten til banen
        //vis sjekker alle sidene samtidig
        if (currentPosX <= 0 || ((ruteStr * gridBredde)) <= currentPosX || currentPosY <= 0 || ((ruteStr * gridHøyde)) <= currentPosY) {
            // hvis dette er sant, er pacman nærme en kant
            System.out.println("Nærme kant");
            System.out.println("currentPosX: " + currentPosX);

            // vi sjekker nå om vi allerede er i en tunell - hvis vi kom akuratt inn i en tunell nå, så må vi lage kopien
            if (!iTunell) {
                iTunell = true;
                System.out.println("Tunell: " + iTunell);

                // vi finner ut hvilken side pac går inn i tunell

                //sjekker sidene først
                if (currentPosX <= ruteStr) {
                    //venstre
                    System.out.println("Venstre side");
                    //bredde er størrelse på grid uten index

                    kopiX = currentPosX;
                    kopiY = currentPosY;

                    //pacman spawner på andre siden av banen
                    currentPosX = ((gridBredde * ruteStr) - radius) + currentPosX - radius + ruteStr;
                    currentPosY = currentPosY;
                } else if (((ruteStr * gridBredde) - radius) <= currentPosX) {
                    System.out.println("Høyre side");

                    kopiX = currentPosX;
                    kopiY = currentPosY;

                    //pacman spawner på andre siden av banen
                    currentPosX = ((gridBredde * ruteStr) - radius) - currentPosX + radius;
                    currentPosY = currentPosY;

                }
            }

        }else{
            if(iTunell){
                iTunell = false;
            }
        }
    }


    public enum Retning {
        INGEN,
        OPP,
        NED,
        HØYRE,
        VENSTRE

    }
}
