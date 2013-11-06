package engine.serializable;

import engine.Position;

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
    private Position<Float, Float> position;

    public SerializedPlayer(String uniqueID, String image, Position<Float, Float> position) {
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

    public Position<Float, Float> getPosition() {
        return position;
    }
    
    public void setPosition(Position<Float, Float> position) {
        this.position = position;
    }
    
    /*
    public static SerializedPlayer serialize(Player player) {
        return new SerializedPlayer(player.getUUID(), player.getImage(), player.getPosition());
    }
    */
}
