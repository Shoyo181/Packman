package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.SpøkelsesModus;
import com.example.packman.misc.Vector2D;
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
            clydeBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Clyde01.png")));
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

    public void flyttClyde() {
        //metoden er til for å bevege dete spøkelse
        if (modus == null) {
            return;
        }
        bevegSøkelse();
    }




    public ImageView getClyde() {
        return clydeBildeView;
    }
}