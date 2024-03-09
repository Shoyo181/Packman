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


    public Levende(Rute[][] grid) {
        super(grid);
        retning = Retning.INGEN;
        speed = 3;
    }


    // metoder for å sjekke om noe kolliderer ved å gå en retning på banen.

    public void setRetning(Retning r) {
        retning = r;
    }


    public boolean sjekkRetning(Retning r){
       // gjør ferdig så for vi har metode til å oversette x og y til posisjon i grid
       int x = sjekkPosX();
       int y = sjekkPosY();
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
       }
       return false;
   }

    // metode som oversetter grid til kordinater
    public void sjekkKordinatIGrid(int x, int y) {


    }

    // Metode som sjekker hvilken grid pos levende er i på
    public int sjekkPosX() {
        System.out.println("sjekkPosX(): " + ((currentPosX + ruteStr/2)/ruteStr));
        return (int) ((currentPosX + ruteStr/2)/ruteStr);
    }
    public int sjekkPosY() {
        System.out.println("sjekkPosY(): " + ((currentPosY + ruteStr/2)/ruteStr));
        return (int) ((currentPosY + ruteStr/2)/ruteStr);
    }

    // Metode som sjekker hvilken grid pos levende er i nå
    public void sjekkPosisjonIGrid() {

    }

    public enum Retning {
        INGEN,
        OPP,
        NED,
        HØYRE,
        VENSTRE

    }
}
