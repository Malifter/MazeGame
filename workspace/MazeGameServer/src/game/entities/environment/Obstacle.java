package game.entities.environment;

import engine.physics.RigidBody;
import game.entities.Entity;

/*
* Classname:            Obstacle.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * Obstacle: can consist of damaging traps (i.e. spikes) or destructable rocks or objects in the way in general
 */
public class Obstacle extends Entity {
    public static final int COLLISION_DAMAGE = 10;
    protected boolean destructable = false;
    protected boolean dangerous = false;
    protected boolean blocking = false;
    
    public Obstacle(String img, RigidBody rb) {
        super(img, rb);
    }
    
    public boolean isDestructable() {
        return destructable;
    }
    
    public boolean isDangerous() {
        return dangerous;
    }
    
    public boolean isBlocking() {
        return blocking;
    }
}