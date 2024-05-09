/**
 * Klasse for Vector2D. 
 * Grunnen til at vi har brukt dette, er at 
 * det er veldig kjekt å kunne plassere to verdier i en datatype.
 * Blir mest brukt til posisjoner i Grid. 
 */
package com.example.packman.misc;

public class Vector2D {
    private int x, y;
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void kjartan (Kjartan k) {
        this.kjartan = k;
    }

    //Hei test
}
