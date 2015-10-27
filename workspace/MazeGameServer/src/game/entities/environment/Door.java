package game.entities.environment;

import game.entities.npcs.Player;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.ItemType;
import game.enums.Side;
import game.environment.Room;
import engine.Vector2i;
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
    private Vector2i exitLocation;
    private boolean locked = false;
    private boolean disguised = false;
    

    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Door(RigidBody rb, Vector2i exitLoc, Room room, Door linkedDoor, Side side) {
        super(AnimationPath.DOOR, rb, room, side);
        this.linkedDoor = linkedDoor;
        if(this.linkedDoor != null) {
            this.linkedDoor.setLink(this);
        }
        exitLocation = exitLoc;
        randomLock();
    }
    
    // TODO: Bad function name maybe call collide instead
    @Override
    public boolean interact(Player player) {
        // TODO: quick fix, but make sure priority here is correct.
        // We might want both to be true, or just one or the other
        if(disguised) {
            reveal();
            player.takeDamage(this, 10);
        } else if(locked && player.getInventory().hasItem(ItemType.DKEY)) {
            unlock();
            player.getInventory().removeItem(ItemType.DKEY);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean transport(Player player) {
        if(!locked && contains(player)) {
            player.getRigidBody().setLocation(linkedDoor.getExit().x, linkedDoor.getExit().y);
            linkedDoor.getRoom().addPlayer(player);
            return true;
        }
        else return false;
    }
    
    private void randomLock(){
        if(Math.random()>0.90){
            lock();
        }
        else{
            unlock();
        }
    }
    
    public Vector2i getExit() {
        return exitLocation;
    }
    
    public Door getLink() {
        return linkedDoor;
    }
    
    public void setLink(Door linkedDoor) {
        this.linkedDoor = linkedDoor;
        this.linkedDoor.linkedDoor = this;
    }
    
    // TODO: I walked through a door that was unlocked on one side but locked on the other. I don't believe this is
    // intentional so it needs to be fixed. (some how both of my keys also were consumed in this process
    // After I left the room through another door and came back, my key (I got in a dif room) was consumed and unlocked the door.
    // This functionality is totally wrong
    public void lock() {
        locked = true;
        rBody.enable();
        animState = AnimationState.IDLE;
        if(linkedDoor != null && !linkedDoor.locked){
            linkedDoor.lock();
        }
    }
    
    public void unlock() {
        locked = false;
        rBody.disable();
        animState = AnimationState.ACTIVE;
        if(linkedDoor!=null && linkedDoor.locked){
            linkedDoor.unlock();
        }
    }
    
    public boolean isActive() {
        return !locked && !disguised;
    }
    
    // TODO: Fix the disguised door so that it actually is disguised.
    // We will need to generate probably a custom serialized entity so that it can be disguised properly.
    // It needs to match the walls of the room it is in, should mostly be easy though to do.
    
    public void disguise() {
        disguised = true;
        rBody.enable();
        animState = AnimationState.ATTACK;
        if(linkedDoor != null && !linkedDoor.disguised){//making sure both sides are disguised or neither side is disguised
            linkedDoor.disguise();
        }
    }
    
    public void reveal() {
        this.disguised = false;
        rBody.disable();
        animState = AnimationState.ACTIVE;
        if(linkedDoor != null && linkedDoor.disguised){//making sure both sides are disguised or neither side is disguised
            linkedDoor.reveal();
        }
    }
}
