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
    private int høyde;
    private int bredde;
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
        banen.getChildren().add(mapSetUp(filnavn));
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
            if (høyde < bredde) {
                ruteStr = (vinduStrX - 100) / bredde;
            } else {
                ruteStr = (vinduStrY - 100) / høyde;
            }
            //System.out.println("rute størrelse: " + ruteStr);

            // henter tileset med hjelp av filnavn og rute størrelse
            tileset = hentTileset(tileFilnavn);

            //behandler resten av filen - selve banen
            int linjeTeller = 0;
            while (leser.hasNextLine()) {
                linje = leser.nextLine();
                //System.out.println("linje: " + linje);
                String[] baneTab = linje.split(",");
                for (int i = 0; i < bredde; i++) {
                    int index = Integer.parseInt(baneTab[i]);
                    //System.out.print("Index: " + index + "; ");
                    grid[i][linjeTeller] = tileset.kopierFraRuteSamling(index);

                    // dette er ikke lovelig hvor det blir duplikater i gridet
                    //grid[i][linjeTeller].setRuteX(ruteStr*i);
                    //grid[i][linjeTeller].setRuteY(ruteStr*linjeTeller);
                    //Rute nyRute = grid[i][linjeTeller].getRute();
                    // to bort node i Rute


                    // lager kopi av rektanglet i ruteklassen, siden vi ikke får lov til å legge inn
                    // duplikater i grid
                    Rectangle nyRute = grid[i][linjeTeller].kopierTile();


                    //System.out.println("i: " + i + ", linjeTeller: " + linjeTeller);
                    //      objekt,                       x,   y index
                    g.add(nyRute, i, linjeTeller);

                }
                linjeTeller++;
                //System.out.println();
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
            Scanner leser = new Scanner(new File(LENKE + "tilesets/" + tileFilnavn + ".txt"));
            // behandler datastrøm
            while(leser.hasNextLine()){
                String linje = leser.nextLine();
                String[] datTab = linje.split(";");
                Rectangle utsende = new Rectangle();
                Paint farge = Paint.valueOf(datTab[1]);
                utsende.setFill(farge);
                Rute.RuteType type = Rute.RuteType.valueOf(datTab[2]);

                /*boolean walkable = Boolean.parseBoolean(datTab[2]);
                boolean ghostWalk = Boolean.parseBoolean(datTab[3]);
                boolean door = Boolean.parseBoolean(datTab[4]);
                boolean home = Boolean.parseBoolean(datTab[5]);

                int id = Integer.parseInt(datTab[0]);
                Rute nyRute = new Rute(id, utsende, walkable, ghostWalk, door, home);
                nyRute.setRuteStr(ruteStr);
                samling.leggTil(nyRute);

                 */
                int id = Integer.parseInt(datTab[0]);
                Rute nyRute = new Rute(id, utsende, type);
                nyRute.setRuteStr(ruteStr);
                samling.leggTil(nyRute);
            }
            // lukker datastrøm
            leser.close();

        }catch (Exception e){
            System.out.println("Noe gikk galt med filbehandling - tileset \n" + e);
            return null;
        }
        System.out.println("Vellykket henting av tileset");
        System.out.println("Hentet " + samling.hentSamlingStr() + " forskjellige tiles");
        return samling;
    }




}
