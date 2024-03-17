package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pinky extends Spøkelser {

    public Pinky(Rute[][] grid) {
        super(grid);
    }
    public void byggPinky() {
        lev.setFill(Color.PINK);
        plasserSpøkelse();

    }
    public Circle getPinky() {
        return lev;
    }
}

