package com.example.packman;

import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PackmanGui extends Application {

    final int WIN = 1000;
    BorderPane mainPane;
    Rute[][] grid;
    final String startLenke ="src/main/resources/com/example/packman/";
    RuteSamling tileset;

    @Override
    public void start(Stage stage) throws IOException {
        mainPane = new BorderPane();
        mainPane.setCenter(new BanePane("test", WIN));
        Scene scene = new Scene(mainPane, WIN, WIN);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    /*
    public GridPane mapSetUp(String baneNavn) {
        // setup, g returneres, grid to dim tabell som tar vare på alle rektangler for nå
        GridPane g = new GridPane();
        try {
            // åpner datastrøm
            Scanner leser = new Scanner(new File(startLenke + "baner/" + baneNavn + ".txt"));
            //behandler starten av filen - info om banen
            String linje = leser.nextLine();
            System.out.println("Bane info: ");
            String[] datTab = linje.split(";");
            int b = Integer.parseInt(datTab[0]);
            int h = Integer.parseInt(datTab[1]);
            String filnavn = datTab[2];
            System.out.println("bredde - " + b + ", høyde - " + h + ", filnavn for tileset - " + filnavn);

            // lager tabell for banen
            grid = new Rute[b][h];
            // regner ut hvor stor en rute skal være
            int rute;
            if (h < b) {
                rute = (WIN - 100) / b;
            } else {
                rute = (WIN - 100) / h;
            }
            System.out.println("rute størrelse: " + rute);

            // henter tileset med hjelp av filnavn og rute størrelse
            tileset = hentTileset(filnavn, rute);

            //behandler resten av filen - selve banen
            int linjeTeller = 0;
            while (leser.hasNextLine()) {
                linje = leser.nextLine();
                System.out.println("linje: " + linje);
                String[] baneTab = linje.split(",");
                for (int i = 0; i < b; i++) {
                    int index = Integer.parseInt(baneTab[i]);
                    System.out.print(index + ",");
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

    public RuteSamling hentTileset(String filnavn, double r){
        RuteSamling samling = new RuteSamling();
        System.out.println("Henter tilset");
        try{
            // åpner datastrøm for å hente tilset
            Scanner leser = new Scanner(new File(startLenke + "tilesets/" + filnavn + ".txt"));
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
                nyRute.setRuteStr(r);
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
*/















    public static void main(String[] args) {
        launch();
    }
}
