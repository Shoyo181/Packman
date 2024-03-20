package com.example.packman.misc;

public class VectorDouble {

    private double x, y;
    public VectorDouble(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
}
