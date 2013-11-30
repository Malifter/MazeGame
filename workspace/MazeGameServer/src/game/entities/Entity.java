package game.entities;

import java.util.ArrayList;
import java.util.UUID;

import engine.Vertex2f;
import engine.physics.RigidBody;
import engine.serializable.SerializedEntity;
import engine.serializable.SerializedObject;

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
    protected String image;
    protected final String uuid;
    protected RigidBody rBody;
    protected boolean enabled = false;
    
    public Entity(String img, RigidBody rb) {
        rBody = rb;
        image = img;
        uuid = UUID.randomUUID().toString();
        enable();
    }
    
    public RigidBody getRigidBody() {
        return rBody;
    }
    
    public void setRigidBody(RigidBody rb) {
        rBody = rb;
    }
    
    public void update(long time) {
        
    }
    
    public void update(ArrayList<Boolean> inputs, long time) {
        
    }
    
    public SerializedObject serialize() {
        return new SerializedEntity(uuid, image, new Vertex2f(rBody.getLocation()), !isEnabled());
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
}
