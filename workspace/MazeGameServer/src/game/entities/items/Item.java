package game.entities.items;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

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
    
    public Item(AnimationPath ap, RigidBody rb) {
        super(ap, rb);
    }
    
    public abstract void pickUp(Player p);
    
    public void update(long elapsedTime) {
        // do nothing
    }
}
    
    
    



