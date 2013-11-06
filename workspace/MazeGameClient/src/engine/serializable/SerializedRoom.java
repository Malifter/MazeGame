package engine.serializable;

import engine.Position;
import game.Room;

/*
* Classname:            SerializedRoom.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class SerializedRoom extends SerializedObject {
    private static final long serialVersionUID = 6807826454185661545L;
    private Position<Integer, Integer> position;

    public SerializedRoom(Position<Integer, Integer> position) {
        super(null);
        this.position = position;
    }

    public Position<Integer, Integer> getPosition() {
        return position;
    }
    
    public void setPosition(Position<Integer, Integer> position) {
        this.position = position;
    }
    
    public static SerializedRoom serialize(Room room) {
        return new SerializedRoom(room.getCenter());
    }
}
