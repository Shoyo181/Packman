package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cherry extends IkkeLevende {
    ImageView cherry;
    Circle hitBox;
    public Cherry (Rute[][] grid, Vector2D spawnPoint) {
        super(grid, spawnPoint);
        byggCherry();

    }
    public void byggCherry()  {
        try {
            cherry = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Cherry05.png")));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        hitBox = new Circle();
        hitBox.setCenterX(spawnPoint.getX()*ruteStr + ruteStr/2);
        hitBox.setCenterY(spawnPoint.getY()*ruteStr + ruteStr/2);
        hitBox.setRadius(ruteStr/4);
        hitBox.setFill(Color.PINK);
        cherry.setFitWidth(ruteStr);
        cherry.setFitHeight(ruteStr);
        cherry.setLayoutX(spawnPoint.getX()*ruteStr);
        cherry.setLayoutY(spawnPoint.getY()*ruteStr);

        cherry.setPreserveRatio(true);
    }


    public ImageView getCherry() {
        return cherry;
    }

    public Circle getHitBox() {
        return hitBox;
    }
}
