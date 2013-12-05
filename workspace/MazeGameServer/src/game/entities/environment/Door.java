package game.entities.environment;

import game.entities.npcs.Player;
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
    private boolean locked; // Implement later
    private boolean disguished = false;
    
    private static final String LOCKED_ARRAY[]={"animations/door/locked/up/door.gif", 
                                                "animations/door/locked/down/door.gif",
                                                "animations/door/locked/left/door.gif",
                                                "animations/door/locked/right/door.gif"};
    
    private static final String UNLOCKED_ARRAY[]={"animations/door/unlocked/up/door.gif", 
                                                "animations/door/unlocked/down/door.gif",
                                                "animations/door/unlocked/left/door.gif",
                                                "animations/door/unlocked/right/door.gif"};
    /**
     * Constructor
     * @param g
     * @param anImage
     * @param x
     * @param y
     */
    public Door(RigidBody rb, Vector2i exitLoc, Room room, Door linkedDoor, Side side) {
        super(LOCKED_ARRAY[0], rb);
        this.room = room;
        this.linkedDoor = linkedDoor;
        if(this.linkedDoor != null) {
            this.linkedDoor.setLink(this);
        }
        exitLocation = exitLoc;
        this.side = side;
        randomLock();
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
        if(side.equals(Side.TOP)) {
            image = LOCKED_ARRAY[1];
        } else if(side.equals(Side.LEFT)) {
            image = LOCKED_ARRAY[3];
        } else if(side.equals(Side.RIGHT)) {
            image = LOCKED_ARRAY[2];
        } else {
            image = LOCKED_ARRAY[0];
        }
        locked = true;
        rBody.enable();
        if(linkedDoor!=null&&!linkedDoor.isLocked()){
            this.linkedDoor.lock();
            this.linkedDoor.rBody.enable();
        }
    }
    
    public void unlock() {
        if(side.equals(Side.TOP)) {
            image = UNLOCKED_ARRAY[1];
        } else if(side.equals(Side.LEFT)) {
            image = UNLOCKED_ARRAY[3];
        } else if(side.equals(Side.RIGHT)) {
            image = UNLOCKED_ARRAY[2];
        } else {
            image = UNLOCKED_ARRAY[0];
        }
        locked = false;
        rBody.disable();
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
