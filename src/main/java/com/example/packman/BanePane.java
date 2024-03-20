package com.example.packman;

import com.example.packman.Elementer.IkkeLevende.Cherry;
import com.example.packman.Elementer.IkkeLevende.Dots;
import com.example.packman.Elementer.IkkeLevende.PowerUp;
import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Elementer.Levende.PacMan;
import com.example.packman.Elementer.Levende.Spøkelser.*;
import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import com.example.packman.misc.IkkeLevendeType;
import com.example.packman.misc.SpøkelsesModus;
import com.example.packman.misc.Vector2D;
import com.example.packman.misc.VectorDouble;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
/* Egen klasse for å sette opp panel som setter igang spillet
 *
 * Mulige bugs:
 * - når banen bygges kan det hende det ikke regnes ut riktig hvor stor rute størrelsen er (har ikk testet hvis banen er breiere enn den er høy
 */

public class BanePane extends BorderPane {

    private final String LENKE = "src/main/resources/com/example/packman/";
    private int høyde, bredde, pxPerRute = 16, score;

    private int vinduStrX, vinduStrY;
    private RuteSamling tileset;
    private String filnavn;
    private Rute[][] grid;
    private double ruteStr, sistPosX, sistPosY;
    private Timeline animasjon;
    private StackPane banen;
    private Pane elementer;
    private PacMan pac, kopiPac;
    private Clyde clyde;
    private Inky inky;
    private Blinky blinky;
    private Pinky pinky;
    private Levende.Retning nesteRetning, sistRetning = Levende.Retning.INGEN;
    private GridPane gridPanel;
    private ArrayList<Dots> dotsListe;
    private ArrayList<PowerUp> powerListe;
    private ArrayList<Rectangle> veggListe, sidenTilBanen;
    private ArrayList<Spøkelser> spøkelseListe; // for alle spøkelsene, må lagre hitboxen deres
    private ArrayList<HjerteContainer> hjerteListe;
    private HBox bunnPanel, tomSide;
    private Cherry cherry;
    private boolean cherrySpawned = false, erImun, iTunell;

    Label scoreLabel, time;

    private Date start, imun;


    /***        Konstruktør        ***/
    public BanePane(String filnavn, int vinduStrX, int vinduStrY) {
        this.filnavn = filnavn;
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;
        // setter opp selve banen
        banePlussSpiller();
        setCenter(banen);
        byggToppPanel();
        byggBunnPanel();
        setBottom(bunnPanel);
        //tomSide();
        // settter igang animasjon av spillet

        animasjon = new Timeline(
                new KeyFrame(Duration.millis(30), e -> bevegelse())
        );
        animasjon.setCycleCount(Timeline.INDEFINITE);
        //animasjon.play();


    }

/*public HBox tomSide() {
        // bsnen tar bredde*ruteStr i x-aksen og høyde*ruteStr i y-aksen
        // bunn tar høyde*ruteStr i x-aksen og høyde*ruteStr i y-aksen

        double plassIgjenX = vinduStrX- (bredde * ruteStr) ;

        tomSide = new HBox();
        setLeft(tomSide);
        tomSide.setPrefWidth(plassIgjenX/2);
        tomSide.setPrefHeight(vinduStrY);
        tomSide.setStyle("-fx-background-color: transparent;");

        return tomSide;

    }*/
    /***      Metoder       ***/

    public void bevegelse() {
        // denne metoden kjører så mange ganger i sekundet som bestemt i duration i banePane konstruktøren
        // tom nå, men her kommer bevegelsen til spøkelsene inn og sjekk om de treffer packman sampt om packman spiser de opp



        // før vi kan bevege på spøkelsene må vi bestemme hvilken mode de skal ha

        //bestemMode();
        oppdaterPacmanPos();

        clyde.flyttClyde();
        pinky.flyttPinky();
        blinky.flyttBlinky();
        inky.flyttInky();

        kollisjonMellomPacOgSpøkelse();
        //kollisjonMellomPacOgSidenTilBanen();
        //tunellHåndtering();

        if(pac.sjekkRetningLedig(nesteRetning, veggListe)) {
            pac.setRetning(nesteRetning);
            pac.flyttPacManIgjen();
            sistRetning = nesteRetning;
            if (!pac.spiserPacman()) {
                pac.startSpiseing();
                start = new Date();
            }
        } else {
            pac.setRetning(sistRetning);
            pac.flyttPacManIgjen();
        }

        spise();
        beregnTid();
        setCherrySpawned();


      //  System.out.println("Score: " + score + ", time (min, sek): " + (new Date().getTime() - start.getTime()) / 1000 / 60 + ", " + (new Date().getTime() - start.getTime()) / 1000 % 60);


    }

