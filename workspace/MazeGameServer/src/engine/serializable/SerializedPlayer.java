package engine.serializable;

import engine.Vector2f;

/*
* Classname:            SerializedPlayer.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class SerializedPlayer extends SerializedObject {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 2223980202920884570L;
    private String image;
    private Vector2f position;

    public SerializedPlayer(String uniqueID, String image, Vector2f position) {
        super(uniqueID);
        this.image = image;
        this.position = position;
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
    }

    public Vector2f getPosition() {
        return position;
    }
    
    public void setPosition(Vector2f position) {
        this.position = position;
    }
}
