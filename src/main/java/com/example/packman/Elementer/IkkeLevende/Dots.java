/**
 * Klasse for Dots.
 */
package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dots extends IkkeLevende {

    private Circle dot;


    public Dots (Rute[][] grid, Vector2D spawnPoint) {
        super(grid, spawnPoint);
        byggDots();
    }
    public void byggDots() {
        dot = new Circle();
        dot.setCenterX(spawnPoint.getX()*ruteStr + ruteStr/2);
        dot.setCenterY(spawnPoint.getY()*ruteStr + ruteStr/2);
        dot.setRadius(ruteStr/10);
        dot.setFill(Color.WHITE);
        dot.setStroke(Color.WHITE);
    }

    public Circle getDot() {
        return dot;
    }
}

