package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Elementer.Levende.Levende;

public class Spøkelser extends Levende {
    protected SpøkelsesType type;

    protected SpøkelsesModus modus;





    public Spøkelser( SpøkelsesType type, double startPosX, double startPosY, double currentPosX, double currentPosY, double speed, SpøkelsesModus modus) {
        super(startPosX, startPosY, currentPosX, currentPosY, speed);
        this.type = type;
        this.modus = modus;
    }
}



