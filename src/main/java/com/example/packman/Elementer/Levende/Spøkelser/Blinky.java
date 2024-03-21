/**
 * Klasse for Blinky. 
 */
package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.ModusSamling;
import com.example.packman.misc.ModusTid;
import com.example.packman.misc.SpøkelsesModus;
import com.example.packman.misc.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Blinky extends Spøkelser {
    private ImageView blinkyBildeView;

    public Blinky(Rute[][] grid) {
        super(grid);
        try {
            blinkyBildeView = new ImageView(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Blinky05.png")));
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
        byggStack();

        lagScatterPoint();
    }
    public void flyttBlinky() {
        sjekkModus();

        if (modus == null) {
            return;
        } else if(modus == SpøkelsesModus.CHASE) {
            lagChasePoint();
        }
        bevegSøkelse();
    }

    @Override
    public void lagChasePoint() {
        // Blinky's strategi er å følge posisjonen til Pac-Man.
        chasePos = pacmanPos;
    }

    @Override
    public void lagScatterPoint() {
        // Setter Blinky's scatterPos.
        scatterPos = new Vector2D(grid.length - 1, grid[0].length - 1);
    }

    @Override
    public void byggStack() {
        modusStack = new ModusSamling();
        aktivModusTid = new ModusTid(SpøkelsesModus.ATHOME, 1);

        modusStack.push(new ModusTid(SpøkelsesModus.CHASE, 100000));
        modusStack.push(new ModusTid(SpøkelsesModus.SCATTER, 5));
        modusStack.push(new ModusTid(SpøkelsesModus.CHASE, 20));
        modusStack.push(new ModusTid(SpøkelsesModus.SCATTER, 5));
        modusStack.push(new ModusTid(SpøkelsesModus.CHASE, 20));
        modusStack.push(new ModusTid(SpøkelsesModus.SCATTER, 8));
        modusStack.push(new ModusTid(SpøkelsesModus.CHASE, 15));
        modusStack.push(new ModusTid(SpøkelsesModus.SCATTER, 10));
        modusStack.push(new ModusTid(SpøkelsesModus.PÅVEIUT, 3));
    }
    public ImageView getBlinky() {
        return blinkyBildeView;
    }

}

