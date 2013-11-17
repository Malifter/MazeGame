package game;

/*
* Classname:            Exterior.java
*
* Version information:  1.0
*
* Date:                 11/12/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import java.util.ArrayList;

import engine.Vertex2;

public class Exterior extends Room{
    public static final int HEIGHT = 528;
    public static final int WIDTH = 720;
    private ArrayList<Vertex2> playerSpawns = new ArrayList<Vertex2>(); // for now make these the safe zones too possibly
    
    public Exterior(int layout) {
        super(layout);
    }
    
    public void addPlayerSpawn(Vertex2 location) {
        playerSpawns.add(location);
    }
    
    public ArrayList<Vertex2> getPlayerSpawns() {
        return playerSpawns;
    }
}
