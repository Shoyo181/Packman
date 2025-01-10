/**
 * Klasse for IkkeLevende. Altså de tingene som ikke skal bevege seg på kartet. 
 */
package com.example.packman.Elementer.IkkeLevende;

import com.example.packman.Elementer.Elementer;
import com.example.packman.Rute.Rute;
import com.example.packman.misc.Vector2D;

public class IkkeLevende extends Elementer {

    Vector2D spawnPoint;

    public IkkeLevende(Rute[][] grid, Vector2D spawnPoint) {
        super(grid);
        this.spawnPoint = spawnPoint;
    }
}
