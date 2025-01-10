/* Klassen skal opprette en løkke av spill til brukeren ikke lenger har flere liv igjen
 * hvis vi får det til skal den også økehastigheten etter man har spilt noen spill
 */

package com.example.packman;

import javafx.scene.layout.BorderPane;

public class GameLoop extends BorderPane {

    private int score;
    private int liv;
    private String filnavn;
    private int vinduStrX;
    private int vinduStrY;
    BanePane bane;
    public GameLoop(String filnavn, int vinduStrX, int vinduStrY) {
        this.filnavn = filnavn;
        this.vinduStrX = vinduStrX;
        this.vinduStrY = vinduStrY;

        // når en loop startes skal score være null og liv være 3
        score = 0;
        liv = 3;




    }





}
