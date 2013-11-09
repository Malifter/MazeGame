package engine.serializable;

import engine.Position;

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
    private Position<Float, Float> position;

    public SerializedEntity(String uniqueID, String image, Position<Float, Float> position, boolean delete) {
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

    public Position<Float, Float> getPosition() {
        return position;
    }
    
    public void setPosition(Position<Float, Float> position) {
        this.position = position;
    }
    
    public boolean needsDelete() {
        return delete;
    }
    /*
    public static SerializedEntity serialize(Entity entity) {
        return new SerializedEntity(entity.getUUID(), entity.getImage(), entity.getPosition(), entity.isEnabled());
    }
    */
}