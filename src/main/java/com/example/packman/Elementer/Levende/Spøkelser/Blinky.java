package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Blinky extends Spøkelser{
    private ImageView blinkyBildeView;

    public Blinky(Rute[][] grid) {
        super(grid);
        try {
            blinkyBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Blinky01.png")));
            blinkyBildeView.setFitWidth(ruteStr);
            blinkyBildeView.setFitHeight(ruteStr);
            blinkyBildeView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void byggBlinky() {
        bildeSpøkelse = blinkyBildeView;
        plasserSpøkelse();
    }

    public ImageView getBlinky() {
        return blinkyBildeView;
    }
}