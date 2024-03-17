package com.example.packman.Elementer;

import com.example.packman.Rute.Rute;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class Elementer {

    protected Rute [][] grid;
    protected int ruteStr, gridHøyde, gridBredde;

    public Elementer(Rute[][] grid) {
        this.grid = grid;
        this.ruteStr = hentRuteStr(grid[0][0].getTile());
        regnutHøydeBredde();
        //System.out.println("(Fra Elementer) Høyde: " + gridHøyde + ", Bredde: " + gridBredde);
    }
    public void regnutHøydeBredde() {
        this.gridHøyde = grid[0].length;       // y-akse
        this.gridBredde = grid.length;   // x-akse
    }

    private int hentRuteStr(Rectangle r) {
        return (int) r.getHeight();
    }




    // metoder som sjekker om hvordan type rute som er rundt elementet
    public Rute.RuteType getTypeOver(int x, int y) {
        return grid[x][y-1].getType();
    }
    public Rute.RuteType getTypeUnder(int x, int y) {
        return grid[x][y].getType();
    }
    public Rute.RuteType getTypeHøyre(int x, int y) {
        return grid[x][y].getType();
    }
    public Rute.RuteType getTypeVenstre(int x, int y) {
        return grid[x-1][y].getType();
    }
    public Rute.RuteType getTypeOverHøyre(int x, int y) {
        return grid[x+1][y-1].getType();
    }
    public Rute.RuteType getTypeOverVenstre(int x, int y) {
        return grid[x-1][y-1].getType();
    }
    public Rute.RuteType getTypeUnderVenstre(int x, int y) {
        return grid[x-1][y+1].getType();
    }
    public Rute.RuteType getTypeUnderHøyre(int x, int y) {
        return grid[x+1][y+1].getType();
    }


    // metoder som returnerer ruten som er rundt elementet
    // bruker x og y for å finne current rute
    public Rute getRuteOver(int x, int y) {
        return grid[x][y-1].getRute();
    }
    public Rute getRuteUnder(int x, int y) {
        return grid[x][y+1].getRute();
    }
    public Rute getRuteHøyre(int x, int y) {
        return grid[x+1][y].getRute();
    }
    public Rute getRuteVenstre(int x, int y) {
        return grid[x-1][y].getRute();
    }
    // finner diagonalen rundt elementet
    public Rute getRuteOverHøyre(int x, int y) {
        return grid[x+1][y-1].getRute();
    }
    public Rute getRuteOverVenstre(int x, int y) {
        return grid[x-1][y-1].getRute();
    }
    public Rute getRuteUnderVenstre(int x, int y) {
        return grid[x-1][y+1].getRute();
    }
    public Rute getRuteUnderHøyre(int x, int y) {
        return grid[x+1][y+1].getRute();
    }




}
