package game.entities.items;

import engine.physics.RigidBody;
import game.entities.Entity;

/*
* Classname:            Item.java
*
* Version information:  1.0
*
* Date:                 11/17/2013
*
* Copyright notice:     Copyright (c) 2013 Lizhu Ma
*/

public abstract class Item extends Entity {
    //public enum ItemType {HEALTHBOOSTER, WEAPON, KEY};
    //public ItemType type;

    public Item(String anImage, RigidBody rb) {
        super(anImage, rb);
    }
}
    
    
    



