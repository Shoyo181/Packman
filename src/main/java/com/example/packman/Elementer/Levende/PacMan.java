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
        int randomTall; // slik at hvis random så blir det riktig får både dør og hjem
        if(dørPlassering.size() == 1) {
            String linje = dørPlassering.get(0);
            String[] deler = linje.split(";");
            midtDørX = Integer.parseInt(deler[0]);
            midtDørY = Integer.parseInt(deler[1]);
        }else{
            // hvis det er flere dører, velger vi den som er i midten, hvis partall en random
            if(dørPlassering.size() % 2 == 0){
                // partall
                // lager random tall 0 eller 1
                randomTall = (int) (Math.random() * 2);
                System.out.println("randomTall: " + randomTall);
                // velger midten av listen, plusser på random tall
                String linje = dørPlassering.get(dørPlassering.size()/2 + randomTall);
                String[] deler = linje.split(";");
                midtDørX = Integer.parseInt(deler[0]);
                midtDørY = Integer.parseInt(deler[1]);

            }else{
                // oddetall
                // henter stringen i midten av lista. .size teller fra 1 i index så trenger bare å dele på 2
                //                                    .get teller fra 0 i index, derfor trenger vi ikke -1 eller +1
                String linje = dørPlassering.get(dørPlassering.size()/2);
                String[] deler = linje.split(";");
                midtDørX = Integer.parseInt(deler[0]);
                midtDørY = Integer.parseInt(deler[1]);
            }
        }
        // nå som vi har posisjonen til midten av dør, sjekker vi om hjem er plassert under, over eller veder på banen.

        // sjekker bare en linje - foreslått kode - sjekk det!
        for(int i = 0; i < hjemPlassering.size(); i++){
            String linje = hjemPlassering.get(i);
            String[] deler = linje.split(";");
            int tempMidtHjemX = Integer.parseInt(deler[0]);
            int tempMidtHjemY = Integer.parseInt(deler[1]);

            if(midtDørX == tempMidtHjemX && midtDørY == tempMidtHjemY){
                randomTall = (int) (Math.random() * 3);
                if(randomTall == 1){
                    // veder
                    midtHjemX = tempMidtHjemX - 1;
                    midtHjemY = tempMidtHjemY;
        }



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