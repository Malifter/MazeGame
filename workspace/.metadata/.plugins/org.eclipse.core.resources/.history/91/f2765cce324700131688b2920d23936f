package engine;

import java.io.Serializable;
import java.util.ArrayList;


public class SerializedObject implements Serializable{
	private static final long serialVersionUID = -4988010332278134153L;
	private String image;
    private String uniqueID; // or class ID
    private boolean delete = false;
    private Position<Float, Float> position;

    public SerializedObject(String uniqueID, String image, Position<Float, Float> position, boolean delete) {
    	super();
    	this.uniqueID = uniqueID;
    	this.image = image;
    	this.position = position;
    	this.delete = delete;
    }
    
    public SerializedObject(String uniqueID, String image, float x, float y, ArrayList<Integer> sound, boolean delete) {
    	super();
    	this.uniqueID = uniqueID;
    	this.image = image;
    	this.position = new Position<Float, Float>(x, y);
    	this.delete = delete;
    }
    
    public boolean equals(Object other) {
    	if (other instanceof SerializedObject) {
    	    SerializedObject otherPair = (SerializedObject) other;
    		return 
    		((  this.uniqueID == otherPair.uniqueID ||
    			( this.uniqueID != null && otherPair.uniqueID != null &&
    			  this.uniqueID.equals(otherPair.uniqueID))) );
    	}

    	return false;
    }

    public String getID() {
    	return uniqueID;
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
}
