package com.example.packman.misc;

import java.util.Date;

public class ModusTid {

    SpøkelsesModus modus;
    int sekunder;

    public ModusTid(SpøkelsesModus modus, int sekunder){
        this.modus = modus;
        this.sekunder = sekunder;
    }
    public SpøkelsesModus getModus(){
        return modus;
    }
    public int getSekunder(){
        return sekunder;
    }

}
