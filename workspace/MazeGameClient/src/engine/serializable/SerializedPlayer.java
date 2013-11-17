package engine.serializable;

import engine.Vertex2;
import engine.Vertex2f;

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
    private Vertex2f position;

    public SerializedPlayer(String uniqueID, String image, Vertex2f position) {
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

    public Vertex2f getPosition() {
        return position;
    }
    
    public void setPosition(Vertex2f position) {
        this.position = position;
    }
    
    /*
    public static SerializedPlayer serialize(Player player) {
        return new SerializedPlayer(player.getUUID(), player.getImage(), player.getPosition());
    }
    */
}
