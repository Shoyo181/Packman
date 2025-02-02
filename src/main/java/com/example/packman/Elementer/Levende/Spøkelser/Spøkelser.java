/**
 * Klasse for Spøkelser.
 * Inneholder alle metoder som spøkelse har.
 * Også abstrakt.
 */
package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Rute.Rute;
import com.example.packman.misc.ModusSamling;
import com.example.packman.misc.ModusTid;
import com.example.packman.misc.SpøkelsesModus;
import com.example.packman.misc.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public abstract class Spøkelser extends Levende {

    protected SpøkelsesModus modus;

    protected Vector2D chasePos, scatterPos, foranDørPos, pacmanPos;
    protected boolean harMål, sjekkOmFrightFirstTime, bleSpist, harVærtRedd, bleSpistFirstTime, harNåddHjem, sjekkOmFrightFirstIRetning;
    protected double sistRuteX, sistRuteY, sluttRuteX, sluttRuteY, backupRuteX, backupRuteY;
    protected Retning pacmanRetning, sistRetning, backupRetningFrighten;
    protected ArrayList<Rectangle> veggUtenDørList;
    protected ModusSamling modusStack;
    protected ModusTid aktivModusTid;
    protected Date modusKlokke;
    protected ImageView bildeSpøkelse, bildeScaredView, bildeEatandView;
    protected Image bildeScared, bildeEatand;
    protected boolean bevegerSeg;

    public Spøkelser(Rute[][] grid) {
        super(grid);
        bevegerSeg = false;
        byggKollisjonTab();
        bildeSetUp();
        speed = 2;
    }

    public void bildeSetUp(){
        try{
            bildeScared = new Image(getClass().getResourceAsStream("/com/example/packman/bilder/Scared05.png"));
            bildeScaredView = new ImageView(bildeScared);
            bildeScaredView.setFitWidth(ruteStr);
            bildeScaredView.setFitHeight(ruteStr);
            bildeScaredView.setPreserveRatio(true);

            bildeEatand =new Image(getClass().getResourceAsStream("/com/example/packman/bilder/Eyes05.png"));
            bildeEatandView = new ImageView(bildeEatand);
            bildeEatandView.setFitWidth(ruteStr);
            bildeEatandView.setFitHeight(ruteStr);
            bildeEatandView.setPreserveRatio(true);

        } catch (Exception e){
            System.out.println("Fant ikke bilde");
        }
    }

    public void setModus(SpøkelsesModus modus){
        this.modus = modus;
    }

    public SpøkelsesModus getModus(){
        return modus;
    }
    public void setPacmanRetning(Retning pacmanRetning){
        this.pacmanRetning = pacmanRetning;
    }
    public void plasserSpøkelse () {
        // Finner hjemmet i banen - lagrer grid pos
        ArrayList <Vector2D> hjemPos =  new ArrayList<>();
        for (int x = 0; x < gridBredde; x++) {
            for (int y = 0; y < gridHøyde; y++) {
                if (grid[x][y].getType() == Rute.RuteType.HJEM) {
                    Vector2D nyHjemPos = new Vector2D(x, y);
                    hjemPos.add(nyHjemPos);
                }
            }
        }
        // Plasserer spøkelse i hjemposisjon - random, legger til rutestr
        Vector2D randomPos = hjemPos.get((int) (Math.random() * hjemPos.size()));

        // pos regnes fra midten av rute
        startPosX = randomPos.getX()*ruteStr + radius;
        startPosY = randomPos.getY()*ruteStr + radius;

        lev.setCenterX(startPosX);
        lev.setCenterY(startPosY);

        // siden dette er et bilde, starter x og y på toppen
        bildeSpøkelse.setLayoutX(startPosX - radius);
        bildeSpøkelse.setLayoutY(startPosY - radius);

        currentPosX = startPosX;
        currentPosY = startPosY;

        lev.setFill(Color.TRANSPARENT);
        lev.setStroke(Color.TRANSPARENT);
    }

    public Retning bestemSpøkelseRetning () {

        // utifra dette skal metoden regne ut hvilken retning som er raskest for spøkelsene, men metoden har noen regler
        //  - spøkelse kan ikke gå den veien de kom fra
        //  - spøkelse kan ikke gå inn i en vegg

        //vi lagrer siste kordinater spøkelse hadde som mål, altså før de kom til denne metoden igjen
        // dette gjør vi for å få noen kordinater hvis spøkelse kommer i FRIGHTENED
        sistRuteY = sluttRuteY;
        sistRuteX = sluttRuteX;

        ArrayList<Retning> retninger = new ArrayList<>(); // liste over alle retninger
        // vi legger inn alle retningene i listen
        retninger.add(Retning.OPP);
        retninger.add(Retning.HØYRE);
        retninger.add(Retning.NED);
        retninger.add(Retning.VENSTRE);
        Retning komFraRetning = Retning.INGEN;

        // vi fjerner den retningen spøkelse kom fra
        if (retning == Retning.HØYRE) {
            retninger.remove(Retning.VENSTRE);
            komFraRetning = Retning.VENSTRE;
        } else if (retning == Retning.VENSTRE) {
            retninger.remove(Retning.HØYRE);
            komFraRetning = Retning.HØYRE;
        } else if (retning == Retning.NED) {
            retninger.remove(Retning.OPP);
            komFraRetning = Retning.OPP;
        } else if (retning == Retning.OPP) {
            retninger.remove(Retning.NED);
            komFraRetning = Retning.NED;
        }



        // nå har vi fullført den ene reglen
        // Vi fjerner alle retningen de kolliderer med en vegg
        if(modus == SpøkelsesModus.PÅVEIUT){
            retninger.removeIf(r -> !sjekkRetningLedig(r, veggUtenDørList));
        }else{
            retninger.removeIf(r -> !sjekkRetningLedig(r, veggList));
        }

        sistRetning = komFraRetning;
        backupRetningFrighten = retning;



        // da sitter vi igjen med antall retninger spøkelse kan ta
        // hvis det bare er en retning igjen velger vi den
        if (retninger.size() == 1) {
            Vector2D nestePos = regnUtNestePos(retninger.get(0));
            // regner ut neste rute for spøkelse
            sluttRuteX = (nestePos.getX() * ruteStr) - radius + ruteStr;
            sluttRuteY = (nestePos.getY() * ruteStr) - radius + ruteStr;
            return retninger.get(0);
        }

        // hvis det ikke er noen retninger igjen må spøkelse gå tilbake der den kom fra - bryter den ene regelen
        if (retninger.size() == 0) {
            Vector2D nestePos = regnUtNestePos(komFraRetning);
            // regner ut neste rute for spøkelse
            sluttRuteX = (nestePos.getX() * ruteStr) - radius + ruteStr;
            sluttRuteY = (nestePos.getY() * ruteStr) - radius + ruteStr;
            return komFraRetning;
        }

        // for FRIGHTENED velges alltid en random retning, vi lagrer også på siste retning for når de går inn i denne modusen

        if(modus == SpøkelsesModus.FRIGHTENED){
            int random = (int) (Math.random() * retninger.size());
            Vector2D nestePos = regnUtNestePos(retninger.get(random));
            sluttRuteX = (nestePos.getX() * ruteStr) - radius + ruteStr;
            sluttRuteY = (nestePos.getY() * ruteStr) - radius + ruteStr;
            return retninger.get(random);
        }


        if (modus == SpøkelsesModus.ATHOME && retning == Retning.INGEN) {
            //velger vi bare en random vei, hvis spøkelse er hjemme (bare hvis den spawner i midten)
            int random = (int) (Math.random() * retninger.size());
            Vector2D nestePos = regnUtNestePos(retninger.get(random));
            // regner ut neste rute for spøkelse
            sluttRuteX = (nestePos.getX() * ruteStr) - radius + ruteStr;
            sluttRuteY = (nestePos.getY() * ruteStr) - radius + ruteStr;
            return retninger.get(random);
        }


        // da har vi alle tilfeller ute med reglene som er satt for nå
        // neste steg er å finne ut hvilken vei som er minst til målet med de gjenstående retningene i listen

        // hvis det er mer enn en retning igjen velger vi en tilfeldig retning
        //return retninger.get((int) (Math.random() * retninger.size()));

        Vector2D endeMål = bestemMål();

        System.out.println("Modus: " + modus + " - Endemål: " + endeMål);

        // endemål er målet til spøkelsene, vi beregner hvilken retning som er kortest for spøkelse
        // med de resterende retningene i listen
        // finner pos fra retning først
        ArrayList<Vector2D> retningPos = new ArrayList<>();
        for(Retning r : retninger){
            if(r == Retning.OPP){
                retningPos.add( new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY() - 1) );
            } else if(r == Retning.HØYRE){
                retningPos.add( new Vector2D(sjekkPosisjonIGridIndexX() + 1, sjekkPosisjonIGridIndexY()) );
            } else if(r == Retning.NED){
                retningPos.add( new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY() + 1) );
            } else if(r == Retning.VENSTRE){
                retningPos.add( new Vector2D(sjekkPosisjonIGridIndexX() - 1, sjekkPosisjonIGridIndexY()) );
            }
        }
        // nå har vi posisjonen til ruta spøkelse kommer ut fra, da kan vi regne ut hvilken vei som er kortest
        // kan hende vi må opphøyde begge i formelen under
        int[] retningPosArray = new int[retninger.size()];
        for(int i = 0; i < retningPosArray.length; i++) {
            retningPosArray[i] = (int) (Math.pow(Math.abs(endeMål.getX() - retningPos.get(i).getX()), 2) +
                                        Math.pow(Math.abs(endeMål.getY() - retningPos.get(i).getY()), 2) );
        }
        //System.out.println("sjekker hvilken vei som er kortest");
        // vi må huske hvilken retning som er kortest
        int min = retningPosArray[0];
        int index = 0;
        for(int i = 0; i < retningPosArray.length; i++) {
            //System.out.println("index: " + i + ", retningPosArray[index] (verdi): " + retningPosArray[i] + ", retning: " + retninger.get(i));
            if(retningPosArray[i] < min){
                min = retningPosArray[i];
                index = i;
            }
        }
        //velger retning som er kortest
        // hvis to retninger er like kortest velger prioriteres rekkefølgen til retningene, for eksempel - opp er først
        // setter også nytt travel point i pixel kordinat
        // (husk dette er grid index så må plusse på ruteStr)
        sluttRuteX = (retningPos.get(index).getX() * ruteStr) - radius + ruteStr;
        sluttRuteY = (retningPos.get(index).getY() * ruteStr) - radius + ruteStr;
        //System.out.println("Spøkelse befinner seg på - x: " + currentPosX + ", y: " + currentPosY);
        //System.out.println("Spøkelse beveger seg til - x: " + sluttRuteX + ", y: " + sluttRuteY);

        //System.out.println("Rute spøkelse er på: - x: " + sjekkPosisjonIGridIndexX() + ", y: " + sjekkPosisjonIGridIndexY());
        //System.out.println("Rute beveger seg til - x: " + retningPos.get(index).getX() + ", y: " + retningPos.get(index).getY());
        //System.out.println("Rute sitt ende mål   - x: " + endeMål.getX() + ", y: " + endeMål.getY());

        //System.out.println("Retning valgt: " + retninger.get(index));

        return retninger.get(index);
    }
    public Vector2D regnUtNestePos(Retning r){
        switch (r){
            case OPP:
                return new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY() - 1);
            case HØYRE:
                return new Vector2D(sjekkPosisjonIGridIndexX() + 1, sjekkPosisjonIGridIndexY());
            case NED:
                return new Vector2D(sjekkPosisjonIGridIndexX(), sjekkPosisjonIGridIndexY() + 1);
            case VENSTRE:
                return new Vector2D(sjekkPosisjonIGridIndexX() - 1, sjekkPosisjonIGridIndexY());
        }

        return null;
    }

    public void bevegSøkelse(){
        // hver gang ett spøkelse skal bevege på seg så går den en rute på banen
        // det betyr at etter hver rute så må spøkelse finne en ny rute å gå til

        //hvis spøkelse ikke har ett mål bestemmer vi et mål når vi bestemmer retning
        if(!harMål){
            retning = bestemSpøkelseRetning();
        }
        //System.out.println("Spøkelse sine kordinater -  x: " + currentPosX + ", y: " + currentPosY);
        //System.out.println("Spøkelse sitt mål        -  x: " + sluttRuteX + ", y: " + sluttRuteY);


        //etter spøkelse er flyttet seg en hel rute, bergner vi ett nytt mål
        if(currentPosX == sluttRuteX && currentPosY == sluttRuteY){
            harMål = false;
            //System.out.println("spøkelse har et mål - test");
        }else if(modus == SpøkelsesModus.PÅVEIUT || modus == SpøkelsesModus.EATEN){

            flyttSøkelse(veggUtenDørList);
        }else{
            flyttSøkelse(veggList);
        }
    }

    public Vector2D bestemMål(){
        // vi sender ut hva målet er for spøkelse
        // hvis det er chase bruker vi pacman sine kordinater
        if(modus == SpøkelsesModus.CHASE) {
            harMål = true;
            return chasePos;
        } else if(modus == SpøkelsesModus.SCATTER){
            harMål = true;
            return scatterPos;
        } else if(modus == SpøkelsesModus.ATHOME){
            harMål = true;
            return finnMålAtHome();
        } else if (modus == SpøkelsesModus.PÅVEIUT){
            harMål = true;
            return foranDørPos;
        } else if (modus == SpøkelsesModus.FRIGHTENED){
            harMål = true;
            return pacmanPos; // vil aldri bli valgt, har skal spøkelse bare velge random
        } else if (modus == SpøkelsesModus.EATEN){
            harMål = true;
            return eatenPos();
        }
        return null;
    }

    public Vector2D finnMålAtHome(){
        // vi finner et random punkt i hjemmet
        ArrayList <Vector2D> hjemPos =  new ArrayList<>();
        for(int x = 0; x < gridBredde; x++) {
            for(int y = 0; y < gridHøyde; y++) {
                if(grid[x][y].getType() == Rute.RuteType.HJEM) {
                    Vector2D nyHjemPos = new Vector2D(x, y);
                    hjemPos.add(nyHjemPos);
                }
            }
        }
        // velger en radom posisjon av tilgjengelige mål
        Vector2D randomPos = hjemPos.get((int)(Math.random()*hjemPos.size()));
        return randomPos;
    }

    public Vector2D eatenPos(){
        // må modusen blir til EATEN som betyr at denne metoden blir kalt hver gang spøkelse prøver å finne kordinater å gå
        // kordinatene vil være hjemmet deres

        //burde være en sjekk her som sjekker om spøkelse kom hjem også bytter vi over modus igjen til PÅVEIUT
        // men for nå gir vi bare ut et kordinat i hjemmet
        return finnMålAtHome();

    }
    public void gotEaten(){
        //setter modusen til EATEN

        if(!bleSpist) {
            modus = SpøkelsesModus.EATEN;
            // den modusen spøkelsene har vært i før denne modusen er FRIGHTENED, så det betyr at vi ikke trenger å lagre på dette
            ModusTid modusTid = new ModusTid(SpøkelsesModus.EATEN, 20);
            // hvis spøkelse blir spist må vi ordne litt på stacken
            modusStack.push(new ModusTid(SpøkelsesModus.PÅVEIUT, 3));
            //modusStack.push(new ModusTid(SpøkelsesModus.ATHOME, 1));
            modusStack.push(modusTid);
            speed = 4;

            //spøkelse har blitt spist
            bleSpist = true;
            bleSpistFirstTime = true;

            //oppdaterer bildet til spøkelse
            bildeSpøkelse.setImage(bildeEatand);
        }
    }

    public void setPackmanPos(Vector2D pacmanPos) {
        this.pacmanPos = pacmanPos;
    }

    public void setNåddHjem(){
        harNåddHjem = true;
    }

    public void flyttSøkelse(ArrayList <Rectangle> liste) {

        switch (retning) {
            case OPP:
                if(sjekkKollisjon(currentPosX, currentPosY - speed, liste)){
                    if(retning == sistRetning){
                        System.out.println("Bug - sitterfast i veggen");
                        retning = backupRetningFrighten;
                        sluttRuteY = backupRuteY;
                        sluttRuteX = backupRuteX;
                    }
                    break;
                }
                currentPosY -= speed;
                tunellHåndtering();
                setSpøkelsePos();
                break;
            case NED:
                if(sjekkKollisjon(currentPosX, currentPosY + speed, liste)){
                    if(retning == sistRetning){
                        System.out.println("Bug - sitterfast i veggen");
                        retning = backupRetningFrighten;
                        sluttRuteY = backupRuteY;
                        sluttRuteX = backupRuteX;
                    }
                    break;
                }
                currentPosY += speed;
                tunellHåndtering();
                setSpøkelsePos();
                break;
            case HØYRE:
                if (sjekkKollisjon(currentPosX + speed, currentPosY, liste)) {
                    if(retning == sistRetning){
                        System.out.println("Bug - sitterfast i veggen");
                        retning = backupRetningFrighten;
                        sluttRuteY = backupRuteY;
                        sluttRuteX = backupRuteX;
                    }
                    break;
                }
                currentPosX += speed;
                tunellHåndtering();
                setSpøkelsePos();
                break;
            case VENSTRE:
                if (sjekkKollisjon(currentPosX - speed, currentPosY, liste)) {
                    if(retning == sistRetning){
                        System.out.println("Bug - sitterfast i veggen");
                        retning = backupRetningFrighten;
                        sluttRuteY = backupRuteY;
                        sluttRuteX = backupRuteX;
                    }
                    break;
                }
                currentPosX -= speed;
                tunellHåndtering();
                setSpøkelsePos();
                break;
            case INGEN:
                break;
        }
    }
    public void setSpøkelsePos(){
        // tar med hitbox pos også
        setHitBox(currentPosX, currentPosY);
        bildeSpøkelse.setLayoutX(currentPosX - radius);
        bildeSpøkelse.setLayoutY(currentPosY - radius);
    }


    public void byggKollisjonTab(){
        // metode som setter opp en tabell for kollisjoner uten dører
        // legger også til hvor døren går ut fra hjemmet
        veggUtenDørList = new ArrayList<>();
        ArrayList<Vector2D> hjemPosisjoner = new ArrayList<>();
        ArrayList<Vector2D> dørPosisjoner = new ArrayList<>();
        for(int x = 0; x < gridBredde; x++){
            for(int y = 0; y < gridHøyde; y++){
                if( grid[x][y].getType() == Rute.RuteType.VEGG ) {
                    veggUtenDørList.add(grid[x][y].getTile());
                }
                if (grid[x][y].getType() == Rute.RuteType.HJEM) {
                    hjemPosisjoner.add(new Vector2D(x, y));
                }
                if (grid[x][y].getType() == Rute.RuteType.DØR) {
                    dørPosisjoner.add(new Vector2D(x, y));
                }
            }
        }
        // vi har nå laget en liste over ting å kollidere i
        // men også lagre pos for å regne ut hvor posisjon utenfor døren er
        // må først finne ut om døren er over/under eller ved siden av hjemmet
        int x = 0;
        int y = 0;
        //System.out.println("finner posisjon utenfor dør");
        //System.out.println("hjemPosisjoner: " + hjemPosisjoner);

        // ikke optimalt, men dette finner den første døren som er "over" hjemmet
        for(int i = 0; i < dørPosisjoner.size(); i++){
            //System.out.println("sjekk i: " + i);
            for(int j = 0; j < hjemPosisjoner.size(); j++){
                //System.out.println("sjekk j: " + j);
                if (dørPosisjoner.get(i).getX() == hjemPosisjoner.get(j).getX()){
                    //hvis dette er sant sier vi at døren er over eller under hjemmet
                    // da vi finne ut hvem det er
                    //System.out.println("dør funnet over eller under");
                    if(dørPosisjoner.get(i).getY() < hjemPosisjoner.get(j).getY()){
                        //dør er over
                        x = dørPosisjoner.get(i).getX();
                        y = dørPosisjoner.get(i).getY() - 1;
                        //System.out.println("x: " + x + ", y: " + y);
                        //System.out.println("dør er over");
                        foranDørPos = new Vector2D(x, y);
                        break;
                    } else if(dørPosisjoner.get(i).getY() > hjemPosisjoner.get(j).getY()){
                        //dør er under
                        x = dørPosisjoner.get(i).getX();
                        y = dørPosisjoner.get(i).getY() + 1;
                        //System.out.println("x: " + x + ", y: " + y);
                        //System.out.println("dør er under");
                        foranDørPos = new Vector2D(x, y);
                        break;
                    }
                }else if(dørPosisjoner.get(i).getY() == hjemPosisjoner.get(j).getY()){
                    //dør er på samme linje som hjemmet
                    //System.out.println("dør er på samme linje som hjemmet");
                    if(dørPosisjoner.get(i).getX() < hjemPosisjoner.get(j).getX()){
                        //dør er på venstre
                        x = dørPosisjoner.get(i).getX() - 1;
                        y = dørPosisjoner.get(i).getY();
                        //System.out.println("x: " + x + ", y: " + y);
                        //System.out.println("dør er på venstre");
                        foranDørPos = new Vector2D(x, y);
                        break;
                    } else if(dørPosisjoner.get(i).getX() > hjemPosisjoner.get(j).getX()){
                        //dør er på høyre
                        x = dørPosisjoner.get(i).getX() + 1;
                        y = dørPosisjoner.get(i).getY();
                        //System.out.println("x: " + x + ", y: " + y);
                        //System.out.println("dør er på høyre");
                        foranDørPos = new Vector2D(x, y);
                        break;
                    }
                }
            }
        }
        // etter disse løkkene burde det finnes en posisjon hvor spøkelser kan komme seg ut av hjemmet til/på


    }



    // abstrakte metoder

    public void lagChasePoint(){
        // denne er forskjellig fra hver spøkelse
    }
    public void lagScatterPoint(){
        // denne er forskjellig fra hver spøkelse
    }

    /***            METODER FOR MODUSSLOGIKK            ***/
    /* Vi bruker en egen klasse for å holde styr på hvilken modus som er aktiv
     * vi må passe på at når spøkelse er FRIGHTEN må ikke dette ødelegge rekkefølgen
     */
    public void byggStack() {
        // metoden er abstrakt
    }

    public void sjekkModus(){
        // metoden håndterer hvilken modus spøkelse er i og bytter mellom de

        //må først sjekke om spøkelse er skremt eller ikke
        if(modus == SpøkelsesModus.FRIGHTENED && sjekkOmFrightFirstTime){
            //setter hvor mye tid det er igjen før vi byttet modus til FRIGHTENED
            // (selvom stacken er tom - burde aktivt modus være CHASE med 100000 sek i seg)
            aktivModusTid.setSekunder(aktivModusTid.getSekunder() - (int)(new Date().getTime() - modusKlokke.getTime()) / 1000);

            //Legger modus tilbake i stacken med resterende tid før spøkelse ble FRIGHTENED
            modusStack.push(aktivModusTid);

            //setter aktivt modus til FRIGHTENED med tid denne gangen
            aktivModusTid = new ModusTid(SpøkelsesModus.FRIGHTENED, 6);
            // setter også inn en boolean så dette ikke skjer hver gang metoden kjører
            sjekkOmFrightFirstTime = false;

            modusKlokke = new Date();
            System.out.println("Frighted modus startet");
        }
        if(modus == SpøkelsesModus.EATEN && bleSpistFirstTime){
            // starter klokka for denne modusen også
            modusKlokke = new Date();
            bleSpistFirstTime = false;
        }
        if(modus == SpøkelsesModus.EATEN && harNåddHjem){
            // starter klokka for denne modusen på nytt
            modusKlokke = new Date();
            aktivModusTid = modusStack.pop();
            harNåddHjem = false;
            return;
        }

        if(modusStack.erTom() && modus != SpøkelsesModus.FRIGHTENED){
            System.out.println("Stack er tom");
            modus = SpøkelsesModus.CHASE;
            return;
        }
        // tid i sekunder for å sjekke om lengde på moduser
        int sekSidenModusStart = (int)(new Date().getTime() - modusKlokke.getTime()) / 1000;

        //System.out.println("SekSidenModusStart:  - " + sekSidenModusStart);
        //System.out.println("AktivModusTid:       - " + aktivModusTid.getSekunder());
        //System.out.println("Modus:               - " + aktivModusTid.getModus());
        if(sekSidenModusStart >= aktivModusTid.getSekunder()){ // vi må bytte modus
            System.out.print("Modus blir byttet til - ");
            if(!harVærtRedd){
                //når vi har byttet modus, betyr det at spøkelsene ikke trenger å være redde lenger (for å bytte utseende)
                harVærtRedd = true;
            }
            aktivModusTid = modusStack.pop();
            modus = aktivModusTid.getModus();
            System.out.println(aktivModusTid.getModus());
            modusKlokke = new Date();
        }

        if(aktivModusTid.getSekunder() == 0){
            aktivModusTid = modusStack.pop();
            modusKlokke = new Date();
        }
    }
    public void startKlokke(){
        modus = aktivModusTid.getModus();
        modusKlokke = new Date();
    }
    public void setFrightenModus(){
        if(!sjekkOmFrightFirstTime && modus != SpøkelsesModus.FRIGHTENED && modus != SpøkelsesModus.EATEN) {
            modus = SpøkelsesModus.FRIGHTENED;
            sjekkOmFrightFirstTime = true;
            sjekkOmFrightFirstIRetning = true;
            backupRetningFrighten = retning;
            backupRuteY = sluttRuteY;
            backupRuteX = sluttRuteX;
            retning = sistRetning;
            sluttRuteX = sistRuteX;
            sluttRuteY = sistRuteY;
            bildeSpøkelse.setImage(bildeScared);
        }
    }

}




