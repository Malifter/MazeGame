package game.levelloader;

/*
* Classname:            Level.java
*
* Version information:  1.0
*
* Date:                 11/15/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

import game.environment.Exterior;
import game.environment.Interior;
import game.environment.Room;

import java.util.ArrayList;

public class Level {
    
    private ArrayList<Interior> rooms = new ArrayList<Interior>();
    private Exterior exterior = null;
    
    public Level() {   
    }
    
    public void addRoom(Interior room) {
        rooms.add(room);
    }
    
    public ArrayList<Interior> getRooms() {
        return rooms;
    }
    
    public void setExterior(Exterior outer) {
        exterior = outer;
    }
    
    public Exterior getExterior() {
        return exterior;
    }
}
