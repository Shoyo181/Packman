/* Denne klassen skal levere en tile editor.
 * Her bruker vi superklassen Editor og bygger videre på den
 * Vi skal vise frem frager på venstre side og gjære andre tiles i samme samling over
 * Høyre side skal ha noen knapper og comboboxser som vi lager her
 */

package com.example.packman.editor;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class TileEditor extends Editor {

    private final String LENKE_FARGER = "src/main/resources/com/example/packman/fargeSamlinger/";
    private final String LENKE_OVERSIKT = "src/main/resources/com/example/packman/Oversikter/";
    private final String LENKE_TILESET = "src/main/resources/com/example/packman/tilesets/";

    private Color leggTilfarge;
    private Color valgtfarge;
    private Button btNyFarge, btNyFargeSamling, btHentFargeSamling, btLagreRute, btNyTileSamling, btHentTileSamling;
    private ArrayList<Color> fargerSamling;
    private GridPane fargePallet, tilePallet;
    private HBox fargeInfo, testBox;
    private int palletRader, palletKolonner, tileRuteStr, palettRuteStr;
    private TextField tfFargeSamling, tfTileSamling;
    private Label lFargeInfo, lTileInfo;
    private ComboBox<String> cbFargeSamling, cbTileSamling, cbTileType; //ComboBox
    private String[] alleFargeSamlinger, alleTileSamlinger, alleTileTyper;
    private CheckBox chRuteNett;
    private RuteSamling tileset;

    final ColorPicker colorPicker = new ColorPicker();

    public TileEditor(int vinduStrX, int vinduStrY) {
        super(vinduStrX, vinduStrY);
        // tileRuteStr må være i 16 gangen
        palettRuteStr = ruteStr;
        tileRuteStr = 48;
        ruteStr = ruteStr - 16;

        viderePallet();
        videreInfoPanel();
        byggTilePalett();


        gridPane.setOnMousePressed(e -> this.tileClick(e));
        gridPane.setOnMouseDragged(e -> this.tileClick(e));
    }

    public void byggTilePalett(){
        tilePallet = new GridPane();
        tilePallet.setHgap(1);
        tilePallet.setVgap(1);
        tilePallet.setPadding(new Insets(5, 5, 5, 5));
        Rectangle test = new Rectangle(tileRuteStr, tileRuteStr, Color.RED);
        StackPane stack = new StackPane();
        stack.getChildren().add(test);

        GridPane g = new GridPane();

        Rectangle[][] testTable = new Rectangle[16][16];
        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                testTable[i][j] = new Rectangle((ruteStr/16) - 1, (ruteStr/16)-1, Color.TRANSPARENT);
                testTable[i][j].setStroke(Color.BLACK);
                testTable[i][j].setStrokeWidth(1);
                g.add(testTable[i][j], j, i);
            }
        }

        stack.getChildren().add(g);

        tilePallet.add(stack, 0, 0);
        tilePallet.setStyle("-fx-background-color: white; -fx-border-width: 1px; -fx-border-color: black; -fx-border-radius: 5px;");
        tilePallet.setAlignment(Pos.CENTER);
        tilePallet.setMinWidth(500);
        tilePallet.setMinHeight(180);



        testBox = new HBox();
        testBox.setStyle("-fx-background-color: white; -fx-border-width: 1px;");
        testBox.setPadding(new Insets(10, 10, 10, 10));
        testBox.setSpacing(10);
        testBox.setMinHeight(200);
        testBox.setAlignment(Pos.CENTER);
        //testBox.setMinWidth(900);
        testBox.getChildren().add(tilePallet);
        testBox.setStyle("-fx-background-color: #000046");



        byggCanvas();
        midt.getChildren().clear();
        midt.getChildren().addAll(testBox, canvas);

        setCenter(midt);
    }

    public void oppdaterTilepalett(){
        // metode som oppdaterer hva som er i tilepallet, siden bruker skal kunne bytte mellom tilesets
        tilePallet.getChildren().clear();
        int tellerKol = 0, tellerRad = 0;

        for(int i = 0; i < tileset.hentSamlingStr(); i++){
            if(tellerKol == 3){
               tellerKol = 0;
               tellerRad++;
           }
           System.out.println("Prøver å vise frem tile id - " + tileset.getRuteFraSamling(i).getRuteId());
           tilePallet.add(tileset.getRuteFraSamling(i), tellerRad, tellerKol);
           tellerKol++;
        }
    }


    public void tileClick(MouseEvent e){
        //valgtfarge = fargerSamling.get(0);
        //System.out.println("Trykk");
        // +2 siden border også tar plass
        int x = (int) ((e.getX())/ (ruteStr ));
        int y = (int) ((e.getY())/ (ruteStr ));

        //System.out.println("Rektangel har høyde: " + tile[x][y].getHeight() + ", bredden: " + tile[x][y].getWidth());
        //System.out.println("ruteStr: " + ruteStr);
        System.out.println("X: " + x + ", Y: " + y);
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
        // knapper og slikt for infopanelet
        hentCBinfo();

        //legger til comboboxene i infopanelet
        cbTileSamling = new ComboBox<>();
        cbTileType = new ComboBox<>();
        cbTileSamling.setPromptText("Velg tileset");
        cbTileType.setPromptText("Velg rute type");
        cbTileSamling.getItems().addAll(alleTileSamlinger);
        cbTileType.getItems().addAll(alleTileTyper);

        //lager knapp for å hente tilsamling
        btHentTileSamling = new Button("Hent tileset");
        btHentTileSamling.setOnAction(e -> hentTileset());

        HBox infoTileset = new HBox();
        infoTileset.setSpacing(10);
        infoTileset.getChildren().addAll(cbTileSamling, btHentTileSamling);

        //lager checkbox for å vise frem grid eller ikke
        chRuteNett = new CheckBox("Vis rutenett");
        chRuteNett.setStyle("-fx-text-fill: #ffe148");
        chRuteNett.setSelected(true);
        chRuteNett.setOnAction(e -> ruteNett());

        //lager knapp slik at bruker kan lagre ruten de lagere
        btLagreRute = new Button("Lagre ruten");
        btLagreRute.setOnAction(e -> lagreRute());

        //Legger inn teksboks og knapp for nye tileset-samlinger
        tfTileSamling = new TextField();
        btNyTileSamling = new Button("Nytt tileset");
        btNyTileSamling.setOnAction(e -> nyTileSamling());

        HBox infoTile = new HBox();
        infoTile.setSpacing(10);
        infoTile.getChildren().addAll(tfTileSamling, btNyTileSamling);

        //lager label som gir tilbakemeld til bruker
        lTileInfo = new Label();

        info.getChildren().addAll(chRuteNett, infoTileset, cbTileType, btLagreRute, infoTile, lTileInfo);

    }

    public void nyTileSamling() {
        System.out.println("Nytt tileset");
        //sjekker om bruker har skrevet inn en navn
        if (tfTileSamling.getText().equals("")) {
            lTileInfo.setText("Skriv inn en navn");
            return;
        }
        // tar vekk tidligere feilmeld
        lTileInfo.setText("");

        //sjekker om tilesetet allerede finnes
        for (int i = 0; i < alleTileSamlinger.length; i++) {
            if (tfTileSamling.getText().equals(alleTileSamlinger[i])) {
                lTileInfo.setText("Tilesetet finnes allerede");
                return;
            }
        }
        lTileInfo.setText("");

        //legger til tilesetet i arrayet
        alleTileSamlinger = Arrays.copyOf(alleTileSamlinger, alleTileSamlinger.length + 1);
        alleTileSamlinger[alleTileSamlinger.length - 1] = tfTileSamling.getText();

        //legger til i comboboxen
        cbTileSamling.getItems().add(tfTileSamling.getText());

        //lagrer tilesetet
        try{
            // her skal vi bare lage en .txt som er tom (som vi senere legger info inn om)
            // vi skal også oppdatere tilset-samling oversikten
            PrintWriter writer = new PrintWriter(new FileWriter(LENKE_TILESET + tfTileSamling.getText() + ".txt"));
            writer.close();

            //skriver videre i txt-filen
            writer = new PrintWriter(new FileWriter(LENKE_OVERSIKT + "tilesetSamling.txt", true));
            writer.println(tfTileSamling.getText());
            writer.close();

        }catch (Exception e){
            System.out.println("Klarte ikke å lagre tilesetet");
            return;
        }
        tfTileSamling.clear();
    }

    public void hentTileset(){
        System.out.println("Henter tileset");
        //sjekker om bruker har valgt en tileset-samling
        String filnavn = cbTileSamling.getValue();
        if(filnavn == null || filnavn.equals("Velg tileset")){
            lTileInfo.setText("Velg en tileset-samling");
            return;
        }
        System.out.println("Henter inn tilesetet fra: " + filnavn);
        tileset = new RuteSamling();
        int teller = 0;
        // vi sjekker hvor mange tils det er i samlingen
        try{
            Scanner scanner = new Scanner(new File(LENKE_TILESET + filnavn + ".txt"));

            while(scanner.hasNextLine()){
                //skal gå igjennom hele filen
                String linje = scanner.nextLine();
                String datTab[] = linje.split(":");
                int id = Integer.parseInt(datTab[0]);
                linje = scanner.nextLine();
                Rute.RuteType type = Rute.RuteType.valueOf(linje);
                // nå har vi hentet all info mangler bare hvordan ruta ser ut
                Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                int y = 0;
                while (scanner.hasNextLine()) {
                    linje = scanner.nextLine();
                    if (linje.equals(":")) {
                        break;
                    }
                    String dat[] = linje.split(";");
                    System.out.println(linje);
                    //hver linje er x aksen
                    for(int i = 0; i < dat.length; i++){
                        //hver linje er y aksen (teller)
                        utseende[i][y] = new Rectangle(ruteStr - 1, ruteStr - 1);
                        utseende[i][y].setFill(Color.valueOf(dat[i]));
                    }
                    y++;
                    // hvis y er lik pxPerRute, må vi gå ut av siste whilen
                    if (y == pxPerRute) {
                        break;
                    }

                }
                teller++;
                String test = scanner.nextLine();
                System.out.println(test);

            }
            scanner.close();

        } catch (Exception e){
            System.out.println("Klarte ikke å hente tilesetet");
            return;
        }
        System.out.println("Det er " + teller + " ruter");
        // vi legger inn alle rutene i samlingen
        for(int i = 0; i < teller; i++){
           tileset.leggTil( leggInnEnRute(i, filnavn) );
           System.out.println("Lagt til rute nr: " + i);
        }

        System.out.println("Funker frem til hit");
        oppdaterTilepalett();

        System.out.println("Hentet tilesetet");
    }
    public Rute leggInnEnRute(int ruteId, String filnavn) {
        // metode for å hente en rute fra en rutesamling
        try {
            Scanner scanner = new Scanner(new File(LENKE_TILESET + filnavn + ".txt"));
            //sjekker hvor id ligger i samlingsfilen
            int id = 0;
            String linje = "";
            while (scanner.hasNextLine()) {
                linje = scanner.nextLine();
                //System.out.println("Linje fra leggInnEnRute(): " + linje);
                String[] datTab = linje.split(":");
                id = Integer.parseInt(datTab[0]);
                if (id == ruteId) {
                    System.out.println("Fant ruten: " + ruteId);
                    // funnet riktig plasering i tekstfil
                    break;
                }
                // hvis ikke id er funnet, leter vi til neste gang en id kommer i tekstfil
                // det er 1 rad med type info
                linje = scanner.nextLine();
                //System.out.println(linje);
                // det er 16 linjer med utseende info
                for (int i = 0; i < 16; i++) {
                    linje = scanner.nextLine();
                    //System.out.println(linje);
                }
                // det er en linje med : for å dele opp objektene
                linje = scanner.nextLine();
                //System.out.println(linje);

            }

            // hvis det ikke finnes en linje til må vi ut av metoden
            if (!scanner.hasNextLine()) {
                scanner.close();
                System.out.println("Fant ikke ruten: " + ruteId);
                return null;
            }


            // neste linje er rute type

            linje = scanner.nextLine();
            Rute.RuteType type = Rute.RuteType.valueOf(linje);
            Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
            int teller = 0;
            // neste linje etter type er info om utseende
            while(scanner.hasNextLine()) {
                linje = scanner.nextLine();
                String[] datTab = linje.split(";");

                //en linje her er en rad med utseende (x)
                for(int i = 0; i < datTab.length; i++) {
                    utseende[i][teller] = new Rectangle();
                    utseende[i][teller].setFill(Color.valueOf(datTab[i]));
                }
                // teller er y verdien i 2dim tab
                teller++;

                // når vi har fylt opp hele utseende, må vi ut av while-løkken
                if(teller >= pxPerRute){
                    break;
                }
            }
            scanner.close();
            System.out.println("Rute legges inn i tileset, ruteId: " + ruteId);
            return new Rute(ruteId, type, new Rectangle(), utseende, tileRuteStr);

        } catch (Exception e) {
            System.out.println("Klarte ikke å hente rute: " + ruteId);
            return null;
        }


    }

    public void lagreRute(){
        System.out.println("Prøver å lagre rute");
        //sjekker om bruker har valgt en tileset-samling
        String filnavn = cbTileSamling.getValue();
        // vet ikke om jeg må ha eller funksjonen her, men gjør det for å være sikker
        if(filnavn == null || filnavn.equals("Velg tileset")){
            lTileInfo.setText("Velg en tileset-samling");
            return;
        }
        if(cbTileType.getValue() == null){
            lTileInfo.setText("Velg en rute type");
            System.out.println("Velg en rute type");
            return;
        }
        lTileInfo.setText("");

        //lagrer rute info på den nye måten
        Rectangle placeholder = new Rectangle();
        placeholder.setFill(Color.TRANSPARENT);
        // returnerer str på Rute Arraylist i RuteSamling
        int idNr = tileset.hentSamlingStr();
        System.out.println("idNr = " + idNr);
        Rute.RuteType type = Rute.RuteType.valueOf(cbTileType.getValue());

        //vi kopierer det malte tilen til en annen tabell
        Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
        for(int i = 0; i < utseende.length; i++){
            for(int j = 0; j < utseende[i].length; j++){
                utseende[i][j] = new Rectangle();
                utseende[i][j].setFill(tile[i][j].getFill());
            }
        }

        Rute nyRute = new Rute(idNr, type, placeholder, utseende, tileRuteStr);
        skrivRuteTilTileFil(nyRute, filnavn);
        System.out.println("Lagret rute");
        // vi legger ny rute inn i tileset
        tileset.leggTil(nyRute);
        oppdaterTilepalett();
        System.out.println("Ruten ble lagret i tileset");

    }
    public void skrivRuteTilTileFil(Rute rute, String filnavn){
        //metode som skriver rute info til tilefilen
        System.out.println("prøver å lagre en rute i metoden skrivRuteTilTileFil:");
        Rectangle[][] utseende = rute.getUtseende();
        System.out.println("krasj her?");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(LENKE_TILESET + filnavn + ".txt", true));
            writer.print(rute.getRuteId());
            System.out.print(rute.getRuteId());
            writer.println(":");
            System.out.println(":");
            writer.println(rute.getType().toString());
            System.out.println(rute.getType().toString());

            for(int i = 0; i < pxPerRute; i++){
                for(int j = 0; j < pxPerRute; j++){
                    writer.print(utseende[i][j].getFill().toString());
                    System.out.print( utseende[i][j].getFill().toString() );
                    if(j < pxPerRute - 1){
                        writer.print(";");
                        System.out.print(";");
                    }
                }
                System.out.println();
                writer.println();
            }
            writer.println(":");
            System.out.println(":");
            writer.close();
            System.out.println("Ruten ble lagret i tilefilen: " + filnavn);
        }catch(Exception e){
            System.out.println("Klarte ikke å lagre ruten");
            return;
        }
    }

    public void ruteNett(){
        if(chRuteNett.isSelected()){
            gridPane.setVisible(true);
            //gridPane.setGridLinesVisible(true);
        }else{
            gridPane.setVisible(false);
            //gridPane.setGridLinesVisible(false);
        }
    }

    public void hentCBinfo(){
        //henter infor til comboboxsene i infopanelet

        // alle type ruter vi har
        alleTileTyper = new String[]{ "GULV", "VEGG", "DØR", "HJEM" };

        try{
            //henter infor til tileset comboboxsen
            Scanner sc = new Scanner(new File(LENKE_OVERSIKT + "tilesetSamling.txt"));
            int teller = 0;
            while(sc.hasNextLine()){
                String linje = sc.nextLine();
                teller++;
            }
            alleTileSamlinger = new String[teller];
            teller = 0;
            sc.close();
            sc = new Scanner(new File(LENKE_OVERSIKT + "tilesetSamling.txt"));
            while(sc.hasNextLine()){
                String linje = sc.nextLine();
                alleTileSamlinger[teller] = linje;
                teller++;
            }
            sc.close();

        }catch (Exception e){
            System.out.println("Feil ved henting av tilesetSamling");
            return;
        }


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
        //sjekker om farge allerede er i samlingen

        for(int i = 0; i < fargerSamling.size(); i++){
            if(fargerSamling.get(i).equals(leggTilfarge)){
                lFargeInfo.setText("Farge er allerede i samling!");
                return;
            }
        }
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
            str = palettRuteStr - 5;      // -5 for litt rom i pallet
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
