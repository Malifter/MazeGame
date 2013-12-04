package game.entities.environment;

import engine.Vector2f;
import engine.physics.RigidBody;
import game.entities.Entity;
import game.entities.EntityFactory;
import game.entities.npcs.Player;
import game.enums.ItemType;
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
    private boolean locked = true; // Implement later
    private ArrayList<ItemType> items =  new ArrayList<ItemType>(); // Implement lat
    private Interior room;

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Chest(RigidBody rb, Interior room) {
        super("items/chest/chest.gif", rb);
        generateContents();
        this.room = room;
        blocking = true;
        destructable = false;
        dangerous = false;
        openable = true;
    }
    
    public void generateContents() {
        double rand = Math.random();
        if(rand>0.25){items.add(ItemType.randomItem());}
        if(rand>0.5){items.add(ItemType.randomItem());}
        if(rand>0.75){items.add(ItemType.randomItem());}
    }
    
    public void interact(Player player){
        System.out.println("Interacted");
        //if(player.getInventory().hasItem(ItemType.DKEY)&&locked){
        if(locked){
            disable();
            unlock();
            dropContents();
        }
    }
    
    public boolean dropContents() {
        if(!locked) {
            for(ItemType i : items){
                room.addItem(EntityFactory.createItem(rBody.getLocation(), i));
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
    }
    
    public void unlock() {
        locked = false;
    }
}
