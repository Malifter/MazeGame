package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.Entity;
import game.enums.ItemType;

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
public class Chest extends Entity {
    private boolean locked = false; // Implement later
    private ArrayList<ItemType> items; // Implement later

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Chest(String img, RigidBody rb) {
        super(img, rb);
        generateContents();
    }
    
    public void generateContents() {
        // randomly generate an item (currently item class doesn't exist)
        // will probably have a list that all chests can choose from
        // then it just randomly selects items from the list
        // we'll make this a list of ENUM's rather than Items because
        // there is no point in instancing a class for items inside
        // the chests. It's a waste of memory.
        
        // this will also most likely happen when the chest is constructed rather than when unlocked/opened
        items = new ArrayList<ItemType>();
        
        // for now for a test random stuff
        items.add(ItemType.DKEY);
    }
    
    public boolean dropContents() {
        if(!locked) {
        // create the items that this chest generated
        // create an abstract factory that generates items
        // perhaps this should be taken care of outside
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
