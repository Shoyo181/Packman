package com.example.packman;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Elementer.Levende.PacMan;
import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.util.Scanner;
/* Egen klasse for å sette opp panel som setter igang spillet
 *
 * Mulige bugs:
 * - når banen bygges kan det hende det ikke regnes ut riktig hvor stor rute størrelsen er (har ikk testet hvis banen er breiere enn den er høy
 */

public class BanePane extends BorderPane{

    private final String LENKE ="src/main/resources/com/example/packman/";
    private int høyde, bredde, pxPerRute= 16;

    private int vinduStrX;
    private int vinduStrY;
    private RuteSamling tileset;
    private String filnavn;
    private Rute[][] grid;
    private double ruteStr;
    private Timeline animasjon;
    private StackPane banen;
    private Pane elementer;
    private PacMan pac; //midlertidlig, for en test
    private Levende.Retning nesteRetning;
    private GridPane gridPanel;



    /***        Konstruktør        ***/
    public BanePane(String filnavn, int vinduStrX, int vinduStrY) {
        this.filnavn = filnavn;
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;
        // setter opp selve banen
        banePlussSpiller();
        setCenter(banen);

        // settter igang animasjon av spillet
        animasjon = new Timeline(
                new KeyFrame(Duration.millis(40), e -> bevegelse())
        );
        animasjon.setCycleCount(Timeline.INDEFINITE);
        animasjon.play();

    }


    /***      Metoder       ***/

    public void bevegelse(){
        // denne metoden kjører så mange ganger i sekundet som bestemt i duration i banePane konstruktøren
        // tom nå, men her kommer bevegelsen til spøkelsene inn og sjekk om de treffer packman sampt om packman spiser de opp
        if(pac.sjekkRetning(nesteRetning))
            pac.setRetning(nesteRetning);
        pac.flyttPacMan();
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

        elementer.getChildren().add(pac.getPacMan());
        banen.getChildren().add(elementer);
        System.out.println("PacMan er plassert");
        System.out.println("banen sin;");
        System.out.println("       bredde - " + banen.getHeight() + ", høyde - " + banen.getWidth());


        System.out.println("Banen er bygget");
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

                    Rectangle tile = new Rectangle(ruteStr, ruteStr);
                    tile.setFill(Color.TRANSPARENT);

                    //henter utseende
                    Rectangle[][] utseendeSjekk = tileset.getRuteFraSamling(id).getUtseende();

                    Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                    //utseende
                    for(int x = 0; x < utseende.length; x++) {
                        for(int y = 0; y < utseende[i].length; y++) {
                            utseende[x][y] = new Rectangle(ruteStr/16, ruteStr/16);
                            utseende[x][y].setFill(utseendeSjekk[x][y].getFill());
                        }
                    }

                    Rute.RuteType type = tileset.getRuteFraSamling(id).getType();

                    System.out.println("id: " + id + ", type: " + type);
                    //lager ny rute og legger den inn in gridet
                    Rute nyRute = new Rute(id, type, tile, utseende, ruteStr);


                    grid[i][linjeTeller] = nyRute;


                    // lager kopi av rektanglet i ruteklassen, siden vi ikke får lov til å legge inn
                    // duplikater i grid



                    //System.out.println("i: " + i + ", linjeTeller: " + linjeTeller);
                    //      objekt,                       x,   y index
                    g.add(grid[i][linjeTeller].kopierRute(), i, linjeTeller);

                }
                linjeTeller++;
                System.out.println(linje);
            }
            leser.close();

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
                System.out.println(linje);
                String datTab[] = linje.split(":");
                int id = Integer.parseInt(datTab[0]);
                // type
                linje = scanner.nextLine();
                System.out.println(linje);
                Rute.RuteType type = Rute.RuteType.valueOf(linje);
                // design til ruta
                Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                for (int i = 0; i < pxPerRute; i++) {
                    linje = scanner.nextLine();
                    System.out.println(linje);
                    String strTab[] = linje.split(";");
                    for (int j = 0; j < pxPerRute; j++) {
                        Rectangle pixel = new Rectangle(ruteStr/16, ruteStr/16);
                        pixel.setFill(Color.valueOf(strTab[j]));
                        utseende[i][j] = pixel;
                    }
                }
                Rectangle tile = new Rectangle(ruteStr, ruteStr);
                tile.setFill(Color.TRANSPARENT);
                System.out.println("all info er hentet ");

                linje =scanner.nextLine();
                System.out.println("linje i tilesethenting, skal være ':', og er - " + linje);
                // legger inn til rutesamling
                samling.leggTil( new Rute(id, type, tile, utseende, ruteStr) );
                System.out.println("tileset er lagt til i samling");
                // nå har vi hentet et helt rute objekt
                // men leseren er fortsatt på feil plass

            }
            scanner.close();

        }catch (Exception e){
            System.out.println("Noe gikk galt med filbehandling - tileset \n" + e);
            return null;
        }
        System.out.println("Vellykket henting av tileset");
        System.out.println("Hentet " + samling.hentSamlingStr() + " forskjellige tiles");
        return samling;
    }




}
