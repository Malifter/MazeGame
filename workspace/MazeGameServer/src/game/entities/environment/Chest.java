package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.EntityFactory;
import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.ItemType;
import game.enums.ObstacleType;
import game.environment.Interior;

import java.util.ArrayList;

/*
* Classname:            Chest.java
*
* Version information:  1.0
*
* Date:                 11/8/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * EnvironmentTile: Level background tile
 */
public class Chest extends Obstacle {
    // TODO: Add min interaction time after it opens
    private boolean locked = true;
    private ArrayList<ItemType> items =  new ArrayList<ItemType>();
    private Interior room;

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Chest(RigidBody rb, Interior room) {
        super(AnimationPath.CHEST, rb);
        this.room = room;
        openable = true;
        moveable = true;
        blocking = true;
        heavy = true;
        lock();
        generateContents();
    }
    
    public void generateContents() {
        double rand = Math.random();
        if(rand <= 0.05) items.add(ItemType.randomItem());
        if(rand <= 0.3) items.add(ItemType.randomItem());
        if(rand <= 0.90) items.add(ItemType.randomItem());
        else items.add(ItemType.ACTIVE_BOMB);
    }
    
    public void interact(Player player){
        if(locked && player.getInventory().hasItem(ItemType.DKEY)) {
            unlock();
            dropContents();
            player.getInventory().removeItem(ItemType.DKEY);
        }
    }
    
    public boolean dropContents() {
        if(!locked) {
            for(ItemType item : items) {
                if(item == ItemType.ACTIVE_BOMB) {
                    // TODO: THIs is the concurrent modification issue. The fact is that the chest dropped an active bomb
                    // which is considered an obsacle and therefore we added an obstacle while iterating over the obstacle array
                    room.addObstacle(EntityFactory.createObstacle(rBody.getLocation(), ObstacleType.ACTIVE_BOMB, room));
                } else {
                    room.addItem(EntityFactory.createItem(rBody.getLocation(), item));
                }
            }
            return true;
        }
        return false;
    }
    
    public ArrayList<ItemType> getContents() {
        return items;
    }
    
    public void lock() {
        locked = true;
        animState = AnimationState.IDLE;
    }
    
    public void unlock() {
        locked = false;
        animState = AnimationState.ACTIVE;
    }
}
