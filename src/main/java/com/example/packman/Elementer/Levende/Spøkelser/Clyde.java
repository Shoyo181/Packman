package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;

public class Clyde extends Spøkelser {
    private ImageView clydeBildeView;

    public Clyde(Rute[][] grid) {
        super(grid);
        try {
            clydeBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Clyde05.png")));
            clydeBildeView.setFitWidth(ruteStr);
            clydeBildeView.setFitHeight(ruteStr);
            clydeBildeView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void byggClyde() {
        bildeSpøkelse = clydeBildeView;
        plasserSpøkelse();
    }

    public ImageView getClyde() {
        return clydeBildeView;
    }
}