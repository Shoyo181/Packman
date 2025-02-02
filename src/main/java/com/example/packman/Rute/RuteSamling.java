/**
 * RuteSamling klasse. 
 * En liste med forskjellige typer. 
 * Brukes for å samle tilesets, tilsets er en samling av
 * forskjellige ruter som brukes til baner og til editor. 
 */
package com.example.packman.Rute;

import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class RuteSamling {
    private ArrayList<Rute> samling;

    public RuteSamling(){
        samling = new ArrayList<>();
    }

    public void leggTil(Rute r){
        samling.add(r);
    }
    public Rute getRuteFraSamling(int index){
        return (Rute) samling.get(index);
    }
    public Rute kopierFraRuteSamling(int index){
        return new Rute(samling.get(index).getRuteId(), samling.get(index).getType(), samling.get(index).getTile());
    }
    public Rute kopierFraRuteSamlingStack(int index){
        return new Rute(samling.get(index).getRuteId(), samling.get(index).getType(), samling.get(index).getTile(), samling.get(index).getUtseende(), samling.get(index).getRuteStr());
    }
    public GridPane getUtseendePanelFraSamling(int index){
        return samling.get(index).getUtseendePanel();
    }
    //public GridPane kopierUtseendePanelFraSamling(int index){
        //return new Rute(samling.get(index).getRuteId(), samling.get(index).getType(), samling.get(index).getTile(),samling.get(index).getTile().getUtseendePanel());
    //}
    public int hentSamlingStr(){
        return samling.size();
    }
}
