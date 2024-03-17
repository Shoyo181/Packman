package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Rute.Rute;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dots extends IkkeLevende {

    private Circle dot;


    public Dots (Rute[][] grid) {
        super(grid);
        byggDots();
    }
    public void byggDots() {
        dot = new Circle();
        dot.setRadius(ruteStr/10);
        dot.setFill(Color.WHITE);
        dot.setStroke(Color.WHITE);
    }

    public Circle getDot() {
        return dot;
    }
}

