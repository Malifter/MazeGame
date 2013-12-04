package game.entities.environment;

import game.entities.npcs.Player;
import game.enums.ItemType;
import game.enums.Side;
import game.environment.Room;
import engine.physics.RigidBody;

/*
* Classname:            CellDoor.java
*
* Version information:  1.0
*
* Date:                 12/1/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * EnvironmentTile: Level background tile
 */
public class CellDoor extends Obstacle {
    private boolean locked = false; // Implement later

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public CellDoor(String img, RigidBody rb) {
        super(img, rb);
        lock();
        dangerous = false;
    }

    @Override
    public void interact(Player player) {
        if(player.getInventory().hasItem(ItemType.CKEY)) {
            unlock();
        }
    }
    
    public void lock() {
        locked = true;
        blocking = true;
        destructable = true;
        openable = true;
        rBody.enable();
    }
    
    public void unlock() {
        locked = false;
        blocking = false;
        destructable = false;
        openable = false;
        rBody.disable();
    }
    
    public boolean isLocked() {
        return locked;
    }
}
