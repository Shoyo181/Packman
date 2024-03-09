package com.example.packman.Rute;

import java.util.ArrayList;

public class RuteSamling {
    private ArrayList<Rute> samling;

    public RuteSamling(){
        samling = new ArrayList<>();
    }

    public void leggTil(Rute r){
        samling.add(r);
    }
    public Rute getRute(int index){
        return (Rute) samling.get(index);
    }
    public Rute kopierFraRuteSamling(int index){
        return new Rute(samling.get(index).getRuteId(), samling.get(index).getTile(), samling.get(index).getType());
    }
    public int hentSamlingStr(){
        return samling.size();
    }
}
