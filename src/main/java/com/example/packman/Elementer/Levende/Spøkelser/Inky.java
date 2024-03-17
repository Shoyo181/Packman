package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
public class Inky extends Spøkelser {

    private ImageView inkyBildeView;

    public Inky(Rute[][] grid) {
    super(grid);
        try {
        inkyBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Inky01.png")));
        inkyBildeView.setFitWidth(ruteStr);
        inkyBildeView.setFitHeight(ruteStr);
        inkyBildeView.setPreserveRatio(true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void byggInky() {
        bildeSpøkelse = inkyBildeView;
        plasserSpøkelse();
    }

    public ImageView getInky() {
        return inkyBildeView;
    }

}