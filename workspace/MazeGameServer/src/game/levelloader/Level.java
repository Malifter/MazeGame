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

import java.util.ArrayList;

public class Level {
    
    private ArrayList<Interior> rooms = new ArrayList<Interior>();
    private Exterior exterior = null;
    private int numRooms = 0;
    public Level() {}
    
    public void update(long elapsedTime) {
        exterior.update(elapsedTime);
        for(Interior room: rooms) {
            room.update(elapsedTime);
        }
    }
    
    public void applyCollisions() {
        exterior.applyCollisions();
        for(Interior room: rooms) {
            room.applyCollisions();
        }
    }
    
    public void serialize() {
        exterior.serialize();
        for(Interior room: rooms) {
            room.serialize();
        }
    }
    
    public void addRoom(Interior room) {
        room.setRoomID(numRooms+1);
        rooms.add(room);
        numRooms++;
    }
    
    public ArrayList<Interior> getRooms() {
        return rooms;
    }
    
    public void setExterior(Exterior outer) {
        outer.setRoomID(0);
        exterior = outer;
    }
    
    public Exterior getExterior() {
        return exterior;
    }
}
