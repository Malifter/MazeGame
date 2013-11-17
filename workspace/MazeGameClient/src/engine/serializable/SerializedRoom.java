package engine.serializable;

import engine.Vertex2;

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
    private Vertex2 position;
    private Integer index;

    public SerializedRoom(Vertex2 position, int index) {
        super(null);
        this.position = position;
        this.index = index;
    }

    public Vertex2 getPosition() {
        return position;
    }
    
    public void setPosition(Vertex2 position) {
        this.position = position;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    /*
    public static SerializedRoom serialize(Interior room) {
        return new SerializedRoom(room.getCenter(), );
    }*/
}
