/**
 * Klasse for Inky.
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

public class Inky extends Spøkelser {
    private ImageView inkyBildeView;
    private Image bildeInky;
    Vector2D blinkyPos;

    public Inky(Rute[][] grid) {
        super(grid);
        lagScatterPoint();
        try {
            bildeInky = new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Inky05.png"));
            inkyBildeView = new ImageView(bildeInky);
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
        byggStack();
    }
    public void flyttInky() {
        sjekkModus();

        if (modus == null) {
            return;
        } else if (harVærtRedd) {
            bildeSpøkelse.setImage(bildeInky);
            harVærtRedd = false;
        } else if(harNåddHjem && bleSpist) {
            bildeSpøkelse.setImage(bildeInky);
            bleSpist = false;
            harVærtRedd = false;
            speed = 2;
        } else if(modus == SpøkelsesModus.CHASE) {
            lagChasePoint();
        }
        bevegSøkelse();
    }

    public ImageView getInky() {
        return inkyBildeView;
    }
    public void hentBlinkyPos(Vector2D blinkyPos) {
        this.blinkyPos = blinkyPos;
    }

    @Override
    public void lagChasePoint() {

        // Finner vectoret mellom Blinky og Pac-Man
        Vector2D vectorToPacMan = new Vector2D(pacmanPos.getX() - blinkyPos.getX(), pacmanPos.getY() - blinkyPos.getY());

        // Inky's target er blinkyPos + 2 * vektoren til Pacman
        Vector2D inkyTarget = new Vector2D(blinkyPos.getX() + 2 * vectorToPacMan.getX(), blinkyPos.getY() + 2 * vectorToPacMan.getY());

        chasePos = inkyTarget; // Set Inky's chase position
    }

    @Override
    public void lagScatterPoint() {
        // Setter Inky's scatter point.
        scatterPos = new Vector2D(grid.length - 1, 0);
    }

    @Override
    public void byggStack() {

        modusStack = new ModusSamling();
        aktivModusTid = new ModusTid(SpøkelsesModus.ATHOME, 8);

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


}

