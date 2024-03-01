package com.example.packman.Elementer.Levende.Spøkelser;

public class Pinky extends Spøkelser{

    public Pinky(SpøkelsesType type, double startPosX, double startPosY, double currentPosX, double currentPosY, double speed, SpøkelsesModus modus) {
        super(type, startPosX, startPosY, currentPosX, currentPosY, speed, modus);
    }

    public Pinky(double startPosX, double startPosY, double currentPosX, double currentPosY, double speed, SpøkelsesModus modus) {
        super(SpøkelsesType.PINKY, startPosX, startPosY, currentPosX, currentPosY, speed, modus);
    }

}
