package com.example.packman.editor;

import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.Scanner;

public class MapEditor extends Editor {

    private final String LENKE_OVERSIKT = "src/main/resources/com/example/packman/Oversikter/";
    private final String LENKE_TILESET = "src/main/resources/com/example/packman/tilesets/";
    private final int FAKE_ID = 2024;
    private ComboBox<String> cbTilesetPallet;
    private String[] alleTilesetSamlinger;
    private Button btHentTileset, btLagBane;
    private Label lTilesetInfo;
    private RuteSamling tileset;
    private int palettRuteStr, valgtRuteId;
    private GridPane tilePalletPane;
    private HBox samletTilesetPallet;
    private Rute valgtRute;
    private Slider sliderX, sliderY;
    private TextField tfHøyde, tfBredde;
    private Rute[][] ruteMap;
    private GridPane rutePanel;

    //test

    public MapEditor(int vinduStrX, int vinduStrY) {
        super(vinduStrX, vinduStrY);

        // regner ut rutestr til map editor
        regnUtRuteStrMap();
        byggViderePallet();
        byggVidereInfoPanel();

    }
    public void regnUtRuteStrMap() {
        //regner ut rutestr til map editor
        // pallet ruteStr har bare 150 px(for lite) og vi vil gå opp i 16 gangen (og litt mellomrom)

        palettRuteStr = (150 -10) / 16;
        palettRuteStr = (palettRuteStr * 16 )/ 3; // i tileknapper i pallet
        // blei feil, se på det senere
        palettRuteStr = 16*3;
        System.out.println("palettRuteStr: " + palettRuteStr);

    }


    /***               Canvas              ***/
    /* Metoder for å lage canvas for mapedit */

    public void byggVidereCanvas() {
        // før vi kan bygge canvas må vi regne ut hvor store rutene kan være

        //husk at målet vi ender opp med må kunne deles på 16
        regnUtRuteStr();

        byggCanvas();

        // vedhjelp av tidliegere metoder har vi laget grid og tile, dette er rektangel 2dim tabeller
        // men siden vi ikke skal male med carger og heller tiles, må vi lage en ny 2dim tabell med ruter
        // tar bort tidligere canvas
        midt.getChildren().clear();
        canvas.getChildren().clear();

        // lager nytt panel som vi trenger for denne jobben, og legger inn i canvas
        byggRutePanel();
        canvas.getChildren().addAll(rutePanel, gridPane);

        //legger til canvas i midten igjen, og setter midten i mainPane
        midt.getChildren().add(canvas);
        setCenter(midt);

        // når bruker trykker på gridPane så skal det males i rutePanel istedenfor
        gridPane.setOnMousePressed(e -> {
            tileClick(e);
        });

        //lager en bane med ruter
        System.out.println("Lag bane!!");
    }
    public void tileClick(MouseEvent e) {
        //valgtfarge = fargerSamling.get(0);
        System.out.println("Trykk");
        // +2 siden borderMemoryWarning
        int x = (int) ((e.getX()) / (ruteStr));
        int y = (int) ((e.getY()) / (ruteStr));

        maling(x, y);
    }
    public void maling(int x, int y) {
        //farger valgt rute
        //oppdaterer ruteMap så vi husker hvor ting er plassert
        //System.out.println("lengden på utseende i tileset" + tileset.getRuteFraSamling(valgtRuteId).getUtseende().length);

        // lager en ny rute med info fra tileset
        //tile
        Rectangle tile = new Rectangle(ruteStr, ruteStr);
        tile.setFill(Color.TRANSPARENT);

        //henter utseende
        Rectangle[][] utseendeSjekk = tileset.getRuteFraSamling(valgtRuteId).getUtseende();

        Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
        //utseende
        for(int i = 0; i < utseende.length; i++) {
            for(int j = 0; j < utseende[i].length; j++) {
                utseende[i][j] = new Rectangle(ruteStr/16, ruteStr/16);
                utseende[i][j].setFill(utseendeSjekk[i][j].getFill());
            }
        }

        int id = tileset.getRuteFraSamling(valgtRuteId).getRuteId();
        Rute.RuteType type = tileset.getRuteFraSamling(valgtRuteId).getType();

        Rute nyRute = new Rute(id, type, tile, utseende, ruteStr);

        ruteMap[x][y] = nyRute;
        System.out.println(ruteMap[x][y].getType().toString());
        System.out.println(tileset.getRuteFraSamling(valgtRuteId).getType().toString());
        System.out.println(tileset.hentSamlingStr());

        System.out.println("lengden på utseende i tileset " +tileset.getRuteFraSamling(valgtRuteId).getUtseende().length);


        for(int i = 0; i < utseendeSjekk.length; i++) {
            for(int j = 0; j < utseendeSjekk[i].length; j++) {
                System.out.print(utseendeSjekk[i][j].getFill().toString());
                if (j < utseendeSjekk[i].length - 1) {
                    System.out.print(";");
                }
            }
            System.out.println();
        }


        //legger til rute i rutePanel
        System.out.println("valgtruteId: " + valgtRuteId + " -  x: " + x + " y: " + y);

        oppdaterRutePanel();
    }
    public void oppdaterRutePanel() {
        //oppdaterer rutePanel
        //fjerner først alle komponenter i rutePanel
        rutePanel.getChildren().clear();

        // Legger til oppdaterte ruter fra ruteMap i rutePanel
        for (int i = 0; i < bredde; i++) {
            for (int j = 0; j < høyde; j++) {
                System.out.println("bygger rutePanel, i: " + i + " j: " + j + ",type " + ruteMap[i][j].getType().toString());
                rutePanel.add(ruteMap[i][j].kopierRute(), i, j);
            }
        }
    }

