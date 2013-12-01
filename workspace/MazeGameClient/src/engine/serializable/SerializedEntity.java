package engine.serializable;

import engine.Vector2f;

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
    private String image;
    private boolean delete = false;
    private Vector2f position;

    public SerializedEntity(String uniqueID, String image, Vector2f position, boolean delete) {
        super(uniqueID);
        this.image = image;
        this.position = position;
        this.delete = delete;
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
        this.image = image;
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
