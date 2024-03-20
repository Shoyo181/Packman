/**
 *
 */
package com.example.packman.editor;

import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class MapEditor extends Editor {

    private final String LENKE_OVERSIKT = "src/main/resources/com/example/packman/Oversikter/";
    private final String LENKE_TILESET = "src/main/resources/com/example/packman/tilesets/";
    private final String LENKE_BANER = "src/main/resources/com/example/packman/baner/";
    private final int FAKE_ID = 2024;
    private ComboBox<String> cbTilesetPallet;
    private String[] alleTilesetSamlinger;
    private Button btHentTileset, btLagBane, btLagreMap;
    private Label lTilesetInfo, lRuteTypeInfo;
    private RuteSamling tileset;
    private int palettRuteStr, valgtRuteId;
    private GridPane tilePalletPane;
    private HBox samletTilesetPallet;
    private Rute valgtRute;
    private Slider sliderX, sliderY;
    private TextField tfHøyde, tfBredde, tfMapNavn;
    private Rute[][] ruteMap;
    private GridPane rutePanel;

    public MapEditor(int vinduStrX, int vinduStrY) {
        super(vinduStrX, vinduStrY);

        // Regner ut rutestr til map editor.
        regnUtRuteStrMap();
        byggViderePallet();
        byggVidereInfoPanel();


    }
    /**
     * Metode for å regne ut rutestr til map editor.
     */
    public void regnUtRuteStrMap() {
        // palletRuteStr har bare 150 px(for lite) og vi vil gå opp i 16 gangen (og litt mellomrom).

        palettRuteStr = (150 -10) / 16;
        palettRuteStr = (palettRuteStr * 16 )/ 3; // I tileknapper i pallet
        // blei feil, se på det senere
        palettRuteStr = 16*3;
        System.out.println("palettRuteStr: " + palettRuteStr);

    }


    /***               Canvas              ***/
    /* Metoder for å lage canvas for mapedit */

    /**
     * Metode for å bygge videre på canvaset.
     */

    public void byggVidereCanvas() {
        // Før vi kan bygge canvas må vi regne ut hvor store rutene kan være.
        // Husk at målet vi ender opp med må kunne deles på 16.
        regnUtRuteStr();

        byggCanvas();

        // Ved hjelp av tidligere metoder har vi laget grid og tile.
        // Dette er rektangel i 2dim tabeller, men siden vi ikke skal male med farger og heller tiles,
        // må vi lage en ny 2dim tabell med ruter.

        // Tar bort tidligere canvas.
        midt.getChildren().clear();
        canvas.getChildren().clear();

        // Lager nytt panel som vi trenger for dette, og legger inn i canvas.
        byggRutePanel();
        //rutePanel.setAlignment(Pos.CENTER);
        //rutePanel.setStyle("-fx-background-color: white; -fx-padding: 20px;");
        canvas.getChildren().addAll(rutePanel, gridPane);

        //Legger til canvas i midten igjen, og setter midten i mainPane.
        midt.getChildren().add(canvas);
        setCenter(midt);

        // Når bruker trykker på gridPane så skal det males i rutePanel istedenfor
        gridPane.setOnMousePressed(e -> {
            tileClick(e);
        });
        gridPane.setOnMouseDragged(e -> {
            tileClick(e);
        });

        //Lager en bane med ruter
        System.out.println("Lag bane!!");
    }
    /**
     * Metode for å håndtere klikk på rute som er valgt.
     * @param e ser hvor ruten er klikket.
     */
    public void tileClick(MouseEvent e) {
        //valgtfarge = fargerSamling.get(0);

        System.out.println("Trykk");
        try{
            int x = (int) ((e.getX()) / (ruteStr));
            int y = (int) ((e.getY()) / (ruteStr));

            maling(x, y);
        } catch (Exception e2){
            System.out.println("Trykket utenfor canvas");
        }
    }
    /**
     * Metode for å farge rutene i canvas.
     */
    public void maling(int x, int y) {
        //Farger rute som er valgt.
        //Oppdaterer ruteMap så vi husker hvor ting er plassert.
        //System.out.println("lengden på utseende i tileset" + tileset.getRuteFraSamling(valgtRuteId).getUtseende().length);

        // Lager en ny rute med info fra tilesetet.
        Rectangle tile = new Rectangle(ruteStr, ruteStr);
        tile.setFill(Color.TRANSPARENT);

        //Henter utseende.
        Rectangle[][] utseendeSjekk = tileset.getRuteFraSamling(valgtRuteId).getUtseende();

        Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
        //Utseende.
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
        //System.out.println(ruteMap[x][y].getType().toString());
        //System.out.println(tileset.getRuteFraSamling(valgtRuteId).getType().toString());
        //System.out.println(tileset.hentSamlingStr());

        //System.out.println("lengden på utseende i tileset " +tileset.getRuteFraSamling(valgtRuteId).getUtseende().length);

        /*
        for(int i = 0; i < utseendeSjekk.length; i++) {
            for(int j = 0; j < utseendeSjekk[i].length; j++) {
                System.out.print(utseendeSjekk[i][j].getFill().toString());
                if (j < utseendeSjekk[i].length - 1) {
                    System.out.print(";");
                }
            }
            System.out.println();
        }*/


        //Legger til rute i rutePanel.
        System.out.println("valgtruteId: " + valgtRuteId + " -  x: " + x + " y: " + y);

        oppdaterRutePanel();
    }
    /**
     * Metode for å oppdatere rutePanel.
     * Fjerner først alle komponenter i rutePanel.
     */
    public void oppdaterRutePanel() {
        //Oppdaterer rutePanel.
        //Fjerner først alle komponenter i rutePanel.
        rutePanel.getChildren().clear();

        // Legger til oppdaterte ruter fra ruteMap i rutePanel.
        for (int i = 0; i < bredde; i++) {
            for (int j = 0; j < høyde; j++) {
                //System.out.println("bygger rutePanel, i: " + i + " j: " + j + ",type " + ruteMap[i][j].getType().toString());
                rutePanel.add(ruteMap[i][j].kopierRute(), i, j);
            }
        }
    }
    /**
     *  Metode for å lage Lage grid panel for map-editor.
     */
    public void byggRutePanel() {

        // Oppretter det vi trenger.
        System.out.println("bygger rutePanel");

        ruteMap = new Rute[bredde][høyde];
        rutePanel = new GridPane();

        // Når vi legger inn ruter i tabellen må den ha en verdi,
        // og den vil være null, altså id alt for høy slik at vi kan teste på den senere.

        // Fyller inn det som skal inn i grid pane.
        for(int i = 0; i < høyde; i++) {
            for(int j = 0; j <bredde; j++) {

                int id = FAKE_ID;
                Rectangle tile = new Rectangle(ruteStr, ruteStr, Color.TRANSPARENT);
                Rectangle[][] utseende = new Rectangle[pxPerRute][pxPerRute];
                Rute.RuteType type = Rute.RuteType.INGEN;

                // Lager ruter i ruteMap.
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
        // Bygger bane med ruter, og for nå så blir bare verdien
        // det som er i sliderene.
        System.out.println("Lag bane!!");
        System.out.println("X: " + (int) sliderX.getValue());
        System.out.println("Y: " + (int) sliderY.getValue());

        // Dobbeltsjekker om bruker har tastet inn tall.
        try{
            int x = (int) sliderX.getValue();
            int y = (int) sliderY.getValue();
            bredde = x;
            høyde = y;
            // Nå som vi har fått vite hvor mange ruter ruter vi skal ha (x*y), kan vi endelig bygge canvas.
            byggVidereCanvas();

        }catch (Exception e){
            System.out.println("Du har ikke tastet inn tall");
            return;
        }
    }


    /***               InfoPanel              ***/
    /* Metoder/klasse for bygging av info panel */

    /**
     * Metode for å bygge et infopanel.
     */
    public void byggVidereInfoPanel() {
        // Det vi trenger først i infoPanel er endten slidere eller to tekstbokser som bestemmer hvor stort canvas skal være,
        // og en knapp for å generere canvas, med disse målene.
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

        // Lager en knapp som setter opp banen med ruter.
        // Lager også teksbokser for x og y størrelse som bruker kan endre i hvis de vil.
        btLagBane = new Button("Lag bane");
        btLagBane.setOnAction(e -> lagCanvas());

        tfBredde = new TextField();
        tfBredde.setPrefColumnCount(3);
        tfHøyde = new TextField();
        tfHøyde.setPrefColumnCount(3);

        tfHøyde.setText((int) sliderX.getValue() + "");
        tfBredde.setText((int) sliderY.getValue() + "");

        // Legger til litt funksjonalitet.
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
            // Gjør noe med den nye teksten (newValue) her.
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
            // Gjør noe med den nye teksten (newValue) her.
            System.out.println("Ny tekst: " + newValue);
            try{
                if((Double.parseDouble(newValue) > 10.0) && (Double.parseDouble(newValue) < 50.0)){
                    sliderY.setValue(Double.parseDouble(tfHøyde.getText()));
                }
            }catch (Exception e){
                System.out.println("Ikke et tall");
            }
        });
        // Med denne funksjonaliteten vil tekstbokser og slider "snakke" sammen.

        // Legger disse txt boksene og knappen inn i egen HBox.
        HBox baneInfo = new HBox();
        baneInfo.getChildren().addAll(tfBredde, tfHøyde, btLagBane);

        // Legger alle komponenter inn i info panelet.
        info.getChildren().addAll(sliderX, sliderY, baneInfo);

        // Vi trenger også en knapp for å lagre det man har gjort med kartet,
        // og en tesktboks for å skrive inn navnet til kartet.
        btLagreMap = new Button("Lagre kart");
        btLagreMap.setOnAction(e -> lagreMap());

        tfMapNavn = new TextField();
        tfMapNavn.setPrefColumnCount(10);

        // Legger disse sammen i en HBox.
        HBox mapInfoPanel = new HBox();
        mapInfoPanel.getChildren().addAll(tfMapNavn, btLagreMap);

        info.getChildren().add(mapInfoPanel);

        // Når bruker har valgt en rute vil vi kunne vise frem hvordan type rute det er.

        lRuteTypeInfo = new Label();
        lRuteTypeInfo.setFont(new Font(20)); // Setter skriftstørrelsen til 20
        lRuteTypeInfo.setTextFill(Color.LIGHTGRAY);
        info.getChildren().add(lRuteTypeInfo);

        // Skal vi ha en funksjon for å hente tidligere baner også?

    }
    /**
     * Metode for å Lagre baner.
     */
    public void lagreMap() {
        System.out.println("Prøver å lagre bane");
        // Sjekker først om det kartet har et navn.
        String filnavn = tfMapNavn.getText();

        if (filnavn.equals("")) {
            System.out.println("Du ma først skrive inn navnet til mappet din");

            return;
        }
        try{
            // Åpner datastrøm.
            PrintWriter writer = new PrintWriter(LENKE_BANER + filnavn + ".txt");

            // Behandler datastrøm.
            // I filen skal det først stå hvor stor banen er bredde, så høyde, så skal det stå hvilket tileset den bruker.
            // Skiller med de ulike tingene i banen med ";"
            writer.println(bredde + ";" + høyde + ";" + cbTilesetPallet.getValue());
            System.out.println(bredde + ";" + høyde + ";" + cbTilesetPallet.getValue());

            // Deretter skriver vi inn banen fra ruteMap.
            for(int y = 0; y < høyde; y++){
                for(int x = 0; x < bredde; x++){
                    //vi siller hver dute id med ";"
                    writer.print(ruteMap[x][y].getRuteId());
                    System.out.print(ruteMap[x][y].getRuteId());
                    if( x < bredde -1){
                        writer.print(";");
                        System.out.print(";");
                    }
                }
                if(y < høyde -1){
                    writer.println();
                    System.out.println();
                }
            }
            // Lukker datastrøm.
            writer.close();

            System.out.println("Mappet har blitt lagret");

        }catch (Exception e){
            System.out.println("Karte ikke å lagre mappet");
            return;
        }
    }



    /***              Pallet              ***/
    /* Metoder/klasse for bygging av pallet */

    /**
     * Metode for å hente tilsets.
     */
    public void byggViderePallet() {
        // Palletet til MapEditor handler om å vise frem tilsets, ikke farger.
        // Derfor trenger vi først en combobox som viser frem de tilesetsene som er lagret,  også henter vi de senere.

        // Vi henter info ComboBox trenger.
        hentTilsetsSamlingOversikt();
        // Vi lager ComboBox og gir den de funksjonene vi ønsker der.
        cbTilesetPallet = new ComboBox<>();
        cbTilesetPallet.getItems().addAll(alleTilesetSamlinger);
        cbTilesetPallet.setPromptText("Velg tileset");
        // Vi lager knapp for å hente tilsets.
        btHentTileset = new Button("Hent");
        btHentTileset.setOnAction(e -> hentTileset());
        //Llegger knapp og legger ComboBox inn i et pane.
        samletTilesetPallet = new HBox();
        samletTilesetPallet.getChildren().addAll(cbTilesetPallet, btHentTileset);


        // Setter opp en info label hvis bruker gjør feil.
        lTilesetInfo = new Label();

        // Når bruken har hentet tilset, må vi også vise det frem, vi viser det frem i form av knapper i en GridPane.
        tilePalletPane = new GridPane();
        tilePalletPane.setHgap(2);
        tilePalletPane.setVgap(2);
        tilePalletPane.setPadding(new javafx.geometry.Insets(3, 3, 3, 3));

        pallet.getChildren().addAll(samletTilesetPallet,lTilesetInfo, tilePalletPane);
    }

    /**
     * Metode for å hente tilsets.
     */

    public void hentTileset() {
        // Her henter vi valgt tileset og legger det inn i en RuteSamling (tileset).
        // Sjekker først om det er lovlig verdi i ComboBox.
        String valgtTileset = cbTilesetPallet.getValue();
        if(valgtTileset == null || valgtTileset.equals("Velg tileset")) {
            lTilesetInfo.setText("Velg et tileset");
            System.out.println("Velg et tileset");
            return;
        }
        lTilesetInfo.setText("");
        // Lager ny rutesamling.
        System.out.println("Henter tilset fra: " + valgtTileset);
        tileset = new RuteSamling();
        System.out.println("Opprettet nytt tileset");
        // Henter verdi å legge inn i RuteSamling.
        try{
            Scanner scanner = new Scanner(new File(LENKE_TILESET + valgtTileset + ".txt"));
            //System.out.println("Leter inn fra fil");
            while(scanner.hasNextLine()) {
                //Id.
                String linje = scanner.nextLine();
                //System.out.println(linje);
                String datTab[] = linje.split(":");
                int id = Integer.parseInt(datTab[0]);
                // Type.
                linje = scanner.nextLine();
                //System.out.println(linje);
                Rute.RuteType type = Rute.RuteType.valueOf(linje);
                // Design til ruten.
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
                // Legger inn til RuteSamling.
                tileset.leggTil( new Rute(id, type, tile, utseende, palettRuteStr));
                //System.out.println("tileset er lagt til i samling");
                // Nå har vi hentet et helt rute objekt,
                // men leseren er fortsatt på feil plass.
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
    /**
     * Metode for å oppdatere tilepalletet.
     */

    public void oppdaterTilePallet() {
        // Oppdaterer tilepalletet.
        tilePalletPane.getChildren().clear();
        int tellerKol = 0, tellerRad = 0;
        // Henter den informasjonen vi trenger fra tileset.
        for(int i = 0; i < tileset.hentSamlingStr(); i++) {
            if(tellerKol == 3) {
                tellerKol = 0;
                tellerRad++;
            }
            tilePalletPane.add( new PalletKnapp( tileset.getRuteFraSamling(i) ), tellerKol, tellerRad);
            tellerKol++;
        }
    }

    /**
     * Metode for å hente tilsets-samling, og lagrer det i en String-array,
     * slik at de senere kan vises frem.
     */
    public void hentTilsetsSamlingOversikt() {
        try {
            // Teller først hvor mange tilsets vi har i oversikten.
            Scanner scanner = new Scanner(new File(LENKE_OVERSIKT + "tilesetSamling.txt"));
            int teller = 0;
            while (scanner.hasNextLine()) {
                String linje = scanner.nextLine();
                teller++;
            }
            scanner.close();
            // Lager en String-array med alle tilsets.
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
    /**
     * Metode for å få frem knappene i palettet.
     */
    private class PalletKnapp extends Button {
        private Rute rute;
        public PalletKnapp(Rute rute) {
            this.rute = rute;
            setGraphic(rute.getRute());
            setPadding(new Insets(0));

            setOnAction(e -> {
                // valgtRute = this.rute;
                valgtRuteId = this.rute.getRuteId();
                // Viser hvordan type ruten er til bruker.
                lRuteTypeInfo.setText("");
                lRuteTypeInfo.setText("RuteType = " + this.rute.getType().toString());
                System.out.println("Ny valgt rute/tile sin id = " + rute.getRuteId());
            });

        }

    }

}
