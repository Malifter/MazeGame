package engine.serializable;

import engine.Vector2f;
import game.enums.AnimationPath;
import game.enums.AnimationState;
import game.enums.Face;

/*
* Classname:            SerializedEntity.java
*
* Version information:  1.0
*
* Date:                 11/6/2013
*
* Copyright notice:     Copyright (c) 2013 Garrett Benoit
*/

public class SerializedEntity extends SerializedObject {
    private static final long serialVersionUID = -3253907327685796548L;
    private AnimationPath animPath;
    private AnimationState animState;
    private Face face;
    private boolean delete = false;
    private Vector2f position;
    private int animSpeed = -1;

    public SerializedEntity(String uniqueID, int animSpeed, AnimationPath animPath, AnimationState animState, Face face, Vector2f position, boolean delete) {
        super(uniqueID);
        this.animPath = animPath;
        this.animState = animState;
        this.animSpeed = animSpeed;
        this.face = face;
        this.position = position;
        this.delete = delete;
    }
    
    public int getAnimSpeed() {
        return animSpeed;
    }
    
    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }

    public AnimationPath getAnimPath() {
        return animPath;
    }
    
    public void setAnimPath(AnimationPath animPath) {
        this.animPath = animPath;
    }
    
    public AnimationState getAnimState() {
        return animState;
    }
    
    public void setAnimState(AnimationState animState) {
        this.animState = animState;
    }
    
    public Face getFace() {
        return face;
    }
    
    public void setFace(Face face) {
        this.face = face;
    }

    public Vector2f getPosition() {
        return position;
    }
    
    public void setPosition(Vector2f position) {
        this.position = position;
    }
    
    public boolean needsDelete() {
        return delete;
    }
}
