package engine.serializable;

import java.util.List;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

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
    private List<Integer> itemCount; // map of items
    int selectedItem;
    int health;
    int lives;
    
    // TODO: Can simplify the serialization code by instead of passing in many values,
    // we can just pass in Player and then pass it to the super since it's an entity.
    // This also allows all the functions to be called internally. Might be something to consider.
    // TODO: NEVERMIND it suddenly hit me that the reason we don't use the base classes as direct input
    // is because we don't want to actually over-burden our client with classes it won't actually use.
    // The client doesn't need to ever access Player or Entity or many of those implemented classes. It
    // just gets the info it needs to render them from the Serialized objects.
    public SerializedPlayer(String uniqueID, int animSpeed, AnimationPath animPath, AnimationState animState,
            Face face, Vector2f position, List<Integer> itemCount, int selectedItem, int health, int lives) {
        super(uniqueID, animSpeed, animPath, animState, face, position, false);
        this.itemCount = itemCount;
        this.selectedItem = selectedItem;
        this.health = health;
        this.lives = lives;
    }
    
    public void setItems(List<Integer> itemCount){
        this.itemCount = itemCount;
    }
    
    public List<Integer> getItems(){
        return itemCount;
    }
    
    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
    
    public int getSelectedItem() {
        return selectedItem;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }
    
    public int getLives() {
        return lives;
    }
}
