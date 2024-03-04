package com.example.packman;

import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.util.Scanner;
/* Egen klasse for å sette opp panel som setter igang spillet
 *
 *
 */

public class BanePane extends BorderPane{

    private final String LENKE ="src/main/resources/com/example/packman/";
    private int høyde;
    private int bredde;
    private int vinduStr;
    private RuteSamling tileset;
    private String filnavn;
    private Rute[][] grid;
    private double ruteStr;
    private Timeline animasjon;
    private StackPane banen;

    private Circle pacman; //midlertidlig, for en test



    /***        Konstruktør        ***/
    public BanePane(String filnavn, int vinduStr) {
        this.filnavn = filnavn;
        this.vinduStr = vinduStr;
        setCenter(banen);

        animasjon = new Timeline(
                new KeyFrame(Duration.millis(100), e -> bevegelse())
        );
        animasjon.setCycleCount(Timeline.INDEFINITE);
        animasjon.play();
    }

    /***      Metoder       ***/

    public void bevegelse(){
        // denne metoden kjører så mange ganger i sekundet som bestemt i duration i banePane konstruktøren
        // tom nå, men her kommer bevegelsen til spøkelsene inn og sjekk om de treffer packman sampt om packman spiser de opp

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

    public void packmanOpp(){
        //bygg ferdig packman og lag metoder for bevegelse og slikt.
        // trenger også en måte å sjekke om man kolliderer med veggruter
    }




    /*  Bygger spillflaten.   */

    public void banePlussSpiller() {
        banen = new StackPane();
        //legger inn selve banen
        banen.getChildren().add(mapSetUp(filnavn));
        //legger inn elementer til banen
        banen.getChildren().add(pacman);

    }
    public GridPane mapSetUp(String baneFilnavn) {
        // setup, g returneres, grid to dim tabell som tar vare på alle rektangler for nå
        GridPane g = new GridPane();
        try {
            // åpner datastrøm
            Scanner leser = new Scanner(new File(LENKE + "baner/" + baneFilnavn + ".txt"));
            //behandler starten av filen - info om banen
            String linje = leser.nextLine();
            System.out.println("Bane info: ");
            String[] datTab = linje.split(";");
            bredde = Integer.parseInt(datTab[0]);
            høyde = Integer.parseInt(datTab[1]);
            String tileFilnavn = datTab[2];
            System.out.println("bredde - " + bredde + ", høyde - " + høyde + ", filnavn for tileset - " + tileFilnavn);

            // lager tabell for banen
            grid = new Rute[bredde][høyde];
            // regner ut hvor stor en rute skal være
            ruteStr = 0;
            if (høyde < bredde) {
                ruteStr = (vinduStr - 100) / bredde;
            } else {
                ruteStr = (vinduStr - 100) / høyde;
            }
            System.out.println("rute størrelse: " + ruteStr);

            // henter tileset med hjelp av filnavn og rute størrelse
            tileset = hentTileset(tileFilnavn);

            //behandler resten av filen - selve banen
            int linjeTeller = 0;
            while (leser.hasNextLine()) {
                linje = leser.nextLine();
                System.out.println("linje: " + linje);
                String[] baneTab = linje.split(",");
                for (int i = 0; i < bredde; i++) {
                    int index = Integer.parseInt(baneTab[i]);
                    System.out.print("Index: " + index + "; ");
                    grid[i][linjeTeller] = tileset.getRute(index);
                    //grid[i][linjeTeller].setRuteX(rute*i);
                    //grid[i][linjeTeller].setRuteY(rute*linjeTeller);

                    // lager kopi av rektanglet i ruteklassen, siden vi ikke får lov til å legge inn
                    // duplikater i grid
                    Rectangle nyRute = grid[i][linjeTeller].kopierTile();
                    System.out.println("i: " + i + ", linjeTeller: " + linjeTeller);
                    //      objekt,                       x,   y index
                    g.add(nyRute, i, linjeTeller);
                }
                linjeTeller++;
                System.out.println();
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
                boolean walkable = Boolean.parseBoolean(datTab[2]);
                boolean ghostWalk = Boolean.parseBoolean(datTab[3]);
                boolean door = Boolean.parseBoolean(datTab[4]);
                boolean home = Boolean.parseBoolean(datTab[5]);

                int id = Integer.parseInt(datTab[0]);
                Rute nyRute = new Rute(id, utsende, walkable, ghostWalk, door, home);
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
        return samling;
    }




}