    public void byggRutePanel() {
        // lager grid panel for map-editor
        // oppretter det vi trenge
        System.out.println("bygger rutePanel");

        ruteMap = new Rute[bredde][høyde];
        rutePanel = new GridPane();

        // når vi legger inn ruter i tabellen må den ha en verdi, og den vil være null altså id alt for høy slik at vi kan teste på den senere


        // fyller inn det som skal inn i grid pane
        for(int i = 0; i < høyde; i++) {
            for(int j = 0; j <bredde; j++) {

                int id = FAKE_ID;
                Rectangle tile = new Rectangle(ruteStr, ruteStr, Color.TRANSPARENT);
                Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                Rute.RuteType type = Rute.RuteType.INGEN;

                // lager ruter i ruteMap
                for(int o = 0; o < pxPerRute; o++) {
                    for(int l = 0; l < pxPerRute; l++) {
                        utseende[o][l] = new Rectangle(ruteStr/16, ruteStr/16, Color.TRANSPARENT);
                    }
                }


                ruteMap[j][i] = new Rute(id, type, tile, utseende, ruteStr);
                rutePanel.add(ruteMap[j][i].getRute(), j, i);
            }
        }
        System.out.println("bygget rutePanel");
    }

    public void lagCanvas() {
        //lager en bane med ruter
        // for nå bare gir verdien som er i sliderene
        System.out.println("Lag bane!!");
        System.out.println("X: " + (int) sliderX.getValue());
        System.out.println("Y: " + (int) sliderY.getValue());

        //dobbeltsjekker om at bruker har tastet inn tall
        try{
            int x = (int) sliderX.getValue();
            int y = (int) sliderY.getValue();
            bredde = x;
            høyde = y;
            // nå som vi har fått vite hvor mange ruter ruter vi skal ha (x*y), kan vi endelig bygge canvas
            byggVidereCanvas();

        }catch (Exception e){
            System.out.println("Du har ikke tastet inn tall");
            return;
        }
    }


