package com.example.packman;

import com.example.packman.Elementer.IkkeLevende.Dots;
import com.example.packman.Elementer.IkkeLevende.PowerUp;
import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Elementer.Levende.PacMan;
import com.example.packman.Elementer.Levende.Spøkelser.Blinky;
import com.example.packman.Elementer.Levende.Spøkelser.Clyde;
import com.example.packman.Elementer.Levende.Spøkelser.Inky;
import com.example.packman.Elementer.Levende.Spøkelser.Pinky;
import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import com.example.packman.misc.IkkeLevendeType;
import com.example.packman.misc.Vector2D;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/* Egen klasse for å sette opp panel som setter igang spillet
 *
 * Mulige bugs:
 * - når banen bygges kan det hende det ikke regnes ut riktig hvor stor rute størrelsen er (har ikk testet hvis banen er breiere enn den er høy
 */

public class BanePane extends BorderPane{

    private final String LENKE ="src/main/resources/com/example/packman/";
    private int høyde, bredde, pxPerRute= 16, score;

    private int vinduStrX, vinduStrY;
    private RuteSamling tileset;
    private String filnavn;
    private Rute[][] grid;
    private double ruteStr, sistPosX, sistPosY;
    private Timeline animasjon;
    private StackPane banen;
    private Pane elementer;
    private PacMan pac; //midlertidlig, for en test
    private Clyde clyde;
    private Inky inky;
    private Blinky blinky;
    private Pinky pinky;
    private Levende.Retning nesteRetning, sistRetning = Levende.Retning.INGEN;
    private GridPane gridPanel;
    private ArrayList<Dots> dotsListe;
    private ArrayList<PowerUp> powerListe;
    private ArrayList<Rectangle> veggListe;
    Label scoreLabel;



    /***        Konstruktør        ***/
    public BanePane(String filnavn, int vinduStrX, int vinduStrY) {
        this.filnavn = filnavn;
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;
        // setter opp selve banen
        banePlussSpiller();
        setCenter(banen);
        byggToppPanel();

        System.out.println("pacman placed: " + pac.getPacMan().getCenterX() + ", "+ pac.getPacMan().getCenterY());
        System.out.println("UP    - TileY: 768.0 ElementY: 704.0");

        System.out.println( "Tile høyde: " + grid[8][12].getHeight() + ", tile bredde: " + grid[8][12].getWidth() );

        /*
        xAndY: (8, 12)
        Info om tile - høyde: 64.0, bredde: 64.0, x: 512.0, y: 704.0
        UP    - TileY: 768.0 ElementY: 704.0

        */

        // settter igang animasjon av spillet

        animasjon = new Timeline(
                new KeyFrame(Duration.millis(40), e -> bevegelse())
        );
        animasjon.setCycleCount(Timeline.INDEFINITE);
        animasjon.play();

        //System.out.println("pacman placed: " + pac.getPacMan().getCenterX() + ", "+ pac.getPacMan().getCenterY());
        //System.out.println("UP    - TileY: 768.0 ElementY: 704.0");


    }


    /***      Metoder       ***/

    public void bevegelse(){
        // denne metoden kjører så mange ganger i sekundet som bestemt i duration i banePane konstruktøren
        // tom nå, men her kommer bevegelsen til spøkelsene inn og sjekk om de treffer packman sampt om packman spiser de opp


        if(pac.sjekkRetningLedig(nesteRetning)) {
            pac.setRetning(nesteRetning);
            pac.flyttPacManIgjen();
            sistRetning = nesteRetning;
        }else{
            pac.setRetning(sistRetning);
            pac.flyttPacManIgjen();
        }

       spise();

    }
    public void byggToppPanel()  {
        scoreLabel = new Label();
        scoreLabel.setStyle("-fx-background-color: #d5bd00; -fx-font-family: 'MS Gothic'; -fx-text-fill: #0022ff; -fx-font-size: 24px; -fx-font-weight: bold;");
        setTop(scoreLabel);
    }

    public void spise(){

        if(kollisjonMellomPacOgDots()){
            score += 100;
            scoreLabel.setText("Score: " + score);
        }
        if(kollisjonMellomPacOgPower()){
            score += 200;
            scoreLabel.setText("Score: " + score);
        }

    }


    public void start(){
        animasjon.play();
    }
    public void stop(){
        animasjon.stop();
    }
    public void pause(){
        animasjon.pause();
    }