    public Vector2D getVinduStrIgjen(){
        // returnerer hvor mye plass som er igjen i begge akser
        int x = (int) ((vinduStrX - (bredde * ruteStr)) /2);
        int y = (int) ((vinduStrY - (høyde * ruteStr)) /2);
        return new Vector2D(x, y);
    }


    public void beregnTid() {
        // beregner tid spilt i spillet
        int min =(int) (new Date().getTime() - start.getTime()) / 1000 / 60;
        int sek =(int) (new Date().getTime() - start.getTime()) / 1000 % 60;
        String tid = "";
        if(min < 10) {
            tid += "0" + min+ ":";
        } else {
            tid += min + ":";
        }
        if(sek < 10) {
            tid += "0" + sek;
        }else {
            tid += sek;
        }
        time.setText(tid);

        // oppdaterer om spiller er imun eller ikke

        if(erImun) {
            // betyr at spiller har akuratt blitt truffet
            //bestemmer hvor lenge pacman skal være imun - vi velger 2 sekunder
            if((new Date().getTime() - imun.getTime()) / 1000 >= 2) {
                erImun = false;
                //stopper klokken også
                imun = null;
            }
        }

    }

    public void byggToppPanel() {

        HBox toppInfo = new HBox();

        GridPane grid = new GridPane();

        Label highScore = new Label("Highscore: ");
        highScore.setStyle("-fx-font-family: 'MS PGothic'; -fx-text-fill: #fff200; -fx-font-weight: bold; -fx-font-size: 15;");
        Text highestScore = new Text();
        highestScore.setStyle("-fx-font-family: 'MS PGothic'; -fx-text-fill: #fff200; -fx-font-weight: bold; -fx-font-size: 15;");

        Label score = new Label("Score: ");
        score.setStyle("-fx-font-family: 'MS PGothic'; -fx-text-fill: #fff200; -fx-font-weight: bold; -fx-font-size: 15;");
        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-font-family: 'MS PGothic'; -fx-text-fill: #fff200; -fx-font-weight: bold; -fx-font-size: 15;");


        time = new Label("Time: ");
        time.setStyle("-fx-font-family: 'MS PGothic'; -fx-text-fill: #fff200; -fx-font-weight: bold; -fx-font-size: 15;");

        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(50);
        grid.setVgap(10);
        grid.add(highScore, 0, 0);
        grid.add(highestScore, 0, 1);
        grid.add(score, 2, 0);
        grid.add(scoreLabel, 2, 1);
        grid.add(new Label("Time: "), 4, 0);
        grid.add(time, 4, 1);


        //toppInfo.setStyle("-fx-background-color: #000000;");

        toppInfo.getChildren().add(grid);
        setTop(toppInfo);



        //System.out.println("Score: " + score + ", time (min, sek): " + (new Date().getTime() - start.getTime()) / 1000 / 60 + ", " + (new Date().getTime() - start.getTime()) / 1000 % 60);;

    }

    public void oppdaterPacmanPos(){
        // oppdaterer pacman posisjonen for spøkelsene
        clyde.setPackmanPos(pac.getPositionIGridIndex());
        pinky.setPackmanPos(pac.getPositionIGridIndex());
        pinky.setPacmanRetning(pac.getRetning());
        blinky.setPackmanPos(pac.getPositionIGridIndex());
        inky.hentBlinkyPos(blinky.getPositionIGridIndex());
        inky.setPackmanPos(pac.getPositionIGridIndex());
        inky.setPacmanRetning(pac.getRetning());
    }

