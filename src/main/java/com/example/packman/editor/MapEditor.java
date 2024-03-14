package com.example.packman.editor;

import com.example.packman.Rute.Rute;
import com.example.packman.Rute.RuteSamling;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private ComboBox<String> cbTilesetPallet;
    private String[] alleTilesetSamlinger;
    private Button btHentTileset;
    private Label lTilesetInfo;
    private RuteSamling tileset;
    private int palettRuteStr;
    private GridPane tilePalletPane;
    private HBox samletTilesetPallet;
    private Rute valgtRute;

    public MapEditor(int vinduStrX, int vinduStrY) {
        super(vinduStrX, vinduStrY);

        // regner ut rutestr til map editor
        regnUtRuteStrMap();
        byggViderePallet();

    }
    public void regnUtRuteStrMap() {
        //regner ut rutestr til map editor
        // pallet ruteStr har bare 150 px(for lite) og vi vil gå opp i 16 gangen (og litt mellomrom)

        palettRuteStr = (150 -10) / 16;
        palettRuteStr = (palettRuteStr * 16 )/ 3; // i tileknapper i pallet
        // blei feil, se på det senere
        palettRuteStr = 16*2;
        System.out.println("palettRuteStr: " + palettRuteStr);

    }

    /***               InfoPanel              ***/
    /* Metoder/klasse for bygging av info panel */
    public void byggVidereInfoPanel() {
        
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
                        Rectangle pixel = new Rectangle();
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

            setOnAction(e -> {
                valgtRute = this.rute;
                System.out.println("Ny valgt rute/tile sin id = " + rute.getRuteId());
            });

        }

    }

}
