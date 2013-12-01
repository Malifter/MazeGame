package engine.serializable;

import engine.Vector2i;

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
    private Vector2i position;
    private Integer index;

    public SerializedRoom(Vector2i position, int index) {
        super(null);
        this.position = position;
        this.index = index;
    }

    public Vector2i getPosition() {
        return position;
    }
    
    public void setPosition(Vector2i position) {
        this.position = position;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
}