    public void bestemMode(){
        if(start == null){
            return;
        }
        // setter hvilken modus spøkelsene skal ha
        if( ((new Date().getTime() - start.getTime()) / 1000 % 60) >=15){
            clyde.setModus(SpøkelsesModus.CHASE);

            System.out.println("Clyde modus: " + clyde.getModus().toString());
        }else if (((new Date().getTime() - start.getTime()) / 1000 % 60) >= 10){
            clyde.setModus(SpøkelsesModus.PÅVEIUT);
            System.out.println("Clyde modus: " + clyde.getModus().toString());
        } else{
            clyde.setModus(SpøkelsesModus.ATHOME);
            System.out.println("Clyde modus: " + clyde.getModus().toString());
        }


        // hvis det er chase så må vi fore inn data til spøkelsene, hvilket kordinat pacman er på


    }

    public void spise() {

        if (kollisjonMellomPacOgDots()) {
            score += 100;
            scoreLabel.setText("Score: " + score);
        }
        if (kollisjonMellomPacOgPower()) {
            score += 200;
            scoreLabel.setText("Score: " + score);

        }
        if (kollisjonMellomPacOgCherry()) {
            score += 300;
            scoreLabel.setText("Score: " + score);
        }

    }


    public void start() {
        animasjon.play();
        clyde.startKlokke();
        pinky.startKlokke();
        blinky.startKlokke();
        inky.startKlokke();
    }

    public void stop() {
        animasjon.stop();
    }

    public void pause() {
        animasjon.pause();

    }

    public void resume() {
        animasjon.play();
    }

    public void oppdaterPacManRetning(Levende.Retning r) {
        // setter retning pacman vil gå
        nesteRetning = r;
    }


    /*  Bygger spillflaten.   */
    

    public void banePlussSpiller() {
        banen = new StackPane();
        //legger inn selve banen
        gridPanel = new GridPane();
        gridPanel = mapSetUp(filnavn);
        banen.getChildren().add(gridPanel);

        //legger inn elementer til banen
        elementer = new Pane();
        elementer.setPrefSize(vinduStrX, vinduStrY);


        pac = new PacMan(grid);
        pac.plasserPacMan();
        // legger pacman inn i elementer

        elementer.getChildren().addAll(pac.getPacman(), pac.getHitBox());

        // setter opp spøkelsene
        spøkelseListe = new ArrayList<>();

        clyde = new Clyde(grid);
        clyde.byggClyde();
        //legger clyde inn i elementer - husk hitboxen til clyde.
        //elementer.getChildren().add(clyde.getClyde());
        elementer.getChildren().addAll(clyde.getClyde() , clyde.getHitBox());
        spøkelseListe.add(clyde);

        inky = new Inky(grid);
        inky.byggInky();
        spøkelseListe.add(inky);
        elementer.getChildren().addAll(inky.getInky(), inky.getHitBox());

        blinky = new Blinky(grid);
        blinky.byggBlinky();
        spøkelseListe.add(blinky);
        elementer.getChildren().addAll(blinky.getBlinky(), blinky.getHitBox());

        pinky = new Pinky(grid);
        pinky.byggPinky();
        spøkelseListe.add(pinky);
        elementer.getChildren().addAll(pinky.getPinky(), pinky.getHitBox());

        setUpElementer();
        banen.getChildren().add(elementer);
        System.out.println("PacMan er plassert");
        System.out.println("banen sin;");
        System.out.println("       bredde - " + banen.getHeight() + ", høyde - " + banen.getWidth());


        System.out.println("Banen er bygget");
    }

