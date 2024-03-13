/* Denne klassen skal levere en tile editor.
 * Her bruker vi superklassen Editor og bygger videre på den
 * Vi skal vise frem frager på venstre side og gjære andre tiles i samme samling over
 * Høyre side skal ha noen knapper og comboboxser som vi lager her
 */

package com.example.packman.editor;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class TileEditor extends Editor {

    private final String LENKE_FARGER = "src/main/resources/com/example/packman/fargeSamlinger/";
    private final String LENKE_OVERSIKT = "src/main/resources/com/example/packman/Oversikter/";

    private Color leggTilfarge;
    private Color valgtfarge;
    private int antFarger;
    private Button btNyFarge, btNyFargeSamling, btHentFargeSamling;
    private ArrayList<Color> fargerSamling;
    private GridPane fargePallet;
    private HBox fargeInfo;
    private int palletRader;
    private int palletKolonner;
    private TextField tfFargeSamling;
    private Label lFargeInfo;
    private ComboBox<String> cbFargeSamling; //ComboBox
    private String[] alleFargeSamlinger;

    final ColorPicker colorPicker = new ColorPicker();

    public TileEditor(int vinduStrX, int vinduStrY) {
        super(vinduStrX, vinduStrY);

        viderePallet();
        //gridPane.setOnMousePressed(e -> this.tileClick(e));
        gridPane.setOnMouseDragged(e -> this.tileClick(e));
    }


    public void tileClick(MouseEvent e){
        //valgtfarge = fargerSamling.get(0);
        //System.out.println("Trykk");
        // +2 siden border også tar plass
        int x = (int) ((e.getX())/ (ruteStr ));
        int y = (int) ((e.getY())/ (ruteStr ));

        //System.out.println("Rektangel har høyde: " + tile[x][y].getHeight() + ", bredden: " + tile[x][y].getWidth());
        //System.out.println("ruteStr: " + ruteStr);
        //System.out.println("X: " + x + ", Y: " + y);
        //System.out.println("e(getX): " + e.getX() + ", e(getY): " + e.getY());

        // farger valg rute
        maling(x, y);
    }

    public void maling(int x, int y){
        /*if(x > pxPerRute || y > pxPerRute || x < 0 || y < 0){
            return;  // vil ikke male hvis x eller y er mindre enn pxPerRute,
                     // eller uten for canvas (tile tabell)
        }*/
        try{
            tile[x][y].setFill(valgtfarge);
        }catch (IndexOutOfBoundsException e){
            //kan ikke tegne hvis peker er utenfor tabellern
        }

    }


    public void videreInfoPanel(){



    }


// Metoder og klasser for pallet

    public void viderePallet() {
        // vi bygger vidre på pallet, viser frem farger som bruker kan velge

        // setter litt info som er relatert til pallet
        btNyFarge = new Button("Ny farge");
        btNyFarge.setOnAction(e -> nyFarge());
        //
        leggTilfarge = Color.BLACK;

        hentFargeSamlingOversikt();

        HBox samlingInfo = new HBox();

        cbFargeSamling = new ComboBox<>();
        cbFargeSamling.setPromptText("Velg fargesamling");
        cbFargeSamling.setMinWidth(ruteStr*3);
        cbFargeSamling.getItems().addAll(alleFargeSamlinger);

        btHentFargeSamling = new Button("Hent");
        btHentFargeSamling.setOnAction(e -> hentPallet());

        samlingInfo.getChildren().addAll(cbFargeSamling, btHentFargeSamling);
        pallet.getChildren().add(samlingInfo);

        fargeInfo = new HBox();
        fargePallet = new GridPane();
        fargerSamling = new ArrayList<>();

        colorPicker.setValue(leggTilfarge);
        colorPicker.setMaxWidth(ruteStr*3);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event e) {
                leggTilfarge = colorPicker.getValue();
            }
        });
        fargeInfo.getChildren().addAll(colorPicker, btNyFarge);
        pallet.getChildren().add(fargePallet);
        pallet.getChildren().add(fargeInfo);

        tfFargeSamling = new TextField();
        pallet.getChildren().add(tfFargeSamling);

        btNyFargeSamling = new Button("Legg til ny/oppdater fargesamling");
        btNyFargeSamling.setOnAction(e -> lagrePallet());
        pallet.getChildren().add(btNyFargeSamling);

        lFargeInfo = new Label();
        pallet.getChildren().add(lFargeInfo);

    }

    public void nyFarge(){
        //legger ny farge inn i samling
        System.out.println("Ny farge!! " + colorPicker.getValue());
        fargerSamling.add(leggTilfarge);
        if( (fargerSamling.size() - 1 )% 3 == 0){ // må ha -1 hvor size ikke tar med 0 som index
            palletRader++;
            palletKolonner = 0;
        }

        fargePallet.add(new Farge(leggTilfarge), palletKolonner, palletRader);
        palletKolonner++;
        //System.out.println("Antall farger i samling = " + fargerSamling.size());
        //System.out.println("palletRader = " + palletRader + ", palletKolonner = " + palletKolonner);
    }

    public void lagrePallet(){
        // lagrer farger fra fargerSamling
        String filnavn = tfFargeSamling.getText();

        if(filnavn.equals("")){
            System.out.println("Samlingsnavn mangler.");
            lFargeInfo.setText("Samlingsnavn mangler!");
            return;
        }
        lFargeInfo.setText("");

        boolean oppdater = false;
        for(int i = 0; i < alleFargeSamlinger.length; i++){
            if(alleFargeSamlinger[i].equals(filnavn)){
                oppdater = true;
                break;
            }
        }
        if(oppdater){
            lFargeInfo.setText("Fargesamling oppdatert!");
        }
        try{
            // legger inn farger til tekst fil fra fargeSamling
            PrintWriter writer = new PrintWriter(new FileWriter(LENKE_FARGER + filnavn + ".txt"));
            for(Color c : fargerSamling){
                writer.println(c.toString());
            }
            writer.close();
            lFargeInfo.setText("Fargesamling lagret!");
            writer.close();

            // lagrer også navnet i fargesamling.txt
            writer = new PrintWriter(new FileWriter(LENKE_OVERSIKT + "fargeSamling.txt", true));

            writer.println(filnavn);
            writer.close();

            //fil ikke oppdatere fargeSamling oversikt hvis bruker bare oppdaterer
            if(oppdater){
                return;
            }

            // legger inn ny fargesamling i fargesamling.txt
            String[] gammel = alleFargeSamlinger;
            alleFargeSamlinger = new String[gammel.length + 1];
            for(int i = 0; i < gammel.length; i++){
                alleFargeSamlinger[i] = gammel[i];
            }
            // ikke +1 hvor length er uten index
            alleFargeSamlinger[gammel.length] = filnavn;

            //oppdaterer oversikt som er synelig for bruker
            cbFargeSamling.getItems().clear();
            cbFargeSamling.getItems().addAll(alleFargeSamlinger);

        }catch (Exception e){
            lFargeInfo.setText("Ugyldig farge!");
            return;
        }

    }

    public void hentPallet() {
        String filnavn = cbFargeSamling.getValue();
        if(filnavn == null || filnavn.equals("Velg fargesamling")){
            System.out.println("Samlingsnavn mangler.");
            lFargeInfo.setText("Samlingsnavn mangler!");
            return;
        }
        lFargeInfo.setText("");
        try{
            // legger inn farger til tekst fil fra fargeSamling
            Scanner scanner = new Scanner(new File( LENKE_FARGER + filnavn + ".txt"));

            fargerSamling.clear();

            while(scanner.hasNextLine()){
                String linje = scanner.nextLine();
                Color c = Color.valueOf(linje);
                fargerSamling.add(c);
            }
            scanner.close();

            // tømmer gridpane
            fargePallet.getChildren().clear();
            palletRader = 0;
            palletKolonner = 0;
            // legger inn farger på nytt
            for(Color c : fargerSamling){
                if(palletKolonner == 3){
                    palletKolonner = 0;
                    palletRader++;
                }
                fargePallet.add(new Farge(c), palletKolonner, palletRader);
                palletKolonner++;
            }

            lFargeInfo.setText("Fargesamling hentet!");
            System.out.println("Fargesamling: " + filnavn + " er hentet!");

        }catch (Exception e){
            lFargeInfo.setText("Noe gikk galt med henting av fargesamling!");
            return;
        }
    }

    public void hentFargeSamlingOversikt(){
        //henter oversikt over fargesamlinger
        System.out.println("Henter oversikt over fargesamlinger");
        int teller = 0;
        try{
            // leser fargesamling oversikt
            Scanner scanner = new Scanner(new File( LENKE_OVERSIKT + "fargeSamling.txt"));

            while(scanner.hasNextLine()){
                String linje = scanner.nextLine();
                teller++;
            }
            scanner.close();
            alleFargeSamlinger = new String[teller];
            teller = 0;

            scanner = new Scanner(new File( LENKE_OVERSIKT + "fargeSamling.txt"));
            while(scanner.hasNextLine()){
                String linje = scanner.nextLine();
                alleFargeSamlinger[teller] = linje;
                teller++;
            }

            System.out.println("Fargesamling hentet!");

        }catch (Exception e){
            lFargeInfo.setText("Noe gikk galt med henting av fargesamling!");
            return;
        }




        // alleFargeSamlinger
    }


    private class Farge extends Button {
        // indre klasse for å lage farge knapper til pallet
        private Color farge;
        private int str;
        private int id;     // reflekterer Arraylist fargeSamling
        private int teller = 0;
        private Rectangle fargeRek;
        public Farge(Color farge) {
            this.farge = farge;
            str = ruteStr - 5;      // -5 for litt rom i pallet
            setPrefSize(str, str);
            fargeRek = new Rectangle(str, str);
            fargeRek.setFill(farge);
            fargeRek.setStroke(Color.BLACK);
            fargeRek.setStrokeWidth(1);
            setGraphic(fargeRek);

            //setStyle("-fx-border-color: black; -fx-border-width: 1px;"); //  -fx-background-color: " + farge.toString() + ";"

            setOnAction(e -> {
                valgtfarge = this.farge;
                System.out.println("Ny valgt farge = " + valgtfarge);
            });
        }

        public Color getFarge() {
            return farge;
        }

    }


}
