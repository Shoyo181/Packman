/**
 * Klasse for PowerUps. 
 */
package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PowerUp extends IkkeLevende{

    Circle powerUp;

    public PowerUp(Rute[][] grid, Vector2D spawnPoint) {
        super(grid, spawnPoint);
        byggPowerUp();
    }
    public void byggPowerUp(){
        powerUp = new Circle();
        powerUp.setCenterX(spawnPoint.getX()*ruteStr + ruteStr/2);
        powerUp.setCenterY(spawnPoint.getY()*ruteStr + ruteStr/2);
        powerUp.setRadius(ruteStr/5);
        powerUp.setFill(Color.PURPLE);
    }



    public Circle getPowerUp() {
        return powerUp;
    }
}
