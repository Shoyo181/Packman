package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pinky extends Spøkelser {

    private ImageView pinkyBildeView;

    public Pinky(Rute[][] grid) {
        super(grid);
        try {
            pinkyBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Pinky01.png")));
            pinkyBildeView.setFitWidth(ruteStr);
            pinkyBildeView.setFitHeight(ruteStr);
            pinkyBildeView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void byggPinky() {
        bildeSpøkelse = pinkyBildeView;
        plasserSpøkelse();
    }

    public ImageView getPinky() {
        return pinkyBildeView;
    }
}