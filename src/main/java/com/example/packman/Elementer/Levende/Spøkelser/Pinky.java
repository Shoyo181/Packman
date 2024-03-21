/**
 * Klasse for Pinky.
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

public class Pinky extends Spøkelser {
    private ImageView pinkyBildeView;
    private Image bildePinky;

    public Pinky(Rute[][] grid) {
        super(grid);
        lagScatterPoint();
        try {
            bildePinky = new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Pinky05.png"));
            pinkyBildeView = new ImageView(bildePinky);
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
        byggStack();
    }

    public void flyttPinky() {
        sjekkModus();

        if (modus == null) {
            return;
        } else if (harVærtRedd) {
            bildeSpøkelse.setImage(bildePinky);
            harVærtRedd = false;
        } else if(modus == SpøkelsesModus.ATHOME && bleSpist) {
            bildeSpøkelse.setImage(bildePinky);
            bleSpist = false;
            harVærtRedd = false;
        } else if(modus == SpøkelsesModus.CHASE) {
            lagChasePoint();
        }
        bevegSøkelse();
    }

    public ImageView getPinky() {
        return pinkyBildeView;
    }

    @Override
    public void lagChasePoint() {

        Vector2D targetPos = pacmanPos; // Finner posisjonen til Pacman.

        // Basert på retningen til Pacman, lager vi posisjonen ut ifra hvordan Pinky skal bevege seg.
        switch (pacmanRetning) {
            case OPP: // Up
                targetPos.setY(targetPos.getY() - 4);
                break;
            case NED: // Down
                targetPos.setY(targetPos.getY() + 4);
                break;
            case HØYRE: // Right
                targetPos.setX(targetPos.getX() + 4);
                break;
            case VENSTRE: // Left
                targetPos.setX(targetPos.getX() - 4);
                break;
        }


        if (pacmanRetning == Retning.OPP) {
            targetPos.setX(targetPos.getX() - 4);
        }

        chasePos = targetPos; // Setter Pinky's Chase point til targetPos.
    }

    @Override
    public void lagScatterPoint() {
       // Setter Pinky's Scatter point til (0, 0)
        scatterPos = new Vector2D(0, 0);
    }

    @Override
    public void byggStack() {

        modusStack = new ModusSamling();
        aktivModusTid = new ModusTid(SpøkelsesModus.ATHOME, 5);

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