    public void resume(){
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

        clyde = new Clyde(grid);
        clyde.byggClyde();

        inky = new Inky(grid);
        inky.byggInky();

        blinky = new Blinky(grid);
        blinky.byggBlinky();

        pinky = new Pinky(grid);
        pinky.byggPinky();


        elementer.getChildren().addAll(pac.getPacMan(), clyde.getClyde(), inky.getInky(), blinky.getBlinky(), pinky.getPinky());

        setUpElementer();
        banen.getChildren().add(elementer);
        System.out.println("PacMan er plassert");
        System.out.println("banen sin;");
        System.out.println("       bredde - " + banen.getHeight() + ", høyde - " + banen.getWidth());


        System.out.println("Banen er bygget");
    }

    public void setUpElementer(){
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
        int hjørneBredde = bredde/hjørneSkala; //halv bredd
        int hjørtneHøyde = høyde/hjørneSkala;
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[0].length; y++){
                if(grid[x][y].getType() == Rute.RuteType.GULV) {
                    // lagrer alle posisjoner power up kan spawne på - bare GULV
                    if (x < hjørneBredde && y < hjørtneHøyde) {
                        // topleft
                        topLeftPowerSpawnPos.add(new Vector2D(x ,y));
                    } else if (x < hjørneBredde && y > hjørtneHøyde * (hjørneSkala -1)) {
                        // botleft
                        botLeftPowerSpawnPos.add(new Vector2D(x ,y));
                    } else if (x > hjørneBredde * (hjørneSkala -1) && y < hjørtneHøyde) {
                        // topright
                        topRightPowerSpawnPos.add(new Vector2D(x ,y));
                    } else if (x > hjørneBredde * (hjørneSkala -1) && y > hjørtneHøyde * (hjørneSkala -1)) {
                        //botright
                        botRightPowerSpawnPos.add(new Vector2D(x ,y));
                    }
                }
            }
        }
        System.out.println(" topLeftPowerSpawnPos size: " + topLeftPowerSpawnPos.size());
        System.out.println(" topLeftPowerSpawnPos randomPos: " + topLeftPowerSpawnPos.get( (int) Math.random() * topLeftPowerSpawnPos.size() ));
        // velger en random plass power up kan spawne
        Vector2D randomPosTopLeft = topLeftPowerSpawnPos.get( (int) (Math.random() * topLeftPowerSpawnPos.size()) );
        Vector2D randomPosTopRight = topRightPowerSpawnPos.get( (int) (Math.random() * topRightPowerSpawnPos.size()) );
        Vector2D randomPosBotLeft = botLeftPowerSpawnPos.get( (int) (Math.random() * botLeftPowerSpawnPos.size()) );
        Vector2D randomPosBotRight = botRightPowerSpawnPos.get( (int) (Math.random() * botRightPowerSpawnPos.size()) );

        spawnPowerUp(randomPosBotLeft);
        spawnPowerUp(randomPosTopLeft);
        spawnPowerUp(randomPosBotRight);
        spawnPowerUp(randomPosTopRight);

        //legger inn dots nå, siden alle plasser som trenges å bli tatt er oppdatta som betyr at vi kan fylle ut resten med dots
        dotsListe = new ArrayList<>();
        //går igjennom hele grid og leter etter ledige plasser
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[0].length; y++){
                if(grid[x][y].getLedigForElement()){
                    //legger inn dots i tabell og gir dem x og y verdi
                    spawnDot(new Vector2D(x, y));
                }
            }
        }


    }
    public void spawnDot(Vector2D pos){
        Dots dot = new Dots(grid, pos);
        dotsListe.add(dot);
        grid[pos.getX()][pos.getY()].setLedigForElement(false);
        grid[pos.getX()][pos.getY()].setElementType(IkkeLevendeType.DOT);
        elementer.getChildren().add(dotsListe.get(dotsListe.size()-1).getDot());
    }

    public void spawnPowerUp(Vector2D pos){

        PowerUp newPower = new PowerUp(grid, pos);

        powerListe.add(newPower);
        grid[pos.getX()][pos.getY()].setLedigForElement(false);
        grid[pos.getX()][pos.getY()].setElementType(IkkeLevendeType.POWERUP);
        elementer.getChildren().add(powerListe.get(powerListe.size()-1).getPowerUp());
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
            ruteStr = (vinduStrX - 100)  / bredde;
            ruteStr = (int) ruteStr / 16;
            ruteStr = (int) ruteStr * 16;

            System.out.println("Siste rute størrelse test: " + ruteStr);

            // henter tileset med hjelp av filnavn og rute størrelse
            tileset = hentTileset(tileFilnavn);
            veggListe = new ArrayList<>();
            System.out.println("tileset er hentet");

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

                    Rectangle tile = new Rectangle( ruteStr, ruteStr);
                    tile.setFill(Color.TRANSPARENT);

                    //henter utseende
                    Rectangle[][] utseendeSjekk = tileset.getRuteFraSamling(id).getUtseende();

                    Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                    //utseende
                    for(int x = 0; x < utseende.length; x++) {
                        for(int y = 0; y < utseende[x].length; y++) {
                            utseende[x][y] = new Rectangle(ruteStr/16, ruteStr/16);
                            utseende[x][y].setFill(utseendeSjekk[x][y].getFill());
                        }
                    }

                    Rute.RuteType type = tileset.getRuteFraSamling(id).getType();


                    //lager ny rute og legger den inn in gridet
                    Rute nyRute = new Rute(id, type, tile, utseende, ruteStr);
                    nyRute.setRuteX(ruteStr*i);
                    nyRute.setRuteY(ruteStr*linjeTeller);


                    grid[i][linjeTeller] = nyRute;

                    if(nyRute.getType() != Rute.RuteType.GULV){
                        //System.out.println("ikke gulv");
                        veggListe.add(nyRute.getTile());
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

        } catch (IndexOutOfBoundsException e){
            System.out.println();
            System.out.println("Tabell feil - \n" + e);
            return null;
        }catch (Exception e){
            System.out.println();
            System.out.println("Noe fikk galt under filbehandlig - bane \n" + e);
            return null;
        }

        return g;
    }
    public boolean kollisjonMellomPacOgDots() {
        // metode for å spise dots
        for (Dots d : dotsListe) {
            // Sjekk kollisjon mellom sirkelen og hvert rektangel
            Circle dot = d.getDot();
            Shape intersect = Shape.intersect(pac.getPacMan(), dot);

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
    public boolean kollisjonMellomPacOgPower(){
        // metode for å sjekke om pacman spsier en powerup
        for (PowerUp p : powerListe) {
            // Sjekk kollisjon mellom sirkelen og hvert rektangel
            Circle power = p.getPowerUp();
            Shape intersect = Shape.intersect(pac.getPacMan(), power);

            if(intersect.getBoundsInLocal().getWidth() != -1){
                // Det er en kollisjon mellom pacman og en powerup
                //System.out.println("Kollisjon oppdaget med powerup: " + power);
                power.setFill(Color.TRANSPARENT);
                power.setStroke(Color.TRANSPARENT);
                powerListe.remove(p);
                return true;
            }
        }

        return false;
    }

    public RuteSamling hentTileset(String tileFilnavn){
        RuteSamling samling = new RuteSamling();
        System.out.println("Henter tilset");
        try{
            // åpner datastrøm for å hente tilset
            Scanner scanner = new Scanner(new File(LENKE + "tilesets/" + tileFilnavn + ".txt"));
            // behandler datastrøm
            while(scanner.hasNextLine()) {
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
                        Rectangle pixel = new Rectangle(ruteStr/16, ruteStr/16);
                        pixel.setFill(Color.valueOf(strTab[j]));
                        utseende[i][j] = pixel;
                    }
                }
                Rectangle tile = new Rectangle(ruteStr, ruteStr);
                tile.setFill(Color.TRANSPARENT);
                //System.out.println("all info er hentet ");

                linje =scanner.nextLine();
                //System.out.println("linje i tilesethenting, skal være ':', og er - " + linje);
                // legger inn til rutesamling
                samling.leggTil( new Rute(id, type, tile, utseende, ruteStr) );
                //System.out.println("tileset er lagt til i samling");
                // nå har vi hentet et helt rute objekt
                // men leseren er fortsatt på feil plass

            }
            System.out.println("tilesettet er hentet");
            scanner.close();

        }catch (Exception e){
            System.out.println("Noe gikk galt med filbehandling - tileset \n" + e);
            return null;
        }
        System.out.println("Vellykket henting av tileset");
        System.out.println("Hentet " + samling.hentSamlingStr() + " forskjellige tiles");
        return samling;
    }

    public HBox scoreTeller () {
        HBox score = new HBox();
        score.setSpacing(10);
        Text textSpis = new Text();
        return score;
    }


}
