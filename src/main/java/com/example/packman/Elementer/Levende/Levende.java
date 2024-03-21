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


    public Levende(Rute[][] grid) {
        super(grid);
        retning = Retning.INGEN;
        speed = 2;
        radius = (ruteStr / 2);
        lev = new Circle();

        byggVeggList();
        lev.setRadius(radius);
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
        // skulle egentlig håndtere hitbox og eventuelle grupper(fremvisning) - men kom aldri dit

        currentPosX = x;
        currentPosY = y;

        lev.setCenterX(x);
        lev.setCenterY(y);
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

    public Vector2D getPositionIGridIndex() {
        return new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY());
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
        // metode for å håndtere tunellgjennomgang av levende (pacman og spøkelser)

        //legger alt i en if som søker om levende er nærme kanten til banen
        //vis sjekker alle sidene samtidig
        if (currentPosX <= 0 || ((ruteStr * gridBredde)) <= currentPosX || currentPosY <= 0 || ((ruteStr * gridHøyde)) <= currentPosY) {
            // hvis dette er sant, er levende nærme en kant
            System.out.println("Nærme kant");
            System.out.println("currentPosX: " + currentPosX);

            // vi sjekker nå om vi allerede er i en tunell - så slipper denne metoden å kjøre hundre ganger
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

                // har ikke satt opp kode for opp og ned - hvor det ikker har vært behov for det
                // TODO: legg til kode for å håndtere opp og ned
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