    /***               InfoPanel              ***/
    /* Metoder/klasse for bygging av info panel */
    public void byggVidereInfoPanel() {
        // det vi trenger først i infoPanel er endten slidere eller to tekstbokser som bestemmer hvor stort canvas skal være
        // og en knapp for å genirere canvas, med disse målene
        sliderX = new Slider();
        sliderX.setMin(10);
        sliderX.setMax(50);
        sliderX.setValue(10);
        sliderX.setMajorTickUnit(10);
        sliderX.setMinorTickCount(1);
        sliderX.setShowTickLabels(true);
        sliderX.setShowTickMarks(true);
        //sliderX.setSnapToTicks(true);

        sliderY = new Slider();
        sliderY.setMin(10);
        sliderY.setMax(50);
        sliderY.setValue(10);
        sliderY.setMajorTickUnit(10);
        sliderY.setMinorTickCount(1);
        sliderY.setShowTickLabels(true);
        sliderY.setShowTickMarks(true);

        //lager en knapp som setter opp banen med ruter
        //lager også teksbokser for x og y størrelse som bruker kan endre i hvis de vil
        btLagBane = new Button("Lag bane");
        btLagBane.setOnAction(e -> lagCanvas());

        tfBredde = new TextField();
        tfBredde.setPrefColumnCount(3);
        tfHøyde = new TextField();
        tfHøyde.setPrefColumnCount(3);

        tfHøyde.setText((int) sliderX.getValue() + "");
        tfBredde.setText((int) sliderY.getValue() + "");

        //legger til litt funksjonalitet
        sliderX.valueChangingProperty().addListener((obs, oldValue, isChanging) -> {
            if (!isChanging) {
                double finalValue = sliderX.getValue();
                System.out.println("Endelig verdi: " + finalValue);
                tfBredde.setText((int) sliderX.getValue() + "");
            }
        });
        sliderY.valueChangingProperty().addListener((obs, oldValue, isChanging) -> {
            if (!isChanging) {
                double finalValue = sliderY.getValue();
                System.out.println("Endelig verdi: " + finalValue);
                tfHøyde.setText((int) sliderY.getValue() + "");
            }
        });
        tfBredde.textProperty().addListener((observable, oldValue, newValue) -> {
            // Gjør noe med den nye teksten (newValue) her
            System.out.println("Ny tekst: " + newValue);
            try{
                if((Double.parseDouble(newValue) > 10.0) && (Double.parseDouble(newValue) < 50.0)){
                    sliderX.setValue(Double.parseDouble(tfBredde.getText()));
                }
            }catch (Exception e){
                System.out.println("Ikke et tall");
            }
        });
        tfHøyde.textProperty().addListener((observable, oldValue, newValue) -> {
            // Gjør noe med den nye teksten (newValue) her
            System.out.println("Ny tekst: " + newValue);
            try{
                if((Double.parseDouble(newValue) > 10.0) && (Double.parseDouble(newValue) < 50.0)){
                    sliderY.setValue(Double.parseDouble(tfHøyde.getText()));
                }
            }catch (Exception e){
                System.out.println("Ikke et tall");
            }
        });
        // med denne funksjonaliteten vil tekstbokser og slider "snakke" sammen

        // legger disse txt boksene og knappen inn i egen hbox
        HBox baneInfo = new HBox();
        baneInfo.getChildren().addAll(tfBredde, tfHøyde, btLagBane);

        //legger alle komponenter inn i info panel
        info.getChildren().addAll(sliderX, sliderY, baneInfo);


        // vi trenger også en knapp for å lagre det man har gjort med kartet

        //skal vi ha en funksjon for å hente tidligere baner også?

    }



