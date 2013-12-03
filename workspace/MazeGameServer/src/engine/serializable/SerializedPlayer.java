package engine.serializable;

import java.util.List;

import engine.Vector2f;
import game.enums.ItemType;

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
    private List <ItemType> items; // map of items
    // int health
    // int lives
    public SerializedPlayer(String uniqueID, String image, Vector2f position, List<ItemType> items) {
        super(uniqueID);
        this.image = image;
        this.position = position;
        this.items = items;
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
    
    public void setItems(List<ItemType> items){
        this.items = items;
    }
    
    public List<ItemType> getItems(){
        return items;
    }
}
