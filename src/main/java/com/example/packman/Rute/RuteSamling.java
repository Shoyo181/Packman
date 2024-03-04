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
}
