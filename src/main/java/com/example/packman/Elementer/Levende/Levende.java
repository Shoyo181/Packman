package com.example.packman.Elementer.Levende;

import com.example.packman.Elementer.Elementer;
import com.example.packman.Rute.Rute;

public class Levende extends Elementer {
    protected double startPosX;
    protected double startPosY;
    protected double currentPosX;
    protected double currentPosY;
    protected Rute currentRute;
    protected double speed;
    protected Retning retning;

    //variabler for pixel plassering av levende
    protected int plasStartX, plasStartY, plasEndX, plasEndY;


    public Levende(Rute[][] grid) {
        super(grid);
        retning = Retning.INGEN;
        speed = 1;
    }


    // metoder for å sjekke om noe kolliderer ved å gå en retning på banen.

    public void setRetning(Retning r) {
        retning = r;
    }


    public boolean sjekkRetning(Retning r){
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

    public boolean sjekkRetningNy(){
        // sjekker først om retning er satt
        if(retning == null) {
            return false;
        }
        // sjekker først hvliken retningn levende går siden vi regner ut kollisjon forskjelldig for hver retning
        // setter verdi fra senter for nå - bearbeider de etter
        int x = sjekkPosisjonIGridX();
        int y = sjekkPosisjonIGridY();

        if(retning == Retning.OPP){
            //må vi regne fra top y , x ikke farlig
            y = (int) ((currentPosY + ruteStr/2)/ruteStr ) -1;
        }
        if(retning == Retning.NED){
            //må vi regne fra bunn y , x ikke farlig
            y = (int) ((currentPosY + ruteStr/2)/ruteStr ) +1;
        }
        if(retning == Retning.HØYRE){
            //må vi regne fra venstre x , y ikke farlig
            x = (int) ((currentPosX + ruteStr/2)/ruteStr ) +1;
        }
        if(retning == Retning.VENSTRE){
            //må vi regne fra høyre x , y ikke farlig
            x = (int) ((currentPosX + ruteStr/2)/ruteStr ) -1;
        }
        System.out.println("x: " + x + ", y: " + y);

        switch (retning){
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
        System.out.println("sjekkPosX(): " + ((currentPosX + ruteStr/2)/ruteStr));
        return (int) ((currentPosX + ruteStr/2)/ruteStr);
    }
    public int sjekkPosisjonIGridY() {
        System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (int) ((currentPosY + ruteStr/2)/ruteStr);
    }

    // bygger nye metoder for å sjekke om levende kolliderer på banen.



    public enum Retning {
        INGEN,
        OPP,
        NED,
        HØYRE,
        VENSTRE

    }
}
