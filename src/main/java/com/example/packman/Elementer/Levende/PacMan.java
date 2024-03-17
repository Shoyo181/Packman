package com.example.packman.Elementer.Levende;

import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;
import javafx.scene.control.skin.TextInputControlSkin;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;


public class PacMan extends Levende{

    //private Circle pac;




    public PacMan(Rute[][] grid) {
        super(grid);

        lev.setFill(Paint.valueOf("yellow"));
    }

    public Circle getPacMan(){
        return lev;
    }

    public void plasserPacMan(){

        // Finner hvor dør er og hvor hjem er på denne banen og spawner pacman på andre siden av døren, altså i denne
        // rekkefølgen; DØR, HJEM, VEGG, en til, SPAWN, på en til må vi sjekke om det er plass til pacman.

        //lager to tomme tabeller
        ArrayList<String> dørPlassering = new ArrayList<>();
        ArrayList<String> hjemPlassering = new ArrayList<>();

        for (int y = 0; y < gridHøyde; y++) {
            System.out.println("y = " + y);
            for (int x = 0; x < gridBredde; x++) {
                System.out.println("x = " + x + ", " + grid[x][y].getType());
                Rute.RuteType currType = grid[x][y].getType();
                if (currType == Rute.RuteType.DØR) {
                    String nyDør = x + ";" + y;
                    dørPlassering.add(nyDør);
                }
                if (currType == Rute.RuteType.HJEM) {
                    String nyHjem = x + ";" + y;
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
        int randomTall = 0; // slik at hvis random så blir det riktig får både dør og hjem
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
        // nå som vi har posisjonen til midten av dør, sjekker vi om hjem er plassert under, over eller ved siden av på banen.
        // denne koden tar foruttar at det er en ledig plass under hjem for at pacman spawner, hvis ikke plass - ingen spawning
        // sjekker bare en linje - foreslått kode - sjekk det!
        for(int i = 0; i < hjemPlassering.size(); i++){
            String linje = hjemPlassering.get(i);
            String[] deler = linje.split(";");
            int tempMidtHjemX = Integer.parseInt(deler[0]);
            int tempMidtHjemY = Integer.parseInt(deler[1]);


            // finne ut hvilken retning pacman skal starte på banen.
            System.out.println("");
            System.out.println("Hjem er: x " + tempMidtHjemX + ", y " + tempMidtHjemY);
            System.out.println("Dør er: x " + midtDørX + ", y " + midtDørY);
            System.out.print("Pacman spawner - ");
            if(midtDørX == tempMidtHjemX){
                // over eller under hjem
                if(midtDørY > tempMidtHjemY){
                    System.out.println("over");
                    // pac spawner over
                    // sjekker om det er plass under til å spawne
                    for(int j = tempMidtHjemY - 3; j >= 0; j--){
                        // + 2 siden vi vil gjerne hoppe over en flate som er gulv
                        System.out.println("For løkke for å sjekke om det er plass over: j - " + j + ", grid[midtDørX].length - " + grid[midtDørX].length );
                        // sjekker alle rader under tempMidtHjemY
                        if(grid[midtDørX][j].getType() == Rute.RuteType.GULV) {
                            // sjekker om det er plass til å spawne
                            startPosX = midtDørX * ruteStr;
                            startPosY = j * ruteStr;
                            currentRute = grid[midtDørX][j];

                            grid[midtDørX][j].setLedigForElement(false);
                            break;
                        }
                    }
                    break;
                }else{
                    System.out.println("under");
                    // under
                    // sjekker om det er plass over til å spawne
                    for(int j = tempMidtHjemY + 3; j < grid[midtDørX].length ; j++){
                        // - 2 siden vi vil gjerne hoppe over en flate som er gulv
                        System.out.println("For løkke for å sjekke om det er plass under: j - " + j + ", midtDørX - " + midtDørX );
                        // sjekker alle rader under tempMidtHjemY
                        if(grid[midtDørX][j].getType() == Rute.RuteType.GULV) {
                            // sjekker om det er plass til å spawne

                            System.out.println("j(y)        = " + j);
                            System.out.println("midtDørX(x) = " + midtDørX);
                            System.out.println("rute størrelse = " + ruteStr);

                            startPosX = midtDørX * ruteStr;
                            startPosY = j * ruteStr;
                            currentRute = grid[midtDørX][j];

                            System.out.println("currentPosX = " + currentPosX);
                            System.out.println("currentPosY = " + currentPosY);
                            grid[midtDørX][j].setLedigForElement(false);
                            break;
                        }
                    }
                    break;
                }

            }else if (midtDørY == tempMidtHjemY){
                // venstre eller høyre
                if(midtDørX > tempMidtHjemX){
                    System.out.println("venstre");
                    // venstre
                    // sjekker om det er plass venstre til å spawne
                    for(int j = tempMidtHjemX - 3; j >= 0; j--){
                        // + 2 siden vi vil gjerne hoppe over en flate som er gulv
                        System.out.println("For løkke for å sjekke om det er plass venstre: j - " + j + ", grid[midtDørX].length - " + grid[midtDørX].length );
                        // sjekker alle rader under tempMidtHjemY
                        if(grid[j][midtDørY].getType() == Rute.RuteType.GULV) {
                            // sjekker om det er plass til å spawne
                            startPosX = j * ruteStr;
                            startPosY = midtDørY * ruteStr;
                            currentRute = grid[j][midtDørY];
                            grid[j][midtDørY].setLedigForElement(false);
                            break;
                        }
                    }
                    break;
                }else{
                    System.out.println("høyre");
                    // høyre
                    // sjekker om det er plass høyre til på spawne
                    for(int j = tempMidtHjemX + 3; j < grid.length; j++){
                        // - 2 siden vi vil gjerne hoppe over en flate som er gulv
                        System.out.println("For løkke for på sjekke om det er plass høyre: j - " + j + ", grid[midtDørX].length - " + grid[midtDørX].length );
                        // sjekker alle rader under tempMidtHjemY
                        if(grid[j][midtDørY].getType() == Rute.RuteType.GULV) {
                            // sjekker om det er plass til på spawne
                            startPosX = j * ruteStr;
                            startPosY = midtDørY * ruteStr;
                            currentRute = grid[j][midtDørY];
                            grid[j][midtDørY].setLedigForElement(false);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        // hopper ut av disse løkkene over så fort vi får ett treff
        // utregning hvis det currentPosX og currentPosY ikke har verdi, altså fant ingen ledig plass
        // skal vi finne randum plass da????
        // TODO: finne random plass for pacman hvis det ikke er plass til å spawne


        // legger til radius slik at plassering blir riktig
        startPosX += radius;
        startPosY += radius;

        // setter plassering til PACMAN
        lev.setCenterX(startPosX);
        lev.setCenterY(startPosY);
        currentPosX = startPosX;
        currentPosY = startPosY;
        byggHitBox();
    }

    public void flyttPacManIgjen(){

        switch (retning) {
            case OPP:
                if(sjekkKollisjon(currentPosX, currentPosY - speed)){
                    break;
                }
                currentPosY -= speed;
                setHitBox(currentPosX, currentPosY);
                break;
            case NED:
                if(sjekkKollisjon(currentPosX, currentPosY + speed)){
                    break;
                }
                currentPosY += speed;
                setHitBox(currentPosX, currentPosY);
                break;
            case HØYRE:
                if (sjekkKollisjon(currentPosX + speed, currentPosY)) {
                    break;
                }
                currentPosX += speed;
                setHitBox(currentPosX, currentPosY);
                break;
            case VENSTRE:
                if (sjekkKollisjon(currentPosX - speed, currentPosY)) {
                    break;
                }
                currentPosX -= speed;
                setHitBox(currentPosX, currentPosY);
                break;
        }
    }

    public Vector2D getPosition() {

        switch (retning) {
            case OPP:
                return new Vector2D( (int) currentPosX / ruteStr, (int) ( currentPosY + radius ) / ruteStr);
            case NED:
                return new Vector2D( (int) currentPosX / ruteStr, (int) ( currentPosY - radius ) / ruteStr);
            case HØYRE:
                return new Vector2D( (int) ( currentPosX + radius ) / ruteStr, (int) currentPosY / ruteStr);
            case VENSTRE:
                return new Vector2D( (int) ( currentPosX - radius ) / ruteStr, (int) currentPosY / ruteStr);
            default:
                return new Vector2D(0, 0);
        }
    }
}