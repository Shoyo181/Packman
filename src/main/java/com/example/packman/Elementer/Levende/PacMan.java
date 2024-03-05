package com.example.packman.Elementer.Levende;

import com.example.packman.Rute.Rute;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.shape.Circle;

import java.util.ArrayList;


public class PacMan extends Levende{

    Circle pac;


    public PacMan(Rute[][] grid) {
        super(grid);
        pac = new Circle();
    }

    public void plasserPacMan(){

        // Finner hvor dør er og hvor hjem er på denne banen og spawner pacman på andre siden av døren, altså i denne
        // rekkefølgen; DØR, HJEM, VEGG, en til, SPAWN, på en til må vi sjekke om det er plass til pacman.

        //lager to tomme tabeller
        ArrayList<String> dørPlassering = new ArrayList<>();
        ArrayList<String> hjemPlassering = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].getType() == Rute.RuteType.DØR) {
                    String nyDør = i + ";" + j;
                    dørPlassering.add(nyDør);
                }
                if (grid[i][j].getType() == Rute.RuteType.HJEM) {
                    String nyHjem = i + ";" + j;
                    hjemPlassering.add(nyHjem);
                }
            }
        }
        // nå har vi funnet hvor mange dører, hjem det er
        // så nå kan vi finne ut hvor pacman skal starte på banen.
        // vi starter med å finne ut om dør pos er plasert over hjem eller under. sjekker også om den er på siden

        // må også sjekke om det er flere dører - men gidder ikke for nå

        int midtDørX, midtDørY;
        int midtHjemX, midtHjemY;

        if(dørPlassering.size() == 1) {
            String linje = dørPlassering.get(0);
            String[] deler = linje.split(";");
            midtDørX = Integer.parseInt(deler[0]);
            midtDørY = Integer.parseInt(deler[1]);
        }else{
            // hvis det er flere dører, velger vi den som er i midten, hvis partall en random

            int dørPlasseringTall = (int) (Math.random() * dørPlassering.size());
        }




        //lager to random tall
        int dørPlasseringTall = (int) (Math.random() * dørPlassering.size());
        int hjemPlasseringTall = (int) (Math.random() * hjemPlassering.size());



        pac.setCenterX(currentPosX);
        pac.setCenterY(currentPosY);
        pac.setRadius(10);
    }

    public void erGulvLedig(String valg){
        // en måte å gjøre det på
        if(valg == "opp"){

        }
    }
}