/**
 * Klasse for Clyde.
 */
package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.ModusSamling;
import com.example.packman.misc.ModusTid;
import com.example.packman.misc.SpøkelsesModus;
import com.example.packman.misc.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Vector;

public class Clyde extends Spøkelser {
    private ImageView clydeBildeView;
    boolean chasePacman, chaseRandomPos;

    public Clyde(Rute[][] grid) {
        super(grid);
        lagScatterPoint();
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
        byggStack();
    }


    public void flyttClyde() {
        //metoden er til for å bevege dete spøkelse
        sjekkModus();

        // dobbeltsjekk
        if (modus == null) {
            return;
        }else if(modus == SpøkelsesModus.CHASE){
            lagChasePoint();
        }
        bevegSøkelse();
    }




    public ImageView getClyde() {
        return clydeBildeView;
    }

    // abstrakte metoder
    @Override
    public void lagChasePoint(){
        // for Clyde skal han egentlig jakte helt til han er innenfor 8 ruter til pacman
        // etter dette skal han gå tilbake til scatterpoint
        int avstand = 8;

        if(!chasePacman && chasePos == null){
            chasePos = new Vector2D(pacmanPos.getX() - 1, pacmanPos.getY() - 1);
        }

        // hvis Clyde er innenfor 8 ruter til pacman
        // vi må regne ut fra begge retninger og sjekke om vi er innenfor 8 ruter til pacman
        if (Math.abs(currentPosX - pacmanPos.getX()) <= avstand && Math.abs(currentPosY - pacmanPos.getY()) <= avstand && chasePacman) {
            // hvis vi er innenfor 8 ruter til pacman, skal vi sette pacman til scatterpoint
            // men istedenfor scatter point så setter vi en posisjon nærme scatterpoint

            // vi finner en posisjon som er næreme scatterpoint ved hjelp av løkker
            ArrayList<Vector2D> nyRandomPointsListe = new ArrayList<>();
            int offset;
            if (gridBredde > gridHøyde){
                offset = gridBredde / 3;
            } else {
                offset = gridHøyde / 3;
            }
            for(int x = 0; x < offset; x++){
                for(int y = gridHøyde; y > offset*2; y--){
                    // nå søker vi etter posisjoner i venstre hjørne av banen
                    if(grid[x][y].getType() == Rute.RuteType.GULV){
                        nyRandomPointsListe.add(new Vector2D(x, y));
                    }
                }
            }

            // vi velger en random posisjon i lista - bruker fortsatt chasepos
            int index = (int) (Math.random()*nyRandomPointsListe.size());
            chasePos = nyRandomPointsListe.get(index);

            // vi må huske at dette ikke faktisk er ett chase point så bruker en boolean for dette.
            // da genererer vi ikke ett nytt chasepoint hele tiden
            chasePacman = false;
        }else if(!chasePacman && currentPosY == chasePos.getY() && currentPosX == chasePos.getX()){
            // så hvis clyde har et random chasepoint og han treffer det må vi stille tilbake til packman pos
            chasePos = pacmanPos;
            chasePacman = true;
        }


    }
    public void lagScatterPoint(){
        // Clyde skal ha dette pointet nederst til venstre
        scatterPos = new Vector2D (grid.length - 1, 0);
    }
    @Override
    public void byggStack() {
        // metoden bygger opp rekkefølgen til modusene for spøkelsene
        modusStack = new ModusSamling();
        aktivModusTid = new ModusTid(SpøkelsesModus.ATHOME, 10);

        // bygges som er stack, så sisteman in skal først ut
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