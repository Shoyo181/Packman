package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Blinky extends Spøkelser{

    public Blinky(Rute[][] grid) {
        super(grid);
    }
    public void byggBlinky() {
        lev.setFill(Color.RED);
        plasserSpøkelse();

    }
    public Circle getBlinky() {
        return lev;
    }
}


