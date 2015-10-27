package game.entities.environment;

import engine.Vector2f;
import engine.physics.RigidBody;
import engine.serializable.SerializedObject;
import engine.serializable.SerializedObstacle;
import game.entities.Entity;
import game.entities.npcs.Hostile;
import game.entities.npcs.Player;
import game.enums.AnimationPath;

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
public abstract class Obstacle extends Entity {
    public static final int COLLISION_DAMAGE = 10;
    public static final int DRAIN_DAMAGE = 1;
    protected boolean destructable = false; // do bombs destroy it
    protected boolean dangerous = false; // does it hurt non flyers
    protected boolean blocking = false; // blocks projectile or movement
    protected boolean openable = false; // does it contain items
    protected boolean moveable = false; // can be pushed around
    protected boolean heavy = false; // determine if slows pusher
    
    public Obstacle(AnimationPath ap, RigidBody rb) {
        super(ap, rb);
    }
    
    public void update(long elapsedTime) {
        // do nothing
    }
    
    public void collide(Hostile hostile) {
        if(hostile instanceof Player) {
            hostile.takeDamage(this, COLLISION_DAMAGE);
        } else {
            hostile.takeDamage(this, DRAIN_DAMAGE);
        }
    }
    
    public void interact(Player player) {
        
    }
    
    public void destroy() {
        if(destructable) {
            if(this instanceof ActiveBomb) {
                ((ActiveBomb) this).explode();
            }
            disable();
        }
    }
    
    public boolean isOpenable() {
        return openable;
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
    
    public boolean isMoveable() {
        return moveable;
    }
    
    public boolean isHeavy() {
        return heavy;
    }
    
    @Override
    public SerializedObject serialize() {
        return new SerializedObstacle(uuid, 175, animPath, animState, facing, new Vector2f(rBody.getLocation()), !isEnabled(), moveable);
    }
}