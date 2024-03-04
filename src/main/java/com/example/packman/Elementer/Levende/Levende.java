package com.example.packman.Elementer.Levende;

import com.example.packman.Elementer.Elementer;

public class Levende extends Elementer {
    protected double startPosX;
    protected double startPosY;
    protected double currentPosX;
    protected double currentPosY;
    protected double speed;

    public Levende(double startPosX, double startPosY, double currentPosX, double currentPosY, double speed) {
        this.startPosX = startPosX;
        this.startPosY = startPosY;
        this.currentPosX = currentPosX;
        this.currentPosY = currentPosY;
        this.speed = speed;
    }
}
