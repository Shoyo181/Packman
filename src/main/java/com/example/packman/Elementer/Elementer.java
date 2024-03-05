package com.example.packman.Elementer;

import com.example.packman.Rute.Rute;

public class Elementer {

    protected Rute [][] grid;

    public Elementer(Rute[][] grid) {
        this.grid = grid;
    }



    // metoder som sjekker om hvordan type rute som er rundt elementet
    public Rute.RuteType getTypeOver(int x, int y) {
        return grid[x][y-1].getType();
    }
    public Rute.RuteType getTypeUnder(int x, int y) {
        return grid[x][y+1].getType();
    }
    public Rute.RuteType getTypeHøyre(int x, int y) {
        return grid[x+1][y].getType();
    }
    public Rute.RuteType getTypeVenstre(int x, int y) {
        return grid[x-1][y].getType();
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
