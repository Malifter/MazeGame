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
    private boolean disguished = false;
    

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
    
    @Override
    public boolean interact(Player player) {
        if(locked && player.getInventory().hasItem(ItemType.DKEY)) {
            unlock();
            player.getInventory().removeItem(ItemType.DKEY);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean transport(Player player) {
        if(!isLocked() && contains(player)) {
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
    
    public void lock() {
        locked = true;
        rBody.enable();
        animState = AnimationState.IDLE;
        if(linkedDoor!=null&&!linkedDoor.isLocked()){
            this.linkedDoor.lock();
            this.linkedDoor.rBody.enable();
        }
    }
    
    public void unlock() {
        locked = false;
        rBody.disable();
        animState = AnimationState.ACTIVE;
        if(linkedDoor!=null&&linkedDoor.isLocked()){
            this.linkedDoor.unlock();
            this.linkedDoor.rBody.disable();
        }
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public boolean isDisguished(){
        return disguished;
    }
    
    public void setDisguished(boolean disguished){
        if(linkedDoor.isDisguished()!=disguished){//making sure both sides are disguished or neither side is disguished
            this.disguished = disguished;
        }
    }
}
