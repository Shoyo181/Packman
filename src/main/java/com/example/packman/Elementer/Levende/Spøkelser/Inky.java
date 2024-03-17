package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Inky extends Spøkelser {

    public Inky(Rute[][] grid) {
        super(grid);
    }


    public void byggInky() {
        lev.setFill(Color.CYAN);
        plasserSpøkelse();

    }

    public Circle getInky() {
        return lev;
    }
}

