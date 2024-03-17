package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Rute.Rute;

import java.util.ArrayList;

public class Spøkelser extends Levende {

    protected SpøkelsesModus modus;


    public Spøkelser(Rute[][] grid) {
        super(grid);


    }


    public void plasserSpøkelse () {
        // Finner hjemmet i banen
        ArrayList <Vector2D> hjemPos =  new ArrayList<>();
        for (int x = 0; x < gridBredde; x++) {
            for (int y = 0; y < gridHøyde; y++) {
                if (grid[x][y].getType() == Rute.RuteType.HJEM) {
                    Vector2D nyHjemPos = new Vector2D(x, y);
                    hjemPos.add(nyHjemPos);
                }
            }
        }
        // Plasserer spøkelse i hjemposisjon
        Vector2D randomPos = hjemPos.get((int) (Math.random() * hjemPos.size()));
        lev.setCenterX(randomPos.getX()*ruteStr+radius);
        lev.setCenterY(randomPos.getY()*ruteStr+radius);
        bildeSpøkelse.setLayoutX(randomPos.getX()*ruteStr+radius);
        bildeSpøkelse.setLayoutY(randomPos.getY()*ruteStr+radius);


    }
}