    /***              Pallet              ***/
    /* Metoder/klasse for bygging av pallet */
    public void byggViderePallet() {
        // palletet til map editor handler om å vise frem tilsets ikke farger
        // derfor trenger vi først en combobox som viser frem de tilesetsene vi har også henter vi de senere

        //vi henter info combobox trenger
        hentTilsetsSamlingOversikt();
        // vi lager comboboxen og gir den de funksjonene vi vil
        cbTilesetPallet = new ComboBox<>();
        cbTilesetPallet.getItems().addAll(alleTilesetSamlinger);
        cbTilesetPallet.setPromptText("Velg tileset");
        // vi lager knapp for å hente tilsets
        btHentTileset = new Button("Hent");
        btHentTileset.setOnAction(e -> hentTileset());
        //legger knapp og comboboxen inn i et pane
        samletTilesetPallet = new HBox();
        samletTilesetPallet.getChildren().addAll(cbTilesetPallet, btHentTileset);


        // setter opp en info label hvis bruker gjør noe feil
        lTilesetInfo = new Label();

        // når bruke har hentet tilset, må vi også vise det frem, vi viser det frem i form av knapper i en gridpane
        tilePalletPane = new GridPane();
        tilePalletPane.setHgap(2);
        tilePalletPane.setVgap(2);
        tilePalletPane.setPadding(new javafx.geometry.Insets(3, 3, 3, 3));

        pallet.getChildren().addAll(samletTilesetPallet,lTilesetInfo, tilePalletPane);
    }
    public void hentTileset() {
        // her henter vi valgt tilset of legger det inn i en Rutesamling (tileset)
        //sjekker først om det er lovelig verdi i combobox
        String valgtTileset = cbTilesetPallet.getValue();
        if(valgtTileset == null || valgtTileset.equals("Velg tileset")) {
            lTilesetInfo.setText("Velg en tileset");
            System.out.println("Velg en tileset");
            return;
        }
        lTilesetInfo.setText("");
        // lager ny rutesamling
        System.out.println("Henter tilset fra: " + valgtTileset);
        tileset = new RuteSamling();
        System.out.println("opprettet nytt tileset");
        // henter verdi å legge inn i rutesamling
        try{
            Scanner scanner = new Scanner(new File(LENKE_TILESET + valgtTileset + ".txt"));
            //System.out.println("Leter inn fra fil");
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
                Rectangle tile = new Rectangle();
                tile.setFill(Color.TRANSPARENT);
                //System.out.println("all info er hentet ");
                // legger inn til rutesamling
                tileset.leggTil( new Rute(id, type, tile, utseende, palettRuteStr));
                //System.out.println("tileset er lagt til i samling");
                // nå har vi hentet et helt rute objekt
                // men leseren er fortsatt på feil plass
                linje =scanner.nextLine();
                //System.out.println("linje i tilesethenting, skal være ':', og er - " + linje);
            }
            scanner.close();

            System.out.println("Vellykket henting hele tileset");

            oppdaterTilePallet();


        }catch (Exception e) {
            System.out.println("Klarte ikke å hente tileset");
        }

    }
    public void oppdaterTilePallet() {
        // oppdaterer tilepalletet
        tilePalletPane.getChildren().clear();
        int tellerKol = 0, tellerRad = 0;
        // henter den informasjonen vi trenger fra tileset
        for(int i = 0; i < tileset.hentSamlingStr(); i++) {
            if(tellerKol == 3) {
                tellerKol = 0;
                tellerRad++;
            }
            tilePalletPane.add( new PalletKnapp( tileset.getRuteFraSamling(i) ), tellerKol, tellerRad);
            tellerKol++;
        }
    }
    public void hentTilsetsSamlingOversikt() {
        // denne metoden henter alle tilsets slik at vi kan senere vis de frem
        // lagrer det i en String-array
        try {
            //teller først hvor mange tilsets vi har i oversikten
            Scanner scanner = new Scanner(new File(LENKE_OVERSIKT + "tilesetSamling.txt"));
            int teller = 0;
            while (scanner.hasNextLine()) {
                String linje = scanner.nextLine();
                teller++;
            }
            scanner.close();
            //lager en String-array med alle tilsets
            alleTilesetSamlinger = new String[teller];
            teller = 0;
            scanner = new Scanner(new File(LENKE_OVERSIKT + "tilesetSamling.txt"));
            while (scanner.hasNextLine()) {
                String linje = scanner.nextLine();
                alleTilesetSamlinger[teller] = linje;
                teller++;
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("Klate ikke å hente tilsets-samling");
        }

    }

    // vi bruker en klasse for å få frem knappene i palleten
    private class PalletKnapp extends Button {
        private Rute rute;
        public PalletKnapp(Rute rute) {
            this.rute = rute;
            setGraphic(rute.getRute());
            setPadding(new Insets(0));

            setOnAction(e -> {
                //valgtRute = this.rute;
                valgtRuteId = this.rute.getRuteId();
                System.out.println("Ny valgt rute/tile sin id = " + rute.getRuteId());
            });

        }

    }

}
