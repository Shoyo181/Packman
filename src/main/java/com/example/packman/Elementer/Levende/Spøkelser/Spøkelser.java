package com.example.packman.Elementer.Levende.Spøkelser;

import com.example.packman.Elementer.Levende.Levende;
import com.example.packman.Rute.Rute;

public class Spøkelser extends Levende {
    protected SpøkelsesType type;

    protected SpøkelsesModus modus;





    public Spøkelser( SpøkelsesType type,  Rute[][] grid) {
        super( grid);
        this.type = type;
    }
}



