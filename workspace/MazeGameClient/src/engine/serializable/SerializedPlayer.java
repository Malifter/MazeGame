package engine.serializable;

import java.util.HashMap;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;
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

public class SerializedPlayer extends SerializedEntity {
    private static final long serialVersionUID = 2223980202920884570L;
    private HashMap<ItemType, Integer> items; // map of items
    // int health
    // int lives
    
    public SerializedPlayer(String uniqueID, int animSpeed, AnimationPath animPath, AnimationState animState, Face face, Vector2f position, HashMap<ItemType, Integer> items) {
        super(uniqueID, animSpeed, animPath, animState, face, position, false);
        this.items = items;
    }
    
    public void setItems(HashMap<ItemType,Integer> items){
        this.items = items;
    }
    
    public HashMap<ItemType,Integer> getItems(){
        return items;
    }
}
