package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Rute.Rute;
import javafx.scene.shape.Circle;

public class PowerUp extends IkkeLevende{

    Circle powerUp;

    public PowerUp(Rute[][] grid) {
        super(grid);
        powerUp = new Circle();
    }
}
