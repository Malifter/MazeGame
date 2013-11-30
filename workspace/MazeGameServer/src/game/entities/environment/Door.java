package game.entities.environment;

import game.entities.npcs.Player;
import game.enums.Side;
import game.environment.Room;
import engine.Vertex2;
import engine.physics.RigidBody;

/*
* Classname:            Door.java
*
* Version information:  1.0
*
* Date:                 11/3/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * EnvironmentTile: Level background tile
 */
public class Door extends Entry {
    private Door linkedDoor;
    private Vertex2 exitLocation;
    private boolean locked = false; // Implement later
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Door(String img, RigidBody rb, Vertex2 exitLoc, Room room, Door linkedDoor, Side side, boolean locked) {
        super(img, rb);
        this.room = room;
        this.linkedDoor = linkedDoor;
        if(this.linkedDoor != null) {
            this.linkedDoor.setLink(this);
        }
        exitLocation = exitLoc;
        this.side = side;
        if(locked) lock();
        else unlock();
    }
    
    @Override
    public boolean transport(Player player) {
        if(!isLocked() && contains(player)) {
            player.getRigidBody().setLocation(linkedDoor.getExit().x, linkedDoor.getExit().y);
            room.removePlayer(player); // This may cause problems.
            linkedDoor.getRoom().addPlayer(player);
            return true;
        }
        else return false;
    }
    
    public Vertex2 getExit() {
        return exitLocation;
    }
    
    public Door getLink() {
        return linkedDoor;
    }
    
    public void setLink(Door linkedDoor) {
        this.linkedDoor = linkedDoor;
        this.linkedDoor.linkedDoor = this;
    }
    
    public void lock() {
        locked = true;
        rBody.enable();
    }
    
    public void unlock() {
        locked = false;
        rBody.disable();
    }
    
    public boolean isLocked() {
        return locked;
    }
}