    public void setUpElementer() {
        //legger inn powerUp først siden de tar en plass først
        powerListe = new ArrayList<>();

        ArrayList<Vector2D> topLeftPowerSpawnPos = new ArrayList<>();
        ArrayList<Vector2D> topRightPowerSpawnPos = new ArrayList<>();
        ArrayList<Vector2D> botLeftPowerSpawnPos = new ArrayList<>();
        ArrayList<Vector2D> botRightPowerSpawnPos = new ArrayList<>();

        //går igjennom grid 4 ganger og leter etter ledige plasser
        // men med disse vil vi bare ha de i hjørnene men en random plass () der
        // top right
        int hjørneSkala = 4;
        int hjørneBredde = bredde / hjørneSkala; //halv bredd
        int hjørneHøyde = høyde / hjørneSkala;
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y].getType() == Rute.RuteType.GULV) {
                    // lagrer alle posisjoner power up kan spawne på - bare GULV
                    if (x < hjørneBredde && y < hjørneHøyde) {
                        // topleft
                        topLeftPowerSpawnPos.add(new Vector2D(x, y));
                    } else if (x < hjørneBredde && y > hjørneHøyde * (hjørneSkala - 1)) {
                        // botleft
                        botLeftPowerSpawnPos.add(new Vector2D(x, y));
                    } else if (x > hjørneBredde * (hjørneSkala - 1) && y < hjørneHøyde) {
                        // topright
                        topRightPowerSpawnPos.add(new Vector2D(x, y));
                    } else if (x > hjørneBredde * (hjørneSkala - 1) && y > hjørneHøyde * (hjørneSkala - 1)) {
                        //botright
                        botRightPowerSpawnPos.add(new Vector2D(x, y));
                    }
                }
            }
        }



        // velger en random plass power up kan spawne
        Vector2D randomPosTopLeft = topLeftPowerSpawnPos.get((int) (Math.random() * topLeftPowerSpawnPos.size()));
        Vector2D randomPosTopRight = topRightPowerSpawnPos.get((int) (Math.random() * topRightPowerSpawnPos.size()));
        Vector2D randomPosBotLeft = botLeftPowerSpawnPos.get((int) (Math.random() * botLeftPowerSpawnPos.size()));
        Vector2D randomPosBotRight = botRightPowerSpawnPos.get((int) (Math.random() * botRightPowerSpawnPos.size()));

        spawnPowerUp(randomPosBotLeft);
        spawnPowerUp(randomPosTopLeft);
        spawnPowerUp(randomPosBotRight);
        spawnPowerUp(randomPosTopRight);




        //legger inn dots nå, siden alle plasser som trenges å bli tatt er oppdatta som betyr at vi kan fylle ut resten med dots
        dotsListe = new ArrayList<>();
        //går igjennom hele grid og leter etter ledige plasser
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (grid[x][y].getLedigForElement()) {
                    //legger inn dots i tabell og gir dem x og y verdi
                    spawnDot(new Vector2D(x, y));
                }
            }
        }


    }
    public void sjekkCount () {
        if (dotsListe.size() == 0) {
            System.out.println("YOU WIN");
        }
    }
    public void spawnCherry() {
        ArrayList<Vector2D> cherrySpawnPos = new ArrayList<>();

        int baneDeler;
        if (grid.length < grid[0].length) {
            baneDeler = grid.length / 3;
        } else {
            baneDeler = grid[0].length / 3;
        }

        for (int i = baneDeler; i < grid.length - baneDeler; i++) {
            for (int j = baneDeler; j < grid[0].length - baneDeler; j++) {
                if (grid[i][j].getType() == Rute.RuteType.GULV) {
                    cherrySpawnPos.add(new Vector2D(i, j));
                }
            }
        }
        // Velger en random posisjon av alle cherry spawn posisjoner
        Vector2D pos = cherrySpawnPos.get((int) (Math.random() * cherrySpawnPos.size()));

        cherry = new Cherry(grid, pos);

        grid[pos.getX()][pos.getY()].setElementType(IkkeLevendeType.CHERRY);
        elementer.getChildren().addAll(cherry.getCherry(), cherry.getHitBox());

        System.out.println("Spawned cherry at: x: " + pos.getX() + ", y: " + pos.getY());
    }

    public void spawnDot(Vector2D pos) {
        Dots dot = new Dots(grid, pos);
        dotsListe.add(dot);
        grid[pos.getX()][pos.getY()].setLedigForElement(false);
        grid[pos.getX()][pos.getY()].setElementType(IkkeLevendeType.DOT);
        elementer.getChildren().add(dotsListe.get(dotsListe.size() - 1).getDot());
    }

    public void spawnPowerUp(Vector2D pos) {

        PowerUp newPower = new PowerUp(grid, pos);

        powerListe.add(newPower);
        grid[pos.getX()][pos.getY()].setLedigForElement(false);
        grid[pos.getX()][pos.getY()].setElementType(IkkeLevendeType.POWERUP);
        elementer.getChildren().add(powerListe.get(powerListe.size() - 1).getPowerUp());
    }


    public GridPane mapSetUp(String baneFilnavn) {
        // setup, g returneres, grid to dim tabell som tar vare på alle rektangler for nå
        GridPane g = new GridPane();
        try {
            // åpner datastrøm
            Scanner leser = new Scanner(new File(LENKE + "baner/" + baneFilnavn + ".txt"));
            //behandler starten av filen - info om banen
            String linje = leser.nextLine();
            //System.out.println("Bane info: ");
            String[] datTab = linje.split(";");
            bredde = Integer.parseInt(datTab[0]);
            høyde = Integer.parseInt(datTab[1]);
            String tileFilnavn = datTab[2];
            //System.out.println("bredde - " + bredde + ", høyde - " + høyde + ", filnavn for tileset - " + tileFilnavn);

            // lager tabell for banen
            grid = new Rute[bredde][høyde];
            System.out.println("Tabell for banen lageres - bredde: " + bredde + ", høyde: " + høyde);
            // regner ut hvor stor en rute skal være
            ruteStr = 0;
            /*
            if (høyde < bredde) {
                ruteStr = (vinduStrX - 100) / bredde;
            } else {
                ruteStr = (vinduStrY - 100) / høyde;
            }*/
            //ruteStr må være i 16 gangen siden hver rute trenger 16px hver
            //ruteStr = (vinduStrX - 100) / bredde;
            //TODO: Vil alltid velge Y som størrelse. Kanskje lage en funksjon for dette
            int tempRuteStrlX = (vinduStrX - 100) / bredde;
            int tempRuteStrlY = (vinduStrY - 100) / høyde;
            if (tempRuteStrlX < tempRuteStrlY) {
                ruteStr = tempRuteStrlX;
            } else {
                ruteStr = tempRuteStrlY;
            }
            ruteStr = (int) ruteStr / 16;
            ruteStr = (int) ruteStr * 16;

            System.out.println("Siste rute størrelse test: " + ruteStr);

            // henter tileset med hjelp av filnavn og rute størrelse
            tileset = hentTileset(tileFilnavn);
            System.out.println("tileset er hentet");

            //oppretter også lister vi trenger senere for å beregne kollisjon
            veggListe = new ArrayList<>();
            sidenTilBanen = new ArrayList<>();

            //behandler resten av filen - selve banen
            int linjeTeller = 0; // teller linjer i filen (y eller høyden)
            while (leser.hasNextLine()) {

                linje = leser.nextLine();

                //System.out.println("linje: " + linje);
                String[] baneTab = linje.split(";");

                for (int i = 0; i < bredde; i++) {      // i teller x eller bredde fra filen
                    //henter id fra bane filen
                    int id = Integer.parseInt(baneTab[i]);
                    //System.out.print("Index: " + index + "; ");
                    //grid[i][linjeTeller] = tileset.kopierFraRuteSamling(index);

                    // dette er ikke lovelig hvor det blir duplikater i gridet
                    //grid[i][linjeTeller].setRuteX(ruteStr*i);
                    //grid[i][linjeTeller].setRuteY(ruteStr*linjeTeller);
                    //Rute nyRute = grid[i][linjeTeller].getRute();
                    // to bort node i Rute

                    Rectangle tile = new Rectangle(ruteStr, ruteStr);
                    tile.setFill(Color.TRANSPARENT);

                    //henter utseende
                    Rectangle[][] utseendeSjekk = tileset.getRuteFraSamling(id).getUtseende();

                    Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                    //utseende
                    for (int x = 0; x < utseende.length; x++) {
                        for (int y = 0; y < utseende[x].length; y++) {
                            utseende[x][y] = new Rectangle(ruteStr / 16, ruteStr / 16);
                            utseende[x][y].setFill(utseendeSjekk[x][y].getFill());
                        }
                    }

                    Rute.RuteType type = tileset.getRuteFraSamling(id).getType();


                    //lager ny rute og legger den inn in gridet
                    Rute nyRute = new Rute(id, type, tile, utseende, ruteStr);
                    nyRute.setRuteX(ruteStr * i);
                    nyRute.setRuteY(ruteStr * linjeTeller);


                    grid[i][linjeTeller] = nyRute;

                    // vi legger alle ruter som ikke er gulv inn i veggListe, de oppfører seg som en hitbox for banen
                    if (nyRute.getType() != Rute.RuteType.GULV) {
                        //System.out.println("ikke gulv");
                        veggListe.add(nyRute.getTile());
                    }
                    // Vi lagrer også på de gulvflatene som er på enden til banen, hvor vi trenger de for å regne ut tuneller
                    if(nyRute.getType() == Rute.RuteType.GULV) {
                        if(i == 0 || i == bredde-1 || linjeTeller == 0 || linjeTeller == høyde-1) {
                            sidenTilBanen.add(nyRute.getTile());
                            System.out.println("Siden til sidenTilBanen - " + nyRute.getTile());
                            System.out.println("i: " + i + ", linjeTeller: " + linjeTeller + ", tile: " + nyRute.getTile());
                            System.out.println("");
                        }
                    }


                    // lager kopi av rektanglet i ruteklassen, siden vi ikke får lov til å legge inn
                    // duplikater i grid


                    //System.out.println("i: " + i + ", linjeTeller: " + linjeTeller);
                    //      objekt,                       x,   y index
                    g.add(grid[i][linjeTeller].kopierRute(), i, linjeTeller);


                }
                linjeTeller++;
                //System.out.println(linje);
            }
            leser.close();
            System.out.println("Banen er bygget");

            // hvor stor en rute skal være

        } catch (IndexOutOfBoundsException e) {
            System.out.println();
            System.out.println("Tabell feil - \n" + e);
            return null;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Noe fikk galt under filbehandlig - bane \n" + e);
            return null;
        }

        return g;
    }


    /***            Kollisjoner            ***/

    public void kollisjonMellomPacOgSpøkelse() {
        // metode for se kollisjon mellom pacman og spøkelser
        // vil ikke sjekke kollisjon hvis pacman er imun
        if(erImun) {
            return;
        }

        for (Spøkelser s : spøkelseListe) {
            // Sjekk kollisjon mellom sirkelen og hvert rektangel
            Circle spøk = s.getHitBox();
            Shape intersect = Shape.intersect(pac.getHitBox(), spøk);

            if (intersect.getBoundsInLocal().getWidth() != -1) {
                // Det er en kollisjon mellom pacman og en dot
                //System.out.println("Kollisjon oppdaget med dot: " + dot);
                spøk.setFill(Color.TRANSPARENT);
                if(s.getModus() != SpøkelsesModus.FRIGHTENED) {
                    bunnPanel.getChildren().remove(hjerteListe.size() - 1);
                    hjerteListe.remove(hjerteListe.size() - 1);
                    imunStart();
                }else if(s.getModus() == SpøkelsesModus.FRIGHTENED){
                    s.gotEaten();
                }

            }
        }
    }

    public void imunStart(){
        erImun = true;
        imun = new Date();
    }

    public boolean kollisjonMellomPacOgDots() {
        // metode for å spise dots
        for (Dots d : dotsListe) {
            // Sjekk kollisjon mellom sirkelen og hvert rektangel
            Circle dot = d.getDot();
            Shape intersect = Shape.intersect(pac.getHitBox(), dot);

            if (intersect.getBoundsInLocal().getWidth() != -1) {
                // Det er en kollisjon mellom pacman og en dot
                //System.out.println("Kollisjon oppdaget med dot: " + dot);
                dot.setFill(Color.TRANSPARENT);
                dot.setStroke(Color.TRANSPARENT);
                dotsListe.remove(d);
                return true;
            }
        }
        return false;
    }

    public boolean kollisjonMellomPacOgPower() {
        // metode for å sjekke om pacman spsier en powerup
        for (PowerUp p : powerListe) {
            // Sjekk kollisjon mellom sirkelen og hvert rektangel
            Circle power = p.getPowerUp();
            Shape intersect = Shape.intersect(pac.getHitBox(), power);

            if (intersect.getBoundsInLocal().getWidth() != -1) {
                // Det er en kollisjon mellom pacman og en powerup
                //System.out.println("Kollisjon oppdaget med powerup: " + power);
                alleSpøkelserFrighten();
                power.setFill(Color.TRANSPARENT);
                power.setStroke(Color.TRANSPARENT);
                powerListe.remove(p);
                return true;
            }
        }

        return false;
    }
    public void alleSpøkelserFrighten() {
        for (Spøkelser s : spøkelseListe) {
            s.setFrightenModus();
        }
    }
    public boolean kollisjonMellomPacOgCherry() {
        // Metode for å sjekke om PacMan spsier en cherry

        if (cherry == null){
            return false;
        }
        Shape intersect = Shape.intersect(pac.getHitBox(), cherry.getHitBox());
        if (intersect.getBoundsInLocal().getWidth() != -1) {
            // Det er en kollisjon mellom pacman og en cherry
            elementer.getChildren().removeAll(cherry.getHitBox(), cherry.getCherry());


            cherrySpawned = false;

            return true;
        }
        return false;
    }

    private void setCherrySpawned() {
        if (!cherrySpawned) {
           Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(15), e -> spawnCherry()));
           timeline.play();
            cherrySpawned = true;
        }
    }
    public void kollisjonMellomPacOgSidenTilBanen() {
        // metode for å sjekke kollisjon mellom pacman og siden til banen - altså en tunell
        // blir egentlig ikke en kollisjon men heller

        for (Rectangle r : sidenTilBanen) {
            Shape intersect = Shape.intersect(pac.getHitBox(), r);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                System.out.println("Kollisjon oppdaget med siden til banen: " + r);
                // når vi oppdater en kollisjon spawner vi denne eniniteten på andre siden av banen også ved hjelp av en metode
                VectorDouble pacCurrKord = pac.getKordinatPosisjon();

                if (!iTunell) {
                    iTunell = true;
                    kopiPac = new PacMan(grid);
                    System.out.println("KopiPac er opprettet");
                    elementer.getChildren().addAll(kopiPac.getPacman(), kopiPac.getHitBox());
                }
                // vi finner ut hvilken side pac går inn i tunell
                double kopiX = 0;
                double kopiY = 0;
                if (r.getX() == 0) {
                    //venstre
                    System.out.println("Venstre side");
                    //bredde er størrelse på grid uten index
                    kopiX = ((bredde * ruteStr) - ruteStr / 2) + pacCurrKord.getX() + ruteStr;
                    kopiY = pacCurrKord.getY();
                } else if (r.getX() == (bredde - 1)*ruteStr) {
                    System.out.println("Høyre side");
                    kopiX = ((bredde * ruteStr) - ruteStr / 2) - pacCurrKord.getX() - ruteStr;
                    kopiY = pacCurrKord.getY();
                }
                kopiPac.setKordinatPosisjon(new VectorDouble(kopiX, kopiY));
                kopiPac.setPosisjon();
                System.out.println("KopiPac er posisjonert på : " + kopiPac.getKordinatPosisjon().toString());

            }else{
                if(iTunell){
                    iTunell = false;
                }
                if(kopiPac != null){
                    pac = kopiPac;
                    elementer.getChildren().removeAll(kopiPac.getPacman(), kopiPac.getHitBox());
                    kopiPac = null;
                }
            }
        }
    }
    public void tunellHåndtering() {
        // metode for å håndtere tunellgjennomgang av pacman


        VectorDouble pacCurrKord = pac.getKordinatPosisjon();

        //legger alt i en if som søker om pacman er nærme kanten til banen
        //vis sjekker alle sidene samtidig
        if (pacCurrKord.getX() <= ruteStr/2 || ((ruteStr * bredde) - ruteStr /2) <= pacCurrKord.getX() || pacCurrKord.getY() <= ruteStr/2 || ((ruteStr * høyde) - ruteStr /2) <= pacCurrKord.getY()) {
            // hvis dette er sant, er pacman nærme en kant

            // vi sjekker nå om vi allerede er i en tunell - hvis vi kom akuratt inn i en tunell nå, så må vi lage kopien
            if (!iTunell) {
                iTunell = true;
                kopiPac = new PacMan(grid);
                System.out.println("KopiPac er opprettet");
                elementer.getChildren().addAll(kopiPac.getPacman(), kopiPac.getHitBox());
            }
            // vi finner ut hvilken side pac går inn i tunell
            double kopiX = 0;
            double kopiY = 0;

            //sjekker sidene først
            if (pacCurrKord.getX() <= ruteStr/2) {
                //venstre
                System.out.println("Venstre side");
                //bredde er størrelse på grid uten index
                kopiX = ((bredde * ruteStr) - ruteStr / 2) + pacCurrKord.getX() + ruteStr;
                kopiY = pacCurrKord.getY();
            } else if (((ruteStr * bredde) - ruteStr /2) <= pacCurrKord.getX()) {
                System.out.println("Høyre side");
                kopiX = ((bredde * ruteStr) - ruteStr / 2) - pacCurrKord.getX() - ruteStr;
                kopiY = pacCurrKord.getY();
            }
            kopiPac.setKordinatPosisjon(new VectorDouble(kopiX, kopiY));
            kopiPac.setPosisjon();
            System.out.println("KopiPac er posisjonert på : " + kopiPac.getKordinatPosisjon().toString());
        }else{
            if(iTunell){
                iTunell = false;
            }
            if(kopiPac != null){
                pac = kopiPac;
                elementer.getChildren().removeAll(kopiPac.getPacman(), kopiPac.getHitBox());
                kopiPac = null;
            }
        }
    }


    public RuteSamling hentTileset(String tileFilnavn) {
        RuteSamling samling = new RuteSamling();
        System.out.println("Henter tilset");
        try {
            // åpner datastrøm for å hente tilset
            Scanner scanner = new Scanner(new File(LENKE + "tilesets/" + tileFilnavn + ".txt"));
            // behandler datastrøm
            while (scanner.hasNextLine()) {
                //id
                String linje = scanner.nextLine();
                //System.out.println(linje);
                String datTab[] = linje.split(":");
                int id = Integer.parseInt(datTab[0]);
                // type
                linje = scanner.nextLine();
                //System.out.println(linje);
                Rute.RuteType type = Rute.RuteType.valueOf(linje);
                // design til ruta
                Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                for (int i = 0; i < pxPerRute; i++) {
                    linje = scanner.nextLine();
                    //System.out.println(linje);
                    String strTab[] = linje.split(";");
                    for (int j = 0; j < pxPerRute; j++) {
                        Rectangle pixel = new Rectangle(ruteStr / 16, ruteStr / 16);
                        pixel.setFill(Color.valueOf(strTab[j]));
                        utseende[i][j] = pixel;
                    }
                }
                Rectangle tile = new Rectangle(ruteStr, ruteStr);
                tile.setFill(Color.TRANSPARENT);
                //System.out.println("all info er hentet ");

                linje = scanner.nextLine();
                //System.out.println("linje i tilesethenting, skal være ':', og er - " + linje);
                // legger inn til rutesamling
                samling.leggTil(new Rute(id, type, tile, utseende, ruteStr));
                //System.out.println("tileset er lagt til i samling");
                // nå har vi hentet et helt rute objekt
                // men leseren er fortsatt på feil plass

            }
            System.out.println("tilesettet er hentet");
            scanner.close();

        } catch (Exception e) {
            System.out.println("Noe gikk galt med filbehandling - tileset \n" + e);
            return null;
        }
        System.out.println("Vellykket henting av tileset");
        System.out.println("Hentet " + samling.hentSamlingStr() + " forskjellige tiles");
        return samling;
    }

    public HBox toppBar() {
        HBox toppInfo = new HBox();

        GridPane grid = new GridPane();
        Label highScore = new Label("Highscore: ");
        highScore.setStyle("-fx-background-color: #d5bd00; -fx-font-family: 'MS Gothic'; -fx-text-fill: #0022ff; -fx-font-weight: bold;");
        Text highestScore = new Text();

        Label score = new Label("Score: ");
        score.setStyle("-fx-background-color: #d5bd00; -fx-font-family: 'MS Gothic'; -fx-text-fill: #0022ff; -fx-font-weight: bold;");
        Text currentScore = new Text();


        return toppInfo;
    }


    public void byggBunnPanel()
    {
        bunnPanel = new HBox();
        bunnPanel.setMinHeight(getVinduStrIgjen().getY());
        hjerteListe = new ArrayList<>();
        //bygger hjerter i bunnen
        for (int i = 0; i < 3; i++) {
            HjerteContainer h = new HjerteContainer();
            hjerteListe.add(h);
            bunnPanel.getChildren().add(h);
            System.out.println("Hjerte bilde er lastet inn");
        }
    }

    private class HjerteContainer extends ImageView {
        public HjerteContainer() {
            byggBilde();
        }
        public void byggBilde() {
            try {
                new ImageView();
                setImage(new Image(new FileInputStream("src/main/resources/com/example/packman/bilder/Hjerte05.png")));
                setFitWidth(ruteStr);
                setFitHeight(ruteStr);

                System.out.println("Hjerte bilde er lastet inn");
            }catch (Exception e) {
                System.out.println("Noe gikk galt med filbehandling \n" + e);
            }
        }



    }

}

