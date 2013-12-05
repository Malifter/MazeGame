package game.entities;

import java.util.UUID;
import engine.Vector2f;
import engine.physics.RigidBody;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

/*
* Classname:            Entity.java
*
* Version information:  1.0
*
* Date:                 10/30/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

/**
 * IDrawable: Interface for entity objects.
 */
public abstract class Entity {
    protected final String uuid;
    protected RigidBody rBody;
    protected boolean enabled = false;
    protected final AnimationPath animPath;
    protected AnimationState animState = AnimationState.IDLE;
    protected Face facing = Face.NONE;
    
    public Entity(AnimationPath ap, RigidBody rb) {
        animPath = ap;
        rBody = rb;
        uuid = UUID.randomUUID().toString();
        enable();
    }
    
    public String getUUID() {
        return uuid;
    }
    
    public RigidBody getRigidBody() {
        return rBody;
    }
    
    public void setRigidBody(RigidBody rb) {
        rBody = rb;
    }
    
    public abstract void update(long elapsedTime);
    
    public SerializedObject serialize() {
        return new SerializedEntity(uuid, animPath, animState, facing, new Vector2f(rBody.getLocation()), !isEnabled());
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void disable() {
        enabled = false;
        rBody.disable();
    }
    
    public void enable() {
        enabled = true;
        rBody.enable();
    }
    
    public AnimationPath getAnimationPath() {
        return animPath;
    }
    
    public Face getFaceDirection() {
        return facing;
    }
    
    public AnimationState getAnimationState() {
        return animState;
    }
}
